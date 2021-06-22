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
 * <h3>{@code ALLOC( ADDR , CONST )}</h3>
 * An instruction that allocate a pre-specified constant to the heap at a pre-specified
 * address.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.31
 */
@Deprecated
public class AllocAddrConst implements Instruction {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 4592223353913897009L;

	/**
	 * The address to allocate to.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	protected final String address;
	/**
	 * The constant to be allocated.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	protected final String constant;
	/**
	 * The tree this instruction was declared at.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@Nullable
	protected final Tree tree;

	/**
	 * Construct a new instruction that allocates the given {@code constant} to the given
	 * {@code address} at the heap.
	 *
	 * @param address  the address to where to allocate.
	 * @param constant the constant to allocate.
	 * @throws NullPointerException if the given {@code address} or {@code constant} is
	 *                              null.
	 * @since 0.2.0 ~2021.05.31
	 */
	public AllocAddrConst(@NotNull String address, @NotNull String constant) {
		Objects.requireNonNull(address, "address");
		Objects.requireNonNull(constant, "constant");
		this.tree = null;
		this.address = address;
		this.constant = constant;
	}

	/**
	 * Construct a new instruction that allocates the given {@code constant} to the given
	 * {@code address} at the heap.
	 *
	 * @param tree     the tree from where this constant was declared.
	 * @param address  the address to where to allocate.
	 * @param constant the constant to allocate.
	 * @throws NullPointerException if the given {@code tree} or {@code address} or {@code
	 *                              constant} is null.
	 * @since 0.2.0 ~2021.05.31
	 */
	public AllocAddrConst(@NotNull Tree tree, @NotNull String address, @NotNull String constant) {
		Objects.requireNonNull(tree, "tree");
		Objects.requireNonNull(address, "address");
		Objects.requireNonNull(constant, "constant");
		this.tree = tree;
		this.address = address;
		this.constant = constant;
	}

	@Override
	public void exec(@NotNull Environment environment, @NotNull Memory memory) {
		Objects.requireNonNull(environment, "environment");
		Objects.requireNonNull(memory, "memory");

		//ALLOC( ADDR , CONST )
		String address = this.address;
		String constant = this.constant;
		memory.set(address, m -> constant);
	}

	@Nullable
	@Override
	public Tree getTree() {
		return this.tree;
	}
}
