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
//package org.jamplate.model.node;
//
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//
//import java.util.Objects;
//
///**
// * An abstraction for the interface {@link Node}. Implementing the relations methods.
// *
// * @param <T> the tpe of the value of the node.
// * @author LSafer
// * @version 0.2.0
// * @since 0.2.0 ~2021.02.14
// */
//public abstract class AbstractNode<T> implements Node<T> {
//	/**
//	 * The node above this node.
//	 *
//	 * @since 0.2.0 ~2021.02.14
//	 */
//	@Nullable
//	protected Node<T> above;
//	/**
//	 * The node below this node.
//	 *
//	 * @since 0.2.0 ~2021.02.14
//	 */
//	@Nullable
//	protected Node<T> below;
//	/**
//	 * The next node of this node.
//	 *
//	 * @since 0.2.0 ~2021.02.14
//	 */
//	@Nullable
//	protected Node<T> next;
//	/**
//	 * The previous node of this node.
//	 *
//	 * @since 0.2.0 ~2021.02.14
//	 */
//	@Nullable
//	protected Node<T> previous;
//
//	@Override
//	public Node getAbove() {
//		return this.above;
//	}
//
//	@Override
//	public Node getBelow() {
//		return this.below;
//	}
//
//	@Override
//	public Node getNext() {
//		return this.next;
//	}
//
//	@Override
//	public Node getPrevious() {
//		return this.previous;
//	}
//
//	@Override
//	public void removeAbove() {
//		Node<T> above = this.above;
//
//		if (above != null) {
//			this.above = null;
//
//			if (above.getBelow() == this)
//				above.removeAbove();
//		}
//	}
//
//	@Override
//	public void removeBelow() {
//		Node<T> below = this.below;
//
//		if (below != null) {
//			this.below = null;
//
//			if (below.getAbove() == this)
//				below.removeAbove();
//		}
//	}
//
//	@Override
//	public void removeNext() {
//		Node<T> next = this.next;
//
//		if (next != null) {
//			this.next = null;
//
//			if (next.getPrevious() == this)
//				next.removePrevious();
//		}
//	}
//
//	@Override
//	public void removePrevious() {
//		Node<T> previous = this.previous;
//
//		if (previous != null) {
//			this.previous = null;
//
//			if (previous.getNext() == this)
//				previous.removeNext();
//		}
//	}
//
//	@Override
//	public void setAbove(@NotNull Node node) {
//		Objects.requireNonNull(node, "node");
//		Node<T> above = this.above;
//		Node<T> below = node.getBelow();
//
//		if (below != null && below != this)
//			throw new IllegalStateException("above");
//
//		this.above = node;
//
//		if (above != null && above.getBelow() == this)
//			above.removeBelow();
//
//		if (below == null)
//			node.setBelow(this);
//	}
//
//	@Override
//	public void setBelow(@NotNull Node node) {
//		Objects.requireNonNull(node, "node");
//		Node<T> below = this.below;
//		Node<T> above = node.getAbove();
//
//		if (above != null && above != this)
//			throw new IllegalStateException("below");
//
//		if (below != null && below.getAbove() == this)
//			below.removeAbove();
//
//		this.below = node;
//
//		if (above == null)
//			node.setAbove(this);
//	}
//
//	@Override
//	public void setNext(@NotNull Node node) {
//		Objects.requireNonNull(node, "node");
//		Node<T> next = this.next;
//		Node<T> previous = node.getPrevious();
//
//		if (previous != null && previous != this)
//			throw new IllegalStateException("next");
//
//		if (next != null && next.getPrevious() == this)
//			next.removePrevious();
//
//		this.next = node;
//
//		if (previous == null)
//			node.setPrevious(this);
//	}
//
//	@Override
//	public void setPrevious(@NotNull Node node) {
//		Objects.requireNonNull(node, "node");
//		Node<T> previous = this.previous;
//		Node<T> next = node.getNext();
//
//		if (next != null && next != this)
//			throw new IllegalStateException("previous");
//
//		if (previous != null && previous.getNext() == this)
//			previous.removeNext();
//
//		this.previous = node;
//
//		if (next == null)
//			node.setNext(this);
//	}
//}
////
////	@Override
////	public <T> T compute(String name, Class<T> type, Supplier<T> supplier) {
////		Objects.requireNonNull(name, "name");
////		Objects.requireNonNull(type, "type");
////		Objects.requireNonNull(supplier, "supplier");
////		return (T) this.values.computeIfAbsent(
////				name,
////				k -> new HashMap<>()
////		).computeIfAbsent(
////				type,
////				k -> type.cast(supplier.get())
////		);
////	}
////
////	@Override
////	public Document document() {
////		return this.document;
////	}
////
////	@Override
////	public <T> void put(String name, Class<T> type, T value) {
////		Objects.requireNonNull(name, "name");
////		Objects.requireNonNull(type, "type");
////		Objects.requireNonNull(value, "value");
////		this.values.computeIfAbsent(
////				name,
////				k -> new HashMap<>()
////		).put(
////				type,
////				type.cast(value)
////		);
////	}
////
////	@Override
////	public Reference reference() {
////		return this.reference;
////	}
////
////	@Override
////	public Element getElement() {
////		return this.element;
////	}
////
////	@Override
////	public Sketch getSketch() {
////		return this.sketch;
////	}
////
////	@Override
////	public boolean hasElement() {
////		return this.element != null;
////	}
////
////	@Override
////	public boolean hasSketch() {
////		return this.sketch != null;
////	}
////
////	@Override
////	public boolean mutableChild() {
////		return true;
////	}
////
////	@Override
////	public boolean mutableDocument() {
////		return this.document == null;
////	}
////
////	@Override
////	public boolean mutableElement() {
////		return this.element == null;
////	}
////
////	@Override
////	public boolean mutableNext() {
////		return true;
////	}
////
////	@Override
////	public boolean mutableParent() {
////		return true;
////	}
////
////	@Override
////	public boolean mutablePrevious() {
////		return true;
////	}
////
////	@Override
////	public boolean mutableReference() {
////		return this.reference == null;
////	}
////
////	@Override
////	public boolean mutableSketch() {
////		return this.sketch == null;
////	}
////
////	@Override
////	public void removeElement() {
////		if (this.mutableElement())
////			this.element = null;
////		else
////			throw new UnsupportedOperationException("immutable");
////	}
////
////	@Override
////	public void removeSketch() {
////		if (this.mutableSketch())
////			this.sketch = null;
////		else
////			throw new UnsupportedOperationException("immutable");
////	}
////
////	@Override
////	public void setSketch(Sketch sketch) {
////		Objects.requireNonNull(sketch, "sketch");
////		if (this.mutableSketch())
////			this.sketch = sketch;
////		else
////			throw new UnsupportedOperationException("immutable");
////	}
////
////	@Override
////	public void setElement(Element element) {
////		Objects.requireNonNull(element, "element");
////		if (this.mutableElement())
////			this.element = element;
////		else
////			throw new UnsupportedOperationException("immutable");
////	}
////	/**
////	 * The sketch of this node.
////	 *
////	 * @since 0.2.0 ~2021.02.14
////	 */
////	protected Sketch sketch;
////	/**
////	 * The element of this node.
////	 *
////	 * @since 0.2.0 ~2021.02.14
////	 */
////	protected Element element;
////
////	public FlexNode(Sketch sketch) {
////		Objects.requireNonNull(sketch, "sketch");
////		this.sketch = sketch;
////	}
////
////	public FlexNode(Element element) {
////		Objects.requireNonNull(element, "element");
////		this.element = element;
////	}
//
////
////	@Override
////	public void setReference(Reference reference) {
////		Objects.requireNonNull(reference, "reference");
////		this.reference = reference;
////	}
////
////	@Override
////	public boolean hasDocument() {
////		return this.document != null;
////	}
//
////
////	@Override
////	public void setDocument(Document document) {
////		Objects.requireNonNull(document, "document");
////		//		if (this.mutableDocument())
////		this.document = document;
////		//		else
////		//			throw new UnsupportedOperationException("immutable");
////	}
////
////	@Override
////	public void removeReference() {
////		//		if (this.mutableReference())
////		this.reference = null;
////		//		else
////		//			throw new UnsupportedOperationException("immutable");
////	}
////
////	@Override
////	public void removeDocument() {
////		//		if (this.mutableDocument())
////		this.document = null;
////		//		else
////		//			throw new UnsupportedOperationException("immutable");
////	}
////
////	@Override
////	public boolean hasReference() {
////		return this.reference != null;
////	}
//
////	public FlexNode(Reference reference) {
////		Objects.requireNonNull(reference, "reference");
////		this.reference = reference;
////	}
//
////	/**
////	 * The document of this node.
////	 *
////	 * @since 0.2.0 ~2021.02.14
////	 */
////	protected final Document document;
////	/**
////	 * The reference of this node.
////	 *
////	 * @since 0.2.0 ~2021.02.14
////	 */
////	protected final Reference reference;
////	protected final Map<String, Map<Class, Object>> values = new HashMap<>();
////
////	/**
////	 * Construct a new node with default reference (0, 0) and default document (pseudo).
////	 *
////	 * @since 0.2.0 ~2021.02.19
////	 */
////	public FlexNode() {
////		this.reference = new Reference();
////		this.document = new PseudoDocument();
////	}
////
////	/**
////	 * Construct a new node with the given {@code reference} and default document
////	 * (pseudo).
////	 *
////	 * @param reference the reference of the constructed node.
////	 * @throws NullPointerException if the given {@code reference} is null.
////	 * @since 0.2.0 ~2021.02.19
////	 */
////	public FlexNode(Reference reference) {
////		Objects.requireNonNull(reference, "reference");
////		this.document = new PseudoDocument();
////		this.reference = new Reference();
////	}
////
////	/**
////	 * Construct a new node with default reference (0, 0) and the given {@code document}.
////	 *
////	 * @param document the document of the constructed node.
////	 * @throws NullPointerException if the given {@code document} is null.
////	 * @since 0.2.0 ~2021.02.19
////	 */
////	public FlexNode(Document document) {
////		Objects.requireNonNull(document, "document");
////		this.document = document;
////		this.reference = new Reference();
////	}
////
////	/**
////	 * Construct a new flexible node with the given {@code document} and {@code
////	 * reference}.
////	 *
////	 * @param document  the document this node is from.
////	 * @param reference the reference where this node is located at its document.
////	 * @throws NullPointerException if the given {@code document} or {@code reference} is
////	 *                              null.
////	 * @since 0.2.0 ~2021.02.19
////	 */
////	public FlexNode(Document document, Reference reference) {
////		Objects.requireNonNull(document, "document");
////		this.document = document;
////		this.reference = reference;
////	}
////
////	@Override
////	public boolean hasAbove() {
////		return this.above != null;
////	}
////
////	@Override
////	public boolean hasBelow() {
////		return this.below != null;
////	}
////
////	@Override
////	public boolean hasNext() {
////		return this.next != null;
////	}
////
////	@Override
////	public boolean hasPrevious() {
////		return this.previous != null;
////	}
