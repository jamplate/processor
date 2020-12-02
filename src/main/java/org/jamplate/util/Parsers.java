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
package org.jamplate.util;

import org.cufy.preprocessor.Parser;
import org.cufy.preprocessor.Poll;

import java.util.ListIterator;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Common utilities for {@link Parser}s.
 *
 * @author LSafer
 * @version 0.0.b
 * @since 0.0.b ~2020.10.04
 */
public final class Parsers {
	public static BiPredicate<ListIterator, Object> parse(Pattern pattern, BiPredicate<ListIterator, Matcher> predicate) {
		Objects.requireNonNull(pattern, "pattern");
		Objects.requireNonNull(predicate, "predicate");
		return (iterator, object) -> {
			if (object instanceof String) {
				String string = (String) object;
				Matcher matcher = pattern.matcher(string);

				//the end of the previous match
				if (matcher.find() && predicate.test(iterator, matcher)) {
					String before = string.substring(0, matcher.start());
					String after = string.substring(matcher.end());

					if (!before.matches("\\s*"))
						Poll.addPrevious(iterator, before);
					if (!after.matches("\\s*"))
						Poll.addNext(iterator, after);

					return true;
				}
			}

			return false;
		};
	}

	public static BiPredicate<ListIterator, Object> process(Pattern[] escapables, Pattern[] nestables, Pattern pattern, BiPredicate<ListIterator, Matcher> predicate) {
		Objects.requireNonNull(escapables, "escapables");
		Objects.requireNonNull(nestables, "nestables");
		Objects.requireNonNull(pattern, "pattern");
		Objects.requireNonNull(predicate, "predicate");
		return (iterator, object) -> {
			if (object instanceof String) {
				String string = (String) object;
				MatchResult result = Strings.group(
						string,
						escapables,
						nestables,
						pattern
				);

				if (result.groupCount() == 0) {
					Matcher matcher = pattern.matcher(result.group());

					if (matcher.find() && predicate.test(iterator, matcher)) {
						String before = string.substring(0, result.start());
						String after = string.substring(result.end());

						if (!before.matches("\\s*"))
							Poll.addPrevious(iterator, before);
						if (!after.matches("\\s*"))
							Poll.addNext(iterator, after);

						return true;
					}
				}
			}

			return false;
		};
	}
}
//	/**
//	 * Determine if the given {@code left} is equals to the given {@code right}.
//	 *
//	 * @param memory the memory where to check the equality of the two logics.
//	 * @param left   the logic in the left.
//	 * @param right  the logic in the right.
//	 * @return true, if the given {@code left} is equals the given {@code right}.
//	 * @throws NullPointerException if the given {@code memory} is null.
//	 * @since 0.0.1 ~2020.09.17
//	 */
//	public static boolean equals(Memory memory, Logic left, Logic right) {
//		Objects.requireNonNull(memory, "memory");
//
//		if (left == right)
//			return true;
//		if (left == null || right == null)
//			return false;
//
//		String leftString = left.evaluateString(memory);
//		String rightString = right.evaluateString(memory);
//
//		return Objects.equals(
//				leftString,
//				rightString
//		);
//	}

//	/**
//	 * Evaluate the given {@code left} and {@code right}. Then, join the resultant strings. If the
//	 * resultant strings are double-parsable, then the returned string will be the string value of
//	 * the sum of the parsed doubles. Otherwise, the result of joining the resultant strings will be
//	 * returned.
//	 *
//	 * @param memory the memory to evaluate with.
//	 * @param left   the logic at the left.
//	 * @param right  the logic at the right.
//	 * @return the result of joining the evaluated values of the given logics.
//	 * @throws NullPointerException if the given {@code memory} is null.
//	 * @since 0.0.b ~2020.10.04
//	 */
//	public static Object join(Memory memory, Logic left, Logic right) {
//		Objects.requireNonNull(memory, "memory");
//
//		if (left == null && right == null)
//			return "";
//		if (left == null)
//			return right.evaluate(memory);
//		if (right == null)
//			return left.evaluate(memory);
//
//		Object leftEvaluated = left.evaluate(memory);
//		Object rightEvaluated = right.evaluate(memory);
//
//		try {
//			double leftNumber = Double.parseDouble(leftString);
//			double rightNumber = Double.parseDouble(rightString);
//
//			return String.valueOf(leftNumber + rightNumber)
//					.replaceAll("[.]0$", "");
//		} catch (NumberFormatException ignored) {
//			return leftString + rightString;
//		}
//	}

//	public static String[] toArray(Memory memory, Logic logic) {
//		Objects.requireNonNull(memory, "memory");
//
//		if (logic == null)
//			return new String[0];
//
//		if (logic instanceof Group) {
//			List<String> list = new ArrayList();
//
//			for (Logic element : ((Group) logic).getLogics())
//				list.add(element.evaluateString(memory));
//
//			return list.toArray(new String[0]);
//		}
//
//		return new String[]{
//				logic.evaluateString(memory)
//		};
//	}

//	/**
//	 * Evaluate the given {@code logic} to a boolean value.
//	 * <p>
//	 * <ul>
//	 *     <li>if the given {@code logic} is null, {@code false} will be returned.</li>
//	 *     <li>if the given {@code logic} evaluated to {@code ""} or {@code "0"} or {@code "false"}, {@code false} will be returned.</li>
//	 *     <li>if the given {@code logic} evaluated to {@code "1"} or {@code "true"}, {@code true} will be returned.</li>
//	 *     <li>Otherwise, {@code true} will be returned.</li>
//	 * </ul>
//	 *
//	 * @param memory the memory to evaluate with.
//	 * @param logic  the logic to be evaluated to a boolean value.
//	 * @return a boolean value from evaluating the given {@code logic}.
//	 * @throws NullPointerException if the given {@code memory} is null.
//	 * @since 0.0.b ~2020.10.04
//	 */
//	public static boolean toBoolean(Memory memory, Logic logic) {
//		Objects.requireNonNull(memory, "memory");
//
//		if (logic == null)
//			return false;
//
//		String logicString = logic.evaluateString(memory);
//
//		switch (logicString) {
//			case "":
//			case "false":
//				return false;
//			case "true":
//				return true;
//			default:
//				try {
//					double number = Double.parseDouble(logicString);
//
//					if (number == 0)
//						return false;
//				} catch (NumberFormatException ignored) {
//				}
//
//				return true;
//		}
//	}

//	/**
//	 * Evaluate the given {@code logic} to a number value.
//	 * <ul>
//	 *     <li>if the given {@code logic} is null, {@code 0} will be returned.</li>
//	 *     <li>if the given {@code logic} evaluated to {@code ""} or {@code "false"}, {@code 0} will be returned.</li>
//	 *     <li>if the given {@code logic} evaluated to {@code "true"}, {@code 1} will be returned.</li>
//	 *     <li>if the given {@code logic} evaluated to a double-parsable string, the result of parsing the string as a double will be returned.</li>
//	 *     <li>Otherwise, {@code 1} will be returned.</li>
//	 * </ul>
//	 *
//	 * @param memory the memory to evaluate with.
//	 * @param logic  the logic to be evaluated to a number value.
//	 * @return a number value from evaluating the given {@code logic}.
//	 * @throws NullPointerException if the given {@code memory} is null.
//	 * @since 0.0.b ~2020.10.04
//	 */
//	public static double toNumber(Memory memory, Logic logic) {
//		Objects.requireNonNull(memory, "memory");
//
//		if (logic == null)
//			return 0;
//
//		String logicString = logic.evaluateString(memory);
//
//		try {
//			return Double.parseDouble(logicString);
//		} catch (NumberFormatException e) {
//			switch (logicString) {
//				case "":
//				case "false":
//					return 0;
//				case "true":
//				default:
//					return 1;
//			}
//		}
//	}

//	public static String[] toPair(Memory memory, Logic logic) {
//		Objects.requireNonNull(memory, "memory");
//
//		if (logic instanceof Pair) {
//			return new String[]{
//					((Pair) logic).getFirst().evaluateString(memory),
//					((Pair) logic).getSecond().evaluateString(memory)
//			};
//		}
//
//		return new String[]{
//				"",
//				logic.evaluateString(memory)
//		};
//	}

//	public static String toString(Memory memory, Logic logic) {
//		Objects.requireNonNull(memory, "memory");
//
//		if (logic == null)
//			return "";
//
//		Object evaluated = logic.evaluate(memory);
//
//		if (evaluated == null)
//			return "";
//
//		if (evaluated instanceof List) {
//		}
//	}
//					String it = string.substring(start, end);
//
//					iterator.remove();
//					if (!before.matches("\\s*"))
//						iterator.add(before);
//					iterator.add(it);
//					if (!after.matches("\\s*")) {
//						iterator.add(after);
//						iterator.previous();
//					}
//
//					iterator.previous();
//					iterator.next();
//
//
//					iterator.set(predicate.apply(
//
//					));
//					consumer.accept(iterator, matcher);
//
//
//					return true;
