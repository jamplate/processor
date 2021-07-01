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
package org.jamplate.instruction.flow;

import org.jamplate.model.*;
import org.jamplate.value.BooleanValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

/**
 * An instruction that executes a pre-specified instruction {@code branch} if the top
 * value in the stack evaluated to {@code true} and executes an instruction {@code
 * fallback} if the top value in the stack evaluated to {@code false}. The top value in
 * the stack will be popped in the process.
 * <br>
 * If the top value in the stack is not a {@link BooleanValue boolean}, then an {@link
 * ExecutionException} will be thrown.
 * <br><br>
 * Memory Visualization (before executing the branch):
 * <pre>
 *     [..., param:boolean]
 *     [...]
 * </pre>
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.11
 */
public class Branch implements Instruction {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 6900017381637501606L;

	/**
	 * The instruction to be executed when the value from popping the stack was not {@link
	 * Value#NULL}.
	 *
	 * @since 0.3.0 ~2021.06.11
	 */
	@NotNull
	protected final Instruction branch;
	/**
	 * The instruction to be executed when the value from popping the stack was {@link
	 * Value#NULL}.
	 *
	 * @since 0.3.0 ~2021.06.11
	 */
	@NotNull
	protected final Instruction fallback;
	/**
	 * A reference of this instruction in the source code.
	 *
	 * @since 0.3.0 ~2021.06.11
	 */
	@Nullable
	protected final Tree tree;

	/**
	 * Construct a new instruction that executes {@code param} and if the results is
	 * {@link Value#NULL} then executes {@code fallback}. Otherwise, executes {@code
	 * delegate}.
	 *
	 * @param branch   the instruction to be executed when the condition is satisfied.
	 * @param fallback the instruction to be executed when the condition is not
	 *                 satisfied.
	 * @throws NullPointerException if the given {@code branch} or {@code fallback} is
	 *                              null.
	 * @since 0.3.0 ~2021.06.11
	 */
	public Branch(
			@NotNull Instruction branch,
			@NotNull Instruction fallback
	) {
		Objects.requireNonNull(branch, "branch");
		Objects.requireNonNull(fallback, "fallback");
		this.tree = null;
		this.branch = branch;
		this.fallback = fallback;
	}

	/**
	 * Construct a new instruction that pops the stack and if the value was not {@link
	 * Value#NULL} then executes {@code branch}. Otherwise, executes {@code fallback}.
	 *
	 * @param tree     a reference for the constructed instruction in the source code.
	 * @param branch   the instruction to be executed when the condition is satisfied.
	 * @param fallback the instruction to be executed when the condition is not
	 *                 satisfied.
	 * @throws NullPointerException if the given {@code tree} or {@code branch} or {@code
	 *                              fallback} is null.
	 * @since 0.3.0 ~2021.06.11
	 */
	public Branch(
			@NotNull Tree tree,
			@NotNull Instruction branch,
			@NotNull Instruction fallback
	) {
		Objects.requireNonNull(tree, "tree");
		Objects.requireNonNull(branch, "branch");
		Objects.requireNonNull(fallback, "fallback");
		this.tree = tree;
		this.branch = branch;
		this.fallback = fallback;
	}

	@Override
	public void exec(@NotNull Environment environment, @NotNull Memory memory) {
		Objects.requireNonNull(environment, "environment");
		Objects.requireNonNull(memory, "memory");

		Value value0 = memory.pop();
		String text0 = value0.evaluate(memory);

		switch (text0) {
			case "true":
				this.branch.exec(environment, memory);
				break;
			case "false":
				this.fallback.exec(environment, memory);
				break;
			default:
				throw new ExecutionException(
						"BRANCH expected a boolean but got: " + text0,
						this.tree
				);
		}
	}

	@Nullable
	@Override
	public Tree getTree() {
		return this.tree;
	}

	@NotNull
	@Override
	public Iterator<Instruction> iterator() {
		return Arrays.asList(this.branch, this.fallback).iterator();
	}

	@NotNull
	@Override
	public Instruction optimize(int mode) {
		return mode < 0 ?
			   new Branch(
					   this.branch.optimize(mode),
					   this.fallback.optimize(mode)
			   ) :
			   new Branch(
					   new Tree(this.tree),
					   this.branch.optimize(mode),
					   this.fallback.optimize(mode)
			   );
	}
}
