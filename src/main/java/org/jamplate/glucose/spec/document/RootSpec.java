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
package org.jamplate.glucose.spec.document;

import org.jamplate.api.Spec;
import org.jamplate.function.Compiler;
import org.jamplate.function.Initializer;
import org.jamplate.glucose.instruction.memory.heap.Alloc;
import org.jamplate.glucose.instruction.memory.resource.PushConst;
import org.jamplate.impl.instruction.Block;
import org.jamplate.impl.model.CompilationImpl;
import org.jamplate.model.Sketch;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.NotNull;

import java.io.File;

import static org.jamplate.glucose.internal.util.Values.number;
import static org.jamplate.glucose.internal.util.Values.text;
import static org.jamplate.impl.compiler.CombineCompiler.combine;
import static org.jamplate.impl.compiler.FallbackCompiler.fallback;
import static org.jamplate.impl.compiler.FilterCompiler.filter;
import static org.jamplate.internal.compiler.FlattenCompiler.flatten;
import static org.jamplate.internal.util.Functions.compiler;
import static org.jamplate.internal.util.Functions.initializer;
import static org.jamplate.internal.util.Query.is;
import static org.jamplate.internal.util.Source.line;

/**
 * A specification that targets root trees.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.19
 */
public class RootSpec implements Spec {
	/**
	 * An instance of this spec.
	 *
	 * @since 0.3.0 ~2021.06.19
	 */
	@NotNull
	public static final RootSpec INSTANCE = new RootSpec();

	/**
	 * The kind of the root tree.
	 *
	 * @since 0.3.0 ~2021.06.19
	 */
	@NotNull
	public static final String KIND = "root";

	/**
	 * The qualified name of this spec.
	 *
	 * @since 0.3.0 ~2021.06.19
	 */
	@NotNull
	public static final String NAME = RootSpec.class.getSimpleName();

	/**
	 * The weight of the root tree.
	 *
	 * @since 0.3.0 ~2021.07.02
	 */
	public static final int WEIGHT = -1;

	@NotNull
	@Override
	public Compiler getCompiler() {
		return compiler(
				//target the root
				c -> filter(c, is(RootSpec.KIND)),
				//combined compile
				c -> combine(
						//compile the root
						(compiler, compilation, tree) -> {
							//determine the line where the root was declared
							int line = line(tree);
							//determine the file where the root was declared
							String file = tree.getDocument().toString();
							//determine the directory where the root was declared
							String dir = new File(file).getParent();

							//document initiation block
							return new Block(
									tree,
									//Define __PATH__
									new PushConst(tree, text("__PATH__")),
									new PushConst(tree, text(file)),
									new Alloc(tree),
									//Define __DIR__
									new PushConst(tree, text("__DIR__")),
									new PushConst(tree, text((Object) dir)),
									new Alloc(tree),
									//Define __LINE__
									new PushConst(tree, text("__LINE__")),
									new PushConst(tree, number(line)),
									new Alloc(tree)
							);
						},
						//compile children (flattened) with the other compilers
						flatten(fallback())
				)
		);
	}

	@NotNull
	@Override
	public Initializer getInitializer() {
		return initializer(
				i -> (environment, document) ->
						new CompilationImpl(
								environment,
								new Tree(
										document,
										new Sketch(RootSpec.KIND),
										RootSpec.WEIGHT
								)
						)
		);
	}

	@NotNull
	@Override
	public String getQualifiedName() {
		return RootSpec.NAME;
	}
}
