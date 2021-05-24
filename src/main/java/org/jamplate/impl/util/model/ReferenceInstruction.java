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
 * An instruction that takes a specified value from the heap and push it to the stack.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.24
 */
public class ReferenceInstruction implements Instruction {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -3380257902335591663L;

	/**
	 * The name of the reference to be pushed.
	 *
	 * @since 0.2.0 ~2021.05.24
	 */
	@NotNull
	protected final String name;
	/**
	 * The tree of this instruction.
	 *
	 * @since 0.2.0 ~2021.05.24
	 */
	@Nullable
	protected final Tree tree;

	/**
	 * Construct a new reference instruction that takes the value at the heap with the
	 * given {@code name} and pushes it to the stack.
	 *
	 * @param name the name of the value in the heap.
	 * @throws NullPointerException if the given {@code name} is null.
	 * @since 0.2.0 ~2021.05.24
	 */
	public ReferenceInstruction(@NotNull String name) {
		Objects.requireNonNull(name, "name");
		this.tree = null;
		this.name = name;
	}

	/**
	 * Construct a new reference instruction that takes the value at the heap with the
	 * given {@code name} and pushes it to the stack.
	 *
	 * @param tree the tree of the constructed instruction.
	 * @param name the name of the value in the heap.
	 * @throws NullPointerException if the given {@code tree} or {@code name} is null.
	 * @since 0.2.0 ~2021.05.24
	 */
	public ReferenceInstruction(@NotNull Tree tree, @NotNull String name) {
		Objects.requireNonNull(tree, "tree");
		Objects.requireNonNull(name, "name");
		this.tree = tree;
		this.name = name;
	}

	@Override
	public void exec(@NotNull Environment environment, @NotNull Memory memory) {
		Objects.requireNonNull(environment, "environment");
		Objects.requireNonNull(memory, "memory");
		Value value = memory.get(this.name);

		if (value == Value.NULL)
			value = m -> this.name;

		memory.push(value);
	}

	@Nullable
	@Override
	public Tree getTree() {
		return this.tree;
	}
}
