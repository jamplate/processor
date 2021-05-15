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
//package org.jamplate.model.element;
//
//import org.jamplate.model.Reference;
//
//import java.util.Objects;
//
///**
// * An abstraction for the {@link Element} interface. Implementing the basic functionality
// * of an element.
// *
// * @author LSafer
// * @version 0.2.0
// * @since 0.2.0 ~2021.01.31
// */
//public abstract class AbstractElement implements Element {
//	@SuppressWarnings("JavaDoc")
//	private static final long serialVersionUID = 2076907574721903279L;
//
//	/**
//	 * The reference to the source of this element.
//	 *
//	 * @since 0.2.0 ~2021.01.31
//	 */
//	protected final Reference reference;
//
//	/**
//	 * Construct a new element with no reference to its source.
//	 *
//	 * @since 0.2.0 ~2021.01.31
//	 */
//	protected AbstractElement() {
//		this.reference = null;
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
//	protected AbstractElement(Reference reference) {
//		Objects.requireNonNull(reference, "reference");
//		this.reference = reference;
//	}
//
//	@Override
//	public boolean equals(Object object) {
//		return this == object;
//	}
//
//	@Override
//	public int hashCode() {
//		return System.identityHashCode(this);
//	}
//
//	@Override
//	public Reference reference() {
//		return this.reference;
//	}
//
//	@Override
//	public String toString() {
//		return this.getClass().getSimpleName() + " (" + this.reference + " )";
//	}
//}
