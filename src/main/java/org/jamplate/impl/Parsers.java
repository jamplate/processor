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

import org.jamplate.internal.util.parser.pattern.PatternGroupParser;
import org.jamplate.impl.parser.PatternParser;
import org.jamplate.internal.util.parser.pattern.PatternRangeParser;
import org.jamplate.model.Sketch;
import org.jamplate.model.Tree;
import org.jamplate.function.Parser;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

/**
 * Jamplate syntax-level default implementation constants.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.18
 */
@Deprecated
public final class Parsers {
	//CM

	/**
	 * A parser that parses comment blocks.
	 *
	 * @since 0.2.0 ~2021.05.19
	 */
	@NotNull
	public static final Parser CM_BLK = new EnclosureParser(
			Pattern.compile("/\\*"),
			Pattern.compile("\\*/"),
			(d, r) -> new Tree(d, r, new Sketch(Kind.CM_BLK)),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.OPEN)
					 .setKind(Kind.CX_ANC_OPEN)
			)),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.CLOSE)
					 .setKind(Kind.CX_ANC_CLOSE)
			))
	);

	/**
	 * A parser that parses commented lines.
	 *
	 * @since 0.2.0 ~2021.05.19
	 */
	@NotNull
	public static final Parser CM_SLN = new EnclosureParser(
			Pattern.compile("//"),
			Pattern.compile("(?=[\r\n]|$)"),
			(d, r) -> new Tree(d, r, new Sketch(Kind.CM_SLN)),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.OPEN)
					 .setKind(Kind.CX_ANC_OPEN),
					1
			)),
			(t, r) -> new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.CLOSE)
					 .setKind(Kind.CX_ANC_CLOSE)
			)
	);

	//CX CMD

	/**
	 * A parser that parses a single-line command.
	 *
	 * @since 0.2.0 ~2021.05.19
	 */
	@NotNull
	public static final Parser CX_CMD = new EnclosureParser(
			Pattern.compile("(?<=^|[\r\n])[\\t ]*#"),
			Pattern.compile("(?=[\r\n]|$)"),
			(d, r) -> new Tree(d, r, new Sketch(Kind.CX_CMD)),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.OPEN)
					 .setKind(Kind.CX_ANC_OPEN)
			)),
			(t, r) -> new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.CLOSE)
					 .setKind(Kind.CX_ANC_CLOSE)
			)
	);

	/**
	 * A parser that parses {@code #capture} commands.
	 *
	 * @since 0.2.0 ~2021.06.01
	 */
	@NotNull
	public static final Parser CX_CMD_CAPTURE = new PatternGroupParser(
			Pattern.compile("^[\\t ]*#((?i)capture)\\s(\\S+)\\s*$"),
			(d, r) -> new Tree(d, r, new Sketch(Kind.CX_CMD_CAPTURE), 1),
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
	 * A parser that parses {@code #console} commands.
	 *
	 * @since 0.2.0 ~2021.05.30
	 */
	@NotNull
	public static final Parser CX_CMD_CONSOLE = new PatternGroupParser(
			Pattern.compile("^[\\t ]*#((?i)console)\\s?(.*)$"),
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
					-100
			))
	);

	/**
	 * A parser that parses {@code #declare} commands.
	 *
	 * @since 0.2.0 ~2021.05.30
	 */
	@NotNull
	public static final Parser CX_CMD_DECLARE = new PatternGroupParser(
			Pattern.compile("^[\\t ]*#((?i)declare)\\s([^\\s\\[\\]]+)\\s?(.*)$", Pattern.DOTALL),
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
					-100
			))
	);

	/**
	 * A parser that parses {@code #define} commands.
	 *
	 * @since 0.2.0 ~2021.05.30
	 */
	@NotNull
	public static final Parser CX_CMD_DEFINE = new PatternGroupParser(
			Pattern.compile("^[\\t ]*#((?i)define)\\s(\\S+)\\s?(.*)$"),
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
					-100
			))
	);

	/**
	 * A parser that parses {@code #elif} commands.
	 *
	 * @since 0.2.0 ~2021.05.30
	 */
	@NotNull
	public static final Parser CX_CMD_ELIF = new PatternGroupParser(
			Pattern.compile("^[\\t ]*#((?i)elif)\\s(.+)$"),
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
					-100
			))
	);

	/**
	 * A parser that parses {@code #elifdef} commands.
	 *
	 * @since 0.2.0 ~2021.05.30
	 */
	@NotNull
	public static final Parser CX_CMD_ELIFDEF = new PatternGroupParser(
			Pattern.compile("^[\\t ]*#((?i)elifdef)\\s(\\S+)\\s*$"),
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
	public static final Parser CX_CMD_ELIFNDEF = new PatternGroupParser(
			Pattern.compile("^[\\t ]*#((?i)elifndef)\\s(\\S+)\\s*$"),
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
	public static final Parser CX_CMD_ELSE = new PatternGroupParser(
			Pattern.compile("^[\\t ]*#((?i)else)\\s*$"),
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
	 * A parser that parses {@code #endcapture} commands.
	 *
	 * @since 0.2.0 ~2021.06.01
	 */
	@NotNull
	public static final Parser CX_CMD_ENDCAPTURE = new PatternGroupParser(
			Pattern.compile("^[\\t ]*#((?i)endcapture)\\s*$"),
			(d, r) -> new Tree(d, r, new Sketch(Kind.CX_CMD_ENDCAPTURE), 1),
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
	public static final Parser CX_CMD_ENDFOR = new PatternGroupParser(
			Pattern.compile("^[\\t ]*#((?i)endfor)\\s*$"),
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
	public static final Parser CX_CMD_ENDIF = new PatternGroupParser(
			Pattern.compile("^[\\t ]*#((?i)endif)\\s*$"),
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
	 * A parser that parses {@code #endwhile} commands.
	 *
	 * @since 0.2.0 ~2021.06.03
	 */
	@NotNull
	public static final Parser CX_CMD_ENDWHILE = new PatternGroupParser(
			Pattern.compile("^[\\t ]*#((?i)endwhile)\\s*$"),
			(d, r) -> new Tree(d, r, new Sketch(Kind.CX_CMD_ENDWHILE), 1),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.TYPE)
					 .setKind(Kind.CX_CMD_TYPE)
			))
	);

	/**
	 * A parser that parses {@code #error} commands.
	 *
	 * @since 0.2.0 ~2021.06.02
	 */
	@NotNull
	public static final Parser CX_CMD_ERROR = new PatternGroupParser(
			Pattern.compile("^[\\t ]*#((?i)error)\\s?(.*)$"),
			(d, r) -> new Tree(d, r, new Sketch(Kind.CX_CMD_ERROR), 1),
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
					-100
			))
	);

	/**
	 * A parser that parses {@code #for} commands.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	public static final Parser CX_CMD_FOR = new PatternGroupParser(
			Pattern.compile("^[\\t ]*#((?i)for)\\s(\\S+)\\s?(.*)$"),
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
					-100
			))
	);

	/**
	 * A parser that parses {@code #if} commands.
	 *
	 * @since 0.2.0 ~2021.05.30
	 */
	@NotNull
	public static final Parser CX_CMD_IF = new PatternGroupParser(
			Pattern.compile("^[\\t ]*#((?i)if)\\s(.+)$"),
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
					-100
			))
	);

	/**
	 * A parser that parses {@code #ifdef} commands.
	 *
	 * @since 0.2.0 ~2021.05.30
	 */
	@NotNull
	public static final Parser CX_CMD_IFDEF = new PatternGroupParser(
			Pattern.compile("^[\\t ]*#((?i)ifdef)\\s(\\S+)\\s*$"),
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
	public static final Parser CX_CMD_IFNDEF = new PatternGroupParser(
			Pattern.compile("^[\\t ]*#((?i)ifndef)\\s(\\S+)\\s*$"),
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
	public static final Parser CX_CMD_INCLUDE = new PatternGroupParser(
			Pattern.compile("^[\\t ]*#((?i)include)\\s(.+)$"),
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
					-100
			))
	);

	/**
	 * A parser that parses {@code #message} commands.
	 *
	 * @since 0.2.0 ~2021.06.02
	 */
	@NotNull
	public static final Parser CX_CMD_MESSAGE = new PatternGroupParser(
			Pattern.compile("^[\\t ]*#((?i)message)\\s?(.*)$"),
			(d, r) -> new Tree(d, r, new Sketch(Kind.CX_CMD_MESSAGE), 1),
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
					-100
			))
	);

	/**
	 * A parser that parses {@code #spread} commands.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	public static final Parser CX_CMD_SPREAD = new PatternGroupParser(
			Pattern.compile("^[\\t ]*#((?i)spread)\\s(.+)$"),
			(d, r) -> new Tree(d, r, new Sketch(Kind.CX_CMD_SPREAD), 1),
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
					-100
			))
	);

	/**
	 * A parser that parses {@code #undec} commands.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	public static final Parser CX_CMD_UNDEC = new PatternGroupParser(
			Pattern.compile("^[\\t ]*#((?i)undec)\\s(\\S+)\\s*$"),
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
	public static final Parser CX_CMD_UNDEF = new PatternGroupParser(
			Pattern.compile("^[\\t ]*#((?i)undef)\\s(\\S+)\\s*$"),
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

	/**
	 * A parser that parses {@code #while} commands.
	 *
	 * @since 0.2.0 ~2021.06.03
	 */
	@NotNull
	public static final Parser CX_CMD_WHILE = new PatternGroupParser(
			Pattern.compile("^[\\t ]*#((?i)while)\\s(.+)$"),
			(d, r) -> new Tree(d, r, new Sketch(Kind.CX_CMD_WHILE), 1),
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
					-100
			))
	);

	//CX INJ

	/**
	 * A parser parsing injection sequences.
	 *
	 * @since 0.2.0 ~2021.05.19
	 */
	@NotNull
	public static final Parser CX_INJ = new EnclosureParser(
			Pattern.compile("#\\{"),
			Pattern.compile("\\}#"),
			(d, r) -> new Tree(d, r, new Sketch(Kind.CX_INJ)),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.OPEN)
					 .setKind(Kind.CX_ANC_OPEN)
			)),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.CLOSE)
					 .setKind(Kind.CX_ANC_CLOSE)
			)),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.PARAMETER)
					 .setKind(Kind.CX_PRM),
					-100
			))
	);

	//DC

	/**
	 * A parser parsing line separators ({@code \n} or {@code \r} or {@code \r\n}).
	 *
	 * @since 0.2.0 ~2021.05.19
	 */
	@NotNull
	public static final Parser DC_EOL = new TermParser(
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
	public static final Parser OP_ADD = new TermParser(
			Pattern.compile("\\+"),
			(d, r) -> new Tree(d, r, new Sketch(Kind.OP_ADD))
	);

	/**
	 * A parser that parses division symbols.
	 *
	 * @since 0.2.0 ~2021.06.02
	 */
	@NotNull
	public static final Parser OP_DIV = new TermParser(
			Pattern.compile("\\/"),
			(d, r) -> new Tree(d, r, new Sketch(Kind.OP_DIV))
	);

	/**
	 * A parser that parses equal symbols.
	 *
	 * @since 0.2.0 ~2021.06.02
	 */
	@NotNull
	public static final Parser OP_EQL = new TermParser(
			Pattern.compile("=="),
			(d, r) -> new Tree(d, r, new Sketch(Kind.OP_EQL))
	);

	/**
	 * A parser that parses less than or equals symbols.
	 *
	 * @since 0.2.0 ~2021.06.02
	 */
	@NotNull
	public static final Parser OP_LEQ = new TermParser(
			Pattern.compile("<="),
			(d, r) -> new Tree(d, r, new Sketch(Kind.OP_LEQ))
	);

	/**
	 * A parser that parses logical and symbols.
	 *
	 * @since 0.2.0 ~2021.06.02
	 */
	@NotNull
	public static final Parser OP_LND = new TermParser(
			Pattern.compile("&&"),
			(d, r) -> new Tree(d, r, new Sketch(Kind.OP_LND))
	);

	/**
	 * A parser that parses logical or symbols.
	 *
	 * @since 0.2.0 ~2021.06.02
	 */
	@NotNull
	public static final Parser OP_LOR = new TermParser(
			Pattern.compile("\\|\\|"),
			(d, r) -> new Tree(d, r, new Sketch(Kind.OP_LOR))
	);

	/**
	 * A parser that parses less than symbols.
	 *
	 * @since 0.2.0 ~2021.06.02
	 */
	@NotNull
	public static final Parser OP_LTN = new TermParser(
			Pattern.compile("<"),
			(d, r) -> new Tree(d, r, new Sketch(Kind.OP_LTN))
	);

	/**
	 * A parser that parses more than or equals symbols.
	 *
	 * @since 0.2.0 ~2021.06.02
	 */
	@NotNull
	public static final Parser OP_MEQ = new TermParser(
			Pattern.compile(">="),
			(d, r) -> new Tree(d, r, new Sketch(Kind.OP_MEQ))
	);

	/**
	 * A parser that modulo symbols.
	 *
	 * @since 0.2.0 ~2021.06.02
	 */
	@NotNull
	public static final Parser OP_MOD = new TermParser(
			Pattern.compile("%"),
			(d, r) -> new Tree(d, r, new Sketch(Kind.OP_MOD))
	);

	/**
	 * A parser that parses more than symbols.
	 *
	 * @since 0.2.0 ~2021.06.02
	 */
	@NotNull
	public static final Parser OP_MTN = new TermParser(
			Pattern.compile(">"),
			(d, r) -> new Tree(d, r, new Sketch(Kind.OP_MTN))
	);

	/**
	 * A parser that parses multiplication symbols.
	 *
	 * @since 0.2.0 ~2021.06.02
	 */
	@NotNull
	public static final Parser OP_MUL = new TermParser(
			Pattern.compile("\\*"),
			(d, r) -> new Tree(d, r, new Sketch(Kind.OP_MUL))
	);

	/**
	 * A parser that parses negation symbols.
	 *
	 * @since 0.2.0 ~2021.06.02
	 */
	@NotNull
	public static final Parser OP_NEG = new TermParser(
			Pattern.compile("!"),
			(d, r) -> new Tree(d, r, new Sketch(Kind.OP_NEG))
	);

	/**
	 * A parser that parses not-equals symbols.
	 *
	 * @since 0.2.0 ~2021.06.02
	 */
	@NotNull
	public static final Parser OP_NQL = new TermParser(
			Pattern.compile("!="),
			(d, r) -> new Tree(d, r, new Sketch(Kind.OP_NQL))
	);

	/**
	 * A parser that parses subtraction symbols.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	@NotNull
	public static final Parser OP_SUB = new TermParser(
			Pattern.compile("-"),
			(d, r) -> new Tree(d, r, new Sketch(Kind.OP_SUB))
	);

	//SX

	/**
	 * A parser parsing colons.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final Parser SX_CLN = new TermParser(
			Pattern.compile(":"),
			(d, r) -> new Tree(d, r, new Sketch(Kind.SX_CLN))
	);

	/**
	 * A parser parsing commas.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final Parser SX_CMA = new TermParser(
			Pattern.compile(","),
			(d, r) -> new Tree(d, r, new Sketch(Kind.SX_CMA))
	);

	/**
	 * A parser parsing curly brackets.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final Parser SX_CUR = new EnclosureParser(
			Pattern.compile("\\{"),
			Pattern.compile("\\}"),
			(d, r) -> new Tree(d, r, new Sketch(Kind.SX_CUR)),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.OPEN)
					 .setKind(Kind.CX_ANC_OPEN)
			)),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.CLOSE)
					 .setKind(Kind.CX_ANC_CLOSE)
			))
	);

	/**
	 * A parser parsing double quotes.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final Parser SX_DQT = new EnclosureParser(
			Pattern.compile("(?<!(?<!\\\\)\\\\)\""),
			Pattern.compile("(?<!(?<!\\\\)\\\\)\""),
			(d, r) -> new Tree(d, r, new Sketch(Kind.SX_DQT)),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.OPEN)
					 .setKind(Kind.CX_ANC_OPEN)
			)),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.CLOSE)
					 .setKind(Kind.CX_ANC_CLOSE)
			))
	);

	/**
	 * A parser that parses names.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	public static final Parser SX_NME = new TermParser(
			Pattern.compile("[A-Za-z_$][A-Za-z_$0-9]*"),
			(d, r) -> new Tree(d, r, new Sketch(Kind.SX_NME))
	);

	/**
	 * A parser parsing numbers.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	public static final Parser SX_NUM = new TermParser(
			Pattern.compile("(?:0[xb]|[0-9.])[0-9A-Fa-f_.]*(?:[Ee][0-9]*)?"),
			(d, r) -> new Tree(d, r, new Sketch(Kind.SX_NUM))
	);

	/**
	 * A parser parsing quotes.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final Parser SX_QTE = new EnclosureParser(
			Pattern.compile("(?<!(?<!\\\\)\\\\)'"),
			Pattern.compile("(?<!(?<!\\\\)\\\\)'"),
			(d, r) -> new Tree(d, r, new Sketch(Kind.SX_QTE)),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.OPEN)
					 .setKind(Kind.CX_ANC_OPEN)
			)),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.CLOSE)
					 .setKind(Kind.CX_ANC_CLOSE)
			))
	);

	/**
	 * A parser parsing round brackets.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final Parser SX_RND = new EnclosureParser(
			Pattern.compile("\\("),
			Pattern.compile("\\)"),
			(d, r) -> new Tree(d, r, new Sketch(Kind.SX_RND)),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.OPEN)
					 .setKind(Kind.CX_ANC_OPEN)
			)),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.CLOSE)
					 .setKind(Kind.CX_ANC_CLOSE)
			))
	);

	/**
	 * A parser parsing square brackets.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final Parser SX_SQR = new EnclosureParser(
			Pattern.compile("\\["),
			Pattern.compile("\\]"),
			(d, r) -> new Tree(d, r, new Sketch(Kind.SX_SQR)),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.OPEN)
					 .setKind(Kind.CX_ANC_OPEN)
			)),
			(t, r) -> t.offer(new Tree(
					t.document(),
					r,
					t.getSketch()
					 .get(Component.CLOSE)
					 .setKind(Kind.CX_ANC_CLOSE)
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
