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
package org.jamplate.instruction.operator.logic;

import org.jamplate.model.*;
import org.jamplate.value.BooleanValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * An instruction that pops the last value and pushes a value that evaluates to {@code
 * false} if the popped value evaluated to {@code true} and pushes {@code true} if the
 * popped value evaluated to {@code false}.
 * <br>
 * This instruction will throw an {@link ExecutionException} if the popped value was not a
 * {@link BooleanValue boolean}.
 * <br><br>
 * Memory Visualization:
 * <pre>
 *     [..., param:boolean*]
 *     [..., result:boolean*]
 * </pre>
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.11
 */
public class Negate implements Instruction {
	/**
	 * An instance of this instruction.
	 *
	 * @since 0.3.0 ~2021.06.11
	 */
	@NotNull
	public static final Negate INSTANCE = new Negate();

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
	 * Construct a new instruction that pops the last value and pushes the opposite of
	 * it.
	 *
	 * @since 0.3.0 ~2021.06.12
	 */
	public Negate() {
		this.tree = null;
	}

	/**
	 * Construct a new instruction that pops the last value and pushes the opposite of
	 * it.
	 *
	 * @param tree a reference for the constructed instruction in the source code.
	 * @throws NullPointerException if the given {@code tree} is null.
	 * @since 0.3.0 ~2021.06.12
	 */
	public Negate(@NotNull Tree tree) {
		Objects.requireNonNull(tree, "tree");
		this.tree = tree;
	}

	@Override
	public void exec(@NotNull Environment environment, @NotNull Memory memory) {
		Objects.requireNonNull(environment, "environment");
		Objects.requireNonNull(memory, "memory");

		//parameter
		Value value0 = memory.pop();

		if (value0 instanceof BooleanValue) {
			//parameter
			BooleanValue boolean0 = (BooleanValue) value0;

			//result
			BooleanValue boolean1 = boolean0.apply((m, b) -> !b);

			memory.push(boolean1);
			return;
		}

		throw new ExecutionException(
				"NEG (!) expected a boolean but got: " +
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
		return mode < 0 ? Negate.INSTANCE : new Negate(new Tree(this.tree));
	}
}
