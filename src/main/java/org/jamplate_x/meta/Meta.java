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
//package org.jamplate.model.meta;
//
//import java.util.NoSuchElementException;
//
///**
// * Meta data container. This container has 5 primary storages. See the below chart for
// * more details:
// * <br><br>
// * <table width="100%" style="margin: 20">
// *     <tr>
// *         <th>Storage</th>
// *         <th>Type</th>
// *         <th>Get</th>
// *         <th>Set</th>
// *         <th>Remove</th>
// *         <th>Check</th>
// *     </tr>
// *     <tr>
// *         <td>Structures</td>
// *         <td>{@link Object}</td>
// *         <td>{@link #get}</td>
// *         <td>{@link #set}</td>
// *         <td>{@link #remove}</td>
// *         <td>{@link #has}</td>
// *     </tr>
// *     <tr>
// *         <td>Literals</td>
// *         <td>{@link String}</td>
// *         <td>{@link #getString}</td>
// *         <td>{@link #setString}</td>
// *         <td>{@link #removeString}</td>
// *         <td>{@link #hasString}</td>
// *     </tr>
// *     <tr>
// *         <td>Numbers</td>
// *         <td>{@code long}</td>
// *         <td>{@link #getLong}</td>
// *         <td>{@link #setLong}</td>
// *         <td>{@link #removeLong}</td>
// *         <td>{@link #hasLong}</td>
// *     </tr>
// *     <tr>
// *         <td>Letters</td>
// *         <td>{@code char}</td>
// *         <td>{@link #getChar}</td>
// *         <td>{@link #setChar}</td>
// *         <td>{@link #removeChar}</td>
// *         <td>{@link #hasChar}</td>
// *     </tr>
// *     <tr>
// *         <td>Bytes</td>
// *         <td>{@code byte}</td>
// *         <td>{@link #getByte}</td>
// *         <td>{@link #setByte}</td>
// *         <td>{@link #removeByte}</td>
// *         <td>{@link #hasByte}</td>
// *     </tr>
// *     <tr>
// *         <td>Bits</td>
// *         <td>{@code boolean}</td>
// *         <td>{@link #getBoolean}</td>
// *         <td>{@link #setBoolean}</td>
// *         <td>{@link #removeBoolean}</td>
// *         <td>{@link #hasBoolean}</td>
// *     </tr>
// * </table>
// *
// * @author LSafer
// * @version 0.2.0
// * @since 0.2.0 ~2021.02.20
// */
//public interface Meta {
//	//get
//
//	/**
//	 * Get the meta of type {@code Object} with the given {@code name} in this meta.
//	 *
//	 * @param name the name of the meta to get.
//	 * @return the meta of type {@code Object} with the given {@code name} in this.
//	 * @throws NullPointerException   if the given {@code name} is null.
//	 * @throws NoSuchElementException if no such meta was found.
//	 * @since 0.2.0 ~2021.02.20
//	 */
//	Object get(String name);
//
//	/**
//	 * Get the meta of type {@code boolean} with the given {@code name} in this meta.
//	 *
//	 * @param name the name of the meta to get.
//	 * @return the meta of type {@code boolean} with the given {@code name} in this.
//	 * @throws NullPointerException   if the given {@code name} is null.
//	 * @throws NoSuchElementException if no such meta was found.
//	 * @since 0.2.0 ~2021.02.20
//	 */
//	boolean getBoolean(String name);
//
//	/**
//	 * Get the meta of type {@code byte} with the given {@code name} in this meta.
//	 *
//	 * @param name the name of the meta to get.
//	 * @return the meta of type {@code byte} with the given {@code name} in this.
//	 * @throws NullPointerException   if the given {@code name} is null.
//	 * @throws NoSuchElementException if no such meta was found.
//	 * @since 0.2.0 ~2021.02.20
//	 */
//	byte getByte(String name);
//
//	/**
//	 * Get the meta of type {@code char} with the given {@code name} in this meta.
//	 *
//	 * @param name the name of the meta to get.
//	 * @return the meta of type {@code char} with the given {@code name} in this.
//	 * @throws NullPointerException   if the given {@code name} is null.
//	 * @throws NoSuchElementException if no such meta was found.
//	 * @since 0.2.0 ~2021.02.20
//	 */
//	char getChar(String name);
//
//	/**
//	 * Get the meta of type {@code long} with the given {@code name} in this meta.
//	 *
//	 * @param name the name of the meta to get.
//	 * @return the meta of type {@code long} with the given {@code name} in this.
//	 * @throws NullPointerException   if the given {@code name} is null.
//	 * @throws NoSuchElementException if no such meta was found.
//	 * @since 0.2.0 ~2021.02.20
//	 */
//	long getLong(String name);
//
//	/**
//	 * Get the meta of type {@code String} with the given {@code name} in this meta.
//	 *
//	 * @param name the name of the meta to get.
//	 * @return the meta of type {@code String} with the given {@code name} in this.
//	 * @throws NullPointerException   if the given {@code name} is null.
//	 * @throws NoSuchElementException if no such meta was found.
//	 * @since 0.2.0 ~2021.02.20
//	 */
//	String getString(String name);
//
//	//has
//
//	/**
//	 * Return true, if this has a meta with type {@code Object} and has the given {@code
//	 * name}.
//	 *
//	 * @param name the name of the meta to be checked.
//	 * @return true, if this has such meta.
//	 * @throws NullPointerException if the given {@code name} is null.
//	 * @since 0.2.0 ~2021.02.20
//	 */
//	boolean has(String name);
//
//	/**
//	 * Return true, if this has a meta with type {@code boolean} and has the given {@code
//	 * name}.
//	 *
//	 * @param name the name of the meta to be checked.
//	 * @return true, if this has such meta.
//	 * @throws NullPointerException if the given {@code name} is null.
//	 * @since 0.2.0 ~2021.02.20
//	 */
//	boolean hasBoolean(String name);
//
//	/**
//	 * Return true, if this has a meta with type {@code byte} and has the given {@code
//	 * name}.
//	 *
//	 * @param name the name of the meta to be checked.
//	 * @return true, if this has such meta.
//	 * @throws NullPointerException if the given {@code name} is null.
//	 * @since 0.2.0 ~2021.02.20
//	 */
//	boolean hasByte(String name);
//
//	/**
//	 * Return true, if this has a meta with type {@code char} and has the given {@code
//	 * name}.
//	 *
//	 * @param name the name of the meta to be checked.
//	 * @return true, if this has such meta.
//	 * @throws NullPointerException if the given {@code name} is null.
//	 * @since 0.2.0 ~2021.02.20
//	 */
//	boolean hasChar(String name);
//
//	/**
//	 * Return true, if this has a meta with type {@code long} and has the given {@code
//	 * name}.
//	 *
//	 * @param name the name of the meta to be checked.
//	 * @return true, if this has such meta.
//	 * @throws NullPointerException if the given {@code name} is null.
//	 * @since 0.2.0 ~2021.02.20
//	 */
//	boolean hasLong(String name);
//
//	/**
//	 * Return true, if this has a meta with type {@code String} and has the given {@code
//	 * name}.
//	 *
//	 * @param name the name of the meta to be checked.
//	 * @return true, if this has such meta.
//	 * @throws NullPointerException if the given {@code name} is null.
//	 * @since 0.2.0 ~2021.02.20
//	 */
//	boolean hasString(String name);
//
//	//remove
//
//	/**
//	 * If any, remove the meta with type {@code Object} and the given {@code name} in
//	 * this.
//	 *
//	 * @param name the name of the meta to be removed.
//	 * @throws NullPointerException if the given {@code name} is null.
//	 * @since 0.2.0 ~2021.02.20
//	 */
//	void remove(String name);
//
//	/**
//	 * If any, remove the meta with type {@code boolean} and the given {@code name} in
//	 * this.
//	 *
//	 * @param name the name of the meta to be removed.
//	 * @throws NullPointerException if the given {@code name} is null.
//	 * @since 0.2.0 ~2021.02.20
//	 */
//	void removeBoolean(String name);
//
//	/**
//	 * If any, remove the meta with type {@code byte} and the given {@code name} in this.
//	 *
//	 * @param name the name of the meta to be removed.
//	 * @throws NullPointerException if the given {@code name} is null.
//	 * @since 0.2.0 ~2021.02.20
//	 */
//	void removeByte(String name);
//
//	/**
//	 * If any, remove the meta with type {@code char} and the given {@code name} in this.
//	 *
//	 * @param name the name of the meta to be removed.
//	 * @throws NullPointerException if the given {@code name} is null.
//	 * @since 0.2.0 ~2021.02.20
//	 */
//	void removeChar(String name);
//
//	/**
//	 * If any, remove the meta with type {@code long} and the given {@code name} in this.
//	 *
//	 * @param name the name of the meta to be removed.
//	 * @throws NullPointerException if the given {@code name} is null.
//	 * @since 0.2.0 ~2021.02.20
//	 */
//	void removeLong(String name);
//
//	/**
//	 * If any, remove the meta with type {@code String} and the given {@code name} in
//	 * this.
//	 *
//	 * @param name the name of the meta to be removed.
//	 * @throws NullPointerException if the given {@code name} is null.
//	 * @since 0.2.0 ~2021.02.20
//	 */
//	void removeString(String name);
//	//set
//
//	/**
//	 * Set the meta in this with type {@code Object} and the given {@code name} to be the
//	 * given {@code value}.
//	 *
//	 * @param name  the name of the meta to be set.
//	 * @param value the new value.
//	 * @throws NullPointerException if the given {@code name} or {@code value} is null.
//	 * @since 0.2.0 ~2021.02.20
//	 */
//	void set(String name, Object value);
//
//	/**
//	 * Set the meta in this with type {@code boolean} and the given {@code name} to be the
//	 * given {@code value}.
//	 *
//	 * @param name  the name of the meta to be set.
//	 * @param value the new value.
//	 * @throws NullPointerException if the given {@code name} is null.
//	 * @since 0.2.0 ~2021.02.20
//	 */
//	void setBoolean(String name, boolean value);
//
//	/**
//	 * Set the meta in this with type {@code byte} and the given {@code name} to be the
//	 * given {@code value}.
//	 *
//	 * @param name  the name of the meta to be set.
//	 * @param value the new value.
//	 * @throws NullPointerException if the given {@code name} is null.
//	 * @since 0.2.0 ~2021.02.20
//	 */
//	void setByte(String name, byte value);
//
//	/**
//	 * Set the meta in this with type {@code char} and the given {@code name} to be the
//	 * given {@code value}.
//	 *
//	 * @param name  the name of the meta to be set.
//	 * @param value the new value.
//	 * @throws NullPointerException if the given {@code name} is null.
//	 * @since 0.2.0 ~2021.02.20
//	 */
//	void setChar(String name, char value);
//
//	/**
//	 * Set the meta in this with type {@code long} and the given {@code name} to be the
//	 * given {@code value}.
//	 *
//	 * @param name  the name of the meta to be set.
//	 * @param value the new value.
//	 * @throws NullPointerException if the given {@code name} is null.
//	 * @since 0.2.0 ~2021.02.20
//	 */
//	void setLong(String name, long value);
//
//	/**
//	 * Set the meta in this with type {@code String} and the given {@code name} to be the
//	 * given {@code value}.
//	 *
//	 * @param name  the name of the meta to be set.
//	 * @param value the new value.
//	 * @throws NullPointerException if the given {@code name} or {@code value} is null.
//	 * @since 0.2.0 ~2021.02.20
//	 */
//	void setString(String name, String value);
//}
////boolean
////byte
////char
////X double <- float
////long <- int, short
