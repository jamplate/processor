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

import org.jamplate.source.reference.Dominance;
import org.jamplate.source.reference.Reference;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
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
	 * Find an available source that starts with the given {@code startPatter} and ends
	 * with the given {@code endPatter}. The two patterns each has a dominance of {@link
	 * Dominance#NONE} with every inner sketch in the given {@code sketch}. The returned
	 * source will have no AVAILABLE sequence that matches the given {@code startPattern}
	 * in between.
	 *
	 * @param sketch       the sketch to find in.
	 * @param startPattern the pattern of the start.
	 * @param endPattern   the pattern of the end.
	 * @return a new fixed length array with the second item being the reference matching
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
	 *         Visited via {@link Visitor#visitSketch(Sketch)}
	 *     </li>
	 *     <li>
	 *         Any non-reserved areas (areas that has no sketch reserving it) met.
	 *         <br>
	 *         Visited via {@link Visitor#visitNonSketched(Sketch, int, int)}
	 *     </li>
	 * </ul>
	 *
	 * @param visitor the visitor to be invoked for each element.
	 * @param <R>     the type of the results of the given {@code visitor}.
	 * @return an optional containing the results of visiting an element. Or {@code null}
	 * 		if the visitor wishes to continue the loop.
	 * @throws NullPointerException if the given {@code visitor} is null.
	 * @since 0.2.0 ~2021.01.11
	 */
	<R> Optional<R> accept(Visitor<R> visitor);

	/**
	 * Check if the given area {@code [start, end)} can be put to this sketch or not. An
	 * area get rejected when the area has a dominance other than {@link Dominance#PART}
	 * nor {@link Dominance#EXACT} with this sketch or has a dominance other than {@link
	 * Dominance#NONE} with any of the sketches contained currently in this sketch.
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
	 * sketch is a {@link Dominance#PART} with a sketch in this sketch. Then the given
	 * {@code sketch} should be put into that sketch instead. On the other hand, if a
	 * sketch in this sketch has a dominance of {@link Dominance#PART} with the given
	 * {@code sketch}. Then this sketch will transfer that sketch to the given {@code
	 * sketch}. (unless a clash happened, then an exception thrown and nothing happens)
	 *
	 * @param sketch the sketch to be put.
	 * @throws NullPointerException          if the given {@code sketch} is null.
	 * @throws IllegalStateException         if this sketch is deserialized; if the given
	 *                                       {@code sketch} is clashing with a previously
	 *                                       reserved area (has a dominance of either
	 *                                       {@link Dominance#SHARE} or {@link
	 *                                       Dominance#EXACT}).
	 * @throws IllegalArgumentException      if the given {@code sketch} has a dominance
	 *                                       other than {@link Dominance#PART} or {@link
	 *                                       Dominance#EXACT} with this sketch.
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
}
