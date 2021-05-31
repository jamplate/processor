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
package org.jamplate.impl.instruction;

import org.jamplate.model.*;
import org.jamplate.impl.util.Memories;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * <h3>{@code BRANCH( EXEC( INSTR0 ) , INSTR1 , INSTR2 )}</h3>
 * An instruction that execute a pre-specified instruction {@code A} then if the results
 * was not {@code false}* then executes a pre-specified instruction {@code B} otherwise
 * executes a pre-specified instruction {@code C}.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.24
 */
public class BranchExecInstr0Instr1Instr2 implements Instruction {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -9034207643511548467L;

	/**
	 * The condition instruction.
	 *
	 * @since 0.2.0 ~2021.05.24
	 */
	@NotNull
	protected final Instruction instruction0;
	/**
	 * The instruction to be executed when the condition passes.
	 *
	 * @since 0.2.0 ~2021.05.24
	 */
	@NotNull
	protected final Instruction instruction1;
	/**
	 * The instruction to be executed when the condition fails.
	 *
	 * @since 0.2.0 ~2021.05.24
	 */
	@NotNull
	protected final Instruction instruction2;
	/**
	 * The tree this instruction was declared at.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	@Nullable
	protected final Tree tree;

	/**
	 * Construct a new instruction that executes the given {@code instruction0} and
	 * executes the given {@code instruction1} when the condition succeed.
	 *
	 * @param instruction0 the condition instruction.
	 * @param instruction1 the instruction to be executed when the condition succeed.
	 * @throws NullPointerException if the given {@code instruction0} or {@code
	 *                              instruction1} is null.
	 * @since 0.2.0 ~2021.05.24
	 */
	public BranchExecInstr0Instr1Instr2(@NotNull Instruction instruction0, @NotNull Instruction instruction1) {
		Objects.requireNonNull(instruction0, "instruction0");
		Objects.requireNonNull(instruction1, "instruction1");
		this.tree = null;
		this.instruction0 = instruction0;
		this.instruction1 = instruction1;
		this.instruction2 = (e, m) -> {
		};
	}

	/**
	 * Construct a new instruction that executes the given {@code instruction0} and
	 * executes the given {@code instruction1} when the condition succeed and execute the
	 * given {@code instruction2} when the condition fails.
	 *
	 * @param instruction0 the condition instruction.
	 * @param instruction1 the instruction to be executed when the condition succeed.
	 * @param instruction2 the instruction to be executed when the condition fails.
	 * @throws NullPointerException if the given {@code instruction0} or {@code
	 *                              instruction1} or {@code instruction2} is null.
	 * @since 0.2.0 ~2021.05.24
	 */
	public BranchExecInstr0Instr1Instr2(@NotNull Instruction instruction0, @NotNull Instruction instruction1, @NotNull Instruction instruction2) {
		Objects.requireNonNull(instruction0, "instruction0");
		Objects.requireNonNull(instruction1, "instruction1");
		Objects.requireNonNull(instruction2, "instruction2");
		this.tree = null;
		this.instruction0 = instruction0;
		this.instruction1 = instruction1;
		this.instruction2 = instruction2;
	}

	/**
	 * Construct a new instruction that executes the given {@code instruction0} and
	 * executes the given {@code instruction1} when the condition succeed.
	 *
	 * @param tree         the tree from where this instruction was declared.
	 * @param instruction0 the condition instruction.
	 * @param instruction1 the instruction to be executed when the condition succeed.
	 * @throws NullPointerException if the given {@code instruction0} or {@code
	 *                              instruction1} is null.
	 * @since 0.2.0 ~2021.05.24
	 */
	public BranchExecInstr0Instr1Instr2(@NotNull Tree tree, @NotNull Instruction instruction0, @NotNull Instruction instruction1) {
		Objects.requireNonNull(tree, "tree");
		Objects.requireNonNull(instruction0, "instruction0");
		Objects.requireNonNull(instruction1, "instruction1");
		this.tree = tree;
		this.instruction0 = instruction0;
		this.instruction1 = instruction1;
		this.instruction2 = Instruction.empty(tree);
	}

	/**
	 * Construct a new instruction that executes the given {@code instruction0} and
	 * executes the given {@code instruction1} when the condition succeed and execute the
	 * given {@code instruction2} when the condition fails.
	 *
	 * @param tree         the tree from where this instruction was declared.
	 * @param instruction0 the condition instruction.
	 * @param instruction1 the instruction to be executed when the condition succeed.
	 * @param instruction2 the instruction to be executed when the condition fails.
	 * @throws NullPointerException if the given {@code instruction0} or {@code
	 *                              instruction1} or {@code instruction2} is null.
	 * @since 0.2.0 ~2021.05.24
	 */
	public BranchExecInstr0Instr1Instr2(@NotNull Tree tree, @NotNull Instruction instruction0, @NotNull Instruction instruction1, @NotNull Instruction instruction2) {
		Objects.requireNonNull(tree, "tree");
		Objects.requireNonNull(instruction0, "instruction0");
		Objects.requireNonNull(instruction1, "instruction1");
		Objects.requireNonNull(instruction2, "instruction2");
		this.tree = tree;
		this.instruction0 = instruction0;
		this.instruction1 = instruction1;
		this.instruction2 = instruction2;
	}

	@Override
	public void exec(@NotNull Environment environment, @NotNull Memory memory) {
		Objects.requireNonNull(environment, "environment");
		Objects.requireNonNull(memory, "memory");

		// EXEC( INSTR0 )
		memory.pushFrame(new Frame(this.instruction0));
		this.instruction0.exec(environment, memory);
		Value value = Memories.joinPop(memory);
		memory.popFrame();

		//BRANCH( EXEC( INSTR0 ) , INSTR1 , INSTR2 )
		String text = value.evaluate(memory);
		switch (text) {
			case "":
			case "0":
			case "\0":
			case "false":
				memory.pushFrame(new Frame(this.instruction2));
				this.instruction2.exec(environment, memory);
				memory.popFrame();
				break;
			default:
				memory.pushFrame(new Frame(this.instruction1));
				this.instruction1.exec(environment, memory);
				memory.popFrame();
				break;
		}
	}

	@Nullable
	@Override
	public Tree getTree() {
		return this.tree;
	}
}
