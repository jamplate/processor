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
package org.jamplate.instruction.cast;

import org.jamplate.model.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;
import java.util.stream.Collectors;

/**
 * An instruction that pops the last value in the stack and pushes a value that evaluate
 * to the popped value interpreted as an array.
 * <br>
 * <table>
 *     <tr>
 *         <th>Evaluation</th>
 *         <th>Interpretation</th>
 *     </tr>
 *     <tr>
 *         <td>Array</td>
 *         <td>The array</td>
 *     </tr>
 *     <tr>
 *         <td>Object</td>
 *         <td>The keys of the object sorted as by {@link String#compareTo(String)}</td>
 *     </tr>
 *     <tr>
 *         <td>The rest</td>
 *         <td>A singleton array containing the text.</td>
 *     </tr>
 * </table>
 * <br><br>
 * Memory Visualization:
 * <pre>
 *     [..., input:text]
 *     [...]
 *     [..., output:array]
 * </pre>
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.13
 */
public class CastArray implements Instruction {
	/**
	 * An instance of this instruction.
	 *
	 * @since 0.3.0 ~2021.06.13
	 */
	@NotNull
	public static final CastArray INSTANCE = new CastArray();

	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -8004089805216287695L;

	/**
	 * A reference of this instruction in the source code.
	 *
	 * @since 0.3.0 ~2021.06.13
	 */
	@Nullable
	protected final Tree tree;

	/**
	 * Construct a new instruction that pops the last value in the stack and pushes a
	 * value that evaluate to the popped value interpreted as an array.
	 *
	 * @since 0.3.0 ~2021.06.13
	 */
	public CastArray() {
		this.tree = null;
	}

	/**
	 * Construct a new instruction that pops the last value in the stack and pushes a
	 * value that evaluate to the popped value interpreted as an array.
	 *
	 * @param tree a reference for the constructed instruction in the source code.
	 * @throws NullPointerException if the given {@code tree} is null.
	 * @since 0.3.0 ~2021.06.13
	 */
	public CastArray(@NotNull Tree tree) {
		Objects.requireNonNull(tree, "tree");
		this.tree = tree;
	}

	@Override
	public void exec(@NotNull Environment environment, @NotNull Memory memory) {
		Objects.requireNonNull(environment, "environment");
		Objects.requireNonNull(memory, "memory");

		Value value0 = memory.pop();

		memory.push(m -> {
			String text0 = value0.evaluate(m);

			try {
				JSONArray array0 = new JSONArray(text0);

				return String.valueOf(array0);
			} catch (JSONException ignored0) {
				try {
					JSONObject object0 = new JSONObject(text0);
					JSONArray array0 = object0.toJSONArray(
							new JSONArray(
									object0.keySet()
										   .stream()
										   .sorted()
										   .collect(Collectors.toList())
							)
					);

					return String.valueOf(array0);
				} catch (JSONException ignored1) {
					JSONArray array0 = new JSONArray();

					array0.put(0, text0);

					return String.valueOf(array0);
				}
			}
		});
	}

	@Nullable
	@Override
	public Tree getTree() {
		return this.tree;
	}
}
