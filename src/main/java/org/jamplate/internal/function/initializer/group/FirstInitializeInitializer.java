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
package org.jamplate.internal.function.initializer.group;

import org.jamplate.function.Initializer;
import org.jamplate.model.Compilation;
import org.jamplate.model.Document;
import org.jamplate.model.Environment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * An initializer that tris initializing using multiple other initializers in order and
 * returns the results of the first initializer to succeed.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.07.02
 */
public class FirstInitializeInitializer implements Initializer {
	/**
	 * The initializers backing this initializer. (ordered)
	 *
	 * @since 0.3.0 ~2021.07.02
	 */
	@NotNull
	protected final List<Initializer> initializers;

	/**
	 * Construct a new order-initializer that uses the given {@code initializers}.
	 * <br>
	 * Null initializers in the given array will be ignored.
	 *
	 * @param initializers the initializers (in-order) to be used by the constructed
	 *                     initializer.
	 * @throws NullPointerException if the given {@code initializers} is null.
	 * @since 0.3.0 ~2021.07.02
	 */
	public FirstInitializeInitializer(@Nullable Initializer @NotNull ... initializers) {
		Objects.requireNonNull(initializers, "initializers");
		this.initializers = Arrays
				.stream(initializers)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}

	/**
	 * Construct a new order-initializer that uses the given {@code initializers}.
	 * <br>
	 * Null initializers in the given list will be ignored.
	 *
	 * @param initializers the initializers (in-order) to be used by the constructed
	 *                     initializer.
	 * @throws NullPointerException if the given {@code initializers} is null.
	 * @since 0.3.0 ~2021.07.02
	 */
	public FirstInitializeInitializer(@NotNull List<Initializer> initializers) {
		Objects.requireNonNull(initializers, "initializers");
		this.initializers = new ArrayList<>();
		for (Initializer initializer : initializers)
			if (initializer != null)
				this.initializers.add(initializer);
	}

	@Nullable
	@Override
	public Compilation initialize(@NotNull Environment environment, @NotNull Document document) {
		Objects.requireNonNull(environment, "environment");
		Objects.requireNonNull(document, "document");
		for (Initializer initializer : this.initializers) {
			Compilation compilation = initializer.initialize(environment, document);

			if (compilation != null)
				return compilation;
		}

		return null;
	}
}
