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
package org.jamplate.impl.compilation;

import org.jamplate.model.Compilation;
import org.jamplate.model.Environment;
import org.jamplate.model.Instruction;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * A basic compilation implementation with no special features.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.17
 */
public class CompilationImpl implements Compilation {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 7506240715961471670L;

	/**
	 * The environment this compilation is from.
	 *
	 * @since 0.2.0 ~2021.05.18
	 */
	@NotNull
	protected final Environment environment;

	/**
	 * The root tree of this compilation.
	 *
	 * @since 0.2.0 ~2021.05.18
	 */
	@NotNull
	protected final Tree root;

	/**
	 * The instruction of this compilation. This is the results of compiling this
	 * compilation.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	@Nullable
	protected Instruction instruction;

	/**
	 * Construct a new compilation with the given {@code root} tree to be its root tree.
	 *
	 * @param environment the environment for the constructed compilation.
	 * @param root        the root tree for the constructed compilation.
	 * @throws NullPointerException if the given {@code tree} is null.
	 * @since 0.2.0 ~2021.05.18
	 */
	public CompilationImpl(@NotNull Environment environment, @NotNull Tree root) {
		Objects.requireNonNull(environment, "environment");
		Objects.requireNonNull(root, "root");
		this.environment = environment;
		this.root = root;
	}

	@NotNull
	@Override
	public Environment getEnvironment() {
		return this.environment;
	}

	@Nullable
	@Override
	public Instruction getInstruction() {
		return this.instruction;
	}

	@NotNull
	@Override
	public Tree getRootTree() {
		return this.root;
	}

	@Override
	public void setInstruction(@NotNull Instruction instruction) {
		Objects.requireNonNull(instruction, "instruction");
		this.instruction = instruction;
	}
}
