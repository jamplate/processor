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
package org.jamplate.parse.parser;

import org.jamplate.parse.sketcher.Sketcher;
import org.jamplate.model.sketch.Sketch;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * An abstraction of the basic functionality of a {@link Parser} that uses {@link Sketcher
 * sketchers} to perform the parsing.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.01.26
 */
public class SketchersParser implements Parser {
	/**
	 * A list of the sketchers used by this parser. (non-null, cheat checked)
	 *
	 * @since 0.2.0 ~2021.01.26
	 */
	protected final List<Sketcher> sketchers;

	/**
	 * Construct a new parser that uses the given {@code sketchers}.
	 *
	 * @param sketchers the sketchers to be used by this parser. (null or elements are
	 *                  simply ignored)
	 * @throws NullPointerException if the given {@code sketchers} is null.
	 * @since 0.2.0 ~2021.01.26
	 */
	public SketchersParser(Sketcher... sketchers) {
		Objects.requireNonNull(sketchers, "sketchers");
		this.sketchers = Arrays.stream(sketchers)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}

	/**
	 * Construct a new parser that uses the given {@code sketchers}.
	 *
	 * @param sketchers the sketchers to be used by this parser. (null or non-sketcher
	 *                  elements are simply ignored)
	 * @throws NullPointerException if the given {@code sketchers} is null.
	 * @since 0.2.0 ~2021.01.26
	 */
	public SketchersParser(Iterable<Sketcher> sketchers) {
		Objects.requireNonNull(sketchers, "sketchers");
		//noinspection ConstantConditions
		this.sketchers = StreamSupport.stream(sketchers.spliterator(), false)
				.filter(Sketcher.class::isInstance)
				.collect(Collectors.toList());
	}

	@Override
	public void parse(Sketch sketch) {
		Objects.requireNonNull(sketch, "sketch");
		for (Sketcher sketcher : this.sketchers)
			while (true) {
				Optional<Sketch> optional = sketch.accept(sketcher);

				if (optional == null)
					break;

				optional.ifPresent(sketch::put);
			}
	}
}
