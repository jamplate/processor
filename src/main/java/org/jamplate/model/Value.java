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
package org.jamplate.model;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * The value function is a function that evaluates to a value depending on the state of
 * the memory given to it.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.21
 */
@FunctionalInterface
public interface Value extends Serializable {
	/**
	 * The {@code null} value.
	 *
	 * @since 0.2.0 ~2021.05.21
	 */
	@NotNull
	Value NULL = memory -> "";

	/**
	 * Evaluate this value with the given {@code memory}.
	 *
	 * @param memory the memory to evaluate this value with.
	 * @return the evaluated value.
	 * @throws NullPointerException if the given {@code memory} is null.
	 * @since 0.2.0 ~2021.05.21
	 */
	@NotNull
	@Contract(pure = true)
	String evaluate(@NotNull Memory memory);
}
