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
package org.jamplate.internal.util.analyzer.wrapper;

import org.jamplate.function.Analyzer;
import org.jamplate.model.Compilation;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * An analyzer that analyzes using another analyzer and only analyzes trees with none of
 * pre-specified kinds.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.24
 */
public class FilterByNotKindAnalyzer implements Analyzer {
	/**
	 * The analyzer to be used.
	 *
	 * @since 0.3.0 ~2021.06.24
	 */
	@NotNull
	protected final Analyzer analyzer;
	/**
	 * The skipped kinds.
	 *
	 * @since 0.3.0 ~2021.06.24
	 */
	@NotNull
	protected final Set<String> kinds;

	/**
	 * Construct a new analyzer that analyzes the trees without the given {@code kind}
	 * using the given {@code analyzer}.
	 *
	 * @param kind     the kind of trees to skip analyzing.
	 * @param analyzer the analyzer to be used.
	 * @throws NullPointerException if the given {@code kind} or {@code analyzer} is
	 *                              null.
	 * @since 0.3.0 ~2021.06.24
	 */
	public FilterByNotKindAnalyzer(@NotNull String kind, @NotNull Analyzer analyzer) {
		Objects.requireNonNull(kind, "kind");
		Objects.requireNonNull(analyzer, "analyzer");
		this.kinds = Collections.singleton(kind);
		this.analyzer = analyzer;
	}

	/**
	 * Construct a new analyzer that analyzes the trees with none of the given {@code
	 * kinds} using the given {@code analyzer}.
	 *
	 * @param analyzer the analyzer to be used.
	 * @param kinds    the kinds of trees to skip analyzing.
	 * @throws NullPointerException if the given {@code analyzer} or {@code kinds} is
	 *                              null.
	 * @since 0.3.0 ~2021.06.24
	 */
	public FilterByNotKindAnalyzer(@NotNull Analyzer analyzer, @Nullable String @NotNull ... kinds) {
		Objects.requireNonNull(kinds, "kinds");
		Objects.requireNonNull(analyzer, "analyzer");
		this.kinds = Arrays
				.stream(kinds)
				.filter(Objects::nonNull)
				.collect(Collectors.toSet());
		this.analyzer = analyzer;
	}

	@Override
	public boolean analyze(@NotNull Compilation compilation, @NotNull Tree tree) {
		Objects.requireNonNull(compilation, "compilation");
		Objects.requireNonNull(tree, "tree");

		if (!this.kinds.contains(tree.getSketch().getKind()))
			return this.analyzer.analyze(compilation, tree);

		return false;
	}
}
