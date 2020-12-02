package org.cufy.text;

import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings("JavaDoc")
public interface Source {
	String available();

	/**
	 * One past the index of the last character of this source is.
	 *
	 * @return the end index of this source.
	 * @since 0.0.c ~2020.11.04
	 */
	int end();

	String getScratch();

	String getSubstring();

	boolean isReserved();

	List<Source> list();

	/**
	 * Return the parent source of this source.
	 *
	 * @return the parent source of this source. Or null if this source the root source (has no
	 * 		parent)
	 */
	@Nullable
	Source parent();

	void reserve(Source source, int start, int end);

	String source();

	/**
	 * The index of the first character of this source.
	 *
	 * @return the start index of this source.
	 * @since 0.0.c ~2020.11.04
	 */
	int start();

	/**
	 * The text of the context this source is from.
	 *
	 * @return the context text.
	 * @since 0.0.c ~2020.11.04
	 */
	String text();
}
