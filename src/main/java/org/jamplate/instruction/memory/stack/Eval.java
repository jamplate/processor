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
package org.jamplate.instruction.memory.stack;

import org.jamplate.memory.Memory;
import org.jamplate.memory.Value;
import org.jamplate.model.*;
import org.jamplate.value.Tokenizer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * An instruction that pops the top value at the stack and pushes the result of evaluating
 * it.
 * <br><br>
 * Memory Visualization:
 * <pre>
 *     [..., param:value]
 *     [..., result:text]
 * </pre>
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.24
 */
public class Eval implements Instruction {
	/**
	 * An instance of this instruction.
	 *
	 * @since 0.3.0 ~2021.06.24
	 */
	@NotNull
	public static final Eval INSTANCE = new Eval();

	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 9194981107095992939L;

	/**
	 * A reference of this instruction in the source code.
	 *
	 * @since 0.3.0 ~2021.06.24
	 */
	@Nullable
	protected final Tree tree;

	/**
	 * Construct a new instruction that pops the top value at the stack and pushes the
	 * result of evaluating it.
	 *
	 * @since 0.3.0 ~2021.06.24
	 */
	public Eval() {
		this.tree = null;
	}

	/**
	 * Construct a new instruction that pops the top value at the stack and pushes the
	 * result of evaluating it.
	 *
	 * @param tree a reference for the constructed instruction in the source code.
	 * @throws NullPointerException if the given {@code tree} is null.
	 * @since 0.3.0 ~2021.06.24
	 */
	public Eval(@NotNull Tree tree) {
		Objects.requireNonNull(tree, "tree");
		this.tree = tree;
	}

	@Override
	public void exec(@NotNull Environment environment, @NotNull Memory memory) {
		Objects.requireNonNull(environment, "environment");
		Objects.requireNonNull(memory, "memory");

		//pop the last value
		Value value0 = memory.pop();

		//result
		String text1 = value0.evaluate(memory);
		Value value1 = Tokenizer.cast(text1);

		//push the result
		memory.push(value1);
	}

	@Nullable
	@Override
	public Tree getTree() {
		return this.tree;
	}

	@NotNull
	@Override
	public Instruction optimize(int mode) {
		return mode < 0 ? Eval.INSTANCE : new Eval(new Tree(this.tree));
	}
}
