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
 * <h3>{@code DEF( ADDR )}</h3>
 * An instruction that pushes the constant {@code true} if a pre-specified address is
 * defined and pushes the constant {@code false} otherwise.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.25
 */
public class DefAddr implements Instruction {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -3432648177276191300L;

	/**
	 * The address.
	 *
	 * @since 0.2.0 ~2021.05.25
	 */
	@NotNull
	protected final String address;
	/**
	 * The tree this instruction was declared at.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	@Nullable
	protected final Tree tree;

	/**
	 * Construct a new instruction that pushes {@code true} if the given {@code address}
	 * is defined or pushes {@code false} otherwise.
	 *
	 * @param address the address.
	 * @throws NullPointerException if the given {@code address} is null.
	 * @since 0.2.0 ~2021.05.25
	 */
	public DefAddr(@NotNull String address) {
		Objects.requireNonNull(address, "address");
		this.tree = null;
		this.address = address;
	}

	/**
	 * Construct a new instruction that pushes {@code true} if the given {@code address}
	 * is defined or pushes {@code false} otherwise.
	 *
	 * @param tree    the tree from where this instruction was declared.
	 * @param address the address.
	 * @throws NullPointerException if the given {@code tree} or {@code address} is null.
	 * @since 0.2.0 ~2021.05.25
	 */
	public DefAddr(@NotNull Tree tree, @NotNull String address) {
		Objects.requireNonNull(tree, "tree");
		Objects.requireNonNull(address, "address");
		this.tree = tree;
		this.address = address;
	}

	@Override
	public void exec(@NotNull Environment environment, @NotNull Memory memory) {
		Objects.requireNonNull(environment, "environment");
		Objects.requireNonNull(memory, "memory");

		//DEF( ADDR )
		Value value = memory.get(this.address);

		if (value == Value.NULL)
			memory.push(m -> "true");
		else
			memory.push(m -> "false");
	}
}
