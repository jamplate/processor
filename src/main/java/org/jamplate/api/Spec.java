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

import org.jamplate.function.Analyzer;
import org.jamplate.function.Compiler;
import org.jamplate.function.Parser;
import org.jamplate.function.Processor;
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
 * For a spec to support sub-specs, it must implement the methods {@link #iterator()},
 * {@link #removeSpec(Spec)} and {@link #hasSpec(Spec)}.
 * <br>
 * For a spec to support variable sub-specs, it must implement the methods {@link
 * #addSpecBefore(Spec)}, {@link #addSpecBefore(Spec, Spec)}, {@link #addSpecAfter(Spec)},
 * {@link #addSpecAfter(Spec, Spec)}.
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
	 * <br>
	 * If the spec is already added to this spec, this method call will be ignored.
	 *
	 * @param spec the spec to be added.
	 * @return true, if the spec was not already a subspec in this spec.
	 * @throws NullPointerException          if the given {@code spec} is null.
	 * @throws UnsupportedOperationException if this spec does not allow sub-specs.
	 * @implSpec this implementation will delegate to {@link #addSpecAfter(Spec)}.
	 * @since 0.3.0 ~2021.06.19
	 */
	@Contract(mutates = "this")
	default boolean addSpec(@NotNull Spec spec) {
		return this.addSpecAfter(spec);
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
	default boolean addSpecAfter(@NotNull Spec spec) {
		throw new UnsupportedOperationException("addSpec");
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
		throw new UnsupportedOperationException("addSpec");
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
	default boolean addSpecBefore(@NotNull Spec spec) {
		throw new UnsupportedOperationException("addSpec");
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
		throw new UnsupportedOperationException("addSpec");
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
		return compilation -> false;
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
		return (compilation, tree) -> false;
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
		return compilation -> false;
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
		return (compiler, compilation, tree) -> null;
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
		return compilation -> false;
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
		return (compilation, tree) -> Collections.emptySet();
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
}
