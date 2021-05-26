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
 * A double pattern parser that also parses triple parameters.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.25
 */
public class CommandParser extends DoublePatternParser {
	/**
	 * Construct a new scope parser that parses the sketches looking for areas that starts
	 * with the given {@code pattern} and ends with the given {@code pattern}.
	 *
	 * @param pattern the pattern matching the start and the end of the areas the
	 *                constructed parser will be looking for.
	 * @throws NullPointerException if the given {@code pattern} is null.
	 * @since 0.2.0 ~2021.05.16
	 */
	public CommandParser(@NotNull Pattern pattern) {
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
	public CommandParser(@NotNull Supplier<Sketch> constructor, @NotNull Pattern pattern) {
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
	public CommandParser(@NotNull Pattern startPattern, @NotNull Pattern endPattern) {
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
	public CommandParser(@NotNull Supplier<Sketch> constructor, @NotNull Pattern startPattern, @NotNull Pattern endPattern) {
		super(constructor, startPattern, endPattern);
	}

	@SuppressWarnings("OverlyLongMethod")
	@NotNull
	@Override
	public Set<Tree> parse(@NotNull Compilation compilation, @NotNull Tree tree) {
		Set<Tree> treeSet = super.parse(compilation, tree);
		//noinspection OverlyLongLambda
		treeSet.forEach(t -> {
			//define the trees of `type` and `parameter`
			Sketch sketch = t.getSketch();
			Document document = t.document();
			Reference open = sketch.get(Component.OPEN).getTree().reference();
			Reference close = sketch.get(Component.CLOSE).getTree().reference();
			int position = open.position() + open.length();
			int length = close.position() - position;

			int middle = document.read(new Reference(position, length))
								 .toString()
								 .indexOf(' ');

			Tree type = new Tree(document, new Reference(
					position,
					middle == -1 ? length : middle
			), sketch.get(Component.TYPE));
			Tree param = new Tree(document, new Reference(
					middle == -1 ? position + length : position + middle,
					middle == -1 ? 0 : length - middle
			), sketch.get(Component.PARAMETER));

			sketch.get(Component.TYPE).setTree(type);
			sketch.get(Component.PARAMETER).setTree(param);

			if (type.reference().length() != 0)
				t.offer(type);
			if (param.reference().length() != 0)
				t.offer(param);
		});
		//noinspection OverlyLongLambda
		treeSet.forEach(t -> {
			//define `key` and `value`
			Sketch sketch = t.getSketch();
			Document document = t.document();
			Reference parameter = sketch.get(Component.PARAMETER).getTree().reference();

			int position = parameter.position();
			int length = parameter.length();
			int p = length == 0 ? position : position + 1;
			int l = length == 0 ? 0 : length - 1;
			int middle = document.read(new Reference(p, l))
								 .toString()
								 .indexOf(' ');

			Tree key = new Tree(document, new Reference(
					p,
					middle == -1 ? l : middle
			), sketch.get(Component.PARAMETER).get(Component.KEY));
			Tree value = new Tree(document, new Reference(
					middle == -1 ? p + l : p + middle,
					middle == -1 ? 0 : l - middle
			), sketch.get(Component.PARAMETER).get(Component.VALUE));

			sketch.get(Component.PARAMETER).get(Component.KEY).setTree(key);
			sketch.get(Component.PARAMETER).get(Component.VALUE).setTree(value);
		});
		return treeSet;
	}
}
