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
package org.jamplate.model.source;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * An implementation for the interface {@link Source} that represents an actual {@link File} as its source.
 *
 * @author LSafer
 * @version 0.0.2
 * @since 0.0.2 ~2021.01.08
 */
public class FileSource extends AbstractSource<File> {
	/**
	 * Construct a new file source that takes the given {@code document} as its actual source.
	 *
	 * @param document the file of the constructed source.
	 * @throws NullPointerException if the given {@code document} is null.
	 * @throws IOException          if any I/O exception occurs.
	 * @since 0.0.2 ~2021.01.8
	 */
	public FileSource(File document) throws IOException {
		super(
				document,
				new String(Files.readAllBytes(document.toPath())),
				0
		);
	}

	/**
	 * Construct a new sub-source from the given {@code parent} source. The constructed source will have the same {@link #root()} and {@link
	 * #document()} as the given {@code parent} source. But, it will have its {@link #content()} equals to the {@link String#substring(int, int)} of
	 * the {@link #content()} of the given {@code parent} source. Also, the constructed source will have its {@link #position()} equals to the sum of
	 * the given {@code pos} and the {@link #position()} of the given {@code parent} source. Finally, its obvious that the constructed source will
	 * have the given {@code parent} source as its {@link #parent()}.
	 * <br>
	 * Note: this constructor was built on trust. It trusts the implementation of the given {@code parent} source.
	 * <br>
	 * Also Note: it is possible that the given {@code parent} returns an object that does not fit into the {@code D} type parameter when invoking its
	 * {@link Source#document()} method.
	 *
	 * @param parent the parent source.
	 * @param pos    the sub-position to get from the given {@code parent} source.
	 * @param len    the length to get from the given {@code parent} source.
	 * @throws NullPointerException      if the given {@code parent} is null.
	 * @throws IllegalArgumentException  if the given {@code pos} or {@code len} is negative.
	 * @throws IndexOutOfBoundsException if {@code parent.content().substring(pos, len)} throws it.
	 * @since 0.0.2 ~2021.01.8
	 */
	protected FileSource(FileSource parent, int pos, int len) {
		super(
				parent,
				pos,
				len
		);
	}

	@Override
	public FileSource slice(int pos) {
		return new FileSource(
				this,
				pos,
				this.content.length() - pos
		);
	}

	@Override
	public FileSource slice(int pos, int len) {
		return new FileSource(
				this,
				pos,
				len
		);
	}
}
