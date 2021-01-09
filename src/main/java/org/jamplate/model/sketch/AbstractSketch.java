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

/**
 * An abstract of the interface {@link Sketch} that implements the basic functionality of a sketch.
 *
 * @author LSafer
 * @version 0.0.2
 * @since 0.0.2 ~2021.01.07
 */
public abstract class AbstractSketch implements Sketch {
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
}
