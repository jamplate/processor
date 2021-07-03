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
package org.jamplate.model;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Objects;

/**
 * A function that takes the value {@code T} and return a modified version of it.
 *
 * @param <T> the type of the value the accumulator accepts.
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.07.03
 */
@FunctionalInterface
public interface Pipe<T> extends Serializable {
	/**
	 * Return a pipe that evaluates to the value of evaluating the given {@code pipe} with
	 * the value of evaluating this pipe as the parameter.
	 *
	 * @param pipe the wrapper pipe.
	 * @return a pipe that evaluates the given {@code pipe} with the value of this pipe as
	 * 		the parameter.
	 * @throws NullPointerException if the given {@code pipe} is null.
	 * @since 0.3.0 ~2021.07.03
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	default Pipe<T> apply(@NotNull Pipe<T> pipe) {
		Objects.requireNonNull(pipe, "pipe");
		return (m, v) -> pipe.eval(m, this.eval(m, v));
	}

	/**
	 * Evaluate this pipe without a chain value.
	 *
	 * @param memory the memory to evaluate with.
	 * @return the value.
	 * @throws NullPointerException if the given {@code memory} is null.
	 * @since 0.3.0 ~2021.07.03
	 */
	@Nullable
	@Contract(pure = true)
	default T eval(@NotNull Memory memory) {
		return this.eval(memory, null);
	}

	/**
	 * Return a new value that evaluates to the string value of evaluating this pipe. The
	 * pipe of the returned value will be this.
	 *
	 * @return a value from this pipe.
	 * @since 0.3.0 ~2021.07.03
	 */
	@NotNull
	@Contract(pure = true)
	default Value<T> toValue() {
		return new Value<T>() {
			@SuppressWarnings("JavaDoc")
			private static final long serialVersionUID = 8887217069908351493L;

			@NotNull
			@Override
			public String evaluate(@NotNull Memory memory) {
				return String.valueOf(Pipe.this.eval(memory));
			}

			@NotNull
			@Override
			public Pipe<T> getPipe() {
				return Pipe.this;
			}
		};
	}

	/**
	 * Evaluate a modified version of the given {@code value}.
	 *
	 * @param memory the memory to evaluate with.
	 * @param value  the value to returned a modified version of.
	 * @return a modified version of the given {@code value}.
	 * @throws NullPointerException if the given {@code memory} is null; if the given
	 *                              {@code value} is null and this pipe is not a head
	 *                              pipe.
	 * @since 0.3.0 ~2021.07.03
	 */
	@Nullable
	@Contract(pure = true)
	T eval(@NotNull Memory memory, @Nullable T value);
}
