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
//import org.jamplate.runtime.diagnostic.Diagnostic;
//
//import java.util.Objects;
//
///**
// * A link with a pre-evaluated element. It always gives its element directly. It throws an
// * exception when an evaluation attempt occurs. (prohibits duplicate evaluations)
// *
// * @author LSafer
// * @version 0.2.0
// * @since 0.2.0 ~2021.02.15
// */
//public class EvaluateLink implements Link {
//	protected final Element element;
//	protected final String qualifiedName;
//
//	/**
//	 * Construct a new evaluating link with the given {@code qualifiedName} as its
//	 * qualified name and links to the given {@code element}.
//	 *
//	 * @param qualifiedName the qualified name of the link.
//	 * @param element the element to be
//	 */
//	public EvaluateLink(String qualifiedName, Element element) {
//		Objects.requireNonNull(qualifiedName, "qualifiedName");
//		Objects.requireNonNull(element, "element");
//		this.qualifiedName = qualifiedName;
//		this.element = element;
//	}
//
//	@Override
//	public Element element() {
//		return this.element;
//	}
//
//	@Override
//	public void evaluate(Link link) {
//		if (link != null && Objects.equals(this.qualifiedName, link.qualifiedName())) {
//			Element element = link.element();
//
//			if (element != null) {
//				Diagnostic.appendError(
//						"Duplicate token " + this.qualifiedName,
//						this.element.reference(),
//						element.reference()
//				);
//				throw new IllegalStateException(
//						"Link declared twice " + this.qualifiedName
//				);
//			}
//		}
//	}
//
//	@Override
//	public String qualifiedName() {
//		return this.qualifiedName;
//	}
//}
