package org.jamplate.impl.syntax;

import org.jamplate.model.Compilation;
import org.jamplate.model.Document;
import org.jamplate.model.Environment;
import org.jamplate.model.Tree;
import org.jamplate.util.Trees;
import org.jamplate.util.model.CompilationImpl;
import org.jamplate.util.model.EnvironmentImpl;
import org.jamplate.util.model.PseudoDocument;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public class JamplateSyntaxTest {
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
		Compilation compilation = environment.getCompilation(document);

		while (JamplateSyntax.PARSER_PROCESSOR.process(compilation))
			;

		Tree hashDefine = compilation.getRootTree().getChild(); //the first line

		assertEquals(
				TransientKind.COMMAND,
				hashDefine.getSketch().getKind(),
				"expected #Define"
		);

		Tree type = hashDefine.getChild().getNext(); //DEFINE

		Tree parameter = type.getNext(); //("10 + 5") + [(5 + 10), ".01"]

		Tree round = parameter.getChild(); //("10 + 5")

		assertEquals(
				SyntaxKind.ROUND,
				round.getSketch().getKind(),
				"(\"10 + 5\") is expected"
		);

		Tree tenPlusFive = round.getChild().getNext(); //"10 + 5"

		assertEquals(
				SyntaxKind.DQUOTE,
				tenPlusFive.getSketch().getKind(),
				"\"10 + 5\" is expected"
		);

		Tree square = round.getNext(); //[(5 + 10), ".01"]

		//todo the rest
	}

	@RepeatedTest(50)
	public void parse() {
		Environment environment = new EnvironmentImpl();
		Document document = new PseudoDocument("{\"name\":\"\\\"Sula{i}man\\\"{](\\\"\", \"ag}e\":\"\\\\\"}");
		Tree tree = new Tree(document);
		Compilation compilation = new CompilationImpl(environment, tree);

		JamplateSyntax.PARSER_PROCESSOR.process(compilation);

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

		JamplateSyntax.PARSER_PROCESSOR.process(compilation);

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

		JamplateSyntax.PARSER_PROCESSOR.process(compilation);

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
		JamplateSyntax.PARSER_PROCESSOR.process(compilation);

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
					SyntaxKind.DQUOTE,
					tree.getSketch().getKind(),
					"Expected all to be double quotes"
			);
		}
	}

	@Test
	public void x() {
//		Parser parser = new CollectParser(new CommandParser(
//				Pattern.compile("#"),
//				Pattern.compile("\n|$"),
//				Pattern.compile("include"),
//				Pattern.compile("<string\\.h>")
//		));
//		Processor processor = new ParserProcessor(parser);
//
//		Document document = new PseudoDocument(
//				"#include <string.h>\n#outclude <math.h>"
//		);
//
//		Environment environment = new EnvironmentImpl();
//		Compilation compilation = environment.getCompilation(document);
//
//		while (processor.process(compilation))
//			;
//
//		int i = 0;
	}
}
