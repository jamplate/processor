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

import org.jamplate.diagnostic.Diagnostic;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Map;

/**
 * The environment is a unit holding all the data, managers and variables about a single
 * process.
 * <br>
 * The subclasses must support serialization.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.16
 */
public interface Environment extends Serializable {
	/**
	 * Get the compilation registered in this environment with the document qualified with
	 * the given {@code name} or return {@code null} if no such compilation has been set
	 * in this environment.
	 *
	 * @param name the qualification name of the document of the compilation to be
	 *             returned.
	 * @return the compilation with the given {@code name} set to this environment. Or
	 *        {@code null} if no such compilation exist.
	 * @throws NullPointerException if the given {@code name} is null.
	 * @since 0.2.0 ~2021.05.21
	 */
	@Nullable
	@Contract(pure = true)
	Compilation getCompilation(@NotNull String name);

	/**
	 * Get the compilation set to this environment with the given {@code document} or
	 * return {@code null} if no such compilation exist in this environment.
	 *
	 * @param document the document of the compilation to be returned.
	 * @return the compilation with the given {@code document} in this environment. Or
	 *        {@code null} if no such compilation exist.
	 * @throws NullPointerException if the given {@code document} is null.
	 * @since 0.2.0 ~2021.05.21
	 */
	@Nullable
	@Contract(pure = true)
	Compilation getCompilation(@NotNull Document document);

	/**
	 * Return the diagnostic manager in this environment.
	 *
	 * @return the diagnostic manager.
	 * @since 0.2.0 ~2021.05.17
	 */
	@NotNull
	@Contract(pure = true)
	Diagnostic getDiagnostic();

	/**
	 * Returns the meta-data map of this environment.
	 * <br>
	 * By default, the returned map will be a modifiable checked map. Unless, the class of
	 * this said otherwise.
	 *
	 * @return the meta-data map of this.
	 * @since 0.2.0 ~2021.05.21
	 */
	@NotNull
	@Contract(pure = true)
	Map<String, Object> getMeta();

	/**
	 * Return the compilation set to this environment with the given {@code document} or
	 * create a new compilation for the given {@code document}, add it to this environment
	 * and return it.
	 *
	 * @param document the document to get a compilation for.
	 * @return the compilation with the given {@code document} in this environment or a
	 * 		newly created, then added compilation for the given {@code document}.
	 * @throws NullPointerException if the given {@code document} is null.
	 * @since 0.2.0 ~2021.05.23
	 */
	@NotNull
	@Contract(mutates = "this")
	Compilation optCompilation(@NotNull Document document);

	/**
	 * Associate the given {@code compilation} to the given {@code document} in this
	 * environment.
	 *
	 * @param document    the document to be associated to the given {@code compilation}.
	 * @param compilation the compilation.
	 * @throws NullPointerException if the given {@code document} or {@code compilation}
	 *                              is null.
	 * @since 0.2.0 ~2021.05.19
	 */
	@Contract(mutates = "this")
	void setCompilation(@NotNull Document document, @NotNull Compilation compilation);
}
//	//gate
//
//	/**
//	 * Compile the given {@code compilation} using the compiler of this environment. If
//	 * the given {@code compilation} was already compiled, then the results of the
//	 * previous compile will be returned. Otherwise, the given {@code compilation} will be
//	 * compiled.
//	 *
//	 * @param compilation the compilation to be compiled
//	 * @return the results of compiling the given {@code compilation}.
//	 * @throws NullPointerException if the given {@code compilation} is null.
//	 * @throws CompilationException if failed to compile the given {@code compilation}.
//	 * @since 0.2.0 ~2021.05.21
//	 */
//	@NotNull
//	@Contract(mutates = "this")
//	Instruction compile(@NotNull Compilation compilation);
//
//	/**
//	 * If this environment has an already parsed compilation for the given {@code
//	 * document}. Then this method will return that compilation. Otherwise, this method
//	 * will created a new compilation for the given {@code document}, add it to this
//	 * environment, parse it using the parser set to this compilation, then return it.
//	 *
//	 * @param document the document to be parsed.
//	 * @return the compilation from parsing the given {@code document}.
//	 * @throws NullPointerException if the given {@code document} is null.
//	 * @throws IllegalTreeException if failed to parse the given {@code document}.
//	 * @since 0.2.0 ~2021.05.19
//	 */
//	@NotNull
//	@Contract(mutates = "this")
//	Compilation parse(@NotNull Document document);

//functions
//
//	/**
//	 * Return the compiler used by this environment.
//	 *
//	 * @return the compiler used by this environment.
//	 * @since 0.2.0 ~2021.05.21
//	 */
//	@NotNull
//	@Contract(pure = true)
//	Compiler getCompiler();
//
//	/**
//	 * Set the compiler of this environment to be the given {@code compiler}.
//	 *
//	 * @param compiler the compiler to be used in this environment.
//	 * @throws NullPointerException if the given {@code compiler} is null.
//	 * @since 0.2.0 ~2021.05.21
//	 */
//	@Contract(mutates = "this")
//	void setCompiler(@NotNull Compiler compiler);
//
//	/**
//	 * Get the parser used by this environment.
//	 *
//	 * @return the parser used by this environment.
//	 * @since 0.2.0 ~2021.05.21
//	 */
//	@NotNull
//	@Contract(pure = true)
//	Parser getParser();
//
//	/**
//	 * Set the parser used by this environment to be the given {@code parser}.
//	 *
//	 * @param parser the parser to be set.
//	 * @throws NullPointerException if the given {@code parser} is null.
//	 * @since 0.2.0 ~2021.05.21
//	 */
//	@Contract(mutates = "this")
//	void setParser(@NotNull Parser parser);

//getter `instruction`
//
//	/**
//	 * Return the instruction compiled by this environment from a compilation that its
//	 * document qualified by the given {@code name}.
//	 *
//	 * @param name the qualification of the document of the compilation to get the
//	 *             resultant instruction from compiling it.
//	 * @return the instruction compiled by this from a compilation with the given {@code
//	 * 		name}. Or {@code null} if the no such compilation has being compiled by this.
//	 * @throws NullPointerException if the given {@code name} is null.
//	 * @since 0.2.0 ~2021.05.21
//	 */
//	@Nullable
//	@Contract(pure = true)
//	Instruction getInstruction(@NotNull String name);
//
//	/**
//	 * Return the instruction compiled by this environment from a compilation with the
//	 * given {@code document}.
//	 *
//	 * @param document the document of the compilation to get the resultant instruction
//	 *                 from compiling it.
//	 * @return the instruction compiled by this from a compilation with the given {@code
//	 * 		document}. Or {@code null} if the no such compilation has being compiled by
//	 * 		this.
//	 * @throws NullPointerException if the given {@code document} is null.
//	 * @since 0.2.0 ~2021.05.21
//	 */
//	@Nullable
//	@Contract(pure = true)
//	Instruction getInstruction(@NotNull Document document);
//
//	/**
//	 * Return the instruction compiled by this environment from the given {@code
//	 * compilation}.
//	 *
//	 * @param compilation the compilation to get the resultant instruction from compiling
//	 *                    it.
//	 * @return the instruction compiled by this from the given {@code compilation}. Or
//	 *        {@code null} if the given {@code compilation} has not being compiled by this.
//	 * @throws NullPointerException if the given {@code compilation} is null.
//	 * @since 0.2.0 ~2021.05.21
//	 */
//	@Nullable
//	@Contract(pure = true)
//	Instruction getInstruction(@NotNull Compilation compilation);
//
//	//setter `instruction`
//
//	/**
//	 * Set the resultant instruction from compiling the given {@code compilation} to be
//	 * the given {@code compilation}.
//	 *
//	 * @param compilation the compilation to set its compiling result instruction.
//	 * @param instruction the instruction to be set.
//	 * @throws NullPointerException if the given {@code compilation} or {@code
//	 *                              instruction} is null.
//	 * @since 0.2.0 ~2021.05.21
//	 */
//	@Contract(mutates = "this")
//	void setInstruction(@NotNull Compilation compilation, @NotNull Instruction instruction);
