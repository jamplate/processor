//Use this class to manually test the directory test_input
package org.jamplate;

import org.jamplate.impl.Jamplate;
import org.jamplate.impl.Meta;
import org.jamplate.impl.model.EnvironmentImpl;
import org.jamplate.impl.model.FileDocument;
import org.jamplate.impl.model.PseudoDocument;
import org.jamplate.model.Compilation;
import org.jamplate.model.Document;
import org.jamplate.model.Environment;
import org.junit.jupiter.api.Test;

import java.io.File;

public class FileTest {
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void pseudo() {
		Document[] documents = {
				new PseudoDocument(
						"index.jamplate",
						""
				)
		};

		Environment environment = new EnvironmentImpl();

		environment.getMeta().put(Meta.PROJECT, new File("test_input"));

		boolean compiled = Jamplate.compile(environment, documents);

		if (!compiled) {
			System.err.println("Compilation Error");
			System.err.println();
			environment.getDiagnostic()
					   .flush(true);
			return;
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
					   .flush(true);
			return;
		}

		environment.getDiagnostic()
				   .flush();

		System.out.println();
		System.out.println("Jamplate ended successfully");
	}

	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void run() {
		Document[] documents = FileDocument.hierarchy(new File("test_input"));

		Environment environment = new EnvironmentImpl();

		environment.getMeta().put(Meta.PROJECT, new File("test_input"));

		boolean compiled = Jamplate.compile(environment, documents);

		if (!compiled) {
			System.err.println("Compilation Error");
			System.err.println();
			environment.getDiagnostic()
					   .flush(true);
			return;
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
					   .flush(true);
			return;
		}

		environment.getDiagnostic()
				   .flush();

		System.out.println();
		System.out.println("Jamplate ended successfully");
	}
}
