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
package org.jamplate;

import org.jetbrains.annotations.NotNull;

/**
 * Library manifest.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.07.05
 */
public final class Jamplate {
	/**
	 * The jamplate implementation code.
	 *
	 * @since 0.3.0 ~2021.07.05
	 */
	@NotNull
	public static final String CODE = "glucose";
	/**
	 * The library version name.
	 *
	 * @since 0.3.0 ~2021.07.05
	 */
	@NotNull
	public static final String VERSION = "0.3.0";

	/**
	 * Tool classes does not need instantiating.
	 *
	 * @throws AssertionError when called.
	 * @since 0.3.0 ~2021.07.05
	 */
	private Jamplate() {
		throw new AssertionError("No instance for you!");
	}
}
