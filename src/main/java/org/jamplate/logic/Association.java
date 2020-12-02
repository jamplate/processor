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
import org.jamplate.util.Operators;
import org.cufy.preprocessor.Poll;

import java.util.AbstractMap;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * A logic that holds a logic mapped to another logic.
 *
 * @author LSafer
 * @version 0.0.b
 * @since 0.0.b ~2020.10.07
 */
public class Association extends AbstractOperator {
	/**
	 * Targets {@code association} statements.
	 * <ul>
	 *     <li>{@code OPERATOR} the operator detected</li>
	 * </ul>
	 *
	 * @since 0.0.b ~2020.10.07
	 */
	public static final Pattern PATTERN = Pattern.compile("(?<OPERATOR>[:])");

	/**
	 * Construct a new association with its {@link #left} and {@link #right} not initialized.
	 *
	 * @since 0.0.b ~2020.10.07
	 */
	public Association() {
	}

	/**
	 * Construct a new association with its {@link #left} initialized to the given {@code left} and
	 * its {@link #right} initialized to the given {@code right}.
	 *
	 * @param left  the logic at the left of the constructed association.
	 * @param right the logic at the right of the constructed association.
	 * @throws NullPointerException if the given {@code left} or {@code right} is null.
	 * @since 0.0.b ~2020.10.07
	 */
	public Association(Logic left, Logic right) {
		super(left, right);
	}

	@Override
	public Object evaluate(Memory memory) {
		Objects.requireNonNull(memory, "memory");
		return new AbstractMap.SimpleImmutableEntry(
				this.left.evaluate(memory),
				this.right.evaluate(memory)
		);
	}

	@Override
	public String evaluateString(Memory memory) {
		return this.left.evaluateString(memory) + ":" +
			   this.right.evaluateString(memory);
	}

	@Override
	public String toString() {
		return this.left + ":" + this.right;
	}

	/**
	 * The default parser class of the {@link Association} logic.
	 *
	 * @author LSafer
	 * @version 0.0.b
	 * @since 0.0.b ~2020.10.07
	 */
	public static class Parser extends AbstractParser.AbstractVote<Association> {
		@Override
		public boolean link(List poll, org.cufy.preprocessor.Parser<? super Association> parser) {
			return Poll.iterate(poll, Operators.link(Association.class));
		}

		@Override
		public boolean parse(List poll) {
			return Poll.iterate(poll, Operators.parse(
					Association.PATTERN,
					operator -> {
						switch (operator) {
							case ":":
								return new Association();
							default:
								throw new InternalError("Unknown operator: " + operator);
						}
					}
			));
		}
	}
}
