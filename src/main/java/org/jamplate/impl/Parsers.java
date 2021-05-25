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

import org.jamplate.impl.sketch.CommandSketch;
import org.jamplate.impl.sketch.InjectionSketch;
import org.jamplate.impl.sketch.ScopeSketch;
import org.jamplate.impl.parser.LiteralParser;
import org.jamplate.impl.parser.ScopeParser;
import org.jamplate.model.Document;
import org.jamplate.model.Reference;
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
		public static final Parser COMMA = new LiteralParser(
				Patterns.COMMA
		).peek(tree -> tree.getSketch().setKind(Kind.Syntax.COMMA));

		/**
		 * A parser parsing curly brackets.
		 *
		 * @since 0.2.0 ~2021.05.16
		 */
		@NotNull
		public static final Parser CURLY = new ScopeParser(
				Patterns.CURLY_OPEN, Patterns.CURLY_CLOSE
		).peek(tree -> {
			tree.getSketch().setKind(Kind.Syntax.CURLY);
			((ScopeSketch) tree.getSketch()).getOpenAnchorSketch().setKind(Kind.Syntax.CURLY_OPEN);
			((ScopeSketch) tree.getSketch()).getCloseAnchorSketch().setKind(Kind.Syntax.CURLY_CLOSE);
		});

		/**
		 * A parser parsing double quotes.
		 *
		 * @since 0.2.0 ~2021.05.16
		 */
		@NotNull
		public static final Parser DQUOTE = new ScopeParser(
				Patterns.DQUOTE
		).peek(tree -> {
			tree.getSketch().setKind(Kind.Syntax.DQUOTE);
			((ScopeSketch) tree.getSketch()).getOpenAnchorSketch().setKind(Kind.Syntax.DQUOTE_OPEN);
			((ScopeSketch) tree.getSketch()).getCloseAnchorSketch().setKind(Kind.Syntax.DQUOTE_CLOSE);
		});

		/**
		 * A parser parsing escaped sequences.
		 *
		 * @since 0.2.0 ~2021.05.17
		 */
		@NotNull
		public static final Parser ESCAPE = new LiteralParser(
				Patterns.ESCAPE
		).peek(tree -> tree.getSketch().setKind(Kind.Syntax.ESCAPE));

		/**
		 * A parser parsing line separators ({@code \n} or {@code \r} or {@code \r\n}).
		 *
		 * @since 0.2.0 ~2021.05.19
		 */
		@NotNull
		public static final Parser LN = new LiteralParser(
				Patterns.LN
		).peek(tree -> tree.getSketch().setKind(Kind.Syntax.LN));

		/**
		 * A parser parsing quotes.
		 *
		 * @since 0.2.0 ~2021.05.16
		 */
		@NotNull
		public static final Parser QUOTE = new ScopeParser(
				Patterns.QUOTE
		).peek(tree -> {
			tree.getSketch().setKind(Kind.Syntax.QUOTE);
			((ScopeSketch) tree.getSketch()).getOpenAnchorSketch().setKind(Kind.Syntax.QUOTE_OPEN);
			((ScopeSketch) tree.getSketch()).getCloseAnchorSketch().setKind(Kind.Syntax.QUOTE_CLOSE);
		});

		/**
		 * A parser parsing round brackets.
		 *
		 * @since 0.2.0 ~2021.05.16
		 */
		@NotNull
		public static final Parser ROUND = new ScopeParser(
				Patterns.ROUND_OPEN, Patterns.ROUND_CLOSE
		).peek(tree -> {
			tree.getSketch().setKind(Kind.Syntax.ROUND);
			((ScopeSketch) tree.getSketch()).getOpenAnchorSketch().setKind(Kind.Syntax.ROUND_OPEN);
			((ScopeSketch) tree.getSketch()).getCloseAnchorSketch().setKind(Kind.Syntax.ROUND_CLOSE);
		});

		/**
		 * A parser parsing square brackets.
		 *
		 * @since 0.2.0 ~2021.05.16
		 */
		@NotNull
		public static final Parser SQUARE = new ScopeParser(
				Patterns.SQUARE_OPEN, Patterns.SQUARE_CLOSE
		).peek(tree -> {
			tree.getSketch().setKind(Kind.Syntax.SQUARE);
			((ScopeSketch) tree.getSketch()).getOpenAnchorSketch().setKind(Kind.Syntax.SQUARE_OPEN);
			((ScopeSketch) tree.getSketch()).getCloseAnchorSketch().setKind(Kind.Syntax.SQUARE_CLOSE);
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
		public static final Parser COMMAND = new ScopeParser(
				CommandSketch::new, Patterns.COMMAND_OPEN, Patterns.COMMAND_CLOSE
		).peek(tree -> {
			CommandSketch sketch = (CommandSketch) tree.getSketch();
			sketch.setKind(Kind.Transient.COMMAND);
			sketch.getOpenAnchorSketch().setKind(Kind.Transient.COMMAND_OPEN);
			sketch.getCloseAnchorSketch().setKind(Kind.Transient.COMMAND_CLOSE);
			sketch.getCloseAnchorSketch().getTree().pop();
			sketch.getTypeSketch().setKind(Kind.Transient.COMMAND_TYPE);
			sketch.getParameterSketch().setKind(Kind.Transient.COMMAND_PARAMETER);
			sketch.getParameterSketch().getKeySketch().setKind(Kind.Transient.COMMAND_PARAMETER_KEY);
			sketch.getParameterSketch().getValueSketch().setKind(Kind.Transient.COMMAND_PARAMETER_VALUE);
		})
		 .peek(tree -> {
			 //define the trees of `type` and `parameter`
			 CommandSketch sketch = (CommandSketch) tree.getSketch();
			 Document document = tree.document();
			 Reference open = sketch.getOpenAnchorSketch().getTree().reference();
			 Reference close = sketch.getCloseAnchorSketch().getTree().reference();
			 int position = open.position() + open.length();
			 int length = close.position() - position;

			 int middle = document.read(new Reference(position, length))
								  .toString()
								  .indexOf(' ');

			 Tree t = new Tree(document, new Reference(
					 position,
					 middle == -1 ? length : middle
			 ), sketch.getTypeSketch());
			 Tree p = new Tree(document, new Reference(
					 middle == -1 ? position + length : position + middle,
					 middle == -1 ? 0 : length - middle
			 ), sketch.getParameterSketch());

			 sketch.getTypeSketch().setTree(t);
			 sketch.getParameterSketch().setTree(p);

			 if (t.reference().length() != 0)
				 tree.offer(t);
			 if (p.reference().length() != 0)
				 tree.offer(p);
		 })
		 .peek(tree -> {
			 //define `key` and `value`
			 CommandSketch sketch = (CommandSketch) tree.getSketch();
			 Document document = tree.document();
			 Reference parameter = sketch.getParameterSketch().getTree().reference();

			 int position = parameter.position();
			 int length = parameter.length();
			 int p = length == 0 ? position : position + 1;
			 int l = length == 0 ? 0 : length - 1;
			 int middle = document.read(new Reference(p, l))
								  .toString()
								  .indexOf(' ');

			 Tree k = new Tree(document, new Reference(
					 p,
					 middle == -1 ? l : middle
			 ), sketch.getParameterSketch().getKeySketch());
			 Tree v = new Tree(document, new Reference(
					 middle == -1 ? p + l : p + middle,
					 middle == -1 ? 0 : l - middle
			 ), sketch.getParameterSketch().getValueSketch());

			 sketch.getParameterSketch().getKeySketch().setTree(k);
			 sketch.getParameterSketch().getValueSketch().setTree(v);
		 });

		/**
		 * A parser that parses comment blocks.
		 *
		 * @since 0.2.0 ~2021.05.19
		 */
		@NotNull
		public static final Parser COMMENT_BLOCK = new ScopeParser(
				Patterns.COMMENT_BLOCK_OPEN, Patterns.COMMENT_BLOCK_CLOSE
		).peek(tree -> {
			tree.getSketch().setKind(Kind.Transient.COMMENT_BLOCK);
			((ScopeSketch) tree.getSketch()).getOpenAnchorSketch().setKind(Kind.Transient.COMMENT_BLOCK_OPEN);
			((ScopeSketch) tree.getSketch()).getCloseAnchorSketch().setKind(Kind.Transient.COMMENT_BLOCK_CLOSE);
		});

		/**
		 * A parser that parses commented lines.
		 *
		 * @since 0.2.0 ~2021.05.19
		 */
		@NotNull
		public static final Parser COMMENT_LINE = new ScopeParser(
				Patterns.COMMENT_LINE_OPEN, Patterns.COMMENT_LINE_CLOSE
		).peek(tree -> {
			tree.getSketch().setKind(Kind.Transient.COMMENT_LINE);
			((ScopeSketch) tree.getSketch()).getOpenAnchorSketch().setKind(Kind.Transient.COMMENT_LINE_OPEN);
			((ScopeSketch) tree.getSketch()).getCloseAnchorSketch().setKind(Kind.Transient.COMMENT_LINE_CLOSE);
			((ScopeSketch) tree.getSketch()).getCloseAnchorSketch().getTree().pop();
		});

		/**
		 * A parser parsing injection sequences.
		 *
		 * @since 0.2.0 ~2021.05.19
		 */
		@SuppressWarnings("OverlyLongLambda")
		@NotNull
		public static final Parser INJECTION = new ScopeParser(
				InjectionSketch::new, Patterns.INJECTION_OPEN, Patterns.INJECTION_CLOSE
		).peek(tree -> {
			InjectionSketch sketch = (InjectionSketch) tree.getSketch();
			sketch.setKind(Kind.Transient.INJECTION);
			sketch.getOpenAnchorSketch().setKind(Kind.Transient.INJECTION_OPEN);
			sketch.getCloseAnchorSketch().setKind(Kind.Transient.INJECTION_CLOSE);
			sketch.getParameterSketch().setKind(Kind.Transient.INJECTION_PARAMETER);
		})
		 .peek(tree -> {
			 //define the trees of `type` and `parameter`
			 InjectionSketch sketch = (InjectionSketch) tree.getSketch();
			 Document document = tree.document();
			 Reference open = sketch.getOpenAnchorSketch().getTree().reference();
			 Reference close = sketch.getCloseAnchorSketch().getTree().reference();
			 int position = open.position() + open.length();
			 int length = close.position() - position;

			 Tree p = new Tree(document, new Reference(
					 position,
					 length
			 ), sketch.getParameterSketch());

			 sketch.getParameterSketch().setTree(p);

			 if (p.reference().length() != 0)
				 tree.offer(p);
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
		public static final Parser NUMBER = new LiteralParser(
				Patterns.NUMBER
		).peek(tree -> tree.getSketch().setKind(Kind.Value.NUMBER));

		/**
		 * A parser that parses references.
		 *
		 * @since 0.2.0 ~2021.05.24
		 */
		@NotNull
		public static final Parser REFERENCE = new LiteralParser(
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
					case Kind.Transient.COMMAND_PARAMETER:
					case Kind.Transient.COMMAND_PARAMETER_VALUE:
					case Kind.Transient.INJECTION_PARAMETER:
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
		public static final Parser ADDITION = new LiteralParser(
				Patterns.ADDITION
		).condition(Values.CONDITION)
		 .peek(tree -> tree.getSketch().setKind(Kind.Value.ADDITION));

		/**
		 * A parser that parses subtraction symbols.
		 *
		 * @since 0.2.0 ~2021.05.23
		 */
		@NotNull
		public static final Parser SUBTRACTION = new LiteralParser(
				Patterns.SUBTRACTION
		).condition(Values.CONDITION)
		 .peek(tree -> tree.getSketch().setKind(Kind.Value.SUBTRACTION));

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
			public static final Pattern REFERENCE = Pattern.compile("[A-Za-z][A-Za-z0-9_$]*");

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
