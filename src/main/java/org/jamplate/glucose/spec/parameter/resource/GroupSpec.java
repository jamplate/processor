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
import org.jamplate.glucose.spec.standard.AnchorSpec;
import org.jamplate.glucose.spec.syntax.enclosure.ParenthesesSpec;
import org.jetbrains.annotations.NotNull;

import static org.jamplate.internal.util.Query.is;
import static org.jamplate.impl.function.compiler.FilterCompiler.filter;
import static org.jamplate.impl.function.compiler.FallbackCompiler.fallback;
import static org.jamplate.internal.function.compiler.FlattenCompiler.flatten;
import static org.jamplate.internal.util.Functions.compiler;

/**
 * Logical group specification.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.20
 */
public class GroupSpec implements Spec {
	/**
	 * An instance of this spec.
	 *
	 * @since 0.3.0 ~2021.06.20
	 */
	@NotNull
	public static final GroupSpec INSTANCE = new GroupSpec();

	/**
	 * The qualified name of this spec.
	 *
	 * @since 0.3.0 ~2021.06.20
	 */
	@NotNull
	public static final String NAME = GroupSpec.class.getSimpleName();

	@NotNull
	@Override
	public Compiler getCompiler() {
		return compiler(
				//target parentheses
				c -> filter(c, is(ParenthesesSpec.KIND)),
				//flatten parts
				c -> flatten(c),
				//compile target the body
				c -> filter(c, is(AnchorSpec.KIND_BODY)),
				//flatten the body
				c -> flatten(c),
				//compile the body parts using the fallback compiler
				c -> fallback()
		);
	}

	@NotNull
	@Override
	public String getQualifiedName() {
		return GroupSpec.NAME;
	}
}
