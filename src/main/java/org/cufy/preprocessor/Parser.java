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

import java.util.List;
import java.util.Objects;

/**
 * A parser that parses strings into the type {@link T}. The parser uses a {@code poll} {@link List}
 * that contains parsed and non-parsed elements.
 *
 * @param <T> the type of the elements to be parsed.
 * @author LSafer
 * @version 0.0.b
 * @since 0.0.1 ~2020.09.20
 */
public interface Parser<T> {
	/**
	 * Parse the given {@code string}.
	 *
	 * @param string the string to be parsed.
	 * @return an element from parsing the given {@code string}.
	 * @throws NullPointerException if the given {@code string} is null.
	 * @throws ParseException       if any parse exception occurs.
	 * @since 0.0.1 ~2020.09.20
	 */
	default T parse(String string) {
		Objects.requireNonNull(string, "string");
		List poll = this.poll(string);

		this.process(poll);
		this.parse(poll);
		this.link(poll);

		return this.make(poll);
	}

	/**
	 * Return the votes list of this parser.
	 *
	 * @return the votes list of this parser. Or null if it has not been set yet.
	 * @since 0.0.b ~2020.10.04
	 */
	List<Vote<? extends T>> getVotes();

	/**
	 * Link the given {@code poll} into a single parsed element. This method should be invoked after
	 * {@link #parse(List)}.
	 *
	 * @param poll the poll to be linked.
	 * @return true, if the given {@code poll} has changed due to this method's invocation.
	 * @throws NullPointerException if the given {@code poll} is null.
	 * @throws ParseException       if any parse exception occurs.
	 * @since 0.0.1 ~2020.09.20
	 */
	boolean link(List poll);

	/**
	 * Make a parsed single object from the given resolved {@code poll}. The given poll should be
	 * {@link #process(List) processed}, {@link #parse(List) parsed} and {@link #link(List)
	 * linked}.
	 *
	 * @param poll the poll to make the object from.
	 * @return an element from the given {@code poll}.
	 * @throws NullPointerException if the given {@code poll} is null.
	 * @throws ParseException       if any parse exception occurs.
	 * @since 0.0.b ~2020.10.02
	 */
	T make(List poll);

	/**
	 * Parse any {@link String} in the given {@code poll}. After calling this method, no string
	 * should remain in the given {@code poll}.
	 *
	 * @param poll the poll to parse any string in it.
	 * @return true, if the given {@code poll} has changed due to this method's invocation.
	 * @throws NullPointerException if the given {@code poll} is null.
	 * @throws ParseException       if any parse exception occurs.
	 * @since 0.0.1 ~2020.09.20
	 */
	boolean parse(List poll);

	/**
	 * Create a new poll for the given {@code string}.
	 *
	 * @param string the string to be parsed.
	 * @return a new poll to be used for the methods {@link #link(List)} and {@link #parse(List)}.
	 * @throws NullPointerException if the given {@code string} is null.
	 * @throws ParseException       if any aspect of the given {@code string} is rejected.
	 * @since 0.0.1 ~2020.09.20
	 */
	List poll(String string);

	/**
	 * Process the raw strings in the given {@code poll} before parsing them.
	 *
	 * @param poll the poll to process the strings in it.
	 * @return true, if the given {@code poll} has changed due to this method's invocation.
	 * @throws NullPointerException if the given {@code poll} is null.
	 * @throws ParseException       if any parse exception occurs.
	 * @since 0.0.b ~2020.09.29
	 */
	boolean process(List poll);

	/**
	 * Set the votes list of this parser to the given {@code votes}.
	 *
	 * @param votes the votes to be set as the votes of this parser.
	 * @throws NullPointerException  if the given {@code votes} is null.
	 * @throws IllegalStateException if the votes of this parser has already been set.
	 * @since 0.0.b ~2020.10.04
	 */
	void setVotes(List<Vote<? extends T>> votes);

	/**
	 * A mini parser that could be attached to a {@link Parser} to have a vote when any essential
	 * operation get called.
	 *
	 * @param <T> the type of the elements parsed by this vote.
	 * @author LSafer
	 * @version 0.0.b
	 * @since 0.0.b ~2020.09.29
	 */
	interface Vote<T> {
		/**
		 * A method that will be invoked when a {@link Parser} (that has this vote) invokes its
		 * {@link Parser#link(List)} method.
		 *
		 * @param poll the poll to link the elements in it depending on this parser.
		 * @return true, if the given {@code poll} has changed due to this method's invocation.
		 * @throws NullPointerException if the given {@code poll} is null.
		 * @throws ParseException       if any parse exception occurs.
		 * @since 0.0.b ~2020.09.29
		 */
		default boolean link(List poll) {
			Objects.requireNonNull(poll, "poll");
			return false;
		}

		/**
		 * A method that will be invoked when a {@link Parser} (that has this vote) invokes its
		 * {@link Parser#link(List)} method.
		 * <p>
		 * This method should be invoked after {@link #link(List)}.
		 *
		 * @param poll   the poll to link the elements in it depending on this parser.
		 * @param parser the parser that has invoked this method.
		 * @return true, if the given {@code poll} has changed due to this method's invocation.
		 * @throws NullPointerException if the given {@code poll} is null.
		 * @throws ParseException       if any parse exception occurs.
		 * @since 0.0.b ~2020.09.29
		 */
		default boolean link(List poll, Parser<? super T> parser) {
			Objects.requireNonNull(poll, "poll");
			Objects.requireNonNull(parser, "parser");
			return false;
		}

		/**
		 * A method that will be invoked when a {@link Parser} (that has this vote) invokes its
		 * {@link Parser#parse(List)} method.
		 *
		 * @param poll the poll to parse the elements in it depending on this parser.
		 * @return true, if the given {@code poll} has changed due to this method's invocation.
		 * @throws NullPointerException if the given {@code poll} is null.
		 * @throws ParseException       if any parse exception occurs.
		 * @since 0.0.b ~2020.09.29
		 */
		default boolean parse(List poll) {
			Objects.requireNonNull(poll, "poll");
			return false;
		}

		/**
		 * A method that will be invoked when a {@link Parser} (that has this vote) invokes its
		 * {@link Parser#parse(List)} method.
		 * <p>
		 * This method should be invoked after {@link #parse(List)}.
		 *
		 * @param poll   the poll to parse the elements in it depending on this parser.
		 * @param parser the parser that has invoked this method.
		 * @return true, if the given {@code poll} has changed due to this method's invocation.
		 * @throws NullPointerException if the given {@code poll} is null.
		 * @throws ParseException       if any parse exception occurs.
		 * @since 0.0.b ~2020.09.29
		 */
		default boolean parse(List poll, Parser<? super T> parser) {
			Objects.requireNonNull(poll, "poll");
			Objects.requireNonNull(parser, "parser");
			return false;
		}

		/**
		 * A method that will be invoked when a {@link Parser} (that has this vote) invokes its
		 * {@link Parser#process(List)} method.
		 *
		 * @param poll the poll to process the elements in it depending on this parser.
		 * @return true, if the given {@code poll} has changed due to this method's invocation.
		 * @throws NullPointerException if the given {@code poll} is null.
		 * @throws ParseException       if any parse exception occurs.
		 * @since 0.0.b ~2020.09.29
		 */
		default boolean process(List poll) {
			Objects.requireNonNull(poll, "poll");
			return false;
		}

		/**
		 * A method that will be invoked when a {@link Parser} (that has this vote) invokes its
		 * {@link Parser#process(List)} method.
		 * <p>
		 * This method should be invoked after {@link #process(List)}.
		 *
		 * @param poll   the poll to process the elements in it depending on this parser.
		 * @param parser the parser that has invoked this method.
		 * @return true, if the given {@code poll} has changed due to this method's invocation.
		 * @throws NullPointerException if the given {@code poll} is null.
		 * @throws ParseException       if any parse exception occurs.
		 * @since 0.0.b ~2020.09.29
		 */
		default boolean process(List poll, Parser<? super T> parser) {
			Objects.requireNonNull(poll, "poll");
			Objects.requireNonNull(parser, "parser");
			return false;
		}
	}
}
