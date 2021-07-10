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
package org.jamplate.glucose.spec.command.hashcapture;

import org.jamplate.unit.Spec;
import org.jamplate.function.Analyzer;
import org.jamplate.function.Compiler;
import org.jamplate.glucose.instruction.flow.ICapture;
import org.jamplate.glucose.instruction.memory.heap.IAlloc;
import org.jamplate.glucose.instruction.memory.resource.IPushConst;
import org.jamplate.glucose.spec.element.CommandSpec;
import org.jamplate.glucose.spec.element.FlowSpec;
import org.jamplate.impl.instruction.Block;
import org.jamplate.model.CompileException;
import org.jamplate.model.Instruction;
import org.jamplate.model.Sketch;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.NotNull;

import static org.jamplate.glucose.internal.util.Values.text;
import static org.jamplate.impl.analyzer.FilterAnalyzer.filter;
import static org.jamplate.impl.analyzer.HierarchyAnalyzer.hierarchy;
import static org.jamplate.impl.compiler.FilterCompiler.filter;
import static org.jamplate.glucose.internal.analyzer.BinaryFlowAnalyzer.flow;
import static org.jamplate.util.Functions.analyzer;
import static org.jamplate.util.Functions.compiler;
import static org.jamplate.util.Query.*;
import static org.jamplate.util.Source.read;

/**
 * Capture flow specification.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.07.03
 */
public class FlowCaptureSpec implements Spec {
	/**
	 * An instance of this spec.
	 *
	 * @since 0.3.0 ~2021.07.03
	 */
	@NotNull
	public static final FlowCaptureSpec INSTANCE = new FlowCaptureSpec();

	/**
	 * The kind of a capture flow.
	 *
	 * @since 0.3.0 ~2021.07.03
	 */
	@NotNull
	public static final String KIND = "flow:capture";

	/**
	 * The qualified name of this spec.
	 *
	 * @since 0.3.0 ~2021.07.03
	 */
	@NotNull
	public static final String NAME = FlowCaptureSpec.class.getSimpleName();

	@NotNull
	@Override
	public Analyzer getAnalyzer() {
		return analyzer(
				//analyze the whole hierarchy
				a -> hierarchy(a),
				//target unprocessed valid trees
				a -> filter(a, and(
						//must not have been processed before
						not(FlowCaptureSpec.KIND),
						//must contain any of
						child(or(
								//any #capture
								is(HashCaptureSpec.KIND),
								//any #endcapture
								is(HashEndcaptureSpec.KIND)
						))
				)),
				//analyze
				a -> flow(
						//start command predicate
						is(HashCaptureSpec.KIND),
						//end command predicate
						is(HashEndcaptureSpec.KIND),
						//flow wrapper constructor
						(d, r) -> new Tree(
								d,
								r,
								new Sketch(FlowCaptureSpec.KIND),
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
				//target capture flow
				c -> filter(c, is(FlowCaptureSpec.KIND)),
				//compile
				c -> (compiler, compilation, tree) -> {
					//gather component trees
					Tree startT = tree.getSketch().get(FlowSpec.KEY_START).getTree();
					Tree endT = tree.getSketch().get(FlowSpec.KEY_END).getTree();
					Tree bodyT = tree.getSketch().get(FlowSpec.KEY_BODY).getTree();

					//check required component trees
					if (startT == null || endT == null || bodyT == null)
						throw new CompileException(
								"Context CAPTURE is missing some components",
								tree
						);

					//gather subcomponent trees
					Tree keyT = startT.getSketch().get(CommandSpec.KEY_KEY).getTree();

					//check required subcomponent trees
					if (keyT == null)
						throw new CompileException(
								"Command CAPTURE is missing some components",
								startT
						);

					//compile the key
					Instruction keyI = new IPushConst(
							keyT,
							text(read(keyT))
					);
					//compile the body
					Instruction bodyI = compiler.compile(
							compiler,
							compilation,
							bodyT
					);

					//validate subcomponents
					if (bodyI == null)
						//the body must be compiled
						throw new CompileException(
								"Unrecognized token",
								bodyT
						);

					//compile
					return new Block(
							tree,
							//push the address to where to allocate the result
							keyI,
							//capture sandbox
							new ICapture(
									tree,
									bodyI
							),
							//allocate
							new IAlloc(tree)
					);
				}
		);
	}

	@NotNull
	@Override
	public String getQualifiedName() {
		return FlowCaptureSpec.NAME;
	}
}
