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
 * A class holding classes about sketching {@code ""}. (double quotes)
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.01.24
 */
public final class DoubleQuotes {
	/**
	 * The pattern of an ending quote.
	 *
	 * @since 0.2.0 ~2021.01.24
	 */
	public static final Pattern PATTERN_END = Pattern.compile("(?<!\\\\)[\"]");
	/**
	 * The pattern of a starting quote.
	 *
	 * @since 0.2.0 ~2021.01.24
	 */
	public static final Pattern PATTERN_START = Pattern.compile("(?<!\\\\)[\"]");

	/**
	 * The singleton instance of the {@link DoubleQuotes.DoubleQuotesSketcher}.
	 *
	 * @since 0.2.0 ~2021.01.24
	 */
	public static final Sketch.Visitor SKETCHER = new DoubleQuotesSketcher();

	/**
	 * A private always-fail constructor to avoid any instantiation of this class.
	 *
	 * @throws AssertionError when called.
	 * @since 0.2.0 ~2021.01.24
	 */
	private DoubleQuotes() {
		throw new AssertionError("No instance for you!");
	}

	/**
	 * A sketch that represents a {@code "}. (double quote)
	 *
	 * @author LSafer
	 * @version 0.2.0
	 * @since 0.2.0 ~2021.01.24
	 */
	public static final class DoubleQuoteSketch extends AbstractConcreteSketch {
		@SuppressWarnings("JavaDoc")
		private static final long serialVersionUID = -1307615983399045672L;

		/**
		 * Construct a new sketch for the given {@code source}. The given source is the
		 * source the constructed sketch will reserve.
		 *
		 * @param reference the source of the constructed sketch.
		 * @throws NullPointerException if the given {@code source} is null.
		 * @since 0.2.0 ~2021.01.24
		 */
		private DoubleQuoteSketch(Reference reference) {
			super(reference);
		}
	}

	/**
	 * A sketch that represents {@code ""}. (double quotes context)
	 *
	 * @author LSafer
	 * @version 0.2.0
	 * @since 0.2.0 ~2021.01.23
	 */
	public static final class DoubleQuotesSketch extends AbstractContextSketch {
		@SuppressWarnings("JavaDoc")
		private static final long serialVersionUID = -3272886549112160976L;

		/**
		 * Construct a new sketch for the given {@code source}. The given source is the
		 * source the constructed sketch will reserve.
		 *
		 * @param reference the source of the constructed sketch.
		 * @throws NullPointerException if the given {@code source} is null.
		 * @since 0.2.0 ~2021.01.24
		 */
		private DoubleQuotesSketch(Reference reference) {
			super(reference);
		}
	}

	/**
	 * A visitor that makes {@link DoubleQuotes.DoubleQuoteSketch} and {@link
	 * DoubleQuotes.DoubleQuotesSketch} sketches.
	 *
	 * @author LSafer
	 * @version 0.2.0
	 * @since 0.2.0 ~2021.01.24
	 */
	public static final class DoubleQuotesSketcher implements Sketch.Visitor {
		/**
		 * A private constructor to avoid creating multiple instances of this.
		 *
		 * @since 0.2.0 ~2021.01.24
		 */
		private DoubleQuotesSketcher() {
		}

		@Override
		public boolean visit(Sketch sketch) {
			Objects.requireNonNull(sketch, "sketch");
			Reference[] references = Sketch.find(sketch, DoubleQuotes.PATTERN_START, DoubleQuotes.PATTERN_END);

			if (references != null) {
				Sketch s = new DoubleQuotesSketch(references[0]);
				s.put(new DoubleQuoteSketch(references[1]));
				s.put(new DoubleQuoteSketch(references[2]));
				sketch.put(s);
				return true;
			}

			return false;
		}
	}
}
