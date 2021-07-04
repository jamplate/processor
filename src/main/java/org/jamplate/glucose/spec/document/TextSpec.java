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
import org.jamplate.glucose.instruction.flow.Block;
import org.jamplate.glucose.instruction.memory.console.FPrint;
import org.jamplate.glucose.instruction.memory.heap.Access;
import org.jamplate.glucose.instruction.memory.resource.PushConst;
import org.jamplate.glucose.instruction.operator.cast.CastObject;
import org.jamplate.glucose.value.TextValue;
import org.jamplate.internal.util.Source;
import org.jetbrains.annotations.NotNull;

import static org.jamplate.impl.function.compiler.FallbackCompiler.fallback;
import static org.jamplate.internal.function.compiler.FlattenCompiler.flatten;
import static org.jamplate.internal.util.Functions.compiler;

/**
 * A document-wise spec that transform any unrecognized token into an {@link FPrint}.
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
				c -> flatten(
						fallback(),
						c
				),
				c -> (compiler, compilation, tree) -> {
					String text = Source.read(tree).toString();

					return new Block(
							new PushConst(tree, new TextValue(text)),
							new PushConst(tree, new TextValue("__DEFINE__")),
							new Access(tree),
							new CastObject(tree),
							new FPrint(tree)
					);
				}
		);
	}

	@NotNull
	@Override
	public String getQualifiedName() {
		return TextSpec.NAME;
	}
}
