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
package org.jamplate.glucose.instruction.memory.frame;

import org.jamplate.glucose.value.ArrayValue;
import org.jamplate.memory.Memory;
import org.jamplate.memory.Value;
import org.jamplate.model.Environment;
import org.jamplate.model.ExecutionException;
import org.jamplate.model.Instruction;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.jamplate.glucose.internal.util.Values.array;

/**
 * An instruction that pops the items from the last item and before the last {@link
 * Value#NULL} and then pushes a value that evaluates to the results of concatenating all
 * the popped values from the last popped to the first popped. The concatenation will be
 * array concatenation.
 * <br>
 * If one of the values is not an {@link ArrayValue array}, an {@link ExecutionException}
 * will be thrown.
 * <br><br>
 * Memory Visualization:
 * <pre>
 *     [..., ...param:array*]
 *     [..., result:array*]
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

		ArrayValue array0 = array();

		while (true) {
			Value value1 = memory.pop();

			if (value1 == Value.NULL) {
				memory.push(array0);
				return;
			}

			if (value1 instanceof ArrayValue) {
				ArrayValue array1 = (ArrayValue) value1;

				array0 = array0.apply((m, l) -> {
					List<Value> list = new ArrayList<>(l);
					list.addAll(array1.getPipe().eval(m));
					return list;
				});
				continue;
			}

			throw new ExecutionException(
					"CONCAT expected array but got: " + value1.evaluate(memory),
					this.tree
			);
		}
	}

	@Nullable
	@Override
	public Tree getTree() {
		return this.tree;
	}

	@NotNull
	@Override
	public Instruction optimize(int mode) {
		return mode < 0 ? ConcatFrame.INSTANCE : new ConcatFrame(new Tree(this.tree));
	}
}
