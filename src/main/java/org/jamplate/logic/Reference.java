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
package org.jamplate.logic;

import org.jamplate.memory.Memory;

import java.util.Objects;

/**
 * A logic that evaluates the value of a pre-defined address.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2020.09.16
 */
public class Reference implements Logic {
	/**
	 * The address this definition is pointing to.
	 *
	 * @since 0.0.1 ~2020.09.16
	 */
	protected final String address;

	/**
	 * Construct a new definition logic that evaluates to the value defined to the given {@code
	 * address}.
	 *
	 * @param address the address where the value of this definition is stored at.
	 * @throws NullPointerException if the given {@code address} is null.
	 * @since 0.0.1 ~2020.09.16
	 */
	public Reference(String address) {
		Objects.requireNonNull(address, "address");
		this.address = address;
	}

	/**
	 * Get the {@link #address} of this.
	 *
	 * @return the {@link #address} of this.
	 * @since 0.0.1 ~2020.09.19
	 */
	public final String address() {
		return this.address;
	}

	@Override
	public String evaluate(Memory memory) {
		Objects.requireNonNull(memory, "scope");
		Logic logic = memory.find(this.address);
		return logic == null || logic == this ?
			   this.address :
			   logic.evaluate(memory);
	}
}
