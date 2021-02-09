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
 * A parser that targets sequences matching a pattern.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.01.26
 */
public class ConcreteParser implements Parser {
	/**
	 * A function backing this parser when it needs to construct a sketch for a match.
	 *
	 * @since 0.2.0 ~2021.02.09
	 */
	protected final Function<Reference, Sketch> constructor;
	/**
	 * The pattern to be used by this parser to match the sequences with.
	 *
	 * @since 0.2.0 ~2021.02.09
	 */
	protected final Pattern pattern;

	/**
	 * Construct a new parser that uses the given {@code regex}.
	 *
	 * @param regex       the regex to be used by the constructed parser.
	 * @param constructor a function backing the constructed parser to construct a new
	 *                    sketches.
	 * @throws NullPointerException   if the given {@code regex} or {@code constructor} is
	 *                                null.
	 * @throws PatternSyntaxException if the given {@code regex} has a syntax error.
	 * @since 0.2.0 ~2021.02.09
	 */
	public ConcreteParser(
			@Language("RegExp") String regex,
			Function<Reference, Sketch> constructor
	) {
		Objects.requireNonNull(regex, "regex");
		Objects.requireNonNull(constructor, "constructor");
		this.pattern = Pattern.compile(regex);
		this.constructor = constructor;
	}

	/**
	 * Construct a new parser that uses the given {@code pattern}.
	 *
	 * @param pattern     the pattern to be used by the constructed parser.
	 * @param constructor a function backing the constructed parser to construct a new
	 *                    sketches.
	 * @throws NullPointerException if the given {@code pattern} or {@code constructor} is
	 *                              null.
	 * @since 0.2.0 ~2021.02.09
	 */
	public ConcreteParser(
			Pattern pattern,
			Function<Reference, Sketch> constructor
	) {
		Objects.requireNonNull(pattern, "pattern");
		Objects.requireNonNull(constructor, "constructor");
		this.pattern = pattern;
		this.constructor = constructor;
	}

	@Override
	public Set<Sketch> parse(Sketch sketch) {
		Objects.requireNonNull(sketch, "sketch");

		Reference reference = sketch.reference();
		CharSequence content = reference.document().readContent();

		int p = reference.position();
		int t = p + reference.length();

		Matcher matcher = this.pattern.matcher(content)
				.region(p, t)
				.useTransparentBounds(true)
				.useAnchoringBounds(true);

		//search for a valid match
		while (matcher.find()) {
			int i = matcher.start();
			int j = matcher.end();

			//validate match
			if (sketch.check(i, j)) {
				//bingo!
				Sketch match = this.constructor.apply(reference.subReference(
						i - p, j - i
				));

				if (match != null)
					return Collections.singleton(match);
			}
		}

		//no valid matches
		return Collections.emptySet();
	}
}
