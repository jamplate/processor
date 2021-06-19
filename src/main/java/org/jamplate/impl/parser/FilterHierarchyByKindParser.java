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

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A parser that searches in the hierarchy of the tree given to it (including the tree
 * itself) looking for trees with a pre-specified kind. Then, parses those trees and
 * returns the results.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.06.01
 */
public class FilterHierarchyByKindParser implements Parser {
	/**
	 * The kind this parser is looking for.
	 *
	 * @since 0.2.0 ~2021.06.01
	 */
	@NotNull
	protected final String kind;
	/**
	 * The wrapped parser.
	 *
	 * @since 0.2.0 ~2021.06.01
	 */
	@NotNull
	protected final Parser parser;

	/**
	 * Construct a new parser that parses the trees with the given {@code kind} in the
	 * tree given to it (including the tree itself) using the given {@code parser}.
	 *
	 * @param kind   the kind of the trees the constructed parser will look for.
	 * @param parser the parser to be used by the constructed parser.
	 * @throws NullPointerException if the given {@code kind} or {@code parser} is null.
	 * @since 0.2.0 ~2021.06.01
	 */
	public FilterHierarchyByKindParser(@NotNull String kind, @NotNull Parser parser) {
		Objects.requireNonNull(kind, "kind");
		Objects.requireNonNull(parser, "parser");
		this.kind = kind;
		this.parser = parser;
	}

	@NotNull
	@Override
	public Set<Tree> parse(@NotNull Compilation compilation, @NotNull Tree tree) {
		Objects.requireNonNull(compilation, "compilation");
		Objects.requireNonNull(tree, "tree");
		Set<Tree> results = new HashSet<>();

		if (tree.getSketch().getKind().equals(this.kind))
			results.addAll(this.parser.parse(compilation, tree));

		for (Tree child : tree)
			results.addAll(this.parse(compilation, child));

		return results;
	}
}
