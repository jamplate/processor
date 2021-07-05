package org.jamplate.glucose.spec.command.hasherror;

import org.jamplate.api.Unit;
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
import org.jamplate.glucose.spec.tool.DebugSpec;
import org.jamplate.glucose.spec.tool.OptimizeSpec;
import org.jamplate.impl.api.MultiSpec;
import org.jamplate.impl.api.UnitImpl;
import org.jamplate.impl.model.PseudoDocument;
import org.jamplate.model.Document;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ErrorTest {
	@Test
	public void test0() {
		OutputStream errBuffer = new ByteArrayOutputStream();
		PrintStream errOrigin = System.err;
		System.setErr(new PrintStream(errBuffer));

		//------------------------------------------------------------------------------
		Document document = new PseudoDocument(
				"#error 'Hmm... ' + 'SoMeThInGi' + \"\\nSwEiRd\""
		);
		String expectedAtOut = "Hmm... SoMeThInGi\nSwEiRd";

		Unit unit = new UnitImpl(new MultiSpec(
				//name
				"MainSpec",
				//tools
				DebugSpec.INSTANCE,
				OptimizeSpec.INSTANCE,
				//commands
				new CommandSpec(
						HashErrorSpec.INSTANCE
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
		System.setErr(errOrigin);

		assertEquals(
				expectedAtOut,
				errBuffer.toString(),
				"Unexpected message printed to System.out"
		);
	}
}
