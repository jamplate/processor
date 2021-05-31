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
import org.jamplate.model.CompileException;
import org.jamplate.model.Reference;
import org.jamplate.model.Sketch;
import org.jamplate.model.Tree;
import org.jamplate.model.function.Processor;
import org.jetbrains.annotations.NotNull;

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

	//CX FLW

	/**
	 * A processor that parses if context.
	 *
	 * @since 0.2.0 ~2021.05.24
	 */
	@SuppressWarnings("OverlyLongLambda")
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
