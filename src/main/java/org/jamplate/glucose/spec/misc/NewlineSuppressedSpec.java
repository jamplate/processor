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
package org.jamplate.glucose.spec.misc;

import org.jamplate.unit.Spec;
import org.jamplate.function.Analyzer;
import org.jamplate.function.Compiler;
import org.jamplate.glucose.instruction.memory.heap.IAlloc;
import org.jamplate.glucose.instruction.memory.resource.IPushConst;
import org.jamplate.impl.instruction.Block;
import org.jetbrains.annotations.NotNull;

import static org.jamplate.glucose.internal.util.Values.number;
import static org.jamplate.glucose.internal.util.Values.text;
import static org.jamplate.impl.analyzer.FilterAnalyzer.filter;
import static org.jamplate.impl.analyzer.HierarchyAnalyzer.hierarchy;
import static org.jamplate.impl.compiler.FilterCompiler.filter;
import static org.jamplate.util.Functions.analyzer;
import static org.jamplate.util.Functions.compiler;
import static org.jamplate.util.Query.*;
import static org.jamplate.util.Source.line;

/**
 * Suppressed new line specification. Suppresses newlines nearby commands.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.24
 */
public class NewlineSuppressedSpec implements Spec {
	/**
	 * An instance of this spec.
	 *
	 * @since 0.3.0 ~2021.06.24
	 */
	@NotNull
	public static final NewlineSuppressedSpec INSTANCE = new NewlineSuppressedSpec();

	/**
	 * The kind of a suppressed line separator trees.
	 *
	 * @since 0.3.0 ~2021.06.24
	 */
	@NotNull
	public static final String KIND = "newline:suppressed";

	/**
	 * The qualified name of this spec.
	 *
	 * @since 0.3.0 ~2021.06.24
	 */
	@NotNull
	public static final String NAME = NewlineSuppressedSpec.class.getSimpleName();

	@NotNull
	@Override
	public Analyzer getAnalyzer() {
		return analyzer(
				a -> hierarchy(a),
				//target newlines
				a -> filter(a, and(
						is(NewlineSpec.KIND),
						or(
								next(equal("^command.*")),
								previous(equal("^command.*"))
						)
				)),
				//analyze
				a -> (compilation, tree) -> {
					tree.getSketch().setKind(NewlineSuppressedSpec.KIND);
					return true;
				}
		);
	}

	@NotNull
	@Override
	public Compiler getCompiler() {
		return compiler(
				//target suppressed newlines
				c -> filter(c, is(NewlineSuppressedSpec.KIND)),
				//compile the suppressed newlines
				c -> (compiler, compilation, tree) -> {
					//determine the line number of the next line
					int line = line(tree) + 1;

					return new Block(
							//Define __LINE__
							new IPushConst(tree, text("__LINE__")),
							new IPushConst(tree, number(line)),
							new IAlloc(tree)
					);
				}
		);
	}

	@NotNull
	@Override
	public String getQualifiedName() {
		return NewlineSuppressedSpec.NAME;
	}
}
