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
package org.cufyplate.sketch.anchor;

import org.jamplate.model.reference.Reference;
import org.jamplate.model.sketch.AbstractConcreteSketch;
import org.jamplate.processor.maker.Maker;

/**
 * A sketch that represents a {@code '}. (quote)
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.01.23
 */
public final class QuoteSketch extends AbstractConcreteSketch {
	/**
	 * The maker of the concrete sketch.
	 *
	 * @since 0.2.0 ~2021.01.30
	 */
	public static final Maker MAKER = QuoteSketch::new;

	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 777485260949931605L;

	/**
	 * Construct a new sketch for the given {@code source}. The given source is the source
	 * the constructed sketch will reserve.
	 *
	 * @param reference the source of the constructed sketch.
	 * @throws NullPointerException if the given {@code source} is null.
	 * @since 0.2.0 ~2021.01.17
	 */
	private QuoteSketch(Reference reference) {
		super(reference);
	}
}
