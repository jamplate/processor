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

/**
 * An enumeration of the allowed types of a diagnostic message.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.01.17
 */
public enum DiagnosticKind {
	/**
	 * A type for messages that get printed for internal debugging.
	 *
	 * @since 0.2.0 ~2021.01.17
	 */
	DEBUG,
	/**
	 * A type for messages that get printed to notify the user about the progress.
	 *
	 * @since 0.2.0 ~2021.01.24
	 */
	PROGRESS,
	/**
	 * A type for messages that get printed to notify the user about an update or an
	 * advice.
	 *
	 * @since 0.2.0 ~2021.01.17
	 */
	NOTE,
	/**
	 * A type for messages that get printed to warn the user about a potential error.
	 *
	 * @since 0.2.0 ~2021.01.17
	 */
	WARNING,
	/**
	 * A type for messages that get printed to tell the user more about an error to be or
	 * already thrown.
	 *
	 * @since 0.2.0 ~2021.01.17
	 */
	ERROR
}
