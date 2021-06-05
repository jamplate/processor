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

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/**
 * A parser that parses using another parser and only parses trees with a kind equal to a
 * pre-specified kind.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.31
 */
public class FilterByKindParser implements Parser {
	/**
	 * The target kind.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	protected final String kind;
	/**
	 * The wrapped parser.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	protected final Parser parser;

	/**
	 * Construct a new parser that only parses trees that have the given {@code kind} and
	 * parses using the given {@code parser}.
	 *
	 * @param kind   the targeted kind.
	 * @param parser the wrapped parser.
	 * @throws NullPointerException if the given {@code kind} or {@code parser} is null.
	 * @since 0.2.0 ~2021.05.31
	 */
	public FilterByKindParser(@NotNull String kind, @NotNull Parser parser) {
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
		if (tree.getSketch().getKind().equals(this.kind))
			return this.parser.parse(compilation, tree);

		return Collections.emptySet();
	}
}
