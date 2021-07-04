package org.jamplate.glucose.spec.parameter.operator;

import org.jamplate.api.Unit;
import org.jamplate.glucose.instruction.flow.Idle;
import org.jamplate.glucose.spec.document.LogicSpec;
import org.jamplate.glucose.spec.element.ParameterSpec;
import org.jamplate.glucose.spec.parameter.resource.ReferenceSpec;
import org.jamplate.glucose.spec.syntax.symbol.ExclamationSpec;
import org.jamplate.glucose.spec.syntax.symbol.PlusSpec;
import org.jamplate.glucose.spec.syntax.term.WordSpec;
import org.jamplate.glucose.spec.tool.DebugSpec;
import org.jamplate.impl.api.Action;
import org.jamplate.impl.api.UnitImpl;
import org.jamplate.impl.model.PseudoDocument;
import org.jamplate.internal.util.Query;
import org.jamplate.memory.Memory;
import org.jamplate.model.Document;
import org.junit.jupiter.api.Test;

import static org.jamplate.impl.compiler.FallbackCompiler.fallback;
import static org.jamplate.impl.compiler.FilterCompiler.filter;
import static org.jamplate.internal.compiler.FlattenCompiler.flatten;
import static org.jamplate.internal.compiler.MandatoryCompiler.mandatory;
import static org.jamplate.internal.util.Functions.compiler;
import static org.jamplate.internal.util.Specs.compiler;
import static org.jamplate.internal.util.Specs.listener;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class NotSpecTest {
	@Test
	public void test0() {
		for (boolean i : new boolean[]{false, true}) {
			Document document = new PseudoDocument("!!!" + i);
			//noinspection DoubleNegation
			String expected = String.valueOf(!!!i);

			Unit unit = new UnitImpl();

			unit.getSpec().add(DebugSpec.INSTANCE);

			unit.getSpec().add(LogicSpec.INSTANCE);
			unit.getSpec().add(new ParameterSpec(
					//syntax
					WordSpec.INSTANCE,
					ExclamationSpec.INSTANCE,
					//value
					ReferenceSpec.INSTANCE,
					//operator
					NotSpec.INSTANCE
			));
			unit.getSpec().add(listener(event -> {
				if (event.getAction().equals(Action.POST_EXEC)) {
					Memory memory = event.getMemory();
					String actual = memory.peek().evaluate(memory);

					assertEquals(
							expected,
							actual,
							"Unexpected result"
					);
				}
			}));

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

	@Test
	public void wrap0() {
		//run variables
		Unit unit = new UnitImpl();
		Document document = new PseudoDocument("!!!false + !!!true");

		//specs
		unit.getSpec().add(LogicSpec.INSTANCE);
		unit.getSpec().add(PlusSpec.INSTANCE);
		unit.getSpec().add(ExclamationSpec.INSTANCE);
		unit.getSpec().add(WordSpec.INSTANCE);
		unit.getSpec().add(AdderSpec.INSTANCE);
		unit.getSpec().add(NotSpec.INSTANCE);
		unit.getSpec().add(ReferenceSpec.INSTANCE);
		unit.getSpec().add(DebugSpec.INSTANCE);
		unit.getSpec().add(compiler(
				flatten(
						mandatory(fallback()),
						mandatory(compiler(
								c -> filter(c, Query.whitespace()),
								c -> (compiler, compilation, tree) -> Idle.INSTANCE
						))
				)
		));
		unit.getSpec().add(listener(event -> {
			if (event.getAction().equals(Action.POST_EXEC)) {
				Memory memory = event.getMemory();
				String result = memory.pop().evaluate(memory);

				//noinspection SpellCheckingInspection
				assertEquals(
						"truefalse",
						result,
						"!!!false + !!!true = truefalse"
				);
			}
		}));

		//run
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
