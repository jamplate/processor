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

import org.jamplate.model.document.Document;
import org.jamplate.model.document.FileDocument;
import org.jamplate.model.document.PseudoDocument;
import org.jamplate.model.source.DocumentSource;

import java.io.File;
import java.io.IOError;

/**
 * An unqualified (not specialized with a specific class) sketch for a whole document.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.01.17
 */
public class DocumentSketch extends AbstractContextSketch {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -381022842178840192L;

	/**
	 * Construct a new document sketch with a {@link PseudoDocument} that have the given
	 * {@code content}.
	 *
	 * @param content the content of the pseudo document of the constructed sketch.
	 * @throws NullPointerException if the given {@code content} is null.
	 * @since 0.2.0 ~2021.01.17
	 */
	public DocumentSketch(CharSequence content) {
		super(new DocumentSource(content));
	}

	/**
	 * Construct a new document sketch with a {@link FileDocument} that have the given
	 * {@code file}.
	 *
	 * @param file the file of the file document of the constructed sketch.
	 * @throws NullPointerException if the given {@code file} is null.
	 * @throws IOError              if any I/O exception occur.
	 * @since 0.2.0 ~2021.01.17
	 */
	public DocumentSketch(File file) {
		super(new DocumentSource(file));
	}

	/**
	 * Construct a new sketch for the given {@code document}.
	 *
	 * @param document the document the constructed sketch will be for.
	 * @throws NullPointerException if the given {@code document} is null.
	 * @throws IOError              if any I/O exception occur.
	 * @since 0.2.0 ~2021.01.12
	 */
	public DocumentSketch(Document document) {
		super(new DocumentSource(document));
	}

	/**
	 * Construct a new sketch for the given document {@code source}.
	 *
	 * @param source the document source the constructed sketch will be for.
	 * @throws NullPointerException if the given {@code source} is null.
	 * @since 0.2.0 ~2021.01.12
	 */
	public DocumentSketch(DocumentSource source) {
		super(source);
	}
}
