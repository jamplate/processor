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

import org.jamplate.impl.Address;
import org.jamplate.impl.util.JSONUtil;
import org.jamplate.internal.util.Trees;
import org.jamplate.model.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.Objects;

/**
 * <h3>{@code REPRNT( CONST )}</h3>
 * An instruction that prints a pre-specified constant to the console.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.23
 */
public class ReprntConst implements Instruction {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 4950811636884162730L;

	/**
	 * The constant to be printed.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	@NotNull
	protected final String constant;
	/**
	 * The tree from where this instruction was declared.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	@Nullable
	protected final Tree tree;

	/**
	 * Construct a new instruction that prints the content of the given {@code tree} to
	 * the console when executed.
	 *
	 * @param tree the tree of the constructed instruction.
	 * @throws NullPointerException if the given {@code tree} is null.
	 * @since 0.2.0 ~2021.05.23
	 */
	public ReprntConst(@NotNull Tree tree) {
		Objects.requireNonNull(tree, "tree");
		this.tree = tree;
		this.constant = Trees.read(tree).toString();
	}

	/**
	 * Construct a new instruction that prints the given {@code constant} to the console
	 * when executed.
	 *
	 * @param constant the constant to be printed.
	 * @throws NullPointerException if the given {@code constant} is null.
	 * @since 0.2.0 ~2021.05.23
	 */
	public ReprntConst(@NotNull String constant) {
		Objects.requireNonNull(constant, "constant");
		this.tree = null;
		this.constant = constant;
	}

	/**
	 * Construct a new instruction that prints the given {@code constant} to the console
	 * when executed.
	 *
	 * @param tree     the tree of the constructed instruction.
	 * @param constant the constant to be printed.
	 * @throws NullPointerException if the given {@code tree} or {@code constant} is
	 *                              null.
	 * @since 0.2.0 ~2021.05.23
	 */
	public ReprntConst(@NotNull Tree tree, @NotNull String constant) {
		Objects.requireNonNull(tree, "tree");
		Objects.requireNonNull(constant, "constant");
		this.tree = tree;
		this.constant = constant;
	}

	@Override
	public void exec(@NotNull Environment environment, @NotNull Memory memory) {
		Objects.requireNonNull(environment, "environment");
		Objects.requireNonNull(memory, "memory");

		//REPRNT( CONST )
		Value value = memory.get(Address.DEFINE);
		String text0 = value.evaluate(memory);
		JSONObject object = JSONUtil.asJSONObject(text0);

		String text1 = this.constant;

		for (String replace : object.keySet()) {
			String replacement = object.getString(replace);

			text1 = text1.replace(replace, replacement);
		}

		memory.print(text1);
	}

	@Nullable
	@Override
	public Tree getTree() {
		return this.tree;
	}
}
