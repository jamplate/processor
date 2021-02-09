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

import org.jamplate.model.sketch.Sketch;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * A wrapping parser that uses a list of parsers and chooses the first sketch from the
 * first parser produces it. (depending on the order of the parsers list)
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.02.08
 */
public class SequentialParser implements Parser {
	/**
	 * The parsers used by this parser. (the wrapped parsers)
	 *
	 * @since 0.2.0 ~2021.02.09
	 */
	protected final List<Parser> parsers;

	/**
	 * Construct a new parser wrapping the given {@code parser} and choosing the first
	 * sketch produced by the first parser.
	 *
	 * @param parsers the parsers to be wrapped by this parser.
	 * @throws NullPointerException if the given {@code parsers} is null.
	 * @since 0.2.0 ~2021.02.09
	 */
	public SequentialParser(Parser... parsers) {
		Objects.requireNonNull(parsers, "parsers");
		this.parsers = Arrays.stream(parsers)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}

	/**
	 * Construct a new parser wrapping the given {@code parser} and choosing the first
	 * sketch produced by the first parser.
	 *
	 * @param parsers the parsers to be wrapped by this parser.
	 * @throws NullPointerException if the given {@code parsers} is null.
	 * @since 0.2.0 ~2021.02.09
	 */
	public SequentialParser(Iterable<Parser> parsers) {
		Objects.requireNonNull(parsers, "parsers");
		//noinspection ConstantConditions
		this.parsers = StreamSupport.stream(parsers.spliterator(), false)
				.filter(Parser.class::isInstance)
				.collect(Collectors.toList());
	}

	@Override
	public Set<Sketch> parse(Sketch sketch) {
		Objects.requireNonNull(sketch, "sketch");
		return this.parsers.stream()
				.map(parser -> parser.parse(sketch))
				.flatMap(Collection::stream)
				.findFirst()
				.map(Collections::singleton)
				.orElseGet(Collections::emptySet);
	}
}
