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
import org.jamplate.instruction.memory.stack.Dup;
import org.jamplate.instruction.memory.stack.Swap;
import org.jamplate.instruction.operator.cast.CastBoolean;
import org.jamplate.instruction.operator.logic.Compare;
import org.jamplate.instruction.operator.logic.Negate;
import org.jamplate.instruction.operator.logic.Or;
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
import org.jamplate.spec.syntax.symbol.OpenChevronEqualSpec;
import org.jamplate.value.NumberValue;
import org.jetbrains.annotations.NotNull;

/**
 * Less-Than-Equals operator specifications.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.25
 */
@SuppressWarnings({"OverlyCoupledClass", "OverlyCoupledMethod"})
public class LessThanEqualsSpec implements Spec {
	/**
	 * An instance of this spec.
	 *
	 * @since 0.3.0 ~2021.06.25
	 */
	@NotNull
	public static final LessThanEqualsSpec INSTANCE = new LessThanEqualsSpec();

	/**
	 * The kind of a less-than-equals operator context.
	 *
	 * @since 0.3.0 ~2021.06.25
	 */
	@NotNull
	public static final String KIND = "operator:less_than_equals";

	/**
	 * The qualified name of this spec.
	 *
	 * @since 0.3.0 ~2021.06.25
	 */
	@NotNull
	public static final String NAME = LessThanEqualsSpec.class.getSimpleName();

	@NotNull
	@Override
	public Analyzer getAnalyzer() {
		return Functions.analyzer(
				//analyze the whole hierarchy
				HierarchyAnalyzer::new,
				//filter only if not already wrapped
				a -> new FilterByNotParentKindAnalyzer(LessThanEqualsSpec.KIND, a),
				//target open-chevron-equal
				a -> new FilterByKindAnalyzer(OpenChevronEqualSpec.KIND, a),
				//wrap
				a -> new BinaryOperatorAnalyzer(
						//context wrapper constructor
						(d, r) -> new Tree(
								d,
								r,
								new Sketch(LessThanEqualsSpec.KIND),
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
				//target less-than-equals operator
				c -> new FilterByKindCompiler(LessThanEqualsSpec.KIND, c),
				//compile
				c -> (compiler, compilation, tree) -> {
					Tree leftT = tree.getSketch().get(OperatorSpec.KEY_LEFT).getTree();
					Tree rightT = tree.getSketch().get(OperatorSpec.KEY_RIGHT).getTree();

					if (leftT == null || rightT == null)
						throw new CompileException(
								"Operator LESS_THAN_EQUALS (<=) is missing some components",
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
								"The operator LESS_THAN_EQUALS (<=) cannot be applied to <" +
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
							//duplicate for the two checks
							new Dup(tree),
							//cast the first duplicate to boolean
							new CastBoolean(tree),
							//negate the first duplicate
							new Negate(tree),
							//swap the duplicates
							new Swap(tree),
							//push '-1' to compare with the duplicate
							new PushConst(tree, new NumberValue(-1)),
							//compare the second duplicate with `-1`
							new Compare(tree),
							//cast the second duplicate to boolean
							new CastBoolean(tree),
							//negate the second duplicate
							new Negate(tree),
							//less than or equals
							new Or(tree)
					);
				}
		);
	}

	@NotNull
	@Override
	public String getQualifiedName() {
		return LessThanEqualsSpec.NAME;
	}
}
