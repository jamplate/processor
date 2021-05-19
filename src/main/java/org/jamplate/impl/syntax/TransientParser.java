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
package org.jamplate.impl.syntax;

import org.jamplate.compile.Parser;
import org.jamplate.impl.syntax.compile.ScopeParser;
import org.jamplate.impl.syntax.model.SyntaxScope;
import org.jetbrains.annotations.NotNull;

/**
 * Parsers that parses sketches that to be elements.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.19
 */
public final class TransientParser {
	/**
	 * A parser parsing single-line commands.
	 *
	 * @since 0.2.0 ~2021.05.19
	 */
	@NotNull
	public static final Parser COMMAND = new ScopeParser(
			TransientPattern.COMMAND_OPEN, TransientPattern.COMMAND_CLOSE
	).peek(tree -> {
		tree.getSketch().setKind(TransientKind.COMMAND);
		((SyntaxScope) tree.getSketch()).getOpenAnchor().setKind(TransientKind.COMMAND_OPEN);
		((SyntaxScope) tree.getSketch()).getCloseAnchor().setKind(TransientKind.COMMAND_CLOSE);
	});

	/**
	 * A parser parsing injection sequences.
	 *
	 * @since 0.2.0 ~2021.05.19
	 */
	@NotNull
	public static final Parser INJECTION = new ScopeParser(
			TransientPattern.INJECTION_OPEN, TransientPattern.INJECTION_CLOSE
	).peek(tree -> {
		tree.getSketch().setKind(TransientKind.INJECTION);
		((SyntaxScope) tree.getSketch()).getOpenAnchor().setKind(TransientKind.INJECTION_OPEN);
		((SyntaxScope) tree.getSketch()).getCloseAnchor().setKind(TransientKind.INJECTION_CLOSE);
	});

	/**
	 * Utility classes must not be initialized.
	 *
	 * @throws AssertionError when called.
	 * @since 0.2.0 ~2021.05.19
	 */
	private TransientParser() {
		throw new AssertionError("No instance for you");
	}
}
