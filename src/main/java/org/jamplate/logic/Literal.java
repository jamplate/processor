/*
 *	Copyright 2020 Cufy
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
package org.jamplate.logic;

import org.cufy.preprocessor.invoke.Memory;
import org.cufy.preprocessor.AbstractParser;
import org.cufy.preprocessor.Poll;
import org.jamplate.util.Strings;
import org.jamplate.util.Values;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * A logic that evaluates to a literal value.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2020.09.16
 */
public class Literal extends AbstractValue {
	/**
	 * Targets {@code literal} statements.
	 * <ul>
	 *     <li>{@code OPEN} the open operator</li>
	 *     <li>{@code VALUE} the content between the open and close operators</li>
	 *     <li>{@code CLOSE} the close operator</li>
	 * </ul>
	 *
	 * @since 0.0.b ~2020.09.29
	 */
	public static final Pattern PATTERN = Pattern.compile("(?<OPEN>(?<!\\\\)[\"])(?<VALUE>(?:[^\"]|(?:\\\\\"))*)(?<CLOSE>(?<!\\\\)[\"])");

	/**
	 * Construct a new literal with its {@link #value} not initialized.
	 *
	 * @since 0.0.b ~2020.10.04
	 */
	public Literal() {
	}

	/**
	 * Construct a new logic that returns the given {@code value} when it gets evaluated.
	 *
	 * @param value the value this logic will evaluates.
	 * @throws NullPointerException if the given {@code value} is null.
	 * @since 0.0.1 ~2020.09.16
	 */
	public Literal(String value) {
		super(value);
	}

	@Override
	public String evaluate(Memory memory) {
		Objects.requireNonNull(memory, "scope");
		return Strings.descaped(this.value);
	}

	@Override
	public String toString() {
		return "\"" + this.value + "\"";
	}

	/**
	 * The default parser class of the {@link Literal} logic.
	 *
	 * @author LSafer
	 * @version 0.0.b
	 * @since 0.0.b ~2020.09.29
	 */
	public static class Parser extends AbstractParser.AbstractVote<Literal> {
		@Override
		public boolean parse(List poll) {
			return Poll.iterate(poll, Values.parse(
					Literal.PATTERN,
					Literal::new
			));
		}
	}
}
//			Polls.iterateMatches(
//					poll,
//					Literal.PATTERN,
//					Values.parse(Literal::new)
//			);
