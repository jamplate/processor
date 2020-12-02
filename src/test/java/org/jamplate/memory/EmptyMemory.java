/*
 *	Copyright 2020 Cufy
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
package org.jamplate.memory;

import org.cufy.preprocessor.invoke.AbstractMemory;
import org.cufy.preprocessor.invoke.Memory;

/**
 * A memory backed by nothing.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2020.09.19
 */
public class EmptyMemory extends AbstractMemory {
	/**
	 * Construct a new empty memory.
	 *
	 * @since 0.0.1 ~2020.09.19
	 */
	public EmptyMemory() {
	}

	/**
	 * Construct a new memory from the given {@code memory}.
	 *
	 * @param memory the memory to copy from.
	 * @throws NullPointerException if the given {@code memory} is null.
	 * @since 0.0.1 ~2020.09.19
	 */
	public EmptyMemory(Memory memory) {
		super(memory);
	}
}
