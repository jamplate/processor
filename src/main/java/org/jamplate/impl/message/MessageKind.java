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
package org.jamplate.impl.message;

import org.jetbrains.annotations.NotNull;

/**
 * A class containing message kinds constants.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.31
 */
public final class MessageKind {
	/**
	 * A compiling exception.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	public static final String COMPILE = "compile";
	/**
	 * An execution exception.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	public static final String EXECUTION = "execution";

	/**
	 * Utility classes must not be initialized.
	 *
	 * @throws AssertionError when called.
	 * @since 0.2.0 ~2021.05.31
	 */
	private MessageKind() {
		throw new AssertionError("No instance for you");
	}
}
