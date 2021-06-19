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

import org.jamplate.model.Compilation;
import org.jamplate.model.Instruction;
import org.jamplate.model.Tree;
import org.jamplate.function.Compiler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * A compiler that targets a pre-specified kind of trees and compiles using another
 * compiler.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.28
 */
public class FilterByKindCompiler implements Compiler {
	/**
	 * The wrapped compiler.
	 *
	 * @since 0.2.0 ~2021.05.28
	 */
	@NotNull
	protected final Compiler compiler;
	/**
	 * The kind of the trees to accept.
	 *
	 * @since 0.2.0 ~2021.05.28
	 */
	@NotNull
	protected final String kind;

	/**
	 * Construct a new kind-filtered compiler that compiles trees with the given {@code
	 * kind} using the given {@code compiler}.
	 *
	 * @param kind     the kind of trees the constructed compiler will target.
	 * @param compiler the wrapped compiler.
	 * @throws NullPointerException if the given {@code kind} or {@code compiler} is
	 *                              null.
	 * @since 0.2.0 ~2021.05.28
	 */
	public FilterByKindCompiler(@NotNull String kind, @NotNull Compiler compiler) {
		Objects.requireNonNull(kind, "kind");
		Objects.requireNonNull(compiler, "compiler");
		this.kind = kind;
		this.compiler = compiler;
	}

	@Nullable
	@Override
	public Instruction compile(@NotNull Compiler compiler, @NotNull Compilation compilation, @NotNull Tree tree) {
		Objects.requireNonNull(tree, "tree");
		if (tree.getSketch().getKind().equals(this.kind))
			return this.compiler.compile(compiler, compilation, tree);

		return null;
	}
}
