package org.jamplate.glucose.spec.command.hashfor;

import org.jamplate.unit.Unit;
import org.jamplate.glucose.spec.command.hashdeclare.HashDeclareSpec;
import org.jamplate.glucose.spec.command.hasherror.HashErrorSpec;
import org.jamplate.glucose.spec.command.hashmessage.HashMessageSpec;
import org.jamplate.glucose.spec.document.RootSpec;
import org.jamplate.glucose.spec.document.TextSpec;
import org.jamplate.glucose.spec.element.CommandSpec;
import org.jamplate.glucose.spec.element.FlowSpec;
import org.jamplate.glucose.spec.element.ParameterSpec;
import org.jamplate.glucose.spec.misc.NewlineEscapedSpec;
import org.jamplate.glucose.spec.misc.NewlineSpec;
import org.jamplate.glucose.spec.misc.NewlineSuppressedSpec;
import org.jamplate.glucose.spec.parameter.resource.ArraySpec;
import org.jamplate.glucose.spec.parameter.resource.NumberSpec;
import org.jamplate.glucose.spec.parameter.resource.ReferenceSpec;
import org.jamplate.glucose.spec.syntax.enclosure.BracketsSpec;
import org.jamplate.glucose.spec.syntax.symbol.CommaSpec;
import org.jamplate.glucose.spec.syntax.term.DigitsSpec;
import org.jamplate.glucose.spec.syntax.term.WordSpec;
import org.jamplate.glucose.spec.tool.DebugSpec;
import org.jamplate.impl.unit.UnitImpl;
import org.jamplate.impl.document.PseudoDocument;
import org.jamplate.model.Document;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

public class FlowForSpecTest {
	@Test
	public void test0() {
		Document document = new PseudoDocument(
				/*01*/ "The first line\n" +
				/*02*/ "The second line\n" +
				/*03*/ "\n" +
				/*04*/ "#for i [1, 2, \\\n" +
				/*05*/ "3, 4]\n" +
				/*06*/ "#error i\n" +
				/*07*/ "#endfor\n" +
				/*08*/ "\n" +
				/*09*/ "final line"
				/*10*/
				/*11*/
		);

		Unit unit = new UnitImpl();

		unit.getSpec().add(RootSpec.INSTANCE);
		unit.getSpec().add(DebugSpec.INSTANCE);
		unit.getSpec().add(NewlineSpec.INSTANCE);
		unit.getSpec().add(NewlineEscapedSpec.INSTANCE);
		unit.getSpec().add(NewlineSuppressedSpec.INSTANCE);

		unit.getSpec().add(new FlowSpec(
				FlowForSpec.INSTANCE
		));
		unit.getSpec().add(new CommandSpec(
				HashForSpec.INSTANCE,
				HashEndforSpec.INSTANCE,
				HashDeclareSpec.INSTANCE,
				HashMessageSpec.INSTANCE,
				HashErrorSpec.INSTANCE
		));
		unit.getSpec().add(new ParameterSpec(
				//syntax
				WordSpec.INSTANCE,
				DigitsSpec.INSTANCE,
				BracketsSpec.INSTANCE,
				CommaSpec.INSTANCE,
				//param
				ReferenceSpec.INSTANCE,
				NumberSpec.INSTANCE,
				ArraySpec.INSTANCE,
				//compat
				NewlineEscapedSpec.INSTANCE
		));

		unit.getSpec().add(TextSpec.INSTANCE);

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
}
