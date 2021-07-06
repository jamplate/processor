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

import java.util.Objects;

/**
 * A value that evaluates to a boolean and can be evaluated to a raw state.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.30
 */
public final class BooleanValue implements Value<Boolean> {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -1562079784562666632L;

	/**
	 * The evaluating function that evaluates to the raw value of this value.
	 *
	 * @since 0.3.0 ~2021.07.01
	 */
	@NotNull
	private final Pipe<Object, Boolean> pipe;

	/**
	 * An internal constructor to construct a new boolean value with the given {@code
	 * pipe}.
	 *
	 * @param pipe the pipe that evaluates to the state of the constructed boolean value.
	 * @throws NullPointerException if the given {@code pipe} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	public BooleanValue(@NotNull Pipe<Object, Boolean> pipe) {
		Objects.requireNonNull(pipe, "pipe");
		this.pipe = pipe;
	}

	@NotNull
	@Override
	public BooleanValue apply(@NotNull Pipe<Boolean, Boolean> pipe) {
		Objects.requireNonNull(pipe, "pipe");
		return new BooleanValue(this.pipe.apply(pipe));
	}

	@NotNull
	@Override
	public String eval(@NotNull Memory memory) {
		Objects.requireNonNull(memory, "memory");
		return this.pipe.eval(memory) ?
			   "true" :
			   "false";
	}

	@NotNull
	@Override
	public Pipe<Object, Boolean> getPipe() {
		return this.pipe;
	}

	@NotNull
	@Override
	public String toString() {
		return "Boolean:" + Integer.toHexString(this.hashCode());
	}
}
