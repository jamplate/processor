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
//package org.jamplate.model.link;
//
//import org.jamplate.model.element.Element;
//
///**
// * @author LSafer
// * @version 0.2.0
// * @since 0.2.0 ~2021.02.15
// */
//public interface Link {
//	/**
//	 * The node hosting the link.
//	 *
//	 * @return the node hosting the link.
//	 */
//	Element node();
//
//	/**
//	 * A link opposite to this link. (might be constant)
//	 *
//	 * @return the link opposite to this link.
//	 * @since 0.2.0 ~2021.02.28
//	 */
//	Link opposite();
//
//	/**
//	 * The node that got linked to the host node by this link.
//	 *
//	 * @return the other node.
//	 * @since 0.2.0 ~2021.02.28
//	 */
//	Element other();
//
//	/**
//	 * A constant string describing what kind of relation the other node is related to the
//	 * host node.
//	 *
//	 * @return the type of this link.
//	 * @since 0.2.0 ~2021.02.28
//	 */
//	String type();
//
//	//	enum Type {
//	//		/**
//	//		 * A link type where the host node is above the other node.
//	//		 *
//	//		 * @since 0.2.0 ~2021.02.28
//	//		 */
//	//		ABOVE("BELOW"),
//	//		/**
//	//		 * A link type where the host node is previous to the other node.
//	//		 */
//	//		PREVIOUS("NEXT"),
//	//		NEXT("PREVIOUS"),
//	//		BELOW("ABOVE"),
//	//		REFERRER("REF"),
//	//		REFERENT("REFERENCE");
//	//
//	//		private final String opposite;
//	//
//	//		Type(String opposite) {
//	//			Objects.requireNonNull(opposite, "opposite");
//	//			this.opposite = opposite;
//	//		}
//	//	}
//}
////	/**
////	 * Evaluate this link with the given {@code element} if the given {@code element}
////	 * satisfies this link. If the given {@code element} is null. Then, this link will
////	 * return the element it has. Otherwise, it will try to take the given {@code
////	 * element}.
////	 *
////	 * @param element the element to evaluate with.
////	 * @return if the given {@code element} is null. Then, the element this link has.
////	 * 		Otherwise, null.
////	 * @throws IllegalStateException if this link prohibit further evaluating.
////	 * @since 0.2.0 ~2021.02.15
////	 */
////	Node evaluate(Node element);
//
//// * A function that carries information about an element either available to be linked or
////		 * expected to be linked. An "Available to be Linked" element is an element known at the
////		 * creation of the link and anyone can have it. A "Expected to be linked" element is an
////		 * element required to exist but is unknown to the creator of the link. Thus, the creator
////		 * of the link provides a callback to it when it exists.
