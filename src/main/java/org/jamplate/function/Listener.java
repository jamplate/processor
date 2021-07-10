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

import org.jamplate.unit.Event;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Iterator;

/**
 * A function that get invoked when an event occurred while processing a compilation.
 * <br><br>
 * <strong>Members</strong>
 * <ul>
 *     <li>{@link Listener}[]</li>
 * </ul>
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.07.02
 */
@FunctionalInterface
public interface Listener extends Iterable<Listener> {
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
		public void trigger(@NotNull Event event) {
		}
	};

	/**
	 * Return an immutable iterator iterating over sub-listeners of this listener.
	 *
	 * @return an iterator iterating over the sub-listeners of this listener.
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	@Override
	default Iterator<Listener> iterator() {
		return Collections.emptyIterator();
	}

	/**
	 * Invoke this listener because of the occurrence of an event with the given {@code
	 * event} name.
	 *
	 * @param event the name of the event occurred.
	 * @throws NullPointerException if the given {@code event} is null.
	 * @since 0.3.0 ~2021.07.02
	 */
	@Contract(mutates = "this,param")
	void trigger(@NotNull Event event);
}
