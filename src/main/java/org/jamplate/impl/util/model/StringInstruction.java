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

import org.jamplate.model.Environment;
import org.jamplate.model.Instruction;
import org.jamplate.model.Memory;
import org.jamplate.model.Tree;
import org.jamplate.util.Trees;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * An instruction that pushes a predefined string to the stack when it gets executed.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.23
 */
public class StringInstruction implements Instruction {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -764009371662392281L;

	/**
	 * The tree of this instruction.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	@Nullable
	protected final Tree tree;
	/**
	 * The value to be pushed.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	@NotNull
	protected final String value;

	/**
	 * Construct a new string instruction that pushes the content of the given {@code
	 * tree} to the stack when executed.
	 *
	 * @param tree the tree of the constructed instruction.
	 * @throws NullPointerException if the given {@code tree} is null.
	 * @since 0.2.0 ~2021.05.23
	 */
	public StringInstruction(@NotNull Tree tree) {
		Objects.requireNonNull(tree, "tree");
		this.tree = tree;
		//noinspection DynamicRegexReplaceableByCompiledPattern
		this.value = Trees
				.read(tree)
				.toString()
				.replaceAll("\\\\(.)", "$1");
	}

	/**
	 * Construct a new string instruction that pushes the given {@code value} to the stack
	 * when executed.
	 *
	 * @param value the value to be pushed to the stack by the constructed instruction
	 *              when it gets executed.
	 * @throws NullPointerException if the given {@code value} is null.
	 * @since 0.2.0 ~2021.05.23
	 */
	public StringInstruction(@NotNull String value) {
		Objects.requireNonNull(value, "value");
		this.tree = null;
		this.value = value;
	}

	/**
	 * Construct a new string instruction that pushes the given {@code value} to the stack
	 * when executed.
	 *
	 * @param tree  the tree of the constructed instruction.
	 * @param value the value to be pushed to the stack by the constructed instruction
	 *              when it gets executed.
	 * @throws NullPointerException if the given {@code tree} or {@code value} is null.
	 * @since 0.2.0 ~2021.05.23
	 */
	public StringInstruction(@NotNull Tree tree, @NotNull String value) {
		Objects.requireNonNull(tree, "tree");
		Objects.requireNonNull(value, "value");
		this.tree = tree;
		this.value = value;
	}

	@Override
	public void exec(@NotNull Environment environment, @NotNull Memory memory) {
		Objects.requireNonNull(environment, "environment");
		Objects.requireNonNull(memory, "memory");
		memory.push(m -> this.value);
	}

	@Nullable
	@Override
	public Tree getTree() {
		return this.tree;
	}
}
