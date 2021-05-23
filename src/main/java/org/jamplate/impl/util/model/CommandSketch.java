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
	protected final Parameter parameter;

	/**
	 * The type of this command.
	 *
	 * @since 0.2.0 ~2021.05.20
	 */
	@NotNull
	protected final Type type;

	/**
	 * Construct a new command sketch.
	 *
	 * @since 0.2.0 ~2021.05.20
	 */
	public CommandSketch() {
		this.type = new Type();
		this.parameter = new Parameter();
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
		this.type = new Type();
		this.parameter = new Parameter();
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
	protected CommandSketch(@NotNull Anchor openAnchor, @NotNull Anchor closeAnchor, @NotNull Type type, @NotNull Parameter parameter) {
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
	public Parameter getParameter() {
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
	public Type getType() {
		return this.type;
	}

	/**
	 * A sketch for the parameter passed to the command.
	 *
	 * @author LSafer
	 * @version 0.2.0
	 * @since 0.2.0 ~2021.05.20
	 */
	public class Parameter extends Sketch {
		@SuppressWarnings("JavaDoc")
		private static final long serialVersionUID = -300019613180151971L;

		/**
		 * Return the command sketch.
		 *
		 * @return the sketch of the command this sketch is in.
		 * @since 0.2.0 ~2021.05.20
		 */
		@NotNull
		@Contract(pure = true)
		public CommandSketch getCommand() {
			return CommandSketch.this;
		}
	}

	/**
	 * A sketch for the type of the command.
	 *
	 * @author LSafer
	 * @version 0.2.0
	 * @since 0.2.0 ~2021.05.20
	 */
	public class Type extends LiteralSketch {
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
		public CommandSketch getCommand() {
			return CommandSketch.this;
		}
	}
}
