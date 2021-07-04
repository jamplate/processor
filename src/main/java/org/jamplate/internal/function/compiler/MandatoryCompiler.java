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
package org.jamplate.internal.function.compiler;

import org.jamplate.function.Compiler;
import org.jamplate.model.Compilation;
import org.jamplate.model.CompileException;
import org.jamplate.model.Instruction;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * A compiler that delegates to another compiler and raises an exception if that other
 * compiler failed to compile.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.28
 */
public class MandatoryCompiler implements Compiler {
	/**
	 * The wrapped compiler.
	 *
	 * @since 0.2.0 ~2021.05.28
	 */
	@NotNull
	protected final Compiler compiler;

	/**
	 * Construct a new compiler wrapper that wraps the given {@code compiler} and throws
	 * an exception if it fails to compile.
	 *
	 * @param compiler the compiler to be wrapped.
	 * @throws NullPointerException if the given {@code compiler} is null.
	 * @since 0.2.0 ~2021.05.28
	 */
	public MandatoryCompiler(@NotNull Compiler compiler) {
		Objects.requireNonNull(compiler, "compiler");
		this.compiler = compiler;
	}

	/**
	 * Construct a new compiler that compiles using the given {@code compiler} and throw
	 * an exception if the compiler failed to compile.
	 *
	 * @param compiler the compiler to be used.
	 * @return a new mandatory compiler that uses the given {@code compiler}.
	 * @throws NullPointerException if the givne {@code compiler} is null.
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static MandatoryCompiler mandatory(@NotNull Compiler compiler) {
		return new MandatoryCompiler(compiler);
	}

	@Nullable
	@Override
	public Instruction compile(@NotNull Compiler compiler, @NotNull Compilation compilation, @NotNull Tree tree) {
		Instruction instruction = this.compiler.compile(compiler, compilation, tree);

		if (instruction == null)
			throw new CompileException(
					"Unrecognized token",
					tree
			);

		return instruction;
	}
}
