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
import org.jamplate.model.function.Parser;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A parser that wraps another parser and parses recursively.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.29
 */
public class RecursiveParser implements Parser {
	/**
	 * The wrapped parser.
	 *
	 * @since 0.2.0 ~2021.05.29
	 */
	@NotNull
	protected final Parser parser;

	/**
	 * Construct a new recursive parser that parses using the given {@code parser}.
	 *
	 * @param parser the wrapped parser.
	 * @throws NullPointerException if the given {@code parser} is null.
	 * @since 0.2.0 ~2021.05.29
	 */
	public RecursiveParser(@NotNull Parser parser) {
		Objects.requireNonNull(parser, "parser");
		this.parser = parser;
	}

	@NotNull
	@Override
	public Set<Tree> parse(@NotNull Compilation compilation, @NotNull Tree tree) {
		Objects.requireNonNull(compilation, "compilation");
		Objects.requireNonNull(tree, "tree");
		Set<Tree> treeSet = new HashSet<>(this.parser.parse(compilation, tree));

		for (Tree child : tree)
			treeSet.addAll(this.parse(compilation, child));

		return treeSet;
	}
}
