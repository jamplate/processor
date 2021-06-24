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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.Objects;

/**
 * An instruction that pops the top value at the stack and splits the items in it
 * (assuming its an array) and push every item to the stack.
 * <br>
 * The items will be pushed from the first to the last, so popping the stack will result
 * to reading the array backwards.
 * <br>
 * If the popped value is not an array, an {@link ExecutionException} will be thrown.
 * <br><br>
 * Memory Visualization:
 * <pre>
 *     [..., struct: array]
 *     [..., item0:text, item1:text, item2:text, ...]
 * </pre>
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.15
 */
public class Split implements Instruction {
	/**
	 * An instance of this instruction.
	 *
	 * @since 0.3.0 ~2021.06.15
	 */
	@NotNull
	public static final Split INSTANCE = new Split();

	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 3303241345074920017L;

	/**
	 * A reference of this instruction in the source code.
	 *
	 * @since 0.3.0 ~2021.06.15
	 */
	@Nullable
	protected final Tree tree;

	/**
	 * Construct a new instruction that pops the top value in the stack and splits it
	 * (assuming its an array) and pushes every item in it to the stack.
	 *
	 * @since 0.3.0 ~2021.06.15
	 */
	public Split() {
		this.tree = null;
	}

	/**
	 * Construct a new instruction that pops the top value in the stack and splits it
	 * (assuming its an array) and pushes every item in it to the stack.
	 *
	 * @param tree a reference for the constructed instruction in the source code.
	 * @throws NullPointerException if the given {@code tree} is null.
	 * @since 0.3.0 ~2021.06.15
	 */
	public Split(@NotNull Tree tree) {
		Objects.requireNonNull(tree, "tree");
		this.tree = tree;
	}

	@Override
	public void exec(@NotNull Environment environment, @NotNull Memory memory) {
		Objects.requireNonNull(environment, "environment");
		Objects.requireNonNull(memory, "memory");

		Value value0 = memory.pop();
		String text0 = value0.evaluate(memory);

		try {
			JSONArray array = new JSONArray(text0);

			for (int i = 0, l = array.length(); i < l; i++) {
				String text1 = array.optString(i);

				memory.push(m -> text1);
			}
		} catch (JSONException ignored0) {
			throw new ExecutionException(
					"SPLIT expected an array but got: " + text0,
					this.tree
			);
		}
	}

	@Nullable
	@Override
	public Tree getTree() {
		return this.tree;
	}
}
