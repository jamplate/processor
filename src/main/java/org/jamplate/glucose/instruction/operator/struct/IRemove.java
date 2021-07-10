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
package org.jamplate.glucose.instruction.operator.struct;

import org.jamplate.glucose.value.VArray;
import org.jamplate.glucose.value.VNumber;
import org.jamplate.glucose.value.VObject;
import org.jamplate.memory.Memory;
import org.jamplate.memory.Value;
import org.jamplate.model.Environment;
import org.jamplate.model.Instruction;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static org.jamplate.glucose.internal.util.Structs.remove;
import static org.jamplate.glucose.internal.util.Values.object;
import static org.jamplate.glucose.internal.util.Values.quote;

/**
 * An instruction that pops the top two values in the stack and removes the value from the
 * second popped value at the key resultant from evaluating the first popped value.
 * <br>
 * If the key is not in the struct, nothing will be changed.
 * <br>
 * If the struct is not an object nor an array, nothing will happen.
 * <br><br>
 * Memory Visualization:
 * <pre>
 *     [..., param:value*|array*|object*, key:value*|number*]
 *     [..., result:value*|array*|object*]
 * </pre>
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.07.09
 */
public class IRemove implements Instruction {
	/**
	 * An instance of this instruction.
	 *
	 * @since 0.3.0 ~2021.07.09
	 */
	@NotNull
	public static final IRemove INSTANCE = new IRemove();

	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 8139203410508479354L;

	/**
	 * A reference of this instruction in the source code.
	 *
	 * @since 0.3.0 ~2021.07.09
	 */
	@Nullable
	protected final Tree tree;

	/**
	 * Construct a new instruction that pops the top two values in the stack and removes
	 * the value from the second popped value at the key resultant from evaluating the
	 * first popped value.
	 *
	 * @since 0.3.0 ~2021.07.09
	 */
	public IRemove() {
		this.tree = null;
	}

	/**
	 * Construct a new instruction that pops the top two values in the stack and removes
	 * the value from the second popped value at the key resultant from evaluating the
	 * first popped value.
	 *
	 * @param tree a reference for the constructed instruction in the source code.
	 * @throws NullPointerException if the given {@code tree} is null.
	 * @since 0.3.0 ~2021.07.09
	 */
	public IRemove(@NotNull Tree tree) {
		Objects.requireNonNull(tree, "tree");
		this.tree = tree;
	}

	@Override
	public void exec(@NotNull Environment environment, @NotNull Memory memory) {
		Objects.requireNonNull(environment, "environment");
		Objects.requireNonNull(memory, "memory");

		//key
		Value value1 = memory.pop();
		//struct
		Value value2 = memory.pop();

		if (value2 instanceof VArray && value1 instanceof VNumber) {
			//key
			VNumber number1 = (VNumber) value1;
			//struct
			VArray array2 = (VArray) value2;

			//result
			VArray array3 = remove(
					array2,
					number1
			);

			memory.push(array3);
		}
		if (value2 instanceof VObject) {
			//key
			Value quote1 = quote(value1);
			//struct
			VObject object2 = object(value2);

			//result
			VObject object3 = remove(
					object2,
					quote1
			);

			memory.push(object3);
		}
	}

	@Nullable
	@Override
	public Tree getTree() {
		return this.tree;
	}

	@NotNull
	@Override
	public Instruction optimize(int mode) {
		return mode < 0 ? IRemove.INSTANCE : new IRemove(new Tree(this.tree));
	}
}
