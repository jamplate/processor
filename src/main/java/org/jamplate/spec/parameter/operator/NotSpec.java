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
package org.jamplate.spec.parameter.operator;

import org.jamplate.api.Spec;
import org.jamplate.function.Analyzer;
import org.jamplate.function.Compiler;
import org.jamplate.instruction.flow.Block;
import org.jamplate.instruction.operator.cast.CastBoolean;
import org.jamplate.instruction.operator.logic.Negate;
import org.jamplate.internal.function.analyzer.alter.UnaryOperatorAnalyzer;
import org.jamplate.internal.function.analyzer.filter.FilterByKindAnalyzer;
import org.jamplate.internal.function.analyzer.filter.FilterByNotParentKindAnalyzer;
import org.jamplate.internal.function.analyzer.router.HierarchyAnalyzer;
import org.jamplate.internal.function.compiler.filter.FilterByKindCompiler;
import org.jamplate.internal.util.Functions;
import org.jamplate.internal.util.IO;
import org.jamplate.model.CompileException;
import org.jamplate.model.Instruction;
import org.jamplate.model.Sketch;
import org.jamplate.model.Tree;
import org.jamplate.spec.element.ParameterSpec;
import org.jamplate.spec.standard.OperatorSpec;
import org.jamplate.spec.syntax.symbol.ExclamationSpec;
import org.jetbrains.annotations.NotNull;

/**
 * Not operator specifications.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.25
 */
public class NotSpec implements Spec {
	/**
	 * An instance of this spec.
	 *
	 * @since 0.3.0 ~2021.06.25
	 */
	@NotNull
	public static final NotSpec INSTANCE = new NotSpec();

	/**
	 * The kind of a not operator context.
	 *
	 * @since 0.3.0 ~2021.06.25
	 */
	@NotNull
	public static final String KIND = "operator:not";

	/**
	 * The qualified name of this spec.
	 *
	 * @since 0.3.0 ~2021.06.25
	 */
	@NotNull
	public static final String NAME = NotSpec.class.getSimpleName();

	@NotNull
	@Override
	public Analyzer getAnalyzer() {
		return Functions.analyzer(
				//analyze the whole hierarchy
				HierarchyAnalyzer::new,
				//filter only if not already wrapped
				a -> new FilterByNotParentKindAnalyzer(NotSpec.KIND, a),
				//target exclamation
				a -> new FilterByKindAnalyzer(ExclamationSpec.KIND, a),
				//wrap
				a -> new UnaryOperatorAnalyzer(
						//context wrapper constructor
						(d, r) -> new Tree(
								d,
								r,
								new Sketch(NotSpec.KIND),
								OperatorSpec.WEIGHT
						),
						//operator constructor
						(w, t) -> w.getSketch().set(
								OperatorSpec.KEY_SIGN,
								t.getSketch()
						),
						//right-side wrapper constructor
						(w, r) -> w.offer(new Tree(
								w.document(),
								r,
								w.getSketch()
								 .get(OperatorSpec.KEY_RIGHT)
								 .setKind(ParameterSpec.KIND),
								ParameterSpec.WEIGHT
						))
				)
		);
	}

	@NotNull
	@Override
	public Compiler getCompiler() {
		return Functions.compiler(
				//target not operator
				c -> new FilterByKindCompiler(NotSpec.KIND, c),
				//compile
				c -> (compiler, compilation, tree) -> {
					Tree rightT = tree.getSketch().get(OperatorSpec.KEY_RIGHT).getTree();

					if (rightT == null)
						throw new CompileException(
								"Operator NOT (!) is missing some components",
								tree
						);

					Instruction rightI = compiler.compile(
							compiler,
							compilation,
							rightT
					);

					if (rightI == null)
						throw new CompileException(
								"The operator NOT (!) cannot be applied to <" +
								IO.read(rightT) +
								">",
								tree
						);

					return new Block(
							tree,
							rightI,
							new CastBoolean(tree),
							new Negate(tree)
					);
				}
		);
	}

	@NotNull
	@Override
	public String getQualifiedName() {
		return NotSpec.NAME;
	}
}
