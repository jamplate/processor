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
package org.jamplate.source.document;

import java.io.*;
import java.util.Comparator;

/**
 * An interface that abstracts the functionality required to deal with source-code files.
 * <br>
 * The document should serialize its {@link #qualifiedName()}, {@link #name()} and {@link
 * #simpleName()}. It is not encouraged to serialized additional data.
 * <br>
 * If a document is a deserialized document then any method that attempts to read the
 * content or its length will throw an {@link IllegalStateException}.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.01.13
 */
public interface Document extends Serializable {
	/**
	 * The standard document comparator. This comparator is sorting documents by their
	 * qualified name. (natural ordering)
	 *
	 * @since 0.2.0 ~2021.01.13
	 */
	Comparator<Document> COMPARATOR = Comparator.comparing(Document::qualifiedName);

	/**
	 * Determines if the given {@code object} equals this document or not. An object
	 * equals a document when that object is a document and has the same {@link
	 * #qualifiedName()} as this document. (regardless of its content, assuming the user
	 * is honest and does not provide two documents with same qualified name but from
	 * different origins or have different content)
	 * <pre>
	 *     equals = object instanceof Document &&
	 *     			this.qualifiedName.equals(object.qualifiedName)
	 * </pre>
	 *
	 * @param object the object to be matched.
	 * @return true, if the given {@code object} is a document and equals this document.
	 * @since 0.2.0 ~2021.01.13
	 */
	@Override
	boolean equals(Object object);

	/**
	 * Calculates the hashCode of this document. The hash code of a document is the hash
	 * code of the {@link #qualifiedName()} of it.
	 * <pre>
	 *     hashCode = &lt;QualifiedNameHashCode&gt;
	 * </pre>
	 *
	 * @return the hash code of this document.
	 * @since 0.2.0 ~2021.01.13
	 */
	@Override
	int hashCode();

	/**
	 * Returns a string representation of this document. The string representation of a
	 * document is its {@link #qualifiedName()}.
	 * <pre>
	 *     toString = &lt;QualifiedNameToString&gt;
	 * </pre>
	 *
	 * @return a string representation of this document.
	 * @since 0.2.0 ~2021.01.13
	 */
	@Override
	String toString();

	/**
	 * Returns the length of this document. This method must always return the same value
	 * on the same instance.
	 *
	 * @return the length of this document.
	 * @throws IllegalStateException if this document is a deserialized document.
	 * @throws IOError               if any I/O exception occurs.
	 * @since 0.2.0 ~2021.01.17
	 */
	int length();

	/**
	 * Return the name of this document.
	 *
	 * @return the name of this document.
	 * @since 0.2.0 ~2021.01.13
	 */
	String name();

	/**
	 * Open a new input-stream that reads the content of this document.
	 *
	 * @return a new input-stream that reads the content of this document.
	 * @throws IOException           if any I/O exception occurs.
	 * @throws IllegalStateException if this document is a deserialized document.
	 * @since 0.2.0 ~2021.01.13
	 */
	InputStream openInputStream() throws IOException;

	/**
	 * Open a new reader that reads the content of this document.
	 *
	 * @return a new reader that reads the content of this document.
	 * @throws IOException           if any I/O exception occurs. (optional)
	 * @throws IllegalStateException if this document is a deserialized document.
	 * @since 0.2.0 ~2021.01.13
	 */
	Reader openReader() throws IOException;

	/**
	 * Return the qualified name of this document. The qualified name usually refers to
	 * the full path where this document belong.
	 *
	 * @return the qualified name of this document.
	 * @since 0.2.0 ~2021.01.13
	 */
	String qualifiedName();

	/**
	 * Read the content of this document. Once the content read, it should be cached. So,
	 * invoking this method multiple times must be easy to perform.
	 *
	 * @return the content of this document. (unmodifiable view)
	 * @throws IOError               if any I/O exception occurs. (optional)
	 * @throws IllegalStateException if this document is a deserialized document.
	 * @since 0.2.0 ~2021.01.13
	 */
	CharSequence readContent();

	/**
	 * Return the simple name of this document. The simple name usually refers to the name
	 * but without any extensions (like {@code .class}, {@code .java} and {@code
	 * .jamplate}).
	 *
	 * @return the simple name of this document.
	 * @since 0.2.0 ~2021.01.13
	 */
	String simpleName();
}
