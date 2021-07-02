/*
 *	Copyright 2021 Cufy
 *
 *	Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *	You may obtain a copy of the License at
 *
 *	    http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	See the License for the specific language governing permissions and
 *	limitations under the License.
 */
package org.jamplate.model;

import org.jamplate.diagnostic.Diagnostic;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

/**
 * The environment is a unit holding all the data, managers and variables about a single
 * process.
 * <br>
 * The subclasses must support serialization.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.16
 */
public interface Environment extends Iterable<Compilation>, Serializable {
	/**
	 * An iterator iterating over the compilations in this environment.
	 * <br>
	 * The mutability of the iterator is mutable by default unless the implementation
	 * specifies otherwise.
	 *
	 * @return an iterator iterating over the compilations in this environment.
	 * @since 0.3.0 ~2021.06.24
	 */
	@NotNull
	@Override
	Iterator<Compilation> iterator();

	/**
	 * Get the compilation set to this environment with the given {@code document} or
	 * return {@code null} if no such compilation exist in this environment.
	 *
	 * @param document the document of the compilation to be returned.
	 * @return the compilation with the given {@code document} in this environment. Or
	 *        {@code null} if no such compilation exist.
	 * @throws NullPointerException if the given {@code document} is null.
	 * @since 0.2.0 ~2021.05.21
	 */
	@Nullable
	@Contract(pure = true)
	Compilation getCompilation(@NotNull Document document);

	/**
	 * Get the compilation registered in this environment with the document qualified with
	 * the given {@code name} or return {@code null} if no such compilation has been set
	 * in this environment.
	 *
	 * @param name the qualification name of the document of the compilation to be
	 *             returned.
	 * @return the compilation with the given {@code name} set to this environment. Or
	 *        {@code null} if no such compilation exist.
	 * @throws NullPointerException if the given {@code name} is null.
	 * @since 0.2.0 ~2021.05.21
	 */
	@Nullable
	@Contract(pure = true)
	Compilation getCompilation(@NotNull String name);

	/**
	 * Return the diagnostic manager in this environment.
	 *
	 * @return the diagnostic manager.
	 * @since 0.2.0 ~2021.05.17
	 */
	@NotNull
	@Contract(pure = true)
	Diagnostic getDiagnostic();

	/**
	 * Returns the meta-data map of this environment.
	 * <br>
	 * By default, the returned map will be a modifiable checked map. Unless, the class of
	 * this said otherwise.
	 *
	 * @return the meta-data map of this.
	 * @since 0.2.0 ~2021.05.21
	 */
	@NotNull
	@Contract(pure = true)
	Map<String, Object> getMeta();

	/**
	 * Associate the given {@code compilation} to the given {@code document} in this
	 * environment.
	 *
	 * @param document    the document to be associated to the given {@code compilation}.
	 * @param compilation the compilation.
	 * @throws NullPointerException if the given {@code document} or {@code compilation}
	 *                              is null.
	 * @since 0.2.0 ~2021.05.19
	 */
	@Contract(mutates = "this")
	void setCompilation(@NotNull Document document, @NotNull Compilation compilation);
}
