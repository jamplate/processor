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
package org.jamplate.glucose.instruction.memory.frame;

import org.jamplate.glucose.value.VGlue;
import org.jamplate.memory.Memory;
import org.jamplate.memory.Value;
import org.jamplate.model.Environment;
import org.jamplate.model.Instruction;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.jamplate.glucose.internal.util.Values.glue;

/**
 * An instruction that glues the values from the top value to the first {@link Value#NULL
 * null} in the stack of the top frame into a single value and pushes the result to the
 * top of the stack. If the glued values are just one value, that one value will be placed
 * untouched, otherwise, the values will be wrapped in a {@link VGlue}.
 * <br><br>
 * Memory Visualization:
 * <pre>
 *      [..., ...param:value*]
 *      [..., result:value*|glue*]
 *  </pre>
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.07.05
 */
public class IGlueFrame implements Instruction {
	/**
	 * An instance of this instruction.
	 *
	 * @since 0.3.0 ~2021.07.06
	 */
	@NotNull
	public static final IGlueFrame INSTANCE = new IGlueFrame();

	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 7763753752267957709L;

	/**
	 * A reference of this instruction in the source code.
	 *
	 * @since 0.3.0 ~2021.07.06
	 */
	@Nullable
	protected final Tree tree;

	/**
	 * Construct a new reduce frame instruction.
	 *
	 * @since 0.3.0 ~2021.07.06
	 */
	public IGlueFrame() {
		this.tree = null;
	}

	/**
	 * Construct a new reduce frame instruction.
	 *
	 * @param tree a reference for the constructed instruction in the source code.
	 * @throws NullPointerException if the given {@code tree} is null.
	 * @since 0.3.0 ~2021.07.06
	 */
	public IGlueFrame(@NotNull Tree tree) {
		Objects.requireNonNull(tree, "tree");
		this.tree = tree;
	}

	@Override
	public void exec(@NotNull Environment environment, @NotNull Memory memory) {
		Objects.requireNonNull(environment, "environment");
		Objects.requireNonNull(memory, "memory");

		List<Value> values = new ArrayList<>();

		while (true) {
			Value value0 = memory.pop();

			if (value0 == Value.NULL)
				break;

			values.add(value0);
		}

		Collections.reverse(values);

		Value value1 = values.size() == 1 ?
					   values.get(0) :
					   glue(values);

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
		return mode < 0 ? IGlueFrame.INSTANCE : new IGlueFrame(new Tree(this.tree));
	}
}
