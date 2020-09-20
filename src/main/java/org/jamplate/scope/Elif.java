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
import org.jamplate.memory.ScopeMemory;

import java.io.IOException;
import java.util.Objects;

/**
 * A scope that branches an {@link If} or another {@link Elif} scope.
 * <p>
 * Relations:
 * <ul>
 *     <li>Previous: {@link If} | {@link Elif}</li>
 *     <li>Fork: {@link Scope}</li>
 *     <li>Branch: {@link Elif} | {@link Else}</li>
 *     <li>Next: null</li>
 * </ul>
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2020.09.17
 */
public class Elif extends AbstractBranchScope {
	/**
	 * The logic of this scope.
	 *
	 * @since 0.0.1 ~2020.09.17
	 */
	protected final Logic condition;

	/**
	 * Construct a scope that branches an {@link If} or another {@link Elif} scope. And uses the
	 * given {@code logic}.
	 *
	 * @param logic the logic of this scope.
	 * @throws NullPointerException if the given {@code logic} is null.
	 * @since 0.0.1 ~2020.09.17
	 */
	public Elif(Logic logic) {
		Objects.requireNonNull(logic, "logic");
		this.condition = logic;
	}

	/**
	 * Return the {@link #condition} of this scope.
	 *
	 * @return the {@link #condition} of this scope.
	 * @since 0.0.1 ~2020.09.19
	 */
	public final Logic condition() {
		return condition;
	}

	@Override
	public Appendable invoke(Appendable appendable, ScopeMemory memory) throws IOException {
		Objects.requireNonNull(appendable, "appendable");

		boolean logic = this.condition.evaluateBoolean(memory);

		if (logic) {
			if (this.fork != null)
				appendable = this.fork.invoke(appendable, memory);
		} else {
			if (this.branch != null)
				appendable = this.branch.invoke(appendable, memory);
		}

		return super.invoke(appendable, memory);
	}

	@Override
	public boolean tryAttachTo(Scope scope) {
		Objects.requireNonNull(scope, "scope");
		return (scope instanceof If || scope instanceof Elif) &&
			   super.tryAttachTo(scope);
	}

	@Override
	public boolean tryBranch(Scope branch) {
		Objects.requireNonNull(branch, "branch");
		return (branch instanceof Elif || branch instanceof Else) &&
			   super.tryBranch(branch);
	}
}
