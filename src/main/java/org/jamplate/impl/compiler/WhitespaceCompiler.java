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
package org.jamplate.impl.compiler;

import org.jamplate.model.Compilation;
import org.jamplate.model.Instruction;
import org.jamplate.model.Tree;
import org.jamplate.model.function.Compiler;
import org.jamplate.impl.util.Trees;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * A compiler that compiles whitespaces.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.28
 */
public class WhitespaceCompiler implements Compiler {
	@Nullable
	@Override
	public Instruction compile(@NotNull Compiler compiler, @NotNull Compilation compilation, @NotNull Tree tree) {
		Objects.requireNonNull(compiler, "compiler");
		Objects.requireNonNull(compilation, "compilation");
		Objects.requireNonNull(tree, "tree");
		if (Trees.read(tree).toString().trim().isEmpty())
			return Instruction.empty(tree);

		return null;
	}
}