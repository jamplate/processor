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
package org.jamplate.impl.util.model;

import org.jamplate.model.Sketch;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * A specialized implementation of the interface {@link Sketch} that represents a scope.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.17
 */
public class ScopeSketch extends Sketch {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -3006066369136395993L;

	/**
	 * The closing anchor of this scope.
	 *
	 * @since 0.2.0 ~2021.05.17
	 */
	@NotNull
	protected final ScopeSketch.Anchor closeAnchor;
	/**
	 * The opening anchor of this scope.
	 *
	 * @since 0.2.0 ~2021.05.17
	 */
	@NotNull
	protected final ScopeSketch.Anchor openAnchor;

	/**
	 * Construct a new scope.
	 *
	 * @since 0.2.0 ~2021.05.17
	 */
	public ScopeSketch() {
		this.openAnchor = new Anchor();
		this.closeAnchor = new Anchor();
	}

	/**
	 * Construct a new scope that references the given {@code tree} as its tree.
	 *
	 * @param tree the tree the constructed scope will be pointing to.
	 * @throws NullPointerException if the given {@code tree} is null.
	 * @since 0.2.0 ~2021.05.17
	 */
	public ScopeSketch(@NotNull Tree tree) {
		super(tree);
		this.openAnchor = new Anchor();
		this.closeAnchor = new Anchor();
	}

	/**
	 * An internal constructor to make subclasses able to change the anchors.
	 *
	 * @param openAnchor  the opening anchor.
	 * @param closeAnchor the closing anchor.
	 * @throws NullPointerException if the given {@code openAnchor} or {@code closeAnchor}
	 *                              is null.
	 * @since 0.2.0 ~2021.05.17
	 */
	protected ScopeSketch(@NotNull ScopeSketch.Anchor openAnchor, @NotNull ScopeSketch.Anchor closeAnchor) {
		Objects.requireNonNull(openAnchor, "openAnchor");
		Objects.requireNonNull(closeAnchor, "closeAnchor");
		this.openAnchor = openAnchor;
		this.closeAnchor = closeAnchor;
	}

	/**
	 * Get the sketch of the closing anchor of this scope.
	 *
	 * @return the closing anchor sketch of this.
	 * @since 0.2.0 ~2021.05.17
	 */
	@NotNull
	@Contract(pure = true)
	public final ScopeSketch.Anchor getCloseAnchor() {
		return this.closeAnchor;
	}

	/**
	 * Get the sketch of the opening anchor of this scope.
	 *
	 * @return the opening anchor sketch of this.
	 * @since 0.2.0 ~2021.05.17
	 */
	@NotNull
	@Contract(pure = true)
	public final ScopeSketch.Anchor getOpenAnchor() {
		return this.openAnchor;
	}

	/**
	 * A sketch representing an anchor of a scope sketch.
	 *
	 * @author LSafer
	 * @version 0.2.0
	 * @since 0.2.0 ~2021.05.17
	 */
	public class Anchor extends Sketch {
		@SuppressWarnings("JavaDoc")
		private static final long serialVersionUID = -3412465985053992082L;

		/**
		 * Return the scope sketch of this sketch.
		 *
		 * @return the scope sketch of this.
		 * @since 0.2.0 ~2021.05.17
		 */
		@NotNull
		@Contract(pure = true)
		public final ScopeSketch getScope() {
			return ScopeSketch.this;
		}
	}
}
