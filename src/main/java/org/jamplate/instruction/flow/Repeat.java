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
package org.jamplate.instruction.flow;

import org.jamplate.model.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * An instruction that repeatedly executes a pre-specified instruction while the top value
 * in the stack evaluates to true. The top value will be popped before being executed.
 * <br>
 * If the top value in the stack is not a boolean, then an {@link ExecutionException} will
 * be thrown.
 * <br><br>
 * Memory Visualization (before each execution of the instruction):
 * <pre>
 *     [..., condition:boolean]
 *     [...]
 * </pre>
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.13
 */
public class Repeat implements Instruction {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -3033907913105874302L;

	/**
	 * The instruction to be repeatedly executed.
	 *
	 * @since 0.3.0 ~2021.06.15
	 */
	@NotNull
	protected final Instruction instruction;
	/**
	 * A reference of this instruction in the source code.
	 *
	 * @since 0.3.0 ~2021.06.15
	 */
	@Nullable
	protected final Tree tree;

	/**
	 * Construct a new instruction that repeatedly executes the given {@code instruction}
	 * until the top value in the stack evaluates to {@code false}.
	 *
	 * @param instruction the instruction to be repeatedly executed.
	 * @throws NullPointerException if the given {@code instruction} is null.
	 * @since 0.3.0 ~2021.06.15
	 */
	public Repeat(@NotNull Instruction instruction) {
		Objects.requireNonNull(instruction, "instruction");
		this.tree = null;
		this.instruction = instruction;
	}

	/**
	 * Construct a new instruction that repeatedly executes the given {@code instruction}
	 * until the top value in the stack evaluates to {@code false}.
	 *
	 * @param tree        a reference for the constructed instruction in the source code.
	 * @param instruction the instruction to be repeatedly executed.
	 * @throws NullPointerException if the given {@code tree} or {@code instruction} is
	 *                              null.
	 * @since 0.3.0 ~2021.06.15
	 */
	public Repeat(@NotNull Tree tree, @NotNull Instruction instruction) {
		Objects.requireNonNull(tree, "tree");
		Objects.requireNonNull(instruction, "instruction");
		this.tree = tree;
		this.instruction = instruction;
	}

	@Override
	public void exec(@NotNull Environment environment, @NotNull Memory memory) {
		Objects.requireNonNull(environment, "environment");
		Objects.requireNonNull(memory, "memory");
		while (true) {
			Value value0 = memory.pop();
			String text0 = value0.evaluate(memory);

			switch (text0) {
				case "true":
					this.instruction.exec(environment, memory);
					break;
				case "false":
					return;
				default:
					throw new ExecutionException(
							"REPEAT expected a boolean but got: " + text0,
							this.tree
					);
			}
		}
	}

	@Nullable
	@Override
	public Tree getTree() {
		return this.tree;
	}
}
