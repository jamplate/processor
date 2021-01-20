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
package org.jamplate.source.sketch;

import org.jamplate.Diagnostic;
import org.jamplate.source.reference.Reference;

import java.util.Iterator;
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
	 * The elements of this set. Must all have a dominance of {@link
	 * Reference.Dominance#PART} with this sketch and a dominance of {@link
	 * Reference.Dominance#NONE} with each other.
	 *
	 * @since 0.2.0 ~2021.01.12
	 */
	protected final TreeSet<Sketch> sketches = new TreeSet<>(Sketch.COMPARATOR);

	/**
	 * Construct a new sketch for the given {@code source}. The given source is the source
	 * the constructed sketch will reserve.
	 *
	 * @param reference the source of the constructed sketch.
	 * @throws NullPointerException if the given {@code source} is null.
	 * @since 0.2.0 ~2021.01.12
	 */
	protected AbstractContextSketch(Reference reference) {
		super(reference);
	}

	@SuppressWarnings("OverlyComplexMethod")
	@Override
	public boolean accept(Visitor visitor) {
		Objects.requireNonNull(visitor, "visitor");

		//visit this
		if (visitor.visit(this))
			return true;

		Iterator<Sketch> iterator = this.sketches.iterator();

		int i = this.reference.position();
		int m = this.reference.length();
		int j = i + m;

		//if this sketch has any inner sketches
		if (iterator.hasNext()) {
			Sketch first = iterator.next();

			int s0 = first.reference().position();
			int ep = s0 + first.reference().length();

			//visit `[i, s0)` (if not empty)
			if (i != s0 && visitor.visit(this, i, s0 - i))
				return true;

			//visit the first sketch
			if (first.accept(visitor))
				return true;

			//iterate over next sketches (might be none)
			while (iterator.hasNext()) {
				Sketch next = iterator.next();

				int sn = next.reference().position();
				int en = sn + next.reference().length();

				//visit `[ep, sn)` (if not empty)
				if (ep < sn && visitor.visit(this, ep, sn - ep))
					return true;

				//visit the next sketch
				if (next.accept(visitor))
					return true;

				ep = en;
			}

			//visit `[ep, j)` (if not empty)
			return ep != j && visitor.visit(this, ep, j - ep);
		}

		//visit `[i, j)` (if not empty)
		return m != 0 && visitor.visit(this, i, m);
	}

	@Override
	public boolean check(int start, int end) {
		if (start < 0 || start > end)
			return false;
		Reference.Dominance d = Reference.dominance(this.reference, start, end);
		return (d == Reference.Dominance.PART || d == Reference.Dominance.EXACT) &&
			   this.sketches.stream()
					   .allMatch(sketch ->
							   Reference.dominance(sketch.reference(), start, end) ==
							   Reference.Dominance.NONE
					   );
	}

	@Override
	public void put(Sketch sketch) {
		if (!this.constructed)
			throw new IllegalStateException("Deserialized Sketch");

		Objects.requireNonNull(sketch, "sketch");
		//case not Dominance.PART or Dominance.EXACT with this sketch
		switch (Reference.dominance(this.reference, sketch.reference())) {
			case PART:
			case EXACT:
				break;
			default:
				Diagnostic.printError("Context Sketch Clash", this.reference, sketch.reference());
				throw new IllegalArgumentException("Cannot put a sketch with a dominance other than PART");
		}

		//case Dominance.SHARE or Dominance.EXACT with another sketch
		for (Sketch next : this.sketches)
			switch (Reference.dominance(next.reference(), sketch.reference())) {
				case SHARE:
				case EXACT:
					Diagnostic.printError("Ambiguous Sketch Clash", next.reference(), sketch.reference());
					throw new IllegalStateException("Sketch Clash");
			}

		//case Dominance.CONTAIN or Dominance.PART with another sketch
		Iterator<Sketch> iterator = this.sketches.iterator();
		while (iterator.hasNext()) {
			Sketch next = iterator.next();

			switch (Reference.dominance(sketch.reference(), next.reference())) {
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
