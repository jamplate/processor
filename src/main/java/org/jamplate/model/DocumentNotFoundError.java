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

import org.jetbrains.annotations.Nullable;

import java.io.IOError;

/**
 * An unchecked throwable equivalent to {@link DocumentNotFoundError}.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.15
 */
public class DocumentNotFoundError extends IOError {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -3082811406541644283L;

	/**
	 * Constructs a new instance of DocumentNotFoundError with the specified cause. The
	 * DocumentNotFoundError is created with the detail message of
	 * <tt>(cause==null ? null : cause.toString())</tt> (which typically
	 * contains the class and detail message of cause).
	 *
	 * @param cause The cause of this error, or <tt>null</tt> if the cause is not known
	 * @since 0.2.0 ~2021.05.19
	 */
	public DocumentNotFoundError(@Nullable Throwable cause) {
		super(cause);
	}
}
