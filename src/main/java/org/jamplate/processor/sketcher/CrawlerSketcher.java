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
package org.jamplate.processor.sketcher;

import org.jamplate.model.reference.Reference;
import org.jamplate.model.sketch.Sketch;
import org.jamplate.processor.crawler.Crawler;
import org.jamplate.processor.maker.Maker;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

/**
 * A sketcher that uses a {@link Crawler} to search for valid references.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.01.30
 */
public class CrawlerSketcher implements Sketcher {
	/**
	 * The crawler used by this sketcher.
	 *
	 * @since 0.2.0 ~2021.01.30
	 */
	protected final Crawler crawler;
	/**
	 * An ordered list of the constructors of each reference result from the crawler of
	 * this. (non-null, cheat-checked)
	 *
	 * @since 0.2.0 ~2021.01.30
	 */
	protected final List<Maker> makers;

	/**
	 * Construct a new sketcher that uses the given {@code crawler}.
	 *
	 * @param crawler the crawler to be used by this sketcher.
	 * @param makers  an array of maker for each reference returned as a result from the
	 *                given {@code crawler}.
	 * @throws NullPointerException if the given {@code crawler} or {@code makers} is
	 *                              null.
	 * @since 0.2.0 ~2021.01.30
	 */
	public CrawlerSketcher(Crawler crawler, Maker... makers) {
		Objects.requireNonNull(crawler, "makers");
		this.crawler = crawler;
		this.makers = Arrays.stream(makers)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}

	/**
	 * Construct a new sketcher that uses the given {@code crawler}.
	 *
	 * @param crawler the crawler to be used by this sketcher.
	 * @param makers  an array of maker for each reference returned as a result from the
	 *                given {@code crawler}.
	 * @throws NullPointerException if the given {@code crawler} or {@code makers} is
	 *                              null.
	 * @since 0.2.0 ~2021.01.30
	 */
	public CrawlerSketcher(Crawler crawler, Iterable<Maker> makers) {
		Objects.requireNonNull(crawler, "crawler");
		Objects.requireNonNull(makers, "makers");
		this.crawler = crawler;
		//noinspection ConstantConditions
		this.makers = StreamSupport.stream(makers.spliterator(), false)
				.filter(Maker.class::isInstance)
				.collect(Collectors.toList());
	}

	@Override
	public Sketch visit(Sketch sketch) {
		Objects.requireNonNull(sketch, "sketch");
		List<Reference> references = this.crawler.crawl(sketch);

		if (references != null)
			return IntStream.range(0, references.size())
					.limit(this.makers.size())
					.mapToObj(i ->
							this.makers.get(i).make(references.get(i))
					)
					.reduce((root, sub) -> {
						root.put(sub);
						return root;
					})
					.orElse(null);

		return null;
	}
}
