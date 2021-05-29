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
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
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
	default Parser then(@NotNull Parser parser) {
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
	default Parser then(@NotNull BiConsumer<Compilation, Tree> consumer) {
		Objects.requireNonNull(consumer, "consumer");
		return (compilation, tree) -> {
			Set<Tree> trees = this.parse(compilation, tree);
			for (Tree child : trees)
				consumer.accept(compilation, child);
			return trees;
		};
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
