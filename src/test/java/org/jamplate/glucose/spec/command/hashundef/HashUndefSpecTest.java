package org.jamplate.glucose.spec.command.hashundef;

import org.jamplate.api.Unit;
import org.jamplate.glucose.spec.GlucoseSpec;
import org.jamplate.glucose.spec.tool.DebugSpec;
import org.jamplate.impl.api.Action;
import org.jamplate.impl.api.UnitImpl;
import org.jamplate.impl.model.PseudoDocument;
import org.jamplate.model.Document;
import org.junit.jupiter.api.Test;

import static org.jamplate.internal.util.Specs.listener;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class HashUndefSpecTest {
	@Test
	public void test0() {
		Document document = new PseudoDocument(
				//name
				"Test",
				//content
				"#define oniichan bro",
				"oniichan #{oniichan \"\\n\"}#",
				"#undef oniichan",
				"oniichan #{oniichan \"\\n\"}#"
		);
		String expectedConsole = "bro bro\noniichan oniichan\n";

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
