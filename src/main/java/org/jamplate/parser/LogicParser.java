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
import org.jamplate.logic.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The default jamplate parser that parses {@link String}s into {@link Logic}s.
 *
 * @author LSafer
 * @version 0.0.6
 * @since 0.0.1 ~2020.09.19
 */
public class LogicParser implements PollParser<Logic> {
	/**
	 * A pattern that detects constants in jamplate logic statements. The pattern should be used
	 * after clearing:
	 * <ul>
	 *     <li>parenthesis</li>
	 *     <li>any wrapper syntax that could be escaped in a string literal...</li>
	 * </ul>
	 *
	 * @since 0.0.1 ~2020.09.19
	 */
	protected final Pattern PATTERN_CONSTANT = Pattern.compile("\"([^\"]|((?<=\\\\)\"))*\"");
	/**
	 * A pattern that detects references in jamplate logic statements. The pattern should be used
	 * after clearing:
	 * <ul>
	 *     <li>parenthesis</li>
	 *     <li>constants</li>
	 *     <li>any wrapper syntax...</li>
	 * </ul>
	 *
	 * @since 0.0.1 ~2020.09.19
	 */
	protected final Pattern PATTERN_REFERENCE = Pattern.compile("(\\w|\\d)+");
	/**
	 * A pattern that detects whitespaces or any places that could be a whitespace. The pattern
	 * should be used after clearing:
	 * <ul>
	 *     <li>parenthesis</li>
	 *     <li>constants</li>
	 *     <li>references</li>
	 *     <li>any syntax that could contain a whitespace...</li>
	 * </ul>
	 *
	 * @since 0.0.1 ~2020.09.19
	 */
	protected final Pattern PATTERN_WHITESPACES = Pattern.compile("(\\s+)|[|]|[+]|[&]|(!=)|(==)|(!(?!=))");

	@Override
	public Logic link(List poll) {
		Objects.requireNonNull(poll, "poll");

		if (poll.size() == 1) {
			Object object = poll.get(0);

			if (object instanceof Logic)
				return (Logic) object;
			else
				throw new ParseException("Logic not resolved", String.valueOf(object));
		} else
			throw new ParseException("Logics not resolved to a single logic");
	}

	@Override
	public void parse(List poll) {
		Objects.requireNonNull(poll, "poll");

		//pre-parsing
		this.processSpecialCases(poll);

		//clear all parenthesis
		this.processParenthesis(poll);

		//clear all constants, references
		this.parseConstants(poll);
		this.parseReferences(poll);

		//reform whitespaces
		this.processWhitespaces(poll);

		//clear all negations, equations
		this.parseNegations(poll);
		this.parseAdditions(poll);
		this.parseAnds(poll);
		this.parseOrs(poll);
		this.parseEquations(poll);
	}

	@Override
	public List poll(String string) {
		Objects.requireNonNull(string, "string");
		return new ArrayList(Collections.singleton(string));
	}

	/**
	 * Parse any possible {@link Addition}s in the given {@code poll}. After calling this method, no
	 * addition statement in a {@link String} should remain in the given {@code poll}.
	 * <p>
	 * Clear these before calling this method:
	 * <ul>
	 *     <li>{@link #processParenthesis(List)}</li>
	 *     <li>{@link #parseConstants(List)}</li>
	 *     <li>{@link #parseReferences(List)}</li>
	 *     <li>{@link #processWhitespaces(List)}</li>
	 *     <li>{@link #parseNegations(List)}</li>
	 *     <li>any statement that could be before or next to an addition</li>
	 * </ul>
	 *
	 * @param poll the poll to parse any addition in it.
	 * @throws NullPointerException if the given {@code poll} is null.
	 * @throws ParseException       if any parse exception occurs; if an addition has no element
	 *                              before or after it; if an element before or after an addition
	 *                              has not been resolved into a {@link Logic}.
	 * @since 0.0.6 ~2020.09.22
	 */
	protected void parseAdditions(List poll) {
		Objects.requireNonNull(poll, "poll");

		ListIterator iterator = poll.listIterator();
		while (iterator.hasNext()) {
			Object next = iterator.next();

			if (next instanceof String) {
				String string = (String) next;

				if (string.equals("+"))
					try {
						iterator.remove();
						Logic rightLogic = (Logic) iterator.next();
						iterator.remove();
						Logic leftLogic = (Logic) iterator.previous();
						iterator.remove();

						Logic logic = new Addition(leftLogic, rightLogic);

						iterator.add(logic);
					} catch (NoSuchElementException | ClassCastException e) {
						throw new ParseException("Invalid Addition", e);
					}
			}
		}
	}

	/**
	 * Parse any possible {@link And}s in the given {@code poll}. After calling this method, no and
	 * statement in a {@link String} should remain in the given {@code poll}.
	 * <p>
	 * Clear these before calling this method:
	 * <ul>
	 *     <li>{@link #processParenthesis(List)}</li>
	 *     <li>{@link #parseConstants(List)}</li>
	 *     <li>{@link #parseReferences(List)}</li>
	 *     <li>{@link #processWhitespaces(List)}</li>
	 *     <li>{@link #parseNegations(List)}</li>
	 *     <li>any statement that could be before or next to a and</li>
	 * </ul>
	 *
	 * @param poll the poll to parse any and in it.
	 * @throws NullPointerException if the given {@code poll} is null.
	 * @throws ParseException       if any parse exception occurs; if an and has no element before
	 *                              or after it; if an element before or after an and has not been
	 *                              resolved into a {@link Logic}.
	 * @since 0.0.1 ~2020.09.19
	 */
	protected void parseAnds(List poll) {
		Objects.requireNonNull(poll, "poll");

		ListIterator iterator = poll.listIterator();
		while (iterator.hasNext()) {
			Object next = iterator.next();

			if (next instanceof String) {
				String string = (String) next;

				if (string.equals("&"))
					try {
						iterator.remove();
						Logic rightLogic = (Logic) iterator.next();
						iterator.remove();
						Logic leftLogic = (Logic) iterator.previous();
						iterator.remove();

						Logic logic = new And(leftLogic, rightLogic);

						iterator.add(logic);
					} catch (NoSuchElementException | ClassCastException e) {
						throw new ParseException("Invalid And", e);
					}
			}
		}
	}

	/**
	 * Parse any possible {@link Constant}s in the given {@code poll}. After calling this method, no
	 * constant statement in a {@link String} should remain in the given {@code poll}.
	 * <p>
	 * Clear these before calling this method:
	 * <ul>
	 *     <li>{@link #processParenthesis(List)}</li>
	 *     <li>any wrapper syntax that could be escaped in a string literal...</li>
	 * </ul>
	 *
	 * @param poll the poll to parse any constant in it.
	 * @throws NullPointerException if the given {@code poll} is null.
	 * @throws ParseException       if any parse exception occurs.
	 * @since 0.0.1 ~2020.09.19
	 */
	protected void parseConstants(List poll) {
		Objects.requireNonNull(poll, "poll");

		ListIterator iterator = poll.listIterator();
		while (iterator.hasNext()) {
			Object next = iterator.next();

			if (next instanceof String) {
				String string = (String) next;

				iterator.remove();

				Matcher matcher = this.PATTERN_CONSTANT.matcher(string);
				int i = 0;
				while (matcher.find()) {
					int start = matcher.start();
					int end = matcher.end();

					String before = string.substring(i, start);
					String it = string.substring(start + 1, end - 1);

					if (!before.isEmpty())
						iterator.add(before);

					iterator.add(new Constant(it));

					i = end;
				}

				String after = string.substring(i);
				if (!after.isEmpty())
					iterator.add(after);
			}
		}
	}

	/**
	 * Parse any possible {@link Equation}s in the given {@code poll}. After calling this method, no
	 * equation statement in a {@link String} should remain in the given {@code poll}.
	 * <p>
	 * Clear these before calling this method:
	 * <ul>
	 *     <li>{@link #processParenthesis(List)}</li>
	 *     <li>{@link #parseConstants(List)}</li>
	 *     <li>{@link #parseReferences(List)}</li>
	 *     <li>{@link #processWhitespaces(List)}</li>
	 *     <li>{@link #parseNegations(List)}</li>
	 *     <li>any statement that could be before or next to an equation</li>
	 * </ul>
	 *
	 * @param poll the poll to parse any equation in it.
	 * @throws NullPointerException if the given {@code poll} is null.
	 * @throws ParseException       if any parse exception occurs; if an equation has no element
	 *                              before or after it; if an element before or after an equation
	 *                              has not been resolved into a {@link Logic}.
	 * @since 0.0.1 ~2020.09.19
	 */
	protected void parseEquations(List poll) {
		Objects.requireNonNull(poll, "poll");

		ListIterator iterator = poll.listIterator();
		while (iterator.hasNext()) {
			Object next = iterator.next();

			if (next instanceof String) {
				String string = (String) next;

				if (string.equals("==") || string.equals("!="))
					try {
						iterator.remove();
						Logic rightLogic = (Logic) iterator.next();
						iterator.remove();
						Logic leftLogic = (Logic) iterator.previous();
						iterator.remove();

						Logic logic = new Equation(leftLogic, rightLogic);

						if (string.equals("!="))
							logic = new Negation(logic);

						iterator.add(logic);
					} catch (NoSuchElementException | ClassCastException e) {
						throw new ParseException("Invalid Equation", e);
					}
			}
		}
	}

	/**
	 * Parse any possible {@link Negation} in the given {@code poll}. After calling this method, no
	 * negation statement in a {@link String} should remain in the given {@code poll}.
	 * <p>
	 * Clear these before calling this method:
	 * <ul>
	 *     <li>{@link #processParenthesis(List)}</li>
	 *     <li>{@link #parseConstants(List)}</li>
	 *     <li>{@link #parseReferences(List)}</li>
	 *     <li>{@link #processWhitespaces(List)}</li>
	 *     <li>any statement that could be next to a negation</li>
	 * </ul>
	 *
	 * @param poll the poll to parse any equation in it.
	 * @throws NullPointerException if the given {@code poll} is null.
	 * @throws ParseException       if any parse exception occurs; if a negation has no element
	 *                              after it; if an element after a negation has not been resolved
	 *                              into a {@link Logic}.
	 * @since 0.0.1 ~2020.09.19
	 */
	protected void parseNegations(List poll) {
		Objects.requireNonNull(poll, "poll");

		ListIterator iterator = poll.listIterator();
		while (iterator.hasNext()) {
			Object next = iterator.next();

			if (next instanceof String) {
				String string = (String) next;

				if (string.equals("!"))
					try {
						iterator.remove();

						Logic nextLogic = (Logic) iterator.next();
						Logic logic = new Negation(nextLogic);

						iterator.set(logic);
					} catch (NoSuchElementException | ClassCastException e) {
						throw new ParseException("Invalid Negation", e);
					}
			}
		}
	}

	/**
	 * Parse any possible {@link Or}s in the given {@code poll}. After calling this method, no or
	 * statement in a {@link String} should remain in the given {@code poll}.
	 * <p>
	 * Clear these before calling this method:
	 * <ul>
	 *     <li>{@link #processParenthesis(List)}</li>
	 *     <li>{@link #parseConstants(List)}</li>
	 *     <li>{@link #parseReferences(List)}</li>
	 *     <li>{@link #processWhitespaces(List)}</li>
	 *     <li>{@link #parseNegations(List)}</li>
	 *     <li>any statement that could be before or next to an or</li>
	 * </ul>
	 *
	 * @param poll the poll to parse any or in it.
	 * @throws NullPointerException if the given {@code poll} is null.
	 * @throws ParseException       if any parse exception occurs; if an or has no element before or
	 *                              after it; if an element before or after an or has not been
	 *                              resolved into a {@link Logic}.
	 * @since 0.0.1 ~2020.09.19
	 */
	protected void parseOrs(List poll) {
		Objects.requireNonNull(poll, "poll");

		ListIterator iterator = poll.listIterator();
		while (iterator.hasNext()) {
			Object next = iterator.next();

			if (next instanceof String) {
				String string = (String) next;

				if (string.equals("|"))
					try {
						iterator.remove();
						Logic rightLogic = (Logic) iterator.next();
						iterator.remove();
						Logic leftLogic = (Logic) iterator.previous();
						iterator.remove();

						Logic logic = new Or(leftLogic, rightLogic);

						iterator.add(logic);
					} catch (NoSuchElementException | ClassCastException e) {
						throw new ParseException("Invalid Equation", e);
					}
			}
		}
	}

	/**
	 * Parse any possible {@link Reference} in the given {@code poll}. After calling this method, no
	 * reference statement in a {@link String} should remain in the given {@code poll}.
	 * <P>
	 * Clear these before calling this method:
	 * <ul>
	 *     <li>{@link #processParenthesis(List)}</li>
	 *     <li>{@link #parseConstants(List)}</li>
	 *     <li>any wrapper syntax...</li>
	 * </ul>
	 *
	 * @param poll the to parse any reference in it.
	 * @throws NullPointerException if the given {@code poll} is null.
	 * @throws ParseException       if any parse exception occurs.
	 * @since 0.0.1 ~2020.09.19
	 */
	protected void parseReferences(List poll) {
		Objects.requireNonNull(poll, "poll");

		ListIterator iterator = poll.listIterator();
		while (iterator.hasNext()) {
			Object next = iterator.next();

			if (next instanceof String) {
				String string = (String) next;

				iterator.remove();

				Matcher matcher = this.PATTERN_REFERENCE.matcher(string);
				int i = 0;
				while (matcher.find()) {
					int start = matcher.start();
					int end = matcher.end();

					String before = string.substring(i, start);
					String it = string.substring(start, end);

					if (!before.isEmpty())
						iterator.add(before);

					iterator.add(new Reference(it));

					i = end;
				}

				String after = string.substring(i);
				if (!after.isEmpty())
					iterator.add(after);
			}
		}
	}

	/**
	 * Parse any possible logic inside a parenthesis independently. After calling this method, no
	 * statements in parenthesis in a {@link String} should remain in the given {@code poll}.
	 *
	 * @param poll the poll to parse any equation in it.
	 * @throws NullPointerException if the given {@code poll} is null.
	 * @throws ParseException       if any parse exception occurs.
	 * @since 0.0.1 ~2020.09.19
	 */
	protected void processParenthesis(List poll) {
		Objects.requireNonNull(poll, "poll");

		ListIterator iterator = poll.listIterator();
		while (iterator.hasNext()) {
			Object next = iterator.next();

			if (next instanceof String) {
				String string = (String) next;

				String[] array = this.parenthesis(string);

				if (array[1] != null) {
					iterator.remove();

					if (array[0] != null)
						//already processed!
						iterator.add(array[0]);

					iterator.add(this.parse(array[1]));

					if (array[2] != null) {
						List list = new ArrayList(Collections.singleton(array[2]));
						this.processParenthesis(list);

						for (Object object : list)
							iterator.add(object);
					}
				}
			}
		}
	}

	/**
	 * Process all special cases.
	 * <p>
	 * The special cases:
	 * <ul>
	 *     <li>Empty strings</li>
	 * </ul>
	 *
	 * @param poll the poll to solve the special cases in it.
	 * @throws NullPointerException if the given {@code poll} is null.
	 * @since 0.0.3 ~2020.09.21
	 */
	protected void processSpecialCases(List poll) {
		Objects.requireNonNull(poll, "poll");

		ListIterator iterator = poll.listIterator();
		while (iterator.hasNext()) {
			Object object = iterator.next();

			if (object instanceof String) {
				String string = (String) object;

				if (string.trim().isEmpty())
					iterator.set(new Constant(string));
			}
		}
	}

	/**
	 * Rearrange the whitespaces in the {@link String}s in the given {@code poll}.
	 * <p>
	 * Clear these before clearing this method:
	 * <ul>
	 *     <li>{@link #processParenthesis(List)}</li>
	 *     <li>{@link #parseConstants(List)}</li>
	 *     <li>{@link #parseReferences(List)}</li>
	 *     <li>any syntax that could contain a whitespace...</li>
	 * </ul>
	 *
	 * @param poll the poll to rearrange the whitespaces in it.
	 * @throws NullPointerException if the given {@code poll} is null.
	 * @throws ParseException       if any parse exception occurs.
	 * @since 0.0.1 ~2020.09.19
	 */
	protected void processWhitespaces(List poll) {
		Objects.requireNonNull(poll, "poll");

		ListIterator iterator = poll.listIterator();
		while (iterator.hasNext()) {
			Object next = iterator.next();

			if (next instanceof String) {
				String string = (String) next;

				iterator.remove();

				Matcher matcher = this.PATTERN_WHITESPACES.matcher(string);
				int i = 0;
				while (matcher.find()) {
					int start = matcher.start();
					int end = matcher.end();

					String before = string.substring(i, start).trim();
					String it = string.substring(start, end).trim();

					if (!before.isEmpty())
						iterator.add(before);
					if (!it.isEmpty())
						iterator.add(it);

					i = end;
				}

				String after = string.substring(i).trim();
				if (!after.isEmpty())
					iterator.add(after);
			}
		}
	}

	/**
	 * Get the first index of the given {@code c} after the given {@code i} in the given {@code s},
	 * ignoring the char if it is between literal areas.
	 *
	 * @param s the s to search in.
	 * @param c the character to search for.
	 * @param i the index to start searching from in the given {@code s}.
	 * @return the first index of the given {@code c} in the given {@code s} starting from the given
	 *        {@code i}.
	 * @throws NullPointerException if the given {@code s} is null.
	 * @since 0.0.1 ~2020.09.19
	 */
	private int indexOf(String s, char c, int i) {
		Objects.requireNonNull(s, "s");

		List<int[]> literals = new ArrayList();
		Matcher matcher = this.PATTERN_CONSTANT.matcher(s);
		while (matcher.find())
			literals.add(new int[]{
					matcher.start(),
					matcher.end()
			});

		while (true) {
			int index = s.indexOf(c, i);

			if (literals.stream().anyMatch(area -> index > area[0] && index < area[1])) {
				i = index + 1;
				continue;
			}

			return index;
		}
	}

	/**
	 * If possible, split the given {@code s} into three strings. The string before the first
	 * parenthesis, the string inside the first parenthesis, the string after the first
	 * parenthesis.
	 *
	 * @param s the string to be split.
	 * @return the given string split into three strings. (before, first, after), each string could
	 * 		be null if it do not exist.
	 * @throws NullPointerException if the given {@code s} is null.
	 * @since 0.0.1 ~2020.09.19
	 */
	private String[] parenthesis(String s) {
		Objects.requireNonNull(s, "s");

		int i = this.indexOf(s, '(', 0);
		int j = i;
		int k = i;
		while (!(j < k || k == -1)) {
			j = this.indexOf(s, ')', j + 1);
			k = this.indexOf(s, '(', k + 1);
		}

		if (i == -1 || j == -1)
			return new String[3];
		else {
			String before = s.substring(0, i).trim();
			String it = s.substring(i + 1, j).trim();
			String after = s.substring(j + 1).trim();

			return new String[]{
					before.isEmpty() ? null : before,
					it.isEmpty() ? null : it,
					after.isEmpty() ? null : after
			};
		}
	}
}
