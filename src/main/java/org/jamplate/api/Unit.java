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

import org.jamplate.model.Compilation;
import org.jamplate.model.Document;
import org.jamplate.model.Environment;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

/**
 * A gate unit that holds the variables necessary to do any jamplate task.
 * <br><br>
 * <strong>Members</strong>
 * <ul>
 *     <li>spec: {@link Spec}</li>
 *     <li>environment: {@link Environment}</li>
 * </ul>
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.19
 */
public interface Unit {
	/**
	 * Analyze the compilations of the given {@code documents} in the environment set for
	 * this unit using the analyzer of the spec set for this unit.
	 * <br><br>
	 * Null documents are ignored.
	 *
	 * @param documents the documents of the compilations to be analyzed.
	 * @return true, if the operation was successfully executed.
	 * @throws NullPointerException  if the given {@code documents} is null.
	 * @throws IllegalStateException if no compilation was associated to a document in the
	 *                               given {@code document}.
	 * @since 0.3.0 ~2021.07.10
	 */
	@Contract(mutates = "this")
	default boolean analyze(@Nullable Document @NotNull ... documents) {
		Objects.requireNonNull(documents, "documents");
		boolean success = true;

		for (Document document : documents)
			if (document != null)
				success &= this.analyze(document);

		return success;
	}

	/**
	 * Analyze the compilations of the given {@code documents} in the environment set for
	 * this unit using the analyzer of the spec set for this unit.
	 * <br><br>
	 * Null documents are ignored.
	 *
	 * @param documents the documents of the compilations to be analyzed.
	 * @return true, if the operation was successfully executed.
	 * @throws NullPointerException  if the given {@code documents} is null.
	 * @throws IllegalStateException if no compilation was associated to a document in the
	 *                               given {@code document}.
	 * @since 0.3.0 ~2021.07.10
	 */
	@Contract(mutates = "this")
	default boolean analyze(@NotNull List<Document> documents) {
		Objects.requireNonNull(documents, "documents");
		boolean success = true;

		for (Document document : documents)
			if (document != null)
				success &= this.analyze(document);

		return success;
	}

	/**
	 * Analyze all the compilations in the environment set for this unit using the
	 * analyzer of the spec set for this unit.
	 *
	 * @return true, if the operation was successfully executed.
	 * @since 0.3.0 ~2021.07.10
	 */
	@Contract(mutates = "this")
	default boolean analyzeAll() {
		boolean success = true;

		for (Compilation compilation : this.getEnvironment())
			success &= this.analyze(compilation.getRootTree().getDocument());

		return success;
	}

	/**
	 * Compile the compilations of the given {@code documents} in the environment set for
	 * this unit using the compiler of the spec set for this unit.
	 * <br><br>
	 * Null documents are ignored.
	 *
	 * @param documents the documents of the compilations to be compiled.
	 * @return true, if the operation was successfully executed.
	 * @throws NullPointerException  if the given {@code documents} is null.
	 * @throws IllegalStateException if no compilation was associated to a document in the
	 *                               given {@code document}.
	 * @since 0.3.0 ~2021.07.10
	 */
	@Contract(mutates = "this")
	default boolean compile(@Nullable Document @NotNull ... documents) {
		Objects.requireNonNull(documents, "documents");
		boolean success = true;

		for (Document document : documents)
			if (document != null)
				success &= this.compile(document);

		return success;
	}

	/**
	 * Compile the compilations of the given {@code documents} in the environment set for
	 * this unit using the compiler of the spec set for this unit.
	 * <br><br>
	 * Null documents are ignored.
	 *
	 * @param documents the documents of the compilations to be compiled.
	 * @return true, if the operation was successfully executed.
	 * @throws NullPointerException  if the given {@code documents} is null.
	 * @throws IllegalStateException if no compilation was associated to a document in the
	 *                               given {@code document}.
	 * @since 0.3.0 ~2021.07.10
	 */
	@Contract(mutates = "this")
	default boolean compile(@NotNull List<Document> documents) {
		Objects.requireNonNull(documents, "documents");
		boolean success = true;

		for (Document document : documents)
			if (document != null)
				success &= this.compile(document);

		return success;
	}

	/**
	 * Compile all the compilations in the environment set for this unit using the
	 * compiler of the spec set for this unit.
	 *
	 * @return true, if the operation was successfully executed.
	 * @since 0.3.0 ~2021.07.10
	 */
	@Contract(mutates = "this")
	default boolean compileAll() {
		boolean success = true;

		for (Compilation compilation : this.getEnvironment())
			success &= this.compile(compilation.getRootTree().getDocument());

		return success;
	}

	/**
	 * Execute the compilations of the given {@code documents} in the environment set for
	 * this unit.
	 * <br><br>
	 * Null documents are ignored.
	 *
	 * @param documents the documents of the compilations to be executed.
	 * @return true, if the operation was successfully executed.
	 * @throws NullPointerException  if the given {@code documents} is null.
	 * @throws IllegalStateException if no compilation was associated to a document in the
	 *                               given {@code document} or if an associated
	 *                               compilation does not have an instruction set to it.
	 * @since 0.3.0 ~2021.07.10
	 */
	@Contract(mutates = "this")
	default boolean execute(@Nullable Document @NotNull ... documents) {
		Objects.requireNonNull(documents, "documents");
		boolean success = true;

		for (Document document : documents)
			if (document != null)
				success &= this.execute(document);

		return success;
	}

	/**
	 * Execute the compilations of the given {@code documents} in the environment set for
	 * this unit.
	 * <br><br>
	 * Null documents are ignored.
	 *
	 * @param documents the documents of the compilations to be executed.
	 * @return true, if the operation was successfully executed.
	 * @throws NullPointerException  if the given {@code documents} is null.
	 * @throws IllegalStateException if no compilation was associated to a document in the
	 *                               given {@code document} or if an associated
	 *                               compilation does not have an instruction set to it.
	 * @since 0.3.0 ~2021.07.10
	 */
	@Contract(mutates = "this")
	default boolean execute(@NotNull List<Document> documents) {
		Objects.requireNonNull(documents, "documents");
		boolean success = true;

		for (Document document : documents)
			if (document != null)
				success &= this.execute(document);

		return success;
	}

	/**
	 * Execute all the compilations in the environment set for this unit.
	 *
	 * @return true, if the operation was successfully executed.
	 * @throws IllegalStateException if a compilation does not have an instruction set to
	 *                               it.
	 * @since 0.3.0 ~2021.07.10
	 */
	@Contract(mutates = "this")
	default boolean executeAll() {
		boolean success = true;

		for (Compilation compilation : this.getEnvironment())
			success &= this.execute(compilation.getRootTree().getDocument());

		return success;
	}

	/**
	 * Initialize compilations for the given {@code documents} in the environment set for
	 * this unit.
	 *
	 * @param documents the documents to initialize compilations for it in this unit.
	 * @return true, if the operation was successfully executed.
	 * @throws NullPointerException  if the given {@code documents} is null.
	 * @throws IllegalStateException if a document in the given {@code documents} already
	 *                               have a compilation associated to it.
	 * @since 0.3.0 ~2021.07.10
	 */
	@Contract(mutates = "this")
	default boolean initialize(@Nullable Document @NotNull ... documents) {
		Objects.requireNonNull(documents, "documents");
		boolean success = true;

		for (Document document : documents)
			if (document != null)
				success &= this.initialize(document);

		return success;
	}

	/**
	 * Initialize compilations for the given {@code documents} in the environment set for
	 * this unit.
	 *
	 * @param documents the documents to initialize compilations for it in this unit.
	 * @return true, if the operation was successfully executed.
	 * @throws NullPointerException  if the given {@code documents} is null.
	 * @throws IllegalStateException if a document in the given {@code documents} already
	 *                               have a compilation associated to it.
	 * @since 0.3.0 ~2021.07.10
	 */
	@Contract(mutates = "this")
	default boolean initialize(@NotNull List<Document> documents) {
		Objects.requireNonNull(documents, "documents");
		boolean success = true;

		for (Document document : documents)
			if (document != null)
				success &= this.initialize(document);

		return success;
	}

	/**
	 * Parse the compilations of the given {@code documents} in the environment set for
	 * this unit using the parser of the spec set for this unit.
	 * <br><br>
	 * Null documents are ignored.
	 *
	 * @param documents the documents of the compilations to be parsed.
	 * @return true, if the operation was successfully executed.
	 * @throws NullPointerException  if the given {@code documents} is null.
	 * @throws IllegalStateException if no compilation was associated to a document in the
	 *                               given {@code document}.
	 * @since 0.3.0 ~2021.07.10
	 */
	@Contract(mutates = "this")
	default boolean parse(@Nullable Document @NotNull ... documents) {
		Objects.requireNonNull(documents, "documents");
		boolean success = true;

		for (Document document : documents)
			if (document != null)
				success &= this.parse(document);

		return success;
	}

	/**
	 * Parse the compilations of the given {@code documents} in the environment set for
	 * this unit using the parser of the spec set for this unit.
	 * <br><br>
	 * Null documents are ignored.
	 *
	 * @param documents the documents of the compilations to be parsed.
	 * @return true, if the operation was successfully executed.
	 * @throws NullPointerException  if the given {@code documents} is null.
	 * @throws IllegalStateException if no compilation was associated to a document in the
	 *                               given {@code document}.
	 * @since 0.3.0 ~2021.07.10
	 */
	@Contract(mutates = "this")
	default boolean parse(@NotNull List<Document> documents) {
		Objects.requireNonNull(documents, "documents");
		boolean success = true;

		for (Document document : documents)
			if (document != null)
				success &= this.parse(document);

		return success;
	}

	/**
	 * Parse all the compilations in the environment set for this unit using the parser of
	 * the spec set for this unit.
	 *
	 * @return true, if the operation was successfully executed.
	 * @since 0.3.0 ~2021.07.10
	 */
	@Contract(mutates = "this")
	default boolean parseAll() {
		boolean success = true;

		for (Compilation compilation : this.getEnvironment())
			success &= this.parse(compilation.getRootTree().getDocument());

		return success;
	}

	/**
	 * Analyze the compilation of the given {@code document} in the environment set for
	 * this unit using the analyzer of the spec set for this unit.
	 *
	 * @param document the document to analyze the compilation associated to it in this
	 *                 unit.
	 * @return true, if the operation was successfully executed.
	 * @throws NullPointerException  if the given {@code document} is null.
	 * @throws IllegalStateException if no compilation was associated to the given {@code
	 *                               document}.
	 * @since 0.3.0 ~2021.06.19
	 */
	@Contract(mutates = "this")
	boolean analyze(@NotNull Document document);

	/**
	 * Compile the compilation of the given {@code document} in the environment set for
	 * this unit using the compiler of the spec set for this unit.
	 *
	 * @param document the document to compile the compilation associated to it in this
	 *                 unit.
	 * @return true, if the operation was successfully executed.
	 * @throws NullPointerException  if the given {@code document} is null.
	 * @throws IllegalStateException if no compilation was associated to the given {@code
	 *                               document}.
	 * @since 0.3.0 ~2021.06.19
	 */
	@Contract(mutates = "this")
	boolean compile(@NotNull Document document);

	/**
	 * Execute the diagnostic sequence. The diagnostic sequence is the process of
	 * outputting the messages printed to the diagnostic manager in the environment set
	 * for this unit.
	 *
	 * @since 0.3.0 ~2021.06.19
	 */
	void diagnostic();

	/**
	 * Execute the instruction of the compilation of the given {@code document} in the
	 * environment set for this unit.
	 *
	 * @param document the document to compile the compilation associated to it in this
	 *                 unit.
	 * @return true, if the operation was successfully executed.
	 * @throws NullPointerException  if the given {@code document} is null.
	 * @throws IllegalStateException if no compilation was associated to the given {@code
	 *                               document} or if the associated compilation does not
	 *                               have an instruction set to it.
	 * @since 0.3.0 ~2021.06.19
	 */
	@Contract(mutates = "this")
	boolean execute(@NotNull Document document);

	/**
	 * Return the environment set for this unit.
	 *
	 * @return the environment of this unit.
	 * @since 0.3.0 ~2021.06.19
	 */
	@NotNull
	@Contract(pure = true)
	Environment getEnvironment();

	/**
	 * Return the spec set for this unit.
	 *
	 * @return the spec of this unit.
	 * @since 0.3.0 ~2021.06.19
	 */
	@NotNull
	@Contract(pure = true)
	Spec getSpec();

	/**
	 * Initialize a compilation for the given {@code document} in the environment set for
	 * this unit.
	 *
	 * @param document the document to initialize a compilation for it in this unit.
	 * @return true, if the operation was successfully executed.
	 * @throws NullPointerException  if the given {@code document} is null.
	 * @throws IllegalStateException if the given {@code document} already have a
	 *                               compilation associated to it.
	 * @since 0.3.0 ~2021.06.19
	 */
	@Contract(mutates = "this")
	boolean initialize(@NotNull Document document);

	/**
	 * Optimize the the compilation of the given {@code document} in the environment set
	 * for this unit.
	 *
	 * @param document the document to optimize its compilation in this unit.
	 * @param mode     the optimization mode. negative for extreme optimization.
	 * @throws NullPointerException  if the given {@code document} is null.
	 * @throws IllegalStateException if no compilation was associated to the given {@code
	 *                               document}.
	 * @since 0.3.0 ~2021.07.01
	 */
	@Contract(mutates = "this")
	void optimize(@NotNull Document document, int mode);

	/**
	 * Parse the trees in the compilation of the given {@code document} in the environment
	 * set for this unit using the parser of the spec set for this unit.
	 *
	 * @param document the document to parse the trees in the compilation associated to it
	 *                 in this unit.
	 * @return true, if the operation was successfully executed.
	 * @throws NullPointerException  if the given {@code document} is null.
	 * @throws IllegalStateException if no compilation was associated to the given {@code
	 *                               document}.
	 * @since 0.3.0 ~2021.06.19
	 */
	@Contract(mutates = "this")
	boolean parse(@NotNull Document document);

	/**
	 * Set the environment of this unit to be the given {@code environment}.
	 *
	 * @param environment the new environment to be set.
	 * @throws NullPointerException if the given {@code environment} is null.
	 * @since 0.3.0 ~2021.06.19
	 */
	@Contract(mutates = "this")
	void setEnvironment(@NotNull Environment environment);

	/**
	 * Set the spec to be used by this unit.
	 *
	 * @param spec the new spec to be set.
	 * @throws NullPointerException if the given {@code spec} is null.
	 * @since 0.3.0 ~2021.06.19
	 */
	@Contract(mutates = "this")
	void setSpec(@NotNull Spec spec);
}
