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
package org.jamplate.internal.util.compiler.wrapper;

import org.jamplate.function.Compiler;
import org.jamplate.internal.util.IO;
import org.jamplate.model.Compilation;
import org.jamplate.model.Instruction;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * A wrapper compiler that compiles whitespaces only, using another compiler.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.20
 */
public class FilterWhitespaceCompiler implements Compiler {
	/**
	 * The wrapped compiler.
	 *
	 * @since 0.2.0 ~2021.05.28
	 */
	@NotNull
	protected final Compiler compiler;

	/**
	 * Construct a new whitespace-filtered compiler that compiles trees that are just
	 * whitespace using the given {@code compiler}.
	 *
	 * @param compiler the wrapped compiler.
	 * @throws NullPointerException if the given {@code compiler} is null.
	 * @since 0.3.0 ~2021.05.28
	 */
	public FilterWhitespaceCompiler(@NotNull Compiler compiler) {
		Objects.requireNonNull(compiler, "compiler");
		this.compiler = compiler;
	}

	@Nullable
	@Override
	public Instruction compile(@NotNull Compiler compiler, @NotNull Compilation compilation, @NotNull Tree tree) {
		Objects.requireNonNull(compiler, "compiler");
		Objects.requireNonNull(compilation, "compilation");
		Objects.requireNonNull(tree, "tree");

		if (IO.read(tree).toString().trim().isEmpty())
			return this.compiler.compile(compiler, compilation, tree);

		return null;
	}
}
