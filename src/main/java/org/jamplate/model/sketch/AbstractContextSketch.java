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
package org.jamplate.model.sketch;

import org.jamplate.model.source.Source;

import java.util.Iterator;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * An abstract of the interface {@link Sketch} that implements the basic functionality of
 * a context sketch. (a context sketch is a sketch that can have inner sketches)
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.01.07
 */
public abstract class AbstractContextSketch extends AbstractSketch {
	/**
	 * The inner sketches of this sketch.
	 * <br>
	 * The elements of this set. Must all have a dominance of {@link
	 * Source.Dominance#PART} with this sketch and a dominance of {@link
	 * Source.Dominance#NONE} with each other.
	 *
	 * @since 0.2.0 ~2021.01.12
	 */
	protected final SortedSet<Sketch> sketches = new TreeSet<>(Sketch.COMPARATOR);

	/**
	 * Construct a new sketch for the given {@code source}. The given source is the source
	 * the constructed sketch will reserve.
	 *
	 * @param source the source of the constructed sketch.
	 * @throws NullPointerException if the given {@code source} is null.
	 * @since 0.2.0 ~2021.01.12
	 */
	protected AbstractContextSketch(Source source) {
		super(source);
	}

	@Override
	public boolean accept(Visitor visitor) {
		Objects.requireNonNull(visitor, "visitor");
		return visitor.visit(this) ||
			   this.sketches.stream().anyMatch(
					   sketch -> sketch.accept(visitor)
			   );
	}

	@Override
	public boolean check(int start, int end) {
		if (start < 0 || start > end)
			return false;
		Source.Dominance dominance = Source.dominance(this.source, start, end);
		return (dominance == Source.Dominance.PART ||
				dominance == Source.Dominance.EXACT) &&
			   this.sketches.stream()
					   .allMatch(sketch ->
							   Source.dominance(sketch.source(), start, end) ==
							   Source.Dominance.NONE
					   );
	}

	@Override
	public void put(Sketch sketch) {
		Objects.requireNonNull(sketch, "sketch");
		//case not Dominance.PART or Dominance.EXACT with this sketch
		switch (Source.dominance(this.source, sketch.source())) {
			case PART:
			case EXACT:
				break;
			default:
				throw new IllegalArgumentException("Cannot put a sketch with a dominance other than PART");
		}

		//case Dominance.SHARE or Dominance.EXACT with another sketch
		for (Sketch next : this.sketches)
			switch (Source.dominance(next.source(), sketch.source())) {
				case SHARE:
				case EXACT:
					throw new IllegalStateException("Sketch Clash");
			}

		//case Dominance.CONTAIN or Dominance.PART with another sketch
		Iterator<Sketch> iterator = this.sketches.iterator();
		while (iterator.hasNext()) {
			Sketch next = iterator.next();

			switch (Source.dominance(sketch.source(), next.source())) {
				case CONTAIN:
					next.put(sketch);
					return;
				case PART:
					iterator.remove();
					sketch.put(next);
					break;
			}
		}

		//case Dominance.NONE with all other sketches
		this.sketches.add(sketch);
	}
}
