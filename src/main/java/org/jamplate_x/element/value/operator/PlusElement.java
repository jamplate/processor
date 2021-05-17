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
//import org.jamplate.model.Reference;
//import org.jamplate.runtime.diagnostic.Diagnostic;
//
///**
// * @author LSafer
// * @version 0.0.0
// * @since 0.0.0 ~2021.01.31
// */
//public class PlusElement extends AbstractOperatorElement {
//	/**
//	 * Construct a new element with no reference to its source.
//	 *
//	 * @since 0.2.0 ~2021.01.31
//	 */
//	public PlusElement() {
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
//	public PlusElement(Reference reference) {
//		super(reference);
//	}
//
//	@Override
//	protected Object operate(Object object, Object argument) {
//		if (object instanceof Number && argument instanceof Number)
//			return ((Number) object).longValue() + ((Number) argument).longValue();
//
//		Diagnostic.appendError(
//				"+ operator cannot be applied to " + object.getClass() + " " +
//				argument.getClass(),
//				this.reference
//		);
//		throw new IllegalStateException("operator+");
//	}
//}