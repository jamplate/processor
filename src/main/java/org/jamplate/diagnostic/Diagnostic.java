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

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;
import java.io.Serializable;

/**
 * An interface to store and print notes, warnings and errors to the console.
 * <br><br>
 * <strong>Members</strong>
 * <ul>
 *     <li>{@link Message}[]</li>
 * </ul>
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.16
 */
public interface Diagnostic extends Iterable<Message>, Serializable {
	/**
	 * Flush the printed messages to the default print stream.
	 *
	 * @return this.
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	@Contract(value = "->this", mutates = "this")
	default Diagnostic flush() {
		//noinspection UseOfSystemOutOrSystemErr
		return this.flush(false, System.out, System.err);
	}

	/**
	 * Flush the printed messages to the default print stream.
	 *
	 * @param debug pass true to force debug mode.
	 * @return this.
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Diagnostic flush(boolean debug) {
		//noinspection UseOfSystemOutOrSystemErr
		return this.flush(debug, System.out, System.err);
	}

	/**
	 * Flush the printed messages to the default print stream.
	 *
	 * @param out the default output stream.
	 * @param err the error stream.
	 * @return this.
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	@Contract(value = "_,_->this", mutates = "this,param1,param2")
	default Diagnostic flush(PrintStream out, PrintStream err) {
		return this.flush(false, out, err);
	}

	/**
	 * Format the given {@code message}.
	 *
	 * @param message the message to be formatted.
	 * @return a string representation of the given {@code message} in the style of this
	 * 		diagnostic system.
	 * @throws NullPointerException if the given {@code message} is null.
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	default String format(@NotNull Message message) {
		return this.format(false, message);
	}

	/**
	 * Clear the message queue.
	 *
	 * @return this.
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	@Contract(value = "->this", mutates = "this")
	Diagnostic clear();

	/**
	 * Flush the printed messages to the default print stream.
	 *
	 * @param debug pass true to force debug mode.
	 * @param out   the default output stream.
	 * @param err   the error stream.
	 * @return this.
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	@Contract(value = "_,_,_->this", mutates = "this,param2,param3")
	Diagnostic flush(boolean debug, PrintStream out, PrintStream err);

	/**
	 * Format the given {@code message}.
	 *
	 * @param debug   pass ture to force debug mode.
	 * @param message the message to be formatted.
	 * @return a string representation of the given {@code message} in the style of this
	 * 		diagnostic system.
	 * @throws NullPointerException if the given {@code message} is null.
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	@Contract(value = "_,_->new", pure = true)
	String format(boolean debug, @NotNull Message message);

	/**
	 * Print the given {@code message}.
	 *
	 * @param message the message to be printed.
	 * @return this.
	 * @throws NullPointerException if the given {@code message} is null.
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	Diagnostic print(@NotNull Message message);
}
