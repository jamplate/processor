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
package org.jamplate.impl.function.parser;

import org.jamplate.function.Parser;
import org.jamplate.model.Compilation;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

/**
 * A parser that parses trees satisfying a pre-specified predicate using another parser.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.07.04
 */
public class FilterParser implements Parser {
	/**
	 * The parser to be used.
	 *
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	protected final Parser parser;
	/**
	 * The predicate to be satisfied to parse.
	 *
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	protected final Predicate<Tree> predicate;

	/**
	 * Construct a new filter parser that parses using the given {@code parser} for the
	 * trees that satisfies the given {@code predicate}.
	 *
	 * @param predicate the predicate that to be satisfied to parse.
	 * @param parser    the parser to be used.
	 * @throws NullPointerException if the given {@code predicate} or {@code parser} is
	 *                              null.
	 * @since 0.3.0 ~2021.07.04
	 */
	public FilterParser(@NotNull Predicate<Tree> predicate, @NotNull Parser parser) {
		Objects.requireNonNull(predicate, "predicate");
		Objects.requireNonNull(parser, "parser");
		this.predicate = predicate;
		this.parser = parser;
	}

	/**
	 * Construct a new filter parser that parses using the given {@code parser} for the
	 * trees that satisfies the given {@code predicate}.
	 *
	 * @param parser    the parser to be used.
	 * @param predicate the predicate that to be satisfied to parse.
	 * @return a filter parser that uses the given {@code parser} when the given {@code
	 * 		predicate} is satisfied.
	 * @throws NullPointerException if the given {@code predicate} or {@code parser} is
	 *                              null.
	 * @since 0.3.0 ~2021.07.03
	 */
	@NotNull
	@Contract(value = "_,_->new", pure = true)
	public static FilterParser filter(@NotNull Parser parser, @NotNull Predicate<Tree> predicate) {
		return new FilterParser(predicate, parser);
	}

	@NotNull
	@Override
	public Set<Tree> parse(@NotNull Compilation compilation, @NotNull Tree tree) {
		Objects.requireNonNull(tree, "tree");
		return this.predicate.test(tree) ?
			   this.parser.parse(compilation, tree) :
			   Collections.emptySet();
	}
}
