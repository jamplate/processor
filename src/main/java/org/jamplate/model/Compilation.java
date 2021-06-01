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
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Map;

/**
 * A compilation is a structure holding the variables for a single compilation unit (like
 * a file).
 * <br>
 * The implementing class must support serialization.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.04.28
 */
public interface Compilation extends Serializable {
	/**
	 * Return the environment this compilation is on.
	 *
	 * @return the environment of this compilation.
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	@Contract(pure = true)
	Environment getEnvironment();

	/**
	 * Return the instruction set for this compilation.
	 *
	 * @return the instruction of this compilation.
	 * @since 0.2.0 ~2021.05.23
	 */
	@Nullable
	@Contract(pure = true)
	Instruction getInstruction();

	/**
	 * Returns the meta-data map of this compilation.
	 * <br>
	 * By default, the returned map will be a modifiable checked map. Unless, the class of
	 * this said otherwise.
	 *
	 * @return the meta-data map of this.
	 * @since 0.2.0 ~2021.05.21
	 */
	@NotNull
	@Contract(pure = true)
	Map<String, Object> getMeta();

	/**
	 * Return the root tree of this compilation.
	 *
	 * @return the root tree.
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	@Contract(pure = true)
	Tree getRootTree();

	/**
	 * Set the intrusion of the compilation to be the given {@code instruction}. It is
	 * expected pass an instruction resultant from compiling this compilation with a real
	 * compiler. But, there is no additional checks to validate that. So, be nice and
	 * follow the rules. Please?
	 *
	 * @param instruction the instruction to be set for this compilation.
	 * @throws NullPointerException if the given {@code instruction} is null.
	 * @since 0.2.0 ~2021.05.23
	 */
	@Contract(mutates = "this")
	void setInstruction(@NotNull Instruction instruction);
}
