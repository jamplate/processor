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
 * A logic that can evaluate to a {@code string}.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2020.09.17
 */
public interface Logic {
	/**
	 * Determine if the given {@code left} is equals to the given {@code right}.
	 *
	 * @param memory the memory where to check the equality of the two logics.
	 * @param left   the logic in the left.
	 * @param right  the logic in the right.
	 * @return true, if the given {@code left} is equals the given {@code right}.
	 * @throws NullPointerException if the given {@code memory} is null.
	 * @since 0.0.1 ~2020.09.17
	 */
	static boolean equals(Memory memory, Logic left, Logic right) {
		Objects.requireNonNull(memory, "memory");

		if (left == right)
			return true;
		if (left == null || right == null)
			return false;

		return Objects.equals(
				left.evaluate(memory),
				right.evaluate(memory)
		);
	}

	/**
	 * Evaluate this logic then parse it as a {@link Boolean}.
	 *
	 * @param memory the memory where this logic should be evaluated.
	 * @return true, if this logic evaluated to true.
	 * @throws NullPointerException if the given {@code memory} is null.
	 * @since 0.0.1 ~2020.09.17
	 */
	default boolean evaluateBoolean(Memory memory) {
		Objects.requireNonNull(memory, "memory");

		String value = this.evaluate(memory);

		switch (value) {
			case "":
			case "0":
			case "false":
				return false;
			default:
				return true;
		}
	}

	/**
	 * Evaluate this logic then parse it as a {@link Number}.
	 *
	 * @param memory the memory where this logic should be evaluated.
	 * @return the result of evaluating this logic parsed as a {@link Number}.
	 * @throws NullPointerException if the given {@code memory} is null.
	 * @since 0.0.1 ~2020.09.17
	 */
	default Number evaluateNumber(Memory memory) {
		Objects.requireNonNull(memory, "memory");

		String value = this.evaluate(memory);

		try {
			if (value.contains("."))
				return Double.parseDouble(value);
			else
				return Long.parseLong(value);
		} catch (NumberFormatException ignored) {
			return 0;
		}
	}

	/**
	 * Evaluate this logic.
	 *
	 * @param memory the memory where this logic should be evaluated.
	 * @return the result of evaluating this logic.
	 * @throws NullPointerException if the given {@code memory} is null.
	 * @since 0.0.1 ~2020.09.17
	 */
	String evaluate(Memory memory);
}
