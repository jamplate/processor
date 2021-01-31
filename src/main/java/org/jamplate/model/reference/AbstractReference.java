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
package org.jamplate.model.reference;

import org.jamplate.model.document.Document;

import java.io.IOError;
import java.util.NoSuchElementException;
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
	 * The line this reference is at in its document.
	 *
	 * @since 0.2.0 ~2021.01.27
	 */
	protected final int line;
	/**
	 * The position where the content of this reference starts at its {@link #document}.
	 *
	 * @since 0.2.0 ~2021.01.09
	 */
	protected final int position;

	/**
	 * True, if this reference has been constructed using its constructor. (in other words
	 * 'not deserialized')
	 *
	 * @since 0.2.0 ~2021.01.17
	 */
	@SuppressWarnings("TransientFieldNotInitialized")
	protected final transient boolean constructed;

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
	 * @param document the document the constructed reference will be referencing.
	 * @throws NullPointerException  if the given {@code document} is null.
	 * @throws IllegalStateException if the given {@code document} is a deserialized
	 *                               document.
	 * @throws IOError               if any I/O exception occur.
	 * @since 0.2.0 ~2021.01.17
	 */
	protected AbstractReference(Document document) {
		Objects.requireNonNull(document, "document");
		this.document = document;
		this.position = 0;
		this.length = document.length();
		this.line = 1;
		this.constructed = true;
	}

	/**
	 * Construct a new reference that points to the line with the given {@code line}
	 * number in the given {@code document}.
	 *
	 * @param document the document the constructed reference will be referencing.
	 * @param line     the line the constructed reference will point to.
	 * @throws NullPointerException     if the given {@code document} is null.
	 * @throws IllegalArgumentException if {@code line < 1}.
	 * @throws NoSuchElementException   if the given {@code document} do not have such
	 *                                  line.
	 * @throws IllegalStateException    if the given {@code document} is a deserialized
	 *                                  document.
	 * @throws IOError                  if any I/O error occur.
	 * @since 0.2.0 ~2021.01.27
	 */
	protected AbstractReference(Document document, int line) {
		Objects.requireNonNull(document, "document");
		if (line < 1)
			throw new IllegalArgumentException("non-positive line");
		if (line > document.lines().count())
			throw new NoSuchElementException("line not found");
		this.document = document;
		this.position = document.lines()
				.skip(line - 1)
				.findFirst()
				.getAsInt();
		this.length = document.lines()
							  .skip(line)
							  .findFirst()
							  .orElseGet(document::length) -
					  this.position;
		this.line = line;
		this.constructed = true;
	}

	/**
	 * Construct a new reference that points to a fragment in the given {@code document}
	 * from the given {@code position} with the given {@code length}.
	 *
	 * @param document the document the constructed reference will be referencing.
	 * @param position the position where the constructed reference will point at in the
	 *                 given {@code document}.
	 * @param length   the length of the fragment the constructed reference will point
	 *                 at.
	 * @throws NullPointerException     if the given {@code document} is null.
	 * @throws IllegalArgumentException if the given {@code position} or {@code length} is
	 *                                  negative.
	 * @throws NoSuchElementException   if the given {@code document} does not have such
	 *                                  fragment.
	 * @throws IllegalStateException    if the given {@code document} is a deserialized
	 *                                  document.
	 * @throws IOError                  if any I/O error occur.
	 * @since 0.2.0 ~2021.01.31
	 */
	protected AbstractReference(Document document, int position, int length) {
		Objects.requireNonNull(document, "document");
		if (position < 0)
			throw new IllegalArgumentException("negative position");
		if (length < 0)
			throw new IllegalArgumentException("negative length");
		if (position + length > document.length())
			throw new NoSuchElementException("fragment not found");
		this.document = document;
		this.position = position;
		this.length = length;
		//noinspection NumericCastThatLosesPrecision
		this.line = (int) document
				.lines()
				.filter(i -> i <= position)
				.count();
		this.constructed = true;
	}

	@Override
	public CharSequence content() {
		if (!this.constructed)
			throw new IllegalStateException("Deserialized Reference");
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
	public Reference documentReference() {
		if (!this.constructed)
			throw new IllegalStateException("Deserialized Reference");
		return new DocumentReference(
				this.document
		);
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
	public int line() {
		return this.line;
	}

	@Override
	public Reference lineReference() {
		if (!this.constructed)
			throw new IllegalStateException("Deserialized Reference");
		return new LineReference(
				this.document,
				this.line
		);
	}

	@Override
	public int position() {
		return this.position;
	}

	@Override
	public Reference subReference(int position) {
		if (!this.constructed)
			throw new IllegalStateException("Deserialized Reference");
		if (position < 0)
			throw new IllegalArgumentException("negative position");
		if (position > this.length)
			throw new IndexOutOfBoundsException("position > this.length");
		return new SubReference(
				this.document,
				this.position + position,
				this.length - position
		);
	}

	@Override
	public Reference subReference(int position, int length) {
		if (!this.constructed)
			throw new IllegalStateException("Deserialized Reference");
		if (position < 0)
			throw new IllegalArgumentException("negative position");
		if (length < 0)
			throw new IllegalArgumentException("negative length");
		if (position + length > this.length)
			throw new IndexOutOfBoundsException("position + length > this.length");
		return new SubReference(
				this.document,
				this.position + position,
				length
		);
	}

	@Override
	public String toString() {
		return this.document + "[" + this.position + ", " + this.length + "]";
	}
}
