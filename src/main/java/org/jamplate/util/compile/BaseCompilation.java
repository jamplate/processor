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
package org.jamplate.util.compile;

import org.jamplate.compile.Compilation;
import org.jamplate.compile.Environment;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * A basic compilation implementation with no special features.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.17
 */
public class BaseCompilation implements Compilation {
	/**
	 * The root tree of this compilation.
	 *
	 * @since 0.2.0 ~2021.05.18
	 */
	@NotNull
	protected final Tree root;
	/**
	 * The environment this compilation is from.
	 *
	 * @since 0.2.0 ~2021.05.18
	 */
	@NotNull //fixme initialize the environment and add `final` modifier
	protected Environment environment;

	/**
	 * Construct a new compilation with the given {@code root} tree to be its root tree.
	 *
	 * @param root the root tree for the constructed compilation.
	 * @throws NullPointerException if the given {@code tree} is null.
	 * @since 0.2.0 ~2021.05.18
	 */
	public BaseCompilation(@NotNull Tree root) {
		Objects.requireNonNull(root, "root");
		this.root = root;
	}

	@NotNull
	@Override
	public Environment environment() {
		return this.environment;
	}

	@NotNull
	@Override
	public Tree root() {
		return this.root;
	}
}
