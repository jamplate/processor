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
package org.jamplate.impl.process;

import org.jamplate.model.function.Parser;
import org.jamplate.model.function.Processor;
import org.jamplate.impl.model.Command;
import org.jamplate.impl.model.Kind;
import org.jamplate.impl.model.Scope;
import org.jamplate.impl.util.LiteralParser;
import org.jamplate.impl.util.ScopeParser;
import org.jamplate.model.Document;
import org.jamplate.model.Reference;
import org.jamplate.model.Tree;
import org.jamplate.util.model.function.*;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

/**
 * Jamplate syntax-level default implementation constants.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.18
 */
public final class JamplateParse {
	/**
	 * An all-in-one parser used by the jamplate default implementation.
	 *
	 * @since 0.2.0 ~2021.05.17
	 */
	@NotNull
	public static final Parser PARSER =
			new CollectParser(new OrderParser(
					JamplateParse.Syntax.LN,
					JamplateParse.Transient.COMMENT_LINE,
					new MergeParser(new CombineParser(
							JamplateParse.Transient.COMMENT_BLOCK,
							new CombineParser(
									JamplateParse.Syntax.QUOTE,
									JamplateParse.Syntax.DQUOTE
							).also(JamplateParse.Syntax.ESCAPE)
					)),
					new FlatOrderParser(
							JamplateParse.Transient.INJECTION,
							JamplateParse.Transient.COMMAND
					),
					new MergeParser(new CombineParser(
							JamplateParse.Syntax.CURLY,
							JamplateParse.Syntax.SQUARE,
							JamplateParse.Syntax.ROUND
					))
			));

	/**
	 * A parser that fully parses the compilations passed to it using the default jamplate
	 * implementation parser.
	 *
	 * @since 0.2.0 ~2021.05.18
	 */
	@NotNull
	public static final Processor PROCESSOR =
			new ParserProcessor(JamplateParse.PARSER);

	/**
	 * Utility classes must not be initialized.
	 *
	 * @throws AssertionError when called.
	 * @since 0.2.0 ~2021.05.16
	 */
	private JamplateParse() {
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
			((Scope) tree.getSketch()).getOpenAnchor().setKind(Kind.Syntax.CURLY_OPEN);
			((Scope) tree.getSketch()).getCloseAnchor().setKind(Kind.Syntax.CURLY_CLOSE);
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
			((Scope) tree.getSketch()).getOpenAnchor().setKind(Kind.Syntax.DQUOTE_OPEN);
			((Scope) tree.getSketch()).getCloseAnchor().setKind(Kind.Syntax.DQUOTE_CLOSE);
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
			((Scope) tree.getSketch()).getOpenAnchor().setKind(Kind.Syntax.QUOTE_OPEN);
			((Scope) tree.getSketch()).getCloseAnchor().setKind(Kind.Syntax.QUOTE_CLOSE);
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
			((Scope) tree.getSketch()).getOpenAnchor().setKind(Kind.Syntax.ROUND_OPEN);
			((Scope) tree.getSketch()).getCloseAnchor().setKind(Kind.Syntax.ROUND_CLOSE);
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
			((Scope) tree.getSketch()).getOpenAnchor().setKind(Kind.Syntax.SQUARE_OPEN);
			((Scope) tree.getSketch()).getCloseAnchor().setKind(Kind.Syntax.SQUARE_CLOSE);
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
	public static final class Transient {
		/**
		 * A parser that parses a single-line command.
		 *
		 * @since 0.2.0 ~2021.05.19
		 */
		@SuppressWarnings("OverlyLongLambda")
		@NotNull
		public static final Parser COMMAND = new ScopeParser(
				Command::new, Patterns.COMMAND_OPEN, Patterns.COMMAND_CLOSE
		).peek(tree -> {
			Command sketch = (Command) tree.getSketch();
			sketch.setKind(Kind.Transient.COMMAND);
			sketch.getOpenAnchor().setKind(Kind.Transient.COMMAND_OPEN);
			sketch.getCloseAnchor().setKind(Kind.Transient.COMMAND_CLOSE);
			sketch.getType().setKind(Kind.Transient.COMMAND_TYPE);
			sketch.getParameter().setKind(Kind.Transient.COMMAND_PARAMETER);

			//define the trees of `type` and `parameter`
			Document document = tree.document();
			Reference open = sketch.getOpenAnchor().getTree().reference();
			Reference close = sketch.getCloseAnchor().getTree().reference();
			int position = open.position() + open.length();
			int length = close.position() - position;

			int middle = document.read(new Reference(position, length))
								 .toString()
								 .indexOf(' ');

			Tree t = new Tree(document, new Reference(
					position,
					middle == -1 ? length : middle
			), sketch.getType());
			Tree p = new Tree(document, new Reference(
					middle == -1 ? position + length : position + middle + 1,
					middle == -1 ? 0 : length - middle - 1
			), sketch.getParameter());

			sketch.getType().setTree(t);
			sketch.getParameter().setTree(p);

			if (t.reference().length() != 0)
				tree.offer(t);
			if (p.reference().length() != 0)
				tree.offer(p);
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
			((Scope) tree.getSketch()).getOpenAnchor().setKind(Kind.Transient.COMMENT_BLOCK_OPEN);
			((Scope) tree.getSketch()).getCloseAnchor().setKind(Kind.Transient.COMMENT_BLOCK_CLOSE);
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
			((Scope) tree.getSketch()).getOpenAnchor().setKind(Kind.Transient.COMMENT_LINE_OPEN);
			((Scope) tree.getSketch()).getCloseAnchor().setKind(Kind.Transient.COMMENT_LINE_CLOSE);
		});

		/**
		 * A parser parsing injection sequences.
		 *
		 * @since 0.2.0 ~2021.05.19
		 */
		@NotNull
		public static final Parser INJECTION = new ScopeParser(
				Patterns.INJECTION_OPEN, Patterns.INJECTION_CLOSE
		).peek(tree -> {
			tree.getSketch().setKind(Kind.Transient.INJECTION);
			((Scope) tree.getSketch()).getOpenAnchor().setKind(Kind.Transient.INJECTION_OPEN);
			((Scope) tree.getSketch()).getCloseAnchor().setKind(Kind.Transient.INJECTION_CLOSE);
		});

		/**
		 * Utility classes must not be initialized.
		 *
		 * @throws AssertionError when called.
		 * @since 0.2.0 ~2021.05.19
		 */
		private Transient() {
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
}
