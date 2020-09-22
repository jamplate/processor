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
package org.jamplate.scope;

import org.jamplate.logic.Logic;
import org.jamplate.memory.ScopeMemory;

import java.io.IOException;
import java.util.Collections;
import java.util.Objects;

/**
 * A scope that appends tabs.
 *
 * @author LSafer
 * @version 0.0.8
 * @since 0.0.8 ~2020.09.22
 */
public class Tab extends AbstractScope {
	/**
	 * The logic evaluates how many lines to be appended.
	 *
	 * @since 0.0.8 ~2020.09.22
	 */
	protected final Logic logic;

	/**
	 * Construct a new scope that appends tabs {@code logic} many times.
	 *
	 * @param logic the logic evaluates how many tabs to be appended.
	 * @throws NullPointerException if the given {@code logic} is null.
	 * @since 0.0.8 ~2020.09.22
	 */
	public Tab(Logic logic) {
		Objects.requireNonNull(logic, "logic");
		this.logic = logic;
	}

	@Override
	protected Appendable invoke(Appendable appendable, ScopeMemory memory) throws IOException {
		Objects.requireNonNull(appendable, "appendable");
		Objects.requireNonNull(memory, "memory");

		appendable = appendable.append(String.join(
				"",
				Collections.nCopies(
						this.logic.evaluateNumber(memory).intValue() + 1,
						"\t"
				)
		));

		return super.invoke(appendable, memory);
	}
}
