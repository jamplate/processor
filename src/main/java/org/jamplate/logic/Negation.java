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
 * A logic evaluates to the inverse of the result from evaluating another logic.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2020.09.17
 */
public class Negation implements Logic {
	/**
	 * The logic this logic is the inverse of.
	 *
	 * @since 0.0.1 ~2020.09.17
	 */
	protected final Logic logic;

	/**
	 * Construct a logic that evaluates to the inverse of the given {@code logic}.
	 *
	 * @param logic the logic the constructed logic is inverting.
	 * @throws NullPointerException if the given {@code logic} is null.
	 * @since 0.0.1 ~2020.09.17
	 */
	public Negation(Logic logic) {
		Objects.requireNonNull(logic, "logic");
		this.logic = logic;
	}

	/**
	 * Get the {@link #logic} of this.
	 *
	 * @return the {@link #logic} of this.
	 * @since 0.0.1 ~2020.09.19
	 */
	public final Logic logic() {
		return this.logic;
	}

	@Override
	public String evaluate(Memory memory) {
		Objects.requireNonNull(memory, "scope");
		boolean logic = this.logic.evaluateBoolean(memory);
		return logic ? "false" : "true";
	}
}
