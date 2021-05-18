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
import org.jamplate.compile.Processor;
import org.jamplate.impl.syntax.compile.SyntaxParser;
import org.jamplate.util.compile.*;
import org.jetbrains.annotations.NotNull;

/**
 * Jamplate syntax-level default implementation constants.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.18
 */
public final class JamplateSyntax {
	/**
	 * An all-in-one parser used by the jamplate default implementation.
	 *
	 * @since 0.2.0 ~2021.05.17
	 */
	@NotNull
	public static final Parser PARSER =
			new CollectParser(new OrderParser(
					new MergeParser(new CombineParser(
							SyntaxParser.QUOTE,
							SyntaxParser.DQUOTE
					)).also(SyntaxParser.ESCAPE),
					new MergeParser(new CombineParser(
							SyntaxParser.CURLY,
							SyntaxParser.SQUARE,
							SyntaxParser.ROUND
					))
			));

	/**
	 * A parser that fully parses the compilations passed to it using the default jamplate
	 * implementation parser.
	 *
	 * @since 0.2.0 ~2021.05.18
	 */
	@NotNull
	public static final Processor PARSER_PROCESSOR =
			new ParserProcessor(JamplateSyntax.PARSER);

	/**
	 * Utility classes must not be initialized.
	 *
	 * @throws AssertionError when called.
	 * @since 0.2.0 ~2021.05.16
	 */
	private JamplateSyntax() {
		throw new AssertionError("No instance for you");
	}
}
