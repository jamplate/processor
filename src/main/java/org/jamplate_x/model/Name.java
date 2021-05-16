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
//package org.jamplate.model;
//
//import java.io.Serializable;
//import java.util.Objects;
//
///**
// * A representation of the name of an item. The normal form can be accessed via {@link
// * #toString}. The qualified form can be accessed via {@link #toQualifiedString()}. The
// * simple form can be accessed via {@link #toSimpleString()}.
// *
// * @author LSafer
// * @version 0.2.0
// * @since 0.2.0 ~2021.02.15
// */
//public final class Name implements Serializable, Comparable<Name> {
//	@SuppressWarnings("JavaDoc")
//	private static final long serialVersionUID = 2471464850777801270L;
//
//	/**
//	 * The normal form of this name.
//	 *
//	 * @since 0.2.0 ~2021.02.15
//	 */
//	private final String normal;
//	/**
//	 * The qualified form of this name.
//	 *
//	 * @since 0.2.0 ~2021.02.15
//	 */
//	private final String qualified;
//	/**
//	 * The simple form of this name.
//	 *
//	 * @since 0.2.0 ~2021.02.15
//	 */
//	private final String simple;
//
//	/**
//	 * Construct a new name.
//	 *
//	 * @param qualified the qualified form of the constructed name.
//	 * @param normal    the normal form of the constructed name.
//	 * @param simple    the simple form of the constructed name.
//	 * @throws NullPointerException if the given {@code qualified} or {@code normal} or
//	 *                              {@code simple} is null.
//	 * @since 0.2.0 ~2021.02.15
//	 */
//	public Name(String qualified, String normal, String simple) {
//		Objects.requireNonNull(qualified, "qualified");
//		Objects.requireNonNull(normal, "normal");
//		Objects.requireNonNull(simple, "simple");
//		this.qualified = qualified;
//		this.normal = normal;
//		this.simple = simple;
//	}
//
//	/**
//	 * Construct a new name.
//	 *
//	 * @param qualified the qualified form of the constructed name.
//	 * @param normal    the simple and normal form of the constructed name.
//	 * @throws NullPointerException if the given {@code qualified} or {@code normal} or
//	 *                              {@code simple} is null.
//	 * @since 0.2.0 ~2021.02.15
//	 */
//	public Name(String qualified, String normal) {
//		this.qualified = qualified;
//		this.normal = normal;
//		this.simple = normal;
//	}
//
//	/**
//	 * Construct a new name.
//	 *
//	 * @param qualified the qualified, normal and simple form of the constructed name.
//	 * @throws NullPointerException if the given {@code qualified} or {@code normal} or
//	 *                              {@code simple} is null.
//	 * @since 0.2.0 ~2021.02.15
//	 */
//	public Name(String qualified) {
//		this.qualified = qualified;
//		this.normal = qualified;
//		this.simple = qualified;
//	}
//
//	@Override
//	public int compareTo(Name name) {
//		return this.qualified.compareTo(name.qualified);
//	}
//
//	/**
//	 * An object is equals to this name when that object is a name and its qualified form
//	 * equals the qualified form of this name.
//	 *
//	 * @param object the object to be matched.
//	 * @return true, if the given {@code object} is a name and equals this name.
//	 * @since 0.2.0 ~2021.02.15
//	 */
//	@Override
//	public boolean equals(Object object) {
//		if (object == this)
//			return true;
//		if (object instanceof Name) {
//			Name name = (Name) object;
//			return Objects.equals(name.qualified, this.qualified);
//		}
//
//		return false;
//	}
//
//	/**
//	 * The hashcode of a name is the hash code of its qualified form.
//	 *
//	 * @return the hash code of this name.
//	 * @since 0.2.0 ~2021.02.15
//	 */
//	@Override
//	public int hashCode() {
//		return this.qualified.hashCode();
//	}
//
//	/**
//	 * Returns the normal form of this name.
//	 *
//	 * @return the normal form of this name.
//	 * @since 0.2.0 ~2021.02.15
//	 */
//	@Override
//	public String toString() {
//		return this.normal;
//	}
//
//	/**
//	 * Returns the qualified form of this name.
//	 *
//	 * @return the qualified form of this name.
//	 * @since 0.2.0 ~2021.02.15
//	 */
//	public String toQualifiedString() {
//		return this.qualified;
//	}
//
//	/**
//	 * Returns the simplest most user-friendly form of this name.
//	 *
//	 * @return the simplest form of this name.
//	 * @since 0.2.0 ~2021.02.15
//	 */
//	public String toSimpleString() {
//		return this.simple;
//	}
//}
