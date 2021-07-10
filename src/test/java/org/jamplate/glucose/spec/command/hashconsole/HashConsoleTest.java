package org.jamplate.glucose.spec.command.hashconsole;

import org.jamplate.unit.Unit;
import org.jamplate.glucose.spec.GlucoseSpec;
import org.jamplate.glucose.spec.tool.DebugSpec;
import org.jamplate.impl.unit.Action;
import org.jamplate.impl.unit.UnitImpl;
import org.jamplate.impl.document.PseudoDocument;
import org.jamplate.memory.Memory;
import org.jamplate.model.Document;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.jamplate.util.Specs.listener;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class HashConsoleTest {
	@Test
	public void test0() throws IOException {
		File file = new File("test_log.txt");

		Document document = new PseudoDocument(
				"Test",
				"The first line",
				"#console \"" + file + "\"",
				"The third line",
				"#console ''",
				"The fifth#{' '}#line"
		);
		String expectedLog = "The third line";
		String expectedConsole = "The fifth line";

		Unit unit = new UnitImpl(new GlucoseSpec(
				DebugSpec.INSTANCE,
				listener(event -> {
					if (event.getAction().equals(Action.POST_EXEC)) {
						Memory memory = event.getMemory();
						String actualConsole = memory.getConsole().read();

						assertEquals(
								expectedConsole,
								actualConsole,
								"Console not reset"
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

		String actualLog = new String(Files.readAllBytes(file.toPath()));

		assertEquals(
				expectedLog,
				actualLog,
				"Log file not written"
		);

		file.delete();
	}
}
