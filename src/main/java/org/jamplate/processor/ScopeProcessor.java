package org.jamplate.processor;

import org.jamplate.memory.Memory;
import org.jamplate.parser.Parser;
import org.jamplate.scope.Scope;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

/**
 * A processor that process files to files.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2020.09.20
 */
public class ScopeProcessor implements Processor {
	/**
	 * The logic parser used by this processor.
	 *
	 * @since 0.0.1 ~2020.09.20
	 */
	protected final Parser<Scope> scope;

	/**
	 * Construct a new file processor that uses the given {@code parser} parser and the given {@code
	 * logic} parser.
	 *
	 * @param parser the parser parser used by this file processor.
	 * @throws NullPointerException if the given {@code parser} or {@code logic} is null.
	 * @since 0.0.1 ~2020.09.20
	 */
	public ScopeProcessor(Parser<Scope> parser) {
		Objects.requireNonNull(parser, "parser");
		this.scope = parser;
	}

	@Override
	public Parser<Scope> parser() {
		return this.scope;
	}

	@Override
	public void process(File input, File output, Memory memory) throws IOException {
		Objects.requireNonNull(input, "input");
		Objects.requireNonNull(output, "output");
		Objects.requireNonNull(memory, "memory");

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

		//reading stage
		String string = builder.toString();
		//parsing and linking stage
		Scope scope = this.scope.parse(string);
		//invoking stage
		scope.invoke(output, memory);
	}

	@Override
	public Appendable process(String input, Appendable output, Memory memory) throws IOException {
		Objects.requireNonNull(input, "input");
		Objects.requireNonNull(output, "output");
		Objects.requireNonNull(memory, "memory");

		//parsing and linking stage
		Scope scope = this.scope.parse(input);
		//invoking stage
		return scope.invoke(output, memory);
	}
}
