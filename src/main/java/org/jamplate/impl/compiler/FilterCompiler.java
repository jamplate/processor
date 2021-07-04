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

import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * A compiler that compiles trees satisfying a pre-specified predicate using another
 * compiler.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.07.04
 */
public class FilterCompiler implements Compiler {
	/**
	 * The compiler to be used.
	 *
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	protected final Compiler compiler;
	/**
	 * The predicate to be satisfied to compile.
	 *
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	protected final Predicate<Tree> predicate;

	/**
	 * Construct a new filter compiler that compiles using the given {@code compiler} for
	 * the trees that satisfies the given {@code predicate}.
	 *
	 * @param predicate the predicate that to be satisfied to compile.
	 * @param compiler  the compiler to be used.
	 * @throws NullPointerException if the given {@code predicate} or {@code compiler} is
	 *                              null.
	 * @since 0.3.0 ~2021.07.04
	 */
	public FilterCompiler(@NotNull Predicate<Tree> predicate, @NotNull Compiler compiler) {
		Objects.requireNonNull(predicate, "predicate");
		Objects.requireNonNull(compiler, "compiler");
		this.predicate = predicate;
		this.compiler = compiler;
	}

	/**
	 * Construct a new filter compiler that compiles using the given {@code compiler} for
	 * the trees that satisfies the given {@code predicate}.
	 *
	 * @param compiler  the compiler to be used.
	 * @param predicate the predicate that to be satisfied to compile.
	 * @return a filter compiler that uses the given {@code compiler} when the given
	 *        {@code predicate} is satisfied.
	 * @throws NullPointerException if the given {@code compiler} or {@code predicate} is
	 *                              null.
	 * @since 0.3.0 ~2021.07.03
	 */
	@NotNull
	@Contract(value = "_,_->new", pure = true)
	public static FilterCompiler filter(@NotNull Compiler compiler, @NotNull Predicate<Tree> predicate) {
		return new FilterCompiler(predicate, compiler);
	}

	@Nullable
	@Override
	public Instruction compile(@NotNull Compiler compiler, @NotNull Compilation compilation, @NotNull Tree tree) {
		Objects.requireNonNull(tree, "tree");
		return this.predicate.test(tree) ?
			   this.compiler.compile(compiler, compilation, tree) :
			   null;
	}

	@NotNull
	@Override
	public Iterator<Compiler> iterator() {
		return Collections.singleton(this.compiler).iterator();
	}
}
