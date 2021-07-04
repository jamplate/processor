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
import org.jamplate.model.Compilation;
import org.jamplate.model.Reference;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

/**
 * An analyzer that wraps a context of a separator tokens.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.27
 */
public class SeparatorsAnalyzer implements Analyzer {
	/**
	 * The slot wrapper constructor.
	 *
	 * @since 0.3.0 ~2021.07.02
	 */
	@NotNull
	protected final BiConsumer<Tree, Reference> constructor;
	/**
	 * The predicate to test the separators with.
	 *
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	protected final Predicate<Tree> predicate;

	/**
	 * Construct a new separators analyzer that detects separators using the given {@code
	 * predicate} and wraps the trees between the separators using the given {@code
	 * constructor}.
	 *
	 * @param predicate   a predicate to test the separators with.
	 * @param constructor the slot wrapper constructor.
	 * @throws NullPointerException if the given {@code predicate} or {@code constructor}
	 *                              is null.
	 * @since 0.3.0 ~2021.07.02
	 */
	public SeparatorsAnalyzer(
			@NotNull Predicate<Tree> predicate,
			@NotNull BiConsumer<Tree, Reference> constructor
	) {
		Objects.requireNonNull(predicate, "predicate");
		Objects.requireNonNull(constructor, "constructor");
		this.predicate = predicate;
		this.constructor = constructor;
	}

	/**
	 * Construct a new separators analyzer that detects separators using the given {@code
	 * predicate} and wraps the trees between the separators using the given {@code
	 * constructor}.
	 *
	 * @param predicate   a predicate to test the separators with.
	 * @param constructor the slot wrapper constructor.
	 * @return a new separators constructor that uses the given {@code predicate} and
	 *        {@code constructor}.
	 * @throws NullPointerException if the given {@code predicate} or {@code constructor}
	 *                              is null.
	 * @since 0.3.0 ~2021.07.02
	 */
	@NotNull
	@Contract(value = "_,_->new", pure = true)
	public static SeparatorsAnalyzer separators(
			@NotNull Predicate<Tree> predicate,
			@NotNull BiConsumer<Tree, Reference> constructor
	) {
		return new SeparatorsAnalyzer(
				predicate,
				constructor
		);
	}

	@Override
	public boolean analyze(@NotNull Compilation compilation, @NotNull Tree tree) {
		Objects.requireNonNull(compilation, "compilation");
		Objects.requireNonNull(tree, "tree");

		Iterator<Tree> iterator = tree.iterator();

		//the last reference tree
		Tree last = null;
		while (iterator.hasNext()) {
			Tree next = iterator.next();

			if (this.predicate.test(next)) {
				if (last == null)
					//from the start to the next separator
					this.constructor.accept(
							tree,
							References.inclusiveExclusive(
									tree,
									next
							)
					);
				else
					//from the last separator to the next separator
					this.constructor.accept(
							tree,
							References.exclusive(
									last,
									next
							)
					);

				last = next;
			}
		}

		if (last != null)
			//from the first separator to the end
			this.constructor.accept(
					tree,
					References.exclusiveInclusive(
							last,
							tree
					)
			);
		else
			//no separator
			this.constructor.accept(
					tree,
					tree.getReference()
			);
		return true;
	}
}
