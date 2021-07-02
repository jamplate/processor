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
 * A class containing extension context internal specifications.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.23
 */
public class ExtensionSpec implements Spec {
	/**
	 * An instance of this spec.
	 *
	 * @since 0.3.0 ~2021.06.23
	 */
	@NotNull
	public static final ExtensionSpec INSTANCE = new ExtensionSpec();

	/**
	 * The key to get the extension sign of an extension context.
	 *
	 * @since 0.3.0 ~2021.06.23
	 */
	@NotNull
	public static final Node.Key KEY_SIGN = Sketch.component("extension:sign");
	/**
	 * The key to get the targeted token by the extension.
	 *
	 * @since 0.3.0 ~2021.06.23
	 */
	@NotNull
	public static final Node.Key KEY_TARGET = Sketch.component("extension:target");

	/**
	 * The qualified name of this spec.
	 *
	 * @since 0.3.0 ~2021.06.23
	 */
	@NotNull
	public static final String NAME = ExtensionSpec.class.getSimpleName();

	/**
	 * The {@code weight} of an extension tree.
	 *
	 * @since 0.3.0 ~2021.06.23
	 */
	public static final int WEIGHT = 0;

	@NotNull
	@Override
	public String getQualifiedName() {
		return ExtensionSpec.NAME;
	}
}
