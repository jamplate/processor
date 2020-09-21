/*
 *	Copyright 2020 Cufy
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

import java.util.Collections;

/**
 * An exception indicates that a text can not be parsed.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2020.09.20
 */
public class ParseException extends RuntimeException {
	/**
	 * Construct a new parse exception.
	 *
	 * @since 0.0.1 ~2020.09.20
	 */
	public ParseException() {
	}

	/**
	 * Construct a new parse exception with the given {@code message}.
	 *
	 * @param message detailed message about the exception.
	 * @since 0.0.1 ~2020.09.20
	 */
	public ParseException(String message) {
		super(message);
	}

	/**
	 * Construct a new parse exception with the given {@code message} for the given {@code text}.
	 *
	 * @param message detailed message about the exception.
	 * @param text    the text cause this exception.
	 * @since 0.0.1 ~2020.09.20
	 */
	public ParseException(String message, String text) {
		super(ParseException.details(message, text, 0));
	}

	/**
	 * Construct a new parse exception with the given {@code message} for the given {@code text},
	 * with a specified index where the error is.
	 *
	 * @param message detailed message about the exception.
	 * @param text    the text cause this exception.
	 * @param index   index where the error is at the text caused this exception.
	 * @since 0.0.1 ~2020.09.20
	 */
	public ParseException(String message, String text, int index) {
		super(ParseException.details(message, text, index));
	}

	/**
	 * Construct a new parse exception.
	 *
	 * @param cause the throwable caused this exception.
	 * @since 0.0.1 ~2020.09.20
	 */
	public ParseException(Throwable cause) {
		super(cause);
	}

	/**
	 * Construct a new parse exception with the given {@code message}.
	 *
	 * @param message detailed message about the exception.
	 * @param cause   the throwable caused this exception.
	 * @since 0.0.1 ~2020.09.20
	 */
	public ParseException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Construct a new parse exception with the given {@code message} for the given {@code text}.
	 *
	 * @param message detailed message about the exception.
	 * @param cause   the throwable caused this exception.
	 * @param text    the text cause this exception.
	 * @since 0.0.1 ~2020.09.20
	 */
	public ParseException(String message, Throwable cause, String text) {
		super(ParseException.details(message, text, 0), cause);
	}

	/**
	 * Construct a new parse exception with the given {@code message} for the given {@code text},
	 * with a specified index where the error is.
	 *
	 * @param message detailed message about the exception.
	 * @param cause   the throwable caused this exception.
	 * @param text    the text cause this exception.
	 * @param index   index where the error is at the text caused this exception.
	 * @since 0.0.1 ~2020.09.20
	 */
	public ParseException(String message, Throwable cause, String text, int index) {
		super(ParseException.details(message, text, index), cause);
	}

	/**
	 * Draw a detailed message from the given parameters.
	 *
	 * @param message a description of how the exception occurred.
	 * @param text    the text cause the exception.
	 * @param index   the index specifically where the error is in the cause text.
	 * @return a detailed message from the given parameters.
	 * @since 0.0.1 ~2020.09.20
	 */
	protected static String details(String message, String text, int index) {
		return message + '\n' + text + '\n' +
			   String.join(
					   "",
					   Collections.nCopies(
							   Math.max(0, index),
							   " ")
			   ) +
			   '^';
	}
}
