package org.jamplate;

import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

@SuppressWarnings("ALL")
public class JamplateTest {
	@Test
	@Ignore
	public void testFolder() throws IOException {
		//make sure that 'test_input' and 'test_output' exists!
		File inputDirectory = new File("test_input");
		File outputDirectory = new File("test_output");
		for (File input : inputDirectory.listFiles()) {
			Jamplate.process(
					input,
					new File(outputDirectory, input.getName().replace(".jamplate", ".java")),
					Collections.emptyMap()
			);
		}
	}
}
