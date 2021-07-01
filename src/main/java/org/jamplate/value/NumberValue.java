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
import java.util.function.ToDoubleFunction;

/**
 * A value that evaluates to a number and can be evaluated to a raw number.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.29
 */
public final class NumberValue extends TokenValue<Double> {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 2949570788002453838L;

	/**
	 * The evaluating function that evaluates to the raw value of this value.
	 *
	 * @since 0.3.0 ~2021.06.30
	 */
	@NotNull
	private final ToDoubleFunction<Memory> function;

	/**
	 * Construct a new number value that evaluates to the result of parsing the given
	 * {@code source}.
	 *
	 * @param source the source text.
	 * @throws NullPointerException if the given {@code source} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	public NumberValue(@NotNull String source) {
		Objects.requireNonNull(source, "source");
		double number = NumberValue.parse(source);
		this.function = m -> number;
	}

	/**
	 * Construct a new number value that evaluates to the given raw {@code number}.
	 *
	 * @param number the raw value of the constructed number value.
	 * @since 0.3.0 ~2021.06.30
	 */
	public NumberValue(double number) {
		this.function = m -> number;
	}

	/**
	 * Construct a new number value that evaluates to the given raw {@code number}.
	 *
	 * @param number the raw value of the constructed number value.
	 * @throws NullPointerException if the given {@code number} is null.
	 * @since 0.3.0 ~2021.06.30
	 */
	public NumberValue(@NotNull Number number) {
		Objects.requireNonNull(number, "number");
		double d = number.doubleValue();
		this.function = m -> d;
	}

	/**
	 * Construct a new number value that evaluates to the result of parsing the evaluation
	 * of the given {@code value}.
	 *
	 * @param value the value of the constructed number value.
	 * @throws NullPointerException if the given {@code value} is null.
	 * @since 0.3.0 ~2021.06.30
	 */
	@SuppressWarnings("LambdaUnfriendlyMethodOverload")
	public NumberValue(@NotNull Value value) {
		Objects.requireNonNull(value, "value");
		this.function = m -> NumberValue.parse(value.evaluate(m));
	}

	/**
	 * An internal constructor to construct a new number value with the given {@code
	 * function}.
	 *
	 * @param function the function that evaluates to the number of the constructed number
	 *                 value.
	 * @throws NullPointerException if the given {@code function} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	@SuppressWarnings("LambdaUnfriendlyMethodOverload")
	private NumberValue(@NotNull ToDoubleFunction<Memory> function) {
		Objects.requireNonNull(function, "function");
		this.function = function;
	}

	/**
	 * Cast the given {@code object} into a number value.
	 *
	 * @param object the object to be cast.
	 * @return a new number value evaluating to a number interpretation of the given
	 *        {@code object}.
	 * @throws NullPointerException if the given {@code object} is null.
	 * @since 0.3.0 ~2021.06.30
	 */
	@NotNull
	@Contract(pure = true)
	public static NumberValue cast(@Nullable Object object) {
		//it
		if (object instanceof NumberValue)
			return (NumberValue) object;
		//wrap
		if (object instanceof Value)
			return new NumberValue((Value) object);
		//raw
		if (object instanceof Number)
			return new NumberValue((Number) object);

		//parse
		return new NumberValue(TokenValue.toString(object));
	}

	/**
	 * Parse the given {@code source} text into a number.
	 *
	 * @param source the source to be parsed.
	 * @return a number from parsing the given {@code source}.
	 * @throws NullPointerException if the given {@code source} is null.
	 * @since 0.3.0 ~2021.06.30
	 */
	@Contract(pure = true)
	public static double parse(@NotNull String source) {
		Objects.requireNonNull(source, "source");

		switch (source) {
			case "":
			case "0":
			case "false":
			case "null":
				return 0;
			case "1":
			case "true":
				return 1;
			default:
				try {
					return Double.parseDouble(source);
				} catch (NumberFormatException ignored) {
					return Double.NaN;
				}
		}
	}

	@NotNull
	@Override
	public NumberValue apply(@NotNull BiFunction<Memory, Double, Double> function) {
		Objects.requireNonNull(function, "function");
		return new NumberValue((ToDoubleFunction<Memory>) m ->
				function.apply(m, this.evaluateToken(m))
		);
	}

	@NotNull
	@Override
	public String evaluate(@NotNull Memory memory) {
		Objects.requireNonNull(memory, "memory");
		double number = this.evaluatePrimitiveToken(memory);
		//noinspection DynamicRegexReplaceableByCompiledPattern
		return number % 1 == 0 ?
			   String.format("%d", (long) number) :
			   String.format("%f", number)
					 .replaceAll("0*$", "");
	}

	@NotNull
	@Override
	public Double evaluateToken(@NotNull Memory memory) {
		Objects.requireNonNull(memory, "memory");
		return this.function.applyAsDouble(memory);
	}

	/**
	 * Evaluate this value to a primitive raw number.
	 *
	 * @param memory the memory to evaluate with.
	 * @return a primitive raw evaluation of this value.
	 * @throws NullPointerException if the given {@code memory} is null.
	 * @since 0.3.0 ~2021.06.30
	 */
	@Contract(pure = true)
	public double evaluatePrimitiveToken(@NotNull Memory memory) {
		Objects.requireNonNull(memory, "memory");
		return this.function.applyAsDouble(memory);
	}
}
