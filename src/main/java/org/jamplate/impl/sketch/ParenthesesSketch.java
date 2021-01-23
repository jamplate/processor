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
import org.jamplate.source.sketch.AbstractContextSketch;
import org.jamplate.source.sketch.Sketch;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * A sketch for parentheses (plural) context.
 * <pre>
 *     ()
 * </pre>
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.01.17
 */
public final class ParenthesesSketch extends AbstractContextSketch {
	/**
	 * A visitor that makes {@link ParenthesesSketch} when it found available parenthesis
	 * pair in a sketch.
	 *
	 * @since 0.2.0 ~2021.01.17
	 */
	public static final SketcherVisitor VISITOR = new SketcherVisitor();
	/**
	 * A pattern that detects the start of a parentheses context.
	 *
	 * @since 0.2.0 ~2021.01.17
	 */
	public static final Pattern PATTERN_END = Pattern.compile("[)]");
	/**
	 * A pattern that detects the end of a parentheses context.
	 *
	 * @since 0.2.0 ~2021.01.17
	 */
	public static final Pattern PATTERN_START = Pattern.compile("[(]");

	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 1731395059192255934L;

	/**
	 * Construct a new sketch for the given {@code source}. The given source is the source
	 * the constructed sketch will reserve.
	 *
	 * @param reference the source of the constructed sketch.
	 * @throws NullPointerException if the given {@code source} is null.
	 * @since 0.2.0 ~2021.01.17
	 */
	private ParenthesesSketch(Reference reference) {
		super(reference);
	}

	/**
	 * A visitor that makes {@link ParenthesesSketch} when it found available parenthesis
	 * pair in a sketch.
	 *
	 * @author LSafer
	 * @version 0.2.0
	 * @since 0.2.0 ~2021.01.18
	 */
	public static class SketcherVisitor implements Sketch.Visitor {
		@Override
		public boolean visit(Sketch sketch) {
			Objects.requireNonNull(sketch, "sketch");
			Reference reference = Sketch.find(
					sketch,
					ParenthesesSketch.PATTERN_START,
					ParenthesesSketch.PATTERN_END
			);

			if (reference != null) {
				Sketch parentheses = new ParenthesesSketch(reference);
				while (parentheses.accept(ParenthesisSketch.VISITOR))
					//parse the parenthesis (singular)
					;
				sketch.put(parentheses);
				return true;
			}

			return false;
		}
	}
}