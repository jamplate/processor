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
 * A value that evaluate to an object and can be evaluated to a raw list of pair values.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.29
 */
public final class VObject implements Value<List<VPair>> {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -5610810574617562364L;

	/**
	 * The evaluating function that evaluates to the raw value of this value.
	 *
	 * @since 0.3.0 ~2021.07.01
	 */
	@NotNull
	private final Pipe<Object, List<VPair>> pipe;

	/**
	 * An internal constructor to construct a new object value with the given {@code
	 * function}.
	 *
	 * @param pipe the function that evaluates to the pairs of the constructed object
	 *             value.
	 * @throws NullPointerException if the given {@code function} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	public VObject(@NotNull Pipe<Object, List<VPair>> pipe) {
		Objects.requireNonNull(pipe, "pipe");
		this.pipe = pipe;
	}

	@NotNull
	@Override
	public VObject apply(@NotNull Pipe<List<VPair>, List<VPair>> pipe) {
		Objects.requireNonNull(pipe, "pipe");
		return new VObject(this.pipe.apply(pipe));
	}

	@NotNull
	@Override
	public String eval(@NotNull Memory memory) {
		Objects.requireNonNull(memory, "memory");
		return this.pipe
				.eval(memory)
				.stream()
				.map(pair -> pair.eval(memory))
				.collect(Collectors.joining(",", "{", "}"));
	}

	@NotNull
	@Override
	public Pipe<Object, List<VPair>> getPipe() {
		return this.pipe;
	}

	@NotNull
	@Override
	public String toString() {
		return "Object:" + Integer.toHexString(this.hashCode());
	}
}
