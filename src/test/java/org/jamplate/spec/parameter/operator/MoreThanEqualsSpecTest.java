package org.jamplate.spec.parameter.operator;

import org.jamplate.api.Unit;
import org.jamplate.instruction.flow.Block;
import org.jamplate.instruction.memory.resource.PushConst;
import org.jamplate.instruction.memory.stack.Dup;
import org.jamplate.instruction.memory.stack.Swap;
import org.jamplate.instruction.operator.cast.CastBoolean;
import org.jamplate.instruction.operator.logic.Compare;
import org.jamplate.instruction.operator.logic.Negate;
import org.jamplate.instruction.operator.logic.Or;
import org.jamplate.internal.api.EditSpec;
import org.jamplate.internal.api.Event;
import org.jamplate.internal.api.UnitImpl;
import org.jamplate.internal.model.EnvironmentImpl;
import org.jamplate.internal.model.PseudoDocument;
import org.jamplate.model.Document;
import org.jamplate.model.Environment;
import org.jamplate.model.Memory;
import org.jamplate.model.Value;
import org.jamplate.spec.document.LogicSpec;
import org.jamplate.spec.element.ParameterSpec;
import org.jamplate.spec.parameter.resource.NumberSpec;
import org.jamplate.spec.syntax.symbol.CloseChevronEqualSpec;
import org.jamplate.spec.syntax.symbol.MinusSpec;
import org.jamplate.spec.syntax.term.DigitsSpec;
import org.jamplate.spec.tool.DebugSpec;
import org.jamplate.value.NumberValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class MoreThanEqualsSpecTest {
	@Test
	public void manualAssembly0() {
		for (int i = -2; i < 3; i++)
			for (int j = -2; j < 3; j++) {
				int left = i;
				int right = j;
				boolean result = left >= right;

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
						//push '1' to compare with the duplicate
						new PushConst(new NumberValue(1)),
						//compare the second duplicate with `1`
						Compare.INSTANCE,
						//cast the second duplicate to boolean
						CastBoolean.INSTANCE,
						//negate the second duplicate
						Negate.INSTANCE,
						//more than or equals
						Or.INSTANCE
				).exec(environment, memory);

				Value value = memory.pop();
				String text = value.evaluate(memory);

				assertEquals(
						String.valueOf(result),
						text,
						left + " >= " + right + " is expected to be " + result +
						" but got " +
						text
				);
			}
	}

	@Test
	public void test0() {
		for (int i : new int[]{-2, -1, 0, 1, 2})
			for (int j : new int[]{-2, -1, 0, 1, 2}) {
				Document document = new PseudoDocument(i + ">=" + j);
				String expected = String.valueOf(i >= j);

				Unit unit = new UnitImpl();

				unit.getSpec().add(DebugSpec.INSTANCE);

				unit.getSpec().add(LogicSpec.INSTANCE);
				unit.getSpec().add(new ParameterSpec(
						//syntax
						DigitsSpec.INSTANCE,
						CloseChevronEqualSpec.INSTANCE,
						MinusSpec.INSTANCE,
						//value
						NumberSpec.INSTANCE,
						//operator
						MoreThanEqualsSpec.INSTANCE,
						SubtractorSpec.INSTANCE
				));
				unit.getSpec().add(new EditSpec().setListener(
						(event, compilation, parameter) -> {
							if (event.equals(Event.POST_EXEC)) {
								Memory memory = (Memory) parameter;
								String actual = memory.peek().evaluate(memory);

								assertEquals(
										expected,
										actual,
										"Unexpected result"
								);
							}
						}
				));

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
