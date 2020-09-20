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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A scope is an invocable that processes a simple part of a {@code jamplate} code. Scopes build up
 * by linking them to each other.
 * <p>
 * The jamplate building using scopes will be in 3 stages.
 * <ol>
 *     <li>Constructing scopes</li>
 *     <li>Linking scopes</li>
 *     <li>Invoking scopes</li>
 * </ol>
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
public interface Scope {
	/**
	 * Invoke this scope to a new {@link StringBuilder}.
	 *
	 * @param memory the memory to be used.
	 * @return a string result of invoking this scope.
	 * @throws IOException if any I/O exception occurs.
	 * @since 0.0.1 ~2020.09.19
	 */
	default String invoke(Memory memory) throws IOException {
		return this.invoke(new StringBuilder(), memory).toString();
	}

	/**
	 * Invoke this scope to the given {@code file}, it is not necessarily only the given {@code
	 * file} this method will invoke to, its siblings may be effected.
	 *
	 * @param file   the file to invoke to.
	 * @param memory the memory to be used.
	 * @throws NullPointerException if the given {@code file} is null.
	 * @throws IOException          if any I/O exception occurs.
	 * @since 0.0.1 ~2020.09.19
	 */
	default void invoke(File file, Memory memory) throws IOException {
		try (FileWriter writer = new FileWriter(file)) {
			this.invoke(writer, memory);
		}
	}

	/**
	 * Append the text that this scope has evaluated to the given {@code appendable}.
	 *
	 * @param appendable the appendable to invoke this scope to.
	 * @param memory     the memory to be used.
	 * @return an of the given {@code appendable} followed immediately by the value of evaluating
	 * 		this scope.
	 * @throws NullPointerException if the given {@code appendable} is null.
	 * @throws IOException          if any I/O exception occurred.
	 * @since 0.0.1 ~2020.09.16
	 */
	Appendable invoke(Appendable appendable, Memory memory) throws IOException;

	/**
	 * Get the definition stored in this scope at the given {@code address}. If this scope do not
	 * have the given {@code address}, It will check the scope before it.
	 *
	 * @param address the address to get the definition stored at it.
	 * @return the definition stored at the given {@code address}.
	 * @throws NullPointerException if the given {@code address} is null.
	 * @since 0.0.1 ~2020.09.16
	 */
	Logic memory(String address);

	/**
	 * Return the scope next to this scope.
	 *
	 * @return the scope next to this scope, or null if no such scope exists.
	 * @since 0.0.1 ~2020.09.19
	 */
	Scope next();

	/**
	 * Return the scope before this scope.
	 *
	 * @return the scope before this scope, or null if no such scope exists.
	 * @since 0.0.1 ~2020.09.19
	 */
	Scope previous();

	/**
	 * If possible, make the given {@code scope} the {@code next} scope after this scope. This scope
	 * will see the opinion of the given {@code scope} (by invoking {@link #tryAttachTo(Scope)})
	 * before making it the {@code next} scope of this scope.
	 *
	 * @param scope the scope to be attached to this scope.
	 * @return true, if the given {@code scope} successfully attached to this scope.
	 * @throws NullPointerException if the given {@code scope} is null.
	 * @since 0.0.1 ~2020.09.17
	 */
	boolean tryAttach(Scope scope);

	/**
	 * If possible, make the given {@code scope} the {@code previous} scope before this scope.
	 *
	 * @param scope the scope to attach this scope to.
	 * @return true, if this scope successfully attached to the given {@code scope}.
	 * @throws NullPointerException if the given {@code scope} is null.
	 * @since 0.0.1 ~2020.09.17
	 */
	boolean tryAttachTo(Scope scope);

	/**
	 * Try attach the given {@code scope} to the {@code head scope} of the {@code scope chain} from
	 * this scope to the last scope after it.
	 *
	 * @param scope the scope to be pushed.
	 * @return true, if the given {@code scope} has been attached to the {@code head scope} of the
	 *        {@code scope chain} from this scope to the last scope of it.
	 * @throws NullPointerException if the given {@code scope} is null.
	 * @since 0.0.1 ~2020.09.17
	 */
	boolean tryPush(Scope scope);
}
