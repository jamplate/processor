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

import java.util.Objects;

/**
 * An enumeration of possible relations between sources.
 * <br>
 * The names is describing the other source (in THE source perspective.
 * <pre>
 * 		SAME
 * 			i == s <= j == e
 * 			...|~~~|...
 * 			...|~~~|...
 * 		FRAGMENT | CONTAINER
 * 			i < s <= e < j
 * 			.<--~~~-->.
 * 			...|~~~|...
 * 		START | AHEAD
 * 			i == s <= e < j
 * 			.|----~~~>.
 * 			.|--->.....
 * 		END | BEHIND
 * 			i < s <= e == j
 * 			.<~~~----|.
 * 			.....<---|.
 * 		OVERFLOW | UNDERFLOW
 * 			i < s < j < e
 * 			.<~~--|....
 * 			....|--~~>.
 * 		NEXT | PREVIOUS
 * 			i < j == s < e
 * 			.<---|.....
 * 			.....|--->.
 * 		AFTER | BEFORE
 * 			i <= j < s <= e
 * 			.<~~|......
 * 			......|~~>.
 * </pre>
 *
 * @author LSafer
 * @version 0.0.2
 * @since 0.0.2 ~2021.01.9
 */
public enum SourceRelation {
	/**
	 * <b>Same Source</b> {@link #SAME (opposite)} {@code (i == s <= j == e)}
	 * <br>
	 * When the source has the exact bounds as the other source.
	 * <pre>
	 *     ...|---|...
	 *     ...|---|...
	 * </pre>
	 *
	 * @since 0.0.2 ~2021.01.9
	 */
	SAME("SAME", false, true),

	/**
	 * <b>Fragment Source</b> {@link #CONTAINER (opposite)} {@code (i < s <= e < j)}
	 * <br>
	 * When the source has its bounds containing the bounds of the other source but do not touch.
	 * <pre>
	 *     .<------->.
	 *     ...|---|...
	 * </pre>
	 *
	 * @since 0.0.2 ~2021.01.9
	 */
	FRAGMENT("CONTAINER", false, false),
	/**
	 * <b>Containing Source</b> {@link #FRAGMENT (opposite)} {@code (s < i <= j < e)}
	 * <br>
	 * When the bounds of the source are contained in the bounds of the other source but do not touch.
	 * <pre>
	 *     ...|---|...
	 *     .<------->.
	 * </pre>
	 *
	 * @since 0.0.2 ~2021.01.9
	 */
	CONTAINER("FRAGMENT", true, false),

	/**
	 * <b>At The Start</b> {@link #AHEAD (opposite)} {@code (i == s <= e < j)}
	 * <br>
	 * When the source contains the other source at its start.
	 * <pre>
	 *     .|--->.....
	 *     .|------->.
	 * </pre>
	 *
	 * @since 0.0.2 ~2021.01.9
	 */
	START("AHEAD", false, false),
	/**
	 * <b>From The Start And Ahead</b> {@link #START (opposite)} {@code (s == i <= j < e)}
	 * <br>
	 * When the source is contained at the very start of the other source.
	 * <pre>
	 *     .|--->.....
	 *     .|------->.
	 * </pre>
	 *
	 * @since 0.0.2 ~2021.01.9
	 */
	AHEAD("START", true, false),

	/**
	 * <b>At The End</b> {@link #BEHIND (opposite)} {@code (i < s <= e == j)}
	 * <br>
	 * When the source contains the other source at its end.
	 * <pre>
	 *     .<-------|.
	 *     .....<---|.
	 * </pre>
	 *
	 * @since 0.0.2 ~2021.01.9
	 */
	END("BEHIND", false, false),
	/**
	 * <b>From The End And behind</b> {@link #END (opposite)} {@code (s < i <= j == e)}
	 * <br>
	 * When the source is contained at the very end of the other source.
	 * <pre>
	 *     .....<---|.
	 *     .<-------|.
	 * </pre>
	 *
	 * @since 0.0.2 ~2021.01.9
	 */
	BEHIND("END", true, false),

	/**
	 * <b>Overflowed Slice</b> {@link #UNDERFLOW (opposite)} {@code (i < s < j < e)}
	 * <br>
	 * When the first fragment of the source is before the other source but the second fragment is in it. (without any bound been exactly at another
	 * bound)
	 * <pre>
	 *     .<----|....
	 *     ....|---->.
	 * </pre>
	 *
	 * @since 0.0.2 ~2021.01.9
	 */
	OVERFLOW("UNDERFLOW", false, true),
	/**
	 * <b>Underflowed Slice</b> {@link #OVERFLOW (opposite)} {@code (s < i < e < j)}
	 * <br>
	 * When the first fragment of the source is in the other source but the second fragment is after it. (without any bound been exactly at another
	 * bound)
	 * <pre>
	 *     ....|---->.
	 *     .<----|....
	 * </pre>
	 *
	 * @since 0.0.2 ~2021.01.9
	 */
	UNDERFLOW("OVERFLOW", true, true),

	/**
	 * <b>Next Source</b> {@link #PREVIOUS (opposite)} {@code (i < j == s < e)}
	 * <br>
	 * When the source is followed immediately by the other source.
	 * <pre>
	 *     .<---|.....
	 *     .....|--->.
	 * </pre>
	 *
	 * @since 0.0.2 ~2021.01.9
	 */
	NEXT("PREVIOUS", false, false),
	/**
	 * <b>Previous Source</b> {@link #NEXT (opposite)} {@code (s < e == i < j)}
	 * <br>
	 * When the source has the other source immediately before it.
	 * <pre>
	 *     .....|--->.
	 *     .<---|.....
	 * </pre>
	 *
	 * @since 0.0.2 ~2021.01.9
	 */
	PREVIOUS("NEXT", false, false),

	/**
	 * <b>After The Source</b> {@link #BEFORE opposite} {@code (i <= j < s <= e)}
	 * <br>
	 * When the other source is after the source but not immediately.
	 * <pre>
	 *     .<--|......
	 *     ......|-->.
	 * </pre>
	 *
	 * @since 0.0.2 ~2021.01.9
	 */
	AFTER("BEFORE", false, false),
	/**
	 * <b>Before The Source</b> {@link #AFTER opposite} {@code (s <= e < i <= j)}
	 * <br>
	 * When the other source is before the source but not immediately.
	 * <pre>
	 *     ......|-->.
	 *     .<--|......
	 * </pre>
	 *
	 * @since 0.0.2 ~2021.01.9
	 */
	BEFORE("AFTER", false, false),
	;

	/**
	 * If this relation clashes with its {@link #opposite} relation.
	 *
	 * @since 0.0.2 ~2021.01.10
	 */
	private final boolean clash;
	/**
	 * If this relation is a dominant relation over its {@link #opposite} relation.
	 *
	 * @since 0.0.2 ~2021.01.10
	 */
	private final boolean dominant;
	/**
	 * The name of the opposite enum.
	 *
	 * @since 0.0.2 ~2021.01.9
	 */
	private final String opposite;

	/**
	 * Construct a new enum with the given {@code opposite} as the name of the opposite enum of it.
	 *
	 * @param opposite the name of the opposite enum.
	 * @param dominant if the constructed relation is dominant over its opposite relation.
	 * @param clash    if the constructed relation clashes with its opposite relation.
	 * @throws NullPointerException if the given {@code opposite} is null.
	 * @since 0.0.2 ~2021.01.10
	 */
	SourceRelation(String opposite, boolean dominant, boolean clash) {
		Objects.requireNonNull(opposite, "opposite");
		this.opposite = opposite;
		this.dominant = dominant;
		this.clash = clash;
	}

	/**
	 * Return true, if this relation clashes with its {@link #opposite()} relation.
	 *
	 * @return true, if this relation clashes with its opposite relation.
	 * @since 0.0.2 ~2021.01.10
	 */
	public boolean clash() {
		return this.clash;
	}

	/**
	 * Returns true, if this relation is dominant over its {@link #opposite()} relation.
	 *
	 * @return true, if this relation is dominant over its opposite relation.
	 * @since 0.0.2 ~2021.01.10
	 */
	public boolean dominant() {
		return this.dominant;
	}

	/**
	 * Get the opposite relation of this relation.
	 *
	 * @return the opposite relation. Or null if none.
	 * @since 0.0.2 ~2021.01.9
	 */
	public SourceRelation opposite() {
		return SourceRelation.valueOf(this.opposite);
	}
}
