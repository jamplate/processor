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

import org.cufy.preprocessor.link.Logic;
import org.cufy.preprocessor.AbstractParser;
import org.cufy.preprocessor.link.Scope;
import org.jamplate.memory.ScopeMemory;
import org.cufy.preprocessor.Poll;
import org.jamplate.util.Scopes;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * A scope that has a {@link #fork} scope that will be invoked only if a {@code logic} is evaluated
 * true. Otherwise, its {@link #branch} scope will be invoked.
 * <p>
 * Relations:
 * <ul>
 *     <li>Previous: {@link Scope}</li>
 *     <li>Fork: {@link Scope}</li>
 *     <li>Branch: {@link Elif} | {@link Else}</li>
 *     <li>Next: {@link Endif}</li>
 * </ul>
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2020.09.17
 */
public class If extends AbstractForkScope {
	/**
	 * The pattern of an {@code if} command.
	 *
	 * @since 0.0.b ~2020.10.08
	 */
	public static final Pattern PATTERN = Pattern.compile("^#(?<NAME>IF)(?<LOGIC>.*)$", Pattern.CASE_INSENSITIVE);

	/**
	 * The logic that if it evaluated true, the {@link #fork} scope will be invoked. Otherwise the
	 * {@link #branch} scope will be invoked.
	 *
	 * @since 0.0.1 ~2020.09.17
	 */
	protected final Logic condition;

	/**
	 * Construct a new scope that will invoke its {@link #fork} if the given {@code logic} evaluated
	 * true. Otherwise, its {@link #branch} will be invoked.
	 *
	 * @param logic the logic of the constructed scope.
	 * @throws NullPointerException if the given {@code logic} is null.
	 * @since 0.0.1 ~2020.09.17
	 */
	public If(Logic logic) {
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
		return this.condition;
	}

	@Override
	public Appendable invoke(Appendable appendable, ScopeMemory memory) throws IOException {
		Objects.requireNonNull(appendable, "appendable");

		if (this.condition.evaluateBoolean(memory)) {
			if (this.fork != null)
				appendable = this.fork.invoke(appendable, memory);
		} else {
			if (this.branch != null)
				appendable = this.branch.invoke(appendable, memory);
		}

		return super.invoke(appendable, memory);
	}

	@Override
	public String toString() {
		return "#IF " + this.condition;
	}

	@Override
	public boolean setNext(Scope scope) {
		Objects.requireNonNull(scope, "scope");
		return scope instanceof Endif &&
			   super.setNext(scope);
	}

	@Override
	public boolean setBranch(Scope branch) {
		Objects.requireNonNull(branch, "branch");
		return (branch instanceof Elif || branch instanceof Else) &&
			   super.setBranch(branch);
	}

	public static class Parser extends AbstractParser.AbstractVote<If> {
		@Override
		public boolean link(List poll, org.cufy.preprocessor.Parser<? super If> parser) {

		}

		@Override
		public boolean parse(List poll, org.cufy.preprocessor.Parser<? super If> parser) {
			Objects.requireNonNull(poll, "poll");
			return Poll.iterate(poll, Scopes.parse(
					If.PATTERN,
					(name, logic) -> {
						switch (name.toLowerCase()) {
							case "if":
								return new If();
							default:
								throw new InternalError("Unknown name: " + name);
						}
					}
			));
		}
	}
}
