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
package org.jamplate.model.reference;

import org.jamplate.model.document.Document;

import java.io.IOError;
import java.util.NoSuchElementException;

/**
 * An implementation of the interface {@link Reference} that is a slice of another
 * reference.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.01.13
 */
public class SubReference extends AbstractReference {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 8737902111302332073L;

	/**
	 * Construct a new reference that points to a fragment in the given {@code document}
	 * from the given {@code position} with the given {@code length}.
	 *
	 * @param document the document the constructed reference will be referencing.
	 * @param position the position where the constructed reference will point at in the
	 *                 given {@code document}.
	 * @param length   the length of the fragment the constructed reference will point
	 *                 at.
	 * @throws NullPointerException     if the given {@code document} is null.
	 * @throws IllegalArgumentException if the given {@code position} or {@code length} is
	 *                                  negative.
	 * @throws NoSuchElementException   if the given {@code document} does not have such
	 *                                  fragment.
	 * @throws IllegalStateException    if the given {@code document} is a deserialized
	 *                                  document.
	 * @throws IOError                  if any I/O error occur.
	 * @since 0.2.0 ~2021.01.31
	 */
	public SubReference(Document document, int position, int length) {
		super(
				document,
				position,
				length
		);
	}
}
