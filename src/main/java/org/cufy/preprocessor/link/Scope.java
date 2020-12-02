///*
// *	Copyright 2020 Cufy
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
//package org.cufy.preprocessor.link;
//
//import org.cufy.text.Element;
//import org.cufy.text.Memory;
//
//import java.io.IOException;
//
///**
// * A scope is an invocable that processes a simple part of a {@code jamplate} code. Scopes build up
// * by linking them to each other.
// * <p>
// * The jamplate building using scopes will be in 3 stages.
// * <ol>
// *     <li>Constructing scopes</li>
// *     <li>Linking scopes</li>
// *     <li>Invoking scopes</li>
// * </ol>
// * <p>
// * Relations:
// * <ul>
// *     <li>Previous: {@link Scope}</li>
// *     <li>Next: {@link Scope}</li>
// * </ul>
// *
// * @param <V> the type of the value logic of this scope.
// * @author LSafer
// * @version 0.0.1
// * @since 0.0.1 ~2020.09.17
// */
//public interface Scope<V extends Logic> extends Element<Scope, V>, Memory {
//	/**
//	 * Get the definition stored in this scope at the given {@code address}. If this scope do not
//	 * have the given {@code address}, It will check the scope before it (if its previous scope has
//	 * been set).
//	 *
//	 * @param address the address to get the definition stored at it.
//	 * @return the definition stored at the given {@code address}.
//	 * @throws NullPointerException if the given {@code address} is null.
//	 * @since 0.0.1 ~2020.09.16
//	 */
//	@Override
//	Logic find(String address);
//
//	/**
//	 * Invoke this scope with the given {@code memory} and append the output to the given {@code
//	 * appendable}.
//	 *
//	 * @param appendable the output appendable to append the result of invoking this scope to.
//	 * @param memory     the memory to be used.
//	 * @return the appendable result of invoking this scope (usually the given {@code appendable}
//	 * 		itself).
//	 * @throws IOException          if any I/O exception occurs.
//	 * @throws NullPointerException if the given {@code appendable} or {@code memory} is null.
//	 * @since 0.0.b ~2020.10.10
//	 */
//	Appendable invoke(Appendable appendable, Memory memory) throws IOException;
//}
