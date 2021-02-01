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

import org.jamplate.model.Dominance;
import org.jamplate.model.reference.Reference;
import org.jamplate.processor.visitor.Visitor;
import org.jamplate.runtime.diagnostic.Diagnostic;

import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Objects;
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
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 2088310421014333821L;

	/**
	 * The inner sketches of this sketch.
	 * <br>
	 * The elements of this set must all be not null and cheat-checked and have a
	 * dominance of {@link Dominance#PART} with this sketch and a dominance of {@link
	 * Dominance#NONE} with each other.
	 *
	 * @since 0.2.0 ~2021.01.12
	 */
	protected final NavigableSet<Sketch> sketches = new TreeSet<>(Sketch.COMPARATOR);

	/**
	 * Construct a new sketch with the given {@code reference}. The given source reference
	 * is the source the constructed sketch will reserve.
	 *
	 * @param reference the source reference of the constructed sketch.
	 * @throws NullPointerException if the given {@code reference} is null.
	 * @since 0.2.0 ~2021.01.12
	 */
	protected AbstractContextSketch(Reference reference) {
		super(reference);
	}

	@Override
	public <R> R accept(Visitor<R> visitor) {
		Objects.requireNonNull(visitor, "visitor");
		R results = visitor.visit(this);

		return results != null ?
			   results :
			   this.sketches.stream()
					   .map(sketch -> sketch.accept(visitor))
					   .filter(Objects::nonNull)
					   .findFirst()
					   .orElse(null);
	}

	@Override
	public boolean check(int start, int end) {
		if (start < 0 || start > end)
			return false;

		switch (Dominance.compute(this, start, end)) {
			case PART:
			case EXACT:
				return this.sketches.stream()
						.allMatch(sketch ->
								Dominance.compute(sketch, start, end) == Dominance.NONE
						);
			default:
				return false;
		}
	}

	@Override
	public Iterator<Sketch> iterator() {
		return this.sketches.iterator();
	}

	@Override
	public void put(Sketch sketch) {
		Objects.requireNonNull(sketch, "sketch");
		if (!this.constructed)
			throw new IllegalStateException("Deserialized Sketch");

		//case not Dominance.PART or Dominance.EXACT with this sketch
		switch (Dominance.compute(this, sketch)) {
			case PART:
			case EXACT:
				break;
			default:
				Diagnostic.appendError("Context Sketch Clash", this.reference, sketch.reference());
				throw new IllegalArgumentException("Cannot put a sketch with a dominance other than PART or EXACT");
		}

		//case Dominance.SHARE or Dominance.EXACT with another sketch
		for (Sketch next : this.sketches)
			switch (Dominance.compute(next, sketch)) {
				case SHARE:
				case EXACT:
					Diagnostic.appendError("Ambiguous Sketch Clash", this.reference, next.reference(), sketch.reference());
					throw new IllegalStateException("Sketch Clash");
			}

		//case Dominance.CONTAIN or Dominance.PART with another sketch
		Iterator<Sketch> iterator = this.sketches.iterator();
		while (iterator.hasNext()) {
			Sketch next = iterator.next();

			switch (Dominance.compute(sketch, next)) {
				case CONTAIN:
					next.put(sketch);
					return;
				case PART:
					//wait for the sketch accept the child, then remove it
					sketch.put(next);
					iterator.remove();
					break;
			}
		}

		//case Dominance.NONE or Dominance.CONTAIN with all other sketches
		this.sketches.add(sketch);
	}
}
