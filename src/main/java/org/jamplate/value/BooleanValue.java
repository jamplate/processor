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
package org.jamplate.value;

import org.jamplate.model.Memory;
import org.jamplate.model.Pipe;
import org.jamplate.model.Value;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
	private final Pipe<Boolean> pipe;

	/**
	 * Construct a new boolean value that evaluates to the result of parsing the given
	 * {@code source}.
	 *
	 * @param source the source text.
	 * @throws NullPointerException if the given {@code source} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	public BooleanValue(@NotNull String source) {
		Objects.requireNonNull(source, "source");
		boolean state = BooleanValue.parse(source);
		this.pipe = (m, v) -> state;
	}

	/**
	 * Construct a new boolean value that evaluates to the given raw {@code state}.
	 *
	 * @param state the raw value of the constructed boolean value.
	 * @since 0.3.0 ~2021.07.01
	 */
	public BooleanValue(boolean state) {
		this.pipe = (m, v) -> state;
	}

	/**
	 * Construct a new boolean value that evaluates to the result of parsing the
	 * evaluation of the given {@code value}.
	 *
	 * @param value the value of the constructed boolean value.
	 * @throws NullPointerException if the given {@code value} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	public BooleanValue(@NotNull Value<?> value) {
		Objects.requireNonNull(value, "value");
		this.pipe = (m, v) -> BooleanValue.parse(value.evaluate(m));
	}

	/**
	 * An internal constructor to construct a new boolean value with the given {@code
	 * pipe}.
	 *
	 * @param pipe the pipe that evaluates to the state of the constructed boolean value.
	 * @throws NullPointerException if the given {@code pipe} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	private BooleanValue(@NotNull Pipe<Boolean> pipe) {
		Objects.requireNonNull(pipe, "pipe");
		this.pipe = pipe;
	}

	/**
	 * Cast the given {@code object} into a boolean value.
	 *
	 * @param object the object to be cast.
	 * @return a new boolean value evaluating to a boolean interpretation of the given
	 *        {@code object}.
	 * @since 0.3.0 ~2021.07.01
	 */
	@NotNull
	@Contract(pure = true)
	public static BooleanValue cast(@Nullable Object object) {
		//it
		if (object instanceof BooleanValue)
			return (BooleanValue) object;
		//wrap
		if (object instanceof Value)
			return new BooleanValue((Value) object);
		//raw
		if (object instanceof Boolean)
			//noinspection AutoUnboxing
			return new BooleanValue((boolean) object);

		//parse
		return new BooleanValue(Tokenizer.toString(object));
	}

	/**
	 * Parse the given {@code source} text into a state.
	 *
	 * @param source the source to be parsed.
	 * @return a state from parsing the given {@code source}.
	 * @throws NullPointerException if the given {@code source} is null.
	 * @since 0.3.0 ~2021.06.30
	 */
	@Contract(pure = true)
	public static boolean parse(@NotNull String source) {
		Objects.requireNonNull(source, "source");
		switch (source) {
			case "":
			case "0":
			case "false":
			case "null":
				return false;
			case "1":
			case "true":
			default:
				return true;
		}
	}

	@NotNull
	@Override
	public BooleanValue apply(@NotNull Pipe<Boolean> pipe) {
		Objects.requireNonNull(pipe, "pipe");
		return new BooleanValue(this.pipe.apply(pipe));
	}

	@NotNull
	@Override
	public String evaluate(@NotNull Memory memory) {
		Objects.requireNonNull(memory, "memory");
		return this.pipe.eval(memory) ?
			   "true" :
			   "false";
	}

	@NotNull
	@Override
	public Pipe<Boolean> getPipe() {
		return this.pipe;
	}

	@NotNull
	@Override
	public String toString() {
		return "Boolean:" + Integer.toHexString(this.hashCode());
	}
}
