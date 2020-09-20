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

/**
 * A scope that has scopes to not be included in the {@code scope chain} of it.
 * <p>
 * Relations:
 * <ul>
 *     <li>Previous: {@link Scope}</li>
 *     <li>Fork: {@link Scope}</li>
 *     <li>Branch: {@link BranchScope}</li>
 *     <li>Next: {@link JoinScope}</li>
 * </ul>
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2020.09.17
 */
public interface ForkScope extends Scope {
	/**
	 * Return the branch scope of this scope.
	 *
	 * @return the branch scope of this scope, or null if no such scope exists.
	 * @since 0.0.1 ~2020.09.19
	 */
	Scope branch();

	/**
	 * Return the scope forked to this scope.
	 *
	 * @return the scope forked to this scope, or null if no such scope exists.
	 * @since 0.0.1 ~2020.09.19
	 */
	Scope fork();

	/**
	 * If possible, make the given {@code scope} the {@code branch} scope of this scope. This scope
	 * will see the opinion of the given {@code scope} (by invoking {@link #tryAttachTo(Scope)})
	 * before making it the {@code branch} scope of this scope.
	 *
	 * @param branch the scope to be the branch scope of this scope.
	 * @return true, if the given {@code scope} successfully branched this scope.
	 * @throws NullPointerException if the given {@code branch} is null.
	 * @since 0.0.1 ~2020.09.17
	 */
	boolean tryBranch(Scope branch);

	/**
	 * If possible, make the given {@code scope} the {@code fork} scope of this scope. This scope
	 * will see the opinion of the given {@code scope} (by invoking {@link #tryAttachTo(Scope)})
	 * before making it the {@code fork} scope of this scope.
	 *
	 * @param fork the scope to be the forked scope to this scope.
	 * @return true, if the given {@code scope} successfully forked by this scope.
	 * @throws NullPointerException if the given {@code fork} is null.
	 * @since 0.0.1 ~2020.09.17
	 */
	boolean tryFork(Scope fork);
}
