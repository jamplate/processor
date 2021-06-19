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
package org.jamplate.internal.model;

import org.jamplate.model.Document;
import org.jamplate.model.DocumentNotFoundError;
import org.jamplate.model.DocumentNotFoundException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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
	@NotNull
	protected final File file;

	/**
	 * The content of this document. (cached, lazily initialized)
	 *
	 * @since 0.2.0 ~2021.01.16
	 */
	@Nullable
	protected String content;

	/**
	 * Construct a new document for the file with the given {@code filename}.
	 *
	 * @param filename the name of the file for the constructed document.
	 * @throws NullPointerException if the given {@code filename} is null.
	 * @since 0.2.0 ~2021.01.13
	 */
	public FileDocument(@NotNull String filename) {
		Objects.requireNonNull(filename, "filename");
		this.file = new File(filename);
	}

	/**
	 * Construct a new document for the given {@code file}.
	 *
	 * @param file the file for the constructed document.
	 * @throws NullPointerException if the given {@code file} is null.
	 * @since 0.2.0 ~2021.01.13
	 */
	public FileDocument(@NotNull File file) {
		Objects.requireNonNull(file, "file");
		this.file = file;
	}

	/**
	 * Return an array of all the document in the hierarchy of the given {@code file}
	 * (including itself).
	 *
	 * @param file the file to collect the documents in its hierarchy (including itself).
	 * @return an array of all the documents in the hierarchy of the given {@code file}.
	 * @throws NullPointerException if the given {@code file} is null.
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static Document[] hierarchy(@NotNull File file) {
		Objects.requireNonNull(file, "file");
		//noinspection ZeroLengthArrayAllocation
		return FileDocument.hierarchySet(file).toArray(new Document[0]);
	}

	/**
	 * Return a set of all the document in the hierarchy of the given {@code file}
	 * (including itself).
	 *
	 * @param file the file to collect the documents in its hierarchy (including itself).
	 * @return a set of all the documents in the hierarchy of the given {@code file}.
	 * @throws NullPointerException if the given {@code file} is null.
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static Set<Document> hierarchySet(@NotNull File file) {
		Objects.requireNonNull(file, "file");
		Set<Document> documentSet = new HashSet<>();
		FileDocument.hierarchySet(documentSet, file);
		return documentSet;
	}

	/**
	 * Collect all the documents in the hierarchy of the given {@code file} and add them
	 * to the given {@code documentSet}. Or if the given {@code file} is not a directory
	 * then add a document for it to the given {@code documentSet}.
	 *
	 * @param documentSet the results set.
	 * @param file        the file to collect the documents in its hierarchy (including
	 *                    itself).
	 * @throws NullPointerException if the given {@code documentSet} or {@code file} is
	 *                              null.
	 * @since 0.2.0 ~2021.05.31
	 */
	@Contract(mutates = "param1")
	private static void hierarchySet(@NotNull Set<Document> documentSet, @NotNull File file) {
		Objects.requireNonNull(documentSet, "documentSet");
		Objects.requireNonNull(file, "file");
		if (file.isDirectory()) {
			File[] children = file.listFiles();

			if (children != null)
				for (File child : children)
					FileDocument.hierarchySet(documentSet, child);

			return;
		}

		documentSet.add(new FileDocument(file));
	}

	@Override
	public boolean equals(@Nullable Object object) {
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
	public InputStream openInputStream() throws FileNotFoundException, DocumentNotFoundException {
		if (!this.file.exists())
			throw new DocumentNotFoundException("Corresponding file does not exist", this);
		return this.content == null ?
			   new FileInputStream(this.file) :
			   new ByteArrayInputStream(this.content.getBytes());
	}

	@NotNull
	@Override
	public Reader openReader() throws FileNotFoundException, DocumentNotFoundException {
		if (!this.file.exists())
			throw new DocumentNotFoundException("Corresponding file does not exist", this);
		return this.content == null ?
			   new FileReader(this.file) :
			   new StringReader(this.content);
	}

	@NotNull
	@Override
	public CharSequence read() {
		if (!this.file.exists())
			throw new DocumentNotFoundError(
					new DocumentNotFoundException("Corresponding file does not exist", this)
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
		return this.file.toString().replace('\\', '/');
	}
}
