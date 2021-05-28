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

import org.jamplate.model.Compilation;
import org.jamplate.model.Instruction;
import org.jamplate.model.Tree;
import org.jamplate.model.function.Compiler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A compiler that tris compiling using multiple other compilers in order and returns the
 * results of the first compiler to succeed.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.21
 */
public class OrderCompiler implements Compiler {
	/**
	 * The compilers backing this compiler. (ordered)
	 *
	 * @since 0.2.0 ~2021.05.21
	 */
	@NotNull
	protected final List<Compiler> compilers;

	/**
	 * Construct a new order-compiler that uses the given {@code compilers}.
	 * <br>
	 * Null compilers in the given array will be ignored.
	 *
	 * @param compilers the compilers (in-order) to be used by the constructed compiler.
	 * @throws NullPointerException if the given {@code compilers} is null.
	 * @since 0.2.0 ~2021.05.21
	 */
	public OrderCompiler(@Nullable Compiler @NotNull ... compilers) {
		Objects.requireNonNull(compilers, "compilers");
		this.compilers = Arrays
				.stream(compilers)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}

	/**
	 * Construct a new order-compiler that uses the given {@code compilers}.
	 * <br>
	 * Null compilers in the given list will be ignored.
	 *
	 * @param compilers the compilers (in-order) to be used by the constructed compiler.
	 * @throws NullPointerException if the given {@code compilers} is null.
	 * @since 0.2.0 ~2021.05.21
	 */
	public OrderCompiler(@NotNull List<Compiler> compilers) {
		Objects.requireNonNull(compilers, "compilers");
		this.compilers = new ArrayList<>();
		for (Compiler compiler : compilers)
			if (compiler != null)
				this.compilers.add(compiler);
	}

	@Nullable
	@Override
	public Instruction compile(@NotNull Compiler compiler, @NotNull Compilation compilation, @NotNull Tree tree) {
		Objects.requireNonNull(compilation, "compilation");
		Objects.requireNonNull(tree, "tree");
		for (Compiler c : this.compilers) {
			Instruction instruction = c.compile(compiler, compilation, tree);

			if (instruction != null)
				return instruction;
		}

		return null;
	}
}
