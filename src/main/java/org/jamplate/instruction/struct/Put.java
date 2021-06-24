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
package org.jamplate.instruction.struct;

import org.jamplate.model.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * An instruction that pops the top three values in the stack and put the first popped
 * value in the third popped value at the key resultant from evaluating the second popped
 * value.
 * <br>
 * If the second popped value is not an array, an {@link ExecutionException} will occur.
 * <br><br>
 * Memory Visualization:
 * <pre>
 *     [..., struct:text:lazy, key:text:lazy, value:text:lazy]
 *     [..., result:text:lazy]
 * </pre>
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.15
 */
public class Put implements Instruction {
	/**
	 * An instance of this instruction.
	 *
	 * @since 0.3.0 ~2021.06.15
	 */
	@NotNull
	public static final Put INSTANCE = new Put();

	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -4102904984181017768L;

	/**
	 * A reference of this instruction in the source code.
	 *
	 * @since 0.3.0 ~2021.06.15
	 */
	@Nullable
	protected final Tree tree;

	/**
	 * Construct a new instruction that pops the last two values in the stack and
	 * allocates the first popped value to the heap at the result of evaluating the second
	 * popped value.
	 *
	 * @since 0.3.0 ~2021.06.15
	 */
	public Put() {
		this.tree = null;
	}

	/**
	 * Construct a new instruction that pops the last two values in the stack and
	 * allocates the first popped value to the heap at the result of evaluating the second
	 * popped value.
	 *
	 * @param tree a reference for the constructed instruction in the source code.
	 * @throws NullPointerException if the given {@code tree} is null.
	 * @since 0.3.0 ~2021.06.15
	 */
	public Put(@NotNull Tree tree) {
		Objects.requireNonNull(tree, "tree");
		this.tree = tree;
	}

	@Override
	public void exec(@NotNull Environment environment, @NotNull Memory memory) {
		Objects.requireNonNull(environment, "environment");
		Objects.requireNonNull(memory, "memory");

		//value
		Value value0 = memory.pop();
		//key
		Value value1 = memory.pop();
		//object
		Value value2 = memory.pop();

		memory.push(m -> {
			//value
			String text0 = value0.evaluate(m);
			//key
			String text1 = value1.evaluate(m);
			//object
			String text2 = value2.evaluate(m);

			try {
				//key
				JSONArray array1 = new JSONArray(text1);
				List<String> list1 = array1
						.toList()
						.stream()
						.map(String::valueOf)
						.collect(Collectors.toList());

				//result
				Object object4 = this.put(text2, list1, text0);

				return String.valueOf(object4);
			} catch (JSONException ignored0) {
				throw new ExecutionException(
						"PUT expected an array but got: " + text1
				);
			}
		});
	}

	@Nullable
	@Override
	public Tree getTree() {
		return this.tree;
	}

	/**
	 * Assuming the given {@code object} is a {@link JSONArray} or a {@link JSONObject},
	 * put the given {@code value} at given nesting {@code keys}.
	 *
	 * @param object the object to put to.
	 * @param keys   the nesting keys.
	 * @param value  the value to be put.
	 * @return the given {@code object} if the value can be put, or a replacement object.
	 * @throws NullPointerException     if the given {@code object} or {@code keys} or
	 *                                  {@code value} is null.
	 * @throws IllegalArgumentException if the given {@code keys} list is empty.
	 * @since 0.3.0 ~2021.06.15
	 */
	@SuppressWarnings("OverlyLongMethod")
	@NotNull
	@Contract(mutates = "param")
	protected Object put(@NotNull Object object, @NotNull List<String> keys, @NotNull Object value) {
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
						this.put(
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
							this.put(
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
				return this.put(
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
			return this.put(new JSONObject(object.toString()), keys, value);
		} catch (JSONException e1) {
			try {
				//can parse 'object' to `json array' => parse then recursive put
				return this.put(new JSONArray(object.toString()), keys, value);
			} catch (JSONException e2) {
				//cannot parse 'object' => new
				try {
					//key is an index => replace with a 'json array' then recursive put
					Integer.parseInt(keys.get(0));
					return this.put(new JSONArray(), keys, value);
				} catch (NumberFormatException e3) {
					//key is not an index => replace with a 'json object' then recursive put
					return this.put(new JSONObject(), keys, value);
				}
			}
		}
	}
}
