//Use this class to manually test the file {@code /test_input2}.
package org.jamplate;

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

		boolean compiled = Jamplate.compile(environment, documents);

		if (!compiled) {
			System.err.println("Compilation Error");
			System.err.println();
			environment.getDiagnostic()
					   .flush();
		}

		Compilation index = environment.getCompilation("test_input/index.jamplate");

		boolean executed = Jamplate.execute(environment, index);

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
//	public static Set<File> children(@Nullable File file) {
//		Objects.requireNonNull(file, "file");
//		Set<File> fileSet = new HashSet<>();
//		children(fileSet, file);
//		return fileSet;
//	}
//
//	public static void children(@NotNull Set<File> fileSet, @Nullable File file) {
//		Objects.requireNonNull(fileSet, "fileSet");
//		Objects.requireNonNull(file, "file");
//		if (file.isDirectory()) {
//			File[] children = file.listFiles();
//
//			if (children != null)
//				for (File child : children)
//					if (child != null)
//						children(fileSet, child);
//
//			return;
//		}
//
//		fileSet.add(file);
//	}
//
//	public static String format(@NotNull ExecutionException exception, @NotNull Memory memory) {
//		Objects.requireNonNull(exception, "exception");
//		StringBuilder builder = new StringBuilder();
//
//		builder.append(exception.getClass())
//			   .append(": ")
//			   .append(exception.getMessage());
//
//		for (Tree tree : memory.getStackTrace())
//			builder.append("\n\t")
//				   .append("at ")
//				   .append(tree.getSketch())
//				   .append("(")
//				   .append(tree.document())
//				   .append(":")
//				   .append(Trees.line(tree))
//				   .append(")");
//
//		return builder.toString();
//	}
//
//	public static String format(@NotNull IllegalTreeException exception) {
//		Objects.requireNonNull(exception, "exception");
//		StringBuilder builder = new StringBuilder();
//		Tree primary = exception.getPrimaryTree();
//		Tree illegal = exception.getIllegalTree();
//
//		builder.append(exception.getClass())
//			   .append(": ")
//			   .append(exception.getMessage());
//
//		if (primary != null)
//			builder.append("\n\tat ")
//				   .append(primary.getSketch())
//				   .append("(")
//				   .append(primary.document())
//				   .append(":")
//				   .append(Trees.line(primary))
//				   .append(") ")
//				   .append("\n\t")
//				   .append(Trees.readLine(primary))
//				   .append("\n\t")
//				   .append(String.join(
//						   "",
//						   Collections.nCopies(
//								   Trees.positionInLine(primary),
//								   " "
//						   )
//				   ))
//				   .append("^")
//				   .append(String.join(
//						   "",
//						   Collections.nCopies(
//								   Math.max(illegal.reference().length() - 1, 0),
//								   "-"
//						   )
//				   ));
//		if (illegal != null)
//			builder.append("\n\tat ")
//				   .append(illegal.getSketch())
//				   .append("(")
//				   .append(illegal.document())
//				   .append(":")
//				   .append(Trees.line(illegal))
//				   .append(") ")
//				   .append("\n\t")
//				   .append(Trees.readLine(illegal))
//				   .append("\n\t")
//				   .append(String.join(
//						   "",
//						   Collections.nCopies(
//								   Trees.positionInLine(illegal),
//								   " "
//						   )
//				   ))
//				   .append("^")
//				   .append(String.join(
//						   "",
//						   Collections.nCopies(
//								   Math.max(illegal.reference().length() - 1, 0),
//								   "-"
//						   )
//				   ));
//
//		return builder.toString();
//	}
//
//	public static String format(@NotNull CompileException exception) {
//		Objects.requireNonNull(exception, "exception");
//		StringBuilder builder = new StringBuilder();
//
//		Tree tree = exception.getTree();
//
//		builder.append(exception.getClass())
//			   .append(": ")
//			   .append(exception.getMessage());
//
//		if (tree != null)
//			builder.append("\n\tat ")
//				   .append(tree.getSketch())
//				   .append("(")
//				   .append(tree.document())
//				   .append(":")
//				   .append(Trees.line(tree))
//				   .append(") ")
//				   .append("\n\t")
//				   .append(Trees.readLine(tree))
//				   .append("\n\t")
//				   .append(String.join(
//						   "",
//						   Collections.nCopies(
//								   Trees.positionInLine(tree),
//								   " "
//						   )
//				   ))
//				   .append("^")
//				   .append(String.join(
//						   "",
//						   Collections.nCopies(
//								   Math.max(tree.reference().length() - 1, 0),
//								   "-"
//						   )
//				   ));
//
//		return builder.toString();
//	}
//
//	public static void run(@Nullable File file) {
//		Objects.requireNonNull(file, "file");
//		Document[] documents =
//				children(file)
//						.stream()
//						.map(FileDocument::new)
//						.toArray(Document[]::new);
//
//		run(documents);
//	}
//
//	public static void run(@Nullable Document @NotNull ... documents) {
//		Environment environment = new EnvironmentImpl();
//
//		try {
//			Jamplate.INITIALIZER.initialize(
//					environment,
//					documents
//			);
//
//			for (Compilation compilation : environment.compilationSet()) {
//				Instruction instruction = compilation.getInstruction();
//
//				Memory m = null;
//				try (Memory memory = m = new Memory()) {
//					memory.pushFrame(new Frame(instruction));
//
//					instruction.exec(environment, memory);
//				} catch (IOException e) {
//					System.err.println(format(new ExecutionException(e), m));
//					System.err.println();
//					e.printStackTrace();
//				} catch (ExecutionException e) {
//					System.err.println(format(e, m));
//					System.err.println();
//					e.printStackTrace();
//				}
//
//				if (m.getConsole() instanceof StringBuilder)
//					System.out.println(m.getConsole());
//			}
//		} catch (CompileException e) {
//			System.err.println(format(e));
//			System.err.println();
//			e.printStackTrace();
//		} catch (IllegalTreeException e) {
//			System.err.println(format(e));
//			System.err.println();
//			e.printStackTrace();
//		}
//	}
