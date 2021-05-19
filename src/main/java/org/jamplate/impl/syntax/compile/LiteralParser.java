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

import org.jamplate.compile.Compilation;
import org.jamplate.compile.Parser;
import org.jamplate.model.Document;
import org.jamplate.model.Reference;
import org.jamplate.model.Sketch;
import org.jamplate.model.Tree;
import org.jamplate.util.Parsing;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
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
		this.pattern = pattern;
	}

	/**
	 * Construct a new literal parser that parses the sketches looking for areas that
	 * matches the given {@code pattern}.
	 *
	 * @param constructor a function to be used to create new sketches for the parsed
	 *                    trees.
	 * @param pattern     the pattern matching the areas the constructed parser will be
	 *                    looking for.
	 * @throws NullPointerException if the given {@code constructor} or {@code pattern} is
	 *                              null.
	 * @since 0.2.0 ~2021.05.16
	 */
	public LiteralParser(@NotNull Function<Reference, Sketch> constructor, @NotNull Pattern pattern) {
		Objects.requireNonNull(constructor, "constructor");
		Objects.requireNonNull(pattern, "pattern");
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
						  Sketch s = new Sketch();
						  Tree t = new Tree(d, m, s);
						  s.setTree(t);
						  return t;
					  })
					  .collect(Collectors.toSet());
	}
}
