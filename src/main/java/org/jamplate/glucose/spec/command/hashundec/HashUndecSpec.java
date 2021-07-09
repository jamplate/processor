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
package org.jamplate.glucose.spec.command.hashundec;

import org.jamplate.api.Spec;
import org.jamplate.function.Compiler;
import org.jamplate.function.Parser;
import org.jamplate.glucose.instruction.memory.heap.IAlloc;
import org.jamplate.glucose.instruction.memory.resource.IPushConst;
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
import static org.jamplate.internal.parser.GroupParser.group;
import static org.jamplate.internal.util.Functions.compiler;
import static org.jamplate.internal.util.Functions.parser;
import static org.jamplate.internal.util.Query.is;
import static org.jamplate.internal.util.Source.read;

/**
 * The specification of the command {@code #undec}.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.07.09
 */
public class HashUndecSpec implements Spec {
	/**
	 * An instance of this spec.
	 *
	 * @since 0.3.0 ~2021.07.09
	 */
	@NotNull
	public static final HashUndecSpec INSTANCE = new HashUndecSpec();

	/**
	 * The kind of the {@code #undec} command.
	 *
	 * @since 0.3.0 ~2021.07.09
	 */
	@NotNull
	public static final String KIND = "command:undec";

	/**
	 * The qualified name of this spec.
	 *
	 * @since 0.3.0 ~2021.07.09
	 */
	@NotNull
	public static final String NAME = HashUndecSpec.class.getSimpleName();

	@NotNull
	@Override
	public Compiler getCompiler() {
		return compiler(
				//target undec commands
				c -> filter(c, is(HashUndecSpec.KIND)),
				//compiler
				c -> (compiler, compilation, tree) -> {
					//gather component trees
					Tree keyT = tree.getSketch().get(CommandSpec.KEY_KEY).getTree();

					if (keyT == null)
						throw new CompileException(
								"Command UNDEC is missing some components",
								tree
						);

					//compile the key
					Instruction keyI = new IPushConst(
							keyT,
							text(read(keyT))
					);

					return new Block(
							tree,
							//push the address
							keyI,
							//push a null
							new IPushConst(tree, Value.NULL),
							//allocate
							new IAlloc(tree)
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
						"[\t ]*(#)((?i)undec) (\\S+)\\s*",
						//constructor (whole match)
						(d, r) -> new Tree(
								d,
								r,
								new Sketch(HashUndecSpec.KIND),
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
		return HashUndecSpec.NAME;
	}
}
