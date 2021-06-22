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

import org.jamplate.model.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOError;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.*;
import java.util.stream.StreamSupport;

/**
 * Common operations functions for {@link Tree trees}.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.18
 */
public final class Trees {
	/**
	 * Utility classes must not be initialized.
	 *
	 * @throws AssertionError when called.
	 * @since 0.2.0 ~2021.05.16
	 */
	private Trees() {
		throw new AssertionError("No instance for you");
	}

	/**
	 * Collect all the children of the given {@code tree} and add them to the given {@code
	 * result}.
	 *
	 * @param tree the tree to get its children.
	 * @return a set containing all the children of the given {@code tree}.
	 * @throws NullPointerException if the given {@code tree} is null.
	 * @since 0.2.0 ~2021.05.23
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static Set<Tree> children(@NotNull Tree tree) {
		Objects.requireNonNull(tree, "tree");
		Set<Tree> result = new HashSet<>();
		Trees.children(result, tree);
		return result;
	}

	/**
	 * Collect all the relatives to the given {@code tree} (including the tree itself).
	 *
	 * @param tree the tree to collect its relatives.
	 * @return a set containing all the relatives of the given {@code tree}.
	 * @throws NullPointerException if the given {@code tree} is null.
	 * @since 0.2.0 ~2021.05.17
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static Set<Tree> collect(@NotNull Tree tree) {
		Objects.requireNonNull(tree, "tree");
		Set<Tree> result = new HashSet<>();
		Trees.collect(result, tree);
		return result;
	}

	/**
	 * Return an ordered list containing the children of the given {@code tree} alongside
	 * with newly created trees that fills the gaps between the children of the given
	 * {@code tree}.
	 *
	 * @param tree the tree.
	 * @return an ordered list containing the actual and possible children of the given
	 *        {@code tree}.
	 * @throws NullPointerException if the given {@code tree} is null.
	 * @since 0.2.0 ~2021.05.24
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static List<Tree> flatChildren(@NotNull Tree tree) {
		Objects.requireNonNull(tree, "tree");
		List<Tree> children = new ArrayList<>();

		Iterator<Tree> iterator = tree.iterator();

		if (iterator.hasNext()) {
			Tree previous = iterator.next();

			if (!Intersection.START.test(tree, previous)) {
				int p = tree.reference().position();
				int l = previous.reference().position() - p;

				if (l > 0) {
					Tree gap = new Tree(tree.document(), new Reference(p, l));

					children.add(gap);
				}
			}

			children.add(previous);

			while (iterator.hasNext()) {
				Tree next = iterator.next();

				if (!Intersection.NEXT.test(previous, next)) {
					int p = previous.reference().position() +
							previous.reference().length();
					int l = next.reference().position() - p;

					if (l > 0) {
						Tree gap = new Tree(tree.document(), new Reference(p, l));

						children.add(gap);
					}
				}

				children.add(next);
				previous = next;
			}

			if (!Intersection.END.test(tree, previous)) {
				int p = previous.reference().position() +
						previous.reference().length();
				int l = tree.reference().position() +
						tree.reference().length() - p;

				if (l > 0) {
					Tree gap = new Tree(tree.document(), new Reference(p, l));

					children.add(gap);
				}
			}
		}

		return children;
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
		int p = tree.reference().position();
		try (LineNumberReader reader = new LineNumberReader(tree.document().openReader())) {
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
		int p = tree.reference().position();
		try (LineNumberReader reader = new LineNumberReader(tree.document().openReader())) {
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
		Document document = tree.document();
		Reference reference = tree.reference();
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
		int p = tree.reference().position();
		String line = "";
		try (BufferedReader reader = new BufferedReader(tree.document().openReader())) {
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

	/**
	 * Return the root tree of the given {@code tree}.
	 *
	 * @param tree the tree to get its root tree.
	 * @return the root tree of the given {@code tree}.
	 * @throws NullPointerException if the given {@code tree} is null.
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	@Contract(pure = true)
	public static Tree root(@NotNull Tree tree) {
		Objects.requireNonNull(tree, "tree");
		Tree root = tree;
		while (true) {
			Tree parent = root.getParent();

			if (parent == null)
				return root;

			root = parent;
		}
	}

	/**
	 * Return an array containing the current children of the given {@code tree}.
	 *
	 * @param tree the tree to get an array containing its current children.
	 * @return a new array containing the children of the given {@code tree}.
	 * @throws NullPointerException if the given {@code tree} is null.
	 * @since 0.2.0 ~2021.05.30
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static Tree[] toArray(@NotNull Tree tree) {
		Objects.requireNonNull(tree, "tree");
		return StreamSupport.stream(
				Spliterators.spliteratorUnknownSize(
						tree.iterator(),
						Spliterator.ORDERED |
						Spliterator.NONNULL |
						Spliterator.DISTINCT
				),
				false
		).toArray(Tree[]::new);
	}

	/**
	 * Collect all the children of the given {@code tree} and add them to the given {@code
	 * result}.
	 *
	 * @param result a set to add the results to.
	 * @param tree   the tree to get its children.
	 * @throws NullPointerException if the given {@code result} or {@code tree} is null.
	 * @since 0.2.0 ~2021.05.23
	 */
	@Contract(mutates = "param1")
	private static void children(@NotNull Set<Tree> result, @NotNull Tree tree) {
		Objects.requireNonNull(result, "result");
		Objects.requireNonNull(tree, "tree");
		for (Tree t : tree) {
			result.add(t);
			Trees.children(result, t);
		}
	}

	/**
	 * Collect all the trees relative to the given {@code tree} and store it to the given
	 * {@code result}. The trees that are already in the given {@code result} will not be
	 * collected.
	 *
	 * @param result the set to add the trees to.
	 * @param tree   the tree to collect its relatives.
	 * @throws NullPointerException if the given {@code result} or {@code tree} is null.
	 * @since 0.2.0 ~2021.05.17
	 */
	@Contract(mutates = "param1")
	private static void collect(@NotNull Set<Tree> result, @NotNull Tree tree) {
		Objects.requireNonNull(result, "result");
		Objects.requireNonNull(tree, "tree");
		Tree parent = tree.getParent();
		Tree previous = tree.getPrevious();
		Tree next = tree.getNext();
		Tree child = tree.getChild();

		//add the tree to not comeback
		result.add(tree);

		//as long as the tree was not collected by this
		if (parent != null && !result.contains(parent))
			Trees.collect(result, parent);
		if (previous != null && !result.contains(previous))
			Trees.collect(result, previous);
		if (next != null && !result.contains(next))
			Trees.collect(result, next);
		if (child != null && !result.contains(child))
			Trees.collect(result, child);
	}
}
