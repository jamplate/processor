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

import org.jamplate.diagnostic.Message;
import org.jamplate.function.Analyzer;
import org.jamplate.function.Compiler;
import org.jamplate.function.Parser;
import org.jamplate.function.Processor;
import org.jamplate.model.Compilation;
import org.jamplate.model.Environment;
import org.jamplate.model.Memory;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;

/**
 * A spec is a unit containing functions necessary to apply a syntax/runtime
 * specification.
 * <br>
 * Every spec can have sub-specs that it enclosing it. Every spec must specify how the
 * sub-specs are treated inside it. Otherwise, the spec must not support sub-specs.
 * <br>
 * Other than registering sub-specs, the user of a spec must not query nor modify the
 * sub-specs since the spec already delegates to the sub-specs its own way.
 * <br>
 * For a spec to support sub-specs, it must implement the method {@link #iterator()}.
 * <br>
 * For a spec to support variable sub-specs, it must implement the methods {@link
 * #addSpecFirst(Spec)}, {@link #addSpecBefore(Spec, Spec)}, {@link #addSpecLast(Spec)},
 * {@link #addSpecAfter(Spec, Spec)}.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.16
 */
@SuppressWarnings("ClassWithTooManyMethods")
public interface Spec extends Iterable<Spec> {
	/**
	 * Return an iterator iterating over the subspecs in this spec.
	 *
	 * @return an iterator iterating over the subspecs in this spec.
	 * @implSpec this implementation returns an empty iterator.
	 * @since 0.3.0 ~2021.06.19
	 */
	@NotNull
	@Contract(value = "->new", pure = true)
	@Override
	default Iterator<Spec> iterator() {
		return Collections.emptyIterator();
	}

	/**
	 * Add the given {@code spec} as a subspec to this spec. The spec will be added at an
	 * unspecified order.
	 * <br>
	 * If the spec is already added to this spec, this method call will be ignored.
	 *
	 * @param spec the spec to be added.
	 * @return true, if the spec was not already a subspec in this spec.
	 * @throws NullPointerException          if the given {@code spec} is null.
	 * @throws UnsupportedOperationException if this spec does not allow sub-specs.
	 * @implSpec this implementation will delegate to {@link #addSpecLast(Spec)}.
	 * @since 0.3.0 ~2021.06.19
	 */
	@Contract(mutates = "this")
	default boolean addSpec(@NotNull Spec spec) {
		return this.addSpecLast(spec);
	}

	/**
	 * Add the given {@code spec} as a subspec to this spec. The spec will be added after
	 * the given {@code ref} spec.
	 * <br>
	 * If the spec is already added to this spec, this method call will be ignored.
	 *
	 * @param ref  the spec for the given {@code spec} to be added after it.
	 * @param spec the spec to be added.
	 * @return true, if the spec was not already a subspec in this spec.
	 * @throws NullPointerException          if the given {@code spec} is null.
	 * @throws IllegalArgumentException      if the given {@code ref} spec is not
	 *                                       currently a subspec in this spec.
	 * @throws UnsupportedOperationException if this spec does not allow sub-specs.
	 * @implSpec this implementation will throw {@link UnsupportedOperationException}
	 * 		immediately.
	 * @since 0.3.0 ~2021.06.19
	 */
	@Contract(mutates = "this")
	default boolean addSpecAfter(@NotNull Spec ref, @NotNull Spec spec) {
		throw new UnsupportedOperationException("addSpecAfter");
	}

	/**
	 * Add the given {@code spec} as a subspec to this spec. The spec will be added before
	 * the given {@code ref} spec.
	 * <br>
	 * If the spec is already added to this spec, this method call will be ignored.
	 *
	 * @param ref  the spec for the given {@code spec} to be added before it.
	 * @param spec the spec to be added.
	 * @return true, if the spec was not already a subspec in this spec.
	 * @throws NullPointerException          if the given {@code spec} is null.
	 * @throws IllegalArgumentException      if the given {@code ref} spec is not
	 *                                       currently a subspec in this spec.
	 * @throws UnsupportedOperationException if this spec does not allow sub-specs.
	 * @implSpec this implementation will throw {@link UnsupportedOperationException}
	 * 		immediately.
	 * @since 0.3.0 ~2021.06.19
	 */
	@Contract(mutates = "this")
	default boolean addSpecBefore(@NotNull Spec ref, @NotNull Spec spec) {
		throw new UnsupportedOperationException("addSpecBefore");
	}

	/**
	 * Add the given {@code spec} as a subspec to this spec. The spec will be added at the
	 * start.
	 * <br>
	 * If the spec is already added to this spec, this method call will be ignored.
	 *
	 * @param spec the spec to be added.
	 * @return true, if the spec was not already a subspec in this spec.
	 * @throws NullPointerException          if the given {@code spec} is null.
	 * @throws UnsupportedOperationException if this spec does not allow sub-specs.
	 * @implSpec this implementation will throw {@link UnsupportedOperationException}
	 * 		immediately.
	 * @since 0.3.0 ~2021.06.19
	 */
	@Contract(mutates = "this")
	default boolean addSpecFirst(@NotNull Spec spec) {
		throw new UnsupportedOperationException("addSpecFirst");
	}

	/**
	 * Add the given {@code spec} as a subspec to this spec. The spec will be added at the
	 * end.
	 * <br>
	 * If the spec is already added to this spec, this method call will be ignored.
	 *
	 * @param spec the spec to be added.
	 * @return true, if the spec was not already a subspec in this spec.
	 * @throws NullPointerException          if the given {@code spec} is null.
	 * @throws UnsupportedOperationException if this spec does not allow sub-specs.
	 * @implSpec this implementation will throw {@link UnsupportedOperationException}
	 * 		immediately.
	 * @since 0.3.0 ~2021.06.19
	 */
	@Contract(mutates = "this")
	default boolean addSpecLast(@NotNull Spec spec) {
		throw new UnsupportedOperationException("addSpecLast");
	}

	/**
	 * Return the processor that must be executed before the analyzing stage to apply this
	 * specification.
	 *
	 * @return the pre-analyzing processor to apply this spec.
	 * @since 0.3.0 ~2021.06.19
	 */
	@NotNull
	@Contract(pure = true)
	default Processor getAnalyzeProcessor() {
		return Processor.IDLE;
	}

	/**
	 * Return the analyzer that must be executed at the analyzing stage to apply this
	 * specification.
	 *
	 * @return the analyzer to apply this spec.
	 * @since 0.3.0 ~2021.06.19
	 */
	@NotNull
	@Contract(pure = true)
	default Analyzer getAnalyzer() {
		return Analyzer.IDLE;
	}

	/**
	 * Return the processor that must be executed before the compiling stage to apply this
	 * specification.
	 *
	 * @return the pre-compiling processor to apply this spec.
	 * @since 0.3.0 ~2021.06.19
	 */
	@NotNull
	@Contract(pure = true)
	default Processor getCompileProcessor() {
		return Processor.IDLE;
	}

	/**
	 * Return the analyzer that must be executed at the compiling stage to apply this
	 * specification.
	 *
	 * @return the compiler to apply this spec.
	 * @since 0.3.0 ~2021.06.19
	 */
	@NotNull
	@Contract(pure = true)
	default Compiler getCompiler() {
		return Compiler.IDLE;
	}

	/**
	 * Return the processor that must be executed before the parsing stage to apply this
	 * specification.
	 *
	 * @return the pre-parsing processor to apply this spec.
	 * @since 0.3.0 ~2021.06.19
	 */
	@NotNull
	@Contract(pure = true)
	default Processor getParseProcessor() {
		return Processor.IDLE;
	}

	/**
	 * Return the parser that must be executed at the parsing stage to apply this
	 * specification.
	 *
	 * @return the parser to apply this spec.
	 * @since 0.3.0 ~2021.06.19
	 */
	@NotNull
	@Contract(pure = true)
	default Parser getParser() {
		return Parser.IDLE;
	}

	/**
	 * Return the qualified name of this spec. The qualified name of a spec is the string
	 * that identifies the spec from other specs.
	 *
	 * @return the qualified name of this spec. Or an empty string if this spec is not
	 * 		qualified.
	 * @since 0.3.0 ~2021.06.19
	 */
	@NotNull
	@Contract(pure = true)
	default String getQualifiedName() {
		return "Spec" + this.hashCode();
	}

	/**
	 * Return {@code true} if this spec has the given subspec {@code spec}.
	 *
	 * @param spec the subspec to check if in this spec.
	 * @return true, if this spec has the given {@code spec} as a subspec.
	 * @throws NullPointerException if the given {@code spec} is null.
	 * @implSpec this implementation iterate using {@link #iterator()} and checks by
	 * 		identity.
	 * @since 0.3.0 ~2021.06.19
	 */
	@Contract(pure = true)
	default boolean hasSpec(@NotNull Spec spec) {
		Objects.requireNonNull(spec, "spec");
		for (Spec value : this)
			if (value == spec)
				return true;
		return false;
	}

	/**
	 * A method that get invoked after the compilation get created for a document at a
	 * unit where this spec is applied.
	 *
	 * @param unit        the unit where this spec is applied. (optional)
	 * @param compilation the created compilation.
	 * @throws NullPointerException if the given {@code compilation} is null.
	 * @since 0.3.0 ~2021.06.22
	 */
	@Contract(mutates = "param")
	default void onCreateCompilation(@Nullable Unit unit, @NotNull Compilation compilation) {
	}

	/**
	 * A method that get invoked when creating the memory before executing an instruction
	 * at the unit where this spec is applied.
	 *
	 * @param compilation the compilation its instruction to be executed.
	 * @param memory      the memory to be used when executing the instruction of the
	 *                    given {@code compilation}.
	 * @throws NullPointerException  if the given {@code compilation} or {@code memory} is
	 *                               null.
	 * @throws IllegalStateException if the given {@code compilation} has no instruction
	 *                               set to it.
	 * @since 0.3.0 ~2021.06.19
	 */
	@Contract(mutates = "param1,param2")
	default void onCreateMemory(@NotNull Compilation compilation, @NotNull Memory memory) {
	}

	/**
	 * A method that get invoked before destroying the memory after executing an
	 * instruction at a unit where this spec is applied.
	 *
	 * @param compilation the compilation its instruction got executed.
	 * @param memory      the memory that has been used to executed the instruction.
	 * @throws NullPointerException if the given {@code compilation} or {@code memory} is
	 *                              null.
	 * @since 0.3.0 ~2021.06.22
	 */
	@Contract(mutates = "param1,param2")
	default void onDestroyMemory(@NotNull Compilation compilation, @NotNull Memory memory) {
	}

	/**
	 * A method that get invoked when a handled error occurred at the unit where this spec
	 * is applied.
	 *
	 * @param environment the environment where the error occurred.
	 * @param message     the message of the error.
	 * @throws NullPointerException if the given {@code environment} or {@code message} is
	 *                              null.
	 * @since 0.3.0 ~2021.06.19
	 */
	@Contract(mutates = "param1")
	default void onDiagnostic(@NotNull Environment environment, @NotNull Message message) {
	}

	/**
	 * A method that get invoked when the user requests to optimize the given {@code
	 * compilation}.
	 *
	 * @param compilation the compilation to be optimized.
	 * @param mode        the optimization mode. Negative for extreme optimizations.
	 * @throws NullPointerException if the given {@code compilation} is null.
	 * @since 0.3.0 ~2021.07.01
	 */
	@Contract(mutates = "param1")
	default void onOptimize(@NotNull Compilation compilation, int mode) {
	}

	/**
	 * Remove the given subspec {@code spec} from this spec.
	 *
	 * @param spec the subspec to be removed.
	 * @return true, if the given {@code spec} was a subspec in this spec.
	 * @throws NullPointerException if the given {@code spec} is null.
	 * @implSpec this implementation uses {@link #iterator()} to remove the spec.
	 * @since 0.3.0 ~2021.06.19
	 */
	@Contract(mutates = "this")
	default boolean removeSpec(@NotNull Spec spec) {
		Objects.requireNonNull(spec, "spec");
		Iterator<Spec> iterator = this.iterator();
		while (iterator.hasNext())
			if (iterator.next() == spec) {
				iterator.remove();
				return true;
			}
		return false;
	}

	/**
	 * Set the pre-analyze processor of this spec to be the given {@code processor}.
	 *
	 * @param processor the new pre-analyze processor.
	 * @throws NullPointerException          if the given {@code processor} is null.
	 * @throws UnsupportedOperationException if this spec does not support changing its
	 *                                       pre-analyze processor.
	 * @since 0.3.0 ~2021.06.19
	 */
	@Contract(mutates = "this")
	default void setAnalyzeProcessor(@NotNull Processor processor) {
		throw new UnsupportedOperationException("setAnalyzeProcessor");
	}

	/**
	 * Set the analyzer of this spec to be the given {@code analyzer}.
	 *
	 * @param analyzer the new analyzer.
	 * @throws NullPointerException          if the given {@code analyzer} is null.
	 * @throws UnsupportedOperationException if this spec does not support changing its
	 *                                       analyzer.
	 * @since 0.3.0 ~2021.06.19
	 */
	@Contract(mutates = "this")
	default void setAnalyzer(@NotNull Analyzer analyzer) {
		throw new UnsupportedOperationException("setAnalyzer");
	}

	/**
	 * Set the pre-compile processor of this spec to be the given {@code processor}.
	 *
	 * @param processor the new pre-compile processor.
	 * @throws NullPointerException          if the given {@code processor} is null.
	 * @throws UnsupportedOperationException if this spec does not support changing its
	 *                                       pre-compile processor.
	 * @since 0.3.0 ~2021.06.19
	 */
	@Contract(mutates = "this")
	default void setCompileProcessor(@NotNull Processor processor) {
		throw new UnsupportedOperationException("setCompileProcessor");
	}

	/**
	 * Set the compiler of this spec to be the given {@code compiler}.
	 *
	 * @param compiler the new compiler.
	 * @throws NullPointerException          if the given {@code compiler} is null.
	 * @throws UnsupportedOperationException if this spec does not support changing its
	 *                                       compiler.
	 * @since 0.3.0 ~2021.06.19
	 */
	@Contract(mutates = "this")
	default void setCompiler(@NotNull Compiler compiler) {
		throw new UnsupportedOperationException("setCompiler");
	}

	/**
	 * Set the pre-parse processor of this spec to be the given {@code processor}.
	 *
	 * @param processor the new pre-parse processor.
	 * @throws NullPointerException          if the given {@code processor} is null.
	 * @throws UnsupportedOperationException if this spec does not support changing its
	 *                                       pre-parse processor.
	 * @since 0.3.0 ~2021.06.19
	 */
	@Contract(mutates = "this")
	default void setParseProcessor(@NotNull Processor processor) {
		throw new UnsupportedOperationException("setParseProcessor");
	}

	/**
	 * Set the parser of this spec to be the given {@code parser}.
	 *
	 * @param parser the new parser.
	 * @throws NullPointerException          if the given {@code parser} is null.
	 * @throws UnsupportedOperationException if this spec does not support changing its
	 *                                       parser.
	 * @since 0.3.0 ~2021.06.19
	 */
	@Contract(mutates = "this")
	default void setParser(@NotNull Parser parser) {
		throw new UnsupportedOperationException("setParser");
	}

	/**
	 * Set the qualified name of this spec to be the given {@code name}.
	 *
	 * @param name the new qualified name.
	 * @throws NullPointerException          if the given {@code name} is null.
	 * @throws UnsupportedOperationException if this spec does not support changing its
	 *                                       qualified name.
	 * @since 0.3.0 ~2021.06.19
	 */
	@Contract(mutates = "this")
	default void setQualifiedName(@NotNull String name) {
		throw new UnsupportedOperationException("setQualifiedName");
	}
}
