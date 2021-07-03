package org.jamplate.spec.parameter.operator;

import org.jamplate.api.Unit;
import org.jamplate.instruction.flow.Block;
import org.jamplate.instruction.memory.resource.PushConst;
import org.jamplate.instruction.operator.cast.CastBoolean;
import org.jamplate.instruction.operator.logic.Compare;
import org.jamplate.instruction.operator.logic.Negate;
import org.jamplate.internal.api.EditSpec;
import org.jamplate.internal.api.Event;
import org.jamplate.internal.api.UnitImpl;
import org.jamplate.internal.model.EnvironmentImpl;
import org.jamplate.internal.model.PseudoDocument;
import org.jamplate.model.Document;
import org.jamplate.model.Environment;
import org.jamplate.memory.Memory;
import org.jamplate.memory.Value;
import org.jamplate.spec.document.LogicSpec;
import org.jamplate.spec.element.ParameterSpec;
import org.jamplate.spec.parameter.resource.NumberSpec;
import org.jamplate.spec.syntax.symbol.CloseChevronSpec;
import org.jamplate.spec.syntax.symbol.MinusSpec;
import org.jamplate.spec.syntax.term.DigitsSpec;
import org.jamplate.spec.tool.DebugSpec;
import org.jamplate.value.NumberValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class MoreThanSpecTest {
	@Test
	public void manualAssembly0() {
		for (int i = -2; i < 3; i++)
			for (int j = -2; j < 3; j++) {
				int left = i;
				int right = j;
				boolean result = left > right;

				Environment environment = new EnvironmentImpl();
				Memory memory = new Memory();
				new Block(
						(env, mem) -> mem.push(new NumberValue(left)),
						(env, mem) -> mem.push(new NumberValue(right)),
						//compare the values
						Compare.INSTANCE,
						//push `1` to compare the comparison result
						new PushConst(new NumberValue(1)),
						//compare the comparison result with `1`
						Compare.INSTANCE,
						//cast the result to boolean
						CastBoolean.INSTANCE,
						//invert the results
						Negate.INSTANCE
				).exec(environment, memory);

				Value value = memory.pop();
				String text = value.evaluate(memory);

				assertEquals(
						String.valueOf(result),
						text,
						left + " > " + right + " is expected to be " + result +
						" but got " +
						text
				);
			}
	}

	@Test
	public void test0() {
		for (int i : new int[]{-2, -1, 0, 1, 2})
			for (int j : new int[]{-2, -1, 0, 1, 2}) {
				Document document = new PseudoDocument(i + ">" + j);
				String expected = String.valueOf(i > j);

				Unit unit = new UnitImpl();

				unit.getSpec().add(DebugSpec.INSTANCE);

				unit.getSpec().add(LogicSpec.INSTANCE);
				unit.getSpec().add(new ParameterSpec(
						//syntax
						DigitsSpec.INSTANCE,
						MinusSpec.INSTANCE,
						CloseChevronSpec.INSTANCE,
						//value
						NumberSpec.INSTANCE,
						//operator
						MoreThanSpec.INSTANCE,
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
