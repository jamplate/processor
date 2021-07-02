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
package org.jamplate.internal.api;

import org.jamplate.api.Unit;
import org.jamplate.diagnostic.Diagnostic;
import org.jamplate.model.Memory;
import org.jetbrains.annotations.NotNull;

/**
 * A class containing event names constants.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.07.02
 */
public final class Event {
	/**
	 * The name of the event that gets triggered to handle the diagnostic messages.
	 * <br>
	 * This event does not require a compilation.
	 * <br>
	 * The parameter of this event is a mandatory {@link Diagnostic}.
	 *
	 * @since 0.3.0 ~2021.07.02
	 */
	@NotNull
	public static final String DIAGNOSTIC = "diagnostic";
	/**
	 * The name of the event that gets triggered to optimize a compilation.
	 * <br>
	 * This event requires a compilation.
	 * <br>
	 * The parameter of this event is a mandatory {@link Number}.
	 *
	 * @since 0.3.0 ~2021.07.02
	 */
	@NotNull
	public static final String OPTIMIZE = "optimize";
	/**
	 * The name of the event that gets triggered after analyzing.
	 * <br>
	 * This event requires a compilation.
	 * <br>
	 * The parameter of this event is an optional {@link Unit}.
	 *
	 * @since 0.3.0 ~2021.07.02
	 */
	@NotNull
	public static final String POST_ANALYZE = "post-analyze";
	/**
	 * The name of the event that gets triggered after compiling.
	 * <br>
	 * This event requires a compilation.
	 * <br>
	 * The parameter of this event is an optional {@link Unit}.
	 *
	 * @since 0.3.0 ~2021.07.02
	 */
	@NotNull
	public static final String POST_COMPILE = "post-compile";
	/**
	 * The name of the event that gets triggered before after an instruction.
	 * <br>
	 * This event requires a compilation.
	 * <br>
	 * The parameter of this event is a mandatory {@link Memory}.
	 *
	 * @since 0.3.0 ~2021.07.02
	 */
	@NotNull
	public static final String POST_EXEC = "post-exec";
	/**
	 * The name of the event that gets triggered after initializing a compilation.
	 * <br>
	 * This event requires a compilation.
	 * <br>
	 * The parameter of this event is an optional {@link Unit}.
	 *
	 * @since 0.3.0 ~2021.07.02
	 */
	@NotNull
	public static final String POST_INIT = "post-init";
	/**
	 * The name of the event that gets triggered after parsing.
	 * <br>
	 * This event requires a compilation.
	 * <br>
	 * The parameter of this event is an optional {@link Unit}.
	 *
	 * @since 0.3.0 ~2021.07.02
	 */
	@NotNull
	public static final String POST_PARSE = "post-parse";
	/**
	 * The name of the event that gets triggered before executing an instruction.
	 * <br>
	 * This event requires a compilation.
	 * <br>
	 * The parameter of this event is a mandatory {@link Memory}.
	 *
	 * @since 0.3.0 ~2021.07.02
	 */
	@NotNull
	public static final String PRE_EXEC = "pre-exec";

	/**
	 * Utility classes must not be initialized.
	 *
	 * @throws AssertionError when called.
	 * @since 0.3.0 ~2021.07.02
	 */
	private Event() {
		throw new AssertionError("No instance for you");
	}
}
