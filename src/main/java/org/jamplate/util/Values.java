//package org.jamplate.util;
//
//import java.util.ListIterator;
//import java.util.Objects;
//import java.util.function.BiConsumer;
//import java.util.function.BiPredicate;
//import java.util.function.Function;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//public class Values {
//	public static <T extends Value> BiConsumer<ListIterator, Matcher> parse(Function<String, T> function) {
//		Objects.requireNonNull(function, "function");
//		return (iterator, matcher) -> {
//			String value = matcher.group("VALUE");
//			iterator.set(function.apply(value));
//		};
//	}
//
//	public static <T extends Value> BiPredicate<ListIterator, Object> parse(Pattern pattern, Function<String, T> function) {
//		Objects.requireNonNull(pattern, "pattern");
//		Objects.requireNonNull(function, "function");
//		return Parsers.parse(pattern, (iterator, matcher) -> {
//			String value = matcher.group("VALUE");
//			iterator.set(function.apply(value));
//			return true;
//		});
//	}
//}
