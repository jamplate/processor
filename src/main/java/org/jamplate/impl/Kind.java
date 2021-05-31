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
public final class Kind {
	//CM

	/**
	 * The kind of comment blocks.
	 *
	 * @since 0.2.0 ~2021.05.19
	 */
	@NotNull
	public static final String CM_BLK = "cm/blk";
	/**
	 * The kind of the closing anchor of comment blocks.
	 *
	 * @since 0.2.0 ~2021.05.19
	 */
	@NotNull
	public static final String CM_BLK_CLOSE = "cm/blk:close";
	/**
	 * The kind of the opening anchor of comment blocks.
	 *
	 * @since 0.2.0 ~2021.05.19
	 */
	@NotNull
	public static final String CM_BLK_OPEN = "cm/blk:open";

	/**
	 * The kind of commented lines.
	 *
	 * @since 0.2.0 ~2021.05.19
	 */
	@NotNull
	public static final String CM_SLN = "cm/sln";
	/**
	 * The kind of the closing anchor of commented lines.
	 *
	 * @since 0.2.0 ~2021.05.19
	 */
	@NotNull
	public static final String CM_SLN_CLOSE = "cm/sln:close";
	/**
	 * The kind of the opening anchor of commented lines.
	 *
	 * @since 0.2.0 ~2021.05.19
	 */
	@NotNull
	public static final String CM_SLN_OPEN = "cm/sln:open";

	//CX CMD

	/**
	 * The kind of a single-line command.
	 *
	 * @since 0.2.0 ~2021.05.19
	 */
	@NotNull
	public static final String CX_CMD = "cx/cmd";

	/**
	 * The kind of the closing anchor of a single-line command.
	 *
	 * @since 0.2.0 ~2021.05.19
	 */
	@NotNull
	public static final String CX_CMD_CLOSE = "cx/cmd:close";

	/**
	 * The command kind of the {@code #console} command.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	@NotNull
	public static final String CX_CMD_CONSOLE = "cx/cmd.console";

	/**
	 * The command that allocates a value into the heap.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	@NotNull
	public static final String CX_CMD_DECLARE = "cx/cmd.declare";

	/**
	 * The command that allocates a value into the heap and replaces all the occurrences
	 * of the name of that variable in the text.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	@NotNull
	public static final String CX_CMD_DEFINE = "cx/cmd.define";

	/**
	 * The command kind of the {@code elif} command.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	@NotNull
	public static final String CX_CMD_ELIF = "cx/cmd.elif";

	/**
	 * The kind of the {@code elifdef} command.
	 *
	 * @since 0.2.0 ~2021.05.29
	 */
	@NotNull
	public static final String CX_CMD_ELIFDEF = "cx/cmd.elifdef";

	/**
	 * The kind of the {@code elifndef} command.
	 *
	 * @since 0.2.0 ~2021.05.29
	 */
	@NotNull
	public static final String CX_CMD_ELIFNDEF = "cx/cmd.elifndef";

	/**
	 * The command kind of the {@code else} command.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	@NotNull
	public static final String CX_CMD_ELSE = "cx/cmd.else";

	/**
	 * The command kind of the {@code endfor} command.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	public static final String CX_CMD_ENDFOR = "cx/cmd.endfor";

	/**
	 * The command kind of the {@code endif} command.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	@NotNull
	public static final String CX_CMD_ENDIF = "cx/cmd.endif";

	/**
	 * The command kind of the {@code for} command.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	public static final String CX_CMD_FOR = "cx/cmd.for";

	/**
	 * The command kind of the {@code #if} command.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	@NotNull
	public static final String CX_CMD_IF = "cx/cmd.if";

	/**
	 * The kind of the {@code #ifdef} command.
	 *
	 * @since 0.2.0 ~2021.05.29
	 */
	@NotNull
	public static final String CX_CMD_IFDEF = "cx/cmd.ifdef";

	/**
	 * The kind of the {@code #ifndef} command.
	 *
	 * @since 0.2.0 ~2021.05.29
	 */
	@NotNull
	public static final String CX_CMD_IFNDEF = "cx/cmd.ifndef";

	/**
	 * The command kind of the {@code #include} command.
	 *
	 * @since 0.2.0 ~2021.05.20
	 */
	@NotNull
	public static final String CX_CMD_INCLUDE = "cx/cmd.include";

	/**
	 * The kind of the key part of a command.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	@NotNull
	public static final String CX_CMD_KEY = "cx/cmd:key";

	/**
	 * The command kind of the {@code #make} command.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	@NotNull
	public static final String CX_CMD_MAKE = "cx/cmd.make";

	/**
	 * The kind of the opening anchor of a single-line command.
	 *
	 * @since 0.2.0 ~2021.05.19
	 */
	@NotNull
	public static final String CX_CMD_OPEN = "cx/cmd:open";

	/**
	 * The command kind of the {@code #spread} command.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	public static final String CX_CMD_SPREAD = "cx/cmd:spread";

	/**
	 * The kind of the type of a single-line command.
	 *
	 * @since 0.2.0 ~2021.05.20
	 */
	@NotNull
	public static final String CX_CMD_TYPE = "cx/cmd.type";

	/**
	 * The command kind of the {@code #undec} command.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	public static final String CX_CMD_UNDEC = "cx/cmd.undec";

	/**
	 * The command kind of the {@code #undef} command.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	public static final String CX_CMD_UNDEF = "cx/cmd.undef";

	//CX FLW

	/**
	 * The for command context kind.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	public static final String CX_FLW_FOR = "cx/flw.for";

	/**
	 * The if command context kind.
	 *
	 * @since 0.2.0 ~2021.05.24
	 */
	@NotNull
	public static final String CX_FLW_IF = "cx/flw.if";

	//CX INJ

	/**
	 * The kind of an injection sequence.
	 *
	 * @since 0.2.0 ~2021.05.19
	 */
	@NotNull
	public static final String CX_INJ = "cx/inj";
	/**
	 * The kind of the closing anchor of an injection.
	 *
	 * @since 0.2.0 ~2021.05.19
	 */
	@NotNull
	public static final String CX_INJ_CLOSE = "cx/inj:close";
	/**
	 * The kind of the opening anchor of an injection.
	 *
	 * @since 0.2.0 ~2021.05.19
	 */
	@NotNull
	public static final String CX_INJ_OPEN = "cx/inj:open";

	//CX PRM

	/**
	 * The kind of parameter context.
	 *
	 * @since 0.2.0 ~2021.05.20
	 */
	@NotNull
	public static final String CX_PRM = "cx/prm";

	//CX TXT

	/**
	 * The kind of source text.
	 *
	 * @since 0.2.0 ~2021.05.28
	 */
	@NotNull
	public static final String CX_TXT = "cx/txt";

	//DC

	/**
	 * The kind for line separators ({@code \n} or {@code \r} or {@code \r\n}).
	 *
	 * @since 0.2.0 ~2021.05.19
	 */
	@NotNull
	public static final String DC_EOL = "dc/eol";
	/**
	 * The kind for suppressed (do-not-print) line separators ({@code \n} or {@code \r} or
	 * {@code \r\n}).
	 *
	 * @since 0.2.0 ~2021.05.19
	 */
	@NotNull
	public static final String DC_EOL_SUPPRESSED = "dc/eol:suppressed";

	/**
	 * The kind for the root element.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	public static final String DC_ROT = "dc/rot";

	//OP

	/**
	 * The kind for the addition symbol.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	@NotNull
	public static final String OP_ADD = "op/add";
	/**
	 * The kind for the context of an addition operation.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	@NotNull
	public static final String OP_ADD_CONTEXT = "op/add:context";

	/**
	 * The kind for the division symbol.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	@NotNull
	public static final String OP_DIV = "op/div";

	/**
	 * The kind for the equating symbol.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	@NotNull
	public static final String OP_EQL = "op/eql";

	/**
	 * The kind for the less-than symbol.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	@NotNull
	public static final String OP_LTN = "op/ltn";

	/**
	 * The kind for the more-than symbol.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	@NotNull
	public static final String OP_MTN = "op/mtn";

	/**
	 * The kind for the multiplication symbol.
	 */
	@NotNull
	public static final String OP_MUL = "op/mul";

	/**
	 * The kind for the not-equals symbol.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	@NotNull
	public static final String OP_NQL = "op/nql";

	/**
	 * The kind for the subtraction symbol.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	@NotNull
	public static final String OP_SUB = "op/sub";

	//SX

	/**
	 * The kind for colons.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	public static final String SX_CLN = "sx/cln";

	/**
	 * The kind for commas.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final String SX_CMA = "sx/cma";

	/**
	 * The kind for curly brackets.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final String SX_CUR = "sx/cur";
	/**
	 * The kind for curly brackets.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final String SX_CUR_CLOSE = "sx/cur:close";
	/**
	 * The kind for curly brackets.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final String SX_CUR_OPEN = "sx/cur:open";

	/**
	 * The kind for double-quotes.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final String SX_DQT = "sx/dqt";
	/**
	 * The kind for double-quotes.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final String SX_DQT_CLOSE = "sx/dqt:close";
	/**
	 * The kind for double-quotes.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final String SX_DQT_OPEN = "sx/dqt:open";

	/**
	 * The kind for names.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	public static final String SX_NME = "sx/nme";

	/**
	 * The kind for numbers.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	public static final String SX_NUM = "sx/num";

	/**
	 * The kind for quotes.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final String SX_QTE = "sx/qte";
	/**
	 * The kind for quotes.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final String SX_QTE_CLOSE = "sx/qte:close";
	/**
	 * The kind for quotes.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final String SX_QTE_OPEN = "sx/qte:open";

	/**
	 * The kind for round brackets.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final String SX_RND = "sx/rnd";
	/**
	 * The kind for round brackets.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final String SX_RND_CLOSE = "sx/rnd:close";
	/**
	 * The kind for round brackets.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final String SX_RND_OPEN = "sx/rnd:open";

	/**
	 * The kind for square brackets.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final String SX_SQR = "sx/sqr";
	/**
	 * The kind for square brackets.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final String SX_SQR_CLOSE = "sx/sqr:close";
	/**
	 * The kind for square brackets.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final String SX_SQR_OPEN = "sx/sqr:open";

	/**
	 * Utility classes must not be initialized.
	 *
	 * @throws AssertionError when called.
	 * @since 0.2.0 ~2021.05.23
	 */
	private Kind() {
		throw new AssertionError("No instance for you");
	}
}
