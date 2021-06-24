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

import org.jamplate.impl.instruction.PushConst;
import org.jamplate.internal.util.IO;
import org.jamplate.model.Compilation;
import org.jamplate.model.Instruction;
import org.jamplate.model.Tree;
import org.jamplate.function.Compiler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONTokener;

import java.util.Objects;

/**
 * A compiler that always compiles into a {@link PushConst} with the constant being the
 * result of reading the tree given to it.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.31
 */
@Deprecated
public class ToPushConstUnescapeCompiler implements Compiler {
	/**
	 * A global instance of this class.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	public static final ToPushConstUnescapeCompiler INSTANCE = new ToPushConstUnescapeCompiler();

	@Nullable
	@Override
	public Instruction compile(@NotNull Compiler compiler, @NotNull Compilation compilation, @NotNull Tree tree) {
		Objects.requireNonNull(compiler, "compiler");
		Objects.requireNonNull(compilation, "compilation");
		Objects.requireNonNull(tree, "tree");
		try {
			String text = new JSONTokener(IO.read(tree).toString())
					.nextValue()
					.toString();

			return new PushConst(tree, text);
		} catch (JSONException e) {
			return new PushConst(tree);
		}
	}
}
