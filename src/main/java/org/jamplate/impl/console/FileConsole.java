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
package org.jamplate.impl.console;

import org.jamplate.memory.Console;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.Objects;

/**
 * A console that prints to a pre-specified file.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.07.03
 */
public class FileConsole implements Console {
	/**
	 * The buffer this console is printing to.
	 *
	 * @since 0.3.0 ~2021.07.03
	 */
	@NotNull
	protected final StringWriter buffer = new StringWriter();
	/**
	 * The actual writer of this console.
	 *
	 * @since 0.3.0 ~2021.07.03
	 */
	@NotNull
	protected final FileWriter writer;

	/**
	 * Construct a new console that prints to the given {@code file}.
	 *
	 * @param file the file the constructed console will print to.
	 * @throws NullPointerException if the given {@code file} is null.
	 * @since 0.3.0 ~2021.07.03
	 */
	public FileConsole(@NotNull File file) {
		Objects.requireNonNull(file, "file");
		try {
			this.writer = new FileWriter(file);
		} catch (IOException e) {
			throw new IOError(e);
		}
	}

	@Override
	public void close() {
		try {
			this.writer.close();
			this.buffer.close();
		} catch (IOException e) {
			throw new IOError(e);
		}
	}

	@NotNull
	@Override
	public Console print(@Nullable Object object) {
		return this.print(String.valueOf(object));
	}

	@NotNull
	@Override
	public Console print(@NotNull CharSequence csq) {
		Objects.requireNonNull(csq, "csq");
		try {
			this.writer.write(csq.toString());
			this.buffer.append(csq);
			return this;
		} catch (IOException e) {
			throw new IOError(e);
		}
	}

	@NotNull
	@Override
	public Console print(@NotNull CharSequence csq, int start, int end) {
		Objects.requireNonNull(csq, "csq");
		try {
			this.writer.write(csq.subSequence(start, end).toString());
			this.buffer.append(csq);
			return this;
		} catch (IOException e) {
			throw new IOError(e);
		}
	}

	@NotNull
	@Override
	public String read() {
		return this.buffer.toString();
	}
}
