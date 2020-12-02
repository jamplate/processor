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
package org.jamplate;

import org.cufy.preprocessor.AbstractProcessor;
import org.cufy.preprocessor.ParseException;
import org.cufy.preprocessor.link.Logic;
import org.cufy.preprocessor.invoke.Memory;
import org.cufy.preprocessor.AbstractParser;
import org.jamplate.logic.*;
import org.jamplate.memory.MapMemory;
import org.jamplate.parser.ScopeParser;
import org.cufy.preprocessor.Poll;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * The main entry-point to the jamplate pre-processor.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2020.09.20
 */
public class Jamplate extends AbstractProcessor {
	/**
	 * An instance of the jamplate processor.
	 *
	 * @since 0.0.b ~2020.10.02
	 */
	public static final Jamplate INSTANCE = new Jamplate();

	/**
	 * Construct a new jamplate processor.
	 *
	 * @since 0.0.b ~2020.10.02
	 */
	public Jamplate() {
		super(new ScopeParser(new LogicParser(
				Arrays.asList(
						new Parentheses.Parser(
								new Pattern[]{Literal.PATTERN},
								new Pattern[]{Array.PATTERN}
						),
						new Array.Parser(
								new Pattern[]{Literal.PATTERN},
								new Pattern[]{Parentheses.PATTERN}
						),
						new Literal.Parser(),
						new Constant.Parser(),
						new Reference.Parser(),
						new Negation.Parser(),
						new And.Parser(),
						new Or.Parser(),
						new Addition.ParserVote()
				)
		)));
	}

	/**
	 * Process the given {@code input} file, then output the results to the given {@code output}
	 * file.
	 * <p>
	 * NOTE: make sure the given {@code input} exists and the parent file of the given {@code
	 * output} is a directory. If ether conditions are false, this method WILL throw an I/O
	 * exception.
	 *
	 * @param input     the input file.
	 * @param output    the output file.
	 * @param variables pre-defined variables.
	 * @throws NullPointerException if the given {@code input} or {@code output} or {@code
	 *                              variables} is null.
	 * @throws IOException          if any I/O exception occurs.
	 * @throws ParseException       if any parse exception occurs.
	 * @since 0.0.1 ~2020.09.20
	 */
	public static void process(File input, File output, Map<String, String> variables) throws IOException {
		Objects.requireNonNull(input, "input");
		Objects.requireNonNull(output, "output");
		Objects.requireNonNull(variables, "variables");

		Memory memory = Jamplate.createMemory(variables);

		Jamplate.INSTANCE.process(input, output, memory);
	}

	/**
	 * Process the given {@code input} string, then append the results to the given {@code output}
	 * appendable.
	 *
	 * @param input     the input text.
	 * @param output    the output appendable to append the results to.
	 * @param variables pre-defined variables.
	 * @return the results of processing the given {@code input}.
	 * @throws NullPointerException if the given {@code input} or {@code output} or {@code
	 *                              variables} is null.
	 * @throws IOException          if any I/O exception occurs.
	 * @throws ParseException       if any parse exception occurs.
	 * @since 0.0.1 ~2020.09.20
	 */
	public static Appendable process(String input, Appendable output, Map<String, String> variables) throws IOException {
		Objects.requireNonNull(input, "input");
		Objects.requireNonNull(output, "output");
		Objects.requireNonNull(variables, "variables");

		Memory memory = Jamplate.createMemory(variables);

		return Jamplate.INSTANCE.process(input, output, memory);
	}

	/**
	 * Process the given {@code input} string, then return the results.
	 *
	 * @param input     the input text.
	 * @param variables pre-defined variables.
	 * @return the results of processing the given {@code input}.
	 * @throws NullPointerException if the given {@code input} or {@code variables} is null.
	 * @throws IOException          if any I/O exception occurs.
	 * @throws ParseException       if any parse exception occurs.
	 * @since 0.0.1 ~2020.09.20
	 */
	public static String process(String input, Map<String, String> variables) throws IOException {
		Objects.requireNonNull(input, "input");
		Objects.requireNonNull(variables, "variables");

		Memory memory = Jamplate.createMemory(variables);

		return Jamplate.INSTANCE.process(input, memory);
	}

	/**
	 * Create a new memory from the given {@code variables} map.
	 *
	 * @param variables the variables map to create a memory from.
	 * @return a memory from the given {@code variables} map.
	 * @throws NullPointerException if the given {@code variables} is null.
	 * @since 0.0.1 ~2020.09.20
	 */
	protected static Memory createMemory(Map<String, String> variables) {
		Objects.requireNonNull(variables, "variables");
		Map<String, Logic> map = new HashMap<>();
		variables.forEach((address, logic) ->
				map.put(address, new Literal(logic))
		);
		return new MapMemory(map);
	}

	/**
	 * The default jamplate parser that parses {@link String}s into {@link Logic}s.
	 *
	 * @author LSafer
	 * @version 0.0.6
	 * @since 0.0.1 ~2020.09.19
	 */
	public static class LogicParser extends AbstractParser<Logic> {
		/**
		 * Construct a new logic parser.
		 *
		 * @param votes the votes to be used by the constructed logic parser.
		 * @since 0.0.b ~2020.10.03
		 */
		public LogicParser(List<Vote<? extends Logic>> votes) {
			super(votes);
		}

		@Override
		public Logic make(List poll) {
			Objects.requireNonNull(poll, "poll");

			if (poll.size() == 0)
				return new Literal("");

			if (poll.size() == 1) {
				Object object = poll.get(0);

				if (object instanceof Logic)
					return (Logic) object;

				throw new ParseException("Invalid logic", Objects.toString(object, ""));
			}

			throw new ParseException("Invalid statement", Poll.toString(poll));
		}
	}
}
//		/**
//		 * A pattern that detects constants in jamplate logic statements. The pattern should be used
//		 * after clearing:
//		 * <ul>
//		 *     <li>parenthesis</li>
//		 *     <li>any wrapper syntax that could be escaped in a string literal...</li>
//		 * </ul>
//		 *
//		 * @since 0.0.1 ~2020.09.19
//		 */
//		protected final Pattern PATTERN_CONSTANT = Pattern.compile("\"([^\"]|((?<=\\\\)\"))*\"");
//		/**
//		 * A pattern that detects references in jamplate logic statements. The pattern should be
//		 * used after clearing:
//		 * <ul>
//		 *     <li>parenthesis</li>
//		 *     <li>constants</li>
//		 *     <li>any wrapper syntax...</li>
//		 * </ul>
//		 *
//		 * @since 0.0.1 ~2020.09.19
//		 */
//		protected final Pattern PATTERN_REFERENCE = Pattern.compile("(-|\\w|\\d)+");
//		/**
//		 * A pattern that detects whitespaces or any places that could be a whitespace. The pattern
//		 * should be used after clearing:
//		 * <ul>
//		 *     <li>parenthesis</li>
//		 *     <li>constants</li>
//		 *     <li>references</li>
//		 *     <li>any syntax that could contain a whitespace...</li>
//		 * </ul>
//		 *
//		 * @since 0.0.1 ~2020.09.19
//		 */
//		protected final Pattern PATTERN_WHITESPACES = Pattern.compile("(\\s+)|[|]|[+]|[&]|(!=)|(==)|(!(?!=))");
