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

import org.jamplate.parsing.Patterns;
import org.jamplate.parsing.sketcher.Sketcher;
import org.jamplate.source.reference.Reference;
import org.jamplate.source.sketch.AbstractConcreteSketch;
import org.jamplate.source.sketch.AbstractContextSketch;
import org.jamplate.source.sketch.Sketch;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * A class holding classes about sketching {@code []}. (square brackets)
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.01.23
 */
public final class Brackets {
	/**
	 * A pattern that detects the start of a brackets context.
	 *
	 * @since 0.2.0 ~2021.01.18
	 */
	public static final Pattern PATTERN_END = Pattern.compile("[\\]]");
	/**
	 * A pattern that detects the end of a brackets context.
	 *
	 * @since 0.2.0 ~2021.01.18
	 */
	public static final Pattern PATTERN_START = Pattern.compile("[\\[]");

	/**
	 * A visitor that makes {@link BracketsSketch} when it found available brackets pair
	 * in a sketch.
	 *
	 * @since 0.2.0 ~2021.01.18
	 */
	public static final Sketcher SKETCHER = new BracketsSketcher();

	/**
	 * A private always-fail constructor to avoid any instantiation of this class.
	 *
	 * @throws AssertionError when called.
	 * @since 0.2.0 ~2021.01.23
	 */
	private Brackets() {
		throw new AssertionError("No instance for you!");
	}

	/**
	 * A sketch for bracket symbol.
	 * <pre>
	 *     [
	 * </pre>
	 * <pre>
	 *     ]
	 * </pre>
	 *
	 * @author LSafer
	 * @version 0.2.0
	 * @since 0.2.0 ~2021.01.18
	 */
	public static final class BracketSketch extends AbstractConcreteSketch {
		@SuppressWarnings("JavaDoc")
		private static final long serialVersionUID = 6108441942086890901L;

		/**
		 * Construct a new sketch for the given {@code source}. The given source is the
		 * source the constructed sketch will reserve.
		 *
		 * @param reference the source of the constructed sketch.
		 * @throws NullPointerException if the given {@code source} is null.
		 * @since 0.2.0 ~2021.01.18
		 */
		private BracketSketch(Reference reference) {
			super(reference);
		}
	}

	/**
	 * A sketch for brackets context.
	 * <pre>
	 *     []
	 * </pre>
	 *
	 * @author LSafer
	 * @version 0.2.0
	 * @since 0.2.0 ~2021.01.18
	 */
	public static final class BracketsSketch extends AbstractContextSketch {
		@SuppressWarnings("JavaDoc")
		private static final long serialVersionUID = 3428179238573892953L;

		/**
		 * Construct a new sketch for the given {@code source}. The given source is the
		 * source the constructed sketch will reserve.
		 *
		 * @param reference the source of the constructed sketch.
		 * @throws NullPointerException if the given {@code source} is null.
		 * @since 0.2.0 ~2021.01.18
		 */
		private BracketsSketch(Reference reference) {
			super(reference);
		}
	}

	/**
	 * A visitor that makes {@link BracketsSketch} when it found available brackets pair
	 * in a sketch.
	 *
	 * @author LSafer
	 * @version 0.2.0
	 * @since 0.2.0 ~2021.01.18
	 */
	public static final class BracketsSketcher implements Sketcher {
		/**
		 * A private constructor to avoid creating multiple instances of this.
		 *
		 * @since 0.2.0 ~2021.01.23
		 */
		private BracketsSketcher() {
		}

		@Override
		public Optional<Sketch> visitSketch(Sketch sketch) {
			Objects.requireNonNull(sketch, "sketch");
			Reference[] references = Patterns.find(sketch, Brackets.PATTERN_START, Brackets.PATTERN_END);

			if (references != null) {
				Sketch s = new BracketsSketch(references[0]);
				s.put(new BracketSketch(references[1]));
				s.put(new BracketSketch(references[2]));
				return Optional.of(s);
			}

			return null;
		}
	}
}