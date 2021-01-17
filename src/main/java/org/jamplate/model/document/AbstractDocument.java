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

import java.util.Objects;

/**
 * An abstraction of the basic functionality of a document.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.01.17
 */
public abstract class AbstractDocument implements Document {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -1442065210677397016L;

	/**
	 * The name of this document.
	 *
	 * @since 0.2.0 ~2021.01.17
	 */
	protected final String name;
	/**
	 * The uniq qualified name of this document.
	 *
	 * @since 0.2.0 ~2021.01.17
	 */
	protected final String qualifiedName;
	/**
	 * The simple name of this document.
	 *
	 * @since 0.2.0 ~2021.01.17
	 */
	protected final String simpleName;

	/**
	 * True, if this document have been constructed using its constructor. (in other words
	 * 'not deserialized')
	 *
	 * @since 0.2.0 ~2021.01.17
	 */
	@SuppressWarnings("TransientFieldNotInitialized")
	protected final transient boolean constructed;

	/**
	 * Construct a new document that have the given {@code qualifiedName}, {@code name}
	 * and {@code simpleName}.
	 *
	 * @param qualifiedName the uniq name of the constructed document.
	 * @param name          the name of the constructed document.
	 * @param simpleName    the simple name of the constructed document.
	 * @throws NullPointerException if the given {@code qualifiedName} or {@code name} or
	 *                              {@code simpleName} is null.
	 * @since 0.2.0 ~2021.01.17
	 */
	protected AbstractDocument(String qualifiedName, String name, String simpleName) {
		Objects.requireNonNull(qualifiedName, "qualifiedName");
		Objects.requireNonNull(name, "name");
		Objects.requireNonNull(simpleName, "simpleName");
		this.qualifiedName = qualifiedName;
		this.name = name;
		this.simpleName = simpleName;
		this.constructed = true;
	}

	@Override
	public boolean equals(Object object) {
		return object instanceof Document &&
			   Objects.equals(this.qualifiedName, ((Document) object).qualifiedName());
	}

	@Override
	public int hashCode() {
		return this.qualifiedName.hashCode();
	}

	@Override
	public String name() {
		return this.name;
	}

	@Override
	public String qualifiedName() {
		return this.qualifiedName;
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
