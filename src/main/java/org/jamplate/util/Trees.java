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
package org.jamplate.util;

import org.jamplate.model.Tree;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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
	 * Collect all the relatives to the given {@code tree} (including the tree itself).
	 *
	 * @param tree the tree to collect its relatives.
	 * @return a set containing all the relatives of the given {@code tree}.
	 * @throws NullPointerException if the given {@code tree} is null.
	 * @since 0.2.0 ~2021.05.17
	 */
	@Contract(value = "_->new", pure = true)
	public static Set<Tree> collect(@NotNull Tree tree) {
		Objects.requireNonNull(tree, "tree");
		Set<Tree> result = new HashSet<>();
		Trees.collect(result, tree);
		return result;
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
