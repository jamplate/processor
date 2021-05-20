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
package org.jamplate.impl.syntax.compile;

import org.jamplate.compile.Parser;
import org.jamplate.impl.model.Literal;
import org.jamplate.model.Compilation;
import org.jamplate.model.Document;
import org.jamplate.model.Tree;
import org.jamplate.util.Parsing;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * A parser parsing literal sketches depending on a specific pattern.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.04.28
 */
public class LiteralParser implements Parser {
	/**
	 * The constructor to be used to construct new sketches.
	 *
	 * @since 0.2.0 ~2021.05.20
	 */
	@NotNull
	protected final Supplier<Literal> constructor;
	/**
	 * A pattern matching the literal.
	 *
	 * @since 0.0.1 ~2021.04.28
	 */
	@NotNull
	protected final Pattern pattern;

	/**
	 * Construct a new literal parser that parses the sketches looking for areas that
	 * matches the given {@code pattern}.
	 *
	 * @param pattern the pattern matching the areas the constructed parser will be
	 *                looking for.
	 * @throws NullPointerException if the given {@code pattern} is null.
	 * @since 0.2.0 ~2021.05.16
	 */
	public LiteralParser(@NotNull Pattern pattern) {
		Objects.requireNonNull(pattern, "pattern");
		this.constructor = Literal::new;
		this.pattern = pattern;
	}

	/**
	 * Construct a new literal parser that parses the sketches looking for areas that
	 * matches the given {@code pattern}.
	 *
	 * @param constructor the constructor to be used by the constructed parser to
	 *                    constructed the sketches of the matches.
	 * @param pattern     the pattern matching the areas the constructed parser will be
	 *                    looking for.
	 * @throws NullPointerException if the given {@code constructor} or {@code pattern} is
	 *                              null.
	 * @since 0.2.0 ~2021.05.16
	 */
	public LiteralParser(@NotNull Supplier<Literal> constructor, @NotNull Pattern pattern) {
		Objects.requireNonNull(constructor, "constructor");
		Objects.requireNonNull(pattern, "pattern");
		this.constructor = constructor;
		this.pattern = pattern;
	}

	@NotNull
	@Override
	public Set<Tree> parse(@NotNull Compilation compilation, @NotNull Tree tree) {
		Objects.requireNonNull(compilation, "compilation");
		Objects.requireNonNull(tree, "sketch");
		return Parsing.parseAll(tree, this.pattern)
					  .parallelStream()
					  .map(m -> {
						  Document d = tree.document();
						  Literal s = this.constructor.get();
						  Tree t = new Tree(d, m, s);
						  s.setTree(t);
						  return t;
					  })
					  .collect(Collectors.toSet());
	}
}
