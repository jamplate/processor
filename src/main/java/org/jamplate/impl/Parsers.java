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

import org.jamplate.impl.parser.CommandParser;
import org.jamplate.impl.parser.DoublePatternParser;
import org.jamplate.impl.parser.InjectionParser;
import org.jamplate.impl.parser.PatternParser;
import org.jamplate.model.Sketch;
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
			Pattern.compile("\\*/")
	).then((compilation, tree) -> {
		tree.getSketch().setKind(Kind.CM_BLK);
		tree.getSketch().get(Component.OPEN).setKind(Kind.CM_BLK_OPEN);
		tree.getSketch().get(Component.CLOSE).setKind(Kind.CM_BLK_CLOSE);
	});

	/**
	 * A parser that parses commented lines.
	 *
	 * @since 0.2.0 ~2021.05.19
	 */
	@NotNull
	public static final Parser CM_SLN = new DoublePatternParser(
			Pattern.compile("//"),
			Pattern.compile("(?=[\r\n]|$)")
	).then((compilation, tree) -> {
		tree.getSketch().setKind(Kind.CM_SLN);
		tree.getSketch().get(Component.OPEN).setKind(Kind.CM_SLN_OPEN);
		tree.getSketch().get(Component.CLOSE).setKind(Kind.CM_SLN_CLOSE);
		tree.getSketch().get(Component.OPEN).getTree().pop();
	});

	//CX

	/**
	 * A parser that parses a single-line command.
	 *
	 * @since 0.2.0 ~2021.05.19
	 */
	@SuppressWarnings("OverlyLongLambda")
	@NotNull
	public static final Parser CX_CMD = new CommandParser(
			Pattern.compile("(?<=^|[\r\n])#"),
			Pattern.compile("(?=[\r\n]|$)")
	).then((compilation, tree) -> {
		Sketch sketch = tree.getSketch();
		sketch.setKind(Kind.CX_CMD);
		sketch.get(Component.OPEN).setKind(Kind.CX_CMD_OPEN);
		sketch.get(Component.CLOSE).setKind(Kind.CX_CMD_CLOSE);
		sketch.get(Component.CLOSE).getTree().pop();
		sketch.get(Component.TYPE).setKind(Kind.CX_CMD_TYPE);
		sketch.get(Component.PARAMETER).setKind(Kind.CX_PRM);
		sketch.get(Component.PARAMETER).get(Component.KEY).setKind(Kind.CX_CMD_KEY);
		sketch.get(Component.PARAMETER).get(Component.VALUE).setKind(Kind.CX_PRM);
	});

	/**
	 * A parser parsing injection sequences.
	 *
	 * @since 0.2.0 ~2021.05.19
	 */
	@NotNull
	public static final Parser CX_INJ = new InjectionParser(
			Pattern.compile("#\\{"),
			Pattern.compile("\\}#")
	).then((compilation, tree) -> {
		Sketch sketch = tree.getSketch();
		sketch.setKind(Kind.CX_INJ);
		sketch.get(Component.OPEN).setKind(Kind.CX_INJ_OPEN);
		sketch.get(Component.CLOSE).setKind(Kind.CX_INJ_CLOSE);
		sketch.get(Component.PARAMETER).setKind(Kind.CX_PRM);
	});

	//OP

	/**
	 * A parser that parses addition symbols.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	@NotNull
	public static final Parser OP_ADD = new PatternParser(
			Pattern.compile("\\+")
	).then((compilation, tree) -> {
		tree.getSketch().setKind(Kind.OP_ADD);
	});

	/**
	 * A parser that parses subtraction symbols.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	@NotNull
	public static final Parser OP_SUB = new PatternParser(
			Pattern.compile("\\-")
	).then((compilation, tree) -> {
		tree.getSketch().setKind(Kind.OP_SUB);
	});

	//SX

	/**
	 * A parser parsing commas.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final Parser SX_CMA = new PatternParser(
			Pattern.compile(",")
	).then((compilation, tree) -> {
		tree.getSketch().setKind(Kind.SX_CMA);
	});

	/**
	 * A parser parsing curly brackets.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final Parser SX_CUR = new DoublePatternParser(
			Pattern.compile("\\{"),
			Pattern.compile("\\}")
	).then((compilation, tree) -> {
		tree.getSketch().setKind(Kind.SX_CUR);
		tree.getSketch().get(Component.OPEN).setKind(Kind.SX_CUR_OPEN);
		tree.getSketch().get(Component.CLOSE).setKind(Kind.SX_CUR_CLOSE);
	});

	/**
	 * A parser parsing double quotes.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final Parser SX_DQT = new DoublePatternParser(
			Pattern.compile("(?<!(?<!\\\\)\\\\)\"")
	).then((compilation, tree) -> {
		tree.getSketch().setKind(Kind.SX_DQT);
		tree.getSketch().get(Component.OPEN).setKind(Kind.SX_DQT_OPEN);
		tree.getSketch().get(Component.CLOSE).setKind(Kind.SX_DQT_CLOSE);
	});

	/**
	 * A parser parsing line separators ({@code \n} or {@code \r} or {@code \r\n}).
	 *
	 * @since 0.2.0 ~2021.05.19
	 */
	@NotNull
	public static final Parser SX_EOL = new PatternParser(
			Pattern.compile("\r\n|\r|\n")
	).then((compilation, tree) -> {
		tree.getSketch().setKind(Kind.SX_EOL);
	});

	/**
	 * A parser parsing quotes.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final Parser SX_QTE = new DoublePatternParser(
			Pattern.compile("(?<!(?<!\\\\)\\\\)'")
	).then((compilation, tree) -> {
		tree.getSketch().setKind(Kind.SX_QTE);
		tree.getSketch().get(Component.OPEN).setKind(Kind.SX_QTE_OPEN);
		tree.getSketch().get(Component.CLOSE).setKind(Kind.SX_QTE_CLOSE);
	});

	/**
	 * A parser parsing round brackets.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final Parser SX_RND = new DoublePatternParser(
			Pattern.compile("\\("),
			Pattern.compile("\\)")
	).then((compilation, tree) -> {
		tree.getSketch().setKind(Kind.SX_RND);
		tree.getSketch().get(Component.OPEN).setKind(Kind.SX_RND_OPEN);
		tree.getSketch().get(Component.CLOSE).setKind(Kind.SX_RND_CLOSE);
	});

	/**
	 * A parser parsing square brackets.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final Parser SX_SQR = new DoublePatternParser(
			Pattern.compile("\\["),
			Pattern.compile("\\]")
	).then((compilation, tree) -> {
		tree.getSketch().setKind(Kind.SX_SQR);
		tree.getSketch().get(Component.OPEN).setKind(Kind.SX_SQR_OPEN);
		tree.getSketch().get(Component.CLOSE).setKind(Kind.SX_SQR_CLOSE);
	});

	//VL

	/**
	 * A parser parsing numbers.
	 *
	 * @since 0.2.0 ~2021.05.25
	 */
	@NotNull
	public static final Parser VL_NUM = new PatternParser(
			Pattern.compile("(?:0[xb])?[0-9_][1-9]*[DdLlFf]?")
	).then((compilation, tree) -> {
		tree.getSketch().setKind(Kind.VL_NUM);
	});

	/**
	 * A parser that parses references.
	 *
	 * @since 0.2.0 ~2021.05.24
	 */
	@NotNull
	public static final Parser VL_REF = new PatternParser(
			Pattern.compile("[A-Za-z_$][A-Za-z_$0-9]*")
	).then((compilation, tree) -> {
		tree.getSketch().setKind(Kind.VL_REF);
	});

	/**
	 * A parser parsing escaped sequences.
	 *
	 * @since 0.2.0 ~2021.05.17
	 */
	@NotNull
	public static final Parser VL_STR_ESCAPE = new PatternParser(
			Pattern.compile("\\\\.")
	).then((compilation, tree) -> {
		tree.getSketch().setKind(Kind.VL_STR_ESCAPE);
	});

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
