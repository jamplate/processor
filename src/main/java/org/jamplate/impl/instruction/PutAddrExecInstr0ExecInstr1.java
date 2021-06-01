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
 * <h3>{@code PUT( ADDR , EXEC( INSTR0 ) , EXEC( INSTR1 ) )}</h3>
 * An instruction that executes two pre-specified instructions and puts the results of
 * executing the second instruction to the results of executing the first instruction in
 * the object located at a pre-specified address in the heap.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.06.01
 */
public class PutAddrExecInstr0ExecInstr1 implements Instruction {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -8173168212774852938L;

	/**
	 * The address to put to.
	 *
	 * @since 0.2.0 ~2021.06.01
	 */
	@NotNull
	protected final String address;
	/**
	 * The instruction of the key to be put.
	 *
	 * @since 0.2.0 ~2021.06.01
	 */
	@NotNull
	protected final Instruction instruction0;
	/**
	 * The instruction of the value to be put.
	 *
	 * @since 0.2.0 ~2021.06.01
	 */
	@NotNull
	protected final Instruction instruction1;
	/**
	 * The tree this instruction was declared at.
	 *
	 * @since 0.2.0 ~2021.06.01
	 */
	@Nullable
	protected final Tree tree;

	/**
	 * Construct a new instruction that puts the value of executing the given {@code
	 * instruction1} to the value of executing the given {@code instruction0} at the given
	 * {@code address} in the heap.
	 *
	 * @param address      the address to put to.
	 * @param instruction0 the instruction of the key to be put.
	 * @param instruction1 the instruction of the value to be put.
	 * @throws NullPointerException if the given {@code address} or {@code instruction0}
	 *                              or {@code instruction1} is null.
	 * @since 0.2.0 ~2021.06.01
	 */
	public PutAddrExecInstr0ExecInstr1(
			@NotNull String address,
			@NotNull Instruction instruction0,
			@NotNull Instruction instruction1
	) {
		Objects.requireNonNull(address, "address");
		Objects.requireNonNull(instruction0, "instruction0");
		Objects.requireNonNull(instruction1, "instruction1");
		this.tree = null;
		this.address = address;
		this.instruction0 = instruction0;
		this.instruction1 = instruction1;
	}

	/**
	 * Construct a new instruction that puts the value of executing the given {@code
	 * instruction1} to the value of executing the given {@code instruction0} at the given
	 * {@code address} in the heap.
	 *
	 * @param tree         the tree from where this instruction was declared.
	 * @param address      the address to put to.
	 * @param instruction0 the instruction of the key to be put.
	 * @param instruction1 the instruction of the value to be put.
	 * @throws NullPointerException if the given {@code tree} or {@code address} or {@code
	 *                              instruction0} or {@code instruction1} is null.
	 * @since 0.2.0 ~2021.06.01
	 */
	public PutAddrExecInstr0ExecInstr1(
			@NotNull Tree tree,
			@NotNull String address,
			@NotNull Instruction instruction0,
			@NotNull Instruction instruction1
	) {
		Objects.requireNonNull(tree, "tree");
		Objects.requireNonNull(address, "address");
		Objects.requireNonNull(instruction0, "instruction0");
		Objects.requireNonNull(instruction1, "instruction1");
		this.tree = tree;
		this.address = address;
		this.instruction0 = instruction0;
		this.instruction1 = instruction1;
	}

	@Override
	public void exec(@NotNull Environment environment, @NotNull Memory memory) {
		Objects.requireNonNull(environment, "environment");
		Objects.requireNonNull(memory, "memory");

		//EXEC( INSTR0 )
		memory.pushFrame(new Frame(this.instruction0));
		this.instruction0.exec(environment, memory);
		Value value0 = Memories.joinPop(memory);
		memory.popFrame();

		//EXEC( INSTR1 )
		memory.pushFrame(new Frame(this.instruction1));
		this.instruction1.exec(environment, memory);
		Value value1 = Memories.joinPop(memory);
		memory.popFrame();

		//PUT( ADDR , EXEC( INSTR0 ) , EXEC( INSTR1 ) )
		String text0 = value0.evaluate(memory);
		String text1 = value1.evaluate(memory);

		String address = this.address;

		memory.compute(address, value2 -> {
			String text2 = value2.evaluate(memory);

			Object object = JSONUtil.put(text2, text0, text1);

			String text3 = object.toString();
			return m -> text3;
		});
	}

	@Nullable
	@Override
	public Tree getTree() {
		return this.tree;
	}
}
