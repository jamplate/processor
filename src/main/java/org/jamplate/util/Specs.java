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
package org.jamplate.util;

import org.jamplate.unit.Spec;
import org.jamplate.function.Compiler;
import org.jamplate.function.*;
import org.jamplate.impl.spec.EditSpec;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Useful tools for the interface {@link Spec}.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.07.04
 */
public final class Specs {
	/**
	 * Utility classes must not be initialized.
	 *
	 * @throws AssertionError when called.
	 * @since 0.3.0 ~2021.07.04
	 */
	private Specs() {
		throw new AssertionError("No instance for you");
	}

	/**
	 * Return a new spec with the analyzer of the given {@code spec}.
	 *
	 * @param spec the spec to get the initial analyzer of the returned spec.
	 * @return a new spec with the analyzer of the given {@code spec}.
	 * @throws NullPointerException if the given {@code spec} is null.
	 * @since 0.3.0 ~2021.07.06
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static Spec analyzer(@NotNull Spec spec) {
		Objects.requireNonNull(spec, "spec");
		return Specs.analyzer(
				"AnalyzerSpec" + Integer.toHexString(spec.hashCode()),
				spec.getAnalyzer()
		).setAnalyzeProcessor(
				spec.getAnalyzeProcessor()
		);
	}

	/**
	 * Return a new spec with the given {@code analyzer}.
	 *
	 * @param analyzer the initial analyzer set to the returned spec.
	 * @return a new spec with the given {@code analyzer}.
	 * @throws NullPointerException if the given {@code analyzer} is null.
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static Spec analyzer(@NotNull Analyzer analyzer) {
		Objects.requireNonNull(analyzer, "analyzer");
		return Specs.analyzer(
				"AnalyzerSpec" + Integer.toHexString(analyzer.hashCode()),
				analyzer
		);
	}

	/**
	 * Return a new spec with the given {@code analyzer}.
	 *
	 * @param name     the initial qualified name of the returned spec.
	 * @param analyzer the initial analyzer set to the returned spec.
	 * @return a new spec with the given {@code analyzer}.
	 * @throws NullPointerException if the given {@code name} or {@code analyzer} is
	 *                              null.
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	@Contract(value = "_,_->new", pure = true)
	public static Spec analyzer(@NotNull String name, @NotNull Analyzer analyzer) {
		Objects.requireNonNull(name, "name");
		Objects.requireNonNull(analyzer, "analyzer");
		return new EditSpec(name)
				.setAnalyzer(analyzer);
	}

	/**
	 * Return a new spec with the compiler of the given {@code spec}.
	 *
	 * @param spec the spec to get the initial compiler of the returned spec.
	 * @return a new spec with the compiler of the given {@code spec}.
	 * @throws NullPointerException if the given {@code spec} is null.
	 * @since 0.3.0 ~2021.07.06
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static Spec compiler(@NotNull Spec spec) {
		Objects.requireNonNull(spec, "spec");
		return Specs.compiler(
				"CompilerSpec" + Integer.toHexString(spec.hashCode()),
				spec.getCompiler()
		).setAnalyzeProcessor(
				spec.getCompileProcessor()
		);
	}

	/**
	 * Return a new spec with the given {@code compiler}.
	 *
	 * @param compiler the initial compiler set to the returned spec.
	 * @return a new spec with the given {@code compiler}.
	 * @throws NullPointerException if the given {@code compiler} is null.
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static Spec compiler(@NotNull Compiler compiler) {
		Objects.requireNonNull(compiler, "compiler");
		return Specs.compiler(
				"CompilerSpec" + Integer.toHexString(compiler.hashCode()),
				compiler
		);
	}

	/**
	 * Return a new spec with the given {@code compiler}.
	 *
	 * @param name     the initial qualified name of the returned spec.
	 * @param compiler the initial compiler set to the returned spec.
	 * @return a new spec with the given {@code compiler}.
	 * @throws NullPointerException if the given {@code name} or {@code compiler} is
	 *                              null.
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	@Contract(value = "_,_->new", pure = true)
	public static Spec compiler(@NotNull String name, @NotNull Compiler compiler) {
		Objects.requireNonNull(name, "name");
		Objects.requireNonNull(compiler, "compiler");
		return new EditSpec(name)
				.setCompiler(compiler);
	}

	/**
	 * Return a new spec with the initializer of the given {@code spec}.
	 *
	 * @param spec the spec to get the initial initializer of the returned spec.
	 * @return a new spec with the initializer of the given {@code spec}.
	 * @throws NullPointerException if the given {@code spec} is null.
	 * @since 0.3.0 ~2021.07.06
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static Spec initializer(@NotNull Spec spec) {
		Objects.requireNonNull(spec, "spec");
		return Specs.initializer(
				"InitializerSpec" + Integer.toHexString(spec.hashCode()),
				spec.getInitializer()
		);
	}

	/**
	 * Return a new spec with the given {@code initializer}.
	 *
	 * @param initializer the initial initializer set to the returned spec.
	 * @return a new spec with the given {@code initializer}.
	 * @throws NullPointerException if the given {@code initializer} is null.
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static Spec initializer(@NotNull Initializer initializer) {
		Objects.requireNonNull(initializer, "initializer");
		return Specs.initializer(
				"InitializerSpec" + Integer.toHexString(initializer.hashCode()),
				initializer
		);
	}

	/**
	 * Return a new spec with the given {@code initializer}.
	 *
	 * @param name        the initial qualified name of the returned spec.
	 * @param initializer the initial initializer set to the returned spec.
	 * @return a new spec with the given {@code initializer}.
	 * @throws NullPointerException if the given {@code name} or {@code initializer} is
	 *                              null.
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	@Contract(value = "_,_->new", pure = true)
	public static Spec initializer(@NotNull String name, @NotNull Initializer initializer) {
		Objects.requireNonNull(name, "name");
		Objects.requireNonNull(initializer, "initializer");
		return new EditSpec(name)
				.setInitializer(initializer);
	}

	/**
	 * Return a new spec with the listener of the given {@code spec}.
	 *
	 * @param spec the spec to get the initial listener of the returned spec.
	 * @return a new spec with the listener of the given {@code spec}.
	 * @throws NullPointerException if the given {@code spec} is null.
	 * @since 0.3.0 ~2021.07.06
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static Spec listener(@NotNull Spec spec) {
		Objects.requireNonNull(spec, "spec");
		return Specs.listener(
				"ListenerSpec" + Integer.toHexString(spec.hashCode()),
				spec.getListener()
		);
	}

	/**
	 * Return a new spec with the given {@code listener}.
	 *
	 * @param listener the initial listener set to the returned spec.
	 * @return a new spec with the given {@code listener}.
	 * @throws NullPointerException if the given {@code listener} is null.
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static Spec listener(@NotNull Listener listener) {
		Objects.requireNonNull(listener, "listener");
		return Specs.listener(
				"ListenerSpec" + Integer.toHexString(listener.hashCode()),
				listener
		);
	}

	/**
	 * Return a new spec with the given {@code listener}.
	 *
	 * @param name     the initial qualified name of the returned spec.
	 * @param listener the initial listener set to the returned spec.
	 * @return a new spec with the given {@code listener}.
	 * @throws NullPointerException if the given {@code name} or {@code listener} is
	 *                              null.
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	@Contract(value = "_,_->new", pure = true)
	public static Spec listener(@NotNull String name, @NotNull Listener listener) {
		Objects.requireNonNull(name, "name");
		Objects.requireNonNull(listener, "listener");
		return new EditSpec(name)
				.setListener(listener);
	}

	/**
	 * Return a new spec with the parser of the given {@code spec}.
	 *
	 * @param spec the spec to get the initial parser of the returned spec.
	 * @return a new spec with the parser of the given {@code spec}.
	 * @throws NullPointerException if the given {@code spec} is null.
	 * @since 0.3.0 ~2021.07.06
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static Spec parser(@NotNull Spec spec) {
		Objects.requireNonNull(spec, "spec");
		return Specs.parser(
				"ParserSpec" + Integer.toHexString(spec.hashCode()),
				spec.getParser()
		).setAnalyzeProcessor(
				spec.getParseProcessor()
		);
	}

	/**
	 * Return a new spec with the given {@code parser}.
	 *
	 * @param parser the initial parser set to the returned spec.
	 * @return a new spec with the given {@code parser}.
	 * @throws NullPointerException if the given {@code parser} is null.
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static Spec parser(@NotNull Parser parser) {
		Objects.requireNonNull(parser, "parser");
		return Specs.parser(
				"ParserSpec" + Integer.toHexString(parser.hashCode()),
				parser
		);
	}

	/**
	 * Return a new spec with the given {@code parser}.
	 *
	 * @param name   the initial qualified name of the returned spec.
	 * @param parser the initial parser set to the returned spec.
	 * @return a new spec with the given {@code parser}.
	 * @throws NullPointerException if the given {@code name} or {@code parser} is null.
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	@Contract(value = "_,_->new", pure = true)
	public static Spec parser(@NotNull String name, @NotNull Parser parser) {
		Objects.requireNonNull(name, "name");
		Objects.requireNonNull(parser, "parser");
		return new EditSpec(name)
				.setParser(parser);
	}
}
