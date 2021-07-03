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
package org.jamplate.instruction.operator.struct;

import org.jamplate.memory.Memory;
import org.jamplate.memory.Value;
import org.jamplate.model.*;
import org.jamplate.value.ArrayValue;
import org.jamplate.value.NumberValue;
import org.jamplate.value.ObjectValue;
import org.jamplate.value.UnquoteValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * An instruction that pops the top two values in the stack and pushes the property in the
 * second popped value at the first popped value.
 * <br><br>
 * Memory Visualization:
 * <pre>
 *     [..., param:value*|array*|object*, key:value*|number*]
 *     [..., result:value*]
 * </pre>
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.13
 */
public class Get implements Instruction {
	/**
	 * An instance of this instruction.
	 *
	 * @since 0.3.0 ~2021.06.13
	 */
	@NotNull
	public static final Get INSTANCE = new Get();

	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 6807005239681352973L;

	/**
	 * A reference of this instruction in the source code.
	 *
	 * @since 0.3.0 ~2021.06.13
	 */
	@Nullable
	protected final Tree tree;

	/**
	 * Construct a new instruction that pops the last two values and pushes the property
	 * in the second popped value at the first popped value.
	 *
	 * @since 0.3.0 ~2021.06.12
	 */
	public Get() {
		this.tree = null;
	}

	/**
	 * Construct a new instruction that pops the last two values and pushes the property
	 * in the second popped value at the first popped value.
	 *
	 * @param tree a reference for the constructed instruction in the source code.
	 * @throws NullPointerException if the given {@code tree} is null.
	 * @since 0.3.0 ~2021.06.13
	 */
	public Get(@NotNull Tree tree) {
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

		if (value1 instanceof ObjectValue) {
			//right
			Object key0 = value0.evaluate(memory);
			//left
			ObjectValue object1 = (ObjectValue) value1;

			//result
			Value value3 = object1.evaluateAt(memory, key0);
			Value value4 = UnquoteValue.cast(value3);

			memory.push(value4);
			return;
		}
		if (value1 instanceof ArrayValue) {
			//right
			NumberValue number0 = NumberValue.cast(value0);
			int index0 = number0.getPipe().eval(memory).intValue();
			//left
			ArrayValue array1 = (ArrayValue) value1;

			//result
			Value value3 = array1.evaluateAt(memory, index0);
			Value value4 = UnquoteValue.cast(value3);

			memory.push(value4);
			return;
		}

		memory.push(Value.NULL);
	}

	@Nullable
	@Override
	public Tree getTree() {
		return this.tree;
	}

	@NotNull
	@Override
	public Instruction optimize(int mode) {
		return mode < 0 ? Get.INSTANCE : new Get(new Tree(this.tree));
	}
}
