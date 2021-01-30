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

import org.jamplate.processor.crawler.ContextCrawler;
import org.jamplate.processor.crawler.Crawler;
import org.jamplate.processor.maker.Maker;
import org.jamplate.processor.sketcher.CrawlerSketcher;
import org.jamplate.processor.sketcher.Sketcher;
import org.jamplate.model.reference.Reference;
import org.jamplate.model.sketch.AbstractConcreteSketch;
import org.jamplate.model.sketch.AbstractContextSketch;

import java.util.regex.Pattern;

/**
 * A class holding classes about sketching {@code {}}. (curly brackets)
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.01.23
 */
public final class Braces {
	/**
	 * The maker of the concrete sketch.
	 *
	 * @since 0.2.0 ~2021.01.30
	 */
	public static final Maker MAKER_CONCRETE = BraceSketch::new;
	/**
	 * The maker of the context sketch.
	 *
	 * @since 0.2.0 ~2021.01.30
	 */
	public static final Maker MAKER_CONTEXT = BracesSketch::new;

	/**
	 * A pattern that detects the start of a brackets context.
	 *
	 * @since 0.2.0 ~2021.01.18
	 */
	public static final Pattern PATTERN_END = Pattern.compile("[}]");
	/**
	 * A pattern that detects the end of a brackets context.
	 *
	 * @since 0.2.0 ~2021.01.18
	 */
	public static final Pattern PATTERN_START = Pattern.compile("[{]");

	/**
	 * The crawler that crawls for possibly valid braces.
	 *
	 * @since 0.2.0 ~2021.01.30
	 */
	public static final Crawler CRAWLER = new ContextCrawler(Braces.PATTERN_START, Braces.PATTERN_END);

	/**
	 * A visitor that makes {@link BracesSketch} when it found available brackets pair in
	 * a sketch.
	 *
	 * @since 0.2.0 ~2021.01.18
	 */
	public static final Sketcher SKETCHER = new CrawlerSketcher(Braces.CRAWLER, Braces.MAKER_CONTEXT, Braces.MAKER_CONCRETE, Braces.MAKER_CONCRETE);

	/**
	 * A private always-fail constructor to avoid any instantiation of this class.
	 *
	 * @throws AssertionError when called.
	 * @since 0.2.0 ~2021.01.23
	 */
	private Braces() {
		throw new AssertionError("No instance for you!");
	}

	/**
	 * A sketch for bracket symbol.
	 * <pre>
	 *     {
	 * </pre>
	 * <pre>
	 *     }
	 * </pre>
	 *
	 * @author LSafer
	 * @version 0.2.0
	 * @since 0.2.0 ~2021.01.18
	 */
	public static final class BraceSketch extends AbstractConcreteSketch {
		@SuppressWarnings("JavaDoc")
		private static final long serialVersionUID = 3996900909023911874L;

		/**
		 * Construct a new sketch for the given {@code source}. The given source is the
		 * source the constructed sketch will reserve.
		 *
		 * @param reference the source of the constructed sketch.
		 * @throws NullPointerException if the given {@code source} is null.
		 * @since 0.2.0 ~2021.01.18
		 */
		private BraceSketch(Reference reference) {
			super(reference);
		}
	}

	/**
	 * A sketch for brackets context.
	 * <pre>
	 *     {}
	 * </pre>
	 *
	 * @author LSafer
	 * @version 0.2.0
	 * @since 0.2.0 ~2021.01.18
	 */
	public static final class BracesSketch extends AbstractContextSketch {
		@SuppressWarnings("JavaDoc")
		private static final long serialVersionUID = 1738781066145922168L;

		/**
		 * Construct a new sketch for the given {@code source}. The given source is the
		 * source the constructed sketch will reserve.
		 *
		 * @param reference the source of the constructed sketch.
		 * @throws NullPointerException if the given {@code source} is null.
		 * @since 0.2.0 ~2021.01.18
		 */
		private BracesSketch(Reference reference) {
			super(reference);
		}
	}
}
