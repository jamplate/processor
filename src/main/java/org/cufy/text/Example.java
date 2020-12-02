package org.cufy.text;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

public final class Example {
	public class If extends AbstractElement {
		@Override
		public Object invoke(@NotNull Memory memory) throws IOException {
			Objects.requireNonNull(memory, "memory");
			Element argument = this.getNext(Element.ARGUMENT);
			Element fork = this.getNext(Element.FORK);
			Element branch = this.getNext(Element.BRANCH);

			Object condition;

			if (argument == null) {
				Object condition = argument.invoke(memory);

				if (condition == null || condition.equals(0) || condition.equals(false)) {

				}
			}
			if (value == null) {
				branch.invoke(memory);
			}

			return null;
		}
	}

	public class Parser {
		public void parse(String string) {

		}
	}
}
