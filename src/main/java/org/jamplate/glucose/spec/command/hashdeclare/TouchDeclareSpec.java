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
package org.jamplate.glucose.spec.command.hashdeclare;

import org.jamplate.unit.Spec;
import org.jamplate.function.Analyzer;
import org.jamplate.function.Compiler;
import org.jamplate.glucose.instruction.memory.frame.IConcatFrame;
import org.jamplate.glucose.instruction.memory.frame.IDumpFrame;
import org.jamplate.glucose.instruction.memory.frame.IGlueFrame;
import org.jamplate.glucose.instruction.memory.frame.IPushFrame;
import org.jamplate.glucose.instruction.memory.heap.IAccess;
import org.jamplate.glucose.instruction.memory.heap.IAlloc;
import org.jamplate.glucose.instruction.memory.resource.IPushConst;
import org.jamplate.glucose.instruction.memory.stack.IDup;
import org.jamplate.glucose.instruction.memory.stack.IEval;
import org.jamplate.glucose.instruction.operator.struct.IReverse;
import org.jamplate.glucose.instruction.operator.struct.ITouch;
import org.jamplate.glucose.spec.element.CommandSpec;
import org.jamplate.glucose.spec.element.ParameterSpec;
import org.jamplate.glucose.spec.syntax.enclosure.BracketsSpec;
import org.jamplate.impl.instruction.Block;
import org.jamplate.model.CompileException;
import org.jamplate.model.Instruction;
import org.jamplate.model.Intersection;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.NotNull;

import static org.jamplate.glucose.internal.util.Values.text;
import static org.jamplate.impl.analyzer.FilterAnalyzer.filter;
import static org.jamplate.impl.analyzer.HierarchyAnalyzer.hierarchy;
import static org.jamplate.impl.compiler.FilterCompiler.filter;
import static org.jamplate.util.Functions.analyzer;
import static org.jamplate.util.Functions.compiler;
import static org.jamplate.util.Query.is;
import static org.jamplate.util.References.exclusiveInclusive;
import static org.jamplate.util.References.inclusive;
import static org.jamplate.util.Source.read;

/**
 * The specification of the command {@code #declare} that accepts nested key.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.07.03
 */
@SuppressWarnings({"OverlyCoupledMethod", "OverlyCoupledClass"})
public class TouchDeclareSpec implements Spec {
	/**
	 * An instance of this spec.
	 *
	 * @since 0.3.0 ~2021.07.03
	 */
	@NotNull
	public static final TouchDeclareSpec INSTANCE = new TouchDeclareSpec();

	/**
	 * The kind of the nested keyed' {@code #declare} command.
	 *
	 * @since 0.3.0 ~2021.07.03
	 */
	@NotNull
	public static final String KIND = "command:declare:touch";

	/**
	 * The qualified name of this spec.
	 *
	 * @since 0.3.0 ~2021.06.19
	 */
	@NotNull
	public static final String NAME = TouchDeclareSpec.class.getSimpleName();

	@NotNull
	@Override
	public Analyzer getAnalyzer() {
		return analyzer(
				//search in the whole hierarchy
				a -> hierarchy(a),
				//target declare commands
				c -> filter(c, is(HashDeclareSpec.KIND)),
				//analyze
				c -> (compilation, tree) -> {
					Tree accessorT = tree.getSketch().get(CommandSpec.KEY_ACCESSOR).getTree();
					Tree keyT = tree.getSketch().get(CommandSpec.KEY_KEY).getTree();
					Tree valueT = tree.getSketch().get(CommandSpec.KEY_VALUE).getTree();

					if (accessorT != null || keyT == null || valueT == null)
						//already processed or missing some components
						return false;

					Tree firstSqrT = valueT.getChild();
					Tree lastSqrT = firstSqrT;

					if (firstSqrT == null)
						//no chain, no processing
						return false;

					if (!Intersection.NEXT.test(keyT, firstSqrT))
						//chain lost at the beginning, no processing
						return false;

					while (true) {
						Tree nextSqrT = lastSqrT.getNext();

						if (nextSqrT == null)
							//reached the end, stop
							break;

						if (nextSqrT.getSketch().getKind().equals(BracketsSpec.KIND))
							if (Intersection.NEXT.test(lastSqrT, nextSqrT))
								//chain proceeded
								lastSqrT = nextSqrT;
							else
								//chain lost, lastSqrT is the last chained []
								break;
						else if (Intersection.AFTER.test(lastSqrT, nextSqrT))
							//chain ended, lastSqr is the last []
							break;
						else
							//chain interrupted, no processing
							return false;
					}

					//replace the kind
					tree.getSketch().setKind(TouchDeclareSpec.KIND);
					//remove the old param context
					valueT.pop();
					//add a new param context
					tree.offer(new Tree(
							tree.getDocument(),
							exclusiveInclusive(
									lastSqrT,
									valueT
							),
							tree.getSketch()
								.replace(CommandSpec.KEY_VALUE)
								.setKind(ParameterSpec.KIND),
							ParameterSpec.WEIGHT
					));
					//add the accessor (also a param context)
					tree.offer(new Tree(
							tree.getDocument(),
							inclusive(
									firstSqrT,
									lastSqrT
							),
							tree.getSketch()
								.get(CommandSpec.KEY_ACCESSOR)
								.setKind(ParameterSpec.KIND),
							ParameterSpec.WEIGHT
					));

					return true;
				}
		);
	}

	@NotNull
	@Override
	public Compiler getCompiler() {
		return compiler(
				c -> filter(c, is(TouchDeclareSpec.KIND)),
				c -> (compiler, compilation, tree) -> {
					//gather component trees
					Tree keyT = tree.getSketch().get(CommandSpec.KEY_KEY).getTree();
					Tree valueT = tree.getSketch().get(CommandSpec.KEY_VALUE).getTree();
					Tree accessorT = tree.getSketch().get(CommandSpec.KEY_ACCESSOR).getTree();

					//check required component trees
					if (keyT == null || valueT == null || accessorT == null)
						throw new CompileException(
								"Command DECLARE is missing some components",
								tree
						);

					//compile the key
					Instruction keyI = new IPushConst(
							keyT,
							text(read(keyT))
					);
					//compile the value
					Instruction valueI = compiler.compile(
							compiler,
							compilation,
							valueT
					);
					//compile the accessor
					Instruction accessorI = compiler.compile(
							compiler,
							compilation,
							accessorT
					);

					//validate the compiled value
					if (valueI == null)
						//the value must be compiled
						throw new CompileException(
								"Unrecognized token",
								valueT
						);
					//validate the compiled accessor
					if (accessorI == null)
						//the accessor must be compiled when provided
						throw new CompileException(
								"Unrecognized token",
								accessorT
						);

					//nested declare
					return new Block(
							tree,
							//push the address
							keyI,
							//duplicate the address (to access the current value)
							new IDup(tree),
							//get the current value at the address
							new IAccess(tree),
							//keys sandbox
							new Block(
									tree,
									//push a new frame for the keys list
									new IPushFrame(tree),
									//push the nesting keys
									accessorI,
									//glue the answer
									new IConcatFrame(tree),
									//reverse the answer
									new IReverse(tree),
									//dump frame
									new IDumpFrame(tree)
							),
							//value sandbox
							new Block(
									tree,
									//push a new frame for the value
									new IPushFrame(tree),
									//run the value
									valueI,
									//glue the answer
									new IGlueFrame(tree),
									//dump frame
									new IDumpFrame(tree),
									//evaluate
									new IEval(tree)
							),
							//nested put
							new ITouch(tree),
							//evaluate
							new IEval(tree),
							//allocate
							new IAlloc(tree)
					);
				}
		);
	}

	@NotNull
	@Override
	public String getQualifiedName() {
		return TouchDeclareSpec.NAME;
	}
}
