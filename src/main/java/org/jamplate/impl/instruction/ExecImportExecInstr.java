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

import java.util.Objects;

/**
 * An instruction that executes another variable instruction with the name returned from
 * another third predefined instruction.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.23
 */
public class ExecImportExecInstr implements Instruction {
	//EXEC( IMPORT( EXEC( INSTR ) ) )

	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 3241587385841635191L;

	/**
	 * The instruction of the parameter.
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
	 * Construct a new include instruction that executes the instruction in the
	 * environment that has the name equal to the string from evaluating the value
	 * returned from executing the given {@code instruction}.
	 *
	 * @param instruction the instruction to perform the constructed instruction on the
	 *                    value returned from executing it.
	 * @throws NullPointerException if the given {@code instruction} is null.
	 * @since 0.2.0 ~ 2021.05.23
	 */
	public ExecImportExecInstr(@NotNull Instruction instruction) {
		Objects.requireNonNull(instruction, "instruction");
		this.tree = null;
		this.instruction = instruction;
	}

	/**
	 * Construct a new include instruction that executes the instruction in the
	 * environment that has the name equal to the string from evaluating the value
	 * returned from executing the given {@code instruction}.
	 *
	 * @param tree        the tree from where this instruction was declared.
	 * @param instruction the instruction to perform the constructed instruction on the
	 *                    value returned from executing it.
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

		memory.pushFrame(new Memory.Frame(this.instruction));
		this.instruction.exec(environment, memory);
		Value parameterValue = memory.pop();
		memory.popFrame();

		if (parameterValue == Value.NULL)
			throw new ExecutionException(
					"Include requires a value",
					this.tree
			);

		String parameter = parameterValue.evaluate(memory);

		Compilation includeCompilation = environment.getCompilation(parameter);

		if (includeCompilation == null)
			throw new ExecutionException(
					"Cannot find a compilation with the name: " + parameter,
					this.tree
			);

		Instruction includeInstruction = includeCompilation.getInstruction();

		if (includeInstruction == null)
			throw new ExecutionException(
					"No instruction found for the compilation: " +
					includeCompilation.getRootTree().document(),
					includeCompilation.getRootTree()
			);

		memory.pushFrame(new Memory.Frame(includeInstruction));
		includeInstruction.exec(environment, memory);
		memory.popFrame();
	}

	@Nullable
	@Override
	public Tree getTree() {
		return this.tree;
	}
}
