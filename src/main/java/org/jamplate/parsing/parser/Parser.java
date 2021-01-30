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
package org.jamplate.parsing.parser;

import org.jamplate.source.document.Document;
import org.jamplate.source.reference.Reference;
import org.jamplate.source.sketch.DocumentSketch;
import org.jamplate.source.sketch.ReferenceSketch;
import org.jamplate.source.sketch.Sketch;

import java.util.Objects;

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
	 * Parse the content of the given {@code document} and return the resultant root
	 * sketch from parsing it.
	 *
	 * @param document the document to be parsed.
	 * @return the root sketch from parsing the content of the given {@code document}.
	 * @throws NullPointerException if the given {@code document} is null.
	 * @throws RuntimeException     if the parser got into an unrecoverable state because
	 *                              of something unexpected in the given {@code
	 *                              document}.
	 * @throws Error                if any unexpected error occurred.
	 * @since 0.2.0 ~2021.01.26
	 */
	default Sketch parse(Document document) {
		Objects.requireNonNull(document, "document");
		Sketch sketch = new DocumentSketch(document);
		this.parse(sketch);
		return sketch;
	}

	/**
	 * Parse the content of the given {@code reference} and return the resultant root
	 * sketch from parsing it.
	 *
	 * @param reference the source reference to be parsed.
	 * @return the root sketch from parsing the content of the given {@code reference}.
	 * @throws NullPointerException if the given {@code reference} is null.
	 * @throws RuntimeException     if the parser got into an unrecoverable state because
	 *                              of something unexpected in the given {@code
	 *                              reference}.
	 * @throws Error                if any unexpected error occurred.
	 * @since 0.2.0 ~2021.01.30
	 */
	default Sketch parse(Reference reference) {
		Objects.requireNonNull(reference, "reference");
		Sketch sketch = new ReferenceSketch(reference);
		this.parse(sketch);
		return sketch;
	}

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
