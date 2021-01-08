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
package org.jamplate.impl.model.source;

import org.jamplate.model.source.Shadow;
import org.jamplate.model.source.Source;

import java.util.Objects;

/**
 * An implementation of the interface {@link Source} that delegates to the information of an actual whole or fragment of a file.
 *
 * @author LSafer
 * @version 0.0.2
 * @since 0.0.2 ~2021.01.04
 */
public class FileSource implements Source {
	/**
	 * The length of this source.
	 *
	 * @since 0.0.2 ~2021.01.6
	 */
	protected final int length;
	/**
	 * The position of this source in the its file.
	 *
	 * @since 0.0.2 ~2021.01.6
	 */
	protected final int position;
	/**
	 * The file of this source.
	 *
	 * @since 0.0.2 ~2021.01.6
	 */
	protected final Shadow shadow;
	/**
	 * A cached sequence of this source.
	 */
	protected String content;

	/**
	 * Construct a new source pointing to the given {@code file}.
	 *
	 * @param shadow the file of the constructed source.
	 * @throws NullPointerException if the given {@code file} is null.
	 * @since 0.0.2 ~2021.01.6
	 */
	public FileSource(Shadow shadow) {
		Objects.requireNonNull(shadow, "shadow");
		this.shadow = shadow;
		this.position = 0;
		this.length = shadow.length();
	}

	/**
	 * Construct a new source pointing to a fragment of the given {@code file} starting from the given {@code pos}.
	 *
	 * @param shadow the file of the constructed source.
	 * @param pos    the pos of the constructed source.
	 * @throws NullPointerException      if the given {@code file} is null.
	 * @throws IllegalArgumentException  if the given {@code pos} is negative.
	 * @throws IndexOutOfBoundsException if {@code pos > file.length()}.
	 * @since 0.0.2 ~2021.01.6
	 */
	public FileSource(Shadow shadow, int pos) {
		Objects.requireNonNull(shadow, "shadow");
		if (pos < 0L)
			throw new IllegalArgumentException("negative pos");
		int length = shadow.length();
		if (pos > length)
			throw new IndexOutOfBoundsException("pos > length");
		this.shadow = shadow;
		this.position = pos;
		this.length = length;
	}

	/**
	 * Construct a new source that points to a fragment of the given {@code file} starting from the given {@code pos} and having the given {@code
	 * len}.
	 *
	 * @param shadow the file of the constructed source.
	 * @param pos    the pos of the constructed source.
	 * @param len    the len of the constructed source.
	 * @throws NullPointerException      if the given {@code file} is null.
	 * @throws IllegalArgumentException  if the given {@code pos} or {@code len} is negative.
	 * @throws IndexOutOfBoundsException if {@code pos + len > file.len()}.
	 * @since 0.0.2 ~2021.01.6
	 */
	public FileSource(Shadow shadow, int pos, int len) {
		Objects.requireNonNull(shadow, "shadow");
		if (pos < 0L)
			throw new IllegalArgumentException("negative pos");
		if (len < 0L)
			throw new IllegalArgumentException("negative len");
		if (pos + len > shadow.length())
			throw new IndexOutOfBoundsException("pos + len > file.len()");
		this.shadow = shadow;
		this.position = pos;
		this.length = len;
	}

	@Override
	public boolean clashes(Source source) {
		if (this.shadow.equals(source.shadow())) {
			long start = source.position();
			long end = start + source.length();
			long s = this.position;
			long e = s + this.length;
			return (s >= start && s < end) ^
				   (e > start && e <= end);
		}

		return false;
	}

	@Override
	public boolean contains(Source source) {
		if (this.shadow.equals(source.shadow())) {
			long start = source.position();
			long end = start + source.length();
			long s = this.position;
			long e = s + this.length;
			return s >= start && s < end &&
				   e > start && e <= end;
		}

		return false;
	}

	@Override
	public String content() {
		return this.content == null ?
			   this.content = this.shadow.content()
					   .substring(this.position, this.position + this.length) :
			   this.content;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Source) {
			Source source = (Source) other;
			return this.shadow.equals(source.shadow()) &&
				   this.position == source.position() &&
				   this.length == source.length();
		}

		return false;
	}

	@Override
	public int hashCode() {
		return this.shadow.hashCode() * this.length + this.position;
	}

	@Override
	public int length() {
		return this.length;
	}

	@Override
	public int position() {
		return this.position;
	}

	@Override
	public Shadow shadow() {
		return this.shadow;
	}

	@Override
	public Source slice(int pos) {
		if (pos < 0L)
			throw new IllegalArgumentException("negative position");
		if (pos > this.length)
			throw new IndexOutOfBoundsException("pos > this.length()");
		return new FileSource(
				this.shadow,
				this.position + pos
		);
	}

	@Override
	public Source slice(int pos, int len) {
		if (pos < 0L)
			throw new IllegalArgumentException("negative position");
		if (len < 0L)
			throw new IllegalArgumentException("negative length");
		if (pos + len > this.length)
			throw new IndexOutOfBoundsException("pos + len > this.length()");
		return new FileSource(
				this.shadow,
				this.position + pos,
				len
		);
	}

	@Override
	public String toString() {
		return this.shadow + " [" + this.position + ", " + this.length + "]";
	}
}
