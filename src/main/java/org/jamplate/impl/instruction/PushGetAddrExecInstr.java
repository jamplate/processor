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

import java.util.Objects;

/**
 * <h3>{@code PUSH( Get( ADDR , EXEC( INSTR ) ) )}</h3>
 * An instruction that pushes the value in the results of evaluating a pre-specified
 * address at the results of executing a pre-specified instruction.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.06.01
 */
public class PushGetAddrExecInstr implements Instruction {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -658193105393407770L;

	/**
	 * The address to evaluate.
	 *
	 * @since 0.2.0 ~2021.06.01
	 */
	@NotNull
	protected final String address;
	/**
	 * The instruction to be executed.
	 *
	 * @since 0.2.0 ~2021.06.01
	 */
	@NotNull
	protected final Instruction instruction;
	/**
	 * The tree this instruction was declared at.
	 *
	 * @since 0.2.0 ~2021.06.01
	 */
	@Nullable
	protected final Tree tree;

	/**
	 * Construct a new instruction that pushes the value in the given {@code address} at
	 * the results of executing the given {@code instruction} to the heap.
	 *
	 * @param address     the address to be evaluated.
	 * @param instruction the instruction to be executed.
	 * @throws NullPointerException if the given {@code address} or {@code instruction} is
	 *                              null.
	 * @since 0.2.0 ~2021.06.01
	 */
	public PushGetAddrExecInstr(@NotNull String address, @NotNull Instruction instruction) {
		Objects.requireNonNull(address, "address");
		Objects.requireNonNull(instruction, "instruction");
		this.tree = null;
		this.address = address;
		this.instruction = instruction;
	}

	/**
	 * Construct a new instruction that pushes the value in the given {@code address} at
	 * the results of executing the given {@code instruction} to the heap.
	 *
	 * @param tree        the tree from where this instruction was declared.
	 * @param address     the address to be evaluated.
	 * @param instruction the instruction to be executed.
	 * @throws NullPointerException if the given {@code tree} or {@code address} or {@code
	 *                              instruction} is null.
	 * @since 0.2.0 ~2021.06.01
	 */
	public PushGetAddrExecInstr(@NotNull Tree tree, @NotNull String address, @NotNull Instruction instruction) {
		Objects.requireNonNull(tree, "tree");
		Objects.requireNonNull(address, "address");
		Objects.requireNonNull(instruction, "instruction");
		this.tree = tree;
		this.address = address;
		this.instruction = instruction;
	}

	@Override
	public void exec(@NotNull Environment environment, @NotNull Memory memory) {
		Objects.requireNonNull(environment, "environment");
		Objects.requireNonNull(memory, "memory");

		//EXEC( INSTR )
		memory.pushFrame(new Frame(this.instruction));
		this.instruction.exec(environment, memory);
		Value value0 = Memories.joinPop(memory);
		memory.popFrame();

		//GET( ADDR , EXEC( INSTR ) )
		String address = this.address;
		String text0 = value0.evaluate(memory);

		Value value1 = memory.get(address);
		String text1 = value1.evaluate(memory);

		Object object = JSONUtil.get(text1, text0);
		String text2 = object.toString();

		//PUSH( GET( ADDR , EXEC( INSTR ) ) )
		memory.push(m -> text2);
	}

	@Nullable
	@Override
	public Tree getTree() {
		return this.tree;
	}
}
