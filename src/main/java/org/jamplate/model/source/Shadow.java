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
package org.jamplate.model.source;

import java.io.File;

/**
 * A shadow is a non-changing capture of a file (non-directory) at a time.
 *
 * @author LSafer
 * @version 0.0.2
 * @since 0.0.2 ~2021.01.07
 */
public interface Shadow {
	/**
	 * Determine if the given {@code other} is equals to this shadow.
	 * <br>
	 * The given {@code other} will equal this shadow, if the given {@code other} is a shadow and have the same {@link #file()} as this shadow.
	 *
	 * @param other the other shadow to be compared.
	 * @return true, if and only if the given object is a shadow and have the same file as this shadow.
	 * @since 0.0.2 ~2021.01.8
	 */
	@Override
	boolean equals(Object other);

	/**
	 * Calculate the hashCode of this shadow.
	 * <pre>
	 *     hashCode = this.{@link #file()}.{@link File#hashCode() hashCode()}
	 * </pre>
	 *
	 * @return the calculated hashCode of this shadow.
	 */
	@Override
	int hashCode();

	/**
	 * Return a string representation of this shadow.
	 * <br>
	 * The string shall follow the following pattern:
	 * <pre>
	 *     ~&lt;{@link #file() File}&gt;
	 * </pre>
	 *
	 * @return a string representation of this shadow.
	 * @since 0.0.2 ~2021.01.8
	 */
	@Override
	String toString();

	/**
	 * Return the content of this shadow.
	 *
	 * @return the content of this shadow.
	 * @since 0.0.2 ~2021.01.8
	 */
	String content();

	/**
	 * The actual file that this shadow is capturing.
	 *
	 * @return the file of this shadow.
	 * @since 0.0.2 ~2021.01.7
	 */
	File file();

	/**
	 * The length of the content of this shadow.
	 *
	 * @return the length of this.
	 * @since 0.0.2 ~2021.01.7
	 */
	int length();
}
