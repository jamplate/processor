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

import org.jamplate.model.Tree;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

/**
 * A diagnostic message containing the details about an error, warning or even a note.
 * <br><br>
 * <strong>Members</strong>
 * <ul>
 *     <li>criticalPoints: {@link Tree}[]</li>
 *     <li>errorKind: {@link String}</li>
 *     <li>exception?: {@link Throwable}</li>
 *     <li>messagePhrase: {@link String}</li>
 *     <li>priority: {@link String}</li>
 *     <li>stackTrace: {@link Tree}[]</li>
 *     <li>fetal: {@link Boolean}</li>
 * </ul>
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.17
 */
public interface Message extends Serializable {
	/**
	 * Return the critical points trees that caused the error represented by this
	 * message.
	 *
	 * @return a clone array of the critical points trees. Or an empty array if none.
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	@Contract(value = "->new", pure = true)
	Tree @NotNull [] getCriticalPoints();

	/**
	 * Return the kind of the error of this message.
	 *
	 * @return the kind of the error.
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	@Contract(pure = true)
	String getErrorKind();

	/**
	 * Return the exception caused this message.
	 *
	 * @return the exception of this message. Or {@code null} if none.
	 * @since 0.2.0 ~2021.05.31
	 */
	@Nullable
	@Contract(pure = true)
	Throwable getException();

	/**
	 * Return the message phrase.
	 *
	 * @return the message phrase.
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	@Contract(pure = true)
	String getMessagePhrase();

	/**
	 * Return the priority of this message.
	 *
	 * @return the priority of this message.
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	@Contract(pure = true)
	String getPriority();

	/**
	 * Return the trace of the error.
	 *
	 * @return the trace of the error, or an empty array if none.
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	@Contract(value = "->new", pure = true)
	Tree @NotNull [] getStackTrace();

	/**
	 * Return {@code true} if this message represent a fetal (unrecoverable) error.
	 *
	 * @return true, if this message represents a fetal error.
	 * @since 0.2.0 ~2021.05.31
	 */
	@Contract(pure = true)
	boolean isFetal();
}
