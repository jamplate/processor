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
package org.jamplate.glucose.spec.tool;

import org.jamplate.api.Spec;
import org.jamplate.api.Unit;
import org.jamplate.diagnostic.Diagnostic;
import org.jamplate.diagnostic.Message;
import org.jamplate.function.Listener;
import org.jamplate.impl.api.Event;
import org.jamplate.impl.diagnostic.MessagePriority;
import org.jamplate.memory.Console;
import org.jamplate.memory.Frame;
import org.jamplate.memory.Memory;
import org.jamplate.model.Document;
import org.jamplate.model.Instruction;
import org.jamplate.model.Tree;
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

	@SuppressWarnings({"OverlyLongLambda", "OverlyLongMethod"})
	@NotNull
	@Override
	public Listener getListener() {
		return (event, compilation, parameter) -> {
			switch (event) {
				case Event.DIAGNOSTIC:
					Diagnostic diagnostic = (Diagnostic) parameter;

					for (Message message : diagnostic)
						if (message.isFetal()) {
							diagnostic.flush(true);

							if (message.getException() instanceof AssertionError)
								throw (AssertionError) message.getException();
						} else {
							String formatted = diagnostic.format(true, message);

							switch (message.getPriority()) {
								case MessagePriority.NOTE:
								case MessagePriority.INFO:
									//cyan
									System.out.println(
											"\u001B[36m" + formatted + "\u001B[0m");
									break;
								case MessagePriority.WARNING:
									//yellow
									System.out.println(
											"\u001B[33m" + formatted + "\u001B[0m");
									break;
								case MessagePriority.ERROR:
									//error
									System.err.println(formatted);
									break;
								case MessagePriority.DEBUG:
									//green
									System.out.println(
											"\u001B[32m" + formatted + "\u001B[0m");
									break;
							}
						}
					break;
				case Event.POST_INIT:
				case Event.POST_PARSE:
				case Event.POST_ANALYZE:
				case Event.POST_COMPILE:
				case Event.PRE_EXEC:
				case Event.POST_EXEC:
					Tree tree = compilation.getRootTree();
					Instruction instruction = compilation.getInstruction();
					Document document = tree.getDocument();

					//head
					System.out.println("Event (" + event + ") :");

					//document
					System.out.println("\t|- Document: " + document);

					//spec
					if (parameter instanceof Unit) {
						Unit unit = (Unit) parameter;

						System.out.println("\t|- Spec:");
						System.out.print(this.format(2, unit.getSpec()));
					}

					//tree
					System.out.println("\t|- Tree:");
					System.out.print(this.format(2, tree));

					//instruction
					if (instruction != null) {
						System.out.println("\t|- Instruction:");
						System.out.print(this.format(2, compilation.getInstruction()));
					}

					//memory
					if (parameter instanceof Memory) {
						Memory memory = (Memory) parameter;

						System.out.println("\t|- Memory:");

						for (Frame frame : memory)
							System.out.print(this.format(2, memory, frame));

						System.out.print(this.format(2, memory.getConsole()));
					}

					System.out.println();
				default:
					break;
			}
		};
	}

	@NotNull
	@Override
	public String getQualifiedName() {
		return DebugSpec.NAME;
	}

	/**
	 * Return the graph of the given {@code tree}.
	 *
	 * @param indent the indention to indent the graph by.
	 * @param tree   the tree to draw a graph of it.
	 * @return a graph of the given {@code tree}.
	 * @throws NullPointerException     if the given {@code tree} is null.
	 * @throws IllegalArgumentException if the given {@code indent} is negative.
	 * @since 0.3.0 ~2021.06.27
	 */
	@Contract(pure = true)
	protected String format(int indent, @NotNull Tree tree) {
		StringBuilder buffer = new StringBuilder();
		this.format(indent, buffer, tree);
		return buffer.toString();
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
	 * Return the graph of the given {@code spec}.
	 *
	 * @param indent the indention to indent the graph by.
	 * @param spec   the spec to draw a graph of it.
	 * @return a graph of the given {@code spec}.
	 * @throws NullPointerException     if the given {@code spec} is null.
	 * @throws IllegalArgumentException if the given {@code indent} is negative.
	 * @since 0.3.0 ~2021.06.25
	 */
	@Contract(pure = true)
	protected String format(int indent, @NotNull Spec spec) {
		StringBuilder buffer = new StringBuilder();
		this.format(indent, buffer, spec);
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
	protected String format(int indent, @NotNull Console console) {
		StringBuilder buffer = new StringBuilder();
		this.format(indent, buffer, console);
		return buffer.toString();
	}

	/**
	 * Draw the graph of the given {@code tree} to the given {@code buffer}.
	 *
	 * @param indent the indention to indent the graph by.
	 * @param buffer the buffer to draw to.
	 * @param tree   the tree to draw a graph of it.
	 * @throws NullPointerException     if the given {@code buffer} or {@code tree} is
	 *                                  null.
	 * @throws IllegalArgumentException if the given {@code indent} is negative.
	 * @since 0.3.0 ~2021.06.27
	 */
	@Contract(mutates = "param2")
	protected void format(int indent, @NotNull StringBuilder buffer, @NotNull Tree tree) {
		Objects.requireNonNull(buffer, "buffer");
		Objects.requireNonNull(tree, "tree");
		if (indent < 0)
			throw new IllegalArgumentException("indent < 0");

		StringJoiner indentation = new StringJoiner("|\t", "\t", "|- ");
		for (int i = 0; i < indent; i++)
			indentation.add("");

		String treeString = tree.toString();

		buffer.append(indentation);
		buffer.append(treeString);
		buffer.append("\n");

		for (Tree child : tree)
			this.format(indent + 1, buffer, child);
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
	 * Draw the graph of the given {@code spec} to the given {@code buffer}.
	 *
	 * @param indent the indention to indent the graph by.
	 * @param buffer the buffer to draw to.
	 * @param spec   the spec to draw a graph of it.
	 * @throws NullPointerException     if the given {@code buffer} or {@code spec} is
	 *                                  null.
	 * @throws IllegalArgumentException if the given {@code indent} is negative.
	 * @since 0.3.0 ~2021.06.25
	 */
	@Contract(mutates = "param2")
	protected void format(int indent, @NotNull StringBuilder buffer, @NotNull Spec spec) {
		Objects.requireNonNull(buffer, "buffer");
		Objects.requireNonNull(spec, "spec");
		if (indent < 0)
			throw new IllegalArgumentException("indent < 0");

		StringJoiner indentation = new StringJoiner("|\t", "\t", "|- ");
		for (int i = 0; i < indent; i++)
			indentation.add("");

		String qualifiedName = spec.getQualifiedName();

		buffer.append(indentation);
		buffer.append(qualifiedName);
		buffer.append("\n");

		for (Spec subspec : spec)
			this.format(indent + 1, buffer, subspec);
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
		buffer.append("\n");
		frame.forEach(value -> {
			buffer.append(indentationSubSub);
			buffer.append("[");
			buffer.append(value.toString());
			buffer.append("]");
			buffer.append("\t");
			buffer.append(value.evaluate(memory));
			buffer.append("\n");
		});

		buffer.append(indentationSub);
		buffer.append("Heap:");
		buffer.append("\n");
		frame.forEach((address, value) -> {
			buffer.append(indentationSubSub);
			buffer.append(address);
			buffer.append(" ~> ");
			buffer.append("[");
			buffer.append(value.toString());
			buffer.append("]");
			buffer.append("\t");
			buffer.append(value.evaluate(memory));
			buffer.append("\n");
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
	protected void format(int indent, @NotNull StringBuilder buffer, @NotNull Console console) {
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
		String consoleString = console.read().replace("\n", "\n" + indentationSub);

		buffer.append(indentation);
		buffer.append("Console:");
		buffer.append("\n");

		buffer.append(indentationSub);
		buffer.append(consoleString);
		buffer.append("\n");
	}
}
