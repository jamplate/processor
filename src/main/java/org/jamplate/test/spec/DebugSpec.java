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
package org.jamplate.test.spec;

import org.jamplate.api.Spec;
import org.jamplate.diagnostic.Message;
import org.jamplate.internal.diagnostic.MessagePriority;
import org.jamplate.model.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * A specification that adds useful behaviour for debugging/testing.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.25
 */
@SuppressWarnings({"UseOfSystemOutOrSystemErr", "MethodMayBeStatic"})
public class DebugSpec implements Spec {
	/**
	 * An instance of this spec.
	 *
	 * @since 0.3.0 ~2021.06.25
	 */
	@NotNull
	public static final DebugSpec INSTANCE = new DebugSpec();

	/**
	 * The qualified name of this spec.
	 *
	 * @since 0.3.0 ~2021.06.25
	 */
	@NotNull
	public static final String NAME = DebugSpec.class.getSimpleName();

	@NotNull
	@Override
	public String getQualifiedName() {
		return DebugSpec.NAME;
	}

	@Override
	public void onCreateMemory(@NotNull Compilation compilation, @NotNull Memory memory) {
		Objects.requireNonNull(compilation, "compilation");
		Objects.requireNonNull(memory, "memory");
		System.out.println("Instruction:");
		System.out.println(this.format(1, compilation.getInstruction()));
	}

	@Override
	public void onDestroyMemory(@NotNull Compilation compilation, @NotNull Memory memory) {
		Objects.requireNonNull(compilation, "compilation");
		Objects.requireNonNull(memory, "memory");
		System.out.println("Memory:");

		for (Frame frame : memory.getFrames())
			System.out.println(this.format(1, memory, frame));

		System.out.println(this.format(1, memory.getConsole()));
	}

	@Override
	public void onDiagnostic(@NotNull Environment environment, @NotNull Message message) {
		Objects.requireNonNull(environment, "environment");
		Objects.requireNonNull(message, "message");

		if (message.isFetal()) {
			environment.getDiagnostic().flush(true);

			if (message.getException() instanceof AssertionError)
				throw (AssertionError) message.getException();
		} else {
			String formatted = environment.getDiagnostic().format(true, message);

			switch (message.getPriority()) {
				case MessagePriority.NOTE:
				case MessagePriority.INFO:
					//cyan
					System.out.println("\u001B[36m" + formatted + "\u001B[0m");
					break;
				case MessagePriority.WARNING:
					//yellow
					System.out.println("\u001B[33m" + formatted + "\u001B[0m");
					break;
				case MessagePriority.ERROR:
					//error
					System.err.println(formatted);
					break;
				case MessagePriority.DEBUG:
					//green
					System.out.println("\u001B[32m" + formatted + "\u001B[0m");
					break;
			}
		}
	}

	/**
	 * Return the graph of the given {@code instruction}.
	 *
	 * @param indent      the indention to indent the graph by.
	 * @param instruction the instruction to draw a graph of it.
	 * @return a graph of the given {@code instruction}.
	 * @throws NullPointerException     if the given {@code instruction} is null.
	 * @throws IllegalArgumentException if the given {@code indent} is negative.
	 * @since 0.3.0 ~2021.06.25
	 */
	@Contract(pure = true)
	protected String format(int indent, @NotNull Instruction instruction) {
		StringBuilder buffer = new StringBuilder();
		this.format(indent, buffer, instruction);
		return buffer.toString();
	}

	/**
	 * Return the graph of the given {@code frame} at the given {@code memory}.
	 *
	 * @param indent the indention to indent the graph by.
	 * @param memory the memory to evaluate the values with.
	 * @param frame  the frame to draw a graph of it.
	 * @return a graph of the given {@code frame}.
	 * @throws NullPointerException     if the given {@code memory} or {@code frame} is
	 *                                  null.
	 * @throws IllegalArgumentException if the given {@code indent} is negative.
	 * @since 0.3.0 ~2021.06.25
	 */
	@Contract(pure = true)
	protected String format(int indent, @NotNull Memory memory, @NotNull Frame frame) {
		StringBuilder buffer = new StringBuilder();
		this.format(indent, buffer, memory, frame);
		return buffer.toString();
	}

	/**
	 * Return a graph of the given {@code console}.
	 *
	 * @param indent  the indentation to indent the graph by.
	 * @param console the console to draw a graph of.
	 * @return a graph of the given {@code console}.
	 * @throws NullPointerException     if the given {@code console} is null.
	 * @throws IllegalArgumentException if the given {@code indent} is negative.
	 * @since 0.3.0 ~2021.06.25
	 */
	@Contract(pure = true)
	protected String format(int indent, @NotNull Appendable console) {
		StringBuilder buffer = new StringBuilder();
		this.format(indent, buffer, console);
		return buffer.toString();
	}

	/**
	 * Draw the graph of the given {@code instruction} to the given {@code buffer}.
	 *
	 * @param indent      the indention to indent the graph by.
	 * @param buffer      the buffer to draw to.
	 * @param instruction the instruction to draw a graph of it.
	 * @throws NullPointerException     if the given {@code buffer} or {@code instruction}
	 *                                  is null.
	 * @throws IllegalArgumentException if the given {@code indent} is negative.
	 * @since 0.3.0 ~2021.06.25
	 */
	@Contract(mutates = "param2")
	protected void format(int indent, @NotNull StringBuilder buffer, @NotNull Instruction instruction) {
		Objects.requireNonNull(buffer, "buffer");
		Objects.requireNonNull(instruction, "instruction");
		if (indent < 0)
			throw new IllegalArgumentException("indent < 0");

		StringJoiner indentation = new StringJoiner("|\t", "\t", "|- ");
		for (int i = 0; i < indent; i++)
			indentation.add("");

		String simpleName = instruction.getClass().getSimpleName();
		char[] simpleNameChars = new char[Math.max(simpleName.length(), 20)];
		Arrays.fill(simpleNameChars, ' ');
		simpleName.getChars(0, simpleName.length(), simpleNameChars, 0);
		String simpleNamePadded = String.valueOf(simpleNameChars);

		String treeString = String.valueOf(instruction.getTree());
		char[] treeStringChars = new char[Math.max(treeString.length(), 35)];
		Arrays.fill(treeStringChars, ' ');
		treeString.getChars(0, treeString.length(), treeStringChars, 0);
		String treeStringPadded = String.valueOf(treeStringChars);

		//append instruction string
		buffer.append(indentation);
		//append name
		buffer.append(simpleNamePadded);
		buffer.append("\t[ ");
		buffer.append(treeStringPadded);
		buffer.append(" ]");
		buffer.append("\n");

		//foreach sub
		for (Instruction sub : instruction)
			//append format sub
			this.format(indent + 1, buffer, sub);
	}

	/**
	 * Draw the graph of the given {@code frame} at the given {@code memory} to the given
	 * {@code buffer}.
	 *
	 * @param indent the indention to indent the graph by.
	 * @param buffer the buffer to draw to.
	 * @param memory the memory to evaluate the values with.
	 * @param frame  the frame to draw a graph of it.
	 * @throws NullPointerException     if the given {@code buffer} or {@code memory} or
	 *                                  {@code frame} is null.
	 * @throws IllegalArgumentException if the given {@code indent} is negative.
	 * @since 0.3.0 ~2021.06.25
	 */
	@SuppressWarnings("OverlyLongMethod")
	@Contract(mutates = "param2")
	protected void format(int indent, @NotNull StringBuilder buffer, @NotNull Memory memory, @NotNull Frame frame) {
		Objects.requireNonNull(buffer, "buffer");
		Objects.requireNonNull(memory, "memory");
		Objects.requireNonNull(frame, "frame");
		if (indent < 0)
			throw new IllegalArgumentException("indent < 0");

		StringJoiner indentation = new StringJoiner("|\t", "\t", "|- ");
		for (int i = 0; i < indent; i++)
			indentation.add("");
		StringJoiner indentationSub = new StringJoiner("|\t", "\t", "|- ");
		for (int i = -1; i < indent; i++)
			indentationSub.add("");
		StringJoiner indentationSubSub = new StringJoiner("|\t", "\t", "|- ");
		for (int i = -2; i < indent; i++)
			indentationSubSub.add("");

		Instruction instruction = frame.getInstruction();
		Tree tree = instruction == null ? null : instruction.getTree();

		buffer.append(indentation);
		buffer.append("Frame:");
		buffer.append("\n");

		buffer.append(indentationSub);
		buffer.append("Instruction:\t");
		buffer.append(instruction);
		buffer.append("\n");

		buffer.append(indentationSub);
		buffer.append("Tree:\t\t");
		buffer.append(tree);
		buffer.append("\n");

		buffer.append(indentationSub);
		buffer.append("Stack:");
		frame.forEach(value -> {
			buffer.append("\n");
			buffer.append(indentationSubSub);
			buffer.append(
					value == Value.NULL ?
					"Value.NULL" :
					value.evaluate(memory)
			);
		});
		buffer.append("\n");

		buffer.append(indentationSub);
		buffer.append("Heap:");
		frame.forEach((address, value) -> {
			buffer.append("\n");
			buffer.append(indentationSubSub);
			buffer.append(address);
			buffer.append(" ~> ");
			buffer.append(
					value == Value.NULL ?
					"Value.NULL" :
					value.evaluate(memory)
			);
		});
	}

	/**
	 * Draw a graph of the given {@code console} to the given {@code buffer}.
	 *
	 * @param indent  the indentation to indent the graph by.
	 * @param buffer  the buffer to draw to.
	 * @param console the console to draw a graph of.
	 * @throws NullPointerException     if the given {@code buffer} or {@code console} is
	 *                                  null.
	 * @throws IllegalArgumentException if the given {@code indent} is negative.
	 * @since 0.3.0 ~2021.06.25
	 */
	@Contract(mutates = "param2")
	protected void format(int indent, @NotNull StringBuilder buffer, @NotNull Appendable console) {
		Objects.requireNonNull(buffer, "buffer");
		Objects.requireNonNull(console, "console");
		if (indent < 0)
			throw new IllegalArgumentException("indent < 0");

		StringJoiner indentation = new StringJoiner("|\t", "\t", "|- ");
		for (int i = 0; i < indent; i++)
			indentation.add("");
		StringJoiner indentationSub = new StringJoiner("|\t", "\t", "|- ");
		for (int i = -1; i < indent; i++)
			indentationSub.add("");

		//noinspection DynamicRegexReplaceableByCompiledPattern
		String consoleString = console.toString().replace("\n", "\n" + indentationSub);

		buffer.append(indentation);
		buffer.append("Console:");
		buffer.append("\n");

		buffer.append(indentationSub);
		buffer.append(consoleString);
	}
}
