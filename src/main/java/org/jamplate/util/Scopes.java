package org.jamplate.util;

import org.cufy.preprocessor.link.Logic;
import org.cufy.preprocessor.link.Scope;
import org.cufy.preprocessor.Poll;

import java.util.ListIterator;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.regex.Pattern;

public final class Scopes {
	public static <T extends Scope> BiPredicate<ListIterator, Object> link(Class<T> type) {
		Objects.requireNonNull(type, "type");
		return (iterator, object) -> {
			if (type.isInstance(object)) {
				T scope = type.cast(object);

				boolean match = false;

				//link with right logic
				if (scope.getLogic() == null && Poll.hasNext(iterator)) {
					Object right = Poll.peekNext(iterator);

					if (right instanceof Logic) {
						scope.setLogic((Logic) right);

						Poll.removeNext(iterator);

						match = true;
					}
				}

				//link with left scope
				if (scope.getPrevious() == null && Poll.hasPrevious(iterator)) {
					Object left = Poll.peekPrevious(iterator);

					if (left instanceof Scope) {
						((Scope) left).pushElement(scope);

						Poll.remove(iterator);
					}
				}

				return match;
			}

			return false;
		};
	}

	public static <T extends Scope> BiPredicate<ListIterator, Object> parse(Pattern pattern, BiFunction<String, String, T> function) {
		Objects.requireNonNull(pattern, "pattern");
		return Parsers.parse(pattern, (iterator, matcher) -> {
			String name = matcher.group("NAME");
			String logic = matcher.group("LOGIC");
			iterator.set(function.apply(name, logic));
			return true;
		});
	}
}

//Parser.parse(File)
//Votes.forEach()
//                u7777
//jret
