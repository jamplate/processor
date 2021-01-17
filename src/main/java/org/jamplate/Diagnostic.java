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
package org.jamplate;

import org.jamplate.source.reference.Reference;

import java.util.*;

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
				Type.DEBUG,
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
				Type.DEBUG,
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
				Type.DEBUG,
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
				Type.ERROR,
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
				Type.ERROR,
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
				Type.ERROR,
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
				Type.NOTE,
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
				Type.NOTE,
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
				Type.NOTE,
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
				Type.WARNING,
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
				Type.WARNING,
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
				Type.WARNING,
				title,
				details,
				references
		));
	}

	/**
	 * An enumeration of the allowed types of a diagnostic message.
	 *
	 * @author LSafer
	 * @version 0.2.0
	 * @since 0.2.0 ~2021.01.17
	 */
	public enum Type {
		/**
		 * A type for messages that get printed for internal debugging.
		 *
		 * @since 0.2.0 ~2021.01.17
		 */
		DEBUG,
		/**
		 * A type for messages that get printed to notify the user about an update or
		 * an advice.
		 *
		 * @since 0.2.0 ~2021.01.17
		 */
		NOTE,
		/**
		 * A type for messages that get printed to warn the user about a potential
		 * error.
		 *
		 * @since 0.2.0 ~2021.01.17
		 */
		WARNING,
		/**
		 * A type for messages that get printed to tell the user more about an error
		 * to be or already thrown.
		 *
		 * @since 0.2.0 ~2021.01.17
		 */
		ERROR
	}

	/**
	 * A diagnostic message describing a message from a component while it is working.
	 *
	 * @author LSafer
	 * @version 0.2.0
	 * @since 0.2.0 ~2021.01.17
	 */
	public static class Message {
		/**
		 * A detailed message describing this message.
		 *
		 * @since 0.2.0 ~2021.01.17
		 */
		protected final String details;
		/**
		 * The references caused this message. (might be null)
		 *
		 * @since 0.2.0 ~2021.01.17
		 */
		protected final List<Reference> references;
		/**
		 * The title of this message.
		 *
		 * @since 0.2.0 ~2021.01.17
		 */
		protected final String title;
		/**
		 * The type of this message.
		 *
		 * @since 0.2.0 ~2021.01.17
		 */
		protected final Diagnostic.Type type;

		/**
		 * Construct a new message that caused by the given {@code references}.
		 *
		 * @param type       the type of the message.
		 * @param references the references that caused the constructed message.
		 * @throws NullPointerException if the given {@code type} or {@code references} is
		 *                              null.
		 * @since 0.2.0 ~2021.01.17
		 */
		public Message(Diagnostic.Type type, Reference... references) {
			Objects.requireNonNull(type, "type");
			Objects.requireNonNull(references, "references");
			this.type = type;
			this.title = "";
			this.details = "";
			this.references = new ArrayList<>(Arrays.asList(references));
		}

		/**
		 * Construct a new message that have the given {@code title} and caused by the
		 * given {@code references}.
		 *
		 * @param type       the type of the constructed message.
		 * @param title      the title for the constructed message.
		 * @param references the references caused this message.
		 * @throws NullPointerException if the given {@code type} or {@code title} or
		 *                              {@code references} is null.
		 * @since 0.2.0 ~2021.01.17
		 */
		public Message(Diagnostic.Type type, String title, Reference... references) {
			Objects.requireNonNull(type, "type");
			Objects.requireNonNull(title, "title");
			Objects.requireNonNull(references, "references");
			this.type = type;
			this.title = title;
			this.details = "";
			this.references = new ArrayList<>(Arrays.asList(references));
		}

		/**
		 * Construct a new message that have the given {@code title} and the given {@code
		 * details} and caused by the given {@code references}.
		 *
		 * @param type       the type of the constructed message.
		 * @param references the references caused the constructed message.
		 * @param title      the title for the constructed message.
		 * @param details    the detailed message for the constructed message.
		 * @throws NullPointerException if the given {@code type} or {@code title} or
		 *                              {@code details} or {@code references} is null.
		 * @since 0.2.0 ~2021.01.17
		 */
		public Message(Diagnostic.Type type, String title, String details, Reference... references) {
			Objects.requireNonNull(type, "type");
			Objects.requireNonNull(title, "title");
			Objects.requireNonNull(details, "details");
			Objects.requireNonNull(references, "references");
			this.type = type;
			this.title = title;
			this.details = details;
			this.references = new ArrayList<>(Arrays.asList(references));
		}

		/**
		 * A detailed message about this message.
		 *
		 * @return the details of this message.
		 * @since 0.2.0 ~2021.01.17
		 */
		public String details() {
			return this.details;
		}

		/**
		 * The references that caused this message.
		 * <br>
		 *
		 * @return the references that caused this message.
		 * @since 0.2.0 ~2021.01.17
		 */
		public List<Reference> reference() {
			return Collections.unmodifiableList(this.references);
		}

		/**
		 * The title of this message.
		 *
		 * @return the title of this message.
		 * @since 0.2.0 ~2021.01.17
		 */
		public String title() {
			return this.title;
		}

		/**
		 * The type of this message.
		 *
		 * @return the type of this message.
		 * @since 0.2.0 ~2021.01.17
		 */
		public Diagnostic.Type type() {
			return this.type;
		}
	}
}
