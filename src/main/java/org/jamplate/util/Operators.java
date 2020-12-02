//package org.jamplate.util;
//
//import org.cufy.preprocessor.link.Logic;
//
//import java.util.ListIterator;
//import java.util.Objects;
//import java.util.function.BiPredicate;
//import java.util.function.Function;
//import java.util.regex.Pattern;
//
//public class Operators {
//	/**
//	 * @param type
//	 * @param <T>
//	 * @return
//	 */
//	public static <T extends Operator> BiPredicate<ListIterator, Object> link(Class<T> type) {
//		Objects.requireNonNull(type, "type");
//		return (iterator, object) -> {
//			if (type.isInstance(object)) {
//				T operator = type.cast(object);
//
//				boolean match = false;
//
//				//link with left
//				if (operator.getLeft() == null && Polls.hasLeft(iterator)) {
//					Object left = Polls.peekLeft(iterator);
//
//					if (left instanceof Logic) {
//						operator.setLeft((Logic) left);
//
//						Polls.removeLeft(iterator);
//
//						match = true;
//					}
//				}
//
//				//link with right
//				if (operator.getRight() == null && Polls.hasRight(iterator)) {
//					Object right = Polls.peekRight(iterator);
//
//					if (right instanceof Logic) {
//						operator.setRight((Logic) right);
//
//						Polls.removeRight(iterator);
//
//						match = true;
//					}
//				}
//
//				return match;
//			}
//
//			return false;
//		};
//	}
//
//	/**
//	 * @param pattern
//	 * @param function
//	 * @param <T>
//	 * @return
//	 */
//	public static <T extends Operator> BiPredicate<ListIterator, Object> parse(Pattern pattern, Function<String, T> function) {
//		Objects.requireNonNull(pattern, "pattern");
//		Objects.requireNonNull(function, "function");
//		return PollParsers.parse(pattern, (iterator, matcher) -> {
//			String operator = matcher.group("OPERATOR");
//			iterator.set(function.apply(operator));
//			return true;
//		});
//	}
//}
////	/**
////	 * A function that takes a list iterator (with its position set to the next element of the
////	 * operator given to it). And link the operator given to it to the element before it and after
////	 * it. (if possible).
////	 *
////	 * @param <T> the type of the operator.
////	 * @return the function.
////	 * @since 0.0.b ~2020.10.07
////	 */
////	public static <T extends Operator> BiPredicate<ListIterator, T> link() {
////		return (iterator, operator) -> {
////			boolean match = false;
////
////			//link with left
////			if (operator.getLeft() == null && Polls.hasLeft(iterator)) {
////				Object left = Polls.peekLeft(iterator);
////
////				if (left instanceof Logic) {
////					operator.setLeft((Logic) left);
////
////					Polls.removeLeft(iterator);
////
////					match = true;
////				}
////			}
////
////			//link with right
////			if (operator.getRight() == null && Polls.hasRight(iterator)) {
////				Object right = Polls.peekRight(iterator);
////
////				if (right instanceof Logic) {
////					operator.setRight((Logic) right);
////
////					Polls.removeRight(iterator);
////
////					match = true;
////				}
////			}
////
////			return match;
////		};
////	}
//
////	/**
////	 * A function that takes a list iterator (with its position set to the next element of the
////	 * operator given to it). And parse the "OPERATOR" group using the given {@code function}.
////	 *
////	 * @param function a function that takes the string from the "OPERATOR" group and parse it into
////	 *                 an operator.
////	 * @param <T>      the type of the operator.
////	 * @return the function.
////	 * @since 0.0.b ~2020.10.07
////	 */
////	public static <T extends Operator> BiConsumer<ListIterator, Matcher> parse(Function<String, T> function) {
////		Objects.requireNonNull(function, "function");
////		return (iterator, matcher) -> {
////			String operator = matcher.group("OPERATOR");
////			iterator.set(function.apply(operator));
////		};
////	}
