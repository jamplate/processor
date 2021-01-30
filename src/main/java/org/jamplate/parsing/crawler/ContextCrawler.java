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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * A crawler implementation that searches for matches that its start and end matches
 * specific patterns.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.01.30
 */
public class ContextCrawler implements Crawler {
	/**
	 * The pattern this crawler uses to match the end of the matches.
	 *
	 * @since 0.2.0 ~2021.01.30
	 */
	protected final Pattern endPattern;
	/**
	 * The pattern this crawler uses to match the start of the matches.
	 *
	 * @since 0.2.0 ~2021.01.30
	 */
	protected final Pattern startPattern;

	/**
	 * Construct a new crawler that crawl for matches that its start matches the given
	 * {@code startRegex} and its end matches the given {@code endRegex}.
	 *
	 * @param startRegex the regex of the start of the matches satisfying the constructed
	 *                   crawler.
	 * @param endRegex   the regex of the end of the matches satisfying the constructed
	 *                   crawler.
	 * @throws NullPointerException   if the given {@code startRegex} or {@code endRegex}
	 *                                is null.
	 * @throws PatternSyntaxException if the given {@code startRegex} or {@code endRegex}
	 *                                has a syntax error.
	 * @since 0.2.0 ~2021.01.30
	 */
	public ContextCrawler(String startRegex, String endRegex) {
		Objects.requireNonNull(startRegex, "startRegex");
		Objects.requireNonNull(endRegex, "endRegex");
		this.startPattern = Pattern.compile(startRegex);
		this.endPattern = Pattern.compile(endRegex);
	}

	/**
	 * Construct a new crawler that crawl for matches that its start matches the given
	 * {@code startPattern} and its end matches the given {@code endPattern}.
	 *
	 * @param startPattern the pattern of the start of the matches satisfying the
	 *                     constructed crawler.
	 * @param endPattern   the pattern of the end of the matches satisfying the
	 *                     constructed crawler.
	 * @throws NullPointerException if the given {@code startPattern} or {@code
	 *                              endPattern} is null.
	 * @since 0.2.0 ~2021.01.30
	 */
	public ContextCrawler(Pattern startPattern, Pattern endPattern) {
		Objects.requireNonNull(startPattern, "startPattern");
		Objects.requireNonNull(endPattern, "endPattern");
		this.startPattern = startPattern;
		this.endPattern = endPattern;
	}

	@Override
	public List<Reference> crawl(Sketch sketch) {
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

				//validate results
				if (i >= 0 && j >= 0) {
					int position = i - reference.position();
					int length = e - i;

					//bingo!
					Reference match = reference.subReference(position, length);
					Reference startMatch = match.subReference(0, j - i);
					Reference endMatch = match.subReference(s - i, e - s);
					return Collections.unmodifiableList(
							Arrays.asList(
									match,
									startMatch,
									endMatch
							)
					);
				}
			}
		}

		//no valid matches
		return null;
	}
}
