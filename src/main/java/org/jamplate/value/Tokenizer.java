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

import org.jamplate.model.Value;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * The base class of all the values on this package.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.30
 */
public final class Tokenizer {
	/**
	 * Tool classes does not need instantiating.
	 *
	 * @throws AssertionError when called.
	 * @since 0.3.0 ~2021.07.03
	 */
	private Tokenizer() {
		throw new AssertionError("No instance for you!");
	}

	/**
	 * Cast the given {@code object} into a suitable value.
	 *
	 * @param object the object to be cast.
	 * @return a value casted from the given {@code object}.
	 * @throws NullPointerException if the given {@code object} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	@SuppressWarnings("OverlyComplexMethod")
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
			String source = Tokenizer.toString(object);

			if (source.isEmpty())
				return new QuoteValue(new TextValue(""));

			JSONTokener tokener = new JSONTokener(source);
			List<Value> values = new ArrayList<>();

			while (tokener.more()) {
				Object value = tokener.nextValue();

				if (value instanceof String)
					values.add(new QuoteValue(new TextValue(value)));
				else if (value.equals(JSONObject.NULL))
					values.add(new QuoteValue(new TextValue("")));
				else
					values.add(Tokenizer.cast(value));
			}

			return values.isEmpty() ?
				   TextValue.cast(source) :
				   values.size() == 1 ?
				   values.get(0) :
				   GluedValue.cast(values);
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
}
