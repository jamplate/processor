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
package org.jamplate.impl.parser;

import org.jamplate.impl.Component;
import org.jamplate.model.*;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.Supplier;
import java.util.regex.Pattern;

/**
 * A double pattern parser that also parses the parameter component.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.25
 */
public class InjectionParser extends DoublePatternParser {
	/**
	 * Construct a new scope parser that parses the sketches looking for areas that starts
	 * with the given {@code pattern} and ends with the given {@code pattern}.
	 *
	 * @param pattern the pattern matching the start and the end of the areas the
	 *                constructed parser will be looking for.
	 * @throws NullPointerException if the given {@code pattern} is null.
	 * @since 0.2.0 ~2021.05.16
	 */
	public InjectionParser(@NotNull Pattern pattern) {
		super(pattern);
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
	public InjectionParser(@NotNull Supplier<Sketch> constructor, @NotNull Pattern pattern) {
		super(constructor, pattern);
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
	public InjectionParser(@NotNull Pattern startPattern, @NotNull Pattern endPattern) {
		super(startPattern, endPattern);
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
	public InjectionParser(@NotNull Supplier<Sketch> constructor, @NotNull Pattern startPattern, @NotNull Pattern endPattern) {
		super(constructor, startPattern, endPattern);
	}

	@NotNull
	@Override
	public Set<Tree> parse(@NotNull Compilation compilation, @NotNull Tree tree) {
		Set<Tree> treeSet = super.parse(compilation, tree);
		//noinspection OverlyLongLambda
		treeSet.forEach(t -> {
			Document document = t.document();
			Sketch sketch = t.getSketch();
			Sketch param = sketch.get(Component.PARAMETER);

			Reference open = sketch.get(Component.OPEN).getTree().reference();
			Reference close = sketch.get(Component.CLOSE).getTree().reference();

			int position = open.position() + open.length();
			int length = close.position() - position;

			Tree paramTree = new Tree(
					document,
					new Reference(position, length),
					param
			);

			param.setTree(paramTree);

			if (paramTree.reference().length() != 0)
				t.offer(paramTree);
		});
		return treeSet;
	}
}
