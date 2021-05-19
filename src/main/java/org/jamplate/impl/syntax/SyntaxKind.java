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

/**
 * A utility class containing the names of the kinds the default jamplate implementation
 * offers.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.16
 */
public final class SyntaxKind {
	/**
	 * The kind for commas.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final String COMMA = "comma";

	/**
	 * The kind for curly brackets.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final String CURLY = "curly";
	/**
	 * The kind for curly brackets.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final String CURLY_CLOSE = "curly-close";
	/**
	 * The kind for curly brackets.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final String CURLY_OPEN = "curly-open";

	/**
	 * The kind for double-quotes.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final String DQUOTE = "dquote";
	/**
	 * The kind for double-quotes.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final String DQUOTE_CLOSE = "dquote-close";
	/**
	 * The kind for double-quotes.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final String DQUOTE_OPEN = "dquote-open";

	/**
	 * The kind for escaped sequences.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final String ESCAPE = "escape";

	/**
	 * The kind for quotes.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final String QUOTE = "quote";
	/**
	 * The kind for quotes.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final String QUOTE_CLOSE = "quote-close";
	/**
	 * The kind for quotes.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final String QUOTE_OPEN = "quote-open";

	/**
	 * The kind for round brackets.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final String ROUND = "round";
	/**
	 * The kind for round brackets.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final String ROUND_CLOSE = "round-close";
	/**
	 * The kind for round brackets.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final String ROUND_OPEN = "round-open";

	/**
	 * The kind for square brackets.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final String SQUARE = "square";
	/**
	 * The kind for square brackets.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final String SQUARE_CLOSE = "square-close";
	/**
	 * The kind for square brackets.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final String SQUARE_OPEN = "square-open";

	/**
	 * Utility classes must not be initialized.
	 *
	 * @throws AssertionError when called.
	 * @since 0.2.0 ~2021.05.16
	 */
	private SyntaxKind() {
		throw new AssertionError("No instance for you");
	}
}
