///*
// *	Copyright 2021 Cufy
// *
// *	Licensed under the Apache License, Version 2.0 (the "License");
// *	you may not use this file except in compliance with the License.
// *	You may obtain a copy of the License at
// *
// *	    http://www.apache.org/licenses/LICENSE-2.0
// *
// *	Unless required by applicable law or agreed to in writing, software
// *	distributed under the License is distributed on an "AS IS" BASIS,
// *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// *	See the License for the specific language governing permissions and
// *	limitations under the License.
// */
//package org.jamplate.model.sketch;
//
//import org.jamplate.model.document.Document;
//import org.jamplate.model.document.FileDocument;
//import org.jamplate.model.document.PseudoDocument;
//import org.jamplate.model.reference.DocumentReference;
//
//import java.io.File;
//import java.io.IOError;
//
///**
// * A context sketch implementation for a whole document.
// *
// * @author LSafer
// * @version 0.2.0
// * @since 0.2.0 ~2021.01.17
// */
//public class DocumentSketch extends AbstractContextSketch {
//	@SuppressWarnings("JavaDoc")
//	private static final long serialVersionUID = -381022842178840192L;
//
//	/**
//	 * Construct a new document sketch with a {@link PseudoDocument} that have the given
//	 * {@code content}.
//	 *
//	 * @param content the content of the pseudo document of the constructed sketch.
//	 * @throws NullPointerException if the given {@code content} is null.
//	 * @since 0.2.0 ~2021.01.17
//	 */
//	public DocumentSketch(CharSequence content) {
//		super(new DocumentReference(content));
//	}
//
//	/**
//	 * Construct a new document sketch with a {@link FileDocument} that have the given
//	 * {@code file}.
//	 *
//	 * @param file the file of the file document of the constructed sketch.
//	 * @throws NullPointerException if the given {@code file} is null.
//	 * @throws IOError              if any I/O exception occur.
//	 * @since 0.2.0 ~2021.01.17
//	 */
//	public DocumentSketch(File file) {
//		super(new DocumentReference(file));
//	}
//
//	/**
//	 * Construct a new sketch with the given {@code document}.
//	 *
//	 * @param document the document of the constructed sketch.
//	 * @throws NullPointerException  if the given {@code document} is null.
//	 * @throws IllegalStateException if the given {@code document} is a deserialized
//	 *                               document.
//	 * @throws IOError               if any I/O exception occur.
//	 * @since 0.2.0 ~2021.01.12
//	 */
//	public DocumentSketch(Document document) {
//		super(new DocumentReference(document));
//	}
//
//	/**
//	 * Construct a new sketch with the given document {@code reference}.
//	 *
//	 * @param reference the source reference of the constructed sketch.
//	 * @throws NullPointerException if the given {@code reference} is null.
//	 * @since 0.2.0 ~2021.01.12
//	 */
//	public DocumentSketch(DocumentReference reference) {
//		super(reference);
//	}
//}
