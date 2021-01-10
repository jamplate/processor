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

/**
 * An implementation for the basic functionality of a source.
 *
 * @param <D> the type of the actual source of this source.
 * @author LSafer
 * @version 0.0.2
 * @since 0.0.2 ~2021.01.09
 */
public abstract class AbstractSource<D extends Comparable> implements Source<D> {
	/**
	 * The content of this source.
	 *
	 * @since 0.0.2 ~2021.01.8
	 */
	protected final CharSequence content;
	/**
	 * The file of this source.
	 *
	 * @since 0.0.2 ~2021.01.8
	 */
	protected final D document;
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
	 * The root source of this source. (might be this)
	 *
	 * @since 0.0.2 ~2021.01.8
	 */
	protected final Source root;

	/**
	 * Construct a new source that takes the given {@code document} as its actual source.
	 * <br>
	 * This constructor just trusts the caller that the given {@code content} is taken from the given {@code document} starting from the given {@code
	 * position}.
	 *
	 * @param document the document of the constructed source.
	 * @param content  the content from the given {@code document}.
	 * @param position the position the given {@code content} started from the given {@code document}.
	 * @throws NullPointerException if the given {@code document} is null.
	 * @since 0.0.2 ~2021.01.8
	 */
	protected AbstractSource(D document, CharSequence content, int position) {
		Objects.requireNonNull(document, "document");
		Objects.requireNonNull(content, "content");
		this.root = this;
		this.parent = null;
		this.document = document;
		this.content = content;
		this.position = position;
	}

	/**
	 * Construct a new sub-source from the given {@code parent} source. The constructed source will have the same {@link #root()} and {@link
	 * #document()} as the given {@code parent} source. But, it will have its {@link #content()} equals to the {@link String#substring(int, int)} of
	 * the {@link #content()} of the given {@code parent} source. Also, the constructed source will have its {@link #position()} equals to the sum of
	 * the given {@code pos} and the {@link #position()} of the given {@code parent} source. Finally, its obvious that the constructed source will
	 * have the given {@code parent} source as its {@link #parent()}.
	 * <br>
	 * Note: this constructor was built on trust. It trusts the implementation of the given {@code parent} source.
	 * <br>
	 * Also Note: it is possible that the given {@code parent} returns an object that does not fit into the {@code D} type parameter when invoking its
	 * {@link Source#document()} method.
	 *
	 * @param parent the parent source.
	 * @param pos    the sub-position to get from the given {@code parent} source.
	 * @param len    the length to get from the given {@code parent} source.
	 * @throws NullPointerException      if the given {@code parent} is null.
	 * @throws IllegalArgumentException  if the given {@code pos} or {@code len} is negative.
	 * @throws IndexOutOfBoundsException if {@code parent.content().substring(pos, len)} throws it.
	 * @since 0.0.2 ~2021.01.8
	 */
	protected AbstractSource(Source<? extends D> parent, int pos, int len) {
		Objects.requireNonNull(parent, "parent");
		if (pos < 0)
			throw new IllegalArgumentException("negative position");
		if (len < 0)
			throw new IllegalArgumentException("negative length");
		this.root = parent.root();
		this.parent = parent;
		this.document = parent.document();
		this.content = parent.content()
				.subSequence(pos, pos + len);
		this.position = parent.position() + pos;
	}

	@Override
	public CharSequence content() {
		return this.content;
	}

	@Override
	public D document() {
		return this.document;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Source) {
			Source source = (Source) other;
			return Objects.equals(this.document, source.document()) &&
				   this.position == ((Source) other).position() &&
				   this.content.length() == ((Source) other).content().length();
		}

		return false;
	}

	@Override
	public int hashCode() {
		return this.document.hashCode() * this.content.length() + this.position;
	}

	@Override
	public Source<? extends D> parent() {
		return this.parent;
	}

	@Override
	public int position() {
		return this.position;
	}

	@Override
	public Source<? extends D> root() {
		return this.root;
	}

	@Override
	public String toString() {
		return this.document + "[" + this.position + ", " + this.content.length() + "]";
	}
}
