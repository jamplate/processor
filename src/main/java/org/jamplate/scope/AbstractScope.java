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

import org.jamplate.logic.Logic;
import org.jamplate.memory.Memory;
import org.jamplate.memory.ScopeMemory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

/**
 * An abstract of the interface {@link Scope}.
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
public abstract class AbstractScope implements Scope {
	/**
	 * The next scope after this scope.
	 *
	 * @since 0.0.1 ~2020.09.17
	 */
	protected Scope next;
	/**
	 * The previous scope before this scope.
	 *
	 * @since 0.0.1 ~2020.09.17
	 */
	protected Scope previous;

	@Override
	public final Appendable invoke(Appendable appendable, Memory memory) throws IOException {
		return this.invoke(appendable, new ScopeMemory(memory, this));
	}

	@Override
	public final void invoke(File file, Memory memory) throws IOException {
		this.invoke(file, new ScopeMemory(memory, this));
	}

	@Override
	public final Scope next() {
		return this.next;
	}

	@Override
	public final Scope previous() {
		return this.previous;
	}

	@Override
	public String invoke(Memory memory) throws IOException {
		return this.invoke(new ScopeMemory(memory, this));
	}

	@Override
	public Logic memory(String address) {
		Objects.requireNonNull(address, "address");
		return this.previous == null ?
			   null :
			   this.previous.memory(address);
	}

	@Override
	public boolean tryAttach(Scope scope) {
		Objects.requireNonNull(scope, "scope");

		if (this.next == null && scope.tryAttachTo(this)) {
			this.next = scope;
			return true;
		}

		return false;
	}

	@Override
	public boolean tryAttachTo(Scope scope) {
		Objects.requireNonNull(scope, "scope");

		if (this.previous == null) {
			this.previous = scope;
			return true;
		}

		return false;
	}

	@Override
	public boolean tryPush(Scope scope) {
		Objects.requireNonNull(scope, "scope");
		return this.next == null ?
			   this.tryAttach(scope) :
			   this.next.tryPush(scope);
	}

	/**
	 * A method that auto-wired to have a {@link ScopeMemory} of this scope.
	 *
	 * @param memory the memory to be used.
	 * @return a new {@link StringBuilder} with this scope invoked to it.
	 * @throws IOException if any I/O exception occurs.
	 * @since 0.0.1 ~2020.09.19
	 */
	protected String invoke(ScopeMemory memory) throws IOException {
		return this.invoke(new StringBuilder(), memory).toString();
	}

	/**
	 * A method that auto-wired to have a {@link ScopeMemory} of this scope.
	 *
	 * @param file   the file to invoke to.
	 * @param memory the memory to be used.
	 * @throws NullPointerException if the given {@code file} is null.
	 * @throws IOException          if any I/O exception occurs.
	 * @since 0.0.1 ~2020.09.19
	 */
	protected void invoke(File file, ScopeMemory memory) throws IOException {
		try (FileWriter writer = new FileWriter(file)) {
			this.invoke(writer, memory);
		}
	}

	/**
	 * A method that auto-wired to have a {@link ScopeMemory} of this scope.
	 *
	 * @param appendable the appendable to invoke this scope to.
	 * @param memory     the memory to be used.
	 * @return an of the given {@code appendable} followed immediately by the value of evaluating
	 * 		this scope.
	 * @throws NullPointerException if the given {@code appendable} is null.
	 * @throws IOException          if any I/O exception occurred.
	 * @since 0.0.1 ~2020.09.19
	 */
	protected Appendable invoke(Appendable appendable, ScopeMemory memory) throws IOException {
		Objects.requireNonNull(appendable, "appendable");

		if (this.next != null)
			appendable = this.next.invoke(appendable, memory);

		return appendable;
	}
}
