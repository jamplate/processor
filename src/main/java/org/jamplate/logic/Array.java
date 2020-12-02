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
package org.jamplate.logic;

import org.cufy.preprocessor.link.Logic;
import org.cufy.preprocessor.invoke.Memory;
import org.cufy.preprocessor.AbstractParser;
import org.jamplate.util.Groups;
import org.cufy.preprocessor.Poll;
import org.jamplate.util.Strings;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * An array of logics with a specific order.
 *
 * @author LSafer
 * @version 0.0.b
 * @since 0.0.b ~2020.10.05
 */
public class Array extends AbstractGroup {
	/**
	 * Targets {@code array} statements.
	 * <ul>
	 *     <li>{@code OPEN} the open operator</li>
	 *     <li>{@code VALUE} the content between the open and close operators</li>
	 *     <li>{@code CLOSE} the close operator</li>
	 * </ul>
	 * <p>
	 * Note: made to be used with {@link Strings#group(String, Pattern[], Pattern[], Pattern)}.
	 *
	 * @since 0.0.b ~2020.10.07
	 */
	public static final Pattern PATTERN = Pattern.compile("(?<OPEN>\\[)(?<CONTENT>.*)(?<CLOSE>\\])");
	/**
	 * Splits {@code array} statements.
	 * <p>
	 * Note: made to be used with {@link Strings#split(String, Pattern[], Pattern[], Pattern)}.
	 *
	 * @since 0.0.b ~2020.10.07
	 */
	public static final Pattern SPLITTER = Pattern.compile("(?<SPLITTER>[,])");

	/**
	 * Construct a new array with its {@link #logics} not initialized.
	 *
	 * @since 0.0.b ~2020.10.05
	 */
	public Array() {
	}

	/**
	 * Construct a new array with its {@link #logics} set to the given {@code logics}.
	 *
	 * @param logics the logic of the constructed array.
	 * @throws NullPointerException if the given {@code logics} is null.
	 * @since 0.0.b ~2020.10.05
	 */
	public Array(List logics) {
		super(logics);
	}

	@Override
	public Object evaluate(Memory memory) {
		return Collections.unmodifiableList(this.logics);
	}

	@Override
	public String evaluateString(Memory memory) {
		return this.logics.stream()
				.filter(Objects::nonNull)
				.map(logic -> logic.evaluateString(memory))
				.collect(Collectors.joining());
	}

	@Override
	public String toString() {
		StringJoiner joiner = new StringJoiner(", ", "[", "]");

		for (Logic logic : this.logics)
			joiner.add(logic.toString());

		return joiner.toString();
	}

	/**
	 * A parser that parses text into {@link Array}s.
	 *
	 * @author LSafer
	 * @version 0.0.b
	 * @since 0.0.b ~2020.10.07
	 */
	public static class Parser extends AbstractParser.AbstractVote<Array> {
		/**
		 * The patterns of the areas to be ignored in parsed strings.
		 *
		 * @since 0.0.b ~2020.10.07
		 */
		protected Pattern[] escapables;
		/**
		 * The nestable areas to be ignored.
		 *
		 * @since 0.0.b ~2020.10.07
		 */
		protected Pattern[] nestables;

		/**
		 * Construct a new array parser that escapables the areas that matches any of the given
		 * {@code escapables}. And escapables when in any of the given {@code nestables} nestable
		 * areas.
		 * <p>
		 * Make sure the given {@code nestables} does not include an instance of this class! since
		 * this class already takes care of itself!.
		 *
		 * @param escapables the areas to be escaped.
		 * @param nestables  the nestable areas to be escaped.
		 * @throws NullPointerException if the given {@code escapables} or {@code nestables} is
		 *                              null.
		 * @since 0.0.b ~2020.10.07
		 */
		public Parser(Pattern[] escapables, Pattern[] nestables) {
			Objects.requireNonNull(escapables, "escapables");
			Objects.requireNonNull(nestables, "nestables");
			this.escapables = escapables;
			this.nestables = nestables;
		}

		@Override
		public boolean link(List poll, org.cufy.preprocessor.Parser parser) {
			return Poll.iterate(poll, Groups.link(Array.class, parser));
		}

		@Override
		public boolean parse(List poll, org.cufy.preprocessor.Parser parser) {
			return Poll.iterate(poll, Groups.parse(Array.class, parser));
		}

		@Override
		public boolean process(List poll, org.cufy.preprocessor.Parser parser) {
			return Poll.iterate(poll, Groups.process(Array.class, parser));
		}

		@Override
		public boolean process(List poll) {
			return Poll.iterate(poll, Groups.process(
					this.escapables,
					this.nestables,
					Array.PATTERN,
					Array.SPLITTER,
					Array::new
			));
		}
	}
}
//			Polls.iterateInstances(
//					poll,
//					Array.class,
//					Groups.linkGroup(parser)
//			);
//					(iterator, matcher) -> {
//						String open = matcher.group("OPEN");
//						String value = matcher.group("VALUE");
//						String close = matcher.group("CLOSE");
//
//						List subpoll = Polls.create();
//
//						for (String element : Strings.split(
//								value,
//								this.escapes,
//								this.nests,
//								Array.SPLITTER
//						))
//							if (!element.matches("\\s*"))
//								subpoll.add(element);
//
//						iterator.set(new Array(subpoll));
//					}
//			Polls.iterate(
//					poll,
//					this.escapes,
//					Array.OPEN,
//					Array.CLOSE,
//					(iterator, matcher) -> {
//						String open = matcher.group("OPEN");
//						String value = matcher.group("VALUE");
//						String close = matcher.group("CLOSE");
//
//						List subpoll = Polls.create();
//
//						for (String element : Strings.split(value, this.escapes, Array.SPLITTER))
//							if (!element.matches("\\s*"))
//								subpoll.add(element);
//
//						iterator.set(new Array(subpoll));
//					}
//			);
