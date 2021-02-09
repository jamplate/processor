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
package org.jamplate.runtime.compilation;

import org.jamplate.model.sketch.Sketch;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The default compilation implementation.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.01.31
 */
public class DynamicCompilation extends AbstractCompilation {
	@Override
	public void parse() {
		//capture
		Set<Sketch> sketches = new HashSet<>(this.appendedSketches);

		//check -> copy -> clear -> parse -> repeat
		while (!sketches.isEmpty()) {
			Set<Sketch> next = new HashSet<>();

			sketches.parallelStream()
					.forEach(sketch -> {
						//parse -> flatten -> add-tail -> put-tree -> check -> repeat
						while (
								this.usedParsers.parallelStream()
										.map(parser -> parser.parse(sketch))
										.flatMap(Collection::parallelStream)
										.peek(next::add)
										.collect(Collectors.toSet())
										.stream()
										.peek(sketch::put)
										.count() > 0L
						)
							;
					});

			sketches = next;
		}
	}
}
