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
//package org.cufyplate.element.value.operator;
//
//import org.cufyplate.element.value.ValueElement;
//import org.jamplate.model.element.AbstractElement;
//import org.jamplate.model.element.Element;
//import org.jamplate.model.instruction.FallbackInstruction;
//import org.jamplate.model.instruction.Instruction;
//import org.jamplate.model.Reference;
//import org.jamplate.runtime.envirnoment.Environment;
//
///**
// * @author LSafer
// * @version 0.0.0
// * @since 0.0.0 ~2021.01.31
// */
//public abstract class AbstractOperatorElement extends AbstractElement implements OperatorElement {
//	@SuppressWarnings("JavaDoc")
//	private static final long serialVersionUID = -1489921592885667987L;
//
//	/**
//	 * The argument element.
//	 *
//	 * @since 0.2.0 ~2021.01.31
//	 */
//	protected ValueElement argument;
//	/**
//	 * The host element.
//	 *
//	 * @since 0.2.0 ~2021.01.31
//	 */
//	protected ValueElement element;
//
//	/**
//	 * Construct a new element with no reference to its source.
//	 *
//	 * @since 0.2.0 ~2021.01.31
//	 */
//	protected AbstractOperatorElement() {
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
//	protected AbstractOperatorElement(Reference reference) {
//		super(reference);
//	}
//
//	@Override
//	public Instruction compile(Environment environment) {
//		Instruction element = environment.compile(this.element);
//		Instruction argument = environment.compile(this.argument);
//		//noinspection OverlyLongLambda
//		return new FallbackInstruction(
//				element,
//				env -> {
//					Object ext = env.memory().pop();
//
//					return new FallbackInstruction(
//							argument,
//							env2 -> {
//								Object ext2 = env2.memory().pop();
//
//								env2.memory().push(this.operate(ext, ext2));
//								return null;
//							}
//					);
//				}
//		);
//	}
//
//	@Override
//	public void put(Element element) {
//
//	}
//
//
//	protected abstract Object operate(Object object, Object argument);
//}
