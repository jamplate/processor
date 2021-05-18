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
package org.jamplate.impl.syntax.compile;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

/**
 * A utility class containing the patterns the default jamplate implementation offers.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.16
 */
public final class SyntaxPattern {
	/**
	 * A pattern matching commas.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final Pattern COMMA = Pattern.compile(",");

	/**
	 * A pattern matching closing curly brackets.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final Pattern CURLY_CLOSE = Pattern.compile("\\}");
	/**
	 * A pattern matching opening curly brackets.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final Pattern CURLY_OPEN = Pattern.compile("\\{");

	/**
	 * A pattern matching double quotes.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final Pattern DQUOTE = Pattern.compile("(?<!(?<!\\\\)\\\\)\"");

	/**
	 * A pattern matching escaped sequences.
	 *
	 * @since 0.2.0 ~2021.05.17
	 */
	@NotNull
	public static final Pattern ESCAPE = Pattern.compile("\\\\.");

	/**
	 * A pattern matching quotes.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final Pattern QUOTE = Pattern.compile("(?<!(?<!\\\\)\\\\)'");

	/**
	 * A pattern matching closing round brackets.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final Pattern ROUND_CLOSE = Pattern.compile("\\)");
	/**
	 * A pattern matching opening round brackets.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final Pattern ROUND_OPEN = Pattern.compile("\\(");

	/**
	 * A pattern matching closing square brackets.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final Pattern SQUARE_CLOSE = Pattern.compile("\\]");
	/**
	 * A pattern matching opening square brackets.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final Pattern SQUARE_OPEN = Pattern.compile("\\[");

	/**
	 * Utility classes must not be initialized.
	 *
	 * @throws AssertionError when called.
	 * @since 0.2.0 ~2021.05.16
	 */
	private SyntaxPattern() {
		throw new AssertionError("No instance for you");
	}
}
