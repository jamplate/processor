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
package org.jamplate.memory;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Closeable;
import java.io.IOError;

/**
 * A console is a print machine abstracting the processing of printing content to the user
 * desired place.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.07.03
 */
public interface Console extends Closeable {
	@Contract(mutates = "this")
	@Override
	void close();

	/**
	 * Print the given {@code object} to this console.
	 *
	 * @param object the object to be printed.
	 * @return this.
	 * @throws IOError if an I/O error occurs.
	 * @since 0.3.0 ~2021.07.03
	 */
	@NotNull
	@Contract(mutates = "this")
	Console print(@Nullable Object object);

	/**
	 * Print the given {@code csq} to this console.
	 *
	 * @param csq the sequence to be printed.
	 * @return this.
	 * @throws NullPointerException if the given {@code csq} is null.
	 * @throws IOError              if an I/O error occurs.
	 * @since 0.3.0 ~2021.07.03
	 */
	@NotNull
	@Contract(mutates = "this")
	Console print(@NotNull CharSequence csq);

	/**
	 * Print the given {@code csq} from {@code start} to {@code end} to this console.
	 *
	 * @param csq   the sequence to be printed.
	 * @param start the index of the first character in the sequence to be printed.
	 * @param end   one past the index of the last character in the sequence to be
	 *              printed.
	 * @return this.
	 * @throws NullPointerException      if the given {@code csq} is null.
	 * @throws IndexOutOfBoundsException if {@code start < 0} or {@code end < 0} or {@code
	 *                                   start > end} or {@code end > csq.length()}.
	 * @throws IOError                   if an I/O error occurs.
	 * @since 0.3.0 ~2021.07.03
	 */
	@NotNull
	@Contract(value = "_,_,_->this", mutates = "this")
	Console print(@NotNull CharSequence csq, int start, int end);

	/**
	 * Read this console.
	 *
	 * @return the content printed to this console.
	 * @throws IOError if an I/O error occurs.
	 * @since 0.3.0 ~2021.07.03
	 */
	@NotNull
	@Contract(pure = true)
	String read();
}
