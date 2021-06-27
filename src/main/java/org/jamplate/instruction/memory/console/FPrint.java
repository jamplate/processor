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
package org.jamplate.instruction.memory.console;

import org.jamplate.model.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

/**
 * An instruction that pops the last two values from the stack and prints the results of
 * evaluating the second popped value to the console with applying the replacements of
 * evaluating the first popped value.
 * <br>
 * If the first popped value was not an object, an {@link ExecutionException} will be
 * thrown.
 * <br><br>
 * Memory Visualization:
 * <pre>
 *     [..., parameter:text, replacements:object]
 *     [...]
 * </pre>
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.27
 */
public class FPrint implements Instruction {
	/**
	 * An instance of this instruction.
	 *
	 * @since 0.3.0 ~2021.06.27
	 */
	@NotNull
	public static final FPrint INSTANCE = new FPrint();

	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 9113900447926859442L;

	/**
	 * A reference of this instruction in the source code.
	 *
	 * @since 0.3.0 ~2021.06.27
	 */
	@Nullable
	protected final Tree tree;

	/**
	 * Construct a new instruction that pops the last two values from the stack and prints
	 * the results of evaluating the second popped value to the console with applying the
	 * replacements of evaluating the first popped value.
	 *
	 * @since 0.3.0 ~2021.06.27
	 */
	public FPrint() {
		this.tree = null;
	}

	/**
	 * Construct a new instruction that pops the last two values from the stack and prints
	 * the results of evaluating the second popped value to the console with applying the
	 * replacements of evaluating the first popped value.
	 *
	 * @param tree a reference for the constructed instruction in the source code.
	 * @throws NullPointerException if the given {@code tree} is null.
	 * @since 0.3.0 ~2021.06.27
	 */
	public FPrint(@NotNull Tree tree) {
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

		//right
		String text0 = value0.evaluate(memory);
		//left
		String text1 = value1.evaluate(memory);

		try {
			//right
			JSONObject object0 = new JSONObject(text0);

			//result
			String text2 = object0
					.toMap()
					.entrySet()
					.stream()
					.reduce(
							text1,
							(text, entry) ->
									text.replace(
											String.valueOf(entry.getKey()),
											String.valueOf(entry.getValue())
									),
							(t1, t2) -> t1 + t2
					);

			memory.print(text2);
		} catch (JSONException ignored) {
			throw new ExecutionException(
					"FPRINT expected an object but got: " + text0,
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
