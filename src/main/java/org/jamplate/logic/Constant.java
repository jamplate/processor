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
 * A logic that evaluates to a constant value.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2020.09.16
 */
public class Constant implements Logic {
	/**
	 * The value this logic will evaluate.
	 *
	 * @since 0.0.1 ~2020.09.16
	 */
	protected final String value;

	/**
	 * Construct a new logic that returns the given {@code text} when it gets evaluated.
	 *
	 * @param text the text this logic will evaluates.
	 * @throws NullPointerException if the given {@code text} is null.
	 * @since 0.0.1 ~2020.09.16
	 */
	public Constant(String text) {
		Objects.requireNonNull(text, "text");
		this.value = text;
	}

	/**
	 * Get the {@link #value} of this.
	 *
	 * @return the {@link #value} of this.
	 * @since 0.0.1 ~2020.09.19
	 */
	public final String value() {
		return this.value;
	}

	@Override
	public String evaluate(Memory memory) {
		Objects.requireNonNull(memory, "scope");
		return this.value;
	}

	@Override
	public String toString() {
		return "\"" + this.value + "\"";
	}
}
