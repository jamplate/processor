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

import org.jamplate.glucose.value.GlueValue;
import org.jamplate.glucose.value.PairValue;
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

import static org.jamplate.glucose.internal.util.Values.pair;

/**
 * An instruction that pops the top two values at the stack, build a pair from them, then
 * push the built pair to the stack.
 * <br>
 * If the popped values is not both {@link GlueValue}s, an {@link ExecutionException} will
 * be thrown.
 * <br><br>
 * Memory Visualization:
 * <pre>
 *     [..., param:glue*]
 *     [..., result:pair*]
 * </pre>
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.07.05
 */
public class BuildPair implements Instruction {
	/**
	 * An instance of this instruction.
	 *
	 * @since 0.3.0 ~2021.07.06
	 */
	@NotNull
	public static final BuildPair INSTANCE = new BuildPair();

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
	 * Construct a new instruction that pops the top two values at the stack, build a pair
	 * from them, then push the built pair to the stack.
	 *
	 * @since 0.3.0 ~2021.07.06
	 */
	public BuildPair() {
		this.tree = null;
	}

	/**
	 * Construct a new instruction that pops the top two values at the stack, build a pair
	 * from them, then push the built pair to the stack.
	 *
	 * @param tree a reference for the constructed instruction in the source code.
	 * @throws NullPointerException if the given {@code tree} is null.
	 * @since 0.3.0 ~2021.07.06
	 */
	public BuildPair(@NotNull Tree tree) {
		Objects.requireNonNull(tree, "tree");
		this.tree = tree;
	}

	@Override
	public void exec(@NotNull Environment environment, @NotNull Memory memory) {
		Objects.requireNonNull(environment, "environment");
		Objects.requireNonNull(memory, "memory");

		//value
		Value value0 = memory.pop();
		//key
		Value value1 = memory.pop();

		if (value0 instanceof GlueValue && value1 instanceof GlueValue) {
			//value
			GlueValue glue0 = (GlueValue) value0;
			List<Value> list0 = glue0.getPipe().eval(memory);
			//key
			GlueValue glue1 = (GlueValue) value1;
			List<Value> list1 = glue1.getPipe().eval(memory);

			//value
			Value value2 = list0.size() == 1 ? list0.get(0) : glue0;
			//key
			Value value3 = list1.size() == 1 ? list1.get(0) : glue1;

			PairValue pair4 = pair(
					value3,
					value2
			);

			memory.push(pair4);
			return;
		}

		throw new ExecutionException(
				"BUILD_PAIR expected two glues but got: " +
				value0.evaluate(memory) +
				" and " +
				value1.evaluate(memory),
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
		return mode < 0 ? BuildPair.INSTANCE : new BuildPair(new Tree(this.tree));
	}
}
