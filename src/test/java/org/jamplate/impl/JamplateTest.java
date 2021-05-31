package org.jamplate.impl;

import org.jamplate.impl.model.CompilationImpl;
import org.jamplate.impl.model.EnvironmentImpl;
import org.jamplate.impl.model.PseudoDocument;
import org.jamplate.impl.processor.ParserProcessor;
import org.jamplate.impl.util.Trees;
import org.jamplate.model.*;
import org.jamplate.model.function.Processor;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public class JamplateTest {
	@Test
	public void include() {
		Document document = new PseudoDocument(
				"#include <string.h>\n#outclude <math.h>"
		);

		Environment environment = new EnvironmentImpl();
		Compilation compilation = environment.optCompilation(document);

		Jamplate.PARSER_PROCESSOR.process(compilation);

		Tree root = compilation.getRootTree();
		Tree command1 = root.getChild();

		assertEquals(
				Kind.CX_CMD,
				command1.getSketch().getKind(),
				"Expected the command being parsed"
		);

		Tree command2 = command1.getNext().getNext();

		assertEquals(
				Kind.CX_CMD,
				command2.getSketch().getKind(),
				"Expected the other command being parsed"
		);

		while (Jamplate.ANALYZER_PROCESSOR.process(compilation))
			;

		assertEquals(
				Kind.CX_CMD_INCLUDE,
				command1.getChild().getSketch().getKind(),
				"Expected the command being recognized as an include command"
		);
		assertEquals(
				Kind.CX_CMD_OPEN,
				command2.getChild().getSketch().getKind(),
				"Expected the other command not being recognized as any command"
		);
	}

	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void jManualInspection() throws IOException {
		Environment environment = new EnvironmentImpl();
		Jamplate.INITIALIZER.initialize(
				environment,
				new PseudoDocument("Main",
						"#define Hello \"H3110\"\n" +
						"#define Hello \"heLLo\"\n" +
						"#declare Bye \"8y3\"\n" +
						"Hello and Bye\n" +
						"#include \"Hi\"  \n" +
						"\n" +
						"//#console \"test\"\n" +
						"//#include \"Hi\"\n" +
						"#include \"Hi\"\n" +
						"#declare __define__ \"{\\\"Hello\\\":\\\"Yellow\\\"}\"\n" +
						"Hello"
				),
				new PseudoDocument("Hi", "Included")
		);

		for (Compilation compilation : environment.compilationSet()) {
			Instruction instruction = compilation.getInstruction();

			Memory memory = new Memory();
			memory.getFrame().setInstruction(instruction);

			try {
				instruction.exec(environment, memory);
			} catch (ExecutionException e) {
				System.err.print(e.getTree().document());
				System.err.print(":");
				System.err.print(Trees.line(e.getTree()));
				System.err.print(": ");
				System.err.print(e.getMessage());
				System.err.println();
				for (Memory.Frame frame : memory.getFrames()) {
					Tree tree = frame.getInstruction().getTree();
					System.err.print("\t");
					System.err.print("at ");
					System.err.print(tree.getSketch());
					System.err.print("(");
					System.err.print(tree.document());
					System.err.print(":");
					System.err.print(Trees.line(frame.getInstruction().getTree()));
					System.err.print(")");
					System.err.println();
				}
			}

			System.out.println("Compilation: " + compilation.getRootTree().document());
			System.out.println("------------------------------");
			System.out.println(memory.getConsole());
			System.out.println("------------------------------");
			System.out.println();
			memory.close();
		}
	}

	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void manualInspection() throws IOException {
		Environment environment = new EnvironmentImpl();

		Document document = new PseudoDocument(
				"#define Hello \"H3110\"\n" +
				"#define Hello \"heLLo\"\n" +
				"#declare Bye \"8y3\"\n" +
				"Hello and Bye\n" +
				"#include \"Hi\"  \n" +
				"\n" +
				"//#console \"test\"\n" +
				"//#include \"Hi\"\n" +
				"#include \"Hi\"\n" +
				"#declare __define__ \"{\\\"Hello\\\":\\\"Yellow\\\"}\"\n" +
				"Hello"
		);

		Jamplate.INITIALIZER.initialize(environment, document);

		Compilation compilation = environment.getCompilation(document);

		Instruction instruction = compilation.getInstruction();

		environment.optCompilation(new PseudoDocument("Hi", ""))
				   .setInstruction((environment1, memory) ->
						   memory.print("Included")
				   );

		Memory memory = new Memory();
		memory.getFrame().setInstruction(instruction);

		try {
			instruction.exec(environment, memory);
		} catch (ExecutionException e) {
			System.err.print(e.getClass());
			System.err.print(": ");
			System.err.print(e.getMessage());
			System.err.println();
			for (Memory.Frame frame : memory.getFrames()) {
				Tree tree = frame.getInstruction().getTree();
				System.err.print("\t");
				System.err.print("at ");
				System.err.print(tree.getSketch());
				System.err.print("(");
				System.err.print(document);
				System.err.print(":");
				System.err.print(Trees.line(frame.getInstruction().getTree()));
				System.err.print(")");
				System.err.println();
			}
		}

		System.out.println(memory.getConsole());
		memory.close();
	}

	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void manualParseIf() throws IOException {
		Memory memory = null;
		try {
			Environment environment = new EnvironmentImpl();
			Jamplate.INITIALIZER.initialize(
					environment,
					new PseudoDocument("Main",
							/*01*/"BEFORE IF\n" +
							/*02*/"#if \"1\"\r\n" +
							/*03*/"If first\n" +
							/*04*/"#if \"0\"\n" +
							/*05*/"If first and second\n" +
							/*06*/"#else\n" +
							/*07*/"If first but not second\n" +
							/*08*/"#endif\n" +
							/*09*/"#elif \"0\"\n" +
							/*10*/"If not first but third\n" +
							/*11*/"#else\n" +
							/*12*/"If not first nor third\n" +
							/*13*/"#endif\n" +
							/*14*/"AFTER IF"
					)
			);

			for (Compilation compilation : environment.compilationSet()) {
				Instruction instruction = compilation.getInstruction();

				memory = new Memory();
				memory.getFrame().setInstruction(instruction);

				Trees.readLine(compilation.getRootTree().getChild().getNext().getNext());
				instruction.exec(environment, memory);

				System.out.println(
						"Compilation: " + compilation.getRootTree().document()
				);
				System.out.println("------------------------------");
				System.out.println(memory.getConsole());
				System.out.println("------------------------------");
				System.out.println();
				memory.close();
			}
		} catch (CompileException e) {
			Tree eTree = e.getTree();

			if (eTree == null)
				System.err.println(
						e.getClass() +
						": " +
						e.getMessage()
				);
			else
				System.err.println(
						e.getTree().document() +
						":" +
						Trees.line(e.getTree()) +
						": " +
						e.getMessage() +
						"\n\t" +
						Trees.readLine(e.getTree())
				);
		} catch (ExecutionException e) {
			Tree eTree = e.getTree();

			if (eTree == null)
				System.err.println(
						e.getClass() +
						": " +
						e.getMessage()
				);
			else
				System.err.println(
						eTree.document() +
						":" +
						Trees.line(eTree) +
						": " +
						e.getMessage()
				);
			if (memory != null)
				for (Memory.Frame frame : memory.getFrames()) {
					Tree frameTree = frame.getInstruction().getTree();

					if (frameTree != null)
						System.err.println(
								"\t" +
								"at " +
								frameTree.getSketch() +
								"(" +
								frameTree.document() +
								":" +
								Trees.line(frame.getInstruction().getTree()) +
								")"
						);
				}
		}
	}

	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void manualParseRef() throws IOException {
		Memory memory = null;
		try {
			Environment environment = new EnvironmentImpl();
			Jamplate.INITIALIZER.initialize(
					environment,
					new PseudoDocument("Main",
							/*01*/"BEFORE IF\n" +
							/*02*/"#if \"1\"\r\n" +
							/*03*/"#declare A \"HelloWorld\"\n" +
							/*04*/"#if \"1\"\n" +
							/*05*/"#declare B \"WorldHello\"\n" +
							/*06*/"#else\n" +
							/*07*/"#declare C \"Hi\"\n" +
							/*08*/"#endif\n" +
							/*09*/"#elif \"0\"\n" +
							/*10*/"#declare D \"Ohio\"\n" +
							/*11*/"#else\n" +
							/*12*/"#declare X \"Master\"\n" +
							/*13*/"#endif\n" +
							/*14*/"#define output A\n" +
							/*15*/"A is output\n" +
							/*16*/"#define output B\n" +
							/*17*/"B is output\n" +
							/*18*/"#define output C\n" +
							/*19*/"C is output\n" +
							/*20*/"#define output D\n" +
							/*21*/"D is output\n" +
							/*22*/"#define output X\n" +
							/*23*/"X is output\n"
					)
			);

			for (Compilation compilation : environment.compilationSet()) {
				Instruction instruction = compilation.getInstruction();

				memory = new Memory();
				memory.getFrame().setInstruction(instruction);

				Trees.readLine(compilation.getRootTree().getChild().getNext().getNext());
				instruction.exec(environment, memory);

				System.out.println(
						"Compilation: " + compilation.getRootTree().document()
				);
				System.out.println("------------------------------");
				System.out.println(memory.getConsole());
				System.out.println("------------------------------");
				System.out.println();
				memory.close();
			}
		} catch (CompileException e) {
			Tree eTree = e.getTree();

			if (eTree == null)
				System.err.println(
						e.getClass() +
						": " +
						e.getMessage()
				);
			else
				System.err.println(
						e.getTree().document() +
						":" +
						Trees.line(e.getTree()) +
						": " +
						e.getMessage() +
						"\n\t" +
						Trees.readLine(e.getTree())
				);
		} catch (ExecutionException e) {
			Tree eTree = e.getTree();

			if (eTree == null)
				System.err.println(
						e.getClass() +
						": " +
						e.getMessage()
				);
			else
				System.err.println(
						eTree.document() +
						":" +
						Trees.line(eTree) +
						": " +
						e.getMessage()
				);
			if (memory != null)
				for (Memory.Frame frame : memory.getFrames()) {
					Tree frameTree = frame.getInstruction().getTree();

					if (frameTree != null)
						System.err.println(
								"\t" +
								"at " +
								frameTree.getSketch() +
								"(" +
								frameTree.document() +
								":" +
								Trees.line(frame.getInstruction().getTree()) +
								")"
						);
				}
		}
	}

	@RepeatedTest(50)
	public void parseRepeatedIdentical() {
		String value = String.join("", Collections.nCopies(50, "\"'\""));
		Compilation compilation = new CompilationImpl(new EnvironmentImpl(), new Tree(new PseudoDocument(value)));

		//I am actually stunned to see that this works with RandomMergeParser!!!
		Processor processor = new ParserProcessor(Jamplate.PARSER);
		processor.process(compilation);

		assertEquals(
				1 + 50 + (50 << 1), //root + scopes + anchors
				Trees.collect(compilation.getRootTree()).size(),
				"Unexpected number of trees after parsing"
		);

		for (Tree tree : compilation.getRootTree()) {
			assertEquals(
					3,
					tree.reference().length(),
					"All the scopes are expected to have only 4 characters"
			);
			assertSame(
					Kind.SX_DQT,
					tree.getSketch().getKind(),
					"Expected all to be double quotes"
			);
		}
	}
}
