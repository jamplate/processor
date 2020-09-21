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
 * @version 0.0.3
 * @since 0.0.1 ~2020.09.19
 */
public class ScopeParser implements PollParser<Scope> {
	/**
	 * Common pattern to extract array of parameters.
	 *
	 * @since 0.0.2 ~2020.09.21
	 */
	protected static final Pattern COMMON_PARAMETERS_ARRAY = Pattern.compile("\\s*(?<PARAMETER>(?:[^|]|(?:(?<=\\\\)[|]))*\\S)\\s*(?=[|]|$)");
	/**
	 * Common pattern to extract single integer parameter.
	 *
	 * @since 0.0.3 ~2020.09.21
	 */
	protected static final Pattern COMMON_PARAMETERS_INTEGER = Pattern.compile("^\\s*(?<LOGIC>\\d*)\\s*$");
	/**
	 * Common pattern to extract single logic parameter.
	 *
	 * @since 0.0.1 ~2020.09.20
	 */
	protected static final Pattern COMMON_PARAMETERS_LOGIC = Pattern.compile("^\\s*(?<LOGIC>(\\S.*\\S)|(\\S))\\s*$");
	/**
	 * Common pattern to extract array parameter.
	 *
	 * @since 0.0.2 ~2020.09.21
	 */
	protected static final Pattern COMMON_PARAMETER_ARRAY = Pattern.compile("\\s*(?<PARAMETER>(?:[^,]|(?:(?<=\\\\)[,]))*\\S)\\s*(?=[,]|$)");
	/**
	 * Common pattern to match values that contains nothing.
	 *
	 * @since 0.0.1 ~2020.09.20
	 */
	protected static final Pattern COMMON_PARAMETER_NON = Pattern.compile("^\\s*$");
	/**
	 * Common pattern to extract pairs.
	 *
	 * @since 0.0.1 ~2020.09.20
	 */
	protected static final Pattern COMMON_PARAMETER_PAIR = Pattern.compile("\\s*(?<KEY>\\S+)\\s*:\\s*(?<LOGIC>\\S+)\\s*");
	/**
	 * A pattern that catches jamplate commands.
	 *
	 * @since 0.0.1 ~2020.09.20
	 */
	protected static final Pattern PATTERN_COMMANDS = Pattern.compile("\\n?(?:(?<!\\\\)#)(?:(?:[^#\\n])|(?:(?<=\\\\)[#\\n]))*(?:(?:(?<!\\\\)[#\\n])|$)");

	/**
	 * A pattern to be used to detected {@link Define} commands.
	 *
	 * @since 0.0.1 ~2020.09.20
	 */
	protected static final Pattern PATTERN_DEFINE = Pattern.compile("^#DEFINE", Pattern.CASE_INSENSITIVE);
	/**
	 * A pattern to be used to extract the parameters of a {@link Define} command.
	 *
	 * @since 0.0.1 ~2020.09.20
	 */
	protected static final Pattern PATTERN_DEFINE_PARAMETERS = Pattern.compile("^\\s*(?<ADDRESS>\\S+)\\s*(?<LOGIC>.*)$");

	/**
	 * A pattern to be used to detect {@link Elif} commands.
	 *
	 * @since 0.0.1 ~2020.09.20
	 */
	protected static final Pattern PATTERN_ELIF = Pattern.compile("^#ELIF", Pattern.CASE_INSENSITIVE);
	/**
	 * A pattern to be used to extract the parameters of an {@link Elif} command.
	 *
	 * @since 0.0.1 ~2020.09.20
	 */
	protected static final Pattern PATTERN_ELIF_PARAMETERS = ScopeParser.COMMON_PARAMETERS_LOGIC;

	/**
	 * A pattern to be used to detect {@link Else} commands.
	 *
	 * @since 0.0.1 ~2020.09.20
	 */
	protected static final Pattern PATTERN_ELSE = Pattern.compile("^#ELSE", Pattern.CASE_INSENSITIVE);
	/**
	 * A pattern to be used to extract the parameters of an {@link Else} command.
	 *
	 * @since 0.0.1 ~2020.09.20
	 */
	protected static final Pattern PATTERN_ELSE_PARAMETERS = ScopeParser.COMMON_PARAMETER_NON;

	/**
	 * A pattern to be used to detect {@link Endif} commands.
	 *
	 * @since 0.0.1 ~2020.09.20
	 */
	protected static final Pattern PATTERN_ENDIF = Pattern.compile("^#ENDIF", Pattern.CASE_INSENSITIVE);
	/**
	 * A pattern to be used to extract the parameters of an {@link Endif} command.
	 *
	 * @since 0.0.1 ~2020.09.20
	 */
	protected static final Pattern PATTERN_ENDIF_PARAMETERS = ScopeParser.COMMON_PARAMETER_NON;

	/**
	 * A pattern to be used to detect {@link Endwith} commands.
	 *
	 * @since 0.0.1 ~2020.09.20
	 */
	protected static final Pattern PATTERN_ENDWITH = Pattern.compile("^#ENDWITH", Pattern.CASE_INSENSITIVE);
	/**
	 * A pattern to be used to extract the parameters of an {@link Endwith} command.
	 *
	 * @since 0.0.1 ~2020.09.20
	 */
	protected static final Pattern PATTERN_ENDWITH_PARAMETERS = ScopeParser.COMMON_PARAMETER_NON;

	/**
	 * A pattern to be used to detect {@link If} commands.
	 *
	 * @since 0.0.1 ~2020.09.20
	 */
	protected static final Pattern PATTERN_IF = Pattern.compile("^#IF", Pattern.CASE_INSENSITIVE);
	/**
	 * A pattern to be used to extract the parameters of an {@link If} command.
	 *
	 * @since 0.0.1 ~2020.09.20
	 */
	protected static final Pattern PATTERN_IF_PARAMETERS = ScopeParser.COMMON_PARAMETERS_LOGIC;

	/**
	 * A pattern to be used to detect {@link Line} commands.
	 *
	 * @since 0.0.3 ~2020.09.21
	 */
	protected static final Pattern PATTERN_LINE = Pattern.compile("^#(LINE|LN)", Pattern.CASE_INSENSITIVE);
	/**
	 * A pattern to be used to extract the parameters of an {@link Line} command.
	 *
	 * @since 0.0.3 ~2020.09.21
	 */
	protected static final Pattern PATTERN_LINE_PARAMETERS = ScopeParser.COMMON_PARAMETERS_INTEGER;

	/**
	 * A pattern to be used to detect {@link Make} commands.
	 *
	 * @since 0.0.1 ~2020.09.20
	 */
	protected static final Pattern PATTERN_MAKE = Pattern.compile("^#MAKE", Pattern.CASE_INSENSITIVE);
	/**
	 * A pattern to be used to extract a pair from a {@link Make} command.
	 *
	 * @since 0.0.1 ~2020.09.20
	 */
	protected static final Pattern PATTERN_MAKE_PAIR = ScopeParser.COMMON_PARAMETER_PAIR;
	/**
	 * A pattern to be used to extract a parameter from a {@link Make} command.
	 *
	 * @since 0.0.2 ~2020.09.21
	 */
	protected static final Pattern PATTERN_MAKE_PARAMETER = ScopeParser.COMMON_PARAMETER_ARRAY;
	/**
	 * A pattern to be used to extract the parameters of a {@link Make} command.
	 *
	 * @since 0.0.2 ~2020.09.21
	 */
	protected static final Pattern PATTERN_MAKE_PARAMETERS = ScopeParser.COMMON_PARAMETERS_ARRAY;

	/**
	 * A pattern to be used to detect {@link Paste} commands.
	 *
	 * @since 0.0.1 ~2020.09.20
	 */
	protected static final Pattern PATTERN_PASTE = Pattern.compile("^#PASTE", Pattern.CASE_INSENSITIVE);
	/**
	 * A pattern to be used to extract the parameters of a {@link Paste} command.
	 *
	 * @since 0.0.1 ~2020.09.20
	 */
	protected static final Pattern PATTERN_PASTE_PARAMETERS = ScopeParser.COMMON_PARAMETERS_LOGIC;

	/**
	 * A pattern to be used to detect {@link Text} commands.
	 *
	 * @since 0.0.1 ~2020.09.20
	 */
	protected static final Pattern PATTERN_TEXT = Pattern.compile("^#TEXT", Pattern.CASE_INSENSITIVE);
	/**
	 * A pattern to be used to extract the parameters of a {@link Text} command.
	 *
	 * @since 0.0.1 ~2020.09.20
	 */
	protected static final Pattern PATTERN_TEXT_PARAMETERS = Pattern.compile("^\\s*(?<TEXT>.*)$");

	/**
	 * A pattern to be used to detect {@link With} commands.
	 *
	 * @since 0.0.1 ~2020.09.20
	 */
	protected static final Pattern PATTERN_WITH = Pattern.compile("^#WITH", Pattern.CASE_INSENSITIVE);
	/**
	 * A pattern to be used to extract a pair from a {@link With} command.
	 *
	 * @since 0.0.1 ~2020.09.20
	 */
	protected static final Pattern PATTERN_WITH_PAIR = ScopeParser.COMMON_PARAMETER_PAIR;
	/**
	 * A pattern to be used to extract a parameter from a {@link With} command.
	 *
	 * @since 0.0.2 ~2020.09.21
	 */
	protected static final Pattern PATTERN_WITH_PARAMETER = ScopeParser.COMMON_PARAMETER_ARRAY;
	/**
	 * A pattern to be used to extract the parameters of a {@link Make} command.
	 *
	 * @since 0.0.2 ~2020.09.21
	 */
	protected static final Pattern PATTERN_WITH_PARAMETERS = ScopeParser.COMMON_PARAMETERS_ARRAY;

	/**
	 * The logic parser used by this scope parser.
	 *
	 * @since 0.0.1 ~2020.09.20
	 */
	protected final Parser<Logic> logic;

	/**
	 * Construct a new scope parser that uses the given {@code logic} as its logic parser.
	 *
	 * @param logic the parser that parses the logical values for this parser.
	 * @throws NullPointerException if the given {@code logic} is null.
	 * @since 0.0.1 ~2020.09.20
	 */
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

		//pre-parsing
		this.processSpecialCases(poll);

		//clear all scopes
		this.processScopes(poll);

		//clear remaining commands
		this.parseDefines(poll);
		this.parseElifs(poll);
		this.parseElse(poll);
		this.parseEndifs(poll);
		this.parseEndwiths(poll);
		this.parseIfs(poll);
		this.parseLines(poll);
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

	/**
	 * Parse all the define statements in a {@link String} in the given {@code poll} into {@link
	 * Define} scopes.
	 *
	 * @param poll the poll to parse all the define statements in it.
	 * @throws NullPointerException if the given {@code poll} is null.
	 * @throws ParseException       if any parse exception occurs.
	 * @since 0.0.1 ~2020.09.20
	 */
	protected void parseDefines(List poll) {
		Objects.requireNonNull(poll, "poll");

		ListIterator iterator = poll.listIterator();
		while (iterator.hasNext()) {
			Object object = iterator.next();

			if (object instanceof String) {
				//target Strings only
				String string = (String) object;
				Matcher matcher = ScopeParser.PATTERN_DEFINE.matcher(string);

				if (matcher.find()) {
					//target #DEFINE commands only
					String value = string.substring(matcher.end());
					Matcher valueMatcher = ScopeParser.PATTERN_DEFINE_PARAMETERS.matcher(value);

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

	/**
	 * Parse all the elif statements in a {@link String} in the given {@code poll} into {@link Elif}
	 * scopes.
	 *
	 * @param poll the poll to parse all the elif statements in it.
	 * @throws NullPointerException if the given {@code poll} is null.
	 * @throws ParseException       if any parse exception occurs.
	 * @since 0.0.1 ~2020.09.20
	 */
	protected void parseElifs(List poll) {
		Objects.requireNonNull(poll, "poll");

		ListIterator iterator = poll.listIterator();
		while (iterator.hasNext()) {
			Object object = iterator.next();

			if (object instanceof String) {
				//target Strings only
				String string = (String) object;
				Matcher matcher = ScopeParser.PATTERN_ELIF.matcher(string);

				if (matcher.find()) {
					//target #Elif commands only
					String value = string.substring(matcher.end());
					Matcher valueMatcher = ScopeParser.PATTERN_ELIF_PARAMETERS.matcher(value);

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

	/**
	 * Parse all the else statements in a {@link String} in the given {@code poll} into {@link Else}
	 * scopes.
	 *
	 * @param poll the poll to parse all the else statements in it.
	 * @throws NullPointerException if the given {@code poll} is null.
	 * @throws ParseException       if any parse exception occurs.
	 * @since 0.0.1 ~2020.09.20
	 */
	protected void parseElse(List poll) {
		Objects.requireNonNull(poll, "poll");

		ListIterator iterator = poll.listIterator();
		while (iterator.hasNext()) {
			Object object = iterator.next();

			if (object instanceof String) {
				//target Strings only
				String string = (String) object;
				Matcher matcher = ScopeParser.PATTERN_ELSE.matcher(string);

				if (matcher.find()) {
					//target #Else commands only
					String value = string.substring(matcher.end());
					Matcher valueMatcher = ScopeParser.PATTERN_ELSE_PARAMETERS.matcher(value);

					if (valueMatcher.find()) {
						//target valid #Else parameters only

						iterator.set(new Else());
					}
				}
			}
		}
	}

	/**
	 * Parse all the endif statements in a {@link String} in the given {@code poll} into {@link
	 * Endif} scopes.
	 *
	 * @param poll the poll to parse all the endif statements in it.
	 * @throws NullPointerException if the given {@code poll} is null.
	 * @throws ParseException       if any parse exception occurs.
	 * @since 0.0.1 ~2020.09.20
	 */
	protected void parseEndifs(List poll) {
		Objects.requireNonNull(poll, "poll");

		ListIterator iterator = poll.listIterator();
		while (iterator.hasNext()) {
			Object object = iterator.next();

			if (object instanceof String) {
				//target Strings only
				String string = (String) object;
				Matcher matcher = ScopeParser.PATTERN_ENDIF.matcher(string);

				if (matcher.find()) {
					//target #ENDIF commands only
					String value = string.substring(matcher.end());
					Matcher valueMatcher = ScopeParser.PATTERN_ENDIF_PARAMETERS.matcher(value);

					if (valueMatcher.find()) {
						//target valid #ENDIF parameters only

						iterator.set(new Endif());
					}
				}
			}
		}
	}

	/**
	 * Parse all the endwith statements in a {@link String} in the given {@code poll} into {@link
	 * Endwith} scopes.
	 *
	 * @param poll the poll to parse all the endwith statements in it.
	 * @throws NullPointerException if the given {@code poll} is null.
	 * @throws ParseException       if any parse exception occurs.
	 * @since 0.0.1 ~2020.09.20
	 */
	protected void parseEndwiths(List poll) {
		Objects.requireNonNull(poll, "poll");

		ListIterator iterator = poll.listIterator();
		while (iterator.hasNext()) {
			Object object = iterator.next();

			if (object instanceof String) {
				//target Strings only
				String string = (String) object;
				Matcher matcher = ScopeParser.PATTERN_ENDWITH.matcher(string);

				if (matcher.find()) {
					//target #ENDWITH commands only
					String value = string.substring(matcher.end());
					Matcher valueMatcher = ScopeParser.PATTERN_ENDWITH_PARAMETERS.matcher(value);

					if (valueMatcher.find()) {
						//target #ENDWITH with valid parameters only

						iterator.set(new Endwith());
					}
				}
			}
		}
	}

	/**
	 * Parse all the if statements in a {@link String} in the given {@code poll} into {@link If}
	 * scopes.
	 *
	 * @param poll the poll to parse all the if statements in it.
	 * @throws NullPointerException if the given {@code poll} is null.
	 * @throws ParseException       if any parse exception occurs.
	 * @since 0.0.1 ~2020.09.20
	 */
	protected void parseIfs(List poll) {
		Objects.requireNonNull(poll, "poll");

		ListIterator iterator = poll.listIterator();
		while (iterator.hasNext()) {
			Object object = iterator.next();

			if (object instanceof String) {
				//target Strings only
				String string = (String) object;
				Matcher matcher = ScopeParser.PATTERN_IF.matcher(string);

				if (matcher.find()) {
					//target valid #If commands only
					String value = string.substring(matcher.end());
					Matcher valueMatcher = ScopeParser.PATTERN_IF_PARAMETERS.matcher(value);

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

	/**
	 * Parse all the line statements in a {@link String} in the given {@code poll} into {@link Line}
	 * scopes.
	 *
	 * @param poll the poll to parse all the line statements in it.
	 * @throws NullPointerException if the given {@code poll} is null.
	 * @throws ParseException       if any parse exception occurs.
	 * @since 0.0.3 ~2020.09.21
	 */
	protected void parseLines(List poll) {
		Objects.requireNonNull(poll, "poll");

		ListIterator iterator = poll.listIterator();
		while (iterator.hasNext()) {
			Object object = iterator.next();

			if (object instanceof String) {
				//target Strings only
				String string = (String) object;
				Matcher matcher = ScopeParser.PATTERN_LINE.matcher(string);

				if (matcher.find()) {
					//target #LINE or #LN commands only
					String value = string.substring(matcher.end());
					Matcher valueMatcher = ScopeParser.PATTERN_LINE_PARAMETERS.matcher(value);

					if (valueMatcher.find()) {
						//target valid #LINE parameters only
						String logic = valueMatcher.group("LOGIC");

						iterator.set(new Line(
								this.logic.parse(logic)
						));
					}
				}
			}
		}
	}

	/**
	 * Parse all the make statements in a {@link String} in the given {@code poll} into {@link Make}
	 * scopes.
	 *
	 * @param poll the poll to parse all the make statements in it.
	 * @throws NullPointerException if the given {@code poll} is null.
	 * @throws ParseException       if any parse exception occurs.
	 * @since 0.0.1 ~2020.09.20
	 */
	protected void parseMakes(List poll) {
		Objects.requireNonNull(poll, "poll");

		ListIterator iterator = poll.listIterator();
		while (iterator.hasNext()) {
			Object object = iterator.next();

			if (object instanceof String) {
				//target Strings only
				String string = (String) object;
				Matcher matcher = ScopeParser.PATTERN_MAKE.matcher(string);

				if (matcher.find()) {
					//target #MAKE commands only
					String value = string.substring(matcher.end());
					Matcher valueMatcher = ScopeParser.PATTERN_MAKE_PARAMETERS.matcher(value);

					List<Map<String, Logic>> options = new ArrayList<>();

					//compute parameters
					while (valueMatcher.find()) {
						String parameter = valueMatcher.group("PARAMETER")
								.replace("\\|", "|");
						Matcher parameterMatcher = ScopeParser.PATTERN_MAKE_PARAMETER.matcher(parameter);

						Map<String, Logic> option = new HashMap<>();

						while (parameterMatcher.find()) {
							String pair = parameterMatcher.group("PARAMETER")
									.replace("\\,", ",");
							Matcher pairMatcher = ScopeParser.PATTERN_MAKE_PAIR.matcher(pair);

							if (pairMatcher.find()) {
								//process valid parameters only
								String key = pairMatcher.group("KEY");
								String logic = pairMatcher.group("LOGIC");

								option.put(
										key,
										this.logic.parse(logic)
								);
							}
						}

						options.add(option);
					}

					//Replace
					iterator.set(new Make(
							options.toArray(new Map[0])
					));
				}
			}
		}
	}

	/**
	 * Parse all the paste statements in a {@link String} in the given {@code poll} into {@link
	 * Paste} scopes.
	 *
	 * @param poll the poll to parse all the paste statements in it.
	 * @throws NullPointerException if the given {@code poll} is null.
	 * @throws ParseException       if any parse exception occurs.
	 * @since 0.0.1 ~2020.09.20
	 */
	protected void parsePastes(List poll) {
		Objects.requireNonNull(poll, "poll");

		ListIterator iterator = poll.listIterator();
		while (iterator.hasNext()) {
			Object object = iterator.next();

			if (object instanceof String) {
				//target Strings only
				String string = (String) object;
				Matcher matcher = ScopeParser.PATTERN_PASTE.matcher(string);

				if (matcher.find()) {
					//target #PASTE commands only
					String value = string.substring(matcher.end());
					Matcher valueMatcher = ScopeParser.PATTERN_PASTE_PARAMETERS.matcher(value);

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
	 * Parse all the text statements in a {@link String} in the given {@code poll} into {@link Text}
	 * scopes.
	 *
	 * @param poll the poll to parse all the text statements in it.
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
				Matcher matcher = ScopeParser.PATTERN_TEXT.matcher(string);

				if (matcher.find()) {
					//target #TEXT commands only
					String value = string.substring(matcher.end());
					Matcher valueMatcher = ScopeParser.PATTERN_TEXT_PARAMETERS.matcher(value);

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

	/**
	 * Parse all the with statements in a {@link String} in the given {@code poll} into {@link With}
	 * scopes.
	 *
	 * @param poll the poll to parse all the with statements in it.
	 * @throws NullPointerException if the given {@code poll} is null.
	 * @throws ParseException       if any parse exception occurs.
	 * @since 0.0.1 ~2020.09.20
	 */
	protected void parseWiths(List poll) {
		Objects.requireNonNull(poll, "poll");

		ListIterator iterator = poll.listIterator();
		while (iterator.hasNext()) {
			Object object = iterator.next();

			if (object instanceof String) {
				//target Strings only
				String string = (String) object;
				Matcher matcher = ScopeParser.PATTERN_WITH.matcher(string);

				if (matcher.find()) {
					//target #WITH commands only
					String value = string.substring(matcher.end());
					Matcher valueMatcher = ScopeParser.PATTERN_WITH_PARAMETERS.matcher(value);

					List<Map<String, Logic>> options = new ArrayList<>();

					//compute parameters
					while (valueMatcher.find()) {
						String parameter = valueMatcher.group("PARAMETER")
								.replace("\\|", "|");
						Matcher parameterMatcher = ScopeParser.PATTERN_WITH_PARAMETER.matcher(parameter);

						Map<String, Logic> option = new HashMap<>();

						while (parameterMatcher.find()) {
							String pair = parameterMatcher.group("PARAMETER");
							//									.replace("\\,", ",");
							Matcher pairMatcher = ScopeParser.PATTERN_WITH_PAIR.matcher(pair);

							if (pairMatcher.find()) {
								//process valid parameters only
								String key = pairMatcher.group("KEY");
								String logic = pairMatcher.group("LOGIC");

								option.put(
										key,
										this.logic.parse(logic)
								);
							}
						}

						options.add(option);
					}

					//Replace
					iterator.set(new With(
							options.toArray(new Map[0])
					));
				}
			}
		}
	}

	/**
	 * Parse any leftover {@link String}s in the given {@code poll}.
	 *
	 * @param poll the poll to parse all the leftovers.
	 * @throws NullPointerException if the given {@code poll} is null.
	 * @throws ParseException       if any parse exception occurs.
	 * @since 0.0.1 ~2020.09.20
	 */
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

				Matcher matcher = ScopeParser.PATTERN_COMMANDS.matcher(string);
				int i = 0;
				while (matcher.find()) {
					int start = matcher.start();
					int end = matcher.end();

					String before = string.substring(i, start)
							.replace("\\#", "#");
					String it = string.substring(start, end)
							.replace("\\\r", "\r")
							.replace("\\\n", "\n")
							.replace("\\\r\n", "\r\n")
							.trim()
							.replaceAll("(?<!\\\\)#$", "")
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

	/**
	 * Solve the special cases that could invalidate the parser logic.
	 * <p>
	 * The special cases:
	 * <ul>
	 *     <li>Carriage Return ({@code \r}): it could be before {@code \n} and that break the ability to escape it.</li>
	 * </ul>
	 *
	 * @param poll the poll to solve the special cases on it.
	 * @throws NullPointerException if the given {@code poll} is null.
	 * @since 0.0.1 ~2020.09.20
	 */
	protected void processSpecialCases(List poll) {
		Objects.requireNonNull(poll, "poll");

		ListIterator iterator = poll.listIterator();
		while (iterator.hasNext()) {
			Object object = iterator.next();

			if (object instanceof String) {
				String string = (String) object;

				iterator.set(
						string.replace("\r", "")
				);
			}
		}
	}
}
