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
//import java.util.Objects;
//
///**
// * @author LSafer
// * @version 0.0.0
// * @since 0.0.0 ~2021.02.23
// */
//public final class FlowNodes {
//	public static void addAll(Node node, Node other) {
//		//............
//		Objects.requireNonNull(node, "node");
//		Objects.requireNonNull(other, "other");
//
//		Node next = node.getNext();
//		Node last = Node.getTail(other);
//
//		other.removeAbove();
//		other.setPrevious(node);
//	}
//
//	/**
//	 * Dynamically and Safely add the given {@code other} after the given {@code node}. By
//	 * being the next node of the {@code node} and the previous node of its original next
//	 * node.
//	 * <br><br>
//	 * <pre>
//	 *     other.above => null
//	 *     other.previous => node
//	 *     other.next => node.next
//	 * </pre>
//	 *
//	 * @param node  the node to add the other node after it.
//	 * @param other the node to be added after the given {@code node}.
//	 * @throws NullPointerException          if the given {@code node} or {@code other} is
//	 *                                       null.
//	 * @throws UnsupportedOperationException if a node does not allow modifying a
//	 *                                       necessary relation that should be modified
//	 *                                       for the operation to be done.
//	 * @since 0.2.0 ~2021.02.23
//	 */
//	public static void addNext(Node node, Node other) {
//		Objects.requireNonNull(node, "node");
//		Objects.requireNonNull(other, "other");
//		Node next = node.getNext();
//
//		other.removeAbove();
//		other.setPrevious(node);
//		if (next == null)
//			other.removeNext();
//		else
//			other.setNext(next);
//	}
//
//	/**
//	 * Dynamically and safely add the given {@code other} before the given {@code node}.
//	 * By being the previous node of the {@code node} and the next node of its original
//	 * previous node and being the below node of its parent node.
//	 * <br><br>
//	 * <pre>
//	 *     other.above => node.above
//	 *     other.previous => node.previous
//	 *     other.next => node
//	 * </pre>
//	 *
//	 * @param node  the node to add the other node before it.
//	 * @param other the node to be added before the given {@code node}.
//	 * @throws NullPointerException          if the given {@code node} or {@code other} is
//	 *                                       null.
//	 * @throws UnsupportedOperationException if a node does not allow modifying a
//	 *                                       necessary relation that should be modified
//	 *                                       for the operation to be done.
//	 * @since 0.2.0 ~2021.02.23
//	 */
//	public static void addPrevious(Node node, Node other) {
//		Objects.requireNonNull(node, "node");
//		Objects.requireNonNull(other, "other");
//		Node above = node.getAbove();
//		Node previous = node.getPrevious();
//
//		if (above == null)
//			other.removeAbove();
//		else
//			other.setAbove(above);
//		if (previous == null)
//			other.removePrevious();
//		else
//			other.setPrevious(previous);
//		other.setNext(node);
//	}
//
//	public static void remove(Node node) {
//		Objects.requireNonNull(node, "node");
//		Node above = node.getAbove();
//		Node previous = node.getPrevious();
//		Node next = node.getNext();
//		Node below = node.getBelow();
//	}
//
//	/**
//	 * Dynamically and safely replace the given {@code node} with the given {@code other}
//	 * node. By redirecting all of its relations to the other node.
//	 * <br><br>
//	 * <pre>
//	 *     other.above => node.above
//	 *     other.previous => node.previous
//	 *     other.next => node.next
//	 *     other.below => node.below
//	 * </pre>
//	 *
//	 * @param node  the node to be replaced.
//	 * @param other the node to replace with.
//	 * @throws NullPointerException          if the given {@code node} or {@code other} is
//	 *                                       null.
//	 * @throws UnsupportedOperationException if a node does not allow modifying a
//	 *                                       necessary relation that should be modified
//	 *                                       for the operation to be done.
//	 * @since 0.2.0 ~2021.02.24
//	 */
//	public static void replace(Node node, Node other) {
//		Objects.requireNonNull(node, "node");
//		Objects.requireNonNull(other, "other");
//		Node above = node.getAbove();
//		Node previous = node.getPrevious();
//		Node next = node.getNext();
//		Node below = node.getBelow();
//
//		if (above == null)
//			Node.removeAbove(other);
//		else
//			Node.setAbove(other, above);
//		if (previous == null)
//			Node.removePrevious(other);
//		else
//			Node.setPrevious(other, previous);
//		if (next == null)
//			Node.removeNext(other);
//		else
//			Node.setNext(other, next);
//		if (below == null)
//			Node.removeBelow(other);
//		else
//			Node.setBelow(other, below);
//	}
//}
