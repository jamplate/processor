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
package org.jamplate.impl.model.sketch;

import org.jamplate.Visitor;
import org.jamplate.model.sketch.Sketch;
import org.jamplate.model.source.Source;

import java.util.*;

/**
 * An abstract of the interface {@link Sketch} that implements the basic functionality of a sketch.
 *
 * @author LSafer
 * @version 0.0.2
 * @since 0.0.2 ~2021.01.07
 */
public abstract class AbstractSketch implements Sketch {
	/**
	 * The length of this sketch.
	 *
	 * @since 0.0.2 ~2021.01.7
	 */
	protected final long length;
	/**
	 * A map of reserved indices in this sketch.
	 * <br>
	 * Each element is as the following:
	 * <pre>
	 *     new long[]{
	 *     		start, //inclusive
	 *     		end    //exclusive
	 *     }
	 *     :
	 *     sketch
	 * </pre>
	 *
	 * @since 0.0.2 ~2021.01.7
	 */
	protected final Map<long[], Sketch> reserved = new HashMap<>();
	/**
	 * The sketches in this sketch.
	 *
	 * @since 0.0.2 ~2021.01.7
	 */
	protected final Set<Sketch> sketches = new HashSet<>();
	/**
	 * The source of this sketch.
	 *
	 * @since 0.0.2 ~2021.01.7
	 */
	protected final Source source;

	/**
	 * Construct a new abstract sketch.
	 *
	 * @param source the source of this abstract sketch.
	 * @throws NullPointerException if the given {@code source} is null.
	 * @since 0.0.2 ~2021.01.7
	 */
	protected AbstractSketch(Source source) {
		Objects.requireNonNull(source, "source");
		this.source = source;
		this.length = source.length();
	}

	/**
	 * Check if the area between the given {@code s} (inclusive) and the given {@code e} (exclusive) does NOT clash with the area between the given
	 * {@code start} (inclusive) and the given {@code end} exclusive.
	 * <br>
	 * Note: validating the given arguments is the responsibility of the caller.
	 *
	 * @param start the start of the superior area (inclusive).
	 * @param end   the end of the superior area (exclusive).
	 * @param s     the start of the to-be-checked area (inclusive).
	 * @param e     the end of the to-be-checked area (exclusive).
	 * @return true, if the area {@code [s, e)} does NOT clash with the area {@code [start, end)}.
	 * @since 0.0.2 ~2021.01.7
	 */
	private static boolean check(long start, long end, long s, long e) {
		return (s < start || s >= end) && (e <= start || e > end);
	}

	@Override
	public boolean check(long pos, long len) {
		long tip = pos + len;
		return this.reserved.keySet().stream().anyMatch(area ->
				AbstractSketch.check(area[0], area[1], pos, tip)
		);
	}

	@Override
	public long length() {
		return this.length;
	}

	@Override
	public void reserve(Sketch sketch, long pos, long len) {
		Objects.requireNonNull(sketch, "sketch");
		if (pos < 0L)
			throw new IllegalArgumentException("negative position");
		if (len < 0L)
			throw new IllegalArgumentException("negative length");
		if (pos + len > this.length)
			throw new IndexOutOfBoundsException("pos + len > this.length()");
		if (!this.check(pos, len))
			throw new IllegalArgumentException("The area [" + pos + ", " + len + "] clashes with another area");

		this.reserved.put(new long[]{pos, pos + len}, sketch);

	}

	@Override
	public void put(Sketch sketch) {
		Objects.requireNonNull(sketch, "sketch");
		if (!this.source.contains(sketch.source()))
			throw new IllegalArgumentException("irrelevant sketch");
		this.sketches.add(sketch);
	}

	@Override
	public Source source() {
		return this.source;
	}

	@Override
	public void visit(Visitor<Sketch> visitor) {
		this.reserved.entrySet()
				.stream()
				.sorted(Comparator.comparingLong(e -> e.getKey()[0]))
				.map(Map.Entry::getValue)
				.distinct()
				.forEach(visitor::visit);
	}
}
