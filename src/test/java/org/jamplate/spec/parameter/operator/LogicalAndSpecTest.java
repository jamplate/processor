package org.jamplate.spec.parameter.operator;

import org.jamplate.api.Unit;
import org.jamplate.instruction.flow.Block;
import org.jamplate.instruction.operator.cast.CastBoolean;
import org.jamplate.instruction.operator.logic.And;
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
import org.jamplate.spec.parameter.resource.ReferenceSpec;
import org.jamplate.spec.syntax.symbol.AndAndSpec;
import org.jamplate.spec.syntax.term.WordSpec;
import org.jamplate.spec.tool.DebugSpec;
import org.jamplate.value.BooleanValue;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class LogicalAndSpecTest {
	@Test
	public void manualAssembly0() {
		for (boolean i : Arrays.asList(true, false))
			for (boolean j : Arrays.asList(true, false)) {
				boolean left = i;
				boolean right = j;
				boolean result = left && right;

				Environment environment = new EnvironmentImpl();
				Memory memory = new Memory();
				new Block(
						//run first param
						(env, mem) -> mem.push(new BooleanValue(left)),
						//cast first param into boolean
						CastBoolean.INSTANCE,
						//run second param
						(env, mem) -> mem.push(new BooleanValue(right)),
						//cast second param into boolean
						CastBoolean.INSTANCE,
						//do the logic
						And.INSTANCE
				).exec(environment, memory);

				Value value = memory.pop();
				String text = value.evaluate(memory);

				assertEquals(
						String.valueOf(result),
						text,
						left + " && " + right + " is expected to be " + result +
						" but got " +
						text
				);
			}
	}

	@Test
	public void test0() {
		for (boolean i : new boolean[]{false, true})
			for (boolean j : new boolean[]{false, true}) {
				Document document = new PseudoDocument(i + "&&" + j);
				String expected = String.valueOf(i && j);

				Unit unit = new UnitImpl();

				unit.getSpec().add(DebugSpec.INSTANCE);

				unit.getSpec().add(LogicSpec.INSTANCE);
				unit.getSpec().add(new ParameterSpec(
						//syntax
						WordSpec.INSTANCE,
						AndAndSpec.INSTANCE,
						//value
						ReferenceSpec.INSTANCE,
						//operator
						LogicalAndSpec.INSTANCE
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
