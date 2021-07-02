package org.jamplate.spec.parameter;

import org.jamplate.api.Spec;
import org.jamplate.api.Unit;
import org.jamplate.internal.api.UnitImpl;
import org.jamplate.internal.model.PseudoDocument;
import org.jamplate.model.Compilation;
import org.jamplate.model.Document;
import org.jamplate.model.Memory;
import org.jamplate.spec.document.LogicSpec;
import org.jamplate.spec.element.ParameterSpec;
import org.jamplate.spec.tool.DebugSpec;
import org.jamplate.spec.operator.AdderSpec;
import org.jamplate.spec.operator.MultiplierSpec;
import org.jamplate.spec.syntax.enclosure.ParenthesesSpec;
import org.jamplate.spec.syntax.symbol.AsteriskSpec;
import org.jamplate.spec.syntax.symbol.PlusSpec;
import org.jamplate.spec.syntax.term.DigitsSpec;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GroupSpecTest {
	@Test
	public void test0() {
		Document document = new PseudoDocument("1 + 2 * (3 + 5)");
		String expected = String.valueOf(1 + 2 * (3 + 5));

		Unit unit = new UnitImpl();

		unit.getSpec().add(DebugSpec.INSTANCE);
		unit.getSpec().add(LogicSpec.INSTANCE);
		unit.getSpec().add(new ParameterSpec(
				//syntax
				ParenthesesSpec.INSTANCE,
				PlusSpec.INSTANCE,
				AsteriskSpec.INSTANCE,
				DigitsSpec.INSTANCE,
				//values
				GroupSpec.INSTANCE,
				NumberSpec.INSTANCE,
				AdderSpec.INSTANCE,
				MultiplierSpec.INSTANCE
		));
		unit.getSpec().add(new Spec() {
			@Override
			public void onDestroyMemory(@NotNull Compilation compilation, @NotNull Memory memory) {
				String actual = memory.peek().evaluate(memory);

				assertEquals(
						expected,
						actual,
						"Unexpected results"
				);
			}
		});

		if (
				!unit.initialize(document) ||
				!unit.parse(document) ||
				!unit.analyze(document) ||
				!unit.compile(document) ||
				!unit.execute(document)
		)
			unit.diagnostic();
	}
}
