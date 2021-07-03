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

import org.jamplate.memory.Memory;
import org.jamplate.memory.Pipe;
import org.jamplate.memory.Value;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;
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
public final class ArrayValue implements Value<List<Value>> {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 8228913560756371483L;

	/**
	 * The evaluating function that evaluates to the raw value of this value.
	 *
	 * @since 0.3.0 ~2021.06.29
	 */
	@NotNull
	private final Pipe<List<Value>> pipe;

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
		this.pipe = (m, v) -> list;
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
		this.pipe = (m, v) -> list;
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
		this.pipe = (m, v) -> list;
	}

	/**
	 * Construct a new array value that evaluate to the result of parsing the evaluation
	 * of the given {@code value}.
	 *
	 * @param value the value of the constructed array value.
	 * @throws NullPointerException if the given {@code value} is null.
	 * @since 0.3.0 ~2021.06.29
	 */
	public ArrayValue(@NotNull Value<?> value) {
		Objects.requireNonNull(value, "value");
		this.pipe = (m, v) -> ArrayValue.parse(value.evaluate(m));
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
		this.pipe = (m, v) -> ArrayValue.transformPairs(object.getPipe().eval(m));
	}

	/**
	 * An internal constructor to construct a new array value with the given {@code
	 * function}.
	 *
	 * @param pipe the function that evaluates to the elements of the constructed array
	 *             value.
	 * @throws NullPointerException if the given {@code pipe} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	private ArrayValue(@NotNull Pipe<List<Value>> pipe) {
		Objects.requireNonNull(pipe, "pipe");
		this.pipe = pipe;
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
		return new ArrayValue(Tokenizer.toString(object));
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
				Tokenizer.cast(source)
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
					  .map(Tokenizer::cast)
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
						.map(Tokenizer::cast)
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
						.map(Tokenizer::cast)
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
	public ArrayValue apply(@NotNull Pipe<List<Value>> pipe) {
		Objects.requireNonNull(pipe, "pipe");
		return new ArrayValue(this.pipe.apply(pipe));
	}

	@NotNull
	@Override
	public String evaluate(@NotNull Memory memory) {
		Objects.requireNonNull(memory, "memory");
		return this.pipe
				.eval(memory)
				.stream()
				.map(value -> value.evaluate(memory))
				.collect(Collectors.joining(",", "[", "]"));
	}

	@NotNull
	@Override
	public Pipe<List<Value>> getPipe() {
		return this.pipe;
	}

	@NotNull
	@Override
	public String toString() {
		return "Array:" + Integer.toHexString(this.hashCode());
	}

	//--------

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
	public Value<?> evaluateAt(@NotNull Memory memory, int index) {
		Objects.requireNonNull(memory, "memory");
		return this.pipe
				.eval(memory)
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
	public Value<?> get(@NotNull NumberValue index) {
		Objects.requireNonNull(index, "index");
		return m -> {
			//eval key
			double indexNumber = index.getPipe().eval(m);

			//stream elements -> skip to target -> returned eval element
			return this.pipe
					.eval(m)
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
		return new ArrayValue((Pipe<List<Value>>) (m, v) -> {
			//eval the elements
			List<Value> elements = new ArrayList<>(this.pipe.eval(m));
			//eval the index
			double indexNumber = index.getPipe().eval(m);

			//fill if too small
			while (elements.size() <= indexNumber)
				elements.add(new TextValue(""));

			//set
			elements.set((int) indexNumber, value);

			//return
			return Collections.unmodifiableList(elements);
		});
	}

	/**
	 * Return a new array that contains the elements of this array but with the given
	 * {@code value} placed at the given nested {@code indices}.
	 *
	 * @param indices the nested indices to the place to put the value in the returned
	 *                array.
	 * @param value   the value of to be set in the returned array.
	 * @return an array from this array but with the given {@code value} at the given
	 * 		nested {@code indices}.
	 * @throws NullPointerException     if the given {@code keys} or {@code value} is
	 *                                  null.
	 * @throws IllegalArgumentException if the given {@code keys} list is empty or if the
	 *                                  first key has not been interpreted as a number.
	 * @since 0.3.0 ~2021.07.03
	 */
	@NotNull
	@Contract(value = "_,_->new", pure = true)
	public ArrayValue put(@NotNull List<?> indices, @NotNull Value value) {
		Objects.requireNonNull(indices, "indices");
		Objects.requireNonNull(value, "value");
		//cheat-check
		List<Value> list = ArrayValue.transformElements(indices);
		//case no keys
		if (list.isEmpty())
			throw new IllegalArgumentException("Empty keys list");
		//case first key is not a number
		if (!(list.get(0) instanceof NumberValue))
			throw new IllegalArgumentException("First key is not an index");
		//case single key
		if (list.size() == 1)
			return this.put((NumberValue) list.get(0), value);
		//case multiple keys
		return new ArrayValue((Pipe<List<Value>>) (m, v) -> {
			//eval the pairs
			List<Value> elements = new ArrayList<>(this.pipe.eval(m));
			//the index
			NumberValue index = (NumberValue) list.get(0);
			//eval the index
			double indexNumber = index.getPipe().eval(m);
			//the next key
			Value nextKey = list.get(1);

			//if too small
			while (elements.size() <= indexNumber)
				elements.add(new TextValue(""));

			//get middle structure
			Value middle = elements.get((int) indexNumber);

			//case middle can be an array
			if (!(middle instanceof ObjectValue) && nextKey instanceof NumberValue) {
				//middle as array
				ArrayValue middleArray = ArrayValue.cast(middle);

				//replace and done
				elements.set(
						(int) indexNumber,
						middleArray.put(
								list.subList(1, list.size()),
								value
						)
				);
			} else {
				//middle as object
				ObjectValue middleObject = ObjectValue.cast(middle);

				//replace and done
				elements.set(
						(int) indexNumber,
						middleObject.put(
								list.subList(1, list.size()),
								value
						)
				);
			}

			//done
			return Collections.unmodifiableList(elements);
		});
	}
}
