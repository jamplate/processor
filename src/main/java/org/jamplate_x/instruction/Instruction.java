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
//import org.jamplate.model.Reference;
//
///**
// * An instruction that can be executed at runtime.
// *
// * @author LSafer
// * @version 0.2.0
// * @since 0.2.0 ~2021.01.30
// */
//@FunctionalInterface
//public interface Instruction {
//	/**
//	 * Return the reference where this instruction was built from.
//	 *
//	 * @return the reference of this instruction. Or null if it has no reference.
//	 * @since 0.2.0 ~2021.01.31
//	 */
//	default Reference reference() {
//		return null;
//	}
//
//	/**
//	 * Execute this instruction. Invoking this method should execute one actual
//	 * instruction at most. (delegating/intermediate instructions are not counted)
//	 *
////	 * @param environment the process environment instance executing this instruction.
//	 * @return the next instruction. or null to fall back to the caller.
//	 * @throws NullPointerException if the given {@code environment} is null.
//	 * @since 0.2.0 ~2021.01.30
//	 */
//	Instruction execute(/*Environment environment*/);
//}
