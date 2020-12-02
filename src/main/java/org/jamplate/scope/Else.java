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

import org.cufy.preprocessor.link.Scope;
import org.jamplate.memory.ScopeMemory;

import java.io.IOException;
import java.util.Objects;

/**
 * A scope that closes the branch of an {@link If} or {@link Elif} scopes.
 * <p>
 * Relations:
 * <ul>
 *     <li>Previous: {@link If} | {@link Elif}</li>
 *     <li>Fork: {@link Scope}</li>
 *     <li>Branch: null</li>
 *     <li>Next: null</li>
 * </ul>
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2020.09.17
 */
public class Else extends AbstractTipScope {
	@Override
	public Appendable invoke(Appendable appendable, ScopeMemory memory) throws IOException {
		Objects.requireNonNull(appendable, "appendable");

		if (this.fork != null)
			appendable = this.fork.invoke(appendable, memory);

		return super.invoke(appendable, memory);
	}

	@Override
	public String toString() {
		return "#ELSE";
	}

	@Override
	public boolean setPrevious(Scope scope) {
		Objects.requireNonNull(scope, "scope");
		return (scope instanceof If || scope instanceof Elif) &&
			   super.setPrevious(scope);
	}
}
