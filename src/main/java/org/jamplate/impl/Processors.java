/*
 *	Copyright 2021 Cufy
 *
 *	Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *	You may obtain a copy of the License at
 *
 *	    http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	See the License for the specific language governing permissions and
 *	limitations under the License.
 */
package org.jamplate.impl;

import org.jamplate.impl.util.Trees;
import org.jamplate.model.*;
import org.jamplate.model.function.Processor;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * The post-parse processors.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.20
 */
public final class Processors {
	//CX CMD

	/**
	 * A processor that processes commands.
	 *
	 * @since 0.2.0 ~2021.05.30
	 */
	@NotNull
	public static final Processor CX_CMD = compilation -> {
		boolean[] modified = {false};
		Trees.collect(compilation.getRootTree())
			 .stream()
			 .filter(tree -> tree.getSketch().getKind().equals(Kind.CX_CMD))
			 .forEach(tree -> {
				 Tree child = tree.getChild();

				 if (child == null || child.getNext() != null)
					 throw new CompileException(
							 "Unrecognized command",
							 tree
					 );

				 tree.pop();
				 modified[0] = true;
			 });
		return modified[0];
	};

	/**
	 * Since the declare command can take a pointer in the address given to it, this
	 * processor will take the first tree in the parameter, if it is a square and if it
	 * was exactly next to the address, then change it into a pointer and shrink the
	 * address.
	 *
	 * @since 0.2.0 ~2021.06.01
	 */
	@SuppressWarnings("OverlyLongLambda")
	@NotNull
	public static final Processor CX_CMD_DECLARE = compilation -> {
		Objects.requireNonNull(compilation, "compilation");
		boolean[] modified = {false};
		Trees.collect(compilation.getRootTree())
			 .parallelStream()
			 .filter(tree -> tree.getSketch().getKind().equals(Kind.CX_CMD_DECLARE))
			 .filter(tree -> tree.getSketch().get(Component.ACCESSOR).getTree() == null)
			 .forEach(tree -> {
				 Tree key = tree.getSketch().get(Component.KEY).getTree();
				 Tree oldParam = tree.getSketch().get(Component.PARAMETER).getTree();
				 Tree firstSqr = oldParam.getChild();
				 Tree lastSqr = firstSqr;

				 if (Intersection.compute(key, firstSqr) != Intersection.NEXT)
					 //chain lost at the beginning, no processing
					 return;

				 while (true) {
					 Tree nextTree = lastSqr.getNext();

					 if (nextTree == null)
						 //reached the end, stop
						 break;

					 if (!nextTree.getSketch().getKind().equals(Kind.SX_SQR)) {
						 if (Intersection.compute(lastSqr, nextTree) !=
							 Intersection.AFTER)
							 //chain interrupted, no processing
							 return;

						 //chain ended, lastSqr is the last []
						 break;
					 }

					 if (Intersection.compute(lastSqr, nextTree) != Intersection.NEXT)
						 //chain lost, lastSqr is the last chained []
						 break;

					 lastSqr = nextTree;
				 }

				 //bingo
				 int oldParamPos = oldParam.reference().position();
				 int oldParamLen = oldParam.reference().length();

				 int pointPos = firstSqr.reference().position();
				 int pointLen = lastSqr.reference().position() +
								lastSqr.reference().length() -
								pointPos;

				 int newParamPos = pointPos + pointLen;
				 int newParamLength = oldParamLen - pointLen - (pointPos - oldParamPos);

				 oldParam.pop();
				 tree.offer(new Tree(
						 tree.document(),
						 new Reference(
								 newParamPos,
								 newParamLength
						 ),
						 tree.getSketch()
							 .replace(Component.PARAMETER)
							 .setKind(Kind.CX_PRM),
						 -100
				 ));

				 tree.offer(new Tree(
						 tree.document(),
						 new Reference(
								 pointPos,
								 pointLen
						 ),
						 tree.getSketch()
							 .get(Component.ACCESSOR)
							 .setKind(Kind.CX_PRM),
						 -100
				 ));

				 modified[0] = true;
			 });
		return modified[0];
	};

	//CX FLW

	/**
	 * A processor that parses capture context.
	 *
	 * @since 0.2.0 ~2021.06.01
	 */
	@SuppressWarnings("OverlyLongLambda")
	@NotNull
	public static final Processor CX_FLW_CAPTURE = compilation -> {
		boolean[] modified = {false};
		Trees.collect(compilation.getRootTree())
			 .stream()
			 .filter(tree -> {
				 if (!tree.getSketch().getKind().equals(Kind.CX_FLW_CAPTURE))
					 for (Tree t : tree)
						 switch (t.getSketch().getKind()) {
							 case Kind.CX_CMD_CAPTURE:
							 case Kind.CX_CMD_ENDCAPTURE:
								 return true;
						 }

				 return false;
			 })
			 .forEach(tree -> {
				 Tree captureTree = null;

				 for (Tree t : tree)
					 switch (t.getSketch().getKind()) {
						 case Kind.CX_CMD_CAPTURE:
							 captureTree = t;
							 break;
						 case Kind.CX_CMD_ENDCAPTURE:
							 //bingo
							 if (captureTree == null)
								 throw new CompileException(
										 "Endcapture command outside capture context"
								 );

							 int position = captureTree.reference().position();
							 int length = t.reference().position() +
										  t.reference().length() -
										  position;

							 tree.offer(new Tree(
									 tree.document(),
									 new Reference(position, length),
									 new Sketch(Kind.CX_FLW_CAPTURE)
							 ));
							 modified[0] = true;
							 return;
					 }

				 throw new CompileException(
						 "Unclosed capture context",
						 captureTree
				 );
			 });
		return modified[0];
	};

	/**
	 * A processor that parses for context.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@SuppressWarnings("OverlyLongLambda")
	@NotNull
	public static final Processor CX_FLW_FOR = compilation -> {
		boolean[] modified = {false};
		Trees.collect(compilation.getRootTree())
			 .stream()
			 .filter(tree -> {
				 if (!tree.getSketch().getKind().equals(Kind.CX_FLW_FOR))
					 for (Tree t : tree)
						 switch (t.getSketch().getKind()) {
							 case Kind.CX_CMD_FOR:
							 case Kind.CX_CMD_ENDFOR:
								 return true;
						 }

				 return false;
			 })
			 .forEach(tree -> {
				 Tree forTree = null;

				 for (Tree t : tree)
					 switch (t.getSketch().getKind()) {
						 case Kind.CX_CMD_FOR:
							 forTree = t;
							 break;
						 case Kind.CX_CMD_ENDFOR:
							 //bingo
							 if (forTree == null)
								 throw new CompileException(
										 "Endfor command outside for context"
								 );

							 int position = forTree.reference().position();
							 int length = t.reference().position() +
										  t.reference().length() -
										  position;

							 tree.offer(new Tree(
									 tree.document(),
									 new Reference(position, length),
									 new Sketch(Kind.CX_FLW_FOR)
							 ));
							 modified[0] = true;
							 return;
					 }

				 throw new CompileException(
						 "Unclosed for context",
						 forTree
				 );
			 });
		return modified[0];
	};

	/**
	 * A processor that parses if context.
	 *
	 * @since 0.2.0 ~2021.05.24
	 */
	@SuppressWarnings("OverlyLongLambda")
	@NotNull
	public static final Processor CX_FLW_IF = compilation -> {
		boolean[] modified = {false};
		Trees.collect(compilation.getRootTree())
			 .stream()
			 .filter(tree -> {
				 if (!tree.getSketch().getKind().equals(Kind.CX_FLW_IF))
					 for (Tree t : tree)
						 switch (t.getSketch().getKind()) {
							 case Kind.CX_CMD_IF:
							 case Kind.CX_CMD_IFDEF:
							 case Kind.CX_CMD_IFNDEF:
							 case Kind.CX_CMD_ELIF:
							 case Kind.CX_CMD_ELIFDEF:
							 case Kind.CX_CMD_ELIFNDEF:
							 case Kind.CX_CMD_ELSE:
							 case Kind.CX_CMD_ENDIF:
								 return true;
						 }

				 return false;
			 })
			 .forEach(tree -> {
				 Tree ifTree = null;
				 Tree elseTree = null;

				 for (Tree t : tree)
					 switch (t.getSketch().getKind()) {
						 case Kind.CX_CMD_IF:
						 case Kind.CX_CMD_IFDEF:
						 case Kind.CX_CMD_IFNDEF:
							 ifTree = t;
							 elseTree = null;
							 break;
						 case Kind.CX_CMD_ELIF:
						 case Kind.CX_CMD_ELIFDEF:
						 case Kind.CX_CMD_ELIFNDEF:
							 if (ifTree == null)
								 throw new CompileException(
										 "Elif command outside if context",
										 t
								 );
							 if (elseTree != null)
								 throw new CompileException(
										 "Elif command after else command",
										 t
								 );

							 break;
						 case Kind.CX_CMD_ELSE:
							 if (ifTree == null)
								 throw new CompileException(
										 "Else command outside if context"
								 );
							 if (elseTree != null)
								 throw new CompileException(
										 "Double Else commands in one if context",
										 t
								 );
							 elseTree = t;
							 break;
						 case Kind.CX_CMD_ENDIF:
							 //bingo
							 if (ifTree == null)
								 throw new CompileException(
										 "Endif command outside if context"
								 );

							 int position = ifTree.reference().position();
							 int length = t.reference().position() +
										  t.reference().length() -
										  position;

							 tree.offer(new Tree(
									 tree.document(),
									 new Reference(position, length),
									 new Sketch(Kind.CX_FLW_IF)
							 ));
							 modified[0] = true;
							 return;
					 }

				 throw new CompileException(
						 "Unclosed if context",
						 ifTree
				 );
			 });
		return modified[0];
	};

	/**
	 * Utility classes must not be initialized.
	 *
	 * @throws AssertionError when called.
	 * @since 0.2.0 ~2021.05.20
	 */
	private Processors() {
		throw new AssertionError("No instance for you");
	}
}
