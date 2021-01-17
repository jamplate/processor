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
package org.jamplate.impl;

import org.jamplate.source.reference.Reference;
import org.jamplate.source.sketch.AbstractConcreteSketch;
import org.jamplate.source.sketch.AbstractContextSketch;
import org.jamplate.source.sketch.Sketch;

import java.util.regex.Pattern;

/**
 * A container class for parentheses.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.01.13
 */
@SuppressWarnings("UtilityClassCanBeEnum,UtilityClassWithoutPrivateConstructor,UtilityClass")
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
	@SuppressWarnings("OverlyLongLambda")
	public static final Sketch.Visitor SKETCHER = sketch -> {
		Reference reference = Sketch.find(
				sketch,
				Parentheses.PATTERN_START,
				Parentheses.PATTERN_END
		);

		if (reference != null) {
			//All can just be sketch.put(). But, it is better like this. (performance-wise)
			Sketch parentheses = new ParenthesesSketch(reference);
			parentheses.put(new ParenthesisSketch(reference.subReference(0, 1)));
			parentheses.put(new ParenthesisSketch(reference.subReference(
					reference.length() - 1, 1)));
			sketch.put(parentheses);
			return true;
		}

		return false;
	};

	/**
	 * A sketch for parentheses (plural) context.
	 *
	 * @author LSafer
	 * @version 0.2.0
	 * @since 0.2.0 ~2021.01.17
	 */
	public static class ParenthesesSketch extends AbstractContextSketch {
		@SuppressWarnings("JavaDoc")
		private static final long serialVersionUID = -7623010704158441542L;

		/**
		 * Construct a new sketch for the given {@code source}. The given source is the
		 * source the constructed sketch will reserve.
		 *
		 * @param reference the source of the constructed sketch.
		 * @throws NullPointerException if the given {@code source} is null.
		 * @since 0.2.0 ~2021.01.17
		 */
		public ParenthesesSketch(Reference reference) {
			super(reference);
		}
	}

	/**
	 * A sketch for parenthesis (singular) symbol.
	 *
	 * @author LSafer
	 * @version 0.2.0
	 * @since 0.2.0 ~2021.01.17
	 */
	public static class ParenthesisSketch extends AbstractConcreteSketch {
		@SuppressWarnings("JavaDoc")
		private static final long serialVersionUID = -4906923428406502483L;

		/**
		 * Construct a new sketch for the given {@code source}. The given source is the
		 * source the constructed sketch will reserve.
		 *
		 * @param reference the source of the constructed sketch.
		 * @throws NullPointerException if the given {@code source} is null.
		 * @since 0.2.0 ~2021.01.17
		 */
		protected ParenthesisSketch(Reference reference) {
			super(reference);
		}
	}
}
