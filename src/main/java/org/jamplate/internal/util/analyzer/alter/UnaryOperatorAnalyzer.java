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
package org.jamplate.internal.util.analyzer.alter;

import org.jamplate.function.Analyzer;
import org.jamplate.internal.util.References;
import org.jamplate.internal.util.Trees;
import org.jamplate.model.Compilation;
import org.jamplate.model.Document;
import org.jamplate.model.Reference;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * An analyzer that wraps the trees given to it with an operator context.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.22
 */
public class UnaryOperatorAnalyzer implements Analyzer {
	/**
	 * The wrapper constructor.
	 *
	 * @since 0.3.0 ~2021.06.22
	 */
	@NotNull
	protected final BiFunction<Document, Reference, Tree> constructor;
	/**
	 * An optional constructor to alter the operator tree.
	 *
	 * @since 0.3.0 ~2021.06.24
	 */
	@Nullable
	protected final BiConsumer<Tree, Tree> operatorConstructor;
	/**
	 * An optional constructor to wrap the right-side of the operator.
	 *
	 * @since 0.3.0 ~2021.06.24
	 */
	@Nullable
	protected final BiConsumer<Tree, Reference> rightConstructor;

	/**
	 * Construct a new analyzer that wraps the trees given to it with the result of
	 * invoking the given {@code constructor}.
	 *
	 * @param constructor         the wrapper constructor.
	 *                            <div style="padding: 5px">
	 *                                <b>Input:</b> document, wrapper reference.
	 *                                <br>
	 *                                <b>Output:</b> wrapper tree.
	 *                            </div>
	 * @param operatorConstructor a constructor to alter the operator tree. (optional)
	 *                            <div style="padding: 5px">
	 *                                <b>Input:</b> wrapper tree, operator tree
	 *                            </div>
	 * @param rightConstructor    a constructor to wrap the right-side of the operator.
	 *                            (optional)
	 *                            <div style="padding: 5px">
	 *                                <b>Input:</b> wrapper tree, right-side reference
	 *                            </div>
	 * @throws NullPointerException if the given {@code constructor} is null.
	 * @since 0.3.0 ~2021.06.22
	 */
	public UnaryOperatorAnalyzer(
			@NotNull BiFunction<Document, Reference, Tree> constructor,
			@Nullable BiConsumer<Tree, Tree> operatorConstructor,
			@Nullable BiConsumer<Tree, Reference> rightConstructor
	) {
		Objects.requireNonNull(constructor, "constructor");
		this.constructor = constructor;
		this.operatorConstructor = operatorConstructor;
		this.rightConstructor = rightConstructor;
	}

	@Override
	public boolean analyze(@NotNull Compilation compilation, @NotNull Tree tree) {
		Objects.requireNonNull(compilation, "compilation");
		Objects.requireNonNull(tree, "tree");
		Document document = tree.document();
		Tree next = tree.getNext();

		if (next != null) {
			Tree tail = Trees.tail(next);

			Tree wrapper = this.constructor.apply(
					document,
					References.inclusive(tree, tail)
			);

			tree.offer(wrapper);

			if (this.operatorConstructor != null)
				this.operatorConstructor.accept(
						wrapper,
						tree
				);
			if (this.rightConstructor != null)
				this.rightConstructor.accept(
						wrapper,
						References.inclusive(next, tail)
				);
			return true;
		}

		return false;
	}
}
