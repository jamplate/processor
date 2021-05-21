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
import org.jetbrains.annotations.UnmodifiableView;

import java.io.Serializable;
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
public interface Environment extends Serializable {
	/**
	 * If this environment has a compilation for the given {@code document}. Then this
	 * method will return that compilation. Otherwise, this method will created a new
	 * compilation for the given {@code document} and add it to this environment then
	 * return it.
	 *
	 * @param document the document to get a compilation for.
	 * @return the compilation for the given {@code document} in this environment. Or the
	 * 		newly created compilation if this environment was not already having a
	 * 		compilation for the given {@code document}.
	 * @throws NullPointerException if the given {@code document} is null.
	 * @since 0.2.0 ~2021.05.19
	 */
	@NotNull
	@Contract(mutates = "this")
	Compilation getCompilation(@NotNull Document document);

	/**
	 * Return an unmodifiable view of the compilations in this environment.
	 *
	 * @return an unmodifiable view of the compilations in this environment.
	 * @since 0.2.0 ~2021.05.17
	 */
	@NotNull
	@UnmodifiableView
	@Contract(pure = true)
	Map<Document, Compilation> getCompilations();

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
	 * Set the compilation for the given {@code document} to be the given {@code
	 * compilation}.
	 * <br>
	 * If this environment already has a compilation for the given {@code document}, this
	 * environment will replace that compilation with the given {@code compilation}.
	 *
	 * @param document    the document to set its compilation.
	 * @param compilation the compilation to be set.
	 * @throws NullPointerException if the given {@code document} or {@code compilation}
	 *                              is null.
	 * @since 0.2.0 ~2021.05.19
	 */
	@Contract(mutates = "this")
	void setCompilation(@NotNull Document document, @NotNull Compilation compilation);
}
