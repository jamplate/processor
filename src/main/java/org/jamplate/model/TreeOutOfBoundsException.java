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
 * An exception to indicate that a provided tree is out of the expected bounds. Usually,
 * this exception is raised when a tree is about to be put into a place where that tree's
 * reference is not contained on.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.14
 */
public class TreeOutOfBoundsException extends IllegalTreeException {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -2054859355256526113L;

	/**
	 * The tree that defines the bounds.
	 *
	 * @since 0.2.0 ~2021.05.15
	 */
	@Nullable
	protected Tree bounds;

	/**
	 * Constructs a new exception with {@code null} as its detail message. The cause is
	 * not initialized, and may subsequently be initialized by a call to {@link
	 * #initCause}.
	 *
	 * @since 0.2.0 ~2021.05.14
	 */
	public TreeOutOfBoundsException() {
	}

	/**
	 * Constructs a new exception with the specified detail message.  The cause is not
	 * initialized, and may subsequently be initialized by a call to {@link #initCause}.
	 *
	 * @param message the detail message. The detail message is saved for later retrieval
	 *                by the {@link #getMessage()} method.
	 * @since 0.2.0 ~2021.05.14
	 */
	public TreeOutOfBoundsException(@Nullable String message) {
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
	public TreeOutOfBoundsException(@Nullable String message, @Nullable Throwable cause) {
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
	public TreeOutOfBoundsException(@Nullable Throwable cause) {
		super(cause);
	}

	/**
	 * Construct a new tree out of bounds exception with the given {@code bounds} and
	 * {@code illegal} trees.
	 *
	 * @param bounds  the tree that defines the bounds.
	 * @param illegal the illegal tree.
	 * @since 0.2.0 ~2021.05.15
	 */
	@SuppressWarnings("ParameterNameDiffersFromOverriddenParameter")
	public TreeOutOfBoundsException(@Nullable Tree bounds, @Nullable Tree illegal) {
		super(bounds, illegal);
		this.bounds = bounds;
	}

	/**
	 * Construct a new tree out of bounds exception with the given {@code bounds} and
	 * {@code illegal} trees.
	 *
	 * @param message the message.
	 * @param bounds  the tree that defines the bounds.
	 * @param illegal the illegal tree.
	 * @since 0.2.0 ~2021.05.15
	 */
	@SuppressWarnings("ParameterNameDiffersFromOverriddenParameter")
	public TreeOutOfBoundsException(@Nullable String message, @Nullable Tree bounds, @Nullable Tree illegal) {
		super(message, bounds, illegal);
		this.bounds = bounds;
	}

	/**
	 * Construct a new tree out of bounds exception with the given {@code bounds} and
	 * {@code illegal} trees.
	 *
	 * @param message the message.
	 * @param cause   the throwable that caused to the construction of this exception.
	 * @param bounds  the tree that defines the bounds.
	 * @param illegal the illegal tree.
	 * @since 0.2.0 ~2021.05.15
	 */
	@SuppressWarnings("ParameterNameDiffersFromOverriddenParameter")
	public TreeOutOfBoundsException(@Nullable String message, @Nullable Throwable cause, @Nullable Tree bounds, @Nullable Tree illegal) {
		super(message, cause, bounds, illegal);
		this.bounds = bounds;
	}

	/**
	 * Construct a new tree out of bounds exception with the given {@code bounds} and
	 * {@code illegal} trees.
	 *
	 * @param cause   the throwable that caused to the construction of this exception.
	 * @param bounds  the tree that defines the bounds.
	 * @param illegal the illegal tree.
	 * @since 0.2.0 ~2021.05.15
	 */
	@SuppressWarnings("ParameterNameDiffersFromOverriddenParameter")
	public TreeOutOfBoundsException(@Nullable Throwable cause, @Nullable Tree bounds, @Nullable Tree illegal) {
		super(cause, bounds, illegal);
		this.bounds = bounds;
	}

	/**
	 * Return the tree that defines the bounds.
	 *
	 * @return the bounds tree. Or {@code null} if unknown or non-existing.
	 * @since 0.2.0 ~2021.05.15
	 */
	@Nullable
	@Contract(pure = true)
	public Tree getBoundsTree() {
		return this.bounds;
	}
}
