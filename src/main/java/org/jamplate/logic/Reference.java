/*
 *	Copyright 2020 Cufy
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
package org.jamplate.logic;

import org.cufy.preprocessor.link.Logic;
import org.cufy.preprocessor.invoke.Memory;
import org.cufy.preprocessor.AbstractParser;
import org.cufy.preprocessor.Poll;
import org.jamplate.util.Values;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * A logic that evaluates the value of a pre-defined address.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2020.09.16
 */
public class Reference extends AbstractValue {
	/**
	 * Targets {@code reference} statements.
	 * <ul>
	 *     <li>{@code VALUE} the address of the reference</li>
	 * </ul>
	 *
	 * @since 0.0.b ~2020.10.01
	 */
	public static final Pattern PATTERN = Pattern.compile("(?<VALUE>\\w[\\w\\d]*)");

	/**
	 * Construct a new reference with its {@link #value} not initialized.
	 *
	 * @since 0.0.b ~2020.10.04
	 */
	public Reference() {
	}

	/**
	 * Construct a new definition logic that evaluates to the value defined to the given {@code
	 * value}.
	 *
	 * @param value the value where the value of this definition is stored at.
	 * @throws NullPointerException if the given {@code value} is null.
	 * @since 0.0.1 ~2020.09.16
	 */
	public Reference(String value) {
		super(value);
	}

	@Override
	public Object evaluate(Memory memory) {
		Objects.requireNonNull(memory, "scope");
		Logic logic = memory.find(this.value);
		return logic == null || logic == this ?
			   this.value :
			   logic.evaluate(memory);
	}

	@Override
	public String toString() {
		return this.value;
	}

	/**
	 * The default parser class of the {@link Literal} logic.
	 *
	 * @author LSafer
	 * @version 0.0.b
	 * @since 0.0.b ~2020.10.01
	 */
	public static class Parser extends AbstractParser.AbstractVote<Reference> {
		@Override
		public boolean parse(List poll) {
			return Poll.iterate(poll, Values.parse(
					Reference.PATTERN,
					Reference::new
			));
		}
	}
}
//			Polls.iterateMatches(
//					poll,
//					Reference.PATTERN,
//					Values.parse(Reference::new)
//			);
