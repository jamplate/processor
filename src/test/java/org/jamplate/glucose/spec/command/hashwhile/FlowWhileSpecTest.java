package org.jamplate.glucose.spec.command.hashwhile;

import org.jamplate.unit.Unit;
import org.jamplate.glucose.spec.command.hashdeclare.HashDeclareSpec;
import org.jamplate.glucose.spec.document.RootSpec;
import org.jamplate.glucose.spec.document.TextSpec;
import org.jamplate.glucose.spec.element.CommandSpec;
import org.jamplate.glucose.spec.element.FlowSpec;
import org.jamplate.glucose.spec.element.InjectionSpec;
import org.jamplate.glucose.spec.element.ParameterSpec;
import org.jamplate.glucose.spec.misc.NewlineSpec;
import org.jamplate.glucose.spec.misc.NewlineSuppressedSpec;
import org.jamplate.glucose.spec.parameter.operator.AdderSpec;
import org.jamplate.glucose.spec.parameter.operator.LessThanSpec;
import org.jamplate.glucose.spec.parameter.resource.EscapedStringSpec;
import org.jamplate.glucose.spec.parameter.resource.NumberSpec;
import org.jamplate.glucose.spec.parameter.resource.ReferenceSpec;
import org.jamplate.glucose.spec.syntax.enclosure.QuotesSpec;
import org.jamplate.glucose.spec.syntax.symbol.OpenChevronSpec;
import org.jamplate.glucose.spec.syntax.symbol.PlusSpec;
import org.jamplate.glucose.spec.syntax.term.DigitsSpec;
import org.jamplate.glucose.spec.syntax.term.WordSpec;
import org.jamplate.glucose.spec.tool.DebugSpec;
import org.jamplate.glucose.spec.tool.OptimizeSpec;
import org.jamplate.impl.spec.MultiSpec;
import org.jamplate.impl.unit.UnitImpl;
import org.jamplate.impl.document.PseudoDocument;
import org.jamplate.model.Document;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

public class FlowWhileSpecTest {
	@Test
	public void test0() {
		Document document = new PseudoDocument(
				//name
				"Test",
				//content
				"#{'zero'}#",
				"#declare i 1",
				"#while i < 10",
				"#{i}#",
				"#declare i i + 1",
				"#endwhile",
				"#{'ten'}#"
		);
		String expectedConsole = "zero123456789ten";

		Unit unit = new UnitImpl(new MultiSpec(
				//name
				"MainSpec",
				//tools
				DebugSpec.INSTANCE,
				OptimizeSpec.INSTANCE,
				//document
				RootSpec.INSTANCE,
				//misc
				NewlineSpec.INSTANCE,
				NewlineSuppressedSpec.INSTANCE,
				//injection
				InjectionSpec.INSTANCE,
				//flow
				new FlowSpec(
						FlowWhileSpec.INSTANCE
				),
				//commands
				new CommandSpec(
						HashDeclareSpec.INSTANCE,
						HashWhileSpec.INSTANCE,
						HashEndwhileSpec.INSTANCE
				),
				//parameters
				new ParameterSpec(
						//syntax
						WordSpec.INSTANCE,
						DigitsSpec.INSTANCE,
						QuotesSpec.INSTANCE,
						OpenChevronSpec.INSTANCE,
						PlusSpec.INSTANCE,
						//resources
						ReferenceSpec.INSTANCE,
						NumberSpec.INSTANCE,
						EscapedStringSpec.INSTANCE,
						//operator
						AdderSpec.INSTANCE,
						LessThanSpec.INSTANCE
				),
				//glue
				TextSpec.INSTANCE
		));

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
