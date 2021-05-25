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
import org.jamplate.impl.compiler.StrictCompiler;
import org.jamplate.impl.instruction.*;
import org.jamplate.model.*;
import org.jamplate.model.function.Compiler;
import org.jamplate.model.function.Processor;
import org.jamplate.util.Trees;
import org.jamplate.util.model.function.CompilerProcessor;
import org.jamplate.util.model.function.OrderCompiler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * A class containing the default jamplate implementation compilers.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.21
 */
public final class Compilers {
	/**
	 * The default jamplate compiler.
	 *
	 * @since 0.2.0 ~2021.05.21
	 */
	@NotNull
	public static final Compiler COMPILER =
			new OrderCompiler(
					Syntax.LN_SUPPRESSED,
					Commands.CONSOLE,
					Commands.INCLUDE,
					Commands.DECLARE,
					Commands.DEFINE,
					Commands.IF_CONTEXT,
					Transient.INJECTION,
					Values.COMPILER,
					Syntax.TEXT
			);

	/**
	 * A processor that compiles the compilations given to it using {@link #COMPILER the
	 * jamplate default implementation compiler}.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	@NotNull
	public static final Processor PROCESSOR =
			new CompilerProcessor(Compilers.COMPILER);

	/**
	 * Utility classes must not be initialized.
	 *
	 * @throws AssertionError when called.
	 * @since 0.2.0 ~2021.05.21
	 */
	private Compilers() {
		throw new AssertionError("No instance for you");
	}

	/**
	 * Command compilers.
	 *
	 * @author LSafer
	 * @version 0.2.0
	 * @since 0.2.0 ~2021.05.21
	 */
	public static final class Commands {
		/**
		 * A compiler that compiles console commands.
		 *
		 * @since 0.2.0 ~2021.05.21
		 */
		@NotNull
		public static final Compiler CONSOLE =
				(compiler, compilation, tree) -> {
					if (tree.getSketch().getKind().equals(Kind.Command.CONSOLE)) {
						CommandSketch command = (CommandSketch) tree.getSketch();
						CommandSketch.ParameterSketch parameter = command.getParameterSketch();
						Tree parameterTree = parameter.getTree();

						Instruction paramInstruction = compiler.compile(compiler, compilation, parameterTree);

						return new ConsoleExecInstr(
								tree,
								paramInstruction == null ?
								Instruction.create(parameterTree, (e, m) -> {
								}) :
								paramInstruction
						);
					}

					return null;
				};

		/**
		 * A compiler that compiles declare commands.
		 *
		 * @since 0.2.0 ~2021.05.23
		 */
		@SuppressWarnings("OverlyLongLambda")
		@NotNull
		public static final Compiler DECLARE =
				(compiler, compilation, tree) -> {
					if (tree.getSketch().getKind().equals(Kind.Command.DECLARE)) {
						CommandSketch command = (CommandSketch) tree.getSketch();
						CommandSketch.ParameterSketch parameter = command.getParameterSketch();
						CommandSketch.ParameterSketch.KeySketch key = parameter.getKeySketch();
						CommandSketch.ParameterSketch.ValueSketch value = parameter.getValueSketch();
						Tree keyParameterTree = key.getTree();
						Tree valueParameterTree = value.getTree();

						String keyParameter = Trees.read(keyParameterTree).toString();
						Instruction parameterInstruction = compiler.compile(compiler, compilation, valueParameterTree);

						return new AllocAddrExecInstr(
								tree,
								keyParameter,
								parameterInstruction == null ?
								Instruction.create(valueParameterTree, (k, m) -> {
								}) :
								parameterInstruction
						);
					}

					return null;
				};

		/**
		 * A compiler that compiles define commands.
		 *
		 * @since 0.2.0 ~2021.05.23
		 */
		@SuppressWarnings("OverlyLongLambda")
		@NotNull
		public static final Compiler DEFINE =
				(compiler, compilation, tree) -> {
					if (tree.getSketch().getKind().equals(Kind.Command.DEFINE)) {
						CommandSketch command = (CommandSketch) tree.getSketch();
						CommandSketch.ParameterSketch parameter = command.getParameterSketch();
						CommandSketch.ParameterSketch.KeySketch key = parameter.getKeySketch();
						CommandSketch.ParameterSketch.ValueSketch value = parameter.getValueSketch();
						Tree keyParameterTree = key.getTree();
						Tree valueParameterTree = value.getTree();

						String keyParameter = Trees.read(keyParameterTree).toString();
						Instruction parameterInstruction = compiler.compile(compiler, compilation, valueParameterTree);

						return new RepllocAddrExecInstr(
								tree,
								keyParameter,
								parameterInstruction == null ?
								Instruction.create(valueParameterTree, (k, m) -> {
								}) :
								parameterInstruction
						);
					}

					return null;
				};

		/**
		 * A compiler that compiles if-contexts. (including the if, elif-s, else and endif
		 * in it)
		 *
		 * @since 0.2.0 ~2021.05.24
		 */
		@SuppressWarnings("OverlyLongLambda")
		@NotNull
		public static final Compiler IF_CONTEXT =
				(compiler, compilation, tree) -> {
					if (tree.getSketch().getKind().equals(Kind.Command.IF_CONTEXT)) {
						Map<Instruction, List<Instruction>> instructions = new LinkedHashMap<>();
						List<Instruction> current = null;

						//assert IF is the first and ENDIF is the last
						for (Tree t : Trees.flatChildren(tree))
							switch (t.getSketch().getKind()) {
								case Kind.Command.IF:
								case Kind.Command.ELIF:
									CommandSketch command = (CommandSketch) t.getSketch();
									Tree parameterTree = command.getParameterSketch().getTree();

									Instruction condition = compiler.compile(compiler, compilation, parameterTree);

									instructions.put(condition, current = new ArrayList<>());
									break;
								case Kind.Command.ELSE:
									instructions.put(null, current = new ArrayList<>());
									break;
								case Kind.Command.ENDIF:
									//done
									current = null;
									break;
								default:
									//intermediate instruction
									Instruction instruction = compiler.compile(compiler, compilation, t);

									current.add(instruction);
									break;
							}

						List<Instruction> conditions = new ArrayList<>(instructions.keySet());
						Collections.reverse(conditions);

						Instruction instruction = null;
						for (Instruction condition : conditions)
							if (condition == null)
								instruction = new IpedXinstr(
										tree,
										instructions.get(null)
								);
							else if (instruction == null)
								instruction = new BranchExecInstr0Instr1Instr2(
										tree,
										condition,
										new IpedXinstr(instructions.get(condition))
								);
							else
								instruction = new BranchExecInstr0Instr1Instr2(
										tree,
										condition,
										new IpedXinstr(instructions.get(condition)),
										instruction
								);

						return instruction;
					}

					return null;
				};

		/**
		 * A compiler that compiles include commands.
		 *
		 * @since 0.2.0 ~2021.05.21
		 */
		@SuppressWarnings("OverlyLongLambda")
		@NotNull
		public static final Compiler INCLUDE =
				(compiler, compilation, tree) -> {
					if (tree.getSketch().getKind().equals(Kind.Command.INCLUDE)) {
						CommandSketch command = (CommandSketch) tree.getSketch();
						CommandSketch.ParameterSketch parameter = command.getParameterSketch();
						Tree parameterTree = parameter.getTree();

						Instruction paramInstruction = compiler.compile(compiler, compilation, parameterTree);

						if (paramInstruction == null)
							throw new CompileException(
									"Unrecognized parameter",
									parameterTree
							);

						return new ExecImportExecInstr(tree, paramInstruction);
					}

					return null;
				};

		/**
		 * Utility classes must not be initialized.
		 *
		 * @throws AssertionError when called.
		 * @since 0.2.0 ~2021.05.21
		 */
		private Commands() {
			throw new AssertionError("No instance for you");
		}
	}

	/**
	 * Syntax-level compiles.
	 *
	 * @author LSafer
	 * @version 0.2.0
	 * @since 0.2.0 ~2021.05.23
	 */
	public static final class Syntax {
		/**
		 * A compiler that compiles suppressed line separators.
		 *
		 * @since 0.2.0 ~2021.05.23
		 */
		@NotNull
		public static final Compiler LN_SUPPRESSED =
				(compiler, compilation, tree) ->
						tree.getSketch().getKind().equals(Kind.Syntax.LN_SUPPRESSED) ?
						Instruction.create(tree, (environment, memory) -> {
						}) :
						null;

		/**
		 * A compiler that compiles the non-command text.
		 *
		 * @since 0.2.0 ~2021.05.21
		 */
		public static final Compiler TEXT =
				new StrictCompiler((compiler, compilation, tree) ->
						new ReprntConst(tree)
				);

		/**
		 * Utility classes must not be initialized.
		 *
		 * @throws AssertionError when called.
		 * @since 0.2.0 ~2021.05.23
		 */
		private Syntax() {
			throw new AssertionError("No instance for you");
		}
	}

	/**
	 * A class containing the compilers of the transient components.
	 *
	 * @author LSafer
	 * @version 0.2.0
	 * @since 0.2.0 ~2021.05.25
	 */
	public static final class Transient {
		/**
		 * A compiler that compiles injections.
		 *
		 * @since 0.2.0 ~2021.05.25
		 */
		@NotNull
		public static final Compiler INJECTION =
				(compiler, compilation, tree) -> {
					if (tree.getSketch().getKind().equals(Kind.Transient.INJECTION)) {
						InjectionSketch injection = (InjectionSketch) tree.getSketch();
						Tree parameterTree = injection.getParameterSketch().getTree();

						Instruction parameterInstruction = compiler.compile(compiler, compilation, parameterTree);

						if (parameterInstruction == null)
							throw new CompileException(
									"Unrecognized value",
									parameterTree
							);

						return new PrintExecInstr(
								tree,
								parameterInstruction
						);
					}

					return null;
				};

		/**
		 * Utility classes must not be initialized.
		 *
		 * @throws AssertionError when called.
		 * @since 0.2.0 ~2021.05.25
		 */
		private Transient() {
			throw new AssertionError("No instance for you");
		}
	}

	/**
	 * Values compilers.
	 *
	 * @author LSafer
	 * @version 0.2.0
	 * @since 0.2.0 ~2021.05.23
	 */
	public static final class Values {
		/**
		 * A compiler that compiles numbers.
		 *
		 * @since 0.2.0 ~2021.05.25
		 */
		@NotNull
		public static final Compiler NUMBER =
				(compiler, compilation, tree) -> {
					if (tree.getSketch().getKind().equals(Kind.Value.NUMBER)) {
						String value = Trees.read(tree).toString();

						return new PushConst(value);
					}

					return null;
				};

		/**
		 * A compiler that compiles reference instructions.
		 *
		 * @since 0.2.0 ~2021.05.24
		 */
		@NotNull
		public static final Compiler REFERENCE =
				(compiler, compilation, tree) -> {
					if (tree.getSketch().getKind().equals(Kind.Value.REFERENCE)) {
						String name = Trees.read(tree).toString();

						return new PushEvalAddr(tree, name);
					}

					return null;
				};

		/**
		 * A compiler that compiles strings.
		 *
		 * @since 0.2.0 ~2021.05.23
		 */
		@SuppressWarnings("OverlyLongLambda")
		@NotNull
		public static final Compiler STRING =
				(compiler, compilation, tree) -> {
					if (tree.getSketch().getKind().equals(Kind.Value.STRING)) {
						ScopeSketch scope = (ScopeSketch) tree.getSketch();
						Tree open = scope.getOpenAnchorSketch().getTree();
						Tree close = scope.getCloseAnchorSketch().getTree();

						int p = open.reference().position() +
								open.reference().length();
						int l = close.reference().position() - p;

						Tree content = new Tree(tree.document(), new Reference(p, l));

						content.getSketch().setKind(Kind.Value.STRING_CONTENT);

						return new PushConst(content);
					}

					return null;
				};

		/**
		 * The main values compiler.
		 *
		 * @since 0.2.0 ~2021.05.23
		 */
		@NotNull
		public static final Compiler COMPILER =
				new Compiler() {
					/**
					 * The compiler used to compile the context. Invoked with {@link #value} as the fallback compiler.
					 *
					 * @since 0.2.0 ~2021.05.23
					 */
					private final Compiler context =
							new StrictCompiler((compiler, compilation, tree) -> {
								if (Trees.read(tree).toString().trim().isEmpty())
									return Instruction.create(tree, (e, m) -> {
									});

								throw new CompileException(
										"Unrecognized value",
										tree
								);
							});

					/**
					 * The compiler used to compile the value. Invoked with {@link #fallback} as the fallback compiler.
					 *
					 * @since 0.2.0 ~2021.05.23
					 */
					@NotNull
					private final Compiler value =
							new OrderCompiler(
									Values.STRING,
									Values.REFERENCE,
									Values.NUMBER
							);

					/**
					 * The fallback compiler.
					 *
					 * @since 0.2.0 ~2021.05.23
					 */
					@NotNull
					private final Compiler fallback =
							(compiler, compilation, tree) ->
									this.context.compile(
											(c, cmp, t) -> this.value.compile(this, cmp, t),
											compilation,
											tree
									);

					@Nullable
					@Override
					public Instruction compile(@NotNull Compiler compiler, @NotNull Compilation compilation, @NotNull Tree tree) {
						switch (tree.getSketch().getKind()) {
							case Kind.Transient.COMMAND_PARAMETER:
							case Kind.Transient.COMMAND_PARAMETER_VALUE:
							case Kind.Transient.INJECTION_PARAMETER:
								return this.fallback.compile(compiler, compilation, tree);
							default:
								return null;
						}
					}
				};

		/**
		 * Utility classes must not be initialized.
		 *
		 * @throws AssertionError when called.
		 * @since 0.2.0 ~2021.05.23
		 */
		private Values() {
			throw new AssertionError("No instance for you");
		}
	}
}
