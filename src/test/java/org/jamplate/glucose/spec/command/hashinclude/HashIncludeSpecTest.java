package org.jamplate.glucose.spec.command.hashinclude;

import org.jamplate.unit.Unit;
import org.jamplate.glucose.spec.GlucoseSpec;
import org.jamplate.glucose.spec.tool.DebugSpec;
import org.jamplate.impl.unit.Action;
import org.jamplate.impl.unit.UnitImpl;
import org.jamplate.impl.document.PseudoDocument;
import org.jamplate.model.Document;
import org.junit.jupiter.api.Test;

import static org.jamplate.util.Specs.listener;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class HashIncludeSpecTest {
	@Test
	public void test0() {
		Document document0 = new PseudoDocument(
				//name
				"Test0",
				//content
				"#{__PATH__ \"\\n\"}#",
				"#include Test1"
		);
		Document document1 = new PseudoDocument(
				//name
				"Test1",
				//content
				"#{__PATH__}#"
		);
		String expectedConsole = "Test0\nTest1";

		Unit unit = new UnitImpl(new GlucoseSpec(
				DebugSpec.INSTANCE,
				listener(event -> {
					if (event.getAction().equals(Action.POST_EXEC)) {
						String actualConsole = event.getMemory().getConsole().read();

						assertEquals(
								expectedConsole,
								actualConsole,
								"Unexpected console output"
						);
					}
				})
		));

		if (
				!unit.initialize(document0, document1) ||
				!unit.parseAll() ||
				!unit.analyzeAll() ||
				!unit.compileAll() ||
				!unit.execute(document0)
		) {
			unit.diagnostic();
			fail("Uncompleted test invocation");
		}
	}
}
