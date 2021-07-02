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
package org.jamplate.internal.util;

import org.jamplate.model.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOError;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Useful functions for parsing sketches.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.02.20
 */
public final class Parsing {
	/**
	 * Tool classes does not need instantiating.
	 *
	 * @throws AssertionError when called.
	 * @since 0.2.0 ~2021.02.20
	 */
	private Parsing() {
		throw new AssertionError("No instance for you!");
	}

	/**
	 * Construct a new matcher over the current content of the document of the given
	 * {@code tree} with the range of the reference of the given {@code tree} as the
	 * region of it.
	 *
	 * @param tree    the tree to get a matcher over its current content.
	 * @param pattern the pattern of the returned matcher.
	 * @return a matcher from the given {@code pattern} over the current content of the
	 * 		given {@code tree}.
	 * @throws NullPointerException  if the given {@code tree} or {@code pattern} is
	 *                               null.
	 * @throws IllegalStateException if the reference of the given {@code tree} is out of
	 *                               the bounds of its document.
	 * @throws IOError               if an I/O exception occurred while trying to read the
	 *                               document of the given {@code tree}.
	 * @throws DocumentNotFoundError if the document of the given {@code tree} is not
	 *                               available for reading.
	 * @since 0.2.0 ~2021.05.15
	 */
	@NotNull
	@Contract(value = "_,_->new", pure = true)
	public static Matcher matcher(@NotNull Tree tree, @NotNull Pattern pattern) {
		Objects.requireNonNull(tree, "tree");
		Objects.requireNonNull(pattern, "pattern");
		Reference reference = tree.getReference();
		Document document = tree.getDocument();

		CharSequence content = document.read();
		int l = content.length();
		int p = reference.position();
		int t = p + reference.length();

		if (p > l || t > l)
			throw new IllegalStateException("Sketch reference does not fit in its document");

		return pattern.matcher(content)
					  .region(p, t)
					  .useTransparentBounds(true)
					  .useAnchoringBounds(true);
	}

	/**
	 * Parse the whole given {@code tree} with the given {@code pattern} and return the
	 * references of the groups in the given {@code pattern}.
	 *
	 * @param tree    the tree to be parsed.
	 * @param pattern the pattern to look for.
	 * @return an ordered list containing the references of the parsed groups. Or {@code
	 * 		null} if the given {@code tree} does not match the given {@code pattern}.
	 * @throws NullPointerException if the given {@code tree} or {@code pattern} is null.
	 * @since 0.2.0 ~2021.05.30
	 */
	@Nullable
	@Contract(pure = true)
	public static List<Reference> parse(@NotNull Tree tree, @NotNull Pattern pattern) {
		Objects.requireNonNull(tree, "tree");
		Objects.requireNonNull(pattern, "pattern");

		Matcher matcher = Parsing.matcher(tree, pattern);
		Reference reference = tree.getReference();

		if (matcher.matches()) {
			List<Reference> results = new ArrayList<>();

			for (int i = 1, l = matcher.groupCount(); i <= l; i++) {
				int start = matcher.start(i);
				int end = matcher.end(i);

				results.add(new Reference(
						start,
						end - start
				));
			}

			return results;
		}

		return null;
	}

	/**
	 * Parse all the ranges matching the given {@code pattern} in the given {@code tree}.
	 * <br>
	 * The returned ranges will all have a dominance of {@link Dominance#NONE NONE}
	 * between them and the children of the given {@code tree} and will all have a
	 * dominance of {@link Dominance#NONE NONE} between another.
	 * <br>
	 * The returned set is not guaranteed to be mutable nor immutable.
	 *
	 * @param tree    the tree to parse in.
	 * @param pattern the pattern to look for.
	 * @param weight  the weight to accept.
	 * @return a set of references that matches the given {@code pattern} in the given
	 *        {@code tree}.
	 * @throws NullPointerException  if the given {@code tree} or {@code pattern} is
	 *                               null.
	 * @throws IllegalStateException if the reference of the given {@code tree} is out of
	 *                               the bounds of its document.
	 * @throws IOError               if an I/O exception occurred while trying to read the
	 *                               document of the given {@code tree}.
	 * @throws DocumentNotFoundError if the document of the given {@code tree} is not
	 *                               available for reading.
	 * @since 0.2.0 ~2021.05.15
	 */
	@NotNull
	@Contract(value = "_,_,_->new", pure = true)
	public static Set<Reference> parseAll(@NotNull Tree tree, @NotNull Pattern pattern, int weight) {
		Objects.requireNonNull(tree, "tree");
		Objects.requireNonNull(pattern, "pattern");

		Matcher matcher = Parsing.matcher(tree, pattern);

		//search for a valid match
		Set<Reference> results = new HashSet<>();

		while (matcher.find()) {
			int i = matcher.start();
			int j = matcher.end();

			//validate match
			if (Parsing.check(tree, i, j, weight))
				//bingo!
				results.add(
						new Reference(i, j - i)
				);
		}

		return results;
	}

	/**
	 * Parse all the ranges starting with the given {@code startPattern} and ending with
	 * the given {@code endPattern} in the given {@code tree}.
	 * <br>
	 * The returned set will be a set containing sets with each set having three
	 * references: one for the match, another for the starting sequence and a third for
	 * the ending sequence.
	 * <br>
	 * The returned ranges will all have a dominance of {@link Dominance#NONE} between
	 * them and the children of the given {@code tree} and will have a dominance of {@link
	 * Dominance#NONE NONE} between each set of ranges.
	 * <br>
	 * The returned set and its inner sets are not guaranteed to be mutable nor
	 * immutable.
	 *
	 * @param tree         the tree to parse in.
	 * @param startPattern the pattern matching the start of the ranges to look for.
	 * @param endPattern   the pattern matching the end of the ranges to look for.
	 * @param weight       the weight to accept.
	 * @return a set of sets of ranges that matches the given {@code pattern} in the given
	 *        {@code tree}.
	 * @throws NullPointerException  if the given {@code tree} or {@code startPattern} or
	 *                               {@code endPattern} is null.
	 * @throws IllegalStateException if the reference of the given {@code tree} is out of
	 *                               the bounds of its document.
	 * @throws IOError               if an I/O exception occurred while trying to read the
	 *                               document of the given {@code tree}.
	 * @throws DocumentNotFoundError if the document of the given {@code tree} is not
	 *                               available for reading.
	 * @since 0.2.0 ~2021.05.15
	 */
	@NotNull
	@Contract(value = "_,_,_,_->new", pure = true)
	public static Set<List<Reference>> parseAll(@NotNull Tree tree, @NotNull Pattern startPattern, @NotNull Pattern endPattern, int weight) {
		//-E--S-S-S-S---E-E-E-S-E-S-E---S---
		//<>  : : : :   : : : : : : :
		//    < : : :   > : : : : : :
		//      < : :   > : : : : : :
		//        < :   > : : : : : :
		//          <###> : : : : : :
		//              < > : : : : :
		//              <   > : : : :
		//              <     : > : :
		//                    <#> : :
		//                      < : >
		//                        <#>
		Objects.requireNonNull(tree, "tree");
		Objects.requireNonNull(startPattern, "startPattern");
		Objects.requireNonNull(endPattern, "endPattern");

		Matcher startMatcher = Parsing.matcher(tree, startPattern);
		Matcher endMatcher = Parsing.matcher(tree, endPattern);

		Set<List<Reference>> results = new HashSet<>();

		Reference reference = tree.getReference();
		int p = reference.position();
		int t = p + reference.length();
		int x = p;

		//loop from the first end to the last one
		while (endMatcher.find()) {
			int s = endMatcher.start();
			int e = endMatcher.end();

			//validate the range of the end
			if (Parsing.check(tree, s, e, weight)) {
				//the range of the last found start
				int i = -1;
				int j = -1;

				//find the last valid start before the end
				while (startMatcher.find()) {
					int ii = startMatcher.start();
					int jj = startMatcher.end();

					if (ii >= s)
						//the end of the start reached the start of the end
						break;

					//validate the range of the start
					if (Parsing.check(tree, ii, jj, weight)) {
						i = ii;
						j = jj;
					}
				}

				//if a valid start was found
				if (i >= 0 && j >= 0) {
					//bingo!
					results.add(Arrays.asList(
							//context
							new Reference(i, e - i),
							//start
							new Reference(i, j - i),
							//end
							new Reference(s, e - s)
					));

					//make the next rounds search for their start after the end of this match
					x = e;
				}

				//reset the position of the start matcher (to be used in the next round)
				startMatcher
						.reset()
						.region(x, t);
			}
		}

		return results;
	}

	/**
	 * Parse the first range matching the given {@code pattern} in the given {@code
	 * tree}.
	 * <br>
	 * The returned range will all have a dominance of {@link Dominance#NONE NONE} between
	 * it and the children of the given {@code tree}.
	 *
	 * @param tree    the tree to parse in.
	 * @param pattern the pattern to look for.
	 * @param weight  the weight to accept.
	 * @return a reference that matches the given {@code pattern} in the given {@code
	 * 		tree}. Or {@code null} if no match was found.
	 * @throws NullPointerException  if the given {@code tree} or {@code pattern} is
	 *                               null.
	 * @throws IllegalStateException if the reference of the given {@code tree} is out of
	 *                               the bounds of its document.
	 * @throws IOError               if an I/O exception occurred while trying to read the
	 *                               document of the given {@code tree}.
	 * @throws DocumentNotFoundError if the document of the given {@code tree} is not
	 *                               available for reading.
	 * @since 0.2.0 ~2021.05.15
	 */
	@Nullable
	@Contract(value = "_,_,_->new", pure = true)
	public static Reference parseFirst(@NotNull Tree tree, @NotNull Pattern pattern, int weight) {
		Objects.requireNonNull(tree, "tree");
		Objects.requireNonNull(pattern, "pattern");

		Matcher matcher = Parsing.matcher(tree, pattern);

		while (matcher.find()) {
			int i = matcher.start();
			int j = matcher.end();

			if (Parsing.check(tree, i, j, weight))
				//bingo!
				return new Reference(i, j - i);
		}

		//no valid match
		return null;
	}

	/**
	 * Parse all the ranges starting with the given {@code startPattern} and ending with
	 * the given {@code endPattern} in the given {@code tree}.
	 * <br>
	 * The returned set will be having three references: one for the match, another for
	 * the starting sequence and a third for the ending sequence.
	 * <br>
	 * The returned ranges will all have a dominance of {@link Dominance#NONE} between
	 * them and the children of the given {@code tree}.
	 * <br>
	 * The returned set and its inner sets are not guaranteed to be mutable nor
	 * immutable.
	 *
	 * @param tree         the tree to parse in.
	 * @param startPattern the pattern matching the start of the range to look for.
	 * @param endPattern   the pattern matching the end of the range to look for.
	 * @param weight       the weight to accept.
	 * @return a set of the references matching the given {@code pattern} in the given
	 *        {@code tree}.
	 * @throws NullPointerException  if the given {@code tree} or {@code startPattern} or
	 *                               {@code endPattern} is null.
	 * @throws IllegalStateException if the reference of the given {@code tree} is out of
	 *                               the bounds of its document.
	 * @throws IOError               if an I/O exception occurred while trying to read the
	 *                               document of the given {@code tree}.
	 * @throws DocumentNotFoundError if the document of the given {@code tree} is not
	 *                               available for reading.
	 * @since 0.2.0 ~2021.05.15
	 */
	@NotNull
	@Contract(value = "_,_,_,_->new", pure = true)
	public static List<Reference> parseFirst(@NotNull Tree tree, @NotNull Pattern startPattern, @NotNull Pattern endPattern, int weight) {
		Objects.requireNonNull(tree, "tree");
		Objects.requireNonNull(startPattern, "startPattern");
		Objects.requireNonNull(endPattern, "endPattern");

		Matcher startMatcher = Parsing.matcher(tree, startPattern);
		Matcher endMatcher = Parsing.matcher(tree, endPattern);

		Reference reference = tree.getReference();
		int p = reference.position();
		int t = p + reference.length();

		//find the first valid end
		while (endMatcher.find()) {
			int s = endMatcher.start();
			int e = endMatcher.end();

			//validate the range of the end
			if (Parsing.check(tree, s, e, weight)) {
				//the range of the last found start
				int i = -1;
				int j = -1;

				//find the last valid start before the end
				while (startMatcher.find()) {
					int ii = startMatcher.start();
					int jj = startMatcher.end();

					if (ii >= s)
						//the end of the start reached the start of the end
						break;

					//validate the range of the start
					if (Parsing.check(tree, ii, jj, weight)) {
						i = ii;
						j = jj;
					}
				}

				//if a valid start was found
				if (i >= 0 && j >= 0)
					//bingo!
					return Arrays.asList(
							//context
							new Reference(i, e - i),
							//start
							new Reference(i, j - i),
							//end
							new Reference(s, e - s)
					);

				//No valid start for the end of this round
			}

			//reset the position of the start matcher (to be used again in the next round)
			startMatcher
					.reset()
					.region(p, t);
		}

		//no valid matches
		return Collections.emptyList();
	}

	/**
	 * Check if the given range fits in the given {@code tree} and does not clash and is
	 * not taken by any child in the given {@code tree}.
	 *
	 * @param tree   the tree to be checked.
	 * @param i      the first index in the range to be checked.
	 * @param j      one past the last index in range to be checked.
	 * @param weight the weight to accept.
	 * @return true, if the given range is within the given {@code tree} and does not
	 * 		clash and is not taken by any tree in it.
	 * @throws NullPointerException     if the given {@code tree} is null.
	 * @throws IllegalArgumentException if {@code i < 0} or {@code j < i}.
	 * @since 0.2.0 ~2021.05.15
	 */
	@Contract(pure = true)
	private static boolean check(@NotNull Tree tree, int i, int j, int weight) {
		Objects.requireNonNull(tree, "tree");
		if (i < 0 || j < i)
			throw new IllegalArgumentException("i=" + i + " j=" + j);

		switch (Dominance.compute(tree, i, j)) {
			case EXACT:
				if (tree.getWeight() >= weight)
					return false;
			case PART:
				//if the range is within `tree`
				for (Tree s : tree)
					switch (Dominance.compute(s, i, j)) {
						case EXACT:
							if (s.getWeight() <= weight)
								return false;
						case CONTAIN:
						case NONE:
							//if the range does not clash with `n`
							continue;
						case SHARE:
						case PART:
							return false;
						default:
							throw new InternalError();
					}

				return true;
			case NONE:
			case SHARE:
			case CONTAIN:
				return false;
			default:
				throw new InternalError();
		}
	}
}
