/*
 *	Copyright 2021 Cufy
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
package org.jamplate.impl.util;

import org.jamplate.model.Memory;
import org.jamplate.model.Value;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Utilities for memories.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.25
 */
public final class Memories {
	/**
	 * Utility classes must not be initialized.
	 *
	 * @throws AssertionError when called.
	 * @since 0.2.0 ~2021.05.25
	 */
	private Memories() {
		throw new AssertionError("No instance for you");
	}

	/**
	 * Join the values from popping the given {@code memory} until reaching the first
	 * {@link Value#NULL} (aka the start of the memory) on the current frame of it.
	 * <br>
	 * Note: this method will pop the values but not the frame.
	 *
	 * @param memory the memory to join-pop its values
	 * @return a value from joining all the values in the current frame of the given
	 *        {@code memory} after popped them.
	 * @throws NullPointerException if the given {@code memory} is null.
	 * @since 0.2.0 ~2021.05.25
	 */
	public static Value joinPop(@NotNull Memory memory) {
		Objects.requireNonNull(memory, "memory");
		Value value = memory.pop();
		while (true) {
			Value next = memory.pop();

			if (next == Value.NULL)
				return value;

			Value prev = value;
			value = m ->
					prev.evaluate(m) +
					next.evaluate(m);
		}
	}
}
