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
package org.jamplate.glucose.spec.command.hashconsole;

import org.jamplate.api.Spec;
import org.jamplate.function.Compiler;
import org.jamplate.function.Parser;
import org.jamplate.glucose.instruction.memory.console.ISetConsole;
import org.jamplate.glucose.instruction.memory.frame.IDumpFrame;
import org.jamplate.glucose.instruction.memory.frame.IGlueFrame;
import org.jamplate.glucose.instruction.memory.frame.IPushFrame;
import org.jamplate.glucose.spec.element.CommandSpec;
import org.jamplate.glucose.spec.element.ParameterSpec;
import org.jamplate.glucose.spec.standard.AnchorSpec;
import org.jamplate.impl.instruction.Block;
import org.jamplate.model.CompileException;
import org.jamplate.model.Instruction;
import org.jamplate.model.Sketch;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.NotNull;

import static org.jamplate.impl.compiler.FilterCompiler.filter;
import static org.jamplate.internal.parser.GroupParser.group;
import static org.jamplate.internal.util.Functions.compiler;
import static org.jamplate.internal.util.Functions.parser;
import static org.jamplate.internal.util.Query.is;

/**
 * The specification of the command {@code #console}.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.07.04
 */
public class HashConsoleSpec implements Spec {
	/**
	 * An instance of this spec.
	 *
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	public static final HashConsoleSpec INSTANCE = new HashConsoleSpec();

	/**
	 * The kind of the {@code #console} command.
	 *
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	public static final String KIND = "command:console";

	/**
	 * The qualified name of this spec.
	 *
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	public static final String NAME = HashConsoleSpec.class.getSimpleName();

	@NotNull
	@Override
	public Compiler getCompiler() {
		return compiler(
				//target console commands
				c -> filter(c, is(HashConsoleSpec.KIND)),
				//compile
				c -> (compiler, compilation, tree) -> {
					//gather component trees
					Tree valueT = tree.getSketch().get(CommandSpec.KEY_VALUE).getTree();

					//check required component trees
					if (valueT == null)
						throw new CompileException(
								"Command CONSOLE is missing some components",
								tree
						);

					//compile the value
					Instruction valueI = compiler.compile(
							compiler,
							compilation,
							valueT
					);

					//validate compiled value
					if (valueI == null)
						//the value must be compiled
						throw new CompileException(
								"Unrecognized token",
								valueT
						);

					//compile
					return new Block(
							tree,
							//value sandbox
							new Block(
									tree,
									//push a new frame
									new IPushFrame(tree),
									//run the value
									valueI,
									//glue the answer
									new IGlueFrame(tree),
									//dump the frame
									new IDumpFrame(tree)
							),
							//console
							new ISetConsole(tree)
					);
				}
		);
	}

	@NotNull
	@Override
	public Parser getParser() {
		//parse once
		return parser(
				p -> group(
						//the pattern
						"[\t ]*(#)((?i)console) ([\\S\\s]+)",
						//constructor (whole match)
						(d, r) -> new Tree(
								d,
								r,
								new Sketch(HashConsoleSpec.KIND),
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
		return HashConsoleSpec.NAME;
	}
}
