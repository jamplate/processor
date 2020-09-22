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
 * A scope that joins two logics.
 *
 * @author LSafer
 * @version 0.0.6
 * @since 0.0.6 ~2020.09.22
 */
public class Addition implements Logic {
	/**
	 * The logic at the left.
	 *
	 * @since 0.0.6 ~2020.09.22
	 */
	protected final Logic left;
	/**
	 * The logic at the right.
	 *
	 * @since 0.0.6 ~2020.09.22
	 */
	protected final Logic right;

	/**
	 * Construct a new logic that joins two logics.
	 *
	 * @param left  the logic at the left.
	 * @param right the logic at the right.
	 * @since 0.0.6 ~2020.09.22
	 */
	public Addition(Logic left, Logic right) {
		Objects.requireNonNull(left, "left");
		Objects.requireNonNull(right, "right");
		this.left = left;
		this.right = right;
	}

	/**
	 * Get the {@link #left} logic of this.
	 *
	 * @return the {@link #left} logic of this.
	 * @since 0.0.6 ~2020.09.22
	 */
	public final Logic left() {
		return this.left;
	}

	/**
	 * Get the {@link #right} logic of this.
	 *
	 * @return the {@link #right} logic of this.
	 * @since 0.0.6 ~2020.09.22
	 */
	public final Logic right() {
		return this.right;
	}

	@Override
	public String evaluate(Memory memory) {
		Objects.requireNonNull(memory, "memory");
		String left = this.left.evaluate(memory);
		String right = this.right.evaluate(memory);

		//double join
		if (left.matches("^\\d*[.]\\d*$") &&
			right.matches("^\\d*[.]\\d*$"))
			return String.valueOf(
					Double.parseDouble(left) +
					Double.parseDouble(right)
			);
		else if (left.matches("^\\d*$") &&
				 right.matches("^\\d*$"))
			return String.valueOf(
					Long.parseLong(left) +
					Long.parseLong(right)
			);

		return left + right;
	}
}
