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
package org.jamplate.source.sketch;

import org.jamplate.source.Dominance;
import org.jamplate.source.Visitor;
import org.jamplate.source.reference.Reference;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Optional;

/**
 * A temporary sketch containing initial thoughts about an element.
 * <br>
 * Note: sketches are built from bottom to top. So, a typical sketch will store its
 * sub-sketches but never its parent sketch.
 * <br>
 * A sketch should serialize its {@link #reference()} and inner sketches. It is not
 * encouraged to serialize additional data.
 * <br>
 * If a sketch is a deserialized sketch then any modification will throw an {@link
 * IllegalStateException}.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2020.12.25
 */
public interface Sketch extends Serializable {
	/**
	 * The standard sketch comparator. This comparator is sorting sketches from the first
	 * occurrence on the document to the last and from the longest to the shortest.
	 *
	 * @since 0.2.0 ~2021.01.09
	 */
	Comparator<Sketch> COMPARATOR = Comparator.comparing(Sketch::reference, Reference.COMPARATOR);

	/**
	 * A sketch is equals to another object if it is the same instance.
	 * <pre>
	 *     equals = this == object
	 * </pre>
	 *
	 * @return if the given {@code object} is this.
	 * @since 0.2.0 ~2021.01.17
	 */
	@Override
	boolean equals(Object object);

	/**
	 * Calculate the hashcode of this sketch.
	 * <pre>
	 *     hashCode = &lt;ReferenceHashCode&gt; + &lt;ClassHashCode&gt;
	 * </pre>
	 *
	 * @return the hashCode of this sketch.
	 * @since 0.2.0 ~2021.01.13
	 */
	@Override
	int hashCode();

	/**
	 * Returns a string representation of this sketch.
	 * <pre>
	 *     toString = &lt;ClassSimpleName&gt; (&lt;ReferenceToString&gt;)
	 * </pre>
	 *
	 * @return a string representation of this sketch.
	 * @since 0.2.0 ~2021.01.10
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
	 * @param <R>     the type of the results of the given {@code visitor}.
	 * @return an optional containing the results of visiting an element. Or {@code null}
	 * 		if the visitor wishes to continue the loop.
	 * @throws NullPointerException if the given {@code visitor} is null.
	 * @throws RuntimeException     if any exception thrown by the given {@code visitor}.
	 * @throws Error                if any error thrown by the given {@code visitor}.
	 * @since 0.2.0 ~2021.01.11
	 */
	<R> Optional<R> accept(Visitor<R> visitor);

	/**
	 * Check if the given area {@code [start, end)} can be put to this sketch or not. An
	 * area get rejected when the area has a dominance other than {@link Dominance#PART}
	 * nor {@link Dominance#EXACT} with this sketch or has a dominance other than {@link
	 * Dominance#NONE} with any of the sketches contained currently in this sketch.
	 * <br>
	 * Note: if this method returned {@code true} then it is safe to {@link #put(Sketch)}
	 * a sketch with the given area into this sketch. But, it is not necessarily the
	 * opposite.
	 * <br>
	 * Note: this is a checking method. Thus, it will simply return {@code false} if the
	 * given arguments are invalid.
	 *
	 * @param start the first index of the area to be checked.
	 * @param end   one past the last index of the area to be checked.
	 * @return true, if the given area {@code [start, end)} is free at this sketch.
	 * @since 0.2.0 ~2021.01.17
	 */
	boolean check(int start, int end);

	/**
	 * Put the given {@code sketch} to this sketch. Putting a sketch into another sketch
	 * is like telling that other sketch to mark the place of the sketch as reserved. If
	 * the given sketch is a {@link Dominance#PART} with a sketch in this sketch. Then the
	 * given {@code sketch} should be put into that sketch instead (using this method). On
	 * the other hand, if a sketch in this sketch has a dominance of {@link
	 * Dominance#PART} with the given {@code sketch}. Then this sketch will transfer that
	 * sketch to the given {@code sketch} (using this method). If a clash happened, then
	 * an exception thrown and things be as this method has not been called. (there is no
	 * guarantee that the effect will occur for every type of change)
	 *
	 * @param sketch the sketch to be put to this sketch.
	 * @throws NullPointerException          if the given {@code sketch} is null.
	 * @throws IllegalArgumentException      if the given {@code sketch} has a dominance
	 *                                       other than {@link Dominance#PART} or {@link
	 *                                       Dominance#EXACT} with this sketch.
	 * @throws IllegalStateException         if this sketch is deserialized; if the given
	 *                                       {@code sketch} is clashing with a previously
	 *                                       reserved area (has a dominance of either
	 *                                       {@link Dominance#SHARE} or {@link
	 *                                       Dominance#EXACT}).
	 * @throws UnsupportedOperationException if the given {@code sketch} cannot be put to
	 *                                       this sketch.
	 * @since 0.2.0 ~2021.01.12
	 */
	void put(Sketch sketch);

	/**
	 * Get the source reference of this sketch.
	 *
	 * @return the source reference of this sketch.
	 * @since 0.2.0 ~2021.01.07
	 */
	Reference reference();
}
