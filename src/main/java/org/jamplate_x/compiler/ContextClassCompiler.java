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
//import org.jamplate.model.element.Element;
//import org.jamplate.model.node.Node;
//import org.jamplate.model.sketch.Sketch;
//
//import java.util.*;
//import java.util.function.BiPredicate;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//
///**
// * A compiler that searches for sketches that are instances of a specific class.
// *
// * @author LSafer
// * @version 0.2.0
// * @since 0.2.0 ~2021.02.10
// */
//public class ContextClassCompiler implements Compiler {
//	/**
//	 * The constructor used by this linker to construct a new element.
//	 *
//	 * @since 0.2.0 ~2021.02.10
//	 */
//	protected final Function<Sketch, Element> constructor;
//	protected final List<BiPredicate<Element, Node>> setters;
//	protected final Class<? extends Sketch> target;
//
//	public ContextClassCompiler(Class<? extends Sketch> target, Function<Sketch, Element> constructor, BiPredicate<Element, Node>... setters) {
//		Objects.requireNonNull(target, "target");
//		Objects.requireNonNull(constructor, "constructor");
//		Objects.requireNonNull(setters, "setters");
//		this.target = target;
//		this.constructor = constructor;
//		this.setters = Arrays.stream(setters)
//				.filter(Objects::nonNull)
//				.collect(Collectors.toList());
//	}
//
//	//	@Override
//	//	public Set<Sketch> compile(Environment environment, Sketch root, Sketch sketch) {
//	//		Objects.requireNonNull(environment, "environment");
//	//		Objects.requireNonNull(root, "root");
//	//		Objects.requireNonNull(sketch, "sketch");
//	//
//	//		if (this.target.isInstance(sketch)) {
//	//			Element element = this.constructor.apply(sketch);
//	//
//	//			if (element != null) {
//	//				environment.put(sketch, element);
//	//				return Collections.singleton(sketch);
//	//			}
//	//		}
//	//
//	//		return Collections.emptySet();
//	//	}
//
//	@Override
//	public Set<Node> compile(Node node) {
//		Objects.requireNonNull(node, "node");
//
//		Sketch sketch = node.getSketch();
//
//		if (this.target.isInstance(node.getSketch())) {
//			if (node.getElement() == null) {
//				Element element = this.constructor.apply(sketch);
//
//				node.setElement(element);
//
//			} else {
//				Element element = node.getElement();
//
//				for (
//						Node child = node.getChild();
//						child != null;
//						child = child.getNext()
//				) {
//					Element childElement = child.getElement();
//
//					this.setters.stream()
//							.filter(setter -> setter.test(element, childElement))
//					return true;
//				}
//			}
//		}
//
//		return Collections.emptySet();
//	}
//}
