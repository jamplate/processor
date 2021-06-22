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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <h3>{@code WPED( INSTR , XINSTR )}</h3>
 * An instruction that keeps executing a pre-specified list of instruction while the
 * execution of a pre-specified instruction keeps resulting to true.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.06.03
 */
@Deprecated
public class WpedInstrXinstr implements Instruction {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -5104109791490011236L;

	/**
	 * The condition instruction.
	 *
	 * @since 0.2.0 ~2021.06.03
	 */
	@NotNull
	protected final Instruction instruction;
	/**
	 * The instructions to be executed in each iteration.
	 *
	 * @since 0.2.0 ~2021.06.03
	 */
	@NotNull
	protected final List<Instruction> instructions;
	/**
	 * The tree from where this instruction was declared.
	 *
	 * @since 0.2.0 ~2021.05.28
	 */
	@Nullable
	protected final Tree tree;

	/**
	 * Construct a new instruction that executes the given {@code instructions} while
	 * executing the given {@code instruction} evaluates to true.
	 * <br>
	 * Null instructions in the array will be ignored.
	 *
	 * @param instruction  the condition instruction.
	 * @param instructions the instructions to iterate.
	 * @throws NullPointerException if the given {@code instruction} or {@code
	 *                              instructions} is null.
	 * @since 0.2.0 ~2021.06.03
	 */
	public WpedInstrXinstr(@NotNull Instruction instruction, @Nullable Instruction @NotNull ... instructions) {
		Objects.requireNonNull(instruction, "instruction");
		Objects.requireNonNull(instructions, "instructions");
		this.tree = null;
		this.instruction = instruction;
		this.instructions = Arrays
				.stream(instructions)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}

	/**
	 * Construct a new instruction that executes the given {@code instructions} while
	 * executing the given {@code instruction} evaluates to true.
	 * <br>
	 * Null instructions in the list will be ignored.
	 *
	 * @param instruction  the condition instruction.
	 * @param instructions the instructions to iterate.
	 * @throws NullPointerException if the given {@code instruction} or {@code
	 *                              instructions} is null.
	 * @since 0.2.0 ~2021.06.03
	 */
	public WpedInstrXinstr(@NotNull Instruction instruction, @NotNull List<Instruction> instructions) {
		Objects.requireNonNull(instruction, "instruction");
		Objects.requireNonNull(instructions, "instructions");
		this.tree = null;
		this.instruction = instruction;
		this.instructions = new ArrayList<>();
		for (Instruction i : instructions)
			if (i != null)
				this.instructions.add(i);
	}

	/**
	 * Construct a new instruction that executes the given {@code instructions} while
	 * executing the given {@code instruction} evaluates to true.
	 * <br>
	 * Null instructions in the array will be ignored.
	 *
	 * @param tree         the tree from where this instruction was declared.
	 * @param instruction  the condition instruction.
	 * @param instructions the instructions to iterate.
	 * @throws NullPointerException if the given {@code tree} or {@code instruction} or
	 *                              {@code instructions} is null.
	 * @since 0.2.0 ~2021.06.03
	 */
	public WpedInstrXinstr(@NotNull Tree tree, @NotNull Instruction instruction, @Nullable Instruction @NotNull ... instructions) {
		Objects.requireNonNull(tree, "tree");
		Objects.requireNonNull(instruction, "instruction");
		Objects.requireNonNull(instructions, "instructions");
		this.tree = tree;
		this.instruction = instruction;
		this.instructions = Arrays
				.stream(instructions)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}

	/**
	 * Construct a new instruction that executes the given {@code instructions} while
	 * executing the given {@code instruction} evaluates to true.
	 * <br>
	 * Null instructions in the list will be ignored.
	 *
	 * @param tree         the tree from where this instruction was declared.
	 * @param instruction  the condition instruction.
	 * @param instructions the instructions to iterate.
	 * @throws NullPointerException if the given {@code tree} or {@code instruction} or
	 *                              {@code instructions} is null.
	 * @since 0.2.0 ~2021.06.03
	 */
	public WpedInstrXinstr(@NotNull Tree tree, @NotNull Instruction instruction, @NotNull List<Instruction> instructions) {
		Objects.requireNonNull(tree, "tree");
		Objects.requireNonNull(instruction, "instruction");
		Objects.requireNonNull(instructions, "instructions");
		this.tree = tree;
		this.instruction = instruction;
		this.instructions = new ArrayList<>();
		for (Instruction i : instructions)
			if (i != null)
				this.instructions.add(i);
	}

	@Override
	public void exec(@NotNull Environment environment, @NotNull Memory memory) {
		Objects.requireNonNull(environment, "environment");
		Objects.requireNonNull(memory, "memory");

		//WPED( INSTR , XINSTR )
		while (true) {
			memory.pushFrame(new Frame(this.instruction));
			this.instruction.exec(environment, memory);
			Value value = Memories.joinPop(memory);
			memory.popFrame();

			String text = value.evaluate(memory);

			if (Values.toBoolean(text)) {
				memory.pushFrame(new Frame(this));

				for (Instruction instruction : this.instructions) {
					memory.pushFrame(new Frame(instruction));
					instruction.exec(environment, memory);
					memory.dumpFrame();
				}

				memory.popFrame();
				continue;
			}

			break;
		}
	}
}
