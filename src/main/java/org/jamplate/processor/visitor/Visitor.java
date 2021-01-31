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
package org.jamplate.processor.visitor;

import org.jamplate.model.sketch.Sketch;

/**
 * A multipurpose callback that can be passed to a sketch for that sketch to invoke it
 * with every element in it. (recursively)
 * <br>
 * Note: any new method added will always have the modifier {@code default} making this
 * remain a {@link FunctionalInterface} with its functional method being {@link
 * #visit(Sketch)}.
 *
 * @param <R> the type of the element to be carried out as a result of visiting.
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.01.11
 */
@FunctionalInterface
public interface Visitor<R> {
	/**
	 * Invoked for every sketch met down in the tree.
	 *
	 * @param sketch the sketch met.
	 * @return an optional containing the results of visiting the given {@code sketch}. Or
	 *        {@code null} if this visitor wishes to continue the loop.
	 * @throws NullPointerException if the given {@code sketch} is null.
	 * @throws RuntimeException     optional.
	 * @throws Error                optional.
	 * @since 0.2.0 ~2021.01.11
	 */
	R visit(Sketch sketch);
}
