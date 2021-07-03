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
package org.jamplate.instruction.memory.heap;

import org.jamplate.memory.Memory;
import org.jamplate.memory.Value;
import org.jamplate.model.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * An instruction that pops the top two values in the stack and allocates the first popped
 * value to current frame's heap at address resultant from evaluating the second popped
 * value.
 * <br><br>
 * Memory Visualization:
 * <pre>
 *     [..., address:value, param:value*]
 *     [...]
 * </pre>
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.15
 */
public class Set implements Instruction {
	/**
	 * An instance of this instruction.
	 *
	 * @since 0.3.0 ~2021.06.15
	 */
	@NotNull
	public static final Set INSTANCE = new Set();

	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -4102904984181017768L;

	/**
	 * A reference of this instruction in the source code.
	 *
	 * @since 0.3.0 ~2021.06.15
	 */
	@Nullable
	protected final Tree tree;

	/**
	 * Construct a new instruction that pops the last two values in the stack and
	 * allocates the first popped value to the current frame's heap at the result of
	 * evaluating the second popped value.
	 *
	 * @since 0.3.0 ~2021.06.15
	 */
	public Set() {
		this.tree = null;
	}

	/**
	 * Construct a new instruction that pops the last two values in the stack and
	 * allocates the first popped value to the current frame's heap at the result of
	 * evaluating the second popped value.
	 *
	 * @param tree a reference for the constructed instruction in the source code.
	 * @throws NullPointerException if the given {@code tree} is null.
	 * @since 0.3.0 ~2021.06.15
	 */
	public Set(@NotNull Tree tree) {
		Objects.requireNonNull(tree, "tree");
		this.tree = tree;
	}

	@Override
	public void exec(@NotNull Environment environment, @NotNull Memory memory) {
		Objects.requireNonNull(environment, "environment");
		Objects.requireNonNull(memory, "memory");

		//value
		Value value0 = memory.pop();
		//address
		Value value1 = memory.pop();
		String text1 = value1.evaluate(memory);

		memory.getFrame().set(text1, value0);
	}

	@Nullable
	@Override
	public Tree getTree() {
		return this.tree;
	}

	@NotNull
	@Override
	public Instruction optimize(int mode) {
		return mode < 0 ? Set.INSTANCE : new Set(new Tree(this.tree));
	}
}
