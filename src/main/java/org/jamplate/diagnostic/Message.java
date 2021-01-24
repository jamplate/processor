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

import java.util.*;

/**
 * A diagnostic message describing a message from a component while it is working.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.01.17
 */
public class Message {
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
	protected final DiagnosticType type;

	/**
	 * Construct a new message that caused by the given {@code references}.
	 *
	 * @param type       the type of the message.
	 * @param references the references that caused the constructed message.
	 * @throws NullPointerException if the given {@code type} or {@code references} is
	 *                              null.
	 * @since 0.2.0 ~2021.01.17
	 */
	public Message(DiagnosticType type, Reference... references) {
		Objects.requireNonNull(type, "type");
		Objects.requireNonNull(references, "references");
		this.type = type;
		this.title = "";
		this.details = "";
		this.references = new ArrayList<>(Arrays.asList(references));
	}

	/**
	 * Construct a new message that have the given {@code title} and caused by the given
	 * {@code references}.
	 *
	 * @param type       the type of the constructed message.
	 * @param title      the title for the constructed message.
	 * @param references the references caused this message.
	 * @throws NullPointerException if the given {@code type} or {@code title} or {@code
	 *                              references} is null.
	 * @since 0.2.0 ~2021.01.17
	 */
	public Message(DiagnosticType type, String title, Reference... references) {
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
	 * @throws NullPointerException if the given {@code type} or {@code title} or {@code
	 *                              details} or {@code references} is null.
	 * @since 0.2.0 ~2021.01.17
	 */
	public Message(DiagnosticType type, String title, String details, Reference... references) {
		Objects.requireNonNull(type, "type");
		Objects.requireNonNull(title, "title");
		Objects.requireNonNull(details, "details");
		Objects.requireNonNull(references, "references");
		this.type = type;
		this.title = title;
		this.details = details;
		this.references = new ArrayList<>(Arrays.asList(references));
	}

	@Override
	public boolean equals(Object object) {
		return object == this;
	}

	@Override
	public int hashCode() {
		return this.type.hashCode() + this.title.hashCode() + this.details.hashCode();
	}

	@Override
	public String toString() {
		return this.title + ": " + this.details;
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
	public List<Reference> references() {
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
	public DiagnosticType type() {
		return this.type;
	}
}
