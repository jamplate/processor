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
import org.jamplate.source.tools.Sketcher;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * A class holding classes about sketching {@code ''}. (quotes)
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.01.23
 */
public final class Quotes {
	/**
	 * The pattern of an ending quote.
	 *
	 * @since 0.2.0 ~2021.01.23
	 */
	public static final Pattern PATTERN_END = Pattern.compile("(?<!\\\\)[']");
	/**
	 * The pattern of a starting quote.
	 *
	 * @since 0.2.0 ~2021.01.23
	 */
	public static final Pattern PATTERN_START = Pattern.compile("(?<!\\\\)[']");

	/**
	 * The singleton instance of the {@link QuotesSketcher}.
	 *
	 * @since 0.2.0 ~2021.01.23
	 */
	public static final Sketcher SKETCHER = new QuotesSketcher();

	/**
	 * A private always-fail constructor to avoid any instantiation of this class.
	 *
	 * @throws AssertionError when called.
	 * @since 0.2.0 ~2021.01.23
	 */
	private Quotes() {
		throw new AssertionError("No instance for you!");
	}

	/**
	 * A sketch that represents a {@code '}. (quote)
	 *
	 * @author LSafer
	 * @version 0.2.0
	 * @since 0.2.0 ~2021.01.23
	 */
	public static final class QuoteSketch extends AbstractConcreteSketch {
		@SuppressWarnings("JavaDoc")
		private static final long serialVersionUID = 777485260949931605L;

		/**
		 * Construct a new sketch for the given {@code source}. The given source is the
		 * source the constructed sketch will reserve.
		 *
		 * @param reference the source of the constructed sketch.
		 * @throws NullPointerException if the given {@code source} is null.
		 * @since 0.2.0 ~2021.01.17
		 */
		private QuoteSketch(Reference reference) {
			super(reference);
		}
	}

	/**
	 * A sketch that represents {@code ''}. (quotes context)
	 *
	 * @author LSafer
	 * @version 0.2.0
	 * @since 0.2.0 ~2021.01.23
	 */
	public static final class QuotesSketch extends AbstractContextSketch {
		@SuppressWarnings("JavaDoc")
		private static final long serialVersionUID = -3465886194836402720L;

		/**
		 * Construct a new sketch for the given {@code source}. The given source is the
		 * source the constructed sketch will reserve.
		 *
		 * @param reference the source of the constructed sketch.
		 * @throws NullPointerException if the given {@code source} is null.
		 * @since 0.2.0 ~2021.01.23
		 */
		private QuotesSketch(Reference reference) {
			super(reference);
		}
	}

	/**
	 * A visitor that makes {@link QuoteSketch} and {@link QuotesSketch} sketches.
	 *
	 * @author LSafer
	 * @version 0.2.0
	 * @since 0.2.0 ~2021.01.23
	 */
	public static final class QuotesSketcher implements Sketcher {
		/**
		 * A private constructor to avoid creating multiple instances of this.
		 *
		 * @since 0.2.0 ~2021.01.23
		 */
		private QuotesSketcher() {
		}

		@Override
		public Optional<Sketch> visitSketch(Sketch sketch) {
			Objects.requireNonNull(sketch, "sketch");
			Reference[] references = Sketch.find(sketch, Quotes.PATTERN_START, Quotes.PATTERN_END);

			if (references != null) {
				Sketch s = new QuotesSketch(references[0]);
				s.put(new QuoteSketch(references[1]));
				s.put(new QuoteSketch(references[2]));
				return Optional.of(s);
			}

			return null;
		}
	}
}
