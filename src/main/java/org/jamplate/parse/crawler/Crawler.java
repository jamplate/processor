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
package org.jamplate.parse.crawler;

import org.jamplate.model.document.Document;
import org.jamplate.model.reference.DocumentReference;
import org.jamplate.model.reference.Reference;
import org.jamplate.model.sketch.ReferenceSketch;
import org.jamplate.model.sketch.Sketch;

import java.io.IOError;
import java.util.List;
import java.util.Objects;

/**
 * An interface that crawls sources searching for references that satisfies them.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.01.30
 */
@FunctionalInterface
public interface Crawler {
	/**
	 * Crawl for references of a match satisfying this crawler in the given {@code
	 * document}. The returned reference collection shall be sorted from the lowest
	 * position to the highest position and from the longest to the shortest.
	 * <br>
	 * The first reference in the returned array is always a reference containing the
	 * whole match and the other references in the array has a dominance of {@link
	 * org.jamplate.model.Dominance#PART} or {@link org.jamplate.model.Dominance#EXACT}
	 * with the first reference.
	 *
	 * @param document the document to be crawled.
	 * @return an unmodifiable sorted non-empty collection of found references. or null if
	 * 		no match.
	 * @throws NullPointerException  if the given {@code document} is null.
	 * @throws IllegalStateException if the given {@code document} is a deserialized
	 *                               document.
	 * @throws IOError               if any I/O error occur.
	 * @since 0.2.0 ~2021.01.30
	 */
	default List<Reference> crawl(Document document) {
		Objects.requireNonNull(document, "document");
		return this.crawl(new DocumentReference(document));
	}

	/**
	 * Crawl for references of a match satisfying this crawler in the given {@code
	 * reference}. The returned reference collection shall be sorted from the lowest
	 * position to the highest position and from the longest to the shortest.
	 * <br>
	 * The first reference in the returned array is always a reference containing the
	 * whole match and the other references in the array has a dominance of {@link
	 * org.jamplate.model.Dominance#PART} or {@link org.jamplate.model.Dominance#EXACT}
	 * with the first reference.
	 *
	 * @param reference the reference to be crawled.
	 * @return an unmodifiable sorted non-empty collection of found references. or null if
	 * 		no match.
	 * @throws NullPointerException  if the given {@code reference} is null.
	 * @throws IllegalStateException if the given {@code reference} is a deserialized
	 *                               reference or has a deserialized document.
	 * @throws IOError               if any I/O error occur.
	 * @since 0.2.0 ~2021.01.30
	 */
	default List<Reference> crawl(Reference reference) {
		Objects.requireNonNull(reference, "reference");
		return this.crawl(new ReferenceSketch(reference));
	}

	/**
	 * Crawl for references of a match satisfying this crawler in the given {@code
	 * sketch}. The returned reference collection shall be sorted from the lowest position
	 * to the highest position and from the longest to the shortest.
	 * <br>
	 * The first reference in the returned array is always a reference containing the
	 * whole match and the other references in the array has a dominance of {@link
	 * org.jamplate.model.Dominance#PART} or {@link org.jamplate.model.Dominance#EXACT}
	 * with the first reference.
	 *
	 * @param sketch the sketch to be crawled.
	 * @return an unmodifiable sorted non-empty collection of found references. or null if
	 * 		no match.
	 * @throws NullPointerException  if the given {@code sketch} is null.
	 * @throws IllegalStateException if the given {@code sketch} has a deserialized
	 *                               reference or a deserialized document.
	 * @throws IOError               if any I/O error occur.
	 * @since 0.2.0 ~2021.01.30
	 */
	List<Reference> crawl(Sketch sketch);
}
