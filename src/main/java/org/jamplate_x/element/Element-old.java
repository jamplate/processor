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
//import org.jamplate.model.Name;
//import org.jamplate.model.instruction.Instruction;
//import org.jamplate.model.Reference;
//import org.jamplate.runtime.envirnoment.Environment;
//
//import java.io.Serializable;
//
///**
// * A visualization of an element.
// * <br>
// * Note: elements are reference based. So, a typical element will store the elements it
// * requires.
// *
// * @author LSafer
// * @version 0.2.0
// * @since 0.2.0 ~2021.01.17
// */
//public interface Element extends Serializable {
//	/**
//	 * An element is equals to another object if it is the same instance.
//	 * <pre>
//	 *     equals = this == object
//	 * </pre>
//	 *
//	 * @param object the object to be matched.
//	 * @return if the given {@code object} is this.
//	 * @since 0.2.0 ~2021.01.31
//	 */
//	@Override
//	boolean equals(Object object);
//
//	/**
//	 * Calculate the hash code of this element.
//	 * <pre>
//	 *     hashCode = &lt;IdentityHashCode&gt;
//	 * </pre>
//	 *
//	 * @return the hash code of this element.
//	 */
//	@Override
//	int hashCode();
//
//	/**
//	 * Return a string representation of this element.
//	 * <pre>
//	 *     toString = &lt;ClassSimpleName&gt; (&lt;ReferenceToString&gt;)
//	 * </pre>
//	 *
//	 * @return a string representation of this sketch.
//	 * @since 0.2.0 ~2021.01.31
//	 */
//	@Override
//	String toString();
//
//	/**
//	 * Compile this element into an instruction. If this element requires another element
//	 * then if that element has not been compiled then that element will be compiled.
//	 * Otherwise, the compiled instruction of that element will be used instead.
//	 *
//	 * @param environment an environment mapping elements.
//	 * @return turn this element into an instruction.
//	 * @since 0.2.0 ~2021.01.30
//	 */
//	@Deprecated
//	Instruction compile(Environment environment);
//
//	/**
//	 * The name of this element.
//	 *
//	 * @return the name of this element.
//	 */
//	Name name();
//
//	/**
//	 * Put the given {@code element} to be a member of this element.
//	 *
//	 * @param element the element to be put to this element.
//	 * @throws NullPointerException          if the given {@code element} is null.
//	 * @throws IllegalArgumentException      if some aspect of the given {@code element}
//	 *                                       is not accepted by this element.
//	 * @throws UnsupportedOperationException if the given {@code element} cannot be put to
//	 *                                       this element.
//	 * @since 0.2.0 ~2021.01.30
//	 */
//	void put(Element element);
//
//	/**
//	 * The reference this element was built from.
//	 *
//	 * @return the reference of this element. Or null if no reference.
//	 * @since 0.2.0 ~2021.01.31
//	 */
//	Reference reference();
//}
