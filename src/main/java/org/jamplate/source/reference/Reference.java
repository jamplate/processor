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
package org.jamplate.source.reference;

import org.jamplate.source.document.Document;

import java.io.IOError;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A source reference is a component that points to a document or a fragment of it.
 * <br>
 * Note: source references are built from top to bottom. So, a typical source reference
 * will store its parent reference but never store any sub-reference of it.
 * <br>
 * The reference should serialize its {@link #document()}, {@link #line()}, {@link
 * #position()} and {@link #length()}. It is not encouraged to serialize additional data.
 * <br>
 * If a reference is a deserialized reference then any method that attempts to read the
 * document or attempts to access the parent or attempts to create new references will
 * throw an {@link IllegalStateException}.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2020.12.25
 */
public interface Reference extends Serializable {
	/**
	 * The standard reference comparator. This comparator is sorting references from the
	 * first occurrence on the document to the last and from the longest to the shortest.
	 *
	 * @since 0.2.0 ~2021.01.09
	 */
	Comparator<Reference> COMPARATOR = Comparator.comparing(Reference::document, Document.COMPARATOR)
			.thenComparingInt(Reference::position)
			.thenComparing(Comparator.comparingInt(Reference::length).reversed());

	/**
	 * Construct a ready-to-use matcher from the given {@code reference}. The returned
	 * matcher has the whole content of the document of the given {@code reference}. But,
	 * it is limited to the region of the given {@code reference} (using {@link
	 * Matcher#region(int, int)}). The returned matcher also has {@link
	 * Matcher#hasTransparentBounds()} and {@link Matcher#useAnchoringBounds(boolean)}
	 * both enabled.
	 * <br>
	 * Notice: the returned matcher will return {@link Matcher#start()} and {@link
	 * Matcher#end()} as indexes at the original document.
	 *
	 * @param reference the reference to create a matcher for.
	 * @param pattern   the pattern to match.
	 * @return a matcher over the content of the given {@code reference}.
	 * @throws NullPointerException  if the given {@code reference} or {@code pattern} is
	 *                               null.
	 * @throws IllegalStateException if the {@link Reference#document() document} of the
	 *                               given {@code reference} is a deserialized document.
	 * @since 0.2.0 ~2021.01.23
	 */
	static Matcher matcher(Reference reference, Pattern pattern) {
		Objects.requireNonNull(reference, "reference");
		Objects.requireNonNull(pattern, "pattern");
		Matcher matcher = pattern.matcher(reference.document().readContent());
		matcher.region(reference.position(), reference.position() + reference.length());
		matcher.useTransparentBounds(true);
		matcher.useAnchoringBounds(true);
		return matcher;
	}

	/**
	 * A reference is equals another object, if that object is a reference and has the
	 * same {@link #document()}, {@link #position()} and {@link #length()} of this
	 * source.
	 * <pre>
	 *     equals = object instanceof Reference &&
	 *     			object.document.equals(this.document) &&
	 *     			object.position == this.position &&
	 *     			object.length == this.length
	 * </pre>
	 *
	 * @return if the given object is a reference and equals this reference.
	 * @since 0.2.0 ~2021.01.07
	 */
	@Override
	boolean equals(Object object);

	/**
	 * The hashcode of a reference is calculated as follows.
	 * <pre>
	 *     hashCode = &lt;DocumentHashCode&gt; * &lt;Length&gt; + &lt;Position&gt;
	 * </pre>
	 *
	 * @return the hashCode of this reference.
	 * @since 0.2.0 ~2021.01.07
	 */
	@Override
	int hashCode();

	/**
	 * Returns a string representation of this reference.
	 * <pre>
	 *     toString = &lt;DocumentToString&gt; [&lt;Position&gt;, &lt;Length&gt;]
	 * </pre>
	 *
	 * @return a string representation of this reference.
	 * @since 0.2.0 ~2021.01.06
	 */
	@Override
	String toString();

	/**
	 * Return the content of this reference as a string. This method should always return
	 * the same value.
	 *
	 * @return the content of this reference. (unmodifiable)
	 * @throws IllegalStateException if this reference is a deserialized reference.
	 * @throws IOError               if any I/O exception occurs.
	 * @since 0.2.0 ~2021.01.08
	 */
	CharSequence content();

	/**
	 * The document that this reference is from.
	 *
	 * @return the document of this reference.
	 * @since 0.2.0 ~2021.01.07
	 */
	Document document();

	/**
	 * The length of this reference. Must always return the same value. Must always be the
	 * same as the length of the content of this source.
	 *
	 * @return the length of the content of this reference.
	 * @since 0.2.0 ~2021.01.10
	 */
	int length();

	/**
	 * Return the line number of this reference.
	 *
	 * @return the line number of this reference.
	 * @since 0.2.0 ~2021.01.26
	 */
	int line();

	/**
	 * Return a reference for the whole line this reference has occurred at.
	 *
	 * @return a reference of the whole line of this.
	 * @throws IllegalStateException if this reference is a deserialized reference.
	 * @since 0.2.0 ~2021.01.27
	 */
	Reference lineReference();

	/**
	 * The parent reference of this reference.
	 *
	 * @return the parent reference of this reference. Or null if this reference has no
	 * 		parent.
	 * @throws IllegalStateException if this reference is a deserialized reference.
	 * @since 0.2.0 ~2021.01.08
	 */
	Reference parent();

	/**
	 * Return where this reference starts at its {@link #document()}.
	 *
	 * @return the position of this reference.
	 * @since 0.2.0 ~2021.01.04
	 */
	int position();

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
	Reference subReference(int position);

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
	Reference subReference(int position, int length);
}
