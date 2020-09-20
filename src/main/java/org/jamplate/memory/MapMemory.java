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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A memory backed by a map.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2020.09.19
 */
public class MapMemory implements Memory {
	/**
	 * The map backing this memory.
	 *
	 * @since 0.0.1 ~2020.09.19
	 */
	protected final Map<String, Logic> map;
	/**
	 * The old memory this memory may has been made with.
	 *
	 * @since 0.0.1 ~2020.09.19
	 */
	protected final Memory memory;

	/**
	 * Construct a new memory for the given {@code map}.
	 *
	 * @param map the map of this memory.
	 * @throws NullPointerException if the given {@code map} is null.
	 * @since 0.0.1 ~2020.09.19
	 */
	public MapMemory(Map<String, Logic> map) {
		Objects.requireNonNull(map, "map");
		this.memory = null;
		this.map = new HashMap(map);
	}

	/**
	 * Construct a new memory from the given {@code memory} for the given {@code map}.
	 *
	 * @param memory the memory to copy from.
	 * @param map    the map of this memory.
	 * @throws NullPointerException if the given {@code memory} or {@code map} is null.
	 * @since 0.0.1 ~2020.09.19
	 */
	public MapMemory(Memory memory, Map<String, Logic> map) {
		Objects.requireNonNull(memory, "memory");
		Objects.requireNonNull(map, "map");
		this.memory = memory;
		this.map = new HashMap(map);
	}

	@Override
	public Logic find(String address) {
		Objects.requireNonNull(address, "address");
		return this.map.containsKey(address) ?
			   this.map.get(address) :
			   this.memory == null ?
			   null :
			   this.memory.find(address);
	}
}
