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

import org.jamplate.model.Compilation;
import org.jamplate.model.Document;
import org.jamplate.model.Environment;
import org.jamplate.model.function.Processor;
import org.jamplate.util.model.EnvironmentImpl;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * An all-in-one jamplate processor.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.23
 */
public class Jamplate {
	/**
	 * The default jamplate implementation.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	@NotNull
	public static final Jamplate DEFAULT =
			new Jamplate(
					Parsers.PROCESSOR,
					Processors.PROCESSOR,
					Compilers.PROCESSOR
			);

	/**
	 * The processors used by this jamplate implementation.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	@NotNull
	protected final List<Processor> processors;

	/**
	 * Construct a new jamplate implementation that uses the given {@code processors}.
	 * <br>
	 * Null processors in the array will be ignored.
	 *
	 * @param processors the processors used by the constructed jamplate implementation.
	 *                   (in order)
	 * @throws NullPointerException if the given {@code processors} is null.
	 * @since 0.2.0 ~2021.05.23
	 */
	public Jamplate(@Nullable Processor @NotNull ... processors) {
		Objects.requireNonNull(processors, "processors");
		this.processors = Arrays
				.stream(processors)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}

	/**
	 * Construct a new jamplate implementation that uses the given {@code processors}.
	 * <br>
	 * Null processors in the list will be ignored.
	 *
	 * @param processors the processors used by the constructed jamplate implementation.
	 *                   (in order)
	 * @throws NullPointerException if the given {@code processors} is null.
	 * @since 0.2.0 ~2021.05.23
	 */
	public Jamplate(@Nullable List<Processor> processors) {
		Objects.requireNonNull(processors, "processors");
		this.processors = new ArrayList<>();
		for (Processor processor : processors)
			if (processor != null)
				this.processors.add(processor);
	}

	/**
	 * Completely process the given {@code documents} in a new environment using the
	 * processors of this Jamplate implementation.
	 *
	 * @param documents the documents to be processed.
	 * @return the environment from processing the given {@code documents}.
	 * @throws NullPointerException if the given {@code documents} is null.
	 * @since 0.2.0 ~2021.05.23
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public Environment process(@Nullable Document @NotNull ... documents) {
		Objects.requireNonNull(documents, "documents");
		Environment environment = new EnvironmentImpl();

		for (Document document : documents)
			environment.optCompilation(document);

		for (Processor processor : this.processors)
			for (Compilation compilation : environment.compilationSet())
				while (processor.process(compilation))
					;

		return environment;
	}
}
