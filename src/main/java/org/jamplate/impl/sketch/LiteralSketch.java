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
package org.jamplate.impl.sketch;

import org.jamplate.model.Sketch;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.NotNull;

/**
 * A specialized implementation of the interface {@link Sketch} that represents a
 * syntax-level literal.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.19
 */
public class LiteralSketch extends Sketch {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 8126285691779567203L;

	/**
	 * Construct a new sketch.
	 *
	 * @since 0.2.0 ~2021.05.17
	 */
	public LiteralSketch() {
	}

	/**
	 * Construct a new sketch that references the given {@code tree} as its tree.
	 *
	 * @param tree the tree the constructed sketch will be pointing to.
	 * @throws NullPointerException if the given {@code tree} is null.
	 * @since 0.2.0 ~2021.05.19
	 */
	public LiteralSketch(@NotNull Tree tree) {
		super(tree);
	}
}
