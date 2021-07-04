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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Iterator;

/**
 * A processor is a function that takes a sketch and a compilation and process it its way
 * (for example: parsing its value).
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.04.28
 */
@FunctionalInterface
public interface Processor extends Iterable<Processor> {
	/**
	 * A processor that does nothing.
	 *
	 * @since 0.3.0 ~2021.06.24
	 */
	@NotNull
	Processor IDLE = new Processor() {
		@Override
		public boolean process(@NotNull Compilation compilation) {
			return false;
		}

		@Override
		public String toString() {
			return "Processor.IDLE";
		}
	};

	/**
	 * Return an immutable iterator iterating over sub-processors of this processor.
	 *
	 * @return an iterator iterating over the sub-processors of this processor.
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	@Override
	default Iterator<Processor> iterator() {
		return Collections.emptyIterator();
	}

	/**
	 * Process the given {@code compilation}.
	 * <br>
	 * If this method returned {@code true}. The compilation process will have another
	 * round. If all the processors in a compilation return {@code false}, the compilation
	 * process ends.
	 *
	 * @param compilation the compilation the node is from.
	 * @return true, if this processor have done something. False, otherwise.
	 * @throws NullPointerException if the given {@code compilation} is null.
	 * @throws RuntimeException     if any runtime exception occurs.
	 * @throws Error                if any unexpected/non-recoverable exception occurs.
	 * @since 0.0.1 ~2021.04.28
	 */
	@Contract(mutates = "param")
	boolean process(@NotNull Compilation compilation);
}
