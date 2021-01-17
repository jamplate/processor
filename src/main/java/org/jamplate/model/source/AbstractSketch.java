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
package org.jamplate.model.source;

import java.util.Objects;

/**
 * An abstract for the interface {@link Sketch} implementing the very basic functionality
 * of a sketch.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.01.17
 */
public abstract class AbstractSketch implements Sketch {
	/**
	 * The source of this sketch. The source this sketch is reserving.
	 *
	 * @since 0.2.0 ~2021.01.12
	 */
	protected final Source source;

	/**
	 * Construct a new sketch for the given {@code source}. The given source is the source
	 * the constructed sketch will reserve.
	 *
	 * @param source the source of the constructed sketch.
	 * @throws NullPointerException if the given {@code source} is null.
	 * @since 0.2.0 ~2021.01.17
	 */
	protected AbstractSketch(Source source) {
		Objects.requireNonNull(source, "source");
		this.source = source;
	}

	@Override
	public boolean equals(Object object) {
		return object == this;
	}

	@Override
	public int hashCode() {
		return super.hashCode() + this.getClass().hashCode();
	}

	@Override
	public Source source() {
		return this.source;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " (" + this.source + ")";
	}
}
