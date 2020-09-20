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

/**
 * A parser that accepts a {@link String} and convert it into {@link T}.
 *
 * @param <T> the type of the elements parsed by this parser.
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2020.09.20
 */
public interface Parser<T> {
	/**
	 * Parse the given {@code string}.
	 *
	 * @param string the string to be parsed.
	 * @return an element from parsing the given {@code string}.
	 * @throws NullPointerException   if the given {@code string} is null.
	 * @throws ParseException if any parse exception occurs.
	 * @since 0.0.1 ~2020.09.20
	 */
	T parse(String string);
}
