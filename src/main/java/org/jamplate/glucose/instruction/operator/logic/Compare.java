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
package org.jamplate.glucose.instruction.operator.logic;

import org.jamplate.glucose.value.NumberValue;
import org.jamplate.memory.Memory;
import org.jamplate.memory.Value;
import org.jamplate.model.Environment;
import org.jamplate.model.Instruction;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static java.lang.Double.compare;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static org.jamplate.glucose.internal.util.Values.number;

/**
 * An instruction that pops the last two values and pushes a value that evaluates to
 * results of comparing the second popped value to the first popped value as specified by
 * {@link java.util.Comparator#compare(Object, Object)} except that the results will be
 * one of {@code -1, 0, 1}.
 * <br>
 * Note: {@link Value#NULL} is the most low value, does not equal anything other than
 * itself.
 * <br>
 * If the two values are both numbers, the values will be compared mathematically.
 * <br>
 * If one of the values is not a number, the values will be compared literally.
 * <br><br>
 * Memory Visualization:
 * <pre>
 *     [..., left:value*|number*, right:value*|number*]
 *     [..., result:number*]
 * </pre>
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.13
 */
public class Compare implements Instruction {
	/**
	 * An instance of this instruction.
	 *
	 * @since 0.3.0 ~2021.06.13
	 */
	@NotNull
	public static final Compare INSTANCE = new Compare();

	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 8682694132784881037L;

	/**
	 * A reference of this instruction in the source code.
	 *
	 * @since 0.3.0 ~2021.06.13
	 */
	@Nullable
	protected final Tree tree;

	/**
	 * Construct a new instruction that pops the last two values and pushes the results of
	 * comparing the second popped value to the first popped value.
	 *
	 * @since 0.3.0 ~2021.06.13
	 */
	public Compare() {
		this.tree = null;
	}

	/**
	 * Construct a new instruction that pops the last two values and pushes the results of
	 * comparing the second popped value to the first popped value.
	 *
	 * @param tree a reference for the constructed instruction in the source code.
	 * @throws NullPointerException if the given {@code tree} is null.
	 * @since 0.3.0 ~2021.06.13
	 */
	public Compare(@NotNull Tree tree) {
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

		if (value1 == Value.NULL) {
			if (value0 == Value.NULL)
				//both the values are null
				memory.push(number(0));
			else
				//only the left value is null
				memory.push(number(-1));
			return;
		}
		if (value0 == Value.NULL) {
			//only the right value is null
			memory.push(number(1));
			return;
		}

		if (value0 instanceof NumberValue && value1 instanceof NumberValue) {
			//right
			NumberValue number0 = (NumberValue) value0;
			//left
			NumberValue number1 = (NumberValue) value1;

			//result
			NumberValue number2 = number1.apply((m, n) ->
					min(1, max(-1, compare(
							n.doubleValue(),
							number0.getPipe().eval(m).doubleValue()
					)))
			);

			memory.push(number2);
			return;
		}

		//both the values are nonnull
		memory.push(number((m, v) -> {
			//right
			String text0 = value0.eval(m);
			//left
			String text1 = value1.eval(m);

			//result
			int num3 = text1.compareTo(text0);
			int num4 = num3 > 1 ? 1 : num3 < -1 ? -1 : num3;

			return (double) num4;
		}));
	}

	@Nullable
	@Override
	public Tree getTree() {
		return this.tree;
	}

	@NotNull
	@Override
	public Instruction optimize(int mode) {
		return mode < 0 ? Compare.INSTANCE : new Compare(new Tree(this.tree));
	}
}
