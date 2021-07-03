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
package org.jamplate.model;

import org.jamplate.memory.Memory;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;

/**
 * An instruction is a function that executes depending on the state of the environment
 * and memory given to it but does not have a state itself.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.21
 */
@FunctionalInterface
public interface Instruction extends Iterable<Instruction>, Serializable {
	/**
	 * Wrap the given {@code instruction} with another instruction delegates to the given
	 * {@code instruction} when executed.
	 *
	 * @param instruction the instruction to be delegated to by the returned instruction.
	 * @return a new instruction that delegates to the given {@code instruction}.
	 * @throws NullPointerException if the given {@code instruction} is null.
	 * @since 0.2.0 ~2021.05.23
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	static Instruction create(@NotNull Instruction instruction) {
		Objects.requireNonNull(instruction, "instruction");
		return new Instruction() {
			@SuppressWarnings("JavaDoc")
			private static final long serialVersionUID = 5990870318702639863L;

			@Override
			public void exec(@NotNull Environment environment, @NotNull Memory memory) {
				instruction.exec(environment, memory);
			}
		};
	}

	/**
	 * Wrap the given {@code instruction} with another instruction delegates to the given
	 * {@code instruction} when executed and return the given {@code tree} when its {@link
	 * #getTree()} is called.
	 *
	 * @param tree        the tree to be returned by the returned instruction.
	 * @param instruction the instruction to be delegated to by the returned instruction.
	 * @return a new instruction that delegates to the given {@code instruction} and has
	 * 		the given {@code tree}.
	 * @throws NullPointerException if the given {@code tree} or {@code instruction} is
	 *                              null.
	 * @since 0.2.0 ~2021.05.23
	 */
	@NotNull
	@Contract(value = "_,_->new", pure = true)
	static Instruction create(@NotNull Tree tree, @NotNull Instruction instruction) {
		Objects.requireNonNull(tree, "tree");
		Objects.requireNonNull(instruction, "instruction");
		return new Instruction() {
			@SuppressWarnings("JavaDoc")
			private static final long serialVersionUID = 5990870318702639863L;

			@Override
			public void exec(@NotNull Environment environment, @NotNull Memory memory) {
				instruction.exec(environment, memory);
			}

			@NotNull
			@Override
			public Tree getTree() {
				return tree;
			}
		};
	}

	/**
	 * Returns an iterator that iterates over the sub-instructions of this instruction.
	 * This method is very implementation-specific and the default implementation will
	 * return an empty iterator.
	 *
	 * @return an iterator iterating over the sub-instructions of this instruction.
	 * @since 0.3.0 ~2021.06.25
	 */
	@NotNull
	@Override
	default Iterator<Instruction> iterator() {
		return Collections.emptyIterator();
	}

	/**
	 * Return the tree from where this instruction was declared. (optional)
	 *
	 * @return the tree of this instruction.
	 * @implSpec the default implementation returns {@code null}.
	 * @since 0.2.0 ~2021.05.23
	 */
	@Nullable
	@Contract(pure = true)
	default Tree getTree() {
		return null;
	}

	/**
	 * Return an optimized version of this instruction.
	 *
	 * @param mode the mode of the optimization. zero or positive integer for normal
	 *             optimization and negative integer for maximum optimization.
	 * @return an optimized version of this instruction.
	 * @since 0.3.0 ~2021.07.01
	 */
	@NotNull
	@Contract(pure = true)
	default Instruction optimize(int mode) {
		return this;
	}

	/**
	 * Execute this instruction with the given {@code environment} and {@code memory}.
	 *
	 * @param environment the environment this instruction is executed in.
	 * @param memory      the memory holding the variables for this instruction to depend
	 *                    on.
	 * @throws NullPointerException if the given {@code environment} or {@code memory} is
	 *                              null.
	 * @throws ExecutionException   if this instruction failed to be executed.
	 * @since 0.2.0 ~2021.05.21
	 */
	@Contract(mutates = "param1,param2")
	void exec(@NotNull Environment environment, @NotNull Memory memory);
}
