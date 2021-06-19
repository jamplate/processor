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
import org.jamplate.model.Tree;
import org.jamplate.function.Parser;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A parser that parses using another parser then parser each resultant tree using another
 * parser and add them to the results.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.31
 */
public class ThenAddParser implements Parser {
	/**
	 * The parser.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	protected final Parser parser;
	/**
	 * The then parser.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	protected final Parser then;

	/**
	 * Construct a parser that parses using the given {@code parser} then parses each
	 * resultant tree using the given {@code then} and add it to the results.
	 *
	 * @param parser the parser.
	 * @param then   the then parser.
	 * @throws NullPointerException if the given {@code parser} or {@code then} is null.
	 * @since 0.2.0 ~2021.05.31
	 */
	public ThenAddParser(@NotNull Parser parser, @NotNull Parser then) {
		Objects.requireNonNull(parser, "parser");
		Objects.requireNonNull(then, "then");
		this.parser = parser;
		this.then = then;
	}

	@NotNull
	@Override
	public Set<Tree> parse(@NotNull Compilation compilation, @NotNull Tree tree) {
		return this.parser
				.parse(compilation, tree)
				.parallelStream()
				.flatMap(t ->
						Stream.concat(
								Stream.of(t),
								this.then.parse(compilation, t)
										 .stream()
						)
				)
				.collect(Collectors.toSet());
	}
}
