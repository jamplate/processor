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

import org.jamplate.impl.util.Memories;
import org.jamplate.impl.util.Values;
import org.jamplate.model.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * <h3>{@code PUSH( NEG( EXEC( INSTR ) ) )}</h3>
 * An instruction that pushes the results of negating the results of executing a
 * pre-specified instruction.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.06.03
 */
public class PushNegExecInstr implements Instruction {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -1752830169644522620L;

	/**
	 * The instruction in the problem.
	 *
	 * @since 0.2.0 ~2021.06.03
	 */
	@NotNull
	protected final Instruction instruction;
	/**
	 * The tree this instruction was declared at.
	 *
	 * @since 0.2.0 ~2021.06.03
	 */
	@Nullable
	protected final Tree tree;

	/**
	 * Construct a new instruction that pushes the results of negating the result of
	 * executing the given {@code instruction}.
	 *
	 * @param instruction the instruction.
	 * @throws NullPointerException if the given {@code instruction} is null.
	 * @since 0.2.0 ~2021.06.03
	 */
	public PushNegExecInstr(@NotNull Instruction instruction) {
		Objects.requireNonNull(instruction, "instruction");
		this.tree = null;
		this.instruction = instruction;
	}

	/**
	 * Construct a new instruction that pushes the results of negating the result of
	 * executing the given {@code instruction}.
	 *
	 * @param tree        the tree from where this instruction was declared.
	 * @param instruction the instruction.
	 * @throws NullPointerException if the given {@code tree} or {@code instruction} is
	 *                              null.
	 * @since 0.2.0 ~2021.06.03
	 */
	public PushNegExecInstr(@NotNull Tree tree, @NotNull Instruction instruction) {
		Objects.requireNonNull(tree, "tree");
		Objects.requireNonNull(instruction, "instruction");
		this.tree = tree;
		this.instruction = instruction;
	}

	@Override
	public void exec(@NotNull Environment environment, @NotNull Memory memory) {
		Objects.requireNonNull(environment, "environment");
		Objects.requireNonNull(memory, "memory");

		//EXEC( INSTR )
		memory.pushFrame(new Frame(this.instruction));
		this.instruction.exec(environment, memory);
		Value value = Memories.joinPop(memory);
		memory.popFrame();

		//NEG( EXEC( INSTR ) )
		String text1 = value.evaluate(memory);
		String text2 = Boolean.toString(!Values.toBoolean(text1));

		//PUSH( NEG( EXEC( INSTR ) ) )
		memory.push(m -> text2);
	}

	@Nullable
	@Override
	public Tree getTree() {
		return this.tree;
	}
}
