/*
 *	Copyright 2020 Cufy
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

import org.jamplate.Node;
import org.jamplate.model.source.Source;

/**
 * A temporary sketch containing initial thoughts about an element.
 *
 * @author LSafer
 * @version 0.0.2
 * @since 0.0.2 ~2020.12.25
 */
public interface Sketch extends Node<Sketch> {
	/**
	 * Check if the given area is available to be reserved.
	 *
	 * @param pos the position of the area to be reserved.
	 * @param len the length of the area to be reserved.
	 * @return true, if this sketch allows the given area to be reserved.
	 * @since 0.0.2 ~2021.01.7
	 */
	boolean check(long pos, long len);

	/**
	 * The length of this sketch.
	 *
	 * @return the length of this sketch.
	 * @since 0.0.2 ~2021.01.7
	 */
	long length();

	/**
	 * Put the given sketch in this sketch.
	 *
	 * @param sketch the sketch to be put.
	 * @throws NullPointerException     if the given {@code sketch} is null.
	 * @throws IllegalArgumentException if the given {@code sketch} has a source that the source of this sketch does not {@link
	 *                                  Source#contains(Source) contain}.
	 * @since 0.0.2 ~2021.01.7
	 */
	void put(Sketch sketch);

	/**
	 * Reserve the given range in the source of this sketch for the given {@code sketch}.
	 *
	 * @param sketch the sketch to reserve the given area for.
	 * @param pos    the pos of the area to be reserved.
	 * @param len    the len of the area to be reserved.
	 * @throws NullPointerException      if the given {@code sketch} is null.
	 * @throws IllegalArgumentException  if the given {@code pos} or {@code len} is negative.
	 * @throws IndexOutOfBoundsException if {@code pos + len > this.len()}.
	 * @throws IllegalStateException     if the method {@link #check(long, long)} returned false for the given area.
	 * @since 0.0.2 ~2021.01.6
	 */
	void reserve(Sketch sketch, long pos, long len);

	/**
	 * Get the source of this sketch.
	 *
	 * @return the source of this sketch.
	 * @since 0.0.2 ~2021.01.7
	 */
	Source source();
}
