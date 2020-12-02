package org.cufy.text;

import java.util.Objects;

public abstract class AbstractSource implements Source {
	/**
	 * The end index of this source.
	 *
	 * @since 0.0.c ~2020.11.04
	 */
	protected final int end;
	/**
	 * The parent source of this source. (could be null)
	 *
	 * @since 0.0.c ~2020.11.04
	 */
	protected final Source parent;
	/**
	 * The start index of this source.
	 *
	 * @since 0.0.c ~2020.11.04
	 */
	protected final int start;
	protected String scratch;
	protected String text;

	public AbstractSource(Source parent, int start, int end) {
		Objects.requireNonNull(parent, "parent");
		this.parent = parent;
		this.start = start;
		this.end = end;
	}

	@Override
	public int end() {
		return this.end;
	}

	@Override
	public String getScratch() {
		return this.scratch;
	}

	@Override
	public String getSubstring() {
		return this.substring;
	}

	@Override
	public int start() {
		return this.start;
	}

	@Override
	public String text() {
		return this.text;
	}
}
