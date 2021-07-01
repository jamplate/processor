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
 * A value that evaluate to an object and can be evaluated to a raw list of pair values.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.29
 */
public final class ObjectValue extends TokenValue<List<PairValue>> {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -5610810574617562364L;

	/**
	 * The evaluating function that evaluates to the raw value of this value.
	 *
	 * @since 0.3.0 ~2021.07.01
	 */
	@NotNull
	private final Function<Memory, List<PairValue>> function;

	/**
	 * Construct a new object value that evaluates to the result of parsing the given
	 * {@code source} as an object.
	 *
	 * @param source the source text.
	 * @throws NullPointerException if the given {@code source} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	public ObjectValue(@NotNull String source) {
		Objects.requireNonNull(source, "source");
		List<PairValue> list = ObjectValue.parse(source);
		this.function = m -> list;
	}

	/**
	 * Construct a new object value that evaluates to the given raw {@code pairs}.
	 *
	 * @param pairs the pairs for the constructed object value.
	 * @throws NullPointerException if the given {@code pairs} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	public ObjectValue(@Nullable Object @NotNull ... pairs) {
		Objects.requireNonNull(pairs, "pairs");
		List<PairValue> list = ObjectValue.transformPairs(pairs);
		this.function = m -> list;
	}

	/**
	 * Construct a new object value that evaluates to the given raw {@code pairs}.
	 *
	 * @param pairs the pairs for the constructed object value.
	 * @throws NullPointerException if the given {@code pairs} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	public ObjectValue(@NotNull Iterable<PairValue> pairs) {
		Objects.requireNonNull(pairs, "pairs");
		List<PairValue> list = ObjectValue.transformPairs(pairs);
		this.function = m -> list;
	}

	/**
	 * Construct a new object value that evaluates to the given raw {@code pairs}.
	 *
	 * @param pairs a map containing the pairs for the constructed object value.
	 * @throws NullPointerException if the given {@code pairs} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	public ObjectValue(@NotNull Map<?, ?> pairs) {
		Objects.requireNonNull(pairs, "pairs");
		List<PairValue> list = ObjectValue.transformPairs(pairs);
		this.function = m -> list;
	}

	/**
	 * Construct a new object value that evaluate to the result of parsing the evaluation
	 * of the given {@code value}.
	 *
	 * @param value the value of the constructed object value.
	 * @throws NullPointerException if the given {@code value} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	@SuppressWarnings("LambdaUnfriendlyMethodOverload")
	public ObjectValue(@NotNull Value value) {
		Objects.requireNonNull(value, "value");
		this.function = m -> ObjectValue.parse(value.evaluate(m));
	}

	/**
	 * Construct a new object value that evaluate to the result of converting the
	 * evaluation of the given {@code array} value to an object.
	 *
	 * @param array the array value of the constructed object.
	 * @throws NullPointerException if the given {@code object} is null.
	 * @since 0.3.0 ~2021.06.29
	 */
	public ObjectValue(@NotNull ArrayValue array) {
		Objects.requireNonNull(array, "array");
		this.function = m -> ObjectValue.transformElements(array.evaluateToken(m));
	}

	/**
	 * An internal constructor to construct a new object value with the given {@code
	 * function}.
	 *
	 * @param function the function that evaluates to the pairs of the constructed object
	 *                 value.
	 * @throws NullPointerException if the given {@code function} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	@SuppressWarnings("LambdaUnfriendlyMethodOverload")
	private ObjectValue(@NotNull Function<Memory, List<PairValue>> function) {
		Objects.requireNonNull(function, "function");
		this.function = function;
	}

	/**
	 * Cast the given {@code object} into an object value.
	 *
	 * @param object the object to be cast
	 * @return a new object value evaluating to an object interpretation of the given
	 *        {@code object}.
	 * @since 0.3.0 ~2021.06.30
	 */
	@NotNull
	@Contract(pure = true)
	public static ObjectValue cast(@Nullable Object object) {
		//it
		if (object instanceof ObjectValue)
			return (ObjectValue) object;
		//wrap array
		if (object instanceof ArrayValue)
			return new ObjectValue((ArrayValue) object);
		//wrap
		if (object instanceof Value)
			return new ObjectValue((Value) object);
		//raw
		if (object instanceof Map)
			return new ObjectValue((Map<?, ?>) object);
		if (object instanceof JSONObject)
			return new ObjectValue(((JSONObject) object).toMap());
		//raw array
		if (object instanceof Iterable)
			return new ObjectValue(ObjectValue.transformElements((Iterable<?>) object));
		if (object instanceof Object[])
			return new ObjectValue(ObjectValue.transformElements((Object[]) object));

		//parse
		return new ObjectValue(TokenValue.toString(object));
	}

	/**
	 * Parse the given {@code source} into pairs.
	 *
	 * @param source the source to be
	 * @return an immutable list containing the pairs from parsing the given {@code
	 * 		source}.
	 * @throws NullPointerException if the given {@code source} is null.
	 * @since 0.3.0 ~2021.06.30
	 */
	@NotNull
	@Unmodifiable
	@Contract(pure = true)
	public static List<PairValue> parse(@NotNull String source) {
		Objects.requireNonNull(source, "source");
		//attempt to parse as object
		try {
			JSONObject object = new JSONObject(source);

			return ObjectValue.transformPairs(object.toMap());
		} catch (JSONException ignored) {
		}
		//attempt to parse as array
		try {
			JSONArray array = new JSONArray(source);

			return ObjectValue.transformElements(array.toList());
		} catch (JSONException ignored) {
		}
		//attempt to parse as singleton
		return Collections.singletonList(
				PairValue.cast(source)
		);
	}

	/**
	 * Transform the given {@code elements} array into a pairs list.
	 *
	 * @param elements the array to be transformed.
	 * @return an immutable pair-value list interpretation of the given {@code elements}
	 * 		array.
	 * @throws NullPointerException if the given {@code elements} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	@NotNull
	@Unmodifiable
	@Contract(value = "_->new", pure = true)
	public static List<PairValue> transformElements(@Nullable Object @NotNull ... elements) {
		Objects.requireNonNull(elements, "elements");
		int[] index = {0};
		return Collections.unmodifiableList(
				Arrays.stream(elements)
					  .map(item -> new PairValue(
							  TokenValue.cast(item),
							  new NumberValue(index[0]++)
					  ))
					  .collect(Collectors.toList())
		);
	}

	/**
	 * Transform the given {@code elements} iterable into a pairs list.
	 *
	 * @param elements the iterable to be transformed.
	 * @return an immutable pair-value list interpretation of the given {@code elements}
	 * 		iterable.
	 * @throws NullPointerException if the given {@code elements} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	@NotNull
	@Unmodifiable
	@Contract(value = "_->new", pure = true)
	public static List<PairValue> transformElements(@NotNull Iterable<?> elements) {
		Objects.requireNonNull(elements, "elements");
		int[] index = {0};
		return Collections.unmodifiableList(
				StreamSupport
						.stream(elements.spliterator(), false)
						.map(item -> new PairValue(
								TokenValue.cast(item),
								new NumberValue(index[0]++)
						))
						.collect(Collectors.toList())
		);
	}

	/**
	 * Transform the given pairs {@code iterable} into a pairs list.
	 *
	 * @param array the iterable to be transformed.
	 * @return an immutable pair-value list interpretation of the given {@code iterable}.
	 * @throws NullPointerException if the given {@code iterable} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	@NotNull
	@Unmodifiable
	@Contract(value = "_->new", pure = true)
	public static List<PairValue> transformPairs(@Nullable Object @NotNull ... array) {
		Objects.requireNonNull(array, "array");
		return Collections.unmodifiableList(
				Arrays.stream(array)
					  .map(PairValue::cast)
					  .collect(Collectors.toList())
		);
	}

	/**
	 * Transform the given pairs {@code array} into a pairs list.
	 *
	 * @param iterable the array to be transformed.
	 * @return an immutable pair-value list interpretation of the given {@code array}.
	 * @throws NullPointerException if the given {@code array} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	@NotNull
	@Unmodifiable
	@Contract(value = "_->new", pure = true)
	public static List<PairValue> transformPairs(@NotNull Iterable<?> iterable) {
		Objects.requireNonNull(iterable, "iterable");
		return Collections.unmodifiableList(
				StreamSupport
						.stream(iterable.spliterator(), false)
						.map(PairValue::cast)
						.collect(Collectors.toList())
		);
	}

	/**
	 * Transform the given {@code pairs} map into a pairs list.
	 *
	 * @param pairs the map to be transformed.
	 * @return an immutable pair-value list interpretation of the given {@code pairs} map.
	 * @throws NullPointerException if the given {@code pairs} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	@NotNull
	@Unmodifiable
	@Contract(value = "_->new", pure = true)
	public static List<PairValue> transformPairs(@NotNull Map<?, ?> pairs) {
		Objects.requireNonNull(pairs, "pairs");
		//noinspection SimplifyStreamApiCallChains
		return Collections.unmodifiableList(
				StreamSupport
						.stream(pairs.entrySet().spliterator(), false)
						.map(PairValue::cast)
						.collect(Collectors.toList())
		);
	}

	@NotNull
	@Override
	public ObjectValue apply(@NotNull BiFunction<Memory, List<PairValue>, List<PairValue>> function) {
		Objects.requireNonNull(function, "function");
		return new ObjectValue((Function<Memory, List<PairValue>>) m ->
				ObjectValue.transformPairs(function.apply(m, this.evaluateToken(m)))
		);
	}

	@NotNull
	@Override
	public String evaluate(@NotNull Memory memory) {
		Objects.requireNonNull(memory, "memory");
		return this.evaluateToken(memory)
				   .stream()
				   .map(pair -> pair.evaluate(memory))
				   .collect(Collectors.joining(",", "{", "}"));
	}

	@NotNull
	@Unmodifiable
	@Override
	public List<PairValue> evaluateToken(@NotNull Memory memory) {
		Objects.requireNonNull(memory, "memory");
		return this.function.apply(memory);
	}

	/**
	 * Evaluate the token in this object at the given {@code key}.
	 *
	 * @param memory the memory to evaluate with.
	 * @param key    the key of the targeted token.
	 * @return the token (element) at the given {@code key}.
	 * @throws NullPointerException if the given {@code memory} or {@code key} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	@NotNull
	@Contract(pure = true)
	public Value evaluateTokenAt(@NotNull Memory memory, @NotNull Object key) {
		Objects.requireNonNull(memory, "memory");
		return this.evaluateToken(memory)
				   .stream()
				   .map(pair -> pair.evaluateToken(memory))
				   .filter(pair -> pair.getKey().evaluate(memory).equals(key))
				   .findFirst()
				   .map(Map.Entry::getValue)
				   .orElse(Value.NULL);
	}

	/**
	 * Return a value that evaluates to the value mapped to the given {@code key} in this
	 * object.
	 *
	 * @param key the key to target.
	 * @return a value that evaluates to the value mapped to the given {@code key} in this
	 * 		object.
	 * @throws NullPointerException if the given {@code key} is null.
	 * @since 0.3.0 ~2021.06.29
	 */
	@NotNull
	@Contract(pure = true)
	public Value get(@NotNull Value key) {
		Objects.requireNonNull(key, "key");
		return m -> {
			//eval key
			String keyText = key.evaluate(m);

			//stream pairs -> eval keys -> find a matching key -> map to the value -> return eval value
			return this.evaluateToken(m)
					   .stream()
					   .map(pair -> pair.evaluateToken(m))
					   .filter(pair -> pair.getKey().evaluate(m).equals(keyText))
					   .findFirst()
					   .map(pair -> pair.getValue().evaluate(m))
					   .orElse("");
		};
	}

	/**
	 * Return a new object that contains the mappings of this object but with the given
	 * {@code key} and {@code value} pair put to it.
	 *
	 * @param key   the key of the pair to be put in the returned object.
	 * @param value the value of the pair to be put in the returned object.
	 * @return an object from this object but with the given pair.
	 * @throws NullPointerException if the given {@code key} or {@code value} is null.
	 * @since 0.3.0 ~2021.06.29
	 */
	@NotNull
	@Contract(value = "_,_->new", pure = true)
	public ObjectValue put(@NotNull Value key, @NotNull Value value) {
		Objects.requireNonNull(key, "key");
		Objects.requireNonNull(value, "value");
		return new ObjectValue((Function<Memory, List<PairValue>>) m -> {
			//eval the pairs
			List<PairValue> pairs = new ArrayList<>(this.evaluateToken(m));
			//eval the key
			String keyText = key.evaluate(m);

			//attempt replace pair with the same key
			ListIterator<PairValue> iterator = pairs.listIterator();
			while (iterator.hasNext()) {
				PairValue pair = iterator.next();

				//check if same key
				if (
						pair.evaluateToken(m)
							.getKey()
							.evaluate(m)
							.equals(keyText)
				) {
					//replace and done
					iterator.set(new PairValue(
							key, value
					));
					return Collections.unmodifiableList(pairs);
				}
			}

			//no identical key, add new pair
			pairs.add(new PairValue(key, value));
			return Collections.unmodifiableList(pairs);
		});
	}
}
