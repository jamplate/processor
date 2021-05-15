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
//package org.cufyplate.element.value.constant;
//
//import org.jamplate.model.element.AbstractElement;
//import org.jamplate.model.instruction.Instruction;
//import org.jamplate.model.Reference;
//import org.jamplate.runtime.envirnoment.Environment;
//
///**
// * @author LSafer
// * @version 0.0.0
// * @since 0.0.0 ~2021.02.01
// */
//public abstract class AbstractConstantElement extends AbstractElement implements ConstantElement {
//	protected final Object value;
//
//	/**
//	 * Construct a new element with no reference to its source.
//	 *
//	 * @since 0.2.0 ~2021.01.31
//	 */
//	protected AbstractConstantElement(Object value) {
//		this.value = value;
//	}
//
//	/**
//	 * Construct a new element with the given {@code reference} as the reference of its
//	 * source.
//	 *
//	 * @param reference the reference of the constructed element.
//	 * @throws NullPointerException if the given {@code reference} is null.
//	 * @since 0.2.0 ~2021.01.31
//	 */
//	protected AbstractConstantElement(Reference reference, Object value) {
//		super(reference);
//		this.value = value;
//	}
//
//	@Override
//	public Instruction compile(Environment environment) {
//		return env -> {
//			env.memory().push(this.value);
//			return null;
//		};
//	}
//}
