/*
 *	Copyright 2021 Cufy
 *
 *	Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *	You may obtain a copy of the License at
 *
 *	    http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	See the License for the specific language governing permissions and
 *	limitations under the License.
 */
package org.jamplate.model;

import cufy.util.HashNode;
import cufy.util.Node;
import cufy.util.Nodes;
import cufy.util.polygon.Tetragon;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * A tree is a point in a background structure of sketches that hold the variables of a
 * runtime or a text component.
 * <br>
 * The background structure is working like magic and the user cannot interact directly
 * with it.
 * <br>
 * A tree structure can only be modified throw the {@link #offer(Tree)} method ony any
 * tree in it. Any tree that get {@link #offer(Tree) offered} into a structure of another
 * tree will be removed from its previous structure.
 * <br>
 * A structure cannot have any clashing sketches or sketches that does not fit their
 * parent or sketches that breaks the order of their neighboring sketches in it and all
 * the sketches in it will be organized implicitly.
 * <br>
 * The tree class is not thead safe and multiple threads modifying the same tree structure
 * can make that structure corrupted. The corruption due to two thread modifying the same
 * tree structure is undefined. Aside from that, two or more threads just reading the tree
 * structure is totally fine. Also, one thread modifying a tree structure while the other
 * threads just reading it will not corrupt the structure and will only confuse the other
 * threads because random sketches will be moved around while those threads are reading.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2020.12.25
 */
@SuppressWarnings("OverlyComplexClass")
public final class Tree implements Iterable<Tree>, Serializable {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 3068214324610853826L;

	/**
	 * The document this tree is from.
	 *
	 * @since 0.2.0 ~2021.05.17
	 */
	@NotNull
	private final Document document;
	/**
	 * The node representing this tree in an absolute relationships (based on the
	 * reference of this tree).
	 *
	 * @since 0.2.0 ~2021.05.14
	 */
	@NotNull
	private final Node<Tree> node = new HashNode<>(this);
	/**
	 * The reference of this tree.
	 *
	 * @since 0.2.0 ~2021.05.14
	 */
	@NotNull
	private final Reference reference;

	/**
	 * The sketch set to this tree.
	 *
	 * @since 0.2.0 ~2021.05.17
	 */
	@NotNull
	private Sketch sketch;

	/**
	 * Construct a new tree for the whole given {@code document}.
	 *
	 * @param document the document the constructed tree is for.
	 * @throws NullPointerException  if the given {@code document} is null.
	 * @throws java.io.IOError       if any I/O error occurred while reading the given
	 *                               {@code document} to get its length.
	 * @throws DocumentNotFoundError if the given {@code document} is not available for
	 *                               reading.
	 * @since 0.2.0 ~2021.05.17
	 */
	public Tree(@NotNull Document document) {
		Objects.requireNonNull(document, "document");
		this.document = document;
		this.reference = new Reference(0, document.read().length());
		this.sketch = new Sketch();
	}

	/**
	 * Construct a new tree for the whole given {@code document}.
	 *
	 * @param document the document the constructed tree is for.
	 * @param sketch   the initial sketch set to this tree.
	 * @throws NullPointerException  if the given {@code document} is null.
	 * @throws java.io.IOError       if any I/O error occurred while reading the given
	 *                               {@code document} to get its length.
	 * @throws DocumentNotFoundError if the given {@code document} is not available for
	 *                               reading.
	 * @since 0.2.0 ~2021.05.17
	 */
	public Tree(@NotNull Document document, @NotNull Sketch sketch) {
		Objects.requireNonNull(document, "document");
		this.document = document;
		this.reference = new Reference(0, document.read().length());
		this.sketch = sketch;
	}

	/**
	 * Construct a new tree with the given {@code reference}.
	 *
	 * @param document  the document of the constructed tree.
	 * @param reference the reference of the constructed tree.
	 * @throws NullPointerException if the given {@code document} or {@code reference} is
	 *                              null.
	 * @since 0.2.0 ~2021.05.15
	 */
	public Tree(@NotNull Document document, @NotNull Reference reference) {
		Objects.requireNonNull(document, "document");
		Objects.requireNonNull(reference, "reference");
		this.document = document;
		this.reference = reference;
		this.sketch = new Sketch();
	}

	/**
	 * Construct a new tree with the given {@code reference} and the given {@code
	 * sketch}.
	 *
	 * @param document  the document of the constructed tree.
	 * @param reference the reference of the contracted tree.
	 * @param sketch    the initial sketch set to this tree.
	 * @throws NullPointerException if the given {@code document} or {@code reference} or
	 *                              {@code sketch} is null.
	 * @since 0.2.0 ~2021.05.15
	 */
	public Tree(@NotNull Document document, @NotNull Reference reference, @NotNull Sketch sketch) {
		Objects.requireNonNull(document, "document");
		Objects.requireNonNull(reference, "reference");
		Objects.requireNonNull(sketch, "sketch");
		this.document = document;
		this.reference = reference;
		this.sketch = sketch;
	}

	@Contract(value = "null->false", pure = true)
	@Override
	public boolean equals(@Nullable Object object) {
		return super.equals(object);
	}

	@Contract(pure = true)
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @implNote the returned iterator will iterate over the current known children. Any
	 * 		changes to the children will make the iterator's behaviour undefined.
	 * @since 0.2.0 ~2021.05.17
	 */
	@NotNull
	@Override
	public Iterator<Tree> iterator() {
		//noinspection ReturnOfInnerClass
		return new Iterator<Tree>() {
			/**
			 * The next tree to be returned.
			 *
			 * @since 0.2.0 ~2021.05.17
			 */
			@Nullable
			private Tree next = Tree.this.getChild();
			/**
			 * The previous tree that has been returned.
			 *
			 * @since 0.2.0 ~2021.05.17
			 */
			@Nullable
			private Tree previous;

			@Override
			public boolean hasNext() {
				return this.next != null;
			}

			@Override
			public Tree next() {
				Tree next = this.next;

				if (next == null)
					throw new NoSuchElementException("next");

				this.previous = next;
				this.next = next.getNext();
				return next;
			}

			@Override
			public void remove() {
				Tree previous = this.previous;

				if (previous == null)
					throw new IllegalStateException("remove");

				previous.remove();
				this.previous = null;
			}
		};
	}

	@NotNull
	@Override
	public String toString() {
		return this.sketch + " " + this.reference;
	}

	/**
	 * Remove all the children of this tree without removing the structure between them.
	 * <br>
	 * This method will unlink the link between this tree and its first child. With that,
	 * the first child and the other children will have no parent. But, the links between
	 * the children will not be broken nor the links between the children and the grand
	 * children.
	 *
	 * @since 0.2.0 ~2021.05.17
	 */
	@Contract(mutates = "this")
	public void clear() {
		this.node.remove(Tetragon.BOTTOM);
	}

	/**
	 * Get the document of this tree.
	 *
	 * @return the document of this tree.
	 * @since 0.2.0 ~2021.05.17
	 */
	@NotNull
	@Contract(pure = true)
	public Document document() {
		return this.document;
	}

	/**
	 * Get the first child tree of this tree.
	 *
	 * @return the first tree in this tree. Or {@code null} if this tree has no children.
	 * @since 0.2.0 ~2021.05.17
	 */
	@Nullable
	@Contract(pure = true)
	public Tree getChild() {
		Node<Tree> child = this.node.get(Tetragon.BOTTOM);
		return child == null ? null : child.get();
	}

	/**
	 * Get the tree after this tree.
	 *
	 * @return the tree after this tree. Or {@code null} if this tree is the last tree.
	 * @since 0.2.0 ~2021.05.17
	 */
	@Nullable
	@Contract(pure = true)
	public Tree getNext() {
		Node<Tree> end = this.node.get(Tetragon.END);
		return end == null ? null : end.get();
	}

	/**
	 * Get the tree containing this tree.
	 *
	 * @return the parent tree of this tree. Or {@code null} if this tree has no parent.
	 * @since 0.2.0 ~2021.05.17
	 */
	@Nullable
	@Contract(pure = true)
	public Tree getParent() {
		Node<Tree> headTop = Nodes
				.tail(Tetragon.START, this.node)
				.get(Tetragon.TOP);
		return headTop == null ? null : headTop.get();
	}

	/**
	 * Get the tree before this tree.
	 *
	 * @return the tree before this tree. Or {@code null} if this tree is the first tree.
	 * @since 0.2.0 ~2021.05.17
	 */
	@Nullable
	@Contract(pure = true)
	public Tree getPrevious() {
		Node<Tree> start = this.node.get(Tetragon.START);
		return start == null ? null : start.get();
	}

	/**
	 * Return the sketch assigned to this tree.
	 *
	 * @return the sketch of this tree.
	 * @since 0.2.0 ~2021.05.17
	 */
	@NotNull
	@Contract(pure = true)
	public Sketch getSketch() {
		return this.sketch;
	}

	/**
	 * Offer the given {@code tree} to the structure of this tree. The given {@code tree}
	 * will be removed from its structure then put to the proper place in the structure of
	 * this tree.
	 * <br>
	 * If failed to insert the given {@code tree} because of an {@link
	 * IllegalTreeException}, then the method will exit without anything changed.
	 *
	 * @param tree the tree to be added.
	 * @throws NullPointerException     if the given {@code tree} is null.
	 * @throws TreeOutOfBoundsException if the given {@code tree} does not fit in the
	 *                                  parent of this tree.
	 * @throws TreeTakeoverException    if the given {@code tree} has the same range as a
	 *                                  tree in the structure of this tree.
	 * @throws TreeClashException       if the given {@code tree} clashes with another
	 *                                  tree in the structure of this tree.
	 * @since 0.2.0 ~2021.05.14
	 */
	@Contract(mutates = "this,param")
	public void offer(@NotNull Tree tree) {
		Objects.requireNonNull(tree, "tree");
		switch (Intersection.compute(this, tree)) {
			case AHEAD:
			case BEHIND:
			case CONTAINER:
				this.offerParent(tree);
				return;
			case START:
			case FRAGMENT:
			case END:
				this.offerChild(tree);
				return;
			case AFTER:
			case NEXT:
				this.offerNext(tree);
				return;
			case BEFORE:
			case PREVIOUS:
				this.offerPrevious(tree);
				return;
			case SAME:
				throw new TreeTakeoverException("Sketch takeover");
			case OVERFLOW:
			case UNDERFLOW:
				throw new TreeClashException("Sketch clash");
			default:
				throw new InternalError();
		}
	}

	/**
	 * Cleanly remove this tree from the structure it is on.
	 *
	 * @since 0.2.0 ~2021.05.14
	 */
	@Contract(mutates = "this")
	public void pop() {
		Node<Tree> top = this.node.get(Tetragon.TOP);
		Node<Tree> start = this.node.get(Tetragon.START);
		Node<Tree> end = this.node.get(Tetragon.END);
		Node<Tree> bottom = this.node.get(Tetragon.BOTTOM);

		assert top == null || start == null;

		if (bottom != null)
			if (start != null)
				//start -> bottom
				start.put(Tetragon.END, bottom);
			else if (top != null)
				//top |> bottom
				top.put(Tetragon.BOTTOM, bottom);

		if (end != null)
			if (bottom != null)
				//bottomTail -> end
				Nodes.tail(Tetragon.END, bottom)
					 .put(Tetragon.END, end);
			else if (start != null)
				//start -> end
				start.put(Tetragon.END, end);
			else if (top != null)
				//top |> next
				top.put(Tetragon.BOTTOM, end);
	}

	/**
	 * Get the reference of this tree.
	 *
	 * @return the reference of this tree.
	 * @since 0.2.0 ~2021.05.14
	 */
	@NotNull
	@Contract(pure = true)
	public Reference reference() {
		return this.reference;
	}

	/**
	 * Remove this tree from its parent, the tree before it and the tree after it.
	 *
	 * @since 0.2.0 ~2021.05.17
	 */
	@Contract(mutates = "this")
	public void remove() {
		Node<Tree> top = this.node.get(Tetragon.TOP);
		Node<Tree> start = this.node.get(Tetragon.START);
		Node<Tree> end = this.node.get(Tetragon.END);

		assert top == null || start == null;

		if (start != null)
			if (end != null)
				//start -> end
				start.put(Tetragon.END, end);
			else
				//start
				start.remove(Tetragon.END);
		else if (top != null)
			if (end != null)
				//top |> end
				top.put(Tetragon.BOTTOM, end);
			else
				//top
				top.remove(Tetragon.BOTTOM);
		else if (end != null)
			//end
			end.remove(Tetragon.START);
	}

	/**
	 * Set the sketch of this tree to be the given {@code sketch}. The previous tree of
	 * the given {@code sketch} will have a new sketch set to it.
	 *
	 * @param sketch the sketch to be set.
	 * @since 0.2.0 ~2021.05.17
	 */
	@Contract(mutates = "this,param")
	public void setSketch(@NotNull Sketch sketch) {
		Objects.requireNonNull(sketch, "sketch");
		this.sketch = sketch;
	}

	/**
	 * Try to place the given {@code tree} in the proper place between the children of
	 * this tree.
	 * <br>
	 * If failed to insert the given {@code tree} because of an {@link
	 * IllegalTreeException}, then the method will exit without anything changed.
	 *
	 * @param tree the offered tree.
	 * @throws NullPointerException     if the given {@code tree} is null.
	 * @throws TreeOutOfBoundsException if the given {@code tree} does not fit in this
	 *                                  tree.
	 * @throws TreeTakeoverException    if the given {@code tree} has the same range as a
	 *                                  tree in the structure of this tree.
	 * @throws TreeClashException       if the given {@code tree} clashes with another
	 *                                  tree in the structure of this tree.
	 * @since 0.2.0 ~2021.05.15
	 */
	@SuppressWarnings("OverlyLongMethod")
	@Contract(mutates = "this,param")
	private void offerChild(@NotNull Tree tree) {
		Objects.requireNonNull(tree, "tree");
		switch (Dominance.compute(this, tree)) {
			case PART:
				Node<Tree> bottom = this.node.get(Tetragon.BOTTOM);

				//case no children
				if (bottom == null) {
					//clean
					tree.pop();

					//this |> tree
					this.node.put(Tetragon.BOTTOM, tree.node);
					return;
				}

				//compare to the first
				switch (Intersection.compute(bottom.get(), tree)) {
					case BEFORE:
					case PREVIOUS:
						//clean
						tree.pop();

						//this |> tree -> bottom
						this.node.put(Tetragon.BOTTOM, tree.node);
						bottom.put(Tetragon.START, tree.node);
						return;
					case BEHIND:
					case CONTAINER:
					case AHEAD:
						//this |> tree |> bottom
						bottom.get().offerParent(tree);
						return;
					case START:
					case FRAGMENT:
					case END:
						//this |> bottom |> tree
						bottom.get().offerChild(tree);
						return;
					case AFTER:
					case NEXT:
						//this |> bottom -> tree
						bottom.get().offerNext(tree);
						return;
					case SAME:
						throw new TreeTakeoverException("Exact child bounds", bottom.get(), tree);
					case OVERFLOW:
					case UNDERFLOW:
						throw new TreeClashException("Clash with child", bottom.get(), tree);
					default:
						throw new InternalError();
				}
			case CONTAIN:
			case NONE:
				throw new TreeOutOfBoundsException("Out of the bounds", this, tree);
			case EXACT:
				throw new TreeTakeoverException("Exact bounds", this, tree);
			case SHARE:
				throw new TreeClashException("Clash with", this, tree);
			default:
				throw new InternalError();
		}
	}

	/**
	 * Try to place the given {@code tree} somewhere after this tree.
	 * <br>
	 * If failed to insert the given {@code tree} because of an {@link
	 * IllegalTreeException}, then the method will exit without anything changed.
	 *
	 * @param tree the offered tree.
	 * @throws NullPointerException     if the given {@code tree} is null.
	 * @throws IllegalTreeException     if the given {@code tree} is not after this tree.
	 * @throws TreeOutOfBoundsException if the given {@code tree} does not fit in the
	 *                                  parent of this tree.
	 * @throws TreeTakeoverException    if the given {@code tree} has the same range as a
	 *                                  tree in the structure of this tree.
	 * @throws TreeClashException       if the given {@code tree} clashes with another
	 *                                  tree in the structure of this tree.
	 * @since 0.2.0 ~2021.05.15
	 */
	@SuppressWarnings({"OverlyComplexMethod", "OverlyLongMethod"})
	@Contract(mutates = "this,param")
	private void offerNext(@NotNull Tree tree) {
		Objects.requireNonNull(tree, "tree");
		switch (Intersection.compute(this, tree)) {
			case AFTER:
			case NEXT:
				Node<Tree> end = this.node.get(Tetragon.END);

				//case at the end
				if (end == null) {
					Tree parent = this.getParent();

					if (parent == null) {
						//clean
						tree.pop();

						//this -> tree
						this.node.put(Tetragon.END, tree.node);
						return;
					}

					//validate compared to parent bounds
					switch (Dominance.compute(parent, tree)) {
						case PART:
							//clean
							tree.pop();

							//this -> tree
							this.node.put(Tetragon.END, tree.node);
							return;
						case SHARE:
							throw new TreeClashException("Clash with parent", parent, tree);
						case NONE:
							throw new TreeOutOfBoundsException("Out of the bounds of the parent", parent, tree);
						case EXACT:
						case CONTAIN:
							//actually, this must not happen, never!!!
						default:
							throw new InternalError();
					}
				}

				//compare to the next
				switch (Intersection.compute(end.get(), tree)) {
					case NEXT:
					case AFTER:
						//this -> end -> tree
						end.get().offerNext(tree);
						return;
					case BEHIND:
					case CONTAINER:
					case AHEAD:
						//this -> tree |> end
						end.get().offerParent(tree);
						return;
					case START:
					case FRAGMENT:
					case END:
						//this -> end |> tree
						end.get().offerChild(tree);
						return;
					case BEFORE:
					case PREVIOUS:
						//clean
						tree.pop();

						//this -> tree -> end
						this.node.put(Tetragon.END, tree.node);
						end.put(Tetragon.START, tree.node);
						return;
					case SAME:
						throw new TreeTakeoverException("Exact bounds", end.get(), tree);
					case OVERFLOW:
					case UNDERFLOW:
						throw new TreeClashException("Clash with next", end.get(), tree);
					default:
						throw new InternalError();
				}
			case START:
			case FRAGMENT:
			case END:
				//
			case AHEAD:
			case CONTAINER:
			case BEHIND:
				//
			case PREVIOUS:
			case BEFORE:
				throw new IllegalTreeException("Must be after", tree);
			case SAME:
				throw new TreeTakeoverException("Exact bounds", this, tree);
			case OVERFLOW:
			case UNDERFLOW:
				throw new TreeClashException("Clash with", this, tree);
			default:
				throw new InternalError();
		}
	}

	/**
	 * Try to place the given {@code tree} as the parent of this tree and its brothers
	 * that the given {@code tree} fits as their parent.
	 * <br>
	 * If failed to insert the given {@code tree} because of an {@link
	 * IllegalTreeException}, then the method will exit without anything changed.
	 *
	 * @param tree the offered tree.
	 * @throws NullPointerException     if the given {@code tree} is null.
	 * @throws IllegalTreeException     if the given {@code tree} is not valid as a parent
	 *                                  for this tree.
	 * @throws TreeOutOfBoundsException if the given {@code tree} does not fit in the
	 *                                  parent of this tree.
	 * @throws TreeTakeoverException    if the given {@code tree} has the same range as a
	 *                                  tree in the structure of this tree.
	 * @throws TreeClashException       if the given {@code tree} clashes with another
	 *                                  tree in the structure of this tree.
	 * @since 0.2.0 ~2021.05.15
	 */
	@SuppressWarnings({"DuplicatedCode", "OverlyComplexMethod", "OverlyLongMethod"})
	@Contract(mutates = "this,param")
	private void offerParent(@NotNull Tree tree) {
		Objects.requireNonNull(tree, "tree");
		//noinspection SwitchStatementDensity
		switch (Dominance.compute(this, tree)) {
			case CONTAIN:
				Node<Tree> bottom = this.node;
				Node<Tree> top;
				Node<Tree> previous = null;
				Node<Tree> next = null;

				//backwards (collecting `bottom` and `previous`)
				while0:
				while (true) {
					Node<Tree> n = bottom.get(Tetragon.START);

					if (n == null) {
						top = bottom.get(Tetragon.TOP);
						break;
					}

					switch (Dominance.compute(n.get(), tree)) {
						case CONTAIN:
							bottom = n;
							break;
						case NONE:
							previous = n;
							top = bottom.get(Tetragon.TOP);
							break while0;
						case SHARE:
							throw new TreeClashException("Clash with neighbor", n.get(), tree);
						case EXACT:
						case PART:
							//actually, this must not happen, never!!!
						default:
							throw new InternalError();
					}
				}
				//forward (collecting `next`)
				for0:
				for (
						Node<Tree> n = this.node;
						n != null;
						n = n.get(Tetragon.END)
				)
					switch (Dominance.compute(n.get(), tree)) {
						case CONTAIN:
							break;
						case NONE:
							next = n;
							break for0;
						case SHARE:
							throw new TreeClashException("Clash with neighbor", n.get(), tree);
						case EXACT:
						case PART:
							//actually, this must not happen, never!!!
						default:
							throw new InternalError();
					}

				//if this happened, then the structure is corrupted
				assert previous == null || top == null;

				if (previous == null) {
					if (top == null) {
						//clean
						tree.pop();

						//tree |> bottom; tree -> next
						bottom.put(Tetragon.TOP, tree.node);
						if (next != null)
							next.put(Tetragon.START, tree.node);
						return;
					}

					//compare to the top
					switch (Dominance.compute(top.get(), tree)) {
						case PART:
							//clean
							tree.pop();

							//top |> tree |> bottom; tree -> next
							top.put(Tetragon.BOTTOM, tree.node);
							bottom.put(Tetragon.TOP, tree.node);
							if (next != null)
								next.put(Tetragon.START, tree.node);
							return;
						case CONTAIN:
						case NONE:
							throw new TreeOutOfBoundsException("Out of the bounds of the parent", top.get(), tree);
						case EXACT:
							throw new TreeTakeoverException("Exact parent bounds", top.get(), tree);
						case SHARE:
							throw new TreeClashException("Clash with parent", top.get(), tree);
						default:
							throw new InternalError();
					}
				}

				//clean
				tree.pop();

				//previous -> tree |> bottom; tree -> next
				previous.put(Tetragon.END, tree.node);
				bottom.put(Tetragon.TOP, tree.node);
				if (next != null)
					next.put(Tetragon.START, tree.node);
				return;
			case SHARE:
				throw new TreeClashException("Clash with", this, tree);
			case NONE:
			case PART:
				throw new IllegalTreeException("Must fit", tree);
			case EXACT:
				throw new TreeTakeoverException("Exact bounds", this, tree);
			default:
				throw new InternalError();
		}
	}

	/**
	 * Try to place the given {@code tree} somewhere before this tree.
	 * <br>
	 * If failed to insert the given {@code tree} because of an {@link
	 * IllegalTreeException}, then the method will exit without anything changed.
	 *
	 * @param tree the offered tree.
	 * @throws NullPointerException     if the given {@code tree} is null.
	 * @throws IllegalTreeException     if the given {@code tree} is not before this
	 *                                  tree.
	 * @throws TreeOutOfBoundsException if the given {@code tree} does not fit in the
	 *                                  parent of this tree.
	 * @throws TreeTakeoverException    if the given {@code tree} has the same range as a
	 *                                  tree in the structure of this tree.
	 * @throws TreeClashException       if the given {@code tree} clashes with another
	 *                                  tree in the structure of this tree.
	 * @since 0.2.0 ~2021.05.15
	 */
	@SuppressWarnings({"OverlyComplexMethod", "OverlyLongMethod"})
	@Contract(mutates = "this,param")
	private void offerPrevious(@NotNull Tree tree) {
		Objects.requireNonNull(tree, "tree");
		switch (Intersection.compute(this, tree)) {
			case BEFORE:
			case PREVIOUS:
				Node<Tree> start = this.node.get(Tetragon.START);

				//case at the start
				if (start == null) {
					Tree parent = this.getParent();

					if (parent == null) {
						//clean
						tree.pop();

						//tree -> this
						this.node.put(Tetragon.START, tree.node);
						return;
					}

					//validate compared to parent bounds
					switch (Dominance.compute(parent, tree)) {
						case PART:
							//clean
							tree.pop();

							//parent |> tree -> this
							parent.node.put(Tetragon.BOTTOM, tree.node);
							this.node.put(Tetragon.START, tree.node);
							return;
						case SHARE:
							throw new TreeClashException("Clash with parent", parent, tree);
						case NONE:
							throw new TreeOutOfBoundsException("Out of the bounds of the parent", parent, tree);
						case EXACT:
						case CONTAIN:
							//actually, this must not happen, never!!!
						default:
							throw new InternalError();
					}
				}

				//compare to the previous
				switch (Intersection.compute(start.get(), tree)) {
					case PREVIOUS:
					case BEFORE:
						//tree -> start -> this
						start.get().offerPrevious(tree);
						return;
					case BEHIND:
					case CONTAINER:
					case AHEAD:
						//tree |> start; tree -> this
						start.get().offerParent(tree);
						return;
					case START:
					case FRAGMENT:
					case END:
						//start |> tree; start -> this
						start.get().offerChild(tree);
						return;
					case AFTER:
					case NEXT:
						//clean
						tree.pop();

						//start -> tree -> this
						start.put(Tetragon.END, tree.node);
						this.node.put(Tetragon.START, tree.node);
						return;
					case SAME:
						throw new TreeTakeoverException("Exact bounds", start.get(), tree);
					case OVERFLOW:
					case UNDERFLOW:
						throw new TreeClashException("Clash with previous", start.get(), tree);
					default:
						throw new InternalError();
				}
			case START:
			case FRAGMENT:
			case END:
				//
			case AHEAD:
			case CONTAINER:
			case BEHIND:
				//
			case NEXT:
			case AFTER:
				throw new IllegalTreeException("Must be before", tree);
			case SAME:
				throw new TreeTakeoverException("Exact bounds", this, tree);
			case OVERFLOW:
			case UNDERFLOW:
				throw new TreeClashException("Clash with", this, tree);
			default:
				throw new InternalError();
		}
	}
}
