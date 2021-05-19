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
package org.jamplate.util.compile;

import org.jamplate.compile.Parser;
import org.jamplate.model.Compilation;
import org.jamplate.model.Intersection;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

/**
 * A parser that merges the clashing sketches resulting from another parser randomly. This
 * parser is made just to avoid the sorting cost of {@link MergeParser}. So, use this
 * class only if sure no complex clashing might occur in the results.
 * <br>
 * The "complex clashes" include (but not limited to) a scope clashes with another scope
 * that should lose but won just because there was another scope won against it. This
 * class might be removed for this unexpected behaviour.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.17
 */
public class RandomMergeParser implements Parser {
	/**
	 * The parser this parser is wrapping.
	 *
	 * @since 0.2.0 ~2021.05.17
	 */
	@NotNull
	protected final Parser parser;

	/**
	 * Construct a clash merging parser merging the results of invoking the given {@code
	 * parser}.
	 *
	 * @param parser the parser the constructed parser is wrapping.
	 * @throws NullPointerException if the given {@code parser} is null.
	 * @since 0.2.0 ~2021.05.17
	 */
	public RandomMergeParser(@NotNull Parser parser) {
		Objects.requireNonNull(parser, "parser");
		this.parser = parser;
	}

	@NotNull
	@Override
	public Set<Tree> parse(@NotNull Compilation compilation, @NotNull Tree tree) {
		Objects.requireNonNull(compilation, "compilation");
		Objects.requireNonNull(tree, "tree");

		Set<Tree> set = new HashSet<>(this.parser.parse(compilation, tree));

		Iterator<Tree> iterator = set.iterator();
		while (iterator.hasNext()) {
			Tree nextTree = iterator.next();

			//if any clash, remove the tree
			set.parallelStream()
			   .filter(otherTree -> otherTree != nextTree)
			   .filter(otherTree -> !this.check(nextTree, otherTree))
			   .findAny()
			   .ifPresent(other ->
					   iterator.remove()
			   );
		}

		return set;
	}

	/**
	 * Check if the given {@code tree} clashes with the given {@code other} tree and
	 * should be removed.
	 *
	 * @param tree  the tree to be checked.
	 * @param other the other tree to be checked.
	 * @return true, if no need to remove the given {@code tree} because either it does
	 * 		not clash with the other tree or if it came first.
	 * @throws NullPointerException if the given {@code tree} or {@code other} is null.
	 * @since 0.2.0 ~2021.05.18
	 */
	@Contract(pure = true)
	protected boolean check(@NotNull Tree tree, @NotNull Tree other) {
		Objects.requireNonNull(tree, "tree");
		Objects.requireNonNull(other, "other");
		switch (Intersection.compute(tree, other)) {
			case AHEAD:
			case BEHIND:
			case CONTAINER:
				//the tree can fit in the other, check if the tree clashes with any other children
				for (Tree otherChild : other)
					//check if the tree clashes with any child in the other
					if (!this.check(tree, otherChild))
						//the tree clashes with a child in the other and has not won the clash, tree lose
						return false;

				//the tree can fit no problem in the other
				return true;
			case END:
			case START:
			case FRAGMENT:
				//parents always win the argument :)
				return true;
			case NEXT:
			case AFTER:
			case PREVIOUS:
			case BEFORE:
				//neighbors = OK
				return true;
			case UNDERFLOW:
				//the other came first
				return false;
			case OVERFLOW:
				//the tree came first
				return true;
			case SAME:
				//make the other win
				return false;
			default:
				//unknown relation
				throw new InternalError();
		}
	}
}
