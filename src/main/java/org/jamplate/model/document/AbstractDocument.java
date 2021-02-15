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

import org.jamplate.model.Name;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.IntStream;

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
	protected final Name name;

	/**
	 * True, if this document bas been constructed using its constructor. (in other words
	 * 'not deserialized')
	 *
	 * @since 0.2.0 ~2021.01.17
	 */
	@SuppressWarnings("TransientFieldNotInitialized")
	protected final transient boolean constructed;

	/**
	 * The length of this document. (lazily initialized)
	 *
	 * @since 0.2.0 ~2021.01.27
	 */
	@SuppressWarnings("TransientFieldNotInitialized")
	protected transient int length = -1;
	/**
	 * An array of the positions of the lines in this document. (lazily initialized)
	 *
	 * @since 0.2.0 ~2021.01.27
	 */
	protected transient int[] lines;

	/**
	 * Construct a new document with the given {@code name}.
	 *
	 * @param name the name of the constructed document.
	 * @throws NullPointerException if the given {@code name} is null.
	 * @since 0.2.0 ~2021.01.17
	 */
	protected AbstractDocument(Name name) {
		Objects.requireNonNull(name, "name");
		this.name = name;
		this.constructed = true;
	}

	@Override
	public boolean equals(Object object) {
		return object instanceof Document &&
			   Objects.equals(this.name, ((Document) object).name());
	}

	@Override
	public int hashCode() {
		return this.name.hashCode();
	}

	@Override
	public int length() {
		if (!this.constructed)
			throw new IllegalStateException("Deserialized Document");
		if (this.length == -1)
			this.length = this.readContent().length();

		return this.length;
	}

	@Override
	public IntStream lines() {
		if (!this.constructed)
			throw new IllegalStateException("Deserialized Document");
		if (this.lines == null) {
			CharSequence content = this.readContent();
			//noinspection MagicCharacter
			this.lines = IntStream.range(0, content.length())
					.filter(i -> i == 0 || content.charAt(i) == '\n')
					.toArray();
		}

		return Arrays.stream(this.lines);
	}

	@Override
	public Name name() {
		return this.name;
	}

	@Override
	public String toString() {
		return this.name.toQualifiedString();
	}
}
