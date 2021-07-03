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
package org.jamplate.value;

import org.jamplate.model.Memory;
import org.jamplate.model.Pipe;
import org.jamplate.model.Value;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * A value wrapper implementation.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.30
 */
public final class TextValue implements Value<String> {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -8001672867393082818L;

	/**
	 * The function that evaluates the the raw text of this value.
	 *
	 * @since 0.3.0 ~2021.06.30
	 */
	@NotNull
	private final Pipe<String> pipe;

	/**
	 * Construct a new text value that evaluates to the given {@code text}.
	 *
	 * @param text the value of the constructed text value.
	 * @throws NullPointerException if the given {@code text} is null.
	 * @since 0.3.0 ~2021.06.30
	 */
	public TextValue(@NotNull String text) {
		Objects.requireNonNull(text, "text");
		this.pipe = (m, v) -> text;
	}

	/**
	 * Construct a new text value that evaluates to the given {@code text}.
	 *
	 * @param text the value of the constructed text value.
	 * @throws NullPointerException if the given {@code text} is null.
	 * @since 0.3.0 ~2021.06.30
	 */
	public TextValue(@NotNull Object text) {
		Objects.requireNonNull(text, "text");
		String string = Tokenizer.toString(text);
		this.pipe = (m, v) -> string;
	}

	/**
	 * Construct a new text value that evaluate to the evaluation of the given {@code
	 * value}.
	 *
	 * @param value the value of the constructed value.
	 * @throws NullPointerException if the given {@code value} is null.
	 * @since 0.3.0 ~2021.06.30
	 */
	public TextValue(@NotNull Value<?> value) {
		Objects.requireNonNull(value, "value");
		this.pipe = (m, v) -> value.evaluate(m);
	}

	/**
	 * An internal constructor to construct a new text value with the given {@code
	 * function}.
	 *
	 * @param pipe the function that evaluates to text of the constructed text value.
	 * @throws NullPointerException if the given {@code function} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	private TextValue(@NotNull Pipe<String> pipe) {
		Objects.requireNonNull(pipe, "pipe");
		this.pipe = pipe;
	}

	/**
	 * Cast the given {@code object} into a text value.
	 *
	 * @param object the object to be cast.
	 * @return a new text value evaluating to the value of the given {@code object}.
	 * @throws NullPointerException if the given {@code object} is null.
	 * @since 0.3.0 ~2021.06.30
	 */
	@NotNull
	@Contract(pure = true)
	public static TextValue cast(@Nullable Object object) {
		//it
		if (object instanceof TextValue)
			return (TextValue) object;
		//wrap
		if (object instanceof Value)
			return new TextValue((Value) object);

		//stringify
		return new TextValue(Tokenizer.toString(object));
	}

	@NotNull
	@Override
	public TextValue apply(@NotNull Pipe<String> pipe) {
		Objects.requireNonNull(pipe, "pipe");
		return new TextValue(
				this.pipe.apply(pipe)
		);
	}

	@NotNull
	@Override
	public String evaluate(@NotNull Memory memory) {
		Objects.requireNonNull(memory, "memory");
		return this.pipe.eval(memory);
	}

	@NotNull
	@Override
	public Pipe<String> getPipe() {
		return this.pipe;
	}

	@NotNull
	@Override
	public String toString() {
		return "Text:" + Integer.toHexString(this.hashCode());
	}
}
