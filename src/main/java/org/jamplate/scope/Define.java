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
package org.jamplate.scope;

import org.jamplate.logic.Constant;
import org.jamplate.logic.Logic;

import java.util.Objects;

/**
 * A scope that defines a {@link Logic} to an address.
 * <p>
 * Relations:
 * <ul>
 *     <li>Previous: {@link Scope}</li>
 *     <li>Next: {@link Scope}</li>
 * </ul>
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2020.09.17
 */
public class Define extends AbstractScope {
	/**
	 * The address this scope is defining.
	 *
	 * @since 0.0.1 ~2020.09.17
	 */
	protected final String address;
	/**
	 * The logic to define the address to.
	 *
	 * @since 0.0.1 ~2020.09.17
	 */
	protected final Logic value;

	/**
	 * Construct a new scope that defines the given {@code logic} to the given {@code address}.
	 *
	 * @param address the address to define the given {@code logic} to.
	 * @param logic   the logic to be defined to the given {@code address}.
	 * @throws NullPointerException if the given {@code address} or {@code logic} is null.
	 * @since 0.0.1 ~2020.09.17
	 */
	public Define(String address, Logic logic) {
		Objects.requireNonNull(address, "address");
		Objects.requireNonNull(logic, "logic");
		this.address = address;
		this.value = logic;
	}

	/**
	 * Construct a new scope that defines the given {@code text} to the given {@code address}.
	 *
	 * @param address the address to define the given {@code logic} to.
	 * @param text    the text to be defined to the given {@code address}.
	 * @throws NullPointerException if the given {@code address} or {@code text} is null.
	 * @since 0.0.1 ~2020.09.17
	 */
	public Define(String address, String text) {
		Objects.requireNonNull(address, "address");
		Objects.requireNonNull(text, "text");
		this.address = address;
		this.value = new Constant(text);
	}

	/**
	 * Return the {@link #address} of this scope.
	 *
	 * @return the {@link #address} of this scope.
	 * @since 0.0.1 ~2020.09.19
	 */
	public final String address() {
		return this.address;
	}

	/**
	 * Return the {@link #value} of this scope.
	 *
	 * @return the {@link #value} of this scope.
	 * @since 0.0.1 ~2020.09.19
	 */
	public final Logic value() {
		return this.value;
	}

	@Override
	public Logic memory(String address) {
		Objects.requireNonNull(address, "address");
		return this.address.equals(address) ?
			   this.value :
			   super.memory(address);
	}
}
