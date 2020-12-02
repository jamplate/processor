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
 * A logic that returns 'true' or 'false' depending on the equality between other two logics.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2020.09.16
 */
public class Equation extends AbstractOperator {
	/**
	 * Targets {@code equals} statements.
	 * <ul>
	 *     <li>{@code OPERATOR} the operator detected</li>
	 * </ul>
	 *
	 * @since 0.0.b ~2020.10.03
	 */
	public static final Pattern PATTERN = Pattern.compile("(?<OPERATOR>[!=][=][=]?)");

	/**
	 * True, means this is a {@code not equal} logic.
	 *
	 * @since 0.0.b ~2020.10.03
	 */
	protected final boolean negated;

	/**
	 * Construct a new equation operator with its {@link #left} and {@link #right} not initialized.
	 *
	 * @since 0.0.b ~2020.10.02
	 */
	public Equation() {
		this.negated = false;
	}

	/**
	 * Construct a new equation operator with its {@link #left} and {@link #right} not initialized.
	 *
	 * @param negated true, means the constructed equation will be a {@code not equal} logic.
	 * @since 0.0.b ~2020.10.02
	 */
	public Equation(boolean negated) {
		this.negated = negated;
	}

	/**
	 * Construct a new logic that evaluates 'true' if the given {@code left} evaluate to a value
	 * that equals to the value evaluated from the given {@code right}. Otherwise, evaluates to
	 * 'false'.
	 *
	 * @param left  the left logic.
	 * @param right the right logic.
	 * @throws NullPointerException if the given {@code left} or {@code right} is null.
	 * @since 0.0.1 ~2020.09.13
	 */
	public Equation(Logic left, Logic right) {
		super(left, right);
		this.negated = false;
	}

	/**
	 * Construct a new logic that evaluates 'true' if the given {@code left} evaluate to a value
	 * that equals to the value evaluated from the given {@code right}. Otherwise, evaluates to
	 * 'false'.
	 *
	 * @param left    the left logic.
	 * @param right   the right logic.
	 * @param negated true, means the constructed equation will be a {@code not equal} logic.
	 * @throws NullPointerException if the given {@code left} or {@code right} is null.
	 * @since 0.0.1 ~2020.09.13
	 */
	public Equation(Logic left, Logic right, boolean negated) {
		super(left, right);
		this.negated = negated;
	}

	@Override
	public Object evaluate(Memory memory) {
		Objects.requireNonNull(memory, "scope");
		return Logic.equals(memory, this.left, this.right) != this.negated;
	}

	@Override
	public String toString() {
		return this.left + (this.negated ? " != " : " == ") + this.right;
	}

	/**
	 * Determine if this equation is negated or not.
	 *
	 * @return true, if this equation is negated.
	 * @since 0.0.b ~2020.10.04
	 */
	public boolean isNegated() {
		return this.negated;
	}

	/**
	 * The default parser class of the {@link Equation} logic.
	 *
	 * @author LSafer
	 * @version 0.0.b
	 * @since 0.0.b ~2020.10.03
	 */
	public static class Parser extends AbstractParser.AbstractVote<Equation> {
		@Override
		public boolean link(List poll) {
			return Poll.iterate(poll, Operators.link(Equation.class));
		}

		@Override
		public boolean parse(List poll) {
			return Poll.iterate(poll, Operators.parse(
					Equation.PATTERN,
					operator -> {
						switch (operator) {
							case "==":
								return new Equation(false);
							case "!=":
								return new Equation(true);
							case "===":
							case "!==":
								throw new ParseException("Unsupported operator: " + operator);
							default:
								throw new InternalError("Unknown operator: " + operator);
						}
					}
			));
		}
	}
}
