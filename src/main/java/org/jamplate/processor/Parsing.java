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
package org.jamplate.processor;

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
	 * Check if the given range fits in the given {@code sketch} and does not clash and is
	 * not taken by any child in the given {@code sketch}.
	 *
	 * @param sketch the sketch to be checked.
	 * @param i      the first index in the range to be checked.
	 * @param j      one past the last index in range to be checked.
	 * @return true, if the given range is within the given {@code sketch} and does not
	 * 		clash and is not taken by any sketch in it.
	 * @throws NullPointerException     if the given {@code sketch} is null.
	 * @throws IllegalArgumentException if {@code i < 0} or {@code j < i}.
	 * @since 0.2.0 ~2021.05.15
	 */
	public static boolean check(@NotNull Sketch sketch, int i, int j) {
		Objects.requireNonNull(sketch, "sketch");
		if (i < 0 || j < i)
			throw new IllegalArgumentException("i=" + i + " j=" + j);

		switch (Dominance.compute(sketch, i, j)) {
			case PART:
				//if the range is within `sketch`
				for (
						Sketch s = sketch.get(Direction.CHILD);
						s != null;
						s = s.get(Direction.NEXT)
				)
					switch (Dominance.compute(s, i, j)) {
						case CONTAIN:
						case NONE:
							//if the range does not clash with `n`
							continue;
						case PART:
						case EXACT:
						case SHARE:
							return false;
						default:
							throw new InternalError();
					}

				return true;
			case NONE:
			case EXACT:
			case SHARE:
			case CONTAIN:
				return false;
			default:
				throw new InternalError();
		}
	}

	/**
	 * Construct a new matcher over the current content of the document of the given
	 * {@code sketch} with the range of the reference of the given {@code sketch} as the
	 * region of it.
	 *
	 * @param sketch  the sketch to get a matcher over its current content.
	 * @param pattern the pattern of the returned matcher.
	 * @return a matcher from the given {@code pattern} over the current content of the
	 * 		given {@code sketch}.
	 * @throws NullPointerException    if the given {@code sketch} or {@code pattern} is
	 *                                 null.
	 * @throws IllegalStateException   if the reference of the given {@code sketch} is out
	 *                                 of the bounds of its document.
	 * @throws IOError                 if an I/O exception occurred while trying to read
	 *                                 the document of the given {@code sketch}.
	 * @throws UnreadableDocumentError if the document of the given {@code sketch} is not
	 *                                 available for reading.
	 * @since 0.2.0 ~2021.05.15
	 */
	@NotNull
	@Contract(value = "_,_->new", pure = true)
	public static Matcher matcher(@NotNull Sketch sketch, @NotNull Pattern pattern) {
		Objects.requireNonNull(sketch, "sketch");
		Objects.requireNonNull(pattern, "pattern");
		Reference reference = sketch.reference();
		Document document = sketch.getDocument();

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
	 * Parse all the ranges matching the given {@code pattern} in the given {@code
	 * sketch}.
	 * <br>
	 * The returned ranges will all have a dominance of {@link Dominance#NONE NONE}
	 * between them and the children of the given {@code sketch} and will all have a
	 * dominance of {@link Dominance#NONE NONE} between another.
	 * <br>
	 * The returned set is not guaranteed to be mutable nor immutable.
	 *
	 * @param sketch  the sketch to parse in.
	 * @param pattern the pattern to look for.
	 * @return a set of references that matches the given {@code pattern} in the given
	 *        {@code sketch}.
	 * @throws NullPointerException    if the given {@code sketch} or {@code pattern} is
	 *                                 null.
	 * @throws IllegalStateException   if the reference of the given {@code sketch} is out
	 *                                 of the bounds of its document.
	 * @throws IOError                 if an I/O exception occurred while trying to read
	 *                                 the document of the given {@code sketch}.
	 * @throws UnreadableDocumentError if the document of the given {@code sketch} is not
	 *                                 available for reading.
	 * @since 0.2.0 ~2021.05.15
	 */
	@NotNull
	@Contract(value = "_,_->new", pure = true)
	public static Set<Reference> parseAll(@NotNull Sketch sketch, @NotNull Pattern pattern) {
		Objects.requireNonNull(sketch, "sketch");
		Objects.requireNonNull(pattern, "pattern");

		Matcher matcher = Parsing.matcher(sketch, pattern);

		//search for a valid match
		Set<Reference> results = new HashSet<>();

		while (matcher.find()) {
			int i = matcher.start();
			int j = matcher.end();

			//validate match
			if (Parsing.check(sketch, i, j))
				//bingo!
				results.add(
						new Reference(i, j - i)
				);
		}

		return results;
	}

	/**
	 * Parse all the ranges starting with the given {@code startPattern} and ending with
	 * the given {@code endPattern} in the given {@code sketch}.
	 * <br>
	 * The returned set will be a set containing sets with each set having three
	 * references: one for the match, another for the starting sequence and a third for
	 * the ending sequence.
	 * <br>
	 * The returned ranges will all have a dominance of {@link Dominance#NONE} between
	 * them and the children of the given {@code sketch} and will have a dominance of
	 * {@link Dominance#NONE NONE} between each set of ranges.
	 * <br>
	 * The returned set and its inner sets are not guaranteed to be mutable nor
	 * immutable.
	 *
	 * @param sketch       the sketch to parse in.
	 * @param startPattern the pattern matching the start of the ranges to look for.
	 * @param endPattern   the pattern matching the end of the ranges to look for.
	 * @return a set of sets of ranges that matches the given {@code pattern} in the given
	 *        {@code sketch}.
	 * @throws NullPointerException    if the given {@code sketch} or {@code startPattern}
	 *                                 or {@code endPattern} is null.
	 * @throws IllegalStateException   if the reference of the given {@code sketch} is out
	 *                                 of the bounds of its document.
	 * @throws IOError                 if an I/O exception occurred while trying to read
	 *                                 the document of the given {@code sketch}.
	 * @throws UnreadableDocumentError if the document of the given {@code sketch} is not
	 *                                 available for reading.
	 * @since 0.2.0 ~2021.05.15
	 */
	@NotNull
	@Contract(value = "_,_,_->new", pure = true)
	public static Set<Set<Reference>> parseAll(@NotNull Sketch sketch, @NotNull Pattern startPattern, @NotNull Pattern endPattern) {
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
		Objects.requireNonNull(sketch, "sketch");
		Objects.requireNonNull(startPattern, "startPattern");
		Objects.requireNonNull(endPattern, "endPattern");

		Matcher startMatcher = Parsing.matcher(sketch, startPattern);
		Matcher endMatcher = Parsing.matcher(sketch, endPattern);

		Set<Set<Reference>> results = new HashSet<>();

		Reference reference = sketch.reference();
		int p = reference.position();
		int t = p + reference.length();
		int x = p;

		//loop from the first end to the last one
		while (endMatcher.find()) {
			int s = endMatcher.start();
			int e = endMatcher.end();

			//validate the range of the end
			if (Parsing.check(sketch, s, e)) {
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
					if (Parsing.check(sketch, ii, jj)) {
						i = ii;
						j = jj;
					}
				}

				//if a valid start was found
				if (i >= 0 && j >= 0) {
					//bingo!
					results.add(new HashSet<>(Arrays.asList(
							//context
							new Reference(i - p, e - i),
							//start
							new Reference(i - p, j - i),
							//end
							new Reference(s - p, e - s)
					)));

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
	 * sketch}.
	 * <br>
	 * The returned range will all have a dominance of {@link Dominance#NONE NONE} between
	 * it and the children of the given {@code sketch}.
	 *
	 * @param sketch  the sketch to parse in.
	 * @param pattern the pattern to look for.
	 * @return a reference that matches the given {@code pattern} in the given {@code
	 * 		sketch}. Or {@code null} if no match was found.
	 * @throws NullPointerException    if the given {@code sketch} or {@code pattern} is
	 *                                 null.
	 * @throws IllegalStateException   if the reference of the given {@code sketch} is out
	 *                                 of the bounds of its document.
	 * @throws IOError                 if an I/O exception occurred while trying to read
	 *                                 the document of the given {@code sketch}.
	 * @throws UnreadableDocumentError if the document of the given {@code sketch} is not
	 *                                 available for reading.
	 * @since 0.2.0 ~2021.05.15
	 */
	@Nullable
	@Contract(value = "_,_->new", pure = true)
	public static Reference parseFirst(@NotNull Sketch sketch, @NotNull Pattern pattern) {
		Objects.requireNonNull(sketch, "sketch");
		Objects.requireNonNull(pattern, "pattern");

		Matcher matcher = Parsing.matcher(sketch, pattern);

		while (matcher.find()) {
			int i = matcher.start();
			int j = matcher.end();

			if (Parsing.check(sketch, i, j))
				//bingo!
				return new Reference(i, j - i);
		}

		//no valid match
		return null;
	}

	/**
	 * Parse all the ranges starting with the given {@code startPattern} and ending with
	 * the given {@code endPattern} in the given {@code sketch}.
	 * <br>
	 * The returned set will be having three references: one for the match, another for
	 * the starting sequence and a third for the ending sequence.
	 * <br>
	 * The returned ranges will all have a dominance of {@link Dominance#NONE} between
	 * them and the children of the given {@code sketch}.
	 * <br>
	 * The returned set and its inner sets are not guaranteed to be mutable nor
	 * immutable.
	 *
	 * @param sketch       the sketch to parse in.
	 * @param startPattern the pattern matching the start of the range to look for.
	 * @param endPattern   the pattern matching the end of the range to look for.
	 * @return a set of the references matching the given {@code pattern} in the given
	 *        {@code sketch}.
	 * @throws NullPointerException    if the given {@code sketch} or {@code startPattern}
	 *                                 or {@code endPattern} is null.
	 * @throws IllegalStateException   if the reference of the given {@code sketch} is out
	 *                                 of the bounds of its document.
	 * @throws IOError                 if an I/O exception occurred while trying to read
	 *                                 the document of the given {@code sketch}.
	 * @throws UnreadableDocumentError if the document of the given {@code sketch} is not
	 *                                 available for reading.
	 * @since 0.2.0 ~2021.05.15
	 */
	@NotNull
	@Contract(value = "_,_,_->new", pure = true)
	public static Set<Reference> parseFirst(@NotNull Sketch sketch, @NotNull Pattern startPattern, @NotNull Pattern endPattern) {
		Objects.requireNonNull(sketch, "sketch");
		Objects.requireNonNull(startPattern, "startPattern");
		Objects.requireNonNull(endPattern, "endPattern");

		Matcher startMatcher = Parsing.matcher(sketch, startPattern);
		Matcher endMatcher = Parsing.matcher(sketch, endPattern);

		Reference reference = sketch.reference();
		int p = reference.position();
		int t = p + reference.length();

		//find the first valid end
		while (endMatcher.find()) {
			int s = endMatcher.start();
			int e = endMatcher.end();

			//validate the range of the end
			if (Parsing.check(sketch, s, e)) {
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
					if (Parsing.check(sketch, ii, jj)) {
						i = ii;
						j = jj;
					}
				}

				//if a valid start was found
				if (i >= 0 && j >= 0)
					//bingo!
					return new HashSet<>(Arrays.asList(
							//context
							new Reference(i - p, e - i),
							//start
							new Reference(i - p, j - i),
							//end
							new Reference(s - p, e - s)
					));

				//No valid start for the end of this round
			}

			//reset the position of the start matcher (to be used again in the next round)
			startMatcher
					.reset()
					.region(p, t);
		}

		//no valid matches
		return Collections.emptySet();
	}
}
/*
	/**
	 * Parse all the available ranges matching the given {@code pattern} in the given
	 * {@code node}.
	 *
	 * @param node    the node to search on.
	 * @param pattern the pattern to search for.
	 * @return a set of the resultant nodes.
	 * @throws NullPointerException      if the given {@code node} or {@code pattern} is
	 *                                   null.
	 * @throws IOError                   if an I/O error occurred while reading the
	 *                                   document of the sketch of the given {@code
	 *                                   node}.
	 * @throws IllegalStateException     if the document of the sketch of the given {@code
	 *                                   node} is at an illegal state to be read.
	 * @throws IndexOutOfBoundsException if the reference of the sketch of the given
	 *                                   {@code node} is out of the bounds of its
	 *                                   document.
	 * @since 0.2.0 ~2021.04.28
	 * /
	@NotNull
	@Contract(value = "_,_->new", pure = true)
	public static Set<Node<Sketch>> parseAll(@NotNull Node<Sketch> node, @NotNull Pattern pattern) {
		Objects.requireNonNull(node, "node");
		Objects.requireNonNull(pattern, "pattern");

		Sketch sketch = node.get();

		if (sketch == null)
			//ghost sketch
			return Collections.emptySet();

		Document document = sketch.document();
		Reference reference = sketch.reference();

		CharSequence content = document.read();
		int p = reference.position();
		int t = p + reference.length();

		Matcher matcher = pattern
				.matcher(content)
				.region(p, t)
				.useTransparentBounds(true)
				.useAnchoringBounds(true);

		//search for a valid match
		Set<Node<Sketch>> results = new HashSet<>();

		while (matcher.find()) {
			int i = matcher.start();
			int j = matcher.end();

			//validate match
			if (Parsing.check(node, i, j))
				//bingo!
				results.add(new HashNode<>(new Sketch(
						document,
						i, j - i
				)));
		}

		return results;
	}

	/**
	 * Parse all the available ranges their start matches the given {@code startPattern}
	 * and their end matches the given {@code endPattern} in the given {@code node}.
	 *
	 * @param node         the node to search in.
	 * @param startPattern the start of the ranges to search for.
	 * @param endPattern   the end of the ranges to search for.
	 * @return the resultant nodes.
	 * @throws NullPointerException      if the given {@code node} or {@code startPattern}
	 *                                   or {@code endPattern} is null.
	 * @throws IOError                   if an I/O error occurred while reading the
	 *                                   document of the sketch of the given {@code
	 *                                   node}.
	 * @throws IllegalStateException     if the document of the sketch of the given {@code
	 *                                   node} is at an illegal state to be read.
	 * @throws IndexOutOfBoundsException if the reference of the sketch of the given
	 *                                   {@code node} is out of the bounds of its
	 *                                   document.
	 * @since 0.2.0 ~2021.04.28
	 * /
	@SuppressWarnings({"DuplicatedCode", "OverlyLongMethod"})
	@Contract(value = "_,_,_->new", pure = true)
	@NotNull
	public static Set<Node<Sketch>> parseAll(@NotNull Node<Sketch> node, @NotNull Pattern startPattern, @NotNull Pattern endPattern) {
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
		Objects.requireNonNull(node, "node");
		Objects.requireNonNull(startPattern, "startPattern");
		Objects.requireNonNull(endPattern, "endPattern");

		Sketch sketch = node.get();

		if (sketch == null)
			//ghost sketch
			return Collections.emptySet();

		Reference reference = sketch.reference();
		CharSequence content = sketch.document().read();

		int p = reference.position();
		int t = p + reference.length();

		Matcher startMatcher = startPattern
				.matcher(content)
				.region(p, t)
				.useTransparentBounds(true)
				.useAnchoringBounds(true);
		Matcher endMatcher = endPattern
				.matcher(content)
				.region(p, t)
				.useTransparentBounds(true)
				.useAnchoringBounds(true);

		Set<Node<Sketch>> results = new HashSet<>();

		int x = p;

		//loop from the first end to the last one
		while (endMatcher.find()) {
			int s = endMatcher.start();
			int e = endMatcher.end();

			//validate the range of the end
			if (Parsing.check(node, s, e)) {
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
					if (Parsing.check(node, ii, jj)) {
						i = ii;
						j = jj;
					}
				}

				//if a valid start was found
				if (i >= 0 && j >= 0) {
					//bingo!
					Node<Sketch> match = new HashNode<>(new Sketch(
							sketch.document(),
							i - p, e - i
					));
					Node<Sketch> start = new HashNode<>(new Sketch(
							sketch.document(),
							i - p, j - i
					));
					Node<Sketch> end = new HashNode<>(new Sketch(
							sketch.document(),
							s - p, e - s
					));

					match.putNode(Tetragon.BOTTOM, start);
					start.putNode(Tetragon.END, end);
					results.add(match);

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
	 * node}.
	 *
	 * @param node    the node to search on.
	 * @param pattern the pattern to search for.
	 * @return the resultant node.
	 * @throws NullPointerException      if the given {@code node} or {@code pattern} is
	 *                                   null.
	 * @throws IOError                   if an I/O error occurred while reading the
	 *                                   document of the sketch of the given {@code
	 *                                   node}.
	 * @throws IllegalStateException     if the document of the sketch of the given {@code
	 *                                   node} is at an illegal state to be read.
	 * @throws IndexOutOfBoundsException if the reference of the sketch of the given
	 *                                   {@code node} is out of the bounds of its
	 *                                   document.
	 * @since 0.2.0 ~2021.04.28
	 * /
	@Nullable
	@Contract(value = "_,_->new", pure = true)
	public static Node<Sketch> parseFirst(@NotNull Node<Sketch> node, @NotNull Pattern pattern) {
		Objects.requireNonNull(node, "node");
		Objects.requireNonNull(pattern, "pattern");

		Sketch sketch = node.get();

		if (sketch == null)
			//ghost sketch
			return null;

		Document document = sketch.document();
		Reference reference = sketch.reference();

		CharSequence content = document.read();
		int p = reference.position();
		int t = p + reference.length();

		Matcher matcher = pattern
				.matcher(content)
				.region(p, t)
				.useAnchoringBounds(true)
				.useAnchoringBounds(true);

		while (matcher.find()) {
			int i = matcher.start();
			int j = matcher.end();

			if (Parsing.check(node, i, j))
				return new HashNode<>(new Sketch(
						sketch.document(),
						i, j - i
				));
		}

		return null;
	}

	/**
	 * Parse the first range its start matches the given {@code startPattern} and its end
	 * matching the given {@code endPattern} in the given {@code node}.
	 *
	 * @param node         the node to search in.
	 * @param startPattern the start of the range to search for.
	 * @param endPattern   the end of the range to search for.
	 * @return the resultant node.
	 * @throws NullPointerException      if the given {@code node} or {@code startPattern}
	 *                                   or {@code endPattern} is null.
	 * @throws IOError                   if an I/O error occurred while reading the
	 *                                   document of the sketch of the given {@code
	 *                                   node}.
	 * @throws IllegalStateException     if the document of the sketch of the given {@code
	 *                                   node} is at an illegal state to be read.
	 * @throws IndexOutOfBoundsException if the reference of the sketch of the given
	 *                                   {@code node} is out of the bounds of its
	 *                                   document.
	 * @since 0.2.0 ~2021.04.28
	 * /
	@SuppressWarnings({"OverlyLongMethod", "DuplicatedCode"})
	@Contract(pure = true)
	@Nullable
	public static Node<Sketch> parseFirst(@NotNull Node<Sketch> node, @NotNull Pattern startPattern, @NotNull Pattern endPattern) {
		Objects.requireNonNull(node, "node");
		Objects.requireNonNull(startPattern, "startPattern");
		Objects.requireNonNull(endPattern, "endPattern");
		Objects.requireNonNull(node, "node");

		Sketch sketch = node.get();

		if (sketch == null)
			//ghost node
			return null;

		Reference reference = sketch.reference();
		CharSequence content = sketch.document().read();

		int p = reference.position();
		int t = p + reference.length();

		Matcher startMatcher = startPattern
				.matcher(content)
				.region(p, t)
				.useTransparentBounds(true)
				.useAnchoringBounds(true);
		Matcher endMatcher = endPattern
				.matcher(content)
				.region(p, t)
				.useTransparentBounds(true)
				.useAnchoringBounds(true);

		//find the first valid end
		while (endMatcher.find()) {
			int s = endMatcher.start();
			int e = endMatcher.end();

			//validate the range of the end
			if (Parsing.check(node, s, e)) {
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
					if (Parsing.check(node, ii, jj)) {
						i = ii;
						j = jj;
					}
				}

				//if a valid start was found
				if (i >= 0 && j >= 0) {
					//bingo!
					Node<Sketch> match = new HashNode<>(new Sketch(
							sketch.document(),
							i - p, e - i
					));
					Node<Sketch> start = new HashNode<>(new Sketch(
							sketch.document(),
							i - p, j - i
					));
					Node<Sketch> end = new HashNode<>(new Sketch(
							sketch.document(),
							s - p, e - s
					));

					match.putNode(Tetragon.BOTTOM, start);
					start.putNode(Tetragon.END, end);
					return match;
				}

				//No valid start for the end of this round
			}

			//reset the position of the start matcher (to be used again in the next round)
			startMatcher
					.reset()
					.region(p, t);
		}

		//no valid matches
		return null;
	}
*/
//	public static final Comparator<@NotNull Node<@NotNull Sketch>> COMPARATOR =
//			Comparator.comparing(n -> n.get().reference());

//	/**
//	 * Insert the given {@code other} node after the given {@code node}.
//	 *
//	 * @param node
//	 * @param other
//	 */
//	public static void insert(@NotNull Node<Sketch> node, @NotNull Node<Sketch> other) {
//		Nodes.insert(Tetragon.END, node, other);
//	}
//
//	public static void insertAll(@NotNull Node<Sketch> node, @NotNull Node<Sketch> other) {
//		Nodes.insertAll(Tetragon.END, node, other);
//	}

//	/**
//	 * Check if the area {@code [i, j)} is not reserved at the given {@code node}.
//	 *
//	 * @param node the node to be checked.
//	 * @param i    the first index of the range to be checked.
//	 * @param j    one past the last index of the range to be checked.
//	 * @return true, if the range {@code [i, j)} is not reserved. False, otherwise.
//	 * @throws NullPointerException     if the given {@code node} is null.
//	 * @throws IllegalArgumentException if {@code i} is not in the range {@code [0, j]}.
//	 * @since 0.2.0 ~2021.04.28
//	 */
//	@Contract(pure = true)
//	public static boolean check(@NotNull Node<Sketch> node, int i, int j) {
//		Objects.requireNonNull(node, "node");
//		Sketch sketch = node.get();
//
//		if (sketch == null)
//			//ghost node
//			return false;
//
//		switch (Dominance.compute(sketch, i, j)) {
//			case PART:
//			case EXACT:
//				//if the range is within `sketch`
//				for (
//						Node<Sketch> n = node.getNode(Tetragon.BOTTOM);
//						n != null;
//						n = n.getNode(Digon.END)
//				) {
//					Sketch s = n.get();
//
//					if (s == null)
//						//ghost node
//						continue;
//
//					switch (Dominance.compute(s, i, j)) {
//						case CONTAIN:
//						case NONE:
//							//if the range does not clash with `n`
//							continue;
//						default:
//							return false;
//					}
//				}
//
//				return true;
//			default:
//				return false;
//		}
//	}

//horizontal
//	public static void putChild(@NotNull Node<Sketch> node, @NotNull Node<Sketch> child) {
//		Objects.requireNonNull(node, "node");
//		Objects.requireNonNull(child, "child");
//		Node<Sketch> first = node.get(Tetragon.BOTTOM);
//
//		if (first == null) {
//			node.put(Tetragon.BOTTOM, child);
//			return;
//		}
//
//		Parsing.insert(first, child);
//		Parsing.sort(first);
//
//		Sketch sketch = new Sketch.Builder().build();
//	}
//
