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

import org.jamplate.model.Intersection;
import org.jamplate.model.Reference;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;
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
	 * Return an array containing the current children of the given {@code tree}.
	 *
	 * @param tree the tree to get an array containing its current children.
	 * @return a new array containing the children of the given {@code tree}.
	 * @throws NullPointerException if the given {@code tree} is null.
	 * @since 0.2.0 ~2021.05.30
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static List<Tree> children(@NotNull Tree tree) {
		Objects.requireNonNull(tree, "tree");
		return StreamSupport.stream(
				Spliterators.spliteratorUnknownSize(
						tree.iterator(),
						Spliterator.ORDERED |
						Spliterator.NONNULL |
						Spliterator.DISTINCT
				),
				false
		).collect(Collectors.toList());
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
				int p = tree.getReference().position();
				int l = previous.getReference().position() - p;

				if (l > 0) {
					Tree gap = new Tree(tree.getDocument(), new Reference(p, l));

					children.add(gap);
				}
			}

			children.add(previous);

			while (iterator.hasNext()) {
				Tree next = iterator.next();

				if (!Intersection.NEXT.test(previous, next)) {
					int p = previous.getReference().position() +
							previous.getReference().length();
					int l = next.getReference().position() - p;

					if (l > 0) {
						Tree gap = new Tree(tree.getDocument(), new Reference(p, l));

						children.add(gap);
					}
				}

				children.add(next);
				previous = next;
			}

			if (!Intersection.END.test(tree, previous)) {
				int p = previous.getReference().position() +
						previous.getReference().length();
				int l = tree.getReference().position() +
						tree.getReference().length() - p;

				if (l > 0) {
					Tree gap = new Tree(tree.getDocument(), new Reference(p, l));

					children.add(gap);
				}
			}
		}

		return children;
	}

	/**
	 * Return the head tree of the given {@code tree}.
	 *
	 * @param tree the tree to get its head tree.
	 * @return the head tree of the given {@code tree}.
	 * @throws NullPointerException if the given {@code tree} is null.
	 * @since 0.3.0 ~2021.06.22
	 */
	@NotNull
	@Contract(pure = true)
	public static Tree head(@NotNull Tree tree) {
		Objects.requireNonNull(tree, "tree");
		Tree head = tree;
		while (true) {
			Tree previous = head.getPrevious();

			if (previous == null)
				return head;

			head = previous;
		}
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
	public static Set<Tree> hierarchy(@NotNull Tree tree) {
		Objects.requireNonNull(tree, "tree");
		Set<Tree> result = new HashSet<>();
		Trees.hierarchy(result, tree);
		return result;
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
	 * Return the tail tree of the given {@code tree}.
	 *
	 * @param tree the tree to get its tail tree.
	 * @return the tail tree of the given {@code tree}.
	 * @throws NullPointerException if the given {@code tree} is null.
	 * @since 0.3.0 ~2021.06.22
	 */
	@NotNull
	@Contract(pure = true)
	public static Tree tail(@NotNull Tree tree) {
		Objects.requireNonNull(tree, "tree");
		Tree tail = tree;
		while (true) {
			Tree next = tail.getNext();

			if (next == null)
				return tail;

			tail = next;
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
	private static void hierarchy(@NotNull Set<Tree> result, @NotNull Tree tree) {
		Objects.requireNonNull(result, "result");
		Objects.requireNonNull(tree, "tree");
		for (Tree t : tree) {
			result.add(t);
			Trees.hierarchy(result, t);
		}
	}
}
