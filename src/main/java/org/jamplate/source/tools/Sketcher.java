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
package org.jamplate.source.tools;

import org.jamplate.source.sketch.Sketch;

import java.util.*;

/**
 * A visitor sub-interface specialized for visitors that builds the sketch hierarchy.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.01.24
 */
@FunctionalInterface
public interface Sketcher extends SketchVisitor<Sketch> {
	/**
	 * Return a visitor that loops until all the given {@code sketchers} can not find any
	 * matching sketch in the provided sketch anymore. Foreach found sketch in a provided
	 * sketch, the returned visitor will {@link Sketch#put(Sketch)} it to the provided
	 * sketch. The returned visitor will always return {@code null}. (will always continue
	 * the loop)
	 *
	 * @param sketchers the sketchers the returned visitor will use to find sketches.
	 * @return a visitor that builds the sketch hierarchy of the provided sketch using the
	 * 		given {@code sketchers}.
	 * @throws NullPointerException if the given {@code sketchers} is null.
	 * @since 0.2.0 ~2021.01.25
	 */
	static SketchVisitor<?> builder(Sketcher... sketchers) {
		Objects.requireNonNull(sketchers, "sketchers");
		return Sketcher.builder(Arrays.asList(sketchers));
	}

	/**
	 * Return a visitor that loops until all the given {@code sketchers} can not find any
	 * matching sketch in the provided sketch anymore. Foreach found sketch in a provided
	 * sketch, the returned visitor will {@link Sketch#put(Sketch)} it to the provided
	 * sketch. The returned visitor will always return {@code null}. (will always continue
	 * the loop)
	 *
	 * @param sketchers the sketchers the returned visitor will use to find sketches.
	 * @return a visitor that builds the sketch hierarchy of the provided sketch using the
	 * 		given {@code sketchers}.
	 * @throws NullPointerException if the given {@code sketchers} is null.
	 * @since 0.2.0 ~2021.01.25
	 */
	static SketchVisitor<?> builder(Collection<Sketcher> sketchers) {
		Objects.requireNonNull(sketchers, "sketchers");
		//noinspection OverlyLongLambda
		return sketch -> {
			sketchers.stream()
					.filter(Objects::nonNull)
					.forEach(sketcher -> {
						Optional<Sketch> s;
						while ((s = sketch.accept(sketcher)) != null)
							sketch.put(s.get());
					});
			//it is safe from concurrent modification exception
			//since the children will be added to the sketch before
			//the sketch starting to iterate over its children
			return /*continue :)*/ null;
		};
	}

	/**
	 * Combines the given {@code sketchers} into one sketcher that takes the first
	 * matching sketch.
	 *
	 * @param sketchers the sketchers to be combined.
	 * @return a sketcher from the given {@code sketchers}.
	 * @throws NullPointerException if the given {@code sketchers} is null.
	 * @since 0.2.0 ~2021.01.24
	 */
	static Sketcher precedence(Sketcher... sketchers) {
		Objects.requireNonNull(sketchers, "sketchers");
		return Sketcher.precedence(Arrays.asList(sketchers));
	}

	/**
	 * Combines the given {@code sketchers} into one sketcher that takes the first
	 * matching sketch. (first in position)
	 *
	 * @param sketchers the sketchers to be combined.
	 * @return a sketcher from the given {@code sketchers}.
	 * @throws NullPointerException if the given {@code sketchers} is null.
	 * @since 0.2.0 ~2021.01.24
	 */
	static Sketcher precedence(Collection<Sketcher> sketchers) {
		Objects.requireNonNull(sketchers, "sketchers");
		return sketch ->
				sketchers.stream()
						.filter(Objects::nonNull)
						.map(sketch::accept)
						.filter(Objects::nonNull)
						.filter(Optional::isPresent)
						.min(Comparator.comparing(Optional::get, Sketch.COMPARATOR))
						.orElse(null);
	}

	/**
	 * Combines the given {@code sketchers} into one sketcher that takes the first
	 * matching sketch. (first on sketcher)
	 *
	 * @param sketchers the sketchers to be combined.
	 * @return a sketcher from the given {@code sketchers}.
	 * @throws NullPointerException if teh given {@code sketchers} is null.
	 * @since 0.2.0 ~2021.01.24
	 */
	static Sketcher sequence(Sketcher... sketchers) {
		Objects.requireNonNull(sketchers, "sketchers");
		return Sketcher.sequence(Arrays.asList(sketchers));
	}

	/**
	 * Combines the given {@code sketchers} into one sketcher that takes the first
	 * matching sketch. (first on sketcher)
	 *
	 * @param sketchers the sketchers to be combined.
	 * @return a sketcher from the given {@code sketchers}.
	 * @throws NullPointerException if teh given {@code sketchers} is null.
	 * @since 0.2.0 ~2021.01.24
	 */
	static Sketcher sequence(Collection<Sketcher> sketchers) {
		Objects.requireNonNull(sketchers, "sketchers");
		return sketch ->
				sketchers.stream()
						.filter(Objects::nonNull)
						.map(sketch::accept)
						.filter(Objects::nonNull)
						.findFirst()
						.orElse(null);
	}

	/**
	 * Return a matching sketch in the given {@code area} to be {@link Sketch#put(Sketch)}
	 * to the given {@code parent} or a parent of it.
	 *
	 * @param parent   {@inheritDoc}
	 * @param position {@inheritDoc}
	 * @param length   {@inheritDoc}
	 * @return a sketch to be {@link Sketch#put(Sketch)} to the given {@code parent} or a
	 * 		parent of it. Or {@code null} if no matching sketch. (aka, continue the loop)
	 * @throws NullPointerException      {@inheritDoc}
	 * @throws IllegalArgumentException  {@inheritDoc}
	 * @throws IndexOutOfBoundsException {@inheritDoc}
	 * @since 0.2.0 ~2021.01.24
	 */
	@SuppressWarnings("RedundantMethodOverride")
	@Override
	default Optional<Sketch> visitNonSketched(Sketch parent, int position, int length) {
		Objects.requireNonNull(parent, "parent");
		return null;
	}

	/**
	 * Return a matching sketch in the given {@code sketch} to be {@link
	 * Sketch#put(Sketch)} to it or a parent of it.
	 *
	 * @param sketch {@inheritDoc}
	 * @return a sketch to be {@link Sketch#put(Sketch)} to the given {@code sketch} or a
	 * 		parent of it. Or {@code null} if no matching sketch. (aka, continue the loop)
	 * @throws NullPointerException {@inheritDoc}
	 * @since 0.2.0 ~2021.01.24
	 */
	@Override
	Optional<Sketch> visitSketch(Sketch sketch);
}
