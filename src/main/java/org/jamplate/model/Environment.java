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

import java.util.Set;

/**
 * The environment is a unit holding all the data, managers and variables about a single
 * process.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.16
 */
public interface Environment extends Iterable<Compilation> {
	/**
	 * Return all the compilations in this environment.
	 *
	 * @return the compilations in this environment.
	 * @since 0.2.0 ~2021.05.17
	 */
	@NotNull
	@Contract(pure = true)
	Set<Compilation> compilations();

	/**
	 * Return the diagnostic manager in this environment.
	 *
	 * @return the diagnostic manager.
	 * @since 0.2.0 ~2021.05.17
	 */
	@NotNull
	@Contract(pure = true)
	Diagnostic diagnostic();
}
