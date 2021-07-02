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
package org.jamplate.function;

import org.jamplate.model.Compilation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A function that get invoked when an event occurred while processing a compilation.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.07.02
 */
@FunctionalInterface
public interface Listener {
	/**
	 * A listener that does nothing.
	 *
	 * @since 0.3.0 ~2021.07.02
	 */
	@NotNull
	Listener IDLE = new Listener() {
		@Override
		public String toString() {
			return "Listener.IDLE";
		}

		@Override
		public void trigger(@NotNull String event, @Nullable Compilation compilation, @Nullable Object parameter) {
		}
	};

	/**
	 * Invoke this listener because of the occurrence of an event with the given {@code
	 * event} name.
	 *
	 * @param event       the name of the event occurred.
	 * @param compilation the compilation the event occurred on.
	 * @param parameter   the invocation parameter.
	 * @throws NullPointerException if the given {@code event} is null.
	 * @since 0.3.0 ~2021.07.02
	 */
	@Contract(mutates = "param2,param3")
	void trigger(@NotNull String event, @Nullable Compilation compilation, @Nullable Object parameter);
}
