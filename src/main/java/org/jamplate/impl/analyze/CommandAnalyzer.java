/*
 *	Copyright 2021 Cufy
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
package org.jamplate.impl.analyze;

import org.jamplate.compile.Processor;
import org.jamplate.impl.model.Command;
import org.jamplate.impl.syntax.TransientKind;
import org.jamplate.model.Document;
import org.jamplate.model.Reference;
import org.jamplate.util.Trees;
import org.jetbrains.annotations.NotNull;

/**
 * A utility class containing the commands analyzers.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.20
 */
public final class CommandAnalyzer {
	/**
	 * A parser that parses the include command.
	 *
	 * @since 0.2.0 ~2021.05.20
	 */
	@SuppressWarnings("OverlyLongLambda")
	@NotNull
	public static final Processor INCLUDE = compilation -> {
		int processed = Trees
				.collect(compilation.getRootTree())
				.parallelStream()
				.mapToInt(tree -> {
					if (tree.getSketch().getKind().equals(TransientKind.COMMAND)) {
						Command command = (Command) tree.getSketch();
						Command.Type type = command.getType();

						Document document = type.getTree().document();
						Reference reference = type.getTree().reference();
						String content = document.read(reference).toString();

						if (content.equalsIgnoreCase("include")) {
							command.setKind(CommandKind.INCLUDE);
							type.setKind(CommandKind.INCLUDE_TYPE);

							return 1;
						}
					}

					return 0;
				})
				.sum();

		return processed != 0;
	};

	/**
	 * Utility classes must not be initialized.
	 *
	 * @throws AssertionError when called.
	 * @since 0.2.0 ~2021.05.20
	 */
	private CommandAnalyzer() {
		throw new AssertionError("No instance for you");
	}
}
