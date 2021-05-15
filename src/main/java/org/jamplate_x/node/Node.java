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
//import java.util.Objects;
//
///**
// * A node is a unit in a tree. A node knows the nodes around it and the nodes around it
// * only.
// * <br>
// * Two related nodes must have opposite relations between each other. If a node that have
// * another node as its {@code Below} node then that other node MUST have the node as its
// * {@code Above} node and vise versa. Also applies for the relations {@code Previous} and
// * {@code Next} and all other relations.
// * <br>
// * <ul>
// *     <li>{@code remove}: remove a relation from it.</li>
// *     <li>{@code set}: set a relation from it to other.</li>
// *     <li>{@code delete}: remove all relations from it.</li>
// * </ul>
// * <br>
// *
// * @param <T> the tpe of the value of the node.
// * @author LSafer
// * @version 0.2.0
// * @since 0.2.0 ~2021.02.14
// */
//public interface Node<T> {
//	/**
//	 * Set the given {@code other} node to be between the given {@code node} and the node
//	 * above it. By setting the {@code other} node as the node above the given {@code
//	 * node} and the node previously above the given {@code node} to be the node above the
//	 * {@code other} node.
//	 * <ol>
//	 *     <li>set above {@code node} to {@code other}</li>
//	 *     <li>set above {@code other} to {@code node.above}</li>
//	 * </ol>
//	 *
//	 * @param node  the node to add the other node above it.
//	 * @param other the node to be added between the given {@code node} and the node above
//	 *              it.
//	 * @param <T>   the type of the value of the nodes.
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
//	static <T> void addAbove(@NotNull Node<T> node, @NotNull Node<T> other) {
//		Objects.requireNonNull(node, "node");
//		Objects.requireNonNull(other, "other");
//		Node<T> above = node.getAbove();
//
//		node.setAbove(other);
//
//		if (above != null)
//			other.setAbove(above);
//	}
//
//	/**
//	 * Set the given {@code other} node to be between the given {@code node} and the node
//	 * below it. By setting the {@code other} node as the node below the given {@code
//	 * node} and the node previously below the given {@code node} to be the node below the
//	 * {@code other} node.
//	 * <ol>
//	 *     <li>set below {@code node} to {@code other}</li>
//	 *     <li>set below {@code other} to {@code node.below}</li>
//	 * </ol>
//	 *
//	 * @param node  the node to add the other node below it.
//	 * @param other the node to be added between the given {@code node} and the node below
//	 *              it.
//	 * @param <T>   the type of the value of the nodes.
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
//	static <T> void addBelow(@NotNull Node<T> node, @NotNull Node<T> other) {
//		Objects.requireNonNull(node, "node");
//		Objects.requireNonNull(other, "other");
//		Node<T> below = node.getBelow();
//
//		node.setBelow(other);
//
//		if (below != null)
//			other.setBelow(below);
//	}
//
//	/**
//	 * Set the given {@code other} node to be between the given {@code node} and the node
//	 * after it. By setting the {@code other} node as the node after the given {@code
//	 * node} and the node previously after the given {@code node} to be the node after the
//	 * {@code other} node.
//	 * <ol>
//	 *     <li>set next {@code node} to {@code other}</li>
//	 *     <li>set next {@code other} to {@code node.next}</li>
//	 * </ol>
//	 *
//	 * @param node  the node to add the other node after it.
//	 * @param other the node to be added between the given {@code node} and the node after
//	 *              it.
//	 * @param <T>   the type of the value of the nodes.
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
//	static <T> void addNext(@NotNull Node<T> node, @NotNull Node<T> other) {
//		Objects.requireNonNull(node, "node");
//		Objects.requireNonNull(other, "other");
//		Node<T> next = node.getNext();
//
//		node.setNext(other);
//
//		if (next != null)
//			other.setNext(next);
//	}
//
//	/**
//	 * Set the given {@code other} node to be between the given {@code node} and the node
//	 * before it. By setting the {@code other} node as the node before the given {@code
//	 * node} and the node previously before the given {@code node} to be the node before
//	 * the {@code other} node.
//	 * <ol>
//	 *     <li>set previous {@code node} to {@code other}</li>
//	 *     <li>set previous {@code other} to {@code node.previous}</li>
//	 * </ol>
//	 *
//	 * @param node  the node to add the other node before it.
//	 * @param other the node to be added between the given {@code node} and the node
//	 *              before it.
//	 * @param <T>   the type of the value of the nodes.
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
//	static <T> void addPrevious(@NotNull Node<T> node, @NotNull Node<T> other) {
//		Objects.requireNonNull(node, "node");
//		Objects.requireNonNull(other, "other");
//		Node<T> previous = node.getPrevious();
//
//		node.setPrevious(other);
//
//		if (previous != null)
//			other.setPrevious(previous);
//	}
//
//	/**
//	 * Get the most previous node of the given {@code node}. By looping back to the
//	 * previous node that has no previous node to it.
//	 * <br><br>
//	 * <pre>
//	 *     while(node.previous)
//	 *         node = node.previous
//	 *     <p/>
//	 *     return node
//	 * </pre>
//	 *
//	 * @param node the node to get the most previous node of it.
//	 * @param <T>  the tpe of the value of the node.
//	 * @return the most previous node from the given {@code node}. Or the node itself if
//	 * 		it is a head node.
//	 * @throws NullPointerException if the given {@code node} is null.
//	 * @since 0.2.0 ~2021.02.23
//	 */
//	@Contract(pure = true)
//	@NotNull
//	static <T> Node<T> getHead(@NotNull Node<T> node) {
//		Objects.requireNonNull(node, "node");
//		//cheatproof
//		Node<T> i = node;
//		while (true) {
//			Node<T> p = i.getPrevious();
//
//			if (p == null)
//				return i;
//
//			i = p;
//		}
//	}
//
//	/**
//	 * Get the parent node of the given {@code node}. By looping back to the first
//	 * previous node that has a node above it.
//	 * <br><br>
//	 * <pre>
//	 *     while(!node.above && node.previous)
//	 *         node = node.previous
//	 *     <p/>
//	 *     return node.above
//	 * </pre>
//	 *
//	 * @param node the node to get its parent.
//	 * @param <T>  the tpe of the value of the node.
//	 * @return the parent node of the given {@code node}. Or {@code null} if it has no
//	 * 		parent.
//	 * @throws NullPointerException if the given {@code node} is null.
//	 * @since 0.2.0 ~2021.02.23
//	 */
//	@Contract(pure = true)
//	@Nullable
//	static <T> Node<T> getParent(@NotNull Node<T> node) {
//		Objects.requireNonNull(node, "node");
//		//cheatproof
//		Node<T> i = node;
//		while (true) {
//			Node<T> p = i.getAbove();
//
//			if (p == null) {
//				i = i.getPrevious();
//
//				if (i == null)
//					return null;
//
//				continue;
//			}
//
//			return p;
//		}
//	}
//
//	/**
//	 * Get the root node of the given {@code node}. By looping back and up to the most
//	 * parent node.
//	 * <br><br>
//	 * <pre>
//	 *     while(node.parent)
//	 *         node = node.parent
//	 *     <p/>
//	 *     return node
//	 * </pre>
//	 *
//	 * @param node the node to get its root node.
//	 * @param <T>  the tpe of the value of the node.
//	 * @return the root node of the given {@code node}. Or the node itself if it is a root
//	 * 		node.
//	 * @throws NullPointerException if the given {@code node} is null.
//	 * @since 0.2.0 ~2021.02.23
//	 */
//	@Contract(pure = true)
//	@NotNull
//	static <T> Node<T> getRoot(@NotNull Node<T> node) {
//		Objects.requireNonNull(node, "node");
//		//cheatproof
//		Node<T> i = node;
//		while (true) {
//			Node<T> p = Node.getParent(i);
//
//			if (p == null)
//				return i;
//
//			i = p;
//		}
//	}
//
//	/**
//	 * Get the most next node of the given {@code node}. By looping forward to the next
//	 * node that has no next node to it.
//	 * <br><br>
//	 * <pre>
//	 *     while(node.next)
//	 *         node = node.next
//	 *     <p/>
//	 *     return node
//	 * </pre>
//	 *
//	 * @param node the node to get the most next node of it.
//	 * @param <T>  the tpe of the value of the node.
//	 * @return the most next node from the given {@code node}. Or the node itself if it is
//	 * 		a tail node.
//	 * @throws NullPointerException if the given {@code node} is null.
//	 * @since 0.2.0 ~2021.02.23
//	 */
//	@Contract(pure = true)
//	@NotNull
//	static <T> Node<T> getTail(@NotNull Node<T> node) {
//		Objects.requireNonNull(node, "node");
//		//cheatproof
//		Node<T> i = node;
//		while (true) {
//			Node<T> n = i.getNext();
//
//			if (n == null)
//				return i;
//
//			i = n;
//		}
//	}
//
//	/**
//	 * Just remove the given {@code node} from any relation.
//	 * <br><br>
//	 * Operations order:
//	 * <ol>
//	 *     <li>remove above {@code node}</li>
//	 *     <li>remove previous {@code node}</li>
//	 *     <li>remove next {@code node}</li>
//	 *     <li>remove below {@code node}</li>
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
//	static <T> void remove(@NotNull Node<T> node) {
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
//		//
//		//             below
//		//
//		Objects.requireNonNull(node, "node");
//		node.removeAbove();
//		node.removePrevious();
//		node.removeNext();
//		node.removeBelow();
//	}
//
//	/**
//	 * Completely replace the given {@code node} with the {@code other} node. By making
//	 * the {@code other} node have all the relations of the given {@code node}.
//	 * <br><br>
//	 * <ol>
//	 *     <li>remove {@code node}</li>
//	 *     <li>set above {@code other} to {@code node.above}</li>
//	 *     <li>set previous {@code other} to {@code node.previous}</li>
//	 *     <li>set next {@code other} to {@code node.next}</li>
//	 *     <li>set below {@code other} to {@code node.below}</li>
//	 * </ol>
//	 *
//	 * @param node  the node to be completely replaced
//	 * @param other the node to replace the given {@code node}.
//	 * @param <T>   the type of the value of the nodes.
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
//	 * @since 0.2.0 ~2021.02.28
//	 */
//	@Contract(mutates = "param1,param2")
//	static <T> void set(@NotNull Node<T> node, @NotNull Node<T> other) {
//		//
//		//              other
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
//		//
//		//              above
//		//              <|>
//		// previous <-> other <-> next
//		//              <|>
//		//              below
//		//
//		Objects.requireNonNull(node, "node");
//		Node<T> above = node.getAbove();
//		Node<T> previous = node.getPrevious();
//		Node<T> next = node.getNext();
//		Node<T> below = node.getBelow();
//
//		Node.remove(node);
//
//		if (above != null)
//			other.setAbove(above);
//		if (previous != null)
//			other.setPrevious(previous);
//		if (next != null)
//			other.setNext(next);
//		if (below != null)
//			other.setBelow(below);
//	}
//
//	/**
//	 * <b>Get Up</b>
//	 * <br>
//	 * Return the parent node of this node. If this node has a parent and it is its
//	 * parent's first child.
//	 *
//	 * @return the parent node of this node. Or {@code null} if no above node.
//	 * @since 0.2.0 ~2021.02.14
//	 */
//	@Contract(pure = true)
//	@Nullable
//	Node<T> getAbove();
//
//	/**
//	 * <b>Get Down</b>
//	 * <br>
//	 * Return the first child node of this node.
//	 *
//	 * @return the first child node of this node. Or {@code null} if no below node.
//	 * @since 0.2.0 ~2021.02.14
//	 */
//	@Contract(pure = true)
//	@Nullable
//	Node<T> getBelow();
//
//	/**
//	 * <b>Get Right<b>
//	 * <br>
//	 * Return the next node of this node.
//	 *
//	 * @return the node after this node. Or {@code null} if no next node.
//	 * @since 0.2.0 ~2021.02.14
//	 */
//	@Contract(pure = true)
//	@Nullable
//	Node<T> getNext();
//
//	/**
//	 * <b>Get Left</b>
//	 * <br>
//	 * Return the previous node of this node.
//	 *
//	 * @return the node before this node. Or {@code null} if no previous node.
//	 * @since 0.2.0 ~2021.02.14
//	 */
//	@Contract(pure = true)
//	@Nullable
//	Node<T> getPrevious();
//
//	/**
//	 * Get the value of this node.
//	 *
//	 * @return the value of this node.
//	 * @since 0.2.0 ~2021.02.24
//	 */
//	@Contract(pure = true)
//	@Nullable
//	T getValue();
//
//	/**
//	 * <b>Remove Up</b>
//	 * <br>
//	 * Remove the node above this node. If the above node has this node as its below node
//	 * then remove its below node. Therefore, completely unlinking this node from the node
//	 * above it.
//	 * <br><br>
//	 * Operations order:
//	 * <ol>
//	 *     <li>rm above {@code this}</li>
//	 *     <li>remove below {@code this.above} if equal {@code this}</li>
//	 * </ol>
//	 *
//	 * @throws UnsupportedOperationException if this node does not allow removing the node
//	 *                                       above it; if an operation cannot be done due
//	 *                                       to the necessity of an unsupported operation.
//	 *                                       (see the above operations list)
//	 * @since 0.2.0 ~2021.02.14
//	 */
//	@Contract(mutates = "this")
//	void removeAbove();
//
//	/**
//	 * <b>Remove Down</b>
//	 * <br>
//	 * Remove the node below this node. If the below node has this node as its above node
//	 * then remove its above node. Therefore, completely unlinking this node from the node
//	 * below it.
//	 * <br><br>
//	 * Operations order:
//	 * <ol>
//	 *     <li>rm below {@code this}</li>
//	 *     <li>remove above {@code this.below} if equal {@code this}</li>
//	 * </ol>
//	 *
//	 * @throws UnsupportedOperationException if this node does not allow removing the node
//	 *                                       below it; if an operation cannot be done due
//	 *                                       to the necessity of an unsupported operation.
//	 *                                       (see the above operations list)
//	 * @since 0.2.0 ~2021.02.14
//	 */
//	@Contract(mutates = "this")
//	void removeBelow();
//
//	/**
//	 * <b>Remove Right</b>
//	 * <br>
//	 * Remove the node next to this node. If the next node has this node as its previous
//	 * node then remove its previous node. Therefore, completely unlinking this node from
//	 * the node next to it.
//	 * <br><br>
//	 * Operations order:
//	 * <ol>
//	 *     <li>rm next {@code this}</li>
//	 *     <li>remove previous {@code this.next} if equal {@code this}</li>
//	 * </ol>
//	 *
//	 * @throws UnsupportedOperationException if this node does not allow removing the node
//	 *                                       after it; if an operation cannot be done due
//	 *                                       to the necessity of an unsupported operation.
//	 *                                       (see the above operations list)
//	 * @since 0.2.0 ~2021.02.14
//	 */
//	@Contract(mutates = "this")
//	void removeNext();
//
//	/**
//	 * <b>Remove Left</b>
//	 * <br>
//	 * Remove the node previous to this node. If the previous node has this node as its
//	 * next node then remove its next node. Therefore, completely unlinking this node from
//	 * the node previous to it.
//	 * <br><br>
//	 * Operations order:
//	 * <ol>
//	 *     <li>rm previous {@code this}</li>
//	 *     <li>remove next {@code this.previous} if equal {@code this}</li>
//	 * </ol>
//	 *
//	 * @throws UnsupportedOperationException if this node does not allow removing the node
//	 *                                       before it; if an operation cannot be done due
//	 *                                       to the necessity of an unsupported operation.
//	 *                                       (see the above operations list)
//	 * @since 0.2.0 ~2021.02.14
//	 */
//	@Contract(mutates = "this")
//	void removePrevious();
//
//	/**
//	 * Remove the value of this node. By setting it to {@code null}.
//	 *
//	 * @throws UnsupportedOperationException if this node does not allow or does not
//	 *                                       support removing its value.
//	 * @since 0.2.0 ~2021.02.25
//	 */
//	@Contract(mutates = "this")
//	void removeValue();
//
//	/**
//	 * <b>Set Up</b>
//	 * <br>
//	 * Set the given {@code node} to be the node above this node and set this node to be
//	 * the node below the given {@code node}. Also, completely unlink the old above node
//	 * from this node and the old below node from the given {@code node}.
//	 * <br><br>
//	 * Operations order:
//	 * <ol>
//	 *     <li>st above {@code this} to {@code node}</li>
//	 *     <li>remove below {@code this.above} if equal {@code this}</li>
//	 *     <li>set below {@code node} to {@code this} if not equal {@code this}</li>
//	 * </ol>
//	 *
//	 * @param node the new above node.
//	 * @throws NullPointerException          if the given {@code node} is null.
//	 * @throws IllegalStateException         if the given {@code node} has a node below it
//	 *                                       and is not this node.
//	 * @throws UnsupportedOperationException if this node does not allow changing the node
//	 *                                       above it; if an operation cannot be done due
//	 *                                       to the necessity of an unsupported operation.
//	 *                                       (see the above operations list)
//	 * @throws IllegalArgumentException      if this node rejects the given {@code node}
//	 *                                       as a node above it; if an operation cannot be
//	 *                                       done due to a node rejecting to relate to a
//	 *                                       node necessary to relate to due to some
//	 *                                       aspect of it. (see the above operations
//	 *                                       list)
//	 * @since 0.2.0 ~2021.02.14
//	 */
//	@Contract(mutates = "this,param")
//	void setAbove(@NotNull Node<T> node);
//
//	/**
//	 * <b>Set Down</b>
//	 * <br>
//	 * Set the given {@code node} to be the node below this node and set this node to be
//	 * the node above the given {@code node}. Also, completely unlink the old below node
//	 * from this node and the old above node from the given {@code node}.
//	 * <br><br>
//	 * Operations order:
//	 * <ol>
//	 *     <li>st below {@code this} to {@code node}</li>
//	 *     <li>remove above {@code this.below} if equal {@code this}</li>
//	 *     <li>set above {@code node} to {@code this} if not equal {@code this}</li>
//	 * </ol>
//	 *
//	 * @param node the new below node.
//	 * @throws NullPointerException          if the given {@code node} is null.
//	 * @throws IllegalStateException         if the given {@code node} has a node above it
//	 *                                       and is not this node.
//	 * @throws UnsupportedOperationException if this node does not allow changing the node
//	 *                                       below it; if an operation cannot be done due
//	 *                                       to the necessity of an unsupported operation.
//	 *                                       (see the above operations list)
//	 * @throws IllegalArgumentException      if this node rejects the given {@code node}
//	 *                                       as a node below it; if an operation cannot be
//	 *                                       done due to a node rejecting to relate to a
//	 *                                       node necessary to relate to due to some
//	 *                                       aspect of it. (see the above operations
//	 *                                       list)
//	 * @since 0.2.0 ~2021.02.14
//	 */
//	@Contract(mutates = "this,param")
//	void setBelow(@NotNull Node<T> node);
//
//	/**
//	 * <b>Set Right</b>
//	 * <br>
//	 * Set the given {@code node} to be the node next to this node and set this node to be
//	 * the node previous to the given {@code node}. Also, completely unlink the old next
//	 * node from this node and the old previous node from the given {@code node}.
//	 * <br><br>
//	 * Operations order:
//	 * <ol>
//	 *     <li>st next {@code this} to {@code node}</li>
//	 *     <li>remove previous {@code this.next} if equal {@code this}</li>
//	 *     <li>set previous {@code node} to {@code this} if not equal {@code this}</li>
//	 * </ol>
//	 *
//	 * @param node the new next node.
//	 * @throws NullPointerException          if the given {@code node} is null.
//	 * @throws IllegalStateException         if the given {@code node} has a node next to
//	 *                                       it and is not this node.
//	 * @throws UnsupportedOperationException if this node does not allow changing the node
//	 *                                       after it; if an operation cannot be done due
//	 *                                       to the necessity of an unsupported operation.
//	 *                                       (see the above operations list)
//	 * @throws IllegalArgumentException      if this node rejects the given {@code node}
//	 *                                       as a node after it; if an operation cannot be
//	 *                                       done due to a node rejecting to relate to a
//	 *                                       node necessary to relate to due to some
//	 *                                       aspect of it. (see the above operations
//	 *                                       list)
//	 * @since 0.2.0 ~2021.02.14
//	 */
//	@Contract(mutates = "this,param")
//	void setNext(@NotNull Node<T> node);
//
//	/**
//	 * <b>Set Left</b>
//	 * <br>
//	 * Set the given {@code node} to be the node previous to this node and set this node
//	 * to be the node next to the given {@code node}. Also, completely unlink the old
//	 * previous node from this node and the old next node from the given {@code node}.
//	 * <br><br>
//	 * Operations order:
//	 * <ol>
//	 *     <li>st previous {@code this} to {@code node}</li>
//	 *     <li>remove next {@code this.previous} if equal {@code this}</li>
//	 *     <li>set next {@code node} to {@code this} if not equal {@code this}</li>
//	 * </ol>
//	 *
//	 * @param node the new previous node.
//	 * @throws NullPointerException          if the given {@code node} is null.
//	 * @throws IllegalStateException         if the given {@code node} has a node previous
//	 *                                       to it and is not this node.
//	 * @throws UnsupportedOperationException if this node does not allow changing the node
//	 *                                       before it; if an operation cannot be done due
//	 *                                       to the necessity of an unsupported operation.
//	 *                                       (see the above operations list)
//	 * @throws IllegalArgumentException      if this node rejects the given {@code node}
//	 *                                       as a node before it; if an operation cannot
//	 *                                       be done due to a node rejecting to relate to
//	 *                                       a node necessary to relate to due to some
//	 *                                       aspect of it. (see the above operations
//	 *                                       list)
//	 * @since 0.2.0 ~2021.02.14
//	 */
//	@Contract(mutates = "this,param")
//	void setPrevious(@NotNull Node<T> node);
//
//	/**
//	 * Set the value of this node to be the given {@code value}.
//	 *
//	 * @param value the value of this node.
//	 * @throws NullPointerException          if the given {@code value} is null.
//	 * @throws UnsupportedOperationException if this node does not allow or does not
//	 *                                       support changing its value.
//	 * @throws IllegalArgumentException      if this node does not accept the given {@code
//	 *                                       value} for some aspect of it.
//	 * @since 0.2.0 ~2021.02.24
//	 */
//	@Contract(mutates = "this")
//	void setValue(@NotNull T value);
//}
////	/**
////	 * Assign the given {@code value} to the given {@code name} and {@code type} as a
////	 * value in this node.
////	 *
////	 * @param name  the name to assign the value to.
////	 * @param type  the type to assign the value to.
////	 * @param value the value.
////	 * @param <T>   the type of the value.
////	 * @throws NullPointerException if the given {@code name} or {@code type} or {@code
////	 *                              value} is null.
////	 * @throws ClassCastException   if the given {@code value} is not an instance of the
////	 *                              given {@code type}.
////	 * @since 0.2.0 ~2021.02.24
////	 */
////	<T> void put(String name, Class<T> type, T value);
//
////	interface Entity {
////	}
////	/**
////	 * The default comparator of a node. First, compares the documents. Then compares
////	 * references. Performance-wise, it is better to provide nodes that {@link
////	 * Node#hasDocument() have documents} and {@link Node#hasReference() have
////	 * references}.
////	 *
////	 * @since 0.2.0 ~2021.02.15
////	 */
////	Comparator<Node> COMPARATOR =
////			Comparator.<Node, Document>comparing(
////					node ->
////							node.hasDocument() ?
////							node.getDocument() :
////							node.hasReference() ?
////							node.getReference().document() :
////							node.hasSketch() ?
////							node.getSketch().reference().document() :
////							node.hasElement() && node.getElement().reference() != null ?
////							node.getElement().reference().document() :
////							null,
////					(a, b) ->
////							a == null ?
////							b == null ?
////							0 :
////							-1 :
////							b == null ?
////							1 :
////							Document.COMPARATOR.compare(a, b)
////			).thenComparing(
////					node ->
////							node.hasReference() ?
////							node.getReference() :
////							node.hasSketch() ?
////							node.getSketch().reference() :
////							node.hasElement() ?
////							node.getElement().reference() :
////							null,
////					(a, b) ->
////							a == null ?
////							b == null ?
////							0 :
////							-1 :
////							b == null ?
////							1 :
////							Reference.COMPARATOR.compare(a, b)
////			);
//
////	/**
////	 * The sketch of this node.
////	 *
////	 * @return the sketch of this node.
////	 * @since 0.2.0 ~2021.02.14
////	 */
////	Sketch getSketch();
//
////	/**
////	 * Return {@code true} if this node currently has a document set to it.
////	 *
////	 * @return true, if this node has a document.
////	 * @since 0.2.0 ~2021.02.14
////	 */
////	boolean hasElement();
//
////	/**
////	 * Get the element of this node.
////	 *
////	 * @return the element of this node. Or {@code null} if this node has no element yet.
////	 * @since 0.2.0 ~2021.02.14
////	 */
////	Element getElement();
////
////	String getMeta(String name);
//
////
////	/**
////	 * Remove the element of this node.
////	 *
////	 * @throws UnsupportedOperationException if this node does not support changing its
////	 *                                       element.
////	 * @since 0.2.0 ~2021.02.14
////	 */
////	void removeElement();
////
////	/**
////	 * Set the sketch of this node to be the given {@code sketch}.
////	 *
////	 * @param sketch the new sketch.
////	 * @throws NullPointerException          if the given {@code sketch} is null.
////	 * @throws UnsupportedOperationException if this node does not support changing its
////	 *                                       sketch.
////	 * @since 0.2.0 ~2021.02.14
////	 */
////	void setSketch(Sketch sketch);
////
////	/**
////	 * Remove the sketch of this node.
////	 *
////	 * @throws UnsupportedOperationException if this node does not support changing its
////	 *                                       sketch.
////	 * @since 0.2.0 ~2021.02.14
////	 */
////	void removeSketch();
//
////
////	/**
////	 * Return {@code true} if this node currently has a sketch set to it.
////	 *
////	 * @return true, if this node has a sketch.
////	 * @since 0.2.0 ~2021.02.14
////	 */
////	boolean hasSketch();
////
////	/**
////	 * Return {@code true} if this node allow mutating its child node.
////	 *
////	 * @return true, if this node allow changing its child node.
////	 * @since 0.2.0 ~2021.02.14
////	 */
////	boolean mutableChild();
////
////	/**
////	 * Return {@code true} if this node allow mutating its document.
////	 *
////	 * @return true, if this node allow changing its document.
////	 * @since 0.2.0 ~2021.02.14
////	 */
////	boolean mutableDocument();
////
////	/**
////	 * Return {@code true} if this node allow mutating its element.
////	 *
////	 * @return true, if this node allow changing its element.
////	 * @since 0.2.0 ~2021.02.14
////	 */
////	boolean mutableElement();
////
////	/**
////	 * Return {@code true} if this node allow mutating its next node.
////	 *
////	 * @return true, if this node allow changing its next node.
////	 * @since 0.2.0 ~2021.02.14
////	 */
////	boolean mutableNext();
////
////	/**
////	 * Return {@code true} if this node allow mutating its parent node.
////	 *
////	 * @return true, if this node allow changing its parent node.
////	 * @since 0.2.0 ~2021.02.14
////	 */
////	boolean mutableParent();
////
////	/**
////	 * Return {@code true} if this node allow mutating its previous node.
////	 *
////	 * @return true, if this node allow changing its previous node.
////	 * @since 0.2.0 ~2021.02.14
////	 */
////	boolean mutablePrevious();
////
////	/**
////	 * Return {@code true} if this node allow mutating its reference.
////	 *
////	 * @return true, if this node allow changing its reference.
////	 * @since 0.2.0 ~2021.02.14
////	 */
////	boolean mutableReference();
////
////	/**
////	 * Return {@code true} if this node allow mutating its sketch.
////	 *
////	 * @return true, if this node allow changing its sketch.
////	 * @since 0.2.0 ~2021.02.14
////	 */
////	boolean mutableSketch();
////	/**
////	 * The nodes default comparator. The comparator will throw a {@link
////	 * NullPointerException} if a node that does not {@link #hasReference() has a
////	 * reference} was provided.
////	 *
////	 * @since 0.2.0 ~2021.02.15
////	 */
////	Comparator<Node> COMPARATOR = Comparator.comparing(Node::getReference);
////
////	void putMeta(String name, String value);
////
////	/**
////	 * Set the element of this node to be the given {@code element}.
////	 *
////	 * @param element the new element.
////	 * @throws NullPointerException          if the given {@code element} is null.
////	 * @throws UnsupportedOperationException if this node does not support changing its
////	 *                                       element.
////	 * @since 0.2.0 ~2021.02.14
////	 */
////	void setElement(Element element);
//
////
////	/**
////	 * Remove the document of this node.
////	 *
////	 * @throws UnsupportedOperationException if this node does not support changing its
////	 *                                       document.
////	 * @since 0.2.0 ~2021.02.14
////	 */
////	void removeDocument();
////
////	/**
////	 * Remove the reference of this node.
////	 *
////	 * @throws UnsupportedOperationException if this node does not support changing its
////	 *                                       reference.
////	 * @since 0.2.0 ~2021.02.14
////	 */
////	void removeReference();
//
////
////	/**
////	 * Set the reference of this node to be the given {@code reference}.
////	 *
////	 * @param reference the new reference.
////	 * @throws NullPointerException          if the given {@code reference} is null.
////	 * @throws UnsupportedOperationException if this node does not support changing its
////	 *                                       reference.
////	 * @since 0.2.0 ~2021.02.14
////	 */
////	void setReference(Reference reference);//
//////	/**
//////	 * Set the document of this node to be the given {@code document}.
//////	 *
//////	 * @param document the new document.
//////	 * @throws NullPointerException          if the given {@code document} is null.
//////	 * @throws UnsupportedOperationException if this node does not support changing its
//////	 *                                       document.
//////	 * @since 0.2.0 ~2021.02.14
//////	 */
//////	void setDocument(Document document);
////
////	/**
////	 * Return {@code true} if this node currently has a document set to it.
////	 *
////	 * @return true, if this node has a document.
////	 * @since 0.2.0 ~2021.02.14
////	 */
////	boolean hasDocument();
////
////	/**
////	 * Return {@code true} if this node currently has a reference set to it.
////	 *
////	 * @return true, if this node has a reference.
////	 * @since 0.2.0 ~2021.02.14
////	 */
////	boolean hasReference();
////	@Override
////	default Iterator<Node> iterator() {
////		//noinspection ALL
////		return new Iterator<Node>() {
////			/**
////			 * The current/last child iterated.
////			 *
////			 * @since 0.2.0 ~2021.02.17
////			 */
////			protected Node child = Node.this.getChild();
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
////
////	/**
////	 * Returns a spliterator iterating over the children nodes associated to this node.
////	 * The children nodes considered children to this node are the {@link #getNext()}
////	 * chained nodes of the {@link #getChild() child} node of this node.
////	 * <br>
////	 * The bellow is a visualization over the iteration using the returned spliterator.
////	 * <br><br>
////	 * <pre>
////	 *     for(Node child = this.getChild(); child != null; child = child.getNext())
////	 *     		;
////	 * </pre>
////	 * <br>
////	 * Note that the returned spliterator will exactly do an implementation of the above
////	 * code. So, when a change in relations occur to the inner children. The spliterator
////	 * will not know about that and it will simply continue believing that the next
////	 * children after the child it is pointing at (at a specific time) are children of
////	 * this node.
////	 *
////	 * @return a spliterator iterating over the children of this node.
////	 * @since 0.2.0 ~2021.02.19
////	 */
////	@Override
////	default Spliterator<Node> spliterator() {
////		return Spliterators.spliteratorUnknownSize(
////				this.iterator(),
////				Spliterator.NONNULL |
////				Spliterator.ORDERED
////		);
////	}
////
////	/**
////	 * Returns a stream over the children nodes associated to this node. The children
////	 * nodes considered children to this node are the {@link #getNext()} chained nodes of
////	 * the {@link #getChild() child} node of this node.
////	 * <br>
////	 * The bellow is a visualization over the streaming using the returned stream.
////	 * <br><br>
////	 * <pre>
////	 *     for(Node child = this.getChild(); child != null; child = child.getNext())
////	 *     		;
////	 * </pre>
////	 * <br>
////	 * Note that the returned stream will exactly do an implementation of the above code.
////	 * So, when a change in relations occur to the inner children. The stream will not
////	 * know about that and it will simply continue believing that the next children after
////	 * the child it is pointing at (at a specific time) are children of this node.
////	 *
////	 * @return a stream over the children of this node.
////	 * @since 0.2.0 ~2021.02.19
////	 */
////	default Stream<Node> stream() {
////		return StreamSupport.stream(
////				this.spliterator(),
////				false
////		);
////	}
//
////	/**
////	 * Add the given meta {@code type} if it does not exist in this node.
////	 *
////	 * @param type the meta type to be added.
////	 * @throws NullPointerException if the given {@code type} is null.
////	 * @since 0.2.0 ~2021.02.19
////	 */
////	void addMeta(String type);
////
////	/**
////	 * Add the given {@code element} to the given meta {@code type} in this node.
////	 *
////	 * @param type the meta type to add the element to.
////	 * @param element the element to be added to the meta type.
////	 * @throws NullPointerException if the given {@code type} or {@code element} is null.
////	 * @since 0.2.0 ~2021.02.19
////	 */
////	void addMeta(String type, String element);
////
////	/**
////	 * Remove the entire type with the given {@code type} name. Any element in that type
////	 * will be removed.
////	 *
////	 * @param type the name of the type to be removed.
////	 * @since 0.2.0 ~2021.02.19
////	 */
////	void removeMeta(String type);
////
////	/**
////	 * Remove the given {@code element} from the type with the given {@code type} name.
////	 *
////	 * @param type the name of the type to remove the element from.
////	 * @param element the element to be removed.
////	 * @throws NullPointerException if the given {@code type} or {@code element} is null.
////	 * @since 0.2.0 ~2021.02.19
////	 */
////	void removeMeta(String type, String element);
////
////	/**
////	 * Check if a type with the given {@code type} name exist in this node.
////	 *
////	 * @param type the name of the type to be checked.
////	 * @throws NullPointerException if the given {@code type} is null.
////	 * @since 0.2.0 ~2021.02.19
////	 */
////	void hasMeta(String type);
////
////	/**
////	 * Check if the given {@code element} exist in a type with the given {@code type} name in this node.
////	 *
////	 * @param type the name of the type to be checked.
////	 * @param element the element to be checked.
////	 * @throws NullPointerException if the given {@code type} or {@code element} is null.
////	 * @since 0.2.0 ~2021.02.19
////	 */
////	void hasMeta(String type, String element);
////
////	/**
////	 * Get all the elements in the type with the given {@code type} name.
////	 *
////	 * @param type the name of the type to get the elements in it.
////	 * @return an unmodifiable view containing the elements at the type with the given {@code type} name. Or an empty view if none.
////	 * @throws NullPointerException if the given {@code type} is null.
////	 * @since 0.2.0 ~2021.02.19
////	 */
////	Set<String> getMeta(String type);
////
////	/**
////	 * Get the element
////	 *
////	 * @param type
////	 * @param element
////	 * @return
////	 */
////	String getMeta(String type, String element);
//
////
////	/**
////	 * The reference of this node.
////	 *
////	 * @return the reference of this node.
////	 * @since 0.2.0 ~2021.02.14
////	 */
////	Reference reference();
//
////
////	/**
////	 * Return the document of this node.
////	 *
////	 * @return the document of this node.
////	 * @since 0.2.0 ~2021.02.14
////	 */
////	Document document();
//
////
////	/**
////	 * If not assigned, assign the result of the given  {@code supplier} to the given
////	 * {@code name} and {@code type} as a value in this node and return that value.
////	 * Otherwise, return the assigned value.
////	 *
////	 * @param name     the name to assign the value to.
////	 * @param type     the type to assign the value to.
////	 * @param supplier a supplier to get the new value only if needed.
////	 * @param <T>      the type of the value.
////	 * @return the currently assigned value if exist. Otherwise, the value from evaluating
////	 * 		the given {@code supplier}.
////	 * @throws NullPointerException if the given {@code name} or {@code type} or {@code
////	 *                              supplier} is null.
////	 * @throws ClassCastException   if the given {@code supplier} has returned a value
////	 *                              that is not an instance of the given {@code type}.
////	 * @since 0.2.0 ~2021.02.24
////	 */
////	<T> T compute(String name, Class<T> type, Supplier<T> supplier);
//
////
////	/**
////	 * Safely, unlink the given {@code node} and the node above it.
////	 * <br><br>
////	 * <pre>
////	 *     if (node.above.below == node)
////	 *         node.above.below = null
////	 *     node.above = null
////	 * </pre>
////	 *
////	 * @param node the node to safely remove the above node of it.
////	 * @throws NullPointerException          if the given {@code node} is null.
////	 * @throws UnsupportedOperationException if a node does not allow modifying a
////	 *                                       necessary relation that should be modified
////	 *                                       for the operation to be done.
////	 * @since 0.2.0 ~2021.02.23
////	 */
////	static void removeAbove(Node<?> node) {
////		Objects.requireNonNull(node, "node");
////		Node<?> above = node.getAbove();
////
////		if (above != null && above.getBelow() == node)
////			above.removeBelow();
////		node.removeAbove();
////	}
////
////	/**
////	 * Safely, unlink the given {@code node} and the node below it.
////	 * <br><br>
////	 * <pre>
////	 *     if (node.below.above == node)
////	 *         node.below.above = null
////	 *     node.below = null
////	 * </pre>
////	 *
////	 * @param node the node to safely remove the below node of it.
////	 * @throws NullPointerException          if the given {@code node} is null.
////	 * @throws UnsupportedOperationException if a node does not allow modifying a
////	 *                                       necessary relation that should be modified
////	 *                                       for the operation to be done.
////	 * @since 0.2.0 ~2021.02.23
////	 */
////	static void removeBelow(Node<?> node) {
////		Objects.requireNonNull(node, "node");
////		Node<?> below = node.getBelow();
////
////		if (below != null && below.getAbove() == node)
////			below.removeAbove();
////		node.removeBelow();
////	}
////
////	/**
////	 * Safely, unlink the given {@code node} and the node after it.
////	 * <br><br>
////	 * <pre>
////	 *     if (node.next.previous == node)
////	 *         node.next.previous = null
////	 *     node.next = null
////	 * </pre>
////	 *
////	 * @param node the node to safely remove the next node of it.
////	 * @throws NullPointerException          if the given {@code node} is null.
////	 * @throws UnsupportedOperationException if a node does not allow modifying a
////	 *                                       necessary relation that should be modified
////	 *                                       for the operation to be done.
////	 * @since 0.2.0 ~2021.02.23
////	 */
////	static void removeNext(Node<?> node) {
////		Objects.requireNonNull(node, "node");
////		Node<?> next = node.getNext();
////
////		if (next != null && next.getPrevious() == node)
////			next.removePrevious();
////		node.removeNext();
////	}
////
////	/**
////	 * Safely, unlink the given {@code node} and the node before it.
////	 * <br><br>
////	 * <pre>
////	 *     if (node.previous.next == node)
////	 *         node.previous.next = null
////	 *     node.previous = null
////	 * </pre>
////	 *
////	 * @param node the node to safely remove the previous node of it.
////	 * @throws NullPointerException          if the given {@code node} is null.
////	 * @throws UnsupportedOperationException if a node does not allow modifying a
////	 *                                       necessary relation that should be modified
////	 *                                       for the operation to be done.
////	 * @since 0.2.0 ~2021.02.23
////	 */
////	static void removePrevious(Node<?> node) {
////		Objects.requireNonNull(node, "node");
////		Node<?> previous = node.getPrevious();
////
////		if (previous != null && previous.getNext() == node)
////			previous.removeNext();
////		node.removePrevious();
////	}
////
////	/**
////	 * Safely, link the given {@code other} as the above node of the given {@code node}.
////	 * <br><br>
////	 * <pre>
////	 *     if (node.above.below == node)
////	 *         node.above.below = null
////	 *     if (other.below.above == other)
////	 *         other.below.above = null
////	 *     node.above = other
////	 *     other.below = node
////	 * </pre>
////	 *
////	 * @param node  the below node.
////	 * @param other the above node.
////	 * @param <T>   the tpe of the values of the nodes.
////	 * @throws NullPointerException          if the given {@code node} or {@code other} is
////	 *                                       null.
////	 * @throws UnsupportedOperationException if a node does not allow modifying a
////	 *                                       necessary relation that should be modified
////	 *                                       for the operation to be done.
////	 * @since 0.2.0 ~2021.02.23
////	 */
////	static <T> void setAbove(Node<T> node, Node<T> other) {
////		Objects.requireNonNull(node, "node");
////		Objects.requireNonNull(other, "other");
////		Node<T> above = node.getAbove();
////		Node<T> below = other.getBelow();
////
////		if (above != null && above.getBelow() == above)
////			above.removeBelow();
////		if (below != null && below.getAbove() == other)
////			below.removeAbove();
////		node.setAbove(other);
////		other.setBelow(node);
////	}
////
////	/**
////	 * Safely, link the given {@code other} as the below node of the given {@code node}.
////	 * <br><br>
////	 * <pre>
////	 *     if (node.below.above == node)
////	 *         node.below.above = null
////	 *     if (other.above.below == other)
////	 *         other.above.below = null
////	 *     node.below = other
////	 *     other.above = node
////	 * </pre>
////	 *
////	 * @param node  the above node.
////	 * @param other the below node.
////	 * @param <T>   the tpe of the values of the nodes.
////	 * @throws NullPointerException          if the given {@code node} or {@code other} is
////	 *                                       null.
////	 * @throws UnsupportedOperationException if a node does not allow modifying a
////	 *                                       necessary relation that should be modified
////	 *                                       for the operation to be done.
////	 * @since 0.2.0 ~2021.02.23
////	 */
////	static <T> void setBelow(Node<T> node, Node<T> other) {
////		Objects.requireNonNull(node, "node");
////		Objects.requireNonNull(other, "other");
////		Node<T> below = node.getBelow();
////		Node<T> above = other.getAbove();
////
////		if (below != null && below.getAbove() == node)
////			below.removeAbove();
////		if (above != null && above.getBelow() == other)
////			above.removeBelow();
////		node.setBelow(other);
////		other.setAbove(node);
////	}
////
////	/**
////	 * Safely, link the given {@code other} as the next node of the given {@code node}.
////	 * <br><br>
////	 * <pre>
////	 *     if (node.next.previous == node)
////	 *         node.next.previous = null
////	 *     if (other.previous.next == other)
////	 *         other.previous.next = null
////	 *     node.next = other
////	 *     other.previous = node
////	 * </pre>
////	 *
////	 * @param node  the previous node.
////	 * @param other the next node.
////	 * @param <T>   the tpe of the values of the nodes.
////	 * @throws NullPointerException          if the given {@code node} or {@code other} is
////	 *                                       null.
////	 * @throws UnsupportedOperationException if a node does not allow modifying a
////	 *                                       necessary relation that should be modified
////	 *                                       for the operation to be done.
////	 * @since 0.2.0 ~2021.02.23
////	 */
////	static <T> void setNext(Node<T> node, Node<T> other) {
////		Objects.requireNonNull(node, "node");
////		Objects.requireNonNull(other, "other");
////		Node<T> next = node.getNext();
////		Node<T> previous = other.getPrevious();
////
////		if (next != null && next.getPrevious() == node)
////			next.removePrevious();
////		if (previous != null && previous.getNext() == other)
////			previous.removeNext();
////		node.setNext(other);
////		other.setPrevious(node);
////	}
////
////	/**
////	 * Safely, link the given {@code other} as the previous node of the given {@code
////	 * node}.
////	 * <br><br>
////	 * <pre>
////	 *     if (node.previous.next == node)
////	 *         node.previous.next = null
////	 *     if (other.next.previous == other)
////	 *         other.next.previous = null
////	 *     node.previous = other;
////	 *     other.next = node;
////	 * </pre>
////	 *
////	 * @param node  the next node.
////	 * @param other the previous node.
////	 * @param <T>   the tpe of the values of the nodes.
////	 * @throws NullPointerException          if the given {@code node} or {@code other} is
////	 *                                       null.
////	 * @throws UnsupportedOperationException if a node does not allow modifying a
////	 *                                       necessary relation that should be modified
////	 *                                       for the operation to be done.
////	 * @since 0.2.0 ~2021.02.23
////	 */
////	static <T> void setPrevious(Node<T> node, Node<T> other) {
////		Objects.requireNonNull(node, "node");
////		Objects.requireNonNull(other, "other");
////		Node<T> previous = node.getPrevious();
////		Node<T> next = other.getNext();
////
////		if (previous != null && previous.getNext() == node)
////			previous.removeNext();
////		if (next != null && next.getPrevious() == other)
////			next.removePrevious();
////		node.setPrevious(other);
////		other.setNext(node);
////	}
//
////
////	/**
////	 * <b>Check Up</b>
////	 * <br>
////	 * Return {@code true} if this node is the first child of its parent.
////	 *
////	 * @return true, if this node has a node above it.
////	 * @since 0.2.0 ~2021.02.14
////	 */
////	boolean hasAbove();
////
////	/**
////	 * <b>Check Down</b>
////	 * <br>
////	 * Return {@code true} if this node currently has a child node set to it.
////	 *
////	 * @return true, if this node has a node below it.
////	 * @since 0.2.0 ~2021.02.14
////	 */
////	boolean hasBelow();
////
////	/**
////	 * <b>Check Right</b>
////	 * <br>
////	 * Return {@code true} if this node currently has a next node set to it.
////	 *
////	 * @return true, if this node has a node next to it.
////	 * @since 0.2.0 ~2021.02.14
////	 */
////	boolean hasNext();
////
////	/**
////	 * <b>Check Left</b>
////	 * <br>
////	 * Return {@code true} if this node currently has a previous node set to it.
////	 *
////	 * @return true, if this node has a node previous to it.
////	 * @since 0.2.0 ~2021.02.14
////	 */
////	boolean hasPrevious();
////
////	/**
////	 * Return {@code true} if this node currently has a value set to it.
////	 *
////	 * @return true, if this node has a value.
////	 * @since 0.2.0 ~2021.02.25
////	 */
////	boolean hasValue();
