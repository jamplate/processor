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
package org.jamplate.impl.diagnostic;

import org.jamplate.diagnostic.Message;
import org.jamplate.memory.Memory;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;

/**
 * A basic implementation of the interface {@link Message}.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.31
 */
public class MessageImpl implements Message {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 8947009843194729781L;

	/**
	 * The critical points.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	protected final Tree @NotNull [] criticalPoints;
	/**
	 * The error kind.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	protected final String errorKind;
	/**
	 * The exception.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@Nullable
	protected final Throwable exception;
	/**
	 * True, if the error is fetal.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	protected final boolean fetal;
	/**
	 * The message phrase.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	protected final String messagePhrase;
	/**
	 * The message priority.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	protected final String priority;
	/**
	 * The stack trace.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	protected final Tree @NotNull [] stackTrace;

	/**
	 * Construct a new message.
	 *
	 * @param exception      the exception the construction will represent.
	 * @param priority       the priority of the constructed message.
	 * @param errorKind      the kind of the error.
	 * @param fetal          true, if the error is a fetal error.
	 * @param criticalPoints the critical points where the exception concurred.
	 * @throws NullPointerException if the given {@code exception} or {@code priority} or
	 *                              {@code errorKind} or {@code criticalPoints} is null.
	 * @since 0.2.0 ~2021.05.31
	 */
	public MessageImpl(
			@NotNull Throwable exception,
			@NotNull String priority,
			@NotNull String errorKind,
			boolean fetal,
			@Nullable Tree @NotNull ... criticalPoints
	) {
		Objects.requireNonNull(exception, "exception");
		Objects.requireNonNull(priority, "priority");
		Objects.requireNonNull(errorKind, "errorKind");
		Objects.requireNonNull(criticalPoints, "criticalPoints");
		this.exception = exception;
		this.messagePhrase = exception.getClass() +
							 ": " +
							 exception.getMessage();
		//noinspection ZeroLengthArrayAllocation
		this.stackTrace = new Tree[0];
		this.priority = priority;
		this.errorKind = errorKind;
		this.fetal = fetal;
		this.criticalPoints = Arrays
				.stream(criticalPoints)
				.filter(Objects::nonNull)
				.toArray(Tree[]::new);
	}

	/**
	 * Construct a new message.
	 *
	 * @param messagePhrase  the message phrase of the constructed message.
	 * @param priority       the priority of the constructed message.
	 * @param errorKind      the kind of the error.
	 * @param fetal          true, if the error is a fetal error.
	 * @param criticalPoints the critical points where the exception concurred.
	 * @throws NullPointerException if the given {@code messagePhrase} or {@code priority}
	 *                              or {@code errorKind} or {@code criticalPoints} is
	 *                              null.
	 * @since 0.2.0 ~2021.05.31
	 */
	public MessageImpl(
			@NotNull String messagePhrase,
			@NotNull String priority,
			@NotNull String errorKind,
			boolean fetal,
			@Nullable Tree @NotNull ... criticalPoints
	) {
		Objects.requireNonNull(messagePhrase, "messagePhrase");
		Objects.requireNonNull(priority, "priority");
		Objects.requireNonNull(errorKind, "errorKind");
		Objects.requireNonNull(criticalPoints, "criticalPoints");
		this.exception = null;
		this.messagePhrase = messagePhrase;
		//noinspection ZeroLengthArrayAllocation
		this.stackTrace = new Tree[0];
		this.priority = priority;
		this.errorKind = errorKind;
		this.fetal = fetal;
		this.criticalPoints = Arrays
				.stream(criticalPoints)
				.filter(Objects::nonNull)
				.toArray(Tree[]::new);
	}

	/**
	 * Construct a new message.
	 *
	 * @param exception      the exception the construction will represent.
	 * @param memory         the memory at the time of the occurrence of the error.
	 * @param priority       the priority of the constructed message.
	 * @param errorKind      the kind of the error.
	 * @param fetal          true, if the error is a fetal error.
	 * @param criticalPoints the critical points where the exception concurred.
	 * @throws NullPointerException if the given {@code exception} or {@code memory} or
	 *                              {@code priority} or {@code errorKind} or {@code
	 *                              criticalPoints} is null.
	 * @since 0.2.0 ~2021.05.31
	 */
	@SuppressWarnings("ConstructorWithTooManyParameters")
	public MessageImpl(
			@NotNull Throwable exception,
			@NotNull Memory memory,
			@NotNull String priority,
			@NotNull String errorKind,
			boolean fetal,
			@Nullable Tree @NotNull ... criticalPoints
	) {
		Objects.requireNonNull(exception, "exception");
		Objects.requireNonNull(memory, "memory");
		Objects.requireNonNull(priority, "priority");
		Objects.requireNonNull(errorKind, "errorKind");
		Objects.requireNonNull(criticalPoints, "criticalPoints");
		this.exception = exception;
		this.messagePhrase = exception.getClass() +
							 ": " +
							 exception.getMessage();
		this.stackTrace = memory.getStackTrace();
		this.priority = priority;
		this.errorKind = errorKind;
		this.fetal = fetal;
		this.criticalPoints = Arrays
				.stream(criticalPoints)
				.filter(Objects::nonNull)
				.toArray(Tree[]::new);
	}

	/**
	 * Construct a new message.
	 *
	 * @param messagePhrase  the message phrase of the constructed message.
	 * @param memory         the memory at the time of the occurrence of the error.
	 * @param priority       the priority of the constructed message.
	 * @param errorKind      the kind of the error.
	 * @param fetal          true, if the error is a fetal error.
	 * @param criticalPoints the critical points where the exception concurred.
	 * @throws NullPointerException if the given {@code messagePhrase} or {@code memory}
	 *                              or {@code priority} or {@code errorKind} or {@code
	 *                              criticalPoints} is null.
	 * @since 0.2.0 ~2021.05.31
	 */
	@SuppressWarnings("ConstructorWithTooManyParameters")
	public MessageImpl(
			@NotNull String messagePhrase,
			@NotNull Memory memory,
			@NotNull String priority,
			@NotNull String errorKind,
			boolean fetal,
			@Nullable Tree @NotNull ... criticalPoints
	) {
		Objects.requireNonNull(messagePhrase, "messagePhrase");
		Objects.requireNonNull(memory, "memory");
		Objects.requireNonNull(priority, "priority");
		Objects.requireNonNull(errorKind, "errorKind");
		Objects.requireNonNull(criticalPoints, "criticalPoints");
		this.exception = null;
		this.messagePhrase = messagePhrase;
		this.stackTrace = memory.getStackTrace();
		this.priority = priority;
		this.errorKind = errorKind;
		this.fetal = fetal;
		this.criticalPoints = Arrays
				.stream(criticalPoints)
				.filter(Objects::nonNull)
				.toArray(Tree[]::new);
	}

	@NotNull
	@Override
	public Tree @NotNull [] getCriticalPoints() {
		return this.criticalPoints.clone();
	}

	@NotNull
	@Override
	public String getErrorKind() {
		return this.errorKind;
	}

	@Nullable
	@Override
	public Throwable getException() {
		return this.exception;
	}

	@NotNull
	@Override
	public String getMessagePhrase() {
		return this.messagePhrase;
	}

	@NotNull
	@Override
	public String getPriority() {
		return this.priority;
	}

	@NotNull
	@Override
	public Tree @NotNull [] getStackTrace() {
		return this.stackTrace.clone();
	}

	@Override
	public boolean isFetal() {
		return this.fetal;
	}
}
