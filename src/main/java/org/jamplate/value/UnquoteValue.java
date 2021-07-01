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

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * An unquoting wrapper value.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.07.01
 */
public final class UnquoteValue extends TokenValue<Value> {
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
	 * Construct a new unquoted text value that evaluate to the evaluation of the given
	 * {@code value}.
	 *
	 * @param value the value of the constructed value.
	 * @throws NullPointerException if the given {@code value} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	@SuppressWarnings("LambdaUnfriendlyMethodOverload")
	public UnquoteValue(@NotNull Value value) {
		Objects.requireNonNull(value, "value");
		this.function = m -> value;
	}

	/**
	 * An internal constructor to construct an unquoted new text value with the given
	 * {@code function}.
	 *
	 * @param function the function that evaluates to text of the constructed unquoted
	 *                 text value.
	 * @throws NullPointerException if the given {@code function} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	@SuppressWarnings("LambdaUnfriendlyMethodOverload")
	private UnquoteValue(@NotNull Function<Memory, Value> function) {
		Objects.requireNonNull(function, "function");
		this.function = function;
	}

	/**
	 * Cast the given {@code object} into an unquoted text value.
	 *
	 * @param object the object to be cast.
	 * @return a new text value evaluating to the value of the given {@code object}.
	 * @throws NullPointerException if the given {@code object} is null.
	 * @since 0.3.0 ~2021.06.30
	 */
	@NotNull
	@Contract(pure = true)
	public static UnquoteValue cast(@Nullable Object object) {
		//it
		if (object instanceof UnquoteValue)
			return (UnquoteValue) object;
		//wrap
		if (object instanceof Value)
			return new UnquoteValue((Value) object);

		//turn into value
		return new UnquoteValue(TokenValue.cast(object));
	}

	@NotNull
	@Override
	public UnquoteValue apply(@NotNull BiFunction<Memory, Value, Value> function) {
		Objects.requireNonNull(function, "function");
		return new UnquoteValue((Function<Memory, Value>) m ->
				function.apply(m, this.evaluateToken(m))
		);
	}

	@NotNull
	@Override
	public String evaluate(@NotNull Memory memory) {
		Objects.requireNonNull(memory, "memory");
		Value value = this.evaluateToken(memory);

		if (value instanceof QuoteValue)
			return ((QuoteValue) value)
					.evaluateToken(memory)
					.evaluate(memory);

		return value.evaluate(memory);
	}

	@NotNull
	@Override
	public Value evaluateToken(@NotNull Memory memory) {
		Objects.requireNonNull(memory, "memory");
		return this.function.apply(memory);
	}
}
