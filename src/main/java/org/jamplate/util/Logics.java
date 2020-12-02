//package org.jamplate.util;
//
//import org.cufy.preprocessor.link.Linkable;
//import org.cufy.preprocessor.link.Logic;
//import org.cufy.preprocessor.Poll;
//
//import java.util.ListIterator;
//import java.util.Objects;
//import java.util.function.BiPredicate;
//import java.util.function.Function;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//public class Logics {
//	public static <T extends Linkable> BiPredicate<ListIterator, Object> link(Class<? extends T> klass) {
//		Objects.requireNonNull(klass, "klass");
//		return ((iterator, object) -> {
//			if (klass.isInstance(object)) {
//				T element = (T) object;
//
//				if (element.)
//			}
//		});
//		return (iterator, object) ->
//				klass.isInstance(object) &&
//				Logics.link(iterator, (T) object);
//	}
//
//	public static <T extends Logic> BiPredicate<ListIterator, Object> linkLeft(Class<T> type) {
//		Objects.requireNonNull(type, "type");
//		return (iterator, object) ->
//				type.isInstance(object) &&
//				Logics.linkLeft(iterator, (T) object);
//	}
//
//	public static <T extends Logic> BiPredicate<ListIterator, Object> linkRight(Class<T> type) {
//		Objects.requireNonNull(type, "type");
//		return (iterator, object) ->
//				type.isInstance(object) &&
//				Logics.linkRight(iterator, (T) object);
//	}
//
//	public static <T extends Logic> BiPredicate<ListIterator, Object> parse(Pattern pattern, Function<Matcher, T> function) {
//		Objects.requireNonNull(pattern, "pattern");
//		Objects.requireNonNull(function, "function");
//		return (iterator, object) -> {
//
//		};
//	}
//
//	protected static <T extends Logic> boolean link(ListIterator iterator, T logic) {
//		Objects.requireNonNull(iterator, "iterator");
//		Objects.requireNonNull(logic, "logic");
//		return Logics.linkLeft(iterator, logic) |
//			   Logics.linkRight(iterator, logic);
//	}
//
//	protected static <T extends Logic> boolean linkLeft(ListIterator iterator, T logic) {
//		Objects.requireNonNull(iterator, "iterator");
//		Objects.requireNonNull(logic, "logic");
//
//		if (logic.getPrevious() == null && Poll.hasPrevious(iterator)) {
//			Object left = Poll.peekPrevious(iterator);
//
//			if (left instanceof Logic) {
//				Logic leftLogic = (Logic) left;
//
//				logic.setPrevious(leftLogic);
//				Poll.removePrevious(iterator);
//				return true;
//			}
//		}
//
//		return false;
//	}
//
//	protected static <T extends Logic> boolean linkRight(ListIterator iterator, T logic) {
//		Objects.requireNonNull(iterator, "iterator");
//		Objects.requireNonNull(logic, "logic");
//
//		if (logic.getNext() == null && Poll.hasNext(iterator)) {
//			Object right = Poll.peekNext(iterator);
//
//			if (right instanceof Logic) {
//				Logic rightLogic = (Logic) right;
//
//				logic.setNext(rightLogic);
//				Poll.removeNext(iterator);
//				return true;
//			}
//		}
//
//		return false;
//	}
//
//}
