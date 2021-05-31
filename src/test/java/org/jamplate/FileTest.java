//Use this class to manually test the directory test_input
package org.jamplate;

import org.jamplate.impl.Address;
import org.jamplate.impl.Jamplate;
import org.jamplate.impl.model.EnvironmentImpl;
import org.jamplate.impl.model.FileDocument;
import org.jamplate.model.Compilation;
import org.jamplate.model.Document;
import org.jamplate.model.Environment;
import org.junit.jupiter.api.Test;

import java.io.File;

public class FileTest {
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void run() {
		Document[] documents = FileDocument.hierarchy(new File("test_input"));

		Environment environment = new EnvironmentImpl();

		environment.getMeta().put(Address.PROJECT, new File("test_input"));

		boolean compiled = Jamplate.compile(environment, documents);

		if (!compiled) {
			System.err.println("Compilation Error");
			System.err.println();
			environment.getDiagnostic()
					   .flush();
		}

		Compilation[] jamplates = environment
				.compilationSet()
				.stream()
				.filter(compilation -> compilation.getRootTree().document().toString().endsWith(".jamplate"))
				.toArray(Compilation[]::new);

		boolean executed = Jamplate.execute(environment, jamplates);

		if (!executed) {
			System.err.println("Runtime Error");
			System.err.println();
			environment.getDiagnostic()
					   .flush();
		}

		environment.getDiagnostic()
				   .flush();

		System.out.println();
		System.out.println("Jamplate ended successfully");
	}
}
