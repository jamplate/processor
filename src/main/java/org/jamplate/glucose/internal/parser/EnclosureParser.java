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
package org.jamplate.glucose.internal.parser;

import org.intellij.lang.annotations.Language;
import org.jamplate.function.Parser;
import org.jamplate.model.Compilation;
import org.jamplate.model.Document;
import org.jamplate.model.Reference;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

import static org.jamplate.util.Parsing.parseAll;
import static org.jamplate.util.Parsing.parseFirst;
import static org.jamplate.util.References.exclusive;

/**
 * A parser parsing scope sketches depending on a specific starting and ending pattern.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.16
 */
public class EnclosureParser implements Parser {
	/**
	 * The constructor of the body. (optiona)
	 *
	 * @since 0.3.0 ~2021.07.04
	 */
	@Nullable
	protected final BiConsumer<Tree, Reference> bodyConstructor;
	/**
	 * The constructor of the resultant tree.
	 *
	 * @since 0.2.0 ~2021.05.30
	 */
	@NotNull
	protected final BiFunction<Document, Reference, Tree> constructor;
	/**
	 * The constructor of the ending anchor. (optional)
	 *
	 * @since 0.3.0 ~2021.07.04
	 */
	@Nullable
	protected final BiConsumer<Tree, Reference> endConstructor;
	/**
	 * A pattern matching the closing sequence.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	protected final Pattern endPattern;
	/**
	 * True, to parse all valid matches each time.
	 *
	 * @since 0.3.0 ~2021.07.04
	 */
	protected final boolean global;
	/**
	 * The constructor of the starting anchor. (optional)
	 *
	 * @since 0.3.0 ~2021.07.04
	 */
	@Nullable
	protected final BiConsumer<Tree, Reference> startConstructor;
	/**
	 * A pattern matching the opening sequence.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	protected final Pattern startPattern;
	/**
	 * The weight to accept.
	 *
	 * @since 0.2.0 ~ 2021.05.31
	 */
	protected final int weight;

	/**
	 * Construct a new scope parser that parses the trees looking for areas that starts
	 * with the given {@code startPattern} and ends with the given {@code endPattern}.
	 *
	 * @param startPattern     the pattern matching the start of the areas the constructed
	 *                         parser will be looking for.
	 * @param endPattern       the pattern matching the end of the areas the constructed
	 *                         parser will be looking for.
	 * @param weight           the weight to accept.
	 * @param global           pass {@code true} to parse all valid matches each time,
	 *                         pass {@code false} to parse only the first match each
	 *                         time.
	 * @param constructor      the constructor of the result trees.
	 * @param startConstructor the constructor of the starting anchor. (optional)
	 * @param endConstructor   the constructor of the ending anchor. (optional)
	 * @param bodyConstructor  the constructor of the area between the starting anchor and
	 *                         the ending anchor (the body). (optional)
	 * @throws NullPointerException if the given {@code startPattern} or {@code
	 *                              endPattern} or {@code constructor} is null.
	 * @since 0.3.0 ~2021.07.04
	 */
	@SuppressWarnings("ConstructorWithTooManyParameters")
	public EnclosureParser(
			@NotNull Pattern startPattern,
			@NotNull Pattern endPattern,
			int weight,
			boolean global,
			@NotNull BiFunction<Document, Reference, Tree> constructor,
			@Nullable BiConsumer<Tree, Reference> startConstructor,
			@Nullable BiConsumer<Tree, Reference> endConstructor,
			@Nullable BiConsumer<Tree, Reference> bodyConstructor
	) {
		Objects.requireNonNull(startPattern, "startPattern");
		Objects.requireNonNull(endPattern, "endPattern");
		Objects.requireNonNull(constructor, "constructor");
		this.startPattern = startPattern;
		this.endPattern = endPattern;
		this.weight = weight;
		this.global = global;
		this.constructor = constructor;
		this.startConstructor = startConstructor;
		this.endConstructor = endConstructor;
		this.bodyConstructor = bodyConstructor;
	}

	/**
	 * Construct a new scope parser that parses the trees looking for areas that starts
	 * with the given {@code startRegex} and ends with the given {@code endRegex}.
	 *
	 * @param startRegex       the pattern matching the start of the areas the constructed
	 *                         parser will be looking for.
	 * @param endRegex         the pattern matching the end of the areas the constructed
	 *                         parser will be looking for.
	 * @param constructor      the constructor of the result trees.
	 * @param startConstructor the constructor of the starting anchor. (optional)
	 * @param endConstructor   the constructor of the ending anchor. (optional)
	 * @param bodyConstructor  the constructor of the area between the starting anchor and
	 *                         the ending anchor (the body). (optional)
	 * @return a new enclosure parser with the given parameters.
	 * @throws NullPointerException   if the given {@code startRegex} or {@code endRegex}
	 *                                or {@code constructor} is null.
	 * @throws PatternSyntaxException if the given {@code startRegex} or {@code endRegex}
	 *                                has a syntax error.
	 * @since 0.3.0 ~2021.07.04
	 */
	@SuppressWarnings("MethodWithTooManyParameters")
	@NotNull
	@Contract(value = "_,_,_,_,_,_->new", pure = true)
	public static EnclosureParser enclosure(
			@NotNull @Language("RegExp") String startRegex,
			@NotNull @Language("RegExp") String endRegex,
			@NotNull BiFunction<Document, Reference, Tree> constructor,
			@Nullable BiConsumer<Tree, Reference> startConstructor,
			@Nullable BiConsumer<Tree, Reference> endConstructor,
			@Nullable BiConsumer<Tree, Reference> bodyConstructor
	) {
		Objects.requireNonNull(startRegex, "startRegex");
		Objects.requireNonNull(endRegex, "endRegex");
		return new EnclosureParser(
				Pattern.compile(startRegex),
				Pattern.compile(endRegex),
				0,
				false,
				constructor,
				startConstructor,
				endConstructor,
				bodyConstructor
		);
	}

	/**
	 * Construct a new scope parser that parses the trees looking for areas that starts
	 * with the given {@code startRegex} and ends with the given {@code endRegex}.
	 *
	 * @param startRegex       the pattern matching the start of the areas the constructed
	 *                         parser will be looking for.
	 * @param endRegex         the pattern matching the end of the areas the constructed
	 *                         parser will be looking for.
	 * @param weight           the weight to accept.
	 * @param global           pass {@code true} to parse all valid matches each time,
	 *                         pass {@code false} to parse only the first match each
	 *                         time.
	 * @param constructor      the constructor of the result trees.
	 * @param startConstructor the constructor of the starting anchor. (optional)
	 * @param endConstructor   the constructor of the ending anchor. (optional)
	 * @param bodyConstructor  the constructor of the area between the starting anchor and
	 *                         the ending anchor (the body). (optional)
	 * @return a new enclosure parser with the given parameters.
	 * @throws NullPointerException   if the given {@code startRegex} or {@code endRegex}
	 *                                or {@code constructor} is null.
	 * @throws PatternSyntaxException if the given {@code startRegex} or {@code endRegex}
	 *                                has a syntax error.
	 * @since 0.3.0 ~2021.07.04
	 */
	@SuppressWarnings("MethodWithTooManyParameters")
	@NotNull
	@Contract(value = "_,_,_,_,_,_,_,_->new", pure = true)
	public static EnclosureParser enclosure(
			@NotNull @Language("RegExp") String startRegex,
			@NotNull @Language("RegExp") String endRegex,
			int weight,
			boolean global,
			@NotNull BiFunction<Document, Reference, Tree> constructor,
			@Nullable BiConsumer<Tree, Reference> startConstructor,
			@Nullable BiConsumer<Tree, Reference> endConstructor,
			@Nullable BiConsumer<Tree, Reference> bodyConstructor
	) {
		Objects.requireNonNull(startRegex, "startRegex");
		Objects.requireNonNull(endRegex, "endRegex");
		return new EnclosureParser(
				Pattern.compile(startRegex),
				Pattern.compile(endRegex),
				weight,
				global,
				constructor,
				startConstructor,
				endConstructor,
				bodyConstructor
		);
	}

	@SuppressWarnings("DuplicatedCode")
	@NotNull
	@Override
	public Set<Tree> parse(@NotNull Compilation compilation, @NotNull Tree tree) {
		Objects.requireNonNull(compilation, "compilation");
		Objects.requireNonNull(tree, "tree");
		if (this.global) {
			Set<List<Reference>> matches = parseAll(tree, this.startPattern, this.endPattern, this.weight);

			return matches
					.parallelStream()
					.map(match -> {
						Tree result = this.constructor.apply(tree.getDocument(), match.get(0));

						if (this.startConstructor != null)
							this.startConstructor.accept(result, match.get(1));
						if (this.endConstructor != null)
							this.endConstructor.accept(result, match.get(2));
						if (this.bodyConstructor != null)
							this.bodyConstructor.accept(
									result,
									exclusive(
											match.get(1),
											match.get(2)
									)
							);

						return result;
					})
					.collect(Collectors.toSet());
		} else {
			List<Reference> match = parseFirst(
					tree,
					this.startPattern,
					this.endPattern,
					this.weight
			);

			if (match.isEmpty())
				return Collections.emptySet();

			Tree result = this.constructor.apply(tree.getDocument(), match.get(0));

			if (this.startConstructor != null)
				this.startConstructor.accept(result, match.get(1));
			if (this.endConstructor != null)
				this.endConstructor.accept(result, match.get(2));
			if (this.bodyConstructor != null)
				this.bodyConstructor.accept(
						result,
						exclusive(
								match.get(1),
								match.get(2)
						)
				);

			return Collections.singleton(result);
		}
	}
}
