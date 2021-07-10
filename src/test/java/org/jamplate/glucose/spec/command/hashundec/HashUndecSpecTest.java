package org.jamplate.glucose.spec.command.hashundec;

import org.jamplate.unit.Unit;
import org.jamplate.glucose.spec.GlucoseSpec;
import org.jamplate.glucose.spec.tool.DebugSpec;
import org.jamplate.impl.unit.Action;
import org.jamplate.impl.unit.UnitImpl;
import org.jamplate.impl.document.PseudoDocument;
import org.jamplate.memory.Value;
import org.jamplate.model.Document;
import org.junit.jupiter.api.Test;

import static org.jamplate.util.Specs.listener;
import static org.junit.jupiter.api.Assertions.*;

public class HashUndecSpecTest {
	@Test
	public void test0() {
		Document document = new PseudoDocument(
				//name
				"Test",
				//content
				"#declare __X__ __PATH__",
				"#undec __PATH__"
		);
		String expectedConsole = "Test";

		Unit unit = new UnitImpl(new GlucoseSpec(
				DebugSpec.INSTANCE,
				listener(event -> {
					if (event.getAction().equals(Action.POST_EXEC)) {
						Value x = event.getMemory().get("__X__");
						Value path = event.getMemory().get("__PATH__");

						assertNotSame(
								Value.NULL,
								x,
								"Not declared in the first place"
						);
						assertSame(
								Value.NULL,
								path,
								"Not undeclared"
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
