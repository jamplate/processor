package org.jamplate.impl.syntax;

import org.jamplate.compile.Compilation;
import org.jamplate.impl.syntax.compile.SyntaxKind;
import org.jamplate.model.Tree;
import org.jamplate.util.Trees;
import org.jamplate.util.compile.BaseCompilation;
import org.jamplate.util.model.PseudoDocument;
import org.junit.jupiter.api.RepeatedTest;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public class JamplateSyntaxTest {
	@RepeatedTest(50)
	public void parse() {
		Tree tree = new Tree(new PseudoDocument("{\"name\":\"\\\"Sula{i}man\\\"{](\\\"\", \"ag}e\":\"\\\\\"}"));
		Compilation compilation = new BaseCompilation(tree);

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
		Compilation compilation = new BaseCompilation(new Tree(new PseudoDocument(value)));

		JamplateSyntax.PARSER_PROCESSOR.process(compilation);

		assertEquals(
				1 + 50 + (50 << 1) + 3, //root + scopes + anchors + bait
				Trees.collect(compilation.root()).size(),
				"Wrong number of trees"
		);
	}

	@RepeatedTest(50)
	public void parseRepeated() {
		String value = String.join("", Collections.nCopies(50, "{][}"));
		Compilation compilation = new BaseCompilation(new Tree(new PseudoDocument(value)));

		JamplateSyntax.PARSER_PROCESSOR.process(compilation);

		assertEquals(
				1 + 50 + (50 << 1), //root + scopes + anchors
				Trees.collect(compilation.root()).size(),
				"Unexpected number of trees after parsing"
		);

		for (Tree tree : compilation.root())
			assertEquals(
					4,
					tree.reference().length(),
					"All the scopes are expected to have only 4 characters"
			);
	}

	@RepeatedTest(50)
	public void parseRepeatedIdentical() {
		String value = String.join("", Collections.nCopies(50, "\"'\""));
		Compilation compilation = new BaseCompilation(new Tree(new PseudoDocument(value)));

		//I am actually stunned to see that this works with RandomMergeParser!!!
		JamplateSyntax.PARSER_PROCESSOR.process(compilation);

		assertEquals(
				1 + 50 + (50 << 1), //root + scopes + anchors
				Trees.collect(compilation.root()).size(),
				"Unexpected number of trees after parsing"
		);

		for (Tree tree : compilation.root()) {
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
}
