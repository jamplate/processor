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
package org.jamplate.impl.listener;

import org.jamplate.api.Event;
import org.jamplate.function.Listener;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A listener that sequentially execute other listeners in a pre-specified order when
 * called.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.07.02
 */
public class SequentialListener implements Listener {
	/**
	 * The listeners in order.
	 *
	 * @since 0.3.0 ~2021.07.02
	 */
	@NotNull
	protected final List<Listener> listeners;

	/**
	 * Construct a new listener that executes the given listeners in order when called.
	 * <br>
	 * Null listeners in the array will be ignored.
	 *
	 * @param listeners the listeners to be executed when the constructed listener gets
	 *                  executed.
	 * @throws NullPointerException if the given {@code listeners} is null.
	 * @since 0.3.0 ~2021.07.02
	 */
	public SequentialListener(@Nullable Listener @NotNull ... listeners) {
		Objects.requireNonNull(listeners, "listeners");
		this.listeners = Arrays.stream(listeners)
							   .filter(Objects::nonNull)
							   .collect(Collectors.toList());
	}

	/**
	 * Construct a new listener that executes the given listeners in order when called.
	 * <br>
	 * Null listeners in the list will be ignored.
	 *
	 * @param listeners the listeners to be executed when the constructed listener gets
	 *                  executed.
	 * @throws NullPointerException if the given {@code listeners} is null.
	 * @since 0.3.0 ~2021.07.02
	 */
	public SequentialListener(@NotNull List<Listener> listeners) {
		Objects.requireNonNull(listeners, "listeners");
		this.listeners = new ArrayList<>();
		for (Listener listener : listeners)
			if (listener != null)
				this.listeners.add(listener);
	}

	/**
	 * Construct a new listener that executes the given listeners in order.
	 * <br>
	 * Null listeners in the array will be ignored.
	 *
	 * @param listeners the listeners to be executed when the constructed listener gets
	 *                  triggered.
	 * @return a new sequential listener that executes the given {@code listeners} in
	 * 		order.
	 * @throws NullPointerException if the given {@code listeners} is null.
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static SequentialListener sequential(@Nullable Listener @NotNull ... listeners) {
		return new SequentialListener(listeners);
	}

	/**
	 * Construct a new listener that executes the given listeners in order.
	 * <br>
	 * Null listeners in the list will be ignored.
	 *
	 * @param listeners the listeners to be executed when the constructed listener gets
	 *                  triggered.
	 * @return a new sequential listener that executes the given {@code listeners} in
	 * 		order.
	 * @throws NullPointerException if the given {@code listeners} is null.
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static SequentialListener sequential(@NotNull List<Listener> listeners) {
		return new SequentialListener(listeners);
	}

	@NotNull
	@Override
	public Iterator<Listener> iterator() {
		return Collections.unmodifiableList(this.listeners).iterator();
	}

	@Override
	public void trigger(@NotNull Event event) {
		Objects.requireNonNull(event, "event");
		for (Listener listener : this.listeners)
			listener.trigger(event);
	}
}
