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
package org.jamplate.model.sketch;

import org.jamplate.model.reference.Reference;
import org.jamplate.runtime.diagnostic.Diagnostic;

import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;

/**
 * An abstract of the interface {@link Sketch} that implements the basic functionality of
 * a concrete sketch. (a concrete sketch is a sketch that cannot have inner sketches)
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.01.17
 */
public abstract class AbstractConcreteSketch extends AbstractSketch {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -4837539436780739571L;

	/**
	 * Construct a new sketch with the given {@code reference}. The given source reference
	 * is the reference the constructed sketch will reserve.
	 *
	 * @param reference the source reference of the constructed sketch.
	 * @throws NullPointerException if the given {@code reference} is null.
	 * @since 0.2.0 ~2021.01.17
	 */
	protected AbstractConcreteSketch(Reference reference) {
		super(reference);
	}

	@Override
	public boolean check(int start, int end) {
		return false;
	}

	@Override
	public Iterator<Sketch> iterator() {
		return Collections.emptyIterator();
	}

	@Override
	public void put(Sketch sketch) {
		Objects.requireNonNull(sketch, "sketch");
		if (!this.constructed)
			throw new IllegalStateException("Deserialized Document");

		Diagnostic.appendError("Concrete Sketch Clash", this.reference, sketch.reference());
		throw new UnsupportedOperationException("Sketch.put");
	}
}
