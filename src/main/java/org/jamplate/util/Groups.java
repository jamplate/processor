//package org.jamplate.util;
//
//import org.cufy.preprocessor.parser.PollParser;
//
//import java.util.*;
//import java.util.function.BiPredicate;
//import java.util.function.Function;
//import java.util.regex.Pattern;
//
//public final class Groups {
//	/**
//	 * @param type
//	 * @param parser
//	 * @param <T>
//	 * @return
//	 */
//	public static <T extends Group> BiPredicate<ListIterator, Object> link(Class<T> type, PollParser<? super T> parser) {
//		Objects.requireNonNull(type, "type");
//		Objects.requireNonNull(parser, "parser");
//		return (iterator, object) -> {
//			if (type.isInstance(object)) {
//				T group = type.cast(object);
//				List subpoll = group.getContent();
//
//				if (subpoll != null)
//					return parser.link(subpoll);
//			}
//
//			return false;
//		};
//	}
//
//	/**
//	 * @param type
//	 * @param parser
//	 * @param <T>
//	 * @return
//	 */
//	public static <T extends Group> BiPredicate<ListIterator, Object> parse(Class<T> type, PollParser<? super T> parser) {
//		return (iterator, object) -> {
//			if (type.isInstance(object)) {
//				T group = type.cast(object);
//				List subpoll = group.getContent();
//
//				if (subpoll != null)
//					return parser.parse(subpoll);
//			}
//
//			return false;
//		};
//	}
//
//	/**
//	 * @param type
//	 * @param parser
//	 * @param <T>
//	 * @return
//	 */
//	public static <T extends Group> BiPredicate<ListIterator, Object> process(Class<T> type, PollParser<? super T> parser) {
//		Objects.requireNonNull(type, "type");
//		Objects.requireNonNull(parser, "parser");
//		return (iterator, object) -> {
//			if (type.isInstance(object)) {
//				T group = type.cast(object);
//				List subpoll = group.getContent();
//
//				if (subpoll != null)
//					return parser.process(subpoll);
//			}
//
//			return false;
//		};
//	}
//
//	public static <T extends Group> BiPredicate<ListIterator, Object> process(Pattern[] escapables, Pattern[] nestables, Pattern pattern, Pattern splitter, Function<List<String>, T> function) {
//		Objects.requireNonNull(escapables, "escapables");
//		Objects.requireNonNull(nestables, "nestables");
//		Objects.requireNonNull(pattern, "pattern");
//		Objects.requireNonNull(splitter, "splitter");
//		Objects.requireNonNull(function, "function");
//		return Parsers.process(escapables, nestables, pattern, (iterator, matcher) -> {
//			String content = matcher.group("CONTENT");
//			List<String> list = new ArrayList(Arrays.asList(Strings.split(
//					content,
//					escapables,
//					nestables,
//					splitter
//			)));
//			iterator.set(function.apply(list));
//			return true;
//		});
//	}
//}
////	public static <T extends Group> BiPredicate<ListIterator, Object> process(Pattern[] escapes, Pattern[] nests, Pattern pattern, Pattern splitter, Function<List<String>, T> function) {
////		Objects.requireNonNull(escapes, "escapes");
////		Objects.requireNonNull(nests, "nests");
////		Objects.requireNonNull(pattern, "pattern");
////		Objects.requireNonNull(splitter, "splitter");
////		Objects.requireNonNull(function, "function");
////
////		//compile open and close patterns
////		Pattern open = Strings.extract(pattern, "OPEN");
////		Pattern close = Strings.extract(pattern, "CLOSE");
////
////		return (iterator, object) -> {
////			if (object instanceof String) {
////				String string = (String) object;
////				MatchResult result = Strings.group(
////						string,
////						escapes,
////						nests,
////						open,
////						close
////				);
////
////				if (result.groupCount() == 0) {
////					Matcher matcher = pattern.matcher(result.group());
////					if (matcher.find()) {
////						int start = result.start();
////						int end = result.end();
////
////					}
////				}
////
////			}
////
////			return false;
////		};
////	}
////
////	public static <T extends Group> BiPredicate<ListIterator, T> linkGroup(PollParser<? super T> parser) {
////		return (iterator, group) -> {
////			List subpoll = group.getLogics();
////
////			if (subpoll != null)
////				parser.link(subpoll);
////
////			return false;
////		};
////	}
//
////
////	public static <T extends Group> BiPredicate<ListIterator, T> parseGroup(PollParser<? super T> parser) {
////		return (iterator, group) -> {
////			List subpoll = group.getLogics();
////
////			if (subpoll != null)
////				parser.parse(subpoll);
////
////			return false;
////		};
////	}
//
////	public static <T extends Group> BiPredicate<ListIterator, T> processGroup(PollParser<? super T> parser) {
////		return (iterator, group) -> {
////			List subpoll = group.getLogics();
////
////			if (subpoll != null)
////				parser.process(subpoll);
////
////			return false;
////		};
////	}
////	public static <T extends Group> BiConsumer<ListIterator, String> processGroup(Function<String[], T> function) {
////		return ((iterator, matcher) -> {
////			String elements = matcher.group("ELEMENTS");
////			iterator.set(function.apply(
////					Strings.split(
////							elements,
////							escapes,
////							nests,
////							pattern
////					)
////			));
////		});
////	}
