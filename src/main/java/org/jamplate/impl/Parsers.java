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
import org.jamplate.impl.parser.GroupParser;
import org.jamplate.impl.parser.PatternParser;
import org.jamplate.model.Sketch;
import org.jamplate.model.Tree;
import org.jamplate.model.function.Parser;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

/**
 * Jamplate syntax-level default implementation constants.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.18
 */
public final class Parsers {
	//CM

	/**
	 * A parser that parses comment blocks.
	 *
	 * @since 0.2.0 ~2021.05.19
	 */
	@NotNull
	public static final Parser CM_BLK = new DoublePatternParser(
			Pattern.compile("/\\*"),
			Pattern.compile("\\*/"),
			(d, r) -> new Tree(d, r, new Sketch(Kind.CM_BLK)),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.OPEN)
					 .setKind(Kind.CM_BLK_OPEN)
			)),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.CLOSE)
					 .setKind(Kind.CM_BLK_CLOSE)
			))
	);

	/**
	 * A parser that parses commented lines.
	 *
	 * @since 0.2.0 ~2021.05.19
	 */
	@NotNull
	public static final Parser CM_SLN = new DoublePatternParser(
			Pattern.compile("//"),
			Pattern.compile("(?=[\r\n]|$)"),
			(d, r) -> new Tree(d, r, new Sketch(Kind.CM_SLN)),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.OPEN)
					 .setKind(Kind.CM_SLN_OPEN),
					1
			)),
			(t, r) -> new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.CLOSE)
					 .setKind(Kind.CM_SLN_CLOSE)
			)
	);

	//CX CMD

	/**
	 * A parser that parses a single-line command.
	 *
	 * @since 0.2.0 ~2021.05.19
	 */
	@NotNull
	public static final Parser CX_CMD = new DoublePatternParser(
			Pattern.compile("(?<=^|[\r\n])#"),
			Pattern.compile("(?=[\r\n]|$)"),
			(d, r) -> new Tree(d, r, new Sketch(Kind.CX_CMD)),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.OPEN)
					 .setKind(Kind.CX_CMD_OPEN)
			)),
			(t, r) -> new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.CLOSE)
					 .setKind(Kind.CX_CMD_CLOSE)
			)
	);

	/**
	 * A parser that parses {@code #console} commands.
	 *
	 * @since 0.2.0 ~2021.05.30
	 */
	@NotNull
	public static final Parser CX_CMD_CONSOLE = new GroupParser(
			Pattern.compile("^#((?i)console)\\s?(.*)$"),
			(d, r) -> new Tree(d, r, new Sketch(Kind.CX_CMD_CONSOLE), 1),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.TYPE)
					 .setKind(Kind.CX_CMD_TYPE)
			)),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.PARAMETER)
					 .setKind(Kind.CX_PRM),
					-1
			))
	);

	/**
	 * A parser that parses {@code #declare} commands.
	 *
	 * @since 0.2.0 ~2021.05.30
	 */
	@NotNull
	public static final Parser CX_CMD_DECLARE = new GroupParser(
			Pattern.compile("^#((?i)declare)\\s(\\S+)\\s?(.*)$", Pattern.DOTALL),
			(d, r) -> new Tree(d, r, new Sketch(Kind.CX_CMD_DECLARE), 1),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.TYPE)
					 .setKind(Kind.CX_CMD_TYPE)
			)),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.KEY)
					 .setKind(Kind.CX_CMD_KEY),
					-1
			)),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.PARAMETER)
					 .setKind(Kind.CX_PRM),
					-1
			))
	);

	/**
	 * A parser that parses {@code #define} commands.
	 *
	 * @since 0.2.0 ~2021.05.30
	 */
	@NotNull
	public static final Parser CX_CMD_DEFINE = new GroupParser(
			Pattern.compile("^#((?i)define)\\s(\\S+)\\s?(.*)$"),
			(d, r) -> new Tree(d, r, new Sketch(Kind.CX_CMD_DEFINE), 1),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.TYPE)
					 .setKind(Kind.CX_CMD_TYPE)
			)),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.KEY)
					 .setKind(Kind.CX_CMD_KEY),
					-1
			)),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.PARAMETER)
					 .setKind(Kind.CX_PRM),
					-1
			))
	);

	/**
	 * A parser that parses {@code #elif} commands.
	 *
	 * @since 0.2.0 ~2021.05.30
	 */
	@NotNull
	public static final Parser CX_CMD_ELIF = new GroupParser(
			Pattern.compile("^#((?i)elif)\\s(.+)$"),
			(d, r) -> new Tree(d, r, new Sketch(Kind.CX_CMD_ELIF), 1),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.TYPE)
					 .setKind(Kind.CX_CMD_TYPE)
			)),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.PARAMETER)
					 .setKind(Kind.CX_PRM),
					-1
			))
	);

	/**
	 * A parser that parses {@code #elifdef} commands.
	 *
	 * @since 0.2.0 ~2021.05.30
	 */
	@NotNull
	public static final Parser CX_CMD_ELIFDEF = new GroupParser(
			Pattern.compile("^#((?i)elifdef)\\s(\\S+)\\s*$"),
			(d, r) -> new Tree(d, r, new Sketch(Kind.CX_CMD_ELIFDEF), 1),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.TYPE)
					 .setKind(Kind.CX_CMD_TYPE)
			)),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.KEY)
					 .setKind(Kind.CX_CMD_KEY),
					-1
			))
	);

	/**
	 * A parser that parses {@code #elifndef} commands.
	 *
	 * @since 0.2.0 ~2021.05.30
	 */
	@NotNull
	public static final Parser CX_CMD_ELIFNDEF = new GroupParser(
			Pattern.compile("^#((?i)elifndef)\\s(\\S+)\\s*$"),
			(d, r) -> new Tree(d, r, new Sketch(Kind.CX_CMD_ELIFNDEF), 1),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.TYPE)
					 .setKind(Kind.CX_CMD_TYPE)
			)),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.KEY)
					 .setKind(Kind.CX_CMD_KEY),
					-1
			))
	);

	/**
	 * A parser that parses {@code #else} commands.
	 *
	 * @since 0.2.0 ~2021.05.30
	 */
	@NotNull
	public static final Parser CX_CMD_ELSE = new GroupParser(
			Pattern.compile("^#((?i)else)\\s*$"),
			(d, r) -> new Tree(d, r, new Sketch(Kind.CX_CMD_ELSE), 1),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.TYPE)
					 .setKind(Kind.CX_CMD_TYPE)
			))
	);

	/**
	 * A parser that parses {@code #endfor} commands.
	 *
	 * @since 0.2.0 ~2021.05.30
	 */
	@NotNull
	public static final Parser CX_CMD_ENDFOR = new GroupParser(
			Pattern.compile("^#((?i)endfor)\\s*$"),
			(d, r) -> new Tree(d, r, new Sketch(Kind.CX_CMD_ENDFOR), 1),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.TYPE)
					 .setKind(Kind.CX_CMD_TYPE)
			))
	);

	/**
	 * A parser that parses {@code #endif} commands.
	 *
	 * @since 0.2.0 ~2021.05.30
	 */
	@NotNull
	public static final Parser CX_CMD_ENDIF = new GroupParser(
			Pattern.compile("^#((?i)endif)\\s*$"),
			(d, r) -> new Tree(d, r, new Sketch(Kind.CX_CMD_ENDIF), 1),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.TYPE)
					 .setKind(Kind.CX_CMD_TYPE)
			))
	);

	/**
	 * A parser that parses {@code #for} commands.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	public static final Parser CX_CMD_FOR = new GroupParser(
			Pattern.compile("^#((?i)for)\\s(\\S+)\\s?(.*)$"),
			(d, r) -> new Tree(d, r, new Sketch(Kind.CX_CMD_FOR), 1),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.TYPE)
					 .setKind(Kind.CX_CMD_TYPE)
			)),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.KEY)
					 .setKind(Kind.CX_CMD_KEY),
					-1
			)),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.PARAMETER)
					 .setKind(Kind.CX_PRM),
					-1
			))
	);

	/**
	 * A parser that parses {@code #if} commands.
	 *
	 * @since 0.2.0 ~2021.05.30
	 */
	@NotNull
	public static final Parser CX_CMD_IF = new GroupParser(
			Pattern.compile("^#((?i)if)\\s(.+)$"),
			(d, r) -> new Tree(d, r, new Sketch(Kind.CX_CMD_IF), 1),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.TYPE)
					 .setKind(Kind.CX_CMD_TYPE)
			)),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.PARAMETER)
					 .setKind(Kind.CX_PRM),
					-1
			))
	);

	/**
	 * A parser that parses {@code #ifdef} commands.
	 *
	 * @since 0.2.0 ~2021.05.30
	 */
	@NotNull
	public static final Parser CX_CMD_IFDEF = new GroupParser(
			Pattern.compile("^#((?i)ifdef)\\s(\\S+)\\s*$"),
			(d, r) -> new Tree(d, r, new Sketch(Kind.CX_CMD_IFDEF), 1),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.TYPE)
					 .setKind(Kind.CX_CMD_TYPE)
			)),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.KEY)
					 .setKind(Kind.CX_CMD_KEY),
					-1
			))
	);

	/**
	 * A parser that parses {@code #ifndef} commands.
	 *
	 * @since 0.2.0 ~2021.05.30
	 */
	@NotNull
	public static final Parser CX_CMD_IFNDEF = new GroupParser(
			Pattern.compile("^#((?i)ifndef)\\s(\\S+)\\s*$"),
			(d, r) -> new Tree(d, r, new Sketch(Kind.CX_CMD_IFNDEF), 1),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.TYPE)
					 .setKind(Kind.CX_CMD_TYPE)
			)),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.KEY)
					 .setKind(Kind.CX_CMD_KEY),
					-1
			))
	);

	/**
	 * A parser that parses {@code #include} commands.
	 *
	 * @since 0.2.0 ~2021.05.30
	 */
	@NotNull
	public static final Parser CX_CMD_INCLUDE = new GroupParser(
			Pattern.compile("^#((?i)include)\\s(.+)$"),
			(d, r) -> new Tree(d, r, new Sketch(Kind.CX_CMD_INCLUDE), 1),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.TYPE)
					 .setKind(Kind.CX_CMD_TYPE)
			)),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.PARAMETER)
					 .setKind(Kind.CX_PRM),
					-1
			))
	);

	/**
	 * A parser that parses {@code #undec} commands.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	public static final Parser CX_CMD_UNDEC = new GroupParser(
			Pattern.compile("^#((?i)undec)\\s(\\S+)\\s*$"),
			(d, r) -> new Tree(d, r, new Sketch(Kind.CX_CMD_UNDEC), 1),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.TYPE)
					 .setKind(Kind.CX_CMD_TYPE)
			)),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.KEY)
					 .setKind(Kind.CX_CMD_KEY),
					-1
			))
	);

	/**
	 * A parser that parses {@code #undef} commands.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	public static final Parser CX_CMD_UNDEF = new GroupParser(
			Pattern.compile("^#((?i)undef)\\s(\\S+)\\s*$"),
			(d, r) -> new Tree(d, r, new Sketch(Kind.CX_CMD_UNDEF), 1),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.TYPE)
					 .setKind(Kind.CX_CMD_TYPE)
			)),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.KEY)
					 .setKind(Kind.CX_CMD_KEY),
					-1
			))
	);

	//CX INJ

	/**
	 * A parser parsing injection sequences.
	 *
	 * @since 0.2.0 ~2021.05.19
	 */
	@NotNull
	public static final Parser CX_INJ = new DoublePatternParser(
			Pattern.compile("#\\{"),
			Pattern.compile("\\}#"),
			(d, r) -> new Tree(d, r, new Sketch(Kind.CX_INJ)),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.OPEN)
					 .setKind(Kind.CX_INJ_OPEN)
			)),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.CLOSE)
					 .setKind(Kind.CX_INJ_CLOSE)
			)),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.PARAMETER)
					 .setKind(Kind.CX_PRM),
					-1
			))
	);

	//DC

	/**
	 * A parser parsing line separators ({@code \n} or {@code \r} or {@code \r\n}).
	 *
	 * @since 0.2.0 ~2021.05.19
	 */
	@NotNull
	public static final Parser DC_EOL = new PatternParser(
			Pattern.compile("\r\n|\r|\n"),
			(d, r) -> new Tree(d, r, new Sketch(Kind.DC_EOL))
	);

	//OP

	/**
	 * A parser that parses addition symbols.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	@NotNull
	public static final Parser OP_ADD = new PatternParser(
			Pattern.compile("\\+"),
			(d, r) -> new Tree(d, r, new Sketch(Kind.OP_ADD))
	);

	/**
	 * A parser that parses subtraction symbols.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	@NotNull
	public static final Parser OP_SUB = new PatternParser(
			Pattern.compile("\\-"),
			(d, r) -> new Tree(d, r, new Sketch(Kind.OP_SUB))
	);

	//SX

	/**
	 * A parser parsing colons.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final Parser SX_CLN = new PatternParser(
			Pattern.compile(":"),
			(d, r) -> new Tree(d, r, new Sketch(Kind.SX_CLN))
	);

	/**
	 * A parser parsing commas.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final Parser SX_CMA = new PatternParser(
			Pattern.compile(","),
			(d, r) -> new Tree(d, r, new Sketch(Kind.SX_CMA))
	);

	/**
	 * A parser parsing curly brackets.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final Parser SX_CUR = new DoublePatternParser(
			Pattern.compile("\\{"),
			Pattern.compile("\\}"),
			(d, r) -> new Tree(d, r, new Sketch(Kind.SX_CUR)),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.OPEN)
					 .setKind(Kind.SX_CUR_OPEN)
			)),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.CLOSE)
					 .setKind(Kind.SX_CUR_CLOSE)
			))
	);

	/**
	 * A parser parsing double quotes.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final Parser SX_DQT = new DoublePatternParser(
			Pattern.compile("(?<!(?<!\\\\)\\\\)\""),
			Pattern.compile("(?<!(?<!\\\\)\\\\)\""),
			(d, r) -> new Tree(d, r, new Sketch(Kind.SX_DQT)),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.OPEN)
					 .setKind(Kind.SX_DQT_OPEN)
			)),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.CLOSE)
					 .setKind(Kind.SX_DQT_CLOSE)
			))
	);

	/**
	 * A parser that parses names.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	public static final Parser SX_NME = new PatternParser(
			Pattern.compile("[A-Za-z_$][A-Za-z_$0-9]*"),
			(d, r) -> new Tree(d, r, new Sketch(Kind.SX_NME))
	);

	/**
	 * A parser parsing numbers.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	public static final Parser SX_NUM = new PatternParser(
			Pattern.compile("(?:0[xb])?[0-9_][1-9]*[DdLlFf]?"),
			(d, r) -> new Tree(d, r, new Sketch(Kind.SX_NUM))
	);

	/**
	 * A parser parsing quotes.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final Parser SX_QTE = new DoublePatternParser(
			Pattern.compile("(?<!(?<!\\\\)\\\\)'"),
			Pattern.compile("(?<!(?<!\\\\)\\\\)'"),
			(d, r) -> new Tree(d, r, new Sketch(Kind.SX_QTE)),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.OPEN)
					 .setKind(Kind.SX_QTE_OPEN)
			)),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.CLOSE)
					 .setKind(Kind.SX_QTE_CLOSE)
			))
	);

	/**
	 * A parser parsing round brackets.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final Parser SX_RND = new DoublePatternParser(
			Pattern.compile("\\("),
			Pattern.compile("\\)"),
			(d, r) -> new Tree(d, r, new Sketch(Kind.SX_RND)),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.OPEN)
					 .setKind(Kind.SX_RND_OPEN)
			)),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.CLOSE)
					 .setKind(Kind.SX_RND_CLOSE)
			))
	);

	/**
	 * A parser parsing square brackets.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final Parser SX_SQR = new DoublePatternParser(
			Pattern.compile("\\["),
			Pattern.compile("\\]"),
			(d, r) -> new Tree(d, r, new Sketch(Kind.SX_SQR)),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.OPEN)
					 .setKind(Kind.SX_SQR_OPEN)
			)),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.CLOSE)
					 .setKind(Kind.SX_SQR_CLOSE)
			))
	);

	/**
	 * Utility classes must not be initialized.
	 *
	 * @throws AssertionError when called.
	 * @since 0.2.0 ~2021.05.16
	 */
	private Parsers() {
		throw new AssertionError("No instance for you");
	}
}
