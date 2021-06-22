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
package org.jamplate.impl.initializer;

import org.jamplate.model.Compilation;
import org.jamplate.model.Document;
import org.jamplate.model.Environment;
import org.jamplate.function.Initializer;
import org.jamplate.function.Processor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * An initializer that initializes new compilations using a list of processors.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.29
 */
@Deprecated
public class ProcessorsInitializer implements Initializer {
	/**
	 * The processors used by this initializer.
	 *
	 * @since 0.2.0 ~2021.05.29
	 */
	@NotNull
	protected final List<Processor> processors;

	/**
	 * Construct a new initializer that initializes using the given {@code processors}.
	 * <br>
	 * Null processors in the array will be ignored.
	 *
	 * @param processors the processors to be used by the constructed initializer.
	 * @throws NullPointerException if the given {@code processors} is null.
	 * @since 0.2.0 ~2021.05.29
	 */
	public ProcessorsInitializer(@Nullable Processor @NotNull ... processors) {
		Objects.requireNonNull(processors, "processors");
		this.processors = Arrays
				.stream(processors)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}

	/**
	 * Construct a new initializer that initializes using the given {@code processors}.
	 * <br>
	 * Null processors in the list will be ignored.
	 *
	 * @param processors the processors to be used by the constructed initializer.
	 * @throws NullPointerException if the given {@code processors} is null.
	 * @since 0.2.0 ~2021.05.29
	 */
	public ProcessorsInitializer(@NotNull List<Processor> processors) {
		Objects.requireNonNull(processors, "processors");
		this.processors = new ArrayList<>();
		for (Processor processor : processors)
			if (processor != null)
				this.processors.add(processor);
	}

	@Override
	public boolean initialize(@NotNull Environment environment, @Nullable Document @NotNull ... documents) {
		Objects.requireNonNull(environment, "environment");
		Objects.requireNonNull(documents, "documents");
		List<Compilation> compilations = new ArrayList<>();

		for (Document document : documents)
			if (document != null)
				if (environment.getCompilation(document) == null)
					compilations.add(environment.optCompilation(document));

		for (Processor processor : this.processors)
			for (Compilation compilation : compilations)
				while (processor.process(compilation))
					;

		return !compilations.isEmpty();
	}
}
