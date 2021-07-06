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
import org.jamplate.memory.Memory;
import org.jamplate.memory.Value;
import org.jamplate.model.Environment;
import org.jamplate.model.ExecutionException;
import org.jamplate.model.Instruction;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * An instruction that pops the last two values and pushes a value that evaluates to the
 * result of subtracting the first popped value from the second one.
 * <br>
 * If one of the values is not a {@link NumberValue number}, an {@link ExecutionException}
 * will occur.
 * <br><br>
 * Memory Visualization:
 * <pre>
 *     [..., left:number*, right:number*]
 *     [..., result:number*]
 * </pre>
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.11
 */
public class Difference implements Instruction {
	/**
	 * An instance of this instruction.
	 *
	 * @since 0.3.0 ~2021.06.11
	 */
	@NotNull
	public static final Difference INSTANCE = new Difference();

	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 8918042984392129152L;

	/**
	 * A reference of this instruction in the source code.
	 *
	 * @since 0.3.0 ~2021.06.11
	 */
	@Nullable
	protected final Tree tree;

	/**
	 * Construct a new instruction that pops the last two values and pushes the results of
	 * subtract the first popped value from the second popped value.
	 *
	 * @since 0.3.0 ~2021.06.12
	 */
	public Difference() {
		this.tree = null;
	}

	/**
	 * Construct a new instruction that pops the last two values and pushes the results of
	 * subtract the first popped value from the second popped value.
	 *
	 * @param tree a reference for the constructed instruction in the source code.
	 * @throws NullPointerException if the given {@code tree} is null.
	 * @since 0.3.0 ~2021.06.12
	 */
	public Difference(@NotNull Tree tree) {
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
					n.doubleValue() -
					number0.getPipe().eval(m).doubleValue()
			);

			memory.push(number2);
			return;
		}

		throw new ExecutionException(
				"DIFFERENCE (-) expected two numbers but got: " +
				value1.evaluate(memory) +
				" and " +
				value0.evaluate(memory),
				this.tree
		);
	}

	@Nullable
	@Override
	public Tree getTree() {
		return this.tree;
	}

	@NotNull
	@Override
	public Instruction optimize(int mode) {
		return mode < 0 ? Difference.INSTANCE : new Difference(new Tree(this.tree));
	}
}
