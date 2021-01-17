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

import org.jamplate.model.source.Source;

import java.util.Comparator;
import java.util.Objects;
import java.util.regex.Matcher;
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
	static Source find(Sketch sketch, Pattern pattern) {
		Objects.requireNonNull(pattern, "pattern");
		Objects.requireNonNull(pattern, "pattern");

		Source source = sketch.source();

		Matcher matcher = source.matcher(pattern);

		//search for a valid match
		while (matcher.find()) {
			int i = matcher.start();
			int j = matcher.end();

			//validate match
			if (sketch.check(i, j))
				//bingo!
				return source.slice(
						i,
						j - i
				);
		}

		//no valid matches
		return null;
	}

	/**
	 * Find an available source that starts with the given {@code startPatter} and ends
	 * with the given {@code endPatter}. The two patterns each has a dominance of {@link
	 * Source.Dominance#NONE} with every inner sketch in the given {@code sketch}. The
	 * returned source will have no AVAILABLE sequence that matches the given {@code
	 * startPattern} in between.
	 *
	 * @param sketch       the sketch to find in.
	 * @param startPattern the pattern of the start.
	 * @param endPattern   the pattern of the end.
	 * @return a source that starts with the given {@code startPattern} and ends with
	 *        {@code endPattern} in the given {@code sketch}.
	 * @throws NullPointerException if the given {@code sketch} or {@code startPattern} or
	 *                              {@code endPattern} is null.
	 * @since 0.2.0 ~2021.01.17
	 */
	static Source find(Sketch sketch, Pattern startPattern, Pattern endPattern) {
		Objects.requireNonNull(sketch, "sketch");
		Objects.requireNonNull(startPattern, "startPattern");
		Objects.requireNonNull(endPattern, "endPattern");

		Source source = sketch.source();

		Matcher startMatcher = source.matcher(startPattern);
		Matcher endMatcher = source.matcher(endPattern);

		//search for a valid start
		while (startMatcher.find()) {
			int i = startMatcher.start();
			int j = startMatcher.end();

			//validate start
			if (sketch.check(i, j))
				//search for a valid end
				while (endMatcher.find()) {
					int s = endMatcher.start();
					int e = endMatcher.end();

					//validate end
					if (sketch.check(s, e)) {
						int start = i;
						int end = e;

						//search for a shorter start
						while (startMatcher.find()) {
							int ii = startMatcher.start();
							int jj = startMatcher.end();

							//break?
							if (ii > s)
								break;
							//validate shorter start
							if (sketch.check(ii, jj))
								start = ii;
						}

						//bingo!
						return source.slice(
								start,
								end - start
						);
					}
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
	 * Check if the given area {@code [start, end)} can be put to this sketch or not. An
	 * area get rejected when the area has a dominance other than {@link
	 * Source.Dominance#PART} nor {@link Source.Dominance#EXACT} with this sketch or has a
	 * dominance other than {@link Source.Dominance#NONE} with any of the sketches
	 * contained currently in this sketch.
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
