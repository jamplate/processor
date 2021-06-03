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

import java.util.Iterator;
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
	 * A processor that parses while context.
	 *
	 * @since 0.2.0 ~2021.06.03
	 */
	@SuppressWarnings("OverlyLongLambda")
	@NotNull
	public static final Processor CX_FLW_WHILE = compilation -> {
		boolean[] modified = {false};
		Trees.collect(compilation.getRootTree())
			 .stream()
			 .filter(tree -> {
				 if (!tree.getSketch().getKind().equals(Kind.CX_FLW_WHILE))
					 for (Tree t : tree)
						 switch (t.getSketch().getKind()) {
							 case Kind.CX_CMD_WHILE:
							 case Kind.CX_CMD_ENDWHILE:
								 return true;
						 }

				 return false;
			 })
			 .forEach(tree -> {
				 Tree whileTree = null;

				 for (Tree t : tree)
					 switch (t.getSketch().getKind()) {
						 case Kind.CX_CMD_WHILE:
							 whileTree = t;
							 break;
						 case Kind.CX_CMD_ENDWHILE:
							 //bingo
							 if (whileTree == null)
								 throw new CompileException(
										 "Endwhile command outside while context"
								 );

							 int position = whileTree.reference().position();
							 int length = t.reference().position() +
										  t.reference().length() -
										  position;

							 tree.offer(new Tree(
									 tree.document(),
									 new Reference(position, length),
									 new Sketch(Kind.CX_FLW_WHILE)
							 ));
							 modified[0] = true;
							 return;
					 }

				 throw new CompileException(
						 "Unclosed while context",
						 whileTree
				 );
			 });
		return modified[0];
	};

	//SX

	/**
	 * A processor that solves logic trees.
	 *
	 * @since 0.2.0 ~2021.06.02
	 */
	@SuppressWarnings("OverlyLongLambda")
	@NotNull
	public static final Processor CX_PCM_LGC = compilation -> {
		boolean[] modified = {false};
		Trees.collect(compilation.getRootTree())
			 .stream()
			 .filter(tree -> {
				 if (!tree.getSketch().getKind().equals(Kind.CX_PCM_LGC))
					 for (Tree t : tree)
						 switch (t.getSketch().getKind()) {
							 case Kind.OP_ADD:
							 case Kind.OP_DIV:
							 case Kind.OP_EQL:
							 case Kind.OP_LEQ:
							 case Kind.OP_LND:
							 case Kind.OP_LOR:
							 case Kind.OP_LTN:
							 case Kind.OP_MEQ:
							 case Kind.OP_MOD:
							 case Kind.OP_MTN:
							 case Kind.OP_MUL:
							 case Kind.OP_NEG:
							 case Kind.OP_NQL:
							 case Kind.OP_SUB:
								 return true;
						 }

				 return false;
			 })
			 .forEach(tree -> {
				 Tree logicTree = null;
				 Tree openTree = null;
				 Tree closeTree = null;
				 int mode = -1;

				 for0:
				 for (Tree t : tree)
					 //first to wrap -> first to evaluate
					 switch (t.getSketch().getKind()) {
						 case Kind.OP_NEG: // '!'
							 if (mode < 0) {
								 logicTree = t;
								 mode = 0;
							 }
							 break;
						 case Kind.OP_MUL: // '*'
						 case Kind.OP_DIV: // '/'
						 case Kind.OP_MOD: // '%'
							 if (mode < 1) {
								 logicTree = t;
								 mode = 1;
							 }
							 break;
						 case Kind.OP_ADD: // '+'
						 case Kind.OP_SUB: // '-'
							 if (mode < 2) {
								 logicTree = t;
								 mode = 2;
							 }
							 break;
						 case Kind.OP_LTN: // '<'
						 case Kind.OP_LEQ: // '<='
						 case Kind.OP_MTN: // '>'
						 case Kind.OP_MEQ: // '>='
							 if (mode < 3) {
								 logicTree = t;
								 mode = 3;
							 }
							 break;
						 case Kind.OP_EQL: // '=='
						 case Kind.OP_NQL: // '!='
							 if (mode < 4) {
								 logicTree = t;
								 mode = 4;
							 }
							 break;
						 case Kind.OP_LND: // '&&'
							 if (mode < 5) {
								 logicTree = t;
								 mode = 5;
							 }
							 break;
						 case Kind.OP_LOR: // '||'
							 if (mode < 6) {
								 logicTree = t;
								 mode = 6;
							 }
							 break;
						 case Kind.CX_ANC_OPEN:
							 logicTree = null;
							 openTree = t;
							 break;
						 case Kind.CX_ANC_CLOSE:
							 closeTree = t;
							 break for0;
						 default:
							 break;
					 }

				 if (logicTree == null)
					 return;

				 int position =
						 openTree == null ?
						 tree.reference().position() :
						 openTree.reference().position() +
						 openTree.reference().length();
				 int length =
						 closeTree == null ?
						 tree.reference().position() +
						 tree.reference().length() -
						 position :
						 closeTree.reference().position() -
						 position;
				 int leftPos = position;
				 int leftLen = logicTree.reference().position() -
							   leftPos;
				 int rightPos = logicTree.reference().position() +
								logicTree.reference().length();
				 int rightLen = position +
								length -
								rightPos;

				 //results
				 Sketch sketch = logicTree.getSketch()
										  .get(Component.KEY.opposite())
										  .setKind(Kind.CX_PCM_LGC);
				 tree.offer(new Tree(
						 tree.document(),
						 new Reference(
								 position,
								 length
						 ),
						 sketch,
						 100
				 ));
				 //left
				 tree.offer(new Tree(
						 tree.document(),
						 new Reference(
								 leftPos,
								 leftLen
						 ),
						 sketch.get(Component.LEFT)
							   .setKind(Kind.CX_PRM),
						 -100
				 ));
				 //right
				 tree.offer(new Tree(
						 tree.document(),
						 new Reference(
								 rightPos,
								 rightLen
						 ),
						 sketch.get(Component.RIGHT)
							   .setKind(Kind.CX_PRM),
						 -100
				 ));

				 modified[0] = true;
			 });
		return modified[0];
	};

	/**
	 * A processor that parses references.
	 *
	 * @since 0.2.0 ~2021.06.01
	 */
	@SuppressWarnings("OverlyLongLambda")
	@NotNull
	public static final Processor CX_PCM_REF = compilation -> {
		boolean[] modified = {false};
		//noinspection OverlyLongLambda
		Trees.collect(compilation.getRootTree())
			 .parallelStream()
			 .filter(tree -> {
				 if (!tree.getSketch().getKind().equals(Kind.CX_PCM_REF))
					 for (Tree t : tree)
						 switch (t.getSketch().getKind()) {
							 case Kind.SX_NME:
								 return true;
						 }

				 return false;
			 })
			 .forEach(tree -> {
				 Tree nmeTree = null;
				 Tree firstSqr = null;
				 Tree lastSqr = null;

				 for0:
				 for (Tree t : tree)
					 //noinspection SwitchStatementDensity
					 switch (t.getSketch().getKind()) {
						 case Kind.SX_NME:
							 if (nmeTree != null && firstSqr != null)
								 //reached the next tree, stop
								 break for0;

							 firstSqr = null;
							 lastSqr = null;
							 nmeTree = t;
							 break;
						 case Kind.SX_SQR:
							 if (nmeTree != null) {
								 if (lastSqr != null) {
									 if (Intersection.compute(lastSqr, t) !=
										 Intersection.NEXT)
										 //reached last valid square
										 break for0;

									 lastSqr = t;
									 break;
								 }

								 if (Intersection.compute(nmeTree, t) !=
									 Intersection.NEXT)
									 //no valid square
									 return;

								 //first valid square
								 firstSqr = t;
								 lastSqr = t;
								 break;
							 }

							 //previous square
							 break;
						 default:
							 if (nmeTree != null)
								 //no more valid squares
								 break;
					 }

				 if (firstSqr == null)
					 //no valid square
					 return;

				 int pointPos = firstSqr.reference().position();
				 int pointLength = lastSqr.reference().position() +
								   lastSqr.reference().length() -
								   pointPos;

				 int refPosition = nmeTree.reference().position();
				 int refLength = lastSqr.reference().position() +
								 lastSqr.reference().length() -
								 refPosition;

				 Sketch sketch = new Sketch(Kind.CX_PCM_REF);
				 nmeTree.setSketch(
						 sketch.get(Component.KEY)
							   .setKind(Kind.SX_NME)
							   .setTree(nmeTree)
				 );
				 tree.offer(new Tree(
						 tree.document(),
						 new Reference(
								 refPosition,
								 refLength
						 ),
						 sketch,
						 -1
				 ));
				 tree.offer(new Tree(
						 tree.document(),
						 new Reference(
								 pointPos,
								 pointLength
						 ),
						 sketch.get(Component.ACCESSOR)
							   .setKind(Kind.CX_PRM),
						 -100
				 ));

				 modified[0] = true;
			 });
		return modified[0];
	};

	//OZ

	/**
	 * A processor that cleans unwanted trees.
	 *
	 * @since 0.2.0 ~2021.06.03
	 */
	@SuppressWarnings("OverlyLongLambda")
	@NotNull
	public static final Processor OZ_CLEAN = compilation -> {
		boolean[] modified = {false};
		Trees.collect(compilation.getRootTree())
			 .stream()
			 .filter(tree -> {
				 switch (tree.getSketch().getKind()) {
					 case Kind.SX_QTE:
					 case Kind.SX_DQT:
					 case Kind.SX_NME:
					 case Kind.SX_NUM:
					 case Kind.CX_CMD_TYPE:
					 case Kind.CX_CMD_KEY:
						 return true;
					 default:
						 return false;
				 }
			 })
			 .forEach(tree -> {
				 Iterator<Tree> iterator = tree.iterator();

				 while (iterator.hasNext())
					 switch (iterator.next().getSketch().getKind()) {
						 case Kind.CX_ANC_OPEN:
						 case Kind.CX_ANC_CLOSE:
							 break;
						 default:
							 iterator.remove();
							 modified[0] = true;
					 }
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
