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
package org.jamplate.diagnostic;

import org.jetbrains.annotations.Nullable;

/**
 * An interface to store and print notes, warnings and errors to the console.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.16
 */
public interface Diagnostic {
	/**
	 * Print all the held messages.
	 *
	 * @since 0.2.0 ~2021.05.17
	 */
	void flush();

	/**
	 * Add the given message to be printed.
	 *
	 * @param message the message to be printed.
	 * @since 0.2.0 ~2021.05.17
	 */
	void print(@Nullable Message message);
}
