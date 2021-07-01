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
import org.jamplate.model.Value;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A quoting wrapper value.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.07.01
 */
public final class QuoteValue extends TokenValue<Value> {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -8362991250407876199L;

	/**
	 * The function that evaluates the the raw text of this value.
	 *
	 * @since 0.3.0 ~2021.07.01
	 */
	@NotNull
	private final Function<Memory, Value> function;

	/**
	 * Construct a new quoted text value that evaluate to the evaluation of the given
	 * {@code value}.
	 *
	 * @param value the value of the constructed value.
	 * @throws NullPointerException if the given {@code value} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	@SuppressWarnings("LambdaUnfriendlyMethodOverload")
	public QuoteValue(@NotNull Value value) {
		Objects.requireNonNull(value, "value");
		this.function = m -> value;
	}

	/**
	 * An internal constructor to construct a quoted new text value with the given {@code
	 * function}.
	 *
	 * @param function the function that evaluates to text of the constructed quoted text
	 *                 value.
	 * @throws NullPointerException if the given {@code function} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	@SuppressWarnings("LambdaUnfriendlyMethodOverload")
	private QuoteValue(@NotNull Function<Memory, Value> function) {
		Objects.requireNonNull(function, "function");
		this.function = function;
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
		return new QuoteValue(TokenValue.cast(object));
	}

	@NotNull
	@Override
	public QuoteValue apply(@NotNull BiFunction<Memory, Value, Value> function) {
		Objects.requireNonNull(function, "function");
		return new QuoteValue((Function<Memory, Value>) m ->
				function.apply(m, this.evaluateToken(m))
		);
	}

	@NotNull
	@Override
	public String evaluate(@NotNull Memory memory) {
		Objects.requireNonNull(memory, "memory");
		Value value = this.evaluateToken(memory);

		if (value instanceof TextValue)
			return JSONObject.quote(value.evaluate(memory));

		return value.evaluate(memory);
	}

	@NotNull
	@Override
	public Value evaluateToken(@NotNull Memory memory) {
		Objects.requireNonNull(memory, "memory");
		return this.function.apply(memory);
	}
}
