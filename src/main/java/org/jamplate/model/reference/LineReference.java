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
 * A reference that points to a whole line in a document.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.01.27
 */
public class LineReference extends AbstractReference {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -1124656793254168673L;

	/**
	 * Construct a new reference that points to the line with the given {@code line}
	 * number in the given {@code document}.
	 *
	 * @param document the document the constructed reference will be referencing.
	 * @param line     the line the constructed reference will point to.
	 * @throws NullPointerException     if the given {@code document} is null.
	 * @throws IllegalArgumentException if {@code line < 1}.
	 * @throws NoSuchElementException   if the given {@code document} do not have such
	 *                                  line.
	 * @throws IllegalStateException    if the given {@code document} or is a deserialized
	 *                                  document.
	 * @throws IOError                  if any I/O error occur.
	 * @since 0.2.0 ~2021.01.27
	 */
	public LineReference(Document document, int line) {
		super(document, line);
	}
}
