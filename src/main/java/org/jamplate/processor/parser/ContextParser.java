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
package org.jamplate.processor.parser;

import org.intellij.lang.annotations.Language;
import org.jamplate.model.reference.Reference;
import org.jamplate.model.sketch.Sketch;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * A parser that targets sequences that its start matching a start-pattern and its end
 * matching an end-pattern.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.02.08
 */
@SuppressWarnings("ClassWithTooManyConstructors")
public class ContextParser implements Parser {
	/**
	 * A function backing this parser when it needs to construct a context-sketch for a
	 * match.
	 *
	 * @since 0.2.0 ~2021.02.09
	 */
	protected final Function<Reference, Sketch> contextConstructor;
	/**
	 * A function backing this parser when it needs to construct a new end-sketch for a
	 * match.
	 *
	 * @since 0.2.0 ~2021.02.09
	 */
	protected final Function<Reference, Sketch> endConstructor;
	/**
	 * The pattern to be used by this parser to match the end of the sequences with.
	 *
	 * @since 0.2.0 ~2021.02.09
	 */
	protected final Pattern endPattern;
	/**
	 * A function backing this parser when it needs to construct a new start-sketch for a
	 * match.
	 *
	 * @since 0.2.0 ~2021.02.09
	 */
	protected final Function<Reference, Sketch> startConstructor;
	/**
	 * The pattern to be used by this parser to match the start of the sequences with.
	 *
	 * @since 0.2.0 ~2021.02.09
	 */
	protected final Pattern startPattern;

	/**
	 * Construct a new parser that uses the given {@code startRegex} and {@code
	 * endRegex}.
	 *
	 * @param startRegex  the start-regex to be used by the constructed parser.
	 * @param endRegex    the end-regex to be used by the constructed parser.
	 * @param constructor a function backing the constructed parser to construct a new
	 *                    context-sketches.
	 * @throws NullPointerException   if the given {@code startRegex} or {@code endRegex}
	 *                                or {@code constructor} is null.
	 * @throws PatternSyntaxException if the given {@code startRegex} or {@code endRegex}
	 *                                has a syntax error.
	 * @since 0.2.0 ~2021.02.09
	 */
	public ContextParser(
			@Language("RegExp") String startRegex,
			@Language("RegExp") String endRegex,
			Function<Reference, Sketch> constructor
	) {
		Objects.requireNonNull(startRegex, "startRegex");
		Objects.requireNonNull(endRegex, "endRegex");
		Objects.requireNonNull(constructor, "constructor");
		this.startPattern = Pattern.compile(startRegex);
		this.endPattern = Pattern.compile(endRegex);
		this.contextConstructor = constructor;
		this.startConstructor = r -> null;
		this.endConstructor = r -> null;
	}

	/**
	 * Construct a new parser that uses the given {@code startRegex} and {@code
	 * endRegex}.
	 *
	 * @param startRegex         the start-regex to be used by the constructed parser.
	 * @param endRegex           the end-regex to be used by the constructed parser.
	 * @param contextConstructor a function backing the constructed parser to construct a
	 *                           new context-sketches.
	 * @param anchorConstructor  a function backing the constructed parser to construct a
	 *                           new start-sketches/end-sketches.
	 * @throws NullPointerException   if the given {@code startRegex} or {@code endRegex}
	 *                                or {@code contextConstructor} or {@code
	 *                                anchorConstructor} is null.
	 * @throws PatternSyntaxException if the given {@code startRegex} or {@code endRegex}
	 *                                has a syntax error.
	 * @since 0.2.0 ~2021.02.09
	 */
	public ContextParser(
			@Language("RegExp") String startRegex,
			@Language("RegExp") String endRegex,
			Function<Reference, Sketch> contextConstructor,
			Function<Reference, Sketch> anchorConstructor
	) {
		Objects.requireNonNull(startRegex, "startRegex");
		Objects.requireNonNull(endRegex, "endRegex");
		Objects.requireNonNull(contextConstructor, "contextConstructor");
		Objects.requireNonNull(anchorConstructor, "anchorConstructor");
		this.startPattern = Pattern.compile(startRegex);
		this.endPattern = Pattern.compile(endRegex);
		this.contextConstructor = contextConstructor;
		this.startConstructor = anchorConstructor;
		this.endConstructor = anchorConstructor;
	}

	/**
	 * Construct a new parser that uses the given {@code startRegex} and {@code
	 * endRegex}.
	 *
	 * @param startRegex         the start-regex to be used by the constructed parser.
	 * @param endRegex           the end-regex to be used by the constructed parser.
	 * @param contextConstructor a function backing the constructed parser to construct a
	 *                           new context-sketches.
	 * @param startConstructor   a function backing the constructed parser to construct a
	 *                           new start-sketches.
	 * @param endConstructor     a function backing the constructed parser to construct a
	 *                           new end-sketches.
	 * @throws NullPointerException   if the given {@code startRegex} or {@code endRegex}
	 *                                or {@code contextConstructor} or {@code
	 *                                startConstructor} or {@code endConstructor} is
	 *                                null.
	 * @throws PatternSyntaxException if the given {@code startRegex} or {@code endRegex}
	 *                                has a syntax error.
	 * @since 0.2.0 ~2021.02.09
	 */
	public ContextParser(
			@Language("RegExp") String startRegex,
			@Language("RegExp") String endRegex,
			Function<Reference, Sketch> contextConstructor,
			Function<Reference, Sketch> startConstructor,
			Function<Reference, Sketch> endConstructor
	) {
		Objects.requireNonNull(startRegex, "startRegex");
		Objects.requireNonNull(endRegex, "endRegex");
		Objects.requireNonNull(contextConstructor, "contextConstructor");
		Objects.requireNonNull(startConstructor, "startConstructor");
		Objects.requireNonNull(endConstructor, "endConstructor");
		this.startPattern = Pattern.compile(startRegex);
		this.endPattern = Pattern.compile(endRegex);
		this.contextConstructor = contextConstructor;
		this.startConstructor = startConstructor;
		this.endConstructor = endConstructor;
	}

	/**
	 * Construct a new parser that uses the given {@code startPattern} and {@code
	 * endPattern}.
	 *
	 * @param startPattern the start-pattern to be used by the constructed parser.
	 * @param endPattern   the end-pattern to be used by the constructed parser.
	 * @param constructor  a function backing the constructed parser to construct a new
	 *                     context-sketches.
	 * @throws NullPointerException if the given {@code startPattern} or {@code
	 *                              endPattern} or {@code constructor} is null.
	 * @since 0.2.0 ~2021.02.09
	 */
	public ContextParser(
			Pattern startPattern,
			Pattern endPattern,
			Function<Reference, Sketch> constructor
	) {
		Objects.requireNonNull(startPattern, "startPattern");
		Objects.requireNonNull(endPattern, "endPattern");
		Objects.requireNonNull(constructor, "constructor");
		this.startPattern = startPattern;
		this.endPattern = endPattern;
		this.contextConstructor = constructor;
		this.startConstructor = r -> null;
		this.endConstructor = r -> null;
	}

	/**
	 * Construct a new parser that uses the given {@code startPattern} and {@code
	 * endPattern}.
	 *
	 * @param startPattern       the start-pattern to be used by the constructed parser.
	 * @param endPattern         the end-pattern to be used by the constructed parser.
	 * @param contextConstructor a function backing the constructed parser to construct a
	 *                           new context-sketches.
	 * @param anchorConstructor  a function backing the constructed parser to construct a
	 *                           new start-sketches/end-sketches.
	 * @throws NullPointerException if the given {@code startPattern} or {@code
	 *                              endPattern} or {@code contextConstructor} or {@code
	 *                              anchorConstructor} is null.
	 * @since 0.2.0 ~2021.02.09
	 */
	public ContextParser(
			Pattern startPattern,
			Pattern endPattern,
			Function<Reference, Sketch> contextConstructor,
			Function<Reference, Sketch> anchorConstructor
	) {
		Objects.requireNonNull(startPattern, "startPattern");
		Objects.requireNonNull(endPattern, "endPattern");
		Objects.requireNonNull(contextConstructor, "contextConstructor");
		Objects.requireNonNull(anchorConstructor, "anchorConstructor");
		this.startPattern = startPattern;
		this.endPattern = endPattern;
		this.contextConstructor = contextConstructor;
		this.startConstructor = anchorConstructor;
		this.endConstructor = anchorConstructor;
	}

	/**
	 * Construct a new parser that uses the given {@code startPattern} and {@code
	 * endPattern}.
	 *
	 * @param startPattern       the start-pattern to be used by the constructed parser.
	 * @param endPattern         the end-pattern to be used by the constructed parser.
	 * @param contextConstructor a function backing the constructed parser to construct a
	 *                           new context-sketches.
	 * @param startConstructor   a function backing the constructed parser to construct a
	 *                           new start-sketches.
	 * @param endConstructor     a function backing the constructed parser to construct a
	 *                           new end-sketches.
	 * @throws NullPointerException if the given {@code startPattern} or {@code
	 *                              endPattern} or {@code contextConstructor} or {@code
	 *                              startConstructor} or {@code endConstructor} is null.
	 * @since 0.2.0 ~2021.02.09
	 */
	public ContextParser(
			Pattern startPattern,
			Pattern endPattern,
			Function<Reference, Sketch> contextConstructor,
			Function<Reference, Sketch> startConstructor,
			Function<Reference, Sketch> endConstructor
	) {
		Objects.requireNonNull(startPattern, "startPattern");
		Objects.requireNonNull(endPattern, "endPattern");
		Objects.requireNonNull(contextConstructor, "contextConstructor");
		Objects.requireNonNull(startConstructor, "startConstructor");
		Objects.requireNonNull(endConstructor, "endConstructor");
		this.startPattern = startPattern;
		this.endPattern = endPattern;
		this.contextConstructor = contextConstructor;
		this.startConstructor = startConstructor;
		this.endConstructor = endConstructor;
	}

	@SuppressWarnings("OverlyLongMethod")
	@Override
	public Set<Sketch> parse(Sketch sketch) {
		Objects.requireNonNull(sketch, "sketch");

		Reference reference = sketch.reference();
		CharSequence content = reference.document().readContent();

		int p = reference.position();
		int t = p + reference.length();

		Matcher startMatcher = this.startPattern.matcher(content)
				.region(p, t)
				.useTransparentBounds(true)
				.useAnchoringBounds(true);
		Matcher endMatcher = this.endPattern.matcher(content)
				.region(p, t)
				.useTransparentBounds(true)
				.useAnchoringBounds(true);

		//find the first valid end
		while (endMatcher.find()) {
			int s = endMatcher.start();
			int e = endMatcher.end();

			//validate end
			if (sketch.check(s, e)) {
				startMatcher.reset()
						.region(p, t);

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

				//validate match
				if (i >= 0 && j >= 0) {
					//bingo!
					Sketch context = this.contextConstructor.apply(reference.subReference(
							i - p, e - i
					));

					if (context != null) {
						Sketch start = this.startConstructor.apply(reference.subReference(
								i - p, j - i
						));
						Sketch end = this.endConstructor.apply(reference.subReference(
								s - p, e - s
						));

						if (start != null)
							context.put(start);
						if (end != null)
							context.put(end);

						return Collections.singleton(context);
					}
				}
			}
		}

		//no valid matches
		return Collections.emptySet();
	}
}
