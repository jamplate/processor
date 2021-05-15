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
//import java.util.HashSet;
//
///**
// * The default compilation implementation.
// *
// * @author LSafer
// * @version 0.2.0
// * @since 0.2.0 ~2021.01.31
// */
//public class DynamicCompilation extends AbstractCompilation {
//	//	protected final Set<Document> newDocuments = new HashSet<>();
//	//	protected final Set<Reference> newReferences = new HashSet<>();
//	//
//	//	/**
//	//	 * Construct a new compilation that builds the given {@code environment}.
//	//	 *
//	//	 * @param environment the environment the constructed compilation will be building.
//	//	 * @throws NullPointerException if the given {@code environment} is null.
//	//	 * @since 0.2.0 ~2021.02.12
//	//	 */
//	//	@Deprecated
//	//	protected DynamicCompilation(Environment environment) {
//	//		super(environment);
//	//	}
//
//	@Override
//	public void compile() {
//		//until all compilers are done
//		while (
//			//yeah, I am autoboxing booleans; what do you gonna do?
//			//java sucks when it comes to primitives :<
//			//we are solving this issue right now with this project
//				new HashSet<>(this.nodes).stream()
//						.flatMap(node ->
//								this.compilers.stream()
//										.map(compiler ->
//												compiler.compile(this, node)
//										)
//						)
//						.reduce((a, b) -> a || b)
//						.orElse(false)
//		)
//			;
//	}
//
//	@SuppressWarnings("OverlyComplexMethod")
//	@Override
//	public void concrete() {
//		//		//1. !node.sketch && !node.element && node.reference ? sketch(node.reference)
//		//		//2. !node.sketch && !node.element && node.document ? sketch(node.document)
//		//		this.nodes.parallelStream()
//		//				.filter(node -> !node.hasSketch() && node.mutableSketch())
//		//				.filter(node -> !node.hasElement())
//		//				.forEach(node -> {
//		//					if (node.hasReference())
//		//						//from reference
//		//						node.setSketch(new ReferenceSketch(node.reference()));
//		//					else if (node.hasDocument())
//		//						//from document
//		//						node.setSketch(new DocumentSketch(node.document()));
//		//				});
//		//		//1. !node.reference && node.sketch ? node.sketch.reference
//		//		//2. !node.reference && node.element ? node.element.reference
//		//		//3. !node.reference && node.document ? reference(node.document)
//		//		this.nodes.parallelStream()
//		//				.filter(node -> !node.hasReference() && node.mutableReference())
//		//				.forEach(node -> {
//		//					if (node.hasSketch())
//		//						//from sketch
//		//						node.setReference(node.getSketch().reference());
//		//					else if (node.hasElement()) {
//		//						if (node.getElement().reference() != null)
//		//							//from element
//		//							node.setReference(node.getElement().reference());
//		//					} else if (node.hasDocument())
//		//						//from document
//		//						node.setReference(new DocumentReference(node.document()));
//		//				});
//		//		//1. !node.document && node.reference ? node.reference.document
//		//		//2. !node.document && node.sketch ? node.reference.document
//		//		//3. !node.document && node.element.reference ? node.element.reference.document
//		//		this.nodes.parallelStream()
//		//				.filter(node -> !node.hasDocument() && node.mutableDocument())
//		//				.forEach(node -> {
//		//					if (node.hasReference())
//		//						//from reference
//		//						node.setDocument(node.reference().document());
//		//					else if (node.hasSketch())
//		//						//from sketch
//		//						node.setDocument(node.getSketch().reference().document());
//		//					else if (node.hasElement() && node.getElement().reference() != null)
//		//						//from element
//		//						node.setDocument(node.getElement().reference().document());
//		//				});
//	}
//
//	@Override
//	public void link() {
//		//		Set<Link> links = this.nodes.parallelStream()
//		//				.flatMap(node ->
//		//						this.linkers.parallelStream()
//		//								.map(linker -> linker.link(node))
//		//				)
//		//				.flatMap(Collection::parallelStream)
//		//				.collect(Collectors.toSet());
//		//
//		//		links.forEach(link ->
//		//				links.stream()
//		//						.filter(Predicate.isEqual(link).negate())
//		//						.forEach(link::evaluate)
//		//		);
//	}
//
//	@Override
//	public void parse() {
//		//		//capture
//		//		Set<Node> nodes = this.nodes.parallelStream()
//		//				.filter(Node::hasSketch)
//		//				.collect(Collectors.toSet());
//		//
//		//		while (!nodes.isEmpty()) {
//		//			Set<Node> next = new HashSet<>();
//		//
//		//			nodes.parallelStream()
//		//					.map(Node::getSketch)
//		//					.forEach(sketch -> {
//		//						while (
//		//								this.parsers.parallelStream()
//		//										.map(parser -> parser.parse(sketch))
//		//										.flatMap(Collection::parallelStream)
//		//										.collect(Collectors.toSet())
//		//										.stream()
//		//										.peek(sketch::put)
//		//										.map(FlexNode::new)
//		//										.peek(next::add)
//		//										.findAny()
//		//										.isPresent()
//		//						)
//		//							;
//		//					});
//		//
//		//			nodes = next;
//		//		}
//	}
//
//	@SuppressWarnings("OverlyLongMethod")
//	@Override
//	public void skeleton() {
//		//		//capture -> clean -> collect -> link
//		//		Set<Node> nodes = this.nodes.parallelStream()
//		//				.filter(Node::hasReference)
//		//				.collect(Collectors.toSet());
//		//		//noinspection OverlyLongLambda
//		//		nodes.forEach(node -> {
//		//			if (node.hasParent()) {
//		//				if (node.getParent().getChild() == node)
//		//					node.getParent().removeChild();
//		//
//		//				node.removeParent();
//		//			}
//		//			if (node.hasPrevious()) {
//		//				if (node.getPrevious().getNext() == node)
//		//					node.getPrevious().removeNext();
//		//
//		//				node.removePrevious();
//		//			}
//		//			if (node.hasNext()) {
//		//				if (node.getNext().getPrevious() == node)
//		//					node.getNext().removePrevious();
//		//
//		//				node.removeNext();
//		//			}
//		//			if (node.hasChild()) {
//		//				if (node.getChild().getParent() == node)
//		//					node.getChild().removeParent();
//		//
//		//				node.removeChild();
//		//			}
//		//		});
//		//		//noinspection OverlyLongLambda
//		//		nodes.parallelStream()
//		//				//collect {key: parent, value: [children...]}
//		//				.collect(Collectors.toMap(
//		//						node ->
//		//								nodes.parallelStream()
//		//										.filter(Predicate.isEqual(node).negate())
//		//										.filter(n ->
//		//												Dominance.compute(node, n) ==
//		//												Dominance.CONTAIN
//		//										)
//		//										.max(Node.COMPARATOR)
//		//										.orElse(null),
//		//						node -> new HashSet<>(Collections.singleton(node)),
//		//						(a, b) -> {
//		//							a.addAll(b);
//		//							return a;
//		//						}
//		//				))
//		//				.entrySet()
//		//				.parallelStream()
//		//				//filter children without parents and parents without children
//		//				.filter(entry -> entry.getKey() != null && !entry.getValue().isEmpty())
//		//				.forEach(entry -> {
//		//					Node parent = entry.getKey();
//		//					Set<Node> children = entry.getValue();
//		//
//		//					Iterator<Node> iterator = children.stream()
//		//							.sorted(Node.COMPARATOR)
//		//							.iterator();
//		//
//		//					Node node = iterator.next();
//		//
//		//					//link the parent to the first node
//		//					node.setParent(parent);
//		//					//link the first node to its parent
//		//					parent.setChild(node);
//		//
//		//					while (iterator.hasNext()) {
//		//						Node next = iterator.next();
//		//
//		//						//link the parent to the next node
//		//						next.setParent(parent);
//		//						//link the previous node to the next node
//		//						next.setPrevious(node);
//		//						//link the next node to the previous node
//		//						node.setNext(next);
//		//
//		//						node = next;
//		//					}
//		//				});
//	}
//}
////old skeleton()
////----------------------------------------------------------------------------------------
////		//skeleton first-level and capture second-level
////		Map<Node, List<Sketch>> sketches = this.sketches.parallelStream()
////				.collect(Collectors.toMap(
////						SketchNode::new,
////						sketch ->
////								StreamSupport.stream(sketch.spliterator(), false)
////										.sorted(Sketch.COMPARATOR)
////										.collect(Collectors.toList())
////				));
////
////		//until the last level is over
////		while (!sketches.isEmpty()) {
////			//next level container
////			Map<Node, List<Sketch>> next = new HashMap<>();
////
////			//noinspection OverlyLongLambda
////			sketches.entrySet()
////					.parallelStream()
////					.filter(entry -> !entry.getValue().isEmpty())
////					.forEach(entry -> {
////						Node parent = entry.getKey();
////						List<Sketch> children = entry.getValue();
////
////						Iterator<Sketch> iterator = children.iterator();
////
////						Sketch sketch0 = iterator.next();
////						Node nodeP = new SketchNode(sketch0);
////
////						next.put(
////								nodeP,
////								StreamSupport.stream(sketch0.spliterator(), false)
////										.sorted(Sketch.COMPARATOR)
////										.collect(Collectors.toList())
////						);
////
////						nodeP.setParent(parent);
////						parent.setChild(nodeP);
////
////						while (iterator.hasNext()) {
////							Sketch sketchN = iterator.next();
////							Node nodeN = new SketchNode(sketchN);
////
////							next.put(
////									nodeN,
////									StreamSupport.stream(sketchN.spliterator(), false)
////											.sorted(Sketch.COMPARATOR)
////											.collect(Collectors.toList())
////							);
////
////							nodeN.setParent(parent);
////							nodeN.setPrevious(nodeP);
////							nodeP.setNext(nodeN);
////
////							nodeP = nodeN;
////						}
////					});
////
////			//swap levels
////			sketches = next;
////		}
////
////		//swap at last
////		this.nodesX.clear();
////		this.nodesX.addAll(sketches.keySet());
////----------------------------------------------------------------------------------------
////old parse();
////----------------------------------------------------------------------------------------
////		//capture
////		Set<Sketch> sketches = new HashSet<>(this.sketches);
////
////		//check -> parse -> collect -> repeat
////		while (!sketches.isEmpty()) {
////			Set<Sketch> next = new HashSet<>();
////
////			sketches.parallelStream()
////					.forEach(sketch -> {
////						//parse -> flatten -> add-tail -> put-tree -> check -> repeat
////						while (
////								this.parsers.parallelStream()
////										.map(parser -> parser.parse(sketch))
////										.flatMap(Collection::parallelStream)
////										.peek(next::add)
////										.collect(Collectors.toSet())
////										.stream()
////										.peek(sketch::put)
////										.count() > 0L
////						)
////							;
////					});
////
////			sketches = next;
////		}
////----------------------------------------------------------------------------------------
