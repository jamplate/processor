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

import org.jamplate.api.Spec;
import org.jamplate.function.Parser;
import org.jamplate.impl.api.MultiSpec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static org.jamplate.internal.parser.NaturalMergeParser.merge;
import static org.jamplate.internal.util.Collisions.flat;
import static org.jamplate.internal.util.Functions.parser;

/**
 * A subspec supporting implementation with collision management.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.07.04
 */
public class CollisionSpec extends MultiSpec {
	/**
	 * The qualified name of this spec.
	 *
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	public static final String NAME = CollisionSpec.class.getSimpleName();

	/**
	 * Construct a new commands spec.
	 *
	 * @param subspecs the initial subspecs.
	 * @throws NullPointerException if the given {@code subspecs} is null.
	 * @since 0.3.0 ~2021.06.25
	 */
	public CollisionSpec(@Nullable Spec @NotNull ... subspecs) {
		super(CollisionSpec.NAME, subspecs);
	}

	@NotNull
	@Override
	public Parser getParser() {
		return parser(
				p -> merge(p, flat()),
				p -> super.getParser()
		);
	}
}
