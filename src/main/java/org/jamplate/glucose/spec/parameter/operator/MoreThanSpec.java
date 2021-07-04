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
package org.jamplate.glucose.spec.parameter.operator;

import org.jamplate.api.Spec;
import org.jamplate.function.Analyzer;
import org.jamplate.function.Compiler;
import org.jamplate.glucose.instruction.flow.Block;
import org.jamplate.glucose.instruction.memory.resource.PushConst;
import org.jamplate.glucose.instruction.operator.cast.CastBoolean;
import org.jamplate.glucose.instruction.operator.logic.Compare;
import org.jamplate.glucose.instruction.operator.logic.Negate;
import org.jamplate.glucose.spec.element.ParameterSpec;
import org.jamplate.glucose.spec.standard.OperatorSpec;
import org.jamplate.glucose.spec.syntax.symbol.CloseChevronSpec;
import org.jamplate.glucose.value.NumberValue;
import org.jamplate.internal.util.Source;
import org.jamplate.model.CompileException;
import org.jamplate.model.Instruction;
import org.jamplate.model.Sketch;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.NotNull;

import static org.jamplate.internal.util.Query.*;
import static org.jamplate.impl.analyzer.FilterAnalyzer.filter;
import static org.jamplate.internal.analyzer.BinaryOperatorAnalyzer.binaryOperator;
import static org.jamplate.impl.analyzer.HierarchyAnalyzer.hierarchy;
import static org.jamplate.impl.compiler.FilterCompiler.filter;
import static org.jamplate.internal.util.Functions.analyzer;
import static org.jamplate.internal.util.Functions.compiler;

/**
 * More-Than operator specifications.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.25
 */
@SuppressWarnings("OverlyCoupledMethod")
public class MoreThanSpec implements Spec {
	/**
	 * An instance of this spec.
	 *
	 * @since 0.3.0 ~2021.06.25
	 */
	@NotNull
	public static final MoreThanSpec INSTANCE = new MoreThanSpec();

	/**
	 * The kind of an more-than operator context.
	 *
	 * @since 0.3.0 ~2021.06.25
	 */
	@NotNull
	public static final String KIND = "operator:more_than";

	/**
	 * The qualified name of this spec.
	 *
	 * @since 0.3.0 ~2021.06.25
	 */
	@NotNull
	public static final String NAME = MoreThanSpec.class.getSimpleName();

	@NotNull
	@Override
	public Analyzer getAnalyzer() {
		return analyzer(
				//analyze the whole hierarchy
				a -> hierarchy(a),
				//target valid non processed trees
				a -> filter(a, and(
						//target close-chevron symbols
						is(CloseChevronSpec.KIND),
						//skip if already wrapped
						parent(not(MoreThanSpec.KIND))
				)),
				//analyze
				a -> binaryOperator(
						//context wrapper constructor
						(d, r) ->
								new Tree(
										d,
										r,
										new Sketch(MoreThanSpec.KIND),
										OperatorSpec.WEIGHT
								),
						//operator constructor
						(w, t) -> w.getSketch().set(
								OperatorSpec.KEY_SIGN,
								t.getSketch()
						),
						//left-side wrapper constructor
						(w, r) -> w.offer(new Tree(
								w.getDocument(),
								r,
								w.getSketch()
								 .get(OperatorSpec.KEY_LEFT)
								 .setKind(ParameterSpec.KIND),
								ParameterSpec.WEIGHT
						)),
						//right-side wrapper constructor
						(w, r) -> w.offer(new Tree(
								w.getDocument(),
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
		return compiler(
				//target more-than operator
				c -> filter(c, is(MoreThanSpec.KIND)),
				//compile
				c -> (compiler, compilation, tree) -> {
					Tree leftT = tree.getSketch().get(OperatorSpec.KEY_LEFT).getTree();
					Tree rightT = tree.getSketch().get(OperatorSpec.KEY_RIGHT).getTree();

					if (leftT == null || rightT == null)
						throw new CompileException(
								"Operator MORE_THAN (>) is missing some components",
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
								"The operator MORE_THAN (>) cannot be applied to <" +
								Source.read(leftT) +
								"> and <" +
								Source.read(rightT) +
								">",
								tree
						);

					return new Block(
							tree,
							leftI,
							rightI,
							//compare the values
							new Compare(tree),
							//push `1` to compare the comparison result
							new PushConst(new NumberValue(1)),
							//compare the comparison result with `1`
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
		return MoreThanSpec.NAME;
	}
}
