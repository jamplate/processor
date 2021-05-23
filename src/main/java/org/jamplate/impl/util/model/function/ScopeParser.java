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
package org.jamplate.impl.util.model.function;

import org.jamplate.model.function.Parser;
import org.jamplate.impl.util.model.ScopeSketch;
import org.jamplate.model.Compilation;
import org.jamplate.model.Document;
import org.jamplate.model.Tree;
import org.jamplate.util.Parsing;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * A parser parsing scope sketches depending on a specific starting and ending pattern.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.16
 */
public class ScopeParser implements Parser {
	/**
	 * The constructor to be used to constructed new sketches.
	 *
	 * @since 0.2.0 ~2021.05.20
	 */
	@NotNull
	protected final Supplier<ScopeSketch> constructor;
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
	 * Construct a new scope parser that parses the sketches looking for areas that starts
	 * with the given {@code pattern} and ends with the given {@code pattern}.
	 *
	 * @param pattern the pattern matching the start and the end of the areas the
	 *                constructed parser will be looking for.
	 * @throws NullPointerException if the given {@code pattern} is null.
	 * @since 0.2.0 ~2021.05.16
	 */
	public ScopeParser(@NotNull Pattern pattern) {
		Objects.requireNonNull(pattern, "pattern");
		this.constructor = ScopeSketch::new;
		this.startPattern = pattern;
		this.endPattern = pattern;
	}

	/**
	 * Construct a new scope parser that parses the sketches looking for areas that starts
	 * with the given {@code pattern} and ends with the given {@code pattern}.
	 *
	 * @param constructor the constructor to be used by the constructed parser to
	 *                    constructed the sketches of the matches.
	 * @param pattern     the pattern matching the start and the end of the areas the
	 *                    constructed parser will be looking for.
	 * @throws NullPointerException if the given {@code constructor} or {@code pattern} is
	 *                              null.
	 * @since 0.2.0 ~2021.05.16
	 */
	public ScopeParser(@NotNull Supplier<ScopeSketch> constructor, @NotNull Pattern pattern) {
		Objects.requireNonNull(constructor, "constructor");
		Objects.requireNonNull(pattern, "pattern");
		this.constructor = constructor;
		this.startPattern = pattern;
		this.endPattern = pattern;
	}

	/**
	 * Construct a new scope parser that parses the sketches looking for areas that starts
	 * with the given {@code startPattern} and ends with the given {@code endPattern}.
	 *
	 * @param startPattern the pattern matching the start of the areas the constructed
	 *                     parser will be looking for.
	 * @param endPattern   the pattern matching the end of the areas the constructed
	 *                     parser will be looking for.
	 * @throws NullPointerException if the given {@code startPattern} or {@code
	 *                              endPattern} is null.
	 * @since 0.2.0 ~2021.05.16
	 */
	public ScopeParser(@NotNull Pattern startPattern, @NotNull Pattern endPattern) {
		Objects.requireNonNull(startPattern, "startPattern");
		Objects.requireNonNull(endPattern, "endPattern");
		this.constructor = ScopeSketch::new;
		this.startPattern = startPattern;
		this.endPattern = endPattern;
	}

	/**
	 * Construct a new scope parser that parses the sketches looking for areas that starts
	 * with the given {@code startPattern} and ends with the given {@code endPattern}.
	 *
	 * @param constructor  the constructor to be used by the constructed parser to
	 *                     constructed the sketches of the matches.
	 * @param startPattern the pattern matching the start of the areas the constructed
	 *                     parser will be looking for.
	 * @param endPattern   the pattern matching the end of the areas the constructed
	 *                     parser will be looking for.
	 * @throws NullPointerException if the given {@code startPattern} or {@code
	 *                              endPattern} is null.
	 * @since 0.2.0 ~2021.05.16
	 */
	public ScopeParser(@NotNull Supplier<ScopeSketch> constructor, @NotNull Pattern startPattern, @NotNull Pattern endPattern) {
		Objects.requireNonNull(constructor, "constructor");
		Objects.requireNonNull(startPattern, "startPattern");
		Objects.requireNonNull(endPattern, "endPattern");
		this.constructor = constructor;
		this.startPattern = startPattern;
		this.endPattern = endPattern;
	}

	@NotNull
	@Override
	public Set<Tree> parse(@NotNull Compilation compilation, @NotNull Tree tree) {
		Objects.requireNonNull(compilation, "compilation");
		Objects.requireNonNull(tree, "tree");
		//noinspection OverlyLongLambda
		return Parsing.parseAll(tree, this.startPattern, this.endPattern)
					  .parallelStream()
					  .map(m -> {
						  Document d = tree.document();
						  ScopeSketch s = this.constructor.get();
						  Tree t = new Tree(d, m.get(0), s);
						  Tree o = new Tree(d, m.get(1), s.getOpenAnchorSketch());
						  Tree c = new Tree(d, m.get(2), s.getCloseAnchorSketch());
						  if (o.reference().length() != 0)
							  t.offer(o);
						  if (c.reference().length() != 0)
							  t.offer(c);
						  s.setTree(t);
						  s.getOpenAnchorSketch().setTree(o);
						  s.getCloseAnchorSketch().setTree(c);
						  return t;
					  })
					  .collect(Collectors.toSet());
	}
}
