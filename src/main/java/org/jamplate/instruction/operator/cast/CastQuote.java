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
package org.jamplate.instruction.operator.cast;

import org.jamplate.model.*;
import org.jamplate.value.QuoteValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * An instruction that pops the last value in the stack and pushes the value of casting it
 * with {@link QuoteValue#cast(Object)}.
 * <br><br>
 * Memory Visualization:
 * <pre>
 *     [..., param:value*]
 *     [..., result:quote*]
 * </pre>
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.27
 */
public class CastQuote implements Instruction {
	/**
	 * An instance of this instruction.
	 *
	 * @since 0.3.0 ~2021.06.27
	 */
	public static final CastQuote INSTANCE = new CastQuote();

	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -7955758253109124525L;

	/**
	 * A reference of this instruction in the source code.
	 *
	 * @since 0.3.0 ~2021.06.27
	 */
	@Nullable
	protected final Tree tree;

	/**
	 * Construct a new instruction that pops the last value in the stack and pushes the
	 * value of casting it with {@link QuoteValue#cast(Object)}.
	 *
	 * @since 0.3.0 ~2021.06.27
	 */
	public CastQuote() {
		this.tree = null;
	}

	/**
	 * Construct a new instruction that pops the last value in the stack and pushes the
	 * value of casting it with {@link QuoteValue#cast(Object)}.
	 *
	 * @param tree a reference for the constructed instruction in the source code.
	 * @throws NullPointerException if the given {@code tree} is null.
	 * @since 0.3.0 ~2021.06.27
	 */
	public CastQuote(@NotNull Tree tree) {
		Objects.requireNonNull(tree, "tree");
		this.tree = tree;
	}

	@Override
	public void exec(@NotNull Environment environment, @NotNull Memory memory) {
		Objects.requireNonNull(environment, "environment");
		Objects.requireNonNull(memory, "memory");

		Value value0 = memory.pop();
		Value value1 = QuoteValue.cast(value0);

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
		return mode < 0 ? CastQuote.INSTANCE : new CastQuote(new Tree(this.tree));
	}
}
