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
import org.jamplate.util.Memories;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

/**
 * <h3>{@code CONSOLE( EXEC( INSTR ) )}</h3>
 * An instruction executes a pre-specified instruction and set the console to be the
 * results of the execution.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.23
 */
public class ConsoleExecInstr implements Instruction {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 5922427070959211074L;

	/**
	 * The instruction to be executed.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	@NotNull
	protected final Instruction instruction;
	/**
	 * The tree this instruction was declared at.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	@Nullable
	protected final Tree tree;

	/**
	 * Construct a new instruction that sets the console to be outputting to the file with
	 * the name equals to the results of evaluating the value returned from executing the
	 * given {@code instruction}.
	 *
	 * @param instruction the instruction to perform the constructed instruction on the
	 *                    value returned from executing it.
	 * @throws NullPointerException if the given {@code instruction} is null.
	 * @since 0.2.0 ~ 2021.05.23
	 */
	public ConsoleExecInstr(@NotNull Instruction instruction) {
		Objects.requireNonNull(instruction, "instruction");
		this.tree = null;
		this.instruction = instruction;
	}

	/**
	 * Construct a new instruction that sets the console to be outputting to the file with
	 * the name equals to the results of evaluating the value returned from executing the
	 * given {@code instruction}.
	 *
	 * @param tree        the tree from where this instruction was declared.
	 * @param instruction the instruction to perform the constructed instruction on the
	 *                    value returned from executing it.
	 * @throws NullPointerException if the given {@code instruction} is null.
	 * @since 0.2.0 ~ 2021.05.23
	 */
	public ConsoleExecInstr(@NotNull Tree tree, @NotNull Instruction instruction) {
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
		memory.pushFrame(new Memory.Frame(this.instruction));
		this.instruction.exec(environment, memory);
		Value value = Memories.joinPop(memory);
		memory.popFrame();

		//CONSOLE( EXEC( INSTR ) )
		if (value == Value.NULL) {
			memory.setConsole(new StringBuilder());
			return;
		}

		String text = value.evaluate(memory);

		try {
			File file = new File(text);
			File parent = file.getParentFile();

			if (!parent.exists())
				Files.createDirectories(parent.toPath());

			memory.setConsole(new FileWriter(file));
		} catch (IOException e) {
			throw new ExecutionException(e, this.tree);
		}
	}

	@Nullable
	@Override
	public Tree getTree() {
		return this.tree;
	}
}
