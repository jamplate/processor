/*
 *	Copyright 2020-2021 Cufy
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
package org.jamplate.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * A source reference is a component that points to a document or a fragment of it.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2020.12.25
 */
public final class Reference implements Serializable, Comparable<Reference> {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 5282867914272439796L;

	/**
	 * The length of this reference.
	 *
	 * @since 0.2.0 ~2021.01.17
	 */
	private final int length;
	/**
	 * The position where the content of this reference starts at the document.
	 *
	 * @since 0.2.0 ~2021.01.09
	 */
	private final int position;

	/**
	 * Construct a new reference with the position {@code 0} and {@code 0} length.
	 *
	 * @since 0.2.0 ~2021.02.19
	 */
	public Reference() {
		this.position = 0;
		this.length = 0;
	}

	/**
	 * Construct a new reference wit the given {@code position} and {@code 0} length.
	 *
	 * @param position the position of the constructed reference.
	 * @throws IllegalArgumentException if the given {@code position} is negative.
	 * @since 0.2.0 ~2021.02.19
	 */
	public Reference(int position) {
		if (position < 0)
			throw new IllegalArgumentException("negative position");
		this.position = position;
		this.length = 0;
	}

	/**
	 * Construct a new reference that starts at the given {@code position} and has the
	 * given {@code length}.
	 *
	 * @param position the position where the constructed reference will start.
	 * @param length   the length of the constructed reference.
	 * @throws IllegalArgumentException if the given {@code position} or {@code length} is
	 *                                  negative.
	 * @since 0.2.0 ~2021.02.17
	 */
	public Reference(int position, int length) {
		if (position < 0)
			throw new IllegalArgumentException("negative position");
		if (length < 0)
			throw new IllegalArgumentException("negative length");
		this.position = position;
		this.length = length;
	}

	/**
	 * A reference is greater that another reference when it has a position less that the
	 * other. If both references are at the same position then the shortest reference is
	 * the greatest.
	 *
	 * @param reference the reference to be compare to this reference.
	 * @return the results of comparing this reference to the given {@code reference}.
	 *        {@code -1} if this reference is less than the given {@code reference}. {@code 1}
	 * 		if this reference is greater. {@code 0} if they are equal.
	 * @throws NullPointerException if the given {@code reference} is null.
	 * @since 0.2.0 ~2021.02.17
	 */
	@Override
	public int compareTo(Reference reference) {
		Objects.requireNonNull(reference, "reference");
		return reference.position < this.position ?
			   1 :
			   reference.position > this.position ?
			   -1 :
			   Integer.compare(reference.length, this.length);
	}

	/**
	 * A reference is equals another object, if that object is a reference and has the
	 * same {@link #position()} and {@link #length()} of this source.
	 * <pre>
	 *     equals = object instanceof Reference &&
	 *     			object.position == this.position &&
	 *     			object.length == this.length
	 * </pre>
	 *
	 * @param object the object to be matched.
	 * @return if the given object is a reference and equals this reference.
	 * @since 0.2.0 ~2021.01.07
	 */
	@Override
	public boolean equals(Object object) {
		if (object == this)
			return true;
		if (object instanceof Reference) {
			Reference reference = (Reference) object;

			return reference.position == this.position &&
				   reference.length == this.length;
		}

		return false;
	}

	/**
	 * The hash code of a reference is calculated as follows.
	 * <pre>
	 *     hashCode = &lt;Position&gt; + &lt;Length&gt;
	 * </pre>
	 *
	 * @return the hash code of this reference.
	 * @since 0.2.0 ~2021.01.07
	 */
	@Override
	public int hashCode() {
		return this.position + this.length;
	}

	/**
	 * Returns a string representation of this reference.
	 * <pre>
	 *     toString = (&lt;Position&gt;, &lt;Length&gt;)
	 * </pre>
	 *
	 * @return a string representation of this reference.
	 * @since 0.2.0 ~2021.01.06
	 */
	@Override
	public String toString() {
		return "(" + this.position + ", " + this.length + ")";
	}

	/**
	 * The length of this reference. Must always return the same value. Must always be the
	 * same as the length of the content of this source.
	 *
	 * @return the length of the content of this reference.
	 * @since 0.2.0 ~2021.01.10
	 */
	public int length() {
		return this.length;
	}

	/**
	 * Return where this reference starts at its document.
	 *
	 * @return the position of this reference.
	 * @since 0.2.0 ~2021.01.04
	 */
	public int position() {
		return this.position;
	}

	/**
	 * Slice this reference from the given {@code position} to the end of this reference.
	 *
	 * @param position the position where the new slice reference will have relative to
	 *                 this reference.
	 * @return a sub reference of this reference that starts from the given {@code
	 * 		position} relative to this reference.
	 * @throws IllegalArgumentException  if the given {@code position} is negative.
	 * @throws IndexOutOfBoundsException if {@code position > this.length()}.
	 * @throws IllegalStateException     if this reference is a deserialized reference.
	 * @since 0.2.0 ~2021.01.06
	 */
	public Reference subReference(int position) {
		if (position < 0)
			throw new IllegalArgumentException("negative position");
		if (position > this.length)
			throw new IndexOutOfBoundsException("position > this.length");
		return new Reference(
				this.position + position,
				this.length - position
		);
	}

	/**
	 * Slice this reference from the given {@code position} and limit it with the given
	 * {@code length}.
	 *
	 * @param position the position where the new slice reference will have relative to
	 *                 this reference.
	 * @param length   the length of the new slice reference.
	 * @return a slice of this reference that starts from the given {@code position}
	 * 		relative to this reference and have the given {@code length}.
	 * @throws IllegalArgumentException  if the given {@code position} or {@code length}
	 *                                   is negative.
	 * @throws IndexOutOfBoundsException if {@code position + length > this.length()}.
	 * @throws IllegalStateException     if this reference is a deserialized reference.
	 * @since 0.2.0 ~2021.01.06
	 */
	public Reference subReference(int position, int length) {
		if (position < 0)
			throw new IllegalArgumentException("negative position");
		if (length < 0)
			throw new IllegalArgumentException("negative length");
		if (position + length > this.length)
			throw new IndexOutOfBoundsException("position + length > this.length");
		return new Reference(
				this.position + position,
				length
		);
	}
}
//	/**
//	 * Return the line number of this reference.
//	 *
//	 * @return the line number of this reference.
//	 * @since 0.2.0 ~2021.01.26
//	 */
//	int line();
//
//	/**
//	 * Return a reference for the whole line this reference has occurred at.
//	 *
//	 * @return a reference of the whole line of this.
//	 * @throws IllegalStateException if this reference is a deserialized reference.
//	 * @since 0.2.0 ~2021.01.27
//	 */
//	Reference lineReference();
//
//	/**
//	 * Return the content of this reference as a string. This method should always return
//	 * the same value.
//	 *
//	 * @return the content of this reference. (unmodifiable)
//	 * @throws IllegalStateException if this reference is a deserialized reference.
//	 * @throws IOError               if any I/O exception occurs.
//	 * @since 0.2.0 ~2021.01.08
//	 */
//	CharSequence content();
//
//	/**
//	 * The document that this reference is from.
//	 *
//	 * @return the document of this reference.
//	 * @since 0.2.0 ~2021.01.07
//	 */
//	Document document();
//
//	/**
//	 * Return a reference for the whole document this reference has occurred at.
//	 *
//	 * @return a reference of the whole document of this.
//	 * @throws IllegalStateException if this reference is a deserialized reference.
//	 * @since 0.2.0 ~2021.01.31
//	 */
//	Reference documentReference();

//	/**
//	 * The standard reference comparator. This comparator is sorting references from the
//	 * first to the last and from the longest to the shortest.
//	 *
//	 * @since 0.2.0 ~2021.01.09
//	 */
//	public static final Comparator<Reference> COMPARATOR = Comparator.comparingInt(Reference::position)
//			.thenComparing(Comparator.comparingInt(Reference::length).reversed());
