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
package org.jamplate.glucose.internal.analyzer;

import org.jamplate.function.Analyzer;
import org.jamplate.model.Compilation;
import org.jamplate.model.Document;
import org.jamplate.model.Reference;
import org.jamplate.model.Tree;
import org.jamplate.util.References;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * An analyzer that wraps a multi flow starting with a tree satisfying a pre-specified
 * predicate and ending with a tree satisfying a pre-specified predicate.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.07.09
 */
public class MultiFlowAnalyzer implements Analyzer {
	/**
	 * An optional constructor to wrap the body of the context.
	 *
	 * @since 0.3.0 ~2021.07.09
	 */
	@Nullable
	protected final BiConsumer<Tree, Reference> bodyConstructor;
	/**
	 * The wrapper constructor.
	 *
	 * @since 0.3.0 ~2021.07.09
	 */
	@NotNull
	protected final BiFunction<Document, Reference, Tree> constructor;
	/**
	 * An optional constructor to alter the ending tree.
	 *
	 * @since 0.3.0 ~2021.07.09
	 */
	@Nullable
	protected final BiConsumer<Tree, Tree> endConstructor;
	/**
	 * The ending tree predicate.
	 *
	 * @since 0.3.0 ~2021.07.09
	 */
	@NotNull
	protected final Predicate<Tree> endPredicate;
	/**
	 * An optional constructor to alter the middle tree.
	 *
	 * @since 0.3.0 ~2021.07.09
	 */
	@Nullable
	protected final BiConsumer<Tree, Tree> middleConstructor;
	/**
	 * The middle tree predicate.
	 *
	 * @since 0.3.0 ~2021.07.09
	 */
	@NotNull
	protected final BiPredicate<Tree, Tree> middlePredicate;
	/**
	 * An optional constructor to alter the starting tree.
	 *
	 * @since 0.3.0 ~2021.07.09
	 */
	@Nullable
	protected final BiConsumer<Tree, Tree> startConstructor;
	/**
	 * The starting tree predicate.
	 *
	 * @since 0.3.0 ~2021.07.09
	 */
	@NotNull
	protected final Predicate<Tree> startPredicate;
	/**
	 * An optional constructor to link the main flow with the subflow.
	 *
	 * @since 0.3.0 ~2021.07.09
	 */
	@Nullable
	protected final BiConsumer<Tree, Tree> subConstructor;

	/**
	 * Construct a new analyzer that wraps multi fenced contexts.
	 *
	 * @param startPredicate    a predicate to test the starting tree.
	 * @param middlePredicate   a predicate to test the middle tree.
	 * @param endPredicate      a predicate to test the ending tree.
	 * @param constructor       the constructor of the wrappers.
	 *                          <div style="padding: 5px">
	 *                              <b>Input:</b> document, wrapper reference.
	 *                              <br>
	 *                              <b>Output:</b> wrapper tree.
	 *                          </div>
	 * @param subConstructor    a constructor to alter the main wrapper with the
	 *                          sub-wrapper. (optional)
	 *                          <div style="padding: 5px">
	 *                              <b>Input:</b> main wrapper tree, sub wrapper tree
	 *                          </div>
	 * @param startConstructor  a constructor to alter the starting trees. (optional)
	 *                          <div style="padding: 5px">
	 *                              <b>Input:</b> wrapper tree, starting tree
	 *                          </div>
	 * @param middleConstructor a constructor to alter the middle trees. (optional)
	 *                          <div style="padding: 5px">
	 *                              <b>Input:</b> wrapper tree, ending tree
	 *                          </div>
	 * @param endConstructor    a constructor to alter the ending trees. (optional)
	 *                          <div style="padding: 5px">
	 *                              <b>Input:</b> wrapper tree, ending tree
	 *                          </div>
	 * @param bodyConstructor   a constructor to wrap the body of a context. (optional)
	 *                          <div style="padding: 5px">
	 *                              <b>Input:</b> wrapper tree, body reference
	 *                          </div>
	 * @throws NullPointerException if the given {@code startPredicate} or {@code
	 *                              middlePredicate} or {@code endPredicate} or {@code
	 *                              constructor} is null.
	 * @since 0.3.0 ~2021.07.09
	 */
	@SuppressWarnings("ConstructorWithTooManyParameters")
	public MultiFlowAnalyzer(
			@NotNull Predicate<Tree> startPredicate,
			@NotNull BiPredicate<Tree, Tree> middlePredicate,
			@NotNull Predicate<Tree> endPredicate,
			@NotNull BiFunction<Document, Reference, Tree> constructor,
			@Nullable BiConsumer<Tree, Tree> subConstructor,
			@Nullable BiConsumer<Tree, Tree> startConstructor,
			@Nullable BiConsumer<Tree, Tree> middleConstructor,
			@Nullable BiConsumer<Tree, Tree> endConstructor,
			@Nullable BiConsumer<Tree, Reference> bodyConstructor
	) {
		Objects.requireNonNull(startConstructor, "startConstructor");
		Objects.requireNonNull(middlePredicate, "middlePredicate");
		Objects.requireNonNull(endPredicate, "endPredicate");
		Objects.requireNonNull(constructor, "constructor");
		this.startPredicate = startPredicate;
		this.middlePredicate = middlePredicate;
		this.endPredicate = endPredicate;
		this.constructor = constructor;
		this.subConstructor = subConstructor;
		this.startConstructor = startConstructor;
		this.middleConstructor = middleConstructor;
		this.endConstructor = endConstructor;
		this.bodyConstructor = bodyConstructor;
	}

	/**
	 * Construct a new analyzer that wraps multi fenced contexts.
	 *
	 * @param startPredicate    a predicate to test the starting tree.
	 * @param middlePredicate   a predicate to test the middle tree.
	 * @param endPredicate      a predicate to test the ending tree.
	 * @param constructor       the constructor of the wrappers.
	 *                          <div style="padding: 5px">
	 *                              <b>Input:</b> document, wrapper reference.
	 *                              <br>
	 *                              <b>Output:</b> wrapper tree.
	 *                          </div>
	 * @param subConstructor    a constructor to alter the main wrapper with the
	 *                          sub-wrapper. (optional)
	 *                          <div style="padding: 5px">
	 *                              <b>Input:</b> main wrapper tree, sub wrapper tree
	 *                          </div>
	 * @param startConstructor  a constructor to alter the starting trees. (optional)
	 *                          <div style="padding: 5px">
	 *                              <b>Input:</b> wrapper tree, starting tree
	 *                          </div>
	 * @param middleConstructor a constructor to alter the middle trees. (optional)
	 *                          <div style="padding: 5px">
	 *                              <b>Input:</b> wrapper tree, ending tree
	 *                          </div>
	 * @param endConstructor    a constructor to alter the ending trees. (optional)
	 *                          <div style="padding: 5px">
	 *                              <b>Input:</b> wrapper tree, ending tree
	 *                          </div>
	 * @param bodyConstructor   a constructor to wrap the body of a context. (optional)
	 *                          <div style="padding: 5px">
	 *                              <b>Input:</b> wrapper tree, body reference
	 *                          </div>
	 * @return a new multi flow analyzer with the given parameters.
	 * @throws NullPointerException if the given {@code startPredicate} or {@code
	 *                              middlePredicate} or {@code endPredicate} or {@code
	 *                              constructor} is null.
	 * @since 0.3.0 ~2021.07.09
	 */
	@SuppressWarnings("MethodWithTooManyParameters")
	@NotNull
	@Contract(value = "_,_,_,_,_,_,_,_,_->new", pure = true)
	public static MultiFlowAnalyzer flow(
			@NotNull Predicate<Tree> startPredicate,
			@NotNull BiPredicate<Tree, Tree> middlePredicate,
			@NotNull Predicate<Tree> endPredicate,
			@NotNull BiFunction<Document, Reference, Tree> constructor,
			@Nullable BiConsumer<Tree, Tree> subConstructor,
			@Nullable BiConsumer<Tree, Tree> startConstructor,
			@Nullable BiConsumer<Tree, Tree> middleConstructor,
			@Nullable BiConsumer<Tree, Tree> endConstructor,
			@Nullable BiConsumer<Tree, Reference> bodyConstructor
	) {
		return new MultiFlowAnalyzer(
				startPredicate,
				middlePredicate,
				endPredicate,
				constructor,
				subConstructor,
				startConstructor,
				middleConstructor,
				endConstructor,
				bodyConstructor
		);
	}

	@SuppressWarnings({"OverlyLongMethod", "OverlyComplexMethod"})
	@Override
	public boolean analyze(@NotNull Compilation compilation, @NotNull Tree tree) {
		Objects.requireNonNull(compilation, "compilation");
		Objects.requireNonNull(tree, "tree");

		//the document
		Document document = tree.getDocument();

		//the main start tree
		Tree startT = null;
		//the main end tree
		Tree endT = null;
		//the middle trees (from the first to the last)
		LinkedList<Tree> middleTs = new LinkedList<>();

		//query
		for (Tree t : tree) {
			//possible start?
			if (this.startPredicate.test(t)) {
				//start found!
				startT = t;
				//reset middles
				middleTs.clear();
				continue;
			}

			//check on the middle
			if (this.middlePredicate.test(middleTs.peekLast(), t)) {
				//found a middle!
				middleTs.addLast(t);
				continue;
			}

			//check on the end
			if (this.endPredicate.test(t)) {
				//found the end!
				endT = t;
				//done
				break;
			}
		}

		//analytic
		if (startT != null && endT != null) {
			Iterator<Tree> iterator = middleTs.iterator();

			//the previous wrapper
			Tree prevWrapper = null;
			//the start tree of the next wrapper
			Tree gateT = startT;

			while (true) {
				//the middle tree of the next wrapper
				Tree middleT = iterator.hasNext() ?
							   iterator.next() :
							   null;

				//the next wrapper
				Tree wrapper = this.constructor.apply(
						document,
						//if main wrapper
						gateT == startT ?
						//then from start to end
						References.inclusive(startT, endT) :
						//otherwise, from start before end
						References.inclusiveExclusive(gateT, endT)
				);

				//offer to the hierarchy (aka put result)
				tree.offer(wrapper);

				//main wrapper to sub-wrapper
				if (prevWrapper != null && this.subConstructor != null)
					this.subConstructor.accept(
							prevWrapper,
							wrapper
					);
				//wrapper.start constructor
				if (this.startConstructor != null)
					this.startConstructor.accept(
							wrapper,
							gateT
					);
				//wrapper.middle
				if (middleT != null && this.middleConstructor != null)
					this.middleConstructor.accept(
							wrapper,
							middleT
					);
				//wrapper.end
				if (gateT == startT && this.endConstructor != null)
					this.endConstructor.accept(
							wrapper,
							endT
					);
				//wrapper.body
				if (this.bodyConstructor != null)
					this.bodyConstructor.accept(
							wrapper,
							//if no middle
							middleT == null ?
							//then after start before end
							References.exclusive(gateT, endT) :
							//otherwise, after start before middle
							References.exclusive(gateT, middleT)
					);

				//if the ending tree is null, then no
				//starting tree for the next wrapper
				if (middleT == null)
					break;

				prevWrapper = wrapper;
				//the starting tree of the next wrapper
				//is the ending tree of the current wrapper
				gateT = middleT;
			}

			return true;
		}

		return false;
	}
}
