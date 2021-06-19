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

import org.jamplate.model.Document;
import org.jamplate.model.Environment;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The initializer is a function that introduces/initializes compilations for the
 * documents provided to it in the environment provided to it.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.29
 */
@FunctionalInterface
@Deprecated
public interface Initializer {
	/**
	 * Initialize compilations for the given {@code documents} in the given {@code
	 * environment}.
	 * <br>
	 * Null documents in the array will be ignored.
	 *
	 * @param environment the environment to initialize the compilations in.
	 * @param documents   the documents to initialize compilations for.
	 * @return true, if any compilation was initialized for any of the given {@code
	 * 		documents} in the given {@code compilation}, {@code false} if there was already a
	 * 		compilation foreach of the given {@code documents} in the given {@code
	 * 		environment} and this initializer has done nothing to any of them.
	 * @throws NullPointerException if the given {@code environment} or {@code documents}
	 *                              is null.
	 * @throws RuntimeException     if any runtime exception occurs.
	 * @throws Error                if any unexpected/non-recoverable exception occurs.
	 * @since 0.2.0 ~2021.05.29
	 */
	@Contract(mutates = "param1")
	boolean initialize(@NotNull Environment environment, @Nullable Document @NotNull ... documents);
}
