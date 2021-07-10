package org.jamplate.glucose.spec.command.hashmessage;

import org.jamplate.unit.Unit;
import org.jamplate.glucose.spec.document.RootSpec;
import org.jamplate.glucose.spec.document.TextSpec;
import org.jamplate.glucose.spec.element.CommandSpec;
import org.jamplate.glucose.spec.element.ParameterSpec;
import org.jamplate.glucose.spec.parameter.operator.AdderSpec;
import org.jamplate.glucose.spec.parameter.resource.EscapedStringSpec;
import org.jamplate.glucose.spec.parameter.resource.StringSpec;
import org.jamplate.glucose.spec.syntax.enclosure.DoubleQuotesSpec;
import org.jamplate.glucose.spec.syntax.enclosure.QuotesSpec;
import org.jamplate.glucose.spec.syntax.symbol.PlusSpec;
import org.jamplate.glucose.spec.tool.OptimizeSpec;
import org.jamplate.impl.spec.MultiSpec;
import org.jamplate.impl.unit.UnitImpl;
import org.jamplate.impl.document.PseudoDocument;
import org.jamplate.model.Document;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class MessageTest {
	@Test
	public void test0() {
		OutputStream outBuffer = new ByteArrayOutputStream();
		PrintStream outOrigin = System.out;
		System.setOut(new PrintStream(outBuffer));

		//------------------------------------------------------------------------------
		Document document = new PseudoDocument(
				"#message 'Whats ' + 'Up!' + \"\\nMa Man!\""
		);
		String expectedAtOut = "Whats Up!\nMa Man!";

		Unit unit = new UnitImpl(new MultiSpec(
				//name
				"MainSpec",
				//tools
				OptimizeSpec.INSTANCE,
				//commands
				new CommandSpec(
						HashMessageSpec.INSTANCE
				),
				//parameters
				new ParameterSpec(
						//syntax
						QuotesSpec.INSTANCE,
						DoubleQuotesSpec.INSTANCE,
						PlusSpec.INSTANCE,
						//resources
						EscapedStringSpec.INSTANCE,
						StringSpec.INSTANCE,
						//operators
						AdderSpec.INSTANCE
				),
				//document
				RootSpec.INSTANCE,
				TextSpec.INSTANCE
		));

		if (
				!unit.initialize(document) ||
				!unit.parse(document) ||
				!unit.analyze(document) ||
				!unit.compile(document) ||
				!unit.execute(document)
		) {
			unit.diagnostic();
			fail("Uncompleted test invocation");
		}

		//------------------------------------------------------------------------------
		System.setOut(outOrigin);

		assertEquals(
				expectedAtOut,
				outBuffer.toString(),
				"Unexpected message printed to System.out"
		);
	}
}
