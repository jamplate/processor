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
package org.jamplate.glucose.spec.element;

import cufy.util.Node;
import org.jamplate.unit.Spec;
import org.jamplate.impl.spec.MultiSpec;
import org.jamplate.model.Sketch;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A class containing command context internal specifications.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.24
 */
public class FlowSpec extends MultiSpec {
	/**
	 * The key to access the body of a command flow.
	 *
	 * @since 0.3.0 ~2021.06.24
	 */
	@NotNull
	public static final Node.Key KEY_BODY = Sketch.component("flow:body");
	/**
	 * The key to access the end command of a command flow.
	 *
	 * @since 0.3.0 ~2021.06.24
	 */
	@NotNull
	public static final Node.Key KEY_END = Sketch.component("flow:end");
	/**
	 * The key to access the middle command of a command flow.
	 *
	 * @since 0.3.0 ~2021.06.24
	 */
	@NotNull
	public static final Node.Key KEY_MIDDLE = Sketch.component("flow:middle");
	/**
	 * The key to access the start command of a command flow.
	 *
	 * @since 0.3.0 ~2021.06.24
	 */
	@NotNull
	public static final Node.Key KEY_START = Sketch.component("flow:start");
	/**
	 * The key to access the sub command of a command flow.
	 *
	 * @since 0.3.0 ~2021.07.09
	 */
	@NotNull
	public static final Node.Key KEY_SUB = Sketch.component("flow:sub");

	/**
	 * The qualified name of this spec.
	 *
	 * @since 0.3.0 ~2021.06.24
	 */
	@NotNull
	public static final String NAME = FlowSpec.class.getSimpleName();

	/**
	 * The {@code weight} of a command context body.
	 *
	 * @since 0.3.0 ~2021.06.24
	 */
	public static final int WEIGHT = 0;
	/**
	 * The {@code weight} of a command context body.
	 *
	 * @since 0.3.0 ~2021.06.24
	 */
	public static final int WEIGHT_BODY = -1;

	/**
	 * Construct a new flow spec.
	 *
	 * @param subspecs the initial subspecs.
	 * @throws NullPointerException if the given {@code subspecs} is null.
	 * @since 0.3.0 ~2021.07.04
	 */
	public FlowSpec(@Nullable Spec @NotNull ... subspecs) {
		super(FlowSpec.NAME, subspecs);
	}
}
