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
package org.jamplate.glucose.instruction.operator.cast;

import org.jamplate.glucose.value.ArrayValue;
import org.jamplate.glucose.value.GlueValue;
import org.jamplate.memory.Memory;
import org.jamplate.memory.Value;
import org.jamplate.model.Environment;
import org.jamplate.model.ExecutionException;
import org.jamplate.model.Instruction;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

import static org.jamplate.glucose.internal.util.Values.array;

/**
 * An instruction that pops the top value at the stack, build an array from it, then push
 * the built array to the stack.
 * <br>
 * If the popped value is not a {@link GlueValue}, an {@link ExecutionException} will be
 * thrown.
 * <br><br>
 * Memory Visualization:
 * <pre>
 *     [..., param:glue*]
 *     [..., result:array*]
 * </pre>
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.07.05
 */
public class BuildArray implements Instruction {
	/**
	 * An instance of this instruction.
	 *
	 * @since 0.3.0 ~2021.07.06
	 */
	@NotNull
	public static final BuildArray INSTANCE = new BuildArray();

	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -2004740839380218350L;

	/**
	 * A reference of this instruction in the source code.
	 *
	 * @since 0.3.0 ~2021.07.06
	 */
	@Nullable
	protected final Tree tree;

	/**
	 * Construct a new instruction that pops the top value at the stack, build an array
	 * from it, then push the built array to the stack.
	 *
	 * @since 0.3.0 ~2021.07.06
	 */
	public BuildArray() {
		this.tree = null;
	}

	/**
	 * Construct a new instruction that pops the top value at the stack, build an array
	 * from it, then push the built array to the stack.
	 *
	 * @param tree a reference for the constructed instruction in the source code.
	 * @throws NullPointerException if the given {@code tree} is null.
	 * @since 0.3.0 ~2021.07.06
	 */
	public BuildArray(@NotNull Tree tree) {
		Objects.requireNonNull(tree, "tree");
		this.tree = tree;
	}

	@Override
	public void exec(@NotNull Environment environment, @NotNull Memory memory) {
		Objects.requireNonNull(environment, "environment");
		Objects.requireNonNull(memory, "memory");

		Value value0 = memory.pop();

		if (value0 instanceof GlueValue) {
			GlueValue glue0 = (GlueValue) value0;
			List<Value> elements = glue0
					.getPipe()
					.eval(memory);

			ArrayValue value1 = array(elements);

			memory.push(value1);
			return;
		}

		throw new ExecutionException(
				"BUILD_ARRAY expected glue but got: " +
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
		return mode < 0 ? BuildArray.INSTANCE : new BuildArray(new Tree(this.tree));
	}
}
