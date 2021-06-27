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
package org.jamplate.spec.standard;

import cufy.util.Node;
import org.jamplate.api.Spec;
import org.jamplate.model.Sketch;
import org.jetbrains.annotations.NotNull;

/**
 * A class containing command context internal specifications.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.24
 */
public class ContextSpec implements Spec {
	/**
	 * An instance of this spec.
	 *
	 * @since 0.3.0 ~2021.06.24
	 */
	@NotNull
	public static final ContextSpec INSTANCE = new ContextSpec();

	/**
	 * The key to access the body of a command context.
	 *
	 * @since 0.3.0 ~2021.06.24
	 */
	@NotNull
	public static final Node.Key KEY_BODY = Sketch.component("context:body");
	/**
	 * The key to access the end command of a command context.
	 *
	 * @since 0.3.0 ~2021.06.24
	 */
	@NotNull
	public static final Node.Key KEY_END = Sketch.component("context:end");
	/**
	 * The key to access the middle command of a command context.
	 *
	 * @since 0.3.0 ~2021.06.24
	 */
	@NotNull
	public static final Node.Key KEY_MIDDLE = Sketch.component("context:middle");
	/**
	 * The key to access the start command of a command context.
	 *
	 * @since 0.3.0 ~2021.06.24
	 */
	@NotNull
	public static final Node.Key KEY_START = Sketch.component("context:start");

	/**
	 * The qualified name of this spec.
	 *
	 * @since 0.3.0 ~2021.06.24
	 */
	@NotNull
	public static final String NAME = ContextSpec.class.getSimpleName();

	/**
	 * The z-index of a command context body.
	 *
	 * @since 0.3.0 ~2021.06.24
	 */
	public static final int Z_INDEX = 0;
	/**
	 * The z-index of a command context body.
	 *
	 * @since 0.3.0 ~2021.06.24
	 */
	public static final int Z_INDEX_BODY = -1;

	@NotNull
	@Override
	public String getQualifiedName() {
		return ContextSpec.NAME;
	}
}
