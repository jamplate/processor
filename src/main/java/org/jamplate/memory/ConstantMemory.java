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

import org.cufy.preprocessor.link.Logic;
import org.cufy.preprocessor.invoke.AbstractMemory;
import org.cufy.preprocessor.invoke.Memory;
import org.jamplate.logic.Literal;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A memory holding pre-evaluated values.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2020.09.20
 */
public class ConstantMemory extends AbstractMemory {
	/**
	 * The map holding the values of this memory.
	 *
	 * @since 0.0.1 ~2020.09.20
	 */
	protected final Map<String, Literal> map;

	/**
	 * Construct a new constant memory (a memory with {@link Literal}s).
	 *
	 * @param memory the memory to evaluate the values with.
	 * @param map    the map to evaluate its values then store it in this memory.
	 * @throws NullPointerException if the given {@code memory} or {@code map} is null.
	 * @since 0.0.1 ~2020.09.20
	 */
	public ConstantMemory(Memory memory, Map<String, Logic> map) {
		super(memory);
		Objects.requireNonNull(map, "map");
		this.map = new HashMap();
		map.forEach((address, logic) ->
				this.map.put(
						address,
						new Literal(logic.evaluateString(memory))
				)
		);
	}

	@Override
	public Logic find(String address) {
		Objects.requireNonNull(address, "address");
		return this.map.containsKey(address) ?
			   this.map.get(address) :
			   super.find(address);
	}
}
