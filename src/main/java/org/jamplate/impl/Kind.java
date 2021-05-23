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
package org.jamplate.impl;

import org.jetbrains.annotations.NotNull;

/**
 * A class containing the kinds in the jamplate default implementation.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.21
 */
public class Kind {
	/**
	 * A utility class containing the kinds of jamplate commands.
	 *
	 * @author LSafer
	 * @version 0.2.0
	 * @since 0.2.0 ~2021.05.20
	 */
	public static final class Command {
		/**
		 * The command kind of the {@code #console} command.
		 *
		 * @since 0.2.0 ~2021.05.23
		 */
		@NotNull
		public static final String CONSOLE = "command/console";

		/**
		 * The command kind of the {@code #include} command.
		 *
		 * @since 0.2.0 ~2021.05.20
		 */
		@NotNull
		public static final String INCLUDE = "command/include";

		/**
		 * Utility classes must not be initialized.
		 *
		 * @throws AssertionError when called.
		 * @since 0.2.0 ~2021.05.20
		 */
		private Command() {
			throw new AssertionError("No instance for you");
		}
	}

	/**
	 * A utility class containing the names of the kinds the default jamplate
	 * implementation offers.
	 *
	 * @author LSafer
	 * @version 0.2.0
	 * @since 0.2.0 ~2021.05.16
	 */
	public static final class Syntax {
		/**
		 * The kind for commas.
		 *
		 * @since 0.2.0 ~2021.05.16
		 */
		@NotNull
		public static final String COMMA = "comma";

		/**
		 * The kind for curly brackets.
		 *
		 * @since 0.2.0 ~2021.05.16
		 */
		@NotNull
		public static final String CURLY = "curly";
		/**
		 * The kind for curly brackets.
		 *
		 * @since 0.2.0 ~2021.05.16
		 */
		@NotNull
		public static final String CURLY_CLOSE = "curly:close";
		/**
		 * The kind for curly brackets.
		 *
		 * @since 0.2.0 ~2021.05.16
		 */
		@NotNull
		public static final String CURLY_OPEN = "curly:open";

		/**
		 * The kind for double-quotes.
		 *
		 * @since 0.2.0 ~2021.05.16
		 */
		@NotNull
		public static final String DQUOTE = "dquote";
		/**
		 * The kind for double-quotes.
		 *
		 * @since 0.2.0 ~2021.05.16
		 */
		@NotNull
		public static final String DQUOTE_CLOSE = "dquote:close";
		/**
		 * The kind for double-quotes.
		 *
		 * @since 0.2.0 ~2021.05.16
		 */
		@NotNull
		public static final String DQUOTE_OPEN = "dquote:open";

		/**
		 * The kind for escaped sequences.
		 *
		 * @since 0.2.0 ~2021.05.16
		 */
		@NotNull
		public static final String ESCAPE = "escape";

		/**
		 * The kind for line separators ({@code \n} or {@code \r} or {@code \r\n}).
		 *
		 * @since 0.2.0 ~2021.05.19
		 */
		@NotNull
		public static final String LN = "ln";

		/**
		 * The kind for suppressed (do-not-print) line separators ({@code \n} or {@code
		 * \r} or {@code \r\n}).
		 *
		 * @since 0.2.0 ~2021.05.19
		 */
		@NotNull
		public static final String LN_SUPPRESSED = "ln/suppressed";

		/**
		 * The kind for quotes.
		 *
		 * @since 0.2.0 ~2021.05.16
		 */
		@NotNull
		public static final String QUOTE = "quote";
		/**
		 * The kind for quotes.
		 *
		 * @since 0.2.0 ~2021.05.16
		 */
		@NotNull
		public static final String QUOTE_CLOSE = "quote:close";
		/**
		 * The kind for quotes.
		 *
		 * @since 0.2.0 ~2021.05.16
		 */
		@NotNull
		public static final String QUOTE_OPEN = "quote:open";

		/**
		 * The kind for round brackets.
		 *
		 * @since 0.2.0 ~2021.05.16
		 */
		@NotNull
		public static final String ROUND = "round";
		/**
		 * The kind for round brackets.
		 *
		 * @since 0.2.0 ~2021.05.16
		 */
		@NotNull
		public static final String ROUND_CLOSE = "round:close";
		/**
		 * The kind for round brackets.
		 *
		 * @since 0.2.0 ~2021.05.16
		 */
		@NotNull
		public static final String ROUND_OPEN = "round:open";

		/**
		 * The kind for square brackets.
		 *
		 * @since 0.2.0 ~2021.05.16
		 */
		@NotNull
		public static final String SQUARE = "square";
		/**
		 * The kind for square brackets.
		 *
		 * @since 0.2.0 ~2021.05.16
		 */
		@NotNull
		public static final String SQUARE_CLOSE = "square:close";
		/**
		 * The kind for square brackets.
		 *
		 * @since 0.2.0 ~2021.05.16
		 */
		@NotNull
		public static final String SQUARE_OPEN = "square:open";

		/**
		 * Utility classes must not be initialized.
		 *
		 * @throws AssertionError when called.
		 * @since 0.2.0 ~2021.05.16
		 */
		private Syntax() {
			throw new AssertionError("No instance for you");
		}
	}

	/**
	 * A utility class containing the kinds for the sketches that to be elements.
	 *
	 * @author LSafer
	 * @version 0.2.0
	 * @since 0.2.0 ~2021.05.19
	 */
	public static final class Transient {
		/**
		 * The kind of a single-line command.
		 *
		 * @since 0.2.0 ~2021.05.19
		 */
		@NotNull
		public static final String COMMAND = "command";
		/**
		 * The kind of the closing anchor of a single-line command.
		 *
		 * @since 0.2.0 ~2021.05.19
		 */
		@NotNull
		public static final String COMMAND_CLOSE = "command:close";
		/**
		 * The kind of the opening anchor of a single-line command.
		 *
		 * @since 0.2.0 ~2021.05.19
		 */
		@NotNull
		public static final String COMMAND_OPEN = "command:open";
		/**
		 * The kind of the parameter of a single-line command.
		 *
		 * @since 0.2.0 ~2021.05.20
		 */
		@NotNull
		public static final String COMMAND_PARAMETER = "command:parameter";
		/**
		 * The kind of the type of a single-line command.
		 *
		 * @since 0.2.0 ~2021.05.20
		 */
		@NotNull
		public static final String COMMAND_TYPE = "command:type";

		/**
		 * The kind of comment blocks.
		 *
		 * @since 0.2.0 ~2021.05.19
		 */
		@NotNull
		public static final String COMMENT_BLOCK = "comment:block";
		/**
		 * The kind of the closing anchor of comment blocks.
		 *
		 * @since 0.2.0 ~2021.05.19
		 */
		@NotNull
		public static final String COMMENT_BLOCK_CLOSE = "comment-block:close";
		/**
		 * The kind of the opening anchor of comment blocks.
		 *
		 * @since 0.2.0 ~2021.05.19
		 */
		@NotNull
		public static final String COMMENT_BLOCK_OPEN = "comment-block:open";

		/**
		 * The kind of commented lines.
		 *
		 * @since 0.2.0 ~2021.05.19
		 */
		@NotNull
		public static final String COMMENT_LINE = "comment-line";
		/**
		 * The kind of the closing anchor of commented lines.
		 *
		 * @since 0.2.0 ~2021.05.19
		 */
		@NotNull
		public static final String COMMENT_LINE_CLOSE = "comment-line:close";
		/**
		 * The kind of the opening anchor of commented lines.
		 *
		 * @since 0.2.0 ~2021.05.19
		 */
		@NotNull
		public static final String COMMENT_LINE_OPEN = "comment-line:open";

		/**
		 * The kind of an injection sequence.
		 *
		 * @since 0.2.0 ~2021.05.19
		 */
		@NotNull
		public static final String INJECTION = "injection";
		/**
		 * The kind of the closing anchor of an injection.
		 *
		 * @since 0.2.0 ~2021.05.19
		 */
		@NotNull
		public static final String INJECTION_CLOSE = "injection:close";
		/**
		 * The kind of the opening anchor of an injection.
		 *
		 * @since 0.2.0 ~2021.05.19
		 */
		@NotNull
		public static final String INJECTION_OPEN = "injection:open";
		/**
		 * The kind of the parameter of an injection.
		 *
		 * @since 0.2.0 ~2021.05.23
		 */
		@NotNull
		public static final String INJECTION_PARAMETER = "injection:parameter";

		/**
		 * The kind of undefined text.
		 *
		 * @since 0.2.0 ~2021.05.21
		 */
		@NotNull
		public static final String UNDEFINED = "undefined";

		/**
		 * Utility classes must not be initialized.
		 *
		 * @throws AssertionError when called.
		 * @since 0.2.0 ~2021.05.19
		 */
		private Transient() {
			throw new AssertionError("No instance for you");
		}
	}

	/**
	 * A utility class containing the kinds for values.
	 *
	 * @author LSafer
	 * @version 0.2.0
	 * @since 0.2.0 ~2021.05.23
	 */
	public static final class Value {
		/**
		 * The kind for the addition symbol.
		 *
		 * @since 0.2.0 ~2021.05.23
		 */
		@NotNull
		public static final String ADDITION = "addition";
		/**
		 * The kind for the context of an addition operation.
		 *
		 * @since 0.2.0 ~2021.05.23
		 */
		@NotNull
		public static final String ADDITION_CONTEXT = "addition:context";

		/**
		 * The kind for arrays.
		 *
		 * @since 0.2.0 ~2021.05.23
		 */
		@NotNull
		public static final String ARRAY = "array";
		/**
		 * The kind for the division symbol.
		 *
		 * @since 0.2.0 ~2021.05.23
		 */
		@NotNull
		public static final String DIVISION = "division";
		/**
		 * The kind for the equating symbol.
		 *
		 * @since 0.2.0 ~2021.05.23
		 */
		@NotNull
		public static final String EQUALS = "equals";
		/**
		 * The kind for the less-than symbol.
		 *
		 * @since 0.2.0 ~2021.05.23
		 */
		@NotNull
		public static final String LESS_THAN = "less-than";
		/**
		 * The kind for the more-than symbol.
		 *
		 * @since 0.2.0 ~2021.05.23
		 */
		@NotNull
		public static final String MORE_THAN = "more-than";
		/**
		 * The kind for the multiplication symbol.
		 */
		@NotNull
		public static final String MULTIPLICATION = "multiplication";
		/**
		 * The kind for the not-equals symbol.
		 *
		 * @since 0.2.0 ~2021.05.23
		 */
		@NotNull
		public static final String NOT_EQUALS = "not-equals";
		/**
		 * The kind for numbers.
		 *
		 * @since 0.2.0 ~2021.05.23
		 */
		@NotNull
		public static final String NUMBER = "number";
		/**
		 * The kind for objects.
		 *
		 * @since 0.2.0 ~2021.05.23
		 */
		@NotNull
		public static final String OBJECT = "object";

		/**
		 * The kind for stirngs.
		 *
		 * @since 0.2.0 ~2021.05.23
		 */
		@NotNull
		public static final String STRING = "string";
		/**
		 * The kind for the content of the strings.
		 *
		 * @since 0.2.0 ~2021.05.23
		 */
		@NotNull
		public static final String STRING_CONTENT = "string:content";

		/**
		 * The kind for the subtraction symbol.
		 *
		 * @since 0.2.0 ~2021.05.23
		 */
		@NotNull
		public static final String SUBTRACTION = "subtraction";

		/**
		 * Utility classes must not be initialized.
		 *
		 * @throws AssertionError when called.
		 * @since 0.2.0 ~2021.05.23
		 */
		private Value() {
			throw new AssertionError("No instance for you");
		}
	}
}
