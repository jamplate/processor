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
package org.jamplate.glucose.spec.parameter.resource;

import org.jamplate.api.Spec;
import org.jamplate.function.Compiler;
import org.jamplate.glucose.instruction.memory.resource.PushConst;
import org.jamplate.glucose.spec.standard.AnchorSpec;
import org.jamplate.glucose.spec.syntax.enclosure.QuotesSpec;
import org.jamplate.glucose.value.TextValue;
import org.jamplate.internal.util.Source;
import org.jetbrains.annotations.NotNull;

import static org.jamplate.internal.util.Query.is;
import static org.jamplate.impl.function.compiler.FilterCompiler.filter;
import static org.jamplate.internal.function.compiler.FlattenCompiler.flatten;
import static org.jamplate.internal.util.Functions.compiler;

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
		return compiler(
				//target quotes
				c -> filter(c, is(QuotesSpec.KIND)),
				//flatten the parts
				c -> flatten(c),
				//target the body
				c -> filter(c, is(AnchorSpec.KIND_BODY)),
				//compile
				c -> (compiler, compilation, tree) ->
						new PushConst(
								tree,
								new TextValue(Source.read(tree))
						)
		);
	}

	@NotNull
	@Override
	public String getQualifiedName() {
		return EscapedStringSpec.NAME;
	}
}
