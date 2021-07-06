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
package org.jamplate.glucose.instruction.memory.console;

import org.jamplate.glucose.value.ObjectValue;
import org.jamplate.memory.Memory;
import org.jamplate.memory.Value;
import org.jamplate.model.Environment;
import org.jamplate.model.Instruction;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static org.jamplate.glucose.internal.util.Values.object;

/**
 * An instruction that pops the last two values from the stack and prints the results of
 * evaluating the second popped value to the console with applying the replacements of
 * evaluating the first popped value.
 * <br><br>
 * Memory Visualization:
 * <pre>
 *     [..., param:value, replace:value|object]
 *     [...]
 * </pre>
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.27
 */
public class FPrint implements Instruction {
	/**
	 * An instance of this instruction.
	 *
	 * @since 0.3.0 ~2021.06.27
	 */
	@NotNull
	public static final FPrint INSTANCE = new FPrint();

	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 9113900447926859442L;

	/**
	 * A reference of this instruction in the source code.
	 *
	 * @since 0.3.0 ~2021.06.27
	 */
	@Nullable
	protected final Tree tree;

	/**
	 * Construct a new instruction that pops the last two values from the stack and prints
	 * the results of evaluating the second popped value to the console with applying the
	 * replacements of evaluating the first popped value.
	 *
	 * @since 0.3.0 ~2021.06.27
	 */
	public FPrint() {
		this.tree = null;
	}

	/**
	 * Construct a new instruction that pops the last two values from the stack and prints
	 * the results of evaluating the second popped value to the console with applying the
	 * replacements of evaluating the first popped value.
	 *
	 * @param tree a reference for the constructed instruction in the source code.
	 * @throws NullPointerException if the given {@code tree} is null.
	 * @since 0.3.0 ~2021.06.27
	 */
	public FPrint(@NotNull Tree tree) {
		Objects.requireNonNull(tree, "tree");
		this.tree = tree;
	}

	@Override
	public void exec(@NotNull Environment environment, @NotNull Memory memory) {
		Objects.requireNonNull(environment, "environment");
		Objects.requireNonNull(memory, "memory");

		//right
		Value value0 = memory.pop();
		//left
		Value value1 = memory.pop();

		//left
		String text1 = value1.evaluate(memory);

		//right
		ObjectValue object0 = object(value0);

		//result
		String text2 = object0
				.getPipe()
				.eval(memory)
				.stream()
				.map(p -> p.getPipe().eval(memory))
				.reduce(
						text1,
						(s, e) ->
								s.replace(
										e.getKey().evaluate(memory),
										e.getValue().evaluate(memory)
								),
						(a, b) -> a + b
				);

		memory.print(text2);
	}

	@Nullable
	@Override
	public Tree getTree() {
		return this.tree;
	}

	@NotNull
	@Override
	public Instruction optimize(int mode) {
		return mode < 0 ? FPrint.INSTANCE : new FPrint(new Tree(this.tree));
	}
}
