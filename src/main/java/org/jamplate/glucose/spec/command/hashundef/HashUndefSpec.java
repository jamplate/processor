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
package org.jamplate.glucose.spec.command.hashundef;

import org.jamplate.glucose.internal.memory.Address;
import org.jamplate.unit.Spec;
import org.jamplate.function.Compiler;
import org.jamplate.function.Parser;
import org.jamplate.glucose.instruction.memory.heap.IAccess;
import org.jamplate.glucose.instruction.memory.heap.IAlloc;
import org.jamplate.glucose.instruction.memory.resource.IPushConst;
import org.jamplate.glucose.instruction.memory.stack.IDup;
import org.jamplate.glucose.instruction.memory.stack.ISwap;
import org.jamplate.glucose.instruction.memory.stack.ISwap3;
import org.jamplate.glucose.instruction.operator.struct.IRemove;
import org.jamplate.glucose.spec.element.CommandSpec;
import org.jamplate.glucose.spec.standard.AnchorSpec;
import org.jamplate.impl.instruction.Block;
import org.jamplate.memory.Value;
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
 * The specification of the command {@code #undef}.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.07.10
 */
@SuppressWarnings("OverlyCoupledMethod")
public class HashUndefSpec implements Spec {
	/**
	 * An instance of this spec.
	 *
	 * @since 0.3.0 ~2021.07.10
	 */
	@NotNull
	public static final HashUndefSpec INSTANCE = new HashUndefSpec();

	/**
	 * The kind of the {@code #undef} command.
	 *
	 * @since 0.3.0 ~2021.07.10
	 */
	@NotNull
	public static final String KIND = "command:undef";

	/**
	 * The qualified name of this spec.
	 *
	 * @since 0.3.0 ~2021.07.10
	 */
	@NotNull
	public static final String NAME = HashUndefSpec.class.getSimpleName();

	@NotNull
	@Override
	public Compiler getCompiler() {
		return compiler(
				//target undef commands
				c -> filter(c, is(HashUndefSpec.KIND)),
				//compiler
				c -> (compiler, compilation, tree) -> {
					//gather component trees
					Tree keyT = tree.getSketch().get(CommandSpec.KEY_KEY).getTree();

					if (keyT == null)
						throw new CompileException(
								"Command UNDEF is missing some components",
								tree
						);

					//compile the key
					Instruction keyI = new IPushConst(
							keyT,
							text(read(keyT))
					);

					//compile
					return new Block(
							tree,
							//push the address
							keyI, /*[a]*/
							//duplicate the address  (for IAlloc and IPut)
							new IDup(tree), /*[a,a]*/
							//push a null
							new IPushConst(tree, Value.NULL), /*[a,a,null]*/
							//allocate
							new IAlloc(tree), /*[a]*/
							//push the address __DEFINE__
							new IPushConst(tree, text(Address.DEFINE)), /*[a,d]*/
							//duplicate the address __DEFINE__
							new IDup(tree), /*[a,d,d]*/
							//get object at __DEFINE__
							new IAccess(tree), /*[a,d,o]*/
							//swap 2
							new ISwap(tree), /*[a,o,d]*/
							//swap 3
							new ISwap3(tree), /*[d,o,a]*/
							//remove
							new IRemove(tree), /*[d,o]*/
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
						"[\t ]*(#)((?i)undef) (\\S+)\\s*",
						//constructor (whole match)
						(d, r) -> new Tree(
								d,
								r,
								new Sketch(HashUndefSpec.KIND),
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
						//value constructor (3rd group)
						(t, r) -> t.offer(new Tree(
								t.getDocument(),
								r,
								t.getSketch()
								 .get(CommandSpec.KEY_KEY)
						))
				)
		);
	}

	@NotNull
	@Override
	public String getQualifiedName() {
		return HashUndefSpec.NAME;
	}
}
