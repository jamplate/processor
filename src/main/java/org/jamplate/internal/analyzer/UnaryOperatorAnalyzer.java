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
package org.jamplate.internal.analyzer;

import org.jamplate.function.Analyzer;
import org.jamplate.model.Compilation;
import org.jamplate.model.Document;
import org.jamplate.model.Reference;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import static org.jamplate.internal.util.References.inclusive;
import static org.jamplate.internal.util.Trees.head;
import static org.jamplate.internal.util.Trees.tail;

/**
 * An analyzer that wraps the trees given to it with an operator context.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.22
 */
public class UnaryOperatorAnalyzer implements Analyzer {
	/**
	 * An optional constructor to wrap the body of the operator.
	 *
	 * @since 0.3.0 ~2021.06.24
	 */
	@Nullable
	protected final BiConsumer<Tree, Reference> bodyConstructor;
	/**
	 * The wrapper constructor.
	 *
	 * @since 0.3.0 ~2021.06.22
	 */
	@NotNull
	protected final BiFunction<Document, Reference, Tree> constructor;
	/**
	 * True, analyze the left side. False, analyze the right side.
	 *
	 * @since 0.3.0 ~2021.07.08
	 */
	protected final boolean left;
	/**
	 * An optional constructor to alter the operator tree.
	 *
	 * @since 0.3.0 ~2021.06.24
	 */
	@Nullable
	protected final BiConsumer<Tree, Tree> operatorConstructor;

	/**
	 * Construct a new analyzer that wraps the trees given to it with the result of
	 * invoking the given {@code constructor}.
	 *
	 * @param left                true, the analyzer will wrap the left-side. false, the
	 *                            analyzer will wrap the right-side.
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
	 * @param bodyConstructor     a constructor to wrap the body of the operator.
	 *                            (optional)
	 *                            <div style="padding: 5px">
	 *                                <b>Input:</b> wrapper tree, left-side or right-side reference
	 *                            </div>
	 * @throws NullPointerException if the given {@code constructor} is null.
	 * @since 0.3.0 ~2021.06.22
	 */
	public UnaryOperatorAnalyzer(
			boolean left,
			@NotNull BiFunction<Document, Reference, Tree> constructor,
			@Nullable BiConsumer<Tree, Tree> operatorConstructor,
			@Nullable BiConsumer<Tree, Reference> bodyConstructor
	) {
		Objects.requireNonNull(constructor, "constructor");
		this.left = left;
		this.constructor = constructor;
		this.operatorConstructor = operatorConstructor;
		this.bodyConstructor = bodyConstructor;
	}

	/**
	 * Construct a new analyzer that wraps the trees given to it with the result of
	 * invoking the given {@code constructor}.
	 *
	 * @param left                true, the analyzer will wrap the left-side. false, the
	 *                            analyzer will wrap the right-side.
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
	 * @param bodyConstructor     a constructor to wrap the body of the operator.
	 *                            (optional)
	 *                            <div style="padding: 5px">
	 *                                <b>Input:</b> wrapper tree, left-side or right-side reference
	 *                            </div>
	 * @return a new unary operator analyzer that uses the given constructors.
	 * @throws NullPointerException if the given {@code constructor} is null.
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	@Contract(value = "_,_,_,_->new", pure = true)
	public static UnaryOperatorAnalyzer operator(
			boolean left,
			@NotNull BiFunction<Document, Reference, Tree> constructor,
			@Nullable BiConsumer<Tree, Tree> operatorConstructor,
			@Nullable BiConsumer<Tree, Reference> bodyConstructor
	) {
		return new UnaryOperatorAnalyzer(
				left,
				constructor,
				operatorConstructor,
				bodyConstructor
		);
	}

	@Override
	public boolean analyze(@NotNull Compilation compilation, @NotNull Tree tree) {
		Objects.requireNonNull(compilation, "compilation");
		Objects.requireNonNull(tree, "tree");
		Document document = tree.getDocument();

		if (this.left) {
			Tree previous = tree.getPrevious();

			Tree head = head(tree);

			Tree wrapper = this.constructor.apply(
					document,
					inclusive(head, tree)
			);

			tree.offer(wrapper);

			if (this.operatorConstructor != null)
				this.operatorConstructor.accept(
						wrapper,
						tree
				);
			if (previous != null && this.bodyConstructor != null)
				this.bodyConstructor.accept(
						wrapper,
						inclusive(head, previous)
				);
		} else {
			Tree next = tree.getNext();

			Tree tail = tail(tree);

			Tree wrapper = this.constructor.apply(
					document,
					inclusive(tree, tail)
			);

			tree.offer(wrapper);

			if (this.operatorConstructor != null)
				this.operatorConstructor.accept(
						wrapper,
						tree
				);
			if (next != null && this.bodyConstructor != null)
				this.bodyConstructor.accept(
						wrapper,
						inclusive(next, tail)
				);
		}

		return true;
	}
}
