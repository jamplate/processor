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

import org.jamplate.impl.compiler.FlattenCompiler;
import org.jamplate.impl.compiler.KindCompiler;
import org.jamplate.impl.instruction.*;
import org.jamplate.impl.util.Trees;
import org.jamplate.model.*;
import org.jamplate.model.function.Compiler;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * A class containing the default jamplate implementation compilers.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.21
 */
@SuppressWarnings("OverlyCoupledClass")
public final class Compilers {
	//CX CMD

	/**
	 * A compiler that compiles console commands.
	 *
	 * @since 0.2.0 ~2021.05.21
	 */
	@NotNull
	public static final Compiler CX_CMD_CONSOLE =
			new KindCompiler(Kind.CX_CMD_CONSOLE, (compiler, compilation, tree) -> {
				Tree paramT = tree.getSketch().get(Component.PARAMETER).getTree();
				Instruction instruction = compiler.compile(compiler, compilation, paramT);

				return new ConsoleExecInstr(
						tree,
						instruction == null ?
						Instruction.empty(paramT) :
						instruction
				);
			});

	/**
	 * A compiler that compiles declare commands.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	@NotNull
	public static final Compiler CX_CMD_DECLARE =
			new KindCompiler(Kind.CX_CMD_DECLARE, (compiler, compilation, tree) -> {
				Tree keyT = tree.getSketch().get(Component.KEY).getTree();
				Tree paramT = tree.getSketch().get(Component.PARAMETER).getTree();

				String address = Trees.read(keyT).toString();
				Instruction instruction = compiler.compile(compiler, compilation, paramT);

				if (instruction == null)
					throw new CompileException(
							"Unrecognized parameter",
							paramT
					);

				return new RepllocAddrExecInstr(
						tree,
						address,
						instruction
				);
			});

	/**
	 * A compiler that compiles define commands.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	@NotNull
	public static final Compiler CX_CMD_DEFINE =
			new KindCompiler(Kind.CX_CMD_DEFINE, (compiler, compilation, tree) -> {
				Tree keyT = tree.getSketch().get(Component.KEY).getTree();
				Tree paramT = tree.getSketch().get(Component.PARAMETER).getTree();

				String address = Trees.read(keyT).toString();
				Instruction instruction = compiler.compile(compiler, compilation, paramT);

				if (instruction == null)
					throw new CompileException(
							"Unrecognized parameter",
							paramT
					);

				return new RepllocAddrExecInstr(
						tree,
						address,
						instruction
				);
			});

	/**
	 * A compiler that compiles include commands.
	 *
	 * @since 0.2.0 ~2021.05.21
	 */
	@NotNull
	public static final Compiler CX_CMD_INCLUDE =
			new KindCompiler(Kind.CX_CMD_INCLUDE, (compiler, compilation, tree) -> {
				Tree paramT = tree.getSketch().get(Component.PARAMETER).getTree();

				Instruction instruction = compiler.compile(compiler, compilation, paramT);

				if (instruction == null)
					throw new CompileException(
							"Unrecognized parameter",
							paramT
					);

				return new ExecImportExecInstr(
						tree,
						instruction
				);
			});

	//CX FLW

	/**
	 * A compiler that compiles if-contexts. (including the if, elif-s, else and endif in
	 * it)
	 *
	 * @since 0.2.0 ~2021.05.24
	 */
	@SuppressWarnings("OverlyLongLambda")
	@NotNull
	public static final Compiler CX_FLW_IF =
			new KindCompiler(Kind.CX_FLW_IF, (compiler, compilation, tree) -> {
				Map<Instruction, List<Instruction>> instructions = new LinkedHashMap<>();
				List<Instruction> current = null;

				//assert IF/IFDEF/IFNDEF is the first and ENDIF is the last
				for (Tree t : Trees.flatChildren(tree))
					switch (t.getSketch().getKind()) {
						case Kind.CX_CMD_IF:
						case Kind.CX_CMD_ELIF: {
							Tree paramT = t.getSketch().get(Component.PARAMETER).getTree();
							Instruction instruction = compiler.compile(compiler, compilation, paramT);

							instructions.put(instruction, current = new ArrayList<>());
							break;
						}
						case Kind.CX_CMD_IFDEF:
						case Kind.CX_CMD_ELIFDEF: {
							Tree keyT = t.getSketch().get(Component.KEY).getTree();
							String address = Trees.read(keyT).toString();

							instructions.put(new DefAddr(keyT, address), current = new ArrayList<>());
							break;
						}
						case Kind.CX_CMD_IFNDEF:
						case Kind.CX_CMD_ELIFNDEF: {
							Tree keyT = t.getSketch().get(Component.KEY).getTree();
							String address = Trees.read(keyT).toString();

							instructions.put(new NdefAddr(keyT, address), current = new ArrayList<>());
							break;
						}
						case Kind.CX_CMD_ELSE: {
							instructions.put(null, current = new ArrayList<>());
							break;
						}
						case Kind.CX_CMD_ENDIF: {
							//done
							current = null;
							break;
						}
						default: {
							//intermediate instruction
							Instruction instruction = compiler.compile(compiler, compilation, t);

							current.add(instruction);
							break;
						}
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
			});

	//CX INJ

	/**
	 * A compiler that compiles injections.
	 *
	 * @since 0.2.0 ~2021.05.25
	 */
	@NotNull
	public static final Compiler CX_INJ =
			new KindCompiler(Kind.CX_INJ, (compiler, compilation, tree) -> {
				Tree parameterT = tree.getSketch().get(Component.PARAMETER).getTree();

				Instruction instruction = compiler.compile(compiler, compilation, parameterT);

				if (instruction == null)
					throw new CompileException(
							"Unrecognized parameter",
							parameterT
					);

				return new PrintExecInstr(
						tree,
						instruction
				);
			});

	//CX TXT

	/**
	 * A compiler that compiles the non-command text.
	 *
	 * @since 0.2.0 ~2021.05.21
	 */
	@NotNull
	public static final Compiler CX_TXT =
			new FlattenCompiler((compiler, compilation, tree) ->
					new ReprntConst(tree)
			);

	//SX

	/**
	 * A compiler that compiles suppressed line separators.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	@NotNull
	public static final Compiler SX_EOL_SUPPRESSED =
			new KindCompiler(Kind.SX_EOL_SUPPRESSED, (compiler, compilation, tree) ->
					Instruction.empty(tree)
			);

	//VL

	/**
	 * A compiler that compiles numbers.
	 *
	 * @since 0.2.0 ~2021.05.25
	 */
	@NotNull
	public static final Compiler VL_NUM =
			new KindCompiler(Kind.VL_NUM, (compiler, compilation, tree) -> {
				String constant = Trees.read(tree).toString();

				return new PushConst(constant);
			});

	/**
	 * A compiler that compiles reference instructions.
	 *
	 * @since 0.2.0 ~2021.05.24
	 */
	@NotNull
	public static final Compiler VL_REF =
			new KindCompiler(Kind.VL_REF, (compiler, compilation, tree) -> {
				String address = Trees.read(tree).toString();

				return new PushEvalAddr(tree, address);
			});

	/**
	 * A compiler that compiles strings.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	@NotNull
	public static final Compiler VL_STR =
			new KindCompiler(Kind.VL_STR, (compiler, compilation, tree) -> {
				Sketch sketch = tree.getSketch();
				Tree open = sketch.get(Component.OPEN).getTree();
				Tree close = sketch.get(Component.CLOSE).getTree();

				int p = open.reference().position() +
						open.reference().length();
				int l = close.reference().position() - p;

				Tree content = new Tree(tree.document(), new Reference(p, l));

				content.getSketch().setKind(Kind.VL_STR_CONTENT);

				return new PushConst(content);
			});

	/**
	 * Utility classes must not be initialized.
	 *
	 * @throws AssertionError when called.
	 * @since 0.2.0 ~2021.05.21
	 */
	private Compilers() {
		throw new AssertionError("No instance for you");
	}
}
