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
package org.jamplate.instruction.operator.struct;

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

/**
 * An instruction that pops the top two values in the stack and pushes the property in the
 * second popped value at the first popped value.
 * <br><br>
 * Memory Visualization:
 * <pre>
 *     [..., struct:text:lazy, key:text:lazy]
 *     [..., value:text:lazy]
 * </pre>
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.13
 */
public class Get implements Instruction {
	/**
	 * An instance of this instruction.
	 *
	 * @since 0.3.0 ~2021.06.13
	 */
	@NotNull
	public static final Get INSTANCE = new Get();

	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 6807005239681352973L;

	/**
	 * A reference of this instruction in the source code.
	 *
	 * @since 0.3.0 ~2021.06.13
	 */
	@Nullable
	protected final Tree tree;

	/**
	 * Construct a new instruction that pops the last two values and pushes the property
	 * in the second popped value at the first popped value.
	 *
	 * @since 0.3.0 ~2021.06.12
	 */
	public Get() {
		this.tree = null;
	}

	/**
	 * Construct a new instruction that pops the last two values and pushes the property
	 * in the second popped value at the first popped value.
	 *
	 * @param tree a reference for the constructed instruction in the source code.
	 * @throws NullPointerException if the given {@code tree} is null.
	 * @since 0.3.0 ~2021.06.13
	 */
	public Get(@NotNull Tree tree) {
		Objects.requireNonNull(tree, "tree");
		this.tree = tree;
	}

	@Override
	public void exec(@NotNull Environment environment, @NotNull Memory memory) {
		Objects.requireNonNull(environment, "environment");
		Objects.requireNonNull(memory, "memory");

		//right
		Value value0 = memory.pop();
		//left
		Value value1 = memory.pop();

		memory.push(m -> {
			//right
			String text0 = value0.evaluate(m);
			//left
			String text1 = value1.evaluate(m);

			try {
				//right
				JSONArray array0 = new JSONArray(text0);
				//right
				List<String> list0 = array0
						.toList()
						.stream()
						.map(String::valueOf)
						.collect(Collectors.toList());

				//result
				Object object3 = this.get(text1, list0);

				return String.valueOf(object3);
			} catch (JSONException ignored) {
				throw new ExecutionException(
						"GET expected an array but got: " + text1,
						this.tree
				);
			}
		});
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
	 * @throws NullPointerException     if the given {@code object} or {@code keys} is
	 *                                  null.
	 * @throws IllegalArgumentException if the given {@code keys} list is empty.
	 * @since 0.3.0 ~2021.06.13
	 */
	@NotNull
	@Contract(pure = true)
	protected Object get(@NotNull Object object, @NotNull List<String> keys) {
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
				return this.get(value, keys.subList(1, keys.size()));

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
					return this.get(value, keys.subList(1, keys.size()));

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
			return this.get(new JSONObject(object.toString()), keys);
		} catch (JSONException e1) {
			try {
				//can parse 'object' to 'json array' => parse then recursive get
				return this.get(new JSONArray(object.toString()), keys);
			} catch (JSONException e2) {
				//cannot parse 'object' => 'object' contain no value at the key
				return "";
			}
		}
	}
}
