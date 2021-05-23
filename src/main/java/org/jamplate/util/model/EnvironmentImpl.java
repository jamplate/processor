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
package org.jamplate.util.model;

import org.jamplate.diagnostic.Diagnostic;
import org.jamplate.model.Compilation;
import org.jamplate.model.Document;
import org.jamplate.model.Environment;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A basic implementation of the interface {@link Environment}.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.19
 */
public class EnvironmentImpl implements Environment {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -1644390742511931321L;

	/**
	 * The compilations in this environment.
	 *
	 * @since 0.2.0 ~2021.05.19
	 */
	@NotNull
	protected final Map<Document, Compilation> compilations = new HashMap<>();

	/**
	 * The diagnostic manager in this enlivenment.
	 *
	 * @since 0.2.0 ~2021.05.19
	 */
	@NotNull
	protected final Diagnostic diagnostic = new DiagnosticImpl();
	/**
	 * The additional meta-data of this sketch.
	 *
	 * @since 0.2.0 ~2021.05.21
	 */
	@NotNull
	protected final Map<String, Object> meta = new HashMap<>();

	@NotNull
	@Override
	public Compilation getCompilation(@NotNull String name) {
		Objects.requireNonNull(name, "name");
		return this.compilations
				.entrySet()
				.parallelStream()
				.filter(entry -> entry.getKey().toString().equals(name))
				.map(Map.Entry::getValue)
				.findAny()
				.orElse(null);
	}

	@Nullable
	@Override
	public Compilation getCompilation(@NotNull Document document) {
		Objects.requireNonNull(document, "document");
		return this.compilations.get(document);
	}

	@NotNull
	@Override
	public Diagnostic getDiagnostic() {
		return this.diagnostic;
	}

	@NotNull
	@Override
	public Map<String, Object> getMeta() {
		return Collections.checkedMap(this.meta, String.class, Object.class);
	}

	@NotNull
	@Override
	public Compilation optCompilation(@NotNull Document document) {
		Objects.requireNonNull(document, "document");
		return this.compilations.computeIfAbsent(document, k ->
				new CompilationImpl(this, new Tree(document))
		);
	}

	@Override
	public void setCompilation(@NotNull Document document, @NotNull Compilation compilation) {
		Objects.requireNonNull(document, "document");
		Objects.requireNonNull(compilation, "compilation");
		this.compilations.put(document, compilation);
	}
}
//gate
/*
	@NotNull
	@Override
	public Instruction compile(@NotNull Compilation compilation) {
		Objects.requireNonNull(compilation, "compilation");
		return this.instructions.computeIfAbsent(compilation, k -> {
			Tree root = compilation.getRootTree();
			Instruction instruction = this.compiler.compile(this.compiler, compilation, root);

			if (instruction == null)
				throw new CompilationException("Unrecognized compilation", root);

			return instruction;
		});
	}*/
//
//	@NotNull
//	@Override
//	public Compilation parse(@NotNull Document document) {
//		Objects.requireNonNull(document, "document");
//		//noinspection OverlyLongLambda
//		return this.compilations.computeIfAbsent(document,
//				k -> {
//					Tree root = new Tree(document);
//					Compilation compilation = new CompilationImpl(this, root);
//
//					while (true) {
//						Set<Tree> treeSet = this.parser.parse(compilation, root);
//
//						if (treeSet.isEmpty())
//							return compilation;
//
//						for (Tree tree : treeSet)
//							if (tree.reference().equals(root.reference()))
//								//its OK baby its OK to takeover :)
//								root.setSketch(tree.getSketch());
//							else
//								root.offer(tree);
//					}
//				}
//		);
//	}
/**/
//functions
//
//	@NotNull
//	@Override
//	public Compiler getCompiler() {
//		return this.compiler;
//	}
//
//	@Override
//	public void setCompiler(@NotNull Compiler compiler) {
//		Objects.requireNonNull(compiler, "compiler");
//		this.compiler = compiler;
//	}
//
//	@NotNull
//	@Override
//	public Parser getParser() {
//		return this.parser;
//	}
//
//	@Override
//	public void setParser(@NotNull Parser parser) {
//		Objects.requireNonNull(parser, "parser");
//		this.parser = parser;
//	}
//
//	/**
//	 * The compiler used by this compilation.
//	 *
//	 * @since 0.2.0 ~2021.05.21
//	 */
//	@NotNull
//	protected Compiler compiler = (compiler, compilation, tree) -> null;
//	/**
//	 * The parser used by this environment.
//	 *
//	 * @since 0.2.0 ~2021.05.21
//	 */
//	@NotNull
//	protected Parser parser = (compilation, tree) -> Collections.emptySet();
//

//getter `instruction`
//
//	@Nullable
//	@Override
//	public Instruction getInstruction(@NotNull String name) {
//		return this.instructions
//				.entrySet()
//				.parallelStream()
//				.filter(entry ->
//						entry.getKey()
//							 .getRootTree()
//							 .document()
//							 .toString()
//							 .equals(name)
//				)
//				.map(Map.Entry::getValue)
//				.findAny()
//				.orElse(null);
//	}
//
//	@Nullable
//	@Override
//	public Instruction getInstruction(@NotNull Document document) {
//		Objects.requireNonNull(document, "document");
//		return this.instructions
//				.entrySet()
//				.parallelStream()
//				.filter(entry ->
//						entry.getKey()
//							 .getRootTree()
//							 .document()
//							 .equals(document)
//				)
//				.map(Map.Entry::getValue)
//				.findAny()
//				.orElse(null);
//	}
//
//	@Nullable
//	@Override
//	public Instruction getInstruction(@NotNull Compilation compilation) {
//		Objects.requireNonNull(compilation, "compilation");
//		return this.instructions.get(compilation);
//	}
//
//	//setter `instruction`
//
//	@Override
//	public void setInstruction(@NotNull Compilation compilation, @NotNull Instruction instruction) {
//		Objects.requireNonNull(compilation, "compilation");
//		Objects.requireNonNull(instruction, "instruction");
//		this.instructions.put(compilation, instruction);
//	}
//	/**
//	 * The executables in this environment.
//	 *
//	 * @since 0.2.0 ~2021.05.21
//	 */
//	@NotNull
//	protected final Map<Compilation, Instruction> instructions = new HashMap<>();
