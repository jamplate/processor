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
 * A value wrapper implementation.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.30
 */
public final class TextValue implements Value<String> {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -8001672867393082818L;

	/**
	 * The function that evaluates the the raw text of this value.
	 *
	 * @since 0.3.0 ~2021.06.30
	 */
	@NotNull
	private final Pipe<Object, String> pipe;

	/**
	 * An internal constructor to construct a new text value with the given {@code
	 * function}.
	 *
	 * @param pipe the function that evaluates to text of the constructed text value.
	 * @throws NullPointerException if the given {@code function} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	public TextValue(@NotNull Pipe<Object, String> pipe) {
		Objects.requireNonNull(pipe, "pipe");
		this.pipe = pipe;
	}

	@NotNull
	@Override
	public TextValue apply(@NotNull Pipe<String, String> pipe) {
		Objects.requireNonNull(pipe, "pipe");
		return new TextValue(this.pipe.apply(pipe));
	}

	@NotNull
	@Override
	public String evaluate(@NotNull Memory memory) {
		Objects.requireNonNull(memory, "memory");
		return this.pipe.eval(memory);
	}

	@NotNull
	@Override
	public Pipe<Object, String> getPipe() {
		return this.pipe;
	}

	@NotNull
	@Override
	public String toString() {
		return "Text:" + Integer.toHexString(this.hashCode());
	}
}
