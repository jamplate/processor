/*
 *	Copyright 2020-2021 Cufy
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

import java.util.Comparator;

/**
 * A source is a component that points to a {@code D} source or a fragment of it.
 * <br>
 * Note: sources are built from top to bottom. So, a typical source will store its parent source but never store any sub-source of it.
 *
 * @param <D> the type of the actual source of this source.
 * @author LSafer
 * @version 0.0.2
 * @since 0.0.2 ~2020.12.25
 */
public interface Source<D extends Comparable> {
	/**
	 * The standard source comparator.
	 *
	 * @since 0.0.2 ~2021.01.9
	 */
	Comparator<Source> COMPARATOR = Comparator.<Source, Comparable>comparing(Source::document)
			.thenComparingInt(Source::position)
			.thenComparingInt(s -> s.content().length());

	/**
	 * A shortcut for invoking {@link #content()}{@link CharSequence#length() .length()}.
	 * It is encouraged to use this shortcut when the caller only cares about the length
	 * of the content.
	 * <br>
	 * <br>
	 * Invoking this method SHOULD be the same as invoking:
	 * <pre>
	 *     {@link #content()}{@link CharSequence#length() .length()}
	 * </pre>
	 *
	 * @return the length of the content of this source.
	 * @since 0.0.2 ~2021.01.10
	 */
	default int length() {
		return this.content().length();
	}

	/**
	 * A source equals another object, if that object is a source and has the same {@link
	 * #document()}, {@link #position()} and {@link #content()}{@link String#length()
	 * .length()} of this source.
	 *
	 * @return if the given object is a source and equals this source.
	 * @since 0.0.2 ~2021.01.7
	 */
	@Override
	boolean equals(Object other);

	/**
	 * The hashcode of a source is calculated as follows.
	 * <pre>
	 *     hashCode = {@link #document()}.hashCode() * {@link #content()}{@link String#length() .length()} + {@link #position()}
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
	 *     &lt;{@link #document()}&gt; [&lt;{@link #position()}&gt;, &lt;{@link #content()}{@link String#length() .length()}&gt;]
	 * </pre>
	 *
	 * @return a string representation of this source.
	 * @since 0.0.2 ~2021.01.6
	 */
	@Override
	String toString();

	/**
	 * Return the content of this source as a string. Once a source got constructed, it will capture its content and never change it.
	 *
	 * @return the content of this source.
	 * @since 0.0.2 ~2021.01.8
	 */
	CharSequence content();

	/**
	 * The source document that this source is from.
	 *
	 * @return the document of this source.
	 * @since 0.0.2 ~2021.01.7
	 */
	D document();

	/**
	 * The parent source of this source.
	 *
	 * @return the parent source of this source. Or null if this source is a root source.
	 * @since 0.0.2 ~2021.01.8
	 */
	Source<? extends D> parent();

	/**
	 * Return where this source starts at its {@link #document()} file}.
	 *
	 * @return the position of this source.
	 * @since 0.0.2 ~2021.01.4
	 */
	int position();

	/**
	 * The root source of this source, the source that points to the whole file of this
	 * source.
	 * <br>
	 * Note: it is better to get the root source than to create a new one. The reason is that the root source of this source will have its {@link
	 * #content()} haven't changed since its construction.
	 *
	 * @return the root source of this source. Or this source if this source is a root source.
	 * @since 0.0.2 ~2021.01.8
	 */
	Source<? extends D> root();

	/**
	 * Slice this source from the given {@code pos} to the end of this {@code source}.
	 *
	 * @param pos the pos where the new slice source will have.
	 * @return a slice of this source that starts from the given {@code pos}.
	 * @throws IllegalArgumentException  if the given {@code pos} is negative.
	 * @throws IndexOutOfBoundsException if {@code pos > this.length()}.
	 * @since 0.0.2 ~2021.01.6
	 */
	Source<? extends D> slice(int pos);

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
	Source<? extends D> slice(int pos, int len);
}
