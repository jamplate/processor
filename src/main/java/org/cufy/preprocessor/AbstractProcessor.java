///*
// *	Copyright 2020 Cufy
// *
// *	Licensed under the Apache License, Version 2.0 (the "License");
// *	you may not use this file except in compliance with the License.
// *	You may obtain a copy of the License at
// *
// *	    http://www.apache.org/licenses/LICENSE-2.0
// *
// *	Unless required by applicable law or agreed to in writing, software
// *	distributed under the License is distributed on an "AS IS" BASIS,
// *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// *	See the License for the specific language governing permissions and
// *	limitations under the License.
// */
//package org.cufy.preprocessor;
//
//import org.cufy.preprocessor.Parser;
//import org.cufy.preprocessor.token.AbstractInvokeToken;
//
//import java.io.File;
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.Objects;
//
///**
// * An abstraction for the {@link Processor} class. Implementing the essential methods any processor
// * would implement.
// *
// * @author LSafer
// * @version 0.0.b
// * @since 0.0.b ~2020.10.02
// */
//@Deprecated
//public abstract class AbstractProcessor implements Processor {
//	/**
//	 * The parser used by this processor.
//	 *
//	 * @since 0.0.1 ~2020.09.20
//	 */
//	protected final Parser<Scope> parser;
//
//	/**
//	 * Construct a new file processor that uses the given {@code parser}.
//	 *
//	 * @param parser the parser used by this file processor.
//	 * @throws NullPointerException if the given {@code parser} is null.
//	 * @since 0.0.1 ~2020.09.20
//	 */
//	public AbstractProcessor(Parser<Scope> parser) {
//		Objects.requireNonNull(parser, "parser");
//		this.parser = parser;
//	}
//
//	@Override
//	public void process(File input, File output, Memory memory) throws IOException {
//		Objects.requireNonNull(input, "input");
//		Objects.requireNonNull(output, "output");
//		Objects.requireNonNull(memory, "memory");
//
//		StringBuilder builder = new StringBuilder();
//		try (FileReader reader = new FileReader(input)) {
//			char[] buffer = new char[1024];
//
//			while (true) {
//				int length = reader.read(buffer);
//
//				if (length == -1)
//					break;
//				else
//					builder.append(buffer, 0, length);
//			}
//		}
//
//		//reading stage
//		String string = builder.toString();
//		//parsing and linking stage
//		Scope scope = this.parser.parse(string);
//		//invoking stage
//		scope.invoke(new AbstractInvokeToken(new FileWriter(output), memory) {
//		});
//	}
//
//	@Override
//	public Appendable process(String input, Appendable output, Memory memory) throws IOException {
//		Objects.requireNonNull(input, "input");
//		Objects.requireNonNull(output, "output");
//		Objects.requireNonNull(memory, "memory");
//
//		//parsing and linking stage
//		Scope scope = this.parser.parse(input);
//		//invoking stage
//		return scope.invoke(output, memory);
//	}
//
//	@Override
//	public String process(String input, Memory memory) throws IOException {
//		return null;
//	}
//}
