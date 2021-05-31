//Use this class to manually test the file {@code /test_input2}.
package org.jamplate;

import org.jamplate.impl.Jamplate;
import org.jamplate.impl.model.EnvironmentImpl;
import org.jamplate.impl.model.FileDocument;
import org.jamplate.impl.util.Trees;
import org.jamplate.model.*;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Stream;

public class FileTest {
	public static String format(@NotNull CompileException exception) {
		Objects.requireNonNull(exception, "exception");
		StringBuilder builder = new StringBuilder();

		Tree tree = exception.getTree();

		builder.append(exception.getClass())
			   .append(": ")
			   .append(exception.getMessage());

		if (tree != null)
			builder.append("\n\tat ")
				   .append(tree.getSketch())
				   .append("(")
				   .append(tree.document())
				   .append(":")
				   .append(Trees.line(tree))
				   .append(") ")
				   .append("\n\t")
				   .append(Trees.readLine(tree))
				   .append("\n\t")
				   .append(String.join(
						   "",
						   Collections.nCopies(
								   Trees.positionInLine(tree),
								   " "
						   )
				   ))
				   .append("^")
				   .append(String.join(
						   "",
						   Collections.nCopies(
								   Math.max(tree.reference().length() - 1, 0),
								   "-"
						   )
				   ));

		return builder.toString();
	}

	public static String format(@NotNull ExecutionException exception, @NotNull Memory memory) {
		Objects.requireNonNull(exception, "exception");
		StringBuilder builder = new StringBuilder();

		builder.append(exception.getClass())
			   .append(": ")
			   .append(exception.getMessage());

		Tree[] dup = {null};
		Frame[] frames = memory.getFrames();
		Collections.reverse(Arrays.asList(frames));
		Stream.concat(
				Stream.of(exception.getTree()),
				Arrays.stream(frames)
					  .map(Frame::getInstruction)
					  .filter(Objects::nonNull)
					  .map(Instruction::getTree)
		)
			  .sequential()
			  .filter(Objects::nonNull)
			  .filter(t -> {
				  if (dup[0] != t) {
					  dup[0] = t;
					  return true;
				  }

				  return false;
			  })
			  .forEach(t ->
					  builder.append("\n\t")
							 .append("at ")
							 .append(t.getSketch())
							 .append("(")
							 .append(t.document())
							 .append(":")
							 .append(Trees.line(t))
							 .append(")")
			  );

		return builder.toString();
	}

	public static String format(@NotNull IllegalTreeException exception) {
		Objects.requireNonNull(exception, "exception");
		StringBuilder builder = new StringBuilder();
		Tree primary = exception.getPrimaryTree();
		Tree illegal = exception.getIllegalTree();

		builder.append(exception.getClass())
			   .append(": ")
			   .append(exception.getMessage());

		if (primary != null)
			builder.append("\n\tat ")
				   .append(primary.getSketch())
				   .append("(")
				   .append(primary.document())
				   .append(":")
				   .append(Trees.line(primary))
				   .append(") ")
				   .append("\n\t")
				   .append(Trees.readLine(primary))
				   .append("\n\t")
				   .append(String.join(
						   "",
						   Collections.nCopies(
								   Trees.positionInLine(primary),
								   " "
						   )
				   ))
				   .append("^")
				   .append(String.join(
						   "",
						   Collections.nCopies(
								   Math.max(illegal.reference().length() - 1, 0),
								   "-"
						   )
				   ));
		if (illegal != null)
			builder.append("\n\tat ")
				   .append(illegal.getSketch())
				   .append("(")
				   .append(illegal.document())
				   .append(":")
				   .append(Trees.line(illegal))
				   .append(") ")
				   .append("\n\t")
				   .append(Trees.readLine(illegal))
				   .append("\n\t")
				   .append(String.join(
						   "",
						   Collections.nCopies(
								   Trees.positionInLine(illegal),
								   " "
						   )
				   ))
				   .append("^")
				   .append(String.join(
						   "",
						   Collections.nCopies(
								   Math.max(illegal.reference().length() - 1, 0),
								   "-"
						   )
				   ));

		return builder.toString();
	}

	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void run() {
		Environment environment = new EnvironmentImpl();

		try {
			Jamplate.INITIALIZER.initialize(
					environment,
					new FileDocument("test_input2/test.jamplate")
			);

			for (Compilation compilation : environment.compilationSet()) {
				Instruction instruction = compilation.getInstruction();

				Memory m = null;
				try (Memory memory = m = new Memory()) {
					memory.pushFrame(new Frame(instruction));

					instruction.exec(environment, memory);
				} catch (IOException e) {
					System.err.println(format(new ExecutionException(e), m));
					System.err.println();
					e.printStackTrace();
				} catch (ExecutionException e) {
					System.err.println(format(e, m));
					System.err.println();
					e.printStackTrace();
				}

				System.out.println(m.getConsole());
			}
		} catch (CompileException e) {
			System.err.println(format(e));
			System.err.println();
			e.printStackTrace();
		} catch (IllegalTreeException e) {
			System.err.println(format(e));
			System.err.println();
			e.printStackTrace();
		}
	}
}
