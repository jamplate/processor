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

import org.jamplate.impl.Address;
import org.jamplate.impl.util.JSONUtil;
import org.jamplate.impl.util.Memories;
import org.jamplate.model.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.Objects;

/**
 * <h3>{@code REPLLOC( ADDR , EXEC( INSTR ) )}</h3>
 * An instruction that executes a pre-specified instruction and {@link
 * org.jamplate.impl.instruction REPLLOC} the results to a pre-specified address.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.23
 */
@Deprecated
public class RepllocAddrExecInstr implements Instruction {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 7085804102176126751L;

	/**
	 * The address to allocate to.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	@NotNull
	protected final String address;
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
	 * Construct a new instruction that allocates the results from executing the given
	 * {@code instruction} to the given {@code address} as the address in {@link
	 * org.jamplate.impl.instruction REPLLOC} style.
	 *
	 * @param address     the address to allocate to.
	 * @param instruction the instruction be execute.
	 * @throws NullPointerException if the given {@code address} or {@code instruction} is
	 *                              null.
	 * @since 0.2.0 ~2021.05.23
	 */
	public RepllocAddrExecInstr(@NotNull String address, @NotNull Instruction instruction) {
		Objects.requireNonNull(address, "address");
		Objects.requireNonNull(instruction, "instruction");
		this.tree = null;
		this.address = address;
		this.instruction = instruction;
	}

	/**
	 * Construct a new instruction that allocates the results from executing the given
	 * {@code instruction} to the given {@code address} as the address in {@link
	 * org.jamplate.impl.instruction REPLLOC} style.
	 *
	 * @param tree        the tree from where this instruction was declared.
	 * @param address     the address to allocate to.
	 * @param instruction the instruction be execute.
	 * @throws NullPointerException if the given {@code tree} or {@code address} or {@code
	 *                              instruction} is null.
	 * @since 0.2.0 ~2021.05.23
	 */
	public RepllocAddrExecInstr(@NotNull Tree tree, @NotNull String address, @NotNull Instruction instruction) {
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

		//REPLLOC( ADDR , EXEC( INSTR ) )
		String address = this.address;
		String text0 = value0.evaluate(memory);
		memory.set(address, m -> text0);
		memory.compute(Address.DEFINE, value1 -> {
			String text1 = value1.evaluate(memory);

			JSONObject object = JSONUtil.asJSONObject(text1);

			object.put(address, text0);

			String text2 = object.toString();
			return m -> text2;
		});
	}

	@Nullable
	@Override
	public Tree getTree() {
		return this.tree;
	}
}
