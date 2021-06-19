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
import org.jamplate.impl.diagnostic.MessageImpl;
import org.jamplate.impl.diagnostic.MessageKind;
import org.jamplate.impl.diagnostic.MessagePriority;
import org.jamplate.impl.initializer.ProcessorsInitializer;
import org.jamplate.impl.parser.*;
import org.jamplate.impl.processor.AnalyzerProcessor;
import org.jamplate.impl.processor.CompilerProcessor;
import org.jamplate.impl.processor.ParserProcessor;
import org.jamplate.impl.processor.SequentialProcessor;
import org.jamplate.model.*;
import org.jamplate.model.function.Compiler;
import org.jamplate.model.function.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * An all-in-one jamplate processor.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.23
 */
@SuppressWarnings("OverlyCoupledClass")
@Deprecated
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
					new RecursiveAnalyzer(Analyzers.DC_EOL_SUPPRESSED)
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
			new CombineCompiler(
					Compilers.DC_ROT,
					new ExclusiveCompiler(new FirstCompileCompiler(
							/* Non suppressed EOL */
							Compilers.DC_EOL,
							/* suppressed EOL */
							Compilers.DC_EOL_SUPPRESSED,
							/* Console commands */
							Compilers.CX_CMD_CONSOLE,
							/* Include commands */
							Compilers.CX_CMD_INCLUDE,
							/* Declare commands */
							Compilers.CX_CMD_DECLARE,
							/* Define commands */
							Compilers.CX_CMD_DEFINE,
							/* Error commands */
							Compilers.CX_CMD_ERROR,
							/* Message commands */
							Compilers.CX_CMD_MESSAGE,
							/* Spread commands */
							Compilers.CX_CMD_SPREAD,
							/* Undec commands */
							Compilers.CX_CMD_UNDEC,
							/* Undef commands */
							Compilers.CX_CMD_UNDEF,
							/* Capture flow */
							Compilers.CX_FLW_CAPTURE,
							/* If flow */
							Compilers.CX_FLW_IF,
							/* For flow */
							Compilers.CX_FLW_FOR,
							/* While flow */
							Compilers.CX_FLW_WHILE,
							/* Injections */
							Compilers.CX_INJ,
							/* Parameters, encapsulated to ignore outer compilers (ex. CX_TXT) */
							new FilterByKindCompiler(Kind.CX_PRM, new ExclusiveCompiler(
									/* Cleanup ws and throw if unrecognized */
									new FlattenCompiler(
											/* parsed areas */
											FallbackCompiler.INSTANCE,
											/* non parsed areas */
											new MandatoryCompiler(
													/* Ignore whitespace */
													ToIdleWhitespaceCompiler.INSTANCE
											)
									),
									/* Compile recognized logic */
									new FirstCompileCompiler(
											/* References */
											Compilers.CX_PCM_REF,
											/* Logic (Math) */
											Compilers.CX_PCM_LGC,
											/* Object */
											Compilers.SX_CUR,
											/* Reference */
											Compilers.SX_NME,
											/* Number */
											Compilers.SX_NUM,
											/* DQ String */
											Compilers.SX_DQT,
											/* SQ String */
											Compilers.SX_QTE,
											/* Parentheses */
											Compilers.SX_RND,
											/* Array */
											Compilers.SX_SQR,
											/* Nested Parameter */
											new FilterByKindCompiler(Kind.CX_PRM, new FlattenCompiler(
													FallbackCompiler.INSTANCE,
													FallbackCompiler.INSTANCE
											))
									)
							)),
							/* Fallthrough, convert into REPRNT */
							Compilers.CX_TXT
					))
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
			new CollectParser(new MergeByWeightParser(
					/* Separate the file by lines. */
					Parsers.DC_EOL,
					/* Wild Block Battles (first occurs win) */
					new MergeByOrderParser(new CombineParser(
							/* Comment blocks */
							Parsers.CM_BLK,
							/* Strings, encapsulated to parse inner components */
							new CombineParser(
									/* Double quote strings */
									Parsers.SX_QTE,
									/* Single quote strings */
									Parsers.SX_DQT
							)
					)),
					/* Always ignore commented end-of-lines */
					Parsers.CM_SLN,
					/* Runtime elements */
					new MergeFlatByWeightParser(
							/* Injections, injection must win over commands */
							Parsers.CX_INJ,
							new ThenOfferParser(
									/* Commands */
									Parsers.CX_CMD,
									/* each command kind */
									new CollectParser(new CombineParser(
											/* Parse Capture command components */
											Parsers.CX_CMD_CAPTURE,
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
											/* Parse Endcapture command components */
											Parsers.CX_CMD_ENDCAPTURE,
											/* Parse Endfor command components */
											Parsers.CX_CMD_ENDFOR,
											/* Parse Endif command components */
											Parsers.CX_CMD_ENDIF,
											/* Parser Endwhile command components */
											Parsers.CX_CMD_ENDWHILE,
											/* Parse Error command components */
											Parsers.CX_CMD_ERROR,
											/* Parse For command components */
											Parsers.CX_CMD_FOR,
											/* Parse If command components */
											Parsers.CX_CMD_IF,
											/* Parse Ifdef command components */
											Parsers.CX_CMD_IFDEF,
											/* Parse Ifndef command components */
											Parsers.CX_CMD_IFNDEF,
											/* Parse Include command components */
											Parsers.CX_CMD_INCLUDE,
											/* Parse Message command components */
											Parsers.CX_CMD_MESSAGE,
											/* Parse Spread command components */
											Parsers.CX_CMD_SPREAD,
											/* Parse Undec command components */
											Parsers.CX_CMD_UNDEC,
											/* Parse Undef command components */
											Parsers.CX_CMD_UNDEF,
											/* Parse While command components */
											Parsers.CX_CMD_WHILE
									))
							)
					),
					/* Parameters */
					new FilterHierarchyByKindParser(
							Kind.CX_PRM,
							new MergeByOrderParser(new HierarchyParser(new MergeByWeightParser(
									/* Braces */
									Parsers.SX_CUR,
									/* Brackets */
									Parsers.SX_SQR,
									/* Parentheses */
									Parsers.SX_RND,
									/* Names */
									Parsers.SX_NME,
									/* Numbers */
									Parsers.SX_NUM,
									/* Colons */
									Parsers.SX_CLN,
									/* Commas */
									Parsers.SX_CMA,
									/* The add (+) operator */
									Parsers.OP_ADD,
									/* The divide (/) operator */
									Parsers.OP_DIV,
									/* The equals (==) operator */
									Parsers.OP_EQL,
									/* The less-than-or-equals (<=) operator */
									Parsers.OP_LEQ,
									/* The logical-and (&&) operator */
									Parsers.OP_LND,
									/* The logical-or (||) operator */
									Parsers.OP_LOR,
									/* The less-than (<) operator */
									Parsers.OP_LTN,
									/* The modulo (%) operator */
									Parsers.OP_MOD,
									/* The more-than-or-equals (>=) operator */
									Parsers.OP_MEQ,
									/* The more-than (>) operator */
									Parsers.OP_MTN,
									/* The multiply (*) operator */
									Parsers.OP_MUL,
									/* The not-equals (!=) operator */
									Parsers.OP_NQL,
									/* The not (!) operator; check for NQL first */
									Parsers.OP_NEG,
									/* The subtract (-) operator */
									Parsers.OP_SUB
							)))
					)
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
					/* Clean unwanted children */
					Processors.OZ_CLEAN,
					/* Commands */
					Processors.CX_CMD,
					/* Accessors in #declare  */
					Processors.CX_CMD_DECLARE,
					/* Detect capture flows */
					Processors.CX_FLW_CAPTURE,
					/* Detect for flows */
					Processors.CX_FLW_FOR,
					/* Detect while flows */
					Processors.CX_FLW_WHILE,
					/* Detect if flows */
					Processors.CX_FLW_IF,
					/* Detect references */
					Processors.CX_PCM_REF,
					/* Solve logic */
					Processors.CX_PCM_LGC
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

	/**
	 * Compile the given {@code documents} to the given {@code environment}.
	 * <br>
	 * Null documents in the given array are ignored.
	 *
	 * @param environment the environment to compile to.
	 * @param documents   the documents to be compiled.
	 * @return true, if all of the given {@code documents} was successfully compiled.
	 * @throws NullPointerException if the given {@code environment} or {@code documents}
	 *                              is null.
	 * @since 0.2.0 ~2021.05.31
	 */
	@Contract(mutates = "param1,param2")
	public static boolean compile(@NotNull Environment environment, @Nullable Document @NotNull ... documents) {
		Objects.requireNonNull(documents, "documents");
		boolean success = true;

		for (Document document : documents)
			if (document != null)
				try {
					Jamplate.INITIALIZER.initialize(
							environment,
							document
					);
				} catch (IllegalTreeException e) {
					environment.getDiagnostic()
							   .print(new MessageImpl(
									   e,
									   MessagePriority.ERROR,
									   MessageKind.COMPILE,
									   true,
									   e.getPrimaryTree(),
									   e.getIllegalTree()
							   ));
					success = false;
				} catch (CompileException e) {
					environment.getDiagnostic()
							   .print(new MessageImpl(
									   e,
									   MessagePriority.ERROR,
									   MessageKind.COMPILE,
									   true,
									   e.getTree()
							   ));
					success = false;
				} catch (Throwable e) {
					environment.getDiagnostic()
							   .print(new MessageImpl(
									   e,
									   MessagePriority.ERROR,
									   MessageKind.COMPILE,
									   true,
									   new Tree(document)
							   ));
					success = false;
				}

		return success;
	}

	/**
	 * Execute the given {@code compilations} in order with respect to the given {@code
	 * environment}.
	 * <br>
	 * Null compilations or non-compiled compilations in the given array are ignored.
	 *
	 * @param environment  the environment to execute on.
	 * @param compilations the compilations to be executed (in order).
	 * @return true, if all of the given {@code compilations} was successfully executed.
	 * @throws NullPointerException if the given {@code environment} or {@code
	 *                              compilations} is null.
	 * @since 0.2.0 ~2021.05.31
	 */
	public static boolean execute(@NotNull Environment environment, @Nullable Compilation @NotNull ... compilations) {
		Objects.requireNonNull(environment, "environment");
		Objects.requireNonNull(compilations, "compilations");
		//noinspection OverlyLongLambda
		return Jamplate.execute(environment, compilation -> {
			Memory memory = new Memory();

			Object memoryDefaults = environment.getMeta().get(Meta.MEMORY);

			if (memoryDefaults instanceof Map)
				((Map<?, ?>) memoryDefaults).forEach((k, v) -> {
					String address = String.valueOf(k);

					if (v instanceof Value) {
						Value value = (Value) v;

						memory.set(address, value);
					} else {
						String text = String.valueOf(v);

						memory.set(address, m -> text);
					}
				});

			//set project dir
			memory.set(
					Address.PROJECT,
					m -> String.valueOf(environment.getMeta().getOrDefault(Meta.PROJECT, ""))
			);
			memory.set(
					Address.OUTPUT,
					m -> String.valueOf(environment.getMeta().getOrDefault(Meta.OUTPUT, ""))
			);
			memory.set(
					Address.JAMPLATE,
					s -> "0.2.3"
			);
			//noinspection UseOfObsoleteDateTimeApi
			memory.set(
					Address.TIME,
					m -> new SimpleDateFormat("HH:mm:ss")
							.format(new Date())
			);
			//noinspection UseOfObsoleteDateTimeApi
			memory.set(
					Address.DATE,
					m -> new SimpleDateFormat("MMM dd yyyy")
							.format(new Date())
			);

			return memory;
		}, compilations);
	}

	/**
	 * Execute the given {@code compilations} in order with respect to the given {@code
	 * environment}.
	 * <br>
	 * Null compilations or non-compiled compilations in the given array are ignored.
	 *
	 * @param environment    the environment to execute on.
	 * @param memorySupplier a supplier used to create new memory instances.
	 * @param compilations   the compilations to be executed (in order).
	 * @return true, if all of the given {@code compilations} was successfully executed.
	 * @throws NullPointerException if the given {@code environment} or {@code
	 *                              memorySupplier} or {@code compilations} is null.
	 * @since 0.2.0 ~2021.05.31
	 */
	@Contract(mutates = "param1,param2")
	public static boolean execute(@NotNull Environment environment, @NotNull Function<Compilation, Memory> memorySupplier, @Nullable Compilation @NotNull ... compilations) {
		Objects.requireNonNull(environment, "environment");
		Objects.requireNonNull(compilations, "compilations");
		boolean success = true;

		for (Compilation compilation : compilations) {
			Instruction instruction = compilation.getInstruction();

			if (instruction != null) {
				Memory memory = memorySupplier.apply(compilation);

				try {
					memory.pushFrame(new Frame(instruction));

					instruction.exec(environment, memory);

					memory.dumpFrame();
				} catch (ExecutionException e) {
					environment.getDiagnostic()
							   .print(new MessageImpl(
									   e,
									   memory,
									   MessagePriority.ERROR,
									   MessageKind.EXECUTION,
									   true,
									   e.getTree()
							   ));
					success = false;
				} catch (Throwable e) {
					environment.getDiagnostic()
							   .print(new MessageImpl(
									   e,
									   memory,
									   MessagePriority.ERROR,
									   MessageKind.EXECUTION,
									   true,
									   compilation.getRootTree()
							   ));
					success = false;
				} finally {
					try {
						memory.close();
					} catch (IOException e) {
						environment.getDiagnostic()
								   .print(new MessageImpl(
										   e,
										   memory,
										   MessagePriority.WARNING,
										   MessageKind.EXECUTION,
										   false,
										   compilation.getRootTree()
								   ));
					}
				}
			}
		}

		return success;
	}
}
