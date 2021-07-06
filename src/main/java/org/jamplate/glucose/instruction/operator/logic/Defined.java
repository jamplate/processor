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
package org.jamplate.glucose.instruction.operator.logic;

import org.jamplate.memory.Memory;
import org.jamplate.memory.Value;
import org.jamplate.model.Environment;
import org.jamplate.model.Instruction;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static org.jamplate.glucose.internal.util.Values.bool;

/**
 * An instruction that pops the last value and pushes {@code true} if the value is not
 * {@link Value#NULL} and pushes {@code false} if the value is {@link Value#NULL}.
 * <br><br>
 * Memory Visualization:
 * <pre>
 *     [..., param:value*]
 *     [..., result:boolean]
 * </pre>
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.25
 */
public class Defined implements Instruction {
	/**
	 * An instance of this instruction.
	 *
	 * @since 0.3.0 ~2021.06.25
	 */
	@NotNull
	public static final Defined INSTANCE = new Defined();

	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 8399485306399550700L;

	/**
	 * A reference of this instruction in the source code.
	 *
	 * @since 0.3.0 ~2021.06.25
	 */
	@Nullable
	protected final Tree tree;

	/**
	 * Construct a new instruction that pops the last value and pushes {@code true} if the
	 * value is not {@link Value#NULL} and pushes {@code false} if the value is {@link
	 * Value#NULL}.
	 *
	 * @since 0.3.0 ~2021.06.25
	 */
	public Defined() {
		this.tree = null;
	}

	/**
	 * Construct a new instruction that pops the last value and pushes {@code true} if the
	 * value is not {@link Value#NULL} and pushes {@code false} if the value is {@link
	 * Value#NULL}.
	 *
	 * @param tree a reference for the constructed instruction in the source code.
	 * @throws NullPointerException if the given {@code tree} is null.
	 * @since 0.3.0 ~2021.06.25
	 */
	public Defined(@NotNull Tree tree) {
		Objects.requireNonNull(tree, "tree");
		this.tree = tree;
	}

	@Override
	public void exec(@NotNull Environment environment, @NotNull Memory memory) {
		Objects.requireNonNull(environment, "environment");
		Objects.requireNonNull(memory, "memory");

		Value value0 = memory.pop();

		if (value0 == Value.NULL)
			memory.push(bool(false));
		else
			memory.push(bool(true));
	}

	@Nullable
	@Override
	public Tree getTree() {
		return this.tree;
	}

	@NotNull
	@Override
	public Instruction optimize(int mode) {
		return mode < 0 ? Defined.INSTANCE : new Defined(new Tree(this.tree));
	}
}
