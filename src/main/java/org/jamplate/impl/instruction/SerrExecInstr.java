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
import org.jamplate.model.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * <h3>SERR( EXEC( INSTR ) )</h3>
 * An instruction outputting the result of executing a pre-specified instruction to the
 * system error console.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.06.02
 */
public class SerrExecInstr implements Instruction {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 8281068981084721015L;

	/**
	 * The instruction to output its results.
	 *
	 * @since 0.2.0 ~2021.06.02
	 */
	@NotNull
	protected final Instruction instruction;
	/**
	 * The tree from where this instruction was declared.
	 *
	 * @since 0.2.0 ~2021.06.02
	 */
	@Nullable
	protected final Tree tree;

	/**
	 * Construct a new instruction that executes the given {@code instruction} and outputs
	 * its results to the system error console.
	 *
	 * @param instruction the instruction to be executed.
	 * @throws NullPointerException if the given {@code instruction} is null.
	 * @since 0.2.0 ~2021.06.02
	 */
	public SerrExecInstr(@NotNull Instruction instruction) {
		Objects.requireNonNull(instruction, "instruction");
		this.tree = null;
		this.instruction = instruction;
	}

	/**
	 * Construct a new instruction that executes the given {@code instruction} and outputs
	 * its results to the system erro console.
	 *
	 * @param tree        the tree of the constructed instruction.
	 * @param instruction the instruction to be executed.
	 * @throws NullPointerException if the given {@code tree} or {@code instruction} is
	 *                              null.
	 * @since 0.2.0 ~2021.06.02
	 */
	public SerrExecInstr(@NotNull Tree tree, @NotNull Instruction instruction) {
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

		//SERR( EXEC( INSTR ) )
		String text = value.evaluate(memory);
		//noinspection UseOfSystemOutOrSystemErr
		System.err.print(text);
	}
}
