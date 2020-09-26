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
import org.jamplate.memory.ScopeMemory;

import java.io.IOException;
import java.util.Objects;

/**
 * A scope that defines a {@link Logic} to an address, and can be modified later.
 *
 * @author LSafer
 * @version 0.0.5
 * @since 0.0.5 ~2020.09.22
 */
public class Var extends AbstractScope {
	/**
	 * The address this scope is holding its {@link #value} to.
	 *
	 * @since 0.0.5 ~2020.09.22
	 */
	protected final String address;
	/**
	 * The value this scope is currently mapping to {@link #address}.
	 *
	 * @since 0.0.5 ~2020.09.22
	 */
	protected Logic value;

	/**
	 * Create a new scope that can hold a value to the given {@code address}.
	 *
	 * @param address the address the constructed scope is holding values to.
	 * @throws NullPointerException if the given {@code address} is null.
	 * @since 0.0.5 ~2020.09.22
	 */
	public Var(String address) {
		Objects.requireNonNull(address, "address");
		this.address = address;
		this.value = new Constant("");
	}

	/**
	 * Create a new scope that defines the given {@code value} to the given {@code address}. The
	 * created scope's {@code value} can be replaced later.
	 *
	 * @param address the address to map the given {@code value} to.
	 * @param value   the value the created scope will hold initially.
	 * @throws NullPointerException if the given {@code address} or {@code value} is null.
	 * @since 0.0.5 ~2020.09.22
	 */
	public Var(String address, Logic value) {
		Objects.requireNonNull(address, "address");
		Objects.requireNonNull(value, "value");
		this.address = address;
		this.value = value;
	}

	/**
	 * Return the {@link #address} of this scope.
	 *
	 * @return the {@link #address} of this scope.
	 * @since 0.0.5 ~2020.09.22
	 */
	public final String address() {
		return this.address;
	}

	/**
	 * Return the {@link #value} of this scope.
	 *
	 * @return the {@link #value} of this scope.
	 * @since 0.0.5 ~2020.09.22
	 */
	public final Logic value() {
		return this.value;
	}

	@Override
	public synchronized Logic memory(String address) {
		Objects.requireNonNull(address, "address");
		return this.address.equals(address) ?
			   this.value :
			   super.memory(address);
	}

	@Override
	public String toString() {
		return "#VAR " + this.address + " " + this.value;
	}

	@Override
	protected Appendable invoke(Appendable appendable, ScopeMemory memory) throws IOException {
		Objects.requireNonNull(appendable, "appendable");
		Objects.requireNonNull(memory, "memory");

		//little journey to previous scopes :)...
		for (Scope scope = this.previous; scope != null; scope = scope.previous())
			if (scope instanceof Var) {
				//found someone that could understand me!
				Var var = (Var) scope;

				if (this.address.equals(var.address()))
					//that was my brother
					synchronized (this) {
						//hay brother, could you please update your value and be like me?
						var.value(this.value);
					}
			}

		return super.invoke(appendable, memory);
	}

	/**
	 * Update the {@link #value} of this scope.
	 *
	 * @param value the value of this scope.
	 * @throws NullPointerException if the given {@code value} is null.
	 * @since 0.0.5
	 */
	public synchronized void value(Logic value) {
		Objects.requireNonNull(value, "value");
		this.value = value;
	}
}
