/*
 *	Copyright 2020 Cufy
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
package org.cufy.text;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Map;

/**
 * An element that can be linked as a chain of elements. Each element stores what element before it
 * and what element after it. That way, any element in the chain can be the head. The data in this
 * class is meant to be not completely checked, to provide more flexibility.
 * <p>
 * The lifecycle of an element:
 * <ul>
 *     <li>Parsing stage, when an element still </li>
 * </ul>
 *
 * @author LSafer
 * @version 0.0.b
 * @since 0.0.b ~2020.10.11
 */
public interface Element {
	/**
	 * The relation name that links a element with its value.
	 *
	 * @since 0.0.c ~2020.11.04
	 */
	@NotNull
	String ARGUMENT = "argument";
	/**
	 * The relation name that links a element with its branch.
	 *
	 * @since 0.0.c ~2020.11.02
	 */
	@NotNull
	String BRANCH = "branch";
	/**
	 * The default relation name that builds up a linear chain.
	 *
	 * @since 0.0.c ~2020.10.31
	 */
	@NotNull
	String DEFAULT = "";
	/**
	 * The relation name that links a element with its fork.
	 *
	 * @since 0.0.c ~2020.11.02
	 */
	@NotNull
	String FORK = "fork";

	/**
	 * Get the next element to this element.
	 * <p>
	 * Invoking this method is identical to invoking {@link #getNext(String)} with {@link #DEFAULT}
	 * as the {@code relation} argument.
	 *
	 * @return the next element to this element. Or null if it is not set.
	 * @since 0.0.c ~2020.11.04
	 */
	@Nullable
	default Element getNext() {
		return this.getNext(Element.DEFAULT);
	}

	/**
	 * Get the previous element to this element.
	 * <p>
	 * Invoking this method is identical to invoking {@link #getPrevious(String)} with {@link
	 * #DEFAULT} as the {@code relation} argument.
	 *
	 * @return the previous element to this element. Or null if it is not set.
	 * @since 0.0.c ~2020.11.04
	 */
	@Nullable
	default Element getPrevious() {
		return this.getPrevious(Element.DEFAULT);
	}

	/**
	 * Remove the next element of this element.
	 * <p>
	 * Invoking this method is identical to invoking {@link #removeNext(String)} with {@link
	 * #DEFAULT} as the {@code relation} argument.
	 *
	 * @throws IllegalStateException if this element rejected to remove the element. Or the element
	 *                               rejected to remove this element.
	 * @since 0.0.c ~2020.11.04
	 */
	default void removeNext() {
		this.removeNext(Element.DEFAULT);
	}

	/**
	 * Remove the previous element of this element.
	 * <p>
	 * Invoking this method is identical to invoking {@link #removePrevious(String)} with {@link
	 * #DEFAULT} as the {@code relation} argument.
	 *
	 * @throws IllegalStateException if this element rejected to remove the element. Or the element
	 *                               rejected to remove this element.
	 * @since 0.0.c ~2020.11.04
	 */
	default void removePrevious() {
		this.removePrevious(Element.DEFAULT);
	}

	/**
	 * Set the next element of this element to be the given {@code element}.
	 * <p>
	 * Invoking this method is identical to invoking {@link #putNext(String, Element)} with {@link
	 * #DEFAULT} as the {@code relation} argument and the given {@code element} as the {@code
	 * element} argument.
	 *
	 * @param element the element to be set.
	 * @throws NullPointerException  if the given {@code element} is null.
	 * @throws IllegalStateException if this element rejected to remove the set element. Or the set
	 *                               element rejected to remove this element. Or this element
	 *                               rejected to set the given {@code element}. Or the given element
	 *                               rejected to set this element.
	 * @since 0.0.c ~2020.11.04
	 */
	default void setNext(@NotNull Element element) {
		this.putNext(Element.DEFAULT, element);
	}

	/**
	 * Set the previous element of this element to be the given {@code element}.
	 * <p>
	 * Invoking this method is identical to invoking {@link #putPrevious(String, Element)} with
	 * {@link #DEFAULT} as the {@code relation} argument and the given {@code element} as the {@code
	 * element} argument.
	 *
	 * @param element the element to be set.
	 * @throws NullPointerException  if the given {@code element} is null.
	 * @throws IllegalStateException if this element rejected to remove the set element. Or the set
	 *                               element rejected to remove this element. Or this element
	 *                               rejected to set the given {@code element}. Or the given element
	 *                               rejected to set this element.
	 * @since 0.0.c ~2020.11.04
	 */
	default void setPrevious(@NotNull Element element) {
		this.putPrevious(Element.DEFAULT, element);
	}

	/**
	 * Get the next element with the given {@code relation} name.
	 *
	 * @param relation the relation name of the targeted element.
	 * @return the next element with the given {@code relation}. Or null if no element with the
	 * 		given {@code relation} name has been set.
	 * @throws NullPointerException if the given {@code relation} is null.
	 * @since 0.0.c ~2020.10.31
	 */
	@Nullable
	Element getNext(@NotNull String relation);

	/**
	 * Return an immutable view for the mappings between this element and the elements after it.
	 *
	 * @return an immutable map view of the mappings after this element.
	 * @since 0.0.c ~2020.11.04
	 */
	@NotNull
	Map<String, Element> getNextMap();

	/**
	 * Get the previous element with the given {@code relation} name.
	 *
	 * @param relation the relation name of the targeted element.
	 * @return the previous element with the given {@code relation}. Or null if no element with the
	 * 		given {@code relation} name has been set.
	 * @throws NullPointerException if the given {@code relation} is null.
	 * @since 0.0.c ~2020.10.31
	 */
	@Nullable
	Element getPrevious(@NotNull String relation);

	/**
	 * Return an immutable view for the mappings between this element and the elements before it.
	 *
	 * @return an immutable map view of the mappings before this element.
	 * @since 0.0.c ~2020.11.04
	 */
	@NotNull
	Map<String, Element> getPreviousMap();

	/**
	 * Return the source of this element.
	 *
	 * @return the source of this element. Or null if it has not been set yet.
	 * @since 0.0.c ~2020.11.04
	 */
	@Nullable
	Source getSource();

	/**
	 * Invoke this element with the given {@code memory}.
	 *
	 * @param memory the memory to invoke this element with.
	 * @return the result of invoking this element. Or null if the result was appended to the given
	 *        {@code memory}.
	 * @throws NullPointerException if the given {@code memory} is null.
	 * @throws IOException          if any I/O exception occurs.
	 * @since 0.0.b ~2020.10.15
	 */
	@Nullable
	Object invoke(@NotNull Memory memory) throws IOException;

	/**
	 * Set the next element with the given {@code relation} to be the given {@code element}.
	 * <p>
	 * This method should do these steps in order:
	 * <ul>
	 *     <li>Skip if the current assigned element is the given {@code element}.</li>
	 *     <li>Replace the current assigned element with the given {@code element}.</li>
	 *     <li>Invoke {@link #removePrevious(String)} on the removed element with the given {@code relation}.</li>
	 *     <li>Invoke {@link #putPrevious(String, Element)} on the given {@code element} with the given {@code relation} and this element.</li>
	 * </ul>
	 *
	 * @param relation the relation name.
	 * @param element  the element to be set.
	 * @throws NullPointerException  if the given {@code relation} or {@code element} is null.
	 * @throws IllegalStateException if this element rejected to remove the set element. Or the set
	 *                               element rejected to remove this element. Or this element
	 *                               rejected to set the given {@code element}. Or the given element
	 *                               rejected to set this element.
	 * @since 0.0.c ~2020.10.31
	 */
	void putNext(@NotNull String relation, @NotNull Element element);

	/**
	 * Set the previous element with the given {@code relation} to be the given {@code element}.
	 * <p>
	 * This method should do these steps in order:
	 * <ul>
	 *     <li>Skip if the current assigned element is the given {@code element}.</li>
	 *     <li>Replace the current assigned element with the given {@code element}.</li>
	 *     <li>Invoke {@link #removeNext(String)} on the removed element with the given {@code relation}.</li>
	 *     <li>Invoke {@link #putNext(String, Element)} on the given {@code element} with the given {@code relation} and this element.</li>
	 * </ul>
	 *
	 * @param relation the relation name.
	 * @param element  the element to be set.
	 * @throws NullPointerException  if the given {@code relation} or {@code element} is null.
	 * @throws IllegalStateException if this element rejected to remove the set element. Or the set
	 *                               element rejected to remove this element. Or this element
	 *                               rejected to set the given {@code element}. Or the given element
	 *                               rejected to set this element.
	 * @since 0.0.c ~2020.10.31
	 */
	void putPrevious(@NotNull String relation, @NotNull Element element);

	/**
	 * Remove the next element with the given {@code relation} name.
	 * <p>
	 * This method should do these steps in order:
	 * <ul>
	 *     <li>Skip if no current assigned element.</li>
	 *     <li>Remove the current element.</li>
	 *     <li>Invoke {@link #removePrevious(String)} on the removed element with the given {@code relation}.</li>
	 * </ul>
	 *
	 * @param relation the name of the relation of the element to be removed.
	 * @throws NullPointerException  if the given {@code relation} is null.
	 * @throws IllegalStateException if this element rejected to remove the element. Or the element
	 *                               rejected to remove this element.
	 * @since 0.0.c ~2020.10.31
	 */
	void removeNext(@NotNull String relation);

	/**
	 * Remove the previous element with the given {@code relation} name.
	 * <p>
	 * This method should do these steps in order:
	 * <ul>
	 *     <li>Skip if no current assigned element.</li>
	 *     <li>Remove the current element.</li>
	 *     <li>Invoke {@link #removeNext(String)} on the removed element with the given {@code relation}.</li>
	 * </ul>
	 *
	 * @param relation the name of the relation of the element to be removed.
	 * @throws NullPointerException  if the given {@code relation} is null.
	 * @throws IllegalStateException if this element rejected to remove the element. Or the element
	 *                               rejected to remove this element.
	 * @since 0.0.c ~2020.10.31
	 */
	void removePrevious(@NotNull String relation);

	/**
	 * Set the source of this element to be the given {@code source}.
	 *
	 * @param source the source to be the source of this element.
	 * @throws NullPointerException  if the given {@code source} is null.
	 * @throws IllegalStateException if this element has a set source that is not the given {@code
	 *                               source}.
	 * @since 0.0.c ~2020.11.04
	 */
	void setSource(@NotNull Source source);
}

//@SuppressWarnings("JavaDoc")
//class If {
//	static class IfElement extends AbstractElement {
//		protected String logic;
//
//		@Override
//		public Object invoke(Memory memory) throws IOException {
//			Element value = this.getNext(Element.VALUE);
//			Element branch = this.getNext(Element.BRANCH);
//			Element fork = this.getNext(Element.FORK);
//			Element next = this.getNext();
//
//			if (value != null) {
//				Object v = value.invoke(memory);
//
//				if (v.equals(false) || v.equals(0))
//					branch.invoke(memory);
//				else
//					fork.invoke(memory);
//
//				next.invoke(memory);
//			}
//
//			return null;
//		}
//	}
//}

//	/**
//	 * Get the value of this element.
//	 *
//	 * @return the value of this element. Or null if it has not been set yet.
//	 * @since 0.0.b ~2020.10.11
//	 */
//	Object getValue();

//	/**
//	 * Set the value of this linkable to be the given {@code value}.
//	 *
//	 * @param value the value to be set as the value of this linkable.
//	 * @throws NullPointerException if the given {@code value} is null.
//	 * @since 0.0.b ~2020.10.11
//	 */
//	void setValue(Object value);

//
//	/**
//	 * Check if this element's region is reserved for this element. This method should return a
//	 * constant value and never change.
//	 *
//	 * @return true, if the region of this element is reserved for this element.
//	 * @since 0.0.b ~2020.10.15
//	 */
//	boolean isReserved();
//
//	/**
//	 * Set the position of this element to be the given {@code position}.
//	 * <p>
//	 * This method should do these steps in order:
//	 * <ul>
//	 *     <li>Skip if the given position already set in this element.</li>
//	 *     <li>Throw if another position has been set.</li>
//	 *     <li>Set the given position.</li>
//	 *     <li>Invoke {@link Position#setElement(Element)} with this element.</li>
//	 * </ul>
//	 *
//	 * @param position the position to be set.
//	 * @throws NullPointerException  if the given {@code position} is null.
//	 * @throws IllegalStateException if a position have already been set in this element. Or if the
//	 *                               given {@code position} already have an element set to it (other
//	 *                               than this element).
//	 * @since 0.0.c ~2020.11.02
//	 */
//	void setPosition(Position position);
//
//	/**
//	 * Set the given {@code string} to be the source string of this element.The source should always
//	 * be constant across the elements in a chain.
//	 *
//	 * @param string the source string to be the source string of this element.
//	 * @throws NullPointerException  if the given {@code string} is null.
//	 * @throws IllegalStateException if the source string already has been set.
//	 * @since 0.0.b ~2020.10.15
//	 */
//	void setSource(Source source);
//
//	/**
//	 * Return the position of this element.
//	 *
//	 * @return the position of this element. Or null if it has not been set yet.
//	 * @since 0.0.c ~2020.11.02
//	 */
//	Position getPosition();
//
//	/**
//	 * Get the source string of this element. The source should always be constant across the
//	 * elements in a chain.
//	 *
//	 * @return the source string of this element. Or null if it has not been set yet.
//	 * @since 0.0.b ~2020.10.15
//	 */
//	Source getSource();
//
//	/**
//	 * A parser that takes ranges on non {@link Element#isReserved() reserved} positions and tries
//	 * to parse them into a specific type of an {@link Element}.
//	 *
//	 * @author LSafer
//	 * @version 0.0.b
//	 * @since 0.0.b ~2020.10.15
//	 */
//	interface Parser {
//		/**
//		 * Parse a range in the non-{@link Element#isReserved() reserved}-element given to a new
//		 * {@link Element}.
//		 *
//		 * @param element the element to parse a range on it.
//		 * @return the element after the given {@code element} in the chain. Or null if this method
//		 * 		has done nothing.
//		 * @throws NullPointerException if the given {@code element} is null.
//		 * @since 0.0.b ~2020.10.15
//		 */
//		Element parse(Element element);
//	}
