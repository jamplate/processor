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
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * An analyzer that executes a pre-specified list of other analyzers when executed.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.28
 */
public class SequentialAnalyzer implements Analyzer {
	/**
	 * The analyzers used by this analyzer.
	 *
	 * @since 0.2.0 ~2021.05.28
	 */
	@NotNull
	protected final List<Analyzer> analyzers;

	/**
	 * Construct a new analyzer that executes the given {@code analyzers} when executed.
	 * <br>
	 * Null analyzers in the array will be ignored.
	 *
	 * @param analyzers the analyzers to be executed when the constructed analyzer get
	 *                  executed.
	 * @throws NullPointerException if the given {@code analyzers} is null.
	 * @since 0.2.0 ~2021.05.28
	 */
	public SequentialAnalyzer(@Nullable Analyzer @NotNull ... analyzers) {
		Objects.requireNonNull(analyzers, "analyzers");
		this.analyzers = Arrays
				.stream(analyzers)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}

	/**
	 * Construct a new analyzer that executes the given {@code analyzers} when executed.
	 * <br>
	 * Null analyzers in the list will be ignored.
	 *
	 * @param analyzers the analyzers to be executed when the constructed analyzer get
	 *                  executed.
	 * @throws NullPointerException if the given {@code analyzers} is null.
	 * @since 0.2.0 ~2021.05.28
	 */
	public SequentialAnalyzer(@NotNull List<Analyzer> analyzers) {
		Objects.requireNonNull(analyzers, "analyzers");
		this.analyzers = new ArrayList<>();
		for (Analyzer analyzer : analyzers)
			if (analyzer != null)
				this.analyzers.add(analyzer);
	}

	/**
	 * Construct a new analyzer that executes the given analyzers in order.
	 * <br>
	 * Null analyzers in the array will be ignored.
	 *
	 * @param analyzers the analyzers to be executed when the constructed analyzer gets
	 *                  executed.
	 * @return a new sequential analyzer that executes the given {@code analyzers} in
	 * 		order.
	 * @throws NullPointerException if the given {@code analyzer} is null.
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static SequentialAnalyzer sequential(@Nullable Analyzer @NotNull ... analyzers) {
		return new SequentialAnalyzer(analyzers);
	}

	/**
	 * Construct a new analyzer that executes the given analyzers in order.
	 * <br>
	 * Null analyzers in the list will be ignored.
	 *
	 * @param analyzers the analyzers to be executed when the constructed analyzer gets
	 *                  executed.
	 * @return a new sequential analyzer that executes the given {@code analyzers} in
	 * 		order.
	 * @throws NullPointerException if the given {@code analyzer} is null.
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static SequentialAnalyzer sequential(@NotNull List<Analyzer> analyzers) {
		return new SequentialAnalyzer(analyzers);
	}

	@Override
	public boolean analyze(@NotNull Compilation compilation, @NotNull Tree tree) {
		Objects.requireNonNull(compilation, "compilation");
		Objects.requireNonNull(tree, "tree");
		boolean analyzed = false;
		for (Analyzer analyzer : this.analyzers)
			analyzed |= analyzer.analyze(compilation, tree);
		return analyzed;
	}
}
