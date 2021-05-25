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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * A sketch of an injection.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.24
 */
public class InjectionSketch extends ScopeSketch {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 8210784298896674937L;

	/**
	 * The sketch of the parameter of the injection.
	 *
	 * @since 0.2.0 ~2021.05.24
	 */
	@NotNull
	protected final ParameterSketch parameter;

	/**
	 * Construct a new injection sketch.
	 *
	 * @since 0.2.0 ~2021.05.24
	 */
	public InjectionSketch() {
		this.parameter = new ParameterSketch();
	}

	/**
	 * An internal constructor for the subclasses to be able to specify component
	 * sketches.
	 *
	 * @param openAnchor  the sketch of the opening anchor of the constructed injection.
	 * @param closeAnchor the sketch of the closing anchor of the constructed injection.
	 * @param parameter   the sketch of the parameter of the constructed injection.
	 * @throws NullPointerException if the given {@code openAnchor} or {@code closeAnchor}
	 *                              or {@code parameter} is null.
	 * @since 0.2.0 ~2021.05.20
	 */
	protected InjectionSketch(@NotNull AnchorSketch openAnchor, @NotNull AnchorSketch closeAnchor, @NotNull ParameterSketch parameter) {
		super(openAnchor, closeAnchor);
		Objects.requireNonNull(parameter, "parameter");
		this.parameter = parameter;
	}

	/**
	 * Return the sketch of the parameter passed to the injection.
	 *
	 * @return the parameter sketch.
	 * @since 0.2.0 ~2021.05.24
	 */
	@NotNull
	@Contract(pure = true)
	public ParameterSketch getParameterSketch() {
		return this.parameter;
	}

	/**
	 * A sketch for the parameter of the injection.
	 *
	 * @author LSafer
	 * @version 0.2.0
	 * @since 0.2.0 ~2021.05.24
	 */
	public class ParameterSketch extends Sketch {
		@SuppressWarnings("JavaDoc")
		private static final long serialVersionUID = 3439970977647226885L;

		/**
		 * Return the injection sketch this parameter sketch is passed to.
		 *
		 * @return the injection sketch.
		 * @since 0.2.0 ~2021.05.24
		 */
		@NotNull
		@Contract(pure = true)
		public InjectionSketch getInjectionSketch() {
			return InjectionSketch.this;
		}
	}
}
