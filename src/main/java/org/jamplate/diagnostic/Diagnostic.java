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
package org.jamplate.diagnostic;

import org.jamplate.source.reference.Reference;

import java.util.LinkedList;
import java.util.Objects;

/**
 * Diagnostics Manager class. All managed by thread locals.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.01.17
 */
@SuppressWarnings("UtilityClass")
public final class Diagnostic {
	/**
	 * A thread-local containing the errors occurred by a thread.
	 *
	 * @since 0.2.0 ~2021.01.17
	 */
	private static final ThreadLocal<LinkedList<Message>> messages = ThreadLocal.withInitial(LinkedList::new);

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
	 * Returns and removes the last message printed. (making the last message the one
	 * before it)
	 *
	 * @return the last message printed.
	 * @since 0.2.0 ~2021.01.17
	 */
	public static Message poll() {
		return Diagnostic.messages.get()
				.pollLast();
	}

	/**
	 * The given {@code message} will be printed whenever the diagnostic user decided to
	 * print it.
	 *
	 * @param message the message to be printed.
	 * @throws NullPointerException if the given {@code message} is null.
	 * @since 0.2.0 ~2021.01.17
	 */
	public static void print(Message message) {
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
	public static void printDebug(Reference... references) {
		Objects.requireNonNull(references, "references");
		Diagnostic.print(new Message(
				DiagnosticType.DEBUG,
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
	public static void printDebug(String title, Reference... references) {
		Objects.requireNonNull(title, "title");
		Objects.requireNonNull(references, "references");
		Diagnostic.print(new Message(
				DiagnosticType.DEBUG,
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
	public static void printDebug(String title, String details, Reference... references) {
		Objects.requireNonNull(title, "title");
		Objects.requireNonNull(details, "details");
		Objects.requireNonNull(references, "references");
		Diagnostic.print(new Message(
				DiagnosticType.DEBUG,
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
	public static void printError(Reference... references) {
		Objects.requireNonNull(references, "references");
		Diagnostic.print(new Message(
				DiagnosticType.ERROR,
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
	public static void printError(String title, Reference... references) {
		Objects.requireNonNull(title, "title");
		Objects.requireNonNull(references, "references");
		Diagnostic.print(new Message(
				DiagnosticType.ERROR,
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
	public static void printError(String title, String details, Reference... references) {
		Objects.requireNonNull(title, "title");
		Objects.requireNonNull(details, "details");
		Objects.requireNonNull(references, "references");
		Diagnostic.print(new Message(
				DiagnosticType.ERROR,
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
	public static void printNote(Reference... references) {
		Objects.requireNonNull(references, "references");
		Diagnostic.print(new Message(
				DiagnosticType.NOTE,
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
	public static void printNote(String title, Reference... references) {
		Objects.requireNonNull(title, "title");
		Objects.requireNonNull(references, "references");
		Diagnostic.print(new Message(
				DiagnosticType.NOTE,
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
	public static void printNote(String title, String details, Reference... references) {
		Objects.requireNonNull(title, "title");
		Objects.requireNonNull(details, "details");
		Objects.requireNonNull(references, "references");
		Diagnostic.print(new Message(
				DiagnosticType.NOTE,
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
	public static void printWarning(Reference... references) {
		Objects.requireNonNull(references, "references");
		Diagnostic.print(new Message(
				DiagnosticType.WARNING,
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
	public static void printWarning(String title, Reference... references) {
		Objects.requireNonNull(title, "title");
		Objects.requireNonNull(references, "references");
		Diagnostic.print(new Message(
				DiagnosticType.WARNING,
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
	public static void printWarning(String title, String details, Reference... references) {
		Objects.requireNonNull(title, "title");
		Objects.requireNonNull(details, "details");
		Objects.requireNonNull(references, "references");
		Diagnostic.print(new Message(
				DiagnosticType.WARNING,
				title,
				details,
				references
		));
	}
}
