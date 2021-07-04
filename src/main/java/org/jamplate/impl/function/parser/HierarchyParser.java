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

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A parser that attempt to parse every tree in the hierarchy of the tree provided to it
 * (including the tree itself) and parses using another parser.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.31
 */
public class HierarchyParser implements Parser {
	/**
	 * The wrapped parser.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	protected final Parser parser;

	/**
	 * Construct a new hierarchy parser that uses the given {@code parser}.
	 *
	 * @param parser the wrapped parser.
	 * @throws NullPointerException if the given {@code parser} is null.
	 * @since 0.2.0 ~2021.05.31
	 */
	public HierarchyParser(@NotNull Parser parser) {
		Objects.requireNonNull(parser, "parser");
		this.parser = parser;
	}

	/**
	 * Construct a new parser that searches the whole hierarchy of the trees given to it
	 * and parses using the given {@code parser}.
	 *
	 * @param parser the parser to be used.
	 * @return a hierarchy parser that uses the given {@code parser}.
	 * @throws NullPointerException if the given {@code parser} is null.
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static HierarchyParser hierarchy(@NotNull Parser parser) {
		return new HierarchyParser(parser);
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
