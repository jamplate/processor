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
package org.jamplate.impl.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * A utility class containing methods that interprets plain text into specific types.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.06.03
 */
public final class Values {
	/**
	 * Utility classes must not be initialized.
	 *
	 * @throws AssertionError when called.
	 * @since 0.2.0 ~2021.06.03
	 */
	private Values() {
		throw new AssertionError("No instance for you");
	}

	/**
	 * Return a boolean interpretation of the given {@code text}.
	 *
	 * @param text the text to get a boolean interpretation for.
	 * @return the boolean value of the given {@code text}.
	 * @throws NullPointerException if the given {@code text} is null.
	 * @since 0.2.0 ~2021.06.03
	 */
	@Contract(pure = true)
	public static boolean toBoolean(@NotNull String text) {
		Objects.requireNonNull(text, "text");
		switch (text) {
			case "":
			case "0":
			case "\0":
			case "false":
				return false;
			default:
				return true;
		}
	}

	/**
	 * Return a number interpretation of the given {@code text}.
	 *
	 * @param text the text to get a number interpretation for.
	 * @return the number value of the given {@code text}.
	 * @throws NullPointerException  if the given {@code text} is null.
	 * @throws NumberFormatException if the given {@code text} cannot be interpreted as
	 *                               number.
	 * @since 0.2.0 ~2021.06.03
	 */
	@Contract(pure = true)
	public static double toNumber(@NotNull String text) {
		Objects.requireNonNull(text, "text");
		return text.startsWith("0b") ?
			   Long.parseLong(text.substring(2), 2) :
			   text.startsWith("0x") ?
			   Long.parseLong(text.substring(2), 16) :
			   Double.parseDouble(text);
	}
}
