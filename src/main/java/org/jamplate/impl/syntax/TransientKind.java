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
 * A utility class containing the kinds for the sketches that to be elements.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.19
 */
public final class TransientKind {
	/**
	 * The kind of a single-line command.
	 *
	 * @since 0.2.0 ~2021.05.19
	 */
	@NotNull
	public static final String COMMAND = "command";
	/**
	 * The kind of the closing anchor of a single-line command.
	 *
	 * @since 0.2.0 ~2021.05.19
	 */
	@NotNull
	public static final String COMMAND_CLOSE = "command-close";
	/**
	 * The kind of the opening anchor of a single-line command.
	 *
	 * @since 0.2.0 ~2021.05.19
	 */
	@NotNull
	public static final String COMMAND_OPEN = "command-open";
	/**
	 * The kind of the parameter of a single-line command.
	 *
	 * @since 0.2.0 ~2021.05.20
	 */
	@NotNull
	public static final String COMMAND_PARAMETER = "command-parameter";
	/**
	 * The kind of the type of a single-line command.
	 *
	 * @since 0.2.0 ~2021.05.20
	 */
	@NotNull
	public static final String COMMAND_TYPE = "command-type";

	/**
	 * The kind of comment blocks.
	 *
	 * @since 0.2.0 ~2021.05.19
	 */
	@NotNull
	public static final String COMMENT_BLOCK = "comment-block";
	/**
	 * The kind of the closing anchor of comment blocks.
	 *
	 * @since 0.2.0 ~2021.05.19
	 */
	@NotNull
	public static final String COMMENT_BLOCK_CLOSE = "comment-block-close";
	/**
	 * The kind of the opening anchor of comment blocks.
	 *
	 * @since 0.2.0 ~2021.05.19
	 */
	@NotNull
	public static final String COMMENT_BLOCK_OPEN = "comment-block-open";

	/**
	 * The kind of commented lines.
	 *
	 * @since 0.2.0 ~2021.05.19
	 */
	@NotNull
	public static final String COMMENT_LINE = "comment-line";
	/**
	 * The kind of the closing anchor of commented lines.
	 *
	 * @since 0.2.0 ~2021.05.19
	 */
	@NotNull
	public static final String COMMENT_LINE_CLOSE = "comment-line-close";
	/**
	 * The kind of the opening anchor of commented lines.
	 *
	 * @since 0.2.0 ~2021.05.19
	 */
	@NotNull
	public static final String COMMENT_LINE_OPEN = "comment-line-open";

	/**
	 * The kind of an injection sequence.
	 *
	 * @since 0.2.0 ~2021.05.19
	 */
	@NotNull
	public static final String INJECTION = "injection";
	/**
	 * The kind of the closing anchor of an injection.
	 *
	 * @since 0.2.0 ~2021.05.19
	 */
	@NotNull
	public static final String INJECTION_CLOSE = "injection-close";
	/**
	 * The kind of the opening anchor of an injection.
	 *
	 * @since 0.2.0 ~2021.05.19
	 */
	@NotNull
	public static final String INJECTION_OPEN = "injection-open";

	/**
	 * Utility classes must not be initialized.
	 *
	 * @throws AssertionError when called.
	 * @since 0.2.0 ~2021.05.19
	 */
	private TransientKind() {
		throw new AssertionError("No instance for you");
	}
}
