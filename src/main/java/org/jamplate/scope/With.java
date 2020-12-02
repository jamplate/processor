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
import org.cufy.preprocessor.link.Scope;
import org.jamplate.memory.ConstantMemory;
import org.cufy.preprocessor.invoke.Memory;
import org.jamplate.memory.ScopeMemory;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * A scope that has a {@link #fork} scope that will be invoked for each number of times. Each time
 * replacing a specific strings to a variable strings depending on the cycle variables.
 * <p>
 * Relations:
 * <ul>
 *     <li>Previous: {@link Scope}</li>
 *     <li>Fork: {@link Scope}</li>
 *     <li>Branch: null</li>
 *     <li>Next: {@link Endwith}</li>
 * </ul>
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2020.09.17
 */
public class With extends AbstractForkScope {
	/**
	 * The options of this with statement.
	 *
	 * @since 0.0.1 ~2020.09.19
	 */
	protected final Map<String, Logic>[] options;

	/**
	 * Construct a scope that invokes its {@link #fork} multiple times depending on the length of
	 * the given {@code options}.
	 *
	 * @param options the options for each cycle.
	 * @throws NullPointerException if the given {@code options} is null.
	 * @since 0.0.1 ~2020.09.17
	 */
	public With(Map<String, Logic>... options) {
		Objects.requireNonNull(options, "options");
		this.options = options;
	}

	/**
	 * Return a copy of the {@link #options} of this scope.
	 *
	 * @return a copy of the {@link #options} of this scope.
	 * @since 0.0.1 ~2020.09.19
	 */
	public final Map<String, Logic>[] options() {
		return this.options.clone();
	}

	@Override
	public String toString() {
		StringJoiner joiner = new StringJoiner("|", "#WITH", "");

		for (Map<String, Logic> option : this.options) {
			if (option != null) {
				StringJoiner joiner1 = new StringJoiner(", ", "", "");

				for (Map.Entry<String, Logic> entry : option.entrySet())
					joiner1.add(entry.getKey())
							.add(":")
							.add(entry.getValue().toString());

				joiner.add(joiner1.toString());
			}
		}

		return joiner.toString();
	}

	@Override
	public boolean setNext(Scope scope) {
		Objects.requireNonNull(scope, "scope");
		return scope instanceof Endwith &&
			   super.setNext(scope);
	}

	@Override
	public boolean setBranch(Scope branch) {
		Objects.requireNonNull(branch, "branch");
		return false;
	}

	@Override
	protected Appendable invoke(Appendable appendable, ScopeMemory memory) throws IOException {
		Objects.requireNonNull(appendable, "appendable");
		Objects.requireNonNull(memory, "memory");

		if (this.fork != null)
			for (Map<String, Logic> option : this.options)
				if (option != null) {
					Memory optionMemory = new ConstantMemory(memory, option);
					String optionText = this.fork.invoke(optionMemory);

					for (Map.Entry<String, Logic> pair : option.entrySet())
						optionText = optionText.replace(
								pair.getKey(),
								//to not forget about the old definitions, use 'memory' instead of 'optionMemory'
								pair.getValue().evaluateString(optionMemory)
						);

					appendable = appendable.append(optionText);
				}

		return super.invoke(appendable, memory);
	}
}
