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
package org.jamplate.glucose.spec.tool;

import org.jamplate.api.Spec;
import org.jamplate.function.Listener;
import org.jamplate.impl.api.Event;
import org.jamplate.model.Instruction;
import org.jetbrains.annotations.NotNull;

/**
 * An optimization spec. General optimizations. If everything has gone right, this should
 * just optimize the result with no alter to the behaviour.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.07.01
 */
public class OptimizeSpec implements Spec {
	/**
	 * An instance of this spec.
	 *
	 * @since 0.3.0 ~2021.07.01
	 */
	@NotNull
	public static final OptimizeSpec INSTANCE = new OptimizeSpec();

	/**
	 * The qualified name of this spec.
	 *
	 * @since 0.3.0 ~2021.07.01
	 */
	@NotNull
	public static final String NAME = OptimizeSpec.class.getSimpleName();

	@NotNull
	@Override
	public Listener getListener() {
		return (event, compilation, parameter) -> {
			if (event.equals(Event.OPTIMIZE)) {
				Instruction instruction = compilation.getInstruction();

				Number mode = (Number) parameter;

				if (instruction != null) {
					Instruction optimized = instruction.optimize(mode.intValue());

					compilation.setInstruction(optimized);
				}
			}
		};
	}

	@NotNull
	@Override
	public String getQualifiedName() {
		return OptimizeSpec.NAME;
	}
}
