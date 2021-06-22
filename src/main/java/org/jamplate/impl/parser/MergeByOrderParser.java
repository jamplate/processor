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
package org.jamplate.impl.parser;

import org.jamplate.model.Compilation;
import org.jamplate.model.Dominance;
import org.jamplate.model.Tree;
import org.jamplate.function.Parser;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A parser that merges the clashing sketches resulting from another parser going from the
 * first to the last tree sequentially.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.18
 */
@Deprecated
public class MergeByOrderParser implements Parser {
	/**
	 * The parser this parser is wrapping.
	 *
	 * @since 0.2.0 ~2021.05.18
	 */
	@NotNull
	protected final Parser parser;

	/**
	 * Construct a sequential clash merging parser merging the results of invoking the
	 * given {@code parser}.
	 *
	 * @param parser the parser the constructed parser is wrapping.
	 * @throws NullPointerException if the given {@code parser} is null.
	 * @since 0.2.0 ~2021.05.17
	 */
	public MergeByOrderParser(@NotNull Parser parser) {
		Objects.requireNonNull(parser, "parser");
		this.parser = parser;
	}

	@NotNull
	@Override
	public Set<Tree> parse(@NotNull Compilation compilation, @NotNull Tree tree) {
		Objects.requireNonNull(compilation, "compilation");
		Objects.requireNonNull(tree, "tree");
		List<Tree> list = this.parser
				.parse(compilation, tree)
				.stream()
				.sorted(Comparator.comparing(Tree::reference))
				.collect(Collectors.toList());

		ListIterator<Tree> iterator = list.listIterator();
		while (iterator.hasNext()) {
			Tree previousTree = iterator.next();
			int nextIndex = iterator.nextIndex();

			while (iterator.hasNext()) {
				Tree nextTree = iterator.next();

				if (!this.check(previousTree, nextTree))
					iterator.remove();
			}

			iterator = list.listIterator(nextIndex);
		}

		return new HashSet<>(list);
	}

	/**
	 * Check if the down-structure of the given {@code next} can be alongside with the
	 * given {@code previous} in its structure.
	 *
	 * @param previous the previous tree that is always wins.
	 * @param next     the slave tree that might win or might lose depending on if it
	 *                 clashes with the previous or not.
	 * @return true, if the given {@code next} can be with the given {@code previous}.
	 * @throws NullPointerException if the given {@code previous} or {@code next} is
	 *                              null.
	 * @since 0.2.0 ~2021.05.18
	 */
	@Contract(pure = true)
	protected boolean check(@NotNull Tree previous, @NotNull Tree next) {
		Objects.requireNonNull(previous, "previous");
		Objects.requireNonNull(next, "next");
		switch (Dominance.compute(previous, next)) {
			case CONTAIN:
				//the previous fits on the next
				for (Tree secondChild : next)
					//check if the previous does clash with any of the children of the next
					if (!this.check(previous, secondChild))
						//the previous clashes with a child in the next, the next must be removed
						return false;

				//the previous fits perfectly in the next
				return true;
			case PART:
				//the previous can contain the next
				for (Tree primaryChild : previous)
					//check if the next does clash with any of the children of the previous
					if (!this.check(primaryChild, next))
						//the next clashes with a child in the previous, the next must be removed
						return false;

				//the next fits perfectly in the previous
				return true;
			case NONE:
				//the two trees do not intersect
				return true;
			case SHARE:
				//the next cannot be with the previous, the next must be removed
				return false;
			case EXACT:
				return previous.getZIndex() != next.getZIndex();
			default:
				//unexpected
				throw new InternalError();
		}
	}
}
