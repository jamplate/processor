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
 * A sketch of a command.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.20
 */
public class CommandSketch extends ScopeSketch {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 6566273402709048696L;

	/**
	 * The sketch of the parameter of this command.
	 *
	 * @since 0.2.0 ~2021.05.20
	 */
	@NotNull
	protected final CommandSketch.ParameterSketch parameter;

	/**
	 * The type of this command.
	 *
	 * @since 0.2.0 ~2021.05.20
	 */
	@NotNull
	protected final CommandSketch.TypeSketch type;

	/**
	 * Construct a new command sketch.
	 *
	 * @since 0.2.0 ~2021.05.20
	 */
	public CommandSketch() {
		this.type = new TypeSketch();
		this.parameter = new ParameterSketch();
	}

	/**
	 * Construct a new command sketch with the given {@code tree}.
	 *
	 * @param tree the tree of the constructed sketch.
	 * @throws NullPointerException if the given {@code tree} is null.
	 * @since 0.2.0 ~2021.05.20
	 */
	public CommandSketch(@NotNull Tree tree) {
		super(tree);
		this.type = new TypeSketch();
		this.parameter = new ParameterSketch();
	}

	/**
	 * An internal constructor for the subclasses to be able to specify component
	 * sketches.
	 *
	 * @param openAnchor  the sketch of the opening anchor of the constructed command.
	 * @param closeAnchor the sketch of the closing anchor of the constructed command.
	 * @param type        the sketch of the type of the constructed command.
	 * @param parameter   the sketch of the parameter of the constructed command.
	 * @throws NullPointerException if the given {@code openAnchor} or {@code closeAnchor}
	 *                              or {@code type} or {@code parameter} is null.
	 * @since 0.2.0 ~2021.05.20
	 */
	protected CommandSketch(@NotNull ScopeSketch.AnchorSketch openAnchor, @NotNull ScopeSketch.AnchorSketch closeAnchor, @NotNull CommandSketch.TypeSketch type, @NotNull CommandSketch.ParameterSketch parameter) {
		super(openAnchor, closeAnchor);
		Objects.requireNonNull(type, "type");
		Objects.requireNonNull(parameter, "parameter");
		this.type = type;
		this.parameter = parameter;
	}

	/**
	 * Return the sketch of the parameter of this command.
	 *
	 * @return the parameter sketch of this.
	 * @since 0.2.0 ~2021.05.20
	 */
	@NotNull
	@Contract(pure = true)
	public CommandSketch.ParameterSketch getParameterSketch() {
		return this.parameter;
	}

	/**
	 * Return the sketch of the type of this command.
	 *
	 * @return the type of this command.
	 * @since 0.2.0 ~2021.05.20
	 */
	@NotNull
	@Contract(pure = true)
	public CommandSketch.TypeSketch getTypeSketch() {
		return this.type;
	}

	/**
	 * A sketch for the parameter passed to the command.
	 *
	 * @author LSafer
	 * @version 0.2.0
	 * @since 0.2.0 ~2021.05.20
	 */
	public class ParameterSketch extends Sketch {
		@SuppressWarnings("JavaDoc")
		private static final long serialVersionUID = -300019613180151971L;

		/**
		 * The key part of the parameter.
		 *
		 * @since 0.2.0 ~2021.05.23
		 */
		@NotNull
		protected final CommandSketch.ParameterSketch.KeySketch key;
		/**
		 * The value part of the parameter.
		 *
		 * @since 0.2.0 ~2021.05.23
		 */
		@NotNull
		protected final CommandSketch.ParameterSketch.ValueSketch value;

		/**
		 * Construct a new command parameter sketch.
		 *
		 * @since 0.2.0 ~2021.05.23
		 */
		public ParameterSketch() {
			this.key = new KeySketch();
			this.value = new ValueSketch();
		}

		/**
		 * An internal construct for subclasses to be able to specify the components.
		 *
		 * @param key   the key part of the constructed parameter.
		 * @param value the value part of the constructed parameter.
		 * @throws NullPointerException if the given {@code key} or {@code value} is
		 *                              null.
		 * @since 0.2.0 ~2021.05.23
		 */
		protected ParameterSketch(@NotNull CommandSketch.ParameterSketch.KeySketch key, @NotNull CommandSketch.ParameterSketch.ValueSketch value) {
			Objects.requireNonNull(key, "key");
			Objects.requireNonNull(value, "value");
			this.key = key;
			this.value = value;
		}

		/**
		 * Return the command sketch.
		 *
		 * @return the sketch of the command this sketch is in.
		 * @since 0.2.0 ~2021.05.20
		 */
		@NotNull
		@Contract(pure = true)
		public CommandSketch getCommandSketch() {
			return CommandSketch.this;
		}

		/**
		 * Return the key part of the parameter.
		 *
		 * @return the key part of the parameter.
		 * @since 0.2.0 ~2021.05.23
		 */
		@NotNull
		@Contract(pure = true)
		public CommandSketch.ParameterSketch.KeySketch getKeySketch() {
			return this.key;
		}

		/**
		 * Return the value part of the parameter.
		 *
		 * @return the value part of the parameter.
		 * @since 0.2.0 ~2021.05.23
		 */
		@NotNull
		@Contract(pure = true)
		public CommandSketch.ParameterSketch.ValueSketch getValueSketch() {
			return this.value;
		}

		/**
		 * An optional sketch representing the key part of the parameter.
		 *
		 * @author LSafer
		 * @version 0.2.0
		 * @since 0.2.0 ~2021.05.23
		 */
		public class KeySketch extends Sketch {
			@SuppressWarnings("JavaDoc")
			private static final long serialVersionUID = -337916045025377470L;

			/**
			 * Return the command sketch.
			 *
			 * @return the sketch of the command this sketch is in.
			 * @since 0.2.0 ~2021.05.20
			 */
			@NotNull
			@Contract(pure = true)
			public CommandSketch getCommandSketch() {
				return CommandSketch.this;
			}

			/**
			 * Return the parameter sketch.
			 *
			 * @return the sketch of the parameter this sketch is in.
			 * @since 0.2.0 ~2021.05.23
			 */
			@NotNull
			@Contract(pure = true)
			public ParameterSketch getParameterSketch() {
				return ParameterSketch.this;
			}
		}

		/**
		 * An optional sketch representing the value part of the parameter.
		 *
		 * @author LSafer
		 * @version 0.2.0
		 * @since 0.2.0 ~2021.05.23
		 */
		public class ValueSketch extends Sketch {
			@SuppressWarnings("JavaDoc")
			private static final long serialVersionUID = -7663526977228212038L;

			/**
			 * Return the command sketch.
			 *
			 * @return the sketch of the command this sketch is in.
			 * @since 0.2.0 ~2021.05.20
			 */
			@NotNull
			@Contract(pure = true)
			public CommandSketch getCommandSketch() {
				return CommandSketch.this;
			}

			/**
			 * Return the parameter sketch.
			 *
			 * @return the sketch of the parameter this sketch is in.
			 * @since 0.2.0 ~2021.05.23
			 */
			@NotNull
			@Contract(pure = true)
			public ParameterSketch getParameterSketch() {
				return ParameterSketch.this;
			}
		}
	}

	/**
	 * A sketch for the type of the command.
	 *
	 * @author LSafer
	 * @version 0.2.0
	 * @since 0.2.0 ~2021.05.20
	 */
	public class TypeSketch extends LiteralSketch {
		@SuppressWarnings("JavaDoc")
		private static final long serialVersionUID = 821197872627775597L;

		/**
		 * Return the command sketch.
		 *
		 * @return the sketch of the command this sketch is in.
		 * @since 0.2.0 ~2021.05.20
		 */
		@NotNull
		@Contract(pure = true)
		public CommandSketch getCommandSketch() {
			return CommandSketch.this;
		}
	}
}
