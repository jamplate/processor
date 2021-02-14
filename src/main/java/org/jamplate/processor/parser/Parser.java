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
package org.jamplate.processor.parser;

import org.jamplate.model.Dominance;
import org.jamplate.model.sketch.Sketch;

import java.io.IOError;
import java.util.Set;

/**
 * A parser is a non-modifying argument-state-dependent function that takes a sketch and
 * sketch a sketch hierarchy from it.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.01.26
 */
@FunctionalInterface
public interface Parser {
	/**
	 * Return a sketch hierarchy (or part of it) in the given {@code sketch} from the
	 * allowed areas in it.
	 * <br>
	 * Rules:
	 * <ul>
	 *     <li>
	 *         The given {@code sketch} should not be modified by this method.
	 *     </li>
	 *     <li>
	 *         The returned set all has a dominance of {@link Dominance#EXACT} or
	 *         {@link Dominance#PART} with the given {@code sketch}. (be aware of infinite
	 *         recursion -_-')
	 *     </li>
	 *     <li>
	 *         The returned set all has a dominance of {@link Dominance#NONE} with any
	 *         sketch currently in the given {@code sketch}'s tree.
	 *     </li>
	 *     <li>
	 *         All the parsing operations is strictly-limited to the given {@code sketch}.
	 *     </li>
	 * </ul>
	 * <br>
	 * Note:
	 * <ul>
	 *     <li>
	 *         The returned set might have sketches that clashes with each-other.
	 *     </li>
	 *     <li>
	 *         The returned set might have a sketch that has inner sketches.
	 *     </li>
	 * </ul>
	 *
	 * @param sketch the sketch to parse the elements in it.
	 * @return an unmodifiable non-null set of the sketches parsed from the given {@code
	 * 		sketch}. Or an empty set if no match was found.
	 * @throws NullPointerException  if the given {@code sketch} is null.
	 * @throws IllegalStateException if the given {@code sketch} is deserialized or has a
	 *                               deserialized reference or a deserialized document.
	 * @throws IOError               if any I/O error occur.
	 * @since 0.2.0 ~2021.01.29
	 */
	Set<Sketch> parse(Sketch sketch);
}
