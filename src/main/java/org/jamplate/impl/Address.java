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
 * Commonly used memory addresses.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.23
 */
public final class Address {
	/**
	 * An address used to store the date at the time of the execution.
	 *
	 * @since 0.2.0 ~2021.06.02
	 */
	@NotNull
	public static final String DATE = "__DATE__";

	/**
	 * An address used to allocate a json string containing the sequences to be replaced
	 * and their replacement.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	@NotNull
	public static final String DEFINE = "__DEFINE__";

	/**
	 * An address used to allocate the current directory.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	public static final String DIR = "__DIR__";

	/**
	 * An address used to allocate the current file name.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	public static final String FILE = "__FILE__";

	/**
	 * The address used to allocate the jamplate version.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	public static final String JAMPLATE = "__JAMPLATE__";

	/**
	 * An address used to allocate the current line.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	public static final String LINE = "__LINE__";

	/**
	 * An address used to store the output directory.
	 *
	 * @since 0.2.0 ~2021.06.03
	 */
	@NotNull
	public static final String OUTPUT = "__OUTPUT__";

	/**
	 * An address used to allocate the current path.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	public static final String PATH = "__PATH__";

	/**
	 * An address used to store the project directory.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	public static final String PROJECT = "__PROJECT__";

	/**
	 * An address used to store the time at the time of the execution.
	 *
	 * @since 0.2.0 ~2021.06.02
	 */
	@NotNull
	public static final String TIME = "__TIME__";

	/**
	 * Utility classes must not be initialized.
	 *
	 * @throws AssertionError when called.
	 * @since 0.2.0 ~2021.05.23
	 */
	private Address() {
		throw new AssertionError("No instance for you");
	}
}
