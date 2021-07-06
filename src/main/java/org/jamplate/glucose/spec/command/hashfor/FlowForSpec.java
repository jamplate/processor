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
package org.jamplate.glucose.spec.command.hashfor;

import org.jamplate.api.Spec;
import org.jamplate.function.Analyzer;
import org.jamplate.function.Compiler;
import org.jamplate.glucose.instruction.flow.Repeat;
import org.jamplate.glucose.instruction.memory.frame.DumpFrame;
import org.jamplate.glucose.instruction.memory.frame.PushFrame;
import org.jamplate.glucose.instruction.memory.frame.GlueFrame;
import org.jamplate.glucose.instruction.memory.heap.Set;
import org.jamplate.glucose.instruction.memory.resource.PushConst;
import org.jamplate.glucose.instruction.memory.stack.Dup;
import org.jamplate.glucose.instruction.memory.stack.Swap;
import org.jamplate.glucose.instruction.operator.cast.CastArray;
import org.jamplate.glucose.instruction.operator.logic.Defined;
import org.jamplate.glucose.instruction.operator.struct.Invert;
import org.jamplate.glucose.instruction.operator.struct.Split;
import org.jamplate.glucose.spec.element.CommandSpec;
import org.jamplate.glucose.spec.element.FlowSpec;
import org.jamplate.impl.instruction.Block;
import org.jamplate.memory.Value;
import org.jamplate.model.CompileException;
import org.jamplate.model.Instruction;
import org.jamplate.model.Sketch;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.NotNull;

import static org.jamplate.glucose.internal.util.Values.text;
import static org.jamplate.impl.analyzer.FilterAnalyzer.filter;
import static org.jamplate.impl.analyzer.HierarchyAnalyzer.hierarchy;
import static org.jamplate.impl.compiler.FilterCompiler.filter;
import static org.jamplate.internal.analyzer.BinaryFlowAnalyzer.binaryFlow;
import static org.jamplate.internal.util.Functions.analyzer;
import static org.jamplate.internal.util.Functions.compiler;
import static org.jamplate.internal.util.Query.*;
import static org.jamplate.internal.util.Source.read;

/**
 * For flow specification.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.20
 */
@SuppressWarnings({"OverlyCoupledMethod", "OverlyCoupledClass"})
public class FlowForSpec implements Spec {
	/**
	 * An instance of this spec.
	 *
	 * @since 0.3.0 ~2021.06.20
	 */
	@NotNull
	public static final FlowForSpec INSTANCE = new FlowForSpec();

	/**
	 * The kind of a for flow.
	 *
	 * @since 0.3.0 ~2021.06.15
	 */
	@NotNull
	public static final String KIND = "flow:for";

	/**
	 * The qualified name of this spec.
	 *
	 * @since 0.3.0 ~2021.06.19
	 */
	@NotNull
	public static final String NAME = FlowForSpec.class.getSimpleName();

	@NotNull
	@Override
	public Analyzer getAnalyzer() {
		return analyzer(
				//analyze the whole hierarchy
				a -> hierarchy(a),
				//target unprocessed valid trees
				a -> filter(a, and(
						//must not have been processed before
						not(FlowForSpec.KIND),
						//must contain any of
						child(or(
								//any #for
								is(HashForSpec.KIND),
								//any #endfor
								is(HashEndforSpec.KIND)
						))
				)),
				//analyze
				a -> binaryFlow(
						//start command predicate
						is(HashForSpec.KIND),
						//end command predicate
						is(HashEndforSpec.KIND),
						//flow wrapper constructor
						(d, r) -> new Tree(
								d,
								r,
								new Sketch(FlowForSpec.KIND),
								FlowSpec.WEIGHT
						),
						//start command constructor
						(w, t) -> w.getSketch().set(
								FlowSpec.KEY_START,
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
				//target for flow
				c -> filter(c, is(FlowForSpec.KIND)),
				//compile
				c -> (compiler, compilation, tree) -> {
					//gather component trees
					Tree startT = tree.getSketch().get(FlowSpec.KEY_START).getTree();
					Tree endT = tree.getSketch().get(FlowSpec.KEY_END).getTree();
					Tree bodyT = tree.getSketch().get(FlowSpec.KEY_BODY).getTree();

					//check required component trees
					if (startT == null || endT == null || bodyT == null)
						throw new CompileException(
								"Context FOR is missing some components",
								tree
						);

					//gather subcomponent trees
					Tree keyT = startT.getSketch().get(CommandSpec.KEY_KEY).getTree();
					Tree valueT = startT.getSketch().get(CommandSpec.KEY_VALUE).getTree();

					//check required subcomponent trees
					if (keyT == null || valueT == null)
						throw new CompileException(
								"For command is missing some components",
								startT
						);

					Instruction keyI = new PushConst(
							keyT,
							text(read(keyT))
					);
					//compile the value
					Instruction valueI = compiler.compile(
							compiler,
							compilation,
							valueT
					);
					//compile the body
					Instruction bodyI = compiler.compile(
							compiler,
							compilation,
							bodyT
					);

					//validate the compiled value
					if (valueI == null)
						//the value must be compiled
						throw new CompileException(
								"Unrecognized token",
								valueT
						);
					//validate the compiled body
					if (bodyI == null)
						//the body must be compiled
						throw new CompileException(
								"Unrecognized token",
								bodyT
						);

					//compile as a foreach REPEAT
					return new Block(
							tree,
							//push an anchoring null
							new PushConst(tree, Value.NULL),
							//value sandbox
							new Block(
									tree,
									//push a new frame
									new PushFrame(valueT),
									//run the value
									valueI,
									//glue the answer
									new GlueFrame(tree),
									//cast the answer into array
									new CastArray(tree),
									//reverse the array
									new Invert(tree),
									//dump the frame
									new DumpFrame(tree)
							),
							//spread the evaluated value
							new Split(tree),
							//duplicate the value
							new Dup(tree),
							//check if anchor
							new Defined(tree),
							//iterate until the anchoring null
							new Repeat(tree, new Block(
									tree,
									//push the allocation address
									keyI,
									//swap the address with the value
									new Swap(tree),
									//allocate the loop variable, at top frame
									new Set(tree),
									//execute the body
									bodyI,
									//duplicate the value
									new Dup(tree),
									//check if null
									new Defined(tree)
							)),
							//push the allocation address (to deallocate it from the top frame)
							keyI,
							//swap the anchoring null with the allocation address
							new Swap(tree),
							//allocate the anchoring null to the allocation address, at top frame
							new Set(tree)
					);
				}
		);
	}

	@NotNull
	@Override
	public String getQualifiedName() {
		return FlowForSpec.NAME;
	}
}
