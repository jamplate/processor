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
package org.jamplate.impl.sketch;

import org.jamplate.parsing.crawler.ConcreteCrawler;
import org.jamplate.parsing.crawler.Crawler;
import org.jamplate.parsing.maker.Maker;
import org.jamplate.parsing.sketcher.CrawlerSketcher;
import org.jamplate.parsing.sketcher.Sketcher;
import org.jamplate.source.reference.Reference;
import org.jamplate.source.sketch.AbstractConcreteSketch;

import java.util.regex.Pattern;

/**
 * A class holding classes about sketching {@code ,}. (comma)
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.01.26
 */
public final class Comma {
	/**
	 * The maker of the sketch.
	 *
	 * @since 0.2.0 ~2021.01.30
	 */
	public static final Maker MAKER = CommaSketch::new;

	/**
	 * A pattern that detects commas.
	 *
	 * @since 0.2.0 ~2021.01.26
	 */
	public static final Pattern PATTERN = Pattern.compile("[,]");

	/**
	 * The crawler that crawls for possibly valid commas.
	 *
	 * @since 0.2.0 ~2021.01.30
	 */
	public static final Crawler CRAWLER = new ConcreteCrawler(Comma.PATTERN);

	/**
	 * A visitor that makes {@link CommaSketch} when it found available comma in a
	 * sketch.
	 *
	 * @since 0.2.0 ~2021.01.26
	 */
	public static final Sketcher SKETCHER = new CrawlerSketcher(Comma.CRAWLER, Comma.MAKER);

	/**
	 * A private always-fail constructor to avoid any instantiation of this class.
	 *
	 * @throws AssertionError when called.
	 * @since 0.2.0 ~2021.01.26
	 */
	private Comma() {
		throw new AssertionError("No instance for you!");
	}

	/**
	 * A sketch for comma symbol.
	 * <pre>
	 *     ,
	 * </pre>
	 *
	 * @author LSafer
	 * @version 0.2.0
	 * @since 0.2.0 ~2021.01.18
	 */
	public static final class CommaSketch extends AbstractConcreteSketch {
		@SuppressWarnings("JavaDoc")
		private static final long serialVersionUID = 1417694492546192558L;

		/**
		 * Construct a new sketch with the given {@code reference}. The given source
		 * reference is the reference the constructed sketch will reserve.
		 *
		 * @param reference the source reference of the constructed sketch.
		 * @throws NullPointerException if the given {@code reference} is null.
		 * @since 0.2.0 ~2021.01.26
		 */
		private CommaSketch(Reference reference) {
			super(reference);
		}
	}
}
