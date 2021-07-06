package org.jamplate.glucose.spec.command.hashdeclare;

import org.jamplate.api.Unit;
import org.jamplate.glucose.spec.document.RootSpec;
import org.jamplate.glucose.spec.document.TextSpec;
import org.jamplate.glucose.spec.element.CommandSpec;
import org.jamplate.glucose.spec.element.ParameterSpec;
import org.jamplate.glucose.spec.misc.NewlineEscapedSpec;
import org.jamplate.glucose.spec.misc.NewlineSpec;
import org.jamplate.glucose.spec.misc.NewlineSuppressedSpec;
import org.jamplate.glucose.spec.parameter.resource.ArraySpec;
import org.jamplate.glucose.spec.parameter.resource.EscapedStringSpec;
import org.jamplate.glucose.spec.parameter.resource.NumberSpec;
import org.jamplate.glucose.spec.parameter.resource.ReferenceSpec;
import org.jamplate.glucose.spec.syntax.enclosure.BracketsSpec;
import org.jamplate.glucose.spec.syntax.enclosure.DoubleQuotesSpec;
import org.jamplate.glucose.spec.syntax.enclosure.QuotesSpec;
import org.jamplate.glucose.spec.syntax.term.DigitsSpec;
import org.jamplate.glucose.spec.syntax.term.WordSpec;
import org.jamplate.glucose.spec.tool.DebugSpec;
import org.jamplate.glucose.spec.tool.OptimizeSpec;
import org.jamplate.impl.api.Action;
import org.jamplate.impl.api.UnitImpl;
import org.jamplate.impl.model.PseudoDocument;
import org.jamplate.memory.Memory;
import org.jamplate.model.Document;
import org.junit.jupiter.api.Test;

import static org.jamplate.internal.util.Specs.listener;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class HashDeclareSpecTest {
	@Test
	public void test0() {
		Document document = new PseudoDocument(
				/*01*/"The first line\n" +
				/*02*/"The second line\n" +
				/*03*/"#declare Address['Key'] Value\\\n" +
				/*04*/"The fourth line\\\n" +
				/*05*/"The fifth line\n" +
				/*06*/"The seventh line\\\n" +
				/*06*/"The eighth line\\\n" +
				/*06*/"The ninth line\n"
		);

		Unit unit = new UnitImpl();

		//misc
		unit.getSpec().add(DebugSpec.INSTANCE);
		unit.getSpec().add(OptimizeSpec.INSTANCE);

		//document
		unit.getSpec().add(RootSpec.INSTANCE);
		unit.getSpec().add(NewlineSpec.INSTANCE);
		unit.getSpec().add(NewlineEscapedSpec.INSTANCE);
		unit.getSpec().add(NewlineSuppressedSpec.INSTANCE);

		//commands
		unit.getSpec().add(new CommandSpec(
				HashDeclareSpec.INSTANCE,
				TouchDeclareSpec.INSTANCE
		));

		//parameters
		unit.getSpec().add(new ParameterSpec(
				//syntax
				WordSpec.INSTANCE,
				BracketsSpec.INSTANCE,
				QuotesSpec.INSTANCE,
				//param
				ReferenceSpec.INSTANCE,
				ArraySpec.INSTANCE,
				EscapedStringSpec.INSTANCE,
				//compat
				NewlineEscapedSpec.INSTANCE
		));

		//glue
		unit.getSpec().add(TextSpec.INSTANCE);

		//assertions
		unit.getSpec().add(listener(event -> {
			if (event.getAction().equals(Action.POST_EXEC)) {
				Memory memory = event.getMemory();
				String address = memory.get("Address").evaluate(memory);
				String console = memory.getConsole().read();

				assertEquals(
						"{\"Key\":\"Value" + "The" + "fourth" + "line" + "The" +
						"fifth" +
						"line\"}",
						address,
						"Unexpected value at the address 'address'"
				);
				assertEquals(
						"The first line\n" +
						"The second line" +
						"The seventh line" +
						"The eighth line" +
						"The ninth line\n" +
						"",
						console,
						"Unexpected console value"
				);
			}
		}));

		if (
				!unit.initialize(document) ||
				!unit.parse(document) ||
				!unit.analyze(document) ||
				!unit.compile(document)
		) {
			unit.diagnostic();
			fail("Uncompleted test invocation");
		}

		unit.optimize(document, -1);

		if (!unit.execute(document)) {
			unit.diagnostic();
			fail("Uncompleted test invocation");
		}
	}

	@Test
	public void test1() {
		Document document = new PseudoDocument(
				"#declare Address[Key][Nested][Plex][3] 123"
		);
		String expectedAddress = "{\"Key\":{\"Nested\":{\"Plex\":[,,,123]}}}";

		Unit unit = new UnitImpl();

		//misc
		unit.getSpec().add(DebugSpec.INSTANCE);
		unit.getSpec().add(OptimizeSpec.INSTANCE);

		//document
		unit.getSpec().add(RootSpec.INSTANCE);
		unit.getSpec().add(NewlineSpec.INSTANCE);
		unit.getSpec().add(NewlineEscapedSpec.INSTANCE);
		unit.getSpec().add(NewlineSuppressedSpec.INSTANCE);

		//commands
		unit.getSpec().add(new CommandSpec(
				HashDeclareSpec.INSTANCE,
				TouchDeclareSpec.INSTANCE
		));

		//parameters
		unit.getSpec().add(new ParameterSpec(
				//syntax
				WordSpec.INSTANCE,
				BracketsSpec.INSTANCE,
				QuotesSpec.INSTANCE,
				DoubleQuotesSpec.INSTANCE,
				DigitsSpec.INSTANCE,
				//param
				ReferenceSpec.INSTANCE,
				NumberSpec.INSTANCE,
				ArraySpec.INSTANCE,
				EscapedStringSpec.INSTANCE,
				//compat
				NewlineEscapedSpec.INSTANCE
		));

		//glue
		unit.getSpec().add(TextSpec.INSTANCE);

		//assertions
		unit.getSpec().add(listener(event -> {
			if (event.getAction().equals(Action.POST_EXEC)) {
				Memory memory = event.getMemory();

				String address = memory.get("Address").evaluate(memory);
				String console = memory.getConsole().toString();

				assertEquals(
						expectedAddress,
						address,
						"Unexpected value at the address 'address'"
				);
			}
		}));

		if (
				!unit.initialize(document) ||
				!unit.parse(document) ||
				!unit.analyze(document) ||
				!unit.compile(document)
		) {
			unit.diagnostic();
			fail("Uncompleted test invocation");
		}

		unit.optimize(document, -1);

		if (!unit.execute(document)) {
			unit.diagnostic();
			fail("Uncompleted test invocation");
		}
	}
}
