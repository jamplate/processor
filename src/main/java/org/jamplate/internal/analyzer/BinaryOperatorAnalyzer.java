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
public class BinaryOperatorAnalyzer implements Analyzer {
	/**
	 * The wrapper constructor.
	 *
	 * @since 0.3.0 ~2021.06.22
	 */
	@NotNull
	protected final BiFunction<Document, Reference, Tree> constructor;
	/**
	 * An optional constructor to wrap the left-side of the operator.
	 *
	 * @since 0.3.0 ~2021.06.24
	 */
	@Nullable
	protected final BiConsumer<Tree, Reference> leftConstructor;
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
	 * @param leftConstructor     a constructor to wrap the left-side of the operator.
	 *                            (optional)
	 *                            <div style="padding: 5px">
	 *                                <b>Input:</b> wrapper tree, left-side reference
	 *                            </div>
	 * @param rightConstructor    a constructor to wrap the right-side of the operator.
	 *                            (optional)
	 *                            <div style="padding: 5px">
	 *                                <b>Input:</b> wrapper tree, right-side reference
	 *                            </div>
	 * @throws NullPointerException if the given {@code constructor} is null.
	 * @since 0.3.0 ~2021.06.22
	 */
	public BinaryOperatorAnalyzer(
			@NotNull BiFunction<Document, Reference, Tree> constructor,
			@Nullable BiConsumer<Tree, Tree> operatorConstructor,
			@Nullable BiConsumer<Tree, Reference> leftConstructor,
			@Nullable BiConsumer<Tree, Reference> rightConstructor
	) {
		Objects.requireNonNull(constructor, "constructor");
		this.constructor = constructor;
		this.operatorConstructor = operatorConstructor;
		this.leftConstructor = leftConstructor;
		this.rightConstructor = rightConstructor;
	}

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
	 * @param leftConstructor     a constructor to wrap the left-side of the operator.
	 *                            (optional)
	 *                            <div style="padding: 5px">
	 *                                <b>Input:</b> wrapper tree, left-side reference
	 *                            </div>
	 * @param rightConstructor    a constructor to wrap the right-side of the operator.
	 *                            (optional)
	 *                            <div style="padding: 5px">
	 *                                <b>Input:</b> wrapper tree, right-side reference
	 *                            </div>
	 * @return a new binary operator analyzer that uses the given constructors.
	 * @throws NullPointerException if the given {@code constructor} is null.
	 * @since 0.3.0 ~2021.06.22
	 */
	@NotNull
	@Contract(value = "_,_,_,_->new", pure = true)
	public static BinaryOperatorAnalyzer operator(
			@NotNull BiFunction<Document, Reference, Tree> constructor,
			@Nullable BiConsumer<Tree, Tree> operatorConstructor,
			@Nullable BiConsumer<Tree, Reference> leftConstructor,
			@Nullable BiConsumer<Tree, Reference> rightConstructor
	) {
		return new BinaryOperatorAnalyzer(
				constructor,
				operatorConstructor,
				leftConstructor,
				rightConstructor
		);
	}

	@Override
	public boolean analyze(@NotNull Compilation compilation, @NotNull Tree tree) {
		Objects.requireNonNull(compilation, "compilation");
		Objects.requireNonNull(tree, "tree");
		Document document = tree.getDocument();

		Tree previous = tree.getPrevious();
		Tree next = tree.getNext();

		Tree head = head(tree);
		Tree tail = tail(tree);

		Tree wrapper = this.constructor.apply(
				document,
				inclusive(head, tail)
		);

		tree.offer(wrapper);

		if (this.operatorConstructor != null)
			this.operatorConstructor.accept(
					wrapper,
					tree
			);
		if (previous != null && this.leftConstructor != null)
			this.leftConstructor.accept(
					wrapper,
					inclusive(head, previous)
			);
		if (next != null && this.rightConstructor != null)
			this.rightConstructor.accept(
					wrapper,
					inclusive(next, tail)
			);

		return true;
	}
}
