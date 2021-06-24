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

import org.jamplate.model.Environment;
import org.jamplate.model.Instruction;
import org.jamplate.model.Memory;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * An instruction that executes a pre-specified instruction list when.
 * <br><br>
 * Memory Visualization:
 * <pre>
 *     No memory manipulation
 * </pre>
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.11
 */
public class Block implements Instruction {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 8927808313803502091L;

	/**
	 * The instructions to be executed by this instruction when this instruction get
	 * executed.
	 *
	 * @since 0.3.0 ~2021.06.12
	 */
	@NotNull
	protected final List<Instruction> instructions;
	/**
	 * A reference of this instruction in the source code.
	 *
	 * @since 0.3.0 ~2021.06.11
	 */
	@Nullable
	protected final Tree tree;

	/**
	 * Construct a new instructions block that executes the given {@code instructions} in
	 * order.
	 * <br>
	 * Note: if a block instruction is among the given {@code instruction}, the
	 * constructed block might just flatten that block.
	 * <br>
	 * Note: {@link Idle} instructions in the given array might be ignored.
	 * <br>
	 * Note: nulls in the given array will be completely ignored.
	 *
	 * @param instructions the instructions for the constructed instruction to execute.
	 * @throws NullPointerException if the given {@code instructions} is null.
	 * @since 0.3.0 ~2021.06.12
	 */
	public Block(@Nullable Instruction @NotNull ... instructions) {
		Objects.requireNonNull(instructions, "instructions");
		this.tree = null;
		this.instructions =
				Arrays.stream(instructions)
					  .filter(Objects::nonNull)
					  .flatMap(instruction -> {
						  if (instruction instanceof Block) {
							  Block block = (Block) instruction;

							  return block.instructions.stream();
						  }

						  if (instruction instanceof Idle)
							  return Stream.empty();

						  return Stream.of(instruction);
					  })
					  .collect(Collectors.toList());
	}

	/**
	 * Construct a new instructions block that executes the given {@code instructions} in
	 * order.
	 * <br>
	 * Note: if a block instruction is among the given {@code instruction}, the
	 * constructed block might just flatten that block.
	 * <br>
	 * Note: {@link Idle} instructions in the given array might be ignored.
	 * <br>
	 * Note: nulls in the given iterable will be completely ignored.
	 *
	 * @param instructions the instructions for the constructed instruction to execute.
	 * @throws NullPointerException if the given {@code instructions} is null.
	 * @since 0.3.0 ~2021.06.12
	 */
	public Block(@NotNull Iterable<Instruction> instructions) {
		Objects.requireNonNull(instructions, "instructions");
		this.tree = null;
		this.instructions = new ArrayList<>();
		for (Instruction instruction : instructions) {
			if (instruction instanceof Block) {
				Block block = (Block) instruction;

				this.instructions.addAll(block.instructions);
				continue;
			}

			if (instruction instanceof Idle)
				continue;

			if (instruction != null)
				this.instructions.add(instruction);
		}
	}

	/**
	 * Construct a new instructions block that executes the given {@code instructions} in
	 * order.
	 * <br>
	 * Note: if a block instruction is among the given {@code instruction}, the
	 * constructed block might just flatten that block.
	 * <br>
	 * Note: {@link Idle} instructions in the given array might be ignored.
	 * <br>
	 * Note: nulls in the given array will be completely ignored.
	 *
	 * @param tree         a reference for the constructed instruction in the source
	 *                     code.
	 * @param instructions the instructions for the constructed instruction to execute.
	 * @throws NullPointerException if the given {@code tree} or {@code instructions} is
	 *                              null.
	 * @since 0.3.0 ~2021.06.12
	 */
	public Block(@NotNull Tree tree, @Nullable Instruction @NotNull ... instructions) {
		Objects.requireNonNull(tree, "tree");
		Objects.requireNonNull(instructions, "instructions");
		this.tree = tree;
		this.instructions =
				Arrays.stream(instructions)
					  .filter(Objects::nonNull)
					  .flatMap(instruction -> {
						  if (instruction instanceof Block) {
							  Block block = (Block) instruction;

							  return block.instructions.stream();
						  }
						  if (instruction instanceof Idle)
							  return Stream.empty();

						  return Stream.of(instruction);
					  })
					  .collect(Collectors.toList());
	}

	/**
	 * Construct a new instructions block that executes the given {@code instructions} in
	 * order.
	 * <br>
	 * Note: if a block instruction is among the given {@code instruction}, the
	 * constructed block might just flatten that block.
	 * <br>
	 * Note: {@link Idle} instructions in the given array might be ignored.
	 * <br>
	 * Note: nulls in the given iterable will be completely ignored.
	 *
	 * @param tree         a reference for the constructed instruction in the source
	 *                     code.
	 * @param instructions the instructions for the constructed instruction to execute.
	 * @throws NullPointerException if the given {@code tree} or {@code instructions} is
	 *                              null.
	 * @since 0.3.0 ~2021.06.12
	 */
	public Block(@NotNull Tree tree, @NotNull Iterable<Instruction> instructions) {
		Objects.requireNonNull(tree, "tree");
		Objects.requireNonNull(instructions, "instructions");
		this.tree = tree;
		this.instructions = new ArrayList<>();
		for (Instruction instruction : instructions) {
			if (instruction instanceof Block) {
				Block block = (Block) instruction;

				this.instructions.addAll(block.instructions);
				continue;
			}

			if (instruction instanceof Idle)
				continue;

			if (instruction != null)
				this.instructions.add(instruction);
		}
	}

	@Override
	public void exec(@NotNull Environment environment, @NotNull Memory memory) {
		Objects.requireNonNull(environment, "environment");
		Objects.requireNonNull(memory, "memory");

		for (Instruction instruction : this.instructions)
			instruction.exec(environment, memory);
	}

	@Nullable
	@Override
	public Tree getTree() {
		return this.tree;
	}
}
