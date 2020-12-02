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

import org.cufy.preprocessor.invoke.Memory;
import org.cufy.preprocessor.AbstractParser;
import org.cufy.preprocessor.Poll;
import org.jamplate.util.Values;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * A logic that holds a constant value.
 *
 * @author LSafer
 * @version 0.0.b
 * @since 0.0.b ~2020.10.04
 */
public class Constant extends AbstractValue {
	/**
	 * Target {@code constant} statements.
	 * <ul>
	 *     <li>{@code VALUE} the constant value</li>
	 * </ul>
	 *
	 * @since 0.0.b ~2020.10.05
	 */
	public static final Pattern PATTERN = Pattern.compile("(?<VALUE>(" +
														  "(?:(?:true)|(?:false))|" +
														  "(?:(?:\\d[.]?\\d)+)" +
														  "))");

	/**
	 * Construct a new constant with its {@link #value} not initialized.
	 *
	 * @since 0.0.b ~2020.10.04
	 */
	public Constant() {
	}

	/**
	 * Construct a new constant with its {@link #value} initialized to the given {@code value}.
	 *
	 * @param value the value of the constructed constant.
	 * @throws NullPointerException if the given {@code value} is null.
	 * @since 0.0.b ~2020.10.04
	 */
	public Constant(String value) {
		super(value);
	}

	@Override
	public Object evaluate(Memory memory) {
		Objects.requireNonNull(memory, "memory");
		//auto evaluated :)
		return this.value;
	}

	@Override
	public String toString() {
		return this.value;
	}

	/**
	 * The default parser class of the {@link Constant} logic.
	 *
	 * @author LSafer
	 * @version 0.0.b
	 * @since 0.0.b ~2020.10.05
	 */
	public static class Parser extends AbstractParser.AbstractVote<Constant> {
		@Override
		public boolean parse(List poll) {
			return Poll.iterate(poll, Values.parse(
					Constant.PATTERN,
					Constant::new
			));
		}
	}
}
//			Polls.iterateMatches(
//					poll,
//					Constant.PATTERN,
//					Values.parse(Constant::new)
//			);
