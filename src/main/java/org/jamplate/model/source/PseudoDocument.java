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
package org.jamplate.model.source;

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
public class PseudoDocument implements Document {
	/**
	 * The content of this document.
	 *
	 * @since 0.2.0 ~2021.01.13
	 */
	protected final CharSequence content;
	/**
	 * The name of this document.
	 *
	 * @since 0.2.0 ~2021.01.13
	 */
	protected final String name;
	/**
	 * The qualified name of this document.
	 *
	 * @since 0.2.0 ~2021.01.13
	 */
	protected final String qualifiedName;
	/**
	 * The simple name of this document.
	 *
	 * @since 0.2.0 ~2021.01.13
	 */
	protected final String simpleName;

	/**
	 * Construct a new pseudo document that have the given {@code content}. The qualified
	 * name will be the string of the {@link System#identityHashCode(Object) identitiy
	 * hash code} of the given content.
	 *
	 * @param content the content of the constructed pseudo content.
	 * @throws NullPointerException if the given {@code content} is null.
	 * @since 0.2.0 ~2021.01.13
	 */
	public PseudoDocument(CharSequence content) {
		Objects.requireNonNull(content, "content");
		this.content = content;
		this.qualifiedName = Integer.toHexString(System.identityHashCode(content));
		this.name = "";
		this.simpleName = "";
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
		Objects.requireNonNull(content, "content");
		Objects.requireNonNull(name, "name");
		this.content = content;
		this.qualifiedName = name;
		this.name = name;
		this.simpleName = name;
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
		Objects.requireNonNull(content, "content");
		Objects.requireNonNull(qualifiedName, "qualifiedName");
		Objects.requireNonNull(name, "name");
		Objects.requireNonNull(simpleName, "simpleName");
		this.content = content;
		this.qualifiedName = qualifiedName;
		this.name = name;
		this.simpleName = simpleName;
	}

	@Override
	public boolean equals(Object object) {
		return object instanceof Document &&
			   Objects.equals(((Document) object).qualifiedName(), this.qualifiedName);
	}

	@Override
	public int hashCode() {
		return this.qualifiedName.hashCode();
	}

	@Override
	public int length() {
		return this.content.length();
	}

	@Override
	public String name() {
		return this.name;
	}

	@Override
	public InputStream openInputStream() {
		return new ByteArrayInputStream(this.content.toString().getBytes());
	}

	@Override
	public Reader openReader() {
		return new StringReader(this.content.toString());
	}

	@Override
	public String qualifiedName() {
		return this.qualifiedName;
	}

	@Override
	public CharSequence readContent() {
		return this.content;
	}

	@Override
	public String simpleName() {
		return this.simpleName;
	}

	@Override
	public String toString() {
		return this.qualifiedName;
	}
}
