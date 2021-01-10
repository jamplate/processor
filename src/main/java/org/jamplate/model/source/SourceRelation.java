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
public enum SourceRelation {
	/**
	 * <b>Same Source</b> {@link #SAME (opposite)} {@code (i == s <= j == e)}
	 * <br>
	 * When the source has the exact bounds as the other source.
	 * <pre>
	 *     ...|---|...
	 *     ...|---|...
	 * </pre>
	 *
	 * @since 0.0.2 ~2021.01.9
	 */
	SAME("SAME", Dominance.EXACT),

	/**
	 * <b>Fragment Source</b> {@link #CONTAINER (opposite)} {@code (i < s <= e < j)}
	 * <br>
	 * When the source has its bounds containing the bounds of the other source but do not
	 * touch.
	 * <pre>
	 *     .<------->.
	 *     ...|---|...
	 * </pre>
	 *
	 * @since 0.0.2 ~2021.01.9
	 */
	FRAGMENT("CONTAINER", Dominance.PART),
	/**
	 * <b>Containing Source</b> {@link #FRAGMENT (opposite)} {@code (s < i <= j < e)}
	 * <br>
	 * When the bounds of the source are contained in the bounds of the other source but
	 * do not touch.
	 * <pre>
	 *     ...|---|...
	 *     .<------->.
	 * </pre>
	 *
	 * @since 0.0.2 ~2021.01.9
	 */
	CONTAINER("FRAGMENT", Dominance.CONTAIN),

	/**
	 * <b>At The Start</b> {@link #AHEAD (opposite)} {@code (i == s <= e < j)}
	 * <br>
	 * When the source contains the other source at its start.
	 * <pre>
	 *     .|--->.....
	 *     .|------->.
	 * </pre>
	 *
	 * @since 0.0.2 ~2021.01.9
	 */
	START("AHEAD", Dominance.PART),
	/**
	 * <b>This And Ahead</b> {@link #START (opposite)} {@code (s == i <= j < e)}
	 * <br>
	 * When the source is contained at the very start of the other source.
	 * <pre>
	 *     .|--->.....
	 *     .|------->.
	 * </pre>
	 *
	 * @since 0.0.2 ~2021.01.9
	 */
	AHEAD("START", Dominance.CONTAIN),

	/**
	 * <b>At The End</b> {@link #BEHIND (opposite)} {@code (i < s <= e == j)}
	 * <br>
	 * When the source contains the other source at its end.
	 * <pre>
	 *     .<-------|.
	 *     .....<---|.
	 * </pre>
	 *
	 * @since 0.0.2 ~2021.01.9
	 */
	END("BEHIND", Dominance.PART),
	/**
	 * <b>This And Behind</b> {@link #END (opposite)} {@code (s < i <= j == e)}
	 * <br>
	 * When the source is contained at the very end of the other source.
	 * <pre>
	 *     .....<---|.
	 *     .<-------|.
	 * </pre>
	 *
	 * @since 0.0.2 ~2021.01.9
	 */
	BEHIND("END", Dominance.CONTAIN),

	/**
	 * <b>Overflowed Slice</b> {@link #UNDERFLOW (opposite)} {@code (i < s < j < e)}
	 * <br>
	 * When the first fragment of the source is before the other source but the second
	 * fragment is in it. (without any bound been exactly at another bound)
	 * <pre>
	 *     .<----|....
	 *     ....|---->.
	 * </pre>
	 *
	 * @since 0.0.2 ~2021.01.9
	 */
	OVERFLOW("UNDERFLOW", Dominance.SHARE),
	/**
	 * <b>Underflowed Slice</b> {@link #OVERFLOW (opposite)} {@code (s < i < e < j)}
	 * <br>
	 * When the first fragment of the source is in the other source but the second
	 * fragment is after it. (without any bound been exactly at another bound)
	 * <pre>
	 *     ....|---->.
	 *     .<----|....
	 * </pre>
	 *
	 * @since 0.0.2 ~2021.01.9
	 */
	UNDERFLOW("OVERFLOW", Dominance.SHARE),

	/**
	 * <b>Next Source</b> {@link #PREVIOUS (opposite)} {@code (i < j == s < e)}
	 * <br>
	 * When the source is followed immediately by the other source.
	 * <pre>
	 *     .<---|.....
	 *     .....|--->.
	 * </pre>
	 *
	 * @since 0.0.2 ~2021.01.9
	 */
	NEXT("PREVIOUS", Dominance.NONE),
	/**
	 * <b>Previous Source</b> {@link #NEXT (opposite)} {@code (s < e == i < j)}
	 * <br>
	 * When the source has the other source immediately before it.
	 * <pre>
	 *     .....|--->.
	 *     .<---|.....
	 * </pre>
	 *
	 * @since 0.0.2 ~2021.01.9
	 */
	PREVIOUS("NEXT", Dominance.NONE),

	/**
	 * <b>After The Source</b> {@link #BEFORE opposite} {@code (i <= j < s <= e)}
	 * <br>
	 * When the other source is after the source but not immediately.
	 * <pre>
	 *     .<--|......
	 *     ......|-->.
	 * </pre>
	 *
	 * @since 0.0.2 ~2021.01.9
	 */
	AFTER("BEFORE", Dominance.NONE),
	/**
	 * <b>Before The Source</b> {@link #AFTER opposite} {@code (s <= e < i <= j)}
	 * <br>
	 * When the other source is before the source but not immediately.
	 * <pre>
	 *     ......|-->.
	 *     .<--|......
	 * </pre>
	 *
	 * @since 0.0.2 ~2021.01.9
	 */
	BEFORE("AFTER", Dominance.NONE),
	;

	/**
	 * How dominant this relation over the opposite relation.
	 *
	 * @since 0.0.2 ~2021.01.10
	 */
	private final Dominance dominance;
	/**
	 * The name of the opposite enum.
	 *
	 * @since 0.0.2 ~2021.01.9
	 */
	private final String opposite;

	/**
	 * Construct a new enum with the given {@code opposite} as the name of the opposite
	 * enum of it.
	 *
	 * @param opposite  the name of the opposite enum.
	 * @param dominance how dominant the constructed relation over its opposite relation.
	 * @throws NullPointerException if the given {@code opposite} is null.
	 * @since 0.0.2 ~2021.01.10
	 */
	SourceRelation(String opposite, Dominance dominance) {
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
	 * retaliation {@link #FRAGMENT fragmnet} will be returned.
	 *
	 * @param i the first index of the first area.
	 * @param j one past the last index of the first area.
	 * @param s the first index of the second area.
	 * @param e one past the last index of the second area.
	 * @return the relation constant describing the relation of the second area to the
	 * 		first area.
	 * @throws IllegalArgumentException if {@code i} is not in the range {@code [0, j]} or
	 *                                  if {@code s} is not in the range {@code [0, e]}
	 * @since 0.0.2 ~2021.01.10
	 */
	@SuppressWarnings("OverlyComplexMethod")
	public static SourceRelation calculate(int i, int j, int s, int e) {
		if (i >= 0 && s >= 0 && i <= j && s <= e)
			throw new IllegalArgumentException("Illegal Indices");
		//If you think you can optimize it more, then optimize it and do a pull-request :)
		return j < s ?
			   //i <= j < s <= e
			   SourceRelation.AFTER :
			   e < i ?
			   //s <= e < i <= j
			   SourceRelation.BEFORE :
			   i == s ?
			   j < e ? //duplicate 0
			   //s == i <= j < e
			   SourceRelation.AHEAD :
			   e < j ? //duplicate 1
			   //i == s <= e < j
			   SourceRelation.START :
			   //i == s <= j == e
			   SourceRelation.SAME :
			   j == e ?
			   s < i ?
			   //s < i <= j == e
			   SourceRelation.BEHIND :
			   //i < s <= e == j
			   SourceRelation.END :
			   j == s ?
			   //i < j == s < e
			   SourceRelation.NEXT :
			   e == i ?
			   //s < e == i < j
			   SourceRelation.PREVIOUS :
			   i < s ?
			   e < j ? //duplicate 1
			   //i < s <= e < j
			   SourceRelation.FRAGMENT :
			   //i < s < j < e
			   SourceRelation.OVERFLOW :
			   j < e ? //duplicate 0
			   //s < i <= j < e
			   SourceRelation.CONTAINER :
			   //s < i < e < j
			   SourceRelation.UNDERFLOW;
	}

	/**
	 * Returns how dominance this relation over its opposite relation.
	 *
	 * @return how dominance this relation.
	 * @since 0.0.2 ~2021.01.10
	 */
	public Dominance dominance() {
		return this.dominance;
	}

	/**
	 * Get the opposite relation of this relation.
	 *
	 * @return the opposite relation. Or null if none.
	 * @since 0.0.2 ~2021.01.9
	 */
	public SourceRelation opposite() {
		return SourceRelation.valueOf(this.opposite);
	}

	/**
	 * An enumeration of how dominant a relation is over another relation.
	 *
	 * @author LSafer
	 * @version 0.0.2
	 * @since 0.0.2 ~2021.01.10
	 */
	public enum Dominance {
		/**
		 * Defines that a source that have the relation contains the other source in it
		 * (but not exact).
		 *
		 * @see #PART
		 * @since 0.0.2 ~2021.01.10
		 */
		CONTAIN,
		/**
		 * Defines that a source that have the relation has the same relation as the other
		 * source.
		 *
		 * @since 0.0.2 ~2021.01.10
		 */
		EXACT,
		/**
		 * Defines that a source that have the relation shares some (but not all) of the
		 * other source and vice versa.
		 *
		 * @since 0.0.2 ~2021.01.10
		 */
		SHARE,
		/**
		 * Defines that a source that have the relation shares some (but not all) of the
		 * other source. (one but not both)
		 *
		 * @see #CONTAIN
		 * @since 0.0.2 ~2021.01.10
		 */
		PART,
		/**
		 * Defines that a source that have the relation shares none of the other source.
		 *
		 * @since 0.0.2 ~2021.01.10
		 */
		NONE,
	}
}
