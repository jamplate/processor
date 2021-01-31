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
package org.cufyplate.sketch.group;

import org.cufyplate.sketch.anchor.BraceSketch;
import org.jamplate.model.reference.Reference;
import org.jamplate.model.sketch.AbstractContextSketch;
import org.jamplate.processor.crawler.ContextCrawler;
import org.jamplate.processor.crawler.Crawler;
import org.jamplate.processor.maker.Maker;
import org.jamplate.processor.sketcher.CrawlerSketcher;
import org.jamplate.processor.sketcher.Sketcher;

import java.util.regex.Pattern;

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
public final class BracesSketch extends AbstractContextSketch {
	/**
	 * The maker of the context sketch.
	 *
	 * @since 0.2.0 ~2021.01.30
	 */
	public static final Maker MAKER = BracesSketch::new;
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
	public static final Crawler CRAWLER = new ContextCrawler(BracesSketch.PATTERN_START, BracesSketch.PATTERN_END);
	/**
	 * A visitor that makes {@link BracesSketch} when it found available brackets pair in
	 * a sketch.
	 *
	 * @since 0.2.0 ~2021.01.18
	 */
	public static final Sketcher SKETCHER = new CrawlerSketcher(BracesSketch.CRAWLER, BracesSketch.MAKER, BraceSketch.MAKER, BraceSketch.MAKER);

	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 1738781066145922168L;

	/**
	 * Construct a new sketch for the given {@code source}. The given source is the source
	 * the constructed sketch will reserve.
	 *
	 * @param reference the source of the constructed sketch.
	 * @throws NullPointerException if the given {@code source} is null.
	 * @since 0.2.0 ~2021.01.18
	 */
	private BracesSketch(Reference reference) {
		super(reference);
	}
}
