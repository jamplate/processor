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

import org.jamplate.model.source.Dominance;
import org.jamplate.model.source.Relation;
import org.jamplate.model.source.Source;

import java.util.Iterator;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * An abstract of the interface {@link Sketch} that implements the basic functionality of
 * a sketch.
 *
 * @author LSafer
 * @version 0.0.2
 * @since 0.0.2 ~2021.01.07
 */
public abstract class AbstractSketch implements Sketch {
	/**
	 * <br>
	 * The elements of this set must all have a dominance of {@link Dominance#PART} with
	 * this sketch.
	 */
	protected final Source source;

	public AbstractSketch(Source source) {
		Objects.requireNonNull(source, "source");
		this.source = source;
	}

	@SuppressWarnings("OverlyComplexMethod")
	@Override
	public boolean accept(SketchVisitor visitor) {
		Objects.requireNonNull(visitor, "visitor");

		//Visit this
		if (visitor.visitSketch(this))
			return true;

		Iterator<Sketch> iterator = this.sketches.iterator();

		//Check if this sketch has elements (if so, visit them)
		if (iterator.hasNext()) {
			Sketch last = iterator.next();

			//Check if there is a source before the first sketch (if so, visit it)
			if (Sketch.relation(this, last) != Relation.START)
				if (visitor.visitSource(this, Source.cutStartStart(
						this.source,
						last.source()
				)))
					return true;

			//Visit the first sketch
			if (last.accept(visitor))
				return true;

			//Loop over the sketches after the first sketch
			while (iterator.hasNext()) {
				Sketch next = iterator.next();

				//Check if there is a source between the 'last' sketch and the 'next' sketch (if so, visit it)
				if (Sketch.relation(last, next) != Relation.NEXT)
					if (visitor.visitSource(this, Source.cutEndStart(
							last.source(),
							next.source()
					)))
						return true;

				//Visit the 'next' sketch
				if (next.accept(visitor))
					return true;

				last = next;
			}

			//Check if there is a source after the last sketch (if so, visit it)
			if (Sketch.relation(this, last) != Relation.END)
				return visitor.visitSource(this, Source.cutEndEnd(
						last.source(),
						this.source
				));

			//do not stop
			return false;
		}

		//Visit the source of this (only if this sketch has no inner sketches)
		return visitor.visitSource(this, this.source);
	}

	@Override
	public void put(Sketch sketch) {
		Objects.requireNonNull(sketch, "sketch");
		//case not Dominance.PART with this sketch
		if (Sketch.dominance(this, sketch) != Dominance.PART)
			throw new IllegalArgumentException("Cannot put a sketch with a dominance other than PART");

		//case Dominance.SHARE or Dominance.EXACT with another sketch
		for (Sketch next : this.sketches) {
			Dominance dominance = Sketch.dominance(next, sketch);

			if (dominance == Dominance.SHARE || dominance == Dominance.EXACT)
				throw new IllegalStateException("Sketch Clash");
		}

		//case Dominance.CONTAIN or Dominance.PART with another sketch
		Iterator<Sketch> iterator = this.sketches.iterator();
		while (iterator.hasNext()) {
			Sketch next = iterator.next();

			Dominance dominance = Sketch.dominance(sketch, next);

			if (dominance == Dominance.CONTAIN) {
				next.put(sketch);
				return;
			}
			if (dominance == Dominance.PART) {
				iterator.remove();
				sketch.put(next);
			}
		}

		//case Dominance.NONE with all other sketches
		this.sketches.add(sketch);
	}

	@Override
	public Source source() {
		return this.source;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " (" + this.source + ")";
	}
}
