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
package org.jamplate.model;

import org.jamplate.model.reference.Reference;
import org.jamplate.model.sketch.Sketch;

import java.util.Objects;

/**
 * An enumeration of possible relations between sources.
 * <br>
 * The names is describing the other source.
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
 * @version 0.2.0
 * @since 0.2.0 ~2021.01.09
 */
public enum Relation {
	/**
	 * <b>Containing Source</b> {@link #FRAGMENT (opposite)}
	 * <br>
	 * When the bounds of the source are contained in the bounds of the other source but
	 * do not touch.
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
	 * @since 0.2.0 ~2021.01.09
	 */
	CONTAINER("FRAGMENT", Dominance.CONTAIN),
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
	 * @since 0.2.0 ~2021.01.09
	 */
	AHEAD("START", Dominance.CONTAIN),
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
	 * @since 0.2.0 ~2021.01.09
	 */
	BEHIND("END", Dominance.CONTAIN),

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
	 * @since 0.2.0 ~2021.01.09
	 */
	SAME("SAME", Dominance.EXACT),

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
	 * @since 0.2.0 ~2021.01.09
	 */
	OVERFLOW("UNDERFLOW", Dominance.SHARE),
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
	 * @since 0.2.0 ~2021.01.09
	 */
	UNDERFLOW("OVERFLOW", Dominance.SHARE),

	/**
	 * <b>Fragment Source</b> {@link #CONTAINER (opposite)}
	 * <br>
	 * When the source has its bounds containing the bounds of the other source but do not
	 * touch.
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
	 * @since 0.2.0 ~2021.01.09
	 */
	FRAGMENT("CONTAINER", Dominance.PART),
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
	 * @since 0.2.0 ~2021.01.09
	 */
	START("AHEAD", Dominance.PART),
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
	 * @since 0.2.0 ~2021.01.09
	 */
	END("BEHIND", Dominance.PART),

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
	 * @since 0.2.0 ~2021.01.09
	 */
	NEXT("PREVIOUS", Dominance.NONE),
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
	 * @since 0.2.0 ~2021.01.09
	 */
	PREVIOUS("NEXT", Dominance.NONE),
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
	 * @since 0.2.0 ~2021.01.09
	 */
	AFTER("BEFORE", Dominance.NONE),
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
	 * @since 0.2.0 ~2021.01.09
	 */
	BEFORE("AFTER", Dominance.NONE),

	/**
	 * <b>Parallel Universe</b> {@link #PARALLEL (opposite)}
	 * <br>
	 * When the two source found to be from different document.
	 *
	 * @see Dominance#NONE
	 * @since 0.2.0 ~2021.01.26
	 */
	PARALLEL("PARALLEL", Dominance.NONE),
	;

	/**
	 * How dominant this relation over the opposite relation.
	 *
	 * @since 0.2.0 ~2021.01.10
	 */
	private final Dominance dominance;
	/**
	 * The name of the opposite enum.
	 *
	 * @since 0.2.0 ~2021.01.09
	 */
	private final String opposite;

	/**
	 * Construct a new enum with the given {@code opposite} as the name of the opposite
	 * enum of it.
	 *
	 * @param opposite  the name of the opposite enum.
	 * @param dominance how dominant the constructed relation over its opposite relation.
	 * @throws NullPointerException if the given {@code opposite} is null.
	 * @since 0.2.0 ~2021.01.10
	 */
	Relation(String opposite, Dominance dominance) {
		Objects.requireNonNull(opposite, "opposite");
		Objects.requireNonNull(dominance, "dominance");
		this.opposite = opposite;
		this.dominance = dominance;
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
	 * @since 0.2.0 ~2021.01.10
	 */
	@SuppressWarnings("OverlyComplexMethod")
	public static Relation compute(int i, int j, int s, int e) {
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
	 * Calculate what is the relation between the given {@code reference} and the given
	 * area {@code [s, e)}.
	 *
	 * @param reference the reference (first area).
	 * @param s         the first index of the second area.
	 * @param e         one past the last index of the second area.
	 * @return the relation constant describing the relation of the second area to the
	 * 		reference.
	 * @throws NullPointerException     if the given {@code reference} is null.
	 * @throws IllegalArgumentException if {@code s} is not in the range {@code [0, e]}.
	 * @see Relation#compute(int, int, int, int)
	 * @since 0.2.0 ~2021.01.10
	 */
	public static Relation compute(Reference reference, int s, int e) {
		Objects.requireNonNull(reference, "reference");
		int i = reference.position();
		int j = i + reference.length();
		return Relation.compute(i, j, s, e);
	}

	/**
	 * Calculate the relation between the references {@code reference} and {@code other}.
	 *
	 * @param reference the first reference.
	 * @param other     the second reference.
	 * @return the relation constant describing the relation of the second reference to
	 * 		the first reference.
	 * @throws NullPointerException if the given {@code reference} or {@code other} is
	 *                              null.
	 * @see Relation#compute(int, int, int, int)
	 * @since 0.2.0 ~2021.01.10
	 */
	public static Relation compute(Reference reference, Reference other) {
		Objects.requireNonNull(reference, "reference");
		Objects.requireNonNull(other, "other");

		if (!reference.document().equals(other.document()))
			return Relation.PARALLEL;

		int i = reference.position();
		int j = i + reference.length();
		int s = other.position();
		int e = s + other.length();
		return Relation.compute(i, j, s, e);
	}

	/**
	 * Calculate what is the relation between the given {@code sketch} and the given area
	 * {@code [s, e)}.
	 *
	 * @param sketch the sketch (first area).
	 * @param s      the first index of the second area.
	 * @param e      one past the last index of the second area.
	 * @return the relation constant describing the relation of the second area to the
	 * 		sketch.
	 * @throws NullPointerException     if the given {@code sketch} is null.
	 * @throws IllegalArgumentException if {@code s} is not in the range {@code [0, e]}.
	 * @see Relation#compute(int, int, int, int)
	 * @since 0.2.0 ~2021.01.25
	 */
	public static Relation compute(Sketch sketch, int s, int e) {
		Objects.requireNonNull(sketch, "sketch");
		return Relation.compute(sketch.reference(), s, e);
	}

	/**
	 * Calculate the relation between the sketches {@code sketch} and {@code other}.
	 *
	 * @param sketch the first sketch.
	 * @param other  the second sketch.
	 * @return the relation constant describing the relation of the second sketch to the
	 * 		first sketch.
	 * @throws NullPointerException if the given {@code sketch} or {@code other} is null.
	 * @see Relation#compute(int, int, int, int)
	 * @since 0.2.0 ~2021.01.25
	 */
	public static Relation compute(Sketch sketch, Reference other) {
		Objects.requireNonNull(sketch, "sketch");
		Objects.requireNonNull(other, "other");
		return Relation.compute(sketch.reference(), other);
	}

	/**
	 * Calculate the relation between the sketches {@code sketch} and {@code other}.
	 *
	 * @param sketch the first sketch.
	 * @param other  the second sketch.
	 * @return the relation constant describing the relation of the second sketch to the
	 * 		first sketch.
	 * @throws NullPointerException if the given {@code sketch} or {@code other} is null.
	 * @see Relation#compute(int, int, int, int)
	 * @since 0.2.0 ~2021.01.25
	 */
	public static Relation compute(Sketch sketch, Sketch other) {
		Objects.requireNonNull(sketch, "sketch");
		Objects.requireNonNull(other, "other");
		return Relation.compute(sketch.reference(), other.reference());
	}

	/**
	 * Returns how dominance this relation over its opposite relation.
	 *
	 * @return how dominance this relation.
	 * @since 0.2.0 ~2021.01.10
	 */
	public Dominance dominance() {
		return this.dominance;
	}

	/**
	 * Get the opposite relation of this relation.
	 *
	 * @return the opposite relation. Or null if none.
	 * @since 0.2.0 ~2021.01.09
	 */
	public Relation opposite() {
		return Relation.valueOf(this.opposite);
	}
}
