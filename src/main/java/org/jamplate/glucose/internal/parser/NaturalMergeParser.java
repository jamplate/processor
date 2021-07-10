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
package org.jamplate.glucose.internal.parser;

import org.jamplate.function.Parser;
import org.jamplate.model.Compilation;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

/**
 * A parser that merges the clashing sketches resulting from another parser going from the
 * first to the last tree sequentially.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.18
 */
public class NaturalMergeParser implements Parser {
	/**
	 * The parser this parser is wrapping.
	 *
	 * @since 0.2.0 ~2021.05.18
	 */
	@NotNull
	protected final Parser parser;

	/**
	 * The collision predicate.
	 *
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	protected final BiPredicate<Tree, Tree> predicate;

	/**
	 * Construct a sequential clash merging parser merging the results of invoking the
	 * given {@code parser}.
	 *
	 * @param predicate the collision predicate.
	 * @param parser    the parser the constructed parser is wrapping.
	 * @throws NullPointerException if the given {@code predicate} or {@code parser} is
	 *                              null.
	 * @since 0.3.0 ~2021.07.04
	 */
	public NaturalMergeParser(
			@NotNull BiPredicate<Tree, Tree> predicate,
			@NotNull Parser parser
	) {
		Objects.requireNonNull(predicate, "predicate");
		Objects.requireNonNull(parser, "parser");
		this.predicate = predicate;
		this.parser = parser;
	}

	/**
	 * Construct a sequential clash merging parser merging the results of invoking the
	 * given {@code parser}.
	 *
	 * @param predicate the collision predicate.
	 * @param parser    the parser the constructed parser is wrapping.
	 * @return a merge parser that uses the given {@code parer} and detect collisions
	 * 		using the given {@code predicate}.
	 * @throws NullPointerException if the given {@code predicate} or {@code parser} is
	 *                              null.
	 * @since 0.3.0 ~2021.07.04
	 */
	public static NaturalMergeParser merge(
			@NotNull Parser parser,
			@NotNull BiPredicate<Tree, Tree> predicate
	) {
		return new NaturalMergeParser(
				predicate,
				parser
		);
	}

	@NotNull
	@Override
	public Iterator<Parser> iterator() {
		return Collections.singleton(this.parser).iterator();
	}

	@NotNull
	@Override
	public Set<Tree> parse(@NotNull Compilation compilation, @NotNull Tree tree) {
		Objects.requireNonNull(compilation, "compilation");
		Objects.requireNonNull(tree, "tree");
		List<Tree> list = this.parser
				.parse(compilation, tree)
				.stream()
				.sorted(Comparator.comparing(Tree::getReference))
				.collect(Collectors.toList());

		ListIterator<Tree> iterator = list.listIterator();
		while (iterator.hasNext()) {
			Tree previousTree = iterator.next();
			int nextIndex = iterator.nextIndex();

			while (iterator.hasNext()) {
				Tree nextTree = iterator.next();

				if (!this.predicate.test(previousTree, nextTree))
					iterator.remove();
			}

			iterator = list.listIterator(nextIndex);
		}

		return new HashSet<>(list);
	}
}
