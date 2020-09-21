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

import org.jamplate.logic.Constant;
import org.jamplate.logic.Logic;
import org.jamplate.memory.MapMemory;
import org.jamplate.memory.Memory;
import org.jamplate.parser.LogicParser;
import org.jamplate.parser.ScopeParser;
import org.jamplate.processor.ScopeProcessor;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * The main entry-point to the jamplate pre-processor.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2020.09.20
 */
public final class Jamplate {
	/**
	 * A global instance of the class {@link LogicParser}.
	 *
	 * @since 0.0.1 ~2020.09.20
	 */
	private static final LogicParser LOGIC_PARSER = new LogicParser();
	/**
	 * A global instance of the class {@link ScopeParser}.
	 *
	 * @since 0.0.1 ~2020.09.20
	 */
	private static final ScopeParser SCOPE_PARSER = new ScopeParser(Jamplate.LOGIC_PARSER);
	/**
	 * A global instance of the class {@link ScopeProcessor}.
	 *
	 * @since 0.0.1 ~2020.09.20
	 */
	private static final ScopeProcessor PROCESSOR = new ScopeProcessor(Jamplate.SCOPE_PARSER);

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

		Jamplate.PROCESSOR.process(input, output, memory);
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

		return Jamplate.PROCESSOR.process(input, output, memory);
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

		return Jamplate.PROCESSOR.process(input, memory);
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
				map.put(address, new Constant(logic))
		);
		return new MapMemory(map);
	}
}
