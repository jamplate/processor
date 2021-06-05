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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * <h3>{@code IDLE}</h3>
 * An instruction that does nothing. Other instructions can completely ignore executing
 * this instruction.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.06.05
 */
public class Idle implements Instruction {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -1121111314421762482L;

	/**
	 * The tree this instruction was declared at.
	 *
	 * @since 0.2.0 ~2021.06.05
	 */
	@Nullable
	protected final Tree tree;

	/**
	 * Construct a new instruction that does nothing.
	 *
	 * @since 0.2.0 ~2021.06.05
	 */
	public Idle() {
		this.tree = null;
	}

	/**
	 * Construct a new instruction that does nothing.
	 *
	 * @param tree the tree from where this instruction was declared.
	 * @throws NullPointerException if the given {@code tree} is null.
	 * @since 0.2.0 ~2021.06.05
	 */
	public Idle(@NotNull Tree tree) {
		Objects.requireNonNull(tree, "tree");
		this.tree = tree;
	}

	@Override
	public void exec(@NotNull Environment environment, @NotNull Memory memory) {
	}
}
