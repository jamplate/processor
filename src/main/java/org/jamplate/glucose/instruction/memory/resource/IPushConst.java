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
package org.jamplate.glucose.instruction.memory.resource;

import org.jamplate.memory.Memory;
import org.jamplate.memory.Value;
import org.jamplate.model.Environment;
import org.jamplate.model.Instruction;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * An instruction that pushes a pre-specified value to the stack.
 * <br><br>
 * Memory Visualization:
 * <pre>
 *     [...]
 *     [..., result:value*]
 * </pre>
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.11
 */
public class IPushConst implements Instruction {
	/**
	 * A constant push-const instruction that pushes {@link Value#NULL}.
	 *
	 * @since 0.3.0 ~2021.06.24
	 */
	@NotNull
	public static final IPushConst NULL = new IPushConst(Value.NULL);

	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -6262887458863783425L;

	/**
	 * A reference of this instruction in the source code.
	 *
	 * @since 0.3.0 ~2021.06.11
	 */
	@Nullable
	protected final Tree tree;
	/**
	 * The value to be pushed to the stack.
	 *
	 * @since 0.3.0 ~2021.06.12
	 */
	@NotNull
	protected final Value value;

	/**
	 * Construct a new instruction that pushes the given {@code value} to the stack.
	 *
	 * @param value the value to be pushed.
	 * @throws NullPointerException if the given {@code value} is null.
	 * @since 0.3.0 ~2021.06.12
	 */
	public IPushConst(@NotNull Value<?> value) {
		Objects.requireNonNull(value, "value");
		this.tree = null;
		this.value = value;
	}

	/**
	 * Construct a new instruction that pushes the given {@code value} to the stack.
	 *
	 * @param tree  a reference for the constructed instruction in the source code.
	 * @param value the value to be pushed.
	 * @throws NullPointerException if the given {@code tree} or {@code value} is null.
	 * @since 0.3.0 ~2021.06.12
	 */
	public IPushConst(@NotNull Tree tree, @NotNull Value<?> value) {
		Objects.requireNonNull(tree, "tree");
		Objects.requireNonNull(value, "value");
		this.tree = tree;
		this.value = value;
	}

	@Override
	public void exec(@NotNull Environment environment, @NotNull Memory memory) {
		Objects.requireNonNull(environment, "environment");
		Objects.requireNonNull(memory, "memory");

		memory.push(this.value);
	}

	@Nullable
	@Override
	public Tree getTree() {
		return this.tree;
	}

	@NotNull
	@Override
	public Instruction optimize(int mode) {
		return mode < 0 ?
			   new IPushConst(
					   this.value
			   ) :
			   new IPushConst(
					   new Tree(this.tree),
					   this.value
			   );
	}
}
