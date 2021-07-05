package org.jamplate.glucose.spec.element;

import org.jamplate.api.Unit;
import org.jamplate.glucose.spec.GlucoseSpec;
import org.jamplate.glucose.spec.command.hashdeclare.HashDeclareSpec;
import org.jamplate.glucose.spec.command.hashfor.FlowForSpec;
import org.jamplate.glucose.spec.command.hashfor.HashEndforSpec;
import org.jamplate.glucose.spec.command.hashfor.HashForSpec;
import org.jamplate.glucose.spec.document.RootSpec;
import org.jamplate.glucose.spec.document.TextSpec;
import org.jamplate.glucose.spec.misc.NewlineEscapedSpec;
import org.jamplate.glucose.spec.misc.NewlineSpec;
import org.jamplate.glucose.spec.misc.NewlineSuppressedSpec;
import org.jamplate.glucose.spec.parameter.extension.GetterSpec;
import org.jamplate.glucose.spec.parameter.operator.AdderSpec;
import org.jamplate.glucose.spec.parameter.operator.PairSpec;
import org.jamplate.glucose.spec.parameter.resource.*;
import org.jamplate.glucose.spec.syntax.enclosure.BracesSpec;
import org.jamplate.glucose.spec.syntax.enclosure.BracketsSpec;
import org.jamplate.glucose.spec.syntax.enclosure.DoubleQuotesSpec;
import org.jamplate.glucose.spec.syntax.enclosure.QuotesSpec;
import org.jamplate.glucose.spec.syntax.symbol.ColonSpec;
import org.jamplate.glucose.spec.syntax.symbol.CommaSpec;
import org.jamplate.glucose.spec.syntax.symbol.PlusSpec;
import org.jamplate.glucose.spec.syntax.term.DigitsSpec;
import org.jamplate.glucose.spec.syntax.term.WordSpec;
import org.jamplate.glucose.spec.tool.DebugSpec;
import org.jamplate.impl.api.Action;
import org.jamplate.impl.api.UnitImpl;
import org.jamplate.impl.model.PseudoDocument;
import org.jamplate.memory.Memory;
import org.jamplate.memory.Value;
import org.jamplate.model.Document;
import org.junit.jupiter.api.Test;

import static org.jamplate.internal.util.Specs.listener;
import static org.junit.jupiter.api.Assertions.*;

public class InjectionSpecTest {
	@Test
	public void collision() {
		Document document = new PseudoDocument(
				//name
				"Test",
				//content
				"#{\"'\"'\"'}#'m'#{\"'\"'\"'}#"
		);
		String expectedConsole = "'\"'m''\"";

		Unit unit = new UnitImpl(new GlucoseSpec(
				DebugSpec.INSTANCE,
				listener(event -> {
					if (event.getAction().equals(Action.POST_EXEC)) {
						Memory memory = event.getMemory();

						String actualConsole = memory.getConsole().read();

						assertEquals(
								expectedConsole,
								actualConsole,
								"Unexpected output printed to the console"
						);
					}
				})
		));

		if (
				!unit.initialize(document) ||
				!unit.parse(document) ||
				!unit.analyze(document) ||
				!unit.compile(document) ||
				!unit.execute(document)
		) {
			unit.diagnostic();
			fail("Uncompleted test");
		}
	}

	@Test
	public void test0() {
		Document document = new PseudoDocument(
				/*01*/ "The first line\\\n" +
				/*02*/ "The second line\n" +
				/*03*/ "\n" +
				/*04*/ "#for i [1, 2, \\\n" +
				/*05*/ "3, 4, 5]\n" +
				/*06*/
					   "#{i + ['a', 'b'[0], {a:b,c:2,d:4}, '\\n' + 'd', 'e', 'f' + \"\\n\"][i] \"\\n\"}#\n" +
				/*07*/ "#endfor\n" +
				/*08*/ "\n" +
				/*09*/ "final line"
				/*10*/
				/*11*/
		);
		String expectedConsole =
				"The first lineThe second line\n" +
				"1\n" +
				"2{\"a\":\"b\",\"c\":2,\"d\":4}\n" +
				"3\\nd\n" +
				"4e\n" +
				"5f\n\n" +
				"\n" +
				"final line";
		String terminationLine = "9";

		Unit unit = new UnitImpl();

		unit.getSpec().add(RootSpec.INSTANCE);
		unit.getSpec().add(DebugSpec.INSTANCE);
		unit.getSpec().add(NewlineSpec.INSTANCE);
		unit.getSpec().add(NewlineEscapedSpec.INSTANCE);
		unit.getSpec().add(NewlineSuppressedSpec.INSTANCE);

		unit.getSpec().add(new FlowSpec(
				FlowForSpec.INSTANCE
		));
		unit.getSpec().add(InjectionSpec.INSTANCE);
		unit.getSpec().add(new CommandSpec(
				HashForSpec.INSTANCE,
				HashEndforSpec.INSTANCE
		));
		unit.getSpec().add(new ParameterSpec(
				//syntax
				WordSpec.INSTANCE,
				DigitsSpec.INSTANCE,
				BracketsSpec.INSTANCE,
				BracesSpec.INSTANCE,
				CommaSpec.INSTANCE,
				ColonSpec.INSTANCE,
				PlusSpec.INSTANCE,
				QuotesSpec.INSTANCE,
				DoubleQuotesSpec.INSTANCE,
				//param
				ArraySpec.INSTANCE,
				ObjectSpec.INSTANCE,
				ReferenceSpec.INSTANCE,
				NumberSpec.INSTANCE,
				EscapedStringSpec.INSTANCE,
				StringSpec.INSTANCE,
				//operator
				AdderSpec.INSTANCE,
				PairSpec.INSTANCE,
				//extension
				GetterSpec.INSTANCE,
				//compat
				NewlineSpec.INSTANCE,
				NewlineEscapedSpec.INSTANCE
		));

		unit.getSpec().add(TextSpec.INSTANCE);

		unit.getSpec().add(listener(event -> {
			if (event.getAction().equals(Action.POST_EXEC)) {
				Memory memory = event.getMemory();
				Value i = memory.get("i");
				Value line = memory.get("__LINE__");
				String console = memory.getConsole().read();

				assertEquals(
						expectedConsole,
						console,
						"Unexpected console output"
				);
				assertSame(
						Value.NULL,
						i,
						"loop variable has not been reset"
				);
				assertEquals(
						terminationLine,
						line.evaluate(memory),
						"Termination line"
				);
			}
		}));

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
	}

	@Test
	public void test1() {
		Document document = new PseudoDocument(
				"#declare x [123, 123, 123, 'HI']\n" +
				"#{ x[3] }#"
		);
		String expectedConsole = "HI";

		Unit unit = new UnitImpl();

		unit.getSpec().add(RootSpec.INSTANCE);
		unit.getSpec().add(DebugSpec.INSTANCE);
		unit.getSpec().add(InjectionSpec.INSTANCE);
		unit.getSpec().add(new CommandSpec(
				HashDeclareSpec.INSTANCE
		));
		unit.getSpec().add(NewlineSpec.INSTANCE);
		unit.getSpec().add(NewlineSuppressedSpec.INSTANCE);
		unit.getSpec().add(new ParameterSpec(
				//syntax
				DigitsSpec.INSTANCE,
				CommaSpec.INSTANCE,
				BracketsSpec.INSTANCE,
				QuotesSpec.INSTANCE,
				DoubleQuotesSpec.INSTANCE,
				WordSpec.INSTANCE,
				//param
				NumberSpec.INSTANCE,
				ArraySpec.INSTANCE,
				EscapedStringSpec.INSTANCE,
				StringSpec.INSTANCE,
				ReferenceSpec.INSTANCE,
				//operator
				GetterSpec.INSTANCE
		));
		unit.getSpec().add(TextSpec.INSTANCE);
		unit.getSpec().add(listener(event -> {
			if (event.getAction().equals(Action.POST_EXEC)) {
				Memory memory = event.getMemory();
				String console = memory.getConsole().read();

				assertEquals(
						expectedConsole,
						console,
						"Unexpected console output"
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

		if (
				!unit.execute(document)
		) {
			unit.diagnostic();
			fail("Uncompleted test invocation");
		}
	}
}
