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
import org.jamplate.model.document.FileDocument;
import org.jamplate.model.document.PseudoDocument;

import java.io.File;
import java.io.IOError;

/**
 * An implementation of the interface {@link Reference} that takes a whole {@link
 * Document}.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.01.17
 */
public class DocumentReference extends AbstractReference {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -3080162447729968421L;

	/**
	 * Construct a new document reference with a {@link PseudoDocument} that have the
	 * given {@code content}.
	 *
	 * @param content the content of the pseudo document of the constructed reference.
	 * @throws NullPointerException if the given {@code content} is null.
	 * @since 0.2.0 ~2021.01.17
	 */
	public DocumentReference(CharSequence content) {
		super(new PseudoDocument(content));
	}

	/**
	 * Create a new document reference with a {@link FileDocument} that have the given
	 * {@code file}.
	 *
	 * @param file the file of the file document of the constructed reference.
	 * @throws NullPointerException if the given {@code file} is null.
	 * @throws IOError              if any I/O exception occurs.
	 * @since 0.2.0 ~2021.01.17
	 */
	public DocumentReference(File file) {
		super(new FileDocument(file));
	}

	/**
	 * Construct a new reference that takes the whole given {@code document} as its actual
	 * source.
	 *
	 * @param document the document the constructed reference will be referencing.
	 * @throws NullPointerException  if the given {@code document} is null.
	 * @throws IllegalStateException if the given {@code document} is a deserialized
	 *                               document.
	 * @throws IOError               if any I/O exception occur.
	 * @since 0.2.0 ~2021.01.13
	 */
	public DocumentReference(Document document) {
		super(document);
	}
}
