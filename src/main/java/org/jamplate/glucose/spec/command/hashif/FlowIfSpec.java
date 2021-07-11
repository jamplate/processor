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
package org.jamplate.glucose.spec.command.hashif;

import org.jamplate.unit.Spec;
import org.jamplate.function.Analyzer;
import org.jamplate.function.Compiler;
import org.jamplate.glucose.instruction.flow.IBranch;
import org.jamplate.glucose.instruction.memory.frame.IDumpFrame;
import org.jamplate.glucose.instruction.memory.frame.IGlueFrame;
import org.jamplate.glucose.instruction.memory.frame.IPushFrame;
import org.jamplate.glucose.instruction.memory.heap.IAccess;
import org.jamplate.glucose.instruction.memory.resource.IPushConst;
import org.jamplate.glucose.instruction.operator.cast.ICastBoolean;
import org.jamplate.glucose.instruction.operator.logic.IDefined;
import org.jamplate.glucose.instruction.operator.logic.INegate;
import org.jamplate.glucose.spec.element.CommandSpec;
import org.jamplate.glucose.spec.element.FlowSpec;
import org.jamplate.impl.instruction.Block;
import org.jamplate.impl.instruction.Idle;
import org.jamplate.model.CompileException;
import org.jamplate.model.Instruction;
import org.jamplate.model.Sketch;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.NotNull;

import static org.jamplate.glucose.internal.util.Values.bool;
import static org.jamplate.glucose.internal.util.Values.text;
import static org.jamplate.impl.analyzer.FilterAnalyzer.filter;
import static org.jamplate.impl.analyzer.HierarchyAnalyzer.hierarchy;
import static org.jamplate.impl.compiler.FilterCompiler.filter;
import static org.jamplate.glucose.internal.analyzer.MultiFlowAnalyzer.flow;
import static org.jamplate.util.Functions.analyzer;
import static org.jamplate.util.Functions.compiler;
import static org.jamplate.util.Query.*;
import static org.jamplate.util.Source.read;

/**
 * If flow specification.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.07.08
 */
@SuppressWarnings({"OverlyCoupledMethod", "OverlyCoupledClass", "OverlyLongMethod",
				   "OverlyLongLambda"
})
public class FlowIfSpec implements Spec {
	/**
	 * An instance of this spec.
	 *
	 * @since 0.3.0 ~2021.07.08
	 */
	@NotNull
	public static final FlowIfSpec INSTANCE = new FlowIfSpec();

	/**
	 * The kind of an if flow.
	 *
	 * @since 0.3.0 ~2021.07.08
	 */
	@NotNull
	public static final String KIND = "flow:if";

	/**
	 * The qualified name of this spec.
	 *
	 * @since 0.3.0 ~2021.07.08
	 */
	@NotNull
	public static final String NAME = FlowIfSpec.class.getSimpleName();

	@NotNull
	@Override
	public Analyzer getAnalyzer() {
		return analyzer(
				//analyzer the whole hierarchy
				a -> hierarchy(a),
				//target unprocessed valid trees
				a -> filter(a, and(
						//must not have bee processed before
						not(FlowIfSpec.KIND),
						child(or(
								//#if
								is(HashIfSpec.KIND),
								//#ifdef
								is(HashIfdefSpec.KIND),
								//#ifndef
								is(HashIfndefSpec.KIND),
								//#elif
								is(HashElifSpec.KIND),
								//#elifdef
								is(HashElifdefSpec.KIND),
								//#elifndef
								is(HashElifndefSpec.KIND),
								//#else
								is(HashElseSpec.KIND),
								//#endif
								is(HashEndifSpec.KIND)
						))
				)),
				//analyze
				a -> flow(
						//start command predicate
						or(
								//#if
								is(HashIfSpec.KIND),
								//#ifdef
								is(HashIfdefSpec.KIND),
								//#ifndef
								is(HashIfndefSpec.KIND)
						),
						//middle command predicate
						bi(
								//previous
								or(
										//#if #ifdef #ifndef
										nil(),
										//#elif
										is(HashElifSpec.KIND),
										//#elifdef
										is(HashElifdefSpec.KIND),
										//#elifndef
										is(HashElifndefSpec.KIND)
								),
								//it
								or(
										//#elif
										is(HashElifSpec.KIND),
										//#elifdef
										is(HashElifdefSpec.KIND),
										//#elifndef
										is(HashElifndefSpec.KIND),
										//#else
										is(HashElseSpec.KIND)
								)
						),
						//end command predicate
						is(
								//#endif
								HashEndifSpec.KIND
						),
						//flow wrapper constructor
						(d, r) -> new Tree(
								d,
								r,
								new Sketch(FlowIfSpec.KIND),
								FlowSpec.WEIGHT
						),
						(w, t) ->
								w.getSketch().set(
										FlowSpec.KEY_SUB,
										t.getSketch()
								),
						//start command constructor
						(w, t) -> w.getSketch().set(
								FlowSpec.KEY_START,
								t.getSketch()
						),
						//middle command constructor
						(w, t) -> w.getSketch().set(
								FlowSpec.KEY_MIDDLE,
								t.getSketch()
						),
						//end command constructor
						(w, t) -> w.getSketch().set(
								FlowSpec.KEY_END,
								t.getSketch()
						),
						//body wrapper constructor
						(w, r) -> w.offer(new Tree(
								w.getDocument(),
								r,
								w.getSketch()
								 .get(FlowSpec.KEY_BODY),
								FlowSpec.WEIGHT_BODY
						))
				)
		);
	}

	@NotNull
	@Override
	public Compiler getCompiler() {
		return compiler(
				//target if flow
				c -> filter(c, is(FlowIfSpec.KIND)),
				//compile
				c -> (compiler, compilation, tree) -> {
					//gather component trees
					Tree startT = tree.getSketch().get(FlowSpec.KEY_START).getTree();
					Tree subT = tree.getSketch().get(FlowSpec.KEY_SUB).getTree();
					Tree bodyT = tree.getSketch().get(FlowSpec.KEY_BODY).getTree();

					//check required component trees
					if (startT == null || bodyT == null)
						throw new CompileException(
								"Flow IF is missing some components",
								tree
						);

					//compile the value
					Instruction valueWrapI;
					switch (startT.getSketch().getKind()) {
						case HashIfSpec.KIND:
						case HashElifSpec.KIND: {
							Tree valueT = startT.getSketch().get(CommandSpec.KEY_VALUE).getTree();

							//check required subcomponent trees
							if (valueT == null)
								throw new CompileException(
										"Command IF/ELIF is missing some components",
										tree
								);

							//compile the value
							Instruction valueI = compiler.compile(
									compiler,
									compilation,
									valueT
							);

							//validate the compiled value
							if (valueI == null)
								//the value must be compiled
								throw new CompileException(
										"Unrecognized token",
										valueT
								);

							//value sandbox
							valueWrapI = new Block(
									tree,
									//push a new frame for the value
									new IPushFrame(tree),
									//run the value
									valueI,
									//glue the answer
									new IGlueFrame(tree),
									//cast the answer to boolean
									new ICastBoolean(tree),
									//dump the frame
									new IDumpFrame(tree)
							);
							break;
						}
						case HashIfdefSpec.KIND:
						case HashElifdefSpec.KIND: {
							Tree valueT = startT.getSketch().get(CommandSpec.KEY_VALUE).getTree();

							//check required subcomponent trees
							if (valueT == null)
								throw new CompileException(
										"Command IFDEF/ELIFDEF is missing some components",
										tree
								);

							//def sandbox
							valueWrapI = new Block(
									tree,
									//push the address
									new IPushConst(
											valueT,
											text(read(valueT))
									),
									//access the address
									new IAccess(tree),
									//check
									new IDefined(tree)
							);
							break;
						}
						case HashIfndefSpec.KIND:
						case HashElifndefSpec.KIND: {
							Tree valueT = startT.getSketch().get(CommandSpec.KEY_VALUE).getTree();

							//check required subcomponent trees
							if (valueT == null)
								throw new CompileException(
										"Command IFNDEF/ELIFNDEF is missing some components",
										tree
								);

							//ndef sandbox
							valueWrapI = new Block(
									tree,
									//push the address
									new IPushConst(
											valueT,
											text(read(valueT))
									),
									//access the address
									new IAccess(tree),
									//check
									new IDefined(tree),
									//negate
									new INegate(tree)
							);
							break;
						}
						case HashElseSpec.KIND: {
							//true
							valueWrapI = new IPushConst(
									tree,
									bool(true)
							);
							break;
						}
						default: {
							throw new CompileException(
									"Unrecognized IF component",
									startT
							);
						}
					}

					//compile the body
					Instruction bodyI = compiler.compile(
							compiler,
							compilation,
							bodyT
					);
					//compile subbranch (fallback branch)
					Instruction subI =
							subT == null ?
							new Idle(tree) :
							compiler.compile(
									compiler,
									compilation,
									subT
							);

					//validate the compiled body
					if (bodyI == null)
						//the body must be compiled
						throw new CompileException(
								"Unrecognized token",
								bodyT
						);
					//validate the compiled subbranch
					if (subI == null)
						//subbranch must be compiled
						throw new CompileException(
								"Unrecognized token",
								subT
						);

					//compile
					return new Block(
							tree,
							//execute the condition
							valueWrapI,
							//execute branch with the condition
							new IBranch(
									tree,
									//execute the branch's body
									bodyI,
									//execute the whole fallback
									subI
							)
					);
				}
		);
	}

	@NotNull
	@Override
	public String getQualifiedName() {
		return FlowIfSpec.NAME;
	}
}
