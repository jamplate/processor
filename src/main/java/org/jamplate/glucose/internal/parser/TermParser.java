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

import org.intellij.lang.annotations.Language;
import org.jamplate.function.Parser;
import org.jamplate.model.Compilation;
import org.jamplate.model.Document;
import org.jamplate.model.Reference;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

import static org.jamplate.util.Parsing.parseAll;
import static org.jamplate.util.Parsing.parseFirst;

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
	 * True, to parse all valid matches each time.
	 *
	 * @since 0.3.0 ~2021.07.04
	 */
	protected final boolean global;
	/**
	 * A pattern matching the literal.
	 *
	 * @since 0.0.1 ~2021.04.28
	 */
	@NotNull
	protected final Pattern pattern;
	/**
	 * The weight to accept.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	protected final int weight;

	/**
	 * Construct a new literal parser that parses the sketches looking for areas that
	 * matches the given {@code pattern}.
	 *
	 * @param pattern     the pattern matching the areas the constructed parser will be
	 *                    looking for.
	 * @param weight      the weight to accept.
	 * @param global      pass {@code true} to parse all valid matches each time, pass
	 *                    {@code false} to parse only the first match each time.
	 * @param constructor the constructor.
	 * @throws NullPointerException if the given {@code pattern} or {@code constructor} is
	 *                              null.
	 * @since 0.2.0 ~2021.05.16
	 */
	public TermParser(
			@NotNull Pattern pattern,
			int weight,
			boolean global,
			@NotNull BiFunction<Document, Reference, Tree> constructor
	) {
		Objects.requireNonNull(pattern, "pattern");
		Objects.requireNonNull(constructor, "constructor");
		this.pattern = pattern;
		this.weight = weight;
		this.global = global;
		this.constructor = constructor;
	}

	/**
	 * Construct a new literal parser that parses the sketches looking for areas that
	 * matches the given {@code regex}.
	 *
	 * @param regex       the regex matching the areas the constructed parser will be
	 *                    looking for.
	 * @param constructor the constructor.
	 * @return a new term parser.
	 * @throws NullPointerException   if the given {@code regex} or {@code constructor} is
	 *                                null.
	 * @throws PatternSyntaxException if the given {@code regex} has syntax error.
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	@Contract(value = "_,_->new", pure = true)
	public static TermParser term(
			@NotNull @Language("RegExp") String regex,
			@NotNull BiFunction<Document, Reference, Tree> constructor
	) {
		Objects.requireNonNull(regex, "regex");
		return new TermParser(
				Pattern.compile(regex),
				0,
				false,
				constructor
		);
	}

	/**
	 * Construct a new literal parser that parses the sketches looking for areas that
	 * matches the given {@code regex}.
	 *
	 * @param regex       the regex matching the areas the constructed parser will be
	 *                    looking for.
	 * @param weight      the weight to accept.
	 * @param global      pass {@code true} to parse all valid matches each time, pass
	 *                    {@code false} to parse only the first match each time.
	 * @param constructor the constructor.
	 * @return a new term parser.
	 * @throws NullPointerException   if the given {@code regex} or {@code constructor} is
	 *                                null.
	 * @throws PatternSyntaxException if the given {@code regex} has syntax error.
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	@Contract(value = "_,_,_,_->new", pure = true)
	public static TermParser term(
			@NotNull @Language("RegExp") String regex,
			int weight,
			boolean global,
			@NotNull BiFunction<Document, Reference, Tree> constructor
	) {
		Objects.requireNonNull(regex, "regex");
		return new TermParser(
				Pattern.compile(regex),
				weight,
				global,
				constructor
		);
	}

	@NotNull
	@Override
	public Set<Tree> parse(@NotNull Compilation compilation, @NotNull Tree tree) {
		Objects.requireNonNull(compilation, "compilation");
		Objects.requireNonNull(tree, "sketch");
		if (this.global) {
			Set<Reference> matches = parseAll(
					tree,
					this.pattern,
					this.weight
			);

			return matches
					.parallelStream()
					.map(match -> this.constructor.apply(tree.getDocument(), match))
					.collect(Collectors.toSet());
		} else {
			Reference match = parseFirst(
					tree,
					this.pattern,
					this.weight
			);

			return match == null ?
				   Collections.emptySet() :
				   Collections.singleton(
						   this.constructor.apply(tree.getDocument(), match)
				   );
		}
	}
}
