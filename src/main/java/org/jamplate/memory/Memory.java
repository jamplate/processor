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

import org.jamplate.impl.memory.BufferedConsole;
import org.jamplate.model.Instruction;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOError;
import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

/**
 * The memory is a structure that saves the variables at the runtime.
 * <br><br>
 * <strong>Members</strong>
 * <ul>
 *     <li>{@link Frame}[]</li>
 *     <li>frame: {@link Frame}</li>
 *     <li>console: {@link Console}</li>
 * </ul>
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.21
 */
public final class Memory implements Iterable<Frame> {
	/**
	 * The frames in this memory. Always not empty.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	@NotNull
	private final Deque<Frame> frames;

	/**
	 * The current set console.
	 *
	 * @since 0.2.0 ~2021.05.21
	 */
	@NotNull
	private Console console;

	/**
	 * Construct a new empty memory.
	 *
	 * @since 0.2.0 ~2021.05.21
	 */
	public Memory() {
		this.frames = new LinkedList<>(Collections.singleton(new Frame()));
		this.console = new BufferedConsole();
	}

	/**
	 * Construct a new copy of the given {@code memory}.
	 *
	 * @param memory the memory to be copied.
	 * @throws NullPointerException if the given {@code memory} is null.
	 * @since 0.2.0 ~2021.05.23
	 */
	public Memory(@NotNull Memory memory) {
		Objects.requireNonNull(memory, "memory");
		this.frames = memory.frames
				.stream()
				.map(Frame::new)
				.collect(Collectors.toCollection(LinkedList::new));
		this.console = memory.console;
	}

	/**
	 * Return an immutable iterator over the frames in this memory. (from the base to the
	 * top)
	 *
	 * @return an iterator over the frames in the memory.
	 * @since 0.3.0 ~2021.06.25
	 */
	@NotNull
	@Override
	public Iterator<Frame> iterator() {
		return Collections.unmodifiableCollection(this.frames).iterator();
	}

	/**
	 * Replace the value at the given {@code address} with the result of invoking the
	 * given {@code operator} with the current value.
	 * <br>
	 * If the given {@code operator} thrown any exception when invoked by this method. The
	 * thrown exception will fall throw this method with nothing changed.
	 * <br>
	 * The computing will be performed at the heap of the first frame.
	 *
	 * @param address  the address to replace the value at.
	 * @param operator the function to be invoked to acquire the replacement value.
	 * @return the computed value.
	 * @throws NullPointerException if the given {@code address} or {@code operator} is
	 *                              null.
	 * @since 0.2.0 ~2021.05.21
	 */
	@Contract(mutates = "this")
	public Value compute(@NotNull String address, @NotNull UnaryOperator<Value> operator) {
		Objects.requireNonNull(address, "address");
		Objects.requireNonNull(operator, "operator");
		return this.frames.getFirst().compute(address, operator);
	}

	/**
	 * Dump the stack and the heap of the current frame to frame before it. If the current
	 * frame is the base frame, then a copy of the base from will be returned.
	 *
	 * @return the dumped frame.
	 * @since 0.2.0 ~2021.05.23
	 */
	@NotNull
	@Contract(mutates = "this")
	public Frame dumpFrame() {
		if (this.frames.size() == 1)
			return new Frame(this.frames.peek());

		Frame frame = this.frames.pollLast();
		Frame before = this.frames.peekLast();
		before.stack.addAll(frame.stack);
		before.heap.putAll(frame.heap);
		return frame;
	}

	/**
	 * Return the value allocated at the given {@code address} in this memory.
	 * <br>
	 * The value is returned from the last frame that has an allocation in the given
	 * {@code address}.
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
		Iterator<Frame> iterator = this.frames.descendingIterator();
		while (iterator.hasNext()) {
			Value value = iterator.next().heap.getOrDefault(address, null);

			if (value != null)
				return value;
		}

		return Value.NULL;
	}

	/**
	 * Return current set console.
	 *
	 * @return the current console.
	 * @since 0.2.0 ~2021.05.23
	 */
	@NotNull
	@Contract(pure = true)
	public Console getConsole() {
		return this.console;
	}

	/**
	 * Return the current last frame.
	 *
	 * @return the current frame.
	 * @since 0.2.0 ~2021.05.23
	 */
	@NotNull
	@Contract(pure = true)
	public Frame getFrame() {
		return this.frames.getLast();
	}

	/**
	 * Build the current stack trace of trees.
	 *
	 * @return the current stack trace.
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	@Contract(pure = true)
	public Tree[] getStackTrace() {
		List<Tree> stacktrace = new ArrayList<>(this.frames.size());

		Tree previous = null;

		Iterator<Frame> iterator = this.frames.descendingIterator();
		while (iterator.hasNext()) {
			Instruction instruction = iterator.next().getInstruction();

			if (instruction != null) {
				Tree next = instruction.getTree();

				if (next != null && next != previous) {
					stacktrace.add(next);
					previous = next;
				}
			}
		}

		//noinspection ZeroLengthArrayAllocation
		return stacktrace.toArray(new Tree[0]);
	}

	/**
	 * Return the last pushed value to the stack.
	 * <br>
	 * The stack of the last frame will be accessed.
	 *
	 * @return the last value pushed to the stack. Or {@link Value#NULL} if the stack was
	 * 		empty.
	 * @since 0.3.0 ~2021.06.11
	 */
	@NotNull
	@Contract(pure = true)
	public Value peek() {
		return this.frames.getLast().peek();
	}

	/**
	 * Return and remove the last pushed value to the stack.
	 * <br>
	 * The stack of the last frame will be modified.
	 *
	 * @return the last value pushed to the stack. Or {@link Value#NULL} if the stack was
	 * 		empty.
	 * @since 0.2.0 ~2021.05.21
	 */
	@NotNull
	@Contract(mutates = "this")
	public Value pop() {
		return this.frames.getLast().pop();
	}

	/**
	 * Pop the current stack frame. Popping the stack frame will result to this memory
	 * representing the frame previous to the last pushed frame. If the base frame to be
	 * popped, then the base frame will be replaced with a new one instead of popped.
	 *
	 * @return the popped frame.
	 * @since 0.2.0 ~2021.05.21
	 */
	@Contract(mutates = "this")
	public Frame popFrame() {
		Frame frame = this.frames.pollLast();

		if (this.frames.isEmpty())
			this.frames.addLast(new Frame());

		return frame;
	}

	/**
	 * Print the given {@code text} to the console.
	 * <br>
	 * Note: the console is the buffer containing the output of the execution of the
	 * instruction.
	 *
	 * @param text the text to be printed.
	 * @throws NullPointerException if the given {@code text} is null.
	 * @throws IOError              if an error occurred while writing to the console.
	 * @since 0.2.0 ~2021.05.21
	 */
	@Contract(mutates = "this")
	public void print(@NotNull CharSequence text) {
		Objects.requireNonNull(text, "text");
		this.console.print(text);
	}

	/**
	 * Push the given {@code value} to the stack part of this memory.
	 * <br>
	 * The value will be pushed to the stack of the last frame.
	 *
	 * @param value the value to be pushed.
	 * @throws NullPointerException if the given {@code value} is null.
	 * @since 0.2.0 ~2021.05.21
	 */
	@Contract(mutates = "this")
	public void push(@NotNull Value value) {
		Objects.requireNonNull(value, "value");
		this.frames.getLast().push(value);
	}

	/**
	 * Push a new frame.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	@Contract(mutates = "this")
	public void pushFrame() {
		this.frames.addLast(new Frame());
	}

	/**
	 * Push the given {@code frame} in this memory.
	 *
	 * @param frame the frame to be pushed.
	 * @throws NullPointerException if the given {@code frame} is null.
	 * @since 0.2.0 ~2021.05.21
	 */
	@Contract(mutates = "this")
	public void pushFrame(@NotNull Frame frame) {
		Objects.requireNonNull(frame, "frame");
		this.frames.addLast(frame);
	}

	/**
	 * Set the value at the given {@code address} to be the given {@code value}.
	 * <br>
	 * The value will be allocated at the heap of the first frame.
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
		this.frames.getFirst().set(address, value);
	}

	/**
	 * Set the console to be the given {@code console}.
	 *
	 * @param console the new console to be set.
	 * @throws NullPointerException if the given {@code console} is null.
	 * @since 0.2.0 ~2021.05.23
	 */
	@Contract(mutates = "this")
	public void setConsole(@NotNull Console console) {
		Objects.requireNonNull(console, "console");
		this.console = console;
	}
}
