package org.jamplate.glucose.spec.parameter.operator;

import org.jamplate.api.Unit;
import org.jamplate.impl.instruction.Block;
import org.jamplate.glucose.instruction.memory.resource.PushConst;
import org.jamplate.glucose.instruction.memory.stack.Dup;
import org.jamplate.glucose.instruction.memory.stack.Swap;
import org.jamplate.glucose.instruction.operator.cast.CastBoolean;
import org.jamplate.glucose.instruction.operator.logic.Compare;
import org.jamplate.glucose.instruction.operator.logic.Negate;
import org.jamplate.glucose.instruction.operator.logic.Or;
import org.jamplate.glucose.spec.document.LogicSpec;
import org.jamplate.glucose.spec.element.ParameterSpec;
import org.jamplate.glucose.spec.parameter.resource.NumberSpec;
import org.jamplate.glucose.spec.syntax.symbol.MinusSpec;
import org.jamplate.glucose.spec.syntax.symbol.OpenChevronEqualSpec;
import org.jamplate.glucose.spec.syntax.term.DigitsSpec;
import org.jamplate.glucose.spec.tool.DebugSpec;
import org.jamplate.glucose.value.NumberValue;
import org.jamplate.impl.api.Action;
import org.jamplate.impl.api.UnitImpl;
import org.jamplate.impl.model.EnvironmentImpl;
import org.jamplate.impl.model.PseudoDocument;
import org.jamplate.memory.Memory;
import org.jamplate.memory.Value;
import org.jamplate.model.Document;
import org.jamplate.model.Environment;
import org.junit.jupiter.api.Test;

import static org.jamplate.internal.util.Specs.listener;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class LessThanEqualsSpecTest {
	@Test
	public void manualAssembly0() {
		for (int i = -2; i < 3; i++)
			for (int j = -2; j < 3; j++) {
				int left = i;
				int right = j;
				boolean result = left <= right;

				Environment environment = new EnvironmentImpl();
				Memory memory = new Memory();
				new Block(
						(env, mem) -> mem.push(new NumberValue(left)),
						(env, mem) -> mem.push(new NumberValue(right)),
						//compare the values
						Compare.INSTANCE,
						//duplicate for the two checks
						Dup.INSTANCE,
						//cast the first duplicate to boolean
						CastBoolean.INSTANCE,
						//negate the first duplicate to boolean
						Negate.INSTANCE,
						//swap the duplicates
						Swap.INSTANCE,
						//push '-1' to compare with the duplicate
						new PushConst(new NumberValue(-1)),
						//compare the second duplicate with `-1`
						Compare.INSTANCE,
						//cast the second duplicate to boolean
						CastBoolean.INSTANCE,
						//negate the second duplicate
						Negate.INSTANCE,
						//less than or equals
						Or.INSTANCE
				).exec(environment, memory);

				Value value = memory.pop();
				String text = value.evaluate(memory);

				assertEquals(
						String.valueOf(result),
						text,
						left + " <= " + right + " is expected to be " + result +
						" but got " +
						text
				);
			}
	}

	@Test
	public void test0() {
		for (int i : new int[]{-2, -1, 0, 1, 2})
			for (int j : new int[]{-2, -1, 0, 1, 2}) {
				Document document = new PseudoDocument(i + "<=" + j);
				String expected = String.valueOf(i <= j);

				Unit unit = new UnitImpl();

				unit.getSpec().add(DebugSpec.INSTANCE);

				unit.getSpec().add(LogicSpec.INSTANCE);
				unit.getSpec().add(new ParameterSpec(
						//syntax
						DigitsSpec.INSTANCE,
						OpenChevronEqualSpec.INSTANCE,
						MinusSpec.INSTANCE,
						//value
						NumberSpec.INSTANCE,
						//operator
						LessThanEqualsSpec.INSTANCE,
						SubtractorSpec.INSTANCE
				));
				unit.getSpec().add(listener(event -> {
					if (event.getAction().equals(Action.POST_EXEC)) {
						Memory memory = event.getMemory();
						String actual = memory.peek().evaluate(memory);

						assertEquals(
								expected,
								actual,
								"Unexpected result"
						);
					}
				}));

				if (
						!unit.initialize(document) ||
						!unit.parse(document) ||
						!unit.analyze(document) ||
						!unit.compile(document) ||
						!unit.execute(document)
				) {
					unit.diagnostic();
					fail("Uncompleted test invocation");
				}
			}
	}
}
