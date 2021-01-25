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
package org.jamplate.source.reference;

import org.jamplate.source.document.Document;

import java.io.IOError;
import java.util.Objects;

/**
 * An implementation for the basic functionality of a source.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.01.09
 */
public abstract class AbstractReference implements Reference {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 1099496699050172132L;

	/**
	 * The document of this reference.
	 *
	 * @since 0.2.0 ~2021.01.09
	 */
	protected final Document document;
	/**
	 * The length of this reference.
	 *
	 * @since 0.2.0 ~2021.01.17
	 */
	protected final int length;
	/**
	 * The position where the content of this reference starts at its {@link #document}.
	 *
	 * @since 0.2.0 ~2021.01.09
	 */
	protected final int position;

	/**
	 * True, if this reference has been constructed using its constructor. (in other
	 * words 'not deserialized')
	 *
	 * @since 0.2.0 ~2021.01.17
	 */
	@SuppressWarnings("TransientFieldNotInitialized")
	protected final transient boolean constructed;
	/**
	 * The parent reference of this reference. (might be null)
	 *
	 * @since 0.2.0 ~2021.01.08
	 */
	@SuppressWarnings("TransientFieldNotInitialized")
	protected final transient Reference parent;

	/**
	 * The content of this source. (lazily initialized)
	 *
	 * @since 0.2.0 ~2021.01.08
	 */
	protected transient CharSequence content;

	/**
	 * Construct a new reference that takes the whole given {@code document} as its actual
	 * source.
	 *
	 * @param document the document of the constructed reference.
	 * @throws NullPointerException if the given {@code document} is null.
	 * @throws IOError              if any I/O exception occur.
	 * @since 0.2.0 ~2021.01.17
	 */
	protected AbstractReference(Document document) {
		Objects.requireNonNull(document, "document");
		this.document = document;
		this.parent = null;
		this.position = 0;
		this.length = document.length();
		this.constructed = true;
	}

	/**
	 * Construct a new sub-reference from the given {@code parent} reference. The
	 * constructed reference will have the same {@link #document()} as the given {@code
	 * parent} reference. It will have its {@link #content()} lazily initialized and
	 * equals to the {@link String#substring(int, int)} of the {@link
	 * Document#readContent()} of the document of the given {@code parent} reference.
	 * Also, the constructed reference will have its {@link #position()} equals to the sum
	 * of the given {@code position} and the {@link #position()} of the given {@code
	 * parent} reference. Finally, its obvious that the constructed reference will have
	 * the given {@code parent} reference as its {@link #parent()}.
	 * <br>
	 * Note: this constructor was built on trust. It trusts the implementation of the
	 * given {@code parent} reference.
	 *
	 * @param parent   the parent source reference.
	 * @param position the sub-position to get from the given {@code parent} reference.
	 * @param length   the length to get from the given {@code parent} reference.
	 * @throws NullPointerException      if the given {@code parent} is null.
	 * @throws IllegalArgumentException  if the given {@code position} or {@code length}
	 *                                   is negative.
	 * @throws IndexOutOfBoundsException if {@code position + length} is more than the
	 *                                   length of the given {@code parent}.
	 * @since 0.2.0 ~2021.01.17
	 */
	protected AbstractReference(Reference parent, int position, int length) {
		Objects.requireNonNull(parent, "parent");
		if (position < 0)
			throw new IllegalArgumentException("negative position");
		if (length < 0)
			throw new IllegalArgumentException("negative length");
		if (position + length > parent.length())
			throw new IndexOutOfBoundsException("position + length > parent.length()");
		this.document = parent.document();
		this.parent = parent;
		this.position = parent.position() + position;
		this.length = length;
		this.constructed = true;
	}

	@Override
	public CharSequence content() {
		if (!this.constructed)
			throw new IllegalStateException("Deserialized Source");
		if (this.content == null)
			this.content = this.document.readContent()
					.subSequence(
							this.position,
							this.position + this.length
					);

		return this.content;
	}

	@Override
	public Document document() {
		return this.document;
	}

	@Override
	public boolean equals(Object object) {
		return object instanceof Reference &&
			   Objects.equals(this.document, ((Reference) object).document()) &&
			   this.position == ((Reference) object).position() &&
			   this.length == ((Reference) object).length();
	}

	@Override
	public int hashCode() {
		return this.document.hashCode() * this.length + this.position;
	}

	@Override
	public int length() {
		return this.length;
	}

	@Override
	public Reference parent() {
		if (!this.constructed)
			throw new IllegalStateException("Deserialized Source");
		return this.parent;
	}

	@Override
	public int position() {
		return this.position;
	}

	@Override
	public String toString() {
		return this.document + "[" + this.position + ", " + this.length + "]";
	}
}
