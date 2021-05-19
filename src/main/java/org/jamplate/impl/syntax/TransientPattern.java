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
package org.jamplate.impl.syntax;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

/**
 * A utility class containing the patterns of the components that to be elements.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.19
 */
public final class TransientPattern {
	/**
	 * A pattern matching the ending anchor of a single-line command.
	 *
	 * @since 0.2.0 ~2021.05.19
	 */
	@NotNull
	public static final Pattern COMMAND_CLOSE = Pattern.compile("(?=[\r\n]|$)");
	/**
	 * A pattern matching the opening anchor of a single-line command.
	 *
	 * @since 0.2.0 ~2021.05.19
	 */
	@NotNull
	public static final Pattern COMMAND_OPEN = Pattern.compile("(?<=^|[\n\r])#");

	/**
	 * A pattern matching the ending anchor of an injection sequence.
	 *
	 * @since 0.2.0 ~2021.05.19
	 */
	@NotNull
	public static final Pattern INJECTION_CLOSE = Pattern.compile("\\}#");
	/**
	 * A pattern matching the opening anchor of an injection sequence.
	 *
	 * @since 0.2.0 ~2021.05.19
	 */
	@NotNull
	public static final Pattern INJECTION_OPEN = Pattern.compile("#\\{");

	/**
	 * Utility classes must not be initialized.
	 *
	 * @throws AssertionError when called.
	 * @since 0.2.0 ~2021.05.19
	 */
	private TransientPattern() {
		throw new AssertionError("No instance for you");
	}
}
