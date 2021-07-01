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
package org.jamplate.internal.function.analyzer.filter;

import org.jamplate.function.Analyzer;
import org.jamplate.model.Compilation;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * An analyzer that filters the trees by their kind.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.27
 */
public class FilterByNotChildKindAnalyzer implements Analyzer {
	/**
	 * The wrapped analyzer.
	 *
	 * @since 0.3.0 ~2021.07.02
	 */
	@NotNull
	protected final Analyzer analyzer;
	/**
	 * The kind of the targeted trees.
	 *
	 * @since 0.3.0 ~2021.07.02
	 */
	@NotNull
	protected final String kind;

	/**
	 * Construct a new analyzer that uses the given {@code analyzer} and analyzes trees
	 * with the given {@code kind}.
	 *
	 * @param kind     the kind of the trees to be analyzed.
	 * @param analyzer the analyzer to be used.
	 * @throws NullPointerException if the given {@code kind} or {@code analyzer} is
	 *                              null.
	 * @since 0.3.0 ~2021.07.02
	 */
	public FilterByNotChildKindAnalyzer(@NotNull String kind, @NotNull Analyzer analyzer) {
		Objects.requireNonNull(kind, "kind");
		Objects.requireNonNull(analyzer, "analyzer");
		this.kind = kind;
		this.analyzer = analyzer;
	}

	@Override
	public boolean analyze(@NotNull Compilation compilation, @NotNull Tree tree) {
		Objects.requireNonNull(compilation, "compilation");
		Objects.requireNonNull(tree, "tree");

		for (Tree child : tree)
			if (this.kind.equals(child.getSketch().getKind()))
				return false;

		return this.analyzer.analyze(compilation, tree);
	}
}
