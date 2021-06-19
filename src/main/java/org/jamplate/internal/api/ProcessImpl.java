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
package org.jamplate.internal.api;

import org.jamplate.api.Process;
import org.jamplate.api.Spec;
import org.jamplate.diagnostic.Diagnostic;
import org.jamplate.diagnostic.Message;
import org.jamplate.function.Analyzer;
import org.jamplate.function.Compiler;
import org.jamplate.function.Parser;
import org.jamplate.function.Processor;
import org.jamplate.internal.diagnostic.MessageImpl;
import org.jamplate.internal.diagnostic.MessageKind;
import org.jamplate.internal.diagnostic.MessagePriority;
import org.jamplate.internal.model.CompilationImpl;
import org.jamplate.internal.model.EnvironmentImpl;
import org.jamplate.model.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;

/**
 * An implementation of the interface {@link Process}.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.19
 */
@SuppressWarnings({"OverlyCoupledMethod", "OverlyCoupledClass"})
public class ProcessImpl implements Process {
	/**
	 * The current environment.
	 *
	 * @since 0.3.0 ~2021.06.19
	 */
	@NotNull
	protected Environment environment;
	/**
	 * The spec to follow.
	 *
	 * @since 0.3.0 ~2021.06.19
	 */
	@NotNull
	protected Spec spec;

	/**
	 * Construct a new unit.
	 *
	 * @since 0.3.0 ~2021.06.19
	 */
	public ProcessImpl() {
		this.environment = new EnvironmentImpl();
		this.spec = new ContextSpec();
	}

	@Override
	public boolean analyze(@NotNull Document document) {
		Objects.requireNonNull(document, "document");
		Environment environment = this.environment;
		Spec spec = this.spec;

		Compilation compilation = this.requireCompilation(document);
		Tree root = compilation.getRootTree();

		try {
			Processor processor = spec.getAnalyzeProcessor();
			Analyzer analyzer = spec.getAnalyzer();

			//pre-analyze process
			processor.process(compilation);

			//analyze
			while (analyzer.analyze(compilation, root))
				;

			return true;
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
			return false;
		} catch (CompileException e) {
			environment.getDiagnostic()
					   .print(new MessageImpl(
							   e,
							   MessagePriority.ERROR,
							   MessageKind.COMPILE,
							   true,
							   e.getTree()
					   ));
			return false;
		} catch (Throwable e) {
			environment.getDiagnostic()
					   .print(new MessageImpl(
							   e,
							   MessagePriority.ERROR,
							   MessageKind.COMPILE,
							   true,
							   root
					   ));
			return false;
		}
	}

	@Override
	public boolean compile(@NotNull Document document) {
		Objects.requireNonNull(document, "document");
		Environment environment = this.environment;
		Spec spec = this.spec;

		Compilation compilation = this.requireCompilation(document);
		Tree root = compilation.getRootTree();

		try {
			Processor processor = spec.getCompileProcessor();
			Compiler compiler = spec.getCompiler();

			//pre-compile process
			processor.process(compilation);

			//compile
			Instruction instruction = compiler.compile(compiler, compilation, root);

			if (instruction == null) {
				environment.getDiagnostic()
						   .print(new MessageImpl(
								   "No instruction resulted",
								   MessagePriority.ERROR,
								   MessageKind.COMPILE,
								   false,
								   root
						   ));
				return false;
			}

			compilation.setInstruction(instruction);
			return true;
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
			return false;
		} catch (CompileException e) {
			environment.getDiagnostic()
					   .print(new MessageImpl(
							   e,
							   MessagePriority.ERROR,
							   MessageKind.COMPILE,
							   true,
							   e.getTree()
					   ));
			return false;
		} catch (Throwable e) {
			environment.getDiagnostic()
					   .print(new MessageImpl(
							   e,
							   MessagePriority.ERROR,
							   MessageKind.COMPILE,
							   true,
							   root
					   ));
			return false;
		}
	}

	@Override
	public void diagnostic() {
		Environment environment = this.environment;
		Spec spec = this.spec;

		Diagnostic diagnostic = environment.getDiagnostic();

		for (Message message : diagnostic)
			spec.onDiagnostic(environment, message);
	}

	@Override
	public boolean execute(@NotNull Document document) {
		Objects.requireNonNull(document, "document");
		Environment environment = this.environment;
		Spec spec = this.spec;

		Compilation compilation = this.requireCompilation(document);
		Instruction instruction = compilation.getInstruction();
		Tree root = compilation.getRootTree();

		if (instruction == null)
			throw new IllegalStateException("No instruction to execute");

		Memory memory = new Memory();

		//pre-execution
		spec.onCreateMemory(compilation, memory);

		//execute
		try {
			memory.pushFrame(new Frame(instruction));

			instruction.exec(environment, memory);

			memory.dumpFrame();
			return true;
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
			return false;
		} catch (Throwable e) {
			environment.getDiagnostic()
					   .print(new MessageImpl(
							   e,
							   memory,
							   MessagePriority.ERROR,
							   MessageKind.EXECUTION,
							   true,
							   root
					   ));
			return false;
		} finally {
			try {
				memory.close();
			} catch (IOException e) {
				environment.getDiagnostic()
						   .print(new MessageImpl(
								   e,
								   MessagePriority.WARNING,
								   MessageKind.EXECUTION,
								   false,
								   root
						   ));
			}
		}
	}

	@NotNull
	@Override
	public Environment getEnvironment() {
		return this.environment;
	}

	@NotNull
	@Override
	public Spec getSpec() {
		return this.spec;
	}

	@Override
	public boolean initialize(@NotNull Document document) {
		Objects.requireNonNull(document, "document");
		Environment environment = this.environment;

		if (environment.getCompilation(document) != null)
			throw new IllegalStateException(
					"Document already initialized: " + document
			);

		Compilation compilation = new CompilationImpl(
				environment,
				new Tree(document, new Sketch(), -1)
		);

		environment.setCompilation(document, compilation);
		return true;
	}

	@Override
	public boolean parse(@NotNull Document document) {
		Objects.requireNonNull(document, "document");
		Environment environment = this.environment;
		Spec spec = this.spec;

		Compilation compilation = this.requireCompilation(document);
		Tree root = compilation.getRootTree();

		try {
			Processor processor = spec.getParseProcessor();
			Parser parser = spec.getParser();

			//pre-parse process
			processor.process(compilation);

			//parse
			while (true) {
				Set<Tree> treeSet = parser.parse(compilation, root);

				if (treeSet.isEmpty())
					break;

				for (Tree tree : treeSet)
					root.offer(tree);
			}

			return true;
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
			return false;
		} catch (CompileException e) {
			environment.getDiagnostic()
					   .print(new MessageImpl(
							   e,
							   MessagePriority.ERROR,
							   MessageKind.COMPILE,
							   true,
							   e.getTree()
					   ));
			return false;
		} catch (Throwable e) {
			environment.getDiagnostic()
					   .print(new MessageImpl(
							   e,
							   MessagePriority.ERROR,
							   MessageKind.COMPILE,
							   true,
							   root
					   ));
			return false;
		}
	}

	@Override
	public void setEnvironment(@NotNull Environment environment) {
		Objects.requireNonNull(environment, "environment");
		this.environment = environment;
	}

	@Override
	public void setSpec(@NotNull Spec spec) {
		Objects.requireNonNull(spec, "spec");
		this.spec = spec;
	}

	/**
	 * Return the compilation associated to the given {@code document} in the environment.
	 * An exception will be thrown if no such compilation.
	 *
	 * @param document the document to get the compilation associated to it.
	 * @return the compilation associated to the given {@code document}.
	 * @throws NullPointerException  if the given {@code document} is null.
	 * @throws IllegalStateException if no compilation is associated to the given {@code
	 *                               document} in the environment.
	 * @since 0.3.0 ~2021.06.19
	 */
	@Contract(pure = true)
	protected Compilation requireCompilation(@NotNull Document document) {
		Objects.requireNonNull(document, "document");
		Environment environment = this.environment;
		Compilation compilation = environment.getCompilation(document);

		if (compilation == null)
			throw new IllegalStateException(
					"No compilation for the document: " + document
			);

		return compilation;
	}
}
