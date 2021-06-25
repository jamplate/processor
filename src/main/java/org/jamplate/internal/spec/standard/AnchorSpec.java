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
package org.jamplate.internal.spec.standard;

import cufy.util.Node;
import org.jamplate.api.Spec;
import org.jamplate.model.Sketch;
import org.jetbrains.annotations.NotNull;

/**
 * Anchors specification.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.20
 */
public class AnchorSpec implements Spec {
	/**
	 * An instance of this spec.
	 *
	 * @since 0.3.0 ~2021.06.16
	 */
	@NotNull
	public static final AnchorSpec INSTANCE = new AnchorSpec();

	/**
	 * The key to access the closing anchor.
	 *
	 * @since 0.3.0 ~2021.06.20
	 */
	@NotNull
	public static final Node.Key KEY_CLOSE = Sketch.component("anchor:close");
	/**
	 * The key to access the opening anchor.
	 *
	 * @since 0.3.0 ~2021.06.20
	 */
	@NotNull
	public static final Node.Key KEY_OPEN = Sketch.component("anchor:open");

	/**
	 * The kind of a closing anchor.
	 *
	 * @since 0.3.0 ~2021.06.20
	 */
	@NotNull
	public static final String KIND_CLOSE = "anchor:close";
	/**
	 * The kind of an opening anchor.
	 *
	 * @since 0.3.0 ~2021.06.20
	 */
	@NotNull
	public static final String KIND_OPEN = "anchor:open";

	/**
	 * The qualified name of this spec.
	 *
	 * @since 0.3.0 ~2021.06.20
	 */
	@NotNull
	public static final String NAME = AnchorSpec.class.getSimpleName();

	@NotNull
	@Override
	public String getQualifiedName() {
		return AnchorSpec.NAME;
	}
}
