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
package org.jamplate.model.document;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.Objects;

/**
 * A pseudo document that get made programmatically.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.01.13
 */
public class PseudoDocument extends AbstractDocument {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 5906973056576376713L;

	/**
	 * The content of this document.
	 *
	 * @since 0.2.0 ~2021.01.13
	 */
	@SuppressWarnings("TransientFieldNotInitialized")
	protected final transient String content;

	/**
	 * Construct a new pseudo document that have the given {@code content}. The qualified
	 * name will be the string in hex of the {@link System#identityHashCode(Object)
	 * identitiy hash code} of the given content.
	 *
	 * @param content the content of the constructed pseudo content.
	 * @throws NullPointerException if the given {@code content} is null.
	 * @since 0.2.0 ~2021.01.13
	 */
	public PseudoDocument(CharSequence content) {
		super(
				Integer.toHexString(System.identityHashCode(content)),
				"",
				""
		);
		Objects.requireNonNull(content, "content");
		this.content = content.toString();
	}

	/**
	 * Construct a new pseudo document that have the given {@code content} and have all of
	 * shapes of its name as the given {@code name}.
	 *
	 * @param content the content of the constructed pseudo content.
	 * @param name    the qualified-name, name and simple-name of the constructed
	 *                document.
	 * @throws NullPointerException if the given {@code content} or {@code name} is null.
	 * @since 0.2.0 ~2021.01.13
	 */
	public PseudoDocument(CharSequence content, String name) {
		super(name, name, name);
		Objects.requireNonNull(content, "content");
		this.content = content.toString();
	}

	/**
	 * Construct a new pseudo document that have the given {@code content} and the given
	 * names.
	 *
	 * @param content       the content of the constructed document.
	 * @param qualifiedName the qualified name of the constructed document.
	 * @param name          the name of the constructed document.
	 * @param simpleName    the simple name of the constructed document.
	 * @throws NullPointerException if the given {@code content} or {@code qualifiedName}
	 *                              or {@code name} or {@code simpleName} is null.
	 * @since 0.2.0 ~2021.01.13
	 */
	public PseudoDocument(CharSequence content, String qualifiedName, String name, String simpleName) {
		super(qualifiedName, name, simpleName);
		Objects.requireNonNull(content, "content");
		this.content = content.toString();
	}

	@Override
	public InputStream openInputStream() {
		if (!this.constructed)
			throw new IllegalStateException("Deserialized Document");
		return new ByteArrayInputStream(this.content.getBytes());
	}

	@Override
	public Reader openReader() {
		if (!this.constructed)
			throw new IllegalStateException("Deserialized Document");
		return new StringReader(this.content);
	}

	@Override
	public CharSequence readContent() {
		if (!this.constructed)
			throw new IllegalStateException("Deserialized Document");
		return this.content;
	}
}
