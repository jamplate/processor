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
package org.jamplate.glucose.internal.util;

import org.jamplate.glucose.value.*;
import org.jamplate.memory.Pipe;
import org.jamplate.memory.Value;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.Collections.singletonList;

/**
 * Methods to generate good values from any object.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.07.05
 */
@SuppressWarnings({"OverlyComplexClass", "UnqualifiedInnerClassAccess",
				   "ClassWithTooManyMethods"
})
public final class Values {
	/**
	 * Tool classes does not need instantiating.
	 *
	 * @throws AssertionError when called.
	 * @since 0.3.0 ~2021.07.06
	 */
	private Values() {
		throw new AssertionError("No instance for you!");
	}

	// Array -----------------------------------------------------------------------------

	/**
	 * Construct a new array value with the given {@code elements} values.
	 *
	 * @param elements the values of the elements of the constructed array value.
	 * @return a new array value with the given {@code elements}.
	 * @throws NullPointerException     if the given {@code elements} is null.
	 * @throws IllegalArgumentException if the given {@code elements} contain {@code
	 *                                  null}.
	 * @since 0.3.0 ~2021.07.06
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static VArray arr(@NotNull Value @NotNull ... elements) {
		return Values.array(Arrays.asList(elements));
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
	@Contract(value = "_->new", pure = true)
	public static VArray array(@Nullable Object object) {
		//it
		if (object instanceof VArray)
			return (VArray) object;
		//raw
		if (object instanceof Iterable) {
			Iterable iterable = (Iterable) object;
			List<Value> elements = Values.fromElementsToElements(iterable);
			return Values.array(elements);
		}
		if (object instanceof Object[]) {
			Object[] array = (Object[]) object;
			List<Value> elements = Values.fromElementsToElements(Arrays.asList(array));
			return Values.array(elements);
		}
		//raw object
		if (object instanceof Map) {
			Map map = (Map) object;
			List<Value> elements = Values.fromPairsToElements(map.entrySet());
			return Values.array(elements);
		}
		if (object instanceof JSONObject) {
			JSONObject json = (JSONObject) object;
			List<Value> elements = Values.fromPairsToElements(json.toMap().entrySet());
			return Values.array(elements);
		}
		//wrap object
		if (object instanceof VObject) {
			VObject obj = (VObject) object;
			return Values.array((m, v) ->
					Values.fromPairsToElements(obj.getPipe().eval(m))
			);
		}
		//wrap
		if (object instanceof Value) {
			Value value = (Value) object;
			return Values.array((m, v) ->
					Values.parseElements(value.eval(m))
			);
		}

		//parse
		String string = Values.stringify(object);
		List<Value> elements = Values.parseElements(string);
		return Values.array(elements);
	}

	/**
	 * Construct a new array value with the given {@code elements}.
	 *
	 * @param elements the elements of the array.
	 * @return a new array value with the given {@code elements}.
	 * @throws NullPointerException if the given {@code elements} is null.
	 * @since 0.3.0 ~2021.07.06
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static VArray array(@Nullable Object @NotNull ... elements) {
		Objects.requireNonNull(elements, "elements");
		return Values.array((Object) elements);
	}

	/**
	 * Construct a new array value with the given {@code elements} values.
	 *
	 * @param elements the values of the elements of the constructed array value.
	 * @return a new array value with the given {@code elements}.
	 * @throws NullPointerException     if the given {@code elements} is null.
	 * @throws IllegalArgumentException if the given {@code elements} contain {@code
	 *                                  null}.
	 * @since 0.3.0 ~2021.07.06
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static VArray array(@NotNull List<@NotNull Value> elements) {
		Objects.requireNonNull(elements, "elements");
		//noinspection ZeroLengthArrayAllocation
		Value[] array = elements.toArray(new Value[0]);
		for (Value value : array)
			if (value == null)
				throw new IllegalArgumentException(
						"Null value"
				);
		return Values.array((m, v) -> Arrays.asList(array));
	}

	/**
	 * Construct a new array value with the given {@code pipe}.
	 *
	 * @param pipe the pipe for the constructed array value.
	 * @return a new array value with the given {@code pipe}.
	 * @throws NullPointerException if the given {@code pipe} is null.
	 * @since 0.3.0 ~2021.07.06
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static VArray array(@NotNull Pipe<Object, List<Value>> pipe) {
		return new VArray(pipe);
	}

	// bool ------------------------------------------------------------------------------

	/**
	 * Cast the given {@code object} into a boolean value.
	 *
	 * @param object the object to be cast.
	 * @return a new boolean value evaluating to a boolean interpretation of the given
	 *        {@code object}.
	 * @since 0.3.0 ~2021.07.01
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static VBoolean bool(@Nullable Object object) {
		//it
		if (object instanceof VBoolean)
			return (VBoolean) object;
		//raw
		if (object instanceof Boolean)
			//noinspection AutoUnboxing
			return Values.bool((boolean) object);
		//wrap
		if (object instanceof Value) {
			Value value = (Value) object;
			return Values.bool((m, v) ->
					Values.parseBoolean(value.eval(m))
			);
		}

		//parse
		String string = Values.stringify(object);
		boolean bool = Values.parseBoolean(string);
		return Values.bool(bool);
	}

	/**
	 * Construct a new boolean value with the given {@code state}.
	 *
	 * @param state the state the returned boolean value will result to.
	 * @return a new boolean value with the given {@code state}.
	 * @since 0.3.0 ~2021.07.06
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static VBoolean bool(boolean state) {
		return Values.bool((m, v) -> state);
	}

	/**
	 * Construct a new boolean value with the given {@code pipe}.
	 *
	 * @param pipe the pipe for the constructed boolean value.
	 * @return a new boolean value with the given {@code pipe}.
	 * @throws NullPointerException if the given {@code pipe} is null.
	 * @since 0.3.0 ~2021.07.06
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static VBoolean bool(@NotNull Pipe<Object, Boolean> pipe) {
		return new VBoolean(pipe);
	}

	// glue ------------------------------------------------------------------------------

	/**
	 * Construct a new glued value that evaluates to the given raw {@code values}.
	 *
	 * @param values the raw array of the values the constructed valued will have.
	 * @return a new glue gluing the given {@code values}.
	 * @throws NullPointerException     if the given {@code pairs} is or contain null.
	 * @throws IllegalArgumentException if the given {@code values} contain null.
	 * @since 0.3.0 ~2021.07.06
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static VGlue glu(@NotNull Value @NotNull ... values) {
		return Values.glue(Arrays.asList(values));
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
	@Contract(value = "_->new", pure = true)
	public static VGlue glue(@Nullable Object object) {
		//it
		if (object instanceof VGlue)
			return (VGlue) object;
		//raw
		if (object instanceof Iterable) {
			Iterable iterable = (Iterable) object;
			List<Value> values = Values.fromValuesToValues(iterable);
			return Values.glue(values);
		}
		if (object instanceof Object[]) {
			Object[] array = (Object[]) object;
			List<Value> values = Values.fromValuesToValues(Arrays.asList(array));
			return Values.glue(values);
		}
		//wrap
		if (object instanceof Value) {
			Value value = (Value) object;
			return Values.glue((m, v) ->
					singletonList(value)
			);
		}

		//singleton
		Value value = Values.value(object);
		return Values.glue(singletonList(value));
	}

	/**
	 * Construct a new glued value that evaluates to the given raw {@code values}.
	 *
	 * @param values the raw array of the values the constructed valued will have.
	 * @return a new glue gluing the given {@code values}.
	 * @throws NullPointerException     if the given {@code pairs} is or contain null.
	 * @throws IllegalArgumentException if the given {@code values} contain null.
	 * @since 0.3.0 ~2021.07.01
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static VGlue glue(@NotNull List<@NotNull Value> values) {
		Objects.requireNonNull(values, "values");
		//noinspection ZeroLengthArrayAllocation
		Value[] array = values.toArray(new Value[0]);
		for (Value value : array)
			if (value == null)
				throw new IllegalArgumentException(
						"Null value"
				);
		return Values.glue((m, v) -> Arrays.asList(array));
	}

	/**
	 * Construct a new glue value with the given {@code pipe}.
	 *
	 * @param pipe the pipe for the constructed glue value.
	 * @return a new glue value with the given {@code pipe}.
	 * @throws NullPointerException if the given {@code pipe} is null.
	 * @since 0.3.0 ~2021.07.06
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static VGlue glue(@NotNull Pipe<Object, List<Value>> pipe) {
		return new VGlue(pipe);
	}

	// number ----------------------------------------------------------------------------

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
	@Contract(value = "_->new", pure = true)
	public static VNumber number(@Nullable Object object) {
		//it
		if (object instanceof VNumber)
			return (VNumber) object;
		//raw
		if (object instanceof Number)
			return Values.number((Number) object);
		//wrap
		if (object instanceof Value) {
			Value value = (Value) object;
			return Values.number((m, v) ->
					Values.parseNumber(value.eval(m))
			);
		}

		//parse
		String string = Values.stringify(object);
		double number = Values.parseNumber(string);
		return Values.number(number);
	}

	/**
	 * Construct a new number that evaluates to the given {@code number}.
	 *
	 * @param number the number the returned number value will evaluate to.
	 * @return a new number value for the given {@code number}.
	 * @throws NullPointerException if the given {@code number} is null.
	 * @since 0.3.0 ~2021.07.06
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static VNumber number(@NotNull Number number) {
		Objects.requireNonNull(number, "number");
		return Values.number((m, v) -> number);
	}

	/**
	 * Construct a new number value with the given {@code pipe}.
	 *
	 * @param pipe the pipe for the constructed number value.
	 * @return a new number value with the given {@code pipe}.
	 * @throws NullPointerException if the given {@code pipe} is null.
	 * @since 0.3.0 ~2021.07.06
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static VNumber number(@NotNull Pipe<Object, Number> pipe) {
		return new VNumber(pipe);
	}

	// object ----------------------------------------------------------------------------

	/**
	 * Construct a new object value from the given {@code pairs} values list.
	 *
	 * @param pairs the pairs for the returned object value.
	 * @return a new object value with the given {@code pairs}.
	 * @throws NullPointerException     if the given {@code pairs} is null.
	 * @throws IllegalArgumentException if the given {@code pairs} contains a null.
	 * @since 0.3.0 ~2021.07.06
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static VObject obj(@NotNull VPair @NotNull ... pairs) {
		return Values.object(Arrays.asList(pairs));
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
	@Contract(value = "_->new", pure = true)
	public static VObject object(@Nullable Object object) {
		//it
		if (object instanceof VObject)
			return (VObject) object;
		//raw
		if (object instanceof Map) {
			Map map = (Map) object;
			List<VPair> pairs = Values.fromPairsToPairs(map.entrySet());
			return Values.object(pairs);
		}
		if (object instanceof JSONObject) {
			JSONObject json = (JSONObject) object;
			List<VPair> pairs = Values.fromPairsToPairs(json.toMap().entrySet());
			return Values.object(pairs);
		}
		//raw array
		if (object instanceof Iterable) {
			Iterable iterable = (Iterable) object;
			List<VPair> pairs = Values.fromElementsToPairs(iterable);
			return Values.object(pairs);
		}
		if (object instanceof Object[]) {
			Object[] array = (Object[]) object;
			List<VPair> pairs = Values.fromElementsToPairs(Arrays.asList(array));
			return Values.object(pairs);
		}
		//wrap array
		if (object instanceof VArray) {
			VArray array = (VArray) object;
			return Values.object((m, v) ->
					Values.fromElementsToPairs(array.getPipe().eval(m))
			);
		}
		//wrap
		if (object instanceof Value) {
			Value value = (Value) object;
			return Values.object((m, v) ->
					Values.parsePairs(value.eval(m))
			);
		}

		//parse
		String string = Values.stringify(object);
		List<VPair> pairs = Values.parsePairs(string);
		return Values.object(pairs);
	}

	/**
	 * Construct a new object value from the given {@code pairs} values list.
	 *
	 * @param pairs the pairs for the returned object value.
	 * @return a new object value with the given {@code pairs}.
	 * @throws NullPointerException     if the given {@code pairs} is null.
	 * @throws IllegalArgumentException if the given {@code pairs} contains a null.
	 * @since 0.3.0 ~2021.07.06
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static VObject object(@NotNull List<@NotNull VPair> pairs) {
		Objects.requireNonNull(pairs, "pairs");
		//noinspection ZeroLengthArrayAllocation
		VPair[] array = pairs.toArray(new VPair[0]);
		for (VPair pair : array)
			if (pair == null)
				throw new IllegalArgumentException(
						"Null value"
				);
		return Values.object((m, v) -> Arrays.asList(array));
	}

	/**
	 * Construct a new object value with the given {@code pipe}.
	 *
	 * @param pipe the pipe for the constructed object value.
	 * @return a new object value with the given {@code pipe}.
	 * @throws NullPointerException if the given {@code pipe} is null.
	 * @since 0.3.0 ~2021.07.06
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static VObject object(@NotNull Pipe<Object, List<VPair>> pipe) {
		return new VObject(pipe);
	}

	// pair ------------------------------------------------------------------------------

	/**
	 * Cast the given {@code object} into a pair.
	 *
	 * @param object the object to be cast.
	 * @return a pair evaluating to a pair interpretation of the given {@code object}.
	 * @since 0.3.0 ~2021.07.01
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static VPair pair(@Nullable Object object) {
		//it
		if (object instanceof VPair)
			return (VPair) object;
		//raw
		if (object instanceof Entry) {
			Entry entry = (Entry) object;
			Entry<Value, Value> pair = Values.fromPairToPair(entry);
			return Values.pair(pair);
		}
		//wrap
		if (object instanceof Value) {
			Value value = (Value) object;
			return Values.pair((m, v) ->
					Values.parsePair(value.eval(m))
			);
		}

		//parse
		String string = Values.stringify(object);
		Entry<Value, Value> pair = Values.parsePair(string);
		return Values.pair(pair);
	}

	/**
	 * Construct a new pair value with the given {@code key} and {@code value}.
	 *
	 * @param key   the key.
	 * @param value the value.
	 * @return a pair value that evaluates to the given {@code key} and {@code value}.
	 * @throws NullPointerException if the given {@code key} or {@code value} is null.
	 * @since 0.3.0 ~2021.07.06
	 */
	@NotNull
	@Contract(value = "_,_->new", pure = true)
	public static VPair pair(@Nullable Object key, @Nullable Object value) {
		return Values.pair(new AbstractMap.SimpleEntry<>(key, value));
	}

	/**
	 * Construct a new pair value that evaluates to the given raw {@code pair}.
	 *
	 * @param pair the raw pair of the constructed pair value.
	 * @return a new pair value that evaluates to the given {@code pair}.
	 * @throws NullPointerException if the given {@code pair} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static VPair pair(@NotNull Map.Entry<Value, Value> pair) {
		Objects.requireNonNull(pair, "pair");
		Entry<Value, Value> entry = new AbstractMap.SimpleImmutableEntry<>(pair);
		return Values.pair((m, v) -> entry);
	}

	/**
	 * Construct a new pair value with the given {@code pipe}.
	 *
	 * @param pipe the pipe for the constructed pair value.
	 * @return a new pair value with the given {@code pipe}.
	 * @throws NullPointerException if the given {@code pipe} is null.
	 * @since 0.3.0 ~2021.07.06
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static VPair pair(@NotNull Pipe<Object, Entry<Value, Value>> pipe) {
		return new VPair(pipe);
	}

	// quote -----------------------------------------------------------------------------

	/**
	 * Cast the given {@code object} into a quoted text value.
	 *
	 * @param object the object to be cast.
	 * @return a new text value evaluating to the value of the given {@code object}.
	 * @throws NullPointerException if the given {@code object} is null.
	 * @since 0.3.0 ~2021.06.30
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static VQuote quote(@Nullable Object object) {
		//it
		if (object instanceof VQuote)
			return (VQuote) object;
		//raw
		if (object instanceof Value) {
			Value value = (Value) object;
			return Values.quote(value);
		}

		//turn into value
		Value value = Values.value(object);
		return Values.quote(value);
	}

	/**
	 * Construct a new quoted text value that evaluate to the evaluation of the given
	 * {@code value}.
	 *
	 * @param value the value of the constructed value.
	 * @return a new quote value wrapping the given {@code value}.
	 * @throws NullPointerException if the given {@code value} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static VQuote quote(@NotNull Value<?> value) {
		Objects.requireNonNull(value, "value");
		return Values.quote((m, v) -> value);
	}

	/**
	 * Construct a new quote value with the given {@code pipe}.
	 *
	 * @param pipe the pipe for the constructed quote value.
	 * @return a new quote value with the given {@code pipe}.
	 * @throws NullPointerException if the given {@code pipe} is null.
	 * @since 0.3.0 ~2021.07.06
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static VQuote quote(@NotNull Pipe<Object, Value> pipe) {
		return new VQuote(pipe);
	}

	// text ------------------------------------------------------------------------------

	/**
	 * Cast the given {@code object} into a text value.
	 *
	 * @param object the object to be cast.
	 * @return a new text value evaluating to the value of the given {@code object}.
	 * @throws NullPointerException if the given {@code object} is null.
	 * @since 0.3.0 ~2021.06.30
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static VText text(@Nullable Object object) {
		//it
		if (object instanceof VText)
			return (VText) object;
		//wrap
		if (object instanceof Value) {
			Value value = (Value) object;
			return Values.text((m, v) ->
					value.eval(m)
			);
		}

		//stringify
		String string = Values.stringify(object);
		return Values.text(string);
	}

	/**
	 * Construct a new text value that evaluates to the given {@code text}.
	 *
	 * @param text the text for the returned text value to evaluate to.
	 * @return a new text value that evaluates to the given {@code text}.
	 * @throws NullPointerException if the given {@code text} is null.
	 * @since 0.3.0 ~2021.07.06
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static VText text(@NotNull String text) {
		Objects.requireNonNull(text, "text");
		return Values.text((m, v) -> text);
	}

	/**
	 * Construct a new text value with the given {@code pipe}.
	 *
	 * @param pipe the pipe for the constructed text value.
	 * @return a new text value with the given {@code pipe}.
	 * @throws NullPointerException if the given {@code pipe} is null.
	 * @since 0.3.0 ~2021.07.06
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static VText text(@NotNull Pipe<Object, String> pipe) {
		return new VText(pipe);
	}

	// unquote ---------------------------------------------------------------------------

	/**
	 * Cast the given {@code object} into an unquoted text value.
	 *
	 * @param object the object to be cast.
	 * @return a new text value evaluating to the value of the given {@code object}.
	 * @throws NullPointerException if the given {@code object} is null.
	 * @since 0.3.0 ~2021.06.30
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static VUnquote unquote(@Nullable Object object) {
		//it
		if (object instanceof VUnquote)
			return (VUnquote) object;
		//wrap
		if (object instanceof Value) {
			Value value = (Value) object;
			return Values.unquote(value);
		}

		//turn into value
		Value value = Values.value(object);
		return Values.unquote(value);
	}

	/**
	 * Construct a new unquote value that evaluate to the evaluation of the given {@code
	 * value}.
	 *
	 * @param value the value of the constructed unquote value.
	 * @return a new unquote value de-wrapping the given {@code value}.
	 * @throws NullPointerException if the given {@code value} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static VUnquote unquote(@NotNull Value<?> value) {
		Objects.requireNonNull(value, "value");
		return Values.unquote((m, v) -> value);
	}

	/**
	 * Construct a new unquote value with the given {@code pipe}.
	 *
	 * @param pipe the pipe for the constructed unquote value.
	 * @return a new unquote value with the given {@code pipe}.
	 * @throws NullPointerException if the given {@code pipe} is null.
	 * @since 0.3.0 ~2021.07.06
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static VUnquote unquote(@NotNull Pipe<Object, Value> pipe) {
		return new VUnquote(pipe);
	}

	// auto ------------------------------------------------------------------------------

	/**
	 * Cast the given {@code object} into a suitable value.
	 *
	 * @param object the object to be cast.
	 * @return a value casted from the given {@code object}.
	 * @throws NullPointerException if the given {@code object} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static Value value(@Nullable Object object) {
		//it
		if (object instanceof Value)
			return (Value) object;
		//object
		if (object instanceof Map || object instanceof JSONObject)
			return Values.object(object);
		//pair
		if (object instanceof Map.Entry)
			return Values.pair(object);
		//array
		if (object instanceof Iterable || object instanceof Object[])
			return Values.array(object);
		//number
		if (object instanceof Number)
			return Values.number(object);
		//boolean
		if (object instanceof Boolean)
			return Values.bool(object);

		//parse -> re-try
		String source = Values.stringify(object);

		JSONTokener tokener = new JSONTokener(source);
		List<Value> values = new ArrayList<>();

		while (tokener.more())
			try {
				Object value = tokener.nextValue();

				if (value instanceof String)
					values.add(Values.quote(Values.text(value)));
				else if (value.equals(JSONObject.NULL))
					values.add(Values.quote(Values.text("")));
				else
					values.add(Values.value(value));
			} catch (JSONException ignored) {
				break;
			}

		return values.isEmpty() ?
			   Values.text(source) :
			   values.size() == 1 ?
			   values.get(0) :
			   Values.glue(values);
	}

	// internal transform ----------------------------------------------------------------

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
	@Contract(value = "_->new", pure = true)
	private static List<Value> fromElementsToElements(@NotNull Iterable<?> elements) {
		Objects.requireNonNull(elements, "elements");
		return Collections.unmodifiableList(
				StreamSupport
						.stream(elements.spliterator(), false)
						.map(Values::value)
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
	private static List<VPair> fromElementsToPairs(@NotNull Iterable<?> elements) {
		Objects.requireNonNull(elements, "elements");
		int[] index = {0};
		return Collections.unmodifiableList(
				StreamSupport
						.stream(elements.spliterator(), false)
						.map(item -> Values.pair(
								Values.value(item),
								Values.number(index[0]++)
						))
						.collect(Collectors.toList())
		);
	}

	/**
	 * Transform the given {@code pair} entry into a pair values entry.
	 *
	 * @param pair the entry to be transformed.
	 * @return an immutable values entry interpretation of the given {@code pair} entry.
	 * @throws NullPointerException if the given {@code pair} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	@NotNull
	@Unmodifiable
	@Contract(value = "_->new", pure = true)
	private static Entry<Value, Value> fromPairToPair(@NotNull Entry<?, ?> pair) {
		Objects.requireNonNull(pair, "pair");
		return new AbstractMap.SimpleImmutableEntry<>(
				Values.value(pair.getKey()),
				Values.value(pair.getValue())
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
	@Contract(value = "_->new", pure = true)
	private static List<Value> fromPairsToElements(@NotNull Iterable<?> pairs) {
		Objects.requireNonNull(pairs, "pairs");
		return Collections.unmodifiableList(
				StreamSupport
						.stream(pairs.spliterator(), false)
						.map(Values::pair)
						.map(Structs::getKey)
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
	private static List<VPair> fromPairsToPairs(@NotNull Iterable<?> iterable) {
		Objects.requireNonNull(iterable, "iterable");
		return Collections.unmodifiableList(
				StreamSupport
						.stream(iterable.spliterator(), false)
						.map(Values::pair)
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
	@Contract(value = "_->new", pure = true)
	private static List<Value> fromValuesToValues(@NotNull Iterable<?> values) {
		Objects.requireNonNull(values, "values");
		return Collections.unmodifiableList(
				StreamSupport
						.stream(values.spliterator(), false)
						.map(Values::value)
						.collect(Collectors.toList())
		);
	}

	// internal parse --------------------------------------------------------------------

	/**
	 * Parse the given {@code source} text into a state.
	 *
	 * @param source the source to be parsed.
	 * @return a state from parsing the given {@code source}.
	 * @throws NullPointerException if the given {@code source} is null.
	 * @since 0.3.0 ~2021.06.30
	 */
	@Contract(pure = true)
	private static boolean parseBoolean(@NotNull String source) {
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
	@Contract(value = "_->new", pure = true)
	private static List<Value> parseElements(@NotNull String source) {
		Objects.requireNonNull(source, "source");
		//attempt to parse as array
		try {
			JSONArray array = new JSONArray(source);

			return Values.fromElementsToElements(array.toList());
		} catch (JSONException ignored) {
		}
		//attempt to parse as object
		try {
			JSONObject object = new JSONObject(source);

			return Values.fromPairsToElements(object.toMap().entrySet());
		} catch (JSONException ignored) {
		}
		//attempt to parse as singleton
		return source.trim().isEmpty() ?
			   Collections.emptyList() :
			   singletonList(
					   Values.value(source)
			   );
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
	private static double parseNumber(@NotNull String source) {
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

	/**
	 * Parse the given {@code source} into a pair.
	 *
	 * @param source the source to be parsed.
	 * @return a pair from parsing the given {@code source}.
	 * @throws NullPointerException if the given {@code source} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	@NotNull
	@Unmodifiable
	@Contract(value = "_->new", pure = true)
	private static Entry<Value, Value> parsePair(@NotNull String source) {
		Objects.requireNonNull(source, "source");
		//attempt to parse as singleton object
		try {
			JSONObject object = new JSONObject("{" + source + "}");

			return object.toMap()
						 .entrySet()
						 .stream()
						 .map(e -> new AbstractMap.SimpleImmutableEntry<>(
								 Values.value(e.getKey()),
								 Values.value(e.getValue())
						 ))
						 .findFirst()
						 .orElse(new AbstractMap.SimpleImmutableEntry<>(
								 Value.NULL,
								 Value.NULL
						 ));
		} catch (JSONException ignored) {
		}
		//attempt to parse as marker
		return new AbstractMap.SimpleImmutableEntry<>(
				Values.text(source),
				Values.text("")
		);
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
	@Contract(value = "_->new", pure = true)
	private static List<VPair> parsePairs(@NotNull String source) {
		Objects.requireNonNull(source, "source");
		//attempt to parse as object
		try {
			JSONObject object = new JSONObject(source);

			return Values.fromPairsToPairs(object.toMap().entrySet());
		} catch (JSONException ignored) {
		}
		//attempt to parse as array
		try {
			JSONArray array = new JSONArray(source);

			return Values.fromElementsToPairs(array.toList());
		} catch (JSONException ignored) {
		}
		//attempt to parse as singleton
		return source.trim().isEmpty() ?
			   Collections.emptyList() :
			   singletonList(
					   Values.pair(source)
			   );
	}

	// internal stringify ----------------------------------------------------------------

	/**
	 * Return a string representation of the given {@code object}.
	 *
	 * @param object the object to get a string representation of.
	 * @return the string representation of the given {@code object}.
	 * @since 0.3.0 ~2021.07.01
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	private static String stringify(@Nullable Object object) {
		//// FIXME: 2021.07.06 better stringify
		if (
				object instanceof boolean[] ||
				object instanceof byte[] ||
				object instanceof char[] ||
				object instanceof double[] ||
				object instanceof float[] ||
				object instanceof int[] ||
				object instanceof long[] ||
				object instanceof short[]
		)
			return JSONObject.valueToString(object);
		if (object instanceof Object[])
			return Arrays.deepToString((Object[]) object);

		return object == null ? "" : object.toString();
	}
}
