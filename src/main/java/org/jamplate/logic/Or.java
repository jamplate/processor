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

import org.cufy.preprocessor.ParseException;
import org.cufy.preprocessor.link.Logic;
import org.cufy.preprocessor.invoke.Memory;
import org.cufy.preprocessor.AbstractParser;
import org.jamplate.util.Operators;
import org.cufy.preprocessor.Poll;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * A logic evaluates to {@code true} if ether two logics evaluates to {@code true}.
 *
 * @author LSafer
 * @version 0.0.6
 * @since 0.0.4 ~2020.09.22
 */
public class Or extends AbstractOperator {
	/**
	 * Targets {@code or} statements.
	 * <ul>
	 *     <li>{@code OPERATOR} the operator detected</li>
	 * </ul>
	 *
	 * @since 0.0.b ~2020.10.03
	 */
	public static final Pattern PATTERN = Pattern.compile("(?<OPERATOR>[|][|]?)");

	/**
	 * Construct a new or operator with its {@link #left} and {@link #right} not initialized.
	 *
	 * @since 0.0.b ~2020.10.02
	 */
	public Or() {
	}

	/**
	 * Construct a new {@code or} statement.
	 *
	 * @param left  the left logic in the {@code or} statement.
	 * @param right the right logic in the {@code or} statement.
	 * @throws NullPointerException if the given {@code left} or {@code right} is null.
	 * @since 0.0.4 ~2020.09.22
	 */
	public Or(Logic left, Logic right) {
		super(left, right);
	}

	@Override
	public Object evaluate(Memory memory) {
		Objects.requireNonNull(memory, "memory");
		return this.left.evaluateBoolean(memory) |
			   this.right.evaluateBoolean(memory);
	}

	@Override
	public String toString() {
		return this.left + " | " + this.right;
	}

	/**
	 * The default parser class of the {@link Or} logic.
	 *
	 * @author LSafer
	 * @version 0.0.b
	 * @since 0.0.b ~2020.10.03
	 */
	public static class Parser extends AbstractParser.AbstractVote<Or> {
		@Override
		public boolean link(List poll) {
			return Poll.iterate(poll, Operators.link(Or.class));
		}

		@Override
		public boolean parse(List poll) {
			return Poll.iterate(poll, Operators.parse(
					Or.PATTERN,
					operator -> {
						switch (operator) {
							case "|":
								return new Or();
							case "||":
								throw new ParseException("Unsupported operator: " + operator);
							default:
								throw new InternalError("Unknown operator: " + operator);
						}
					}
			));
		}
	}
}
