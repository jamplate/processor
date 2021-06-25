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
package org.jamplate.internal.spec.parameter;

import org.jamplate.api.Spec;
import org.jamplate.function.Compiler;
import org.jamplate.internal.spec.standard.AnchorSpec;
import org.jamplate.internal.spec.syntax.enclosure.ParenthesesSpec;
import org.jamplate.internal.util.Functions;
import org.jamplate.internal.util.compiler.branch.FlattenCompiler;
import org.jamplate.internal.util.compiler.concrete.ToIdleCompiler;
import org.jamplate.internal.util.compiler.group.FirstCompileCompiler;
import org.jamplate.internal.util.compiler.router.FallbackCompiler;
import org.jamplate.internal.util.compiler.wrapper.FilterByKindCompiler;
import org.jamplate.internal.util.compiler.wrapper.FilterWhitespaceCompiler;
import org.jamplate.internal.util.compiler.wrapper.MandatoryCompiler;
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
				//flatten non-parsed trees. but first, try to compile with other compilers
				c -> new FlattenCompiler(FallbackCompiler.INSTANCE, c),
				//when the condition is met, compile is mandatory
				MandatoryCompiler::new,
				//compile if not compiled by other compilers
				c -> new FirstCompileCompiler(
						//if not compiled, compile opening anchors to Idle
						new FilterByKindCompiler(AnchorSpec.KIND_OPEN, ToIdleCompiler.INSTANCE),
						//if not compiled, compile closing anchors to Idle
						new FilterByKindCompiler(AnchorSpec.KIND_CLOSE, ToIdleCompiler.INSTANCE),
						//if not compiled, compile whitespace to Idle
						new FilterWhitespaceCompiler(ToIdleCompiler.INSTANCE)
				)
		);
	}

	@NotNull
	@Override
	public String getQualifiedName() {
		return GroupSpec.NAME;
	}
}
//		return new FilterByHierarchyKindCompiler(
//				ParameterSpec.KIND,
//				new FilterByKindCompiler(
//						ParenthesesSpec.KIND,
//						new FlattenCompiler(
//								FallbackCompiler.INSTANCE,
//								new MandatoryCompiler(new FirstCompileCompiler(
//										new FilterByKindCompiler(AnchorSpec.KIND_OPEN, ToIdleCompiler.INSTANCE),
//										new FilterByKindCompiler(AnchorSpec.KIND_CLOSE, ToIdleCompiler.INSTANCE),
//										new FilterWhitespaceCompiler(ToIdleCompiler.INSTANCE)
//								))
//						)
//				)
//		);
