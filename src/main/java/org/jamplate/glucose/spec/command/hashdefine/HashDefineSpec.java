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
package org.jamplate.glucose.spec.command.hashdefine;

import org.jamplate.glucose.internal.memory.Address;
import org.jamplate.unit.Spec;
import org.jamplate.function.Compiler;
import org.jamplate.function.Parser;
import org.jamplate.glucose.instruction.memory.frame.IDumpFrame;
import org.jamplate.glucose.instruction.memory.frame.IGlueFrame;
import org.jamplate.glucose.instruction.memory.frame.IPushFrame;
import org.jamplate.glucose.instruction.memory.heap.IAccess;
import org.jamplate.glucose.instruction.memory.heap.IAlloc;
import org.jamplate.glucose.instruction.memory.resource.IPushConst;
import org.jamplate.glucose.instruction.memory.stack.*;
import org.jamplate.glucose.instruction.operator.struct.IPut;
import org.jamplate.glucose.spec.element.CommandSpec;
import org.jamplate.glucose.spec.element.ParameterSpec;
import org.jamplate.glucose.spec.standard.AnchorSpec;
import org.jamplate.impl.instruction.Block;
import org.jamplate.model.CompileException;
import org.jamplate.model.Instruction;
import org.jamplate.model.Sketch;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.NotNull;

import static org.jamplate.glucose.internal.util.Values.text;
import static org.jamplate.impl.compiler.FilterCompiler.filter;
import static org.jamplate.glucose.internal.parser.GroupParser.group;
import static org.jamplate.util.Functions.compiler;
import static org.jamplate.util.Functions.parser;
import static org.jamplate.util.Query.is;
import static org.jamplate.util.Source.read;

/**
 * The specification of the command {@code #define}.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.07.10
 */
@SuppressWarnings({"OverlyCoupledMethod", "OverlyCoupledClass"})
public class HashDefineSpec implements Spec {
	/**
	 * An instance of this spec.
	 *
	 * @since 0.3.0 ~2021.07.10
	 */
	@NotNull
	public static final HashDefineSpec INSTANCE = new HashDefineSpec();

	/**
	 * The kind of the {@code #define} command.
	 *
	 * @since 0.3.0 ~2021.07.10
	 */
	@NotNull
	public static final String KIND = "command:define";

	/**
	 * The qualified name of this spec.
	 *
	 * @since 0.3.0 ~2021.07.10
	 */
	@NotNull
	public static final String NAME = HashDefineSpec.class.getSimpleName();

	@NotNull
	@Override
	public Compiler getCompiler() {
		return compiler(
				//target define commands that has not been compiled by a context
				c -> filter(c, is(HashDefineSpec.KIND)),
				//compile
				c -> (compiler, compilation, tree) -> {
					//gather component trees
					Tree keyT = tree.getSketch().get(CommandSpec.KEY_KEY).getTree();
					Tree valueT = tree.getSketch().get(CommandSpec.KEY_VALUE).getTree();

					if (keyT == null || valueT == null)
						throw new CompileException(
								"Command DEFINE is missing some components",
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

					//validate the compiled value
					if (valueI == null)
						//the value must be compiled
						throw new CompileException(
								"Unrecognized token",
								valueT
						);

					//compile
					return new Block(
							tree,
							//push the address
							keyI, /*[a]*/
							//duplicate the address (for IAlloc and IPut)
							new IDup(tree), /*[a,a]*/
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
							//duplicate the value (for IAlloc and IPut)
							new IDup(tree), /*[a,a,v,v]*/
							//swap 3
							new ISwap3(tree), /*[a,v,v,a]*/
							//swap 2
							new ISwap(tree), /*[a,v,a,v]*/
							//allocate
							new IAlloc(tree), /*[a,v]*/
							//push the address __DEFINE__
							new IPushConst(tree, text(Address.DEFINE)), /*[a,v,d]*/
							//duplicate the address __DEFINE__
							new IDup(tree), /*[a,v,d,d]*/
							//get object at __DEFINE__
							new IAccess(tree), /*[a,v,d,o]*/
							//swap 2
							new ISwap(tree), /*[a,v,o,d]*/
							//swap 4
							new ISwap4(tree), /*[d,o,v,a]*/
							//swap 2
							new ISwap(tree), /*[d,o,a,v]*/
							//put
							new IPut(tree), /*[d,o]*/
							//allocate back
							new IAlloc(tree) /*[]*/
					);
				}
		);
	}

	@NotNull
	@Override
	public Parser getParser() {
		//parse at the root
		return parser(
				p -> group(
						//the pattern
						"[\t ]*(#)((?i)define) (\\S+) ?([\\S\\s]*)",
						//constructor (whole match)
						(d, r) -> new Tree(
								d,
								r,
								new Sketch(HashDefineSpec.KIND),
								CommandSpec.WEIGHT
						),
						//anchor constructor (1st group)
						(t, r) -> t.offer(new Tree(
								t.getDocument(),
								r,
								t.getSketch()
								 .get(AnchorSpec.KEY_OPEN)
								 .setKind(AnchorSpec.KIND_OPEN)
						)),
						//type constructor (2nd group)
						(t, r) -> t.offer(new Tree(
								t.getDocument(),
								r,
								t.getSketch()
								 .get(CommandSpec.KEY_TYPE)
						)),
						//key constructor (3rd group)
						(t, r) -> t.offer(new Tree(
								t.getDocument(),
								r,
								t.getSketch()
								 .get(CommandSpec.KEY_KEY)
						)),
						//value constructor (4th group)
						(t, r) -> t.offer(new Tree(
								t.getDocument(),
								r,
								t.getSketch()
								 .get(CommandSpec.KEY_VALUE)
								 .setKind(ParameterSpec.KIND),
								ParameterSpec.WEIGHT
						))
				)
		);
	}

	@NotNull
	@Override
	public String getQualifiedName() {
		return HashDefineSpec.NAME;
	}
}
