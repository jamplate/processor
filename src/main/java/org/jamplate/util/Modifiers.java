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
//public final class Modifiers {
//	/**
//	 * @param type
//	 * @param <T>
//	 * @return
//	 */
//	public static <T extends Modifier> BiPredicate<ListIterator, Object> link(Class<T> type) {
//		return (iterator, object) -> {
//			if (type.isInstance(object)) {
//				T modifier = type.cast(object);
//
//				//link with right
//				if (modifier.getLogic() == null && Polls.hasRight(iterator)) {
//					Object right = Polls.peekRight(iterator);
//
//					if (right instanceof Logic) {
//						modifier.setLogic((Logic) right);
//
//						Polls.removeRight(iterator);
//
//						return true;
//					}
//				}
//			}
//
//			return false;
//		};
//	}
//
//	/**
//	 * <p>
//	 * The given {@code pattern} should have a group with the name {@code "MODIFIER"}.
//	 *
//	 * @param pattern
//	 * @param function
//	 * @param <T>
//	 * @return
//	 */
//	public static <T extends Modifier> BiPredicate<ListIterator, Object> parse(Pattern pattern, Function<String, T> function) {
//		Objects.requireNonNull(pattern, "pattern");
//		Objects.requireNonNull(function, "function");
//		return PollParsers.parse(pattern, (iterator, matcher) -> {
//			String modifier = matcher.group("MODIFIER");
//			iterator.set(function.apply(modifier));
//			return true;
//		});
//	}
//}
////	@Deprecated
////	public static <T extends Modifier> BiPredicate<ListIterator, T> link() {
////		return (iterator, modifier) -> {
////			//link with right
////			if (modifier.getLogic() == null && Polls.hasRight(iterator)) {
////				Object right = Polls.peekRight(iterator);
////
////				if (right instanceof Logic) {
////					modifier.setLogic((Logic) right);
////
////					Polls.removeRight(iterator);
////
////					return true;
////				}
////			}
////
////			return false;
////		};
////	}
//
////
////	public static <T extends Modifier> BiConsumer<ListIterator, Matcher> parse(Function<String, T> function) {
////		Objects.requireNonNull(function, "function");
////		return (iterator, matcher) -> {
////			String modifier = matcher.group("MODIFIER");
////			iterator.set(function.apply(modifier));
////		};
////	}
