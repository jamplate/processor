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

import java.util.Objects;

/**
 * An abstract of the interface {@link ForkScope}.
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
public abstract class AbstractForkScope extends AbstractScope implements ForkScope {
	/**
	 * The branch scope.
	 *
	 * @since 0.0.1 ~2020.09.17
	 */
	protected Scope branch;
	/**
	 * The fork scope.
	 *
	 * @since 0.0.1 ~2020.09.17
	 */
	protected Scope fork;

	@Override
	public Scope branch() {
		return this.branch;
	}

	@Override
	public Scope fork() {
		return this.fork;
	}

	@Override
	public boolean tryAttach(Scope scope) {
		Objects.requireNonNull(scope, "scope");
		return scope instanceof JoinScope &&
			   super.tryAttach(scope);
	}

	@Override
	public boolean tryBranch(Scope branch) {
		Objects.requireNonNull(branch, "branch");

		if (branch instanceof BranchScope) {
			if (this.branch == null && branch.tryAttachTo(this)) {
				this.branch = branch;
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean tryFork(Scope fork) {
		Objects.requireNonNull(fork, "fork");

		if (this.fork == null && fork.tryAttachTo(this)) {
			this.fork = fork;
			return true;
		}

		return false;
	}

	@Override
	public boolean tryPush(Scope scope) {
		Objects.requireNonNull(scope, "scope");
		return this.next == null ?
			   this.branch == null ?
			   this.fork == null ?

			   //next=null branch=null fork=null
			   this.tryAttach(scope) ||
			   this.tryBranch(scope) ||
			   this.tryFork(scope) :

			   //next=null branch=null fork=notnull
			   this.fork.tryPush(scope) ||
			   this.tryAttach(scope) ||
			   this.tryBranch(scope) :

			   //next=null branch=notnull
			   this.branch.tryPush(scope) ||
			   this.tryAttach(scope) :

			   //next=notnull
			   this.next.tryPush(scope);
	}
}
