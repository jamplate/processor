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
import java.util.function.Predicate;

/**
 * A value that evaluates to a boolean and can be evaluated to a raw state.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.30
 */
public final class BooleanValue extends TokenValue<Boolean> {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -1562079784562666632L;

	/**
	 * The evaluating function that evaluates to the raw value of this value.
	 *
	 * @since 0.3.0 ~2021.07.01
	 */
	@NotNull
	private final Predicate<Memory> function;

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
		this.function = m -> state;
	}

	/**
	 * Construct a new boolean value that evaluates to the given raw {@code state}.
	 *
	 * @param state the raw value of the constructed boolean value.
	 * @since 0.3.0 ~2021.07.01
	 */
	public BooleanValue(boolean state) {
		this.function = m -> state;
	}

	/**
	 * Construct a new boolean value that evaluates to the result of parsing the
	 * evaluation of the given {@code value}.
	 *
	 * @param value the value of the constructed boolean value.
	 * @throws NullPointerException if the given {@code value} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	@SuppressWarnings("LambdaUnfriendlyMethodOverload")
	public BooleanValue(@NotNull Value value) {
		Objects.requireNonNull(value, "value");
		this.function = m -> BooleanValue.parse(value.evaluate(m));
	}

	/**
	 * An internal constructor to construct a new boolean value with the given {@code
	 * function}.
	 *
	 * @param function the function that evaluates to the state of the constructed boolean
	 *                 value.
	 * @throws NullPointerException if the given {@code function} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	@SuppressWarnings("LambdaUnfriendlyMethodOverload")
	private BooleanValue(@NotNull Predicate<Memory> function) {
		Objects.requireNonNull(function, "function");
		this.function = function;
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
		return new BooleanValue(TokenValue.toString(object));
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
	public BooleanValue apply(@NotNull BiFunction<Memory, Boolean, Boolean> function) {
		Objects.requireNonNull(function, "function");
		return new BooleanValue((Predicate<Memory>) m ->
				function.apply(m, this.evaluateToken(m))
		);
	}

	@NotNull
	@Override
	public String evaluate(@NotNull Memory memory) {
		Objects.requireNonNull(memory, "memory");
		return this.evaluatePrimitiveToken(memory) ?
			   "true" :
			   "false";
	}

	@NotNull
	@Override
	public Boolean evaluateToken(@NotNull Memory memory) {
		Objects.requireNonNull(memory, "memory");
		return this.function.test(memory);
	}

	/**
	 * Evaluate this value to a primitive raw state.
	 *
	 * @param memory the memory to evaluate with.
	 * @return a primitive raw evaluation of this value.
	 * @throws NullPointerException if the given {@code memory} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	@Contract(pure = true)
	public boolean evaluatePrimitiveToken(@NotNull Memory memory) {
		Objects.requireNonNull(memory, "memory");
		return this.function.test(memory);
	}
}
