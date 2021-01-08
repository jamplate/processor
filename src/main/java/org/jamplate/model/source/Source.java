/*
 *	Copyright 2020 Cufy
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

/**
 * A source is a component that points to a fragment of a source file.
 *
 * @author LSafer
 * @version 0.0.2
 * @since 0.0.2 ~2020.12.25
 */
public interface Source {
	/**
	 * A source equals another object, if that object is a source and has the same {@link #shadow()}, {@link #position()} and {@link #length()} of
	 * this source.
	 *
	 * @return if the given object is a source and equals this source.
	 * @since 0.0.2 ~2021.01.7
	 */
	@Override
	boolean equals(Object other);

	/**
	 * The hashcode of a source is calculated as follows.
	 * <pre>
	 *     hashCode = {@link #shadow()}.{@link Shadow#hashCode() hashCode()} * (int) {@link #length()} + (int) {@link #position()}
	 * </pre>
	 *
	 * @return the hashCode of this source.
	 * @since 0.0.2 ~2021.01.7
	 */
	@Override
	int hashCode();

	/**
	 * Returns a string representation of this source. The source shall follow the below template:
	 * <pre>
	 *     &lt;{@link #shadow() Shadow}&gt; [&lt;{@link #position() Position}&gt;, &lt;{@link #length() Length}&gt;]
	 * </pre>
	 *
	 * @return a string representation of this source.
	 * @since 0.0.2 ~2021.01.6
	 */
	@Override
	String toString();

	/**
	 * Check if the given {@code source} clashes with this source or this source clashes with the given {@code source}.
	 * <br>
	 * A source clashes with another source, if one (and only one) of its bounds get contained between the other source's bounds.
	 *
	 * @param source the source to be checked.
	 * @return true, if the given {@code source} and this source clashes with each other.
	 * @throws NullPointerException if the given {@code source} is null.
	 * @since 0.0.2 ~2021.01.7
	 */
	boolean clashes(Source source);

	/**
	 * Check if the given source is contained in this source.
	 *
	 * @param source the source to be checked.
	 * @return true, if the given {@code source} is a sub-source of this source.
	 * @throws NullPointerException if the given {@code source} is null.
	 * @since 0.0.2 ~2021.01.7
	 */
	boolean contains(Source source);

	/**
	 * Return the content of this source as a string.
	 *
	 * @return the content of this source.
	 * @since 0.0.2 ~2021.01.8
	 */
	String content();

	/**
	 * Return the length of how many characters this source has.
	 *
	 * @return the length of this source.
	 * @since 0.0.2 ~2021.01.4
	 */
	int length();

	/**
	 * Return where this source starts at its {@link #shadow() file}.
	 *
	 * @return the position of this source.
	 * @since 0.0.2 ~2021.01.4
	 */
	int position();

	/**
	 * Returns the file this source is from.
	 *
	 * @return the file this source is from.
	 * @since 0.0.2 ~2020.12.28
	 */
	Shadow shadow();

	/**
	 * Slice this source from the given {@code pos} and limit it with the given {@code len}.
	 *
	 * @param pos the pos where the new slice source will have.
	 * @param len the len of the new slice source.
	 * @return a slice of this source that starts from the given {@code pos} and have the given {@code len}.
	 * @throws IllegalArgumentException  if the given {@code pos} or {@code len} is negative.
	 * @throws IndexOutOfBoundsException if {@code pos + len > this.len()}.
	 * @since 0.0.2 ~2021.01.6
	 */
	Source slice(int pos, int len);

	/**
	 * Slice this source from the given {@code pos} to the end of this {@code source}.
	 *
	 * @param pos the pos where the new slice source will have.
	 * @return a slice of this source that starts from the given {@code pos}.
	 * @throws IllegalArgumentException  if the given {@code pos} is negative.
	 * @throws IndexOutOfBoundsException if {@code pos > this.length()}.
	 * @since 0.0.2 ~2021.01.6
	 */
	Source slice(int pos);
}
