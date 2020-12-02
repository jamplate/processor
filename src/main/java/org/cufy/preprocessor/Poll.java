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
package org.cufy.preprocessor;

import org.cufy.preprocessor.Parser;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

/**
 * Common utilities for the polls used in a {@link Parser}.
 *
 * @author LSafer
 * @version 0.0.b
 * @since 0.0.b ~2020.10.02
 */
public final class Poll {
	/**
	 * Add the given element to the right of the last element returned by the given {@code iterator}
	 * in the given {@code iterator} then reset the iterator to its original state. The {@code
	 * lastReturn} (as used by {@link ListIterator#set(Object)}) and {@link Iterator#remove()} will
	 * always be set to element before the last element returned by the given {@code iterator}
	 * before calling this method.
	 *
	 * @param iterator the iterator to add the given element to it.
	 * @param element  the element to be added to the given {@code iterator}.
	 * @throws NullPointerException if the given {@code iterator} is null.
	 * @since 0.0.b ~2020.10.07
	 */
	public static void addNext(ListIterator iterator, Object element) {
		Objects.requireNonNull(iterator, "iterator");
		iterator.add(element);
		iterator.previous();
		if (iterator.hasPrevious()) {
			iterator.previous();
			iterator.next();
		}
	}

	/**
	 * Add the given element to the left of the last element returned by the given {@code iterator}
	 * in the given {@code iterator} then reset the iterator to its original state. The {@code
	 * lastReturn} (as used by {@link ListIterator#set(Object)}) and {@link Iterator#remove()} will
	 * always be set to element before the last element returned by the given {@code iterator}
	 * before calling this method.
	 *
	 * @param iterator the iterator to add the given element to it.
	 * @param element  the element to be added to the given {@code iterator}.
	 * @throws NullPointerException   if the given {@code iterator} is null.
	 * @throws NoSuchElementException if there where no element before the next element in the given
	 *                                {@code iterator}.
	 * @since 0.0.b ~2020.10.07
	 */
	public static void addPrevious(ListIterator iterator, Object element) {
		Objects.requireNonNull(iterator, "iterator");
		iterator.previous();
		iterator.add(element);
		iterator.next();
	}

	/**
	 * Create a new empty poll.
	 *
	 * @return a new empty poll.
	 * @since 0.0.b ~2020.10.12
	 */
	public static List create() {
		return new ArrayList();
	}

	/**
	 * Create a new poll with the given {@code string}.
	 *
	 * @param string the string to be initially stored in the constructed poll.
	 * @return a new poll containing the given {@code string}.
	 * @throws NullPointerException if the given {@code string} is null.
	 * @since 0.0.b ~2020.10.12
	 */
	public static List create(String string) {
		Objects.requireNonNull(string, "string");
		return new ArrayList(Collections.singleton(string));
	}

	/**
	 * Check if the last element returned by the given {@code iterator} has an element to the right
	 * of it in the given {@code iterator}. Then reset the given {@code iterator} to its original
	 * state.
	 *
	 * @param iterator the iterator to be checked.
	 * @return true, if the last element returned from the given {@code iterator} has an element to
	 * 		the right of it.
	 * @throws NullPointerException if the given {@code iterator} is null.
	 * @since 0.0.b ~2020.10.03
	 */
	public static boolean hasNext(ListIterator iterator) {
		Objects.requireNonNull(iterator, "iterator");
		return iterator.hasNext();
	}

	/**
	 * Check if the last element returned by the given {@code iterator} has an element to the left
	 * of it in the given {@code iterator}. Then reset the given {@code iterator} to its original
	 * state.
	 *
	 * @param iterator the iterator to be checked.
	 * @return true, if the last element returned from the given {@code iterator} has an element to
	 * 		the left of it.
	 * @throws NullPointerException if the given {@code iterator} is null.
	 * @since 0.0.b ~2020.10.02
	 */
	public static boolean hasPrevious(ListIterator iterator) {
		Objects.requireNonNull(iterator, "iterator");
		if (iterator.hasPrevious()) {
			iterator.previous();

			if (iterator.hasPrevious()) {
				iterator.next();
				return true;
			}

			iterator.next();
		}

		return false;
	}

	/**
	 * Iterate over all the elements in the given {@code poll} and break the loop when the given
	 * {@code predicate} returned false on all the elements in an iteration.
	 * <p>
	 * The predicate will be invoked with an iterator that its cursor is at the index after the
	 * index of the object given to it, and its last-return is at the index of the object given to
	 * it.
	 *
	 * @param poll      the poll to iterate on.
	 * @param predicate the predicate to be invoked while iterating.
	 * @return true, if the given {@code predicate} returned true in any invocation it get invoked
	 * 		by this method.
	 * @throws NullPointerException if the given {@code poll} or {@code predicate} is null.
	 * @since 0.0.b ~2020.10.02
	 */
	public static boolean iterate(List poll, BiPredicate<ListIterator, Object> predicate) {
		Objects.requireNonNull(poll, "poll");
		Objects.requireNonNull(predicate, "predicate");

		boolean any = false;
		while (true) {
			boolean match = false;

			ListIterator iterator = poll.listIterator();
			while (iterator.hasNext()) {
				Object object = iterator.next();

				if (predicate.test(iterator, object))
					any = match = true;
			}

			if (!match)
				return any;
		}
	}

	/**
	 * Get the next element (of the last returned element) from the given {@code iterator} then
	 * reset the iterator to its original state. The {@code lastReturn} (as used by {@link
	 * ListIterator#set(Object)}) and {@link Iterator#remove()} will always be set to element before
	 * the last element returned by the given {@code iterator} before calling this method. If no
	 * element was before that element, the iterator's {@code lastReturn} will be 0.
	 *
	 * @param iterator the iterator to get the next element from.
	 * @return the next  element (to the last element returned) from the given {@code iterator}.
	 * @throws NullPointerException   if the given {@code iterator} is null.
	 * @throws NoSuchElementException if there where no element next to the last element returned
	 *                                from the given {@code iterator} in the given {@code
	 *                                iterator}.
	 * @since 0.0.b ~2020.10.03
	 */
	public static Object peekNext(ListIterator iterator) {
		Objects.requireNonNull(iterator, "iterator");
		//cursor = 1, lastReturn = 0
		Object next = iterator.next();
		//cursor = 2, lastReturn = 1
		iterator.previous();
		//cursor = 1, lastReturn = 1

		if (iterator.hasPrevious()) {
			iterator.previous();
			//cursor = 0, lastReturn = 0
			iterator.next();
			//cursor = 1, lastReturn = 0
		}

		return next;
	}

	/**
	 * Get the previous element (of the last returned element) from the given {@code iterator} then
	 * reset the iterator to its original state. The {@code lastReturn} (as used by {@link
	 * ListIterator#set(Object)}) and {@link Iterator#remove()} will always be set to element before
	 * the last element returned by the given {@code iterator} before calling this method.
	 *
	 * @param iterator the iterator to get the previous element from.
	 * @return the previous element (to the last element returned) from the given {@code iterator}.
	 * @throws NullPointerException   if the given {@code iterator} is null.
	 * @throws NoSuchElementException if there where no element before the last element returned
	 *                                from the given {@code iterator} in the given {@code
	 *                                iterator}.
	 * @since 0.0.b ~2020.10.03
	 */
	public static Object peekPrevious(ListIterator iterator) {
		Objects.requireNonNull(iterator, "iterator");
		//cursor = 2, lastReturn = 1
		iterator.previous();
		//cursor = 1, lastReturn = 1
		Object previous = iterator.previous();
		//cursor = 0, lastReturn = 0
		iterator.next();
		//cursor = 1, lastReturn = 0
		iterator.next();
		//cursor = 2, lastReturn = 1
		return previous;
	}

	/**
	 * Remove that last element returned by the given {@code iterator}. The last element always
	 * refer to the element returned by a call to {@link ListIterator#previous()}.
	 *
	 * @param iterator the iterator to remove the last element returned from it.
	 * @throws NullPointerException   if the given {@code iterator} is null.
	 * @throws NoSuchElementException if the given {@code iterator} has no previous element.
	 * @since 0.0.b ~2020.10.12
	 */
	public static void remove(ListIterator iterator) {
		Objects.requireNonNull(iterator, "iterator");
		iterator.previous();
		iterator.remove();
		if (iterator.hasPrevious()) {
			iterator.previous();
			iterator.next();
		}
	}

	/**
	 * Remove the element to the right of the last element returned from the given {@code iterator}
	 * in the given {@code iterator}. Then reset the given {@code iterator} to its original state.
	 * The {@code lastReturn} (as used by {@link ListIterator#set(Object)}) and {@link
	 * Iterator#remove()} will always be set to element before the last element returned by the
	 * given {@code iterator} before calling this method. If no element was before that element, the
	 * iterator's {@code lastReturn} will be 0.
	 *
	 * @param iterator the iterator to get the next element from.
	 * @return the removed element.
	 * @throws NullPointerException   if the given {@code iterator} is null.
	 * @throws NoSuchElementException if there where no element next to the last element returned
	 *                                from the given {@code iterator} in the given {@code
	 *                                iterator}.
	 * @since 0.0.b ~2020.10.03
	 */
	public static Object removeNext(ListIterator iterator) {
		Objects.requireNonNull(iterator, "iterator");
		//cursor = 1, lastReturn = 0
		Object next = iterator.next();
		//cursor = 2, lastReturn = 1
		iterator.remove();
		//cursor = 1, lastReturn = -1

		if (iterator.hasPrevious()) {
			iterator.previous();
			//cursor = 0, lastReturn = 0
			iterator.next();
			//cursor = 1, lastReturn = 0
		} else {
			iterator.next();
			//cursor => 2, lastReturn => 1
			iterator.previous();
			//cursor => 1, lastReturn => 1
		}

		return next;
	}

	/**
	 * Remove the element to the left of the last element returned from the given {@code iterator}
	 * in the given {@code iterator}. Then reset the given {@code iterator} to its original state.
	 * The {@code lastReturn} (as used by {@link ListIterator#set(Object)}) and {@link
	 * Iterator#remove()} will always be set to element before the last element returned by the
	 * given {@code iterator} before calling this method.
	 *
	 * @param iterator the iterator to get the previous element from.
	 * @return the removed element.
	 * @throws NullPointerException   if the given {@code iterator} is null.
	 * @throws NoSuchElementException if there where no element before the last element returned
	 *                                from the given {@code iterator} in the given {@code
	 *                                iterator}.
	 * @since 0.0.b ~2020.10.03
	 */
	public static Object removePrevious(ListIterator iterator) {
		Objects.requireNonNull(iterator, "iterator");
		//cursor = 2, lastReturn = 1
		iterator.previous();
		//cursor = 1, lastReturn = 1
		Object left = iterator.previous();
		//cursor = 0, lastReturn = 0
		iterator.remove();
		//cursor = 0, lastReturn = -1
		iterator.next();
		//cursor = 1, lastReturn = 0
		return left;
	}

	/**
	 * Get a string representation of a {@code poll}.
	 *
	 * @param poll the poll to get a string representation of it.
	 * @return a string representation of the given {@code poll}.
	 * @since 0.0.b ~2020.10.07
	 */
	public static String toString(List poll) {
		return poll == null ? "" : ((List<?>) poll).stream()
				.filter(Objects::nonNull)
				.map(Object::toString)
				.collect(Collectors.joining());
	}
}
