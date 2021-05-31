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

import org.jamplate.model.Dominance;
import org.jamplate.model.Tree;
import org.jamplate.model.function.Parser;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * A merging parser that does not allow two trees having any dominance between each other.
 * The first dominant tree always wins.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.19
 */
public class FlatMergeParser extends MergeParser {
	/**
	 * Construct a new parser that filters the results to have no dominance between
	 * etcher.
	 *
	 * @param parser the parser to be wrapped by the constructed parser.
	 * @throws NullPointerException if the given {@code parser} is null.
	 * @since 0.2.0 ~2021.05.20
	 */
	public FlatMergeParser(@NotNull Parser parser) {
		super(parser);
	}

	@Override
	protected boolean check(@NotNull Tree previous, @NotNull Tree next) {
		Objects.requireNonNull(previous, "previous");
		Objects.requireNonNull(next, "next");
		switch (Dominance.compute(previous, next)) {
			case NONE:
				//flat
			case CONTAIN:
				//the next is the superior
				return true;
			case PART:
				//the previous is the superior
			case SHARE:
				//a clash!!
				return false;
			case EXACT:
				//takeover
				return previous.getZIndex() != next.getZIndex();
			default:
				//unexpected
				throw new InternalError();
		}
	}
}
