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
package org.jamplate.model.source;

import java.util.Comparator;
import java.util.regex.Pattern;

/**
 * A temporary sketch containing initial thoughts about an element.
 * <br>
 * Note: sketches are built from bottom to top. So, a typical sketch will store its
 * sub-sketches but never its parent sketch.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2020.12.25
 */
public interface Sketch {
	/**
	 * The standard sketch comparator.
	 *
	 * @since 0.2.0 ~2021.01.9
	 */
	Comparator<Sketch> COMPARATOR = Comparator.comparing(Sketch::source, Source.COMPARATOR);

	/**
	 * A sketch is equals to another sketch if they are the same instance.
	 *
	 * @return if the given {@code object} is this sketch instance.
	 * @since 0.2.0 ~2021.01.17
	 */
	@Override
	boolean equals(Object object);

	/**
	 * Calculate the hashcode of this sketch.
	 * <pre>
	 *     hashCode = {@link #source()}.hashCode() + {@code <ClassHashCode>}
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
	 *     {@code <ClassSimpleName>} ({@link #source() &lt;source()&gt;})
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
	 * @return true, if the given {@code visitor} stopped the loop.
	 * @throws NullPointerException if the given {@code visitor} is null.
	 * @since 0.2.0 ~2021.01.11
	 */
	boolean accept(Visitor visitor);

	/**
	 * Find a source that matches the given {@code pattern} while not interacting with any
	 * of the sketches in this (not interacting means {@link Source.Dominance#NONE}).
	 * <br>
	 * Important Note: the implementation might want to reserve its area to itself. So, it
	 * might always return null.
	 *
	 * @param pattern the pattern to be matched.
	 * @return a source that matches the given {@code pattern} while not interacting with
	 * 		any sketches in this.
	 * @throws NullPointerException if the given {@code pattern} is null.
	 * @since 0.2.0 ~2021.01.13
	 */
	Source find(Pattern pattern);

	/**
	 * Find a source that starts with the given {@code startPattern} and ends with the
	 * given {@code endPattern} while not interacting with any of the sketches in this
	 * (not interacting means {@link Source.Dominance#NONE}).
	 * <br>
	 * Important Note: the implementation might want to reserve its area to itself. So, it
	 * might always return null.
	 *
	 * @param startPattern the pattern to be matched with the starting sequence.
	 * @param endPattern   the pattern to be matched with the ending sequence.
	 * @return a source that its start matches the given {@code startPattern} and its end
	 * 		matches the given {@code endPattern} while not interacting with any sketches in
	 * 		this.
	 * @throws NullPointerException if the given {@code startPattern} or {@code
	 *                              endPattern} is null.
	 * @since 0.2.0 ~2021.01.13
	 */
	Source find(Pattern startPattern, Pattern endPattern);

	/**
	 * Put the given {@code sketch} to this sketch. Putting a sketch into another sketch
	 * is like telling that other sketch to mark its place as reserved. If the given
	 * sketch is a {@link Source.Dominance#PART} with a sketch in this sketch. Then the
	 * given {@code sketch} should be put into that sketch instead. On the other hand, if
	 * a sketch in this sketch has a dominance of {@link Source.Dominance#PART} with the
	 * given {@code sketch}. Then this sketch will transfer that sketch to the given
	 * {@code sketch}. (unless a clash happened, then an exception thrown and nothing
	 * happens)
	 *
	 * @param sketch the sketch to be put.
	 * @throws NullPointerException          if the given {@code sketch} is null.
	 * @throws IllegalStateException         if the given {@code sketch} is clashing with
	 *                                       a previously reserved area (has a dominance
	 *                                       of either {@link Source.Dominance#SHARE} or
	 *                                       {@link Source.Dominance#EXACT}).
	 * @throws IllegalArgumentException      if the given {@code sketch} has a dominance
	 *                                       other than {@link Source.Dominance#PART} or
	 *                                       {@link Source.Dominance#EXACT} with this
	 *                                       sketch.
	 * @throws UnsupportedOperationException if this sketch cannot have inner sketches.
	 * @since 0.2.0 ~2021.01.12
	 */
	void put(Sketch sketch);

	/**
	 * Get the source of this sketch.
	 *
	 * @return the source of this sketch.
	 * @since 0.2.0 ~2021.01.7
	 */
	Source source();

	/**
	 * A callback that can be passed to a sketch for that sketch to invoke this visitor
	 * with every element in it. (recursively)
	 * <br>
	 * Note: any new method added will always have the modifier {@code default} making
	 * this remain a {@link FunctionalInterface}.
	 *
	 * @author LSafer
	 * @version 0.2.0
	 * @since 0.2.0 ~2021.01.11
	 */
	@FunctionalInterface
	interface Visitor {
		/**
		 * Invoked for every sketch met down in the tree.
		 *
		 * @param sketch the sketch met.
		 * @return true, if this visitor wishes to stop the loop.
		 * @throws NullPointerException if the given {@code sketch} is null.
		 * @since 0.2.0 ~2021.01.11
		 */
		boolean visit(Sketch sketch);
	}
}
