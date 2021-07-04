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

import org.jamplate.function.Analyzer;
import org.jamplate.model.Compilation;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * An analyzer that analyzes trees satisfying a pre-specified predicate using another
 * analyzer.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.07.03
 */
public class FilterAnalyzer implements Analyzer {
	/**
	 * The analyzer to be used.
	 *
	 * @since 0.3.0 ~2021.07.03
	 */
	@NotNull
	protected final Analyzer analyzer;
	/**
	 * The predicate to be satisfied to analyze.
	 *
	 * @since 0.3.0 ~2021.07.03
	 */
	@NotNull
	protected final Predicate<Tree> predicate;

	/**
	 * Construct a new filter analyzer that analyzes using the given {@code analyzer} for
	 * the trees that satisfies the given {@code predicate}.
	 *
	 * @param predicate the predicate that to be satisfied to analyze.
	 * @param analyzer  the analyzer to be used.
	 * @throws NullPointerException if the given {@code predicate} or {@code analyzer} is
	 *                              null.
	 * @since 0.3.0 ~2021.07.03
	 */
	public FilterAnalyzer(@NotNull Predicate<Tree> predicate, @NotNull Analyzer analyzer) {
		Objects.requireNonNull(predicate, "predicate");
		Objects.requireNonNull(analyzer, "analyzer");
		this.predicate = predicate;
		this.analyzer = analyzer;
	}

	/**
	 * Construct a new filter analyzer that analyzes using the given {@code analyzer} for
	 * the trees that satisfies the given {@code predicate}.
	 *
	 * @param analyzer  the analyzer to be used.
	 * @param predicate the predicate that to be satisfied to analyze.
	 * @return a filter analyzer that uses the given {@code analyzer} when the given
	 *        {@code predicate} is satisfied.
	 * @throws NullPointerException if the given {@code predicate} or {@code analyzer} is
	 *                              null.
	 * @since 0.3.0 ~2021.07.03
	 */
	@NotNull
	@Contract(value = "_,_->new", pure = true)
	public static FilterAnalyzer filter(@NotNull Analyzer analyzer, @NotNull Predicate<Tree> predicate) {
		return new FilterAnalyzer(predicate, analyzer);
	}

	@Override
	public boolean analyze(@NotNull Compilation compilation, @NotNull Tree tree) {
		Objects.requireNonNull(tree, "tree");
		return this.predicate.test(tree) &&
			   this.analyzer.analyze(compilation, tree);
	}

	@NotNull
	@Override
	public Iterator<Analyzer> iterator() {
		return Collections.singleton(this.analyzer).iterator();
	}
}
