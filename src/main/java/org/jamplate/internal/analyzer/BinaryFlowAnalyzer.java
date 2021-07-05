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
import java.util.function.Predicate;

import static org.jamplate.internal.util.References.exclusive;
import static org.jamplate.internal.util.References.inclusive;

/**
 * An analyzer that wraps a context starting with a tree satisfying a pre-specified
 * predicate and ending with a tree satisfying a pre-specified predicate.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.24
 */
public class BinaryFlowAnalyzer implements Analyzer {
	/**
	 * An optional constructor to wrap the body of the context.
	 *
	 * @since 0.3.0 ~2021.06.24
	 */
	@Nullable
	protected final BiConsumer<Tree, Reference> bodyConstructor;
	/**
	 * The wrapper constructor.
	 *
	 * @since 0.3.0 ~2021.06.24
	 */
	@NotNull
	protected final BiFunction<Document, Reference, Tree> constructor;
	/**
	 * An optional constructor to alter the ending tree.
	 *
	 * @since 0.3.0 ~2021.06.24
	 */
	@Nullable
	protected final BiConsumer<Tree, Tree> endConstructor;
	/**
	 * The ending tree predicate.
	 *
	 * @since 0.3.0 ~2021.06.24
	 */
	@NotNull
	protected final Predicate<Tree> endPredicate;
	/**
	 * An optional constructor to alter the starting tree.
	 *
	 * @since 0.3.0 ~2021.06.24
	 */
	@Nullable
	protected final BiConsumer<Tree, Tree> startConstructor;
	/**
	 * The starting tree predicate.
	 *
	 * @since 0.3.0 ~2021.06.24
	 */
	@NotNull
	protected final Predicate<Tree> startPredicate;

	/**
	 * Construct a new analyzer that wraps fenced contexts.
	 *
	 * @param startPredicate   a predicate to test the starting tree.
	 * @param endPredicate     a predicate to test the ending tree.
	 * @param constructor      the constructor of the wrapper.
	 *                         <div style="padding: 5px">
	 *                             <b>Input:</b> document, wrapper reference.
	 *                             <br>
	 *                             <b>Output:</b> wrapper tree.
	 *                         </div>
	 * @param startConstructor a constructor to alter the starting tree. (optional)
	 *                         <div style="padding: 5px">
	 *                             <b>Input:</b> wrapper tree, starting tree
	 *                         </div>
	 * @param endConstructor   a constructor to alter the ending tree. (optional)
	 *                         <div style="padding: 5px">
	 *                             <b>Input:</b> wrapper tree, ending tree
	 *                         </div>
	 * @param bodyConstructor  a constructor to wrap the body of the context. (optional)
	 *                         <div style="padding: 5px">
	 *                             <b>Input:</b> wrapper tree, body reference
	 *                         </div>
	 * @throws NullPointerException if the given {@code startPredicate} or {@code
	 *                              endPredicate} or {@code constructor} is null.
	 * @since 0.3.0 ~2021.06.24
	 */
	@SuppressWarnings("ConstructorWithTooManyParameters")
	public BinaryFlowAnalyzer(
			@NotNull Predicate<Tree> startPredicate,
			@NotNull Predicate<Tree> endPredicate,
			@NotNull BiFunction<Document, Reference, Tree> constructor,
			@Nullable BiConsumer<Tree, Tree> startConstructor,
			@Nullable BiConsumer<Tree, Tree> endConstructor,
			@Nullable BiConsumer<Tree, Reference> bodyConstructor
	) {
		Objects.requireNonNull(startPredicate, "startPredicate");
		Objects.requireNonNull(endPredicate, "endPredicate");
		Objects.requireNonNull(constructor, "constructor");
		this.startPredicate = startPredicate;
		this.endPredicate = endPredicate;
		this.constructor = constructor;
		this.startConstructor = startConstructor;
		this.endConstructor = endConstructor;
		this.bodyConstructor = bodyConstructor;
	}

	/**
	 * Construct a new analyzer that wraps fenced contexts.
	 *
	 * @param startPredicate   a predicate to test the starting tree.
	 * @param endPredicate     a predicate to test the ending tree.
	 * @param constructor      the constructor of the wrapper.
	 *                         <div style="padding: 5px">
	 *                             <b>Input:</b> document, wrapper reference.
	 *                             <br>
	 *                             <b>Output:</b> wrapper tree.
	 *                         </div>
	 * @param startConstructor a constructor to alter the starting tree. (optional)
	 *                         <div style="padding: 5px">
	 *                             <b>Input:</b> wrapper tree, starting tree
	 *                         </div>
	 * @param endConstructor   a constructor to alter the ending tree. (optional)
	 *                         <div style="padding: 5px">
	 *                             <b>Input:</b> wrapper tree, ending tree
	 *                         </div>
	 * @param bodyConstructor  a constructor to wrap the body of the context. (optional)
	 *                         <div style="padding: 5px">
	 *                             <b>Input:</b> wrapper tree, body reference
	 *                         </div>
	 * @return a new binary flow analyzer that uses the given constructors.
	 * @throws NullPointerException if the given {@code startPredicate} or {@code
	 *                              endPredicate} or {@code constructor} is null.
	 * @since 0.3.0 ~2021.07.04
	 */
	@SuppressWarnings("MethodWithTooManyParameters")
	@NotNull
	@Contract(value = "_,_,_,_,_,_->new", pure = true)
	public static BinaryFlowAnalyzer binaryFlow(
			@NotNull Predicate<Tree> startPredicate,
			@NotNull Predicate<Tree> endPredicate,
			@NotNull BiFunction<Document, Reference, Tree> constructor,
			@Nullable BiConsumer<Tree, Tree> startConstructor,
			@Nullable BiConsumer<Tree, Tree> endConstructor,
			@Nullable BiConsumer<Tree, Reference> bodyConstructor
	) {
		return new BinaryFlowAnalyzer(
				startPredicate,
				endPredicate,
				constructor,
				startConstructor,
				endConstructor,
				bodyConstructor
		);
	}

	@Override
	public boolean analyze(@NotNull Compilation compilation, @NotNull Tree tree) {
		Objects.requireNonNull(compilation, "compilation");
		Objects.requireNonNull(tree, "tree");
		Document document = tree.getDocument();
		Tree startT = null;
		Tree endT = null;

		for (Tree t : tree) {
			if (this.startPredicate.test(t))
				startT = t;
			if (this.endPredicate.test(t))
				endT = t;
		}

		if (startT != null && endT != null) {
			Tree wrapper = this.constructor.apply(
					document,
					inclusive(
							startT,
							endT
					)
			);

			tree.offer(wrapper);

			if (this.startConstructor != null)
				this.startConstructor.accept(
						wrapper,
						startT
				);
			if (this.endConstructor != null)
				this.endConstructor.accept(
						wrapper,
						endT
				);
			if (this.bodyConstructor != null)
				this.bodyConstructor.accept(
						wrapper,
						exclusive(
								startT,
								endT
						)
				);
		}

		return false;
	}
}
