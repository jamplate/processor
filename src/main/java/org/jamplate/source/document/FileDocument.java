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
package org.jamplate.source.document;

import java.io.*;
import java.util.Objects;

/**
 * A document that delegates to a {@link File}.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.01.13
 */
public class FileDocument extends AbstractDocument {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -210192396401745547L;

	/**
	 * The file of this file document.
	 *
	 * @since 0.2.0 ~2021.01.13
	 */
	@SuppressWarnings("TransientFieldNotInitialized")
	protected final transient File file;

	/**
	 * The content of this document. (cached, lazily initialized)
	 *
	 * @since 0.2.0 ~2021.01.16
	 */
	protected transient String content;

	/**
	 * Construct a new document for the given {@code file}.
	 *
	 * @param file the file for the constructed document.
	 * @throws NullPointerException if the given {@code file} is null.
	 * @since 0.2.0 ~2021.01.13
	 */
	public FileDocument(File file) {
		super(
				FileDocument.qualifiedName(file),
				FileDocument.name(file),
				FileDocument.simpleName(file)
		);
		this.file = file;
	}

	/**
	 * Returns the name of the given {@code file}.
	 *
	 * @param file the file to get its name.
	 * @return the name of the given {@code file}.
	 * @throws NullPointerException if the given {@code file} is null.
	 * @since 0.2.0 ~2021.01.17
	 */
	public static String name(File file) {
		Objects.requireNonNull(file, "file");
		return file.getName();
	}

	/**
	 * Returns the qualified name of the given {@code file}.
	 *
	 * @param file the file to get its qualified name.
	 * @return the qualified name of the given {@code file}.
	 * @throws NullPointerException if the given {@code file} is null.
	 * @since 0.2.0 ~2021.01.17
	 */
	public static String qualifiedName(File file) {
		Objects.requireNonNull(file, "file");
		return file.toString();
	}

	/**
	 * Returns the simple name of the given {@code file}.
	 *
	 * @param file the file to get its simple name.
	 * @return the simple name of the given {@code file}.
	 * @throws NullPointerException if the given {@code file} is null.
	 * @since 0.2.0 ~2021.01.17
	 */
	public static String simpleName(File file) {
		Objects.requireNonNull(file, "file");
		//noinspection DynamicRegexReplaceableByCompiledPattern
		return file.getName().replaceAll("[.][^.]*$", "");
	}

	@Override
	public InputStream openInputStream() throws FileNotFoundException {
		if (!this.constructed)
			throw new IllegalStateException("Deserialized Document");
		return new FileInputStream(this.file);
	}

	@Override
	public Reader openReader() throws FileNotFoundException {
		if (!this.constructed)
			throw new IllegalStateException("Deserialized Document");
		return new FileReader(this.file);
	}

	@Override
	public CharSequence readContent() {
		if (!this.constructed)
			throw new IllegalStateException("Deserialized Document");
		if (this.content == null)
			try (Reader reader = new FileReader(this.file)) {
				StringBuilder builder = new StringBuilder();
				char[] buffer = new char[1024];

				while (true) {
					int l = reader.read(buffer);

					if (l < 0)
						break;
					if (l > 0)
						builder.append(buffer, 0, l);
				}

				this.content = builder.toString();
			} catch (IOException e) {
				throw new IOError(e);
			}

		return this.content;
	}
}
