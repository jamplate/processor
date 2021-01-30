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
package org.cufyplate.sketch;

import org.jamplate.parse.crawler.ContextCrawler;
import org.jamplate.parse.crawler.Crawler;
import org.jamplate.parse.maker.Maker;
import org.jamplate.parse.sketcher.CrawlerSketcher;
import org.jamplate.parse.sketcher.Sketcher;
import org.jamplate.model.reference.Reference;
import org.jamplate.model.sketch.AbstractConcreteSketch;
import org.jamplate.model.sketch.AbstractContextSketch;

import java.util.regex.Pattern;

/**
 * A class holding classes about sketching {@code ''}. (quotes)
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.01.23
 */
public final class Quotes {
	/**
	 * The maker of the concrete sketch.
	 *
	 * @since 0.2.0 ~2021.01.30
	 */
	public static final Maker MAKER_CONCRETE = QuoteSketch::new;
	/**
	 * The maker of the context sketch.
	 *
	 * @since 0.2.0 ~2021.01.30
	 */
	public static final Maker MAKER_CONTEXT = QuotesSketch::new;

	/**
	 * The pattern of an ending quote.
	 *
	 * @since 0.2.0 ~2021.01.23
	 */
	public static final Pattern PATTERN_END = Pattern.compile("(?<!\\\\)[']");
	/**
	 * The pattern of a starting quote.
	 *
	 * @since 0.2.0 ~2021.01.23
	 */
	public static final Pattern PATTERN_START = Pattern.compile("(?<!\\\\)[']");

	/**
	 * The crawler that crawls for possibly valid quotes.
	 *
	 * @since 0.2.0 ~2021.01.30
	 */
	public static final Crawler CRAWLER = new ContextCrawler(Quotes.PATTERN_START, Quotes.PATTERN_END);

	/**
	 * A visitor that makes {@link QuotesSketch} when it found available quotes pair in a
	 * sketch.
	 *
	 * @since 0.2.0 ~2021.01.23
	 */
	public static final Sketcher SKETCHER = new CrawlerSketcher(Quotes.CRAWLER, Quotes.MAKER_CONTEXT, Quotes.MAKER_CONCRETE, Quotes.MAKER_CONCRETE);

	/**
	 * A private always-fail constructor to avoid any instantiation of this class.
	 *
	 * @throws AssertionError when called.
	 * @since 0.2.0 ~2021.01.23
	 */
	private Quotes() {
		throw new AssertionError("No instance for you!");
	}

	/**
	 * A sketch that represents a {@code '}. (quote)
	 *
	 * @author LSafer
	 * @version 0.2.0
	 * @since 0.2.0 ~2021.01.23
	 */
	public static final class QuoteSketch extends AbstractConcreteSketch {
		@SuppressWarnings("JavaDoc")
		private static final long serialVersionUID = 777485260949931605L;

		/**
		 * Construct a new sketch for the given {@code source}. The given source is the
		 * source the constructed sketch will reserve.
		 *
		 * @param reference the source of the constructed sketch.
		 * @throws NullPointerException if the given {@code source} is null.
		 * @since 0.2.0 ~2021.01.17
		 */
		private QuoteSketch(Reference reference) {
			super(reference);
		}
	}

	/**
	 * A sketch that represents {@code ''}. (quotes context)
	 *
	 * @author LSafer
	 * @version 0.2.0
	 * @since 0.2.0 ~2021.01.23
	 */
	public static final class QuotesSketch extends AbstractContextSketch {
		@SuppressWarnings("JavaDoc")
		private static final long serialVersionUID = -3465886194836402720L;

		/**
		 * Construct a new sketch for the given {@code source}. The given source is the
		 * source the constructed sketch will reserve.
		 *
		 * @param reference the source of the constructed sketch.
		 * @throws NullPointerException if the given {@code source} is null.
		 * @since 0.2.0 ~2021.01.23
		 */
		private QuotesSketch(Reference reference) {
			super(reference);
		}
	}
}
