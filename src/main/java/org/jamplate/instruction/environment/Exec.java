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
package org.jamplate.instruction.environment;

import org.jamplate.model.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * An instruction that pops the top value at the stack and executes the compilation with
 * the name equal to the result of evaluating the popped value.
 * <br>
 * If no compilation was found with the targeted name, an {@link ExecutionException} will
 * be thrown.
 * <br>
 * If the targeted compilation does not have a compiled instruction, an {@link
 * ExecutionException} will be thrown.
 * <br><br>
 * Memory Visualization:
 * <pre>
 *     [..., target:text]
 *     [...]
 * </pre>
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.24
 */
public class Exec implements Instruction {
	/**
	 * An instance of this instruction.
	 *
	 * @since 0.3.0 ~2021.06.24
	 */
	@NotNull
	public static final Exec INSTANCE = new Exec();

	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -5125697387092743891L;

	/**
	 * A reference of this instruction in the source code.
	 *
	 * @since 0.3.0 ~2021.06.24
	 */
	@Nullable
	protected final Tree tree;

	/**
	 * Construct a new instruction that pops the top value at the stack and executes the
	 * compilation with the name equal to the result of evaluating the popped value.
	 *
	 * @since 0.3.0 ~2021.06.24
	 */
	public Exec() {
		this.tree = null;
	}

	/**
	 * Construct a new instruction that pops the top value at the stack and executes the
	 * compilation with the name equal to the result of evaluating the popped value.
	 *
	 * @param tree a reference for the constructed instruction in the source code.
	 * @throws NullPointerException if the given {@code tree} is null.
	 * @since 0.3.0 ~2021.06.24
	 */
	public Exec(@NotNull Tree tree) {
		Objects.requireNonNull(tree, "tree");
		this.tree = tree;
	}

	@Override
	public void exec(@NotNull Environment environment, @NotNull Memory memory) {
		Objects.requireNonNull(environment, "environment");
		Objects.requireNonNull(memory, "memory");

		Value value0 = memory.pop();
		String text0 = value0.evaluate(memory);

		Compilation compilation = environment.getCompilation(text0);

		if (compilation == null)
			throw new ExecutionException(
					"Cannot find the compilation with the name: " + text0,
					this.tree
			);

		Instruction instruction = compilation.getInstruction();

		if (instruction == null)
			throw new ExecutionException(
					"Cannot find executable instruction in the compilation: " + text0,
					compilation.getRootTree()
			);

		instruction.exec(environment, memory);
	}

	@Nullable
	@Override
	public Tree getTree() {
		return this.tree;
	}
}
