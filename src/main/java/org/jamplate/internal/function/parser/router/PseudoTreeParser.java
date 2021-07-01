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
package org.jamplate.internal.function.parser.router;

import org.jamplate.function.Parser;
import org.jamplate.model.Compilation;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A parser that parses a temporary tree using a parser {@code selector} and parses that
 * tree using another parser {@code parser}.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.27
 */
public class PseudoTreeParser implements Parser {
	/**
	 * The parser to parse result trees from the temporary tree.
	 *
	 * @since 0.3.0 ~2021.06.27
	 */
	@NotNull
	protected final Parser parser;
	/**
	 * The parser to parse the temporary tree.
	 *
	 * @since 0.3.0 ~2021.06.27
	 */
	@NotNull
	protected final Parser selector;

	/**
	 * Construct a new parser that parses a temporary tree using the given {@code
	 * selector} parser and parses that tree using the given {@code parser}.
	 *
	 * @param selector the parser to parse the temporary tree with.
	 * @param parser   the parser to parse the result trees from the temporary tree.
	 * @throws NullPointerException if the given {@code selector} or {@code parser} is
	 *                              null.
	 * @since 0.3.0 ~2021.06.27
	 */
	public PseudoTreeParser(@NotNull Parser selector, @NotNull Parser parser) {
		Objects.requireNonNull(selector, "selector");
		Objects.requireNonNull(parser, "parser");
		this.selector = selector;
		this.parser = parser;
	}

	@NotNull
	@Override
	public Set<Tree> parse(@NotNull Compilation compilation, @NotNull Tree tree) {
		Objects.requireNonNull(compilation, "compilation");
		Objects.requireNonNull(tree, "tree");
		return this.selector
				.parse(compilation, tree)
				.stream()
				.flatMap(selected -> this.parser.parse(compilation, selected).stream())
				.collect(Collectors.toSet());
	}
}
