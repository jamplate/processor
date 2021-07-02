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
package org.jamplate.internal.function.parser.merge;

import org.jamplate.function.Parser;
import org.jamplate.model.Dominance;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

/**
 * An {@link MergeByWeightParser} that does not allow any dominance between the results.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.20
 */
public class MergeFlatByWeightParser extends MergeByWeightParser {
	/**
	 * Construct a new parser that uses the given {@code parsers} preferring the results
	 * from the parsers at the start that the results from the parsers that at the end
	 * when a clash occur between their results.
	 * <br>
	 * Null parsers in the array will be ignored.
	 *
	 * @param parsers the parsers to be used by the constructed parser.
	 * @throws NullPointerException if the given {@code parsers} is null.
	 * @since 0.2.0 ~2021.05.18
	 */
	public MergeFlatByWeightParser(@Nullable Parser @NotNull ... parsers) {
		super(parsers);
	}

	/**
	 * Construct a new parser that uses the given {@code parsers} preferring the results
	 * from the parsers at the start that the results from the parsers that at the end
	 * when a clash occur between their results.
	 * <br>
	 * Null parsers in the list will be ignored.
	 *
	 * @param parsers the parsers to be used by the constructed parser.
	 * @throws NullPointerException if the given {@code parsers} is null.
	 * @since 0.2.0 ~2021.05.18
	 */
	public MergeFlatByWeightParser(@NotNull List<Parser> parsers) {
		super(parsers);
	}

	@Override
	protected boolean check(@NotNull Tree primary, @NotNull Tree secondary) {
		Objects.requireNonNull(primary, "primary");
		Objects.requireNonNull(secondary, "secondary");
		switch (Dominance.compute(primary, secondary)) {
			case NONE:
				//flat
				return true;
			case CONTAIN:
				//the secondary is a superior, primary live, secondary die
			case PART:
				//the primary is the superior, as usual :P
			case SHARE:
				//a clash!!
				return false;
			case EXACT:
				//takeover
				return primary.getWeight() != secondary.getWeight();
			default:
				//unexpected
				throw new InternalError();
		}
	}
}
