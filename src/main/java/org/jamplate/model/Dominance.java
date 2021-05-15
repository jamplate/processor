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

import java.util.Objects;

/**
 * An enumeration of how dominant a relation is over another relation.
 * <br>
 * The names is describing the other source.
 * <pre>
 * 		EXACT
 * 			i == s <= j == e
 * 			...|---|...
 * 			...|---|...
 * 		PART | CONTAIN
 * 			i < s <= e < j
 * 			.<------->.
 * 			...|---|...
 * 			<br>
 * 			i == s <= e < j
 * 			.|------->.
 * 			.|--->.....
 * 			<br>
 * 			i < s <= e == j
 * 			.<-------|.
 * 			.....<---|.
 * 		SHARE
 * 			i < s < j < e
 * 			.<----|....
 * 			....|---->.
 * 			<br>
 * 			s < i < e < j
 * 			....|---->.
 * 			.<----|....
 * 		NONE
 * 			i < j == s < e
 * 			.<---|.....
 * 			.....|--->.
 * 			<br>
 * 			s < e == i < j
 * 			.....|--->.
 * 			.<---|.....
 * 			<br>
 * 			i <= j < s <= e
 * 			.<--|......
 * 			......|-->.
 * 			<br>
 * 			s <= e < i <= j
 * 			......|-->.
 * 			.<--|......
 * </pre>
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.01.10
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
	 * @see Direction#PARENT
	 * @see Relation#CONTAINER
	 * @see Relation#AHEAD
	 * @see Relation#BEHIND
	 * @since 0.2.0 ~2021.01.10
	 */
	@NotNull
	CONTAIN() {
		@NotNull
		@Override
		public Dominance opposite() {
			return Dominance.PART;
		}
	},
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
	 * @since 0.2.0 ~2021.01.10
	 */
	@NotNull
	EXACT() {
		@NotNull
		@Override
		public Dominance opposite() {
			return Dominance.EXACT;
		}
	},
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
	 * @since 0.2.0 ~2021.01.10
	 */
	@NotNull
	SHARE() {
		@NotNull
		@Override
		public Dominance opposite() {
			return Dominance.SHARE;
		}
	},
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
	 * @see Direction#CHILD
	 * @see Relation#FRAGMENT
	 * @see Relation#START
	 * @see Relation#END
	 * @since 0.2.0 ~2021.01.10
	 */
	@NotNull
	PART() {
		@NotNull
		@Override
		public Dominance opposite() {
			return Dominance.CONTAIN;
		}
	},
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
	 * @see Direction#NEXT
	 * @see Direction#PREVIOUS
	 * @see Relation#NEXT
	 * @see Relation#PREVIOUS
	 * @see Relation#AFTER
	 * @see Relation#BEFORE
	 * @since 0.2.0 ~2021.01.10
	 */
	@NotNull
	NONE() {
		@NotNull
		@Override
		public Dominance opposite() {
			return Dominance.NONE;
		}
	};

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
	 * @since 0.2.0 ~2021.01.10
	 */
	@SuppressWarnings("OverlyComplexMethod")
	@NotNull
	@Contract(pure = true)
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
	 * Calculate how much dominant the area {@code [s, e)} is over the given {@code
	 * reference}.
	 *
	 * @param reference the reference (first area).
	 * @param s         the first index of the second area.
	 * @param e         one past the last index of the second area.
	 * @return how much dominant the second area over the given {@code reference}.
	 * @throws NullPointerException     if the given {@code reference} is null.
	 * @throws IllegalArgumentException if {@code s} is not in the range {@code [0, e]}.
	 * @see Dominance#compute(int, int, int, int)
	 * @since 0.2.0 ~2021.01.11
	 */
	@NotNull
	@Contract(pure = true)
	public static Dominance compute(@NotNull Reference reference, int s, int e) {
		Objects.requireNonNull(reference, "reference");
		int i = reference.position();
		int j = i + reference.length();
		return Dominance.compute(i, j, s, e);
	}

	/**
	 * Calculate how much dominant the {@code other} reference over the given {@code
	 * reference}.
	 *
	 * @param reference the first reference.
	 * @param other     the second reference.
	 * @return how much dominant the second reference over the first reference.
	 * @throws NullPointerException if the given {@code reference} or {@code other} is
	 *                              null.
	 * @see Dominance#compute(int, int, int, int)
	 * @since 0.2.0 ~2021.01.10
	 */
	@NotNull
	@Contract(pure = true)
	public static Dominance compute(@NotNull Reference reference, @NotNull Reference other) {
		Objects.requireNonNull(reference, "reference");
		Objects.requireNonNull(other, "other");
		int i = reference.position();
		int j = i + reference.length();
		int s = other.position();
		int e = s + other.length();
		return Dominance.compute(i, j, s, e);
	}

	/**
	 * Calculate how much dominant the area {@code [s, e)} is over the given {@code
	 * sketch}.
	 *
	 * @param sketch the sketch (first area).
	 * @param s      the first index of the second area.
	 * @param e      one past the last index of the second area.
	 * @return how much dominant the second area over the given {@code sketch}.
	 * @throws NullPointerException     if the given {@code sketch} is null.
	 * @throws IllegalArgumentException if {@code s} is not in the range {@code [0, e]}.
	 * @see Dominance#compute(int, int, int, int)
	 * @since 0.2.0 ~2021.01.25
	 */
	@NotNull
	@Contract(pure = true)
	public static Dominance compute(@NotNull Sketch sketch, int s, int e) {
		Objects.requireNonNull(sketch, "sketch");
		return Dominance.compute(sketch.reference(), s, e);
	}

	/**
	 * Calculate how much dominant the {@code other} reference over the given {@code
	 * sketch}.
	 *
	 * @param sketch the first sketch.
	 * @param other  the second reference.
	 * @return how much dominant the second reference over the first sketch.
	 * @throws NullPointerException if the given {@code sketch} or {@code other} is null.
	 * @see Dominance#compute(int, int, int, int)
	 * @since 0.2.0 ~2021.01.25
	 */
	@NotNull
	@Contract(pure = true)
	public static Dominance compute(@NotNull Sketch sketch, @NotNull Reference other) {
		Objects.requireNonNull(sketch, "sketch");
		Objects.requireNonNull(other, "other");
		return Dominance.compute(sketch.reference(), other);
	}

	/**
	 * Calculate how much dominant the {@code other} sketch over the given {@code
	 * sketch}.
	 *
	 * @param sketch the first sketch.
	 * @param other  the second sketch.
	 * @return how much dominant the second sketch over the first sketch.
	 * @throws NullPointerException if the given {@code sketch} or {@code other} is null.
	 * @see Dominance#compute(int, int, int, int)
	 * @since 0.2.0 ~2021.01.25
	 */
	@NotNull
	@Contract(pure = true)
	public static Dominance compute(@NotNull Sketch sketch, @NotNull Sketch other) {
		Objects.requireNonNull(sketch, "sketch");
		Objects.requireNonNull(other, "other");
		return Dominance.compute(sketch.reference(), other.reference());
	}

	@NotNull
	@Contract(pure = true)
	@Override
	public String toString() {
		return super.toString();
	}

	/**
	 * Get the opposite dominance type of this dominance type.
	 *
	 * @return the opposite dominance type.
	 * @since 0.2.0 ~2021.01.10
	 */
	@NotNull
	@Contract(pure = true)
	public abstract Dominance opposite();
}
//

//

//
//	/**
//	 * Calculate how much dominant the {@code other} node over the given {@code node}.
//	 *
//	 * @param node  the first node.
//	 * @param other the second node.
//	 * @return how much dominant the second node over the first node.
//	 * @throws NullPointerException if the given {@code node} or {@code other} or {@code
//	 *                              node.getReference()} or {@code other.getReference()}
//	 *                              is null.
//	 * @see Dominance#compute(int, int, int, int)
//	 * @since 0.2.0 ~2021.02.15
//	 */
//	public static Dominance compute(Node node, Node other) {
//		Objects.requireNonNull(node, "node");
//		Objects.requireNonNull(other, "other");
//		return Dominance.compute(node.getReference(), other.getReference());
//	}
//
//	public static Dominance compute(Node node, int s, int e) {
//		Objects.requireNonNull(node, "node");
//		return Dominance.compute(node.reference(), s, e);
//	}
//
//	public static Dominance compute(Node node, Reference reference) {
//		Objects.requireNonNull(node, "node");
//		return Dominance.compute(node.reference(), reference);
//	}
//
//	public static Dominance compute(Node node, Node other) {
//		Objects.requireNonNull(node, "node");
//		Objects.requireNonNull(other, "other");
//		return Dominance.compute(node.reference(), other.reference());
//	}
