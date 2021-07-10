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
package org.jamplate.util;

import org.jamplate.model.Reference;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Reference utilities.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.15
 */
public final class References {
	/**
	 * Utility classes must not be initialized.
	 *
	 * @throws AssertionError when called.
	 * @since 0.3.0 ~2021.06.15
	 */
	private References() {
		throw new AssertionError("No instance for you");
	}

	/**
	 * Returns a reference that starts after the end of the given {@code start} and ends
	 * before the start of the given {@code end}.
	 * <pre>
	 *     start = start.end
	 *     end = end.start
	 * </pre>
	 *
	 * @param start the reference the returned reference will be starting after its end.
	 * @param end   the reference the returned reference will be ending before its start.
	 * @return the reference as described above.
	 * @throws NullPointerException     if the given {@code start} or {@code end} is
	 *                                  null.
	 * @throws IllegalArgumentException if the end of the given {@code start} is after the
	 *                                  start of the given {@code end}.
	 * @since 0.3.0 ~2021.06.15
	 */
	@NotNull
	@Contract(pure = true)
	public static Reference exclusive(@NotNull Reference start, @NotNull Reference end) {
		Objects.requireNonNull(start, "start");
		Objects.requireNonNull(end, "end");
		int position = start.position() + start.length();
		int length = end.position() - position;
		if (length < 0)
			throw new IllegalArgumentException("start.end > end.start");
		return new Reference(position, length);
	}

	/**
	 * Returns a reference that starts after the end of the given {@code start} and ends
	 * before the start of the given {@code end}.
	 * <pre>
	 *     start = start.end
	 *     end = end.start
	 * </pre>
	 *
	 * @param start the reference the returned reference will be starting after its end.
	 * @param end   the reference the returned reference will be ending before its start.
	 * @return the reference as described above.
	 * @throws NullPointerException     if the given {@code start} or {@code end} is
	 *                                  null.
	 * @throws IllegalArgumentException if the end of the given {@code start} is after the
	 *                                  start of the given {@code end}.
	 * @since 0.3.0 ~2021.06.15
	 */
	@NotNull
	@Contract(pure = true)
	public static Reference exclusive(@NotNull Tree start, @NotNull Tree end) {
		Objects.requireNonNull(start, "start");
		Objects.requireNonNull(end, "end");
		return References.exclusive(start.getReference(), end.getReference());
	}

	/**
	 * Returns a reference that starts after the end of the given {@code start} and ends
	 * at the end of the given {@code end}.
	 * <pre>
	 *     start = start.end
	 *     end = end.end
	 * </pre>
	 *
	 * @param start the reference the returned reference will be starting after its end.
	 * @param end   the reference the returned reference will be ending at its end.
	 * @return the reference as described above.
	 * @throws NullPointerException     if the given {@code start} or {@code end} is
	 *                                  null.
	 * @throws IllegalArgumentException if the end of the given {@code start} is after the
	 *                                  end of the given {@code end}.
	 * @since 0.3.0 ~2021.06.24
	 */
	@NotNull
	@Contract(pure = true)
	public static Reference exclusiveInclusive(@NotNull Reference start, @NotNull Reference end) {
		Objects.requireNonNull(start, "start");
		Objects.requireNonNull(end, "end");
		int position = start.position() + start.length();
		int length = end.position() + end.length() - position;
		if (length < 0)
			throw new IllegalArgumentException("start.end > end.end");
		return new Reference(position, length);
	}

	/**
	 * Returns a reference that starts after the end of the given {@code start} and ends
	 * at the end of the given {@code end}.
	 * <pre>
	 *     start = start.end
	 *     end = end.end
	 * </pre>
	 *
	 * @param start the reference the returned reference will be starting after its end.
	 * @param end   the reference the returned reference will be ending at its end.
	 * @return the reference as described above.
	 * @throws NullPointerException     if the given {@code start} or {@code end} is
	 *                                  null.
	 * @throws IllegalArgumentException if the end of the given {@code start} is after the
	 *                                  end of the given {@code end}.
	 * @since 0.3.0 ~2021.06.24
	 */
	@NotNull
	@Contract(pure = true)
	public static Reference exclusiveInclusive(@NotNull Tree start, @NotNull Tree end) {
		Objects.requireNonNull(start, "start");
		Objects.requireNonNull(end, "end");
		return References.exclusiveInclusive(start.getReference(), end.getReference());
	}

	/**
	 * Return a reference that starts at the start of the given {@code start} and ends at
	 * the end of the given {@code end}.
	 * <pre>
	 *     start = start.start
	 *     end = end.end
	 * </pre>
	 *
	 * @param start the reference the returned reference will be starting at its start.
	 * @param end   the reference the returned reference will be ending at its end.
	 * @return the reference as described above.
	 * @throws NullPointerException     if the given {@code start} or {@code end} is
	 *                                  null.
	 * @throws IllegalArgumentException if the start of the given {@code start} is after
	 *                                  the end of the given {@code end}.
	 * @since 0.3.0 ~2021.06.15
	 */
	@NotNull
	@Contract(pure = true)
	public static Reference inclusive(@NotNull Reference start, @NotNull Reference end) {
		Objects.requireNonNull(start, "start");
		Objects.requireNonNull(end, "end");
		int position = start.position();
		int length = end.position() + end.length() - position;
		if (length < 0)
			throw new IllegalArgumentException("start.start > end.end");
		return new Reference(position, length);
	}

	/**
	 * Return a reference that starts at the start of the given {@code start} and ends at
	 * the end of the given {@code end}.
	 * <pre>
	 *     start = start.start
	 *     end = end.end
	 * </pre>
	 *
	 * @param start the reference the returned reference will be starting at its start.
	 * @param end   the reference the returned reference will be ending at its end.
	 * @return the reference as described above.
	 * @throws NullPointerException     if the given {@code start} or {@code end} is
	 *                                  null.
	 * @throws IllegalArgumentException if the start of the given {@code start} is after
	 *                                  the end of the given {@code end}.
	 * @since 0.3.0 ~2021.06.15
	 */
	@NotNull
	@Contract(pure = true)
	public static Reference inclusive(@NotNull Tree start, @NotNull Tree end) {
		Objects.requireNonNull(start, "start");
		Objects.requireNonNull(end, "end");
		return References.inclusive(start.getReference(), end.getReference());
	}

	/**
	 * Returns a reference that starts at the start of the given {@code start} and ends
	 * before the start of the given {@code end}.
	 * <pre>
	 *     start = start.start
	 *     end = end.start
	 * </pre>
	 *
	 * @param start the reference the returned reference will be starting at its start.
	 * @param end   the reference the returned reference will be ending before its start.
	 * @return the reference as described above.
	 * @throws NullPointerException     if the given {@code start} or {@code end} is
	 *                                  null.
	 * @throws IllegalArgumentException if the start of the given {@code start} is after
	 *                                  the start of the given {@code end}.
	 * @since 0.3.0 ~2021.06.24
	 */
	@NotNull
	@Contract(pure = true)
	public static Reference inclusiveExclusive(@NotNull Reference start, @NotNull Reference end) {
		Objects.requireNonNull(start, "start");
		Objects.requireNonNull(end, "end");
		int position = start.position();
		int length = end.position() - position;
		if (length < 0)
			throw new IllegalArgumentException("start.start > end.start");
		return new Reference(position, length);
	}

	/**
	 * Returns a reference that starts at the start of the given {@code start} and ends
	 * before the start of the given {@code end}.
	 * <pre>
	 *     start = start.start
	 *     end = end.start
	 * </pre>
	 *
	 * @param start the reference the returned reference will be starting at its start.
	 * @param end   the reference the returned reference will be ending before its start.
	 * @return the reference as described above.
	 * @throws NullPointerException     if the given {@code start} or {@code end} is
	 *                                  null.
	 * @throws IllegalArgumentException if the start of the given {@code start} is after
	 *                                  the start of the given {@code end}.
	 * @since 0.3.0 ~2021.06.24
	 */
	@NotNull
	@Contract(pure = true)
	public static Reference inclusiveExclusive(@NotNull Tree start, @NotNull Tree end) {
		Objects.requireNonNull(start, "start");
		Objects.requireNonNull(end, "end");
		return References.inclusiveExclusive(start.getReference(), end.getReference());
	}
}
