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

import cufy.util.Node;
import org.intellij.lang.annotations.Language;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.jamplate.util.Source.read;

/**
 * Trees predicates.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.07.03
 */
public final class Query {
	/**
	 * Tool classes does not need instantiating.
	 *
	 * @throws AssertionError when called.
	 * @since 0.3.0 ~2021.07.04
	 */
	private Query() {
		throw new AssertionError("No instance for you!");
	}

	/**
	 * Return a tree predicate that returns {@code true} if all the given {@code
	 * predicates} evaluated to {@code true} for the same tree.
	 * <br>
	 * Null predicates will be ignored.
	 *
	 * @param predicates the predicates.
	 * @return a predicate as described above.
	 * @throws NullPointerException if the given {@code predicates} is null.
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	@SafeVarargs
	@Contract(value = "_->new", pure = true)
	public static Predicate<Tree> and(@Nullable Predicate<Tree> @NotNull ... predicates) {
		Objects.requireNonNull(predicates, "predicates");
		List<Predicate<Tree>> ps = Arrays
				.stream(predicates)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
		return tree -> ps.stream().allMatch(p -> p.test(tree));
	}

	/**
	 * Return a tree bi-predicate that returns {@code true} if all the given {@code
	 * predicates} evaluated to {@code true} for the same trees.
	 * <br>
	 * Null predicates will be ignored.
	 *
	 * @param predicates the predicates.
	 * @return a predicate as described above.
	 * @throws NullPointerException if the given {@code predicates} is null.
	 * @since 0.3.0 ~2021.07.09
	 */
	@NotNull
	@SafeVarargs
	@Contract(value = "_->new", pure = true)
	public static BiPredicate<Tree, Tree> and(@Nullable BiPredicate<Tree, Tree> @NotNull ... predicates) {
		Objects.requireNonNull(predicates, "predicates");
		List<BiPredicate<Tree, Tree>> ps = Arrays
				.stream(predicates)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
		return (f, s) -> ps.stream().allMatch(p -> p.test(f, s));
	}

	/**
	 * Return a bi-predicate that returns true if the given {@code first} predicate
	 * returned true for the first tree and the given {@code second} predicate returned
	 * true for the second tree.
	 *
	 * @param first  the predicate for the first argument (tree).
	 * @param second the predicate for the second argument (tree).
	 * @return a predicate as described above.
	 * @throws NullPointerException if the given {@code first} or {@code second} is null.
	 * @since 0.3.0 ~2021.07.09
	 */
	@NotNull
	@Contract(value = "_,_->new", pure = true)
	public static BiPredicate<Tree, Tree> bi(@NotNull Predicate<Tree> first, @NotNull Predicate<Tree> second) {
		Objects.requireNonNull(first, "first");
		Objects.requireNonNull(second, "second");
		return (f, s) -> first.test(f) && second.test(s);
	}

	/**
	 * Return a predicate that returns {@code true} if the given {@code predicate}
	 * returned {@code true} for a child of the tree.
	 *
	 * @param predicate the predicate.
	 * @return a predicate as described above.
	 * @throws NullPointerException if the given {@code predicate} is null.
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static Predicate<Tree> child(@NotNull Predicate<Tree> predicate) {
		Objects.requireNonNull(predicate, "predicate");
		return tree -> {
			for (Tree child : tree)
				if (predicate.test(child))
					return true;
			return false;
		};
	}

	/**
	 * Return a predicate that returns {@code true} if the tree given to it has a tree for
	 * the component {@code key}.
	 *
	 * @param key the key of the component.
	 * @return a predicate as described above.
	 * @throws NullPointerException if the given {@code key} is null.
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static Predicate<Tree> comp(@NotNull Node.Key key) {
		Objects.requireNonNull(key, "key");
		return tree -> tree.getSketch().get(key).getTree() != null;
	}

	/**
	 * Return a predicate that returns {@code true} if the given {@code predicate}
	 * returned {@code true} for the tree of the component {@code key} of the tree.
	 *
	 * @param key       the key of the component.
	 * @param predicate the predicate.
	 * @return a predicate as described above.
	 * @throws NullPointerException if the given {@code predicate} is null.
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	@Contract(value = "_,_->new", pure = true)
	public static Predicate<Tree> comp(@NotNull Node.Key key, @NotNull Predicate<Tree> predicate) {
		Objects.requireNonNull(key, "key");
		Objects.requireNonNull(predicate, "predicate");
		return tree -> {
			Tree comp = tree.getSketch().get(key).getTree();
			return comp != null && predicate.test(comp);
		};
	}

	/**
	 * Return a predicate that returns {@code true} if the kind of the tree given to it
	 * matches the given {@code regex}.
	 *
	 * @param regex the regex to be applied to the kind.
	 * @return a predicate as described above.
	 * @throws NullPointerException if the given {@code regex} is null.
	 * @since 0.3.0 ~2021.07.04
	 */
	@SuppressWarnings("MisspelledEquals")
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static Predicate<Tree> equal(@NotNull @Language("RegExp") String regex) {
		Objects.requireNonNull(regex, "regex");
		Pattern pattern = Pattern.compile(regex);
		return tree -> pattern.matcher(tree.getSketch().getKind()).matches();
	}

	/**
	 * Return a predicate that returns {@code true} if the tree given to it has a child.
	 *
	 * @return a predicate as described above.
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	@Contract(value = "->new", pure = true)
	public static Predicate<Tree> first() {
		return tree -> tree.getChild() != null;
	}

	/**
	 * Return a predicate that returns {@code true} if the given {@code predicate}
	 * returned {@code true} for the first child of the tree.
	 *
	 * @param predicate the predicate.
	 * @return a predicate as described above.
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static Predicate<Tree> first(@NotNull Predicate<Tree> predicate) {
		Objects.requireNonNull(predicate, "predicate");
		return tree -> {
			Tree child = tree.getChild();
			return child != null && predicate.test(child);
		};
	}

	/**
	 * Return a predicate that returns {@code true} if the kind of the tree given to it
	 * equals the given {@code kind}.
	 *
	 * @param kind the kind.
	 * @return a predicate as described above.
	 * @throws NullPointerException if the given {@code kind} is null.
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static Predicate<Tree> is(@NotNull String kind) {
		Objects.requireNonNull(kind, "kind");
		return tree -> tree.getSketch().getKind().equals(kind);
	}

	/**
	 * Return a predicate that returns {@code true} if the given {@code predicate}
	 * returned {@code true} for any child or grand-child of the tree.
	 *
	 * @param predicate the predicate.
	 * @return a predicate as described above.
	 * @throws NullPointerException if the given {@code predicate} is null.
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static Predicate<Tree> leaf(@NotNull Predicate<Tree> predicate) {
		Objects.requireNonNull(predicate, "predicate");
		return tree -> {
			Deque<Tree> deque = new LinkedList<>(
					Collections.singletonList(tree.getChild())
			);

			while (!deque.isEmpty()) {
				Tree next = deque.pollLast();

				if (next != null) {
					if (predicate.test(next))
						return true;

					deque.addLast(next.getChild());
					deque.addLast(next.getNext());
				}
			}

			return false;
		};
	}

	/**
	 * Return a predicate that returns {@code true} if the tree given to it has a tree
	 * after it.
	 *
	 * @return a predicate as described above.
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	@Contract(value = "->new", pure = true)
	public static Predicate<Tree> next() {
		return tree -> tree.getNext() != null;
	}

	/**
	 * Return a predicate that returns {@code true} if the given {@code predicate}
	 * returned {@code true} for the tree after the tree.
	 *
	 * @param predicate the predicate.
	 * @return a predicate as described above.
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static Predicate<Tree> next(@NotNull Predicate<Tree> predicate) {
		Objects.requireNonNull(predicate, "predicate");
		return tree -> {
			Tree next = tree.getNext();
			return next != null && predicate.test(next);
		};
	}

	/**
	 * Return a predicate that returns {@code true} if the tree given to it is {@code
	 * null}.
	 *
	 * @return a predicate as described above.
	 * @since 0.3.0 ~2021.07.09
	 */
	@NotNull
	@Contract(pure = true)
	public static Predicate<Tree> nil() {
		return tree -> tree == null;
	}

	/**
	 * Return a predicate that returns {@code true} if the kind of the tree given to it
	 * does not equals the given {@code kind}.
	 *
	 * @param kind the not kind.
	 * @return a predicate as described above.
	 * @throws NullPointerException if the given {@code kind} is null.
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static Predicate<Tree> not(@NotNull String kind) {
		Objects.requireNonNull(kind, "kind");
		return tree -> !tree.getSketch().getKind().equals(kind);
	}

	/**
	 * Return a tree predicate that returns {@code true} if any of the given {@code
	 * predicates} evaluated to {@code true} for the same tree.
	 * <br>
	 * Null predicates will be ignored.
	 *
	 * @param predicates the predicates.
	 * @return a predicate as described above.
	 * @throws NullPointerException if the given {@code predicates} is null.
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	@SafeVarargs
	@Contract(value = "_->new", pure = true)
	public static Predicate<Tree> or(@Nullable Predicate<Tree> @NotNull ... predicates) {
		Objects.requireNonNull(predicates, "predicates");
		List<Predicate<Tree>> ps = Arrays
				.stream(predicates)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
		return tree -> ps.stream().anyMatch(p -> p.test(tree));
	}

	/**
	 * Return a tree bi-predicate that returns {@code true} if any of the given {@code
	 * predicates} evaluated to {@code true} for the same trees.
	 * <br>
	 * Null predicates will be ignored.
	 *
	 * @param predicates the predicates.
	 * @return a predicate as described above.
	 * @throws NullPointerException if the given {@code predicates} is null.
	 * @since 0.3.0 ~2021.07.09
	 */
	@NotNull
	@SafeVarargs
	@Contract(value = "_->new", pure = true)
	public static BiPredicate<Tree, Tree> or(@Nullable BiPredicate<Tree, Tree> @NotNull ... predicates) {
		Objects.requireNonNull(predicates, "predicates");
		List<BiPredicate<Tree, Tree>> ps = Arrays
				.stream(predicates)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
		return (f, s) -> ps.stream().anyMatch(p -> p.test(f, s));
	}

	/**
	 * Return a predicate that returns {@code true} if the tree given to it has a parent
	 * tree.
	 *
	 * @return a predicate as described above.
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	@Contract(value = "->new", pure = true)
	public static Predicate<Tree> parent() {
		return tree -> tree.getParent() != null;
	}

	/**
	 * Return a predicate that returns {@code true} if the given {@code predicate}
	 * returned {@code true} for the parent of the tree.
	 *
	 * @param predicate the predicate.
	 * @return a predicate as described above.
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static Predicate<Tree> parent(@NotNull Predicate<Tree> predicate) {
		Objects.requireNonNull(predicate, "predicate");
		return tree -> {
			Tree parent = tree.getParent();
			return parent != null && predicate.test(parent);
		};
	}

	/**
	 * Return a predicate that returns {@code true} if the tree given to it has a previous
	 * tree.
	 *
	 * @return a predicate as described above.
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	@Contract(value = "->new", pure = true)
	public static Predicate<Tree> previous() {
		return tree -> tree.getPrevious() != null;
	}

	/**
	 * Return a predicate that returns {@code true} if the given {@code predicate}
	 * returned {@code true} for the tree before the tree.
	 *
	 * @param predicate the predicate.
	 * @return a predicate as described above.
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static Predicate<Tree> previous(@NotNull Predicate<Tree> predicate) {
		Objects.requireNonNull(predicate, "predicate");
		return tree -> {
			Tree previous = tree.getPrevious();
			return previous != null && predicate.test(previous);
		};
	}

	/**
	 * Return a predicate that returns {@code true} if the given {@code predicate}
	 * returned {@code true} for any parent or grand-parent of the tree.
	 *
	 * @param predicate the predicate.
	 * @return a predicate as described above.
	 * @throws NullPointerException if the given {@code predicate} is null.
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static Predicate<Tree> stem(@NotNull Predicate<Tree> predicate) {
		Objects.requireNonNull(predicate, "predicate");
		return tree -> {
			Tree stem = tree.getParent();

			while (stem != null) {
				if (predicate.test(stem))
					return true;

				stem = stem.getParent();
			}

			return false;
		};
	}

	/**
	 * Return a predicate that returns {@code true} if the text of the tree given to it is
	 * just whitespace.
	 *
	 * @return a predicate as described above.
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	@Contract(value = "->new", pure = true)
	public static Predicate<Tree> whitespace() {
		return tree -> read(tree).toString().trim().isEmpty();
	}
}
