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
package org.jamplate.impl.util.model;

import org.jamplate.model.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * An instruction that executes one of two predefined instructions based on a condition.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.24
 */
public class IfInstruction implements Instruction {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -9034207643511548467L;

	/**
	 * The instruction of the condition.
	 *
	 * @since 0.2.0 ~2021.05.24
	 */
	@NotNull
	protected final Instruction condition;
	/**
	 * The instruction to be executed when the condition fails.
	 *
	 * @since 0.2.0 ~2021.05.24
	 */
	@NotNull
	protected final Instruction conditionFalse;
	/**
	 * The instruction to be executed when the condition passes.
	 *
	 * @since 0.2.0 ~2021.05.24
	 */
	@NotNull
	protected final Instruction conditionTrue;
	/**
	 * The tree this instruction was declared at.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	@Nullable
	protected final Tree tree;

	/**
	 * Construct a new if instruction that checks the given {@code condition} and executes
	 * the given {@code conditionTrue} when the condition succeed.
	 *
	 * @param condition     the condition.
	 * @param conditionTrue the instruction to be executed when the condition succeed.
	 * @throws NullPointerException if the given {@code condition} or {@code
	 *                              conditionTrue} is null.
	 * @since 0.2.0 ~2021.05.24
	 */
	public IfInstruction(@NotNull Instruction condition, @NotNull Instruction conditionTrue) {
		Objects.requireNonNull(condition, "condition");
		Objects.requireNonNull(conditionTrue, "conditionTrue");
		this.tree = null;
		this.condition = condition;
		this.conditionTrue = conditionTrue;
		this.conditionFalse = (e, m) -> {
		};
	}

	/**
	 * Construct a new if instruction that checks the given {@code condition} and executes
	 * the given {@code conditionTrue} when the condition succeed and execute the given
	 * {@code conditionFalse} when the condition fails.
	 *
	 * @param condition      the condition.
	 * @param conditionTrue  the instruction to be executed when the condition succeed.
	 * @param conditionFalse the instruction to be executed when the condition fails.
	 * @throws NullPointerException if the given {@code condition} or {@code
	 *                              conditionTrue} or {@code conditionFalse} is null.
	 * @since 0.2.0 ~2021.05.24
	 */
	public IfInstruction(@NotNull Instruction condition, @NotNull Instruction conditionTrue, @NotNull Instruction conditionFalse) {
		Objects.requireNonNull(condition, "condition");
		Objects.requireNonNull(conditionTrue, "conditionTrue");
		Objects.requireNonNull(conditionFalse, "conditionFalse");
		this.tree = null;
		this.condition = condition;
		this.conditionTrue = conditionTrue;
		this.conditionFalse = conditionFalse;
	}

	/**
	 * Construct a new if instruction that checks the given {@code condition} and executes
	 * the given {@code conditionTrue} when the condition succeed.
	 *
	 * @param tree          the tree from where this instruction was declared.
	 * @param condition     the condition.
	 * @param conditionTrue the instruction to be executed when the condition succeed.
	 * @throws NullPointerException if the given {@code condition} or {@code
	 *                              conditionTrue} is null.
	 * @since 0.2.0 ~2021.05.24
	 */
	public IfInstruction(@NotNull Tree tree, @NotNull Instruction condition, @NotNull Instruction conditionTrue) {
		Objects.requireNonNull(tree, "tree");
		Objects.requireNonNull(condition, "condition");
		Objects.requireNonNull(conditionTrue, "conditionTrue");
		this.tree = tree;
		this.condition = condition;
		this.conditionTrue = conditionTrue;
		this.conditionFalse = Instruction.create(tree, (e, m) -> {
		});
	}

	/**
	 * Construct a new if instruction that checks the given {@code condition} and executes
	 * the given {@code conditionTrue} when the condition succeed and execute the given
	 * {@code conditionFalse} when the condition fails.
	 *
	 * @param tree           the tree from where this instruction was declared.
	 * @param condition      the condition.
	 * @param conditionTrue  the instruction to be executed when the condition succeed.
	 * @param conditionFalse the instruction to be executed when the condition fails.
	 * @throws NullPointerException if the given {@code condition} or {@code
	 *                              conditionTrue} or {@code conditionFalse} is null.
	 * @since 0.2.0 ~2021.05.24
	 */
	public IfInstruction(@NotNull Tree tree, @NotNull Instruction condition, @NotNull Instruction conditionTrue, @NotNull Instruction conditionFalse) {
		Objects.requireNonNull(tree, "tree");
		Objects.requireNonNull(condition, "condition");
		Objects.requireNonNull(conditionTrue, "conditionTrue");
		Objects.requireNonNull(conditionFalse, "conditionFalse");
		this.tree = tree;
		this.condition = condition;
		this.conditionTrue = conditionTrue;
		this.conditionFalse = conditionFalse;
	}

	@Override
	public void exec(@NotNull Environment environment, @NotNull Memory memory) {
		Objects.requireNonNull(environment, "environment");
		Objects.requireNonNull(memory, "memory");
		memory.pushFrame(new Memory.Frame(this.condition));
		this.condition.exec(environment, memory);
		Value value = memory.pop();
		memory.popFrame();

		String condition = value.evaluate(memory);

		switch (condition) {
			case "":
			case "0":
			case "null":
			case "false":
				memory.pushFrame(new Memory.Frame(this.conditionFalse));
				this.conditionFalse.exec(environment, memory);
				memory.popFrame();
				break;
			default:
				memory.pushFrame(new Memory.Frame(this.conditionTrue));
				this.conditionTrue.exec(environment, memory);
				memory.popFrame();
				break;
		}
	}

	@Nullable
	@Override
	public Tree getTree() {
		return this.tree;
	}
}
