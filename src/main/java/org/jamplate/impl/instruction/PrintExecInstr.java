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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * An instruction that prints the values returned from another instruction.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.24
 */
public class PrintExecInstr implements Instruction {
	//PRINT( EXEC( INSTR ) )

	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -1584518958362936537L;

	/**
	 * The instruction to inject its results.
	 *
	 * @since 0.2.0 ~2021.05.24
	 */
	@NotNull
	protected final Instruction instruction;
	/**
	 * The tree this instruction was declared at.
	 *
	 * @since 0.2.0 ~2021.05.24
	 */
	@Nullable
	protected final Tree tree;

	/**
	 * Construct a new instruction that prints the output of the given {@code
	 * instruction}.
	 *
	 * @param instruction the instruction to print its value.
	 * @throws NullPointerException if the given {@code instruction} is null.
	 * @since 0.2.0 ~2021.05.24
	 */
	public PrintExecInstr(@NotNull Instruction instruction) {
		Objects.requireNonNull(instruction, "instruction");
		this.tree = null;
		this.instruction = instruction;
	}

	/**
	 * Construct a new instruction that prints the output of the given {@code
	 * instruction}.
	 *
	 * @param tree        the tree from where this instruction was declared.
	 * @param instruction the instruction to print its value.
	 * @throws NullPointerException if the given {@code tree} or {@code instruction} is
	 *                              null.
	 * @since 0.2.0 ~2021.05.24
	 */
	public PrintExecInstr(@NotNull Tree tree, @NotNull Instruction instruction) {
		Objects.requireNonNull(tree, "tree");
		Objects.requireNonNull(instruction, "instruction");
		this.tree = tree;
		this.instruction = instruction;
	}

	@Override
	public void exec(@NotNull Environment environment, @NotNull Memory memory) {
		Objects.requireNonNull(environment, "environment");
		Objects.requireNonNull(memory, "memory");

		memory.pushFrame(new Memory.Frame(this.instruction));
		this.instruction.exec(environment, memory);
		List<Value> values = new ArrayList<>();
		Value v;
		while ((v = memory.pop()) != Value.NULL)
			values.add(v);
		memory.popFrame();

		for (Value value : values) {
			String injection = value.evaluate(memory);

			memory.print(injection);
		}
	}

	@Nullable
	@Override
	public Tree getTree() {
		return this.tree;
	}
}
