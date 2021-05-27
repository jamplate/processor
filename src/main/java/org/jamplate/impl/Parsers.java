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

import org.jamplate.impl.parser.DoublePatternParser;
import org.jamplate.impl.parser.InjectionParser;
import org.jamplate.impl.parser.PatternParser;
import org.jamplate.impl.parser.CommandParser;
import org.jamplate.model.Sketch;
import org.jamplate.model.Tree;
import org.jamplate.model.function.Parser;
import org.jamplate.model.function.Processor;
import org.jamplate.util.model.function.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * Jamplate syntax-level default implementation constants.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.18
 */
public final class Parsers {
	/**
	 * An all-in-one parser used by the jamplate default implementation.
	 *
	 * @since 0.2.0 ~2021.05.17
	 */
	@NotNull
	public static final Parser PARSER =
			new CollectParser(new OrderParser(
					Syntax.LN,
					Transients.COMMENT_LINE,
					new MergeParser(new CombineParser(
							Transients.COMMENT_BLOCK,
							new CombineParser(
									Syntax.QUOTE,
									Syntax.DQUOTE
							).also(Syntax.ESCAPE)
					)),
					new FlatOrderParser(
							Transients.INJECTION,
							Transients.COMMAND
					),
					new MergeParser(new CombineParser(
							Syntax.CURLY,
							Syntax.SQUARE,
							Syntax.ROUND
					)),
					Values.REFERENCE,
					Values.NUMBER
			));

	/**
	 * A parser that fully parses the compilations passed to it using the default jamplate
	 * implementation parser.
	 *
	 * @since 0.2.0 ~2021.05.18
	 */
	@NotNull
	public static final Processor PROCESSOR =
			new ParserProcessor(Parsers.PARSER);

	/**
	 * Utility classes must not be initialized.
	 *
	 * @throws AssertionError when called.
	 * @since 0.2.0 ~2021.05.16
	 */
	private Parsers() {
		throw new AssertionError("No instance for you");
	}

	/**
	 * The processors the jamplate default implementation offers for parsing.
	 *
	 * @author LSafer
	 * @version 0.2.0
	 * @since 0.2.0 ~2021.05.16
	 */
	public static final class Syntax {
		/**
		 * A parser parsing commas.
		 *
		 * @since 0.2.0 ~2021.05.16
		 */
		@NotNull
		public static final Parser COMMA = new PatternParser(
				Patterns.COMMA
		).peek(tree -> tree.getSketch().setKind(Kind.Syntax.COMMA));

		/**
		 * A parser parsing curly brackets.
		 *
		 * @since 0.2.0 ~2021.05.16
		 */
		@NotNull
		public static final Parser CURLY = new DoublePatternParser(
				Patterns.CURLY_OPEN, Patterns.CURLY_CLOSE
		).peek(tree -> {
			tree.getSketch().setKind(Kind.Syntax.CURLY);
			tree.getSketch().get(Component.OPEN).setKind(Kind.Syntax.CURLY_OPEN);
			tree.getSketch().get(Component.CLOSE).setKind(Kind.Syntax.CURLY_CLOSE);
		});

		/**
		 * A parser parsing double quotes.
		 *
		 * @since 0.2.0 ~2021.05.16
		 */
		@NotNull
		public static final Parser DQUOTE = new DoublePatternParser(
				Patterns.DQUOTE
		).peek(tree -> {
			tree.getSketch().setKind(Kind.Syntax.DQUOTE);
			tree.getSketch().get(Component.OPEN).setKind(Kind.Syntax.DQUOTE_OPEN);
			tree.getSketch().get(Component.CLOSE).setKind(Kind.Syntax.DQUOTE_CLOSE);
		});

		/**
		 * A parser parsing escaped sequences.
		 *
		 * @since 0.2.0 ~2021.05.17
		 */
		@NotNull
		public static final Parser ESCAPE = new PatternParser(
				Patterns.ESCAPE
		).peek(tree -> tree.getSketch().setKind(Kind.Syntax.ESCAPE));

		/**
		 * A parser parsing line separators ({@code \n} or {@code \r} or {@code \r\n}).
		 *
		 * @since 0.2.0 ~2021.05.19
		 */
		@NotNull
		public static final Parser LN = new PatternParser(
				Patterns.LN
		).peek(tree -> tree.getSketch().setKind(Kind.Syntax.LN));

		/**
		 * A parser parsing quotes.
		 *
		 * @since 0.2.0 ~2021.05.16
		 */
		@NotNull
		public static final Parser QUOTE = new DoublePatternParser(
				Patterns.QUOTE
		).peek(tree -> {
			tree.getSketch().setKind(Kind.Syntax.QUOTE);
			tree.getSketch().get(Component.OPEN).setKind(Kind.Syntax.QUOTE_OPEN);
			tree.getSketch().get(Component.CLOSE).setKind(Kind.Syntax.QUOTE_CLOSE);
		});

		/**
		 * A parser parsing round brackets.
		 *
		 * @since 0.2.0 ~2021.05.16
		 */
		@NotNull
		public static final Parser ROUND = new DoublePatternParser(
				Patterns.ROUND_OPEN, Patterns.ROUND_CLOSE
		).peek(tree -> {
			tree.getSketch().setKind(Kind.Syntax.ROUND);
			tree.getSketch().get(Component.OPEN).setKind(Kind.Syntax.ROUND_OPEN);
			tree.getSketch().get(Component.CLOSE).setKind(Kind.Syntax.ROUND_CLOSE);
		});

		/**
		 * A parser parsing square brackets.
		 *
		 * @since 0.2.0 ~2021.05.16
		 */
		@NotNull
		public static final Parser SQUARE = new DoublePatternParser(
				Patterns.SQUARE_OPEN, Patterns.SQUARE_CLOSE
		).peek(tree -> {
			tree.getSketch().setKind(Kind.Syntax.SQUARE);
			tree.getSketch().get(Component.OPEN).setKind(Kind.Syntax.SQUARE_OPEN);
			tree.getSketch().get(Component.CLOSE).setKind(Kind.Syntax.SQUARE_CLOSE);
		});

		/**
		 * Utility classes must not be initialized.
		 *
		 * @throws AssertionError when called.
		 * @since 0.2.0 ~2021.05.16
		 */
		private Syntax() {
			throw new AssertionError("No instance for you");
		}

		/**
		 * A utility class containing the patterns the default jamplate implementation
		 * offers.
		 *
		 * @author LSafer
		 * @version 0.2.0
		 * @since 0.2.0 ~2021.05.16
		 */
		public static final class Patterns {
			/**
			 * A pattern matching commas.
			 *
			 * @since 0.2.0 ~2021.05.16
			 */
			@NotNull
			public static final Pattern COMMA = Pattern.compile(",");

			/**
			 * A pattern matching closing curly brackets.
			 *
			 * @since 0.2.0 ~2021.05.16
			 */
			@NotNull
			public static final Pattern CURLY_CLOSE = Pattern.compile("\\}");
			/**
			 * A pattern matching opening curly brackets.
			 *
			 * @since 0.2.0 ~2021.05.16
			 */
			@NotNull
			public static final Pattern CURLY_OPEN = Pattern.compile("\\{");

			/**
			 * A pattern matching double quotes.
			 *
			 * @since 0.2.0 ~2021.05.16
			 */
			@NotNull
			public static final Pattern DQUOTE = Pattern.compile("(?<!(?<!\\\\)\\\\)\"");

			/**
			 * A pattern matching escaped sequences.
			 *
			 * @since 0.2.0 ~2021.05.17
			 */
			@NotNull
			public static final Pattern ESCAPE = Pattern.compile("\\\\.");

			/**
			 * A pattern matching line separators ({@code \n} or {@code \r} or {@code
			 * \r\n}).
			 *
			 * @since 0.2.0 ~2021.05.19
			 */
			@NotNull
			public static final Pattern LN = Pattern.compile("\r\n|\r|\n");

			/**
			 * A pattern matching quotes.
			 *
			 * @since 0.2.0 ~2021.05.16
			 */
			@NotNull
			public static final Pattern QUOTE = Pattern.compile("(?<!(?<!\\\\)\\\\)'");

			/**
			 * A pattern matching closing round brackets.
			 *
			 * @since 0.2.0 ~2021.05.16
			 */
			@NotNull
			public static final Pattern ROUND_CLOSE = Pattern.compile("\\)");
			/**
			 * A pattern matching opening round brackets.
			 *
			 * @since 0.2.0 ~2021.05.16
			 */
			@NotNull
			public static final Pattern ROUND_OPEN = Pattern.compile("\\(");

			/**
			 * A pattern matching closing square brackets.
			 *
			 * @since 0.2.0 ~2021.05.16
			 */
			@NotNull
			public static final Pattern SQUARE_CLOSE = Pattern.compile("\\]");
			/**
			 * A pattern matching opening square brackets.
			 *
			 * @since 0.2.0 ~2021.05.16
			 */
			@NotNull
			public static final Pattern SQUARE_OPEN = Pattern.compile("\\[");

			/**
			 * Utility classes must not be initialized.
			 *
			 * @throws AssertionError when called.
			 * @since 0.2.0 ~2021.05.16
			 */
			private Patterns() {
				throw new AssertionError("No instance for you");
			}
		}
	}

	/**
	 * Parsers that parses sketches that to be elements.
	 *
	 * @author LSafer
	 * @version 0.2.0
	 * @since 0.2.0 ~2021.05.19
	 */
	public static final class Transients {
		/**
		 * A parser that parses a single-line command.
		 *
		 * @since 0.2.0 ~2021.05.19
		 */
		@SuppressWarnings("OverlyLongLambda")
		@NotNull
		public static final Parser COMMAND = new CommandParser(
				Patterns.COMMAND_OPEN, Patterns.COMMAND_CLOSE
		).peek(tree -> {
			Sketch sketch = tree.getSketch();
			sketch.setKind(Kind.COMMAND);
			sketch.get(Component.OPEN).setKind(Kind.COMMAND_OPEN);
			sketch.get(Component.CLOSE).setKind(Kind.COMMAND_CLOSE);
			sketch.get(Component.CLOSE).getTree().pop();
			sketch.get(Component.TYPE).setKind(Kind.COMMAND_TYPE);
			sketch.get(Component.PARAMETER).setKind(Kind.COMMAND_PARAMETER);
			sketch.get(Component.PARAMETER).get(Component.KEY).setKind(Kind.COMMAND_PARAMETER_KEY);
			sketch.get(Component.PARAMETER).get(Component.VALUE).setKind(Kind.COMMAND_PARAMETER_VALUE);
		});

		/**
		 * A parser that parses comment blocks.
		 *
		 * @since 0.2.0 ~2021.05.19
		 */
		@NotNull
		public static final Parser COMMENT_BLOCK = new DoublePatternParser(
				Patterns.COMMENT_BLOCK_OPEN, Patterns.COMMENT_BLOCK_CLOSE
		).peek(tree -> {
			tree.getSketch().setKind(Kind.COMMENT_BLOCK);
			tree.getSketch().get(Component.OPEN).setKind(Kind.COMMENT_BLOCK_OPEN);
			tree.getSketch().get(Component.CLOSE).setKind(Kind.COMMENT_BLOCK_CLOSE);
		});

		/**
		 * A parser that parses commented lines.
		 *
		 * @since 0.2.0 ~2021.05.19
		 */
		@NotNull
		public static final Parser COMMENT_LINE = new DoublePatternParser(
				Patterns.COMMENT_LINE_OPEN, Patterns.COMMENT_LINE_CLOSE
		).peek(tree -> {
			tree.getSketch().setKind(Kind.COMMENT_LINE);
			tree.getSketch().get(Component.OPEN).setKind(Kind.COMMENT_LINE_OPEN);
			tree.getSketch().get(Component.CLOSE).setKind(Kind.COMMENT_LINE_CLOSE);
			tree.getSketch().get(Component.OPEN).getTree().pop();
		});

		/**
		 * A parser parsing injection sequences.
		 *
		 * @since 0.2.0 ~2021.05.19
		 */
		@NotNull
		public static final Parser INJECTION = new InjectionParser(
				Patterns.INJECTION_OPEN, Patterns.INJECTION_CLOSE
		).peek(tree -> {
			Sketch sketch = tree.getSketch();
			sketch.setKind(Kind.INJECTION);
			sketch.get(Component.OPEN).setKind(Kind.INJECTION_OPEN);
			sketch.get(Component.CLOSE).setKind(Kind.INJECTION_CLOSE);
			sketch.get(Component.PARAMETER).setKind(Kind.INJECTION_PARAMETER);
		});

		/**
		 * Utility classes must not be initialized.
		 *
		 * @throws AssertionError when called.
		 * @since 0.2.0 ~2021.05.19
		 */
		private Transients() {
			throw new AssertionError("No instance for you");
		}

		/**
		 * A utility class containing the patterns of the components that to be elements.
		 *
		 * @author LSafer
		 * @version 0.2.0
		 * @since 0.2.0 ~2021.05.19
		 */
		public static final class Patterns {
			/**
			 * A pattern matching the ending anchor of a single-line command.
			 *
			 * @since 0.2.0 ~2021.05.19
			 */
			@NotNull
			public static final Pattern COMMAND_CLOSE = Pattern.compile("(?=[\r\n]|$)");
			/**
			 * A pattern matching the opening anchor of a single-line command.
			 *
			 * @since 0.2.0 ~2021.05.19
			 */
			@NotNull
			public static final Pattern COMMAND_OPEN = Pattern.compile("(?<=^|[\n\r])#");

			/**
			 * A pattern that matches the opening of comment blocks.
			 *
			 * @since 0.2.0 ~2021.05.19
			 */
			@NotNull
			public static final Pattern COMMENT_BLOCK_CLOSE = Pattern.compile("\\*/");
			/**
			 * A pattern that matches the closing of comment blocks.
			 *
			 * @since 0.2.0 ~2021.05.19
			 */
			@NotNull
			public static final Pattern COMMENT_BLOCK_OPEN = Pattern.compile("/\\*");

			/**
			 * A pattern that matches the closing of commented lines.
			 *
			 * @since 0.2.0 ~2021.05.19
			 */
			@NotNull
			public static final Pattern COMMENT_LINE_CLOSE = Pattern.compile("(?=[\r\n]|$)");
			/**
			 * A pattern that matches the opening of commented lines.
			 *
			 * @since 0.2.0 ~2021.05.19
			 */
			@NotNull
			public static final Pattern COMMENT_LINE_OPEN = Pattern.compile("//");

			/**
			 * A pattern matching the ending anchor of an injection sequence.
			 *
			 * @since 0.2.0 ~2021.05.19
			 */
			@NotNull
			public static final Pattern INJECTION_CLOSE = Pattern.compile("\\}#");
			/**
			 * A pattern matching the opening anchor of an injection sequence.
			 *
			 * @since 0.2.0 ~2021.05.19
			 */
			@NotNull
			public static final Pattern INJECTION_OPEN = Pattern.compile("#\\{");

			/**
			 * Utility classes must not be initialized.
			 *
			 * @throws AssertionError when called.
			 * @since 0.2.0 ~2021.05.19
			 */
			private Patterns() {
				throw new AssertionError("No instance for you");
			}
		}
	}

	/**
	 * A class containing logical-context components.
	 *
	 * @author LSafer
	 * @version 0.2.0
	 * @since 0.2.0 ~2021.05.23
	 */
	public static final class Values {
		/**
		 * A parser parsing numbers.
		 *
		 * @since 0.2.0 ~2021.05.25
		 */
		@NotNull
		public static final Parser NUMBER = new PatternParser(
				Patterns.NUMBER
		).peek(tree -> tree.getSketch().setKind(Kind.Value.NUMBER));

		/**
		 * A parser that parses references.
		 *
		 * @since 0.2.0 ~2021.05.24
		 */
		@NotNull
		public static final Parser REFERENCE = new PatternParser(
				Patterns.REFERENCE
		).peek(tree -> tree.getSketch().setKind(Kind.Value.REFERENCE));

		/**
		 * A predicate that returns {@code true} if the tree provided to it has a parent
		 * with a kind that is known to have a logical context.
		 *
		 * @since 0.2.0 ~2021.05.23
		 */
		@NotNull
		private static final Predicate<Tree> CONDITION = tree -> {
			for (
					Tree parent = tree.getParent();
					parent != null;
					parent = parent.getParent()
			)
				switch (parent.getSketch().getKind()) {
					case Kind.COMMAND_PARAMETER:
					case Kind.COMMAND_PARAMETER_VALUE:
					case Kind.INJECTION_PARAMETER:
						return true;
				}

			return false;
		};

		/**
		 * A parser that parses addition symbols.
		 *
		 * @since 0.2.0 ~2021.05.23
		 */
		@NotNull
		public static final Parser ADDITION = new PatternParser(
				Patterns.ADDITION
		).condition(Values.CONDITION)
		 .peek(tree -> tree.getSketch().setKind(Kind.Operator.ADDITION));

		/**
		 * A parser that parses subtraction symbols.
		 *
		 * @since 0.2.0 ~2021.05.23
		 */
		@NotNull
		public static final Parser SUBTRACTION = new PatternParser(
				Patterns.SUBTRACTION
		).condition(Values.CONDITION)
		 .peek(tree -> tree.getSketch().setKind(Kind.Operator.SUBTRACTION));

		/**
		 * Utility classes must not be initialized.
		 *
		 * @throws AssertionError when called.
		 * @since 0.2.0 ~2021.05.23
		 */
		private Values() {
			throw new AssertionError("No instance for you");
		}

		/**
		 * The patterns used by the class {@link Values}.
		 *
		 * @author LSafer
		 * @version 0.2.0
		 * @since 0.2.0 ~2021.05.23
		 */
		public static final class Patterns {
			/**
			 * A pattern matching addition symbols.
			 *
			 * @since 0.2.0 ~2021.05.23
			 */
			@NotNull
			public static final Pattern ADDITION = Pattern.compile("\\+");

			/**
			 * A pattern matching numbers.
			 *
			 * @since 0.2.0 ~2021.05.25
			 */
			@NotNull
			public static final Pattern NUMBER = Pattern.compile(
					"(?:0[xb])?[0-9_][1-9]*[DdLlFf]?"
			);

			/**
			 * A pattern matching references.
			 *
			 * @since 0.2.0 ~2021.05.24
			 */
			@NotNull
			public static final Pattern REFERENCE = Pattern.compile("[A-Za-z_$][A-Za-z_$0-9]*");

			/**
			 * A pattern matching subtraction symbols.
			 *
			 * @since 0.2.0 ~2021.05.23
			 */
			@NotNull
			public static final Pattern SUBTRACTION = Pattern.compile("\\-");

			/**
			 * Utility classes must not be initialized.
			 *
			 * @throws AssertionError when called.
			 * @since 0.2.0 ~2021.05.23
			 */
			private Patterns() {
				throw new AssertionError("No instance for you");
			}
		}
	}
}
