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
//import org.jetbrains.annotations.Contract;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//
//import java.util.*;
//import java.util.stream.Stream;
//
///**
// * @author LSafer
// * @version 0.2.0
// * @since 0.2.0 ~2021.02.17
// */
//public final class Nodes {
//	/**
//	 * Tool classes does not need instantiating.
//	 *
//	 * @throws AssertionError when called.
//	 * @since 0.2.0 ~2021.02.20
//	 */
//	private Nodes() {
//		throw new AssertionError("No instance for you!");
//	}
//
//	/**
//	 * Returns a modifiable view of the children of the given {@code node}. The view is
//	 * treating the children of the given node as a list. The locate the more {@link
//	 * Node#getPrevious() previous} children as the start and the more {@link
//	 * Node#getNext()} children at the end. Modifications to the children are made by the
//	 * utilities of this class.
//	 *
//	 * @param node the node to get a list view of its children.
//	 * @return a modifiable list view of the children of the given {@code node}.
//	 * @throws NullPointerException if the given {@code node} is null.
//	 * @since 0.2.0 ~2021.02.19
//	 */
//	public static <T> List<Node<T>> asList(Node<T> node) {
//		Objects.requireNonNull(node, "node");
//		//noinspection ALL
//		return new AbstractList<Node<T>>() {
//			@Override
//			@Contract(mutates = "param")
//			public boolean add(@NotNull Node<T> element) {
//				Objects.requireNonNull(element, "element");
//				Node<T> below = node.getBelow();
//
//				if (below == null)
//					node.setBelow(element);
//				else
//					Node.getTail(below)
//							.setNext(element);
//
//				return true;
//			}
//
//			@Override
//			@Contract(mutates = "param2")
//			public void add(int index, @NotNull Node<T> element) {
//				Objects.requireNonNull(element, "element");
//				if (index < 0)
//					throw new IndexOutOfBoundsException("index < 0");
//				Node<T> below = node.getBelow();
//
//				if (below == null)
//					if (index == 0)
//						node.setBelow(element);
//					else
//						throw new IndexOutOfBoundsException(index + " > 0");
//				else
//
//			}
//
//			@Override
//			public Node get(int index) {
//				if (index < 0)
//					throw new IndexOutOfBoundsException("index < 0");
//				return this.stream()
//						.skip(index)
//						.findFirst()
//						.orElseThrow(() ->
//								new IndexOutOfBoundsException("index >= size()")
//						);
//			}
//
//			@Override
//			public boolean isEmpty() {
//				return !node.hasBelow();
//			}
//
//			@Override
//			public Iterator<Node> iterator() {
//				//noinspection ALL
//				return new Iterator<Node>() {
//					/**
//					 * The current/last child iterated.
//					 *
//					 * @since 0.2.0 ~2021.02.17
//					 */
//					private Node child = node.getBelow();
//					/**
//					 * The last node returned from the method {@link #next()}.
//					 *
//					 * @since 0.2.0 ~2021.02.19
//					 */
//					private Node lastReturn;
//
//					@Override
//					public boolean hasNext() {
//						return this.child != null;
//					}
//
//					@Override
//					public Node next() {
//						if (this.child == null)
//							throw new NoSuchElementException("No more children!");
//
//						Node child = this.child;
//						this.child = child.getNext();
//						this.lastReturn = child;
//						return child;
//					}
//
//					@Override
//					public void remove() {
//						if (this.lastReturn == null)
//							throw new IllegalStateException("remove");
//
//						Node last = this.lastReturn;
//						this.lastReturn = null;
//						Nodes.remove(last);
//					}
//				};
//			}
//
//			@Override
//			public ListIterator<Node> listIterator(int index) {
//				if (index < 0)
//					throw new IndexOutOfBoundsException("index < 0");
//				//noinspection ALL
//				return new ListIterator<Node>() {
//					/**
//					 * The last returned node from either {@link #next()} or {@link #previous()}.
//					 *
//					 * @since 0.2.0 ~2021.02.19
//					 */
//					private Node lastReturn;
//					/**
//					 * The next node to be returned with the next call to {@link #next()}.
//					 *
//					 * @since 0.2.0 ~2021.02.19
//					 */
//					private Node next;
//					/**
//					 * The previous node to be returned with the next call to {@link #previous()}.
//					 *
//					 * @since 0.2.0 ~2021.02.19
//					 */
//					private Node previous;
//
//					{
//						if (node.hasBelow()) {
//							//noinspection ALL
//							Node child = stream()
//									.skip(index)
//									.findFirst()
//									.orElse(null);
//
//							if (child == null) {
//								if (index != 0)
//									throw new IndexOutOfBoundsException("index > size()");
//							} else {
//								this.next = child;
//								this.previous = child.getPrevious();
//							}
//						}
//					}
//
//					@Override
//					public void add(Node element) {
//						Objects.requireNonNull(element, "element");
//
//						Node next = this.next;
//						Node previous = this.previous;
//
//						if (next != null)
//							Nodes.placePrevious(next, element);
//						else if (previous != null)
//							Nodes.placeNext(previous, element);
//						else
//							Nodes.placeChild(node, element);
//
//						this.lastReturn = null;
//						this.previous = element;
//					}
//
//					@Override
//					public boolean hasNext() {
//						return this.next != null;
//					}
//
//					@Override
//					public boolean hasPrevious() {
//						return this.previous != null;
//					}
//
//					@Override
//					public Node next() {
//						Node next = this.next;
//
//						if (next == null)
//							throw new NoSuchElementException("End reached");
//
//						this.next = next.getNext();
//						this.previous = next;
//						this.lastReturn = next;
//						return next;
//					}
//
//					@Override
//					public int nextIndex() {
//						Node next = this.next;
//						int i = 0;
//						while ((next = next.getPrevious()) != null)
//							i++;
//						return i;
//					}
//
//					@Override
//					public Node previous() {
//						Node previous = this.previous;
//
//						if (previous == null)
//							throw new NoSuchElementException("Start reached");
//
//						this.previous = previous.getPrevious();
//						this.next = previous;
//						this.lastReturn = previous;
//						return previous;
//					}
//
//					@Override
//					public int previousIndex() {
//						Node previous = this.previous;
//						int i = 0;
//						while ((previous = previous.getPrevious()) != null)
//							i++;
//						return i;
//					}
//
//					@Override
//					public void remove() {
//						Node lastReturn = this.lastReturn;
//						Node previous = this.previous;
//						Node next = this.next;
//
//						if (lastReturn == null)
//							throw new IllegalStateException("remove");
//
//						Nodes.remove(lastReturn);
//
//						this.lastReturn = null;
//						if (next == lastReturn)
//							//the next is removed
//							if (previous != null)
//								//"Onii-Chan, what is my Ototo?" next asking
//								this.next = previous.getNext();
//							else
//								//"Otousan, what is my Ototo?" next asking
//								this.next = node.getBelow();
//						if (previous == lastReturn)
//							//the previous is removed
//							if (next != null)
//								//"Ototo, what is my Onii-Chan?" previous asking
//								this.previous = next.getPrevious();
//							else
//								//"I got no Onii-Chan" previous saying
//								this.previous = null;
//					}
//
//					@Override
//					public void set(Node element) {
//						Objects.requireNonNull(element, "element");
//
//						Node lastReturn = this.lastReturn;
//						Node previous = this.previous;
//						Node next = this.next;
//
//						if (lastReturn == null)
//							throw new IllegalStateException("set");
//
//						Nodes.replace(lastReturn, element);
//
//						this.lastReturn = null;
//						if (previous == lastReturn)
//							this.previous = element;
//						if (next == lastReturn)
//							this.next = element;
//					}
//				};
//			}
//
//			@Override
//			public Node remove(int index) {
//				if (index < 0)
//					throw new IndexOutOfBoundsException("index < 0");
//				Node child = this.stream()
//						.skip(index)
//						.findFirst()
//						.orElseThrow(() ->
//								new IndexOutOfBoundsException("index >= size()")
//						);
//				Nodes.remove(child);
//				return child;
//			}
//
//			@Override
//			public Node set(int index, Node element) {
//				Objects.requireNonNull(element, "element");
//				if (index < 0)
//					throw new IndexOutOfBoundsException("index < 0");
//				Node child = this.stream()
//						.skip(index)
//						.findFirst()
//						.orElseThrow(() ->
//								new IndexOutOfBoundsException("index >= size()")
//						);
//				Nodes.replace(child, element);
//				return child;
//			}
//
//			@Override
//			public int size() {
//				return this.stream()
//						.mapToInt(n -> 1)
//						.sum();
//			}
//
//			@Override
//			public Spliterator<Node> spliterator() {
//				return Spliterators.spliteratorUnknownSize(
//						this.iterator(),
//						Spliterator.NONNULL |
//						Spliterator.ORDERED
//				);
//			}
//
//			@Override
//			public Stream<Node> stream() {
//				return StreamSupport.stream(
//						this.spliterator(),
//						false
//				);
//			}
//		};
//	}
//
//	/**
//	 * Inline the children of the given node in the place of it.
//	 * <br>
//	 * If the node has a child. Then, by linking the {@link Node#getHead(Node) head}
//	 * {@link Node#getBelow() child} of it to the {@link Node#getAbove() above} and {@link
//	 * Node#getPrevious() previous} node of it and linking the {@link Node#getTail(Node)
//	 * tail} {@link Node#getBelow() child} of it to the {@link Node#getNext() next} node
//	 * of it.
//	 * <br>
//	 * If the node does not has a {@link Node#getBelow() child} but has a {@link
//	 * Node#getNext()}. Then, by linking the {@link Node#getNext() next} node of it to the
//	 * {@link Node#getAbove() above} and {@link Node#getPrevious() previous} node of it.
//	 * <br>
//	 * It has been chosen to inline from the head to the tail instead of from below to
//	 * tail to not lose the nodes before the below node since it might have some. (even
//	 * though it is highly discouraged to be done. But, it is possible and not illegal)
//	 * <br><br>
//	 * <ol>
//	 *     <li>remove {@code node}</li>
//	 *     <li>
//	 *         if {@code node.below} not equal {@code null}
//	 *         <ol>
//	 *             <li>set above {@code node.below} to {@code node.above}</li>
//	 *             <li>set previous {@code node.below:head} to {@code node.previous}</li>
//	 *             <li>set next {@code node.below:tail} to {@code node.next}</li>
//	 *         </ol>
//	 *     </li>
//	 *     <li>
//	 *         elif {@code node.next} not equal {@code null}
//	 *         <ol>
//	 *             <li>set above {@code node.next} to {@code node.above}</li>
//	 *             <li>set previous {@code node.next} to {@code node.previous}</li>
//	 *         </ol>
//	 *     </li>
//	 * </ol>
//	 *
//	 * @param node the node to inline its children in its place.
//	 * @param <T>  the type of the value of the node.
//	 * @throws NullPointerException          if the given {@code node} is null.
//	 * @throws IllegalStateException         if the given {@code node} is relating to
//	 *                                       another node that is not relating to it.
//	 * @throws UnsupportedOperationException if an operation cannot be done due to the
//	 *                                       necessity of an unsupported operation. (see
//	 *                                       the above operations list)
//	 * @throws IllegalArgumentException      if an operation cannot be done due to a node
//	 *                                       rejecting to relate to a node necessary to
//	 *                                       relate to due to some aspect of it. (see the
//	 *                                       above operations list)
//	 * @since 0.2.0 ~2021.02.27
//	 */
//	@Contract(mutates = "param")
//	public static <T> void inline(@NotNull Node<T> node) {
//		//
//		//                  above
//		//                  <|>
//		//     previous <-> node <-> next
//		//                  <|>
//		// below:head <...> below <...> below:tail
//		//
//		// --------------------------------------------------------------------
//		//
//		//              node
//		//
//		//              above
//		//              <|>
//		// previous <-> below:head <...> below <...> below:tail <-> next
//		//
//		Objects.requireNonNull(node, "node");
//		Node<T> above = node.getAbove();
//		Node<T> previous = node.getPrevious();
//		Node<T> next = node.getNext();
//		Node<T> below = node.getBelow();
//
//		Node.remove(node);
//
//		if (below != null) {
//			Node<T> belowHead = Node.getHead(below);
//			Node<T> belowTail = Node.getTail(below);
//
//			//above <?|> below
//			if (above != null)
//				below.setAbove(above);
//			//previous <?-> below:head
//			if (previous != null)
//				belowHead.setPrevious(previous);
//			//below:tail <-?> next
//			if (next != null)
//				belowTail.setNext(next);
//		} else if (next != null) {
//			//above <?|> next
//			if (above != null)
//				next.setAbove(above);
//			//previous <?-> next
//			if (previous != null)
//				next.setPrevious(previous);
//		}
//	}
//
//	/**
//	 * Remove the given {@code node} and the nodes below it from the nodes around it.
//	 * Then, link the node after it to the node before and above it.
//	 * <br><br>
//	 * <ol>
//	 *     <li>cut {@code node}</li>
//	 *     <li>set above {@code node.next} to {@code node.above}</li>
//	 *     <li>set previous {@code node.next} to {@code node.previous}</li>
//	 * </ol>
//	 *
//	 * @param node the node to be removed.
//	 * @param <T>  the type of the value of the node.
//	 * @throws NullPointerException          if the given {@code node} is null.
//	 * @throws IllegalStateException         if the given {@code node} is relating to
//	 *                                       another node that is not relating to it.
//	 * @throws UnsupportedOperationException if an operation cannot be done due to the
//	 *                                       necessity of an unsupported operation. (see
//	 *                                       the above operations list)
//	 * @throws IllegalArgumentException      if an operation cannot be done due to a node
//	 *                                       rejecting to relate to a node necessary to
//	 *                                       relate to due to some aspect of it. (see the
//	 *                                       above operations list)
//	 * @since 0.2.0 ~2021.02.27
//	 */
//	@Contract(mutates = "param")
//	public static <T> void pop(@NotNull Node<T> node) {
//		//
//		//              above
//		//              <|>
//		// previous <-> node <-> next
//		//              <|>
//		//              below
//		//
//		// --------------------------------------------------------------------
//		//
//		//              above
//		//              <|>
//		// previous <-> next
//		//
//		//              node
//		//              <|>
//		//              below
//		//
//		Objects.requireNonNull(node, "node");
//		Node<T> above = node.getAbove();
//		Node<T> previous = node.getPrevious();
//		Node<T> next = node.getNext();
//
//		Nodes.removeTree(node);
//
//		if (next != null) {
//			//above <?|> next
//			if (above != null)
//				next.setAbove(above);
//			//previous <?-> next
//			if (previous != null)
//				next.setPrevious(previous);
//		}
//	}
//
//	/**
//	 * Just remove the given {@code node} from any relation except {@code below}. This is
//	 * the tree version of {@link Node#remove(Node)}.
//	 * <br><br>
//	 * Operations order:
//	 * <ol>
//	 *     <li>remove above {@code node}</li>
//	 *     <li>remove previous {@code node}</li>
//	 *     <li>remove next {@code node}</li>
//	 * </ol>
//	 *
//	 * @param node the node to be removed.
//	 * @param <T>  the type of the value of the node.
//	 * @throws NullPointerException          if the given {@code node} is null.
//	 * @throws UnsupportedOperationException if an operation cannot be done due to the
//	 *                                       necessity of an unsupported operation. (see
//	 *                                       the above operations list)
//	 * @since 0.2.0 ~2021.02.27
//	 */
//	@Contract(mutates = "param")
//	public static <T> void removeTree(@NotNull Node<T> node) {
//		//
//		//              above
//		//              <|>
//		// previous <-> node <-> next
//		//              <|>
//		//              below
//		//
//		// --------------------------------------------------------------------
//		//
//		//             above
//		//
//		// previous    node    next
//		//             <|>
//		//             below
//		//
//		Objects.requireNonNull(node, "node");
//		node.removeAbove();
//		node.removePrevious();
//		node.removeNext();
//	}
//
//	/**
//	 * Put the given {@code other} and its children in the place of the given {@code node}
//	 * and its children. This is a tree version of the {@link Node#set(Node, Node)}
//	 * method.
//	 * <br><br>
//	 * Operations order:
//	 * <ol>
//	 *     <li>remove tree {@code node}</li>
//	 *     <li>set above {@code other} to {@code node.above}</li>
//	 *     <li>set previous {@code other} to {@code node.previous}</li>
//	 *     <li>set next {@code other} to {@code node.next}</li>
//	 * </ol>
//	 *
//	 * @param node  the node to be replaced.
//	 * @param other the node to replace the given {@code node}.
//	 * @param <T>   the type of the values of the nodes.
//	 * @throws NullPointerException          if the given {@code node} or {@code other} is
//	 *                                       null.
//	 * @throws IllegalStateException         if the given {@code other} is relating to
//	 *                                       another node that is not the node it suppose
//	 *                                       to have after invoking this method.
//	 * @throws UnsupportedOperationException if an operation cannot be done due to the
//	 *                                       necessity of an unsupported operation. (see
//	 *                                       the above operations list)
//	 * @throws IllegalArgumentException      if an operation cannot be done due to a node
//	 *                                       rejecting to relate to a node necessary to
//	 *                                       relate to due to some aspect of it. (see the
//	 *                                       above operations list)
//	 * @since 0.2.0 ~2021.03.01
//	 */
//	@Contract(mutates = "param1,param2")
//	public static <T> void setTree(@NotNull Node<T> node, @NotNull Node<T> other) {
//		//
//		//              other
//		//              <|>
//		//              other.below
//		//
//		//              above
//		//              <|>
//		// previous <-> node <-> next
//		//              <|>
//		//              below
//		//
//		// --------------------------------------------------------------------
//		//
//		//              node
//		//              <|>
//		//              below
//		//
//		//              above
//		//              <|>
//		// previous <-> other <-> next
//		//              <|>
//		//              other.below
//		//
//		Objects.requireNonNull(node, "node");
//		Objects.requireNonNull(other, "other");
//		Node<T> above = node.getAbove();
//		Node<T> previous = node.getPrevious();
//		Node<T> next = node.getNext();
//
//		Nodes.removeTree(node);
//
//		if (above != null)
//			other.setAbove(above);
//		if (previous != null)
//			other.setPrevious(previous);
//		if (next != null)
//			other.setNext(next);
//	}
//
//	/**
//	 * Mask the given {@code node} with a node that does not allow modifying nothing on
//	 * it.
//	 *
//	 * @param node the node to create an unmodifiable view of it.
//	 * @param <T>  the type of the value of the node.
//	 * @return an unmodifiable view of the given {@code node}.
//	 * @throws NullPointerException if the given {@code node} is null.
//	 * @since 0.2.0 ~2021.03.01
//	 */
//	@NotNull
//	@Contract(value = "_->new", pure = true)
//	public static <T> Node<T> unmodifiableNode(@NotNull Node<T> node) {
//		return new UnmodifiableNode<>(node);
//	}
//
//	public static <T> ListIterator<T> verticalIterator(Node<T> node) {
//		Objects.requireNonNull(node, "node");
//		return new ListIterator<Node<T>>() {
//			@Nullable
//			private Node<T> last;
//			@Nullable
//			private Node<T> next = node;
//			@Nullable
//			private Node<T> previous = node.getAbove();
//
//			@Override
//			public boolean hasNext() {
//				return this.next != null;
//			}
//
//			@Override
//			public boolean hasPrevious() {
//				return this.previous != null;
//			}
//
//			@NotNull
//			@Override
//			public Node<T> next() {
//				Node<T> next = this.next;
//
//				if (next == null)
//					throw new NoSuchElementException("next");
//
//				this.last = next;
//				return next;
//			}
//
//			@Override
//			public int nextIndex() {
//				int i = 0;
//				for (Node<T> n = this.previous; n != null; n = n.getAbove())
//					i++;
//				return i;
//			}
//
//			@NotNull
//			@Override
//			public Node<T> previous() {
//				Node<T> previous = this.previous;
//
//				if (previous == null)
//					throw new NoSuchElementException("previous");
//
//				this.last = previous;
//				return previous;
//			}
//
//			@Override
//			public int previousIndex() {
//				int i = -1;
//				for (Node<T> n = this.previous; n != null; n = n.getAbove())
//					i++;
//				return i;
//			}
//
//			@Override
//			public void remove() {
//				Node<T> last = this.last;
//				Node<T> previous = this.previous;
//				Node<T> next = this.next;
//
//				if (last == null)
//					throw new IllegalStateException("remove");
//
//				this.last = null;
//
//				if (last == next)
//					this.next = last.getBelow();
//				if (last == previous)
//					this.previous = last.getAbove();
//
//				last.removeAbove();
//				last.removeBelow();
//			}
//		};
//	}
//
//	/**
//	 * An unmodifiable view delegating to another node.
//	 *
//	 * @param <T> the type of the value of the node.
//	 * @author LSafer
//	 * @version 0.2.0
//	 * @since 0.2.0 ~2021.03.01
//	 */
//	public static class UnmodifiableNode<T> implements Node<T> {
//		/**
//		 * The node delegating to.
//		 *
//		 * @since 0.2.0 ~2021.03.01
//		 */
//		@NotNull
//		protected final Node<T> node;
//
//		/**
//		 * Construct a new unmodifiable new view for the given {@code node}.
//		 *
//		 * @param node the node the constructed view is for.
//		 * @throws NullPointerException if the given {@code node} is null.
//		 * @since 0.2.0 ~2021.03.01
//		 */
//		public UnmodifiableNode(@NotNull Node<T> node) {
//			Objects.requireNonNull(node, "node");
//			this.node = node;
//		}
//
//		@Override
//		@Nullable
//		public Node<T> getAbove() {
//			Node<T> above = this.node.getAbove();
//			return above == null ?
//				   null :
//				   new UnmodifiableNode<>(above);
//		}
//
//		@Override
//		@Nullable
//		public Node<T> getBelow() {
//			Node<T> below = this.node.getBelow();
//			return below == null ?
//				   null :
//				   new UnmodifiableNode<>(below);
//		}
//
//		@Override
//		@Nullable
//		public Node<T> getNext() {
//			Node<T> next = this.node.getNext();
//			return next == null ?
//				   null :
//				   new UnmodifiableNode<>(next);
//		}
//
//		@Override
//		@Nullable
//		public Node<T> getPrevious() {
//			Node<T> previous = this.node.getPrevious();
//			return previous == null ?
//				   null :
//				   new UnmodifiableNode<>(previous);
//		}
//
//		@Override
//		@Nullable
//		public T getValue() {
//			return this.node.getValue();
//		}
//
//		@Override
//		@Contract("->fail")
//		public void removeAbove() {
//			throw new UnsupportedOperationException("removeAbove");
//		}
//
//		@Override
//		@Contract("->fail")
//		public void removeBelow() {
//			throw new UnsupportedOperationException("removeBelow");
//		}
//
//		@Override
//		@Contract("->fail")
//		public void removeNext() {
//			throw new UnsupportedOperationException("removeNext");
//		}
//
//		@Override
//		@Contract("->fail")
//		public void removePrevious() {
//			throw new UnsupportedOperationException("removePrevious");
//		}
//
//		@Override
//		@Contract("->fail")
//		public void removeValue() {
//			throw new UnsupportedOperationException("removeValue");
//		}
//
//		@Override
//		@Contract("_->fail")
//		public void setAbove(@NotNull Node<T> node) {
//			throw new UnsupportedOperationException("setAbove");
//		}
//
//		@Override
//		@Contract("_->fail")
//		public void setBelow(@NotNull Node<T> node) {
//			throw new UnsupportedOperationException("setBelow");
//		}
//
//		@Override
//		@Contract("_->fail")
//		public void setNext(@NotNull Node<T> node) {
//			throw new UnsupportedOperationException("setNext");
//		}
//
//		@Override
//		@Contract("_->fail")
//		public void setPrevious(@NotNull Node<T> node) {
//			throw new UnsupportedOperationException("setPrevious");
//		}
//
//		@Override
//		@Contract("_->fail")
//		public void setValue(@NotNull T value) {
//			throw new UnsupportedOperationException("setValue");
//		}
//	}
//}
////
////	/**
////	 * Returns a spliterator iterating over the children nodes associated to the given
////	 * node. The children nodes considered children to a node are the {@link
////	 * Node#getNext()} chained nodes of the {@link Node#getChild() child} node of it.
////	 * <br>
////	 * The bellow is a visualization over the iteration using the returned spliterator.
////	 * <br><br>
////	 * <pre>
////	 *     for(Node child = node.getChild(); child != null; child = child.getNext())
////	 *     		;
////	 * </pre>
////	 * <br>
////	 * Note that the returned spliterator will exactly do an implementation of the above
////	 * code. So, when a change in relations occur to the children. The spliterator will
////	 * not know about that and it will simply continue believing that the next children
////	 * after the child it is pointing at (at a specific time) are children of the given
////	 * {@code node}.
////	 *
////	 * @param node the node to return a spliterator iterating over its children.
////	 * @return a spliterator iterating over the children of the given {@code node}.
////	 * @throws NullPointerException if the given {@code node} is null.
////	 * @since 0.2.0 ~2021.02.19
////	 */
////	public static Spliterator<Node> spliterator(Node node) {
////		Objects.requireNonNull(node, "node");
////		return Spliterators.spliteratorUnknownSize(
////				Nodes.iterator(node),
////				Spliterator.NONNULL |
////				Spliterator.ORDERED
////		);
////	}
////
////	/**
////	 * Returns a stream over the children nodes associated to the given {@code node}. The
////	 * children nodes considered children to a node are the {@link Node#getNext()} chained
////	 * nodes of the {@link Node#getChild() child} node of it.
////	 * <br>
////	 * The bellow is a visualization over the streaming using the returned stream.
////	 * <br><br>
////	 * <pre>
////	 *     for(Node child = node.getChild(); child != null; child = child.getNext())
////	 *     		;
////	 * </pre>
////	 * <br>
////	 * Note that the returned stream will exactly do an implementation of the above code.
////	 * So, when a change in relations occur to the children. The stream will not know
////	 * about that and it will simply continue believing that the next children after the
////	 * child it is pointing at (at a specific time) are children of the given {@code
////	 * node}.
////	 *
////	 * @param node the node to return a stream over its children.
////	 * @return a stream over the children of the given {@code node}.
////	 * @throws NullPointerException if the given {@code node} is null.
////	 * @since 0.2.0 ~2021.02.19
////	 */
////	public static Stream<Node> stream(Node node) {
////		Objects.requireNonNull(node, "node");
////		return StreamSupport.stream(
////				Nodes.spliterator(node),
////				false
////		);
////	}
////
////	/**
////	 * Returns an iterator iterating over the children nodes associated to the given
////	 * {@code node}. The children nodes considered children to a node are the {@link
////	 * Node#getNext()} chained nodes of the {@link Node#getChild() child} node of it.
////	 * <br>
////	 * The bellow is a visualization over the iteration using the returned iterator.
////	 * <br><br>
////	 * <pre>
////	 *     for(Node child = node.getChild(); child != null; child = child.getNext())
////	 *     		;
////	 * </pre>
////	 * <br>
////	 * Note that the returned iterator will exactly do an implementation of the above
////	 * code. So, when a change in relations occur to the children. The iterator will not
////	 * know about that and it will simply continue believing that the next children after
////	 * the child it is pointing at (at a specific time) are children of the given {@code
////	 * node}.
////	 *
////	 * @param node the node to return an iterator iterating over its children.
////	 * @return an iterator iterating over the children of the given {@code node}.
////	 * @throws NullPointerException if the given {@code node} is null.
////	 * @since 0.2.0 ~2021.02.19
////	 */
////	public static Iterator<Node> iterator(Node node) {
////		Objects.requireNonNull(node, "node");
////		//noinspection ALL
////		return new Iterator<Node>() {
////			/**
////			 * The current/last child iterated.
////			 *
////			 * @since 0.2.0 ~2021.02.17
////			 */
////			protected Node child = node.getChild();
////
////			@Override
////			public boolean hasNext() {
////				return this.child != null;
////			}
////
////			@Override
////			public Node next() {
////				if (this.child == null)
////					throw new NoSuchElementException("No more children!");
////
////				Node child = this.child;
////				this.child = child.getNext();
////				return child;
////			}
////		};
////	}
//
////	/**
////	 * Returns a modifiable view of the children of the given {@code node}. The view is
////	 * treating the children of the given node as a list. The locate the more {@link
////	 * Node#getPrevious() previous} children as the start and the more {@link
////	 * Node#getNext()} children at the end. Modifications to the children are made by the
////	 * utilities of this class.
////	 *
////	 * @param node the node to get a list view of its children.
////	 * @return a modifiable list view of the children of the given {@code node}.
////	 * @throws NullPointerException if the given {@code node} is null.
////	 * @since 0.2.0 ~2021.02.19
////	 */
////	public static List<Node> asList(Node node) {
////		Objects.requireNonNull(node, "node");
////		//noinspection ALL
////		return new AbstractList<Node>() {
////			@Override
////			public boolean add(Node element) {
////				Objects.requireNonNull(element, "element");
////				Node child = node.getBelow();
////				if (child == null)
////					Nodes.placeChild(
////							node,
////							element
////					);
////				else
////					Nodes.placeNext(
////							Nodes.getTail(child),
////							element
////					);
////				return true;
////			}
////
////			@Override
////			public void add(int index, Node element) {
////				Objects.requireNonNull(element, "element");
////				if (index < 0)
////					throw new IndexOutOfBoundsException("index < 0");
////				if (index == 0)
////					Nodes.placeChild(node, element);
////				else {
////					Node child = this.stream()
////							.skip(index - 1)
////							.findFirst()
////							.orElseThrow(() ->
////									new IndexOutOfBoundsException("index > size()")
////							);
////					Nodes.placeNext(
////							child,
////							element
////					);
////				}
////			}
////
////			@Override
////			public Node get(int index) {
////				if (index < 0)
////					throw new IndexOutOfBoundsException("index < 0");
////				return this.stream()
////						.skip(index)
////						.findFirst()
////						.orElseThrow(() ->
////								new IndexOutOfBoundsException("index >= size()")
////						);
////			}
////
////			@Override
////			public boolean isEmpty() {
////				return !node.hasBelow();
////			}
////
////			@Override
////			public Iterator<Node> iterator() {
////				//noinspection ALL
////				return new Iterator<Node>() {
////					/**
////					 * The current/last child iterated.
////					 *
////					 * @since 0.2.0 ~2021.02.17
////					 */
////					private Node child = node.getBelow();
////					/**
////					 * The last node returned from the method {@link #next()}.
////					 *
////					 * @since 0.2.0 ~2021.02.19
////					 */
////					private Node lastReturn;
////
////					@Override
////					public boolean hasNext() {
////						return this.child != null;
////					}
////
////					@Override
////					public Node next() {
////						if (this.child == null)
////							throw new NoSuchElementException("No more children!");
////
////						Node child = this.child;
////						this.child = child.getNext();
////						this.lastReturn = child;
////						return child;
////					}
////
////					@Override
////					public void remove() {
////						if (this.lastReturn == null)
////							throw new IllegalStateException("remove");
////
////						Node last = this.lastReturn;
////						this.lastReturn = null;
////						Nodes.remove(last);
////					}
////				};
////			}
////
////			@Override
////			public ListIterator<Node> listIterator(int index) {
////				if (index < 0)
////					throw new IndexOutOfBoundsException("index < 0");
////				//noinspection ALL
////				return new ListIterator<Node>() {
////					/**
////					 * The last returned node from either {@link #next()} or {@link #previous()}.
////					 *
////					 * @since 0.2.0 ~2021.02.19
////					 */
////					private Node lastReturn;
////					/**
////					 * The next node to be returned with the next call to {@link #next()}.
////					 *
////					 * @since 0.2.0 ~2021.02.19
////					 */
////					private Node next;
////					/**
////					 * The previous node to be returned with the next call to {@link #previous()}.
////					 *
////					 * @since 0.2.0 ~2021.02.19
////					 */
////					private Node previous;
////
////					{
////						if (node.hasBelow()) {
////							//noinspection ALL
////							Node child = stream()
////									.skip(index)
////									.findFirst()
////									.orElse(null);
////
////							if (child == null) {
////								if (index != 0)
////									throw new IndexOutOfBoundsException("index > size()");
////							} else {
////								this.next = child;
////								this.previous = child.getPrevious();
////							}
////						}
////					}
////
////					@Override
////					public void add(Node element) {
////						Objects.requireNonNull(element, "element");
////
////						Node next = this.next;
////						Node previous = this.previous;
////
////						if (next != null)
////							Nodes.placePrevious(next, element);
////						else if (previous != null)
////							Nodes.placeNext(previous, element);
////						else
////							Nodes.placeChild(node, element);
////
////						this.lastReturn = null;
////						this.previous = element;
////					}
////
////					@Override
////					public boolean hasNext() {
////						return this.next != null;
////					}
////
////					@Override
////					public boolean hasPrevious() {
////						return this.previous != null;
////					}
////
////					@Override
////					public Node next() {
////						Node next = this.next;
////
////						if (next == null)
////							throw new NoSuchElementException("End reached");
////
////						this.next = next.getNext();
////						this.previous = next;
////						this.lastReturn = next;
////						return next;
////					}
////
////					@Override
////					public int nextIndex() {
////						Node next = this.next;
////						int i = 0;
////						while ((next = next.getPrevious()) != null)
////							i++;
////						return i;
////					}
////
////					@Override
////					public Node previous() {
////						Node previous = this.previous;
////
////						if (previous == null)
////							throw new NoSuchElementException("Start reached");
////
////						this.previous = previous.getPrevious();
////						this.next = previous;
////						this.lastReturn = previous;
////						return previous;
////					}
////
////					@Override
////					public int previousIndex() {
////						Node previous = this.previous;
////						int i = 0;
////						while ((previous = previous.getPrevious()) != null)
////							i++;
////						return i;
////					}
////
////					@Override
////					public void remove() {
////						Node lastReturn = this.lastReturn;
////						Node previous = this.previous;
////						Node next = this.next;
////
////						if (lastReturn == null)
////							throw new IllegalStateException("remove");
////
////						Nodes.remove(lastReturn);
////
////						this.lastReturn = null;
////						if (next == lastReturn)
////							//the next is removed
////							if (previous != null)
////								//"Onii-Chan, what is my Ototo?" next asking
////								this.next = previous.getNext();
////							else
////								//"Otousan, what is my Ototo?" next asking
////								this.next = node.getBelow();
////						if (previous == lastReturn)
////							//the previous is removed
////							if (next != null)
////								//"Ototo, what is my Onii-Chan?" previous asking
////								this.previous = next.getPrevious();
////							else
////								//"I got no Onii-Chan" previous saying
////								this.previous = null;
////					}
////
////					@Override
////					public void set(Node element) {
////						Objects.requireNonNull(element, "element");
////
////						Node lastReturn = this.lastReturn;
////						Node previous = this.previous;
////						Node next = this.next;
////
////						if (lastReturn == null)
////							throw new IllegalStateException("set");
////
////						Nodes.replace(lastReturn, element);
////
////						this.lastReturn = null;
////						if (previous == lastReturn)
////							this.previous = element;
////						if (next == lastReturn)
////							this.next = element;
////					}
////				};
////			}
////
////			@Override
////			public Node remove(int index) {
////				if (index < 0)
////					throw new IndexOutOfBoundsException("index < 0");
////				Node child = this.stream()
////						.skip(index)
////						.findFirst()
////						.orElseThrow(() ->
////								new IndexOutOfBoundsException("index >= size()")
////						);
////				Nodes.remove(child);
////				return child;
////			}
////
////			@Override
////			public Node set(int index, Node element) {
////				Objects.requireNonNull(element, "element");
////				if (index < 0)
////					throw new IndexOutOfBoundsException("index < 0");
////				Node child = this.stream()
////						.skip(index)
////						.findFirst()
////						.orElseThrow(() ->
////								new IndexOutOfBoundsException("index >= size()")
////						);
////				Nodes.replace(child, element);
////				return child;
////			}
////
////			@Override
////			public int size() {
////				return this.stream()
////						.mapToInt(n -> 1)
////						.sum();
////			}
////
////			@Override
////			public Spliterator<Node> spliterator() {
////				return Spliterators.spliteratorUnknownSize(
////						this.iterator(),
////						Spliterator.NONNULL |
////						Spliterator.ORDERED
////				);
////			}
////
////			@Override
////			public Stream<Node> stream() {
////				return StreamSupport.stream(
////						this.spliterator(),
////						false
////				);
////			}
////		};
////	}
//
////	public static boolean check(Node node, int i, int j) {
////		Objects.requireNonNull(node, "node");
////		switch (Dominance.compute(node, i, j)) {
////			case PART:
////			case EXACT:
////				return Nodes.asList(node)
////						.parallelStream()
////						.map(n -> Dominance.compute(n, i, j))
////						.allMatch(Predicate.isEqual(Dominance.NONE));
////			default:
////				return false;
////		}
////	}
////
////	/**
////	 * Loop back and get the head node of the given {@code node}. The head node of a node
////	 * is the most {@link Node#getPrevious() previous} node of it.
////	 * <br><br>
////	 * The bellow is a visualization over the steps getting the head.
////	 * <pre>
////	 *     while(node.hasPrevious())
////	 *     		node = node.getPrevious();
////	 *     return node;
////	 * </pre>
////	 *
////	 * @param node the node to get the head node of it.
////	 * @return the head node (the most previous node) of the given {@code node}.
////	 * @throws NullPointerException if the given {@code node} is null.
////	 * @since 0.2.0 ~2021.02.19
////	 */
////	public static Node getHead(Node node) {
////		Objects.requireNonNull(node, "node");
////		Node i = node;
////		while (true) {
////			Node p = i.getPrevious();
////
////			if (p == null)
////				return i;
////
////			i = p;
////		}
////	}
////
////	public static Node getParent(Node node) {
////		Objects.requireNonNull(node, "node");
////		Node i = node;
////		while (true) {
////			Node p = i.getAbove();
////
////			if (p != null)
////				return p;
////
////			p = i.getPrevious();
////
////			if (p == null)
////				return null;
////
////			i = p;
////		}
////	}
////
////	/**
////	 * Loop up and get the root node of the given {@code node}. The root node of a node is
////	 * the most {@link Node#getAbove() parent} node of it.
////	 * <br><br>
////	 * The bellow is a visualization over the steps getting the root.
////	 * <pre>
////	 *     while(node.hasParent())
////	 *     		node = node.getParent();
////	 *     return node;
////	 * </pre>
////	 *
////	 * @param node the node to get the root node of it.
////	 * @return the root node (the most parent node) of the given {@code node}.
////	 * @throws NullPointerException if the given {@code node} is null.
////	 * @since 0.2.0 ~2021.02.19
////	 */
////	public static Node getRoot(Node node) {
////		Objects.requireNonNull(node, "node");
////		Node i = node;
////		while (true) {
////			Node p = i.getAbove();
////
////			if (p == null) {
////				p = i.getPrevious();
////
////				if (p == null)
////					return i;
////			}
////
////			i = p;
////		}
////	}
////
////	/**
////	 * Loop ahead and get the tail node of the given {@code node}. The tail node of a node
////	 * is the most {@link Node#getNext() next} node of it.
////	 * <br><br>
////	 * The bellow is a visualization over the steps getting the tail.
////	 * <pre>
////	 *     while(node.hasNext())
////	 *     		node = node.getNext();
////	 *     return node;
////	 * </pre>
////	 *
////	 * @param node the node to get the tail node of it.
////	 * @return the tail node (the most next node) of the given {@code node}.
////	 * @throws NullPointerException if the given {@code node} is null.
////	 * @since 0.2.0 ~2021.02.19
////	 */
////	public static Node getTail(Node node) {
////		Objects.requireNonNull(node, "node");
////		Node i = node;
////		while (true) {
////			Node n = i.getNext();
////
////			if (n == null)
////				return i;
////
////			i = n;
////		}
////	}
//
////
////	/**
////	 * Place the given {@code other} strictly as a child node of {@code node}.
////	 * <br><br>
////	 * <pre>
////	 *     child = node.child
////	 *     <br>
////	 *     other.parent = node
////	 *     other.previous = null
////	 *     other.next = child
////	 *     <br>
////	 *     node.child = other
////	 *     <br>
////	 *     child.previous = other
////	 * </pre>
////	 *
////	 * @param node  the central node.
////	 * @param other the node to be the next node of the central node.
////	 * @throws NullPointerException if the given {@code node} or {@code other} is null.
////	 * @since 0.2.0 ~2021.02.17
////	 */
////	public static void placeChild(Node node, Node other) {
////		Objects.requireNonNull(node, "node");
////		Objects.requireNonNull(other, "other");
////
////		Node child = node.getBelow();
////
////		//other.parent
////		other.setAbove(node);
////		//other.previous
////		other.removePrevious();
////		//other.next
////		if (child != null)
////			other.setNext(child);
////		else
////			other.removeBelow();
////
////		//node.child
////		node.setBelow(other);
////
////		//child.previous
////		if (child != null)
////			child.setPrevious(other);
////	}
////
////	/**
////	 * Place the given {@code other} strictly as a next node of {@code node}.
////	 * <br><br>
////	 * <pre>
////	 *     parent = node.parent
////	 *     next = node.next
////	 *     <br>
////	 *     other.parent = parent
////	 *     other.previous = node
////	 *     other.next = next
////	 *     <br>
////	 *     node.next = other
////	 *     <br>
////	 *     next.previous = other
////	 * </pre>
////	 *
////	 * @param node  the central node.
////	 * @param other the node to be the next node of the central node.
////	 * @throws NullPointerException if the given {@code node} or {@code other} is null.
////	 * @since 0.2.0 ~2021.02.17
////	 */
////	public static void placeNext(Node node, Node other) {
////		Objects.requireNonNull(node, "node");
////		Objects.requireNonNull(other, "other");
////
////		Node parent = node.getAbove();
////		Node next = node.getNext();
////
////		//other.parent
////		if (parent != null)
////			other.setAbove(parent);
////		else
////			other.removeAbove();
////		//other.previous
////		other.setPrevious(node);
////		//other.next
////		if (next != null)
////			other.setNext(next);
////		else
////			other.removeNext();
////
////		//node.next
////		node.setNext(other);
////
////		//next.previous
////		if (next != null)
////			next.setPrevious(other);
////	}
////
////	/**
////	 * Place the given {@code other} strictly as a previous node of {@code node}.
////	 * <br><br>
////	 * <pre>
////	 *     parent = node.parent
////	 *     previous = node.previous
////	 *     <br>
////	 *     other.parent = parent
////	 *     other.previous = previous
////	 *     other.next = node
////	 *     <br>
////	 *     node.previous = other
////	 *     <br>
////	 *     if(parent.child == node)
////	 *        parent.child = other
////	 *     <br>
////	 *     previous.next = other
////	 * </pre>
////	 *
////	 * @param node  the central node.
////	 * @param other the node to be the previous node of the central node.
////	 * @throws NullPointerException if the given {@code node} or {@code other} is null.
////	 * @since 0.2.0 ~2021.02.17
////	 */
////	public static void placePrevious(Node node, Node other) {
////		Objects.requireNonNull(node, "node");
////		Objects.requireNonNull(other, "other");
////
////		Node parent = node.getAbove();
////		Node previous = node.getPrevious();
////
////		//other.parent
////		if (parent != null)
////			other.setAbove(parent);
////		else
////			other.removeAbove();
////		//other.previous
////		if (previous != null)
////			other.setPrevious(previous);
////		else
////			other.removePrevious();
////		//other.next
////		other.setNext(node);
////
////		//node.previous
////		node.setPrevious(other);
////
////		//parent.child
////		if (parent != null)
////			if (parent.getBelow() == node)
////				parent.setBelow(other);
////
////		//previous.next
////		if (previous != null)
////			previous.setNext(other);
////	}
////
////	public static void putChild(Node parent, Node child) {
////		Objects.requireNonNull(parent, "parent");
////		Objects.requireNonNull(child, "child");
////
////		List<Node> view = Nodes.asList(parent);
////
////		switch (Dominance.compute(parent, child)) {
////			case PART:
////			case EXACT:
////				break;
////			default:
////				throw new IllegalArgumentException("child cannot fit on parent");
////		}
////
////		for (Node node : view)
////			switch (Dominance.compute(node, child)) {
////				case SHARE:
////				case EXACT:
////					throw new IllegalArgumentException("child clash with another child");
////			}
////
////		Iterator<Node> iterator = view.iterator();
////
////		if (iterator.hasNext()) {
////			//the current round brother
////			Node brother = iterator.next();
////
////			//special first brother treatment
////			switch (Relation.compute(brother, child)) {
////				case BEHIND:
////				case BEFORE:
////				case CONTAINER:
////				case PREVIOUS:
////					//I know daddy
////					child.setAbove(parent);
////					//I know my little brother
////					child.setNext(brother);
////					//Hey brother, I am your Onii-Chan
////					brother.setPrevious(child);
////					//Hey daddy, I am the best
////					parent.setBelow(child);
////			}
////
////			//the previous round brother
////			Node older = null;
////
////			while (iterator.hasNext()) {
////				switch (Dominance.compute(brother, child)) {
////					case CONTAIN:
////						//My child, you need to deal with your brothers!
////						Nodes.putChild(child, brother);
////						iterator.remove();
////
////						//Is there an Onii-Chan?
////						if (older != null)
////							//Onii-Chan, is this your next ototo?
////							if (older.getNext() == brother)
////								//Onii-Chan, this is my child. Not your brother!
////								older.setNext(child);
////
////						//Is there an Ototo?
////						if (iterator.hasNext()) {
////							Node younger = iterator.next();
////
////							//Ototo, is this your direct Onii-Chan?
////							if (younger.getPrevious() == brother)
////								//Ototo, this is my child. Not your brother -_-'
////								younger.setPrevious(child);
////						}
////
////						break;
////					case PART:
////						//They were my uncles and aunts!!!
////						Nodes.putChild(brother, child);
////						return;
////				}
////				//
////				//				//Is there a younger brother for the next round?
////				//				if (younger == null)
////				//					//ok stop!
////				//					break;
////				//
////				//				//this round's brother is the next round's Onii-Chan
////				//				older = brother;
////				//				//this round's ototo is the next round's brother
////				//				brother = younger;
////			}
////		} else {
////			//this is the first child!
////			parent.setBelow(child);
////			child.setAbove(parent);
////		}
////	}
//
////
////	/**
////	 * Cleanly remove the given {@code node} from its environment. Putting all of its
////	 * children in its place.
////	 * <br><br>
////	 * <pre>
////	 *     parent = node.parent
////	 *     previous = node.previous
////	 *     next = node.next
////	 *     head = node.child
////	 *     tail = node.tail
////	 *     children = node.children
////	 *     <br>
////	 *     node.parent = null
////	 *     node.previous = null
////	 *     node.next = null
////	 *     node.child = null
////	 *     <br>
////	 *     if(parent.child == node)
////	 *        parent.child = child || next
////	 *     <br>
////	 *     previous.next = child || next
////	 *     <br>
////	 *     next.previous = tip || previous
////	 *     <br>
////	 *     head.previous = previous
////	 *     <br>
////	 *     tail.next = next
////	 *     <br>
////	 *     children.parent = parent
////	 * </pre>
////	 *
////	 * @param node the node to be cleanly removed.
////	 * @throws NullPointerException if the given {@code node} is null.
////	 * @since 0.2.0 ~2021.02.19
////	 */
////	@SuppressWarnings({"OverlyComplexMethod", "OverlyLongMethod"})
////	public static void remove(Node node) {
////		Objects.requireNonNull(node, "node");
////
////		Node parent = node.getAbove();
////		Node previous = node.getPrevious();
////		Node next = node.getNext();
////		Node head = node.getBelow();
////		Node tail = head == null ? null : Nodes.getTail(head);
////
////		//node.parent
////		node.removeAbove();
////		//node.previous
////		node.removePrevious();
////		//node.next
////		node.removeNext();
////		//node.head
////		node.removeBelow();
////
////		//parent.head
////		if (parent != null)
////			if (parent.getBelow() == node)
////				if (head != null)
////					parent.setBelow(head);
////				else if (next != null)
////					parent.setBelow(next);
////				else
////					parent.removeBelow();
////
////		//previous.next
////		if (previous != null)
////			if (head != null)
////				previous.setNext(head);
////			else if (next != null)
////				previous.setNext(next);
////			else
////				previous.removeNext();
////
////		//next.previous
////		if (next != null)
////			if (tail != null)
////				next.setPrevious(tail);
////			else if (previous != null)
////				next.setPrevious(previous);
////			else
////				next.removePrevious();
////
////		//head.previous
////		if (head != null)
////			if (previous != null)
////				head.setPrevious(previous);
////			else
////				head.removePrevious();
////
////		//tail.next
////		if (tail != null)
////			if (next != null)
////				tail.setNext(next);
////			else
////				tail.removeNext();
////
////		//children.parent
////		if (parent != null)
////			for (Node c = head; c != null; c = c.getNext())
////				c.setAbove(parent);
////		else
////			for (Node c = head; c != null; c = c.getNext())
////				c.removeAbove();
////	}
//
////	/**
////	 * Replace the given {@code node} with {@code other}. Redirect all its connections to
////	 * the other node.
////	 * <br><br>
////	 * <pre>
////	 *     parent = node.parent
////	 *     previous = node.previous
////	 *     next = node.next
////	 *     child = node.child
////	 *     children = node.children
////	 *     <br>
////	 *     other.parent = parent
////	 *     other.previous = previous
////	 *     other.next = next
////	 *     other.child = child
////	 *     <br>
////	 *     node.parent = null
////	 *     node.previous = null
////	 *     node.next = null
////	 *     node.child = null
////	 *     <br>
////	 *     if(parent.child == node)
////	 *        parent.child = other
////	 *     <br>
////	 *     previous.next = other
////	 *     <br>
////	 *     next.previous = other
////	 *     <br>
////	 *     children.parent = other
////	 * </pre>
////	 *
////	 * @param node  the central node.
////	 * @param other the node to replace the central node with.
////	 * @throws NullPointerException if the given {@code node} or {@code other} is null.
////	 * @since 0.2.0 ~2021.02.17
////	 */
////	@SuppressWarnings("OverlyLongMethod")
////	public static void replace(Node node, Node other) {
////		Objects.requireNonNull(node, "node");
////		Objects.requireNonNull(other, "other");
////
////		Node parent = node.getAbove();
////		Node previous = node.getPrevious();
////		Node next = node.getNext();
////		Node child = node.getBelow();
////
////		//other.parent
////		if (parent != null)
////			other.setAbove(parent);
////		else
////			other.removeAbove();
////		//other.previous
////		if (previous != null)
////			other.setPrevious(previous);
////		else
////			other.removePrevious();
////		//other.next
////		if (next != null)
////			other.setNext(next);
////		else
////			other.removeNext();
////		//other.child
////		if (child != null)
////			other.setBelow(child);
////		else
////			other.removeBelow();
////
////		//node.parent
////		node.removeAbove();
////		//node.previous
////		node.removePrevious();
////		//node.next
////		node.removeNext();
////		//node.child
////		node.removeBelow();
////
////		//parent.child
////		if (parent != null)
////			if (parent.getBelow() == node)
////				parent.setBelow(other);
////
////		//previous.next
////		if (previous != null)
////			previous.setNext(other);
////
////		//next.previous
////		if (next != null)
////			next.setPrevious(other);
////
////		//children.parent
////		for (Node c = child; c != null; c = c.getNext())
////			c.setAbove(other);
////	}
////--des
////if the relations {@code node->above} or
////	 *                                       {@code node.above->below} or {@code
////	 *                                       node->previous} or {@code node.previous->next}
////	 *                                       or {@code node->next} or {@code
////	 *                                       node.next->previous} or {@code node->below}
////	 *                                       or {@code node.below->above} cannot be
////	 *                                       removed.
////--
