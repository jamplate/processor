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
package org.jamplate.parsing;

import org.jamplate.source.reference.Reference;
import org.jamplate.source.sketch.Sketch;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Temporary class!!!
 *
 * @author LSafer
 * @version 0.0.0
 * @since 0.0.0 ~2021.01.30
 */
public class Patterns {
	//todo ReferenceMatcher
	//todo ReferenceDoubleMatcher

	/**
	 * Find an available area in the given {@code sketch} that matches the given {@code
	 * regex}.
	 *
	 * @param sketch the sketch to match an area of it to the given regex.
	 * @param regex  the regex to match an area in the given sketch with.
	 * @return a reference from the available area in the given {@code sketch} that
	 * 		matches the given {@code regex} or null if no matching area was found.
	 * @throws NullPointerException   if the given {@code sketch} or {@code regex} is
	 *                                null.
	 * @throws PatternSyntaxException if the given {@code regex} has a syntax error.
	 * @since 0.2.0 ~2021.01.25
	 */
	public static Reference find(Sketch sketch, String regex) {
		Objects.requireNonNull(sketch, "sketch");
		Objects.requireNonNull(regex, "regex");
		return Patterns.find(sketch, Pattern.compile(regex));
	}

	/**
	 * Find an available area in the given {@code sketch} that matches the given {@code
	 * pattern}.
	 *
	 * @param sketch  the sketch to match an area of it to the given pattern.
	 * @param pattern the pattern to match an area in the given sketch with.
	 * @return a reference from the available area in the given {@code sketch} that
	 * 		matches the given {@code pattern} or null if no matching area was found.
	 * @throws NullPointerException if the given {@code sketch} or {@code pattern} is
	 *                              null.
	 * @since 0.2.0 ~2021.01.17
	 */
	public static Reference find(Sketch sketch, Pattern pattern) {
		Objects.requireNonNull(sketch, "sketch");
		Objects.requireNonNull(pattern, "pattern");

		Reference reference = sketch.reference();

		Matcher matcher = Patterns.matcher(reference, pattern);

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
	 * Find an available area in the given {@code sketch} that starts with the given
	 * {@code startRegex} and ends with the given {@code endRegex}. The start and the end
	 * should be both {@link Sketch#check(int, int) checked} by the given {@code sketch}
	 * and the check returned {@code true}.
	 *
	 * @param sketch     the sketch to match an area of it to the given regexes.
	 * @param startRegex the regex to match the start of an area in the given sketch
	 *                   with.
	 * @param endRegex   the regex to match the end of an area in teh given sketch with.
	 * @return a new fixed length array. The first item of the array is the reference
	 * 		matching the area between (inclusive) {@code startRegex} and {@code endRegex}.
	 * 		The second item of the array is the reference matching {@code startRegex}. The
	 * 		third item of the array is the reference matching {@code endRegex}.
	 * @throws NullPointerException   if the given {@code sketch} or {@code startRegex} or
	 *                                {@code endRegex} is null.
	 * @throws PatternSyntaxException if the given {@code startRegex} or {@code endRegex}
	 *                                has a syntax error.
	 * @since 0.2.0 ~2021.01.25
	 */
	public static Reference[] find(Sketch sketch, String startRegex, String endRegex) {
		Objects.requireNonNull(sketch, "sketch");
		Objects.requireNonNull(startRegex, "startRegex");
		Objects.requireNonNull(endRegex, "endRegex");
		return Patterns.find(sketch, Pattern.compile(startRegex), Pattern.compile(endRegex));
	}

	/**
	 * Find an available area in the given {@code sketch} that starts with the given
	 * {@code startPattern} and ends with the given {@code endPattern}. The start and the
	 * end should be both {@link Sketch#check(int, int) checked} by the given {@code
	 * sketch} and the check returned {@code true}.
	 *
	 * @param sketch       the sketch to match an area of it to the given patterns.
	 * @param startPattern the pattern to match the start of an area in the given sketch
	 *                     with.
	 * @param endPattern   the pattern to match the end of an area in teh given sketch
	 *                     with.
	 * @return a new fixed length array. The first item of the array is the reference
	 * 		matching the area between (inclusive) {@code startPattern} and {@code
	 * 		endPattern}. The second item of the array is the reference matching {@code
	 * 		startPattern}. The third item of the array is the reference matching {@code
	 * 		endPattern}.
	 * @throws NullPointerException if the given {@code sketch} or {@code startPattern} or
	 *                              {@code endPattern} is null.
	 * @since 0.2.0 ~2021.01.17
	 */
	public static Reference[] find(Sketch sketch, Pattern startPattern, Pattern endPattern) {
		Objects.requireNonNull(sketch, "sketch");
		Objects.requireNonNull(startPattern, "startPattern");
		Objects.requireNonNull(endPattern, "endPattern");

		Reference reference = sketch.reference();

		Matcher endMatcher = Patterns.matcher(reference, endPattern);

		//find the first valid end
		while (endMatcher.find()) {
			int s = endMatcher.start();
			int e = endMatcher.end();

			//validate end
			if (sketch.check(s, e)) {
				Matcher startMatcher = Patterns.matcher(reference, startPattern);

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
	 * Construct a ready-to-use matcher from the given {@code reference}. The returned
	 * matcher has the whole content of the document of the given {@code reference}. But,
	 * it is limited to the region of the given {@code reference} (using {@link
	 * Matcher#region(int, int)}). The returned matcher also has {@link
	 * Matcher#hasTransparentBounds()} and {@link Matcher#useAnchoringBounds(boolean)}
	 * both enabled.
	 * <br>
	 * Notice: the returned matcher will return {@link Matcher#start()} and {@link
	 * Matcher#end()} as indexes at the original document.
	 *
	 * @param reference the reference to create a matcher for.
	 * @param pattern   the pattern to match.
	 * @return a matcher over the content of the given {@code reference}.
	 * @throws NullPointerException  if the given {@code reference} or {@code pattern} is
	 *                               null.
	 * @throws IllegalStateException if the {@link Reference#document() document} of the
	 *                               given {@code reference} is a deserialized document.
	 * @since 0.2.0 ~2021.01.23
	 */
	public static Matcher matcher(Reference reference, Pattern pattern) {
		Objects.requireNonNull(reference, "reference");
		Objects.requireNonNull(pattern, "pattern");
		Matcher matcher = pattern.matcher(reference.document().readContent());
		matcher.region(reference.position(), reference.position() + reference.length());
		matcher.useTransparentBounds(true);
		matcher.useAnchoringBounds(true);
		return matcher;
	}
}
