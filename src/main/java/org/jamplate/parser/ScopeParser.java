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
import org.jamplate.logic.Logic;
import org.jamplate.scope.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The default jamplate parser that parses {@link String}s into {@link Scope}s.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2020.09.19
 */
@SuppressWarnings("JavaDoc")
public class ScopeParser implements PollParser<Scope> {
	protected static final Pattern COMMON_PATTERN_LOGIC_VALUE = Pattern.compile("^\\s*(?<LOGIC>(\\S.*\\S)|(\\S))\\s*$");
	protected static final Pattern COMMON_PATTERN_NON_VALUE = Pattern.compile("^\\s*$");
	protected static final Pattern COMMON_PATTERN_PAIR_VALUE = Pattern.compile("\\s*(?<KEY>\\S+)\\s*:\\s*(?<LOGIC>\\S+)\\s*");

	/**
	 * A pattern that catches jamplate commands.
	 *
	 * @since 0.0.1 ~2020.09.20
	 */
	protected static final Pattern PATTERN_COMMANDS = Pattern.compile("\\n?(?!\\\\)#(([^#\\n])|((?<=\\\\)([#\\n])))*(((?<!\\\\)([#\\n]))|$)");

	protected static final Pattern PATTERN_DEFINE = Pattern.compile("^#DEFINE", Pattern.CASE_INSENSITIVE);
	protected static final Pattern PATTERN_DEFINE_VALUE = Pattern.compile("^\\s*(?<ADDRESS>\\S+)\\s*(?<LOGIC>.*)$");

	protected static final Pattern PATTERN_ELIF = Pattern.compile("^#ELIF", Pattern.CASE_INSENSITIVE);
	protected static final Pattern PATTERN_ELIF_VALUE = COMMON_PATTERN_LOGIC_VALUE;

	protected static final Pattern PATTERN_ELSE = Pattern.compile("^#ELSE", Pattern.CASE_INSENSITIVE);
	protected static final Pattern PATTERN_ELSE_VALUE = COMMON_PATTERN_NON_VALUE;

	protected static final Pattern PATTERN_ENDIF = Pattern.compile("^#ENDIF", Pattern.CASE_INSENSITIVE);
	protected static final Pattern PATTERN_ENDIF_VALUE = COMMON_PATTERN_NON_VALUE;

	protected static final Pattern PATTERN_ENDWITH = Pattern.compile("^#ENDWITH", Pattern.CASE_INSENSITIVE);
	protected static final Pattern PATTERN_ENDWITH_VALUE = COMMON_PATTERN_NON_VALUE;

	protected static final Pattern PATTERN_IF = Pattern.compile("^#IF", Pattern.CASE_INSENSITIVE);
	protected static final Pattern PATTERN_IF_VALUE = COMMON_PATTERN_LOGIC_VALUE;

	protected static final Pattern PATTERN_MAKE = Pattern.compile("^#MAKE", Pattern.CASE_INSENSITIVE);
	protected static final Pattern PATTERN_MAKE_VALUE = COMMON_PATTERN_PAIR_VALUE;

	protected static final Pattern PATTERN_PASTE = Pattern.compile("^#PASTE", Pattern.CASE_INSENSITIVE);
	protected static final Pattern PATTERN_PASTE_VALUE = COMMON_PATTERN_LOGIC_VALUE;

	protected static final Pattern PATTERN_TEXT = Pattern.compile("^#TEXT", Pattern.CASE_INSENSITIVE);
	protected static final Pattern PATTERN_TEXT_VALUE = Pattern.compile("^\\s*(?<TEXT>.*)$");

	protected static final Pattern PATTERN_WITH = Pattern.compile("^#WITH", Pattern.CASE_INSENSITIVE);
	protected static final Pattern PATTERN_WITH_VALUE = COMMON_PATTERN_PAIR_VALUE;

	protected final Parser<Logic> logic;

	public ScopeParser(Parser<Logic> logic) {
		Objects.requireNonNull(logic, "logic");
		this.logic = logic;
	}

	@Override
	public Scope link(List poll) {
		Objects.requireNonNull(poll, "poll");

		if (poll.size() > 0) {
			Object object = poll.get(0);

			if (object instanceof Scope) {
				Scope scope = (Scope) object;

				ListIterator iterator = poll.listIterator(1);
				while (iterator.hasNext()) {
					Object object1 = iterator.next();

					if (object1 instanceof Scope) {
						Scope scope1 = (Scope) object1;

						scope.tryPush(scope1);
					}
				}

				return scope;
			} else
				throw new ParseException("Scope not resolved", String.valueOf(object));
		} else
			throw new ParseException("Scopes resolved to non");
	}

	@Override
	public void parse(List poll) {
		Objects.requireNonNull(poll, "poll");

		//clear all scopes
		this.processScopes(poll);

		//clear remaining commands
		this.parseDefines(poll);
		this.parseElifs(poll);
		this.parseElse(poll);
		this.parseEndifs(poll);
		this.parseEndwiths(poll);
		this.parseIfs(poll);
		this.parseMakes(poll);
		this.parsePastes(poll);
		this.parseTexts(poll);
		this.parseWiths(poll);

		//clear leftovers
		this.processLeftovers(poll);
	}

	@Override
	public List poll(String string) {
		Objects.requireNonNull(string, "string");
		return new ArrayList(Collections.singletonList(string));
	}

	protected void parseDefines(List poll) {
		Objects.requireNonNull(poll, "poll");

		ListIterator iterator = poll.listIterator();
		while (iterator.hasNext()) {
			Object object = iterator.next();

			if (object instanceof String) {
				//target Strings only
				String string = (String) object;
				Matcher matcher = PATTERN_DEFINE.matcher(string);

				if (matcher.find()) {
					//target #DEFINE commands only
					String value = string.substring(matcher.end());
					Matcher valueMatcher = PATTERN_DEFINE_VALUE.matcher(value);

					if (valueMatcher.find()) {
						//target value #DEFINE parameters only
						String address = valueMatcher.group("ADDRESS");
						String logic = valueMatcher.group("LOGIC");

						//Replace
						iterator.set(new Define(
								address,
								this.logic.parse(logic)
						));
					}
				}
			}
		}
	}

	protected void parseElifs(List poll) {
		Objects.requireNonNull(poll, "poll");

		ListIterator iterator = poll.listIterator();
		while (iterator.hasNext()) {
			Object object = iterator.next();

			if (object instanceof String) {
				//target Strings only
				String string = (String) object;
				Matcher matcher = PATTERN_ELIF.matcher(string);

				if (matcher.find()) {
					//target #Elif commands only
					String value = string.substring(matcher.end());
					Matcher valueMatcher = PATTERN_ELIF_VALUE.matcher(value);

					if (valueMatcher.find()) {
						//target valid #Elif parameters only
						String logic = valueMatcher.group("LOGIC");

						//Replace
						iterator.set(new Elif(
								this.logic.parse(logic)
						));
					}
				}
			}
		}
	}

	protected void parseElse(List poll) {
		Objects.requireNonNull(poll, "poll");

		ListIterator iterator = poll.listIterator();
		while (iterator.hasNext()) {
			Object object = iterator.next();

			if (object instanceof String) {
				//target Strings only
				String string = (String) object;
				Matcher matcher = PATTERN_ELSE.matcher(string);

				if (matcher.find()) {
					//target #Else commands only
					String value = string.substring(matcher.end());
					Matcher valueMatcher = PATTERN_ELSE_VALUE.matcher(value);

					if (valueMatcher.find()) {
						//target valid #Else parameters only

						iterator.set(new Else());
					}
				}
			}
		}
	}

	protected void parseEndifs(List poll) {
		Objects.requireNonNull(poll, "poll");

		ListIterator iterator = poll.listIterator();
		while (iterator.hasNext()) {
			Object object = iterator.next();

			if (object instanceof String) {
				//target Strings only
				String string = (String) object;
				Matcher matcher = PATTERN_ENDIF.matcher(string);

				if (matcher.find()) {
					//target #ENDIF commands only
					String value = string.substring(matcher.end());
					Matcher valueMatcher = PATTERN_ENDIF_VALUE.matcher(value);

					if (valueMatcher.find()) {
						//target valid #ENDIF parameters only

						iterator.set(new Endif());
					}
				}
			}
		}
	}

	protected void parseEndwiths(List poll) {
		Objects.requireNonNull(poll, "poll");

		ListIterator iterator = poll.listIterator();
		while (iterator.hasNext()) {
			Object object = iterator.next();

			if (object instanceof String) {
				//target Strings only
				String string = (String) object;
				Matcher matcher = PATTERN_ENDWITH.matcher(string);

				if (matcher.find()) {
					//target #ENDWITH commands only
					String value = string.substring(matcher.end());
					Matcher valueMatcher = PATTERN_ENDIF_VALUE.matcher(value);

					if (valueMatcher.find()) {
						//target #ENDWITH with valid parameters only

						iterator.set(new Endwith());
					}
				}
			}
		}
	}

	protected void parseIfs(List poll) {
		Objects.requireNonNull(poll, "poll");

		ListIterator iterator = poll.listIterator();
		while (iterator.hasNext()) {
			Object object = iterator.next();

			if (object instanceof String) {
				//target Strings only
				String string = (String) object;
				Matcher matcher = PATTERN_IF.matcher(string);

				if (matcher.find()) {
					//target valid #If commands only
					String value = string.substring(matcher.end());
					Matcher valueMatcher = PATTERN_IF_VALUE.matcher(value);

					if (valueMatcher.find()) {
						//target valid #IF parameters only
						String logic = valueMatcher.group("LOGIC");

						iterator.set(new If(
								this.logic.parse(logic)
						));
					}
				}
			}
		}
	}

	protected void parseMakes(List poll) {
		Objects.requireNonNull(poll, "poll");

		ListIterator iterator = poll.listIterator();
		while (iterator.hasNext()) {
			Object object = iterator.next();

			if (object instanceof String) {
				//target Strings only
				String string = (String) object;
				Matcher matcher = PATTERN_MAKE.matcher(string);

				if (matcher.find()) {
					//target #MAKE commands only
					String value = string.substring(matcher.end());

					//Replace
					iterator.set(new Make(
							parseOptions(value, PATTERN_MAKE_VALUE)
					));
				}
			}
		}
	}

	protected void parsePastes(List poll) {
		Objects.requireNonNull(poll, "poll");

		ListIterator iterator = poll.listIterator();
		while (iterator.hasNext()) {
			Object object = iterator.next();

			if (object instanceof String) {
				//target Strings only
				String string = (String) object;
				Matcher matcher = PATTERN_PASTE.matcher(string);

				if (matcher.find()) {
					//target #PASTE commands only
					String value = string.substring(matcher.end());
					Matcher valueMatcher = PATTERN_PASTE_VALUE.matcher(value);

					if (valueMatcher.find()) {
						//target valid #PASTE parameters only
						String logic = valueMatcher.group("LOGIC");

						iterator.set(new Paste(
								this.logic.parse(logic)
						));
					}
				}
			}
		}
	}

	/**
	 * Parse any {@link Text} scope in a {@link String} in the given {@code poll}.
	 * <p>
	 * Clear these before calling this method:
	 * <ul>
	 *     <li>{@link #processScopes(List)}</li>
	 *     <li>Anything that manipulate non-command texts...</li>
	 * </ul>
	 *
	 * @param poll the poll to parse any text in it.
	 * @throws NullPointerException if the given {@code poll} is null.
	 * @throws ParseException       if any parse exception occurs.
	 * @since 0.0.1 ~2020.09.20
	 */
	protected void parseTexts(List poll) {
		Objects.requireNonNull(poll, "poll");

		ListIterator iterator = poll.listIterator();
		while (iterator.hasNext()) {
			Object object = iterator.next();

			if (object instanceof String) {
				//target Strings only
				String string = (String) object;
				Matcher matcher = PATTERN_TEXT.matcher(string);

				if (matcher.find()) {
					//target #TEXT commands only
					String value = string.substring(matcher.end());
					Matcher valueMatcher = PATTERN_TEXT_VALUE.matcher(value);

					if (valueMatcher.find()) {
						//target valid #Text parameters only
						String text = valueMatcher.group("TEXT");

						iterator.set(new Text(
								text
						));
					}
				}
			}
		}
	}

	protected void parseWiths(List poll) {
		Objects.requireNonNull(poll, "poll");

		ListIterator iterator = poll.listIterator();
		while (iterator.hasNext()) {
			Object object = iterator.next();

			if (object instanceof String) {
				//target Strings only
				String string = (String) object;
				Matcher matcher = PATTERN_WITH.matcher(string);

				if (matcher.find()) {
					//target #WITH commands only
					String value = string.substring(matcher.end());

					//Replace
					iterator.set(new With(
							parseOptions(value, PATTERN_WITH_VALUE)
					));
				}
			}
		}
	}

	protected void processLeftovers(List poll) {
		Objects.requireNonNull(poll, "poll");

		ListIterator iterator = poll.listIterator();
		while (iterator.hasNext()) {
			Object object = iterator.next();

			if (object instanceof String) {
				//target any String
				iterator.set(new Text((String) object));
			}
		}
	}

	/**
	 * Split any possible command in a {@link String} and format it to make it ready-to-parse
	 * state.
	 *
	 * @param poll the poll to split any possible command in it.
	 * @throws NullPointerException if the given {@code poll} is null.
	 * @throws ParseException       if any parse exception occurs.
	 * @since 0.0.1 ~2020.09.20
	 */
	protected void processScopes(List poll) {
		Objects.requireNonNull(poll, "poll");

		ListIterator iterator = poll.listIterator();
		while (iterator.hasNext()) {
			Object object = iterator.next();

			if (object instanceof String) {
				String string = (String) object;

				iterator.remove();

				Matcher matcher = PATTERN_COMMANDS.matcher(string);
				int i = 0;
				while (matcher.find()) {
					int start = matcher.start();
					int end = matcher.end();

					String before = string.substring(i, start)
							.replace("\\#", "#");
					String it = string.substring(start, end)
							.replace("\\\n", "\n")
							.trim()
							.replaceAll("#$", "")
							.replace("\\#", "#");

					if (!before.isEmpty())
						iterator.add(before);

					iterator.add(it);

					i = end;
				}

				String after = string.substring(i)
						.replace("\\#", "#");

				iterator.add(after);
			}
		}
	}

	private Map<String, Logic>[] parseOptions(String value, Pattern pattern) {
		Objects.requireNonNull(value, "value");
		Objects.requireNonNull(pattern, "pattern");

		List<Map<String, Logic>> options = new ArrayList<>();

		for (String option : value.split("[|]")) {
			Map<String, Logic> map = new HashMap<>();

			for (String pair : option.split("[,]")) {
				Matcher matcher = pattern.matcher(pair);

				if (matcher.find()) {
					String key = matcher.group("KEY");
					String logic = matcher.group("LOGIC");

					map.put(
							key,
							this.logic.parse(logic)
					);
				}
			}

			options.add(map);
		}

		return options.toArray(new Map[0]);
	}
}
