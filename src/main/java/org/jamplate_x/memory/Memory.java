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
//package org.jamplate.x.memory;
//
///**
// * A memory is a virtualized space that is divided into two divisions. The stack and the
// * heap. Methods {@link #push(Object)}, {@link #peek()} and {@link #pop()} controls the
// * stack and methods {@link #allocate()}, {@link #put(long, Object)}, {@link #get(long)}
// * and {@link #free(long)} controls the heap.
// *
// * @author LSafer
// * @version 0.2.0
// * @since 0.2.0 ~2021.01.30
// */
//public interface Memory {
//	/**
//	 * <b>Heap</b>
//	 * <br>
//	 * Reserve a space in this memory and return the address of it.
//	 *
//	 * @return the address of the newly reserved memory.
//	 * @since 0.2.0 ~2021.01.31
//	 */
//	long allocate();
//
//	/**
//	 * <b>Heap</b>
//	 * <br>
//	 * Free the space at the given {@code address}.
//	 *
//	 * @param address the address of the space to be freed.
//	 * @return the previous value that was stored at the given {@code address}.
//	 * @throws IllegalArgumentException if the given {@code address} is less than or equal
//	 *                                  {@code 0}.
//	 * @throws IllegalStateException    if the given {@code address} is not allocated.
//	 * @since 0.2.0 ~2021.01.31
//	 */
//	Object free(long address);
//
//	/**
//	 * <b>Heap</b>
//	 * <br>
//	 * Get the value allocated to the given {@code address}.
//	 *
//	 * @param address the address of the value to be returned.
//	 * @return the value allocated to the given {@code address}. Or null if no such value
//	 * 		is allocated to the given {@code address}.
//	 * @throws IllegalArgumentException if the given {@code address} is less than or equal
//	 *                                  {@code 0}.
//	 * @throws IllegalStateException    if the given {@code address} is actually not
//	 *                                  allocated.
//	 * @since 0.2.0 ~2021.01.31
//	 */
//	Object get(long address);
//
//	/**
//	 * <b>Stack</b>
//	 * <br>
//	 * Peek the last value put in the stack.
//	 *
//	 * @return the last value put in the stack.
//	 * @throws IllegalStateException if the stack was empty.
//	 * @since 0.2.0 ~2021.01.31
//	 */
//	Object peek();
//
//	/**
//	 * <b>Stack</b>
//	 * <br>
//	 * Remove the last value put in the stack.
//	 *
//	 * @return the last value put in the stack.
//	 * @throws IllegalStateException if the stack was empty.
//	 * @since 0.2.0 ~2021.01.31
//	 */
//	Object pop();
//
//	/**
//	 * <b>Stack</b>
//	 * <br>
//	 * Push the given {@code value} to the stack.
//	 *
//	 * @param value the value to be pushed to the stack.
//	 * @since 0.2.0 ~2021.01.31
//	 */
//	void push(Object value);
//
//	/**
//	 * <b>Heap</b>
//	 * <br>
//	 * Store the given {@code value} to the given {@code address}.
//	 *
//	 * @param address the address to store the given {@code value} to.
//	 * @param value   the value to be store to the given {@code address}.
//	 * @return the previous value that was in the given {@code address}.
//	 * @throws IllegalArgumentException if the given {@code address} is less than or equal
//	 *                                  {@code 0}.
//	 * @throws IllegalStateException    if the given {@code address} is actually not
//	 *                                  allocated.
//	 * @since 0.2.0 ~2021.01.31
//	 */
//	Object put(long address, Object value);
//}
