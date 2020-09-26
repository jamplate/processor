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
 * A logic evaluates to {@code true} if both two logics evaluates to {@code true}.
 *
 * @author LSafer
 * @version 0.0.6
 * @since 0.0.4 ~2020.09.22
 */
public class And implements Logic {
	/**
	 * The logic in the left.
	 *
	 * @since 0.0.4 ~2020.09.22
	 */
	protected final Logic left;
	/**
	 * The logic in the right.
	 *
	 * @since 0.0.4 ~2020.09.22
	 */
	protected final Logic right;

	/**
	 * Construct a new {@code and} statement.
	 *
	 * @param left  the left logic in the {@code and} statement.
	 * @param right the right logic in the {@code and} statement.
	 * @throws NullPointerException if the given {@code left} or {@code right} is null.
	 * @since 0.0.4 ~2020.09.22
	 */
	public And(Logic left, Logic right) {
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
		return this.left.evaluateBoolean(memory) &&
			   this.right.evaluateBoolean(memory) ?
			   "true" :
			   "false";
	}

	@Override
	public String toString() {
		return this.left + " & " + this.right;
	}
}
