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

import org.jamplate.model.source.Source;

/**
 * A callback that can be passed to a sketch for that sketch to invoke this sketch with
 * every element in it. (recursively)
 *
 * @author LSafer
 * @version 0.0.2
 * @since 0.0.2 ~2021.01.11
 */
public interface SketchVisitor {
	/**
	 * Invoked for any sketch met down in the tree.
	 *
	 * @param sketch the sketch met.
	 * @return true, if this visitor wishes to stop the loop.
	 * @throws NullPointerException if the given {@code sketch} is null.
	 * @since 0.0.2 ~2021.01.11
	 */
	boolean visitSketch(Sketch sketch);

	/**
	 * Invoked for any unused source met down in the tree.
	 *
	 * @param source the source met.
	 * @param sketch the sketch the given {@code source} was found in.
	 * @return true, if this visitor wishes to stop the loop.
	 * @throws NullPointerException if the given {@code sketch} or {@code source} is
	 *                              null.
	 * @since 0.0.2 ~2021.01.11
	 */
	boolean visitSource(Sketch sketch, Source source);
}