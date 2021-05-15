///*
// *	Copyright 2021 Cufy
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
//package org.jamplate.processor.compiler;
//
//import org.jamplate.model.node.Node;
//import org.jamplate.runtime.compilation.Compilation;
//
//import java.io.IOError;
//
///**
// *
// * @author LSafer
// * @version 0.2.0
// * @since 0.2.0 ~2021.02.09
// */
//@FunctionalInterface
//public interface Compiler {
//	/**
//	 * Compile the given {@code node} or any node around it.
//	 *
//	 * @param compilation the compilation that called this compiler. (might be null)
//	 * @param node        the node to be compiled.
//	 * @return if any modification occurred in the node or any node around it.
//	 * @throws NullPointerException  if the given {@code node} is null.
//	 * @throws IllegalStateException if a node has a deserialized property and that
//	 *                               property was accessed.
//	 * @throws IOError               if any I/O error occurs.
//	 * @since 0.2.0 ~2021.02.14
//	 */
//	boolean compile(Compilation compilation, Node node);
//}
////	/**
////	 * Compile the given {@code sketch} and the necessary sketches around it and return
////	 * the compiled sketches in an unmodifiable container (to mark as compiled). and put
////	 * the compiled elements in the given {@code environment} respectfully.
////	 *
////	 * @param environment the environment compiling at.
////	 * @param root        the root sketch containing (or itself) the given {@code
////	 *                    sketch}.
////	 * @param sketch      the sketch to be compiled. (eventually)
////	 * @return a set containing the compiled sketches. Or an empty collection if no
////	 * 		sketches was compiled.
////	 * @throws NullPointerException  if the given {@code environment} or {@code root} or
////	 *                               {@code sketch} is null.
////	 * @throws IllegalStateException if the given {@code root} or {@code sketch} is
////	 *                               deserialized or has a deserialized reference or a
////	 *                               deserialized document.
////	 * @throws IOError               if any I/O error occur.
////	 * @since 0.2.0 ~2021.02.14
////	 */
////	Set<Sketch> compile(Environment environment, Sketch root, Sketch sketch);
