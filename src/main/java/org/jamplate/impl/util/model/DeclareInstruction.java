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
 * An instruction that allocates a variable in the heap to a respecified value.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.23
 */
public class DeclareInstruction implements Instruction {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 4592223353913897009L;

	/**
	 * The key parameter.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	@NotNull
	protected final String keyParameter;
	/**
	 * The tree this instruction was declared at.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	@Nullable
	protected final Tree tree;
	/**
	 * The instruction of the value part of the parameter.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	@NotNull
	protected final Instruction valueParameterInstruction;

	/**
	 * Construct a new declare instruction that allocates the results from executing the
	 * given {@code valueParameterInstruction} to the given {@code keyParameter} as the
	 * address.
	 *
	 * @param keyParameter              the address to where to allocate.
	 * @param valueParameterInstruction the instruction to execute to get the value to be
	 *                                  allocated.
	 * @throws NullPointerException if the given {@code keyParameter} or {@code
	 *                              valueParameterInstruction} is null.
	 * @since 0.2.0 ~2021.05.23
	 */
	public DeclareInstruction(@NotNull String keyParameter, @NotNull Instruction valueParameterInstruction) {
		Objects.requireNonNull(keyParameter, "keyParameter");
		Objects.requireNonNull(valueParameterInstruction, "valueParameterInstruction");
		this.tree = null;
		this.keyParameter = keyParameter;
		this.valueParameterInstruction = valueParameterInstruction;
	}

	/**
	 * Construct a new declare instruction that allocates the results from executing the
	 * given {@code valueParameterInstruction} to the given {@code keyParameter} as the
	 * address.
	 *
	 * @param tree                      the tree from where this instruction was
	 *                                  declared.
	 * @param keyParameter              the address to where to allocate.
	 * @param valueParameterInstruction the instruction to execute to get the value to be
	 *                                  allocated.
	 * @throws NullPointerException if the given {@code tree} or {@code keyParameter} or
	 *                              {@code valueParameterInstruction} is null.
	 * @since 0.2.0 ~2021.05.23
	 */
	public DeclareInstruction(@NotNull Tree tree, @NotNull String keyParameter, @NotNull Instruction valueParameterInstruction) {
		Objects.requireNonNull(tree, "tree");
		Objects.requireNonNull(keyParameter, "keyParameter");
		Objects.requireNonNull(valueParameterInstruction, "valueParameterInstruction");
		this.tree = tree;
		this.keyParameter = keyParameter;
		this.valueParameterInstruction = valueParameterInstruction;
	}

	@Override
	public void exec(@NotNull Environment environment, @NotNull Memory memory) {
		Objects.requireNonNull(environment, "environment");
		Objects.requireNonNull(memory, "memory");

		memory.pushFrame(new Memory.Frame(this.valueParameterInstruction));
		this.valueParameterInstruction.exec(environment, memory);
		Value valueParameterValue = memory.pop();
		memory.popFrame();

		String keyParameter = this.keyParameter;
		String valueParameter = valueParameterValue.evaluate(memory);

		memory.set(keyParameter, m -> valueParameter);
	}

	@Nullable
	@Override
	public Tree getTree() {
		return this.tree;
	}
}
