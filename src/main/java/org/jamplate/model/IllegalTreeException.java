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

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

/**
 * An exception indicating that an illegal sketch was provided.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.14
 */
public class IllegalTreeException extends IllegalArgumentException {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -5393956633280978236L;

	/**
	 * The illegal sketch.
	 *
	 * @since 0.2.0 ~2021.05.15
	 */
	@Nullable
	protected Tree illegal;

	/**
	 * Constructs a new exception with {@code null} as its detail message. The cause is
	 * not initialized, and may subsequently be initialized by a call to {@link
	 * #initCause}.
	 *
	 * @since 0.2.0 ~2021.05.14
	 */
	public IllegalTreeException() {
	}

	/**
	 * Constructs a new exception with the specified detail message.  The cause is not
	 * initialized, and may subsequently be initialized by a call to {@link #initCause}.
	 *
	 * @param message the detail message. The detail message is saved for later retrieval
	 *                by the {@link #getMessage()} method.
	 * @since 0.2.0 ~2021.05.14
	 */
	public IllegalTreeException(@Nullable String message) {
		super(message);
	}

	/**
	 * Constructs a new exception with the specified detail message and cause.  <p>Note
	 * that the detail message associated with {@code cause} is <i>not</i> automatically
	 * incorporated in this exception's detail message.
	 *
	 * @param message the detail message (which is saved for later retrieval by the {@link
	 *                #getMessage()} method).
	 * @param cause   the cause (which is saved for later retrieval by the {@link
	 *                #getCause()} method).  (A <tt>null</tt> value is permitted, and
	 *                indicates that the cause is nonexistent or unknown.)
	 * @since 0.2.0 ~2021.05.14
	 */
	public IllegalTreeException(@Nullable String message, @Nullable Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a new exception with the specified cause and a detail message of
	 * <tt>(cause==null ? null : cause.toString())</tt> (which typically contains the
	 * class and detail message of <tt>cause</tt>). This constructor is useful for
	 * exceptions that are little more than wrappers for other throwables (for example,
	 * {@link java.security.PrivilegedActionException}).
	 *
	 * @param cause the cause (which is saved for later retrieval by the {@link
	 *              #getCause()} method).  (A <tt>null</tt> value is permitted, and
	 *              indicates that the cause is nonexistent or unknown.)
	 * @since 0.2.0 ~2021.05.14
	 */
	public IllegalTreeException(@Nullable Throwable cause) {
		super(cause);
	}

	/**
	 * Construct a new illegal sketch exception with the given {@code illegal} sketch.
	 *
	 * @param illegal the illegal sketch.
	 * @since 0.2.0 ~2021.05.15
	 */
	public IllegalTreeException(@Nullable Tree illegal) {
		this.illegal = illegal;
	}

	/**
	 * Construct a new illegal sketch exception with the given {@code illegal} sketch and
	 * the given {@code message}.
	 *
	 * @param message the message of the exception.
	 * @param illegal the illegal sketch.
	 * @since 0.2.0 ~2021.05.15
	 */
	public IllegalTreeException(@Nullable String message, @Nullable Tree illegal) {
		this(message);
		this.illegal = illegal;
	}

	/**
	 * Return the illegal sketch.
	 *
	 * @return the illegal sketch.
	 * @since 0.2.0 ~2021.05.15
	 */
	@Nullable
	@Contract(pure = true)
	public Tree getIllegalSketch() {
		return this.illegal;
	}
}
