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
import org.jamplate.value.ArrayValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * An instruction that pops the top value at the stack and invert it (assuming its an
 * array).
 * <br>
 * If the popped value was not an {@link ArrayValue array}, an {@link ExecutionException}
 * will be thrown.
 * <br><br>
 * Memory Visualization:
 * <pre>
 *     [..., param:array*]
 *     [..., result:array*]
 * </pre>
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.15
 */
public class Invert implements Instruction {
	/**
	 * An instance of this instruction.
	 *
	 * @since 0.3.0 ~2021.06.15
	 */
	@NotNull
	public static final Invert INSTANCE = new Invert();

	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 2382162307023568944L;

	/**
	 * A reference of this instruction in the source code.
	 *
	 * @since 0.3.0 ~2021.06.15
	 */
	@Nullable
	protected final Tree tree;

	/**
	 * An instruction that pops the top value at the stack and invert it. (assuming its an
	 * array)
	 *
	 * @since 0.3.0 ~2021.06.15
	 */
	public Invert() {
		this.tree = null;
	}

	/**
	 * An instruction that pops the top value at the stack and invert it. (assuming its an
	 * array)
	 *
	 * @param tree a reference for the constructed instruction in the source code.
	 * @throws NullPointerException if the given {@code tree} is null.
	 * @since 0.3.0 ~2021.06.15
	 */
	public Invert(@NotNull Tree tree) {
		Objects.requireNonNull(tree, "tree");
		this.tree = tree;
	}

	@Override
	public void exec(@NotNull Environment environment, @NotNull Memory memory) {
		Objects.requireNonNull(environment, "environment");
		Objects.requireNonNull(memory, "memory");

		//parameter
		Value value0 = memory.pop();

		if (value0 instanceof ArrayValue) {
			//parameter
			ArrayValue array0 = (ArrayValue) value0;

			//result
			ArrayValue array1 = array0.apply((m, l) -> {
				List<Value> list = new ArrayList<>(l);
				Collections.reverse(list);
				return list;
			});

			memory.push(array1);
			return;
		}

		throw new ExecutionException(
				"INVERT expected array but got: " +
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
		return mode < 0 ? Invert.INSTANCE : new Invert(new Tree(this.tree));
	}
}
