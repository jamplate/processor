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
package org.jamplate.process.sketcher;

import org.jamplate.process.visitor.Visitor;
import org.jamplate.model.sketch.Sketch;

import java.util.Objects;
import java.util.Optional;

/**
 * A visitor sub-interface specialized for visitors that builds the sketch hierarchy.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.01.24
 */
@FunctionalInterface
public interface Sketcher extends Visitor<Sketch> {
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
	 * @throws RuntimeException          {@inheritDoc}
	 * @throws Error                     {@inheritDoc}
	 * @since 0.2.0 ~2021.01.24
	 */
	@SuppressWarnings("RedundantMethodOverride")
	@Override
	default Optional<Sketch> visitRange(Sketch parent, int position, int length) {
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
	 * @throws RuntimeException     {@inheritDoc}
	 * @throws Error                {@inheritDoc}
	 * @since 0.2.0 ~2021.01.24
	 */
	@Override
	Optional<Sketch> visitSketch(Sketch sketch);
}
