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
	 * Calculate how much dominant the area [s, e) over the area [i, j).
	 *
	 * @param i the first index of the first area.
	 * @param j one past the last index of the first area.
	 * @param s the first index of the second area.
	 * @param e one past the last index of the second area.
	 * @return how much dominant the second area over the first area.
	 * @throws IllegalArgumentException if {@code i} is not in the range {@code [0, j]} or
	 *                                  if {@code s} is not in the range {@code [0, e]}.
	 * @since 0.0.2 ~2021.01.10
	 */
	@SuppressWarnings("OverlyComplexMethod")
	static Dominance dominance(int i, int j, int s, int e) {
		if (i < 0 && s < 0 && i > j && s > e)
			throw new IllegalArgumentException("Illegal Indices");
		return i == s && j == e ?
			   Dominance.EXACT :
			   j == s || i == e || j < s || e < i ?
			   Dominance.NONE :
			   i < s && e < j || i == s && e < j || i < s && j == e ?
			   Dominance.PART :
			   s < i && j < e || i == s /*&& j < e*/ || s < i && j == e ?
			   Dominance.CONTAIN :
			   //i < s && s < j && j < e || s < i && i < e && e < j ?
			   Dominance.SHARE;
	}

	/**
	 * Calculate how much dominant the {@code other} source over the given {@code
	 * source}.
	 *
	 * @param source the first source.
	 * @param other  the second source.
	 * @return how much dominant the second source over the first source.
	 * @throws NullPointerException if the given {@code source} or {@code other} is null.
	 * @since 0.0.2 ~2021.01.10
	 */
	static Dominance dominance(Source<?> source, Source<?> other) {
		Objects.requireNonNull(source, "source");
		Objects.requireNonNull(other, "other");
		int i = source.position();
		int j = i + source.length();
		int s = other.position();
		int e = s + other.length();
		return Source.dominance(i, j, s, e);
	}

	/**
	 * Calculate what is the relation between the areas {@code [i, j)} and {@code [s,
	 * e)}.
	 * <br>
	 * The first area is the area to compare the second area with. The returned relation
	 * will be the relation describing the feelings of the first area about the second
	 * area.
	 * <br>
	 * For example: if the second area is contained in the middle of first area, then the
	 * retaliation {@link Relation#FRAGMENT fragmnet} will be returned.
	 *
	 * @param i the first index of the first area.
	 * @param j one past the last index of the first area.
	 * @param s the first index of the second area.
	 * @param e one past the last index of the second area.
	 * @return the relation constant describing the relation of the second area to the
	 * 		first area.
	 * @throws IllegalArgumentException if {@code i} is not in the range {@code [0, j]} or
	 *                                  if {@code s} is not in the range {@code [0, e]}.
	 * @since 0.0.2 ~2021.01.10
	 */
	@SuppressWarnings("OverlyComplexMethod")
	static Relation relation(int i, int j, int s, int e) {
		if (i < 0 && s < 0 && i > j && s > e)
			throw new IllegalArgumentException("Illegal Indices");
		return j == s ?
			   Relation.NEXT :
			   i == e ?
			   Relation.PREVIOUS :
			   j < s ?
			   Relation.AFTER :
			   e < i ?
			   Relation.BEFORE :
			   s < i && j < e ?
			   Relation.CONTAINER :
			   i == s && j < e ?
			   Relation.AHEAD :
			   s < i && j == e ?
			   Relation.BEHIND :
			   i == s && j == e ?
			   Relation.SAME :
			   i < s && e < j ?
			   Relation.FRAGMENT :
			   i == s /*&& e < j*/ ?
			   Relation.START :
			   i < s && j == e ?
			   Relation.END :
			   i < s /*&& s < j && j < e*/ ?
			   Relation.OVERFLOW :
			   //s < i && i < e && e < j ?
			   Relation.UNDERFLOW;
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
	 * @since 0.0.2 ~2021.01.10
	 */
	static Relation relation(Source<?> source, Source<?> other) {
		Objects.requireNonNull(source, "source");
		Objects.requireNonNull(other, "other");
		int i = source.position();
		int j = i + source.length();
		int s = other.position();
		int e = s + other.length();
		return Source.relation(i, j, s, e);
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
	Source<? extends D> parent();

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
	Source<? extends D> root();

	/**
	 * Slice this source from the given {@code pos} to the end of this {@code source}.
	 *
	 * @param pos the pos where the new slice source will have.
	 * @return a slice of this source that starts from the given {@code pos}.
	 * @throws IllegalArgumentException  if the given {@code pos} is negative.
	 * @throws IndexOutOfBoundsException if {@code pos > this.length()}.
	 * @since 0.0.2 ~2021.01.6
	 */
	Source<? extends D> slice(int pos);

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
	Source<? extends D> slice(int pos, int len);

	/**
	 * An enumeration of how dominant a relation is over another relation.
	 *
	 * @author LSafer
	 * @version 0.0.2
	 * @since 0.0.2 ~2021.01.10
	 */
	enum Dominance {
		/**
		 * <b>Container</b> {@link #PART (opposite)}
		 * <br>
		 * Defines that a source that have the relation contains the other source in it
		 * (but not exact).
		 * <pre>
		 *     ...|---|...
		 *     .<------->.
		 *     <br>
		 *     .|--->.....
		 *     .|------->.
		 *     <br>
		 *     .....<---|.
		 *     .<-------|.
		 * </pre>
		 * <pre>
		 *     (s < i <= j < e)
		 *     (s == i <= j < e)
		 *     (s < i <= j == e)
		 * </pre>
		 * <pre>
		 *     {@code {s < i} & {j < e}}
		 *     {@code {i == s} & {j < e}}
		 *     {@code {s < i} & {j == e}}
		 * </pre>
		 *
		 * @see Relation#CONTAINER
		 * @see Relation#AHEAD
		 * @see Relation#BEHIND
		 * @since 0.0.2 ~2021.01.10
		 */
		CONTAIN("PART"),
		/**
		 * <b>Mirror</b> {@link #EXACT (opposite)}
		 * <br>
		 * Defines that a source that have the relation has the same relation as the other
		 * source.
		 * <pre>
		 *     ...|---|...
		 *     ...|---|...
		 * </pre>
		 * <pre>
		 *     (i == s <= j == e)
		 * </pre>
		 * <pre>
		 *     {i == s} & {j == e}
		 * </pre>
		 *
		 * @see Relation#SAME
		 * @since 0.0.2 ~2021.01.10
		 */
		EXACT("EXACT"),
		/**
		 * <b>Clash</b> {@link #SHARE (opposite)}
		 * <br>
		 * Defines that a source that have the relation shares some (but not all) of the
		 * other source and vice versa.
		 * <pre>
		 *     .<----|....
		 *     ....|---->.
		 *     <br>
		 *     ....|---->.
		 *     .<----|....
		 * </pre>
		 * <pre>
		 *     (i < s < j < e)
		 *     (s < i < e < j)
		 * </pre>
		 * <pre>
		 *     {i < s} & {s < j} & {j < e}
		 *     {s < i} & {i < e} & {e < j}
		 * </pre>
		 *
		 * @see Relation#OVERFLOW
		 * @see Relation#UNDERFLOW
		 * @since 0.0.2 ~2021.01.10
		 */
		SHARE("SHARE"),
		/**
		 * <b>Slice</b> {@link #CONTAIN (opposite)}
		 * <br>
		 * Defines that a source that have the relation shares some (but not all) of the
		 * other source. (one but not both)
		 * <pre>
		 *     .<------->.
		 *     ...|---|...
		 *     <br>
		 *     .|------->.
		 *     .|--->.....
		 *     <br>
		 *     .<-------|.
		 *     .....<---|.
		 * </pre>
		 * <pre>
		 *     (i < s <= e < j)
		 *     (i == s <= e < j)
		 *     (i < s <= e == j)
		 * </pre>
		 * <pre>
		 *     {i < s} & {e < j}
		 *     {i == s} & {e < j}
		 *     {i < s} & {j == e}
		 * </pre>
		 *
		 * @see Relation#FRAGMENT
		 * @see Relation#START
		 * @see Relation#END
		 * @since 0.0.2 ~2021.01.10
		 */
		PART("CONTAIN"),
		/**
		 * <b>Stranger</b> {@link #NONE (opposite)}
		 * <br>
		 * Defines that a source that have the relation shares none of the other source.
		 * <pre>
		 *     .<---|.....
		 *     .....|--->.
		 *     <br>
		 *     .....|--->.
		 *     .<---|.....
		 *     <br>
		 *     .<--|......
		 *     ......|-->.
		 *     <br>
		 *     ......|-->.
		 *     .<--|......
		 * </pre>
		 * <pre>
		 *     (i < j == s < e)
		 *     (s < e == i < j)
		 *     (i <= j < s <= e)
		 *     (s <= e < i <= j)
		 * </pre>
		 * <pre>
		 *     {j == s}
		 *     {i == e}
		 *     {j < s}
		 *     {e < i}
		 * </pre>
		 *
		 * @see Relation#NEXT
		 * @see Relation#PREVIOUS
		 * @see Relation#AFTER
		 * @see Relation#BEFORE
		 * @since 0.0.2 ~2021.01.10
		 */
		NONE("NONE");

		/**
		 * The opposite dominance type of this.
		 *
		 * @since 0.0.2 ~2021.01.10
		 */
		private final String opposite;

		/**
		 * Construct a new dominance type.
		 *
		 * @param opposite the name of the dominance type that is opposite of this
		 *                 dominance type.
		 * @throws NullPointerException if the given {@code opposite} is null.
		 * @since 0.0.2 ~2021.01.10
		 */
		Dominance(String opposite) {
			Objects.requireNonNull(opposite, "opposite");
			this.opposite = opposite;
		}

		/**
		 * Get the opposite dominance type of this dominance type.
		 *
		 * @return the opposite dominance type.
		 * @since 0.0.2 ~2021.01.10
		 */
		public Dominance opposite() {
			return Dominance.valueOf(this.opposite);
		}
	}

	/**
	 * An enumeration of possible relations between sources.
	 * <br>
	 * The names is describing the other source (in THE source perspective.
	 * <pre>
	 * 		SAME
	 * 			i == s <= j == e
	 * 			...|~~~|...
	 * 			...|~~~|...
	 * 		FRAGMENT | CONTAINER
	 * 			i < s <= e < j
	 * 			.<--~~~-->.
	 * 			...|~~~|...
	 * 		START | AHEAD
	 * 			i == s <= e < j
	 * 			.|----~~~>.
	 * 			.|--->.....
	 * 		END | BEHIND
	 * 			i < s <= e == j
	 * 			.<~~~----|.
	 * 			.....<---|.
	 * 		OVERFLOW | UNDERFLOW
	 * 			i < s < j < e
	 * 			.<~~--|....
	 * 			....|--~~>.
	 * 		NEXT | PREVIOUS
	 * 			i < j == s < e
	 * 			.<---|.....
	 * 			.....|--->.
	 * 		AFTER | BEFORE
	 * 			i <= j < s <= e
	 * 			.<~~|......
	 * 			......|~~>.
	 * </pre>
	 *
	 * @author LSafer
	 * @version 0.0.2
	 * @since 0.0.2 ~2021.01.9
	 */
	enum Relation {
		/**
		 * <b>Containing Source</b> {@link #FRAGMENT (opposite)}
		 * <br>
		 * When the bounds of the source are contained in the bounds of the other source
		 * but do not touch.
		 * <pre>
		 *     ...|---|...
		 *     .<------->.
		 * </pre>
		 * <pre>
		 *     (s < i <= j < e)
		 * </pre>
		 * <pre>
		 *     {s < i} & {j < e}
		 * </pre>
		 *
		 * @see Dominance#CONTAIN
		 * @since 0.0.2 ~2021.01.9
		 */
		CONTAINER("FRAGMENT", Source.Dominance.CONTAIN),

		/**
		 * <b>This And Ahead</b> {@link #START (opposite)}
		 * <br>
		 * When the source is contained at the very start of the other source.
		 * <pre>
		 *     .|--->.....
		 *     .|------->.
		 * </pre>
		 * <pre>
		 *     (s == i <= j < e)
		 * </pre>
		 * <pre>
		 *     {i == s} & {j < e}
		 * </pre>
		 *
		 * @see Dominance#CONTAIN
		 * @since 0.0.2 ~2021.01.9
		 */
		AHEAD("START", Source.Dominance.CONTAIN),
		/**
		 * <b>This And Behind</b> {@link #END (opposite)}
		 * <br>
		 * When the source is contained at the very end of the other source.
		 * <pre>
		 *     .....<---|.
		 *     .<-------|.
		 * </pre>
		 * <pre>
		 *     (s < i <= j == e)
		 * </pre>
		 * <pre>
		 *     {s < i} & {j == e}
		 * </pre>
		 *
		 * @see Dominance#CONTAIN
		 * @since 0.0.2 ~2021.01.9
		 */
		BEHIND("END", Source.Dominance.CONTAIN),

		/**
		 * <b>Same Source</b> {@link #SAME (opposite)}
		 * <br>
		 * When the source has the exact bounds as the other source.
		 * <pre>
		 *     ...|---|...
		 *     ...|---|...
		 * </pre>
		 * <pre>
		 *     (i == s <= j == e)
		 * </pre>
		 * <pre>
		 *     {i == s} & {j == e}
		 * </pre>
		 *
		 * @see Dominance#EXACT
		 * @since 0.0.2 ~2021.01.9
		 */
		SAME("SAME", Source.Dominance.EXACT),

		/**
		 * <b>Overflowed Slice</b> {@link #UNDERFLOW (opposite)}
		 * <br>
		 * When the first fragment of the source is before the other source but the second
		 * fragment is in it. (without any bound been exactly at another bound)
		 * <pre>
		 *     .<----|....
		 *     ....|---->.
		 * </pre>
		 * <pre>
		 *     (i < s < j < e)
		 * </pre>
		 * <pre>
		 *     {i < s} & {s < j} & {j < e}
		 * </pre>
		 *
		 * @see Dominance#SHARE
		 * @since 0.0.2 ~2021.01.9
		 */
		OVERFLOW("UNDERFLOW", Source.Dominance.SHARE),
		/**
		 * <b>Underflowed Slice</b> {@link #OVERFLOW (opposite)}
		 * <br>
		 * When the first fragment of the source is in the other source but the second
		 * fragment is after it. (without any bound been exactly at another bound)
		 * <pre>
		 *     ....|---->.
		 *     .<----|....
		 * </pre>
		 * <pre>
		 *     (s < i < e < j)
		 * </pre>
		 * <pre>
		 *     {s < i} & {i < e} & {e < j}
		 * </pre>
		 *
		 * @see Dominance#SHARE
		 * @since 0.0.2 ~2021.01.9
		 */
		UNDERFLOW("OVERFLOW", Dominance.SHARE),

		/**
		 * <b>Fragment Source</b> {@link #CONTAINER (opposite)}
		 * <br>
		 * When the source has its bounds containing the bounds of the other source but do
		 * not touch.
		 * <pre>
		 *     .<------->.
		 *     ...|---|...
		 * </pre>
		 * <pre>
		 *     (i < s <= e < j)
		 * </pre>
		 * <pre>
		 *     {i < s} & {e < j}
		 * </pre>
		 *
		 * @see Dominance#PART
		 * @since 0.0.2 ~2021.01.9
		 */
		FRAGMENT("CONTAINER", Source.Dominance.PART),

		/**
		 * <b>At The Start</b> {@link #AHEAD (opposite)}
		 * <br>
		 * When the source contains the other source at its start.
		 * <pre>
		 *     .|------->.
		 *     .|--->.....
		 * </pre>
		 * <pre>
		 *     (i == s <= e < j)
		 * </pre>
		 * <pre>
		 *     {i == s} & {e < j}
		 * </pre>
		 *
		 * @see Dominance#PART
		 * @since 0.0.2 ~2021.01.9
		 */
		START("AHEAD", Source.Dominance.PART),
		/**
		 * <b>At The End</b> {@link #BEHIND (opposite)}
		 * <br>
		 * When the source contains the other source at its end.
		 * <pre>
		 *     .<-------|.
		 *     .....<---|.
		 * </pre>
		 * <pre>
		 *     (i < s <= e == j)
		 * </pre>
		 * <pre>
		 *     {i < s} & {j == e}
		 * </pre>
		 *
		 * @see Dominance#PART
		 * @since 0.0.2 ~2021.01.9
		 */
		END("BEHIND", Source.Dominance.PART),

		/**
		 * <b>Next Source</b> {@link #PREVIOUS (opposite)}
		 * <br>
		 * When the source is followed immediately by the other source.
		 * <pre>
		 *     .<---|.....
		 *     .....|--->.
		 * </pre>
		 * <pre>
		 *     (i < j == s < e)
		 * </pre>
		 * <pre>
		 *     {j == s}
		 * </pre>
		 *
		 * @see Dominance#NONE
		 * @since 0.0.2 ~2021.01.9
		 */
		NEXT("PREVIOUS", Source.Dominance.NONE),
		/**
		 * <b>Previous Source</b> {@link #NEXT (opposite)}
		 * <br>
		 * When the source has the other source immediately before it.
		 * <pre>
		 *     .....|--->.
		 *     .<---|.....
		 * </pre>
		 * <pre>
		 *     (s < e == i < j)
		 * </pre>
		 * <pre>
		 *     {i == e}
		 * </pre>
		 *
		 * @see Dominance#NONE
		 * @since 0.0.2 ~2021.01.9
		 */
		PREVIOUS("NEXT", Source.Dominance.NONE),

		/**
		 * <b>After The Source</b> {@link #BEFORE opposite}
		 * <br>
		 * When the other source is after the source but not immediately.
		 * <pre>
		 *     .<--|......
		 *     ......|-->.
		 * </pre>
		 * <pre>
		 *     (i <= j < s <= e)
		 * </pre>
		 * <pre>
		 *     {j < s}
		 * </pre>
		 *
		 * @see Dominance#NONE
		 * @since 0.0.2 ~2021.01.9
		 */
		AFTER("BEFORE", Source.Dominance.NONE),
		/**
		 * <b>Before The Source</b> {@link #AFTER opposite}
		 * <br>
		 * When the other source is before the source but not immediately.
		 * <pre>
		 *     ......|-->.
		 *     .<--|......
		 * </pre>
		 * <pre>
		 *     (s <= e < i <= j)
		 * </pre>
		 * <pre>
		 *     {e < i}
		 * </pre>
		 *
		 * @see Dominance#NONE
		 * @since 0.0.2 ~2021.01.9
		 */
		BEFORE("AFTER", Source.Dominance.NONE),
		;

		/**
		 * How dominant this relation over the opposite relation.
		 *
		 * @since 0.0.2 ~2021.01.10
		 */
		private final Source.Dominance dominance;
		/**
		 * The name of the opposite enum.
		 *
		 * @since 0.0.2 ~2021.01.9
		 */
		private final String opposite;

		/**
		 * Construct a new enum with the given {@code opposite} as the name of the
		 * opposite enum of it.
		 *
		 * @param opposite  the name of the opposite enum.
		 * @param dominance how dominant the constructed relation over its opposite
		 *                  relation.
		 * @throws NullPointerException if the given {@code opposite} is null.
		 * @since 0.0.2 ~2021.01.10
		 */
		Relation(String opposite, Source.Dominance dominance) {
			Objects.requireNonNull(opposite, "opposite");
			Objects.requireNonNull(dominance, "dominance");
			this.opposite = opposite;
			this.dominance = dominance;
		}

		/**
		 * Returns how dominance this relation over its opposite relation.
		 *
		 * @return how dominance this relation.
		 * @since 0.0.2 ~2021.01.10
		 */
		public Source.Dominance dominance() {
			return this.dominance;
		}

		/**
		 * Get the opposite relation of this relation.
		 *
		 * @return the opposite relation. Or null if none.
		 * @since 0.0.2 ~2021.01.9
		 */
		public Relation opposite() {
			return Relation.valueOf(this.opposite);
		}

	}
}
