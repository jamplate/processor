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
package org.jamplate.spec.element;

import cufy.util.Node;
import org.jamplate.api.Spec;
import org.jamplate.function.Compiler;
import org.jamplate.function.Parser;
import org.jamplate.instruction.flow.Block;
import org.jamplate.instruction.memory.console.Print;
import org.jamplate.instruction.memory.frame.DumpFrame;
import org.jamplate.instruction.memory.frame.JoinFrame;
import org.jamplate.instruction.memory.frame.PushFrame;
import org.jamplate.internal.function.compiler.filter.FilterByKindCompiler;
import org.jamplate.internal.function.parser.pattern.EnclosureParser;
import org.jamplate.internal.function.parser.router.HierarchyParser;
import org.jamplate.internal.util.Functions;
import org.jamplate.model.CompileException;
import org.jamplate.model.Instruction;
import org.jamplate.model.Sketch;
import org.jamplate.model.Tree;
import org.jamplate.spec.standard.AnchorSpec;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

/**
 * Injection {@code #{...}#} specification.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.25
 */
@SuppressWarnings("OverlyCoupledMethod")
public class InjectionSpec implements Spec {
	/**
	 * An instance of this spec.
	 *
	 * @since 0.3.0 ~2021.06.25
	 */
	@NotNull
	public static final InjectionSpec INSTANCE = new InjectionSpec();

	/**
	 * The key to access the value of an {@link #KIND injection}.
	 *
	 * @since 0.3.0 ~2021.06.25
	 */
	@NotNull
	public static final Node.Key KEY_VALUE = Sketch.component("injection:value");

	/**
	 * The base kind of an injection.
	 *
	 * @since 0.3.0 ~2021.06.25
	 */
	@NotNull
	public static final String KIND = "injection";

	/**
	 * The qualified name of this spec.
	 *
	 * @since 0.3.0 ~2021.06.25
	 */
	@NotNull
	public static final String NAME = InjectionSpec.class.getSimpleName();

	@NotNull
	@Override
	public Compiler getCompiler() {
		return Functions.compiler(
				//target injections
				c -> new FilterByKindCompiler(InjectionSpec.KIND, c),
				//compile
				c -> (compiler, compilation, tree) -> {
					//gather component trees
					Tree valueT = tree.getSketch().get(InjectionSpec.KEY_VALUE).getTree();

					//check required component trees
					if (valueT == null)
						throw new CompileException(
								"Injection is missing some components",
								tree
						);

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

					//compile to print operation
					return new Block(
							tree,
							//push a new frame
							new PushFrame(tree),
							//run the value
							valueI,
							//glue the answer
							new JoinFrame(tree),
							//dump the frame
							new DumpFrame(tree),
							//print
							new Print(tree)
					);
				}
		);
	}

	@NotNull
	@Override
	public Parser getParser() {
		return Functions.parser(
				//search in the whole hierarchy
				HierarchyParser::new,
				//target injections
				p -> new EnclosureParser(
						Pattern.compile("#\\{"),
						Pattern.compile("\\}#"),
						//enclosure constructor
						(d, r) -> new Tree(
								d,
								r,
								new Sketch(InjectionSpec.KIND)
						),
						//open anchor constructor
						(t, r) -> t.offer(new Tree(
								t.getDocument(),
								r,
								t.getSketch()
								 .get(AnchorSpec.KEY_OPEN)
								 .setKind(AnchorSpec.KIND_OPEN)
						)),
						//close anchor constructor
						(t, r) -> t.offer(new Tree(
								t.getDocument(),
								r,
								t.getSketch()
								 .get(AnchorSpec.KEY_CLOSE)
								 .setKind(AnchorSpec.KIND_CLOSE)
						)),
						//value constructor
						(t, r) -> t.offer(new Tree(
								t.getDocument(),
								r,
								t.getSketch()
								 .get(InjectionSpec.KEY_VALUE)
								 .setKind(ParameterSpec.KIND),
								ParameterSpec.WEIGHT
						))
				)
		);
	}

	@NotNull
	@Override
	public String getQualifiedName() {
		return InjectionSpec.NAME;
	}
}
