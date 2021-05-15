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
//package org.jamplate.runtime.compilation;
//
//import org.jamplate.model.Reference;
//import org.jamplate.model.document.Document;
//import org.jamplate.model.node.Node;
//import org.jamplate.processor.compiler.Compiler;
//import org.jamplate.processor.linker.Linker;
//import org.jamplate.processor.parser.Parser;
//
//import java.io.IOError;
//
///**
// * A compilation is a fully-stated linear-storage object that acts like the core processor
// * of a single process.
// * <br>
// * <table width="100%" style="margin: 20;">
// *     <tr>
// *         <th>Method</th>
// *         <th>Must execute before it</th>
// *         <th>Must execute after it</th>
// *         <th>Description</th>
// *         <th>tags</th>
// *     </tr>
// *     <tr>
// *         <td>{@link #use(Parser)}</td>
// *         <td></td>
// *         <td></td>
// *         <td>Adds a parser to be used</td>
// *         <td>idempotent internal linear</td>
// *     </tr>
// *     <tr>
// *         <td>{@link #use(Compiler)}</td>
// *         <td></td>
// *         <td></td>
// *         <td>Adds a compiler to be used</td>
// *         <td>idempotent internal linear</td>
// *     </tr>
// *     <tr>
// *         <td>{@link #use(Linker)}</td>
// *         <td></td>
// *         <td></td>
// *         <td>Adds a linker to be used</td>
// *         <td>idempotent internal linear</td>
// *     </tr>
// *     <tr>
// *         <td>{@link #append(Node)}</td>
// *         <td></td>
// *         <td>{@link #concrete()}</td>
// *         <td>Appends a node to the compilation</td>
// *         <td>idempotent internal linear</td>
// *     </tr>
// *     <tr>
// *         <td>{@link #append(Document)}</td>
// *         <td></td>
// *         <td>{@link #concrete()}</td>
// *         <td>Appends a document to the compilation</td>
// *         <td>idempotent internal linear</td>
// *     </tr>
// *     <tr>
// *         <td>{@link #append(Reference)}</td>
// *         <td></td>
// *         <td>{@link #concrete()}</td>
// *         <td>Appends a reference to the compilation</td>
// *         <td>idempotent internal linear</td>
// *     </tr>
// *     <tr>
// *         <td>{@link #concrete()}</td>
// *         <td></td>
// *         <td></td>
// *         <td>Generates missing items in the nodes</td>
// *         <td>idempotent internal linear</td>
// *     </tr>
// *     <tr>
// *         <td>{@link #skeleton()}</td>
// *         <td>{@link #concrete()}</td>
// *         <td></td>
// *         <td>Rebuild the links between the nodes</td>
// *         <td>idempotent internal</td>
// *     </tr>
// *     <tr>
// *         <td>{@link #parse()}</td>
// *         <td>{@link #concrete()} {@link #use(Parser)}</td>
// *         <td>{@link #concrete()} {@link #skeleton()}</td>
// *         <td>Parse any parsable sketches in the nodes</td>
// *         <td>idempotent outsourced linear</td>
// *     </tr>
// *     <tr>
// *         <td>{@link #compile()}</td>
// *         <td>{@link #use(Compiler)}</td>
// *         <td></td>
// *         <td>Compile the nodes</td>
// *         <td>idempotent outsourced linear</td>
// *     </tr>
// * </table>
// *
// * @author LSafer
// * @version 0.2.0
// * @since 0.2.0 ~2021.01.31
// */
//public interface Compilation {
//	/**
//	 * Add the given {@code node} to the nodes of this compilation.
//	 * <br>
//	 * After invoking this method, you might need to call {@link #concrete()} to make it
//	 * operable.
//	 * <br>
//	 * Invoking this method multiple times with the {@link Object#equals(Object) same}
//	 * argument <b>at the same state</b> will result to the same behaviour as invoking it
//	 * one time.
//	 * <br>
//	 * Once method is called, its effect cannot be undone.
//	 *
//	 * @param node the node to be appended.
//	 * @throws NullPointerException if the given {@code node} is null.
//	 * @since 0.2.0 ~2021.02.14
//	 */
//	void append(Node node);
//
//	/**
//	 * Append the given {@code document} to this compilation with the appropriate method:
//	 * <br>
//	 * <table width="100%" style="margin: 20;">
//	 *     <tr>
//	 *         <th>Condition</th>
//	 *         <th>Method</th>
//	 *     </tr>
//	 *     <tr>
//	 *         <td>Found a node with an equal document</td>
//	 *         <td>Nothing happens</td>
//	 *     </tr>
//	 *     <tr>
//	 *         <td>Found a node that represents the document and is document mutable</td>
//	 *         <td>The document get set to the found node</td>
//	 *     </tr>
//	 *     <tr>
//	 *         <td>Found a node that represents the document and is document immutable</td>
//	 *         <td>Nothing happens</td>
//	 *     </tr>
//	 *     <tr>
//	 *         <td>Otherwise</td>
//	 *         <td>A new node get created and added with the document set to it</td>
//	 *     </tr>
//	 * </table>
//	 * <br>
//	 * After invoking this method, you might need to call {@link #concrete()} to make it
//	 * operable.
//	 * <br>
//	 * Invoking this method multiple times with the {@link Object#equals(Object) same}
//	 * argument will result to the same behaviour as invoking it one time.
//	 * <br>
//	 * Once method is called, its effect cannot be undone.
//	 *
//	 * @param document the document to be appended to this compilation.
//	 * @throws NullPointerException if the given {@code document} is null.
//	 * @since 0.2.0 ~2021.02.01
//	 */
//	void append(Document document);
//
//	/**
//	 * Append the given {@code reference} to this compilation with the appropriate
//	 * method:
//	 * <br>
//	 * <table width="100%" style="margin: 20;">
//	 *     <tr>
//	 *         <th>Condition</th>
//	 *         <th>Method</th>
//	 *     </tr>
//	 *     <tr>
//	 *         <td>Found a node with an equal reference</td>
//	 *         <td>Nothing happens</td>
//	 *     </tr>
//	 *     <tr>
//	 *         <td>Found a node that represents the reference and is reference mutable</td>
//	 *         <td>The reference get set to the found node</td>
//	 *     </tr>
//	 *     <tr>
//	 *         <td>Found a node that represents the reference and is reference immutable</td>
//	 *         <td>Nothing happens</td>
//	 *     </tr>
//	 *     <tr>
//	 *         <td>Otherwise</td>
//	 *         <td>A new node get created and added with the reference set to it</td>
//	 *     </tr>
//	 * </table>
//	 * <br>
//	 * After invoking this method, you might need to call {@link #concrete()} to make it
//	 * operable.
//	 * <br>
//	 * Invoking this method multiple times with the {@link Object#equals(Object) same}
//	 * argument will result to the same behaviour as invoking it one time.
//	 * <br>
//	 * Once method is called, its effect cannot be undone.
//	 *
//	 * @param reference the reference to be appended to this compilation.
//	 * @throws NullPointerException if the given {@code reference} is null.
//	 * @since 0.2.0 ~2021.02.01
//	 */
//	void append(Reference reference);
//	//
//	//	/**
//	//	 * Append the given {@code sketch} to this compilation with the appropriate method:
//	//	 * <br>
//	//	 * <table width="100%" style="margin: 20;">
//	//	 *     <tr>
//	//	 *         <th>Condition</th>
//	//	 *         <th>Method</th>
//	//	 *     </tr>
//	//	 *     <tr>
//	//	 *         <td>Found a node with an equal sketch</td>
//	//	 *         <td>Nothing happens</td>
//	//	 *     </tr>
//	//	 *     <tr>
//	//	 *         <td>Found a node that represents the sketch and has no element and is sketch mutable</td>
//	//	 *         <td>The sketch get set to the found node</td>
//	//	 *     </tr>
//	//	 *     <tr>
//	//	 *         <td>Found a node that represents the sketch and has no element and is sketch immutable</td>
//	//	 *         <td>Nothing happens</td>
//	//	 *     </tr>
//	//	 *     <tr>
//	//	 *         <td>Otherwise</td>
//	//	 *         <td>A new node get created and added with the sketch set to it</td>
//	//	 *     </tr>
//	//	 * </table>
//	//	 * <br>
//	//	 * After invoking this method, you might need to call {@link #concrete()} to make it
//	//	 * operable.
//	//	 * <br>
//	//	 * Invoking this method multiple times with the {@link Object#equals(Object) same}
//	//	 * argument will result to the same behaviour as invoking it one time.
//	//	 * <br>
//	//	 * Once method is called, its effect cannot be undone.
//	//	 *
//	//	 * @param sketch the sketch to be appended.
//	//	 * @throws NullPointerException if the given {@code sketch} is null.
//	//	 * @since 0.2.0 ~2021.02.09
//	//	 */
//	//	void append(Sketch sketch);
////
////	/**
////	 * Append the given {@code element} to this compilation with the appropriate method:
////	 * <br>
////	 * <table width="100%" style="margin: 20;">
////	 *     <tr>
////	 *         <th>Condition</th>
////	 *         <th>Method</th>
////	 *     </tr>
////	 *     <tr>
////	 *         <td>Found a node with an equal element</td>
////	 *         <td>Nothing happens</td>
////	 *     </tr>
////	 *     <tr>
////	 *         <td>Found a node that represents the element and has no sketch and is element mutable</td>
////	 *         <td>The element get set to the found node</td>
////	 *     </tr>
////	 *     <tr>
////	 *         <td>Found a node that represents the element and has no sketch and is element immutable</td>
////	 *         <td>Nothing happens</td>
////	 *     </tr>
////	 *     <tr>
////	 *         <td>Otherwise</td>
////	 *         <td>A new node get created and added with the element set to it</td>
////	 *     </tr>
////	 * </table>
////	 * <br>
////	 * After invoking this method, you might need to call {@link #concrete()} to make it
////	 * operable.
////	 * <br>
////	 * Invoking this method multiple times with the {@link Object#equals(Object) same}
////	 * argument will result to the same behaviour as invoking it one time.
////	 * <br>
////	 * Once method is called, its effect cannot be undone.
////	 *
////	 * @param element the element to be appended.
////	 * @throws NullPointerException if the given {@code element} is null.
////	 * @since 0.2.0 ~2021.02.14
////	 */
////	void append(Element element);
//
//	/**
//	 * Using the compilers of this compilation, compile the nodes of the nodes in this
//	 * compilation. The compiling process is about infinitely looping over every compiler
//	 * in this compilation with every node in this compilation until all the compilers
//	 * returns {@code false} (or in other words, perform no operation)
//	 * <br>
//	 * Invoking this method multiple times <b>at the same state</b> will result to the
//	 * same behaviour as invoking it one time.
//	 * <br>
//	 * Once method is called, its effect cannot be undone.
//	 * <br><br>
//	 * Note: any throwable thrown by any compiler used will fall through this method to
//	 * the caller.
//	 *
//	 * @throws IllegalStateException if a node has a deserialized property and that
//	 *                               property was accessed.
//	 * @throws IOError               if any I/O error occurs.
//	 * @since 0.2.0 ~2021.02.15
//	 */
//	void compile();
//
//	//	/**
//	//	 * Concrete all the nodes in this compilation with the missing data from its data.
//	//	 * <br>
//	//	 * The data that will be generated are the following:
//	//	 * <table width="100%" style="margin: 20;">
//	//	 *     <tr>
//	//	 *         <th>Generating</th>
//	//	 *         <th>Condition</th>
//	//	 *         <th>Source #1</th>
//	//	 *         <th>Source #2</th>
//	//	 *         <th>Source #3</th>
//	//	 *     </tr>
//	//	 *     <tr>
//	//	 *         <td>{@link Node#setDocument(Document) Document}</td>
//	//	 *         <td></td>
//	//	 *         <td>{@link Node#reference() Reference}</td>
//	//	 *         <td>{@link Node#getSketch() Sketch}</td>
//	//	 *         <td>{@link Node#getElement() Element}</td>
//	//	 *     </tr>
//	//	 *     <tr>
//	//	 *         <td>{@link Node#setReference(Reference) Reference}</td>
//	//	 *         <td></td>
//	//	 *         <td>{@link Node#getSketch() Sketch}</td>
//	//	 *         <td>{@link Node#getElement() Element}</td>
//	//	 *         <td>{@link Node#document() Document}</td>
//	//	 *     </tr>
//	//	 *     <tr>
//	//	 *         <td>{@link Node#reference() Reference}</td>
//	//	 *         <td>{@link Node#document() Document}</td>
//	//	 *     </tr>
//	//	 * </table>
//	//	 * <br>
//	//	 * Invoking this method multiple times <b>at the same state</b> will result to the
//	//	 * same behaviour as invoking it one time.
//	//	 * <br>
//	//	 * Once method is called, its effect cannot be undone.
//	//	 *
//	//	 * @throws IllegalStateException if a node has a deserialized property and that
//	//	 *                               property was accessed.
//	//	 * @throws IOError               if any I/O error occurs.
//	//	 * @since 0.2.0 ~2021.02.14
//	//	 */
//	void concrete();
//
//	//	/**
//	//	 * Execute the current instructions of this compilation.
//	//	 *
//	//	 * @return {@code 0} if the instructions got executed successfully. Not {@code 0} if
//	//	 * 		otherwise.
//	//	 * @throws RuntimeException if any runtime exception occurs.
//	//	 * @throws Error            if any error occurs.
//	//	 * @since 0.2.0 ~2021.02.14
//	//	 */
//	//	int execute();
//
//	/**
//	 * Using the linkers of this compilation, link the elements in this compilation with
//	 * the other elements.
//	 * <br>
//	 * Once method is called, its effect cannot be undone.
//	 *
//	 * @since 0.2.0 ~2021.02.15
//	 */
//	void link();
//
//	//	/**
//	//	 * Using the parsers of this compilation, parse the sketches of the nodes in this
//	//	 * compilation. Each sketch is parsed alone. After the end of the parsing, the new
//	//	 * sketches will be {@link Sketch#put(Sketch) put} to their parents and then {@link
//	//	 * #append(Sketch) appended} to this compilation. The process will be repeated until
//	//	 * all the parsers stop providing new sketches. Any exception occurs will stop the
//	//	 * whole method and the successfully put or appended sketches won't be removed.
//	//	 * <br>
//	//	 * Invoking this method multiple times <b>at the same state</b> will result to the
//	//	 * same behaviour as invoking it one time.
//	//	 * <br>
//	//	 * Once method is called, its effect cannot be undone.
//	//	 * <br><br>
//	//	 * Note: any throwable thrown by any parser used will fall through this method to the
//	//	 * caller.
//	//	 *
//	//	 * @throws UnsupportedOperationException if a parser generated a sketch from a sketch
//	//	 *                                       that does not support inner sketches.
//	//	 * @throws IllegalArgumentException      if a parser generated a sketch from a sketch
//	//	 *                                       that cannot be put at.
//	//	 * @throws IllegalStateException         if a node has a deserialized property and
//	//	 *                                       that property was accessed; if a parser
//	//	 *                                       generated a sketch that clashes with another
//	//	 *                                       sketch.
//	//	 * @throws IOError                       if any I/O error occurs.
//	//	 * @since 0.2.0 ~2021.02.01
//	//	 */
//	void parse();
//
//	//	/**
//	//	 * Rebuild the relations between the nodes in this compilation. Only the nodes that
//	//	 * {@link Node#hasReference() has a reference} will be involved. All the nodes that
//	//	 * {@link Node#hasReference() has a reference} will be cleared from any previously
//	//	 * associated relations in both ways (node-other & other-node). The relations of all
//	//	 * the nodes that is not involved that was related to the nodes involved must be
//	//	 * rebuilt before been used again since the nodes involved might play an important
//	//	 * rule in the relations before been cleared.
//	//	 * <br>
//	//	 * Invoking this method multiple times <b>at the same state</b> will result to the
//	//	 * same behaviour as invoking it one time.
//	//	 * <br>
//	//	 * Once method is called, its effect cannot be undone.
//	//	 *
//	//	 * @since 0.2.0 ~2021.02.15
//	//	 */
//	void skeleton();
//
//	/**
//	 * Add the given {@code linker} to the linkers set of this compilation to be used by
//	 * it.
//	 * <br>
//	 * Very Important Note: the linkers in a compilation must not target clash-able sketch
//	 * patterns. Otherwise, clash exceptions might be thrown!
//	 * <br>
//	 * Invoking this method multiple times with the {@link Object#equals(Object) same}
//	 * argument will result to the same behaviour as invoking it one time.
//	 * <br>
//	 * Once method is called, its effect cannot be undone.
//	 *
//	 * @param compiler the linker to be used.
//	 * @throws NullPointerException if the given {@code linker} is null.
//	 * @since 0.2.0 ~2021.02.10
//	 */
//	void use(Compiler compiler);
//
//	/**
//	 * Add the given {@code parser} to the parsers set of this compilation to be used by
//	 * it.
//	 * <br>
//	 * Very Important Note: the parsers in a compilation must not target clash-able
//	 * syntax. Otherwise, clash exceptions might be thrown!
//	 * <br>
//	 * Invoking this method multiple times with the {@link Object#equals(Object) same}
//	 * argument will result to the same behaviour as invoking it one time.
//	 * <br>
//	 * Once method is called, its effect cannot be undone.
//	 *
//	 * @param parser the parser to be used.
//	 * @throws NullPointerException if the given {@code parser} is null.
//	 * @since 0.2.0 ~2021.02.01
//	 */
//	void use(Parser parser);
//
//	void use(Linker linker);
//}
////old concrete();
////----------------------------------------------------------------------------------------
////	/**
////	 * Construct concrete sketches for the sources that has no concrete (root) sketches.
////	 * <br>
////	 * Expected operations:
////	 * <ul>
////	 *     <li>
////	 *         The new documents that got {@link #append(Document)} will be wrapped with a
////	 *         {@link Reference} and added to the references in this and the appended
////	 *         reference then wrapped with a {@link Sketch} and added to the sketches in
////	 *         this.
////	 *     </li>
////	 *     <li>
////	 *         The new references that got {@link #append(Reference)} will be wrapped with
////	 *         a {@link Sketch} and added to the sketches in this.
////	 *     </li>
////	 * </ul>
////	 * <br>
////	 * <pre>
////	 *     {@link Document} & {@link Reference} -> {@link Sketch}
////	 * </pre>
////	 * <br>
////	 * Invoking this method multiple times with with this compilation or any of its inner
////	 * parts and elements not changing will result the same as invoking it one time.
////	 *
////	 * @throws RuntimeException if any runtime exception occurs.
////	 * @throws Error            if any error occurs.
////	 * @since 0.2.0 ~2021.02.14
////	 */
////	void concrete();
////----------------------------------------------------------------------------------------
////old skeleton();
////----------------------------------------------------------------------------------------
////	/**
////	 * Build the skeleton of this compilation.
////	 * <br>
////	 * Expected operations:
////	 * <ul>
////	 *     <li>
////	 *         The old skeleton ({@link Node node} collection) will be replaced with a
////	 *         new skeleton from the {@link Sketch sketches} currently in this
////	 *         compilation.
////	 *     </li>
////	 * </ul>
////	 * <br>
////	 * <pre>
////	 *     {@link Sketch} -> {@link Node}
////	 * </pre>
////	 * <br>
////	 * Invoking this method multiple times with with this compilation or any of its inner
////	 * parts and elements not changing will result the same as invoking it one time.
////	 *
////	 * @throws RuntimeException if any runtime exception occurs.
////	 * @throws Error            if any error occurs.
////	 * @since 0.2.0 ~2021.02.14
////	 */
////	void skeleton();
////----------------------------------------------------------------------------------------
//
////
////	/**
////	 * Append the given {@code instruction} to the instructions in this compilation.
////	 * <br>
////	 * TODO
////	 * <br>
////	 * Invoking this method multiple times with the {@link Object#equals(Object) same}
////	 * argument <b>at the same state</b> will result to the same behaviour as invoking it
////	 * one time.
////	 * <br>
////	 * Once method is called, its effect cannot be undone.
////	 *
////	 * @param instruction the instruction to be appended.
////	 * @throws NullPointerException if the given {@code instruction} is null.
////	 */
////	void append(Instruction instruction);
//
////	/**
////	 * Using the {@link #use(Compiler) compilers} of this compilation, compile the node
////	 * skeleton in this compilation into elements.
////	 * <br>
////	 * Expected operations:
////	 * <ul>
////	 *     <li>
////	 *         The old elements will be replaced with a new elements from compiling the
////	 *         current skeleton of this compilation.
////	 *     </li>
////	 * </ul>
////	 * <br>
////	 * <pre>
////	 *     {@link Node} -> {@link Element}
////	 * </pre>
////	 * <br>
////	 * Invoking this method multiple times with with this compilation or any of its inner
////	 * parts and elements not changing will result the same as invoking it one time.
////	 *
////	 * @throws RuntimeException if any runtime exception occurs.
////	 * @throws Error            if any error occurs.
////	 * @since 0.2.0 ~2021.02.10
////	 */
//
////	/**
////	 * Translate the elements in this compilation into an executable instructions.
////	 * <br>
////	 * Expected operations:
////	 * <ul>
////	 *     <li>
////	 *         The old instructions will be replaced with a new instructions from
////	 *         translating the current elements of this compilation.
////	 *     </li>
////	 * </ul>
////	 * <br>
////	 * <pre>
////	 *     {@link Element} -> {@link Instruction}
////	 * </pre>
////	 * <br>
////	 * Invoking this method multiple times with with this compilation or any of its inner
////	 * parts and elements not changing will result the same as invoking it one time.
////	 *
////	 * @throws RuntimeException if any runtime exception occurs.
////	 * @throws Error            if any error occurs.
////	 * @since 0.2.0 ~2021.02.14
////	 */
////	void translate();
//
////	/**
////	 * Adapt the given {@code compilation} as a sub compilation to this compilation.
////	 * <br>
////	 * The adapted compilation shall be read-only by this compilation.
////	 * <br>
////	 * Invoking this method multiple times with the {@link Object#equals(Object) same}
////	 * argument will result to the same behaviour as invoking it one time.
////	 *
////	 * @param compilation the compilation to be adapted by this compilation.
////	 * @throws NullPointerException if the given {@code compilation} is null.
////	 * @since 0.2.0 ~2021.02.08
////	 */
////	void adapt(Compilation compilation);
