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
 * A logic that evaluates different logic depending on the results of another logic.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2020.09.17
 */
@Deprecated
public class Switch implements Logic {
	/**
	 * The candidates to be compared to the {@link #logic}.
	 *
	 * @since 0.0.1 ~2020.09.17
	 */
	protected final Logic[] candidates;
	/**
	 * The logic to switch depending on.
	 *
	 * @since 0.0.1 ~2020.09.17
	 */
	protected final Logic logic;

	/**
	 * Construct a new logic that compares the given {@code logic} with each logic with an even
	 * index in the given {@code candidates} array. Then return the logic next to the logic that
	 * equals the given {@code logic}. If no logic is equals to the given {@code logic} or no logic
	 * next to the first logic that equals to the given {@code logic}, then this logic will evaluate
	 * to an empty string.
	 *
	 * @param logic      the logic to compared.
	 * @param candidates the candidates to be compared to the given {@code logic} shifted with each
	 *                   candidate's value.
	 * @throws NullPointerException if the given {@code logic} or {@code candidates} is null.
	 * @since 0.0.1 ~2020.09.17
	 */
	public Switch(Logic logic, Logic... candidates) {
		Objects.requireNonNull(logic, "logic");
		Objects.requireNonNull(candidates, "candidates");
		this.logic = logic;
		this.candidates = candidates;
	}

	@Override
	public String evaluate(Memory memory) {
		Objects.requireNonNull(memory, "scope");
		for (int i = 0; i < this.candidates.length; i += 2)
			if (Logic.equals(memory, this.logic, this.candidates[i])) {
				if (this.candidates.length > i + 1) {
					Logic logic = this.candidates[i + 1];

					if (logic != null)
						return logic.evaluate(memory);
				}

				break;
			}

		return "";
	}
}
