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

import org.jamplate.function.Parser;
import org.jamplate.model.Compilation;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A parser that combines the results of multiple other parsers.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.17
 */
public class CombineParser implements Parser {
	/**
	 * The parsers this parser is combining. (not guaranteed to be immutable nor mutable)
	 *
	 * @since 0.2.0 ~2021.05.17
	 */
	@NotNull
	protected final Set<Parser> parsers;

	/**
	 * Construct a new parser that combines the results of the given {@code parsers}.
	 * <br>
	 * Null parsers in the given array will be ignored.
	 *
	 * @param parsers an array containing the parsers to be used by the constructed
	 *                parser.
	 * @throws NullPointerException if the given {@code parsers} is null.
	 * @since 0.2.0 ~2021.05.17
	 */
	public CombineParser(@Nullable Parser @NotNull ... parsers) {
		Objects.requireNonNull(parsers, "parsers");
		this.parsers = Arrays.stream(parsers)
							 .filter(Objects::nonNull)
							 .collect(Collectors.toSet());
	}

	/**
	 * Construct a new parser that combines the results of the given {@code parsers}.
	 * <br>
	 * Null parsers in the given array will be ignored.
	 *
	 * @param parsers an array containing the parsers to be used by the constructed
	 *                parser.
	 * @throws NullPointerException if the given {@code parsers} is null.
	 * @since 0.2.0 ~2021.05.17
	 */
	public CombineParser(@NotNull Iterable<Parser> parsers) {
		Objects.requireNonNull(parsers, "parsers");
		this.parsers = new HashSet<>();
		for (Parser parser : parsers)
			if (parser != null)
				this.parsers.add(parser);
	}

	/**
	 * Construct a new parser that parses with the given {@code parsers} and combine the
	 * answers in a single tree set.
	 *
	 * @param parsers the parsers to be used.
	 * @return a parser that combines the results of the given {@code parsers}.
	 * @throws NullPointerException if the given {@code parsers} is null.
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static CombineParser combine(@Nullable Parser @NotNull ... parsers) {
		return new CombineParser(parsers);
	}

	/**
	 * Construct a new parser that parses with the given {@code parsers} and combine the
	 * answers in a single tree set.
	 *
	 * @param parsers the parsers to be used.
	 * @return a parser that combines the results of the given {@code parsers}.
	 * @throws NullPointerException if the given {@code parsers} is null.
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static CombineParser combine(@NotNull Iterable<Parser> parsers) {
		return new CombineParser(parsers);
	}

	@NotNull
	@Override
	public Set<Tree> parse(@NotNull Compilation compilation, @NotNull Tree tree) {
		return this.parsers
				.parallelStream()
				.map(parser -> parser.parse(compilation, tree))
				.flatMap(Set::stream)
				.collect(Collectors.toSet());
	}
}
