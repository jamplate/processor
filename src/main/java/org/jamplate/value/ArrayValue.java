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
import org.jetbrains.annotations.Unmodifiable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * A value that evaluate to an array and can be evaluated to a raw list of element
 * values.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.29
 */
public final class ArrayValue extends TokenValue<List<Value>> {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 8228913560756371483L;

	/**
	 * The evaluating function that evaluates to the raw value of this value.
	 *
	 * @since 0.3.0 ~2021.06.29
	 */
	@NotNull
	private final Function<Memory, List<Value>> function;

	/**
	 * Construct a new array value that evaluates to the result of parsing the given
	 * {@code source}.
	 *
	 * @param source the source text.
	 * @throws NullPointerException if the given {@code source} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	public ArrayValue(@NotNull String source) {
		Objects.requireNonNull(source, "source");
		List<Value> list = ArrayValue.parse(source);
		this.function = m -> list;
	}

	/**
	 * Construct a new array value that evaluates to the given raw {@code elements}.
	 *
	 * @param elements the raw array of the elements the constructed value will have.
	 * @throws NullPointerException if the given {@code elements} is null.
	 * @since 0.3.0 ~2021.06.29
	 */
	public ArrayValue(@Nullable Object @NotNull ... elements) {
		Objects.requireNonNull(elements, "elements");
		List<Value> list = ArrayValue.transformElements(elements);
		this.function = m -> list;
	}

	/**
	 * Construct a new array value that evaluates to the given raw {@code elements}.
	 *
	 * @param elements the raw array of the elements the constructed valued will have.
	 * @throws NullPointerException if the given {@code elements} is null.
	 * @since 0.3.0 ~2021.06.29
	 */
	public ArrayValue(@NotNull Iterable<?> elements) {
		Objects.requireNonNull(elements, "elements");
		List<Value> list = ArrayValue.transformElements(elements);
		this.function = m -> list;
	}

	/**
	 * Construct a new array value that evaluate to the result of parsing the evaluation
	 * of the given {@code value}.
	 *
	 * @param value the value of the constructed array value.
	 * @throws NullPointerException if the given {@code value} is null.
	 * @since 0.3.0 ~2021.06.29
	 */
	@SuppressWarnings("LambdaUnfriendlyMethodOverload")
	public ArrayValue(@NotNull Value value) {
		Objects.requireNonNull(value, "value");
		this.function = m -> ArrayValue.parse(value.evaluate(m));
	}

	/**
	 * Construct a new array value that evaluate to the result of converting the
	 * evaluation of the given {@code object} value to an array.
	 *
	 * @param object the object value of the constructed array.
	 * @throws NullPointerException if the given {@code object} is null.
	 * @since 0.3.0 ~2021.06.29
	 */
	public ArrayValue(@NotNull ObjectValue object) {
		Objects.requireNonNull(object, "object");
		this.function = m -> ArrayValue.transformPairs(object.evaluateToken(m));
	}

	/**
	 * An internal constructor to construct a new array value with the given {@code
	 * function}.
	 *
	 * @param function the function that evaluates to the elements of the constructed
	 *                 array value.
	 * @throws NullPointerException if the given {@code function} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	@SuppressWarnings("LambdaUnfriendlyMethodOverload")
	private ArrayValue(@NotNull Function<Memory, List<Value>> function) {
		Objects.requireNonNull(function, "function");
		this.function = function;
	}

	/**
	 * Cast the given {@code object} into an array value.
	 *
	 * @param object the object to be cast
	 * @return an array value evaluating to an array interpretation of the given {@code
	 * 		object}.
	 * @since 0.3.0 ~2021.06.30
	 */
	@NotNull
	@Contract(pure = true)
	public static ArrayValue cast(@Nullable Object object) {
		//it
		if (object instanceof ArrayValue)
			return (ArrayValue) object;
		//wrap object
		if (object instanceof ObjectValue)
			return new ArrayValue((ObjectValue) object);
		//wrap
		if (object instanceof Value)
			return new ArrayValue((Value) object);
		//raw
		if (object instanceof Iterable)
			return new ArrayValue((Iterable<?>) object);
		if (object instanceof Object[])
			return new ArrayValue((Object[]) object);
		//raw object
		if (object instanceof Map)
			return new ArrayValue(ArrayValue.transformPairs((Map<?, ?>) object));
		if (object instanceof JSONObject)
			return new ArrayValue(ArrayValue.transformPairs(((JSONObject) object).toMap()));

		//parse
		return new ArrayValue(TokenValue.toString(object));
	}

	/**
	 * Parse the given {@code source} into elements.
	 *
	 * @param source the source to be parsed.
	 * @return elements from parsing the given {@code source}.
	 * @throws NullPointerException if the given {@code source} is null.
	 * @since 0.3.0 ~2021.06.30
	 */
	@NotNull
	@Unmodifiable
	@Contract(pure = true)
	public static List<Value> parse(@NotNull String source) {
		Objects.requireNonNull(source, "source");
		//attempt to parse as array
		try {
			JSONArray array = new JSONArray(source);

			return ArrayValue.transformElements(array.toList());
		} catch (JSONException ignored) {
		}
		//attempt to parse as object
		try {
			JSONObject object = new JSONObject(source);

			return ArrayValue.transformPairs(object.toMap());
		} catch (JSONException ignored) {
		}
		//attempt to parse as singleton
		return Collections.singletonList(
				TokenValue.cast(source)
		);
	}

	/**
	 * Transform the given {@code elements} array into an elements values list.
	 *
	 * @param elements the array to be transformed.
	 * @return an immutable values list interpretation of the given {@code elements}
	 * 		array.
	 * @throws NullPointerException if the given {@code elements} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	@NotNull
	@Unmodifiable
	@Contract(pure = true)
	public static List<Value> transformElements(@Nullable Object @NotNull ... elements) {
		Objects.requireNonNull(elements, "elements");
		return Collections.unmodifiableList(
				Arrays.stream(elements)
					  .map(TokenValue::cast)
					  .collect(Collectors.toList())
		);
	}

	/**
	 * Transform the given {@code elements} iterable into an elements values list.
	 *
	 * @param elements the iterable to be transformed.
	 * @return an immutable values list interpretation of the given {@code elements}
	 * 		iterable.
	 * @throws NullPointerException if the given {@code elements} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	@NotNull
	@Unmodifiable
	@Contract(pure = true)
	public static List<Value> transformElements(@NotNull Iterable<?> elements) {
		Objects.requireNonNull(elements, "elements");
		return Collections.unmodifiableList(
				StreamSupport
						.stream(elements.spliterator(), false)
						.map(TokenValue::cast)
						.collect(Collectors.toList())
		);
	}

	/**
	 * Transform the given {@code pairs} map into an elements values list.
	 *
	 * @param pairs the pairs to be transformed.
	 * @return an immutable values list interpretation of the given {@code pairs} map.
	 * @throws NullPointerException if the given {@code pairs} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	@NotNull
	@Unmodifiable
	@Contract(pure = true)
	public static List<Value> transformPairs(@NotNull Map<?, ?> pairs) {
		Objects.requireNonNull(pairs, "pairs");
		//noinspection SimplifyStreamApiCallChains
		return Collections.unmodifiableList(
				StreamSupport
						.stream(pairs.keySet().spliterator(), false)
						.map(TokenValue::cast)
						.collect(Collectors.toList())
		);
	}

	/**
	 * Transform the given {@code pairs} iterable into an elements values list.
	 *
	 * @param pairs the pairs to be transformed.
	 * @return an immutable values list interpretation of the given {@code pairs}
	 * 		iterable.
	 * @throws NullPointerException if the given {@code pairs} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	@NotNull
	@Unmodifiable
	@Contract(pure = true)
	public static List<Value> transformPairs(@NotNull Iterable<?> pairs) {
		Objects.requireNonNull(pairs, "pairs");
		return Collections.unmodifiableList(
				StreamSupport
						.stream(pairs.spliterator(), false)
						.map(PairValue::cast)
						.map(PairValue::getKey)
						.collect(Collectors.toList())
		);
	}

	@NotNull
	@Override
	public ArrayValue apply(@NotNull BiFunction<Memory, List<Value>, List<Value>> function) {
		Objects.requireNonNull(function, "function");
		return new ArrayValue((Function<Memory, List<Value>>) m ->
				ArrayValue.transformElements(function.apply(m, this.evaluateToken(m)))
		);
	}

	@NotNull
	@Override
	public String evaluate(@NotNull Memory memory) {
		Objects.requireNonNull(memory, "memory");
		return this.evaluateToken(memory)
				   .stream()
				   .map(value -> value.evaluate(memory))
				   .collect(Collectors.joining(",", "[", "]"));
	}

	@NotNull
	@Override
	public List<Value> evaluateToken(@NotNull Memory memory) {
		Objects.requireNonNull(memory, "memory");
		return this.function.apply(memory);
	}

	@NotNull
	@Override
	public String toString() {
		return "Array:" + Integer.toHexString(this.hashCode());
	}

	/**
	 * Evaluate the token in this array at the given {@code index}.
	 *
	 * @param memory the memory to evaluate with.
	 * @param index  the index of the targeted token.
	 * @return the token (element) at the given {@code index}.
	 * @throws NullPointerException     if the given {@code memory} is null.
	 * @throws IllegalArgumentException if the given {@code index} is negative.
	 * @since 0.3.0 ~2021.07.01
	 */
	@NotNull
	@Contract(pure = true)
	public Value evaluateTokenAt(@NotNull Memory memory, int index) {
		Objects.requireNonNull(memory, "memory");
		return this.evaluateToken(memory)
				   .stream()
				   .skip(index)
				   .findFirst()
				   .orElse(Value.NULL);
	}

	/**
	 * Return a value that evaluates to the value at the given {@code index} in this
	 * array.
	 *
	 * @param index the index to target.
	 * @return a value that evaluates to the value at the given {@code index} in this
	 * 		array.
	 * @throws NullPointerException if the given {@code index} is null.
	 * @since 0.3.0 ~2021.06.29
	 */
	@NotNull
	@Contract(pure = true)
	public Value get(@NotNull NumberValue index) {
		Objects.requireNonNull(index, "index");
		return m -> {
			//eval key
			double indexNumber = index.evaluatePrimitiveToken(m);

			//stream elements -> skip to target -> returned eval element
			return this.evaluateToken(m)
					   .stream()
					   .skip((long) indexNumber)
					   .findFirst()
					   .map(element -> element.evaluate(m))
					   .orElse("");
		};
	}

	/**
	 * Return a new array that contains the elements of this array but with the given
	 * {@code value} set at the given {@code index}.
	 *
	 * @param index the index to be set the value at in the returned object.
	 * @param value the value to be set in the returned object.
	 * @return an array from this array but with the given {@code value} at the given
	 *        {@code index}.
	 * @throws NullPointerException if the given {@code index} or {@code value} is null.
	 * @since 0.3.0 ~2021.06.29
	 */
	@NotNull
	@Contract(value = "_,_->new", pure = true)
	public ArrayValue put(@NotNull NumberValue index, @NotNull Value value) {
		Objects.requireNonNull(index, "index");
		Objects.requireNonNull(value, "value");
		return new ArrayValue((Function<Memory, List<Value>>) m -> {
			//eval the elements
			List<Value> elements = new ArrayList<>(this.evaluateToken(m));
			//eval the index
			double indexNumber = index.evaluatePrimitiveToken(m);

			//fill if too small
			while (elements.size() <= indexNumber)
				elements.add(new TextValue(""));

			//set
			elements.set((int) indexNumber, value);

			//return
			return Collections.unmodifiableList(elements);
		});
	}
}
