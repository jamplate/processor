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
package org.jamplate.instruction.memory.frame;

import org.jamplate.model.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * An instruction that pops the items from the last item and before the last {@link
 * Value#NULL} and then pushes a value that evaluates to the results of joining all the
 * popped values from the last popped to the first popped.
 * <br><br>
 * Memory Visualization:
 * <pre>
 *     [..., ...param:array*]
 *     [..., result:value*]
 * </pre>
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.11
 */
public class JoinFrame implements Instruction {
	/**
	 * An instance of this instruction.
	 *
	 * @since 0.3.0 ~2021.06.11
	 */
	@NotNull
	public static final JoinFrame INSTANCE = new JoinFrame();

	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -4496436508554172838L;

	/**
	 * A reference of this instruction in the source code.
	 *
	 * @since 0.3.0 ~2021.06.11
	 */
	@Nullable
	protected final Tree tree;

	/**
	 * Construct a new instruction that pops the items in the stack from the last item and
	 * before the last null then pushes a value that evaluate to the popped items joint
	 * into one string.
	 *
	 * @since 0.3.0 ~2021.06.12
	 */
	public JoinFrame() {
		this.tree = null;
	}

	/**
	 * Construct a new instruction that pops the items in the stack from the last item and
	 * before the last null then pushes a value that evaluate to the popped items joint
	 * into one string.
	 *
	 * @param tree a reference for the constructed instruction in the source code.
	 * @throws NullPointerException if the given {@code tree} is null.
	 * @since 0.3.0 ~2021.06.12
	 */
	public JoinFrame(@NotNull Tree tree) {
		Objects.requireNonNull(tree, "tree");
		this.tree = tree;
	}

	@Override
	public void exec(@NotNull Environment environment, @NotNull Memory memory) {
		Objects.requireNonNull(environment, "environment");
		Objects.requireNonNull(memory, "memory");

		Value value0 = memory.pop();

		while (true) {
			Value value1 = value0;
			Value value2 = memory.peek();

			if (value2 == Value.NULL) {
				memory.push(value1);
				break;
			}

			memory.pop();

			value0 = m ->
					value2.evaluate(m) +
					value1.evaluate(m);
		}
	}

	@Nullable
	@Override
	public Tree getTree() {
		return this.tree;
	}

	@NotNull
	@Override
	public Instruction optimize(int mode) {
		return mode < 0 ? JoinFrame.INSTANCE : new JoinFrame(new Tree(this.tree));
	}
}
