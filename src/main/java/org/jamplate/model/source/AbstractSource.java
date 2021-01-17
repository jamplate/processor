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

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An implementation for the basic functionality of a source.
 *
 * @author LSafer
 * @version 0.0.2
 * @since 0.0.2 ~2021.01.09
 */
public abstract class AbstractSource implements Source {
	/**
	 * The file of this source.
	 *
	 * @since 0.0.2 ~2021.01.8
	 */
	protected final Document document;
	/**
	 * The length of this source. {@code -1} when targeting the whole document)
	 *
	 * @since 0.0.2 ~2021.01.17
	 */
	protected final int length;
	/**
	 * The parent source of this source. (might be null)
	 *
	 * @since 0.0.2 ~2021.01.8
	 */
	protected final Source parent;
	/**
	 * The position where the content of this source starts at its {@link #document}.
	 *
	 * @since 0.0.2 ~2021.01.8
	 */
	protected final int position;
	/**
	 * The content of this source. (lazily initialized)
	 *
	 * @since 0.0.2 ~2021.01.8
	 */
	protected CharSequence content;

	/**
	 * Construct a new source that takes the whole given {@code document} as its actual
	 * source.
	 *
	 * @param document the document of the constructed source.
	 * @throws NullPointerException if the given {@code document} is null.
	 * @since 0.0.2 ~2021.01.17
	 */
	protected AbstractSource(Document document) {
		Objects.requireNonNull(document, "document");
		this.document = document;
		this.parent = null;
		this.position = 0;
		this.length = -1;
	}

	/**
	 * Construct a new sub-source from the given {@code parent} source. The constructed
	 * source will have the same {@link #document()} as the given {@code parent} source.
	 * It will have its {@link #content()} lazily initialized and equals to the {@link
	 * String#substring(int, int)} of the {@link Document#readContent()} of the document
	 * of the given {@code parent} source. Also, the constructed source will have its
	 * {@link #position()} equals to the sum of the given {@code position} and the {@link
	 * #position()} of the given {@code parent} source. Finally, its obvious that the
	 * constructed source will have the given {@code parent} source as its {@link
	 * #parent()}.
	 * <br>
	 * Note: this constructor was built on trust. It trusts the implementation of the
	 * given {@code parent} source.
	 *
	 * @param parent   the parent source.
	 * @param position the sub-position to get from the given {@code parent} source.
	 * @param length   the length to get from the given {@code parent} source.
	 * @throws NullPointerException      if the given {@code parent} is null.
	 * @throws IllegalArgumentException  if the given {@code position} or {@code length}
	 *                                   is negative.
	 * @throws IndexOutOfBoundsException if {@code position + length} is more than the
	 *                                   length of the given {@code parent}.
	 * @since 0.0.2 ~2021.01.17
	 */
	protected AbstractSource(Source parent, int position, int length) {
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
	}

	@Override
	public CharSequence content() {
		if (this.content == null)
			this.content = this.document.readContent()
					.subSequence(
							this.position,
							this.position + this.length()
					);

		return this.content;
	}

	@Override
	public Document document() {
		return this.document;
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof Source &&
			   Objects.equals(this.document, ((Source) other).document()) &&
			   this.position == ((Source) other).position() &&
			   this.length() == ((Source) other).length();
	}

	@Override
	public int hashCode() {
		return this.document.hashCode() * this.length() + this.position;
	}

	@Override
	public int length() {
		return this.length == -1 ?
			   this.document.length() :
			   this.length;
	}

	@Override
	public Matcher matcher(Pattern pattern) {
		Objects.requireNonNull(pattern, "pattern");
		Matcher matcher = pattern.matcher(this.document.readContent());
		matcher.region(this.position, this.position + this.length());
		matcher.useTransparentBounds(true);
		matcher.useAnchoringBounds(true);
		return matcher;
	}

	@Override
	public Source parent() {
		return this.parent;
	}

	@Override
	public int position() {
		return this.position;
	}

	@Override
	public String toString() {
		return this.document + "[" + this.position + ", " + this.content.length() + "]";
	}
}
