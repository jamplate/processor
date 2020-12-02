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

import org.cufy.preprocessor.AbstractLogic;
import org.cufy.text.Element;
import org.cufy.preprocessor.link.Logic;
import org.cufy.text.Memory;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A scope that joins two logics.
 *
 * @author LSafer
 * @version 0.0.6
 * @since 0.0.6 ~2020.09.22
 */
public class Addition extends AbstractLogic<Object> {
	/**
	 * Targets {@code add} statements.
	 * <ul>
	 *     <li>{@code OPERATOR} the operator detected</li>
	 * </ul>
	 *
	 * @since 0.0.b ~2020.09.29
	 */
	public static final Pattern PATTERN = Pattern.compile("(?<OPERATOR>[+])");

	@Override
	public Object evaluate(Memory memory) {
		Objects.requireNonNull(memory, "memory");

		Object object = this.previous.evaluate(memory);
		Object o = this.next.evaluate(memory);

		//join numbers
		if (object instanceof Number && o instanceof Number)
			return ((Number) object).doubleValue() +
				   ((Number) o).doubleValue();

		//join strings
		return Objects.toString(object, "") +
			   Objects.toString(o, "");
	}

	@Override
	public boolean isReserved() {
		return true;
	}

	@Override
	public boolean setBranch(Element branch) {
		return false;
	}

	@Override
	public boolean setFork(Element fork) {
		return false;
	}

	@Override
	public boolean setNext(Element next) {
		return (!(next instanceof Logic)) &&
			   super.setNext(next);
	}

	@Override
	public String toString() {
		return String.join(
				" + ",
				Objects.toString(this.previous, ""),
				Objects.toString(this.next, "")
		);
	}

	/**
	 * The default parser class of the {@link Addition} logic.
	 *
	 * @author LSafer
	 * @version 0.0.b
	 * @since 0.0.b ~2020.09.29
	 */
	public static class ParserVote implements Element.Parser {
		@Override
		public Element parse(Element element) {
			Objects.requireNonNull(element, "element");
			Matcher matcher = Addition.PATTERN.matcher(element.getSource())
					.region(element.getBeginIndex(), element.getEndIndex());

			while (matcher.find())
				if (!Element.reserved(element, matcher.start(), matcher.end()))
					for (Element fork)
		}

		//		@Override
		//		public boolean link(List poll) {
		//			return Poll.iterate(poll, Element.link());
		//		}
		//
		//		@Override
		//		public boolean parse(List poll) {
		//			return Poll.iterate(poll, Operators.parse(
		//					Addition.PATTERN,
		//					operator -> {
		//						switch (operator) {
		//							case "+":
		//								return new Addition();
		//							default:
		//								throw new InternalError("Unknown operator: " + operator);
		//						}
		//					}
		//			));
		//		}
	}
}
//
//	/**
//	 * Construct a new addition operator with its {@link #previous} and {@link #next} not
//	 * initialized.
//	 *
//	 * @since 0.0.b ~2020.10.02
//	 */
//	public Addition() {
//	}
//
//	/**
//	 * Construct a new logic that joins two logics.
//	 *
//	 * @param left  the logic at the left.
//	 * @param right the logic at the right.
//	 * @since 0.0.6 ~2020.09.22
//	 */
//	public Addition(Logic left, Logic right) {
//		super(left, right);
//	}

//						Polls.iterateMatches(
//								poll,
//								Addition.PATTERN,
//								Operators.parse(operator -> {
//									switch (operator) {
//										case "+":
//											return new Addition();
//										default:
//											throw new InternalError(
//													"Unknown operator: " + operator);
//									}
//								})
//						);
