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
package org.jamplate.internal.parser;

import org.jamplate.function.Parser;
import org.jamplate.model.Compilation;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiPredicate;
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
public class ArtificialMergeParser implements Parser {
	/**
	 * The parsers in order.
	 *
	 * @since 0.2.0 ~2021.05.17
	 */
	@NotNull
	protected final List<Parser> parsers;

	/**
	 * The collision predicate.
	 *
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	protected final BiPredicate<Tree, Tree> predicate;

	/**
	 * Construct a new parser that uses the given {@code parsers} preferring the results
	 * from the parsers at the start that the results from the parsers that at the end
	 * when a clash occur between their results.
	 * <br>
	 * Null parsers in the array will be ignored.
	 *
	 * @param predicate the collision predicate.
	 * @param parsers   the parsers to be used by the constructed parser.
	 * @throws NullPointerException if the given {@code predicate} or {@code parsers} is
	 *                              null.
	 * @since 0.3.0 ~2021.07.04
	 */
	public ArtificialMergeParser(
			@NotNull BiPredicate<Tree, Tree> predicate,
			@Nullable Parser @NotNull ... parsers
	) {
		Objects.requireNonNull(predicate, "predicate");
		Objects.requireNonNull(parsers, "parsers");
		this.predicate = predicate;
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
	 * @param predicate the collision predicate.
	 * @param parsers   the parsers to be used by the constructed parser.
	 * @throws NullPointerException if the given {@code predicate} or {@code parsers} is
	 *                              null.
	 * @since 0.3.0 ~2021.07.04
	 */
	public ArtificialMergeParser(
			@NotNull BiPredicate<Tree, Tree> predicate,
			@NotNull List<Parser> parsers
	) {
		Objects.requireNonNull(predicate, "predicate");
		Objects.requireNonNull(parsers, "parsers");
		this.predicate = predicate;
		this.parsers = new ArrayList<>();
		for (Parser parser : parsers)
			if (parser != null)
				this.parsers.add(parser);
	}

	/**
	 * Construct a new parser that uses the given {@code parsers} preferring the results
	 * from the parsers at the start that the results from the parsers that at the end
	 * when a clash occur between their results.
	 * <br>
	 * Null parsers in the array will be ignored.
	 *
	 * @param predicate the collision predicate.
	 * @param parsers   the parsers to be used by the constructed parser.
	 * @return a merger parser that uses the given {@code predicate} and uses the given
	 *        {@code predicate} to detect collisions.
	 * @throws NullPointerException if the given {@code predicate} or {@code parsers} is
	 *                              null.
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	@Contract(value = "_,_->new", pure = true)
	public static ArtificialMergeParser merge(
			@NotNull BiPredicate<Tree, Tree> predicate,
			@Nullable Parser @NotNull ... parsers
	) {
		return new ArtificialMergeParser(
				predicate,
				parsers
		);
	}

	/**
	 * Construct a new parser that uses the given {@code parsers} preferring the results
	 * from the parsers at the start that the results from the parsers that at the end
	 * when a clash occur between their results.
	 * <br>
	 * Null parsers in the list will be ignored.
	 *
	 * @param predicate the collision predicate.
	 * @param parsers   the parsers to be used by the constructed parser.
	 * @return a merger parser that uses the given {@code predicate} and uses the given
	 *        {@code predicate} to detect collisions.
	 * @throws NullPointerException if the given {@code predicate} or {@code parsers} is
	 *                              null.
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	@Contract(value = "_,_->new", pure = true)
	public static ArtificialMergeParser merge(
			@NotNull BiPredicate<Tree, Tree> predicate,
			@NotNull List<Parser> parsers
	) {
		return new ArtificialMergeParser(
				predicate,
				parsers
		);
	}

	@NotNull
	@Override
	public Iterator<Parser> iterator() {
		return Collections.unmodifiableList(this.parsers).iterator();
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
										primaryTree ->
												!this.predicate.test(
														primaryTree,
														secondaryTree
												)
								)
						)
				);
		}

		return list.parallelStream()
				   .flatMap(Set::parallelStream)
				   .collect(Collectors.toSet());
	}
}
