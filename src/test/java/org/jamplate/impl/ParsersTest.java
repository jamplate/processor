package org.jamplate.impl;

import org.jamplate.model.*;
import org.jamplate.util.Trees;
import org.jamplate.util.model.CompilationImpl;
import org.jamplate.util.model.EnvironmentImpl;
import org.jamplate.util.model.PseudoDocument;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public class ParsersTest {
	@RepeatedTest(1)
	public void axe() {
		Document document = new PseudoDocument(
				"#DEFINE SpecialInteger (\"10 + 5\") + [(5 + 10), \".01\"]\n" +
				"import java.util.*; //#Define /*Blocky Block*/\n" +
				"/*\n" +
				"              #{SpecialInteger}#\n" +
				"   */\n" +
				"#{SpecialInteger + \"But\"}#Float\n" +
				"//#ifdef string.h\n" +
				"/*#include <string.h>*/\n" +
				"//#endif\n"
		);

		Environment environment = new EnvironmentImpl();
		Compilation compilation = environment.optCompilation(document);

		while (Parsers.PROCESSOR.process(compilation))
			;

		Tree hashDefine = compilation.getRootTree().getChild(); //the first line

		assertEquals(
				Kind.Transient.COMMAND,
				hashDefine.getSketch().getKind(),
				"expected #Define"
		);

		Tree type = hashDefine.getChild().getNext(); //DEFINE

		Tree parameter = type.getNext(); //("10 + 5") + [(5 + 10), ".01"]

		Tree round = parameter.getChild(); //("10 + 5")

		assertEquals(
				Kind.Syntax.ROUND,
				round.getSketch().getKind(),
				"(\"10 + 5\") is expected"
		);

		Tree tenPlusFive = round.getChild().getNext(); //"10 + 5"

		assertEquals(
				Kind.Syntax.DQUOTE,
				tenPlusFive.getSketch().getKind(),
				"\"10 + 5\" is expected"
		);

		Tree square = round.getNext(); //[(5 + 10), ".01"]

		//todo the rest
	}

	@Test
	public void include() {
		Document document = new PseudoDocument(
				"#include <string.h>\n#outclude <math.h>"
		);

		Environment environment = new EnvironmentImpl();
		Compilation compilation = environment.optCompilation(document);

		while (Parsers.PROCESSOR.process(compilation))
			;

		int i = 0;

		Tree root = compilation.getRootTree();
		Tree command1 = root.getChild();

		assertEquals(
				Kind.Transient.COMMAND,
				command1.getSketch().getKind(),
				"Expected the command being parsed"
		);

		Tree command2 = command1.getNext().getNext();

		assertEquals(
				Kind.Transient.COMMAND,
				command2.getSketch().getKind(),
				"Expected the other command being parsed"
		);

		while (Processors.PROCESSOR.process(compilation))
			;

		assertEquals(
				Kind.Command.INCLUDE,
				command1.getSketch().getKind(),
				"Expected the command being recognized as an include command"
		);
		assertEquals(
				Kind.Transient.COMMAND,
				command2.getSketch().getKind(),
				"Expected the other command not being recognized as any command"
		);
	}

	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void jManualInspection() throws IOException {
		Environment environment = Jamplate.DEFAULT.process(
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

		Compilation compilation = environment.optCompilation(document);

		while (Parsers.PROCESSOR.process(compilation))
			;
		while (Processors.PROCESSOR.process(compilation))
			;
		while (Compilers.PROCESSOR.process(compilation))
			;

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
			Environment environment = Jamplate.DEFAULT.process(
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

	@RepeatedTest(50)
	public void parse() {
		Environment environment = new EnvironmentImpl();
		Document document = new PseudoDocument("{\"name\":\"\\\"Sula{i}man\\\"{](\\\"\", \"ag}e\":\"\\\\\"}");
		Tree tree = new Tree(document);
		Compilation compilation = new CompilationImpl(environment, tree);

		Parsers.PROCESSOR.process(compilation);

		Set<Tree> trees = Trees.collect(tree);

		//01	curly					| {"name":"\"Sula{i}man\"{](\"", "ag}e":"\\"}
		//02		curly-open			| {
		//03		dquote				| "name"
		//04			dquote-open		| "
		//05			dquote-close	| "
		//06		dquote				| "\"Sula{i}man\"{](\""
		//07			dquote-open		| "
		//08			escape			| \"
		//09			curly			| {i}
		//10				curly-open	| {
		//11				curly-close	| }
		//12			escape			| \"
		//13			escape			| \"
		//14			dquote-close	| "
		//15		dquote				| "ag}e"
		//16			dquote-open		| "
		//17			dquote-close	| "
		//18		dquote				| "\\"
		//19			dquote-open		| "
		//20			escape			| \\
		//21			dquote-close	| "
		//22		curly-close			| }
		assertSame(
				22,
				trees.size(),
				"Wrong number of the trees expected"
		);
	}

	@RepeatedTest(2)
	public void parseNested() {
		String value = String.join("", Collections.nCopies(50, "{")) +
					   String.join("", Collections.nCopies(50, "}[")) + "]";
		Compilation compilation = new CompilationImpl(new EnvironmentImpl(), new Tree(new PseudoDocument(value)));

		Parsers.PROCESSOR.process(compilation);

		assertEquals(
				1 + 50 + (50 << 1) + 3, //root + scopes + anchors + bait
				Trees.collect(compilation.getRootTree()).size(),
				"Wrong number of trees"
		);
	}

	@RepeatedTest(50)
	public void parseRepeated() {
		String value = String.join("", Collections.nCopies(50, "{][}"));
		Compilation compilation = new CompilationImpl(new EnvironmentImpl(), new Tree(new PseudoDocument(value)));

		Parsers.PROCESSOR.process(compilation);

		assertEquals(
				1 + 50 + (50 << 1), //root + scopes + anchors
				Trees.collect(compilation.getRootTree()).size(),
				"Unexpected number of trees after parsing"
		);

		for (Tree tree : compilation.getRootTree())
			assertEquals(
					4,
					tree.reference().length(),
					"All the scopes are expected to have only 4 characters"
			);
	}

	@RepeatedTest(50)
	public void parseRepeatedIdentical() {
		String value = String.join("", Collections.nCopies(50, "\"'\""));
		Compilation compilation = new CompilationImpl(new EnvironmentImpl(), new Tree(new PseudoDocument(value)));

		//I am actually stunned to see that this works with RandomMergeParser!!!
		Parsers.PROCESSOR.process(compilation);

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
					Kind.Syntax.DQUOTE,
					tree.getSketch().getKind(),
					"Expected all to be double quotes"
			);
		}
	}
}
