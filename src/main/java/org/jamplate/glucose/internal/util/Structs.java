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

import org.jamplate.glucose.value.VArray;
import org.jamplate.glucose.value.VNumber;
import org.jamplate.glucose.value.VObject;
import org.jamplate.glucose.value.VPair;
import org.jamplate.memory.Value;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import static java.util.Collections.unmodifiableList;
import static org.jamplate.glucose.internal.util.Values.*;

/**
 * Utilities to deal with structural values like objects and arrays.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.07.05
 */
@SuppressWarnings("DuplicatedCode")
public final class Structs {
	/**
	 * Tool classes does not need instantiating.
	 *
	 * @throws AssertionError when called.
	 * @since 0.3.0 ~2021.07.06
	 */
	private Structs() {
		throw new AssertionError("No instance for you!");
	}

	/**
	 * Return a value that evaluates to the value at {@code key} in {@code object}.
	 *
	 * @param object the value of the object.
	 * @param key    the vale of the key.
	 * @return a value that evaluates to the value at {@code key} in {@code object}.
	 * @throws NullPointerException if the given {@code object} or {@code key} is null.
	 * @since 0.3.0 ~2021.07.06
	 */
	@NotNull
	@Contract(value = "_,_->new", pure = true)
	public static Value<Value> get(
			@NotNull VObject object,
			@NotNull Value key
	) {
		Objects.requireNonNull(object, "object");
		Objects.requireNonNull(key, "key");
		return unquote((m, v) -> {
			String k = key.eval(m);

			return object
					.getPipe()
					.eval(m)
					.stream()
					.filter(p ->
							p.getPipe()
							 .eval(m)
							 .getKey()
							 .eval(m)
							 .equals(k)
					)
					.findFirst()
					.map(p ->
							p.getPipe()
							 .eval(m)
							 .getValue()
					)
					.orElseGet(() -> text(""));
		});
	}

	/**
	 * Return a value that evaluates to the value at {@code index} in {@code array}.
	 *
	 * @param array the value of the array.
	 * @param index the vale of the index.
	 * @return a value that evaluates to the value at {@code index} in {@code array}.
	 * @throws NullPointerException if the given {@code array} or {@code index} is null.
	 * @since 0.3.0 ~2021.07.06
	 */
	@NotNull
	@Contract(value = "_,_->new", pure = true)
	public static Value<Value> get(
			@NotNull VArray array,
			@NotNull VNumber index
	) {
		Objects.requireNonNull(array, "array");
		Objects.requireNonNull(index, "index");
		return unquote((m, v) -> {
			int i = index.getPipe().eval(m).intValue();

			return array
					.getPipe()
					.eval(m)
					.stream()
					.skip(i)
					.findFirst()
					.orElse(text(""));
		});
	}

	/**
	 * Return a value that evaluates to the key of {@code pair}.
	 *
	 * @param pair the value of the pair.
	 * @return a value that evaluates to the key of {@code pair}.
	 * @throws NullPointerException if the given {@code pair} is null.
	 * @since 0.3.0 ~2021.07.06
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static Value<Value> getKey(
			@NotNull VPair pair
	) {
		Objects.requireNonNull(pair, "pair");
		return unquote((m, v) ->
				pair.getPipe().eval(m).getKey()
		);
	}

	/**
	 * Return a value that evaluates to the value of {@code pair}.
	 *
	 * @param pair the value of the pair.
	 * @return a value that evaluates to the value of {@code pair}.
	 * @throws NullPointerException if the given {@code pair} is null.
	 * @since 0.3.0 ~2021.07.06
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static Value<Value> getValue(
			@NotNull VPair pair
	) {
		Objects.requireNonNull(pair, "pair");
		return unquote((m, v) ->
				pair.getPipe().eval(m).getValue()
		);
	}

	/**
	 * Return an object that evaluates to the value of {@code object} but with {@code
	 * value} mapped to {@code key}.
	 *
	 * @param object the value of the object.
	 * @param key    the value of the key.
	 * @param value  the value of the value.
	 * @return a copy of {@code object} with {@code value} mapped to {@code key}.
	 * @throws NullPointerException if the given {@code object} or {@code key} or {@code
	 *                              value} is null.
	 * @since 0.3.0 ~2021.07.06
	 */
	@NotNull
	@Contract(value = "_,_,_->new", pure = true)
	public static VObject put(
			@NotNull VObject object,
			@NotNull Value key,
			@NotNull Value value
	) {
		Objects.requireNonNull(object, "object");
		Objects.requireNonNull(key, "key");
		Objects.requireNonNull(value, "value");
		return object.apply((m, v) -> {
			String k = key.eval(m);

			//result
			List<VPair> pairs = new ArrayList<>(v);

			//fill
			int i = IntStream
					.range(0, pairs.size())
					.filter(x ->
							pairs.get(x)
								 .getPipe()
								 .eval(m)
								 .getKey()
								 .eval(m)
								 .equals(k)
					)
					.findFirst()
					.orElseGet(() -> {
						//add if it does not exist
						pairs.add(pair(key, text("")));
						return pairs.size() - 1;
					});

			//put
			pairs.set(i, pair(
					key,
					value
			));

			return unmodifiableList(pairs);
		});
	}

	/**
	 * Return an array that evaluates to the value of {@code array} but with {@code value}
	 * set at {@code index}.
	 *
	 * @param array the value of the array.
	 * @param index the value of the index.
	 * @param value the value of the value.
	 * @return a copy of {@code array} with {@code value} set at {@code index}.
	 * @throws NullPointerException if the given {@code array} or {@code index} or {@code
	 *                              value} is null.
	 * @since 0.3.0 ~2021.07.06
	 */
	@NotNull
	@Contract(value = "_,_,_->new", pure = true)
	public static VArray set(
			@NotNull VArray array,
			@NotNull VNumber index,
			@NotNull Value value
	) {
		Objects.requireNonNull(array, "array");
		Objects.requireNonNull(index, "index");
		Objects.requireNonNull(value, "value");
		return array.apply((m, v) -> {
			int i = index.getPipe().eval(m).intValue();

			//result
			List<Value> elements = new ArrayList<>(v);

			//fill
			while (elements.size() <= i)
				elements.add(text(""));

			//set
			elements.set(i, value);

			//done
			return unmodifiableList(elements);
		});
	}

	/**
	 * Return an object that evaluates to the value of {@code object} but with {@code
	 * value} mapped to the destination of {@code keys}.
	 *
	 * @param object the value of the object.
	 * @param keys   the values of the keys.
	 * @param value  the value of the value.
	 * @return a copy of {@code object} with {@code value} mapped to the destination of
	 *        {@code keys}.
	 * @throws NullPointerException     if the given {@code object} or {@code keys} or
	 *                                  {@code value} is null.
	 * @throws IllegalArgumentException if the given {@code keys} is empty or contain
	 *                                  {@code null}.
	 * @since 0.3.0 ~2021.07.06
	 */
	@NotNull
	@Contract(value = "_,_,_->new", pure = true)
	public static VObject touch(
			@NotNull VObject object,
			@NotNull List<@NotNull Value> keys,
			@NotNull Value value
	) {
		Objects.requireNonNull(object, "object");
		Objects.requireNonNull(keys, "keys");
		Objects.requireNonNull(value, "value");
		//noinspection ZeroLengthArrayAllocation
		Value[] ks = keys.toArray(new Value[0]);

		for (Value key : ks)
			if (key == null)
				throw new IllegalArgumentException(
						"null key"
				);
		if (ks.length == 0)
			throw new IllegalArgumentException(
					"Empty keys list"
			);

		Value key = ks[0];

		if (ks.length == 1)
			return Structs.put(
					object,
					key,
					value
			);

		return object.apply((m, v) -> {
			String k = key.eval(m);

			//result
			List<VPair> pairs = new ArrayList<>(v);

			//fill
			int i = IntStream
					.range(0, pairs.size())
					.filter(j ->
							pairs.get(j)
								 .getPipe()
								 .eval(m)
								 .getKey()
								 .eval(m)
								 .equals(k)
					)
					.findFirst()
					.orElseGet(() -> {
						//add if it does not exist
						pairs.add(pair(key, text("")));
						return pairs.size() - 1;
					});

			//vars
			Value mk = ks[1];
			Value mv = pairs.get(i).getPipe().eval(m).getValue();

			if (mk instanceof VNumber && !(mv instanceof VObject))
				pairs.set(i, pair(key,
						Structs.touch(
								array(mv),
								Arrays.asList(ks).subList(1, ks.length),
								value
						))
				);
			else
				pairs.set(i, pair(key,
						Structs.touch(
								object(mv),
								Arrays.asList(ks).subList(1, ks.length),
								value
						)
				));

			//done
			return unmodifiableList(pairs);
		});
	}

	/**
	 * Return an array that evaluates to the value of {@code array} but with {@code value}
	 * set at the destination of {@code keys}.
	 *
	 * @param array the value of the array.
	 * @param keys  the values of the keys.
	 * @param value the value of the value.
	 * @return a copy of {@code array} with {@code value} set at the destination of {@code
	 * 		keys}.
	 * @throws NullPointerException     if the given {@code array} or {@code keys} or
	 *                                  {@code value} is null.
	 * @throws IllegalArgumentException if the given {@code keys} is empty or contain
	 *                                  {@code null} or the first value is not {@link
	 *                                  VNumber}.
	 * @since 0.3.0 ~2021.07.06
	 */
	@NotNull
	@Contract(value = "_,_,_->new", pure = true)
	public static VArray touch(
			@NotNull VArray array,
			@NotNull List<@NotNull Value> keys,
			@NotNull Value value
	) {
		Objects.requireNonNull(array, "array");
		Objects.requireNonNull(keys, "keys");
		Objects.requireNonNull(value, "value");
		Value[] ks = keys
				.stream()
				.peek(Objects::requireNonNull)
				.toArray(Value[]::new);

		if (ks.length == 0)
			throw new IllegalArgumentException(
					"Empty keys list"
			);
		if (!(ks[0] instanceof VNumber))
			throw new IllegalArgumentException(
					"First key is not an index"
			);

		VNumber index = (VNumber) ks[0];

		if (ks.length == 1)
			return Structs.set(
					array,
					index,
					value
			);

		return array.apply((m, v) -> {
			int i = index.getPipe().eval(m).intValue();

			//result
			List<Value> elements = new ArrayList<>(v);

			//fill
			while (elements.size() <= i)
				elements.add(text(""));

			//vars
			Value mk = ks[1];
			Value mv = elements.get(i);

			//touch
			if (mk instanceof VNumber && !(mv instanceof VObject))
				elements.set(i,
						Structs.touch(
								array(mv),
								Arrays.asList(ks).subList(1, ks.length),
								value
						)
				);
			else
				elements.set(i,
						Structs.touch(
								object(mv),
								Arrays.asList(ks).subList(1, ks.length),
								value
						)
				);

			//done
			return unmodifiableList(elements);
		});
	}
}
