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
 * An enumeration of how dominant a relation is over another relation.
 *
 * @author LSafer
 * @version 0.0.2
 * @since 0.0.2 ~2021.01.10
 */
public enum Dominance {
	/**
	 * <b>Container</b> {@link #PART (opposite)}
	 * <br>
	 * Defines that a source that have the relation contains the other source in it (but
	 * not exact).
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
	 * Defines that a source that have the relation shares some (but not all) of the other
	 * source and vice versa.
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
	 * Defines that a source that have the relation shares some (but not all) of the other
	 * source. (one but not both)
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
	NONE("NONE"),
	;

	/**
	 * The opposite dominance type of this.
	 *
	 * @since 0.0.2 ~2021.01.10
	 */
	private final String opposite;

	/**
	 * Construct a new dominance type.
	 *
	 * @param opposite the name of the dominance type that is opposite of this dominance
	 *                 type.
	 * @throws NullPointerException if the given {@code opposite} is null.
	 * @since 0.0.2 ~2021.01.10
	 */
	Dominance(String opposite) {
		Objects.requireNonNull(opposite, "opposite");
		this.opposite = opposite;
	}

	/**
	 * Calculate how much dominant the area [s, e) over the area [i, j).
	 *
	 * @param i the first index of the first area.
	 * @param j one past the last index of the first area.
	 * @param s the first index of the second area.
	 * @param e one past the last index of the second area.
	 * @return how much dominant the second area over the first area.
	 * @throws IllegalArgumentException if {@code i} is not in the range {@code [0, j)} or
	 *                                  if {@code s} is not in the range {@code [0, e]}.
	 * @since 0.0.2 ~2021.01.10
	 */
	@SuppressWarnings("OverlyComplexMethod")
	public static Dominance compute(int i, int j, int s, int e) {
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
	 * Get the opposite dominance type of this dominance type.
	 *
	 * @return the opposite dominance type.
	 * @since 0.0.2 ~2021.01.10
	 */
	public Dominance opposite() {
		return Dominance.valueOf(this.opposite);
	}
}
