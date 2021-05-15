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
package org.jamplate.model;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Objects;

/**
 * A document that delegates to a {@link File}.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.01.13
 */
public class FileDocument implements Document {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -210192396401745547L;

	/**
	 * The file of this file document.
	 *
	 * @since 0.2.0 ~2021.01.13
	 */
	protected final File file;

	/**
	 * The content of this document. (cached, lazily initialized)
	 *
	 * @since 0.2.0 ~2021.01.16
	 */
	protected String content;

	/**
	 * Construct a new document for the given {@code file}.
	 *
	 * @param file the file for the constructed document.
	 * @throws NullPointerException if the given {@code file} is null.
	 * @since 0.2.0 ~2021.01.13
	 */
	public FileDocument(File file) {
		this.file = file;
	}

	@Override
	public boolean equals(Object object) {
		if (object == this)
			return true;
		if (object instanceof Document) {
			Document document = (Document) object;

			return Objects.equals(document.toString(), this.file.toString());
		}

		return false;
	}

	@Override
	public int hashCode() {
		return this.file.hashCode();
	}

	@NotNull
	@Override
	public InputStream openInputStream() throws FileNotFoundException, UnreadableDocumentException {
		if (!this.file.exists())
			throw new UnreadableDocumentException("Document not available " + this.file);
		return this.content == null ?
			   new FileInputStream(this.file) :
			   new ByteArrayInputStream(this.content.getBytes());
	}

	@NotNull
	@Override
	public Reader openReader() throws FileNotFoundException, UnreadableDocumentException {
		if (!this.file.exists())
			throw new UnreadableDocumentException("Document not available " + this.file);
		return this.content == null ?
			   new FileReader(this.file) :
			   new StringReader(this.content);
	}

	@NotNull
	@Override
	public CharSequence read() {
		if (!this.file.exists())
			throw new UnreadableDocumentError(
					new UnreadableDocumentException("Document not available " + this.file)
			);
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

	@NotNull
	@Override
	public String toString() {
		return this.file.toString();
	}
}
