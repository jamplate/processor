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
package org.jamplate.parsing.crawler;

import org.jamplate.source.reference.Reference;
import org.jamplate.source.sketch.Sketch;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * A crawler implementation that searches for matches matching a specific pattern.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.01.30
 */
public class ConcreteCrawler implements Crawler {
	/**
	 * The pattern this crawler is crawling for.
	 *
	 * @since 0.2.0 ~2021.01.30
	 */
	protected final Pattern pattern;

	/**
	 * Construct a new crawler that crawl for matches that match the given {@code regex}.
	 *
	 * @param regex the regex satisfying the constructed crawler.
	 * @throws NullPointerException   if the given {@code regex} is null.
	 * @throws PatternSyntaxException if the given {@code regex} has a syntax error.
	 * @since 0.2.0 ~2021.01.30
	 */
	public ConcreteCrawler(String regex) {
		Objects.requireNonNull(regex, "regex");
		this.pattern = Pattern.compile(regex);
	}

	/**
	 * Construct a new crawler that crawl for matches that match the given {@code
	 * pattern}.
	 *
	 * @param pattern the pattern satisfying the constructed crawler.
	 * @throws NullPointerException if the given {@code pattern} is null.
	 * @since 0.2.0 ~2021.01.30
	 */
	public ConcreteCrawler(Pattern pattern) {
		Objects.requireNonNull(pattern, "pattern");
		this.pattern = pattern;
	}

	@Override
	public List<Reference> crawl(Sketch sketch) {
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
				int position = i - reference.position();
				int length = j - i;

				//bingo!
				Reference match = reference.subReference(position, length);
				return Collections.singletonList(
						match
				);
			}
		}

		//no valid matches
		return null;
	}
}
