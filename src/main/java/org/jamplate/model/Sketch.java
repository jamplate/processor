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

import java.io.IOError;
import java.io.Serializable;
import java.util.Objects;
import java.util.Properties;

/**
 * A sketch is a point in a background structure of sketches that hold the variables of a
 * runtime or a text component.
 * <br>
 * The background structure is working like magic and the user cannot interact directly
 * with it.
 * <br>
 * A sketch structure can only be modified throw the {@link #offer(Sketch)} method ony any
 * sketch in it. Any sketch that get {@link #offer(Sketch) offered} into a structure of
 * another sketch will be removed from its previous structure.
 * <br>
 * A structure cannot have any clashing sketches or sketches that does not fit their
 * parent or sketches that breaks the order of their neighboring sketches in it and all
 * the sketches in it will be organized implicitly.
 * <br>
 * The sketch's background structure operations are thread safe. But, other sketch
 * operations are not.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2020.12.25
 */
@SuppressWarnings("OverlyComplexClass")
public final class Sketch implements Serializable {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 3068214324610853826L;

	/**
	 * The node representing this sketch in an absolute relationships (based on the
	 * reference of this sketch).
	 *
	 * @since 0.2.0 ~2021.05.14
	 */
	@NotNull
	private final Node<Sketch> node = new HashNode<>(this);

	/**
	 * The properties of this sketch.
	 *
	 * @since 0.2.0 ~2021.05.14
	 */
	@NotNull
	private final Properties properties;
	/**
	 * The reference of this sketch.
	 *
	 * @since 0.2.0 ~2021.05.14
	 */
	@NotNull
	private final Reference reference;

	/**
	 * The current document of this sketch.
	 *
	 * @since 0.2.0 ~2021.05.14
	 */
	@NotNull
	private Document document;
	/**
	 * The current name of this sketch.
	 *
	 * @since 0.2.0 ~2021.05.14
	 */
	@NotNull
	private Name name;

	/**
	 * Construct a new sketch with the given {@code reference}.
	 *
	 * @param reference the reference of the constructed sketch.
	 * @throws NullPointerException if the given {@code reference} is null.
	 * @since 0.2.0 ~2021.05.15
	 */
	public Sketch(@NotNull Reference reference) {
		Objects.requireNonNull(reference, "reference");
		this.name = new Name("");
		this.document = new PseudoDocument("");
		this.reference = reference;
		this.properties = new Properties();
	}

	/**
	 * Construct a new sketch with the given {@code reference} and the given {@code
	 * properties}.
	 * <br>
	 * A clone of the given {@code properties} will be the default properties of the
	 * constructed sketch.
	 *
	 * @param reference  the reference of the contracted sketch.
	 * @param properties the default properties of the constructed sketch.
	 * @throws NullPointerException if the given {@code reference} or {@code properties}
	 *                              is null.
	 * @since 0.2.0 ~2021.05.15
	 */
	public Sketch(@NotNull Reference reference, @NotNull Properties properties) {
		Objects.requireNonNull(reference, "reference");
		Objects.requireNonNull(properties, "properties");
		this.name = new Name("");
		this.document = new PseudoDocument("");
		this.reference = reference;
		Properties defaults = new Properties();
		defaults.putAll(properties);
		this.properties = new Properties(defaults);
	}

	//object

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

	@NotNull
	@Override
	public String toString() {
		return this.name + " " + this.document + "[" + this.reference + "]";
	}

	//public get

	/**
	 * Get the sketch at the given {@code direction} from this sketch.
	 *
	 * @param direction the direction.
	 * @return the sketch at the given {@code direction} form this sketch. Or {@code null}
	 * 		if no such sketch.
	 * @throws NullPointerException if the given {@code sketch} is null.
	 * @since 0.2.0 ~2021.05.15
	 */
	@Nullable
	@Contract(pure = true)
	public Sketch get(@NotNull Direction direction) {
		Objects.requireNonNull(direction, "direction");
		switch (direction) {
			case PARENT:
				Node<Sketch> headTop = Nodes
						.tail(Tetragon.START, this.node)
						.get(Tetragon.TOP);

				return headTop == null ? null : headTop.get();
			case CHILD:
				Node<Sketch> bottom = this.node.get(Tetragon.BOTTOM);

				return bottom == null ? null : bottom.get();
			case NEXT:
				Node<Sketch> end = this.node.get(Tetragon.END);

				return end == null ? null : end.get();
			case PREVIOUS:
				Node<Sketch> start = this.node.get(Tetragon.START);

				return start == null ? null : start.get();
			default:
				throw new InternalError();
		}
	}

	//getters

	/**
	 * Get the document of this sketch.
	 *
	 * @return the document of this sketch.
	 * @since 0.2.0 ~2021.05.14
	 */
	@NotNull
	@Contract(pure = true)
	public Document getDocument() {
		return this.document;
	}

	/**
	 * Get the name of this sketch.
	 *
	 * @return the name of this sketch.
	 * @since 0.2.0 ~2021.05.14
	 */
	@NotNull
	@Contract(pure = true)
	public Name getName() {
		return this.name;
	}

	//public offer

	/**
	 * Offer the given {@code sketch} to the structure of this sketch. The given {@code
	 * sketch} will be removed from its structure then put to the proper place in the
	 * structure of this sketch.
	 * <br>
	 * If failed to insert the given {@code sketch} because of an {@link
	 * IllegalSketchException}, then the method will exit without anything changed.
	 *
	 * @param sketch the sketch to be added.
	 * @throws NullPointerException       if the given {@code sketch} is null.
	 * @throws SketchOutOfBoundsException if the given {@code sketch} does not fit in the
	 *                                    parent of this sketch.
	 * @throws SketchTakeoverException    if the given {@code sketch} has the same range
	 *                                    as a sketch in the structure of this sketch.
	 * @throws SketchClashException       if the given {@code sketch} clashes with another
	 *                                    sketch in the structure of this sketch.
	 * @since 0.2.0 ~2021.05.14
	 */
	@Contract(mutates = "this,param")
	public synchronized void offer(@NotNull Sketch sketch) {
		Objects.requireNonNull(sketch, "sketch");
		switch (Relation.compute(this, sketch)) {
			case AHEAD:
			case BEHIND:
			case CONTAINER:
				this.offerParent(sketch);
				return;
			case START:
			case FRAGMENT:
			case END:
				this.offerChild(sketch);
				return;
			case AFTER:
			case NEXT:
				this.offerNext(sketch);
				return;
			case BEFORE:
			case PREVIOUS:
				this.offerPrevious(sketch);
				return;
			case SAME:
				throw new SketchTakeoverException("Sketch takeover");
			case OVERFLOW:
			case UNDERFLOW:
				throw new SketchClashException("Sketch clash");
			default:
				throw new InternalError();
		}
	}

	//constant getters

	/**
	 * Return the properties of this sketch.
	 *
	 * @return the properties of this sketch.
	 * @since 0.2.0 ~2021.05.14
	 */
	@NotNull
	@Contract(pure = true)
	public Properties properties() {
		//noinspection AssignmentOrReturnOfFieldWithMutableType
		return this.properties;
	}

	/**
	 * Get the reference of this sketch.
	 *
	 * @return the reference of this sketch.
	 * @since 0.2.0 ~2021.05.14
	 */
	@NotNull
	@Contract(pure = true)
	public Reference reference() {
		return this.reference;
	}

	//remove

	/**
	 * Cleanly remove this sketch from the structure it is on.
	 *
	 * @since 0.2.0 ~2021.05.14
	 */
	@Contract(mutates = "this")
	public synchronized void remove() {
		Node<Sketch> top = this.node.get(Tetragon.TOP);
		Node<Sketch> start = this.node.get(Tetragon.START);
		Node<Sketch> end = this.node.get(Tetragon.END);
		Node<Sketch> bottom = this.node.get(Tetragon.BOTTOM);

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
	 * Cleanly remove the sketches at the given {@code direction} of this sketch.
	 * <br><br>
	 * Behaviour list:
	 * <ul>
	 *     <li>{@link Direction#PARENT}: remove the parent of this sketch and its brothers.</li>
	 *     <li>{@link Direction#PREVIOUS}: remove the sketches previous to this from the parent.</li>
	 *     <li>{@link Direction#NEXT}: remove the sketches next to this from the parent.</li>
	 *     <li>{@link Direction#CHILD}: remove the children sketches of this from this.</li>
	 * </ul>
	 *
	 * @param direction the direction where to remove the node at.
	 * @throws NullPointerException if the given {@code direction} is null.
	 * @since 0.2.0 ~2021.05.14
	 */
	@Contract(mutates = "this")
	public synchronized void remove(@NotNull Direction direction) {
		Objects.requireNonNull(direction, "direction");
		switch (direction) {
			case PARENT:
				//look for the most previous sketch and remove its parent
				Nodes.tail(Tetragon.START, this.node)
					 .remove(Tetragon.TOP);
				break;
			case PREVIOUS:
				//look for the most previous sketch and steal its parent
				Node<Sketch> headTop = Nodes
						.tail(Tetragon.START, this.node)
						.get(Tetragon.TOP);

				//steal the parent
				if (headTop != null)
					headTop.put(Tetragon.BOTTOM, this.node);

				//cut the previous
				this.node.remove(Tetragon.START);
				break;
			case NEXT:
				//just remove the next
				this.node.remove(Tetragon.END);
				break;
			case CHILD:
				//just remove the child
				this.node.remove(Tetragon.BOTTOM);
				break;
		}
	}

	//setters

	/**
	 * Set the document of this sketch to be the given {@code document}.
	 *
	 * @param document the document to be set.
	 * @throws NullPointerException if the given {@code document} is null.
	 * @since 0.2.0 ~2021.05.14
	 */
	@Contract(mutates = "this")
	public void setDocument(@NotNull Document document) {
		Objects.requireNonNull(document, "document");
		this.document = document;
	}

	/**
	 * Set the name of this sketch to be the given {@code name}.
	 *
	 * @param name the name to be set.
	 * @throws NullPointerException if the given {@code name} is null.
	 * @since 0.2.0 ~2021.05.14
	 */
	@Contract(mutates = "this")
	public void setName(@NotNull Name name) {
		Objects.requireNonNull(name, "name");
		this.name = name;
	}

	//private offer

	/**
	 * Try to place the given {@code sketch} in the proper place between the children of
	 * this sketch.
	 * <br>
	 * If failed to insert the given {@code sketch} because of an {@link
	 * IllegalSketchException}, then the method will exit without anything changed.
	 *
	 * @param sketch the offered sketch.
	 * @throws NullPointerException       if the given {@code sketch} is null.
	 * @throws SketchOutOfBoundsException if the given {@code sketch} does not fit in this
	 *                                    sketch.
	 * @throws SketchTakeoverException    if the given {@code sketch} has the same range
	 *                                    as a sketch in the structure of this sketch.
	 * @throws SketchClashException       if the given {@code sketch} clashes with another
	 *                                    sketch in the structure of this sketch.
	 * @since 0.2.0 ~2021.05.15
	 */
	@SuppressWarnings({"OverlyLongMethod", "OverlyComplexMethod"})
	private synchronized void offerChild(@NotNull Sketch sketch) {
		Objects.requireNonNull(sketch, "sketch");
		switch (Dominance.compute(this, sketch)) {
			case PART:
				Node<Sketch> bottom = this.node.get(Tetragon.BOTTOM);

				//case no children
				if (bottom == null) {
					//clean
					sketch.remove();

					//this |> sketch
					this.node.put(Tetragon.BOTTOM, sketch.node);
					return;
				}

				//compare to the first
				switch (Relation.compute(bottom.get(), sketch)) {
					case BEFORE:
					case PREVIOUS:
						//clean
						sketch.remove();

						//this |> sketch -> bottom
						this.node.put(Tetragon.BOTTOM, sketch.node);
						bottom.put(Tetragon.START, sketch.node);
						return;
					case BEHIND:
					case CONTAINER:
					case AHEAD:
						//this |> sketch |> bottom
						bottom.get().offerParent(sketch);
						return;
					case START:
					case FRAGMENT:
					case END:
						//this |> bottom |> sketch
						bottom.get().offerChild(sketch);
						return;
					case AFTER:
					case NEXT:
						//this |> bottom -> sketch
						bottom.get().offerNext(sketch);
						return;
					case SAME:
						throw new SketchTakeoverException("Exact child bounds", bottom.get(), sketch);
					case OVERFLOW:
					case UNDERFLOW:
						throw new SketchClashException("Clash with child", bottom.get(), sketch);
					default:
						throw new InternalError();
				}
			case CONTAIN:
			case NONE:
				throw new SketchOutOfBoundsException("Out of the bounds", this, sketch);
			case EXACT:
				throw new SketchTakeoverException("Exact bounds", this, sketch);
			case SHARE:
				throw new SketchClashException("Clash with", this, sketch);
			default:
				throw new InternalError();
		}
	}

	/**
	 * Try to place the given {@code sketch} somewhere after this sketch.
	 * <br>
	 * If failed to insert the given {@code sketch} because of an {@link
	 * IllegalSketchException}, then the method will exit without anything changed.
	 *
	 * @param sketch the offered sketch.
	 * @throws NullPointerException       if the given {@code sketch} is null.
	 * @throws IllegalSketchException     if the given {@code sketch} is not after this
	 *                                    sketch.
	 * @throws SketchOutOfBoundsException if the given {@code sketch} does not fit in the
	 *                                    parent of this sketch.
	 * @throws SketchTakeoverException    if the given {@code sketch} has the same range
	 *                                    as a sketch in the structure of this sketch.
	 * @throws SketchClashException       if the given {@code sketch} clashes with another
	 *                                    sketch in the structure of this sketch.
	 * @since 0.2.0 ~2021.05.15
	 */
	@SuppressWarnings({"OverlyComplexMethod", "OverlyLongMethod"})
	private synchronized void offerNext(@NotNull Sketch sketch) {
		Objects.requireNonNull(sketch, "sketch");
		switch (Relation.compute(this, sketch)) {
			case AFTER:
			case NEXT:
				Node<Sketch> end = this.node.get(Tetragon.END);

				//case at the end
				if (end == null) {
					Sketch parent = this.get(Direction.PARENT);

					if (parent == null) {
						//clean
						sketch.remove();

						//this -> sketch
						this.node.put(Tetragon.END, sketch.node);
						return;
					}

					//validate compared to parent bounds
					switch (Dominance.compute(parent, sketch)) {
						case PART:
							//clean
							sketch.remove();

							//this -> sketch
							this.node.put(Tetragon.END, sketch.node);
							return;
						case SHARE:
							throw new SketchClashException("Clash with parent", parent, sketch);
						case NONE:
							throw new SketchOutOfBoundsException("Out of the bounds of the parent", parent, sketch);
						case EXACT:
						case CONTAIN:
							//actually, this must not happen, never!!!
						default:
							throw new InternalError();
					}
				}

				//compare to the next
				switch (Relation.compute(end.get(), sketch)) {
					case NEXT:
					case AFTER:
						//this -> end -> sketch
						end.get().offerNext(sketch);
						return;
					case BEHIND:
					case CONTAINER:
					case AHEAD:
						//this -> sketch |> end
						end.get().offerParent(sketch);
						return;
					case START:
					case FRAGMENT:
					case END:
						//this -> end |> sketch
						end.get().offerChild(sketch);
						return;
					case BEFORE:
					case PREVIOUS:
						//clean
						sketch.remove();

						//this -> sketch -> end
						this.node.put(Tetragon.END, sketch.node);
						end.put(Tetragon.START, sketch.node);
						return;
					case SAME:
						throw new SketchTakeoverException("Exact bounds", end.get(), sketch);
					case OVERFLOW:
					case UNDERFLOW:
						throw new SketchClashException("Clash with next", end.get(), sketch);
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
				throw new IllegalSketchException("Must be after", sketch);
			case SAME:
				throw new SketchTakeoverException("Exact bounds", this, sketch);
			case OVERFLOW:
			case UNDERFLOW:
				throw new SketchClashException("Clash with", this, sketch);
			default:
				throw new InternalError();
		}
	}

	/**
	 * Try to place the given {@code sketch} as the parent of this sketch and its brothers
	 * that the given {@code sketch} fits as their parent.
	 * <br>
	 * If failed to insert the given {@code sketch} because of an {@link
	 * IllegalSketchException}, then the method will exit without anything changed.
	 *
	 * @param sketch the offered sketch.
	 * @throws NullPointerException       if the given {@code sketch} is null.
	 * @throws IllegalSketchException     if the given {@code sketch} is not valid as a
	 *                                    parent for this sketch.
	 * @throws SketchOutOfBoundsException if the given {@code sketch} does not fit in the
	 *                                    parent of this sketch.
	 * @throws SketchTakeoverException    if the given {@code sketch} has the same range
	 *                                    as a sketch in the structure of this sketch.
	 * @throws SketchClashException       if the given {@code sketch} clashes with another
	 *                                    sketch in the structure of this sketch.
	 * @since 0.2.0 ~2021.05.15
	 */
	@SuppressWarnings({"DuplicatedCode", "OverlyComplexMethod", "OverlyLongMethod"})
	private synchronized void offerParent(@NotNull Sketch sketch) {
		Objects.requireNonNull(sketch, "sketch");
		//noinspection SwitchStatementDensity
		switch (Dominance.compute(this, sketch)) {
			case CONTAIN:
				Node<Sketch> bottom = this.node;
				Node<Sketch> top;
				Node<Sketch> previous = null;
				Node<Sketch> next = null;

				//backwards (collecting `bottom` and `previous`)
				while0:
				while (true) {
					Node<Sketch> n = bottom.get(Tetragon.START);

					if (n == null) {
						top = bottom.get(Tetragon.TOP);
						break;
					}

					switch (Dominance.compute(n.get(), sketch)) {
						case CONTAIN:
							bottom = n;
							break;
						case NONE:
							previous = n;
							top = bottom.get(Tetragon.TOP);
							break while0;
						case SHARE:
							throw new SketchClashException("Clash with neighbor", n.get(), sketch);
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
						Node<Sketch> n = this.node;
						n != null;
						n = n.get(Tetragon.END)
				)
					switch (Dominance.compute(n.get(), sketch)) {
						case CONTAIN:
							break;
						case NONE:
							next = n;
							break for0;
						case SHARE:
							throw new SketchClashException("Clash with neighbor", n.get(), sketch);
						case EXACT:
						case PART:
							//actually, this must not happen, never!!!
						default:
							throw new InternalError();
					}

				if (previous != null && top != null)
					//if this happened, then the structure is corrupted
					throw new InternalError();

				if (previous == null) {
					if (top == null) {
						//clean
						sketch.remove();

						//sketch |> bottom; sketch -> next
						bottom.put(Tetragon.TOP, sketch.node);
						if (next != null)
							next.put(Tetragon.START, sketch.node);
						return;
					}

					//compare to the top
					switch (Dominance.compute(top.get(), sketch)) {
						case PART:
							//clean
							sketch.remove();

							//top |> sketch |> bottom; sketch -> next
							top.put(Tetragon.BOTTOM, sketch.node);
							bottom.put(Tetragon.TOP, sketch.node);
							if (next != null)
								next.put(Tetragon.START, sketch.node);
							return;
						case CONTAIN:
						case NONE:
							throw new SketchOutOfBoundsException("Out of the bounds of the parent", top.get(), sketch);
						case EXACT:
							throw new SketchTakeoverException("Exact parent bounds", top.get(), sketch);
						case SHARE:
							throw new SketchClashException("Clash with parent", top.get(), sketch);
						default:
							throw new InternalError();
					}
				}

				//clean
				sketch.remove();

				//previous -> sketch |> bottom; sketch -> next
				previous.put(Tetragon.END, sketch.node);
				bottom.put(Tetragon.TOP, sketch.node);
				if (next != null)
					next.put(Tetragon.START, sketch.node);
				return;
			case SHARE:
				throw new SketchClashException("Clash with", this, sketch);
			case NONE:
			case PART:
				throw new IllegalSketchException("Must fit", sketch);
			case EXACT:
				throw new SketchTakeoverException("Exact bounds", this, sketch);
			default:
				throw new InternalError();
		}
	}

	/**
	 * Try to place the given {@code sketch} somewhere before this sketch.
	 * <br>
	 * If failed to insert the given {@code sketch} because of an {@link
	 * IllegalSketchException}, then the method will exit without anything changed.
	 *
	 * @param sketch the offered sketch.
	 * @throws NullPointerException       if the given {@code sketch} is null.
	 * @throws IllegalSketchException     if the given {@code sketch} is not before this
	 *                                    sketch.
	 * @throws SketchOutOfBoundsException if the given {@code sketch} does not fit in the
	 *                                    parent of this sketch.
	 * @throws SketchTakeoverException    if the given {@code sketch} has the same range
	 *                                    as a sketch in the structure of this sketch.
	 * @throws SketchClashException       if the given {@code sketch} clashes with another
	 *                                    sketch in the structure of this sketch.
	 * @since 0.2.0 ~2021.05.15
	 */
	@SuppressWarnings({"OverlyComplexMethod", "OverlyLongMethod"})
	private synchronized void offerPrevious(@NotNull Sketch sketch) {
		Objects.requireNonNull(sketch, "sketch");
		switch (Relation.compute(this, sketch)) {
			case BEFORE:
			case PREVIOUS:
				Node<Sketch> start = this.node.get(Tetragon.START);

				//case at the start
				if (start == null) {
					Sketch parent = this.get(Direction.PARENT);

					if (parent == null) {
						//clean
						sketch.remove();

						//sketch -> this
						this.node.put(Tetragon.START, sketch.node);
						return;
					}

					//validate compared to parent bounds
					switch (Dominance.compute(parent, sketch)) {
						case PART:
							//clean
							sketch.remove();

							//parent |> sketch -> this
							parent.node.put(Tetragon.BOTTOM, sketch.node);
							this.node.put(Tetragon.START, sketch.node);
							return;
						case SHARE:
							throw new SketchClashException("Clash with parent", parent, sketch);
						case NONE:
							throw new SketchOutOfBoundsException("Out of the bounds of the parent", parent, sketch);
						case EXACT:
						case CONTAIN:
							//actually, this must not happen, never!!!
						default:
							throw new InternalError();
					}
				}

				//compare to the previous
				switch (Relation.compute(start.get(), sketch)) {
					case PREVIOUS:
					case BEFORE:
						//sketch -> start -> this
						start.get().offerPrevious(sketch);
						return;
					case BEHIND:
					case CONTAINER:
					case AHEAD:
						//sketch |> start; sketch -> this
						start.get().offerParent(sketch);
						return;
					case START:
					case FRAGMENT:
					case END:
						//start |> sketch; start -> this
						start.get().offerChild(sketch);
						return;
					case AFTER:
					case NEXT:
						//clean
						sketch.remove();

						//start -> sketch -> this
						start.put(Tetragon.END, sketch.node);
						this.node.put(Tetragon.START, sketch.node);
						return;
					case SAME:
						throw new SketchTakeoverException("Exact bounds", start.get(), sketch);
					case OVERFLOW:
					case UNDERFLOW:
						throw new SketchClashException("Clash with previous", start.get(), sketch);
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
				throw new IllegalSketchException("Must be before", sketch);
			case SAME:
				throw new SketchTakeoverException("Exact bounds", this, sketch);
			case OVERFLOW:
			case UNDERFLOW:
				throw new SketchClashException("Clash with", this, sketch);
			default:
				throw new InternalError();
		}
	}

	//builder

	/**
	 * A builder that makes building sketches much easier.
	 *
	 * @author LSafer
	 * @version 0.2.0
	 * @since 0.2.0 ~2021.05.15
	 */
	public static class Builder {
		/**
		 * The document to be set as the initial document of the next built sketches.
		 *
		 * @since 0.2.0 ~2021.05.15
		 */
		@NotNull
		protected Document document;
		/**
		 * The name to be set as the initial name of the next built sketches.
		 *
		 * @since 0.2.0 ~2021.05.15
		 */
		@NotNull
		protected Name name;
		/**
		 * The properties to be set as the default properties of the next built sketches.
		 *
		 * @since 0.2.0 ~2021.05.15
		 */
		@NotNull
		protected Properties properties;
		/**
		 * The reference to be set as the reference of the next built sketches.
		 *
		 * @since 0.2.0 ~2021.05.15
		 */
		@NotNull
		protected Reference reference;

		/**
		 * Construct a new builder with the default variables.
		 *
		 * @since 0.2.0 ~2021.05.15
		 */
		public Builder() {
			this.properties = new Properties();
			this.reference = new Reference();
			this.document = new PseudoDocument("");
			this.name = new Name("");
		}

		/**
		 * Construct a new builder copying the given {@code builder}.
		 * <br>
		 * A clone of the properties of the given {@code builder} will be the default
		 * properties of the properties of this builder.
		 *
		 * @param builder the builder to be copied.
		 * @throws NullPointerException if the given {@code builder} is null.
		 * @since 0.2.0 ~2021.05.15
		 */
		public Builder(@NotNull Builder builder) {
			Objects.requireNonNull(builder, "builder");
			this.name = builder.name;
			this.document = builder.document;
			this.reference = builder.reference;
			Properties defaults = new Properties();
			defaults.putAll(builder.properties);
			this.properties = new Properties(defaults);
		}

		/**
		 * Construct a new builder copying the given {@code sketch}.
		 * <br>
		 * A clone of the properties of the given {@code sketch} will be the default
		 * properties of the properties of this builder.
		 *
		 * @param sketch the sketch to be copied.
		 * @throws NullPointerException if the given {@code sketch} is null.
		 * @since 0.2.0 ~2021.05.15
		 */
		public Builder(@NotNull Sketch sketch) {
			Objects.requireNonNull(sketch, "sketch");
			this.name = sketch.name;
			this.document = sketch.document;
			this.reference = sketch.reference;
			Properties defaults = new Properties();
			defaults.putAll(sketch.properties);
			this.properties = new Properties(defaults);
		}

		/**
		 * Construct a new builder with the given parameters.
		 *
		 * @param name       the initial name set to the constructed builder.
		 * @param document   the initial document set to the constructed builder.
		 * @param reference  the initial reference set to the constructed builder.
		 * @param properties the initial properties set to the constructed builder.
		 * @throws NullPointerException if the given {@code name} or {@code document} or
		 *                              {@code reference} or {@code properties} is null.
		 * @since 0.2.0 ~2021.05.15
		 */
		public Builder(@NotNull Name name, @NotNull Document document, @NotNull Reference reference, @NotNull Properties properties) {
			Objects.requireNonNull(name, "name");
			Objects.requireNonNull(document, "document");
			Objects.requireNonNull(reference, "reference");
			Objects.requireNonNull(properties, "properties");
			this.name = name;
			this.document = document;
			this.reference = reference;
			//noinspection AssignmentOrReturnOfFieldWithMutableType
			this.properties = properties;
		}

		/**
		 * Build a sketch with the variables currently set in this builder.
		 * <br>
		 * The returned sketch will not be affected by any changes done to this builder.
		 * <br>
		 * This method will not return the same sketch twice.
		 *
		 * @return a new sketch with the current variables in this builder.
		 * @since 0.2.0 ~2021.05.15
		 */
		@NotNull
		@Contract(value = "->new", pure = true)
		public Sketch build() {
			Sketch sketch = new Sketch(this.reference, this.properties);
			sketch.setName(this.name);
			sketch.setDocument(this.document);
			return sketch;
		}

		//getters

		/**
		 * Return the current set document. Unless changed, the returned document will be
		 * the initial document of the next built sketches by this builder.
		 *
		 * @return the current set document.
		 * @since 0.2.0 ~2021.05.15
		 */
		@NotNull
		@Contract(pure = true)
		public Document getDocument() {
			return this.document;
		}

		/**
		 * Return the current set name. Unless changed, the returned name will be the
		 * initial name of the next built sketches by this builder.
		 *
		 * @return the current set name.
		 * @since 0.2.0 ~2021.05.15
		 */
		@NotNull
		@Contract(pure = true)
		public Name getName() {
			return this.name;
		}

		/**
		 * Return the current set properties. Unless changed, the returned properties will
		 * be the default properties of the next built sketches by this builder.
		 *
		 * @return the current set properties.
		 * @since 0.2.0 ~2021.05.15
		 */
		@NotNull
		@Contract(pure = true)
		public Properties getProperties() {
			//noinspection AssignmentOrReturnOfFieldWithMutableType
			return this.properties;
		}

		/**
		 * Return the current set reference. Unless changed, the returned reference will
		 * be the reference of the next built sketches by this builder.
		 *
		 * @return the current set reference.
		 * @since 0.2.0 ~2021.05.15
		 */
		@NotNull
		@Contract(pure = true)
		public Reference getReference() {
			return this.reference;
		}

		//setters

		/**
		 * Set the initial document of the sketch to be built to the given {@code
		 * document}.
		 *
		 * @param document the document to be the initial document of the next built
		 *                 sketch.
		 * @return this.
		 * @throws NullPointerException     if the given {@code document} is null.
		 * @throws IllegalArgumentException if this builder rejected the given {@code
		 *                                  document}. (optional)
		 * @since 0.2.0 ~2021.05.15
		 */
		@NotNull
		@Contract(value = "_->this", mutates = "this")
		public Builder setDocument(@NotNull Document document) {
			Objects.requireNonNull(document, "document");
			this.document = document;
			return this;
		}

		/**
		 * Set the initial name of the sketch to be built to the given {@code name}.
		 *
		 * @param name the name to be the initial name of the next built sketch.
		 * @return this.
		 * @throws NullPointerException     if the given {@code name} is null.
		 * @throws IllegalArgumentException if this builder rejected the given {@code
		 *                                  name}. (optional)
		 * @since 0.2.0 ~2021.05.15
		 */
		@NotNull
		@Contract(value = "_->this", mutates = "this")
		public Builder setName(@NotNull Name name) {
			Objects.requireNonNull(name, "name");
			this.name = name;
			return this;
		}

		/**
		 * Set the default properties of the sketch to be built to the given {@code
		 * properties}.
		 *
		 * @param properties the default properties to be the properties of the next built
		 *                   sketch.
		 * @return this.
		 * @throws NullPointerException     if the given {@code properties} is null.
		 * @throws IllegalArgumentException if this builder rejected the given {@code
		 *                                  properties}. (optional)
		 * @since 0.2.0 ~2021.05.15
		 */
		@NotNull
		@Contract(value = "_->this", mutates = "this")
		public Builder setProperties(@NotNull Properties properties) {
			Objects.requireNonNull(properties, "properties");
			//noinspection AssignmentOrReturnOfFieldWithMutableType
			this.properties = properties;
			return this;
		}

		/**
		 * Set the reference of the sketch to be built to the given {@code reference}.
		 *
		 * @param reference the reference to be the reference of the next built sketch.
		 * @return this.
		 * @throws NullPointerException     if the given {@code reference} is null.
		 * @throws IllegalArgumentException if this builder rejected the given {@code
		 *                                  reference}. (optional)
		 * @since 0.2.0 ~2021.05.15
		 */
		@NotNull
		@Contract(value = "_->this", mutates = "this")
		public Builder setReference(@NotNull Reference reference) {
			Objects.requireNonNull(reference, "reference");
			this.reference = reference;
			return this;
		}

		/**
		 * Set the reference of the sketch to be built to cover the whole given {@code
		 * document}.
		 * <br>
		 * Note: this method detect the length of the given {@code document} by reading
		 * it.
		 *
		 * @param document the document to calculate the reference from.
		 * @return this.
		 * @throws NullPointerException    if the given {@code document} is null.
		 * @throws IOError                 if an I/O exception occurred while trying to
		 *                                 read the given {@code document}.
		 * @throws UnreadableDocumentError if the given {@code document} is not available
		 *                                 for reading.
		 * @since 0.2.0 ~2021.05.15
		 */
		@NotNull
		@Contract(value = "_->this", mutates = "this")
		public Builder setReference(@NotNull Document document) {
			Objects.requireNonNull(document, "document");
			this.reference = new Reference(0, document.read().length());
			return this;
		}
	}
}
