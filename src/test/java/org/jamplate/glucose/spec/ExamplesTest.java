package org.jamplate.glucose.spec;

import org.jamplate.glucose.spec.tool.DebugSpec;
import org.jamplate.impl.document.PseudoDocument;
import org.jamplate.impl.unit.Action;
import org.jamplate.impl.unit.UnitImpl;
import org.jamplate.model.Document;
import org.jamplate.unit.Unit;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.jamplate.glucose.internal.util.Values.number;
import static org.jamplate.util.Specs.listener;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ExamplesTest {
	@Test
	public void _99bottlesOfBeerOnTheWall() {
		//manual test :)
		Document document = new PseudoDocument(
				"Test",
				"#declare $i 99",
				"#declare $noun 'bottles'",
				"",
				"#while $i > 0",
				"    #message $i ' ' $noun \" of beer on the wall,\\n\"",
				"    #message $i ' ' $noun \" of beer.\\n\"",
				"    #message \"Take one down, pass it around,\\n\"",
				"",
				"    #declare $i $i - 1",
				"",
				"    #if $i == 0",
				"        #message \"No bottles of beer on the wall.\\n\\n\"",
				"    #else",
				"        #if $i == 1",
				"            #declare $noun 'bottle'",
				"#else",
				"        #endif",
				"",
				"        #message $i ' ' $noun \" of beer on the wall.\\n\\n\"",
				"    #endif",
				"#endwhile",
				"",
				"#message \"No bottles of beer on the wall,\\n\"",
				"#message \"No bottles of beer.\\n\"",
				"#message \"Go to the store, buy some more,\\n\"",
				"#message \"99 bottles of beer on the wall.\\n\""
		);

		Unit unit = new UnitImpl(new GlucoseSpec(DebugSpec.INSTANCE));

		if (
				!unit.initialize(document) ||
				!unit.parse(document) ||
				!unit.analyze(document) ||
				!unit.compile(document) ||
				!unit.execute(document)
		) {
			unit.diagnostic();
			fail("Test not completed");
		}
	}

	@Test
	public void _fibonacci() {
		for (
				int[] arr : new int[][]{
				{0, 0},
				{1, 1},
				{2, 1},
				{3, 2},
				{4, 3},
				{5, 5},
				{6, 8},
				{7, 13},
				{8, 21},
				{9, 34},
				{10, 55}
		}
		) {
			Document fibonacci = new PseudoDocument(
					"fibonacci.jh",
					"    #if $p == 0",
					"        #declare $r 0",
					"    #elif $p == 1",
					"        #declare $r 1",
					"    #else",
					"        #for $ort [$rt]",
					"            #declare $p $p - 1",
					"            #include __PATH__",
					"            #declare $rt $r",
					"",
					"            #declare $p $p - 1",
					"            #include __PATH__",
					"            #declare $rt $rt + $r",
					"",
					"            #declare $p $p + 2",
					"            #declare $r $rt",
					"            #declare $rt $ort",
					"        #endfor",
					"    #endif"
			);
			Document main = new PseudoDocument(
					"main.jamplate",
					"    #declare $p $input",
					"    #include __DIR__ 'fibonacci.jh'",
					"    #message $r"
			);

			Unit unit = new UnitImpl(new GlucoseSpec());

			unit.getSpec().add(listener(event -> {
				if (event.getAction().equals(Action.PRE_EXEC))
					event.getMemory().set(
							"$input",
							number(arr[0])
					);
			}));

			OutputStream outBuffer = new ByteArrayOutputStream();
			PrintStream out = System.out;
			System.setOut(new PrintStream(outBuffer));

			if (
					!unit.initialize(fibonacci, main) ||
					!unit.parse(fibonacci, main) ||
					!unit.analyze(fibonacci, main) ||
					!unit.compile(fibonacci, main) ||
					!unit.execute(main)
			) {
				unit.diagnostic();
				fail("Test not completed");
			}

			assertEquals(
					Integer.toString(arr[1]),
					outBuffer.toString(),
					"Fibonacci seq"
			);
			System.setOut(out);
		}
	}

	@Test
	public void _quine() {
		Document document = new PseudoDocument(
				//name
				"Main",
				//content
				"#declare input '#message \"#declare input \" \"\\'\" input \"\\'\" \"\\n\" input'",
				"#message \"#declare input \" \"\\'\" input \"\\'\" \"\\n\" input"
		);

		Unit unit = new UnitImpl(new GlucoseSpec());

		OutputStream outBuffer = new ByteArrayOutputStream();
		PrintStream out = System.out;
		System.setOut(new PrintStream(outBuffer));

		if (
				!unit.initialize(document) ||
				!unit.parse(document) ||
				!unit.analyze(document) ||
				!unit.compile(document) ||
				!unit.execute(document)
		) {
			unit.diagnostic();
			fail("Test not completed");
		}

		assertEquals(
				document.read(),
				outBuffer.toString(),
				"Weird Quine"
		);
		System.setOut(out);
	}
}
