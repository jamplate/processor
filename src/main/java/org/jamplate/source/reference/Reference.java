/*
 *	Copyright 20.2.0021 Cufy
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
 * A source is a component that points to a {@code D} source or a fragment of it.
 * <br>
 * Note: sources are built from top to bottom. So, a typical source will store its parent
 * source but never store any sub-source of it.
 * <br>
 * The source should serialize its {@link #document()}, {@link #position()} and {@link
 * #length()}. It is not encouraged to serialize additional data.
 * <br>
 * If a source is a deserialized source then the methods {@link #content()}, {@link
 * #parent()}, {@link #subReference(int)} and {@link #subReference(int, int)} will throw
 * an {@link IllegalStateException}.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2020.12.25
 */
public interface Reference extends Serializable {
	/**
	 * The standard source comparator.
	 *
	 * @since 0.2.0 ~2021.01.9
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
	 * Important Note: the returned matcher will return {@link Matcher#start()} and {@link
	 * Matcher#end()} as indexes at the original document.
	 *
	 * @param reference the reference to create a matcher for.
	 * @param pattern   the pattern to match.
	 * @return a matcher over the content of the given {@code reference}.
	 * @throws NullPointerException  if the given {@code reference} or {@code pattern} is
	 *                               null.
	 * @throws IllegalStateException if the {@link Reference#document() document} of the
	 *                               given {@code reference} is deserialized.
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
	 * A source equals another object, if that object is a source and has the same {@link
	 * #document()}, {@link #position()} and {@link #length()} of this source.
	 *
	 * @return if the given object is a source and equals this source.
	 * @since 0.2.0 ~2021.01.7
	 */
	@Override
	boolean equals(Object other);

	/**
	 * The hashcode of a source is calculated as follows.
	 * <pre>
	 *     hashCode = {@link #document()}.hashCode() * {@link #length()} + {@link #position()}
	 * </pre>
	 *
	 * @return the hashCode of this source.
	 * @since 0.2.0 ~2021.01.7
	 */
	@Override
	int hashCode();

	/**
	 * Returns a string representation of this source. The source shall follow the below
	 * template:
	 * <pre>
	 *     {@link #document() &lt;document()&gt;} [{@link #position() &lt;position()&gt;}, {@link #length() &lt;length()&gt;}]
	 * </pre>
	 *
	 * @return a string representation of this source.
	 * @since 0.2.0 ~2021.01.6
	 */
	@Override
	String toString();

	/**
	 * Return the content of this source as a string. This method should always return the
	 * same value.
	 *
	 * @return the content of this source. (unmodifiable view)
	 * @throws IllegalStateException if this source is deserialized.
	 * @throws IOError               if any I/O exception occurs.
	 * @since 0.2.0 ~2021.01.8
	 */
	CharSequence content();

	/**
	 * The source document that this source is from.
	 *
	 * @return the document of this source.
	 * @since 0.2.0 ~2021.01.7
	 */
	Document document();

	/**
	 * The length of this source. Must always return the same value. Must always be the
	 * same as the length of the content of this source.
	 *
	 * @return the length of the content of this source.
	 * @since 0.2.0 ~2021.01.10
	 */
	int length();

	/**
	 * The parent source of this source.
	 *
	 * @return the parent source of this source. Or null if this source has no parent.
	 * @throws IllegalStateException if this source is deserialized.
	 * @since 0.2.0 ~2021.01.8
	 */
	Reference parent();

	/**
	 * Return where this source starts at its {@link #document()}.
	 *
	 * @return the position of this source.
	 * @since 0.2.0 ~2021.01.4
	 */
	int position();

	/**
	 * Slice this source from the given {@code position} to the end of this {@code
	 * source}.
	 *
	 * @param position the position where the new slice source will have.
	 * @return a slice of this source that starts from the given {@code position}.
	 * @throws IllegalArgumentException  if the given {@code position} is negative.
	 * @throws IndexOutOfBoundsException if {@code position > this.length()}.
	 * @throws IllegalStateException     if this source is deserialized.
	 * @since 0.2.0 ~2021.01.6
	 */
	Reference subReference(int position);

	/**
	 * Slice this source from the given {@code position} and limit it with the given
	 * {@code length}.
	 *
	 * @param position the position where the new slice source will have.
	 * @param length   the length of the new slice source.
	 * @return a slice of this source that starts from the given {@code position} and have
	 * 		the given {@code length}.
	 * @throws IllegalArgumentException  if the given {@code position} or {@code length}
	 *                                   is negative.
	 * @throws IndexOutOfBoundsException if {@code position + length > this.length()}.
	 * @throws IllegalStateException     if this source is deserialized.
	 * @since 0.2.0 ~2021.01.6
	 */
	Reference subReference(int position, int length);
}
