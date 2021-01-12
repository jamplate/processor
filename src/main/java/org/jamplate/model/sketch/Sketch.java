/*
 *	Copyright 2020 Cufy
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
package org.jamplate.model.sketch;

import org.jamplate.model.source.Dominance;
import org.jamplate.model.source.Relation;
import org.jamplate.model.source.Source;

import java.util.Comparator;

/**
 * A temporary sketch containing initial thoughts about an element.
 * <br>
 * Note: sketches are built from bottom to top. So, a typical sketch will store its
 * sub-sketches but never its parent sketch.
 *
 * @author LSafer
 * @version 0.0.2
 * @since 0.0.2 ~2020.12.25
 */
public interface Sketch {
	/**
	 * The standard sketch comparator.
	 *
	 * @since 0.0.2 ~2021.01.9
	 */
	Comparator<Sketch> COMPARATOR = Comparator.comparing(Sketch::source, Source.COMPARATOR);

	/**
	 * Calculate how dominant the given area {@code [s, e)} over the given {@code
	 * sketch}.
	 *
	 * @param sketch the sketch. (the first area)
	 * @param s      the first index of the second area.
	 * @param e      one past the last index of the second area.
	 * @return how dominant the second area over the given sketch.
	 * @throws NullPointerException     if the given {@code sketch} is null.
	 * @throws IllegalArgumentException if the given {@code s} is not in the range {@code
	 *                                  [0, e]}.
	 * @see Dominance#compute(int, int, int, int)
	 * @since 0.0.2 ~2021.01.11
	 */
	static Dominance dominance(Sketch sketch, int s, int e) {
		return Source.dominance(sketch.source(), s, e);
	}

	/**
	 * Calculate how dominant the given {@code other} sketch over the given {@code
	 * sketch}.
	 *
	 * @param sketch the sketch. (the first area)
	 * @param other  the other sketch. (the second area)
	 * @return how dominant the second sketch over the first sketch.
	 * @throws NullPointerException if the given {@code sketch} or {@code other} is null.
	 * @see Dominance#compute(int, int, int, int)
	 * @since 0.0.2 ~2021.01.11
	 */
	static Dominance dominance(Sketch sketch, Sketch other) {
		return Source.dominance(sketch.source(), other.source());
	}

	/**
	 * Calculate the relation between the given {@code sketch} and the area {@code [s,
	 * e)}.
	 *
	 * @param sketch the sketch. (the first area)
	 * @param s      the first index of the second area.
	 * @param e      one past the last index of the second area.
	 * @return the relation between the given sketch and the given area.
	 * @throws NullPointerException     if the given {@code sketch} is null.
	 * @throws IllegalArgumentException if the given {@code s} is not in the range {@code
	 *                                  [s, e]}.
	 * @see Relation#compute(int, int, int, int)
	 * @since 0.0.2 ~2021.01.11
	 */
	static Relation relation(Sketch sketch, int s, int e) {
		return Source.relation(sketch.source(), s, e);
	}

	/**
	 * Calculate the relation between the given {@code sketch} and the {@code other}
	 * sketch.
	 *
	 * @param sketch the sketch. (the first area)
	 * @param other  the other sketch. (the second area)
	 * @return the relation between the given sketch and the given other sketch.
	 * @throws NullPointerException if the given {@code sketch} or {@code other} is null.
	 * @see Relation#compute(int, int, int, int)
	 * @since 0.0.2 ~2021.01.11
	 */
	static Relation relation(Sketch sketch, Sketch other) {
		return Source.relation(sketch.source(), other.source());
	}

	@Override
	int hashCode();

	/**
	 * Returns a string representation of this sketch.
	 * <pre>
	 *     {@code <Name>} ({@link #source() &lt;source()&gt;})
	 * </pre>
	 *
	 * @return a string representation of this sketch.
	 * @since 0.0.2 ~2021.01.10
	 */
	@Override
	String toString();

	/**
	 * Recursively loop down in the tree and invoke the appropriate method for each
	 * element met. The invocation shall always be ordered from the first to the last and
	 * from the containers to the parts.
	 * <br>
	 * Exceptions thrown by the given {@code visitor} will not be caught.
	 *
	 * @param visitor the visitor to be invoked for each element.
	 * @return true, if the given {@code visitor} wishes to stop the loop.
	 * @throws NullPointerException if the given {@code visitor} is null.
	 * @since 0.0.2 ~2021.01.11
	 */
	boolean accept(SketchVisitor visitor);

	/**
	 * Put the given {@code sketch} to this sketch. Putting a sketch into another sketch
	 * is like telling that other sketch to mark its place as reserved. If the given
	 * sketch is a {@link Dominance#PART} with a sketch in this sketch. Then the given
	 * {@code sketch} should be put into that sketch instead. On the other hand, if a
	 * sketch in this sketch has a dominance of {@link Dominance#PART} with the given
	 * {@code sketch}. Then this sketch will transfer than sketch to the given {@code
	 * sketch}. (unless a clash happened, then an exception thrown and nothing happens)
	 *
	 * @param sketch the sketch to be put.
	 * @throws NullPointerException     if the given {@code sketch} is null.
	 * @throws IllegalStateException    if the given {@code sketch} is clashing with a
	 *                                  previously reserved area (has a dominance of
	 *                                  either {@link Dominance#SHARE} or {@link
	 *                                  Dominance#EXACT}).
	 * @throws IllegalArgumentException if the given {@code sketch} has a dominance other
	 *                                  than {@link Dominance#PART} with this sketch.
	 * @since 0.0.2 ~2021.01.12
	 */
	void put(Sketch sketch);

	/**
	 * Get the source of this sketch.
	 *
	 * @return the source of this sketch.
	 * @since 0.0.2 ~2021.01.7
	 */
	Source source();
}
//	/**
//	 * Transfer the given {@code child} sketch to the given {@code parent} sketch. The given {@code child} sketch MUST be a sketch that this sketch
//	 * contain. The {@code parent} sketch must be able to {@link #put(Sketch)} the given {@code child} sketch in it.
//	 *
//	 * @param parent the sketch to be the new parent for the given {@code child} sketch.
//	 * @param child
//	 * @throws IllegalArgumentException if {@link Source#contains(Source) parent.source().contains(child.source())} returned false.
//	 * @throws NullPointerException     if the given {@code parent} or {@code child} is null.
//	 * @since 0.0.2 ~2021.01.9
//	 */
//	void transfer(Sketch parent, Sketch child);

//	void remove(Sketch sketch);
//
//	boolean check(Source source);
//
//	boolean check(Sketch sketch);
//
//	/**
//	 * Put the given sketch in this sketch.
//	 *
//	 * @param sketch the sketch to be put.
//	 * @throws NullPointerException if the given {@code sketch} is null.
//	 * @since 0.0.2 ~2021.01.7
//	 */
//	void put(Sketch sketch);
//
//	/**
//	 * Check if the given {@code sketch} can be sub sketch of this.
//	 * <br>
//	 * Reasons for a sketch to reject another sub sketch:
//	 * <ul>
//	 *     <li>The parent sketch's source does not {@link Source#contains(Source)} the child's source.</li>
//	 *     <li>The parent sketch has a sub-source that {@link Source#clashWith(Source)} with the child's source.</li>
//	 * </ul>
//	 *
//	 * @param sketch the sketch to be checked.
//	 * @return true, if the given {@code sketch} can be {@link #put(Sketch)} to this sketch.
//	 * @throws NullPointerException if the given {@code sketch} is null.
//	 * @since 0.0.2 ~2021.01.9
//	 */
//	boolean check(Sketch sketch);
