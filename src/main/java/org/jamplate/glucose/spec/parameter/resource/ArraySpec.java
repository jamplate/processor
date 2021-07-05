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
package org.jamplate.glucose.spec.parameter.resource;

import org.jamplate.api.Spec;
import org.jamplate.function.Analyzer;
import org.jamplate.function.Compiler;
import org.jamplate.impl.instruction.Block;
import org.jamplate.impl.instruction.Idle;
import org.jamplate.glucose.instruction.memory.frame.DumpFrame;
import org.jamplate.glucose.instruction.memory.frame.JoinFrame;
import org.jamplate.glucose.instruction.memory.frame.PushFrame;
import org.jamplate.glucose.instruction.memory.resource.PushConst;
import org.jamplate.glucose.instruction.operator.cast.CastArray;
import org.jamplate.glucose.instruction.operator.cast.CastQuote;
import org.jamplate.glucose.spec.standard.AnchorSpec;
import org.jamplate.glucose.spec.syntax.enclosure.BracketsSpec;
import org.jamplate.glucose.spec.syntax.symbol.CommaSpec;
import org.jamplate.glucose.value.GluedValue;
import org.jamplate.internal.util.Source;
import org.jamplate.model.Sketch;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.NotNull;

import static org.jamplate.internal.util.Query.*;
import static org.jamplate.impl.analyzer.FilterAnalyzer.filter;
import static org.jamplate.internal.analyzer.SeparatorsAnalyzer.separators;
import static org.jamplate.impl.analyzer.ChildrenAnalyzer.children;
import static org.jamplate.impl.analyzer.HierarchyAnalyzer.hierarchy;
import static org.jamplate.impl.compiler.FilterCompiler.filter;
import static org.jamplate.impl.compiler.FirstCompileCompiler.first;
import static org.jamplate.impl.compiler.FallbackCompiler.fallback;
import static org.jamplate.internal.compiler.FlattenCompiler.flatten;
import static org.jamplate.internal.util.Functions.analyzer;
import static org.jamplate.internal.util.Functions.compiler;

/**
 * Parameter array specification.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.20
 */
@SuppressWarnings("OverlyCoupledMethod")
public class ArraySpec implements Spec {
	/**
	 * An instance of this spec.
	 *
	 * @since 0.3.0 ~2021.06.20
	 */
	@NotNull
	public static final ArraySpec INSTANCE = new ArraySpec();

	/**
	 * The qualified name of this spec.
	 *
	 * @since 0.3.0 ~2021.06.20
	 */
	@NotNull
	public static final String NAME = ArraySpec.class.getSimpleName();

	@NotNull
	@Override
	public Analyzer getAnalyzer() {
		return analyzer(
				//search in the whole hierarchy
				a -> hierarchy(a),
				//target brackets
				a -> filter(a, is(BracketsSpec.KIND)),
				//foreach child
				a -> children(a),
				//target the body
				a -> filter(a, and(
						//target the body
						is(AnchorSpec.KIND_BODY),
						//skip if already separated
						child(is(AnchorSpec.KIND_SLOT)).negate()
				)),
				//separate
				a -> separators(
						//separator predicate
						is(CommaSpec.KIND),
						//constructor
						(t, r) -> t.offer(new Tree(
								t.getDocument(),
								r,
								new Sketch(AnchorSpec.KIND_SLOT),
								AnchorSpec.WEIGHT_SLOT
						))
				)
		);
	}

	@NotNull
	@Override
	public Compiler getCompiler() {
		return compiler(
				//target brackets
				c -> filter(c, is(BracketsSpec.KIND)),
				//compile the whole context
				c -> (compiler, compilation, tree) ->
						new Block(
								tree,
								//push a frame to encapsulate the content of the array
								new PushFrame(tree),
								//execute inner parts
								c.compile(compiler, compilation, tree),
								//join the execution result
								new JoinFrame(tree),
								//reformat the array
								new CastArray(tree),
								//dump the frame
								new DumpFrame(tree)
						),
				//flatten parts
				c -> flatten(c),
				//compile anchors, body, commas, slots, else
				c -> first(
						//compile opening anchors to PushConst
						compiler(
								cc -> filter(cc, or(
										is(AnchorSpec.KIND_OPEN),
										is(AnchorSpec.KIND_CLOSE)
								)),
								cc -> (compiler, compilation, tree) ->
										new PushConst(
												tree,
												new GluedValue(Source.read(tree))
										)
						),
						c
				),
				//target body
				c -> filter(c, is(AnchorSpec.KIND_BODY)),
				//flatten body parts
				c -> flatten(c),
				//compile commas, slots, else
				c -> first(
						//compile commas
						compiler(
								cc -> filter(cc, is(CommaSpec.KIND)),
								cc -> (compiler, compilation, tree) ->
										new PushConst(
												tree,
												new GluedValue(Source.read(tree))
										)
						),
						//compile the slots
						compiler(
								//target the slots
								cc -> filter(cc, is(AnchorSpec.KIND_SLOT)),
								//flatten slots parts
								cc -> flatten(cc),
								//compile each slot part
								cc -> first(
										//ignore whitespaces
										compiler(
												ccc -> filter(ccc, whitespace()),
												ccc -> (compiler, compilation, tree) ->
														new Idle(tree)
										),
										compiler(
												//compile quoted
												ccc -> (compiler, compilation, tree) ->
														new Block(
																//use the fallback
																ccc.compile(
																		compiler,
																		compilation,
																		tree
																),
																//quote the slot
																new CastQuote(tree)
														),
												ccc -> fallback()
										)
								)
						),
						//compile others using the fallback compiler
						fallback()
				)
		);
	}

	@NotNull
	@Override
	public String getQualifiedName() {
		return ArraySpec.NAME;
	}
}
