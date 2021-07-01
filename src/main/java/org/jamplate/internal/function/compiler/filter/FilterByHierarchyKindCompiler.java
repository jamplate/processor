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
package org.jamplate.internal.function.compiler.filter;

import org.jamplate.function.Compiler;
import org.jamplate.model.Compilation;
import org.jamplate.model.Instruction;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * A compiler that targets the trees with one of their parents has a pre-specified kind
 * and compiles using another compiler.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.20
 */
public class FilterByHierarchyKindCompiler implements Compiler {
	/**
	 * The wrapped compiler.
	 *
	 * @since 0.3.0 ~2021.06.20
	 */
	@NotNull
	protected final Compiler compiler;
	/**
	 * Te kinds of one of the parents of the trees to accept.
	 *
	 * @since 0.3.0 ~2021.06.20
	 */
	@NotNull
	protected final Set<String> kinds;

	/**
	 * Construct a new parent-kind-filtered compiler that compiles trees with one of their
	 * parents has the given {@code kind} using the given {@code compiler}.
	 *
	 * @param kind     the kind of one of the parents of the trees the constructed
	 *                 compiler will target.
	 * @param compiler the wrapped compiler.
	 * @throws NullPointerException if the given {@code kind} or {@code compiler} is
	 *                              null.
	 * @since 0.3.0 ~2021.06.20
	 */
	public FilterByHierarchyKindCompiler(@NotNull String kind, @NotNull Compiler compiler) {
		Objects.requireNonNull(kind, "kind");
		Objects.requireNonNull(compiler, "compiler");
		this.compiler = compiler;
		this.kinds = Collections.singleton(kind);
	}

	/**
	 * Construct a new parent-kinds-filtered compiler that compiles trees with one of
	 * their parents has the given {@code kinds} using the given {@code compiler}.
	 *
	 * @param kinds    the kinds of one of the parents of the trees the constructed
	 *                 compiler will target.
	 * @param compiler the wrapped compiler.
	 * @throws NullPointerException if the given {@code kinds} or {@code compiler} is
	 *                              null.
	 * @since 0.3.0 ~2021.06.20
	 */
	public FilterByHierarchyKindCompiler(@NotNull Compiler compiler, @Nullable String @NotNull ... kinds) {
		Objects.requireNonNull(kinds, "kinds");
		Objects.requireNonNull(compiler, "compiler");
		this.compiler = compiler;
		this.kinds = new HashSet<>(Arrays.asList(kinds));
	}

	@Nullable
	@Override
	public Instruction compile(@NotNull Compiler compiler, @NotNull Compilation compilation, @NotNull Tree tree) {
		Objects.requireNonNull(compiler, "compiler");
		Objects.requireNonNull(compilation, "compilation");
		Objects.requireNonNull(tree, "tree");

		for (
				Tree parent = tree.getParent();
				parent != null;
				parent = parent.getParent()
		)
			if (this.kinds.contains(parent.getSketch().getKind()))
				return this.compiler.compile(compiler, compilation, tree);

		return null;
	}
}
