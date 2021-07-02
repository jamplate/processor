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
package org.jamplate.spec.document;

import org.jamplate.api.Spec;
import org.jamplate.function.Initializer;
import org.jamplate.internal.model.CompilationImpl;
import org.jamplate.model.Sketch;
import org.jamplate.model.Tree;
import org.jamplate.spec.element.ParameterSpec;
import org.jetbrains.annotations.NotNull;

/**
 * A specification for logical root compilations.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.07.01
 */
public class LogicSpec implements Spec {
	/**
	 * An instance of this spec.
	 *
	 * @since 0.3.0 ~2021.07.01
	 */
	@NotNull
	public static final LogicSpec INSTANCE = new LogicSpec();

	/**
	 * The qualified name of this spec.
	 *
	 * @since 0.3.0 ~2021.07.01
	 */
	@NotNull
	public static final String NAME = LogicSpec.class.getSimpleName();

	@NotNull
	@Override
	public Initializer getInitializer() {
		return (environment, document) ->
				new CompilationImpl(
						environment,
						new Tree(
								document,
								new Sketch(ParameterSpec.KIND),
								ParameterSpec.WEIGHT
						)
				);
	}

	@NotNull
	@Override
	public String getQualifiedName() {
		return LogicSpec.NAME;
	}
}
