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
package org.jamplate.model.source;

import java.util.Comparator;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * A source is a component that points to a {@code D} source or a fragment of it.
 * <br>
 * Note: sources are built from top to bottom. So, a typical source will store its parent
 * source but never store any sub-source of it.
 *
 * @param <D> the type of the actual source of this source.
 * @author LSafer
 * @version 0.0.2
 * @since 0.0.2 ~2020.12.25
 */
public interface Source<D extends Comparable> {
	/**
	 * The standard source comparator.
	 *
	 * @since 0.0.2 ~2021.01.9
	 */
	Comparator<Source> COMPARATOR = Comparator.<Source, Comparable>comparing(Source::document)
			.thenComparingInt(Source::position)
			.thenComparingInt(Source::length);

	/**
	 * Return a source that has the range {@code [j, e)}.
	 * <br>
	 * Note that the resultant source is always taken from the first given source as
	 * follows:
	 * <pre>
	 *     source.root().slice(j, e - j)
	 * </pre>
	 *
	 * @param source the first source.
	 * @param other  the second source.
	 * @return a source that has the range {@code [source.position + source.length,
	 * 		other.position + other.length)}.
	 * @throws NullPointerException     if the given {@code source} or {@code other} is
	 *                                  null.
	 * @throws IllegalArgumentException if {@code source.position + source.length} is more
	 *                                  than {@code other.position + other.length}. Or if
	 *                                  the given source are from different documents.
	 * @since 0.0.2 ~2021.01.12
	 */
	static Source<?> cutEndEnd(Source<?> source, Source<?> other) {
		Objects.requireNonNull(source, "source");
		Objects.requireNonNull(other, "other");
		if (!Objects.equals(source.document(), other.document()))
			throw new IllegalArgumentException("Different Document");
		int j = source.position() + source.length();
		int e = other.position() + other.length();
		if (j > e)
			throw new IllegalArgumentException("Reverse Cut");
		return source.root().slice(j, e - j);
	}

	/**
	 * Return a source that has the range {@code [j, s)}.
	 * <br>
	 * Note that the resultant source is always taken from the first given source as
	 * follows:
	 * <pre>
	 *     source.root().slice(j, s - j)
	 * </pre>
	 *
	 * @param source the first source.
	 * @param other  the second source.
	 * @return a source that has the range {@code [source.position + source.length,
	 * 		other.position)}.
	 * @throws NullPointerException     if the given {@code source} or {@code other} is
	 *                                  null.
	 * @throws IllegalArgumentException if {@code source.position + source.length} is more
	 *                                  than {@code other.position}. Or if the given
	 *                                  source are from different documents.
	 * @since 0.0.2 ~2021.01.12
	 */
	static Source<?> cutEndStart(Source<?> source, Source<?> other) {
		Objects.requireNonNull(source, "source");
		Objects.requireNonNull(other, "other");
		if (!Objects.equals(source.document(), other.document()))
			throw new IllegalArgumentException("Different Document");
		int j = source.position() + source.length();
		int s = other.position();
		if (j > s)
			throw new IllegalArgumentException("Reverse Cut");
		return source.root().slice(j, s - j);
	}

	/**
	 * Return a source that has the range {@code [i, e)}.
	 * <br>
	 * Note that the resultant source is always taken from the first given source as
	 * follows:
	 * <pre>
	 *     source.root().slice(i, e - i)
	 * </pre>
	 *
	 * @param source the first source.
	 * @param other  the second source.
	 * @return a source that has the range {@code [source.position, other.position +
	 * 		other.length)}.
	 * @throws NullPointerException     if the given {@code source} or {@code other} is
	 *                                  null.
	 * @throws IllegalArgumentException if {@code source.position} is more than {@code
	 *                                  other.position + other.length}. Or if the given
	 *                                  source are from different documents.
	 * @since 0.0.2 ~2021.01.12
	 */
	static Source<?> cutStartEnd(Source<?> source, Source<?> other) {
		Objects.requireNonNull(source, "source");
		Objects.requireNonNull(other, "other");
		if (!Objects.equals(source.document(), other.document()))
			throw new IllegalArgumentException("Different Document");
		int i = source.position();
		int e = other.position() + other.length();
		if (i > e)
			throw new IllegalArgumentException("Reverse Cut");
		return source.root().slice(i, e - i);
	}

	/**
	 * Return a source that has the range {@code [i, s)}.
	 * <br>
	 * Note that the resultant source is always taken from the first given source as
	 * follows:
	 * <pre>
	 *     source.root().slice(i, s - i)
	 * </pre>
	 *
	 * @param source the first source.
	 * @param other  the second source.
	 * @return a source that has the range {@code [source.position, other.position)}.
	 * @throws NullPointerException     if the given {@code source} or {@code other} is
	 *                                  null.
	 * @throws IllegalArgumentException if {@code source.position} is more than {@code
	 *                                  other.position}. Or if the given source are from
	 *                                  different documents.
	 * @since 0.0.2 ~2021.01.12
	 */
	static Source<?> cutStartStart(Source<?> source, Source<?> other) {
		Objects.requireNonNull(source, "source");
		Objects.requireNonNull(other, "other");
		if (!Objects.equals(source.document(), other.document()))
			throw new IllegalArgumentException("Different document");
		int i = source.position();
		int s = other.position();
		if (i > s)
			throw new IllegalArgumentException("Reverse Cut");
		return source.root().slice(i, s - i);
	}

	/**
	 * Calculate how much dominant the area {@code [s, e)} is over the given {@code
	 * source}.
	 *
	 * @param source the source (first area).
	 * @param s      the first index of the second area.
	 * @param e      one past the last index of the second area.
	 * @return how much dominant the second area over the given {@code source}.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if {@code s} is not in the range {@code [0, e]}.
	 * @see Dominance#compute(int, int, int, int)
	 * @since 0.0.2 ~2021.01.11
	 */
	static Dominance dominance(Source<?> source, int s, int e) {
		Objects.requireNonNull(source, "source");
		int i = source.position();
		int j = i + source.length();
		return Dominance.compute(i, j, s, e);
	}

	/**
	 * Calculate how much dominant the {@code other} source over the given {@code
	 * source}.
	 *
	 * @param source the first source.
	 * @param other  the second source.
	 * @return how much dominant the second source over the first source.
	 * @throws NullPointerException if the given {@code source} or {@code other} is null.
	 * @see Dominance#compute(int, int, int, int)
	 * @since 0.0.2 ~2021.01.10
	 */
	static Dominance dominance(Source<?> source, Source<?> other) {
		Objects.requireNonNull(source, "source");
		Objects.requireNonNull(other, "other");
		int i = source.position();
		int j = i + source.length();
		int s = other.position();
		int e = s + other.length();
		return Dominance.compute(i, j, s, e);
	}

	/**
	 * Calculate what is the relation between the given {@code source} and the given area
	 * {@code [s, e)}.
	 * <br>
	 * The given {@code source} is the source to compare the second area with. The
	 * returned relation will be the relation describing the feelings of the source about
	 * the second area.
	 * <br>
	 * For example: if the second area is contained in the middle of the source, then the
	 * retaliation {@link Relation#FRAGMENT fragmnet} will be returned.
	 *
	 * @param source the source (first area).
	 * @param s      the first index of the second area.
	 * @param e      one past the last index of the second area.
	 * @return the relation constant describing the relation of the second area to the
	 * 		source.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if {@code s} is not in the range {@code [0, e]}.
	 * @see Relation#compute(int, int, int, int)
	 * @since 0.0.2 ~2021.01.10
	 */
	static Relation relation(Source<?> source, int s, int e) {
		Objects.requireNonNull(source, "source");
		int i = source.position();
		int j = i + source.length();
		return Relation.compute(i, j, s, e);
	}

	/**
	 * Calculate what is the relation between the sources {@code source} and {@code
	 * other}.
	 * <br>
	 * The first source is the source to compare the second source with. The returned
	 * relation will be the relation describing the feelings of the first source about the
	 * second source.
	 * <br>
	 * For example: if the second source is contained in the middle of first source, then
	 * the retaliation {@link Relation#FRAGMENT fragmnet} will be returned.
	 *
	 * @param source the first source.
	 * @param other  the second source.
	 * @return the relation constant describing the relation of the second source to the
	 * 		first source.
	 * @throws NullPointerException if the given {@code source} or {@code other} is null.
	 * @see Relation#compute(int, int, int, int)
	 * @since 0.0.2 ~2021.01.10
	 */
	static Relation relation(Source<?> source, Source<?> other) {
		Objects.requireNonNull(source, "source");
		Objects.requireNonNull(other, "other");
		int i = source.position();
		int j = i + source.length();
		int s = other.position();
		int e = s + other.length();
		return Relation.compute(i, j, s, e);
	}

	/**
	 * A source equals another object, if that object is a source and has the same {@link
	 * #document()}, {@link #position()} and {@link #content()}{@link String#length()
	 * .length()} of this source.
	 *
	 * @return if the given object is a source and equals this source.
	 * @since 0.0.2 ~2021.01.7
	 */
	@Override
	boolean equals(Object other);

	/**
	 * The hashcode of a source is calculated as follows.
	 * <pre>
	 *     hashCode = {@link #document()}.hashCode() * {@link #content()}{@link String#length() .length()} + {@link #position()}
	 * </pre>
	 *
	 * @return the hashCode of this source.
	 * @since 0.0.2 ~2021.01.7
	 */
	@Override
	int hashCode();

	/**
	 * Returns a string representation of this source. The source shall follow the below
	 * template:
	 * <pre>
	 *     {@link #document() &lt;document()&gt;} [{@link #position() &lt;position()&gt;}, {@link #content() &lt;content()}{@link String#length() .length()&gt;}]
	 * </pre>
	 *
	 * @return a string representation of this source.
	 * @since 0.0.2 ~2021.01.6
	 */
	@Override
	String toString();

	/**
	 * Return the content of this source as a string. Once a source got constructed, it
	 * will capture its content and never change it.
	 *
	 * @return the content of this source.
	 * @since 0.0.2 ~2021.01.8
	 */
	CharSequence content();

	/**
	 * The source document that this source is from.
	 *
	 * @return the document of this source.
	 * @since 0.0.2 ~2021.01.7
	 */
	D document();

	/**
	 * Find every source that matches the given {@code regex} in this source.
	 * <br>
	 * This method is relevant to invoking {@link #find(Pattern)} with the results of
	 * {@link Pattern#compile(String)} with the given {@code regex}.
	 *
	 * @param regex the regex to be applied.
	 * @return a set of sub-sources of this source that matches the given {@code regex}.
	 * @throws NullPointerException   if the given {@code regex} is null.
	 * @throws PatternSyntaxException if hte given {@code regex} has regex syntax errors.
	 * @since 0.0.2 ~2021.01.11
	 */
	Set<Source<D>> find(String regex);

	/**
	 * Find every source that matches the given {@code pattern} in this source.
	 *
	 * @param pattern the pattern to be applied.
	 * @return a set of sub-sources of this source that matches the given {@code pattern}.
	 * @throws NullPointerException if the given {@code pattern} is null.
	 * @since 0.0.2 ~2021.01.11
	 */
	Set<Source<D>> find(Pattern pattern);

	/**
	 * Find every source that is between a {@code startRegex} match and {@code endRegex}
	 * end.
	 *
	 * @param startRegex the regex to match the start sequence.
	 * @param endRegex   the regex to match the end sequence.
	 * @return a set of sources that have a starting pattern matching the given {@code
	 * 		startRegex} and an ending pattering matching the given {@code endRegex}.
	 * @throws NullPointerException   if the given {@code startRegex} or {@code endRegex}
	 *                                is null.
	 * @throws PatternSyntaxException if the given {@code startRegex} or {@code endRegex}
	 *                                has regex syntax errors.
	 * @since 0.0.2 ~2021.01.11
	 */
	Set<Source<D>> find(String startRegex, String endRegex);

	/**
	 * Find every source that is between a {@code startPattern} match and {@code
	 * endPattern} end.
	 *
	 * @param startPattern the pattern to match the start sequence.
	 * @param endPattern   the pattern to match the end sequence.
	 * @return a set of sources that have a starting pattern matching the given {@code
	 * 		startPattern} and an ending pattering matching the given {@code endPattern}.
	 * @throws NullPointerException if the given {@code startPattern} or {@code
	 *                              endPattern} is null.
	 * @since 0.0.2 ~2021.01.11
	 */
	Set<Source<D>> find(Pattern startPattern, Pattern endPattern);

	/**
	 * A shortcut for invoking {@link #content()}{@link CharSequence#length() .length()}.
	 * It is encouraged to use this shortcut when the caller only cares about the length
	 * of the content.
	 * <br>
	 * <br>
	 * Invoking this method SHOULD be the same as invoking:
	 * <pre>
	 *     {@link #content()}{@link CharSequence#length() .length()}
	 * </pre>
	 *
	 * @return the length of the content of this source.
	 * @since 0.0.2 ~2021.01.10
	 */
	int length();

	/**
	 * The parent source of this source.
	 *
	 * @return the parent source of this source. Or null if this source is a root source.
	 * @since 0.0.2 ~2021.01.8
	 */
	Source<D> parent();

	/**
	 * Return where this source starts at its {@link #document()} file}.
	 *
	 * @return the position of this source.
	 * @since 0.0.2 ~2021.01.4
	 */
	int position();

	/**
	 * The root source of this source, the source that points to the whole file of this
	 * source.
	 * <br>
	 * Note: it is better to get the root source than to create a new one. The reason is
	 * that the root source of this source will have its {@link #content()} haven't
	 * changed since its construction.
	 *
	 * @return the root source of this source. Or this source if this source is a root
	 * 		source.
	 * @since 0.0.2 ~2021.01.8
	 */
	Source<D> root();

	/**
	 * Slice this source from the given {@code pos} to the end of this {@code source}.
	 *
	 * @param pos the pos where the new slice source will have.
	 * @return a slice of this source that starts from the given {@code pos}.
	 * @throws IllegalArgumentException  if the given {@code pos} is negative.
	 * @throws IndexOutOfBoundsException if {@code pos > this.length()}.
	 * @since 0.0.2 ~2021.01.6
	 */
	Source<D> slice(int pos);

	/**
	 * Slice this source from the given {@code pos} and limit it with the given {@code
	 * len}.
	 *
	 * @param pos the pos where the new slice source will have.
	 * @param len the len of the new slice source.
	 * @return a slice of this source that starts from the given {@code pos} and have the
	 * 		given {@code len}.
	 * @throws IllegalArgumentException  if the given {@code pos} or {@code len} is
	 *                                   negative.
	 * @throws IndexOutOfBoundsException if {@code pos + len > this.len()}.
	 * @since 0.0.2 ~2021.01.6
	 */
	Source<D> slice(int pos, int len);
}
