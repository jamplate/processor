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
 * A value wrapper implementation.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.30
 */
public final class TextValue extends TokenValue<String> {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -8001672867393082818L;

	/**
	 * The function that evaluates the the raw text of this value.
	 *
	 * @since 0.3.0 ~2021.06.30
	 */
	@NotNull
	private final Function<Memory, String> function;

	/**
	 * Construct a new text value that evaluates to the given {@code text}.
	 *
	 * @param text the value of the constructed text value.
	 * @throws NullPointerException if the given {@code text} is null.
	 * @since 0.3.0 ~2021.06.30
	 */
	public TextValue(@NotNull String text) {
		Objects.requireNonNull(text, "text");
		this.function = m -> text;
	}

	/**
	 * Construct a new text value that evaluates to the given {@code text}.
	 *
	 * @param text the value of the constructed text value.
	 * @throws NullPointerException if the given {@code text} is null.
	 * @since 0.3.0 ~2021.06.30
	 */
	public TextValue(@NotNull Object text) {
		Objects.requireNonNull(text, "text");
		String string = TokenValue.toString(text);
		this.function = m -> string;
	}

	/**
	 * Construct a new text value that evaluate to the evaluation of the given {@code
	 * value}.
	 *
	 * @param value the value of the constructed value.
	 * @throws NullPointerException if the given {@code value} is null.
	 * @since 0.3.0 ~2021.06.30
	 */
	@SuppressWarnings("LambdaUnfriendlyMethodOverload")
	public TextValue(@NotNull Value value) {
		Objects.requireNonNull(value, "value");
		this.function = value::evaluate;
	}

	/**
	 * An internal constructor to construct a new text value with the given {@code
	 * function}.
	 *
	 * @param function the function that evaluates to text of the constructed text value.
	 * @throws NullPointerException if the given {@code function} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	@SuppressWarnings("LambdaUnfriendlyMethodOverload")
	private TextValue(@NotNull Function<Memory, String> function) {
		Objects.requireNonNull(function, "function");
		this.function = function;
	}

	/**
	 * Cast the given {@code object} into a text value.
	 *
	 * @param object the object to be cast.
	 * @return a new text value evaluating to the value of the given {@code object}.
	 * @throws NullPointerException if the given {@code object} is null.
	 * @since 0.3.0 ~2021.06.30
	 */
	@NotNull
	@Contract(pure = true)
	public static TextValue cast(@Nullable Object object) {
		//it
		if (object instanceof TextValue)
			return (TextValue) object;
		//wrap
		if (object instanceof Value)
			return new TextValue((Value) object);

		//stringify
		return new TextValue(TokenValue.toString(object));
	}

	@NotNull
	@Override
	public TextValue apply(@NotNull BiFunction<Memory, String, String> function) {
		return new TextValue((Function<Memory, String>) m ->
				function.apply(m, this.evaluateToken(m))
		);
	}

	@NotNull
	@Override
	public String evaluate(@NotNull Memory memory) {
		Objects.requireNonNull(memory, "memory");
		return this.evaluateToken(memory);
	}

	@NotNull
	@Override
	public String evaluateToken(@NotNull Memory memory) {
		Objects.requireNonNull(memory, "memory");
		return this.function.apply(memory);
	}
}
