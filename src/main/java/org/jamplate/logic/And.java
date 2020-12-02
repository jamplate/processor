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
 * A logic evaluates to {@code true} if both two logics evaluates to {@code true}.
 *
 * @author LSafer
 * @version 0.0.6
 * @since 0.0.4 ~2020.09.22
 */
public class And extends AbstractOperator {
	/**
	 * Targets {@code and} statements.
	 * <ul>
	 *     <li>{@code OPERATOR} the operator detected</li>
	 * </ul>
	 *
	 * @since 0.0.b ~2020.09.29
	 */
	public static final Pattern PATTERN = Pattern.compile("(?<OPERATOR>[&][&]?)");

	/**
	 * Construct a new and operator with its {@link #left} and {@link #right} not initialized.
	 *
	 * @since 0.0.b ~2020.10.02
	 */
	public And() {

	}

	/**
	 * Construct a new {@code and} statement.
	 *
	 * @param left  the left logic in the {@code and} statement.
	 * @param right the right logic in the {@code and} statement.
	 * @throws NullPointerException if the given {@code left} or {@code right} is null.
	 * @since 0.0.4 ~2020.09.22
	 */
	public And(Logic left, Logic right) {
		super(left, right);
	}

	@Override
	public Object evaluate(Memory memory) {
		Objects.requireNonNull(memory, "memory");
		return this.left.evaluateBoolean(memory) &
			   this.right.evaluateBoolean(memory);
	}

	@Override
	public String toString() {
		return this.left + " & " + this.right;
	}

	/**
	 * The default parser class of the {@link And} logic.
	 *
	 * @author LSafer
	 * @version 0.0.b
	 * @since 0.0.b ~2020.09.29
	 */
	public static class Parser extends AbstractParser.AbstractVote<And> {
		@Override
		public boolean link(List poll) {
			return Poll.iterate(poll, Operators.link(And.class));
		}

		@Override
		public boolean parse(List poll) {
			return Poll.iterate(poll, Operators.parse(
					And.PATTERN,
					operator -> {
						switch (operator) {
							case "&":
								return new And();
							case "&&":
								throw new ParseException("Unsupported operator: " + operator);
							default:
								throw new InternalError("Unknown operator: " + operator);
						}
					}
			));
		}
	}
}
