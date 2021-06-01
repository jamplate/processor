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
 * An exception indicating failure on executing an instruction.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.23
 */
public class ExecutionException extends RuntimeException {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -1232544378053930237L;

	/**
	 * The tree where the failure occurred.
	 *
	 * @since 0.2.0 ~2021.05.15
	 */
	@Nullable
	protected Tree tree;

	/**
	 * Constructs a new execution exception with {@code null} as its detail message.  The
	 * cause is not initialized, and may subsequently be initialized by a call to {@link
	 * #initCause}.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	public ExecutionException() {
	}

	/**
	 * Constructs a new execution exception with the specified detail message. The cause
	 * is not initialized, and may subsequently be initialized by a call to {@link
	 * #initCause}.
	 *
	 * @param message the detail message. The detail message is saved for later retrieval
	 *                by the {@link #getMessage()} method.
	 * @since 0.2.0 ~2021.05.23
	 */
	public ExecutionException(String message) {
		super(message);
	}

	/**
	 * Constructs a new execution exception with the specified detail message and cause.
	 * <p>Note that the detail message associated with {@code cause} is <i>not</i>
	 * automatically incorporated in this runtime exception's detail message.
	 *
	 * @param message the detail message (which is saved for later retrieval by the {@link
	 *                #getMessage()} method).
	 * @param cause   the cause (which is saved for later retrieval by the {@link
	 *                #getCause()} method).  (A <tt>null</tt> value is permitted, and
	 *                indicates that the cause is nonexistent or unknown.)
	 * @since 0.2.0 ~2021.05.23
	 */
	public ExecutionException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a new execution exception with the specified cause and a detail message
	 * of
	 * <tt>(cause==null ? null : cause.toString())</tt> (which typically contains the
	 * class and detail message of
	 * <tt>cause</tt>).  This constructor is useful for runtime exceptions
	 * that are little more than wrappers for other throwables.
	 *
	 * @param cause the cause (which is saved for later retrieval by the {@link
	 *              #getCause()} method).  (A <tt>null</tt> value is permitted, and
	 *              indicates that the cause is nonexistent or unknown.)
	 * @since 0.2.0 ~2021.05.23
	 */
	public ExecutionException(Throwable cause) {
		super(cause);
	}

	/**
	 * Construct a new execution exception with the given {@code tree}.
	 *
	 * @param tree the tree where the failure occurred.
	 * @since 0.2.0 ~2021.05.23
	 */
	public ExecutionException(@Nullable Tree tree) {
		this.tree = tree;
	}

	/**
	 * Construct a new execution exception with the given {@code tree} and {@code
	 * message}.
	 *
	 * @param message the message of the exception.
	 * @param tree    the tree where the failure occurred.
	 * @since 0.2.0 ~2021.05.23
	 */
	public ExecutionException(@Nullable String message, @Nullable Tree tree) {
		super(message);
		this.tree = tree;
	}

	/**
	 * Construct a new execution exception with the given {@code tree}, {@code message}
	 * and {@code cause}.
	 *
	 * @param message the message of the exception.
	 * @param cause   the throwable that caused to the construction of this exception.
	 * @param tree    the tree where the failure occurred.
	 * @since 0.2.0 ~2021.05.23
	 */
	public ExecutionException(@Nullable String message, @Nullable Throwable cause, @Nullable Tree tree) {
		super(message, cause);
		this.tree = tree;
	}

	/**
	 * Construct a new execution exception with the given {@code tree} and {@code cause}.
	 *
	 * @param cause the throwable that caused to the construction of this exception.
	 * @param tree  the tree where the failure occurred.
	 * @since 0.2.0 ~2021.05.23
	 */
	public ExecutionException(@Nullable Throwable cause, @Nullable Tree tree) {
		super(cause);
		this.tree = tree;
	}

	/**
	 * Return the tree where the failure occurred.
	 *
	 * @return the tree where the failure occurred.
	 * @since 0.2.0 ~2021.05.23
	 */
	@Nullable
	@Contract(pure = true)
	public Tree getTree() {
		return this.tree;
	}
}
