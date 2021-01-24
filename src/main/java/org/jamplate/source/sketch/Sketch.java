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
package org.jamplate.source.sketch;

import org.jamplate.source.reference.Reference;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A temporary sketch containing initial thoughts about an element.
 * <br>
 * Note: sketches are built from bottom to top. So, a typical sketch will store its
 * sub-sketches but never its parent sketch.
 * <br>
 * A sketch should serialize its {@link #reference()} and inner sketches. It is not
 * encouraged to serialize additional data.
 * <br>
 * If a sketch is a deserialized sketch then the method {@link #put(Sketch)} will throw an
 * {@link IllegalStateException}.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2020.12.25
 */
public interface Sketch extends Serializable {
	/**
	 * The standard sketch comparator.
	 *
	 * @since 0.2.0 ~2021.01.9
	 */
	Comparator<Sketch> COMPARATOR = Comparator.comparing(Sketch::reference, Reference.COMPARATOR);

	/**
	 * Until the given {@code visitors} all completes a complete loop, Make the given
	 * {@code sketch} {@link Sketch#accept(Visitor) accept} them. (first completes the
	 * loop then the next)
	 *
	 * @param sketch   the sketch to accept the given {@code visitor}.
	 * @param visitors the visitors to loop over the elements of the given {@code
	 *                 sketch}.
	 * @throws NullPointerException if the given {@code sketch} or {@code visitors} is
	 *                              null.
	 * @since 0.2.0 ~2021.01.23
	 */
	static void accept(Sketch sketch, Visitor... visitors) {
		Objects.requireNonNull(sketch, "sketch");
		Objects.requireNonNull(visitors, "visitors");
		for (Visitor visitor : visitors)
			if (visitor != null)
				while (sketch.accept(visitor))
					;
	}

	/**
	 * Find an available source that starts with the given {@code startPatter} and ends
	 * with the given {@code endPatter}. The two patterns each has a dominance of {@link
	 * Reference.Dominance#NONE} with every inner sketch in the given {@code sketch}. The
	 * returned source will have no AVAILABLE sequence that matches the given {@code
	 * startPattern} in between.
	 *
	 * @param sketch       the sketch to find in.
	 * @param startPattern the pattern of the start.
	 * @param endPattern   the pattern of the end.
	 * @return a fixed length array with the second item being the reference matching
	 *        {@code startPattern} and the third item being the reference matching {@code
	 * 		endPattern} and the first item being the reference matching the area between
	 *        {@code startPattern} and {@code endPattern}. (inclusive)
	 * @throws NullPointerException if the given {@code sketch} or {@code startPattern} or
	 *                              {@code endPattern} is null.
	 * @since 0.2.0 ~2021.01.17
	 */
	static Reference[] find(Sketch sketch, Pattern startPattern, Pattern endPattern) {
		Objects.requireNonNull(sketch, "sketch");
		Objects.requireNonNull(startPattern, "startPattern");
		Objects.requireNonNull(endPattern, "endPattern");

		Reference reference = sketch.reference();

		Matcher endMatcher = Reference.matcher(reference, endPattern);

		//find the first valid end
		while (endMatcher.find()) {
			int s = endMatcher.start();
			int e = endMatcher.end();

			//validate end
			if (sketch.check(s, e)) {
				Matcher startMatcher = Reference.matcher(reference, startPattern);

				int i = -1;
				int j = -1;

				//find the nearest valid start before the end
				while (startMatcher.find()) {
					int ii = startMatcher.start();
					int jj = startMatcher.end();

					if (ii >= s)
						//early break
						break;

					//validate start
					if (sketch.check(ii, jj)) {
						i = ii;
						j = jj;
					}
				}

				//validate results
				if (i >= 0 && j >= 0) {
					int position = i - reference.position();
					int length = e - i;

					//bingo!
					Reference match = reference.subReference(position, length);
					Reference startMatch = match.subReference(0, j - i);
					Reference endMatch = match.subReference(s - i, e - s);
					return new Reference[]{
							match,
							startMatch,
							endMatch
					};
				}
			}
		}

		//no valid matches
		return null;
	}

	/**
	 * Find an available source in the given {@code sketch} that matches the given {@code
	 * pattern}.
	 *
	 * @param sketch  the sketch to find a source from.
	 * @param pattern the pattern to match the required source with.
	 * @return an available source in the given {@code sketch} that matches the given
	 *        {@code pattern} or null if no such source found.
	 * @throws NullPointerException if the given {@code sketch} or {@code pattern} is
	 *                              null.
	 * @since 0.2.0 ~2021.01.17
	 */
	static Reference find(Sketch sketch, Pattern pattern) {
		Objects.requireNonNull(pattern, "pattern");
		Objects.requireNonNull(pattern, "pattern");

		Reference reference = sketch.reference();

		Matcher matcher = Reference.matcher(reference, pattern);

		//search for a valid match
		while (matcher.find()) {
			int i = matcher.start();
			int j = matcher.end();

			//validate match
			if (sketch.check(i, j)) {
				int position = i - reference.position();
				int length = j - i;

				//bingo!
				return reference.subReference(position, length);
			}
		}

		//no valid matches
		return null;
	}

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
	 *     hashCode = {@link #reference()}.hashCode() + {@code <ClassHashCode>}
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
	 *     {@code <ClassSimpleName>} ({@link #reference() &lt;reference()&gt;})
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
	 * <br>
	 * <br>
	 * Elements to be visited are:
	 * <ul>
	 *     <li>
	 *         Any sketch met.
	 *         <br>
	 *         Visited via {@link Visitor#visit(Sketch)}
	 *     </li>
	 *     <li>
	 *         Any non-reserved areas (areas that has no sketch reserving it) met.
	 *         <br>
	 *         Visited via {@link Visitor#visit(Sketch, int, int)}
	 *     </li>
	 * </ul>
	 *
	 * @param visitor the visitor to be invoked for each element.
	 * @return true, if the given {@code visitor} stopped the loop.
	 * @throws NullPointerException if the given {@code visitor} is null.
	 * @since 0.2.0 ~2021.01.11
	 */
	boolean accept(Visitor visitor);

	/**
	 * Check if the given area {@code [start, end)} can be put to this sketch or not. An
	 * area get rejected when the area has a dominance other than {@link
	 * Reference.Dominance#PART} nor {@link Reference.Dominance#EXACT} with this sketch or
	 * has a dominance other than {@link Reference.Dominance#NONE} with any of the
	 * sketches contained currently in this sketch.
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
	 * is like telling that other sketch to mark its place as reserved. If the given
	 * sketch is a {@link Reference.Dominance#PART} with a sketch in this sketch. Then the
	 * given {@code sketch} should be put into that sketch instead. On the other hand, if
	 * a sketch in this sketch has a dominance of {@link Reference.Dominance#PART} with
	 * the given {@code sketch}. Then this sketch will transfer that sketch to the given
	 * {@code sketch}. (unless a clash happened, then an exception thrown and nothing
	 * happens)
	 *
	 * @param sketch the sketch to be put.
	 * @throws NullPointerException          if the given {@code sketch} is null.
	 * @throws IllegalStateException         if this sketch is deserialized; if the given
	 *                                       {@code sketch} is clashing with a previously
	 *                                       reserved area (has a dominance of either
	 *                                       {@link Reference.Dominance#SHARE} or {@link
	 *                                       Reference.Dominance#EXACT}).
	 * @throws IllegalArgumentException      if the given {@code sketch} has a dominance
	 *                                       other than {@link Reference.Dominance#PART}
	 *                                       or {@link Reference.Dominance#EXACT} with
	 *                                       this sketch.
	 * @throws UnsupportedOperationException if this sketch cannot have the given {@code
	 *                                       sketch} as an inner sketch.
	 * @since 0.2.0 ~2021.01.12
	 */
	void put(Sketch sketch);

	/**
	 * Get the source of this sketch.
	 *
	 * @return the source of this sketch.
	 * @since 0.2.0 ~2021.01.7
	 */
	Reference reference();

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
		 * Return a new visitor that invokes this visitor then invokes the given {@code
		 * visitor}. (if this visitor did not wish to stop)
		 *
		 * @param visitor the visitor to be invoked after this visitor by the returned
		 *                visitor.
		 * @return a visitor that invokes this visitor then the given visitor.
		 * @throws NullPointerException if the given {@code visitor} is null.
		 * @since 0.2.0 ~2021.01.17
		 */
		default Visitor andThen(Visitor visitor) {
			Objects.requireNonNull(visitor, "visitor");
			return sketch -> this.visit(sketch) || visitor.visit(sketch);
		}

		/**
		 * Visits areas in a non-reserving parent that has not been reserved by any parent
		 * in it.
		 *
		 * @param parent   the non-reserving parent the area was in it.
		 * @param position the position of the area. (relative to the whole document)
		 * @param length   the length of the area.
		 * @return true, if this visitor wishes to stop the loop.
		 * @throws NullPointerException      if the given {@code parent} is null.
		 * @throws IllegalArgumentException  if the given {@code position} or {@code
		 *                                   length} is null. (optional)
		 * @throws IndexOutOfBoundsException if {@code position + length} is larger than
		 *                                   the size of the original document of the
		 *                                   given {@code sketch}. (optional)
		 * @since 0.2.0 ~2021.01.19
		 */
		default boolean visit(Sketch parent, int position, int length) {
			Objects.requireNonNull(parent, "parent");
			return false;
		}

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
