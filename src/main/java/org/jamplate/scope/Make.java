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
import org.jamplate.memory.ConstantMemory;
import org.jamplate.memory.Memory;
import org.jamplate.memory.ScopeMemory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * A scope that makes multiple files with different names.
 * <p>
 * Relations:
 * <ul>
 *     <li>Previous: {@link Make}</li>
 *     <li>Next: {@link Scope}</li>
 * </ul>
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2020.09.19
 */
public class Make extends AbstractHeadScope {
	/**
	 * The options of this with statement.
	 *
	 * @since 0.0.1 ~2020.09.19
	 */
	protected final Map<String, Logic>[] options;

	/**
	 * Construct a scope that makes multiple files.
	 *
	 * @param options the options for each cycle.
	 * @throws NullPointerException if the given {@code options} is null.
	 * @since 0.0.1 ~2020.09.17
	 */
	public Make(Map<String, Logic>... options) {
		Objects.requireNonNull(options, "options");
		this.options = options;
	}

	@Override
	public void invoke(File file, ScopeMemory memory) throws IOException {
		Objects.requireNonNull(file, "file");
		String name = file.getName();
		File directory = file.getParentFile();

		for (Map<String, Logic> option : this.options)
			if (option != null) {
				Memory optionMemory = new ConstantMemory(memory, option);
				String optionName = name;

				for (Map.Entry<String, Logic> pair : option.entrySet())
					optionName = optionName.replace(
							pair.getKey(),
							//to not forget about the old definitions, use 'memory' instead of 'optionMemory'
							pair.getValue().evaluate(optionMemory)
					);

				File optionFile = new File(directory, optionName);

				if (this.next == null)
					try (FileWriter writer = new FileWriter(optionFile)) {
						writer.write("");
					}
				else
					this.next.invoke(optionFile, optionMemory);
			}
	}

	@Override
	public String toString() {
		StringJoiner joiner = new StringJoiner("|", "#MAKE", "");

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
	public boolean tryAttachTo(Scope scope) {
		return scope instanceof Make &&
			   super.tryAttachTo(scope);
	}
}
