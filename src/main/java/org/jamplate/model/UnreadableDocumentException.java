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
package org.jamplate.model;

import java.io.IOException;

/**
 * An exception that indicates that an attempt to read an unreadable document occurred.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.15
 */
public class UnreadableDocumentException extends IOException {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 8457937376257256913L;

	/**
	 * Constructs an {@code IOException} with {@code null} as its error detail message.
	 */
	public UnreadableDocumentException() {
	}

	/**
	 * Constructs an {@code IOException} with the specified detail message.
	 *
	 * @param message The detail message (which is saved for later retrieval by the {@link
	 *                #getMessage()} method)
	 */
	public UnreadableDocumentException(String message) {
		super(message);
	}

	/**
	 * Constructs an {@code IOException} with the specified detail message and cause.
	 * <p> Note that the detail message associated with {@code cause} is
	 * <i>not</i> automatically incorporated into this exception's detail
	 * message.
	 *
	 * @param message The detail message (which is saved for later retrieval by the {@link
	 *                #getMessage()} method)
	 * @param cause   The cause (which is saved for later retrieval by the {@link
	 *                #getCause()} method).  (A null value is permitted, and indicates
	 *                that the cause is nonexistent or unknown.)
	 * @since 1.6
	 */
	public UnreadableDocumentException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs an {@code IOException} with the specified cause and a detail message of
	 * {@code (cause==null ? null : cause.toString())} (which typically contains the class
	 * and detail message of {@code cause}). This constructor is useful for IO exceptions
	 * that are little more than wrappers for other throwables.
	 *
	 * @param cause The cause (which is saved for later retrieval by the {@link
	 *              #getCause()} method).  (A null value is permitted, and indicates that
	 *              the cause is nonexistent or unknown.)
	 * @since 1.6
	 */
	public UnreadableDocumentException(Throwable cause) {
		super(cause);
	}
}
