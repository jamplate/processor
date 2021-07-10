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

import org.jamplate.unit.Spec;
import org.jamplate.function.Compiler;
import org.jamplate.glucose.instruction.memory.resource.IPushConst;
import org.jamplate.glucose.spec.syntax.enclosure.DoubleQuotesSpec;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONTokener;

import static org.jamplate.glucose.internal.util.Values.text;
import static org.jamplate.impl.compiler.FilterCompiler.filter;
import static org.jamplate.util.Functions.compiler;
import static org.jamplate.util.Query.is;
import static org.jamplate.util.Source.read;

/**
 * Parameter string specification.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.20
 */
public class StringSpec implements Spec {
	/**
	 * An instance of this spec.
	 *
	 * @since 0.3.0 ~2021.06.20
	 */
	@NotNull
	public static final StringSpec INSTANCE = new StringSpec();

	/**
	 * The qualified name of this spec.
	 *
	 * @since 0.3.0 ~2021.06.20
	 */
	@NotNull
	public static final String NAME = StringSpec.class.getSimpleName();

	@NotNull
	@Override
	public Compiler getCompiler() {
		return compiler(
				//target double quotes
				c -> filter(c, is(DoubleQuotesSpec.KIND)),
				//compile
				c -> (compiler, compilation, tree) -> {
					//read the tree
					String text = read(tree).toString();

					try {
						//re-interpret the text (unescape, including '\n', "\""... etc)
						String interpreted = new JSONTokener(text)
								.nextValue()
								.toString();

						//compile
						return new IPushConst(tree, text(interpreted));
					} catch (JSONException ignored) {
						//cannot interpret, skip ;P
						return null;
					}
				}
		);
	}

	@NotNull
	@Override
	public String getQualifiedName() {
		return StringSpec.NAME;
	}
}
