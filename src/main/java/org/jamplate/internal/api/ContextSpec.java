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

import org.jamplate.api.Spec;
import org.jamplate.diagnostic.Message;
import org.jamplate.function.Analyzer;
import org.jamplate.function.Compiler;
import org.jamplate.function.Parser;
import org.jamplate.function.Processor;
import org.jamplate.internal.util.analyzer.group.SequentialAnalyzer;
import org.jamplate.internal.util.compiler.group.FirstCompileCompiler;
import org.jamplate.internal.util.parser.group.CombineParser;
import org.jamplate.internal.util.processor.group.SequentialProcessor;
import org.jamplate.model.Compilation;
import org.jamplate.model.Environment;
import org.jamplate.model.Memory;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A subspec supporting implementation of the interface {@link Spec}.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.19
 */
public class ContextSpec implements Spec {
	/**
	 * The subspecs in this spec.
	 *
	 * @since 0.3.0 ~2021.06.19
	 */
	@NotNull
	protected final LinkedList<Spec> specs = new LinkedList<>();

	/**
	 * The qualified name of this spec.
	 *
	 * @since 0.3.0 ~2021.06.19
	 */
	@NotNull
	protected String qualifiedName;

	/**
	 * Construct a new subspec supporting spec.
	 *
	 * @since 0.3.0 ~2021.06.19
	 */
	public ContextSpec() {
		this.qualifiedName = Spec.super.getQualifiedName();
	}

	/**
	 * Construct a new subspec supporting spec.
	 *
	 * @param qualifiedName the qualified name of the constructed spec.
	 * @throws NullPointerException if the given {@code qualifiedName} is null.
	 * @since 0.3.0 ~2021.06.19
	 */
	public ContextSpec(@NotNull String qualifiedName) {
		Objects.requireNonNull(qualifiedName, "qualifiedName");
		this.qualifiedName = qualifiedName;
	}

	@Override
	public boolean addSpecAfter(@NotNull Spec ref, @NotNull Spec spec) {
		Objects.requireNonNull(ref, "ref");
		Objects.requireNonNull(spec, "spec");
		if (this.specs.contains(spec))
			return false;

		ListIterator<Spec> iterator = this.specs.listIterator();

		while (iterator.hasNext()) {
			Spec next = iterator.next();

			if (next == ref) {
				iterator.add(spec);
				return true;
			}
		}

		throw new IllegalArgumentException("Spec not found: " + ref);
	}

	@Override
	public boolean addSpecBefore(@NotNull Spec ref, @NotNull Spec spec) {
		Objects.requireNonNull(ref, "ref");
		Objects.requireNonNull(spec, "spec");
		if (this.specs.contains(spec))
			return false;

		ListIterator<Spec> iterator = this.specs.listIterator(this.specs.size());

		while (iterator.hasPrevious()) {
			Spec previous = iterator.previous();

			if (previous == ref) {
				iterator.set(spec);
				iterator.add(previous);
				return true;
			}
		}

		throw new IllegalArgumentException("Spec not found: " + ref);
	}

	@Override
	public boolean addSpecFirst(@NotNull Spec spec) {
		Objects.requireNonNull(spec, "spec");
		if (this.specs.contains(spec))
			return false;

		this.specs.addFirst(spec);
		return true;
	}

	@Override
	public boolean addSpecLast(@NotNull Spec spec) {
		Objects.requireNonNull(spec, "spec");
		if (this.specs.contains(spec))
			return false;

		this.specs.addLast(spec);
		return true;
	}

	@NotNull
	@Override
	public Processor getAnalyzeProcessor() {
		return new SequentialProcessor(
				this.specs
						.stream()
						.map(Spec::getAnalyzeProcessor)
						.collect(Collectors.toList())
		);
	}

	@NotNull
	@Override
	public Analyzer getAnalyzer() {
		return new SequentialAnalyzer(
				this.specs
						.stream()
						.map(Spec::getAnalyzer)
						.collect(Collectors.toList())
		);
	}

	@NotNull
	@Override
	public Processor getCompileProcessor() {
		return new SequentialProcessor(
				this.specs
						.stream()
						.map(Spec::getCompileProcessor)
						.collect(Collectors.toList())
		);
	}

	@NotNull
	@Override
	public Compiler getCompiler() {
		return new FirstCompileCompiler(
				this.specs
						.stream()
						.map(Spec::getCompiler)
						.collect(Collectors.toList())
		);
	}

	@NotNull
	@Override
	public Processor getParseProcessor() {
		return new SequentialProcessor(
				this.specs
						.stream()
						.map(Spec::getParseProcessor)
						.collect(Collectors.toList())
		);
	}

	@NotNull
	@Override
	public Parser getParser() {
		return new CombineParser(
				this.specs
						.stream()
						.map(Spec::getParser)
						.collect(Collectors.toList())
		);
	}

	@NotNull
	@Override
	public String getQualifiedName() {
		return this.qualifiedName;
	}

	@NotNull
	@Override
	public Iterator<Spec> iterator() {
		return this.specs.iterator();
	}

	@Override
	public void onCreateCompilation(@NotNull Compilation compilation) {
		for (Spec spec : this)
			spec.onCreateCompilation(compilation);
	}

	@Override
	public void onCreateMemory(@NotNull Compilation compilation, @NotNull Memory memory) {
		for (Spec spec : this)
			spec.onCreateMemory(compilation, memory);
	}

	@Override
	public void onDestroyMemory(@NotNull Compilation compilation, @NotNull Memory memory) {
		for (Spec spec : this)
			spec.onDestroyMemory(compilation, memory);
	}

	@Override
	public void onDiagnostic(@NotNull Environment environment, @NotNull Message message) {
		for (Spec spec : this)
			spec.onDiagnostic(environment, message);
	}

	@Override
	public void setQualifiedName(@NotNull String name) {
		Objects.requireNonNull(name, "name");
		this.qualifiedName = name;
	}
}
