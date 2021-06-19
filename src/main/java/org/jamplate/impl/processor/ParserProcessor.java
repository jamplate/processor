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
package org.jamplate.impl.processor;

import org.jamplate.model.Compilation;
import org.jamplate.model.Tree;
import org.jamplate.function.Parser;
import org.jamplate.function.Processor;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Set;

/**
 * A processor that parses using a predefined parser.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.18
 */
public class ParserProcessor implements Processor {
	/**
	 * The parser of this processor.
	 *
	 * @since 0.2.0 ~2021.05.18
	 */
	@NotNull
	protected final Parser parser;

	/**
	 * Construct a new processor that parses using the given {@code parser}.
	 *
	 * @param parser the parser to be used by the constructed processor.
	 * @throws NullPointerException if the given {@code parser} is null.
	 * @since 0.2.0 ~2021.05.18
	 */
	public ParserProcessor(@NotNull Parser parser) {
		Objects.requireNonNull(parser, "parser");
		this.parser = parser;
	}

	@Override
	public boolean process(@NotNull Compilation compilation) {
		Objects.requireNonNull(compilation, "compilation");
		Tree root = compilation.getRootTree();

		boolean modified = false;
		while (true) {
			Set<Tree> treeSet = this.parser.parse(compilation, root);

			if (treeSet.isEmpty())
				return modified;

			for (Tree tree : treeSet)
				root.offer(tree);

			modified = true;
		}
	}
}
