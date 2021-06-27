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
import org.jamplate.spec.syntax.term.DigitsSpec;
import org.jamplate.internal.util.Functions;
import org.jamplate.internal.util.IO;
import org.jamplate.internal.function.compiler.wrapper.FilterByKindCompiler;
import org.jetbrains.annotations.NotNull;

/**
 * Parameter number specification.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.20
 */
public class NumberSpec implements Spec {
	/**
	 * An instance of this spec.
	 *
	 * @since 0.3.0 ~2021.06.20
	 */
	@NotNull
	public static final NumberSpec INSTANCE = new NumberSpec();

	/**
	 * The qualified name of this spec.
	 *
	 * @since 0.3.0 ~2021.06.20
	 */
	@NotNull
	public static final String NAME = NumberSpec.class.getSimpleName();

	@NotNull
	@Override
	public Compiler getCompiler() {
		return Functions.compiler(
				//target digits sequences
				c -> new FilterByKindCompiler(DigitsSpec.KIND, c),
				//compile
				c -> (compiler, compilation, tree) -> {
					//read the tree
					String text = IO.read(tree).toString();

					try {
						//re-interpret the text (binary, octal, hex)
						String interpreted =
								//octal
								text.startsWith("0") && text.length() > 1 ?
								Long.toString(Long.parseLong(
										text.substring(1),
										8
								)) :
								//binary
								text.startsWith("0b") ?
								Long.toString(Long.parseLong(
										text.substring(2),
										2
								)) :
								//hexadecimal
								text.startsWith("0x") ?
								Long.toString(Long.parseLong(
										text.substring(2),
										16
								)) :
								//decimal
								text;

						//compile to PushConst
						return new PushConst(tree, m -> interpreted);
					} catch (NumberFormatException ignored) {
						//cannot interpret, skip ;P
						return null;
					}
				}
		);
	}

	@NotNull
	@Override
	public String getQualifiedName() {
		return NumberSpec.NAME;
	}
}
//		return new FilterByHierarchyKindCompiler(
//				ParameterSpec.KIND,
//				new FilterByKindCompiler(
//						NumberSpec.KIND,
//						ToPushConstCompiler.INSTANCE
//				)
//		);
