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
//import java.util.function.Consumer;
//
///**
// * @author LSafer
// * @version 0.0.0
// * @since 0.0.0 ~2021.02.15
// */
//public class CallbackLink implements Link {
//	protected final Consumer<Element> callback;
//	protected final String qualifiedName;
//	protected Element element;
//
//	public CallbackLink(String qualifiedName, Consumer<Element> callback) {
//		Objects.requireNonNull(qualifiedName, "qualifiedName");
//		Objects.requireNonNull(callback, "callback");
//		this.qualifiedName = qualifiedName;
//		this.callback = callback;
//	}
//
//	@Override
//	public Element element() {
//		return null;
//	}
//
//	@Override
//	public void evaluate(Link link) {
//		if (link != null && Objects.equals(link.qualifiedName(), this.qualifiedName)) {
//			Element element = link.element();
//
//			if (element != null) {
//				if (this.element != null) {
//					Diagnostic.appendError(
//							"Duplicate token " + this.qualifiedName,
//							this.element.reference(),
//							element.reference()
//					);
//					throw new IllegalStateException(
//							"Link evaluated twice" + this.qualifiedName
//					);
//				}
//
//				this.callback.accept(element);
//				this.element = element;
//			}
//		}
//	}
//
//	@Override
//	public String qualifiedName() {
//		return this.qualifiedName;
//	}
//}
