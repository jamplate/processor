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
 * An enumeration of the possible directions to the relatives of a source.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.14
 */
public enum Direction {
	/**
	 * <b>Parent</b> {@link #CHILD (opposite)}
	 * <br>
	 * When the other source can fit the source without being filled.
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
	 *     {s < i} & {j < e}
	 *     {i == s} & {j < e}
	 *     {s < i} & {j == e}
	 * </pre>
	 *
	 * @see Dominance#CONTAIN
	 * @see Intersection#CONTAINER
	 * @see Intersection#AHEAD
	 * @see Intersection#BEHIND
	 * @since 0.2.0 ~2021.05.14
	 */
	PARENT(Dominance.CONTAIN) {
		@NotNull
		@Override
		public Direction opposite() {
			return Direction.CHILD;
		}
	},
	/**
	 * <b>Child</b> {@link #PARENT (opposite)}
	 * <br>
	 * When the other source fits inside the source but is not filling it.
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
	 * @see Dominance#PART
	 * @see Intersection#FRAGMENT
	 * @see Intersection#START
	 * @see Intersection#END
	 * @since 0.2.0 ~2021.05.14
	 */
	CHILD(Dominance.PART) {
		@NotNull
		@Override
		public Direction opposite() {
			return Direction.PARENT;
		}
	},
	/**
	 * <b>Previous</b> {@link #NEXT (opposite)}
	 * <br>
	 * When the other source occurs before the source.
	 * <pre>
	 *     .....|--->.
	 *     .<---|.....
	 *     <br>
	 *     ......|-->.
	 *     .<--|......
	 * </pre>
	 * <pre>
	 *     (s < e == i < j)
	 *     (s <= e < i <= j)
	 * </pre>
	 * <pre>
	 *     {i == e}
	 *     {e < i}
	 * </pre>
	 *
	 * @see Dominance#NONE
	 * @see Intersection#PREVIOUS
	 * @see Intersection#BEFORE
	 * @since 0.2.0 ~2021.05.14
	 */
	PREVIOUS(Dominance.NONE) {
		@NotNull
		@Override
		public Direction opposite() {
			return Direction.NEXT;
		}
	},
	/**
	 * <b>Next</b> {@link #PREVIOUS (opposite)}
	 * <br>
	 * When the other source occurs after the source.
	 * <pre>
	 *     .<---|.....
	 *     .....|--->.
	 *     <br>
	 *     .<--|......
	 *     ......|-->.
	 * </pre>
	 * <pre>
	 *     (i < j == s < e)
	 *     (i <= j < s <= e)
	 * </pre>
	 * <pre>
	 *     {j == s}
	 *     {j < s}
	 * </pre>
	 *
	 * @see Dominance#NONE
	 * @see Intersection#NEXT
	 * @see Intersection#AFTER
	 * @since 0.2.0 ~2021.05.14
	 */
	NEXT(Dominance.NONE) {
		@NotNull
		@Override
		public Direction opposite() {
			return Direction.PREVIOUS;
		}
	};

	/**
	 * How dominant this direction over the opposite direction.
	 *
	 * @since 0.2.0 ~2021.05.15
	 */
	@NotNull
	private final Dominance dominance;

	/**
	 * Construct a enum with the given {@code dominance}.
	 *
	 * @param dominance how dominant the constructed direction over its opposite
	 *                  direction.
	 * @throws NullPointerException if the given {@code dominance} is null.
	 * @since 0.2.0 ~2021.05.15
	 */
	Direction(@NotNull Dominance dominance) {
		Objects.requireNonNull(dominance, "dominance");
		this.dominance = dominance;
	}

	/**
	 * Calculate the direction from the area {@code [i, j)} to the area {@code [s, e)}.
	 * <br>
	 * The direction will be the direction to head from the first area to the second
	 * area.
	 * <br>
	 * For example: if {@code A} is inside {@code B} then the direction from {@code A} to
	 * {@code B} will be {@link Direction#PARENT}.
	 *
	 * @param i the first index of the first area.
	 * @param j one past the last index of the first area.
	 * @param s the first index of the second area.
	 * @param e one past the last index of the second area.
	 * @return the direction from the first area given to the second area given.
	 * @throws IllegalArgumentException if {@code i} is not in the range {@code [0, j]} or
	 *                                  if {@code s} is not in the range {@code [0, e]}.
	 * @since 0.2.0 ~2021.05.15
	 */
	@SuppressWarnings("OverlyComplexMethod")
	@Nullable
	@Contract(pure = true)
	public static Direction compute(int i, int j, int s, int e) {
		if (i < 0 && s < 0 && i > j && s > e)
			throw new IllegalArgumentException("Illegal Indices");
		return j <= s ?
			   Direction.NEXT :
			   e <= i ?
			   Direction.PREVIOUS :
			   s < i && j < e ||
			   s == i && j < e ||
			   s < i && j == e ?
			   Direction.PARENT :
			   i < s && e < j ||
			   i == s && e < j ||
			   i < s && j == e ?
			   Direction.CHILD :
			   null;
	}

	/**
	 * Calculate the direction from the given {@code reference} to the area {@code [s,
	 * e)}.
	 * <br>
	 * The direction will be the direction to head from the first area to the second
	 * area.
	 * <br>
	 * For example: if {@code A} is inside {@code B} then the direction from {@code A} to
	 * {@code B} will be {@link Direction#PARENT}.
	 *
	 * @param reference the reference (first area).
	 * @param s         the first index of the second area.
	 * @param e         one past the last index of the second area.
	 * @return the direction from the given {@code reference} to the second area given.
	 * @throws NullPointerException     if the given {@code reference} is null.
	 * @throws IllegalArgumentException if {@code s} is not in the range {@code [0, e]}.
	 * @see Direction#compute(int, int, int, int)
	 * @since 0.2.0 ~2021.05.15
	 */
	@Nullable
	@Contract(pure = true)
	public static Direction compute(@NotNull Reference reference, int s, int e) {
		Objects.requireNonNull(reference, "reference");
		int i = reference.position();
		int j = i + reference.length();
		return Direction.compute(i, j, s, e);
	}

	/**
	 * Calculate the direction from the given {@code reference} to the {@code other}.
	 * <br>
	 * The direction will be the direction to head from the first area to the second
	 * area.
	 * <br>
	 * For example: if {@code A} is inside {@code B} then the direction from {@code A} to
	 * {@code B} will be {@link Direction#PARENT}.
	 *
	 * @param reference the first reference.
	 * @param other     the second reference.
	 * @return the direction from the given {@code reference} to the given {@code other}.
	 * @throws NullPointerException if the given {@code reference} or {@code other} is
	 *                              null.
	 * @see Direction#compute(int, int, int, int)
	 * @since 0.2.0 ~2021.05.15
	 */
	@Nullable
	@Contract(pure = true)
	public static Direction compute(@NotNull Reference reference, @NotNull Reference other) {
		Objects.requireNonNull(reference, "reference");
		Objects.requireNonNull(other, "other");
		int i = reference.position();
		int j = i + reference.length();
		int s = other.position();
		int e = s + other.length();
		return Direction.compute(i, j, s, e);
	}

	/**
	 * Calculate the direction from the given {@code sketch} to the area {@code [s, e)}.
	 * <br>
	 * The direction will be the direction to head from the first area to the second
	 * area.
	 * <br>
	 * For example: if {@code A} is inside {@code B} then the direction from {@code A} to
	 * {@code B} will be {@link Direction#PARENT}.
	 *
	 * @param tree the sketch (first area).
	 * @param s      the first index of the second area.
	 * @param e      one past the last index of the second area.
	 * @return the direction from the given {@code sketch} to the second area given.
	 * @throws NullPointerException     if the given {@code sketch} is null.
	 * @throws IllegalArgumentException if {@code s} is not in the range {@code [0, e]}.
	 * @see Direction#compute(int, int, int, int)
	 * @since 0.2.0 ~2021.01.25
	 */
	@Nullable
	@Contract(pure = true)
	public static Direction compute(@NotNull Tree tree, int s, int e) {
		Objects.requireNonNull(tree, "sketch");
		return Direction.compute(tree.reference(), s, e);
	}

	/**
	 * Calculate the direction from the given {@code sketch} to the {@code other}.
	 * <br>
	 * The direction will be the direction to head from the first area to the second
	 * area.
	 * <br>
	 * For example: if {@code A} is inside {@code B} then the direction from {@code A} to
	 * {@code B} will be {@link Direction#PARENT}.
	 *
	 * @param tree the first sketch.
	 * @param other  the second sketch.
	 * @return the direction from the given {@code sketch} to the given {@code other}.
	 * @throws NullPointerException if the given {@code sketch} or {@code other} is null.
	 * @see Direction#compute(int, int, int, int)
	 * @since 0.2.0 ~2021.01.25
	 */
	@Nullable
	@Contract(pure = true)
	public static Direction compute(@NotNull Tree tree, @NotNull Reference other) {
		Objects.requireNonNull(tree, "sketch");
		Objects.requireNonNull(other, "other");
		return Direction.compute(tree.reference(), other);
	}

	/**
	 * Calculate the direction from the given {@code sketch} to the {@code other}.
	 * <br>
	 * The direction will be the direction to head from the first area to the second
	 * area.
	 * <br>
	 * For example: if {@code A} is inside {@code B} then the direction from {@code A} to
	 * {@code B} will be {@link Direction#PARENT}.
	 *
	 * @param tree the first sketch.
	 * @param other  the second sketch.
	 * @return the direction from the given {@code sketch} to the given {@code other}.
	 * @throws NullPointerException if the given {@code sketch} or {@code other} is null.
	 * @see Direction#compute(int, int, int, int)
	 * @since 0.2.0 ~2021.01.25
	 */
	@Nullable
	@Contract(pure = true)
	public static Direction compute(@NotNull Tree tree, @NotNull Tree other) {
		Objects.requireNonNull(tree, "sketch");
		Objects.requireNonNull(other, "other");
		return Direction.compute(tree.reference(), other.reference());
	}

	@NotNull
	@Contract(pure = true)
	@Override
	public String toString() {
		return super.toString();
	}

	/**
	 * Returns how dominant this direction over the opposite direction.
	 *
	 * @return how dominant this direction.
	 * @since 0.2.0 ~2021.01.10
	 */
	@NotNull
	@Contract(pure = true)
	public Dominance dominance() {
		return this.dominance;
	}

	/**
	 * The opposite direction of this direction.
	 *
	 * @return the opposite of this.
	 * @since 0.2.0 ~2021.05.15
	 */
	@NotNull
	@Contract(pure = true)
	public abstract Direction opposite();
}
