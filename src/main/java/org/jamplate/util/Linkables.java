package org.jamplate.util;

import org.cufy.text.Element;
import org.cufy.preprocessor.Poll;

import java.util.ListIterator;
import java.util.Objects;
import java.util.function.BiPredicate;

public class Linkables {
	public static <T extends Element> BiPredicate<ListIterator, Object> link(Class<T> klass) {
		Objects.requireNonNull(klass, "klass");
		return (iterator, object) -> {
			if (klass.isInstance(object)) {
				T linkable = (T) object;

				if (linkable.getNext() == null && Poll.hasPrevious(iterator)) {
					Object previous = Poll.peekPrevious(iterator);

					if (previous instanceof Element) {
						linkable.setPrevious((Element) previous);

						Poll.removePrevious(iterator);
					}
				}
			}
		};
	}
}
