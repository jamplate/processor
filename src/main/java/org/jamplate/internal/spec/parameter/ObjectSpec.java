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
import org.jamplate.instruction.flow.Block;
import org.jamplate.instruction.memory.frame.DumpFrame;
import org.jamplate.instruction.memory.frame.JoinFrame;
import org.jamplate.instruction.memory.frame.PushFrame;
import org.jamplate.instruction.operator.cast.CastObject;
import org.jamplate.internal.function.compiler.branch.FlattenCompiler;
import org.jamplate.internal.function.compiler.concrete.ToPushConstCompiler;
import org.jamplate.internal.function.compiler.group.FirstCompileCompiler;
import org.jamplate.internal.function.compiler.router.FallbackCompiler;
import org.jamplate.internal.function.compiler.wrapper.FilterByKindCompiler;
import org.jamplate.internal.spec.standard.AnchorSpec;
import org.jamplate.internal.spec.syntax.enclosure.BracesSpec;
import org.jamplate.internal.spec.syntax.symbol.ColonSpec;
import org.jamplate.internal.spec.syntax.symbol.CommaSpec;
import org.jamplate.internal.util.Functions;
import org.jetbrains.annotations.NotNull;

/**
 * Parameter object specification.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.20
 */
public class ObjectSpec implements Spec {
	/**
	 * An instance of this spec.
	 *
	 * @since 0.3.0 ~2021.06.20
	 */
	@NotNull
	public static final ObjectSpec INSTANCE = new ObjectSpec();

	/**
	 * The qualified name of this spec.
	 *
	 * @since 0.3.0 ~2021.06.20
	 */
	@NotNull
	public static final String NAME = ObjectSpec.class.getSimpleName();

	@NotNull
	@Override
	public Compiler getCompiler() {
		return Functions.compiler(
				//target braces
				c -> new FilterByKindCompiler(BracesSpec.KIND, c),
				//compile the whole context
				c -> (compiler, compilation, tree) ->
						new Block(
								tree,
								//push a frame to encapsulate the content of the object
								new PushFrame(tree),
								//execute inner parts
								c.compile(compiler, compilation, tree),
								//join the execution results
								JoinFrame.INSTANCE,
								//reformat the object
								CastObject.INSTANCE,
								//dump the frame
								DumpFrame.INSTANCE
						),
				//flatten parts
				FlattenCompiler::new,
				//compile anchors, commas, colons and body
				c -> new FirstCompileCompiler(
						//compile opening anchors to PushConst
						new FilterByKindCompiler(AnchorSpec.KIND_OPEN, ToPushConstCompiler.INSTANCE),
						//compile closing anchors to PushConst
						new FilterByKindCompiler(AnchorSpec.KIND_CLOSE, ToPushConstCompiler.INSTANCE),
						//compile body
						Functions.compiler(
								//target body
								cc -> new FilterByKindCompiler(AnchorSpec.KIND_BODY, cc),
								//flatten body parts
								FlattenCompiler::new,
								//compile each part
								cc -> new FirstCompileCompiler(
										//compile commas (,) to PushConst
										new FilterByKindCompiler(CommaSpec.KIND, ToPushConstCompiler.INSTANCE),
										//compile colons (:) to PushConst
										new FilterByKindCompiler(ColonSpec.KIND, ToPushConstCompiler.INSTANCE),
										//compile others using the fallback compiler
										FallbackCompiler.INSTANCE
								)
						)
				)
		);
	}

	@NotNull
	@Override
	public String getQualifiedName() {
		return ObjectSpec.NAME;
	}
}
//		return new FilterByHierarchyKindCompiler(
//				ParameterSpec.KIND,
//				new FilterByKindCompiler(
//						BracesSpec.KIND,
//						new FlattenCompiler(
//								FallbackCompiler.INSTANCE,
//								new MandatoryCompiler(new FirstCompileCompiler(
//										new FilterByKindCompiler(AnchorSpec.KIND_OPEN, ToPushConstCompiler.INSTANCE),
//										new FilterByKindCompiler(AnchorSpec.KIND_CLOSE, ToPushConstCompiler.INSTANCE),
//										new FilterByKindCompiler(CommandSpec.KIND, ToPushConstCompiler.INSTANCE),
//										new FilterByKindCompiler(ColonSpec.KIND, ToPushConstCompiler.INSTANCE),
//										new FilterWhitespaceCompiler(ToIdleCompiler.INSTANCE)
//								))
//						)
//				)
//		);

//		return Functions.compiler(
//				//target braces
//				c -> new FilterByKindCompiler(BracesSpec.KIND, c),
//				//flatten non-parsed trees. but first, try to compile with other compilers
//				c -> new FlattenCompiler(FallbackCompiler.INSTANCE, c),
//				//when the condition is met, compile is mandatory
//				MandatoryCompiler::new,
//				//compile if not compiled by other compilers
//				c -> new FirstCompileCompiler(
//						//if not compiled, compile opening anchors to PushConst
//						new FilterByKindCompiler(AnchorSpec.KIND_OPEN, ToPushConstCompiler.INSTANCE),
//						//if not compiled, compile closing anchors to PushConst
//						new FilterByKindCompiler(AnchorSpec.KIND_CLOSE, ToPushConstCompiler.INSTANCE),
//						//if not compiled, compile commas (,) to PushConst
//						new FilterByKindCompiler(CommaSpec.KIND, ToPushConstCompiler.INSTANCE),
//						//if not compiled, compile colons (:) to PushConst
//						new FilterByKindCompiler(ColonSpec.KIND, ToPushConstCompiler.INSTANCE),
//						//if not compiled, compile whitespaces to Idle
//						new FilterWhitespaceCompiler(ToIdleCompiler.INSTANCE)
//				)
//		);
