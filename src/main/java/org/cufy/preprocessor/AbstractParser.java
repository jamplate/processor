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
package org.cufy.preprocessor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * An abstraction for the {@link Parser} class. Implementing the essential methods any poll parser
 * would implement.
 *
 * @param <T> the type of the elements to be parsed.
 * @author LSafer
 * @version 0.0.b
 * @since 0.0.b ~2020.10.02
 */
public abstract class AbstractParser<T> implements Parser<T> {
	/**
	 * A list of the votes of this parser.
	 *
	 * @since 0.0.b ~2020.10.02
	 */
	protected List<Vote<? extends T>> votes;

	/**
	 * Construct a new parser with its {@link #votes} not initialized.
	 *
	 * @since 0.0.b ~2020.10.04
	 */
	public AbstractParser() {
	}

	/**
	 * Construct a new parser with the given {@code votes}.
	 *
	 * @param votes the votes the constructed parser will use.
	 * @throws NullPointerException if the given {@code votes} is null.
	 * @since 0.0.b ~2020.10.02
	 */
	public AbstractParser(List<Vote<? extends T>> votes) {
		Objects.requireNonNull(votes, "votes");
		this.votes = votes;
	}

	@Override
	public List<Vote<? extends T>> getVotes() {
		return this.votes;
	}

	@Override
	public boolean link(List poll) {
		Objects.requireNonNull(poll, "poll");
		boolean change = false;

		for (Vote<? extends T> vote : this.votes)
			change |= vote.link(poll) |
					  vote.link(poll, this);

		return change;
	}

	@Override
	public boolean parse(List poll) {
		Objects.requireNonNull(poll, "poll");
		boolean change = false;

		for (Vote<? extends T> vote : this.votes)
			change |= vote.parse(poll) |
					  vote.parse(poll, this);

		return change;
	}

	@Override
	public List poll(String string) {
		Objects.requireNonNull(string, "string");
		return new ArrayList(Collections.singleton(string));
	}

	@Override
	public boolean process(List poll) {
		Objects.requireNonNull(poll, "poll");
		boolean change = false;

		for (Vote<? extends T> vote : this.votes)
			change |= vote.process(poll) |
					  vote.process(poll, this);

		return change;
	}

	@Override
	public void setVotes(List<Vote<? extends T>> votes) {
		Objects.requireNonNull(votes, "votes");
		if (this.votes == null)
			this.votes = votes;
		else
			throw new IllegalStateException("Poll Parser votes already set!");
	}

	/**
	 * An abstraction for the {@link Vote} class. Implementing the essential methods any vote would
	 * implement.
	 *
	 * @param <T> the type of the elements parsed by this vote.
	 * @author LSafer
	 * @version 0.0.b
	 * @since 0.0.b ~2020.10.02
	 */
	public abstract static class AbstractVote<T> implements Vote<T> {
	}
}
