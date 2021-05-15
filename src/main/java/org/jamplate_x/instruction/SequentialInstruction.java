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
//package org.jamplate.model.instruction;
//
//import java.util.Iterator;
//import java.util.Objects;
//
///**
// * An instruction with virtualized sequential instruction list execution. Every
// * instruction in the sequence falls back to the instruction after it.
// *
// * @author LSafer
// * @version 0.2.0
// * @since 0.2.0 ~2021.01.30
// */
//public class SequentialInstruction extends FallbackInstruction {
//	/**
//	 * Construct a new sequential instruction that will have the given {@code
//	 * instructions}.
//	 *
//	 * @param instructions the instructions the constructed instruction will iterate
//	 *                     through.
//	 * @throws NullPointerException if the given {@code instructions} is null.
//	 * @since 0.2.0 ~2021.01.30
//	 */
//	public SequentialInstruction(Iterable<Instruction> instructions) {
//		this(
//				Objects.requireNonNull(instructions, "instructions").iterator()
//		);
//	}
//
//	/**
//	 * Construct a new sequential instruction that executes the remaining instructions
//	 * remaining from the given {@code iterator}.
//	 *
//	 * @param iterator the iterator from the instructions collection.
//	 * @throws NullPointerException if the given {@code iterator} is null.
//	 * @since 0.2.0 ~2021.01.30
//	 */
//	public SequentialInstruction(Iterator<Instruction> iterator) {
//		super(
//				Objects.requireNonNull(iterator, "iterator").hasNext() ?
//				iterator.next() :
//				null
//				,
//				iterator.hasNext() ?
//				new SequentialInstruction(iterator) :
//				null
//		);
//	}
//}
