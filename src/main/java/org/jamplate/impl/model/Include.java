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
package org.jamplate.impl.model;

import org.jamplate.model.Sketch;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * A sketch representing the include command.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.20
 */
public class Include extends Sketch {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 4758562461808331354L;

	/**
	 * The tree of the whole command.
	 *
	 * @since 0.2.0 ~2021.05.20
	 */
	@NotNull
	protected final Tree command;

	/**
	 * Construct a new include sketch.
	 *
	 * @param command the tree of the whole command.
	 * @since 0.2.0 ~2021.05.20
	 */
	public Include(@NotNull Tree command) {
		Objects.requireNonNull(command, "command");
		this.command = command;
	}

	/**
	 * Construct a new include sketch with the given {@code tree}.
	 *
	 * @param command the tree of the whole command.
	 * @param tree    the tree of the include sketch.
	 * @since 0.2.0 ~2021.05.20
	 */
	public Include(@NotNull Tree command, @NotNull Tree tree) {
		super(tree);
		Objects.requireNonNull(command, "command");
		this.command = command;
	}

	/**
	 * Return the whole command tree.
	 *
	 * @return the whole command tree.
	 * @since 0.2.0 ~2021.05.20
	 */
	@NotNull
	public Tree getCommandTree() {
		return this.command;
	}
}
