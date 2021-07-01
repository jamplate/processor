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
package org.jamplate.spec.document;

import org.jamplate.api.Spec;
import org.jamplate.function.Compiler;
import org.jamplate.function.Parser;
import org.jamplate.instruction.flow.Block;
import org.jamplate.instruction.memory.console.Print;
import org.jamplate.instruction.memory.heap.Alloc;
import org.jamplate.instruction.memory.resource.PushConst;
import org.jamplate.internal.function.compiler.wrapper.FilterByKindCompiler;
import org.jamplate.internal.function.parser.pattern.TermParser;
import org.jamplate.internal.util.Functions;
import org.jamplate.internal.util.IO;
import org.jamplate.model.Sketch;
import org.jamplate.model.Tree;
import org.jamplate.value.NumberValue;
import org.jamplate.value.TextValue;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

/**
 * Line separators ({@code \n}, {@code \r}, {@code \r\n}) spec.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.24
 */
public class NewlineSpec implements Spec {
	/**
	 * An instance of this spec.
	 *
	 * @since 0.3.0 ~2021.06.24
	 */
	@NotNull
	public static final NewlineSpec INSTANCE = new NewlineSpec();

	/**
	 * The kind of line separator trees.
	 *
	 * @since 0.3.0 ~2021.06.24
	 */
	@NotNull
	public static final String KIND = "newline";

	/**
	 * The qualified name of this spec.
	 *
	 * @since 0.3.0 ~2021.06.24
	 */
	@NotNull
	public static final String NAME = NewlineSpec.class.getSimpleName();

	@NotNull
	@Override
	public Compiler getCompiler() {
		return Functions.compiler(
				//target newlines
				c -> new FilterByKindCompiler(NewlineSpec.KIND, c),
				//compile the newlines
				c -> (compiler, compilation, tree) -> {
					//determine the line number of the next line
					int line = IO.line(tree) + 1;
					//read the tree
					String text = IO.read(tree).toString();

					return new Block(
							//Define __LINE__
							new PushConst(tree, new TextValue("__LINE__")),
							new PushConst(tree, new NumberValue(line)),
							new Alloc(tree),
							//print the newline text
							new PushConst(tree, new TextValue(text)),
							new Print(tree)
					);
				}
		);
	}

	@NotNull
	@Override
	public Parser getParser() {
		//parse only on the first round
		return new TermParser(
				Pattern.compile("(?<!\\\\)(?:\r\n|\r|\n)"),
				(d, r) -> new Tree(
						d,
						r,
						new Sketch(NewlineSpec.KIND)
				)
		);
	}

	@NotNull
	@Override
	public String getQualifiedName() {
		return NewlineSpec.NAME;
	}
}
