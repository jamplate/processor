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
package org.jamplate.glucose.instruction.flow;

import org.jamplate.glucose.value.TextValue;
import org.jamplate.impl.memory.BufferedConsole;
import org.jamplate.memory.Console;
import org.jamplate.memory.Memory;
import org.jamplate.memory.Value;
import org.jamplate.model.Environment;
import org.jamplate.model.Instruction;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;

/**
 * An instruction that replaces the console with a temporary buffer, then executes a
 * pre-specified instruction {@code instruction}, then pushes the value of reading the
 * buffer to the stack, then set the previous console back.
 * <br><br>
 * Memory Visualization (after executing the instruction):
 * <pre>
 *     [...]
 *     [..., param:value]
 * </pre>
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.07.03
 */
public class Capture implements Instruction {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 9188332158716599483L;

	/**
	 * The instruction to be executed.
	 *
	 * @since 0.3.0 ~2021.07.03
	 */
	@NotNull
	protected final Instruction instruction;
	/**
	 * A reference of this instruction in the source code.
	 *
	 * @since 0.3.0 ~2021.07.03
	 */
	@Nullable
	protected final Tree tree;

	/**
	 * Construct a new instruction that replaces the console with a temporary buffer, then
	 * executes a pre-specified instruction {@code instruction}, then pushes the value of
	 * reading the buffer to the stack, then set the previous console back.
	 *
	 * @param instruction the instruction to be executed.
	 * @throws NullPointerException if the given {@code instruction} is null.
	 * @since 0.3.0 ~2021.07.03
	 */
	public Capture(@NotNull Instruction instruction) {
		Objects.requireNonNull(instruction, "instruction");
		this.tree = null;
		this.instruction = instruction;
	}

	/**
	 * Construct a new instruction that replaces the console with a temporary buffer, then
	 * executes a pre-specified instruction {@code instruction}, then pushes the value of
	 * reading the buffer to the stack, then set the previous console back.
	 *
	 * @param tree        a reference for the constructed instruction in the source code.
	 * @param instruction the instruction to be executed.
	 * @throws NullPointerException if the given {@code tree} or {@code instruction} is
	 *                              null.
	 * @since 0.3.0 ~2021.07.03
	 */
	public Capture(@NotNull Tree tree, @NotNull Instruction instruction) {
		Objects.requireNonNull(tree, "tree");
		Objects.requireNonNull(instruction, "instruction");
		this.tree = tree;
		this.instruction = instruction;
	}

	@Override
	public void exec(@NotNull Environment environment, @NotNull Memory memory) {
		Objects.requireNonNull(environment, "environment");
		Objects.requireNonNull(memory, "memory");

		//original console
		Console console0 = memory.getConsole();
		//buffer (temporary) console
		Console console1 = new BufferedConsole();

		//replace the console
		memory.setConsole(console1);

		//execute the instruction
		this.instruction.exec(environment, memory);

		//the console after the execution (in case it got replaced)
		Console console2 = memory.getConsole();

		//close the buffer (temporary) console
		console1.close();
		//close the console after the execution
		console2.close();

		//restore the original console
		memory.setConsole(console0);

		//the captured text
		String text3 = console1.read();
		Value value3 = new TextValue(text3);

		//push the captured text
		memory.push(value3);
	}

	@Nullable
	@Override
	public Tree getTree() {
		return this.tree;
	}

	@NotNull
	@Override
	public Iterator<Instruction> iterator() {
		return Collections.singleton(this.instruction).iterator();
	}

	@NotNull
	@Override
	public Instruction optimize(int mode) {
		return mode < 0 ?
			   new Capture(
					   this.instruction.optimize(mode)
			   ) :
			   new Capture(
					   new Tree(this.tree),
					   this.instruction.optimize(mode)
			   );
	}
}
