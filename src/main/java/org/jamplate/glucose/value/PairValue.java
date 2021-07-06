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
package org.jamplate.glucose.value;

import org.jamplate.memory.Memory;
import org.jamplate.memory.Pipe;
import org.jamplate.memory.Value;
import org.jetbrains.annotations.NotNull;

import java.util.Map.Entry;
import java.util.Objects;

/**
 * A value that evaluates to an object pair and can be evaluates to a raw pair entry.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.30
 */
@SuppressWarnings("UnqualifiedInnerClassAccess")
public final class PairValue implements Value<Entry<Value, Value>> {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 2938159775955369455L;

	/**
	 * The evaluating function that evaluates to the raw value of this value.
	 *
	 * @since 0.3.0 ~2021.06.29
	 */
	@NotNull
	private final Pipe<Object, Entry<Value, Value>> pipe;

	/**
	 * An internal constructor to construct a new pair value with the given {@code
	 * function}.
	 *
	 * @param pipe the function that evaluates to the pair of the constructed pair value.
	 * @throws NullPointerException if the given {@code function} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	public PairValue(@NotNull Pipe<Object, Entry<Value, Value>> pipe) {
		Objects.requireNonNull(pipe, "pipe");
		this.pipe = pipe;
	}

	@NotNull
	@Override
	public PairValue apply(@NotNull Pipe<Entry<Value, Value>, Entry<Value, Value>> pipe) {
		Objects.requireNonNull(pipe, "pipe");
		return new PairValue(this.pipe.apply(pipe));
	}

	@NotNull
	@Override
	public String evaluate(@NotNull Memory memory) {
		Objects.requireNonNull(memory, "memory");
		Entry<Value, Value> pair = this.pipe.eval(memory);
		return pair.getKey().evaluate(memory) +
			   ":" +
			   pair.getValue().evaluate(memory);
	}

	@NotNull
	@Override
	public Pipe<Object, Entry<Value, Value>> getPipe() {
		return this.pipe;
	}

	@NotNull
	@Override
	public String toString() {
		return "Pair:" + Integer.toHexString(this.hashCode());
	}
}
