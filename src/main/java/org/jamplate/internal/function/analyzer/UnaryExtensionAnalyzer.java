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
package org.jamplate.internal.function.analyzer;

import org.jamplate.function.Analyzer;
import org.jamplate.internal.util.References;
import org.jamplate.internal.util.Trees;
import org.jamplate.model.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * An analyzer that wraps the trees given to it with an extension context.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.22
 */
public class UnaryExtensionAnalyzer implements Analyzer {
	/**
	 * The wrapper constructor.
	 *
	 * @since 0.3.0 ~2021.06.22
	 */
	@NotNull
	protected final BiFunction<Document, Reference, Tree> constructor;
	/**
	 * An optional constructor to alter the extension tree.
	 *
	 * @since 0.3.0 ~2021.06.24
	 */
	@Nullable
	protected final BiConsumer<Tree, Tree> extensionConstructor;
	/**
	 * An optional constructor to wrap the left-side of the extension.
	 *
	 * @since 0.3.0 ~2021.06.24
	 */
	@Nullable
	protected final BiConsumer<Tree, Reference> leftConstructor;

	/**
	 * Construct a new analyzer that wraps the trees given to it with the result of
	 * invoking the given {@code constructor}.
	 *
	 * @param constructor          the wrapper constructor.
	 *                             <div style="padding: 5px">
	 *                                 <b>Input:</b> document, wrapper reference.
	 *                                 <br>
	 *                                 <b>Output:</b> wrapper tree.
	 *                             </div>
	 * @param extensionConstructor a constructor to alter the extension tree. (optional)
	 *                             <div style="padding: 5px">
	 *                                 <b>Input:</b> wrapper tree, extension tree
	 *                             </div>
	 * @param leftConstructor      a constructor to wrap the left-side of the extension.
	 *                             (optional)
	 *                             <div style="padding: 5px">
	 *                                 <b>Input:</b> wrapper tree, left-side reference
	 *                             </div>
	 * @throws NullPointerException if the given {@code constructor} is null.
	 * @since 0.3.0 ~2021.06.22
	 */
	public UnaryExtensionAnalyzer(
			@NotNull BiFunction<Document, Reference, Tree> constructor,
			@Nullable BiConsumer<Tree, Tree> extensionConstructor,
			@Nullable BiConsumer<Tree, Reference> leftConstructor
	) {
		Objects.requireNonNull(constructor, "constructor");
		this.constructor = constructor;
		this.extensionConstructor = extensionConstructor;
		this.leftConstructor = leftConstructor;
	}

	/**
	 * Construct a new analyzer that wraps the trees given to it with the result of
	 * invoking the given {@code constructor}.
	 *
	 * @param constructor          the wrapper constructor.
	 *                             <div style="padding: 5px">
	 *                                 <b>Input:</b> document, wrapper reference.
	 *                                 <br>
	 *                                 <b>Output:</b> wrapper tree.
	 *                             </div>
	 * @param extensionConstructor a constructor to alter the extension tree. (optional)
	 *                             <div style="padding: 5px">
	 *                                 <b>Input:</b> wrapper tree, extension tree
	 *                             </div>
	 * @param leftConstructor      a constructor to wrap the left-side of the extension.
	 *                             (optional)
	 *                             <div style="padding: 5px">
	 *                                 <b>Input:</b> wrapper tree, left-side reference
	 *                             </div>
	 * @return a new unary extension analyzer that uses the given constructors.
	 * @throws NullPointerException if the given {@code constructor} is null.
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	@Contract(value = "_,_,_->new", pure = true)
	public static UnaryExtensionAnalyzer unaryExtension(
			@NotNull BiFunction<Document, Reference, Tree> constructor,
			@Nullable BiConsumer<Tree, Tree> extensionConstructor,
			@Nullable BiConsumer<Tree, Reference> leftConstructor
	) {
		return new UnaryExtensionAnalyzer(
				constructor,
				extensionConstructor,
				leftConstructor
		);
	}

	@Override
	public boolean analyze(@NotNull Compilation compilation, @NotNull Tree tree) {
		Objects.requireNonNull(compilation, "compilation");
		Objects.requireNonNull(tree, "tree");
		Document document = tree.getDocument();
		Tree previous = tree.getPrevious();

		if (previous != null && Intersection.PREVIOUS.test(tree, previous)) {
			Tree head = Trees.head(previous);

			Tree wrapper = this.constructor.apply(
					document,
					References.inclusive(head, tree)
			);

			tree.offer(wrapper);

			if (this.extensionConstructor != null)
				this.extensionConstructor.accept(
						wrapper,
						tree
				);
			if (this.leftConstructor != null)
				this.leftConstructor.accept(
						wrapper,
						References.inclusive(head, previous)
				);
			return true;
		}

		return false;
	}
}
