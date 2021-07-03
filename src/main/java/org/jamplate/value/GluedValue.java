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
import org.jetbrains.annotations.Unmodifiable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * A value from gluing multiple other values.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.07.01
 */
public final class GluedValue implements Value<List<Value>> {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 8228913560756371483L;

	/**
	 * The evaluating function that evaluates to the raw value of this value.
	 *
	 * @since 0.3.0 ~2021.07.01
	 */
	@NotNull
	private final Pipe<List<Value>> pipe;

	/**
	 * Construct a new glued value that evaluates to the given raw {@code values}.
	 *
	 * @param values the raw array of the values the constructed value will be gluing.
	 * @throws NullPointerException if the given {@code values} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	public GluedValue(@Nullable Object @NotNull ... values) {
		Objects.requireNonNull(values, "values");
		List<Value> list = GluedValue.transformValues(values);
		this.pipe = (m, v) -> list;
	}

	/**
	 * Construct a new glued value that evaluates to the given raw {@code elements}.
	 *
	 * @param elements the raw array of the elements the constructed valued will have.
	 * @throws NullPointerException if the given {@code elements} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	public GluedValue(@NotNull Iterable<?> elements) {
		Objects.requireNonNull(elements, "elements");
		List<Value> list = ArrayValue.transformElements(elements);
		this.pipe = (m, v) -> list;
	}

	/**
	 * Construct a new glued value that evaluate to the result of the evaluation of the
	 * given {@code value}.
	 *
	 * @param value the value of the constructed glued value.
	 * @throws NullPointerException if the given {@code value} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	public GluedValue(@NotNull Value<?> value) {
		Objects.requireNonNull(value, "value");
		this.pipe = (m, v) -> Collections.singletonList(value);
	}

	/**
	 * An internal constructor to construct a new glued value with the given {@code
	 * function}.
	 *
	 * @param pipe the function that evaluates to the values of the constructed glued
	 *             value.
	 * @throws NullPointerException if the given {@code function} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	private GluedValue(@NotNull Pipe<List<Value>> pipe) {
		Objects.requireNonNull(pipe, "pipe");
		this.pipe = pipe;
	}

	/**
	 * Cast the given {@code object} into an glued value.
	 *
	 * @param object the object to be cast
	 * @return a glued value evaluating to an glued interpretation of the given {@code
	 * 		object}.
	 * @since 0.3.0 ~2021.07.01
	 */
	@NotNull
	@Contract(pure = true)
	public static GluedValue cast(@Nullable Object object) {
		//it
		if (object instanceof GluedValue)
			return (GluedValue) object;
		//wrap
		if (object instanceof Value)
			return new GluedValue((Value) object);
		//raw
		if (object instanceof Iterable)
			return new GluedValue((Iterable<?>) object);
		if (object instanceof Object[])
			return new GluedValue((Object[]) object);

		//singleton
		return new GluedValue(object);
	}

	/**
	 * Transform the given {@code values} array into an values list.
	 *
	 * @param values the array to be transformed.
	 * @return an immutable values list interpretation of the given {@code values} array.
	 * @throws NullPointerException if the given {@code values} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	@NotNull
	@Unmodifiable
	@Contract(pure = true)
	public static List<Value> transformValues(@Nullable Object @NotNull ... values) {
		Objects.requireNonNull(values, "values");
		return Collections.unmodifiableList(
				Arrays.stream(values)
					  .map(Tokenizer::cast)
					  .collect(Collectors.toList())
		);
	}

	/**
	 * Transform the given {@code values} iterable into an values values list.
	 *
	 * @param values the iterable to be transformed.
	 * @return an immutable values list interpretation of the given {@code values}
	 * 		iterable.
	 * @throws NullPointerException if the given {@code values} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	@NotNull
	@Unmodifiable
	@Contract(pure = true)
	public static List<Value> transformValues(@NotNull Iterable<?> values) {
		Objects.requireNonNull(values, "values");
		return Collections.unmodifiableList(
				StreamSupport
						.stream(values.spliterator(), false)
						.map(Tokenizer::cast)
						.collect(Collectors.toList())
		);
	}

	@NotNull
	@Override
	public Value<List<Value>> apply(@NotNull Pipe<List<Value>> pipe) {
		Objects.requireNonNull(pipe, "pipe");
		return new GluedValue(this.pipe.apply(pipe));
	}

	@NotNull
	@Override
	public String evaluate(@NotNull Memory memory) {
		Objects.requireNonNull(memory, "memory");
		return this.pipe
				.eval(memory)
				.stream()
				.map(value -> value.evaluate(memory))
				.collect(Collectors.joining());
	}

	@NotNull
	@Override
	public Pipe<List<Value>> getPipe() {
		return this.pipe;
	}

	@NotNull
	@Override
	public String toString() {
		return "Glued:" + Integer.toHexString(this.hashCode());
	}
}
