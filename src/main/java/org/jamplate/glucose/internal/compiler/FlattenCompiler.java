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
package org.jamplate.glucose.internal.compiler;

import org.jamplate.function.Compiler;
import org.jamplate.impl.instruction.Block;
import org.jamplate.model.Compilation;
import org.jamplate.model.Instruction;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static org.jamplate.util.Trees.flatChildren;

/**
 * A compiler that compiles using two compilers.
 * <br>
 * The first compiler is the "compiler" pre-specified compiler. It uses it for the trees
 * in the tree given to it.
 * <br>
 * The other one is the "fallback" compiler given to its the constructor. It uses it for
 * both the trees that the first compiler couldn't compile and a newly constructed trees
 * for the ranges between the children of the tree given to it or the tree given to it if
 * the tree given to it contain no children.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.23
 */
public class FlattenCompiler implements Compiler {
	/**
	 * The compiler to be used.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	protected final Compiler compiler;
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
	 * @param compiler the compiler.
	 * @param fallBack the compiler to be used to compile unreserved ranges.
	 * @throws NullPointerException if the given {@code compiler} or {@code fallback} is
	 *                              null.
	 * @since 0.2.0 ~2021.05.23
	 */
	public FlattenCompiler(
			@NotNull Compiler compiler,
			@NotNull Compiler fallBack
	) {
		Objects.requireNonNull(compiler, "compiler");
		Objects.requireNonNull(fallBack, "fallBack");
		this.compiler = compiler;
		this.fallBack = fallBack;
	}

	/**
	 * Construct a new flattening compiler that uses the given {@code compiler}.
	 *
	 * @param compiler the compiler to be used to compile both the actual trees and the
	 *                 non-parsed trees.
	 * @return a new flatten compiler that uses the given {@code compiler}.
	 * @throws NullPointerException if the given {@code compiler} is null.
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static FlattenCompiler flatten(
			@NotNull Compiler compiler
	) {
		return new FlattenCompiler(
				compiler,
				compiler
		);
	}

	/**
	 * Construct a new flattening compiler that uses the given {@code compiler} and the
	 * given {@code fallback}.
	 *
	 * @param compiler the compiler to be used to compile parsed trees.
	 * @param fallBack the compiler to be used to compile the trees the given {@code
	 *                 compiler} failed to compile and the non-parsed trees.
	 * @return a new flatten compiler that uses the given {@code compiler} and {@code
	 * 		fallback}.
	 * @throws NullPointerException if the given {@code compiler} or {@code fallback} is
	 *                              null.
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	@Contract(value = "_,_->new", pure = true)
	public static FlattenCompiler flatten(
			@NotNull Compiler compiler,
			@NotNull Compiler fallBack
	) {
		return new FlattenCompiler(
				compiler,
				fallBack
		);
	}

	@Nullable
	@Override
	public Instruction compile(@NotNull Compiler compiler, @NotNull Compilation compilation, @NotNull Tree tree) {
		Objects.requireNonNull(compilation, "compilation");
		Objects.requireNonNull(tree, "tree");
		List<Instruction> instructions = new ArrayList<>();
		List<Tree> children = flatChildren(tree);

		if (children.isEmpty())
			//got no children
			return this.fallBack.compile(compiler, compilation, tree);

		for (Tree child : children) {
			if (child.getParent() != null) {
				Instruction instruction = this.compiler.compile(compiler, compilation, child);

				if (instruction != null) {
					instructions.add(instruction);
					continue;
				}
			}

			instructions.add(this.fallBack.compile(compiler, compilation, child));
		}

		//nulls are auto filtered
		return new Block(tree, instructions);
	}

	@NotNull
	@Override
	public Iterator<Compiler> iterator() {
		return Arrays.asList(this.compiler, this.fallBack).iterator();
	}
}
