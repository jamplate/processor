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

import org.jamplate.model.sketch.Sketch;

/**
 * A parser is an event listener that takes unparsed documents and sketch a sketch
 * hierarchy from them.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.01.26
 */
@FunctionalInterface
public interface Parser {
	/**
	 * Parse any parsable elements in the given {@code sketch}.
	 *
	 * @param sketch the sketch to parse the elements in it.
	 * @throws NullPointerException if the given {@code sketch} is null.
	 * @throws RuntimeException     if the parser got into an unrecoverable state because
	 *                              of something unexpected in the given {@code sketch}.
	 * @throws Error                if any unexpected error occurred.
	 * @since 0.2.0 ~2021.01.29
	 */
	void parse(Sketch sketch);
}
