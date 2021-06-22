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

import org.jamplate.internal.util.Trees;
import org.jamplate.model.Compilation;
import org.jamplate.model.Tree;
import org.jamplate.function.Parser;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

/**
 * A parser that parses using another parser and combines the results into one tree then
 * return it.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.31
 */
@Deprecated
public class OfferParser implements Parser {
	/**
	 * The wrapped parser.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	protected final Parser parser;

	/**
	 * Construct a new parser that parses using the given {@code parser} then offer the
	 * results into one tree and return it.
	 *
	 * @param parser the wrapped parser.
	 * @throws NullPointerException if the given {@code parser} is null.
	 * @since 0.2.0 ~2021.05.31
	 */
	public OfferParser(@NotNull Parser parser) {
		Objects.requireNonNull(parser, "parser");
		this.parser = parser;
	}

	@NotNull
	@Override
	public Set<Tree> parse(@NotNull Compilation compilation, @NotNull Tree tree) {
		Set<Tree> treeSet = this.parser.parse(compilation, tree);
		Iterator<Tree> iterator = treeSet.iterator();

		if (iterator.hasNext()) {
			Tree result = iterator.next();

			iterator.forEachRemaining(t ->
					Trees.collect(t)
						 .forEach(result::offer)
			);

			return Collections.singleton(Trees.root(result));
		}

		return Collections.emptySet();
	}
}
