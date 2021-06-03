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

import java.util.Objects;

/**
 * <h3>{@code PUSH( LOR( EXEC( INSTR0 ) , EXEC( INSTR1 ) ) ) )}</h3>
 * Pushes the results of "or"-ing the results of executing two pre-specified
 * instructions.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.06.03
 */
public class PushLorExecInstr0ExecInstr1 implements Instruction {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -5840219111792127123L;

	/**
	 * The first instruction in the problem.
	 *
	 * @since 0.2.0 ~2021.06.03
	 */
	@NotNull
	protected final Instruction instruction0;
	/**
	 * The second instruction in the problem.
	 *
	 * @since 0.2.0 ~2021.06.03
	 */
	@NotNull
	protected final Instruction instruction1;
	/**
	 * The tree this instruction was declared at.
	 *
	 * @since 0.2.0 ~2021.06.03
	 */
	@Nullable
	protected final Tree tree;

	/**
	 * Construct a new instruction that pushes the results of "or"-ing the result of
	 * executing the given {@code instruction0} with the results of executing the given
	 * {@code instruction1}.
	 *
	 * @param instruction0 the first instruction.
	 * @param instruction1 the second instruction.
	 * @throws NullPointerException if the given {@code instruction0} or {@code
	 *                              instruction1} is null.
	 * @since 0.2.0 ~2021.06.03
	 */
	public PushLorExecInstr0ExecInstr1(@NotNull Instruction instruction0, @NotNull Instruction instruction1) {
		Objects.requireNonNull(instruction0, "instruction0");
		Objects.requireNonNull(instruction1, "instruction1");
		this.tree = null;
		this.instruction0 = instruction0;
		this.instruction1 = instruction1;
	}

	/**
	 * Construct a new instruction that pushes the results of "or"-ing the result of
	 * executing the given {@code instruction0} with the results of executing the given
	 * {@code instruction1}.
	 *
	 * @param tree         the tree from where this instruction was declared.
	 * @param instruction0 the first instruction.
	 * @param instruction1 the second instruction.
	 * @throws NullPointerException if the given {@code tree} or {@code instruction0} or
	 *                              {@code instruction1} is null.
	 * @since 0.2.0 ~2021.06.03
	 */
	public PushLorExecInstr0ExecInstr1(@NotNull Tree tree, @NotNull Instruction instruction0, @NotNull Instruction instruction1) {
		Objects.requireNonNull(tree, "tree");
		Objects.requireNonNull(instruction0, "instruction0");
		Objects.requireNonNull(instruction1, "instruction1");
		this.tree = tree;
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

		//LOR( EXEC( INSTR0 ) , EXEC( INSTR1 ) )
		String text0 = value0.evaluate(memory);
		String text1 = value1.evaluate(memory);
		String text3 = Boolean.toString(
				Values.toBoolean(text0) ||
				Values.toBoolean(text1)
		);

		//PUSH( LOR( EXEC( INSTR0 ) , EXEC( INSTR1 ) ) )
		memory.push(m -> text3);
	}

	@Nullable
	@Override
	public Tree getTree() {
		return this.tree;
	}
}
