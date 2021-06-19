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
package org.jamplate.impl.analyzer;

import org.jamplate.model.Compilation;
import org.jamplate.model.Tree;
import org.jamplate.function.Analyzer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.BiPredicate;

/**
 * An analyzer that transforms trees that has a kind equal to a pre-specified kind to
 * another pre-specified kind if a pre-specified predicate returned true.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.29
 */
public class ConditionTransformAnalyzer implements Analyzer {
	/**
	 * The testing predicate.
	 *
	 * @since 0.2.0 ~2021.05.29
	 */
	@NotNull
	protected final BiPredicate<Compilation, Tree> condition;
	/**
	 * The targeted kind.
	 *
	 * @since 0.2.0 ~2021.05.29
	 */
	@NotNull
	protected final String target;
	/**
	 * The new kind.
	 *
	 * @since 0.2.0 ~2021.05.29
	 */
	@NotNull
	protected final String transform;

	/**
	 * Construct a new analyzer that transforms the trees with kind equals to the given
	 * {@code target} kind into the given {@code transform} kind if the given {@code
	 * condition} function returned true for those trees.
	 *
	 * @param target    the targeted kind.
	 * @param transform the new kind.
	 * @param condition the condition function.
	 * @throws NullPointerException if the given {@code target} or {@code transform} or
	 *                              {@code condition} is null.
	 * @since 0.2.0 ~2021.05.29
	 */
	public ConditionTransformAnalyzer(@NotNull String target, @NotNull String transform, @NotNull BiPredicate<Compilation, Tree> condition) {
		Objects.requireNonNull(target, "target");
		Objects.requireNonNull(transform, "transform");
		Objects.requireNonNull(condition, "condition");
		this.target = target;
		this.transform = transform;
		this.condition = condition;
	}

	@Override
	public boolean analyze(@NotNull Compilation compilation, @NotNull Tree tree) {
		Objects.requireNonNull(compilation, "compilation");
		Objects.requireNonNull(tree, "tree");
		if (tree.getSketch().getKind().equals(this.target))
			if (this.condition.test(compilation, tree)) {
				tree.getSketch().setKind(this.transform);
				return true;
			}

		return false;
	}
}
