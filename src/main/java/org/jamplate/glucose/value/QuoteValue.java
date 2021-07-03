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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.Objects;

/**
 * A quoting wrapper value.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.07.01
 */
public final class QuoteValue implements Value<Value> {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -8362991250407876199L;

	/**
	 * The function that evaluates the the raw text of this value.
	 *
	 * @since 0.3.0 ~2021.07.01
	 */
	@NotNull
	private final Pipe<Value> pipe;

	/**
	 * Construct a new quoted text value that evaluate to the evaluation of the given
	 * {@code value}.
	 *
	 * @param value the value of the constructed value.
	 * @throws NullPointerException if the given {@code value} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	public QuoteValue(@NotNull Value<?> value) {
		Objects.requireNonNull(value, "value");
		this.pipe = (m, v) -> value;
	}

	/**
	 * An internal constructor to construct a quoted new text value with the given {@code
	 * function}.
	 *
	 * @param pipe the function that evaluates to text of the constructed quoted text
	 *             value.
	 * @throws NullPointerException if the given {@code function} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	private QuoteValue(@NotNull Pipe<Value> pipe) {
		Objects.requireNonNull(pipe, "pipe");
		this.pipe = pipe;
	}

	/**
	 * Cast the given {@code object} into a quoted text value.
	 *
	 * @param object the object to be cast.
	 * @return a new text value evaluating to the value of the given {@code object}.
	 * @throws NullPointerException if the given {@code object} is null.
	 * @since 0.3.0 ~2021.06.30
	 */
	@NotNull
	@Contract(pure = true)
	public static QuoteValue cast(@Nullable Object object) {
		//it
		if (object instanceof QuoteValue)
			return (QuoteValue) object;
		//wrap
		if (object instanceof Value)
			return new QuoteValue((Value) object);

		//turn into value
		return new QuoteValue(Tokenizer.cast(object));
	}

	@NotNull
	@Override
	public QuoteValue apply(@NotNull Pipe<Value> pipe) {
		Objects.requireNonNull(pipe, "pipe");
		return new QuoteValue(
				this.pipe.apply(pipe)
		);
	}

	@NotNull
	@Override
	public String evaluate(@NotNull Memory memory) {
		Objects.requireNonNull(memory, "memory");
		Value value = this.pipe.eval(memory);

		if (value instanceof TextValue)
			return JSONObject.quote(value.evaluate(memory));

		return value.evaluate(memory);
	}

	@NotNull
	@Override
	public Pipe<Value> getPipe() {
		return this.pipe;
	}

	@NotNull
	@Override
	public String toString() {
		return "Quote:" + Integer.toHexString(this.hashCode());
	}
}
