package org.jamplate.glucose.spec;

import org.jamplate.glucose.spec.tool.DebugSpec;
import org.jamplate.impl.document.PseudoDocument;
import org.jamplate.impl.unit.UnitImpl;
import org.jamplate.model.Document;
import org.jamplate.unit.Unit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

public class ExamplesTest {
	@Test
	public void _99bottlesOfBeerOnTheWall() {
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
}
