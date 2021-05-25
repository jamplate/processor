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
package org.jamplate.impl.compiler;

import org.jamplate.impl.instruction.IpedXinstr;
import org.jamplate.model.Compilation;
import org.jamplate.model.Instruction;
import org.jamplate.model.Tree;
import org.jamplate.model.function.Compiler;
import org.jamplate.util.Trees;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A compiler that compiles using two compilers.
 * <br>
 * The first compiler is the compiler given to it when compiling. It uses it for the trees
 * in the tree given to it.
 * <br>
 * The other one is the compiler given to its the constructor. It uses it for both the
 * trees that the first compiler couldn't compile and a newly constructed trees for the
 * ranges between the children of the tree given to it or the tree given to it if the tree
 * given to it contain no children.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.23
 */
public class StrictCompiler implements Compiler {
	/**
	 * The compiler used by this compiler to compile the non-reserved ranges.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	@NotNull
	protected final Compiler fallBack;

	/**
	 * Construct a new strict compiler that compiles the unreserved ranges using the given
	 * {@code fallback} compiler.
	 *
	 * @param fallBack the compiler to be used to compile unreserved ranges.
	 * @throws NullPointerException if the given {@code fallback} is null.
	 * @since 0.2.0 ~2021.05.23
	 */
	public StrictCompiler(@NotNull Compiler fallBack) {
		Objects.requireNonNull(fallBack, "fallBack");
		this.fallBack = fallBack;
	}

	@Nullable
	@Override
	public Instruction compile(@NotNull Compiler compiler, @NotNull Compilation compilation, @NotNull Tree tree) {
		Objects.requireNonNull(compilation, "compilation");
		Objects.requireNonNull(tree, "tree");
		List<Instruction> instructions = new ArrayList<>();
		List<Tree> children = Trees.flatChildren(tree);

		if (children.isEmpty())
			//got no children
			return this.fallBack.compile(compiler, compilation, tree);

		for (Tree child : children) {
			if (child.getParent() != null) {
				Instruction instruction = compiler.compile(compiler, compilation, child);

				if (instruction != null) {
					instructions.add(instruction);
					continue;
				}
			}

			instructions.add(this.fallBack.compile(compiler, compilation, child));
		}

		//nulls are auto filtered
		return new IpedXinstr(tree, instructions);
	}
}