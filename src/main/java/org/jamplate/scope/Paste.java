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

import org.cufy.preprocessor.link.Logic;
import org.cufy.preprocessor.AbstractScope;
import org.jamplate.memory.ScopeMemory;

import java.io.IOException;
import java.util.Objects;

/**
 * A scope that evaluate a logic the append the result of it when invoked.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2020.09.20
 */
public class Paste extends AbstractScope {
	/**
	 * The logic to be evaluated and appended when this scope get invoked.
	 *
	 * @since 0.0.1 ~2020.09.20
	 */
	protected final Logic logic;

	/**
	 * Construct a new scope that appends the result of evaluating the given {@code logic}, when
	 * invoked.
	 *
	 * @param logic the logic to be of the constructed scope.
	 * @throws NullPointerException if the given {@code logic} is null.
	 * @since 0.0.1 ~2020.09.20
	 */
	public Paste(Logic logic) {
		Objects.requireNonNull(logic, "logic");
		this.logic = logic;
	}

	/**
	 * Return the {@link #logic} of this scope.
	 *
	 * @return the {@link #logic} of this scope.
	 * @since 0.0.1 ~2020.09.20
	 */
	public final Logic logic() {
		return this.logic;
	}

	@Override
	protected Appendable invoke(Appendable appendable, ScopeMemory memory) throws IOException {
		Objects.requireNonNull(appendable, "appendable");
		Objects.requireNonNull(memory, "memory");

		appendable = appendable.append(this.logic.evaluateString(memory));

		return super.invoke(appendable, memory);
	}

	@Override
	public String toString() {
		return "#PASTE " + this.logic;
	}
}
