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
package org.jamplate.internal.util;

import org.jamplate.model.Document;
import org.jamplate.model.DocumentNotFoundError;
import org.jamplate.model.Reference;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOError;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Objects;

/**
 * Input/Output related functions.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.24
 */
public final class IO {
	/**
	 * Tool classes does not need instantiating.
	 *
	 * @throws AssertionError when called.
	 * @since 0.3.0 ~2021.06.24
	 */
	private IO() {
		throw new AssertionError("No instance for you!");
	}

	/**
	 * Get the line of the given {@code tree} on its document.
	 *
	 * @param tree the tree to get its line.
	 * @return the line of the given {@code tree}.
	 * @throws NullPointerException  if the given {@code tree} is null.
	 * @throws IOError               if any I/O error occurred while reading.
	 * @throws DocumentNotFoundError if the document of the given {@code tree} is
	 *                               unavailable.
	 * @since 0.2.0 ~2021.05.23
	 */
	@Contract(pure = true)
	public static int line(@NotNull Tree tree) {
		Objects.requireNonNull(tree, "tree");
		int p = tree.getReference().position();
		try (LineNumberReader reader = new LineNumberReader(tree.getDocument().openReader())) {
			while ((p -= reader.skip(p)) != 0) {
				if (reader.read() == -1)
					break;

				p--;
			}

			return reader.getLineNumber() + 1;
		} catch (IOException e) {
			throw new IOError(e);
		}
	}

	/**
	 * Return the position of the given {@code tree} on its line.
	 *
	 * @param tree the tree to get its position in its line.
	 * @return the position of the given {@code tree} on its line.
	 * @throws NullPointerException  if the given {@code tree} is null.
	 * @throws IOError               if any I/O error occurred while reading.
	 * @throws DocumentNotFoundError if the document of the given {@code tree} is
	 *                               unavailable.
	 * @since 0.2.0 ~2021.05.24
	 */
	@SuppressWarnings("OverlyLongMethod")
	@Contract(pure = true)
	public static int positionInLine(@NotNull Tree tree) {
		Objects.requireNonNull(tree, "tree");
		int p = tree.getReference().position();
		try (LineNumberReader reader = new LineNumberReader(tree.getDocument().openReader())) {
			char[] b = new char[2];
			while (true) {
				reader.mark(Math.max(10, p << 1));
				String line = reader.readLine();

				if (line == null)
					return 0;

				reader.reset();
				long skipped = reader.skip(line.length());

				while (skipped++ < line.length())
					if (reader.read() == -1)
						return 0;

				int ctrl = 0;
				reader.mark(2);
				reader.read(b);
				if (b[0] == '\n')
					ctrl++;
				if (b[0] == '\r') {
					ctrl++;

					if (b[1] == '\n')
						ctrl++;
				}
				reader.reset();
				long skipped2 = reader.skip(ctrl);
				while (skipped2++ < ctrl)
					if (reader.read() == -1)
						return p;

				if ((p -= line.length() + ctrl) < 0)
					return line.length() + p + ctrl;
			}
		} catch (IOException e) {
			return Math.max(0, p);
		}
	}

	/**
	 * Read the source-code of the given {@code tree}.
	 *
	 * @param tree the tree to read its source-code.
	 * @return the source code of the given {@code tree}.
	 * @throws NullPointerException  if the given {@code tree} is null.
	 * @throws IOError               if any I/O error occurred while reading.
	 * @throws DocumentNotFoundError if the document of the given {@code tree} is
	 *                               unavailable.
	 * @since 0.2.0 ~2021.05.21
	 */
	@NotNull
	@Contract(pure = true)
	public static CharSequence read(@NotNull Tree tree) {
		Objects.requireNonNull(tree, "tree");
		Document document = tree.getDocument();
		Reference reference = tree.getReference();
		return document.read(reference);
	}

	/**
	 * Read the whole line that the given {@code tree} is at on its document.
	 *
	 * @param tree the tree to read the line it is in.
	 * @return the text of the line the given {@code tree} is at.
	 * @throws NullPointerException  if the given {@code tree} is null.
	 * @throws IOError               if any I/O error occurred while reading.
	 * @throws DocumentNotFoundError if the document of the given {@code tree} is
	 *                               unavailable.
	 * @since 0.2.0 ~2021.05.24
	 */
	@SuppressWarnings("OverlyLongMethod")
	@Nullable
	@Contract(pure = true)
	public static CharSequence readLine(@NotNull Tree tree) {
		Objects.requireNonNull(tree, "tree");
		Objects.requireNonNull(tree, "tree");
		int p = tree.getReference().position();
		String line = "";
		try (BufferedReader reader = new BufferedReader(tree.getDocument().openReader())) {
			char[] b = new char[2];
			while (true) {
				reader.mark(Math.max(10, p << 1));
				line = reader.readLine();

				if (line == null)
					return null;

				reader.reset();
				long skipped = reader.skip(line.length());

				while (skipped++ < line.length())
					if (reader.read() == -1)
						return "";

				int ctrl = 0;
				reader.mark(2);
				reader.read(b);
				if (b[0] == '\n')
					ctrl++;
				if (b[0] == '\r') {
					ctrl++;

					if (b[1] == '\n')
						ctrl++;
				}
				reader.reset();
				long skipped2 = reader.skip(ctrl);
				while (skipped2++ < ctrl)
					if (reader.read() == -1)
						return line;

				if ((p -= line.length() + ctrl) < 0)
					return line;
			}
		} catch (IOException e) {
			return line;
		}
	}
}
