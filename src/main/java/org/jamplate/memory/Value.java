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
package org.jamplate.memory;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

/**
 * The value function is a function that evaluates to a value depending on the state of
 * the memory given to it.
 * <br><br>
 * <strong>Members</strong>
 * <ul>
 *     <li>pipe: {@link Pipe}</li>
 * </ul>
 *
 * @param <T> the type of the object that the pipe of the value will evaluate to.
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.21
 */
@FunctionalInterface
public interface Value<T> extends Serializable {
	/**
	 * The {@code null} value.
	 *
	 * @since 0.2.0 ~2021.05.21
	 */
	@NotNull
	Value<Object> NULL = new Value<Object>() {
		@SuppressWarnings("JavaDoc")
		private static final long serialVersionUID = -5499638541705572487L;

		@NotNull
		@Override
		public String eval(@NotNull Memory memory) {
			return "";
		}

		@NotNull
		@Override
		public Pipe getPipe() {
			return (m, v) -> null;
		}

		@Override
		public String toString() {
			return "Value.NULL";
		}
	};

	/**
	 * Return a value that evaluates its pipe to the result of invoking the given {@code
	 * pipe} with the result of evaluating the pipe of this.
	 *
	 * @param pipe the pipe to be applied to the returned value.
	 * @return a value as described above.
	 * @throws NullPointerException if the given {@code pipe} is null.
	 * @implSpec the default implementation returns a new value with its pipe is the
	 * 		result of applying the given {@code pipe} to the pipe of the value.
	 * @since 0.3.0 ~2021.07.01
	 */
	@NotNull
	@Contract(pure = true)
	default Value<T> apply(@NotNull Pipe<T, T> pipe) {
		Objects.requireNonNull(pipe, "pipe");
		return this.getPipe().apply(pipe).toValue();
	}

	/**
	 * Return the pipe of this value.
	 *
	 * @return the pipe of this value.
	 * @implSpec the default implementation will return a pipe that delegates to the
	 *        {@link #eval(Memory)} method of this value unsafely casted to {@link T}.
	 * @since 0.3.0 ~2021.07.03
	 */
	@NotNull
	@Contract(pure = true)
	default Pipe<Object, T> getPipe() {
		return (m, v) -> (T) this.eval(m);
	}

	/**
	 * Evaluate this value with the given {@code memory}.
	 *
	 * @param memory the memory to evaluate this value with.
	 * @return the evaluated value.
	 * @throws NullPointerException if the given {@code memory} is null.
	 * @since 0.2.0 ~2021.05.21
	 */
	@NotNull
	String eval(@NotNull Memory memory);
}
