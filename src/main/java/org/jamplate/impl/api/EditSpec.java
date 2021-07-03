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
package org.jamplate.impl.api;

import org.jamplate.api.Spec;
import org.jamplate.function.Compiler;
import org.jamplate.function.*;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * A variable functions implementation of the interface {@link Spec}.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.19
 */
public class EditSpec implements Spec {
	/**
	 * The current set pre-analyze processor.
	 *
	 * @since 0.3.0 ~2021.06.19
	 */
	@NotNull
	protected Processor analyzeProcessor = Processor.IDLE;
	/**
	 * The current set analyzer.
	 *
	 * @since 0.3.0 ~2021.06.19
	 */
	@NotNull
	protected Analyzer analyzer = Analyzer.IDLE;
	/**
	 * The current set pre-compile processor.
	 *
	 * @since 0.3.0 ~2021.06.19
	 */
	@NotNull
	protected Processor compileProcessor = Processor.IDLE;
	/**
	 * The current set compiler.
	 *
	 * @since 0.3.0 ~2021.06.19
	 */
	@NotNull
	protected Compiler compiler = Compiler.IDLE;
	/**
	 * The current set initializer.
	 *
	 * @since 0.3.0 ~2021.07.02
	 */
	@NotNull
	protected Initializer initializer = Initializer.IDLE;
	/**
	 * The current set listener.
	 *
	 * @since 0.3.0 ~2021.07.02
	 */
	@NotNull
	protected Listener listener = Listener.IDLE;
	/**
	 * The current set pre-parse processor.
	 *
	 * @since 0.3.0 ~2021.06.19
	 */
	@NotNull
	protected Processor parseProcessor = Processor.IDLE;
	/**
	 * The current set parser.
	 *
	 * @since 0.3.0 ~2021.06.19
	 */
	@NotNull
	protected Parser parser = Parser.IDLE;
	/**
	 * The qualified name of this spec.
	 *
	 * @since 0.3.0 ~2021.06.19
	 */
	@NotNull
	protected String qualifiedName;

	/**
	 * Construct a new variable functions spec.
	 *
	 * @since 0.3.0 ~2021.06.19
	 */
	public EditSpec() {
		this.qualifiedName = "EditSpec" + this.hashCode();
	}

	/**
	 * Construct a new variable functions spec.
	 *
	 * @param qualifiedName the qualified name of the constructed spec.
	 * @throws NullPointerException if the given {@code qualifiedName} is null.
	 * @since 0.3.0 ~2021.06.19
	 */
	public EditSpec(@NotNull String qualifiedName) {
		Objects.requireNonNull(qualifiedName, "qualifiedName");
		this.qualifiedName = qualifiedName;
	}

	@NotNull
	@Override
	public Processor getAnalyzeProcessor() {
		return this.analyzeProcessor;
	}

	@NotNull
	@Override
	public Analyzer getAnalyzer() {
		return this.analyzer;
	}

	@NotNull
	@Override
	public Processor getCompileProcessor() {
		return this.compileProcessor;
	}

	@NotNull
	@Override
	public Compiler getCompiler() {
		return this.compiler;
	}

	@NotNull
	@Override
	public Initializer getInitializer() {
		return this.initializer;
	}

	@NotNull
	@Override
	public Listener getListener() {
		return this.listener;
	}

	@NotNull
	@Override
	public Processor getParseProcessor() {
		return this.parseProcessor;
	}

	@NotNull
	@Override
	public Parser getParser() {
		return this.parser;
	}

	@NotNull
	@Override
	public String getQualifiedName() {
		return this.qualifiedName;
	}

	@Override
	public Spec setAnalyzeProcessor(@NotNull Processor processor) {
		Objects.requireNonNull(processor, "processor");
		this.analyzeProcessor = processor;
		return this;
	}

	@Override
	public Spec setAnalyzer(@NotNull Analyzer analyzer) {
		Objects.requireNonNull(analyzer, "analyzer");
		this.analyzer = analyzer;
		return this;
	}

	@Override
	public Spec setCompileProcessor(@NotNull Processor processor) {
		Objects.requireNonNull(processor, "processor");
		this.compileProcessor = processor;
		return this;
	}

	@Override
	public Spec setCompiler(@NotNull Compiler compiler) {
		Objects.requireNonNull(compiler, "compiler");
		this.compiler = compiler;
		return this;
	}

	@Override
	public Spec setInitializer(@NotNull Initializer initializer) {
		Objects.requireNonNull(initializer, "initializer");
		this.initializer = initializer;
		return this;
	}

	@Override
	public Spec setListener(@NotNull Listener listener) {
		Objects.requireNonNull(listener, "listener");
		this.listener = listener;
		return this;
	}

	@Override
	public Spec setParseProcessor(@NotNull Processor processor) {
		Objects.requireNonNull(processor, "processor");
		this.parseProcessor = processor;
		return this;
	}

	@Override
	public Spec setParser(@NotNull Parser parser) {
		Objects.requireNonNull(parser, "parser");
		this.parser = parser;
		return this;
	}

	@Override
	public Spec setQualifiedName(@NotNull String name) {
		Objects.requireNonNull(name, "name");
		this.qualifiedName = name;
		return this;
	}
}
