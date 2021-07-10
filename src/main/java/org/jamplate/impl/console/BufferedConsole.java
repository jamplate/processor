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

import java.io.IOError;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Objects;

/**
 * A console that stores its content to a buffer.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.07.03
 */
public class BufferedConsole implements Console {
	/**
	 * The buffer this console is printing to.
	 *
	 * @since 0.3.0 ~2021.07.03
	 */
	@NotNull
	protected final StringWriter buffer = new StringWriter();

	@Override
	public void close() {
		try {
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
		this.buffer.append(csq);
		return this;
	}

	@NotNull
	@Override
	public Console print(@NotNull CharSequence csq, int start, int end) {
		Objects.requireNonNull(csq, "csq");
		this.buffer.append(csq, start, end);
		return this;
	}

	@NotNull
	@Override
	public String read() {
		return this.buffer.toString();
	}
}
