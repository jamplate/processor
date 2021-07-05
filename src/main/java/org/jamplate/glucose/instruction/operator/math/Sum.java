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
package org.jamplate.glucose.instruction.operator.math;

import org.jamplate.glucose.value.NumberValue;
import org.jamplate.glucose.value.TextValue;
import org.jamplate.memory.Memory;
import org.jamplate.memory.Value;
import org.jamplate.model.Environment;
import org.jamplate.model.Instruction;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * An instruction that pops the last two values and pushes a value that evaluates to the
 * results of adding/joining the result of evaluating the two popped values.
 * <br>
 * If the two values are both numbers, the two values will be added mathematically.
 * <br>
 * If one of the values is not a number, the two values will be concatenated with the
 * first being the last popped and the last being the first popped.
 * <br><br>
 * Memory Visualization:
 * <pre>
 *     [..., left:value*|number*, right:value*|number*]
 *     [..., result:value*|number*]
 * </pre>
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.11
 */
public class Sum implements Instruction {
	/**
	 * An instance of this instruction.
	 *
	 * @since 0.3.0 ~2021.06.11
	 */
	@NotNull
	public static final Sum INSTANCE = new Sum();

	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 3448952003441559955L;

	/**
	 * A reference of this instruction in the source code.
	 *
	 * @since 0.3.0 ~2021.06.11
	 */
	@Nullable
	protected final Tree tree;

	/**
	 * Construct a new instruction that pops the last two values and pushes the results of
	 * adding them.
	 *
	 * @since 0.3.0 ~2021.06.12
	 */
	public Sum() {
		this.tree = null;
	}

	/**
	 * Construct a new instruction that pops the last two values and pushes the results of
	 * adding them.
	 *
	 * @param tree a reference for the constructed instruction in the source code.
	 * @throws NullPointerException if the given {@code tree} is null.
	 * @since 0.3.0 ~2021.06.12
	 */
	public Sum(@NotNull Tree tree) {
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

		if (value0 instanceof NumberValue && value1 instanceof NumberValue) {
			//right
			NumberValue number0 = (NumberValue) value0;
			//left
			NumberValue number1 = (NumberValue) value1;

			//result
			NumberValue number2 = number1.apply((m, n) ->
					n + number0.getPipe().eval(m)
			);

			memory.push(number2);
			return;
		}

		//right
		TextValue text0 = TextValue.cast(value0);
		//left
		TextValue text1 = TextValue.cast(value1);

		//result
		TextValue text2 = text1.apply((m, s) ->
				s + text0.getPipe().eval(m)
		);

		memory.push(text2);
	}

	@Nullable
	@Override
	public Tree getTree() {
		return this.tree;
	}

	@NotNull
	@Override
	public Instruction optimize(int mode) {
		return mode < 0 ? Sum.INSTANCE : new Sum(new Tree(this.tree));
	}
}
