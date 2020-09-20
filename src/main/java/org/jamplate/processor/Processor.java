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
package org.jamplate.processor;

import org.jamplate.ParseException;
import org.jamplate.memory.Memory;
import org.jamplate.parser.Parser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A processor is a class that takes an input and do everything from parsing, linking to invoking.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2020.09.20
 */
public interface Processor {
	/**
	 * Process the given {@code input} and write the result to the given {@code output}.
	 *
	 * @param input  the input to be processed.
	 * @param output the output to write the output to.
	 * @param memory the memory to start invoking with.
	 * @throws IOException            if any I/O exception occurs.
	 * @throws ParseException if any parse exception occurs.
	 * @since 0.0.1 ~2020.09.20
	 */
	default void process(File input, File output, Memory memory) throws IOException {
		StringBuilder builder = new StringBuilder();
		try (FileReader reader = new FileReader(input)) {
			char[] buffer = new char[1024];

			while (true) {
				int length = reader.read(buffer);

				if (length == -1)
					break;
				else
					builder.append(buffer, 0, length);
			}
		}

		String string = this.process(builder.toString(), memory);

		try (FileWriter writer = new FileWriter(output)) {
			writer.write(string);
		}
	}

	/**
	 * Process the given {@code input} and return the result.
	 *
	 * @param input  the input to be processed.
	 * @param memory the memory to start invoking with.
	 * @return the result of processing the given {@code input}.
	 * @throws IOException            if any I/O exception occurs.
	 * @throws ParseException if any parse exception occurs.
	 * @since 0.0.1 ~2020.09.20
	 */
	default String process(String input, Memory memory) throws IOException {
		return this.process(input, new StringBuilder(), memory).toString();
	}

	/**
	 * Return the parser of this processor.
	 *
	 * @return the parser of this processor.
	 * @since 0.0.1 ~2020.09.20
	 */
	Parser parser();

	/**
	 * Process the given {@code input} and write the result to the given {@code output}.
	 *
	 * @param input  the input to be processed.
	 * @param output the output to write the output to.
	 * @param memory the memory to start invoking with.
	 * @return the output of processing the given {@code input}.
	 * @throws IOException            if any I/O exception occurs.
	 * @throws ParseException if any parse exception occurs.
	 * @since 0.0.1 ~2020.09.20
	 */
	Appendable process(String input, Appendable output, Memory memory) throws IOException;
}
