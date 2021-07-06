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

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A value from gluing multiple other values.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.07.01
 */
public final class VGlue implements Value<List<Value>> {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 8228913560756371483L;

	/**
	 * The evaluating function that evaluates to the raw value of this value.
	 *
	 * @since 0.3.0 ~2021.07.01
	 */
	@NotNull
	private final Pipe<Object, List<Value>> pipe;

	/**
	 * An internal constructor to construct a new glued value with the given {@code
	 * function}.
	 *
	 * @param pipe the function that evaluates to the values of the constructed glued
	 *             value.
	 * @throws NullPointerException if the given {@code function} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	public VGlue(@NotNull Pipe<Object, List<Value>> pipe) {
		Objects.requireNonNull(pipe, "pipe");
		this.pipe = pipe;
	}

	@NotNull
	@Override
	public Value<List<Value>> apply(@NotNull Pipe<List<Value>, List<Value>> pipe) {
		Objects.requireNonNull(pipe, "pipe");
		return new VGlue(this.pipe.apply(pipe));
	}

	@NotNull
	@Override
	public String eval(@NotNull Memory memory) {
		Objects.requireNonNull(memory, "memory");
		return this.pipe
				.eval(memory)
				.stream()
				.map(value -> value.eval(memory))
				.collect(Collectors.joining());
	}

	@NotNull
	@Override
	public Pipe<Object, List<Value>> getPipe() {
		return this.pipe;
	}

	@NotNull
	@Override
	public String toString() {
		return "Glued:" + Integer.toHexString(this.hashCode());
	}
}
