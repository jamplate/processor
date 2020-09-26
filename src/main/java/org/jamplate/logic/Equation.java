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

import org.jamplate.memory.Memory;

import java.util.Objects;

/**
 * A logic that returns 'true' or 'false' depending on the equality between other two logics.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2020.09.16
 */
public class Equation implements Logic {
	/**
	 * The logic at the left.
	 *
	 * @since 0.0.1 ~2020.09.16
	 */
	protected final Logic left;
	/**
	 * The logic at the right.
	 *
	 * @since 0.0.1 ~2020.09.16
	 */
	protected final Logic right;

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
		Objects.requireNonNull(left, "left");
		Objects.requireNonNull(right, "right");
		this.left = left;
		this.right = right;
	}

	/**
	 * Get the {@link #left} logic of this.
	 *
	 * @return the {@link #left} logic of this.
	 * @since 0.0.1 ~2020.09.19
	 */
	public final Logic left() {
		return this.left;
	}

	/**
	 * Get the {@link #right} logic of this.
	 *
	 * @return the {@link #right} logic of this.
	 * @since 0.0.1 ~2020.09.19
	 */
	public final Logic right() {
		return this.right;
	}

	@Override
	public String evaluate(Memory memory) {
		Objects.requireNonNull(memory, "scope");
		boolean logic = Logic.equals(memory, this.left, this.right);
		return logic ? "true" : "false";
	}

	@Override
	public String toString() {
		return this.left + " == " + this.right;
	}
}
