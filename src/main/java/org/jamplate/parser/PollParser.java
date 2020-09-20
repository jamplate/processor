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
package org.jamplate.parser;

import org.jamplate.ParseException;

import java.util.List;
import java.util.Objects;

/**
 * A parser that parses strings into the type {@link T}. The parser uses a {@code poll} {@link List}
 * that contains parsed and non-parsed elements.
 *
 * @param <T> the type of the elements to be parsed.
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2020.09.20
 */
public interface PollParser<T> extends Parser<T> {
	@Override
	default T parse(String string) {
		Objects.requireNonNull(string, "string");
		List poll = this.poll(string);
		this.parse(poll);
		return this.link(poll);
	}

	/**
	 * Link the given {@code poll} into a single parsed element. This method should be invoked after
	 * {@link #parse(List)}.
	 *
	 * @param poll the poll to be linked.
	 * @return an element from linking the given {@code poll}.
	 * @throws ParseException if any parse exception occurs.
	 * @since 0.0.1 ~2020.09.20
	 */
	T link(List poll);

	/**
	 * Parse any {@link String} in the given {@code poll}. After calling this method, no string
	 * should remain in the given {@code poll}.
	 *
	 * @param poll the poll to parse any string in it.
	 * @throws NullPointerException   if the given {@code poll} is null.
	 * @throws ParseException if any parse exception occurs.
	 * @since 0.0.1 ~2020.09.20
	 */
	void parse(List poll);

	/**
	 * Create a new poll for the given {@code string}.
	 *
	 * @param string the string to be parsed.
	 * @return a new poll to be used for the methods {@link #link(List)} and {@link #parse(List)}.
	 * @throws NullPointerException   if the given {@code string} is null.
	 * @throws ParseException if any aspect of the given {@code string} is rejected.
	 * @since 0.0.1 ~2020.09.20
	 */
	List poll(String string);
}
