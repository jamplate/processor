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
package org.jamplate.glucose.instruction.memory.stack;

import org.jamplate.memory.Memory;
import org.jamplate.memory.Value;
import org.jamplate.model.Environment;
import org.jamplate.model.Instruction;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * An instruction that swaps the top four values in the stack.
 * <br><br>
 * Memory Visualization:
 * <pre>
 *     [..., param:value*, middle:value*, middle2:value* other:value*]
 *     [..., other:value*, middle2:value*, middle:value*, result:value*]
 * </pre>
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.07.10
 */
public class ISwap4 implements Instruction {
	/**
	 * An instance of this instruction.
	 *
	 * @since 0.3.0 ~2021.07.10
	 */
	@NotNull
	public static final ISwap4 INSTANCE = new ISwap4();

	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 8839416675018494432L;

	/**
	 * A reference of this instruction in the source code.
	 *
	 * @since 0.3.0 ~2021.07.10
	 */
	@Nullable
	protected final Tree tree;

	/**
	 * Construct a new instruction that swaps the top four values in the stack.
	 *
	 * @since 0.3.0 ~2021.07.10
	 */
	public ISwap4() {
		this.tree = null;
	}

	/**
	 * Construct a new instruction that swaps the top four values in the stack.
	 *
	 * @param tree a reference for the constructed instruction in the source code.
	 * @throws NullPointerException if the given {@code tree} is null.
	 * @since 0.3.0 ~2021.07.10
	 */
	public ISwap4(@NotNull Tree tree) {
		Objects.requireNonNull(tree, "tree");
		this.tree = tree;
	}

	@Override
	public void exec(@NotNull Environment environment, @NotNull Memory memory) {
		Objects.requireNonNull(environment, "environment");
		Objects.requireNonNull(memory, "memory");

		Value value0 = memory.pop();
		Value value1 = memory.pop();
		Value value2 = memory.pop();
		Value value3 = memory.pop();

		memory.push(value0);
		memory.push(value1);
		memory.push(value2);
		memory.push(value3);
	}

	@Nullable
	@Override
	public Tree getTree() {
		return this.tree;
	}

	@NotNull
	@Override
	public Instruction optimize(int mode) {
		return mode < 0 ? ISwap4.INSTANCE : new ISwap4(new Tree(this.tree));
	}
}
