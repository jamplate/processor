package org.jamplate;

import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

@SuppressWarnings("ALL")
public class JamplateTest {
	@Test
	public void testFolder() throws IOException {
		//make sure that 'test_input' and 'test_output' exists!
//		File inputDirectory = new File("/Projects/cufy/framework/src/main/jamplate/cufy/util/function");
		File inputDirectory = new File("test_input");
		File outputDirectory = new File("test_output");
		for (File input : inputDirectory.listFiles())
			try {
				Jamplate.process(
						input,
						new File(outputDirectory, input.getName().replace(".jamplate", ".java")),
						Collections.emptyMap()
				);
			} catch (Throwable e) {
				throw new RuntimeException(input.getName(), e);
			}
	}

	@Test
	public void testFile() throws IOException {
		File input = new File("test_input/axe.jamplate");
		File output = new File("test_output/axe.java");
		output.getParentFile().mkdirs();
		Jamplate.process(input, output, Collections.emptyMap());
	}
}
