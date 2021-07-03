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

import java.util.Objects;

/**
 * A value that evaluates to a number and can be evaluated to a raw number.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.29
 */
public final class NumberValue implements Value<Double> {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 2949570788002453838L;

	/**
	 * The evaluating function that evaluates to the raw value of this value.
	 *
	 * @since 0.3.0 ~2021.06.30
	 */
	@NotNull
	private final Pipe<Double> pipe;

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
		this.pipe = (m, v) -> number;
	}

	/**
	 * Construct a new number value that evaluates to the given raw {@code number}.
	 *
	 * @param number the raw value of the constructed number value.
	 * @since 0.3.0 ~2021.06.30
	 */
	public NumberValue(double number) {
		this.pipe = (m, v) -> number;
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
		this.pipe = (m, v) -> d;
	}

	/**
	 * Construct a new number value that evaluates to the result of parsing the evaluation
	 * of the given {@code value}.
	 *
	 * @param value the value of the constructed number value.
	 * @throws NullPointerException if the given {@code value} is null.
	 * @since 0.3.0 ~2021.06.30
	 */
	public NumberValue(@NotNull Value<?> value) {
		Objects.requireNonNull(value, "value");
		this.pipe = (m, v) -> NumberValue.parse(value.evaluate(m));
	}

	/**
	 * An internal constructor to construct a new number value with the given {@code
	 * factory}.
	 *
	 * @param pipe the factory that evaluates to the number of the constructed number
	 *             value.
	 * @throws NullPointerException if the given {@code factory} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	private NumberValue(@NotNull Pipe<Double> pipe) {
		Objects.requireNonNull(pipe, "pipe");
		this.pipe = pipe;
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
		return new NumberValue(Tokenizer.toString(object));
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
	public NumberValue apply(@NotNull Pipe<Double> pipe) {
		Objects.requireNonNull(pipe, "pipe");
		return new NumberValue(
				this.pipe.apply(pipe)
		);
	}

	@NotNull
	@Override
	public String evaluate(@NotNull Memory memory) {
		Objects.requireNonNull(memory, "memory");
		double number = this.pipe.eval(memory);
		//noinspection DynamicRegexReplaceableByCompiledPattern
		return number % 1 == 0 ?
			   String.format("%d", (long) number) :
			   String.format("%f", number)
					 .replaceAll("0*$", "");
	}

	@NotNull
	@Override
	public Pipe<Double> getPipe() {
		return this.pipe;
	}

	@NotNull
	@Override
	public String toString() {
		return "Number:" + Integer.toHexString(this.hashCode());
	}
}
