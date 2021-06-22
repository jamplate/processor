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
 * <h3>{@code FREE( ADDR )}</h3>
 * An instruction that frees the heap at a pre-specified address.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.31
 */
@Deprecated
public class FreeAddr implements Instruction {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 1632111636984164192L;

	/**
	 * The address to be freed.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	protected final String address;
	/**
	 * The tree this instruction was declared at.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@Nullable
	protected final Tree tree;

	/**
	 * Construct a new instruction that frees the given {@code address} in the heap when
	 * executed.
	 *
	 * @param address the address to be freed.
	 * @throws NullPointerException if the given {@code address} is null.
	 * @since 0.2.0 ~2021.05.31
	 */
	public FreeAddr(@NotNull String address) {
		Objects.requireNonNull(address, "address");
		this.tree = null;
		this.address = address;
	}

	/**
	 * Construct a new instruction that frees the given {@code address} in the heap when
	 * executed.
	 *
	 * @param tree    the tree from where this instruction was declared.
	 * @param address the address to be freed.
	 * @throws NullPointerException if the given {@code tree} or {@code address} is null.
	 * @since 0.2.0 ~2021.05.31
	 */
	public FreeAddr(@NotNull Tree tree, @NotNull String address) {
		Objects.requireNonNull(tree, "tree");
		Objects.requireNonNull(address, "address");
		this.tree = tree;
		this.address = address;
	}

	@Override
	public void exec(@NotNull Environment environment, @NotNull Memory memory) {
		Objects.requireNonNull(environment, "environment");
		Objects.requireNonNull(memory, "memory");
		//FREE( ADDR )
		String address = this.address;
		memory.set(address, Value.NULL);
	}

	@Nullable
	@Override
	public Tree getTree() {
		return this.tree;
	}
}
