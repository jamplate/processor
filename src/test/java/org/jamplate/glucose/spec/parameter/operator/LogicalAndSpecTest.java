package org.jamplate.glucose.spec.parameter.operator;

import org.jamplate.api.Unit;
import org.jamplate.glucose.instruction.operator.cast.ICastBoolean;
import org.jamplate.glucose.instruction.operator.logic.IAnd;
import org.jamplate.glucose.spec.document.LogicSpec;
import org.jamplate.glucose.spec.element.ParameterSpec;
import org.jamplate.glucose.spec.parameter.resource.ReferenceSpec;
import org.jamplate.glucose.spec.syntax.symbol.AndAndSpec;
import org.jamplate.glucose.spec.syntax.term.WordSpec;
import org.jamplate.glucose.spec.tool.DebugSpec;
import org.jamplate.impl.api.Action;
import org.jamplate.impl.api.UnitImpl;
import org.jamplate.impl.instruction.Block;
import org.jamplate.impl.model.EnvironmentImpl;
import org.jamplate.impl.model.PseudoDocument;
import org.jamplate.memory.Memory;
import org.jamplate.memory.Value;
import org.jamplate.model.Document;
import org.jamplate.model.Environment;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.jamplate.glucose.internal.util.Values.bool;
import static org.jamplate.internal.util.Specs.listener;
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
						(env, mem) -> mem.push(bool(left)),
						//cast first param into boolean
						ICastBoolean.INSTANCE,
						//run second param
						(env, mem) -> mem.push(bool(right)),
						//cast second param into boolean
						ICastBoolean.INSTANCE,
						//do the logic
						IAnd.INSTANCE
				).exec(environment, memory);

				Value value = memory.pop();
				String text = value.eval(memory);

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
				unit.getSpec().add(listener(event -> {
					if (event.getAction().equals(Action.POST_EXEC)) {
						Memory memory = event.getMemory();
						String actual = memory.peek().eval(memory);

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
