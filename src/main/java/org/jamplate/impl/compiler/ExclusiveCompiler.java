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

import org.jamplate.function.Compiler;
import org.jamplate.model.Compilation;
import org.jamplate.model.Instruction;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * A wrapper compiler that passes a pre-specified fallback compiler to the wrapped
 * compiler.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.28
 */
public class ExclusiveCompiler implements Compiler {
	/**
	 * The fallback compiler.
	 *
	 * @since 0.2.0 ~2021.05.28
	 */
	@NotNull
	protected final Compiler compiler;
	/**
	 * The fallback compiler.
	 *
	 * @since 0.2.0 ~2021.05.28
	 */
	@NotNull
	protected final Compiler fallback;

	/**
	 * Construct a new exclusive compiler that compiles using the given {@code compiler}
	 * with itself as the fallback compiler (ignoring the fallback compiler given to it).
	 *
	 * @param compiler the wrapped compiler.
	 * @throws NullPointerException if the given {@code compiler} is null.
	 * @since 0.2.0 ~2021.05.28
	 */
	public ExclusiveCompiler(@NotNull Compiler compiler) {
		Objects.requireNonNull(compiler, "compiler");
		this.compiler = compiler;
		this.fallback = this;
	}

	/**
	 * Construct a new exclusive compiler that compiles using the given {@code compiler}
	 * with the given {@code fallback} compiler as the fallback compiler (ignoring the
	 * fallback compiler given to it).
	 *
	 * @param compiler the wrapped compiler.
	 * @param fallback the fallback compiler to be passed to the given {@code compiler}.
	 * @since 0.2.0 ~2021.05.28
	 */
	public ExclusiveCompiler(@NotNull Compiler compiler, @NotNull Compiler fallback) {
		Objects.requireNonNull(compiler, "compiler");
		Objects.requireNonNull(fallback, "fallback");
		this.compiler = compiler;
		this.fallback = fallback;
	}

	/**
	 * Construct a new compiler that compiles using the given {@code compiler} with itself
	 * as the fallback compiler.
	 *
	 * @param compiler the compiler to be used.
	 * @return a new exclusive compiler that compiles using the given {@code compiler} and
	 * 		uses itself as the fallback compiler.
	 * @throws NullPointerException if the given {@code compiler} is null.
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	@Contract(pure = true)
	public static ExclusiveCompiler exclusive(@NotNull Compiler compiler) {
		return new ExclusiveCompiler(compiler);
	}

	/**
	 * Construct a new compiler that compiles using the given {@code compiler} with the
	 * given {@code fallback} as the fallback compiler.
	 *
	 * @param compiler the compiler to be used.
	 * @param fallback the fallback compiler.
	 * @return a new exclusive compiler that compiles using the given {@code compiler} and
	 * 		uses the given {@code fallback} as the fallback compiler.
	 * @throws NullPointerException if the given {@code compiler} or {@code fallback} is
	 *                              null.
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	@Contract(pure = true)
	public static ExclusiveCompiler exclusive(@NotNull Compiler compiler, @NotNull Compiler fallback) {
		return new ExclusiveCompiler(compiler, fallback);
	}

	@Nullable
	@Override
	public Instruction compile(@NotNull Compiler compiler, @NotNull Compilation compilation, @NotNull Tree tree) {
		Objects.requireNonNull(compiler, "compiler");
		Objects.requireNonNull(compilation, "compilation");
		Objects.requireNonNull(tree, "tree");
		return this.compiler.compile(
				this.fallback,
				compilation, tree
		);
	}
}
