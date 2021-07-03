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

import org.jamplate.api.Spec;
import org.jamplate.function.Analyzer;
import org.jamplate.function.Compiler;
import org.jamplate.glucose.instruction.flow.Block;
import org.jamplate.glucose.instruction.memory.heap.Alloc;
import org.jamplate.glucose.instruction.memory.resource.PushConst;
import org.jamplate.internal.function.analyzer.filter.FilterByKindAnalyzer;
import org.jamplate.internal.function.analyzer.router.HierarchyAnalyzer;
import org.jamplate.internal.function.compiler.filter.FilterByKindCompiler;
import org.jamplate.internal.util.Functions;
import org.jamplate.internal.util.IO;
import org.jamplate.model.Tree;
import org.jamplate.glucose.value.NumberValue;
import org.jamplate.glucose.value.TextValue;
import org.jetbrains.annotations.NotNull;

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
		return Functions.analyzer(
				//search the whole hierarchy
				HierarchyAnalyzer::new,
				//target newlines
				a -> new FilterByKindAnalyzer(NewlineSpec.KIND, a),
				//analyze
				a -> (compilation, tree) -> {
					//gather surroundings
					Tree previous = tree.getPrevious();
					Tree next = tree.getNext();

					//suppress trees that its kind starts with 'command'
					if (
							previous != null &&
							previous.getSketch().getKind().startsWith("command") ||
							next != null &&
							next.getSketch().getKind().startsWith("command")
					) {
						tree.getSketch().setKind(NewlineSuppressedSpec.KIND);
						return true;
					}

					//nothing interesting...
					return false;
				}
		);
	}

	@NotNull
	@Override
	public Compiler getCompiler() {
		return Functions.compiler(
				//target suppressed newlines
				c -> new FilterByKindCompiler(NewlineSuppressedSpec.KIND, c),
				//compile the suppressed newlines
				c -> (compiler, compilation, tree) -> {
					//determine the line number of the next line
					String line = String.valueOf(IO.line(tree) + 1);

					return new Block(
							//Define __LINE__
							new PushConst(tree, new TextValue("__LINE__")),
							new PushConst(tree, new NumberValue(line)),
							new Alloc(tree)
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
