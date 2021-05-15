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
//import java.util.HashSet;
//import java.util.Objects;
//import java.util.Set;
//
///**
// * An abstraction for the interface {@link Compilation} implementing the basic
// * functionality of a compilation.
// *
// * @author LSafer
// * @version 0.2.0
// * @since 0.2.0 ~2021.02.08
// */
//public abstract class AbstractCompilation implements Compilation {
//	/**
//	 * The compilations adapted by this compilation.
//	 *
//	 * @since 0.2.0 ~2021.02.08
//	 */
//	protected final Set<Compilation> compilations = new HashSet<>();
//	/**
//	 * A set of the {@link Compiler compilers} to be used by this when {@link #compile()
//	 * compiling}.
//	 *
//	 * @since 0.2.0 ~2021.02.10
//	 */
//	protected final Set<Compiler> compilers = new HashSet<>();
//	//
//	//	/**
//	//	 * The last instruction set that resulted from the last invoke of {@link
//	//	 * #translate()}.
//	//	 *
//	//	 * @since 0.2.0 ~2021.02.14
//	//	 */
//	//	protected final Set<Instruction> instructions = new HashSet<>();
//
//	/**
//	 * A set of the {@link Linker linkers} to be used by this when  {@link #link()
//	 * linking}.
//	 *
//	 * @since 0.2.0 ~2021.02.14
//	 */
//	protected final Set<Linker> linkers = new HashSet<>();
//
//	/**
//	 * The nodes of this compilation.
//	 *
//	 * @since 0.2.0 ~2021.02.14
//	 */
//	protected final Set<Node> nodes = new HashSet<>();
//
//	/**
//	 * A set of the {@link Parser parsers} to be  used by this when {@link #parse()
//	 * parsing}.
//	 *
//	 * @since 0.2.0 ~2021.02.08
//	 */
//	protected final Set<Parser> parsers = new HashSet<>();
//
//	@Override
//	public void append(Node node) {
//		Objects.requireNonNull(node, "node");
//		//direct `.equals(...)`
//		this.nodes.add(node);
//	}
//
//	@Override
//	public void append(Document document) {
////		Objects.requireNonNull(document, "document");
////		//1. node.document ? node.document == document
////		//2. node.reference ? node.reference == reference(document)
////		//3. node.sketch ? node.sketch.reference == reference(document)
////		//4. node.element ? node.element.reference == reference(document)
////		Optional<Node> optional = this.nodes.parallelStream()
////				.filter(node ->
////						//from document (n.d == d)
////						node.hasDocument() ?
////						Objects.equals(node.document(), document) :
////						//from reference (n.r.d == d && n.r.dr == n.r)
////						node.hasReference() ?
////						Objects.equals(node.reference().document(), document) &&
////						Objects.equals(node.reference().documentReference(), node.reference())
////											:
////						//from sketch (n.s.r.d == d && n.s.r.dr == n.s.r)
////						node.hasSketch() ?
////						Objects.equals(node.getSketch().reference().document(), document) &&
////						Objects.equals(node.getSketch().reference().documentReference(), node.getSketch().reference())
////										 :
////						//from element (n.e.r.d == d && n.e.r.dr == n.e.r)
////						node.hasElement() && node.getElement().reference() != null &&
////						Objects.equals(node.getElement().reference().document(), document) &&
////						Objects.equals(node.getElement().reference().documentReference(), node.getElement().reference())
////				)
////				.findAny();
////
////		if (optional.isPresent()) {
////			Node node = optional.get();
////
////			if (!node.hasDocument() && node.mutableDocument())
////				node.setDocument(document);
////		} else
////			this.nodes.add(new FlexNode(document));
//	}
//
//	@Override
//	public void append(Reference reference) {
////		Objects.requireNonNull(reference, "reference");
////		//1. node.reference ? node.reference == reference
////		//2. node.sketch ? node.sketch.reference == reference
////		//3. node.element ? node.element.reference == reference
////		//4. node.document ? reference(node.document) == reference
////		Optional<Node> optional = this.nodes.parallelStream()
////				.filter(node ->
////						//from reference (n.r == r)
////						node.hasReference() ?
////						Objects.equals(node.reference(), reference) :
////						//from sketch (n.s.r == r)
////						node.hasSketch() ?
////						Objects.equals(node.getSketch().reference(), reference) :
////						//from element (n.e.r == r)
////						node.hasElement() ?
////						Objects.equals(node.getElement().reference(), reference) :
////						//from document (n.d == r.d && r.dr == r)
////						node.hasDocument() &&
////						Objects.equals(node.document(), reference.document()) &&
////						Objects.equals(reference.documentReference(), reference)
////				)
////				.findAny();
////
////		if (optional.isPresent()) {
////			Node node = optional.get();
////
////			if (!node.hasReference() && node.mutableReference())
////				node.setReference(reference);
////		} else
////			this.nodes.add(new FlexNode(reference));
//	}
////
////	@Override
////	public void append(Sketch sketch) {
////		Objects.requireNonNull(sketch, "sketch");
////		//1. node.sketch ? node.sketch == sketch
////		//2. !node.element && node.reference ? node.reference == sketch.reference
////		//3. !node.element && node.document ? reference(node.document) == sketch.reference
////		Optional<Node> optional = this.nodes.parallelStream()
////				.filter(node ->
////						node.hasSketch() ?
////						Objects.equals(node.getSketch(), sketch) :
////						!node.hasElement() &&
////						(
////								//from reference (n.r == s.r)
////								node.hasReference() ?
////								Objects.equals(node.reference(), sketch.reference()) :
////								//from document (n.d == s.r.d && s.r.dr == s.r)
////								node.hasDocument() &&
////								Objects.equals(node.document(), sketch.reference().document()) &&
////								Objects.equals(sketch.reference().documentReference(), sketch.reference())
////						)
////				)
////				.findAny();
////
////		if (optional.isPresent()) {
////			Node node = optional.get();
////
////			if (!node.hasSketch() && node.mutableSketch())
////				node.setSketch(sketch);
////		} else
////			this.nodes.add(new FlexNode(sketch));
////	}
//
////	@Override
////	public void append(Element element) {
//////		Objects.requireNonNull(element, "element");
//////		//1. node.element ? node.element == element
//////		//2. !node.sketch && node.reference ? node.reference == element.reference
//////		//3. !node.sketch && node.document ? reference(node.document) == element.reference
//////		Optional<Node> optional = this.nodes.parallelStream()
//////				.filter(node ->
//////						node.hasElement() ?
//////						Objects.equals(node.getElement(), element) :
//////						element.reference() != null &&
//////						!node.hasSketch() &&
//////						(
//////								//from reference (n.r == e.r)
//////								node.hasReference() ?
//////								Objects.equals(node.reference(), element.reference()) :
//////								node.hasDocument() &&
//////								//from document (n.d == e.r.d && e.r.dr == e.r)
//////								Objects.equals(node.document(), element.reference().document()) &&
//////								Objects.equals(element.reference().documentReference(), element.reference())
//////						)
//////				)
//////				.findAny();
//////
//////		if (optional.isPresent()) {
//////			Node node = optional.get();
//////
//////			if (!node.hasElement() && node.mutableElement())
//////				node.setElement(element);
//////		} else
//////			this.nodes.add(new FlexNode(element));
////	}
//	//
//	//	@Override
//	//	public void append(Instruction instruction) {
//	//		Objects.requireNonNull(instruction, "instruction");
//	//		//direct `.equals(...)`
//	//		this.instructions.add(instruction);
//	//	}
//
//	@Override
//	public void use(Compiler compiler) {
//		Objects.requireNonNull(compiler, "linker");
//		//direct `.equals(...)`
//		this.compilers.add(compiler);
//	}
//
//	@Override
//	public void use(Parser parser) {
//		Objects.requireNonNull(parser, "parser");
//		//direct `.equals(...)`
//		this.parsers.add(parser);
//	}
//
//	@Override
//	public void use(Linker linker) {
//		Objects.requireNonNull(linker, "linker");
//		//direct `.equals(...)`
//		this.linkers.add(linker);
//	}
//}
////	/**
////	 * A set of documents {@link #append(Document) appended} to this compilation.
////	 *
////	 * @since 0.2.0 ~2021.02.08
////	 */
////	protected final Set<Document> documents = new HashSet<>();
////	protected final Set<Element> elements = new HashSet<>();
////	/**
////	 * The environment this compilation is building.
////	 *
////	 * @since 0.2.0 ~2021.02.12
////	 */
////	@Deprecated
////	protected final Environment environment;
//
////	protected final Set<Node> nodesX = new HashSet<>();
//
////	/**
////	 * A set of references {@link #append(Reference) appended} to this compilation.
////	 *
////	 * @since 0.2.0 ~2021.02.09
////	 */
////	protected final Set<Reference> references = new HashSet<>();
////	/**
////	 * A set of references {@link #append(Sketch) appended} to this compilation.
////	 *
////	 * @since 0.2.0 ~2021.02.09
////	 */
////	protected final Set<Sketch> sketches = new HashSet<>();
////
////	/**
////	 * Construct a new compilation that builds the given {@code environment}.
////	 *
////	 * @param environment the environment the constructed compilation will be building.
////	 * @throws NullPointerException if the given {@code environment} is null.
////	 * @since 0.2.0 ~2021.02.12
////	 */
////	@Deprecated
////	protected AbstractCompilation(Environment environment) {
////		Objects.requireNonNull(environment, "environment");
////		this.environment = environment;
////	}
////
////	@Override
////	public void adapt(Compilation compilation) {
////		Objects.requireNonNull(compilation, "compilation");
////		//direct `.equals(...)`
////		this.compilations.add(compilation);
////	}
//
