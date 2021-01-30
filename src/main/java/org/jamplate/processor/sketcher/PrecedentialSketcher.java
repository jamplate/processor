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

import org.jamplate.model.sketch.Sketch;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * A sketcher that sketches the first sketch (in position) from a list of other
 * sketchers.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.01.30
 */
public class PrecedentialSketcher implements Sketcher {
	/**
	 * The sketchers backing this sketcher. (non-null, cheat checked)
	 *
	 * @since 0.2.0 ~2021.01.30
	 */
	protected final List<Sketcher> sketchers;

	/**
	 * Construct a new sketcher that uses the given {@code sketchers}.
	 *
	 * @param sketchers the sketchers to be backing the constructed sketcher.
	 * @throws NullPointerException if the given {@code sketchers} is null.
	 * @since 0.2.0 ~2021.01.30
	 */
	public PrecedentialSketcher(Sketcher... sketchers) {
		Objects.requireNonNull(sketchers, "sketchers");
		this.sketchers = Arrays.stream(sketchers)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}

	/**
	 * Construct a new sketcher that uses the given {@code sketchers}.
	 *
	 * @param sketchers the sketchers to be backing the constructed sketcher.
	 * @throws NullPointerException if the given {@code sketchers} is null.
	 * @since 0.2.0 ~2021.01.30
	 */
	public PrecedentialSketcher(Iterable<Sketcher> sketchers) {
		Objects.requireNonNull(sketchers, "sketchers");
		//noinspection ConstantConditions
		this.sketchers = StreamSupport.stream(sketchers.spliterator(), false)
				.filter(Sketcher.class::isInstance)
				.collect(Collectors.toList());
	}

	@Override
	public Optional<Sketch> visitSketch(Sketch sketch) {
		return this.sketchers.stream()
				.map(sketch::accept)
				.filter(Objects::nonNull)
				.filter(Optional::isPresent)
				.min(Comparator.comparing(Optional::get, Sketch.COMPARATOR))
				.orElse(null);
	}
}