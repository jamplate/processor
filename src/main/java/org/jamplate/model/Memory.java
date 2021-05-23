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

import java.util.*;
import java.util.function.UnaryOperator;

/**
 * The memory is a structure that saves the variables at the runtime.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.21
 */
public final class Memory {
	/**
	 * A buffer containing the text considered printed to the console.
	 *
	 * @since 0.2.0 ~2021.05.21
	 */
	@SuppressWarnings("StringBufferField")
	@NotNull
	private final StringBuilder console = new StringBuilder();
	/**
	 * A map backing the heap part of this memory.
	 *
	 * @since 0.2.0 ~2021.05.21
	 */
	@NotNull
	private final Map<String, Value> heap = new HashMap<>();
	/**
	 * A queue backing the stack part of this memory.
	 *
	 * @since 0.2.0 ~2021.05.21
	 */
	@NotNull
	private final Queue<Queue<Value>> stack = new LinkedList<>(Collections.singleton(new LinkedList<>()));

	/**
	 * Construct a new empty memory.
	 *
	 * @since 0.2.0 ~2021.05.21
	 */
	public Memory() {
	}

	/**
	 * Construct a copy of the given {@code memory}.
	 *
	 * @param memory the memory to be copied.
	 * @throws NullPointerException if the given {@code memory} is null.
	 * @since 0.2.0 ~2021.05.21
	 */
	public Memory(@NotNull Memory memory) {
		Objects.requireNonNull(memory, "memory");
		this.console.append(memory.console);
		this.heap.putAll(memory.heap);
		memory.stack
				.stream()
				.map(LinkedList::new)
				.forEach(this.stack::add);
	}

	/**
	 * Replace the value at the given {@code address} with the result of invoking the
	 * given {@code operator} with the current value.
	 * <br>
	 * If the given {@code operator} thrown any exception when invoked by this method. The
	 * thrown exception will fall throw this method with nothing changed.
	 *
	 * @param address  the address to replace the value at.
	 * @param operator the function to be invoked to acquire the replacement value.
	 * @throws NullPointerException if the given {@code address} or {@code operator} is
	 *                              null.
	 * @since 0.2.0 ~2021.05.21
	 */
	@Contract(mutates = "this")
	public void compute(@NotNull String address, @NotNull UnaryOperator<Value> operator) {
		Objects.requireNonNull(address, "address");
		Objects.requireNonNull(operator, "operator");
		this.heap.compute(address, (k, v) -> operator.apply(v));
	}

	/**
	 * Return the value allocated at the given {@code address} in this memory.
	 *
	 * @param address the address to be read.
	 * @return the value at the given {@code address}. Or {@link Value#NULL} if no value
	 * 		was allocated there.
	 * @throws NullPointerException if the given {@code address} is null.
	 * @since 0.2.0 ~2021.05.21
	 */
	@NotNull
	@Contract(pure = true)
	public Value get(@NotNull String address) {
		Objects.requireNonNull(address, "address");
		return this.heap.getOrDefault(address, Value.NULL);
	}

	/**
	 * Return and remove the last pushed value to the stack.
	 *
	 * @return the last value pushed to the stack. Or {@link Value#NULL} if the stack was
	 * 		empty.
	 * @since 0.2.0 ~2021.05.21
	 */
	@NotNull
	@Contract(mutates = "this")
	public Value pop() {
		Value value = this.stack.peek().poll();
		return value == null ? Value.NULL : value;
	}

	/**
	 * Pop the current stack frame. Popping the stack frame will result to this memory
	 * representing the frame previous to the last pushed frame. If the base frame to be
	 * popped, then the base frame will be cleared instead of popped.
	 *
	 * @since 0.2.0 ~2021.05.21
	 */
	@Contract(mutates = "this")
	public void popFrame() {
		if (this.stack.size() == 1)
			this.stack.peek().clear();
		else
			this.stack.poll();
	}

	/**
	 * Print the given {@code text} to the console.
	 * <br>
	 * Note: the console is the buffer containing the output of the execution of the
	 * instruction.
	 *
	 * @param text the text to be printed.
	 * @throws NullPointerException if the given {@code text} is null.
	 * @since 0.2.0 ~2021.05.21
	 */
	@Contract(mutates = "this")
	public void print(@NotNull CharSequence text) {
		Objects.requireNonNull(text, "text");
		this.console.append(text);
	}

	/**
	 * Read the contents printed to the console.
	 *
	 * @return the contents printed to the console.
	 * @since 0.2.0 ~2021.05.21
	 */
	@NotNull
	@Contract(pure = true)
	public String read() {
		return this.console.toString();
	}

	/**
	 * Push the given {@code value} to the stack part of this memory.
	 *
	 * @param value the value to be pushed.
	 * @throws NullPointerException if the given {@code value} is null.
	 * @since 0.2.0 ~2021.05.21
	 */
	@Contract(mutates = "this")
	public void push(@NotNull Value value) {
		Objects.requireNonNull(value, "value");
		this.stack.peek().add(value);
	}

	/**
	 * Push a new stack frame.
	 *
	 * @since 0.2.0 ~2021.05.21
	 */
	@Contract(mutates = "this")
	public void pushFrame() {
		this.stack.add(new LinkedList<>());
	}

	/**
	 * Set the value at the given {@code address} to be the given {@code value}.
	 *
	 * @param address the address to set the value to.
	 * @param value   the value to be set at the given {@code address}.
	 * @throws NullPointerException if the given {@code address} or {@code value} is
	 *                              null.
	 * @since 0.2.0 ~2021.05.21
	 */
	@Contract(mutates = "this")
	public void set(@NotNull String address, @NotNull Value value) {
		Objects.requireNonNull(address, "address");
		Objects.requireNonNull(value, "value");
		this.heap.put(address, value);
	}
}
