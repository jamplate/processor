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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <h3>{@code CPED( ADDR , XINSTR )}</h3>
 * An instruction that executes a pre-specified array of instructions and capture the
 * printed text and allocate it to a pre-specified address in the heap.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.28
 */
public class CpedAddrXinstr implements Instruction {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 1740860675009877502L;

	/**
	 * The address to allocate the captured text to.
	 *
	 * @since 0.2.0 ~2021.06.01
	 */
	@NotNull
	protected final String address;
	/**
	 * The instructions to capture its output.
	 *
	 * @since 0.2.0 ~2021.06.01
	 */
	@NotNull
	protected final List<Instruction> instructions;
	/**
	 * The tree from where this instruction was declared.
	 *
	 * @since 0.2.0 ~2021.06.01
	 */
	@Nullable
	protected final Tree tree;

	/**
	 * Construct a new instruction that executes the given {@code instructions} and
	 * capture their output then allocate it to the given {@code address} in the heap.
	 * <br>
	 * Null instructions in the array will be ignored.
	 *
	 * @param address      the address where to allocate the captured text to.
	 * @param instructions the instructions to be executed and their output to be
	 *                     captured.
	 * @throws NullPointerException if the given {@code address} or {@code instructions}
	 *                              is null.
	 * @since 0.2.0 ~2021.06.01
	 */
	public CpedAddrXinstr(@NotNull String address, @Nullable Instruction @NotNull ... instructions) {
		Objects.requireNonNull(address, "address");
		Objects.requireNonNull(instructions, "instructions");
		this.tree = null;
		this.address = address;
		this.instructions = Arrays
				.stream(instructions)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}

	/**
	 * Construct a new instruction that executes the given {@code instructions} and
	 * capture their output then allocate it to the given {@code address} in the heap.
	 * <br>
	 * Null instructions in the list will be ignored.
	 *
	 * @param address      the address where to allocate the captured text to.
	 * @param instructions the instructions to be executed and their output to be
	 *                     captured.
	 * @throws NullPointerException if the given {@code address} or {@code instructions}
	 *                              is null.
	 * @since 0.2.0 ~2021.06.01
	 */
	public CpedAddrXinstr(@NotNull String address, @NotNull List<Instruction> instructions) {
		Objects.requireNonNull(address, "address");
		Objects.requireNonNull(instructions, "instructions");
		this.tree = null;
		this.address = address;
		this.instructions = new ArrayList<>();
		for (Instruction i : instructions)
			if (i != null)
				this.instructions.add(i);
	}

	/**
	 * Construct a new instruction that executes the given {@code instructions} and
	 * capture their output then allocate it to the given {@code address} in the heap.
	 * <br>
	 * Null instructions in the array will be ignored.
	 *
	 * @param tree         the tree from where this instruction was declared.
	 * @param address      the address where to allocate the captured text to.
	 * @param instructions the instructions to be executed and their output to be
	 *                     captured.
	 * @throws NullPointerException if the given {@code tree} or {@code address} or {@code
	 *                              instructions} is null.
	 * @since 0.2.0 ~2021.06.01
	 */
	public CpedAddrXinstr(@NotNull Tree tree, @NotNull String address, @Nullable Instruction @NotNull ... instructions) {
		Objects.requireNonNull(tree, "tree");
		Objects.requireNonNull(address, "address");
		Objects.requireNonNull(instructions, "instructions");
		this.tree = tree;
		this.address = address;
		this.instructions = Arrays
				.stream(instructions)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}

	/**
	 * Construct a new instruction that executes the given {@code instructions} and
	 * capture their output then allocate it to the given {@code address} in the heap.
	 * <br>
	 * Null instructions in the list will be ignored.
	 *
	 * @param tree         the tree from where this instruction was declared.
	 * @param address      the address where to allocate the captured text to.
	 * @param instructions the instructions to be executed and their output to be
	 *                     captured.
	 * @throws NullPointerException if the given {@code tree} or {@code address} or {@code
	 *                              instructions} is null.
	 * @since 0.2.0 ~2021.06.01
	 */
	public CpedAddrXinstr(@NotNull Tree tree, @NotNull String address, @NotNull List<Instruction> instructions) {
		Objects.requireNonNull(tree, "tree");
		Objects.requireNonNull(address, "address");
		Objects.requireNonNull(instructions, "instructions");
		this.tree = tree;
		this.address = address;
		this.instructions = new ArrayList<>();
		for (Instruction i : instructions)
			if (i != null)
				this.instructions.add(i);
	}

	@Override
	public void exec(@NotNull Environment environment, @NotNull Memory memory) {
		Objects.requireNonNull(environment, "environment");
		Objects.requireNonNull(memory, "memory");

		//CPED( ADDR , XINSTR )
		Appendable console = memory.getConsole();

		StringBuilder capture = new StringBuilder();

		memory.setConsole(capture);

		for (Instruction instruction : this.instructions) {
			memory.pushFrame(new Frame(instruction));
			instruction.exec(environment, memory);
			memory.dumpFrame();
		}

		memory.setConsole(console);

		String address = this.address;
		String text = capture.toString();
		memory.set(address, m -> text);
	}

	@Nullable
	@Override
	public Tree getTree() {
		return this.tree;
	}
}
