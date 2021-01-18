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
package org.jamplate.impl.sketch;

import org.jamplate.source.reference.Reference;
import org.jamplate.source.sketch.AbstractConcreteSketch;
import org.jamplate.source.sketch.Sketch;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * A sketch for parenthesis (singular) symbol.
 * <pre>
 *     (
 * </pre>
 * <pre>
 *     )
 * </pre>
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.01.17
 */
public final class ParenthesisSketch extends AbstractConcreteSketch {
	/**
	 * The pattern that matches parenthesis. (singular)
	 *
	 * @since 0.2.0 ~2021.01.18
	 */
	public static final Pattern PATTERN = Pattern.compile("(?:^[(])|(?:[)]$)");
	/**
	 * A global instance of the class {@link SketcherVisitor}.
	 *
	 * @since 0.2.0 ~2021.01.18
	 */
	public static final SketcherVisitor VISITOR = new SketcherVisitor();

	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 5687797376406575149L;

	/**
	 * Construct a new sketch for the given {@code source}. The given source is the source
	 * the constructed sketch will reserve.
	 *
	 * @param reference the source of the constructed sketch.
	 * @throws NullPointerException if the given {@code source} is null.
	 * @since 0.2.0 ~2021.01.17
	 */
	private ParenthesisSketch(Reference reference) {
		super(reference);
	}

	/**
	 * A visitor that parses available matching parenthesis into {@link
	 * ParenthesisSketch}.
	 * <br>
	 * NOTE: this is a partial visitor and should be used in specific places and not in
	 * the main loop.
	 *
	 * @author LSafer
	 * @version 0.2.0
	 * @since 0.2.0 ~2021.01.18
	 */
	public static class SketcherVisitor implements Sketch.Visitor {
		@Override
		public boolean visit(Sketch sketch) {
			Objects.requireNonNull(sketch, "sketch");
			if (sketch instanceof ParenthesesSketch) {
				Reference reference = Sketch.find(
						sketch,
						ParenthesisSketch.PATTERN
				);

				if (reference != null) {
					Sketch parenthesis = new ParenthesisSketch(reference);
					sketch.put(parenthesis);
					return true;
				}
			}

			return false;
		}
	}
}
