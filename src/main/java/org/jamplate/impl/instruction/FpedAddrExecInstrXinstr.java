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

import org.jamplate.impl.util.JSONUtil;
import org.jamplate.impl.util.Memories;
import org.jamplate.model.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <h3>{@code FPED( ADDR , EXEC( INSTR ) , XINSTR )}</h3>
 * An instruction that {@link org.jamplate.impl.instruction FPED} a pre-specified array of
 * instructions foreach item resultant fro executing a pre-specified instruction and
 * allocating to a pre-specified address.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.28
 */
public class FpedAddrExecInstrXinstr implements Instruction {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 1740860675009877502L;

	/**
	 * The address to allocate the item of each iteration to.
	 *
	 * @since 0.2.0 ~2021.05.28
	 */
	@NotNull
	protected final String address;
	/**
	 * The instruction to iterate the items resultant from executing it.
	 *
	 * @since 0.2.0 ~2021.05.28
	 */
	@NotNull
	protected final Instruction instruction;
	/**
	 * The instructions to be iterated.
	 *
	 * @since 0.2.0 ~2021.05.28
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
	 * Construct a new instruction that executes the given {@code instructions} foreach
	 * item resultant from executing the given {@code instruction}.
	 * <br>
	 * Null instructions in the array will be ignored.
	 *
	 * @param address      the address where to allocate the item to on each iteration.
	 * @param instruction  the instruction to be executed to get the items.
	 * @param instructions the instructions to iterate.
	 * @throws NullPointerException if the given {@code address} or {@code instruction} or
	 *                              {@code instructions} is null.
	 * @since 0.2.0 ~2021.05.28
	 */
	public FpedAddrExecInstrXinstr(@NotNull String address, @NotNull Instruction instruction, @Nullable Instruction @NotNull ... instructions) {
		Objects.requireNonNull(address, "address");
		Objects.requireNonNull(instruction, "instruction");
		Objects.requireNonNull(instructions, "instructions");
		this.tree = null;
		this.address = address;
		this.instruction = instruction;
		this.instructions = Arrays
				.stream(instructions)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}

	/**
	 * Construct a new instruction that executes the given {@code instructions} foreach
	 * item resultant from executing the given {@code instruction}.
	 * <br>
	 * Null instructions in the list will be ignored.
	 *
	 * @param address      the address where to allocate the item to on each iteration.
	 * @param instruction  the instruction to be executed to get the items.
	 * @param instructions the instructions to iterate.
	 * @throws NullPointerException if the given {@code address} or {@code instruction} or
	 *                              {@code instructions} is null.
	 * @since 0.2.0 ~2021.05.28
	 */
	public FpedAddrExecInstrXinstr(@NotNull String address, @NotNull Instruction instruction, @NotNull List<Instruction> instructions) {
		Objects.requireNonNull(address, "address");
		Objects.requireNonNull(instruction, "instruction");
		Objects.requireNonNull(instructions, "instructions");
		this.tree = null;
		this.address = address;
		this.instruction = instruction;
		this.instructions = new ArrayList<>();
		for (Instruction i : instructions)
			if (i != null)
				this.instructions.add(i);
	}

	/**
	 * Construct a new instruction that executes the given {@code instructions} foreach
	 * item resultant from executing the given {@code instruction}.
	 * <br>
	 * Null instructions in the array will be ignored.
	 *
	 * @param tree         the tree from where this instruction was declared.
	 * @param address      the address where to allocate the item to on each iteration.
	 * @param instruction  the instruction to be executed to get the items.
	 * @param instructions the instructions to iterate.
	 * @throws NullPointerException if the given {@code tree} or {@code address} or {@code
	 *                              instruction} or {@code instructions} is null.
	 * @since 0.2.0 ~2021.05.28
	 */
	public FpedAddrExecInstrXinstr(@NotNull Tree tree, @NotNull String address, @NotNull Instruction instruction, @Nullable Instruction @NotNull ... instructions) {
		Objects.requireNonNull(tree, "tree");
		Objects.requireNonNull(address, "address");
		Objects.requireNonNull(instruction, "instruction");
		Objects.requireNonNull(instructions, "instructions");
		this.tree = tree;
		this.address = address;
		this.instruction = instruction;
		this.instructions = Arrays
				.stream(instructions)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}

	/**
	 * Construct a new instruction that executes the given {@code instructions} foreach
	 * item resultant from executing the given {@code instruction}.
	 * <br>
	 * Null instructions in the list will be ignored.
	 *
	 * @param tree         the tree from where this instruction was declared.
	 * @param address      the address where to allocate the item to on each iteration.
	 * @param instruction  the instruction to be executed to get the items.
	 * @param instructions the instructions to iterate.
	 * @throws NullPointerException if the given {@code tree} or {@code address} or {@code
	 *                              instruction} or {@code instructions} is null.
	 * @since 0.2.0 ~2021.05.28
	 */
	public FpedAddrExecInstrXinstr(@NotNull Tree tree, @NotNull String address, @NotNull Instruction instruction, @NotNull List<Instruction> instructions) {
		Objects.requireNonNull(tree, "tree");
		Objects.requireNonNull(address, "address");
		Objects.requireNonNull(instruction, "instruction");
		Objects.requireNonNull(instructions, "instructions");
		this.tree = tree;
		this.address = address;
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

		//EXEC( INSTR )
		memory.pushFrame(new Frame(this.instruction));
		this.instruction.exec(environment, memory);
		Value value = Memories.joinPop(memory);
		memory.popFrame();

		//FPED( ADDR , EXEC( INSTR ) , XINSTR )
		String text0 = value.evaluate(memory);
		JSONArray object = JSONUtil.asJSONArray(text0);

		for (Object item : object) {
			String text1 = String.valueOf(item);

			memory.pushFrame(new Frame(this));
			memory.getFrame().set(this.address, m -> text1);

			for (Instruction instruction : this.instructions) {
				memory.pushFrame(new Frame(instruction));
				instruction.exec(environment, memory);
				memory.dumpFrame();
			}

			memory.popFrame();
		}
	}

	@Nullable
	@Override
	public Tree getTree() {
		return this.tree;
	}
}
