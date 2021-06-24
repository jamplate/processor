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
package org.jamplate.instruction.cast;

import org.jamplate.model.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * An instruction that pops the last value in the stack and pushes a value that evaluate
 * to the popped value interpreted as a number.
 * <br>
 * <table>
 *     <tr>
 *         <th>Evaluation</th>
 *         <th>Interpretation</th>
 *     </tr>
 *     <tr>
 *         <td>Number</td>
 *         <td>The number</td>
 *     </tr>
 *     <tr>
 *         <td>""</td>
 *         <td>0</td>
 *     </tr>
 *     <tr>
 *         <td>"0"</td>
 *         <td>0</td>
 *     </tr>
 *     <tr>
 *         <td>"false"</td>
 *         <td>0</td>
 *     </tr>
 *     <tr>
 *         <td>"null"</td>
 *         <td>0</td>
 *     </tr>
 *     <tr>
 *         <td>"1"</td>
 *         <td>1</td>
 *     </tr>
 *     <tr>
 *         <td>"true"</td>
 *         <td>1</td>
 *     </tr>
 *     <tr>
 *         <td>The rest</td>
 *         <td>The hashcode of the text as in {@link String#hashCode()}</td>
 *     </tr>
 * </table>
 * <br><br>
 * Memory Visualization:
 * <pre>
 *     [..., input:text:lazy]
 *     [...]
 *     [..., output:number:lazy]
 * </pre>
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.13
 */
public class CastNumber implements Instruction {
	/**
	 * An instance of this instruction.
	 *
	 * @since 0.3.0 ~2021.06.13
	 */
	@NotNull
	public static final CastNumber INSTANCE = new CastNumber();

	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 4663666280068528631L;

	/**
	 * A reference of this instruction in the source code.
	 *
	 * @since 0.3.0 ~2021.06.13
	 */
	@Nullable
	protected final Tree tree;

	/**
	 * Construct a new instruction that pops the last value in the stack and pushes a
	 * value that evaluate to the popped value interpreted as a number.
	 *
	 * @since 0.3.0 ~2021.06.13
	 */
	public CastNumber() {
		this.tree = null;
	}

	/**
	 * Construct a new instruction that pops the last value in the stack and pushes a
	 * value that evaluate to the popped value interpreted as a number.
	 *
	 * @param tree a reference for the constructed instruction in the source code.
	 * @throws NullPointerException if the given {@code tree} is null.
	 * @since 0.3.0 ~2021.06.13
	 */
	public CastNumber(@NotNull Tree tree) {
		Objects.requireNonNull(tree, "tree");
		this.tree = tree;
	}

	@Override
	public void exec(@NotNull Environment environment, @NotNull Memory memory) {
		Objects.requireNonNull(environment, "environment");
		Objects.requireNonNull(memory, "memory");

		Value value0 = memory.pop();

		memory.push(m -> {
			String text0 = value0.evaluate(m);

			switch (text0) {
				case "":
				case "0":
				case "false":
				case "null":
					return "0";
				case "1":
				case "true":
					return "1";
				default:
					try {
						double num0 = Double.parseDouble(text0);

						return Double.toString(num0);
					} catch (NumberFormatException ignored0) {
						int num0 = text0.hashCode();

						return String.valueOf(num0);
					}
			}
		});
	}

	@Nullable
	@Override
	public Tree getTree() {
		return this.tree;
	}
}
