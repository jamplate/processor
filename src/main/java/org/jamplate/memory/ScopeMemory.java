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
import org.cufy.preprocessor.link.Scope;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A memory based on a scope context. And supported via a {@link Map}.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2020.09.19
 */
public class ScopeMemory extends AbstractMemory {
	/**
	 * A map supporting this memory.
	 *
	 * @since 0.0.1 ~2020.09.19
	 */
	protected final Map<String, Logic> map;
	/**
	 * The source scope of this memory.
	 *
	 * @since 0.0.1 ~2020.09.19
	 */
	protected final Scope scope;

	/**
	 * Construct a new scope memory.
	 *
	 * @param scope the scope of this memory.
	 * @throws NullPointerException if the given {@code scope} is null.
	 * @since 0.0.1 ~2020.09.19
	 */
	public ScopeMemory(Scope scope) {
		Objects.requireNonNull(scope, "scope");
		this.scope = scope;
		this.map = Collections.emptyMap();
	}

	/**
	 * Construct a new scope memory supported with the given {@code map}.
	 *
	 * @param scope the scope of this memory.
	 * @param map   the map supporting this memory.
	 * @throws NullPointerException if the given {@code scope} or {@code map} is null.
	 * @since 0.0.1 ~2020.09.19
	 */
	public ScopeMemory(Scope scope, Map<String, Logic> map) {
		Objects.requireNonNull(scope, "scope");
		Objects.requireNonNull(map, "map");
		this.scope = scope;
		this.map = new HashMap(map);
	}

	/**
	 * Create a memory from the given memory for the given {@code scope}.
	 *
	 * @param memory the memory to copy from.
	 * @param scope  the scope  of this memory.
	 * @throws NullPointerException if the given {@code memory} or {@code scope} is null.
	 * @since 0.0.1 ~2020.09.19
	 */
	public ScopeMemory(Memory memory, Scope scope) {
		super(memory);
		Objects.requireNonNull(scope, "scope");
		this.scope = scope;
		this.map = Collections.emptyMap();
	}

	/**
	 * Create a memory from the given {@code memory} for the given {@code scope}.
	 *
	 * @param memory the memory to copy from.
	 * @param scope  the scope  of this memory.
	 * @param map    the map supporting this memory.
	 * @throws NullPointerException if the given {@code memory} or {@code scope} or {@code map} is
	 *                              null.
	 * @since 0.0.1 ~2020.09.19
	 */
	public ScopeMemory(Memory memory, Scope scope, Map<String, Logic> map) {
		super(memory);
		Objects.requireNonNull(scope, "scope");
		Objects.requireNonNull(map, "map");
		this.scope = scope;
		this.map = new HashMap(map);
	}

	@Override
	public Logic find(String address) {
		Objects.requireNonNull(address, "address");
		Logic logic = this.scope.find(address);
		return logic == null ?
			   this.map.containsKey(address) ?
			   this.map.get(address) :
			   super.find(address) :
			   logic;
	}
}
