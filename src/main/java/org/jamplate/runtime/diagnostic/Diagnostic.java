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
package org.jamplate.runtime.diagnostic;

import org.jamplate.model.reference.Reference;

import java.io.PrintStream;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Objects;

/**
 * Diagnostics Manager class. All managed by thread locals.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.01.17
 */
public final class Diagnostic {
	/**
	 * A thread-local containing the errors occurred by a thread. (non-null, cheat
	 * checked)
	 *
	 * @since 0.2.0 ~2021.01.17
	 */
	private static final ThreadLocal<LinkedList<DiagnosticMessage>> messages = ThreadLocal.withInitial(LinkedList::new);

	/**
	 * This is a utility class and should not be instantiated.
	 *
	 * @throws AssertionError when called.
	 * @since 0.2.0 ~2021.01.17
	 */
	private Diagnostic() {
		throw new AssertionError("No instance for you!");
	}

	/**
	 * The given {@code message} will be printed whenever the diagnostic user decided to
	 * print it.
	 *
	 * @param message the message to be printed.
	 * @throws NullPointerException if the given {@code message} is null.
	 * @since 0.2.0 ~2021.01.17
	 */
	public static void append(DiagnosticMessage message) {
		Objects.requireNonNull(message, "message");
		Diagnostic.messages.get()
				.addLast(message);
	}

	/**
	 * Print a debug message.
	 *
	 * @param references the references caused the message.
	 * @throws NullPointerException if the given {@code references} is null.
	 * @since 0.2.0 ~2021.01.17
	 */
	public static void appendDebug(Reference... references) {
		Objects.requireNonNull(references, "references");
		Diagnostic.append(new DiagnosticMessage(
				DiagnosticKind.DEBUG,
				references
		));
	}

	/**
	 * Print a debug message.
	 *
	 * @param references the references caused the message.
	 * @param title      the title of the message.
	 * @throws NullPointerException if the given {@code references} or {@code title} is
	 *                              null.
	 * @since 0.2.0 ~2021.01.17
	 */
	public static void appendDebug(String title, Reference... references) {
		Objects.requireNonNull(title, "title");
		Objects.requireNonNull(references, "references");
		Diagnostic.append(new DiagnosticMessage(
				DiagnosticKind.DEBUG,
				title,
				references
		));
	}

	/**
	 * Print a debug message.
	 *
	 * @param references the references caused the message.
	 * @param title      the title of the message.
	 * @param details    the message details.
	 * @throws NullPointerException if the given {@code references} or {@code title} or
	 *                              {@code details} is null.
	 * @since 0.2.0 ~2021.01.17
	 */
	public static void appendDebug(String title, String details, Reference... references) {
		Objects.requireNonNull(title, "title");
		Objects.requireNonNull(details, "details");
		Objects.requireNonNull(references, "references");
		Diagnostic.append(new DiagnosticMessage(
				DiagnosticKind.DEBUG,
				title,
				details,
				references
		));
	}

	/**
	 * Print a error message.
	 *
	 * @param references the references caused the message.
	 * @throws NullPointerException if the given {@code references} is null.
	 * @since 0.2.0 ~2021.01.17
	 */
	public static void appendError(Reference... references) {
		Objects.requireNonNull(references, "references");
		Diagnostic.append(new DiagnosticMessage(
				DiagnosticKind.ERROR,
				references
		));
	}

	/**
	 * Print an error message.
	 *
	 * @param references the references caused the message.
	 * @param title      the title of the message.
	 * @throws NullPointerException if the given {@code references} or {@code title} is
	 *                              null.
	 * @since 0.2.0 ~2021.01.17
	 */
	public static void appendError(String title, Reference... references) {
		Objects.requireNonNull(title, "title");
		Objects.requireNonNull(references, "references");
		Diagnostic.append(new DiagnosticMessage(
				DiagnosticKind.ERROR,
				title,
				references
		));
	}

	/**
	 * Print an error message.
	 *
	 * @param references the references caused the message.
	 * @param title      the title of the message.
	 * @param details    the message details.
	 * @throws NullPointerException if the given {@code references} or {@code title} or
	 *                              {@code details} is null.
	 * @since 0.2.0 ~2021.01.17
	 */
	public static void appendError(String title, String details, Reference... references) {
		Objects.requireNonNull(title, "title");
		Objects.requireNonNull(details, "details");
		Objects.requireNonNull(references, "references");
		Diagnostic.append(new DiagnosticMessage(
				DiagnosticKind.ERROR,
				title,
				details,
				references
		));
	}

	/**
	 * Print a note message.
	 *
	 * @param references the references caused the message.
	 * @throws NullPointerException if the given {@code references} is null.
	 * @since 0.2.0 ~2021.01.17
	 */
	public static void appendNote(Reference... references) {
		Objects.requireNonNull(references, "references");
		Diagnostic.append(new DiagnosticMessage(
				DiagnosticKind.NOTE,
				references
		));
	}

	/**
	 * Print a note message.
	 *
	 * @param references the references caused the message.
	 * @param title      the title of the message.
	 * @throws NullPointerException if the given {@code references} or {@code title} is
	 *                              null.
	 * @since 0.2.0 ~2021.01.17
	 */
	public static void appendNote(String title, Reference... references) {
		Objects.requireNonNull(title, "title");
		Objects.requireNonNull(references, "references");
		Diagnostic.append(new DiagnosticMessage(
				DiagnosticKind.NOTE,
				title,
				references
		));
	}

	/**
	 * Print a note message.
	 *
	 * @param references the references caused the message.
	 * @param title      the title of the message.
	 * @param details    the message details.
	 * @throws NullPointerException if the given {@code references} or {@code title} or
	 *                              {@code details} is null.
	 * @since 0.2.0 ~2021.01.17
	 */
	public static void appendNote(String title, String details, Reference... references) {
		Objects.requireNonNull(title, "title");
		Objects.requireNonNull(details, "details");
		Objects.requireNonNull(references, "references");
		Diagnostic.append(new DiagnosticMessage(
				DiagnosticKind.NOTE,
				title,
				details,
				references
		));
	}

	/**
	 * Print a progress message.
	 *
	 * @param references the references caused the message.
	 * @throws NullPointerException if the given {@code references} is null.
	 * @since 0.2.0 ~2021.01.24
	 */
	public static void appendProgress(Reference... references) {
		Objects.requireNonNull(references, "references");
		Diagnostic.append(new DiagnosticMessage(
				DiagnosticKind.PROGRESS,
				references
		));
	}

	/**
	 * Print a progress message.
	 *
	 * @param references the references caused the message.
	 * @param title      the title of the message.
	 * @throws NullPointerException if the given {@code references} or {@code title} is
	 *                              null.
	 * @since 0.2.0 ~2021.01.24
	 */
	public static void appendProgress(String title, Reference... references) {
		Objects.requireNonNull(title, "title");
		Objects.requireNonNull(references, "references");
		Diagnostic.append(new DiagnosticMessage(
				DiagnosticKind.PROGRESS,
				title,
				references
		));
	}

	/**
	 * Print a progress message.
	 *
	 * @param references the references caused the message.
	 * @param title      the title of the message.
	 * @param details    the message details.
	 * @throws NullPointerException if the given {@code references} or {@code title} or
	 *                              {@code details} is null.
	 * @since 0.2.0 ~2021.01.24
	 */
	public static void appendProgress(String title, String details, Reference... references) {
		Objects.requireNonNull(title, "title");
		Objects.requireNonNull(details, "details");
		Objects.requireNonNull(references, "references");
		Diagnostic.append(new DiagnosticMessage(
				DiagnosticKind.PROGRESS,
				title,
				details,
				references
		));
	}

	/**
	 * Print a warning message.
	 *
	 * @param references the references caused the message.
	 * @throws NullPointerException if the given {@code references} is null.
	 * @since 0.2.0 ~2021.01.17
	 */
	public static void appendWarning(Reference... references) {
		Objects.requireNonNull(references, "references");
		Diagnostic.append(new DiagnosticMessage(
				DiagnosticKind.WARNING,
				references
		));
	}

	/**
	 * Print a warning message.
	 *
	 * @param references the references caused the message.
	 * @param title      the title of the message.
	 * @throws NullPointerException if the given {@code references} or {@code title} is
	 *                              null.
	 * @since 0.2.0 ~2021.01.17
	 */
	public static void appendWarning(String title, Reference... references) {
		Objects.requireNonNull(title, "title");
		Objects.requireNonNull(references, "references");
		Diagnostic.append(new DiagnosticMessage(
				DiagnosticKind.WARNING,
				title,
				references
		));
	}

	/**
	 * Print a warning message.
	 *
	 * @param references the references caused the message.
	 * @param title      the title of the message.
	 * @param details    the message details.
	 * @throws NullPointerException if the given {@code references} or {@code title} or
	 *                              {@code details} is null.
	 * @since 0.2.0 ~2021.01.17
	 */
	public static void appendWarning(String title, String details, Reference... references) {
		Objects.requireNonNull(title, "title");
		Objects.requireNonNull(details, "details");
		Objects.requireNonNull(references, "references");
		Diagnostic.append(new DiagnosticMessage(
				DiagnosticKind.WARNING,
				title,
				details,
				references
		));
	}

	/**
	 * Capture the current message stack printed by the caller thread.
	 *
	 * @return an array of the non-polled messages printed by the caller thread.
	 * @since 0.2.0 ~2021.01.30
	 */
	public static DiagnosticMessage[] capture() {
		//noinspection ZeroLengthArrayAllocation
		return Diagnostic.messages.get()
				.toArray(new DiagnosticMessage[0]);
	}

	/**
	 * Clear the message stack.
	 *
	 * @since 0.2.0 ~2021.02.10
	 */
	public static void clear() {
		Diagnostic.messages.get()
				.clear();
	}

	/**
	 * Return but not remove the last message printed by the caller thread.
	 *
	 * @return the last message printed. or null if no such message.
	 * @since 0.2.0 ~2021.01.28
	 */
	public static DiagnosticMessage peek() {
		return Diagnostic.messages.get()
				.peekLast();
	}

	/**
	 * Returns and removes the last message printed by the caller thread. (making the last
	 * message the one before it)
	 *
	 * @return the last message printed.
	 * @since 0.2.0 ~2021.01.17
	 */
	public static DiagnosticMessage poll() {
		return Diagnostic.messages.get()
				.pollLast();
	}

	/**
	 * Print and remove the last message printed by the caller thread to the default
	 * print-stream.
	 *
	 * @since 0.2.0 ~2021.01.30
	 */
	public static void print() {
		Deque<DiagnosticMessage> messages = Diagnostic.messages.get();

		if (!messages.isEmpty())
			messages.pollLast()
					.print();
	}

	/**
	 * Print and remove the last message printed by the caller thread to the given {@code
	 * stream}.
	 *
	 * @param stream the print stream to print the last message to.
	 * @throws NullPointerException if the given {@code stream} is null.
	 * @since 0.2.0 ~2021.01.30
	 */
	public static void print(PrintStream stream) {
		Objects.requireNonNull(stream, "stream");
		Deque<DiagnosticMessage> messages = Diagnostic.messages.get();

		if (!messages.isEmpty())
			messages.pollLast()
					.print(stream);
	}

	/**
	 * Print all the messages printed by the caller thread to the default print-stream.
	 *
	 * @since 0.2.0 ~2021.01.30
	 */
	public static void printAll() {
		for (DiagnosticMessage message : Diagnostic.messages.get())
			message.print();
	}

	/**
	 * Print all the messages printed by the caller thread to the given {@code stream}.
	 *
	 * @param stream the print stream to print the messages to.
	 * @throws NullPointerException if the given {@code stream} is null.
	 * @since 0.2.0 ~2021.01.30
	 */
	public static void printAll(PrintStream stream) {
		for (DiagnosticMessage message : Diagnostic.messages.get())
			message.print(stream);
	}

	/**
	 * Print and remove the last message printed by the caller thread to the default
	 * print-stream. (phrase only)
	 *
	 * @since 0.2.0 ~2021.01.30
	 */
	public static void printPhrase() {
		Deque<DiagnosticMessage> messages = Diagnostic.messages.get();

		if (!messages.isEmpty())
			messages.pollLast()
					.printPhrase();
	}

	/**
	 * Print and remove the last message printed by the caller thread to the given {@code
	 * stream}. (phrase only)
	 *
	 * @param stream the print stream to print the last message to.
	 * @throws NullPointerException if the given {@code stream} is null.
	 * @since 0.2.0 ~2021.01.30
	 */
	public static void printPhrase(PrintStream stream) {
		Objects.requireNonNull(stream, "stream");
		Deque<DiagnosticMessage> messages = Diagnostic.messages.get();

		if (!messages.isEmpty())
			messages.pollLast()
					.printPhrase(stream);
	}

	/**
	 * Print all the messages printed by the caller thread to the default print-stream.
	 * (phrase only)
	 *
	 * @since 0.2.0 ~2021.01.30
	 */
	public static void printPhrases() {
		for (DiagnosticMessage message : Diagnostic.messages.get())
			message.printPhrase();
	}

	/**
	 * Print all the messages printed by the caller thread to the given {@code stream}.
	 * (phrase only)
	 *
	 * @param stream the print stream to print the messages to.
	 * @throws NullPointerException if the given {@code stream} is null.
	 * @since 0.2.0 ~2021.01.30
	 */
	public static void printPhrases(PrintStream stream) {
		Objects.requireNonNull(stream, "stream");
		for (DiagnosticMessage message : Diagnostic.messages.get())
			message.printPhrase(stream);
	}
}
