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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * An exception indicating a failure while compiling.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.21
 */
public class CompileException extends RuntimeException {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -4326036334250884373L;

	/**
	 * A reference to where the compilation failure occurred.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	@Nullable
	protected Tree tree;

	/**
	 * Constructs a new compile exception with {@code null} as its detail message.  The
	 * cause is not initialized, and may subsequently be initialized by a call to {@link
	 * #initCause}.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	public CompileException() {
	}

	/**
	 * Constructs a new compile exception with the specified detail message. The cause is
	 * not initialized, and may subsequently be initialized by a call to {@link
	 * #initCause}.
	 *
	 * @param message the detail message. The detail message is saved for later retrieval
	 *                by the {@link #getMessage()} method.
	 * @since 0.2.0 ~2021.05.23
	 */
	public CompileException(@Nullable String message) {
		super(message);
	}

	/**
	 * Constructs a new compile exception with the specified detail message and cause.
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
	public CompileException(@Nullable String message, @Nullable Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a new compile exception with the specified cause and a detail message
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
	public CompileException(@Nullable Throwable cause) {
		super(cause);
	}

	/**
	 * Construct a new compile exception with the given {@code tree}.
	 *
	 * @param tree a tree reference where the failure occurred.
	 * @since 0.2.0 ~2021.05.23
	 */
	public CompileException(@Nullable Tree tree) {
		Objects.requireNonNull(tree, "tree");
		this.tree = tree;
	}

	/**
	 * Construct a new compile exception with the given {@code tree} and {@code message}.
	 *
	 * @param message the message of the exception.
	 * @param tree    a tree reference where the failure occurred.
	 * @since 0.2.0 ~2021.05.23
	 */
	public CompileException(@Nullable String message, @NotNull Tree tree) {
		super(message);
		this.tree = tree;
	}

	/**
	 * Construct a new compile exception with the given {@code tree}, {@code message} and
	 * {@code cause}.
	 *
	 * @param message the message of the exception.
	 * @param cause   the throwable that caused to the construction of this exception.
	 * @param tree    a tree reference where the failure occurred.
	 * @since 0.2.0 ~2021.05.23
	 */
	public CompileException(@Nullable String message, @Nullable Throwable cause, @Nullable Tree tree) {
		super(message, cause);
		this.tree = tree;
	}

	/**
	 * Construct a new compile exception with the given {@code tree} and {@code cause}.
	 *
	 * @param cause the throwable that caused to the construction of this exception.
	 * @param tree  a tree reference where the failure occurred.
	 * @since 0.2.0 ~2021.05.23
	 */
	public CompileException(@Nullable Throwable cause, @Nullable Tree tree) {
		super(cause);
		this.tree = tree;
	}

	/**
	 * Return a reference tree where the failure occurred.
	 *
	 * @return the tree of the exception. Or {@code null} if unknown or non-existing.
	 * @since 0.2.0 ~2021.05.23
	 */
	@Nullable
	@Contract(pure = true)
	public Tree getTree() {
		return this.tree;
	}
}
