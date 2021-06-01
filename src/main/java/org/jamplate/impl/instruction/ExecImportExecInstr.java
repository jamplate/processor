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
 * <h3>{@code EXEC( IMPORT( EXEC( INSTR ) ) )}</h3>
 * An instruction that executes a pre-specified instruction, then import with the returned
 * results, then execute the imported instruction.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.23
 */
public class ExecImportExecInstr implements Instruction {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 3241587385841635191L;

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
	 * Construct a new instruction that import the instruction with the results of
	 * executing the given {@code instruction}.
	 *
	 * @param instruction the instruction to be executed.
	 * @throws NullPointerException if the given {@code instruction} is null.
	 * @since 0.2.0 ~ 2021.05.23
	 */
	public ExecImportExecInstr(@NotNull Instruction instruction) {
		Objects.requireNonNull(instruction, "instruction");
		this.tree = null;
		this.instruction = instruction;
	}

	/**
	 * Construct a new instruction that import the instruction with the results of
	 * executing the given {@code instruction}.
	 *
	 * @param tree        the tree from where this instruction was declared.
	 * @param instruction the instruction to be executed.
	 * @throws NullPointerException if the given {@code tree} or {@code instruction} is
	 *                              null.
	 * @since 0.2.0 ~ 2021.05.23
	 */
	public ExecImportExecInstr(@NotNull Tree tree, @NotNull Instruction instruction) {
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

		//IMPORT( EXEC( INSTR ) )
		if (value == Value.NULL)
			throw new ExecutionException(
					"Cannot import null",
					this.tree
			);
		String text = value.evaluate(memory);
		Compilation compilation = environment.getCompilation(text);
		if (compilation == null)
			throw new ExecutionException(
					"Compilation does not exist: " + text,
					this.tree
			);
		Instruction instruction = compilation.getInstruction();
		if (instruction == null)
			throw new ExecutionException(
					"Compilation contain no instruction: " +
					compilation.getRootTree().document(),
					compilation.getRootTree()
			);

		//EXEC( IMPORT( EXEC( INSTR ) ) )
		memory.pushFrame(new Frame(instruction));
		instruction.exec(environment, memory);
		memory.popFrame();
	}

	@Nullable
	@Override
	public Tree getTree() {
		return this.tree;
	}
}
