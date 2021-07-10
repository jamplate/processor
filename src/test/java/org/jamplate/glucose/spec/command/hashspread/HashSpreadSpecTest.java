package org.jamplate.glucose.spec.command.hashspread;

import org.jamplate.unit.Unit;
import org.jamplate.glucose.spec.GlucoseSpec;
import org.jamplate.glucose.spec.tool.DebugSpec;
import org.jamplate.glucose.value.VNumber;
import org.jamplate.impl.unit.Action;
import org.jamplate.impl.unit.UnitImpl;
import org.jamplate.impl.document.PseudoDocument;
import org.jamplate.memory.Memory;
import org.jamplate.memory.Value;
import org.jamplate.model.Document;
import org.junit.jupiter.api.Test;

import static org.jamplate.util.Specs.listener;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class HashSpreadSpecTest {
	@Test
	public void test0() {
		Document document = new PseudoDocument(
				//name
				"Test",
				//content
				"#spread {name:Sulaiman, age:9850}"
		);
		String expectedName = "Sulaiman";
		int expectedAge = 9850;

		Unit unit = new UnitImpl(new GlucoseSpec(
				DebugSpec.INSTANCE,
				listener(event -> {
					if (event.getAction().equals(Action.POST_EXEC)) {
						Memory memory = event.getMemory();
						String actualName = memory.get("name").eval(memory);
						Value actualAgeV = memory.get("age");
						int actualAge = ((VNumber) actualAgeV).getPipe().eval(memory).intValue();

						assertEquals(
								expectedName,
								actualName,
								"Value not spread properly"
						);
						assertEquals(
								expectedAge,
								actualAge,
								"Value not spread properly"
						);
					}
				})
		));

		if (
				!unit.initialize(document) ||
				!unit.parse(document) ||
				!unit.analyze(document) ||
				!unit.compile(document) ||
				!unit.execute(document)
		) {
			unit.diagnostic();
			fail("Test not completed");
		}
	}
}
