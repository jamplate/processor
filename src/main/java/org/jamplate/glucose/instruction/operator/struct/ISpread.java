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

import org.jamplate.glucose.value.VObject;
import org.jamplate.glucose.value.VPair;
import org.jamplate.glucose.value.VQuote;
import org.jamplate.memory.Memory;
import org.jamplate.memory.Value;
import org.jamplate.model.Environment;
import org.jamplate.model.ExecutionException;
import org.jamplate.model.Instruction;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static org.jamplate.glucose.internal.util.Values.unquote;

/**
 * An instruction that pops the top value at the stack and spread the mappings in it
 * (assuming its an object) to the heap.
 * <br>
 * If the popped value is not an {@link VObject object}, and {@link ExecutionException}
 * will be thrown.
 * <br><br>
 * Memory Visualization:
 * <pre>
 *     [..., param:object]
 *     [...]
 * </pre>
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.07.10
 */
public class ISpread implements Instruction {
	/**
	 * An instance of this instruction.
	 *
	 * @since 0.0.0 ~2021.07.10
	 */
	@NotNull
	public static final ISpread INSTANCE = new ISpread();

	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 3784777995338524358L;

	/**
	 * A reference of this instruction in the source code.
	 *
	 * @since 0.0.0 ~2021.07.10
	 */
	@Nullable
	protected final Tree tree;

	/**
	 * Construct a new instruction that pops the top value at the stack and spread the
	 * mappings in it (assuming its an object) to the heap.
	 *
	 * @since 0.3.0 ~2021.07.10
	 */
	public ISpread() {
		this.tree = null;
	}

	/**
	 * Construct a new instruction that pops the top value at the stack and spread the
	 * mappings in it (assuming its an object) to the heap.
	 *
	 * @param tree a reference for the constructed instruction in the source code.
	 * @throws NullPointerException if the given {@code tree} is null.
	 * @since 0.3.0 ~2021.07.10
	 */
	public ISpread(@NotNull Tree tree) {
		Objects.requireNonNull(tree, "tree");
		this.tree = tree;
	}

	@Override
	public void exec(@NotNull Environment environment, @NotNull Memory memory) {
		Objects.requireNonNull(environment, "environment");
		Objects.requireNonNull(memory, "memory");

		Value value0 = memory.pop();

		if (value0 instanceof VObject) {
			VObject object0 = (VObject) value0;

			object0.getPipe()
				   .eval(memory)
				   .stream()
				   .map(VPair::getPipe)
				   .map(pair -> pair.eval(memory))
				   .forEach(pair -> {
					   //key
					   Value value1 = pair.getKey();
					   Value value2 =
							   value1 instanceof VQuote ?
							   unquote(value1) :
							   value1;
					   String text2 = value2.eval(memory);
					   //value
					   Value value3 = pair.getValue();
					   Value value4 =
							   value3 instanceof VQuote ?
							   unquote(value3) :
							   value3;

					   memory.set(text2, value4);
				   });

			return;
		}

		throw new ExecutionException(
				"SPREAD expected an object but got: " +
				value0.eval(memory),
				this.tree
		);
	}

	@NotNull
	@Override
	public Instruction optimize(int mode) {
		return mode < 0 ? ISpread.INSTANCE : new ISpread(new Tree(this.tree));
	}
}
