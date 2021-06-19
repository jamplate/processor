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
package org.jamplate.impl;

import org.jetbrains.annotations.NotNull;

/**
 * A class containing common meta keys.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.06.03
 */
@Deprecated
public final class Meta {
	/**
	 * The key for the memory default allocations. The stored value must be of type {@link
	 * java.util.Map}.
	 *
	 * @since 0.2.0 ~2021.06.03
	 */
	@NotNull
	public static final String MEMORY = "memory";

	/**
	 * The key for the output directory.
	 *
	 * @since 0.2.0 ~2021.06.03
	 */
	@NotNull
	public static final String OUTPUT = "output";

	/**
	 * The key for the project directory.
	 *
	 * @since 0.2.0 ~2021.06.03
	 */
	@NotNull
	public static final String PROJECT = "project";

	/**
	 * Utility classes must not be initialized.
	 *
	 * @throws AssertionError when called.
	 * @since 0.2.0 ~2021.06.03
	 */
	private Meta() {
		throw new AssertionError("No instance for you");
	}
}
