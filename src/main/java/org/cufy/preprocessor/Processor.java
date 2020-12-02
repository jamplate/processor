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
//import java.io.File;
//import java.io.IOException;
//
///**
// * A processor is a class that takes an input and do everything from parsing, linking to invoking.
// *
// * @author LSafer
// * @version 0.0.1
// * @since 0.0.1 ~2020.09.20
// */
//@Deprecated
//public interface Processor {
//	/**
//	 * Process the given {@code input} and write the result to the given {@code output}.
//	 *
//	 * @param input  the input to be processed.
//	 * @param output the output to write the output to.
//	 * @param memory the memory to start invoking with.
//	 * @throws IOException    if any I/O exception occurs.
//	 * @throws ParseException if any parse exception occurs.
//	 * @since 0.0.1 ~2020.09.20
//	 */
//	void process(File input, File output, Memory memory) throws IOException;
//
//	/**
//	 * Process the given {@code input} and return the result.
//	 *
//	 * @param input  the input to be processed.
//	 * @param memory the memory to start invoking with.
//	 * @return the result of processing the given {@code input}.
//	 * @throws IOException    if any I/O exception occurs.
//	 * @throws ParseException if any parse exception occurs.
//	 * @since 0.0.1 ~2020.09.20
//	 */
//	String process(String input, Memory memory) throws IOException;
//
//	/**
//	 * Process the given {@code input} and write the result to the given {@code output}.
//	 *
//	 * @param input  the input to be processed.
//	 * @param output the output to write the output to.
//	 * @param memory the memory to start invoking with.
//	 * @return the output of processing the given {@code input}.
//	 * @throws IOException    if any I/O exception occurs.
//	 * @throws ParseException if any parse exception occurs.
//	 * @since 0.0.1 ~2020.09.20
//	 */
//	Appendable process(String input, Appendable output, Memory memory) throws IOException;
//}
