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

import org.jamplate.model.Environment;
import org.jamplate.model.Instruction;
import org.jamplate.model.Memory;
import org.jamplate.model.Tree;
import org.jamplate.impl.util.Trees;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * <h3>{@code PUSH( CONST )}</h3>
 * An instruction that pushes a pre-specified constant to the stack.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.23
 */
public class PushConst implements Instruction {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -764009371662392281L;

	/**
	 * The constant to be pushed.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	@NotNull
	protected final String constant;
	/**
	 * The tree of this instruction.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	@Nullable
	protected final Tree tree;

	/**
	 * Construct a new instruction that pushes the content of the given {@code tree} to
	 * the stack.
	 *
	 * @param tree the tree of the constructed instruction.
	 * @throws NullPointerException if the given {@code tree} is null.
	 * @since 0.2.0 ~2021.05.23
	 */
	public PushConst(@NotNull Tree tree) {
		Objects.requireNonNull(tree, "tree");
		this.tree = tree;
		//noinspection DynamicRegexReplaceableByCompiledPattern
		this.constant = Trees
				.read(tree)
				.toString()
				.replaceAll("\\\\(.)", "$1");
	}

	/**
	 * Construct a new instruction that pushes the given {@code constant} to the stack.
	 *
	 * @param constant the constant to be pushed.
	 * @throws NullPointerException if the given {@code constant} is null.
	 * @since 0.2.0 ~2021.05.23
	 */
	public PushConst(@NotNull String constant) {
		Objects.requireNonNull(constant, "constant");
		this.tree = null;
		this.constant = constant;
	}

	/**
	 * Construct a new instruction that pushes the given {@code constant} to the stack.
	 *
	 * @param tree     the tree of the constructed instruction.
	 * @param constant the constant to be pushed.
	 * @throws NullPointerException if the given {@code tree} or {@code constant} is
	 *                              null.
	 * @since 0.2.0 ~2021.05.23
	 */
	public PushConst(@NotNull Tree tree, @NotNull String constant) {
		Objects.requireNonNull(tree, "tree");
		Objects.requireNonNull(constant, "constant");
		this.tree = tree;
		this.constant = constant;
	}

	@Override
	public void exec(@NotNull Environment environment, @NotNull Memory memory) {
		Objects.requireNonNull(environment, "environment");
		Objects.requireNonNull(memory, "memory");

		//PUSH( CONST )
		memory.push(m -> this.constant);
	}

	@Nullable
	@Override
	public Tree getTree() {
		return this.tree;
	}
}
