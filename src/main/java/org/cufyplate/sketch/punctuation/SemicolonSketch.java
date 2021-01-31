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
package org.cufyplate.sketch.punctuation;

import org.jamplate.model.reference.Reference;
import org.jamplate.model.sketch.AbstractConcreteSketch;
import org.jamplate.processor.crawler.ConcreteCrawler;
import org.jamplate.processor.crawler.Crawler;
import org.jamplate.processor.maker.Maker;
import org.jamplate.processor.sketcher.CrawlerSketcher;
import org.jamplate.processor.sketcher.Sketcher;

import java.util.regex.Pattern;

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
public final class SemicolonSketch extends AbstractConcreteSketch {
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
	public static final Crawler CRAWLER = new ConcreteCrawler(SemicolonSketch.PATTERN);
	/**
	 * A visitor that makes {@link SemicolonSketch} when it found available semicolon in a
	 * sketch.
	 *
	 * @since 0.2.0 ~2021.01.26
	 */
	public static final Sketcher SKETCHER = new CrawlerSketcher(SemicolonSketch.CRAWLER, SemicolonSketch.MAKER);

	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 1417694492546192558L;

	/**
	 * Construct a new sketch with the given {@code reference}. The given source reference
	 * is the reference the constructed sketch will reserve.
	 *
	 * @param reference the source reference of the constructed sketch.
	 * @throws NullPointerException if the given {@code reference} is null.
	 * @since 0.2.0 ~2021.01.26
	 */
	private SemicolonSketch(Reference reference) {
		super(reference);
	}
}
