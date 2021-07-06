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
import org.jamplate.glucose.instruction.flow.IBranch;
import org.jamplate.glucose.instruction.memory.heap.IAccess;
import org.jamplate.glucose.instruction.memory.resource.IPushConst;
import org.jamplate.glucose.instruction.memory.stack.IDup;
import org.jamplate.glucose.instruction.memory.stack.IPop;
import org.jamplate.glucose.instruction.operator.logic.IDefined;
import org.jamplate.glucose.spec.syntax.term.WordSpec;
import org.jamplate.impl.instruction.Block;
import org.jamplate.impl.instruction.Idle;
import org.jetbrains.annotations.NotNull;

import static org.jamplate.glucose.internal.util.Values.text;
import static org.jamplate.impl.compiler.FilterCompiler.filter;
import static org.jamplate.internal.util.Functions.compiler;
import static org.jamplate.internal.util.Query.is;
import static org.jamplate.internal.util.Source.read;

/**
 * Parameter reference specification.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.20
 */
@SuppressWarnings("OverlyCoupledMethod")
public class ReferenceSpec implements Spec {
	/**
	 * An instance of this spec.
	 *
	 * @since 0.3.0 ~2021.06.20
	 */
	@NotNull
	public static final ReferenceSpec INSTANCE = new ReferenceSpec();

	/**
	 * The qualified name of this spec.
	 *
	 * @since 0.3.0 ~2021.06.20
	 */
	@NotNull
	public static final String NAME = ReferenceSpec.class.getSimpleName();

	@NotNull
	@Override
	public Compiler getCompiler() {
		return compiler(
				//target words
				c -> filter(c, is(WordSpec.KIND)),
				//compile
				c -> (compiler, compilation, tree) -> {
					//read the tree
					String text = read(tree).toString();

					//compile
					return new Block(
							tree,
							//push the address
							new IPushConst(tree, text(text)),
							//access
							new IAccess(tree),
							//duplicate the value to be null checked first
							new IDup(tree),
							//null check (true if not null)
							new IDefined(tree),
							//branch if not null
							new IBranch(
									tree,
									//the value is not null, no need to replace
									new Idle(tree),
									//value is null, replace
									new Block(
											tree,
											//pop the duplicate value (it is null)
											new IPop(tree),
											//push the name of the reference
											new IPushConst(tree, text(text))
									)
							)
					);
				}
		);
	}

	@NotNull
	@Override
	public String getQualifiedName() {
		return ReferenceSpec.NAME;
	}
}
