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

import org.jamplate.parse.crawler.ConcreteCrawler;
import org.jamplate.parse.crawler.Crawler;
import org.jamplate.parse.maker.Maker;
import org.jamplate.parse.sketcher.CrawlerSketcher;
import org.jamplate.parse.sketcher.Sketcher;
import org.jamplate.model.reference.Reference;
import org.jamplate.model.sketch.AbstractConcreteSketch;

import java.util.regex.Pattern;

/**
 * A class holding classes about sketching {@code ;}. (semicolons)
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.01.26
 */
public final class Semicolon {
	/**
	 * The maker of the sketch.
	 *
	 * @since 0.2.0 ~2021.01.30
	 */
	public static final Maker MAKER = SemicolonSketch::new;

	/**
	 * A pattern that detects semicolons.
	 *
	 * @since 0.2.0 ~2021.01.26
	 */
	public static final Pattern PATTERN = Pattern.compile("[;]");

	/**
	 * The crawler that crawls for possibly valid semicolons.
	 *
	 * @since 0.2.0 ~2021.01.30
	 */
	public static final Crawler CRAWLER = new ConcreteCrawler(Semicolon.PATTERN);

	/**
	 * A visitor that makes {@link SemicolonSketch} when it found available semicolon in a
	 * sketch.
	 *
	 * @since 0.2.0 ~2021.01.26
	 */
	public static final Sketcher SKETCHER = new CrawlerSketcher(Semicolon.CRAWLER, Semicolon.MAKER);

	/**
	 * A private always-fail constructor to avoid any instantiation of this class.
	 *
	 * @throws AssertionError when called.
	 * @since 0.2.0 ~2021.01.26
	 */
	private Semicolon() {
		throw new AssertionError("No instance for you!");
	}

	/**
	 * A sketch for semicolon symbol.
	 * <pre>
	 *     ;
	 * </pre>
	 *
	 * @author LSafer
	 * @version 0.2.0
	 * @since 0.2.0 ~2021.01.18
	 */
	public static final class SemicolonSketch extends AbstractConcreteSketch {
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
		private SemicolonSketch(Reference reference) {
			super(reference);
		}
	}
}
