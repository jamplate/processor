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
package org.jamplate.internal.diagnostic;

import org.jetbrains.annotations.NotNull;

/**
 * A class containing message priorities constants.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.31
 */
public final class MessagePriority {
	/**
	 * The priority of a debug message.
	 * <br>
	 * For jamplate debugging.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	public static final String DEBUG = "debug";
	/**
	 * The priority of an error message.
	 * <br>
	 * Something bad, must fix.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	public static final String ERROR = "error";
	/**
	 * The priority of an info message.
	 * <br>
	 * Something totally fine and expected happened.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	public static final String INFO = "info";
	/**
	 * The priority of a note message.
	 * <br>
	 * Ok solution, but there is better.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	public static final String NOTE = "note";
	/**
	 * The priority of a warning message.
	 * <br>
	 * Something bad, but still works.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	public static final String WARNING = "warning";

	/**
	 * Utility classes must not be initialized.
	 *
	 * @throws AssertionError when called.
	 * @since 0.2.0 ~2021.05.31
	 */
	private MessagePriority() {
		throw new AssertionError("No instance for you");
	}
}
