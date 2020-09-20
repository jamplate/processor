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

import org.jamplate.memory.ScopeMemory;

import java.io.IOException;
import java.util.Objects;

/**
 * A scope that contains the constant text.
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
public class Text extends AbstractScope {
	/**
	 * The text of this scope.
	 *
	 * @since 0.0.1 ~2020.09.17
	 */
	protected final String text;

	/**
	 * Construct a new scope containing the given {@code text}.
	 *
	 * @param text the text of the constructed scope.
	 * @throws NullPointerException if the given {@code text} is null.
	 * @since 0.0.1 ~2020.09.17
	 */
	public Text(String text) {
		Objects.requireNonNull(text, "text");
		this.text = text;
	}

	/**
	 * Return the {@link #text} of this scope.
	 *
	 * @return the {@link #text} of this scope.
	 * @since 0.0.1 ~2020.09.19
	 */
	public final String text() {
		return this.text;
	}

	@Override
	public Appendable invoke(Appendable appendable, ScopeMemory memory) throws IOException {
		Objects.requireNonNull(appendable, "appendable");
		Objects.requireNonNull(memory, "memory");

		appendable = appendable.append(this.text);

		return super.invoke(appendable, memory);
	}
}
