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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A processor is a function that takes a sketch and a compilation and process it its way
 * (for example: parsing its value).
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.04.28
 */
@FunctionalInterface
public interface Processor {
	/**
	 * Process the given {@code compilation}.
	 * <br>
	 * If this method returned {@code true}. The compilation process will have another
	 * round. If all the processors in a compilation return {@code false}, the compilation
	 * process ends.
	 *
	 * @param compilation the compilation the node is from.
	 * @return true, if this processor have done something. False, otherwise.
	 * @throws NullPointerException if the given {@code compilation} is null.
	 * @since 0.0.1 ~2021.04.28
	 */
	@Contract(mutates = "param")
	boolean process(@NotNull Compilation compilation);
}
