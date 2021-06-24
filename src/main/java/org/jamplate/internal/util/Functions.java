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
package org.jamplate.internal.util;

import org.jamplate.function.Analyzer;
import org.jamplate.function.Compiler;
import org.jamplate.function.Parser;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.ListIterator;
import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 * Useful tools for the package {@link org.jamplate.function}.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.22
 */
public final class Functions {
	/**
	 * Tool classes does not need instantiating.
	 *
	 * @throws AssertionError when called.
	 * @since 0.3.0 ~2021.06.24
	 */
	private Functions() {
		throw new AssertionError("No instance for you!");
	}

	/**
	 * Construct a new analyzer from executing the given {@code analyzers} functions from
	 * the last to the first with the argument being the result of invoking the previous
	 * function with the last function invoked with {@link Analyzer#IDLE}.
	 * <br>
	 * The following is a pseudo-code demonstration of how the product is computed.
	 * <pre>
	 *     product = function0(function1(function2(function3(...functionLast(Analyzer.IDLE)))))
	 * </pre>
	 * <br>
	 * Note: null operators in the given array will be completely ignored.
	 * <br>
	 * Note: an operator will be completely ignored when it returns null.
	 *
	 * @param analyzers the wrappers constructors.
	 * @return the final product.
	 * @throws NullPointerException if the given {@code analyzers} is null.
	 * @since 0.3.0 ~2021.06.24
	 */
	@SafeVarargs
	public static Analyzer analyzer(@NotNull UnaryOperator<Analyzer>... analyzers) {
		Objects.requireNonNull(analyzers, "analyzers");
		ListIterator<UnaryOperator<Analyzer>> iterator =
				Arrays.asList(analyzers)
					  .listIterator(analyzers.length);

		Analyzer product = Analyzer.IDLE;

		while (iterator.hasPrevious()) {
			UnaryOperator<Analyzer> operator = iterator.previous();

			if (operator != null) {
				Analyzer wrapped = operator.apply(product);

				if (wrapped != null)
					product = wrapped;
			}
		}

		return product;
	}

	/**
	 * Construct a new compiler from executing the given {@code compilers} functions from
	 * the last to the first with the argument being the result of invoking the previous
	 * function with the last function invoked with {@link Compiler#IDLE}.
	 * <br>
	 * The following is a pseudo-code demonstration of how the product is computed.
	 * <pre>
	 *     product = function0(function1(function2(function3(...functionLast(Compiler.IDLE)))))
	 * </pre>
	 * <br>
	 * Note: null operators in the given array will be completely ignored.
	 * <br>
	 * Note: an operator will be completely ignored when it returns null.
	 *
	 * @param compilers the wrappers constructors.
	 * @return the final product.
	 * @throws NullPointerException if the given {@code compilers} is null.
	 * @since 0.3.0 ~2021.06.24
	 */
	@SafeVarargs
	public static Compiler compiler(@NotNull UnaryOperator<Compiler>... compilers) {
		Objects.requireNonNull(compilers, "compilers");
		ListIterator<UnaryOperator<Compiler>> iterator =
				Arrays.asList(compilers)
					  .listIterator(compilers.length);

		Compiler product = Compiler.IDLE;

		while (iterator.hasPrevious()) {
			UnaryOperator<Compiler> operator = iterator.previous();

			if (operator != null) {
				Compiler wrapped = operator.apply(product);

				if (wrapped != null)
					product = wrapped;
			}
		}

		return product;
	}

	/**
	 * Construct a new parser from executing the given {@code parsers} functions from the
	 * last to the first with the argument being the result of invoking the previous
	 * function with the last function invoked with {@link Parser#IDLE}.
	 * <br>
	 * The following is a pseudo-code demonstration of how the product is computed.
	 * <pre>
	 *     product = function0(function1(function2(function3(...functionLast(Parser.IDLE)))))
	 * </pre>
	 * <br>
	 * Note: null operators in the given array will be completely ignored.
	 * <br>
	 * Note: an operator will be completely ignored when it returns null.
	 *
	 * @param parsers the wrappers constructors.
	 * @return the final product.
	 * @throws NullPointerException if the given {@code parsers} is null.
	 * @since 0.3.0 ~2021.06.24
	 */
	@SafeVarargs
	public static Parser parser(@NotNull UnaryOperator<Parser>... parsers) {
		ListIterator<UnaryOperator<Parser>> iterator =
				Arrays.asList(parsers)
					  .listIterator(parsers.length);

		Parser product = Parser.IDLE;

		while (iterator.hasPrevious()) {
			UnaryOperator<Parser> operator = iterator.previous();

			if (operator != null) {
				Parser wrapped = operator.apply(product);

				if (wrapped != null)
					product = wrapped;
			}
		}

		return product;
	}
}
