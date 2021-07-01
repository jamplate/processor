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
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.Arrays;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * The base class of all the values on this package.
 *
 * @param <T> the type of the raw token.
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.30
 */
public abstract class TokenValue<T> implements Value {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -502980380564606240L;

	/**
	 * Cast the given {@code object} into a suitable value.
	 *
	 * @param object the object to be cast.
	 * @return a value casted from the given {@code object}.
	 * @throws NullPointerException if the given {@code object} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	@NotNull
	@Contract(pure = true)
	public static Value cast(@Nullable Object object) {
		//it
		if (object instanceof Value)
			return (Value) object;
		//object
		if (object instanceof Map || object instanceof JSONObject)
			return ObjectValue.cast(object);
		//pair
		if (object instanceof Map.Entry)
			return PairValue.cast(object);
		//array
		if (object instanceof Iterable || object instanceof Object[])
			return ArrayValue.cast(object);
		//number
		if (object instanceof Number)
			return NumberValue.cast(object);
		//boolean
		if (object instanceof Boolean)
			return BooleanValue.cast(object);

		//parse -> re-try
		try {
			String source = TokenValue.toString(object);
			JSONTokener tokener = new JSONTokener(source);
			Object value = tokener.nextValue();

			if (value instanceof String)
				return new QuoteValue(new TextValue(value));

			return TokenValue.cast(value);
		} catch (JSONException ignored) {
		}

		//unknown
		return TextValue.cast(object);
	}

	/**
	 * Return a string representation of the given {@code object}.
	 *
	 * @param object the object to get a string representation of.
	 * @return the string representation of the given {@code object}.
	 * @since 0.3.0 ~2021.07.01
	 */
	@NotNull
	@Contract(pure = true)
	public static String toString(@Nullable Object object) {
		if (object instanceof Object[])
			return Arrays.deepToString((Object[]) object);
		if (object instanceof boolean[])
			return Arrays.toString((boolean[]) object);
		if (object instanceof byte[])
			return Arrays.toString((byte[]) object);
		if (object instanceof char[])
			return Arrays.toString((char[]) object);
		if (object instanceof double[])
			return Arrays.toString((double[]) object);
		if (object instanceof float[])
			return Arrays.toString((float[]) object);
		if (object instanceof int[])
			return Arrays.toString((int[]) object);
		if (object instanceof long[])
			return Arrays.toString((long[]) object);
		if (object instanceof short[])
			return Arrays.toString((short[]) object);

		return object == null ? "" : object.toString();
	}

	/**
	 * Return a token value of the same type as this token value that evaluates its token
	 * to the result of invoking the given {@code function} with the result of evaluating
	 * the token of this.
	 *
	 * @param function the function to be applied to the returned token value.
	 * @return a token value of the same type as this as described above.
	 * @throws NullPointerException if the given {@code function} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	@NotNull
	@Contract
	public abstract TokenValue apply(@NotNull BiFunction<Memory, T, T> function);

	/**
	 * Evaluate the raw token of this value.
	 *
	 * @param memory the memory to evaluate with.
	 * @return the raw token of this value.
	 * @since 0.3.0 ~2021.07.01
	 */
	@NotNull
	@Contract(pure = true)
	public abstract T evaluateToken(@NotNull Memory memory);
}