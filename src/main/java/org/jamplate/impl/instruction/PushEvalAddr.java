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

import org.jamplate.model.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * An instruction that takes a specified value from the heap and push it to the stack.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.24
 */
public class PushEvalAddr implements Instruction {
	//PUSH( EVAL( ADDR ) )

	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -3380257902335591663L;

	/**
	 * The name of the reference to be pushed.
	 *
	 * @since 0.2.0 ~2021.05.24
	 */
	@NotNull
	protected final String address;
	/**
	 * The tree of this instruction.
	 *
	 * @since 0.2.0 ~2021.05.24
	 */
	@Nullable
	protected final Tree tree;

	/**
	 * Construct a new reference instruction that takes the value at the heap with the
	 * given {@code address} and pushes it to the stack.
	 *
	 * @param address the name of the value in the heap.
	 * @throws NullPointerException if the given {@code address} is null.
	 * @since 0.2.0 ~2021.05.24
	 */
	public PushEvalAddr(@NotNull String address) {
		Objects.requireNonNull(address, "address");
		this.tree = null;
		this.address = address;
	}

	/**
	 * Construct a new reference instruction that takes the value at the heap with the
	 * given {@code address} and pushes it to the stack.
	 *
	 * @param tree    the tree of the constructed instruction.
	 * @param address the name of the value in the heap.
	 * @throws NullPointerException if the given {@code tree} or {@code address} is null.
	 * @since 0.2.0 ~2021.05.24
	 */
	public PushEvalAddr(@NotNull Tree tree, @NotNull String address) {
		Objects.requireNonNull(tree, "tree");
		Objects.requireNonNull(address, "address");
		this.tree = tree;
		this.address = address;
	}

	@Override
	public void exec(@NotNull Environment environment, @NotNull Memory memory) {
		Objects.requireNonNull(environment, "environment");
		Objects.requireNonNull(memory, "memory");
		Value value = memory.get(this.address);

		if (value == Value.NULL)
			value = m -> this.address;

		memory.push(value);
	}

	@Nullable
	@Override
	public Tree getTree() {
		return this.tree;
	}
}
