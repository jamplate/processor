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
package org.jamplate.glucose.value;

import org.jamplate.memory.Memory;
import org.jamplate.memory.Pipe;
import org.jamplate.memory.Value;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * A value that evaluates to a number and can be evaluated to a raw number.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.29
 */
public final class VNumber implements Value<Number> {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 2949570788002453838L;

	/**
	 * The evaluating function that evaluates to the raw value of this value.
	 *
	 * @since 0.3.0 ~2021.06.30
	 */
	@NotNull
	private final Pipe<Object, Number> pipe;

	/**
	 * An internal constructor to construct a new number value with the given {@code
	 * factory}.
	 *
	 * @param pipe the factory that evaluates to the number of the constructed number
	 *             value.
	 * @throws NullPointerException if the given {@code factory} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	public VNumber(@NotNull Pipe<Object, Number> pipe) {
		Objects.requireNonNull(pipe, "pipe");
		this.pipe = pipe;
	}

	@NotNull
	@Override
	public VNumber apply(@NotNull Pipe<Number, Number> pipe) {
		Objects.requireNonNull(pipe, "pipe");
		return new VNumber(this.pipe.apply(pipe));
	}

	@NotNull
	@Override
	public String eval(@NotNull Memory memory) {
		Objects.requireNonNull(memory, "memory");
		double number = this.pipe.eval(memory).doubleValue();
		//noinspection DynamicRegexReplaceableByCompiledPattern
		return number % 1 == 0 ?
			   String.format("%d", (long) number) :
			   String.format("%f", number)
					 .replaceAll("0*$", "");
	}

	@NotNull
	@Override
	public Pipe<Object, Number> getPipe() {
		return this.pipe;
	}

	@NotNull
	@Override
	public String toString() {
		return "Number:" + Integer.toHexString(this.hashCode());
	}
}
