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

import java.util.Comparator;

/**
 * A temporary sketch containing initial thoughts about an element.
 * <br>
 * Note: sketches are built from bottom to top. So, a typical sketch will store its sub-sketches but never its parent sketch.
 *
 * @author LSafer
 * @version 0.0.2
 * @since 0.0.2 ~2020.12.25
 */
public interface Sketch extends Node<Sketch> {
	/**
	 * The standard sketch comparator.
	 *
	 * @since 0.0.2 ~2021.01.9
	 */
	Comparator<Sketch> COMPARATOR = Comparator.comparing(Sketch::source, Source.COMPARATOR);

	/**
	 * Returns true, if the source of this sketch has an area that has not been reserved by any sub-sketches of this sketch.
	 *
	 * @return true, if this sketch has an un-sketched area.
	 * @since 0.0.2 ~2021.01.9
	 */
	boolean available();
//
//	/**
//	 * Check if the given {@code sketch} can be sub sketch of this.
//	 * <br>
//	 * Reasons for a sketch to reject another sub sketch:
//	 * <ul>
//	 *     <li>The parent sketch's source does not {@link Source#contains(Source)} the child's source.</li>
//	 *     <li>The parent sketch has a sub-source that {@link Source#clashWith(Source)} with the child's source.</li>
//	 * </ul>
//	 *
//	 * @param sketch the sketch to be checked.
//	 * @return true, if the given {@code sketch} can be {@link #put(Sketch)} to this sketch.
//	 * @throws NullPointerException if the given {@code sketch} is null.
//	 * @since 0.0.2 ~2021.01.9
//	 */
//	boolean check(Sketch sketch);

	/**
	 * Put the given sketch in this sketch.
	 *
	 * @param sketch the sketch to be put.
	 * @throws NullPointerException     if the given {@code sketch} is null.
	 * @since 0.0.2 ~2021.01.7
	 */
	void put(Sketch sketch);

//	void remove(Sketch sketch);

	/**
	 * Get the source of this sketch.
	 *
	 * @return the source of this sketch.
	 * @since 0.0.2 ~2021.01.7
	 */
	Source source();

//	/**
//	 * Transfer the given {@code child} sketch to the given {@code parent} sketch. The given {@code child} sketch MUST be a sketch that this sketch
//	 * contain. The {@code parent} sketch must be able to {@link #put(Sketch)} the given {@code child} sketch in it.
//	 *
//	 * @param parent the sketch to be the new parent for the given {@code child} sketch.
//	 * @param child
//	 * @throws IllegalArgumentException if {@link Source#contains(Source) parent.source().contains(child.source())} returned false.
//	 * @throws NullPointerException     if the given {@code parent} or {@code child} is null.
//	 * @since 0.0.2 ~2021.01.9
//	 */
//	void transfer(Sketch parent, Sketch child);
}
