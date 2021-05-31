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

import org.jamplate.impl.analyzer.RecursiveAnalyzer;
import org.jamplate.impl.analyzer.SequentialAnalyzer;
import org.jamplate.impl.compiler.*;
import org.jamplate.impl.initializer.ProcessorsInitializer;
import org.jamplate.impl.parser.*;
import org.jamplate.impl.processor.AnalyzerProcessor;
import org.jamplate.impl.processor.CompilerProcessor;
import org.jamplate.impl.processor.ParserProcessor;
import org.jamplate.impl.processor.SequentialProcessor;
import org.jamplate.model.function.Compiler;
import org.jamplate.model.function.*;
import org.jetbrains.annotations.NotNull;

/**
 * An all-in-one jamplate processor.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.23
 */
@SuppressWarnings("OverlyCoupledClass")
public final class Jamplate {
	/**
	 * The default jamplate analyzer.
	 *
	 * @since 0.2.0 ~2021.05.29
	 */
	@NotNull
	public static final Analyzer ANALYZER =
			new SequentialAnalyzer(
					/* Suppressed EOL First, before `CX_CMD`s get convert */
					new RecursiveAnalyzer(Analyzers.SX_EOL_SUPPRESSED)
					//					/* Commands */
					//					new RecursiveAnalyzer(new SequentialAnalyzer(
					//							/* Console command */
					//							Analyzers.CX_CMD_CONSOLE,
					//							/* Declare command */
					//							Analyzers.CX_CMD_DECLARE,
					//							/* Define command */
					//							Analyzers.CX_CMD_DEFINE,
					//							/* Elif command */
					//							Analyzers.CX_CMD_ELIF,
					//							/* Elifdef command */
					//							Analyzers.CX_CMD_ELIFDEF,
					//							/* Elifndef command */
					//							Analyzers.CX_CMD_ELIFNDEF,
					//							/* Else command */
					//							Analyzers.CX_CMD_ELSE,
					//							/* Endif command */
					//							Analyzers.CX_CMD_ENDIF,
					//							/* If command */
					//							Analyzers.CX_CMD_IF,
					//							/* Ifdef command */
					//							Analyzers.CX_CMD_IFDEF,
					//							/* Ifndef command */
					//							Analyzers.CX_CMD_IFNDEF,
					//							/* Include command */
					//							Analyzers.CX_CMD_INCLUDE
					//					))
			);

	/**
	 * A processor for the jamplate analyzer.
	 *
	 * @since 0.2.0 ~2021.05.30
	 */
	@NotNull
	public static final Processor ANALYZER_PROCESSOR =
			new AnalyzerProcessor(Jamplate.ANALYZER);

	/**
	 * The default jamplate compiler.
	 *
	 * @since 0.2.0 ~2021.05.21
	 */
	@NotNull
	public static final Compiler COMPILER =
			new OrderCompiler(
					/* Seek suppressed EOL, to suppress CX_TXT */
					Compilers.SX_EOL_SUPPRESSED,
					/* Console commands */
					Compilers.CX_CMD_CONSOLE,
					/* Include commands */
					Compilers.CX_CMD_INCLUDE,
					/* Declare commands */
					Compilers.CX_CMD_DECLARE,
					/* Define commands */
					Compilers.CX_CMD_DEFINE,
					/* If flow */
					Compilers.CX_FLW_IF,
					/* Injections */
					Compilers.CX_INJ,
					/* Parameters, encapsulated to ignore outer compilers (ex. CX_TXT) */
					new KindCompiler(Kind.CX_PRM, new ExclusiveCompiler(
							/* Cleanup ws and throw if unrecognized */
							new FlattenCompiler(new MandatoryCompiler(
									/* Ignore whitespace */
									new WhitespaceCompiler()
							)),
							/* Compile recognized logic */
							new OrderCompiler(
									/* String */
									Compilers.VL_STR,
									/* Reference */
									Compilers.VL_REF,
									/* Number */
									Compilers.VL_NUM
							)
					)),
					/* Fallthrough, convert into REPRNT */
					Compilers.CX_TXT
			);

	/**
	 * A processor for the jamplate compiler.
	 *
	 * @since 0.2.0 ~2021.05.30
	 */
	@NotNull
	public static final Processor COMPILER_PROCESSOR =
			new CompilerProcessor(Jamplate.COMPILER);

	/**
	 * An all-in-one parser used by the jamplate default implementation.
	 *
	 * @since 0.2.0 ~2021.05.17
	 */
	@NotNull
	public static final Parser PARSER =
			new CollectParser(new OrderParser(
					/* Separate the file by lines. */
					Parsers.SX_EOL,
					/* Wild Block Battles (first occurs win) */
					new MergeParser(new CombineParser(
							/* Comment blocks */
							Parsers.CM_BLK,
							/* Strings, encapsulated to parse inner components */
							new CombineParser(
									/* Double quote strings */
									Parsers.SX_QTE,
									/* Single quote strings */
									Parsers.SX_DQT
							).then(
									/* Register escaped sequences */
									Parsers.VL_STR_ESCAPE
							)
					)),
					/* Always ignore commented end-of-lines */
					Parsers.CM_SLN,
					/* Runtime elements, encapsulated to parse inner components */
					new FlatOrderParser(
							/* Injections, injection must win over commands */
							Parsers.CX_INJ,
							new ThenOfferParser(
									/* Commands */
									Parsers.CX_CMD,
									/* each command kind */
									new CollectParser(new CombineParser(
											/* Parse Console command components */
											Parsers.CX_CMD_CONSOLE,
											/* Parse Declare command components */
											Parsers.CX_CMD_DECLARE,
											/* Parse Define command components */
											Parsers.CX_CMD_DEFINE,
											/* Parse Elif command components */
											Parsers.CX_CMD_ELIF,
											/* Parse Elifdef command components */
											Parsers.CX_CMD_ELIFDEF,
											/* Parse Elifndef command components */
											Parsers.CX_CMD_ELIFNDEF,
											/* Parse Else command components */
											Parsers.CX_CMD_ELSE,
											/* Parse Endif command components */
											Parsers.CX_CMD_ENDIF,
											/* Parse If command components */
											Parsers.CX_CMD_IF,
											/* Parse Ifdef command components */
											Parsers.CX_CMD_IFDEF,
											/* Parse Ifndef command components */
											Parsers.CX_CMD_IFNDEF,
											/* Parse Include command components */
											Parsers.CX_CMD_INCLUDE
									))
							)
					).then(new RecursiveParser(new MergeParser(new OrderParser(
							/* References */
							Parsers.VL_REF,
							/* Numbers */
							Parsers.VL_NUM,
							/* Braces */
							Parsers.SX_CUR,
							/* Brackets */
							Parsers.SX_SQR,
							/* Parentheses */
							Parsers.SX_RND
					))))
			));

	/**
	 * A processor wrapper for the jamplate parser.
	 *
	 * @since 0.2.0 ~2021.05.30
	 */
	@NotNull
	public static final Processor PARSER_PROCESSOR =
			new ParserProcessor(Jamplate.PARSER);

	/**
	 * A processor that analyzes the default jamplate components searching for known
	 * jamplate specs.
	 *
	 * @since 0.2.0 ~2021.05.20
	 */
	@NotNull
	public static final Processor PROCESSOR =
			new SequentialProcessor(
					/* Commands */
					Processors.CX_CMD,
					/* Detect if flows */
					Processors.CX_FLW_IF,
					/* Detect strings */
					Processors.VL_STR
			);

	/**
	 * The default jamplate initializer.
	 *
	 * @since 0.2.0 ~2021.05.29
	 */
	@NotNull
	public static final Initializer INITIALIZER =
			new ProcessorsInitializer(
					/* Parse stage, parse everything */
					Jamplate.PARSER_PROCESSOR,
					/* Analyze stage, convert wildcards into specifics */
					Jamplate.ANALYZER_PROCESSOR,
					/* Process/link contexts and flows */
					Jamplate.PROCESSOR,
					/* Compile into one instruction */
					Jamplate.COMPILER_PROCESSOR
			);

	/**
	 * Utility classes must not be initialized.
	 *
	 * @throws AssertionError when called.
	 * @since 0.2.0 ~2021.05.29
	 */
	private Jamplate() {
		throw new AssertionError("No instance for you");
	}
}
