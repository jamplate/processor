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
import org.json.JSONObject;

import java.util.Objects;

/**
 * An instruction that executes another instruction and spread the results to the heap.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.31
 */
public class SpreadExecInstr implements Instruction {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 2525377632048215789L;

	/**
	 * The instruction to be executed.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	protected final Instruction instruction;
	/**
	 * The tree this instruction was declared at.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@Nullable
	protected final Tree tree;

	/**
	 * Construct a new instruction that execute the given {@code instruction} and spread
	 * the results to the heap.
	 *
	 * @param instruction the instruction to be executed.
	 * @throws NullPointerException if the given {@code instruction} is null.
	 * @since 0.2.0 ~2021.05.31
	 */
	public SpreadExecInstr(@NotNull Instruction instruction) {
		Objects.requireNonNull(instruction, "instruction");
		this.tree = null;
		this.instruction = instruction;
	}

	/**
	 * Construct a new instruction that execute the given {@code instruction} and spread
	 * the results to the heap.
	 *
	 * @param tree        the tree from where this instruction was declared.
	 * @param instruction the instruction to be executed.
	 * @throws NullPointerException if the given {@code tree} or {@code instruction} is
	 *                              null.
	 * @since 0.2.0 ~2021.05.31
	 */
	public SpreadExecInstr(@NotNull Tree tree, @NotNull Instruction instruction) {
		Objects.requireNonNull(instruction, "instruction");
		this.tree = null;
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

		//SPREAD( EXEC( INSTR ) )
		String text0 = value.evaluate(memory);
		JSONObject object = JSONUtil.asJSONObject(text0);

		for (String address : object.keySet()) {
			String text1 = object.optString(address, "");

			memory.set(address, m -> text1);
		}
	}

	@Nullable
	@Override
	public Tree getTree() {
		return this.tree;
	}
}
