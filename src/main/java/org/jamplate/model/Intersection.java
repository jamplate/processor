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

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * An enumeration of possible intersections between sources.
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
public enum Intersection {
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
	 * @see Relation#PARENT
	 * @since 0.2.0 ~2021.01.09
	 */
	CONTAINER(Dominance.CONTAIN, Relation.PARENT) {
		@NotNull
		@Override
		public Intersection opposite() {
			return Intersection.FRAGMENT;
		}
	},
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
	 * @see Relation#PARENT
	 * @since 0.2.0 ~2021.01.09
	 */
	AHEAD(Dominance.CONTAIN, Relation.PARENT) {
		@NotNull
		@Override
		public Intersection opposite() {
			return Intersection.START;
		}
	},
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
	 * @see Relation#PARENT
	 * @since 0.2.0 ~2021.01.09
	 */
	BEHIND(Dominance.CONTAIN, Relation.PARENT) {
		@NotNull
		@Override
		public Intersection opposite() {
			return Intersection.END;
		}
	},

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
	SAME(Dominance.EXACT, null) {
		@NotNull
		@Override
		public Intersection opposite() {
			return Intersection.SAME;
		}
	},

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
	OVERFLOW(Dominance.SHARE, null) {
		@NotNull
		@Override
		public Intersection opposite() {
			return Intersection.UNDERFLOW;
		}
	},
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
	UNDERFLOW(Dominance.SHARE, null) {
		@NotNull
		@Override
		public Intersection opposite() {
			return Intersection.OVERFLOW;
		}
	},

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
	 * @see Relation#CHILD
	 * @since 0.2.0 ~2021.01.09
	 */
	FRAGMENT(Dominance.PART, Relation.CHILD) {
		@NotNull
		@Override
		public Intersection opposite() {
			return Intersection.CONTAINER;
		}
	},
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
	 * @see Relation#CHILD
	 * @since 0.2.0 ~2021.01.09
	 */
	START(Dominance.PART, Relation.CHILD) {
		@NotNull
		@Override
		public Intersection opposite() {
			return Intersection.AHEAD;
		}
	},
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
	 * @see Relation#CHILD
	 * @since 0.2.0 ~2021.01.09
	 */
	END(Dominance.PART, Relation.CHILD) {
		@NotNull
		@Override
		public Intersection opposite() {
			return Intersection.BEHIND;
		}
	},

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
	 * @see Relation#NEXT
	 * @since 0.2.0 ~2021.01.09
	 */
	NEXT(Dominance.NONE, Relation.NEXT) {
		@NotNull
		@Override
		public Intersection opposite() {
			return Intersection.PREVIOUS;
		}
	},
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
	 * @see Relation#NEXT
	 * @since 0.2.0 ~2021.01.09
	 */
	AFTER(Dominance.NONE, Relation.NEXT) {
		@NotNull
		@Override
		public Intersection opposite() {
			return Intersection.BEFORE;
		}
	},

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
	 * @see Relation#PREVIOUS
	 * @since 0.2.0 ~2021.01.09
	 */
	PREVIOUS(Dominance.NONE, Relation.PREVIOUS) {
		@NotNull
		@Override
		public Intersection opposite() {
			return Intersection.NEXT;
		}
	},
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
	 * @see Relation#PREVIOUS
	 * @since 0.2.0 ~2021.01.09
	 */
	BEFORE(Dominance.NONE, Relation.PREVIOUS) {
		@NotNull
		@Override
		public Intersection opposite() {
			return Intersection.AFTER;
		}
	};

	/**
	 * How dominant this intersection over the opposite intersection.
	 *
	 * @since 0.2.0 ~2021.01.10
	 */
	@NotNull
	private final Dominance dominance;
	/**
	 * The direction from the opposite intersection to this intersection.
	 *
	 * @since 0.2.0 ~2021.05.15
	 */
	@Nullable
	private final Relation relation;

	/**
	 * Construct a new enum with the given {@code dominance} and direction.
	 *
	 * @param dominance how dominant the constructed intersection over its opposite
	 *                  intersection.
	 * @param relation  the direction from the opposite of the constructed intersection to
	 *                  itself. (pass {@code null} if undefined)
	 * @throws NullPointerException if the given {@code dominance} is null.
	 * @since 0.2.0 ~2021.01.10
	 */
	Intersection(@NotNull Dominance dominance, @Nullable Relation relation) {
		Objects.requireNonNull(dominance, "dominance");
		this.dominance = dominance;
		this.relation = relation;
	}

	/**
	 * Calculate what is the intersection between the areas {@code [i, j)} and {@code [s,
	 * e)}.
	 * <br>
	 * The first area is the area to compare the second area with. The returned
	 * intersection will be the intersection describing the feelings of the first area
	 * about the second area.
	 * <br>
	 * For example: if the second area is contained in the middle of first area, then the
	 * retaliation {@link Intersection#FRAGMENT fragmnet} will be returned.
	 *
	 * @param i the first index of the first area.
	 * @param j one past the last index of the first area.
	 * @param s the first index of the second area.
	 * @param e one past the last index of the second area.
	 * @return the intersection constant describing the intersection of the second area to
	 * 		the first area.
	 * @throws IllegalArgumentException if {@code i} is not in the range {@code [0, j]} or
	 *                                  if {@code s} is not in the range {@code [0, e]}.
	 * @since 0.2.0 ~2021.01.10
	 */
	@SuppressWarnings("OverlyComplexMethod")
	@NotNull
	@Contract(pure = true)
	public static Intersection compute(int i, int j, int s, int e) {
		if (i < 0 && s < 0 && i > j && s > e)
			throw new IllegalArgumentException("Illegal Indices");
		return j == s ?
			   Intersection.NEXT :
			   i == e ?
			   Intersection.PREVIOUS :
			   j < s ?
			   Intersection.AFTER :
			   e < i ?
			   Intersection.BEFORE :
			   s < i && j < e ?
			   Intersection.CONTAINER :
			   i == s && j < e ?
			   Intersection.AHEAD :
			   s < i && j == e ?
			   Intersection.BEHIND :
			   i == s && j == e ?
			   Intersection.SAME :
			   i < s && e < j ?
			   Intersection.FRAGMENT :
			   i == s /*&& e < j*/ ?
			   Intersection.START :
			   i < s && j == e ?
			   Intersection.END :
			   i < s /*&& s < j && j < e*/ ?
			   Intersection.OVERFLOW :
			   //s < i && i < e && e < j ?
			   Intersection.UNDERFLOW;
	}

	/**
	 * Calculate what is the intersection between the given {@code reference} and the
	 * given area {@code [s, e)}.
	 *
	 * @param reference the reference (first area).
	 * @param s         the first index of the second area.
	 * @param e         one past the last index of the second area.
	 * @return the intersection constant describing the intersection of the second area to
	 * 		the reference.
	 * @throws NullPointerException     if the given {@code reference} is null.
	 * @throws IllegalArgumentException if {@code s} is not in the range {@code [0, e]}.
	 * @see Intersection#compute(int, int, int, int)
	 * @since 0.2.0 ~2021.01.10
	 */
	@NotNull
	@Contract(pure = true)
	public static Intersection compute(@NotNull Reference reference, int s, int e) {
		Objects.requireNonNull(reference, "reference");
		int i = reference.position();
		int j = i + reference.length();
		return Intersection.compute(i, j, s, e);
	}

	/**
	 * Calculate the intersection between the references {@code reference} and {@code
	 * other}.
	 *
	 * @param reference the first reference.
	 * @param other     the second reference.
	 * @return the intersection constant describing the intersection of the second
	 * 		reference to the first reference.
	 * @throws NullPointerException if the given {@code reference} or {@code other} is
	 *                              null.
	 * @see Intersection#compute(int, int, int, int)
	 * @since 0.2.0 ~2021.01.10
	 */
	@NotNull
	@Contract(pure = true)
	public static Intersection compute(@NotNull Reference reference, @NotNull Reference other) {
		Objects.requireNonNull(reference, "reference");
		Objects.requireNonNull(other, "other");
		int i = reference.position();
		int j = i + reference.length();
		int s = other.position();
		int e = s + other.length();
		return Intersection.compute(i, j, s, e);
	}

	/**
	 * Calculate what is the intersection between the given {@code sketch} and the given
	 * area {@code [s, e)}.
	 *
	 * @param tree the sketch (first area).
	 * @param s    the first index of the second area.
	 * @param e    one past the last index of the second area.
	 * @return the intersection constant describing the intersection of the second area to
	 * 		the sketch.
	 * @throws NullPointerException     if the given {@code sketch} is null.
	 * @throws IllegalArgumentException if {@code s} is not in the range {@code [0, e]}.
	 * @see Intersection#compute(int, int, int, int)
	 * @since 0.2.0 ~2021.01.25
	 */
	@NotNull
	@Contract(pure = true)
	public static Intersection compute(@NotNull Tree tree, int s, int e) {
		Objects.requireNonNull(tree, "sketch");
		return Intersection.compute(tree.reference(), s, e);
	}

	/**
	 * Calculate the intersection between the sketches {@code sketch} and {@code other}.
	 *
	 * @param tree  the first sketch.
	 * @param other the second sketch.
	 * @return the intersection constant describing the intersection of the second sketch
	 * 		to the first sketch.
	 * @throws NullPointerException if the given {@code sketch} or {@code other} is null.
	 * @see Intersection#compute(int, int, int, int)
	 * @since 0.2.0 ~2021.01.25
	 */
	@NotNull
	@Contract(pure = true)
	public static Intersection compute(@NotNull Tree tree, @NotNull Reference other) {
		Objects.requireNonNull(tree, "sketch");
		Objects.requireNonNull(other, "other");
		return Intersection.compute(tree.reference(), other);
	}

	/**
	 * Calculate the intersection between the sketches {@code sketch} and {@code other}.
	 *
	 * @param tree  the first sketch.
	 * @param other the second sketch.
	 * @return the intersection constant describing the intersection of the second sketch
	 * 		to the first sketch.
	 * @throws NullPointerException if the given {@code sketch} or {@code other} is null.
	 * @see Intersection#compute(int, int, int, int)
	 * @since 0.2.0 ~2021.01.25
	 */
	@NotNull
	@Contract(pure = true)
	public static Intersection compute(@NotNull Tree tree, @NotNull Tree other) {
		Objects.requireNonNull(tree, "sketch");
		Objects.requireNonNull(other, "other");
		return Intersection.compute(tree.reference(), other.reference());
	}

	@NotNull
	@Contract(pure = true)
	@Override
	public String toString() {
		return super.toString();
	}

	/**
	 * Returns how dominance this intersection over its opposite intersection.
	 *
	 * @return how dominant this intersection.
	 * @since 0.2.0 ~2021.01.10
	 */
	@NotNull
	@Contract(pure = true)
	public Dominance dominance() {
		return this.dominance;
	}

	/**
	 * Returns the relation from this the opposite intersection to this intersection.
	 *
	 * @return the direction of this. Or {@code null} if there cannot be a direction
	 * 		between them.
	 * @since 0.2.0 ~2021.05.15
	 */
	@Nullable
	@Contract(pure = true)
	public Relation relation() {
		return this.relation;
	}

	/**
	 * Get the opposite intersection of this intersection.
	 *
	 * @return the opposite intersection. Or null if none.
	 * @since 0.2.0 ~2021.01.09
	 */
	@NotNull
	@Contract(pure = true)
	public abstract Intersection opposite();
}
