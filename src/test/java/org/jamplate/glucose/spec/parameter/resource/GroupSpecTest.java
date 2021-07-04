package org.jamplate.glucose.spec.parameter.resource;

import org.jamplate.api.Spec;
import org.jamplate.api.Unit;
import org.jamplate.glucose.spec.document.LogicSpec;
import org.jamplate.glucose.spec.element.ParameterSpec;
import org.jamplate.glucose.spec.parameter.operator.AdderSpec;
import org.jamplate.glucose.spec.parameter.operator.MultiplierSpec;
import org.jamplate.glucose.spec.syntax.enclosure.ParenthesesSpec;
import org.jamplate.glucose.spec.syntax.symbol.AsteriskSpec;
import org.jamplate.glucose.spec.syntax.symbol.PlusSpec;
import org.jamplate.glucose.spec.syntax.term.DigitsSpec;
import org.jamplate.glucose.spec.tool.DebugSpec;
import org.jamplate.impl.api.Action;
import org.jamplate.impl.api.UnitImpl;
import org.jamplate.impl.model.PseudoDocument;
import org.jamplate.memory.Memory;
import org.jamplate.model.Document;
import org.jamplate.model.Environment;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static org.jamplate.internal.util.Specs.listener;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class GroupSpecTest {
	@Test
	public void serialization0() throws Exception {
		Document document = new PseudoDocument("1 + 2 * (3 + 5)");
		String expected = String.valueOf(1 + 2 * (3 + 5));

		Unit unit = new UnitImpl();

		unit.getSpec().add(DebugSpec.INSTANCE);
		unit.getSpec().add(LogicSpec.INSTANCE);
		unit.getSpec().add(new ParameterSpec(
				//syntax
				ParenthesesSpec.INSTANCE,
				PlusSpec.INSTANCE,
				AsteriskSpec.INSTANCE,
				DigitsSpec.INSTANCE,
				//values
				GroupSpec.INSTANCE,
				NumberSpec.INSTANCE,
				AdderSpec.INSTANCE,
				MultiplierSpec.INSTANCE
		));
		unit.getSpec().add(listener(event -> {
			if (event.getAction().equals(Action.POST_EXEC)) {
				Memory memory = event.getMemory();
				String actual = memory.peek().evaluate(memory);

				assertEquals(
						expected,
						actual,
						"Unexpected results"
				);
			}
		}));

		if (
				!unit.initialize(document) ||
				!unit.parse(document) ||
				!unit.analyze(document) ||
				!unit.compile(document)
		) {
			unit.diagnostic();
			fail("Uncompleted test invocation");
		}

		Spec spec = unit.getSpec();
		Environment environment = unit.getEnvironment();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(environment);
		oos.close();

		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		ObjectInputStream ois = new ObjectInputStream(bais);
		Environment sEnvironment = (Environment) ois.readObject();
		ois.close();

		Unit sUnit = new UnitImpl(sEnvironment, spec);

		if (
				!sUnit.execute(document)
		) {
			unit.diagnostic();
			fail("Uncompleted test invocation");
		}
	}

	@Test
	public void test0() {
		Document document = new PseudoDocument("1 + 2 * (3 + 5)");
		String expected = String.valueOf(1 + 2 * (3 + 5));

		Unit unit = new UnitImpl();

		unit.getSpec().add(DebugSpec.INSTANCE);
		unit.getSpec().add(LogicSpec.INSTANCE);
		unit.getSpec().add(new ParameterSpec(
				//syntax
				ParenthesesSpec.INSTANCE,
				PlusSpec.INSTANCE,
				AsteriskSpec.INSTANCE,
				DigitsSpec.INSTANCE,
				//values
				GroupSpec.INSTANCE,
				NumberSpec.INSTANCE,
				AdderSpec.INSTANCE,
				MultiplierSpec.INSTANCE
		));
		unit.getSpec().add(listener(event -> {
			if (event.getAction().equals(Action.POST_EXEC)) {
				Memory memory = event.getMemory();
				String actual = memory.peek().evaluate(memory);

				assertEquals(
						expected,
						actual,
						"Unexpected results"
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
