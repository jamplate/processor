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
package org.jamplate.function;

import org.jamplate.model.Compilation;
import org.jamplate.model.Document;
import org.jamplate.model.Environment;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Iterator;

/**
 * The initializer is a function that constructs a compilation for the document provided
 * to it.
 * <br><br>
 * <strong>Members</strong>
 * <ul>
 *     <li>{@link Initializer}[]</li>
 * </ul>
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.2.0 ~2021.05.29
 */
@FunctionalInterface
public interface Initializer extends Iterable<Initializer> {
	/**
	 * An initializer that initializes nothing.
	 *
	 * @since 0.3.0 ~2021.07.02
	 */
	@NotNull
	Initializer IDLE = new Initializer() {
		@Nullable
		@Override
		public Compilation initialize(@NotNull Environment environment, @NotNull Document document) {
			return null;
		}

		@Override
		public String toString() {
			return "Initializer.IDLE";
		}
	};

	/**
	 * Return an immutable iterator iterating over sub-initializers of this initializer.
	 *
	 * @return an iterator iterating over the sub-initializers of this initializer.
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	@Override
	default Iterator<Initializer> iterator() {
		return Collections.emptyIterator();
	}

	/**
	 * Initialize a compilation for the given {@code documents} in the given {@code
	 * environment}.
	 *
	 * @param environment the environment to create a compilation for.
	 * @param document    the document for the initialized compilation.
	 * @return a compilation for the given {@code document}. Or {@code null} if failed to
	 * 		initialize.
	 * @throws NullPointerException if the given {@code environment} or {@code document}
	 *                              is null.
	 * @since 0.3.0 ~2021.07.02
	 */
	@Nullable
	@Contract(pure = true)
	Compilation initialize(@NotNull Environment environment, @NotNull Document document);
}
