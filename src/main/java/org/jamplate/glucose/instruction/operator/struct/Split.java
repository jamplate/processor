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
package org.jamplate.glucose.instruction.operator.struct;

import org.jamplate.glucose.value.ArrayValue;
import org.jamplate.glucose.value.UnquoteValue;
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
 * An instruction that pops the top value at the stack and splits the items in it
 * (assuming its an array) and push every item to the stack.
 * <br>
 * The items will be pushed from the first to the last, so popping the stack will result
 * to reading the array backwards.
 * <br>
 * If the popped value is not an {@link ArrayValue array}, an {@link ExecutionException}
 * will be thrown.
 * <br><br>
 * Memory Visualization:
 * <pre>
 *     [..., param:array]
 *     [..., ...result:value]
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

		if (value0 instanceof ArrayValue) {
			ArrayValue array0 = (ArrayValue) value0;

			array0.getPipe()
				  .eval(memory)
				  .forEach(value1 -> {
					  UnquoteValue unquote1 = UnquoteValue.cast(value1);

					  memory.push(unquote1);
				  });

			return;
		}

		throw new ExecutionException(
				"SPLIT expected an array but got: " +
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
		return mode < 0 ? Split.INSTANCE : new Split(new Tree(this.tree));
	}
}
