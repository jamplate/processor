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
// * An instruction wrapping another instruction with a fallback instruction.
// *
// * @author LSafer
// * @version 0.2.0
// * @since 0.2.0 ~2021.01.30
// */
//public class FallbackInstruction implements Instruction {
//	/**
//	 * The fallback instruction for the instruction this instruction is forking.
//	 *
//	 * @since 0.2.0 ~2021.01.30
//	 */
//	protected final Instruction fallback;
//	/**
//	 * The forked instruction. (the instruction this instruction is wrapping with a
//	 * fallback)
//	 *
//	 * @since 0.2.0 ~2021.01.30
//	 */
//	protected final Instruction instruction;
//
//	/**
//	 * Construct a new forking instruction that delegates to the given {@code fork} with
//	 * the given {@code next} as a fallback instruction.
//	 *
//	 * @param instruction the instruction to be forked. (null -> no fork)
//	 * @param fallback    the fallback instruction. (null -> no fallback)
//	 * @since 0.2.0 ~2021.01.30
//	 */
//	public FallbackInstruction(Instruction instruction, Instruction fallback) {
//		this.instruction = instruction;
//		this.fallback = fallback;
//	}
//
//	@Override
//	public Instruction execute(/*Environment environment*/) {
//		//		Objects.requireNonNull(environment, "environment");
//		Instruction next = /*this.instruction.execute(environment)*/null;
//		return next == null ?
//			   this.fallback :
//			   new FallbackInstruction(next, this.fallback);
//	}
//
//	@Override
//	public Reference reference() {
//		return this.instruction.reference();
//	}
//}
