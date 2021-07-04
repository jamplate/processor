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
import org.jamplate.impl.analyzer.SequentialAnalyzer;
import org.jamplate.impl.compiler.FirstCompileCompiler;
import org.jamplate.impl.initializer.FirstInitializeInitializer;
import org.jamplate.impl.listener.SequentialListener;
import org.jamplate.impl.parser.CombineParser;
import org.jamplate.impl.processor.SequentialProcessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
public class MultiSpec implements Spec {
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
	 * @param subspecs initial subspecs.
	 * @throws NullPointerException if the given {@code subspecs} is null.
	 * @since 0.3.0 ~2021.06.19
	 */
	public MultiSpec(@Nullable Spec @NotNull ... subspecs) {
		Objects.requireNonNull(subspecs, "subspecs");
		this.qualifiedName = "MultiSpec" + this.hashCode();
		for (Spec spec : subspecs)
			if (spec != null)
				this.specs.add(spec);
	}

	/**
	 * Construct a new subspec supporting spec.
	 *
	 * @param subspecs      initial subspecs.
	 * @param qualifiedName the qualified name of the constructed spec.
	 * @throws NullPointerException if the given {@code qualifiedName} or {@code subspecs}
	 *                              is null.
	 * @since 0.3.0 ~2021.06.19
	 */
	public MultiSpec(@NotNull String qualifiedName, @Nullable Spec @NotNull ... subspecs) {
		Objects.requireNonNull(qualifiedName, "qualifiedName");
		Objects.requireNonNull(subspecs, "subspecs");
		this.qualifiedName = qualifiedName;
		for (Spec spec : subspecs)
			if (spec != null)
				this.specs.add(spec);
	}

	@Override
	public Spec addAfter(@NotNull Spec ref, @NotNull Spec spec) {
		Objects.requireNonNull(ref, "ref");
		Objects.requireNonNull(spec, "spec");
		ListIterator<Spec> iterator = this.specs.listIterator();

		while (iterator.hasNext()) {
			Spec next = iterator.next();

			if (next == ref) {
				iterator.add(spec);
				return this;
			}
		}

		throw new IllegalArgumentException("Spec not found: " + ref);
	}

	@Override
	public Spec addBefore(@NotNull Spec ref, @NotNull Spec spec) {
		Objects.requireNonNull(ref, "ref");
		Objects.requireNonNull(spec, "spec");
		ListIterator<Spec> iterator = this.specs.listIterator(this.specs.size());

		while (iterator.hasPrevious()) {
			Spec previous = iterator.previous();

			if (previous == ref) {
				iterator.set(spec);
				iterator.add(previous);
				return this;
			}
		}

		throw new IllegalArgumentException("Spec not found: " + ref);
	}

	@Override
	public Spec addFirst(@NotNull Spec spec) {
		Objects.requireNonNull(spec, "spec");
		this.specs.addFirst(spec);
		return this;
	}

	@Override
	public Spec addLast(@NotNull Spec spec) {
		Objects.requireNonNull(spec, "spec");
		this.specs.addLast(spec);
		return this;
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
	public Initializer getInitializer() {
		return new FirstInitializeInitializer(
				this.specs
						.stream()
						.map(Spec::getInitializer)
						.collect(Collectors.toList())
		);
	}

	@NotNull
	@Override
	public Listener getListener() {
		return new SequentialListener(
				this.specs.stream()
						  .map(Spec::getListener)
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
	public Spec setQualifiedName(@NotNull String name) {
		Objects.requireNonNull(name, "name");
		this.qualifiedName = name;
		return this;
	}
}
