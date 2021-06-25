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
package org.jamplate.internal.function.parser.pattern;

import org.jamplate.function.Parser;
import org.jamplate.internal.util.Parsing;
import org.jamplate.model.Compilation;
import org.jamplate.model.Document;
import org.jamplate.model.Reference;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * A parser parsing scope sketches depending on a specific starting and ending pattern.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.16
 */
public class EnclosureParser implements Parser {
	/**
	 * The constructor of the resultant tree.
	 *
	 * @since 0.2.0 ~2021.05.30
	 */
	@NotNull
	protected final BiFunction<Document, Reference, Tree> constructor;

	/**
	 * The constructors of the components.
	 *
	 * @since 0.2.0 ~2021.05.30
	 */
	@Nullable
	protected final BiConsumer<Tree, Reference> @NotNull [] constructors;

	/**
	 * A pattern matching the closing sequence.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	protected final Pattern endPattern;
	/**
	 * A pattern matching the opening sequence.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	protected final Pattern startPattern;

	/**
	 * The z-index to accept.
	 *
	 * @since 0.2.0 ~ 2021.05.31
	 */
	protected final int zIndex;

	/**
	 * Construct a new scope parser that parses the sketches looking for areas that starts
	 * with the given {@code startPattern} and ends with the given {@code endPattern}.
	 *
	 * @param startPattern the pattern matching the start of the areas the constructed
	 *                     parser will be looking for.
	 * @param endPattern   the pattern matching the end of the areas the constructed
	 *                     parser will be looking for.
	 * @param zIndex       the z-index to accept.
	 * @param constructor  the constructor of the result trees.
	 * @param constructors the constructors of the components.
	 * @throws NullPointerException     if the given {@code startPattern} or {@code
	 *                                  endPattern} or {@code constructor} or {@code
	 *                                  constructors} is null.
	 * @throws IllegalArgumentException if {@code constructors.length > 3}.
	 * @since 0.2.0 ~2021.05.16
	 */
	@SafeVarargs
	public EnclosureParser(
			@NotNull Pattern startPattern,
			@NotNull Pattern endPattern,
			int zIndex,
			@NotNull BiFunction<Document, Reference, Tree> constructor,
			@Nullable BiConsumer<Tree, Reference> @NotNull ... constructors
	) {
		Objects.requireNonNull(startPattern, "startPattern");
		Objects.requireNonNull(endPattern, "endPattern");
		Objects.requireNonNull(constructors, "constructors");
		this.startPattern = startPattern;
		this.endPattern = endPattern;
		this.zIndex = zIndex;
		this.constructor = constructor;
		if (constructors.length > 3)
			throw new IllegalStateException(
					"Too many constructors: " +
					constructors.length
			);
		this.constructors = Arrays.copyOf(constructors, 3);
	}

	/**
	 * Construct a new scope parser that parses the sketches looking for areas that starts
	 * with the given {@code startPattern} and ends with the given {@code endPattern}.
	 *
	 * @param startPattern the pattern matching the start of the areas the constructed
	 *                     parser will be looking for.
	 * @param endPattern   the pattern matching the end of the areas the constructed
	 *                     parser will be looking for.
	 * @param constructor  the constructor of the result trees.
	 * @param constructors the constructors of the components.
	 * @throws NullPointerException     if the given {@code startPattern} or {@code
	 *                                  endPattern} or {@code constructor} or {@code
	 *                                  constructors} is null.
	 * @throws IllegalArgumentException if {@code constructors.length > 3}.
	 * @since 0.2.0 ~2021.05.16
	 */
	@SafeVarargs
	public EnclosureParser(
			@NotNull Pattern startPattern,
			@NotNull Pattern endPattern,
			@NotNull BiFunction<Document, Reference, Tree> constructor,
			@Nullable BiConsumer<Tree, Reference> @NotNull ... constructors
	) {
		Objects.requireNonNull(startPattern, "startPattern");
		Objects.requireNonNull(endPattern, "endPattern");
		Objects.requireNonNull(constructors, "constructors");
		this.startPattern = startPattern;
		this.endPattern = endPattern;
		this.zIndex = 0;
		this.constructor = constructor;
		if (constructors.length > 3)
			throw new IllegalArgumentException(
					"Too many constructors: " +
					constructors.length
			);
		this.constructors = Arrays.copyOf(constructors, 3);
	}

	@NotNull
	@Override
	public Set<Tree> parse(@NotNull Compilation compilation, @NotNull Tree tree) {
		Objects.requireNonNull(compilation, "compilation");
		Objects.requireNonNull(tree, "tree");
		return Parsing.parseAll(tree, this.startPattern, this.endPattern, this.zIndex)
					  .parallelStream()
					  .map(m -> {
						  Tree result = this.constructor.apply(tree.document(), m.get(0));

						  if (this.constructors[0] != null)
							  this.constructors[0].accept(result, m.get(1));
						  if (this.constructors[1] != null)
							  this.constructors[1].accept(result, m.get(2));
						  if (this.constructors[2] != null) {
							  int position = m.get(1).position() + m.get(1).length();
							  int length = m.get(2).position() - position;

							  this.constructors[2].accept(
									  result,
									  new Reference(position, length)
							  );
						  }

						  return result;
					  })
					  .collect(Collectors.toSet());
	}
}
