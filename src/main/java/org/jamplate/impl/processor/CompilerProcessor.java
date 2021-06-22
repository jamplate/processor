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
package org.jamplate.impl.processor;

import org.jamplate.model.Compilation;
import org.jamplate.model.Instruction;
import org.jamplate.model.Tree;
import org.jamplate.function.Compiler;
import org.jamplate.function.Processor;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * A processor that compiles the compilations given to it using a pre-specified compiler.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.23
 */
@Deprecated
public class CompilerProcessor implements Processor {
	/**
	 * The compiler used by this processor.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	@NotNull
	protected final Compiler compiler;

	/**
	 * Construct a new compiler processor that compiles using the given {@code compiler}.
	 *
	 * @param compiler the compiler to be used by the constructed processor.
	 * @throws NullPointerException if the given {@code compiler} is null.
	 * @since 0.2.0 ~2021.05.23
	 */
	public CompilerProcessor(@NotNull Compiler compiler) {
		Objects.requireNonNull(compiler, "compiler");
		this.compiler = compiler;
	}

	@Override
	public boolean process(@NotNull Compilation compilation) {
		Objects.requireNonNull(compilation, "compilation");
		if (compilation.getInstruction() != null)
			return false;

		Tree root = compilation.getRootTree();
		Instruction instruction = this.compiler.compile(this.compiler, compilation, root);

		if (instruction == null)
			return false;

		compilation.setInstruction(instruction);
		return true;
	}
}
