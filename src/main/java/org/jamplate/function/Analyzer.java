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
package org.jamplate.function;

import org.jamplate.model.Compilation;
import org.jamplate.model.CompileException;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A function that analyzes the trees given to it and modify them (if necessary) depending
 * on its analytic results.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.28
 */
@FunctionalInterface
public interface Analyzer {
	/**
	 * Analyze the given {@code tree} and its relative trees.
	 *
	 * @param compilation the compilation of the given {@code tree}.
	 * @param tree        the tree to be analyzed.
	 * @return true, if this analyzer modified anything in the given {@code tree} or its
	 * 		compilation.
	 * @throws NullPointerException if the given {@code compilation} or {@code tree} is
	 *                              null.
	 * @throws CompileException     if any illegal pattern detected.
	 * @since 0.2.0 ~2021.05.28
	 */
	@Contract(mutates = "param1,param2")
	boolean analyze(@NotNull Compilation compilation, @NotNull Tree tree);
}
