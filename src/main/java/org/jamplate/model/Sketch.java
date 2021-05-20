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
package org.jamplate.model;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Objects;

/**
 * A class that holds the thoughts about a syntax or runtime component.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.17
 */
public class Sketch implements Serializable {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -7132338777576310144L;

	/**
	 * The kind name of this sketch.
	 *
	 * @since 0.2.0 ~2021.05.17
	 */
	@NotNull
	protected String kind = "sketch";
	/**
	 * The current name of this sketch.
	 *
	 * @since 0.2.0 ~2021.05.14
	 */
	@NotNull
	protected String name = "";

	/**
	 * The tree this sketch is from.
	 *
	 * @since 0.2.0 ~2021.05.17
	 */
	@Nullable
	protected Tree tree;

	/**
	 * Construct a new sketch.
	 *
	 * @since 0.2.0 ~2021.05.17
	 */
	public Sketch() {
	}

	/**
	 * Construct a new sketch that references the given {@code tree} as its tree.
	 *
	 * @param tree the tree the constructed sketch will be pointing to.
	 * @throws NullPointerException if the given {@code tree} is null.
	 * @since 0.2.0 ~2021.05.17
	 */
	public Sketch(@NotNull Tree tree) {
		Objects.requireNonNull(tree, "tree");
		this.tree = tree;
	}

	@NotNull
	@Contract(pure = true)
	@Override
	public String toString() {
		return this.kind + (this.name.isEmpty() ? "" : " ") + this.name;
	}

	/**
	 * Return the kind of this sketch.
	 *
	 * @return the kind of this sketch.
	 * @since 0.2.0 ~2021.05.17
	 */
	@NotNull
	@Contract(pure = true)
	public String getKind() {
		return this.kind;
	}

	/**
	 * Get the name of this tree.
	 *
	 * @return the name of this tree.
	 * @since 0.2.0 ~2021.05.14
	 */
	@NotNull
	@Contract(pure = true)
	public String getName() {
		return this.name;
	}

	/**
	 * Return the tree of this sketch.
	 *
	 * @return the tree of this sketch.
	 * @since 0.2.0 ~2021.05.17
	 */
	@Nullable
	@Contract(pure = true)
	public Tree getTree() {
		return this.tree;
	}

	/**
	 * Set the kind of this sketch to be the given {@code kind}.
	 *
	 * @param kind the kind of this sketch.
	 * @since 0.2.0 ~2021.05.17
	 */
	@Contract(mutates = "this")
	public void setKind(@NotNull String kind) {
		Objects.requireNonNull(kind, "kind");
		this.kind = kind;
	}

	/**
	 * Set the name of this sketch to be the given {@code name}.
	 *
	 * @param name the name of this sketch.
	 * @since 0.2.0 ~2021.05.17
	 */
	@Contract(mutates = "this")
	public void setName(@NotNull String name) {
		Objects.requireNonNull(name, "name");
		this.name = name;
	}

	/**
	 * Set the tree this sketch is from to be the given {@code tree}.
	 *
	 * @param tree the tree to be set.
	 * @throws NullPointerException  if the given {@code tree} is null.
	 * @throws IllegalStateException if this sketch already have a tree set.
	 * @since 0.2.0 ~2021.05.17
	 */
	@Contract(mutates = "this")
	public void setTree(@NotNull Tree tree) {
		Objects.requireNonNull(tree, "tree");
		if (this.tree != null)
			throw new IllegalStateException("Sketch already have a tree set");

		this.tree = tree;
	}
}
