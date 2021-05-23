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
package org.jamplate.model.function;

import org.jamplate.model.Compilation;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOError;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A function that takes a compilation and a sketch and parses new sketches from them.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.16
 */
@FunctionalInterface
public interface Parser {
	/**
	 * Return a parser that uses this parser to parse and then add to the result the
	 * result of parsing each one of them using the given {@code parser}.
	 *
	 * @param parser the parser to be used on the results of this parser.
	 * @return a parser that uses this parser then uses the given {@code parser}.
	 * @throws NullPointerException if the given {@code parser} is null.
	 * @since 0.2.0 ~2021.05.18
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	default Parser also(@NotNull Parser parser) {
		return (compilation, tree) ->
				this.parse(compilation, tree)
					.parallelStream()
					.flatMap(t ->
							Stream.concat(
									Stream.of(t),
									parser.parse(compilation, t)
										  .stream()
							)
					)
					.collect(Collectors.toSet());
	}

	/**
	 * Return a new parser that uses this parser for parsing only if the given {@code
	 * predicate} returned {@code true} on the tree to be parsed. Otherwise, returns an
	 * empty set.
	 *
	 * @param predicate the predicate to be used.
	 * @return a parser that uses this parser and only parses if the given {@code
	 * 		predicate} returned {@code true} for the compilation and tree to parse on.
	 * @throws NullPointerException if the given {@code predicate} is null.
	 * @since 0.2.0 ~2021.05.17
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	default Parser condition(@NotNull Predicate<Tree> predicate) {
		Objects.requireNonNull(predicate, "predicate");
		return (compilation, tree) ->
				predicate.test(tree) ?
				this.parse(compilation, tree) :
				Collections.emptySet();
	}

	/**
	 * Return a new parser that uses this parser to parse and then filter the results
	 * depending on the given {@code predicate}.
	 *
	 * @param predicate the predicate to be used to filter the results.
	 * @return a new parser that uses this and filters the results using the given {@code
	 * 		predicate}.
	 * @throws NullPointerException if the given {@code predicate} is null.
	 * @since 0.2.0 ~2021.05.17
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	default Parser filter(@NotNull Predicate<Tree> predicate) {
		Objects.requireNonNull(predicate, "predicate");
		return (compilation, tree) ->
				this.parse(compilation, tree)
					.parallelStream()
					.filter(predicate)
					.collect(Collectors.toSet());
	}

	/**
	 * Return a new parser that uses this parser for parsing and then replaces each parsed
	 * tree with the result of invoking the given {@code operator} with that tree as the
	 * argument.
	 *
	 * @param operator the operator to get the replacement tree from.
	 * @return a parser that parses using this and replaces the results using the given
	 *        {@code operator}.
	 * @throws NullPointerException if the given {@code operator} is null.
	 * @since 0.2.0 ~2021.05.17
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	default Parser map(@NotNull UnaryOperator<Tree> operator) {
		Objects.requireNonNull(operator, "operator");
		return (compilation, tree) ->
				this.parse(compilation, tree)
					.parallelStream()
					.map(operator)
					.collect(Collectors.toSet());
	}

	/**
	 * Return a new parser that uses this parser for parsing and then invokes the given
	 * {@code consumer} for each parsed tree.
	 *
	 * @param consumer the consumer to be invoked for each parsed tree.
	 * @return a new parser that parses using this then invokes the given {@code consumer}
	 * 		on each parsed tree.
	 * @throws NullPointerException if the given {@code consumer} is null.
	 * @since 0.2.0 ~2021.05.17
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	default Parser peek(@NotNull Consumer<Tree> consumer) {
		Objects.requireNonNull(consumer, "consumer");
		return (compilation, tree) -> {
			Set<Tree> trees = this.parse(compilation, tree);
			trees.forEach(consumer);
			return trees;
		};
	}

	/**
	 * Return a new parser that first parses using this parser then parse the results
	 * one-by-one using the given {@code parser}.
	 *
	 * @param parser the parser to be used after this parser by the returned parser.
	 * @return a parser that parses using this parser then the given {@code parser}.
	 * @throws NullPointerException if the given {@code parser} is null.
	 * @since 0.2.0 ~2021.05.17
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	default Parser then(@NotNull Parser parser) {
		return (compilation, tree) ->
				this.parse(compilation, tree)
					.parallelStream()
					.map(t -> parser.parse(compilation, t))
					.flatMap(Set::stream)
					.collect(Collectors.toSet());
	}

	/**
	 * Parse the given {@code sketch} with respect to the given {@code compilation}.
	 *
	 * @param compilation the compilation taking the given {@code sketch}.
	 * @param tree        the sketch to be parsed.
	 * @return the parsed sketches.
	 * @throws NullPointerException if the given {@code compilation} or {@code node} is
	 *                              null.
	 * @throws IOError              if any I/O error occurs.
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	@Contract(value = "_,_->new", pure = true)
	Set<Tree> parse(@NotNull Compilation compilation, @NotNull Tree tree);
}
