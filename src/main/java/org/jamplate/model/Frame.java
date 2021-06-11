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

import java.util.*;
import java.util.function.UnaryOperator;

/**
 * An isolated part in the memory.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.23
 */
public final class Frame {
	/**
	 * The heap part of this frame.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	@NotNull
	final Map<String, Value> heap;
	/**
	 * The stack part of this frame.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	@NotNull
	final Deque<Value> stack;

	/**
	 * The instruction of this frame.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	@Nullable
	private Instruction instruction;

	/**
	 * Construct a new frame.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	public Frame() {
		this.heap = new HashMap<>();
		this.stack = new LinkedList<>();
	}

	/**
	 * Construct a copy of the given {@code frame}.
	 *
	 * @param frame the frame to copy.
	 * @throws NullPointerException if the given {@code frame} is null.
	 * @since 0.2.0 ~2021.05.23
	 */
	public Frame(@NotNull Frame frame) {
		Objects.requireNonNull(frame, "frame");
		this.heap = new HashMap<>(frame.heap);
		this.stack = new LinkedList<>();
		this.instruction = frame.instruction;
	}

	/**
	 * Construct a new frame with the given {@code instruction}.
	 *
	 * @param instruction the instruction the constructed frame is for.
	 * @throws NullPointerException if the given {@code instruction} is null.
	 * @since 0.2.0 ~2021.05.23
	 */
	public Frame(@NotNull Instruction instruction) {
		Objects.requireNonNull(instruction, "instruction");
		this.heap = new HashMap<>();
		this.stack = new LinkedList<>();
		this.instruction = instruction;
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
	 * @return the computed value.
	 * @throws NullPointerException if the given {@code address} or {@code operator} is
	 *                              null.
	 * @since 0.2.0 ~2021.05.23
	 */
	@Contract(mutates = "this")
	public Value compute(@NotNull String address, @NotNull UnaryOperator<Value> operator) {
		Objects.requireNonNull(address, "address");
		Objects.requireNonNull(operator, "operator");
		return this.heap.compute(address, (k, v) -> operator.apply(v));
	}

	/**
	 * Return the value allocated at the given {@code address} in this memory.
	 *
	 * @param address the address to be read.
	 * @return the value at the given {@code address}. Or {@link Value#NULL} if no value
	 * 		was allocated there.
	 * @throws NullPointerException if the given {@code address} is null.
	 * @since 0.2.0 ~2021.05.23
	 */
	@NotNull
	@Contract(pure = true)
	public Value get(@NotNull String address) {
		Objects.requireNonNull(address, "address");
		return this.heap.getOrDefault(address, Value.NULL);
	}

	/**
	 * Return the instruction of this frame.
	 *
	 * @return the instruction of this frame.
	 * @since 0.2.0 ~2021.05.23
	 */
	@Nullable
	@Contract(pure = true)
	public Instruction getInstruction() {
		return this.instruction;
	}

	/**
	 * Return the last pushed value to the stack.
	 *
	 * @return the last value pushed to the stack. Or {@link Value#NULL} if the stack was
	 * 		empty.
	 * @since 0.3.0 ~2021.06.11
	 */
	@NotNull
	@Contract(pure = true)
	public Value peek() {
		Value value = this.stack.peekLast();
		return value == null ? Value.NULL : value;
	}

	/**
	 * Return and remove the last pushed value to the stack.
	 *
	 * @return the last value pushed to the stack. Or {@link Value#NULL} if the stack was
	 * 		empty.
	 * @since 0.2.0 ~2021.05.23
	 */
	@NotNull
	@Contract(mutates = "this")
	public Value pop() {
		Value value = this.stack.pollLast();
		return value == null ? Value.NULL : value;
	}

	/**
	 * Push the given {@code value} to the stack part of this memory.
	 *
	 * @param value the value to be pushed.
	 * @throws NullPointerException if the given {@code value} is null.
	 * @since 0.2.0 ~2021.05.23
	 */
	@Contract(mutates = "this")
	public void push(@NotNull Value value) {
		Objects.requireNonNull(value, "value");
		this.stack.addLast(value);
	}

	/**
	 * Set the value at the given {@code address} to be the given {@code value}.
	 *
	 * @param address the address to set the value to.
	 * @param value   the value to be set at the given {@code address}.
	 * @throws NullPointerException if the given {@code address} or {@code value} is
	 *                              null.
	 * @since 0.2.0 ~2021.05.23
	 */
	@Contract(mutates = "this")
	public void set(@NotNull String address, @NotNull Value value) {
		Objects.requireNonNull(address, "address");
		Objects.requireNonNull(value, "value");
		this.heap.put(address, value);
	}

	/**
	 * Set the instruction of this frame to be the given {@code instruction}.
	 *
	 * @param instruction the instruction of this frame.
	 * @since 0.2.0 ~2021.05.23
	 */
	@Contract(mutates = "this")
	public void setInstruction(@NotNull Instruction instruction) {
		Objects.requireNonNull(instruction, "instruction");
		this.instruction = instruction;
	}
}
