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
package org.jamplate.instruction.math;

import org.jamplate.model.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * An instruction that pops the last two values and pushes a value that evaluates to the
 * result of dividing the second popped value by the first one.
 * <br>
 * If one of the values is not a number, an {@link ExecutionException} will occur.
 * <br><br>
 * Memory Visualization:
 * <pre>
 *     [..., left:number, right:number]
 *     [...]
 *     [..., result:number]
 * </pre>
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.11
 */
public class Divide implements Instruction {
	/**
	 * An instance of this instruction.
	 *
	 * @since 0.3.0 ~2021.06.11
	 */
	@NotNull
	public static final Divide INSTANCE = new Divide();

	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 3667493368990207838L;

	/**
	 * A reference of this instruction in the source code.
	 *
	 * @since 0.3.0 ~2021.06.11
	 */
	@Nullable
	protected final Tree tree;

	/**
	 * Construct a new instruction that pops the last two values and pushes the results of
	 * dividing the second popped value by the first popped value.
	 *
	 * @since 0.3.0 ~2021.06.12
	 */
	public Divide() {
		this.tree = null;
	}

	/**
	 * Construct a new instruction that pops the last two values and pushes the results of
	 * dividing the second popped value by the first popped value.
	 *
	 * @param tree a reference for the constructed instruction in the source code.
	 * @throws NullPointerException if the given {@code tree} is null.
	 * @since 0.3.0 ~2021.06.12
	 */
	public Divide(@NotNull Tree tree) {
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
				double num0 = Double.parseDouble(text0);
				//left
				double num1 = Double.parseDouble(text1);

				if (num0 == 0)
					throw new ExecutionException(
							"Division by zero is kinda illegal :P",
							this.tree
					);

				//result
				double num3 = num1 / num0;

				return num3 % 1 == 0 ?
					   Long.toString((long) num3) :
					   Double.toString(num3);
			} catch (NumberFormatException ignored0) {
				throw new ExecutionException(
						"DIV (/) expected two numbers but got: " + text0 + " and " +
						text1,
						this.tree
				);
			}
		});
	}

	@Nullable
	@Override
	public Tree getTree() {
		return this.tree;
	}
}
