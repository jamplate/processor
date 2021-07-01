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
import org.jamplate.internal.function.compiler.router.FlattenCompiler;
import org.jamplate.internal.function.compiler.concrete.ToIdleCompiler;
import org.jamplate.internal.function.compiler.group.FirstCompileCompiler;
import org.jamplate.internal.function.compiler.router.FallbackCompiler;
import org.jamplate.internal.function.compiler.filter.FilterByKindCompiler;
import org.jamplate.internal.util.Functions;
import org.jamplate.spec.standard.AnchorSpec;
import org.jamplate.spec.syntax.enclosure.ParenthesesSpec;
import org.jetbrains.annotations.NotNull;

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
		return Functions.compiler(
				//target parentheses
				c -> new FilterByKindCompiler(ParenthesesSpec.KIND, c),
				//flatten parts
				FlattenCompiler::new,
				//compile anchors and body
				c -> new FirstCompileCompiler(
						//compile opening anchors to Idle
						new FilterByKindCompiler(AnchorSpec.KIND_OPEN, ToIdleCompiler.INSTANCE),
						//compile closing anchors to Idle
						new FilterByKindCompiler(AnchorSpec.KIND_CLOSE, ToIdleCompiler.INSTANCE),
						//compile body
						c
				),
				//target body
				c -> new FilterByKindCompiler(AnchorSpec.KIND_BODY, c),
				//flatten body parts
				FlattenCompiler::new,
				//compile each part using the fallback compiler
				c -> FallbackCompiler.INSTANCE
		);
	}

	@NotNull
	@Override
	public String getQualifiedName() {
		return GroupSpec.NAME;
	}
}
