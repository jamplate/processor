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
import org.jamplate.source.sketch.AbstractContextSketch;
import org.jamplate.source.sketch.Sketch;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * A class holding classes about sketching {@code ()}. (parentheses)
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.01.23
 */
public final class Parentheses {
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

	/**
	 * A visitor that makes {@link ParenthesesSketch} when it found available parenthesis
	 * pair in a sketch.
	 *
	 * @since 0.2.0 ~2021.01.17
	 */
	public static final Sketch.Visitor SKETCHER = new ParenthesesSketcher();

	/**
	 * A private always-fail constructor to avoid any instantiation of this class.
	 *
	 * @throws AssertionError when called.
	 * @since 0.2.0 ~2021.01.23
	 */
	private Parentheses() {
		throw new AssertionError("No instance for you!");
	}

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
	public static final class ParenthesesSketch extends AbstractContextSketch {
		@SuppressWarnings("JavaDoc")
		private static final long serialVersionUID = 1731395059192255934L;

		/**
		 * Construct a new sketch for the given {@code source}. The given source is the
		 * source the constructed sketch will reserve.
		 *
		 * @param reference the source of the constructed sketch.
		 * @throws NullPointerException if the given {@code source} is null.
		 * @since 0.2.0 ~2021.01.17
		 */
		private ParenthesesSketch(Reference reference) {
			super(reference);
		}
	}

	/**
	 * A visitor that makes {@link ParenthesesSketch} when it found available parenthesis
	 * pair in a sketch.
	 *
	 * @author LSafer
	 * @version 0.2.0
	 * @since 0.2.0 ~2021.01.18
	 */
	public static final class ParenthesesSketcher implements Sketch.Visitor {
		/**
		 * A private constructor to avoid creating multiple instances of this.
		 *
		 * @since 0.2.0 ~2021.01.23
		 */
		private ParenthesesSketcher() {
		}

		@Override
		public boolean visit(Sketch sketch) {
			Objects.requireNonNull(sketch, "sketch");
			Reference reference = Sketch.find(sketch, Parentheses.PATTERN_START, Parentheses.PATTERN_END);

			if (reference != null) {
				Sketch s = new ParenthesesSketch(reference);
				Reference rs = Sketch.find(s, Parentheses.PATTERN_START);
				Reference re = Sketch.find(s, Parentheses.PATTERN_END);

				if (rs != null && re != null) {
					s.put(new ParenthesisSketch(rs));
					s.put(new ParenthesisSketch(re));
					sketch.put(s);
					return true;
				}
			}

			return false;
		}
	}

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
	public static final class ParenthesisSketch extends AbstractConcreteSketch {
		@SuppressWarnings("JavaDoc")
		private static final long serialVersionUID = 5687797376406575149L;

		/**
		 * Construct a new sketch for the given {@code source}. The given source is the
		 * source the constructed sketch will reserve.
		 *
		 * @param reference the source of the constructed sketch.
		 * @throws NullPointerException if the given {@code source} is null.
		 * @since 0.2.0 ~2021.01.17
		 */
		private ParenthesisSketch(Reference reference) {
			super(reference);
		}
	}
}
