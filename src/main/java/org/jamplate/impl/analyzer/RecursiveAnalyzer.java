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

/**
 * A wrapper analyzer that recursively analyze the trees given to it using a pre-specified
 * analyzer.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.28
 */
@Deprecated
public class RecursiveAnalyzer implements Analyzer {
	/**
	 * The wrapped analyzer.
	 *
	 * @since 0.2.0 ~2021.05.28
	 */
	@NotNull
	protected final Analyzer analyzer;

	/**
	 * Construct a wrapper analyzer that recursively analyze the children of the trees
	 * given to it using the given {@code analyzer}.
	 *
	 * @param analyzer the wrapped analyzer.
	 * @throws NullPointerException if the given {@code analyzer} is null.
	 * @since 0.2.0 ~2021.05.28
	 */
	public RecursiveAnalyzer(@NotNull Analyzer analyzer) {
		Objects.requireNonNull(analyzer, "analyzer");
		this.analyzer = analyzer;
	}

	@Override
	public boolean analyze(@NotNull Compilation compilation, @NotNull Tree tree) {
		Objects.requireNonNull(compilation, "compilation");
		Objects.requireNonNull(tree, "tree");
		boolean analyzed = this.analyzer.analyze(compilation, tree);

		for (Tree child : tree)
			analyzed |= this.analyze(compilation, child);

		return analyzed;
	}
}
