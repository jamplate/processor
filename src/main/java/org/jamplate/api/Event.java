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
package org.jamplate.api;

import org.jamplate.diagnostic.Diagnostic;
import org.jamplate.memory.Memory;
import org.jamplate.model.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * An object holding the parameters of an event.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.07.04
 */
public class Event {
	/**
	 * The name of the action of triggered this event.
	 *
	 * @since 0.3.0 ~2021.07.05
	 */
	@NotNull
	protected final String action;
	/**
	 * The compilation.
	 *
	 * @since 0.3.0 ~2021.07.05
	 */
	@Nullable
	protected Compilation compilation;
	/**
	 * The diagnostic.
	 *
	 * @since 0.3.0 ~2021.07.05
	 */
	@Nullable
	protected Diagnostic diagnostic;
	/**
	 * The document.
	 *
	 * @since 0.3.0 ~2021.07.05
	 */
	@Nullable
	protected Document document;
	/**
	 * The environment.
	 *
	 * @since 0.3.0 ~2021.07.05
	 */
	@Nullable
	protected Environment environment;
	/**
	 * Extra data.
	 *
	 * @since 0.3.0 ~2021.07.05
	 */
	@NotNull
	protected Map<String, Object> extra = new HashMap<>();
	/**
	 * The instruction.
	 *
	 * @since 0.3.0 ~2021.07.05
	 */
	@Nullable
	protected Instruction instruction;
	/**
	 * The memory.
	 *
	 * @since 0.3.0 ~2021.07.05
	 */
	@Nullable
	protected Memory memory;
	/**
	 * The tree.
	 *
	 * @since 0.3.0 ~2021.07.05
	 */
	@Nullable
	protected Tree tree;
	/**
	 * The unit.
	 *
	 * @since 0.3.0 ~2021.07.05
	 */
	@Nullable
	protected Unit unit;

	/**
	 * Construct a new event object.
	 *
	 * @param action     the name of the action triggered the event.
	 * @param parameters the parameters of the event object.
	 * @throws NullPointerException     if the given {@code action} or {@code parameters}
	 *                                  is null.
	 * @throws IllegalArgumentException if a parameter is not a valid event parameter.
	 * @since 0.3.0 ~2021.07.05
	 */
	public Event(@NotNull String action, @Nullable Object @NotNull ... parameters) {
		Objects.requireNonNull(parameters, "parameters");
		this.action = action;
		for (Object parameter : parameters)
			if (parameter instanceof Compilation)
				this.setCompilation((Compilation) parameter);
			else if (parameter instanceof Diagnostic)
				this.setDiagnostic((Diagnostic) parameter);
			else if (parameter instanceof Document)
				this.setDocument((Document) parameter);
			else if (parameter instanceof Environment)
				this.setEnvironment((Environment) parameter);
			else if (parameter instanceof Instruction)
				this.setInstruction((Instruction) parameter);
			else if (parameter instanceof Memory)
				this.setMemory((Memory) parameter);
			else if (parameter instanceof Tree)
				this.setTree((Tree) parameter);
			else if (parameter instanceof Unit)
				this.setUnit((Unit) parameter);
			else if (parameter != null)
				throw new IllegalArgumentException(
						"Unrecognized parameter: " + parameter
				);
	}

	/**
	 * Return the name of the action triggered this event.
	 *
	 * @return the action name.
	 * @since 0.3.0 ~2021.07.05
	 */
	@NotNull
	@Contract(pure = true)
	public String getAction() {
		return this.action;
	}

	/**
	 * Return the compilation passed with the event.
	 *
	 * @return the compilation.
	 * @since 0.3.0 ~2021.07.04
	 */
	@Nullable
	@Contract(pure = true)
	public Compilation getCompilation() {
		return this.compilation;
	}

	/**
	 * Return the diagnostic passed with the event.
	 *
	 * @return the diagnostic.
	 * @since 0.3.0 ~2021.07.04
	 */
	@Nullable
	@Contract(pure = true)
	public Diagnostic getDiagnostic() {
		return this.diagnostic;
	}

	/**
	 * Return the document passed with the event.
	 *
	 * @return the document.
	 * @since 0.3.0 ~2021.07.04
	 */
	@Nullable
	@Contract(pure = true)
	public Document getDocument() {
		return this.document;
	}

	/**
	 * Return the environment passed with the event.
	 *
	 * @return the environment.
	 * @since 0.3.0 ~2021.07.04
	 */
	@Nullable
	@Contract(pure = true)
	public Environment getEnvironment() {
		return this.environment;
	}

	/**
	 * Get the extra data set to the given {@code key}.
	 * <br>
	 * Note: follow the applicable standard to avoid unwanted {@link ClassCastException}
	 * issues.
	 *
	 * @param key the key of the data.
	 * @param <T> the expected type of the data. (unsafe)
	 * @return the data.
	 * @throws NullPointerException if the given {@code key} is null.
	 * @since 0.3.0 ~2021.07.05
	 */
	@NotNull
	@Contract(pure = true)
	public <T> T getExtra(@NotNull String key) {
		Objects.requireNonNull(key, "key");
		return (T) this.extra.get(key);
	}

	/**
	 * Return the instruction passed with the event.
	 *
	 * @return the instruction.
	 * @since 0.3.0 ~2021.07.04
	 */
	@Nullable
	@Contract(pure = true)
	public Instruction getInstruction() {
		return this.instruction;
	}

	/**
	 * Return the memory passed with the event.
	 *
	 * @return the memory.
	 * @since 0.3.0 ~2021.07.04
	 */
	@Nullable
	@Contract(pure = true)
	public Memory getMemory() {
		return this.memory;
	}

	/**
	 * Return the tree passed with the event.
	 *
	 * @return the tree.
	 * @since 0.3.0 ~2021.07.04
	 */
	@Nullable
	@Contract(pure = true)
	public Tree getTree() {
		return this.tree;
	}

	/**
	 * Return the unit passed with the event.
	 *
	 * @return the unit.
	 * @since 0.3.0 ~2021.07.04
	 */
	@Nullable
	@Contract(pure = true)
	public Unit getUnit() {
		return this.unit;
	}

	/**
	 * Put the given extra data.
	 * <br>
	 * Note: follow the applicable standard to avoid unwanted {@link ClassCastException}
	 * issues.
	 *
	 * @param key   the key to put the data to.
	 * @param value the value to be put.
	 * @return this.
	 * @throws NullPointerException if the given {@code key} is null.
	 * @since 0.3.0 ~2021.07.05
	 */
	@NotNull
	@Contract(mutates = "this")
	public Event putExtra(@NotNull String key, @Nullable Object value) {
		Objects.requireNonNull(key, "key");
		Objects.requireNonNull(value, "value");
		this.extra.put(key, value);
		return this;
	}

	/**
	 * Set the compilation parameter.
	 *
	 * @param compilation the compilation.
	 * @return this.
	 * @throws NullPointerException if the given {@code compilation} is null.
	 * @since 0.3.0 ~2021.07.05
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	public Event setCompilation(@NotNull Compilation compilation) {
		Objects.requireNonNull(compilation, "compilation");
		this.compilation = compilation;
		if (this.environment == null)
			this.setEnvironment(compilation.getEnvironment());
		if (this.tree == null)
			this.setTree(compilation.getRootTree());
		if (this.instruction == null) {
			Instruction instruction = compilation.getInstruction();

			if (instruction != null)
				this.setInstruction(compilation.getInstruction());
		}
		return this;
	}

	/**
	 * Set the diagnostic parameter.
	 *
	 * @param diagnostic the diagnostic.
	 * @return this.
	 * @throws NullPointerException if the given {@code diagnostic} is null.
	 * @since 0.3.0 ~2021.07.05
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	public Event setDiagnostic(@NotNull Diagnostic diagnostic) {
		Objects.requireNonNull(diagnostic, "diagnostic");
		this.diagnostic = diagnostic;
		return this;
	}

	/**
	 * Set the document parameter.
	 *
	 * @param document the document.
	 * @return this.
	 * @throws NullPointerException if the given {@code document} is null.
	 * @since 0.3.0 ~2021.07.05
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	public Event setDocument(@NotNull Document document) {
		Objects.requireNonNull(document, "document");
		this.document = document;
		return this;
	}

	/**
	 * Set the environment parameter.
	 *
	 * @param environment the environment.
	 * @return this.
	 * @throws NullPointerException if the given {@code environment} is null.
	 * @since 0.3.0 ~2021.07.05
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	public Event setEnvironment(@NotNull Environment environment) {
		Objects.requireNonNull(environment, "environment");
		this.environment = environment;
		if (this.diagnostic == null)
			this.setDiagnostic(environment.getDiagnostic());
		return this;
	}

	/**
	 * Set the instruction parameter.
	 *
	 * @param instruction the instruction.
	 * @return this.
	 * @throws NullPointerException if the given {@code instruction} is null.
	 * @since 0.3.0 ~2021.07.05
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	public Event setInstruction(@NotNull Instruction instruction) {
		Objects.requireNonNull(instruction, "instruction");
		this.instruction = instruction;
		if (this.tree == null) {
			Tree tree = instruction.getTree();

			if (tree != null)
				this.setTree(tree);
		}
		return this;
	}

	/**
	 * Set the memory parameter.
	 *
	 * @param memory the memory.
	 * @return this.
	 * @throws NullPointerException if the given {@code memory} is null.
	 * @since 0.3.0 ~2021.07.05
	 */
	@NotNull
	@Contract(pure = true)
	public Event setMemory(@NotNull Memory memory) {
		Objects.requireNonNull(memory, "memory");
		this.memory = memory;
		return this;
	}

	/**
	 * Set the tree parameter.
	 *
	 * @param tree the tree.
	 * @return this.
	 * @throws NullPointerException if the given {@code tree} is null.
	 * @since 0.3.0 ~2021.07.05
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	public Event setTree(@NotNull Tree tree) {
		Objects.requireNonNull(tree, "tree");
		this.tree = tree;
		if (this.document == null)
			this.setDocument(tree.getDocument());
		return this;
	}

	/**
	 * Set the unit parameter.
	 *
	 * @param unit the unit.
	 * @return this.
	 * @throws NullPointerException if the given {@code unit} is null.
	 * @since 0.3.0 ~2021.07.05
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	public Event setUnit(@NotNull Unit unit) {
		Objects.requireNonNull(unit, "unit");
		this.unit = unit;
		if (this.environment == null)
			this.setEnvironment(unit.getEnvironment());
		return this;
	}
}
