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

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.regex.Pattern;

/**
 * A parentheses is a group of logics that to be processed individually before been processed with
 * its outer group.
 *
 * @author LSafer
 * @version 0.0.b
 * @since 0.0.b ~2020.10.04
 */
public class Parentheses extends AbstractGroup {
	/**
	 * Targets {@code parentheses} statements.
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
	public static final Pattern PATTERN = Pattern.compile("(?<OPEN>[(])(?<CONTENT>.*)(?<CLOSE>[)])");
	/**
	 * Splits {@code parentheses} statements.
	 * <p>
	 * Note: made to be used with {@link Strings#split(String, Pattern[], Pattern[], Pattern)}.
	 *
	 * @since 0.0.b ~2020.10.07
	 */
	public static final Pattern SPLITTER = Pattern.compile("(?<SPLITTER>[,])");

	/**
	 * Construct a new parentheses with its {@link #logics} list not initialized.
	 *
	 * @since 0.0.b ~ 2020.10.04
	 */
	public Parentheses() {

	}

	/**
	 * Construct a new parentheses with its {@link #logics} initialized to the given {@code
	 * logics}.
	 *
	 * @param logics the logics list for the constructed parentheses logic.
	 * @throws NullPointerException if the given {@code logics} is null.
	 * @since 0.0.b ~2020.10.04
	 */
	public Parentheses(List logics) {
		super(logics);
	}

	@Override
	public Object evaluate(Memory memory) {
		Objects.requireNonNull(memory, "memory");
		return Logic.join(memory, this.logics);
	}

	@Override
	public String toString() {
		StringJoiner joiner = new StringJoiner(" ", "(", ")");

		for (Logic logic : this.logics)
			joiner.add(String.valueOf(logic));

		return joiner.toString();
	}

	/**
	 * The default parser class of the {@link Parentheses} logic.
	 *
	 * @author LSafer
	 * @version 0.0.b
	 * @since 0.0.b ~2020.10.04
	 */
	public static class Parser extends AbstractParser.AbstractVote<Parentheses> {
		/**
		 * The patterns of the areas to be ignored in parsed strings.
		 *
		 * @since 0.0.b ~2020.10.04
		 */
		protected final Pattern[] escapables;
		/**
		 * The nestable areas to be ignored.
		 *
		 * @since 0.0.b ~2020.10.07
		 */
		protected final Pattern[] nestables;

		/**
		 * Construct a new array parser that escapables the areas that matches any of the given
		 * {@code escapables}. And escapables when in any of the given {@code nestables} nestable
		 * areas.
		 *
		 * @param escapables the areas to be escaped.
		 * @param nestables  the nestable areas to be escaped.
		 * @throws NullPointerException if the given {@code escapables} or {@code nestables} is
		 *                              null.
		 * @since 0.0.b ~2020.10.04
		 */
		public Parser(Pattern[] escapables, Pattern[] nestables) {
			Objects.requireNonNull(escapables, "escapables");
			Objects.requireNonNull(nestables, "nestables");
			this.escapables = nestables;
			this.nestables = nestables;
		}

		@Override
		public boolean link(List poll, org.cufy.preprocessor.Parser<? super Parentheses> parser) {
			return Poll.iterate(poll, Groups.link(Parentheses.class, parser));
		}

		@Override
		public boolean parse(List poll, org.cufy.preprocessor.Parser<? super Parentheses> parser) {
			return Poll.iterate(poll, Groups.parse(Parentheses.class, parser));
		}

		@Override
		public boolean process(List poll, org.cufy.preprocessor.Parser<? super Parentheses> parser) {
			return Poll.iterate(poll, Groups.process(Parentheses.class, parser));
		}

		@Override
		public boolean process(List poll) {
			return Poll.iterate(poll, Groups.process(
					this.escapables,
					this.nestables,
					Parentheses.PATTERN,
					Parentheses.SPLITTER,
					Parentheses::new
			));
		}
	}
}
//			Polls.iterateGroups(
//					poll,
//					this.escapables,
//					this.nestables,
//					Parentheses.PATTERN,
//					Parentheses.SPLITTER,
//					Groups.process(Parentheses::new)
//			);
//			Polls.iterate(
//					poll,
//					String.class,
//					(iterator, string) -> {
//						MatchResult result = Strings.group(
//								string,
//								this.escapes,
//								Parentheses.OPEN,
//								Parentheses.CLOSE
//						);
//
//						if (result.groupCount() == 0) {
//							int start = result.start();
//							int end = result.end();
//
//							String before = string.substring(0, start);
//							String it = string.substring(start + 1, end - 1);
//							String after = string.substring(end);
//
//							iterator.remove();
//							if (!before.matches("\\s*"))
//								iterator.add(before);
//
//							iterator.add(new Parentheses(new ArrayList(Collections.singleton(it))));
//
//							if (!after.matches("\\s*")) {
//								iterator.add(after);
//								iterator.previous();
//							}
//
//							return true;
//						}
//
//						return false;
//					}
//			);
//	/**
//	 * The parentheses closing character.
//	 *
//	 * @since 0.0.b ~2020.10.06
//	 */
//	public static final char CLOSE = ')';
//	/**
//	 * The parentheses opening character.
//	 *
//	 * @since 0.0.b ~2020.10.06
//	 */
//	public static final char OPEN = '(';
