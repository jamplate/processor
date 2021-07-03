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

import org.jamplate.impl.memory.BufferedConsole;
import org.jamplate.impl.memory.FileConsole;
import org.jamplate.memory.Memory;
import org.jamplate.memory.Value;
import org.jamplate.model.Environment;
import org.jamplate.model.ExecutionException;
import org.jamplate.model.Instruction;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

/**
 * An instruction that pops the top value at the stack and sets the console to the file
 * from evaluating the popped value. If the popped value is {@link Value#NULL}, a new
 * string buffer/builder will be set instead.
 * <br>
 * If an I/O exception occurred while trying to open a file-writer for the targeted file,
 * an {@link ExecutionException} will be thrown.
 * <br><br>
 * Memory Visualization:
 * <pre>
 *     [..., param:value]
 *     [...]
 * </pre>
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.15
 */
public class SetConsole implements Instruction {
	/**
	 * An instance of this instruction.
	 *
	 * @since 0.3.0 ~2021.06.15
	 */
	@NotNull
	public static final SetConsole INSTANCE = new SetConsole();

	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -4360149130636815580L;

	/**
	 * A reference of this instruction in the source code.
	 *
	 * @since 0.3.0 ~2021.06.13
	 */
	@Nullable
	protected final Tree tree;

	/**
	 * Construct a new instruction that pops the top value at the stack and sets the
	 * console to the file from evaluating the popped value.
	 *
	 * @since 0.3.0 ~2021.06.15
	 */
	public SetConsole() {
		this.tree = null;
	}

	/**
	 * Construct a new instruction that pops the top value at the stack and sets the
	 * console to the file from evaluating the popped value.
	 *
	 * @param tree a reference for the constructed instruction in the source code.
	 * @throws NullPointerException if the given {@code tree} is null.
	 * @since 0.3.0 ~2021.06.15
	 */
	public SetConsole(@NotNull Tree tree) {
		Objects.requireNonNull(tree, "tree");
		this.tree = tree;
	}

	@Override
	public void exec(@NotNull Environment environment, @NotNull Memory memory) {
		Objects.requireNonNull(environment, "environment");
		Objects.requireNonNull(memory, "memory");

		Value value0 = memory.pop();
		String text0 = value0.evaluate(memory);

		if (value0 == Value.NULL) {
			memory.getConsole().close();
			memory.setConsole(new BufferedConsole());
			return;
		}

		try {
			File file = new File(text0);
			File parent = file.getParentFile();

			if (parent != null && !parent.exists())
				Files.createDirectories(parent.toPath());

			memory.setConsole(new FileConsole(file));
		} catch (IOException | IOError e0) {
			throw new ExecutionException(e0, this.tree);
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
		return mode < 0 ? SetConsole.INSTANCE : new SetConsole(new Tree(this.tree));
	}
}
