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
package org.jamplate.model.function;

import org.jamplate.model.Compilation;
import org.jamplate.model.CompileException;
import org.jamplate.model.Instruction;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOError;

/**
 * A compiler is a function that compiles {@link Tree}s into {@link Instruction}s
 * depending on the state of the trees and compilations provided to it.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.21
 */
@FunctionalInterface
public interface Compiler {
	/**
	 * Compile the given {@code tree} and the trees in it. If this compiler cannot If this
	 * compiler encountered a tree that it cannot compile, this compiler will pass it to
	 * the given fallback {@code compiler} with the given {@code compiler} as the fallback
	 * compiler.
	 * <br>
	 * Unless this compiler want to change the default compiler, this compiler will invoke
	 * the given {@code compiler} with itself as the default compiler.
	 *
	 * @param compiler    a compiler this compiler must use when dealing with other
	 *                    entities this compiler cannot compile.
	 * @param compilation the compilation the given {@code tree} was taken from.
	 * @param tree        the tree to parse any instruction in it.
	 * @return the compiled instruction. Or {@code null} if this compiler failed to
	 * 		compile the given {@code tree} into an instruction.
	 * @throws NullPointerException if the given {@code compiler} or {@code tree} or
	 *                              {@code executable} or {@code compiler} is null.
	 * @throws CompileException     if this compiler detected a wrong unforgivable pattern
	 *                              in the given {@code tree};
	 * @throws IOError              if any I/O error occurs.
	 * @since 0.2.0 ~2021.05.21
	 */
	@Nullable
	@Contract(pure = true)
	Instruction compile(@NotNull Compiler compiler, @NotNull Compilation compilation, @NotNull Tree tree);
}
