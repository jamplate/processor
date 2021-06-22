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

import cufy.util.HashNode;
import cufy.util.Node;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
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
	 * A node relating to the components of this sketch.
	 *
	 * @since 0.2.0 ~2021.05.25
	 */
	@NotNull
	protected final Node<Sketch> components = new HashNode<>(this);
	/**
	 * The additional meta-data of this sketch.
	 *
	 * @since 0.2.0 ~2021.05.21
	 */
	@NotNull
	protected final Map<String, Object> meta = new HashMap<>();

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
	 * Construct a new sketch.
	 *
	 * @param kind the initial kind of the constructed sketch.
	 * @throws NullPointerException if the given {@code kind} is null.
	 * @since 0.2.0 ~2021.05.30
	 */
	public Sketch(@NotNull String kind) {
		Objects.requireNonNull(kind, "kind");
		this.kind = kind;
	}

	/**
	 * Construct a new sketch.
	 *
	 * @param name the initial name of the constructed sketch.
	 * @param kind the initial kind of the constructed sketch.
	 * @throws NullPointerException if the given {@code name} or {@code kind} is null.
	 * @since 0.2.0 ~2021.05.30
	 */
	public Sketch(@NotNull String name, @NotNull String kind) {
		Objects.requireNonNull(name, "name");
		Objects.requireNonNull(kind, "kind");
		this.kind = kind;
		this.name = name;
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

	/**
	 * Construct a new sketch that references the given {@code tree} as its tree.
	 *
	 * @param tree the tree the constructed sketch will be pointing to.
	 * @param kind the initial kind of the constructed sketch.
	 * @throws NullPointerException if the given {@code tree} or {@code kind} is null.
	 * @since 0.2.0 ~2021.05.17
	 */
	public Sketch(@NotNull Tree tree, @NotNull String kind) {
		Objects.requireNonNull(tree, "tree");
		Objects.requireNonNull(kind, "kind");
		this.tree = tree;
		this.kind = kind;
	}

	/**
	 * Construct a new sketch that references the given {@code tree} as its tree.
	 *
	 * @param tree the tree the constructed sketch will be pointing to.
	 * @param name the initial name of the constructed sketch.
	 * @param kind the initial kind of the constructed sketch.
	 * @throws NullPointerException if the given {@code tree} or {@code name} or {@code
	 *                              kind} is null.
	 * @since 0.2.0 ~2021.05.17
	 */
	public Sketch(@NotNull Tree tree, @NotNull String name, @NotNull String kind) {
		Objects.requireNonNull(tree, "tree");
		Objects.requireNonNull(name, "name");
		Objects.requireNonNull(kind, "kind");
		this.tree = tree;
		this.name = name;
		this.kind = kind;
	}

	/**
	 * Return the component key with the given {@code name}.
	 *
	 * @param name the name of the component.
	 * @return the component key.
	 * @throws NullPointerException if the given {@code name} is null.
	 * @since 0.2.0 ~2021.05.30
	 */
	@NotNull
	@Contract(pure = true)
	public static Node.Key component(String name) {
		Objects.requireNonNull(name, "name");
		return new StringKey(name);
	}

	@NotNull
	@Contract(pure = true)
	@Override
	public String toString() {
		return this.kind + (this.name.isEmpty() ? "" : " ") + this.name;
	}

	/**
	 * Return the component of this sketch with the given {@code key}.
	 *
	 * @param key the key of the component.
	 * @return the component with the given {@code key}.
	 * @throws NullPointerException if the given {@code key} is null.
	 * @since 0.2.0 ~2021.05.25
	 */
	@NotNull
	@Contract(pure = true)
	public Sketch get(@NotNull Node.Key key) {
		Objects.requireNonNull(key, "key");
		Node<Sketch> n = this.components.get(key);

		if (n == null) {
			Sketch sketch = new Sketch();

			this.components.put(key, sketch.components);

			return sketch;
		}

		return n.get();
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
	 * Get the meta-data map of this sketch.
	 * <br>
	 * By default, the returned map will be a modifiable checked map. Unless, the class of
	 * this said otherwise.
	 *
	 * @return the meta-data map of this.
	 * @since 0.2.0 ~2021.05.21
	 */
	@NotNull
	@Contract(pure = true)
	public Map<String, Object> getMeta() {
		return Collections.checkedMap(this.meta, String.class, Object.class);
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
	 * Replace the component of this sketch with the given {@code key} with another
	 * sketch.
	 *
	 * @param key the key of the component.
	 * @return the replacement sketch.
	 * @throws NullPointerException if the given {@code key} is null.
	 * @since 0.2.0 ~2021.06.01
	 */
	@NotNull
	@Contract(mutates = "this")
	public Sketch replace(@NotNull Node.Key key) {
		Objects.requireNonNull(key, "key");
		Sketch sketch = new Sketch();

		this.components.put(key, sketch.components);

		return sketch;
	}

	/**
	 * Set the component of this sketch with the given {@code key} to be the given {@code
	 * sketch}.
	 *
	 * @param key    the key of the component.
	 * @param sketch the sketch to be set.
	 * @return this.
	 * @throws NullPointerException if the given {@code key} or {@code sketch} is null.
	 * @since 0.3.0 ~2021.06.22
	 */
	@NotNull
	@Contract(mutates = "this,param2")
	public Sketch set(@NotNull Node.Key key, @NotNull Sketch sketch) {
		Objects.requireNonNull(key, "key");
		Objects.requireNonNull(sketch, "sketch");
		this.components.put(key, sketch.components);
		return this;
	}

	/**
	 * Set the kind of this sketch to be the given {@code kind}.
	 *
	 * @param kind the kind of this sketch.
	 * @return this.
	 * @since 0.2.0 ~2021.05.17
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	public Sketch setKind(@NotNull String kind) {
		Objects.requireNonNull(kind, "kind");
		this.kind = kind;
		return this;
	}

	/**
	 * Set the name of this sketch to be the given {@code name}.
	 *
	 * @param name the name of this sketch.
	 * @return this.
	 * @since 0.2.0 ~2021.05.17
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	public Sketch setName(@NotNull String name) {
		Objects.requireNonNull(name, "name");
		this.name = name;
		return this;
	}

	/**
	 * Set the tree this sketch is from to be the given {@code tree}.
	 *
	 * @param tree the tree to be set.
	 * @return this.
	 * @throws NullPointerException  if the given {@code tree} is null.
	 * @throws IllegalStateException if this sketch already have a tree set.
	 * @since 0.2.0 ~2021.05.17
	 */
	@Contract(value = "_->this", mutates = "this")
	public Sketch setTree(@NotNull Tree tree) {
		Objects.requireNonNull(tree, "tree");
		if (this.tree != null)
			throw new IllegalStateException("Sketch already have a tree set");

		this.tree = tree;
		return this;
	}

	/**
	 * A key that holds a string and matches the keys with the same type that hold an
	 * equal value.
	 *
	 * @author LSafer
	 * @version 0.2.0
	 * @since 0.2.0 ~2021.05.30
	 */
	private static final class StringKey implements Node.Key, Serializable {
		@SuppressWarnings("JavaDoc")
		private static final long serialVersionUID = -1452970592613534022L;

		/**
		 * The opposite key.
		 *
		 * @since 0.2.0 ~2021.05.30
		 */
		@NotNull
		private final StringKey opposite;
		/**
		 * The value of this key. Or {@code null} if this is the opposite key.
		 *
		 * @since 0.2.0 ~2021.05.30
		 */
		@Nullable
		private final String value;

		/**
		 * Construct a new string key holding the given {@code value}.
		 *
		 * @param value the value.
		 * @throws NullPointerException if the given {@code value} is null.
		 * @since 0.2.0 ~2021.05.30
		 */
		private StringKey(@NotNull String value) {
			Objects.requireNonNull(value, "value");
			this.value = value;
			this.opposite = new StringKey(this);
		}

		/**
		 * Construct a new opposite string key.
		 *
		 * @param opposite the original key.
		 * @throws NullPointerException if the given {@code opposite} is null.
		 * @since 0.2.0 ~2021.05.30
		 */
		private StringKey(@NotNull StringKey opposite) {
			Objects.requireNonNull(opposite, "opposite");
			this.value = null;
			this.opposite = opposite;
		}

		@Override
		public boolean equals(@Nullable Object object) {
			if (object == this)
				return true;
			if (object instanceof StringKey) {
				if (this.value == null)
					return this.opposite.equals(object);

				StringKey key = (StringKey) object;

				if (key.value == null)
					return this.equals(key.opposite);

				return this.value.equals(key.value);
			}

			return false;
		}

		@Override
		public int hashCode() {
			return this.value == null ?
				   ~this.opposite.value.hashCode() :
				   this.value.hashCode();
		}

		@NotNull
		@Override
		public Node.Key opposite() {
			return this.opposite;
		}

		@NotNull
		@Override
		public String toString() {
			return this.value == null ?
				   "!" + this.opposite.value :
				   this.value;
		}
	}
}
