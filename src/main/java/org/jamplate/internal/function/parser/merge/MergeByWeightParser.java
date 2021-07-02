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
package org.jamplate.internal.function.parser.merge;

import org.jamplate.model.Compilation;
import org.jamplate.model.Dominance;
import org.jamplate.model.Tree;
import org.jamplate.function.Parser;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A parser that uses multiple other parsers for parsing. Any clash in the results
 * returned from them will be fixed by taking the results of the more preferred parser on
 * the clash depending on a predefined list.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.17
 */
public class MergeByWeightParser implements Parser {
	/**
	 * The parsers in order.
	 *
	 * @since 0.2.0 ~2021.05.17
	 */
	@NotNull
	protected final List<Parser> parsers;

	/**
	 * Construct a new parser that uses the given {@code parsers} preferring the results
	 * from the parsers at the start that the results from the parsers that at the end
	 * when a clash occur between their results.
	 * <br>
	 * Null parsers in the array will be ignored.
	 *
	 * @param parsers the parsers to be used by the constructed parser.
	 * @throws NullPointerException if the given {@code parsers} is null.
	 * @since 0.2.0 ~2021.05.18
	 */
	public MergeByWeightParser(@Nullable Parser @NotNull ... parsers) {
		Objects.requireNonNull(parsers, "parsers");
		this.parsers = Arrays.stream(parsers)
							 .filter(Objects::nonNull)
							 .collect(Collectors.toList());
	}

	/**
	 * Construct a new parser that uses the given {@code parsers} preferring the results
	 * from the parsers at the start that the results from the parsers that at the end
	 * when a clash occur between their results.
	 * <br>
	 * Null parsers in the list will be ignored.
	 *
	 * @param parsers the parsers to be used by the constructed parser.
	 * @throws NullPointerException if the given {@code parsers} is null.
	 * @since 0.2.0 ~2021.05.18
	 */
	public MergeByWeightParser(@NotNull List<Parser> parsers) {
		Objects.requireNonNull(parsers, "parsers");
		this.parsers = new ArrayList<>();
		for (Parser parser : parsers)
			if (parser != null)
				this.parsers.add(parser);
	}

	@NotNull
	@Override
	public Set<Tree> parse(@NotNull Compilation compilation, @NotNull Tree tree) {
		Objects.requireNonNull(compilation, "compilation");
		Objects.requireNonNull(tree, "tree");
		List<Set<Tree>> list = this.parsers
				.parallelStream()
				.map(parser -> new HashSet<>(parser.parse(compilation, tree)))
				.collect(Collectors.toList());

		ListIterator<Set<Tree>> iterator = list.listIterator();
		while (iterator.hasNext()) {
			Set<Tree> primarySet = iterator.next();

			//remove any clashes with `primarySet` in the sets after `primarySet`
			list.subList(iterator.nextIndex(), list.size())
				.parallelStream()
				.forEach(secondarySet ->
						//remove the matches in `secondarySet` that clashes with any in `primarySet`
						secondarySet.removeIf(secondaryTree ->
								//foreach in `primarySet` remove matches in `secondaryTree` that matches
								primarySet.parallelStream().anyMatch(
										//use `check` to determine if a clash occurred
										primaryTree -> !this.check(primaryTree, secondaryTree)
								)
						)
				);
		}

		return list.parallelStream()
				   .flatMap(Set::parallelStream)
				   .collect(Collectors.toSet());
	}

	/**
	 * Check if the down-structure of the given {@code secondary} can be alongside with
	 * the given {@code primary} in its structure.
	 *
	 * @param primary   the primary tree that is always wins.
	 * @param secondary the slave tree that might win or might lose depending on if it
	 *                  clashes with the primary or not.
	 * @return true, if the given {@code secondary} can be with the given {@code primary}.
	 * @throws NullPointerException if the given {@code primary} or {@code secondary} is
	 *                              null.
	 * @since 0.2.0 ~2021.05.18
	 */
	@Contract(pure = true)
	protected boolean check(@NotNull Tree primary, @NotNull Tree secondary) {
		Objects.requireNonNull(primary, "primary");
		Objects.requireNonNull(secondary, "secondary");
		switch (Dominance.compute(primary, secondary)) {
			case CONTAIN:
				//the primary fits on the secondary
				for (Tree secondChild : secondary)
					//check if the primary does clash with any of the children of the secondary
					if (!this.check(primary, secondChild))
						//the primary clashes with a child in the secondary, the secondary must be removed
						return false;

				//the primary fits perfectly in the secondary
				return true;
			case PART:
				//the primary can contain the secondary
				for (Tree primaryChild : primary)
					//check if the secondary does clash with any of the children of the primary
					if (!this.check(primaryChild, secondary))
						//the secondary clashes with a child in the primary, the secondary must be removed
						return false;

				//the secondary fits perfectly in the primary
				return true;
			case NONE:
				//the two trees do not intersect
				return true;
			case SHARE:
				//the secondary cannot be with the primary, the secondary must be removed
				return false;
			case EXACT:
				return primary.getWeight() != secondary.getWeight();
			default:
				//unexpected
				throw new InternalError();
		}
	}
}
