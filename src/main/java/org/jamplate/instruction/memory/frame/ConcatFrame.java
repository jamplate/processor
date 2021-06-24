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
package org.jamplate.instruction.memory.frame;

import org.jamplate.model.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.Objects;

/**
 * An instruction that pops the items from the last item and before the last {@link
 * Value#NULL} and then pushes a value that evaluates to the results of concatenating all
 * the popped values from the last popped to the first popped. The concatenation will be
 * array concatenation. If one of the values is not an array, an {@link
 * ExecutionException} will be thrown.
 * <br><br>
 * Memory Visualization:
 * <pre>
 *     [...null?, item0:array:lazy, item1:array:lazy, item2:array:lazy, ...]
 *     [...null?]
 *     [...null?, joint:array:lazy]
 * </pre>
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.15
 */
public class ConcatFrame implements Instruction {
	/**
	 * An instance of this instruction.
	 *
	 * @since 0.3.0 ~2021.06.15
	 */
	@NotNull
	public static final ConcatFrame INSTANCE = new ConcatFrame();

	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 7613497485125673719L;

	/**
	 * A reference of this instruction in the source code.
	 *
	 * @since 0.3.0 ~2021.06.15
	 */
	@Nullable
	protected final Tree tree;

	/**
	 * Construct a new instruction that pops the items in the stack from the last item and
	 * before the last null (assuming all are arrays) and join them into one single
	 * array.
	 *
	 * @since 0.3.0 ~2021.06.15
	 */
	public ConcatFrame() {
		this.tree = null;
	}

	/**
	 * Construct a new instruction that pops the items in the stack from the last item and
	 * before the last null (assuming all are arrays) and join them into one single
	 * array.
	 *
	 * @param tree a reference for the constructed instruction in the source code.
	 * @throws NullPointerException if the given {@code tree} is null.
	 * @since 0.3.0 ~2021.06.15
	 */
	public ConcatFrame(@NotNull Tree tree) {
		Objects.requireNonNull(tree, "tree");
		this.tree = tree;
	}

	@Override
	public void exec(@NotNull Environment environment, @NotNull Memory memory) {
		Objects.requireNonNull(environment, "environment");
		Objects.requireNonNull(memory, "memory");

		Value value0 = memory.pop();

		while (true) {
			Value value1 = value0;
			Value value2 = memory.peek();

			if (value2 == Value.NULL) {
				memory.push(value1);
				break;
			}

			memory.pop();

			value0 = m -> {
				String text1 = value1.evaluate(m);
				String text2 = value2.evaluate(m);

				try {
					JSONArray array1 = new JSONArray(text1);
					JSONArray array2 = new JSONArray(text2);

					array2.putAll(array1);

					return String.valueOf(array2);
				} catch (JSONException ignored0) {
					throw new ExecutionException(
							"CONCAT expected two arrays but got: " + text1 + " and " +
							text2,
							this.tree
					);
				}
			};
		}
	}

	@Nullable
	@Override
	public Tree getTree() {
		return this.tree;
	}
}
