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

import org.jamplate.logic.Logic;

/**
 * A memory that can hold constant context variables.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2020.09.19
 */
public interface Memory {
	/**
	 * Find the logic at the given {@code address}.
	 *
	 * @param address the address to find the logic located at it.
	 * @return the logic stored at the given {@code address} in this memory, Or null if no such
	 * 		logic stored at teh given {@code address} in this memory.
	 * @throws NullPointerException if the given {@code address} is null.
	 * @since 0.0.1 ~2020.09.19
	 */
	Logic find(String address);
}
