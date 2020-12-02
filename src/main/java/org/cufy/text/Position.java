///*
// *	Copyright 2020 Cufy
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
//package org.cufy.text;
//
///**
// * A position is a node in an strict non-linear chain. Each position knows what is the positions
// * before it and what is the relation between them and what is the position after it and what is the
// * relation between them.
// * <p>
// * If a position {@code A} is linked with a position {@code B} with the relation {@code R} and the
// * position {@code A} was before the position {@code B}. Then, invoking {@link #getNext(String)
// * A.getNext(R)} should always return the position {@code B}. Also, invoking {@link
// * #getPrevious(String) B.getPrevious(R)} should always return the position {@code A}.
// *
// * @author LSafer
// * @version 0.0.c ~2020.10.31
// * @since 0.0.c ~2020.10.31
// */
//public interface Position {
//	/**
//	 * The relation name that links a position with its branch.
//	 *
//	 * @since 0.0.c ~2020.11.02
//	 */
//	String BRANCH = "branch";
//	/**
//	 * The default relation name that builds up a linear chain.
//	 *
//	 * @since 0.0.c ~2020.10.31
//	 */
//	String DEFAULT = "";
//	/**
//	 * The relation name that links a position with its fork.
//	 *
//	 * @since 0.0.c ~2020.11.02
//	 */
//	String FORK = "fork";
//	/**
//	 * The relation name that links a position with its value.
//	 *
//	 * @since 0.0.c ~2020.11.04
//	 */
//	String VALUE = "value";
//
//	/**
//	 * Get the element at this position.
//	 *
//	 * @return the element at this position. Or null if the element has not been set yet.
//	 * @since 0.0.c ~2020.11.01
//	 */
//	Element getElement();
//
//	/**
//	 * Get the next position with the given {@code relation} name.
//	 *
//	 * @param relation the relation name of the targeted position.
//	 * @return the next position with the given {@code relation}. Or null if no position with the
//	 * 		given {@code relation} name has been set.
//	 * @throws NullPointerException if the given {@code relation} is null.
//	 * @since 0.0.c ~2020.10.31
//	 */
//	Position getNext(String relation);
//
//	/**
//	 * Get the previous position with the given {@code relation} name.
//	 *
//	 * @param relation the relation name of the targeted position.
//	 * @return the previous position with the given {@code relation}. Or null if no position with
//	 * 		the given {@code relation} name has been set.
//	 * @throws NullPointerException if the given {@code relation} is null.
//	 * @since 0.0.c ~2020.10.31
//	 */
//	Position getPrevious(String relation);
//
//	/**
//	 * Get the source of this position.
//	 *
//	 * @return the source of this position. Or null if it has not been set yet.
//	 * @since 0.0.c ~2020.11.03
//	 */
//	Source getSource();
//
//	/**
//	 * Set the next position with the given {@code relation} to be the given {@code position}.
//	 * <p>
//	 * This method should do these steps in order:
//	 * <ul>
//	 *     <li>Skip if the current assigned position is the given {@code position}.</li>
//	 *     <li>Replace the current assigned position with the given {@code position}.</li>
//	 *     <li>Invoke {@link #removePrevious(String)} on the removed position with the given {@code relation}.</li>
//	 *     <li>Invoke {@link #putPrevious(String, Position)} on the given {@code position} with the given {@code relation} and this position.</li>
//	 * </ul>
//	 *
//	 * @param relation the relation name.
//	 * @param position the position to be set.
//	 * @throws NullPointerException  if the given {@code relation} or {@code position} is null.
//	 * @throws IllegalStateException if this position rejected to remove the set position. Or the
//	 *                               set position rejected to remove this position. Or this position
//	 *                               rejected to set the given {@code position}. Or the given
//	 *                               position rejected to set this position.
//	 * @since 0.0.c ~2020.10.31
//	 */
//	void putNext(String relation, Position position);
//
//	/**
//	 * Set the previous position with the given {@code relation} to be the given {@code position}.
//	 * <p>
//	 * This method should do these steps in order:
//	 * <ul>
//	 *     <li>Skip if the current assigned position is the given {@code position}.</li>
//	 *     <li>Replace the current assigned position with the given {@code position}.</li>
//	 *     <li>Invoke {@link #removeNext(String)} on the removed position with the given {@code relation}.</li>
//	 *     <li>Invoke {@link #putNext(String, Position)} on the given {@code position} with the given {@code relation} and this position.</li>
//	 * </ul>
//	 *
//	 * @param relation the relation name.
//	 * @param position the position to be set.
//	 * @throws NullPointerException  if the given {@code relation} or {@code position} is null.
//	 * @throws IllegalStateException if this position rejected to remove the set position. Or the
//	 *                               set position rejected to remove this position. Or this position
//	 *                               rejected to set the given {@code position}. Or the given
//	 *                               position rejected to set this position.
//	 * @since 0.0.c ~2020.10.31
//	 */
//	void putPrevious(String relation, Position position);
//
//	/**
//	 * Remove the next position with the given {@code relation} name.
//	 * <p>
//	 * This method should do these steps in order:
//	 * <ul>
//	 *     <li>Skip if no current assigned position.</li>
//	 *     <li>Remove the current position.</li>
//	 *     <li>Invoke {@link #removePrevious(String)} on the removed position with the given {@code relation}.</li>
//	 * </ul>
//	 *
//	 * @param relation the name of the relation of the position to be removed.
//	 * @throws NullPointerException  if the given {@code relation} is null.
//	 * @throws IllegalStateException if this position rejected to remove the position. Or the
//	 *                               position rejected to remove this position.
//	 * @since 0.0.c ~2020.10.31
//	 */
//	void removeNext(String relation);
//
//	/**
//	 * Remove the previous position with the given {@code relation} name.
//	 * <p>
//	 * This method should do these steps in order:
//	 * <ul>
//	 *     <li>Skip if no current assigned position.</li>
//	 *     <li>Remove the current position.</li>
//	 *     <li>Invoke {@link #removeNext(String)} on the removed position with the given {@code relation}.</li>
//	 * </ul>
//	 *
//	 * @param relation the name of the relation of the position to be removed.
//	 * @throws NullPointerException  if the given {@code relation} is null.
//	 * @throws IllegalStateException if this position rejected to remove the position. Or the
//	 *                               position rejected to remove this position.
//	 * @since 0.0.c ~2020.10.31
//	 */
//	void removePrevious(String relation);
//
//	/**
//	 * Set the element of this position to be the given {@code element}.
//	 * <p>
//	 * This method should do these steps in order:
//	 * <ul>
//	 *     <li>Skip if the given element already set in this position.</li>
//	 *     <li>Throw if another element has been set.</li>
//	 *     <li>Set the given element.</li>
//	 *     <li>Invoke {@link Element#setPosition(Position)} with this position.</li>
//	 * </ul>
//	 *
//	 * @param element the element to be set.
//	 * @throws NullPointerException  if the given {@code element} is null.
//	 * @throws IllegalStateException if the element has been set before. Or the given {@code
//	 *                               element} already has a position set to it (other than this
//	 *                               position).
//	 * @since 0.0.c ~2020.11.01
//	 */
//	void setElement(Element element);
//
//	/**
//	 * Set the source of this position to be the given {@code source}.
//	 * <p>
//	 * This method should do these steps in order:
//	 * <ul>
//	 *     <li>Skip if the given source already set in this position.</li>
//	 *     <li>Throw if another source has been set.</li>
//	 *     <li>Set the given source.</li>
//	 *     <li>Invoke {@link Source}</li>
//	 * </ul>
//	 *
//	 * @param source the source to be the source of this position.
//	 * @throws NullPointerException  if the given {@code source} is null.
//	 * @throws IllegalStateException if the source of this position already has been set.
//	 * @since 0.0.c ~2020.11.03
//	 */
//	void setSource(Source source);
//}
