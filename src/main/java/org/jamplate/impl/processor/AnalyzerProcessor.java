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
package org.jamplate.impl.processor;

import org.jamplate.model.Compilation;
import org.jamplate.model.Tree;
import org.jamplate.model.function.Analyzer;
import org.jamplate.model.function.Processor;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * A processor that analyzes the root trees of the compilations given to it using a
 * pre-specified analyzer.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.28
 */
public class AnalyzerProcessor implements Processor {
	/**
	 * The analyzer.
	 *
	 * @since 0.2.0 ~2021.05.28
	 */
	@NotNull
	protected final Analyzer analyzer;

	/**
	 * Construct a new processor that analyzes the root trees of the compilations given to
	 * it using the given {@code analyzer}.
	 *
	 * @param analyzer the analyzer.
	 * @throws NullPointerException if the given {@code analyzer} is null.
	 * @since 0.2.0 ~2021.05.28
	 */
	public AnalyzerProcessor(@NotNull Analyzer analyzer) {
		Objects.requireNonNull(analyzer, "analyzer");
		this.analyzer = analyzer;
	}

	@Override
	public boolean process(@NotNull Compilation compilation) {
		Objects.requireNonNull(compilation, "compilation");
		Tree root = compilation.getRootTree();

		if (this.analyzer.analyze(compilation, root)) {
			while (this.analyzer.analyze(compilation, root))
				;

			return true;
		}

		return false;
	}
}
