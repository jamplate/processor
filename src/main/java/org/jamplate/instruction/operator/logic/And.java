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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * An instruction that pops the last two values and pushes a value that evaluates to
 * {@code true} if both the popped values evaluated to {@code true} and evaluates to
 * {@code false} if one of the popped values evaluated to {@code false}.
 * <br>
 * This instruction will throw an {@link ExecutionException} if one of the popped values
 * was not a boolean.
 * <br><br>
 * Memory Visualization:
 * <pre>
 *     [..., left:boolean:lazy, right:boolean:lazy]
 *     [...]
 *     [..., result:boolean:lazy]
 * </pre>
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.25
 */
public class And implements Instruction {
	/**
	 * An instance of this instruction.
	 *
	 * @since 0.3.0 ~2021.06.25
	 */
	@NotNull
	public static final And INSTANCE = new And();

	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -5666880465701967135L;

	/**
	 * A reference of this instruction in the source code.
	 *
	 * @since 0.3.0 ~2021.06.25
	 */
	@Nullable
	protected final Tree tree;

	/**
	 * Construct a new instruction that pops the last two values and pushes a value that
	 * evaluates to {@code true} if both the popped values evaluated to {@code true} and
	 * evaluates to {@code false} if one of the popped values evaluated to {@code false}.
	 *
	 * @since 0.3.0 ~2021.06.25
	 */
	public And() {
		this.tree = null;
	}

	/**
	 * Construct a new instruction that pops the last two values and pushes a value that
	 * evaluates to {@code true} if both the popped values evaluated to {@code true} and
	 * evaluates to {@code false} if one of the popped values evaluated to {@code false}.
	 *
	 * @param tree a reference for the constructed instruction in the source code.
	 * @throws NullPointerException if the given {@code tree} is null.
	 * @since 0.3.0 ~2021.06.25
	 */
	public And(@NotNull Tree tree) {
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

			switch (text1) {
				case "true":
					switch (text0) {
						case "true":
							return "true";
						case "false":
							return "false";
						default:
							throw new ExecutionException(
									"AND expected a boolean but got: " +
									text0,
									this.tree
							);
					}
				case "false":
					return "false";
				default:
					throw new ExecutionException(
							"AND expected a boolean but got: " + text1,
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

	@NotNull
	@Override
	public Instruction optimize(int mode) {
		return mode < 0 ? And.INSTANCE : new And(new Tree(this.tree));
	}
}
