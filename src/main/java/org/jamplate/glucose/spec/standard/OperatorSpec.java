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
package org.jamplate.glucose.spec.standard;

import cufy.util.Node;
import org.jamplate.unit.Spec;
import org.jamplate.model.Sketch;
import org.jetbrains.annotations.NotNull;

/**
 * A class containing operator context internal specifications.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.22
 */
public class OperatorSpec implements Spec {
	/**
	 * An instance of this spec.
	 *
	 * @since 0.3.0 ~2021.06.22
	 */
	@NotNull
	public static final OperatorSpec INSTANCE = new OperatorSpec();

	/**
	 * The key to get the left token of an operator context.
	 *
	 * @since 0.3.0 ~2021.06.22
	 */
	@NotNull
	public static final Node.Key KEY_LEFT = Sketch.component("operator:left");
	/**
	 * The key to get the right token of an operator context.
	 *
	 * @since 0.3.0 ~2021.06.22
	 */
	@NotNull
	public static final Node.Key KEY_RIGHT = Sketch.component("operator:right");
	/**
	 * The key to get the operator sign of an operator context.
	 *
	 * @since 0.3.0 ~2021.06.22
	 */
	@NotNull
	public static final Node.Key KEY_SIGN = Sketch.component("operator:sign");

	/**
	 * The qualified name of this spec.
	 *
	 * @since 0.3.0 ~2021.06.22
	 */
	@NotNull
	public static final String NAME = OperatorSpec.class.getSimpleName();

	/**
	 * The {@code weight} of an operator tree.
	 *
	 * @since 0.3.0 ~2021.06.22
	 */
	public static final int WEIGHT = -1;

	@NotNull
	@Override
	public String getQualifiedName() {
		return OperatorSpec.NAME;
	}
}
