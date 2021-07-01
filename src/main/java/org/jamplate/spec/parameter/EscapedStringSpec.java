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
package org.jamplate.spec.parameter;

import org.jamplate.api.Spec;
import org.jamplate.function.Compiler;
import org.jamplate.instruction.memory.resource.PushConst;
import org.jamplate.internal.function.compiler.branch.FlattenCompiler;
import org.jamplate.internal.function.compiler.wrapper.FilterByKindCompiler;
import org.jamplate.internal.util.Functions;
import org.jamplate.internal.util.IO;
import org.jamplate.spec.standard.AnchorSpec;
import org.jamplate.spec.syntax.enclosure.QuotesSpec;
import org.jamplate.value.TextValue;
import org.jetbrains.annotations.NotNull;

/**
 * Parameter escaped string specification.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.20
 */
public class EscapedStringSpec implements Spec {
	/**
	 * An instance of this spec.
	 *
	 * @since 0.3.0 ~2021.06.20
	 */
	@NotNull
	public static final EscapedStringSpec INSTANCE = new EscapedStringSpec();

	/**
	 * The qualified name of this spec.
	 *
	 * @since 0.3.0 ~2021.06.20
	 */
	@NotNull
	public static final String NAME = EscapedStringSpec.class.getSimpleName();

	@NotNull
	@Override
	public Compiler getCompiler() {
		return Functions.compiler(
				//target quotes
				c -> new FilterByKindCompiler(QuotesSpec.KIND, c),
				//flatten the parts
				FlattenCompiler::new,
				//target the body
				c -> new FilterByKindCompiler(AnchorSpec.KIND_BODY, c),
				//compile
				c -> (compiler, compilation, tree) -> {
					//read the tree
					String text = IO.read(tree).toString();

					return new PushConst(tree, new TextValue(text));
				}
		);
	}

	@NotNull
	@Override
	public String getQualifiedName() {
		return EscapedStringSpec.NAME;
	}
}
