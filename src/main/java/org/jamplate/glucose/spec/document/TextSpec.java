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
import org.jamplate.glucose.instruction.memory.console.IFPrint;
import org.jamplate.glucose.instruction.memory.heap.IAccess;
import org.jamplate.glucose.instruction.memory.resource.IPushConst;
import org.jamplate.glucose.instruction.operator.cast.ICastObject;
import org.jamplate.impl.instruction.Block;
import org.jetbrains.annotations.NotNull;

import static org.jamplate.glucose.internal.util.Values.text;
import static org.jamplate.impl.compiler.FallbackCompiler.fallback;
import static org.jamplate.internal.compiler.FlattenCompiler.flatten;
import static org.jamplate.internal.util.Functions.compiler;
import static org.jamplate.internal.util.Source.read;

/**
 * A document-wise spec that transform any unrecognized token into an {@link IFPrint}.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.27
 */
public class TextSpec implements Spec {
	/**
	 * An instance of this spec.
	 *
	 * @since 0.3.0 ~2021.06.27
	 */
	@NotNull
	public static final TextSpec INSTANCE = new TextSpec();

	/**
	 * The qualified name of this spec.
	 *
	 * @since 0.3.0 ~2021.06.27
	 */
	@NotNull
	public static final String NAME = TextSpec.class.getSimpleName();

	@NotNull
	@Override
	public Compiler getCompiler() {
		return compiler(
				//flatten the tree
				c -> flatten(
						//try to compile parsed trees
						fallback(),
						//fallback for unparsed trees and trees that failed to compile
						c
				),
				//compile non-compiled trees
				c -> (compiler, compilation, tree) ->
						new Block(
								tree,
								//push the text of the tree
								new IPushConst(tree, text(read(tree))),
								//push the address to the default replacements object
								new IPushConst(tree, text("__DEFINE__")),
								//access the default replacements object
								new IAccess(tree),
								//cast to object (if the user replaced it with non-object value)
								new ICastObject(tree),
								//fprint
								new IFPrint(tree)
						)
		);
	}

	@NotNull
	@Override
	public String getQualifiedName() {
		return TextSpec.NAME;
	}
}
