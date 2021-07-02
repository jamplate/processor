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
package org.jamplate.internal.function.parser.pattern;

import org.jamplate.function.Parser;
import org.jamplate.internal.util.Parsing;
import org.jamplate.model.Compilation;
import org.jamplate.model.Document;
import org.jamplate.model.Reference;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * A parser parsing literal sketches depending on a specific pattern.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.04.28
 */
public class TermParser implements Parser {
	/**
	 * The constructor to be used to construct new trees.
	 *
	 * @since 0.2.0 ~2021.05.20
	 */
	@NotNull
	protected final BiFunction<Document, Reference, Tree> constructor;
	/**
	 * A pattern matching the literal.
	 *
	 * @since 0.0.1 ~2021.04.28
	 */
	@NotNull
	protected final Pattern pattern;

	/**
	 * The z-index to accept.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	protected final int zIndex;

	/**
	 * Construct a new literal parser that parses the sketches looking for areas that
	 * matches the given {@code pattern}.
	 *
	 * @param pattern the pattern matching the areas the constructed parser will be
	 *                looking for.
	 * @throws NullPointerException if the given {@code pattern} is null.
	 * @since 0.2.0 ~2021.05.16
	 */
	public TermParser(@NotNull Pattern pattern) {
		Objects.requireNonNull(pattern, "pattern");
		this.pattern = pattern;
		this.zIndex = 0;
		this.constructor = Tree::new;
	}

	/**
	 * Construct a new literal parser that parses the sketches looking for areas that
	 * matches the given {@code pattern}.
	 *
	 * @param pattern the pattern matching the areas the constructed parser will be
	 *                looking for.
	 * @param zIndex  the z-index to accept.
	 * @throws NullPointerException if the given {@code pattern} is null.
	 * @since 0.2.0 ~2021.05.16
	 */
	public TermParser(@NotNull Pattern pattern, int zIndex) {
		Objects.requireNonNull(pattern, "pattern");
		this.pattern = pattern;
		this.zIndex = zIndex;
		this.constructor = Tree::new;
	}

	/**
	 * Construct a new literal parser that parses the sketches looking for areas that
	 * matches the given {@code pattern}.
	 *
	 * @param pattern     the pattern matching the areas the constructed parser will be
	 *                    looking for.
	 * @param constructor the constructor.
	 * @throws NullPointerException if the given {@code pattern} or {@code constructor} is
	 *                              null.
	 * @since 0.2.0 ~2021.05.16
	 */
	public TermParser(@NotNull Pattern pattern, @NotNull BiFunction<Document, Reference, Tree> constructor) {
		Objects.requireNonNull(pattern, "pattern");
		Objects.requireNonNull(constructor, "constructor");
		this.pattern = pattern;
		this.zIndex = 0;
		this.constructor = constructor;
	}

	/**
	 * Construct a new literal parser that parses the sketches looking for areas that
	 * matches the given {@code pattern}.
	 *
	 * @param pattern     the pattern matching the areas the constructed parser will be
	 *                    looking for.
	 * @param zIndex      the z-index to accept.
	 * @param constructor the constructor.
	 * @throws NullPointerException if the given {@code pattern} or {@code constructor} is
	 *                              null.
	 * @since 0.2.0 ~2021.05.16
	 */
	public TermParser(@NotNull Pattern pattern, int zIndex, @NotNull BiFunction<Document, Reference, Tree> constructor) {
		Objects.requireNonNull(pattern, "pattern");
		Objects.requireNonNull(constructor, "constructor");
		this.pattern = pattern;
		this.zIndex = zIndex;
		this.constructor = constructor;
	}

	@NotNull
	@Override
	public Set<Tree> parse(@NotNull Compilation compilation, @NotNull Tree tree) {
		Objects.requireNonNull(compilation, "compilation");
		Objects.requireNonNull(tree, "sketch");
		return Parsing.parseAll(tree, this.pattern, this.zIndex)
					  .parallelStream()
					  .map(m -> this.constructor.apply(tree.getDocument(), m))
					  .collect(Collectors.toSet());
	}
}
