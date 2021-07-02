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

import org.jamplate.function.Compiler;
import org.jamplate.function.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

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
 * #addFirst(Spec)}, {@link #addBefore(Spec, Spec)}, {@link #addLast(Spec)}, {@link
 * #addAfter(Spec, Spec)}.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.16
 */
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
	 *
	 * @param spec the spec to be added.
	 * @return this.
	 * @throws NullPointerException          if the given {@code spec} is null.
	 * @throws UnsupportedOperationException if this spec does not allow sub-specs.
	 * @implSpec this implementation will delegate to {@link #addLast(Spec)}.
	 * @since 0.3.0 ~2021.06.19
	 */
	@Contract(value = "_->this", mutates = "this")
	default Spec add(@NotNull Spec spec) {
		return this.addLast(spec);
	}

	/**
	 * Add the given {@code spec} as a subspec to this spec. The spec will be added after
	 * the given {@code ref} spec.
	 *
	 * @param ref  the spec for the given {@code spec} to be added after it.
	 * @param spec the spec to be added.
	 * @return this.
	 * @throws NullPointerException          if the given {@code spec} is null.
	 * @throws IllegalArgumentException      if the given {@code ref} spec is not
	 *                                       currently a subspec in this spec.
	 * @throws UnsupportedOperationException if this spec does not allow sub-specs.
	 * @implSpec this implementation will throw {@link UnsupportedOperationException}
	 * 		immediately.
	 * @since 0.3.0 ~2021.06.19
	 */
	@Contract(value = "_,_->this", mutates = "this")
	default Spec addAfter(@NotNull Spec ref, @NotNull Spec spec) {
		throw new UnsupportedOperationException("addSpecAfter");
	}

	/**
	 * Add the given {@code spec} as a subspec to this spec. The spec will be added before
	 * the given {@code ref} spec.
	 *
	 * @param ref  the spec for the given {@code spec} to be added before it.
	 * @param spec the spec to be added.
	 * @return this.
	 * @throws NullPointerException          if the given {@code spec} is null.
	 * @throws IllegalArgumentException      if the given {@code ref} spec is not
	 *                                       currently a subspec in this spec.
	 * @throws UnsupportedOperationException if this spec does not allow sub-specs.
	 * @implSpec this implementation will throw {@link UnsupportedOperationException}
	 * 		immediately.
	 * @since 0.3.0 ~2021.06.19
	 */
	@Contract(value = "_,_->this", mutates = "this")
	default Spec addBefore(@NotNull Spec ref, @NotNull Spec spec) {
		throw new UnsupportedOperationException("addSpecBefore");
	}

	/**
	 * Add the given {@code spec} as a subspec to this spec. The spec will be added at the
	 * start.
	 *
	 * @param spec the spec to be added.
	 * @return this.
	 * @throws NullPointerException          if the given {@code spec} is null.
	 * @throws UnsupportedOperationException if this spec does not allow sub-specs.
	 * @implSpec this implementation will throw {@link UnsupportedOperationException}
	 * 		immediately.
	 * @since 0.3.0 ~2021.06.19
	 */
	@Contract(value = "_->this", mutates = "this")
	default Spec addFirst(@NotNull Spec spec) {
		throw new UnsupportedOperationException("addSpecFirst");
	}

	/**
	 * Add the given {@code spec} as a subspec to this spec. The spec will be added at the
	 * end.
	 *
	 * @param spec the spec to be added.
	 * @return this.
	 * @throws NullPointerException          if the given {@code spec} is null.
	 * @throws UnsupportedOperationException if this spec does not allow sub-specs.
	 * @implSpec this implementation will throw {@link UnsupportedOperationException}
	 * 		immediately.
	 * @since 0.3.0 ~2021.06.19
	 */
	@Contract(value = "_->this", mutates = "this")
	default Spec addLast(@NotNull Spec spec) {
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
	 * Return the initializer that must be executed at the initializing stage to apply
	 * this specification.
	 *
	 * @return the initializer to apply this spec.
	 * @since 0.3.0 ~2021.07.02
	 */
	@NotNull
	@Contract(pure = true)
	default Initializer getInitializer() {
		return Initializer.IDLE;
	}

	/**
	 * Return the listener of this spec.
	 *
	 * @return the listener of this spec.
	 * @since 0.3.0 ~2021.07.02
	 */
	@NotNull
	@Contract(pure = true)
	default Listener getListener() {
		return Listener.IDLE;
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
	 * Remove the given subspec {@code spec} from this spec.
	 *
	 * @param spec the subspec to be removed.
	 * @return this.
	 * @throws NullPointerException if the given {@code spec} is null.
	 * @implSpec this implementation uses {@link #iterator()} to remove the spec.
	 * @since 0.3.0 ~2021.06.19
	 */
	@Contract(value = "_->this", mutates = "this")
	default Spec remove(@NotNull Spec spec) {
		Objects.requireNonNull(spec, "spec");
		Iterator<Spec> iterator = this.iterator();
		while (iterator.hasNext())
			if (iterator.next() == spec)
				iterator.remove();
		return this;
	}

	/**
	 * Set the pre-analyze processor of this spec to be the given {@code processor}.
	 *
	 * @param processor the new pre-analyze processor.
	 * @return this.
	 * @throws NullPointerException          if the given {@code processor} is null.
	 * @throws UnsupportedOperationException if this spec does not support changing its
	 *                                       pre-analyze processor.
	 * @since 0.3.0 ~2021.06.19
	 */
	@Contract(value = "_->this", mutates = "this")
	default Spec setAnalyzeProcessor(@NotNull Processor processor) {
		throw new UnsupportedOperationException("setAnalyzeProcessor");
	}

	/**
	 * Set the analyzer of this spec to be the given {@code analyzer}.
	 *
	 * @param analyzer the new analyzer.
	 * @return this.
	 * @throws NullPointerException          if the given {@code analyzer} is null.
	 * @throws UnsupportedOperationException if this spec does not support changing its
	 *                                       analyzer.
	 * @since 0.3.0 ~2021.06.19
	 */
	@Contract(value = "_->this", mutates = "this")
	default Spec setAnalyzer(@NotNull Analyzer analyzer) {
		throw new UnsupportedOperationException("setAnalyzer");
	}

	/**
	 * Set the pre-compile processor of this spec to be the given {@code processor}.
	 *
	 * @param processor the new pre-compile processor.
	 * @return this.
	 * @throws NullPointerException          if the given {@code processor} is null.
	 * @throws UnsupportedOperationException if this spec does not support changing its
	 *                                       pre-compile processor.
	 * @since 0.3.0 ~2021.06.19
	 */
	@Contract(value = "_->this", mutates = "this")
	default Spec setCompileProcessor(@NotNull Processor processor) {
		throw new UnsupportedOperationException("setCompileProcessor");
	}

	/**
	 * Set the compiler of this spec to be the given {@code compiler}.
	 *
	 * @param compiler the new compiler.
	 * @return this.
	 * @throws NullPointerException          if the given {@code compiler} is null.
	 * @throws UnsupportedOperationException if this spec does not support changing its
	 *                                       compiler.
	 * @since 0.3.0 ~2021.06.19
	 */
	@Contract(value = "_->this", mutates = "this")
	default Spec setCompiler(@NotNull Compiler compiler) {
		throw new UnsupportedOperationException("setCompiler");
	}

	/**
	 * Set the initializer of this spec to be the given {@code initializer}.
	 *
	 * @param initializer the new initializer.
	 * @return this.
	 * @throws NullPointerException          if the given {@code initializer} is null.
	 * @throws UnsupportedOperationException if this spec does not support changing its
	 *                                       initializer.
	 * @since 0.3.0 ~2021.07.02
	 */
	@Contract(value = "_->this", mutates = "this")
	default Spec setInitializer(@NotNull Initializer initializer) {
		throw new UnsupportedOperationException("setInitializer");
	}

	/**
	 * Set the listener of this spec to be the given {@code listener}.
	 *
	 * @param listener the new listener.
	 * @return this.
	 * @throws NullPointerException          if the given {@code listener} is null.
	 * @throws UnsupportedOperationException if this spec does not support changing its
	 *                                       listener.
	 * @since 0.3.0 ~2021.07.02
	 */
	@Contract(value = "_->this", mutates = "this")
	default Spec setListener(@NotNull Listener listener) {
		throw new UnsupportedOperationException("setListener");
	}

	/**
	 * Set the pre-parse processor of this spec to be the given {@code processor}.
	 *
	 * @param processor the new pre-parse processor.
	 * @return this.
	 * @throws NullPointerException          if the given {@code processor} is null.
	 * @throws UnsupportedOperationException if this spec does not support changing its
	 *                                       pre-parse processor.
	 * @since 0.3.0 ~2021.06.19
	 */
	@Contract(value = "_->this", mutates = "this")
	default Spec setParseProcessor(@NotNull Processor processor) {
		throw new UnsupportedOperationException("setParseProcessor");
	}

	/**
	 * Set the parser of this spec to be the given {@code parser}.
	 *
	 * @param parser the new parser.
	 * @return this.
	 * @throws NullPointerException          if the given {@code parser} is null.
	 * @throws UnsupportedOperationException if this spec does not support changing its
	 *                                       parser.
	 * @since 0.3.0 ~2021.06.19
	 */
	@Contract(value = "_->this", mutates = "this")
	default Spec setParser(@NotNull Parser parser) {
		throw new UnsupportedOperationException("setParser");
	}

	/**
	 * Set the qualified name of this spec to be the given {@code name}.
	 *
	 * @param name the new qualified name.
	 * @return this.
	 * @throws NullPointerException          if the given {@code name} is null.
	 * @throws UnsupportedOperationException if this spec does not support changing its
	 *                                       qualified name.
	 * @since 0.3.0 ~2021.06.19
	 */
	@Contract(value = "_->this", mutates = "this")
	default Spec setQualifiedName(@NotNull String name) {
		throw new UnsupportedOperationException("setQualifiedName");
	}
}
