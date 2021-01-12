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
	protected final Source source;
	//	/**
	//	 * The sketches in this sketch.
	//	 * <br>
	//	 * It shall be always sorted.
	//	 * <br>
	//	 * It shall have no sketch that has a source that {@link Source#contains(Source)} or {@link Source#clashWith(Source)} with any source of another
	//	 * contained sketch.
	//	 *
	//	 * @since 0.0.2 ~2021.01.7
	//	 */
	//	protected final SortedSet<Sketch> sketches = new TreeSet<>(Sketch.COMPARATOR);
	//	/**
	//	 * The source of this sketch.
	//	 *
	//	 * @since 0.0.2 ~2021.01.7
	//	 */
	//	protected final Source source;
	//
	//	/**
	//	 * Construct a new abstract sketch.
	//	 *
	//	 * @param source the source of this abstract sketch.
	//	 * @throws NullPointerException if the given {@code source} is null.
	//	 * @since 0.0.2 ~2021.01.7
	//	 */
	//	protected AbstractSketch(Source source) {
	//		Objects.requireNonNull(source, "source");
	//		this.source = source;
	//	}
	//
	//	@Override
	//	public boolean check(Sketch sketch) {
	//		Objects.requireNonNull(sketch, "sketch");
	//		return this.source.contains(sketch.source()) &&
	//			   this.sketches.stream()
	//					   .noneMatch(s ->
	//							   s.source().clashWith(sketch.source())
	//					   );
	//	}
	//
	//	@Override
	//	public void put(Sketch sketch) {
	//		Objects.requireNonNull(sketch, "sketch");
	//		if (this.check(sketch))
	//			throw new IllegalArgumentException("Sketch rejected");
	//
	//		this.sketches.add(sketch);
	//		Source source = sketch.source();
	//		//		this.sketches.removeIf(sk -> {
	//		//
	//		//		});
	//	}
	//
	//	@Override
	//	public Source source() {
	//		return this.source;
	//	}
	//
	//	@Override
	//	public void visit(Visitor<Sketch> visitor) {
	//		//		this.reserved.entrySet()
	//		//				.stream()
	//		//				.sorted(Comparator.comparingLong(e -> e.getKey()[0]))
	//		//				.map(Map.Entry::getValue)
	//		//				.distinct()
	//		//				.forEach(/visitor::visit);
	//	}

	/**
	 * <br>
	 * The elements of this set must all have a dominance of {@link Dominance#PART} with
	 * this sketch.
	 */
	protected SortedSet<Sketch> sketches = new TreeSet<>(Sketch.COMPARATOR);

	public AbstractSketch(Source source) {
		Objects.requireNonNull(source, "source");
		this.source = source;
	}

	//	@Override
	//	public boolean available() {
	//		if (this.sketches.stream().anyMatch(Sketch::available))
	//			return true;
	//
	//		//assert that `this.sketches` is sorted and has no clashes
	//		Iterator<Sketch> iterator = this.sketches.iterator();
	//
	//		if (iterator.hasNext()) {
	//			Sketch sketch = iterator.next();
	//
	//			if (iterator.hasNext()) {
	//				if (Sketch.relation(this, sketch) == Relation.START) {
	//					do {
	//						Sketch s = iterator.next();
	//
	//						if (Sketch.relation(sketch, s) != Relation.NEXT)
	//							//a one didn't reserved from its previous -> there is a gap
	//							return true;
	//					} while (iterator.hasNext());
	//
	//					//the last one isn't at end -> there is a gap
	//					return Sketch.relation(this, sketch) != Relation.END;
	//				}
	//
	//				//first didn't reserved from the start -> there is a gap
	//				return true;
	//			}
	//
	//			//just one reserved -> that one is not reserving everything
	//			return Sketch.dominance(this, sketch) != Dominance.EXACT;
	//		}
	//
	//		//nothing reserved -> this is not empty
	//		return this.source.length() != 0;
	//	}
	//
	//	public Sketch available() {
	//
	//	}

	@SuppressWarnings("OverlyComplexMethod")
	@Override
	public boolean accept(SketchVisitor visitor) {
		Objects.requireNonNull(visitor, "visitor");

		if (visitor.visitSketch(this))
			return true;

		Iterator<Sketch> iterator = this.sketches.iterator();

		if (iterator.hasNext()) {
			Sketch last = iterator.next();

			if (Sketch.relation(this, last) != Relation.START)
				//There is a gap between the start and the first sketch
				if (visitor.visitSource(this, Source.cutStartStart(
						this.source,
						last.source()
				)))
					return true;

			if (last.accept(visitor))
				return true;

			while (iterator.hasNext()) {
				Sketch next = iterator.next();

				if (Sketch.relation(last, next) != Relation.NEXT)
					//There is a gap between the two sketches
					if (visitor.visitSource(this, Source.cutEndStart(
							last.source(),
							next.source()
					)))
						return true;

				if (next.accept(visitor))
					return true;

				last = next;
			}

			if (Sketch.relation(this, last) != Relation.END)
				//There is a gap between the last sketch and the end
				return visitor.visitSource(this, Source.cutEndEnd(
						last.source(),
						this.source
				));

			return false;
		}

		//The whole source is available
		return visitor.visitSource(this, this.source);
	}

	@Override
	public void put(Sketch sketch) {
		Objects.requireNonNull(sketch, "sketch");
		if (Sketch.dominance(this, sketch) != Dominance.PART)
			throw new IllegalArgumentException("Cannot put a sketch with a dominance other than PART");

		//clash check...
		for (Sketch next : this.sketches) {
			Dominance dominance = Sketch.dominance(next, sketch);

			if (dominance == Dominance.SHARE || dominance == Dominance.EXACT)
				throw new IllegalStateException("Sketch Clash");
		}

		//part/contain check...
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

		//NONE
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
