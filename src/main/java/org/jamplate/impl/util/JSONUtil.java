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
package org.jamplate.impl.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * JSON utility methods.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.06.01
 */
public final class JSONUtil {
	/**
	 * Utility classes must not be initialized.
	 *
	 * @throws AssertionError when called.
	 * @since 0.2.0 ~2021.06.01
	 */
	private JSONUtil() {
		throw new AssertionError("No instance for you");
	}

	/**
	 * Convert the given {@code object} into a json array.
	 *
	 * @param object the object.
	 * @return a json array representing the given {@code object}.
	 * @throws NullPointerException if the given {@code object} is null.
	 * @since 0.2.0 ~2021.06.01
	 */
	@NotNull
	@Contract(pure = true)
	public static JSONArray asJSONArray(@NotNull Object object) {
		Objects.requireNonNull(object, "object");

		//'object' is a 'json array' => return object
		if (object instanceof JSONArray) {
			JSONArray jsonArray = (JSONArray) object;

			return jsonArray;
		}

		//'object' is a 'json object' => convert into 'json array' then return.
		if (object instanceof JSONObject) {
			JSONObject jsonObject = (JSONObject) object;

			return jsonObject.names();
		}

		//'object' is not a 'json array' nor a 'json object' => attempt parse
		try {
			//can parse 'object' to 'json array' => parse then return.
			return new JSONArray(object.toString());
		} catch (JSONException e1) {
			try {
				//can parse 'object' to 'json object' => parse then convert into 'json array' then recursion.
				return JSONUtil.asJSONArray(new JSONObject(object.toString()));
			} catch (JSONException e2) {
				//cannot parse 'object' => return empty array
				return new JSONArray();
			}
		}
	}

	/**
	 * Convert the given {@code object} into a json object.
	 *
	 * @param object the object.
	 * @return a json object representing the given {@code object}.
	 * @throws NullPointerException if the given {@code object} is null.
	 * @since 0.2.0 ~2021.06.01
	 */
	@NotNull
	@Contract(pure = true)
	public static JSONObject asJSONObject(@NotNull Object object) {
		Objects.requireNonNull(object, "object");

		//'object' is a 'json object' => return object
		if (object instanceof JSONObject) {
			JSONObject jsonObject = (JSONObject) object;

			return jsonObject;
		}

		//'object' is a 'json array' => convert into 'json object' then return.
		if (object instanceof JSONArray) {
			JSONArray jsonArray = (JSONArray) object;

			return jsonArray.toJSONObject(new JSONArray(
					IntStream.range(0, jsonArray.length())
							 .mapToObj(Integer::toString)
							 .collect(Collectors.toList())
			));
		}

		//'object' is not a 'json object' nor a 'json array' => attempt parse
		try {
			//can parse 'object' to 'json object' => parse then return.
			return new JSONObject(object.toString());
		} catch (JSONException e1) {
			try {
				//can parse 'object' to 'json array' => parse then convert into 'json object' then recursion.
				return JSONUtil.asJSONObject(new JSONArray(object.toString()));
			} catch (JSONException e2) {
				//cannot parse 'object' => return empty 'json object'
				return new JSONObject();
			}
		}
	}

	/**
	 * Assuming the given {@code object} is a {@link JSONArray} or a {@link JSONObject},
	 * get the value stored at the given nesting {@code keys}. If the given {@code object}
	 * is not a {@link JSONArray} nor a {@link JSONObject} or a node in the middle is not
	 * of these types, an empty string will be returned.
	 *
	 * @param object the object to get from.
	 * @param keys   the nesting keys to get the value from.
	 * @return the value nested with the given {@code keys} in the given {@code object}.
	 * @throws NullPointerException if the given {@code object} or {@code keys} is null.
	 * @since 0.2.0 ~2021.06.01
	 */
	@NotNull
	@Contract(pure = true)
	public static Object get(@NotNull Object object, @NotNull List<String> keys) {
		Objects.requireNonNull(object, "object");
		Objects.requireNonNull(keys, "keys");

		//no keys => error
		if (keys.isEmpty())
			throw new IllegalArgumentException("empty keys list");

		//'object' is a 'json object' => get by key
		if (object instanceof JSONObject) {
			JSONObject jsonObject = (JSONObject) object;

			Object value = jsonObject.opt(keys.get(0));

			//value is null => 'object' contain no value at the key
			if (value == null)
				return "";

			//value not reached yet => recursive get
			if (keys.size() > 1)
				return JSONUtil.get(value, keys.subList(1, keys.size()));

			//value reached => return it
			return value;
		}

		//'object' is a 'json array' => get by index
		if (object instanceof JSONArray) {
			JSONArray array = (JSONArray) object;

			try {
				//the key is an index => get by index
				int index = Integer.parseInt(keys.get(0));

				Object value = array.opt(index);

				//value is null => 'object' contain no value at the key
				if (value == null)
					return "";

				//value not reached yet => recursive get
				if (keys.size() > 1)
					return JSONUtil.get(value, keys.subList(1, keys.size()));

				//value reached => return it
				return value;
			} catch (NumberFormatException e) {
				//the key is not an index => 'object' contain no value at the key
				return "";
			}
		}

		//'object' is not a 'json object' nor a 'json array' => attempt parsing
		try {
			//can parse 'object' to 'json object' => parse then recursive get
			return JSONUtil.get(new JSONObject(object.toString()), keys);
		} catch (JSONException e1) {
			try {
				//can parse 'object' to 'json array' => parse then recursive get
				return JSONUtil.get(new JSONArray(object.toString()), keys);
			} catch (JSONException e2) {
				//cannot parse 'object' => 'object' contain no value at the key
				return "";
			}
		}
	}

	/**
	 * Assuming the given {@code object} is a {@link JSONArray} or a {@link JSONObject},
	 * put the given {@code value} at given nesting {@code keys}.
	 *
	 * @param object the object to put to.
	 * @param keys   the nesting keys.
	 * @param value  the value to be put.
	 * @return the given {@code object} if the value can be put, or a replacement object.
	 * @throws NullPointerException if the given {@code object} or {@code keys} or {@code
	 *                              value} is null.
	 * @since 0.2.0 ~2021.06.01
	 */
	@SuppressWarnings("OverlyLongMethod")
	@NotNull
	@Contract(mutates = "param")
	public static Object put(@NotNull Object object, @NotNull List<String> keys, @NotNull Object value) {
		Objects.requireNonNull(object, "object");
		Objects.requireNonNull(keys, "keys");
		Objects.requireNonNull(value, "value");

		//no keys => error
		if (keys.isEmpty())
			throw new IllegalArgumentException("empty keys list");

		//'object' is a 'json object' => put by the key
		if (object instanceof JSONObject) {
			JSONObject jsonObject = (JSONObject) object;

			//slot not reached yet => recursive put
			if (keys.size() > 1) {
				Object middle = jsonObject.opt(keys.get(0));

				//no middle => place holder
				if (middle == null)
					middle = "";

				//recursive put
				jsonObject.put(
						keys.get(0),
						JSONUtil.put(
								middle,
								keys.subList(1, keys.size()),
								value
						)
				);

				return jsonObject;
			}

			//slot reached => put
			jsonObject.put(keys.get(0), value);

			return jsonObject;
		}

		//'object' is a 'json array' => put by index
		if (object instanceof JSONArray) {
			JSONArray jsonArray = (JSONArray) object;

			try {
				//the key is an index => get by index
				int index = Integer.parseInt(keys.get(0));

				//slot not reached yet => recursive put
				if (keys.size() > 1) {
					Object middle = jsonArray.opt(index);

					//no middle => place holder
					if (middle == null)
						middle = "";

					//recursive put
					jsonArray.put(
							index,
							JSONUtil.put(
									middle,
									keys.subList(1, keys.size()),
									value
							)
					);

					return jsonArray;
				}

				//slot reached => put
				jsonArray.put(index, value);

				return jsonArray;
			} catch (NumberFormatException e) {
				//the key is not an index => convert 'object' to 'json object' then recursive put
				return JSONUtil.put(
						jsonArray.toJSONObject(
								new JSONArray(
										IntStream.range(0, jsonArray.length())
												 .mapToObj(Integer::toString)
												 .collect(Collectors.toList())
								)
						),
						keys,
						value
				);
			}
		}

		//'object' is not a 'json object' nor a 'json array' => attempt parsing
		try {
			//can parse 'object' to 'json object' => parse then recursive put
			return JSONUtil.put(new JSONObject(object.toString()), keys, value);
		} catch (JSONException e1) {
			try {
				//can parse 'object' to `json array' => parse then recursive put
				return JSONUtil.put(new JSONArray(object.toString()), keys, value);
			} catch (JSONException e2) {
				//cannot parse 'object' => new
				try {
					//key is an index => replace with a 'json array' then recursive put
					Integer.parseInt(keys.get(0));
					return JSONUtil.put(new JSONArray(), keys, value);
				} catch (NumberFormatException e3) {
					//key is not an index => replace with a 'json object' then recursive put
					return JSONUtil.put(new JSONObject(), keys, value);
				}
			}
		}
	}
}
