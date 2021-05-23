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

import java.io.Closeable;
import java.io.IOError;
import java.io.IOException;
import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

/**
 * The memory is a structure that saves the variables at the runtime.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.21
 */
public final class Memory implements Closeable {
	/**
	 * The frames in this memory. Always not empty.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	@NotNull
	private final Deque<Frame> frames;

	/**
	 * A an appendable to append the text printed to the console to it.
	 *
	 * @since 0.2.0 ~2021.05.21
	 */
	@NotNull
	private Appendable console;

	/**
	 * Construct a new empty memory.
	 *
	 * @since 0.2.0 ~2021.05.21
	 */
	public Memory() {
		this.frames = new LinkedList<>(Collections.singleton(new Frame()));
		this.console = new StringBuilder();
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

	@Override
	public void close() throws IOException {
		if (this.console instanceof Closeable)
			((Closeable) this.console).close();
	}

	/**
	 * Replace the value at the given {@code address} with the result of invoking the
	 * given {@code operator} with the current value.
	 * <br>
	 * If the given {@code operator} thrown any exception when invoked by this method. The
	 * thrown exception will fall throw this method with nothing changed.
	 * <br>
	 * The computing will be performed on the last frame that contain an allocation to the
	 * given {@code address}. If no frame has an allocation to the given {@code address}
	 * then the result of the computation will be store in the base frame.
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
		Iterator<Frame> iterator = this.frames.descendingIterator();
		while (iterator.hasNext()) {
			Frame frame = iterator.next();
			//noinspection AccessingNonPublicFieldOfAnotherObject
			Value value = frame.heap.getOrDefault(address, null);

			if (value != null) {
				Value v = operator.apply(value);
				frame.set(address, v);
				return v;
			}
		}

		Value v = operator.apply(Value.NULL);
		this.frames.getFirst().set(address, v);
		return v;
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
		//noinspection AccessingNonPublicFieldOfAnotherObject
		before.stack.addAll(frame.stack);
		//noinspection AccessingNonPublicFieldOfAnotherObject
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
			//noinspection AccessingNonPublicFieldOfAnotherObject
			Value value = iterator.next().heap.getOrDefault(address, null);

			if (value != null)
				return value;
		}

		return Value.NULL;
	}

	/**
	 * Return current set console appendable.
	 *
	 * @return the current console.
	 * @since 0.2.0 ~2021.05.23
	 */
	@NotNull
	@Contract(pure = true)
	public Appendable getConsole() {
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
	 * Return an array containing the current frames in this memory.
	 *
	 * @return an array of the current frames.
	 * @since 0.2.0 ~2021.05.23
	 */
	@NotNull
	@Contract(value = "->new", pure = true)
	public Frame[] getFrames() {
		//noinspection SimplifyStreamApiCallChains
		return this.frames.stream().toArray(Frame[]::new);
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
		try {
			this.console.append(text);
		} catch (IOException e) {
			throw new IOError(e);
		}
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
	public void setConsole(@NotNull Appendable console) {
		Objects.requireNonNull(console, "console");
		if (this.console instanceof Closeable)
			try {
				((Closeable) this.console).close();
			} catch (IOException e) {
				//noinspection CallToPrintStackTrace
				e.printStackTrace();
			}

		this.console = console;
	}

	/**
	 * An isolated part in the memory.
	 *
	 * @author LSafer
	 * @version 0.2.0
	 * @since 0.2.0 ~2021.05.23
	 */
	public static final class Frame {
		/**
		 * The heap part of this frame.
		 *
		 * @since 0.2.0 ~2021.05.23
		 */
		@NotNull
		private final Map<String, Value> heap;
		/**
		 * The stack part of this frame.
		 *
		 * @since 0.2.0 ~2021.05.23
		 */
		@NotNull
		private final Queue<Value> stack;

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
		 * If the given {@code operator} thrown any exception when invoked by this method.
		 * The thrown exception will fall throw this method with nothing changed.
		 *
		 * @param address  the address to replace the value at.
		 * @param operator the function to be invoked to acquire the replacement value.
		 * @return the computed value.
		 * @throws NullPointerException if the given {@code address} or {@code operator}
		 *                              is null.
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
		 * @return the value at the given {@code address}. Or {@link Value#NULL} if no
		 * 		value was allocated there.
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
		 * Return and remove the last pushed value to the stack.
		 *
		 * @return the last value pushed to the stack. Or {@link Value#NULL} if the stack
		 * 		was empty.
		 * @since 0.2.0 ~2021.05.23
		 */
		@NotNull
		@Contract(mutates = "this")
		public Value pop() {
			Value value = this.stack.poll();
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
			this.stack.add(value);
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
}
