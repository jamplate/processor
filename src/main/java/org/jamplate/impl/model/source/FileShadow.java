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
package org.jamplate.impl.model.source;

import org.jamplate.model.source.Shadow;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Objects;

/**
 * A implementation of the {@link Shadow} interface specialized for files.
 *
 * @author LSafer
 * @version 0.0.2
 * @since 0.0.2 ~2021.01.07
 */
public class FileShadow implements Shadow {
	/**
	 * The content of this shadow.
	 *
	 * @since 0.0.2 ~2021.01.8
	 */
	protected final String content;
	/**
	 * The file of this shadow.
	 *
	 * @since 0.0.2 ~2021.01.8
	 */
	protected final File file;
	/**
	 * The length of the content of this shadow.
	 *
	 * @since 0.0.2 ~2021.01.8
	 */
	protected final int length;

	/**
	 * Construct a new shadow for the given {@code file}.
	 *
	 * @param file the file to construct a shadow for.
	 * @throws IOException if any I/O exception occurs.
	 * @since 0.0.2 ~2021.01.8
	 */
	public FileShadow(File file) throws IOException {
		Objects.requireNonNull(file, "file");
		try (Reader reader = new FileReader(file)) {
			StringBuilder builder = new StringBuilder();
			char[] buffer = new char[1024];
			while (true) {
				int l = reader.read(buffer);
				if (l < 0)
					break;
				if (l > 0)
					builder.append(buffer, 0, l);
			}
			this.file = file;
			this.content = builder.toString();
			this.length = builder.length();
		}
	}

	@Override
	public String content() {
		return this.content;
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof FileShadow &&
			   ((FileShadow) other).file().equals(this.file);
	}

	@Override
	public File file() {
		return this.file;
	}

	@Override
	public int hashCode() {
		return this.file.hashCode();
	}

	@Override
	public int length() {
		return this.length;
	}

	@Override
	public String toString() {
		return "~" + this.file;
	}
}
