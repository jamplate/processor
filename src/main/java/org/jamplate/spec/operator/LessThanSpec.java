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
package org.jamplate.spec.operator;

import org.jamplate.api.Spec;
import org.jamplate.function.Analyzer;
import org.jamplate.function.Compiler;
import org.jamplate.instruction.flow.Block;
import org.jamplate.instruction.memory.resource.PushConst;
import org.jamplate.instruction.operator.cast.CastBoolean;
import org.jamplate.instruction.operator.logic.Compare;
import org.jamplate.instruction.operator.logic.Negate;
import org.jamplate.internal.function.analyzer.alter.BinaryOperatorAnalyzer;
import org.jamplate.internal.function.analyzer.wrapper.FilterByKindAnalyzer;
import org.jamplate.internal.function.analyzer.wrapper.FilterByNotParentKindAnalyzer;
import org.jamplate.internal.function.analyzer.wrapper.HierarchyAnalyzer;
import org.jamplate.internal.function.compiler.wrapper.FilterByKindCompiler;
import org.jamplate.internal.util.Functions;
import org.jamplate.internal.util.IO;
import org.jamplate.model.CompileException;
import org.jamplate.model.Instruction;
import org.jamplate.model.Sketch;
import org.jamplate.model.Tree;
import org.jamplate.spec.element.ParameterSpec;
import org.jamplate.spec.standard.OperatorSpec;
import org.jamplate.spec.syntax.symbol.OpenChevronSpec;
import org.jamplate.value.NumberValue;
import org.jetbrains.annotations.NotNull;

/**
 * Less-Than operator specifications.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.25
 */
@SuppressWarnings({"OverlyCoupledMethod", "OverlyCoupledClass"})
public class LessThanSpec implements Spec {
	/**
	 * An instance of this spec.
	 *
	 * @since 0.3.0 ~2021.06.25
	 */
	@NotNull
	public static final LessThanSpec INSTANCE = new LessThanSpec();

	/**
	 * The kind of an less-than operator context.
	 *
	 * @since 0.3.0 ~2021.06.25
	 */
	@NotNull
	public static final String KIND = "operator:less_than";

	/**
	 * The qualified name of this spec.
	 *
	 * @since 0.3.0 ~2021.06.25
	 */
	@NotNull
	public static final String NAME = LessThanSpec.class.getSimpleName();

	@NotNull
	@Override
	public Analyzer getAnalyzer() {
		return Functions.analyzer(
				//analyze the whole hierarchy
				HierarchyAnalyzer::new,
				//filter only if not already wrapped
				a -> new FilterByNotParentKindAnalyzer(LessThanSpec.KIND, a),
				//target open-chevrons
				a -> new FilterByKindAnalyzer(OpenChevronSpec.KIND, a),
				//wrap
				a -> new BinaryOperatorAnalyzer(
						//context wrapper constructor
						(d, r) -> new Tree(
								d,
								r,
								new Sketch(LessThanSpec.KIND),
								OperatorSpec.Z_INDEX
						),
						//operator constructor
						(w, t) -> w.getSketch().set(
								OperatorSpec.KEY_SIGN,
								t.getSketch()
						),
						//left-side wrapper constructor
						(w, r) -> w.offer(new Tree(
								w.document(),
								r,
								w.getSketch()
								 .get(OperatorSpec.KEY_LEFT)
								 .setKind(ParameterSpec.KIND),
								ParameterSpec.Z_INDEX
						)),
						//right-side wrapper constructor
						(w, r) -> w.offer(new Tree(
								w.document(),
								r,
								w.getSketch()
								 .get(OperatorSpec.KEY_RIGHT)
								 .setKind(ParameterSpec.KIND),
								ParameterSpec.Z_INDEX
						))
				)
		);
	}

	@NotNull
	@Override
	public Compiler getCompiler() {
		return Functions.compiler(
				//target less-than operator
				c -> new FilterByKindCompiler(LessThanSpec.KIND, c),
				//compile
				c -> (compiler, compilation, tree) -> {
					Tree leftT = tree.getSketch().get(OperatorSpec.KEY_LEFT).getTree();
					Tree rightT = tree.getSketch().get(OperatorSpec.KEY_RIGHT).getTree();

					if (leftT == null || rightT == null)
						throw new CompileException(
								"Operator LESS_THAN (<) is missing some components",
								tree
						);

					Instruction leftI = compiler.compile(
							compiler,
							compilation,
							leftT
					);
					Instruction rightI = compiler.compile(
							compiler,
							compilation,
							rightT
					);

					if (leftI == null || rightI == null)
						throw new CompileException(
								"The operator LESS_THAN (<) cannot be applied to <" +
								IO.read(leftT) +
								"> and <" +
								IO.read(rightT) +
								">",
								tree
						);

					return new Block(
							tree,
							leftI,
							rightI,
							//compare the values
							new Compare(tree),
							//push `-1` to compare the comparison result
							new PushConst(tree, new NumberValue(-1)),
							//compare the comparison result with `-1`
							new Compare(tree),
							//cast the result to boolean
							new CastBoolean(tree),
							//negate the results
							new Negate(tree)
					);
				}
		);
	}

	@NotNull
	@Override
	public String getQualifiedName() {
		return LessThanSpec.NAME;
	}
}
