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
import org.jamplate.internal.util.Trees;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Because {@link Tree#offer(Tree)} uses {@link Tree#pop()}. The children of the trees
 * that was created by some parser implementations that returns a tree with pre-included
 * children wont be added when offered to the primary tree structure. So, this parser is
 * parsing using another parser and taking the trees and separates their relatives and
 * returned them.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.17
 */
@Deprecated
public class CollectParser implements Parser {
	/**
	 * The wrapped parser.
	 *
	 * @since 0.2.0 ~2021.05.17
	 */
	@NotNull
	protected final Parser parser;

	/**
	 * Construct a new parser wrapping the given {@code parser} and splitting the results
	 * returned by it.
	 *
	 * @param parser the parser to be wrapped.
	 * @throws NullPointerException if the given {@code parser} is null.
	 * @since 0.2.0 ~2021.05.17
	 */
	public CollectParser(@NotNull Parser parser) {
		Objects.requireNonNull(parser, "parser");
		this.parser = parser;
	}

	@NotNull
	@Override
	public Set<Tree> parse(@NotNull Compilation compilation, @NotNull Tree tree) {
		return this.parser
				.parse(compilation, tree)
				.parallelStream()
				.map(Trees::collect)
				.flatMap(Set::stream)
				.collect(Collectors.toSet());
	}
}
