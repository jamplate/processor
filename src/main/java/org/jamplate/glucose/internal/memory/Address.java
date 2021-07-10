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
package org.jamplate.glucose.internal.memory;

import org.jamplate.glucose.value.VNumber;
import org.jamplate.glucose.value.VObject;
import org.jamplate.glucose.value.VText;
import org.jetbrains.annotations.NotNull;

/**
 * A class containing the addresses used by the glucose implementation.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.07.10
 */
public final class Address {
	/**
	 * An address used to store a value that evaluates to the current date.
	 * <br>
	 * The value is expected to be a {@link VText lazy text}.
	 *
	 * @since 0.3.0 ~2021.07.10
	 */
	@NotNull
	public static final String DATE = "__DATE__"; //lazy VText
	/**
	 * An address used to store a value that evaluates to the printing replacements.
	 * <br>
	 * The value is expected to be a {@link VObject constant object}.
	 *
	 * @since 0.3.0 ~2021.07.10
	 */
	@NotNull
	public static final String DEFINE = "__DEFINE__"; //const VText
	/**
	 * An address used to store a value that evaluates to the current directory full
	 * path.
	 * <br>
	 * The value is expected to be a {@link VText constant text}.
	 *
	 * @since 0.3.0 ~2021.07.10
	 */
	@NotNull
	public static final String DIR = "__DIR__"; //const VText
	/**
	 * An address used to store a value that evaluates to the current file name.
	 * <br>
	 * The value is expected to be a {@link VText constant text}.
	 *
	 * @since 0.3.0 ~2021.07.10
	 */
	@NotNull
	public static final String FILE = "__FILE__"; //const VText
	/**
	 * An address used to store a value that evaluates to the working jamplate flavor.
	 * <br>
	 * The value is expected to be a {@link VText constant text}.
	 *
	 * @since 0.3.0 ~2021.07.10
	 */
	@NotNull
	public static final String FLAVOR = "__FLAVOR__"; //const VText
	/**
	 * An address used to store a value that evaluates to the glucose implementation
	 * version.
	 * <br>
	 * The value is expected to be a {@link VText constant text}.
	 *
	 * @since 0.3.0 ~2021.07.10
	 */
	@NotNull
	public static final String GLUCOSE = "__GLUCOSE__"; //const VText
	/**
	 * An address used to store a value that evaluates to the jamplate version.
	 * <br>
	 * The value is expected to be a {@link VText constant text}.
	 *
	 * @since 0.3.0 ~2021.07.10
	 */
	@NotNull
	public static final String JAMPLATE = "__JAMPLATE__"; //const VText
	/**
	 * An address used to store a value that evaluates to the current line number.
	 * <br>
	 * The value is expected to be a {@link VNumber constant number}.
	 *
	 * @since 0.3.0 ~2021.07.10
	 */
	@NotNull
	public static final String LINE = "__LINE__"; //const VNumber
	/**
	 * An address used to store a value that evaluates to the user set output directory.
	 * <br>
	 * The value is expected to be a {@link VText constant text}.
	 *
	 * @since 0.3.0 ~2021.07.10
	 */
	@NotNull
	public static final String OUTPUT = "__OUTPUT__"; //const VText
	/**
	 * An address used to store a value that evaluates to the current file full path.
	 * <br>
	 * The value is expected to be a {@link VText constant text}.
	 *
	 * @since 0.3.0 ~2021.07.10
	 */
	@NotNull
	public static final String PATH = "__PATH__"; //const VText
	/**
	 * An address used to store a value that evaluates to the user set project directory.
	 * <br>
	 * The value is expected to be a {@link VText constant text}.
	 *
	 * @since 0.3.0 ~2021.07.10
	 */
	@NotNull
	public static final String PROJECT = "__PROJECT__"; //const VText
	/**
	 * An address used to store a value that evaluates to a random number.
	 * <br>
	 * The value is expected to be a {@link VNumber lazy number}.
	 *
	 * @since 0.3.0 ~2021.07.10
	 */
	@NotNull
	public static final String RANDOM = "__RANDOM__"; //lazy VNumber
	/**
	 * An address used to store a value that evaluates to the current time.
	 * <br>
	 * The value is expected to be a {@link VText lazy text}.
	 *
	 * @since 0.3.0 ~2021.07.10
	 */
	@NotNull
	public static final String TIME = "__TIME__"; //lazy VText

	/**
	 * Tool classes does not need instantiating.
	 *
	 * @throws AssertionError when called.
	 * @since 0.3.0 ~2021.07.10
	 */
	private Address() {
		throw new AssertionError("No instance for you!");
	}
}
