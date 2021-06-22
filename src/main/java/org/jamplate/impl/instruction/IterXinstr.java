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

/**
 * <h3>{@code ITER( XINSTR )}</h3>
 * An instruction that {@link org.jamplate.impl.instruction ITER} a pre-specified array of
 * instructions.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.31
 */
@Deprecated
public class IterXinstr implements Instruction {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 1714505823832660137L;

	/**
	 * The instructions to be executed.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	protected final List<Instruction> instructions;
	/**
	 * The tree from where this instruction was declared.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@Nullable
	protected final Tree tree;

	/**
	 * Construct a new instruction that executes the given {@code instructions} in order
	 * in {@link org.jamplate.impl.instruction ITER} style.
	 * <br>
	 * Null instructions in the array will be ignored.
	 *
	 * @param instructions the instructions to be executed.
	 * @throws NullPointerException if the given {@code instructions} is null.
	 * @since 0.2.0 ~2021.05.31
	 */
	public IterXinstr(@Nullable Instruction @NotNull ... instructions) {
		Objects.requireNonNull(instructions, "instructions");
		this.tree = null;
		this.instructions = Arrays
				.stream(instructions)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}

	/**
	 * Construct a new instruction that executes the given {@code instructions} in order
	 * in a {@link org.jamplate.impl.instruction ITER} style.
	 * <br>
	 * Null instructions in the list will be ignored.
	 *
	 * @param instructions the instructions to be executed.
	 * @throws NullPointerException if the given {@code instructions} is null.
	 * @since 0.2.0 ~2021.05.31
	 */
	public IterXinstr(@NotNull List<Instruction> instructions) {
		Objects.requireNonNull(instructions, "instructions");
		this.tree = null;
		this.instructions = new ArrayList<>();
		for (Instruction instruction : instructions)
			if (instruction != null)
				this.instructions.add(instruction);
	}

	/**
	 * Construct a new instruction that executes the given {@code instructions} in order
	 * in a {@link org.jamplate.impl.instruction ITER} style.
	 * <br>
	 * Null instructions in the array will be ignored.
	 *
	 * @param tree         the tree from where this instruction was declared.
	 * @param instructions the instructions to be executed.
	 * @throws NullPointerException if the given {@code tree} or {@code instructions} is
	 *                              null.
	 * @since 0.2.0 ~2021.05.31
	 */
	public IterXinstr(@NotNull Tree tree, @Nullable Instruction @NotNull ... instructions) {
		Objects.requireNonNull(instructions, "instructions");
		this.tree = tree;
		this.instructions = Arrays
				.stream(instructions)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}

	/**
	 * Construct a new instruction that executes the given {@code instructions} in order
	 * in a {@link org.jamplate.impl.instruction ITER} style.
	 * <br>
	 * Null instructions in the list will be ignored.
	 *
	 * @param tree         the tree from where this instruction was declared.
	 * @param instructions the instructions to be executed.
	 * @throws NullPointerException if the given {@code tree} or {@code instructions} is
	 *                              null.
	 * @since 0.2.0 ~2021.05.31
	 */
	public IterXinstr(@NotNull Tree tree, @NotNull List<Instruction> instructions) {
		Objects.requireNonNull(instructions, "instructions");
		this.tree = tree;
		this.instructions = new ArrayList<>();
		for (Instruction instruction : instructions)
			if (instruction != null)
				this.instructions.add(instruction);
	}

	@Override
	public void exec(@NotNull Environment environment, @NotNull Memory memory) {
		Objects.requireNonNull(environment, "environment");
		Objects.requireNonNull(memory, "memory");

		//ITER( XINSTR )
		for (Instruction instruction : this.instructions)
			instruction.exec(environment, memory);
	}

	@Nullable
	@Override
	public Tree getTree() {
		return this.tree;
	}
}
