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
	private String kind = "";
	/**
	 * The current name of this sketch.
	 *
	 * @since 0.2.0 ~2021.05.14
	 */
	@NotNull
	private String name = "";

	@NotNull
	@Contract(pure = true)
	@Override
	public String toString() {
		return this.name + " (" + this.getClass().getSimpleName() + ")";
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
	public void setName(@NotNull String name) {
		Objects.requireNonNull(name, "name");
		this.name = name;
	}
}
