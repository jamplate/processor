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
package org.jamplate.internal.function.parser;

import org.intellij.lang.annotations.Language;
import org.jamplate.function.Parser;
import org.jamplate.internal.util.Parsing;
import org.jamplate.model.Compilation;
import org.jamplate.model.Document;
import org.jamplate.model.Reference;
import org.jamplate.model.Tree;
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
import java.util.stream.IntStream;

/**
 * A parser that parses by pattern groups.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.30
 */
public class GroupParser implements Parser {
	/**
	 * The constructor of the resultant tree.
	 *
	 * @since 0.2.0 ~2021.05.30
	 */
	@NotNull
	protected final BiFunction<Document, Reference, Tree> constructor;
	/**
	 * The tree constructors.
	 *
	 * @since 0.2.0 ~2021.05.30
	 */
	@Nullable
	protected final BiConsumer<Tree, Reference> @NotNull [] constructors;
	/**
	 * The pattern.
	 *
	 * @since 0.2.0 ~2021.05.30
	 */
	@NotNull
	protected final Pattern pattern;

	/**
	 * Construct a new group parser that uses the given {@code pattern} and the given
	 * {@code constructors}.
	 *
	 * @param pattern      the pattern detecting the groups.
	 * @param constructor  the constructor of the resultant tree.
	 * @param constructors the group constructors.
	 * @throws NullPointerException if the given {@code pattern} or {@code constructor} or
	 *                              {@code constructors} is null.
	 * @since 0.2.0 ~2021.05.30
	 */
	@SafeVarargs
	public GroupParser(
			@NotNull Pattern pattern,
			@NotNull BiFunction<Document, Reference, Tree> constructor,
			@Nullable BiConsumer<Tree, Reference> @NotNull ... constructors
	) {
		Objects.requireNonNull(pattern, "pattern");
		Objects.requireNonNull(constructors, "constructors");
		this.pattern = pattern;
		this.constructor = constructor;
		this.constructors = constructors.clone();
	}

	/**
	 * Construct a new group parser that uses the given {@code regex} and the given {@code
	 * constructors}.
	 *
	 * @param regex        the pattern detecting the groups.
	 * @param constructor  the constructor of the resultant tree.
	 * @param constructors the groups constructors.
	 * @return a new group parser that targets the given {@code regex} and uses the given
	 *        {@code constructor} and {@code constructors}.
	 * @throws NullPointerException   if the given {@code regex} or {@code constructor} or
	 *                                {@code constructors} is null.
	 * @throws PatternSyntaxException if failed to compile the given {@code pattern}.
	 * @since 0.3.0 ~2021.07.03
	 */
	@SafeVarargs
	public static GroupParser group(
			@NotNull @Language("RegExp") String regex,
			@NotNull BiFunction<Document, Reference, Tree> constructor,
			@Nullable BiConsumer<Tree, Reference> @NotNull ... constructors
	) {
		Objects.requireNonNull(regex, "regex");
		return new GroupParser(
				Pattern.compile(regex),
				constructor,
				constructors
		);
	}

	@NotNull
	@Override
	public Set<Tree> parse(@NotNull Compilation compilation, @NotNull Tree tree) {
		Objects.requireNonNull(compilation, "compilation");
		Objects.requireNonNull(tree, "tree");
		List<Reference> references = Parsing.parse(tree, this.pattern);

		if (references == null)
			return Collections.emptySet();

		Document document = tree.getDocument();

		Tree result = this.constructor.apply(document, tree.getReference());

		IntStream.range(0, Math.min(this.constructors.length, references.size()))
				 .filter(i -> this.constructors[i] != null)
				 .forEach(i -> this.constructors[i].accept(result, references.get(i)));

		return Collections.singleton(result);
	}
}
