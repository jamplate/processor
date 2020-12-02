//package org.jamplate.parser;
//
//import org.cufy.preprocessor.link.Logic;
//import org.cufy.preprocessor.logic.Operator;
//import org.cufy.preprocessor.parser.AbstractVoteParser;
//import org.cufy.util.Polls;
//
//import java.util.List;
//import java.util.ListIterator;
//import java.util.Objects;
//import java.util.regex.Matcher;
//
//public abstract class AbstractOperatorVoteParser<T extends Operator> extends AbstractVoteParser<T> {
//	@Override
//	public void link(List poll) {
//		Objects.requireNonNull(poll, "poll");
//		Polls.iterate(
//				poll,
//				this.type(),
//				this::linkOperator
//		);
//	}
//
//	@Override
//	public void parse(List poll) {
//		Objects.requireNonNull(poll, "poll");
//		Polls.iterate(
//				poll,
//				this.pattern(),
//				this::parseOperator
//		);
//	}
//
//	public boolean linkOperator(ListIterator iterator, T operator) {
//		Objects.requireNonNull(iterator, "iterator");
//		Objects.requireNonNull(operator, "operator");
//		boolean match = false;
//
//		//link with left
//		if (operator.getLeft() == null && Polls.hasLeft(iterator)) {
//			Object left = Polls.peekLeft(iterator);
//
//			if (left instanceof Logic) {
//				operator.setLeft((Logic) left);
//
//				Polls.removeLeft(iterator);
//
//				match = true;
//			}
//		}
//
//		//link with right
//		if (operator.getRight() == null && Polls.hasRight(iterator)) {
//			Object right = Polls.peekRight(iterator);
//
//			if (right instanceof Logic) {
//				operator.setRight((Logic) right);
//
//				Polls.removeRight(iterator);
//
//				match = true;
//			}
//		}
//
//		return match;
//	}
//
//	public abstract void parseOperator(ListIterator iterator, Matcher matcher);
//}
