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

import org.cufy.preprocessor.link.Logic;
import org.cufy.preprocessor.invoke.Memory;
import org.cufy.preprocessor.AbstractParser;
import org.jamplate.util.Modifiers;
import org.cufy.preprocessor.Poll;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * A logic evaluates to the inverse of the result from evaluating another logic.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2020.09.17
 */
public class Negation extends AbstractModifier {
	/**
	 * A pattern that targets negation statements.
	 * <ul>
	 *     <li>{@code OPERATOR} the operator used on the negation.</li>
	 * </ul>
	 *
	 * @since 0.0.b ~2020.10.01
	 */
	public static final Pattern PATTERN = Pattern.compile("(?<MODIFIER>[!])");

	/**
	 * Construct a new negation modifier logic with its {@link #logic} not initialized.
	 *
	 * @since 0.0.b ~2020.10.02
	 */
	public Negation() {
	}

	/**
	 * Construct a logic that evaluates to the inverse of the given {@code logic}.
	 *
	 * @param logic the logic the constructed logic is inverting.
	 * @throws NullPointerException if the given {@code logic} is null.
	 * @since 0.0.1 ~2020.09.17
	 */
	public Negation(Logic logic) {
		super(logic);
	}

	@Override
	public Object evaluate(Memory memory) {
		Objects.requireNonNull(memory, "scope");
		return !this.logic.evaluateBoolean(memory);
	}

	@Override
	public String toString() {
		return "!" + this.logic;
	}

	/**
	 * The default parser class of the {@link Negation} logic.
	 *
	 * @author LSafer
	 * @version 0.0.b
	 * @since 0.0.b ~2020.10.01
	 */
	public static class Parser extends AbstractParser.AbstractVote<Negation> {
		@Override
		public boolean link(List poll) {
			return Poll.iterate(poll, Modifiers.link(Negation.class));
		}

		@Override
		public boolean parse(List poll) {
			return Poll.iterate(poll, Modifiers.parse(
					Negation.PATTERN,
					modifier -> {
						switch (modifier) {
							case "!":
								return new Negation();
							default:
								throw new InternalError("Unknown modifier: " + modifier);
						}
					}
			));
		}
	}
}
//			Polls.iterateMatches(
//					poll,
//					Negation.PATTERN,
//					Modifiers.parse(modifier -> {
//						switch (modifier) {
//							case "!":
//								return new Negation();
//							default:
//								throw new InternalError("Unknown modifier: " + modifier);
//						}
//					})
//			);
