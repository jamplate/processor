package org.jamplate.glucose.spec.parameter.operator;

import org.jamplate.unit.Unit;
import org.jamplate.glucose.spec.document.LogicSpec;
import org.jamplate.glucose.spec.element.ParameterSpec;
import org.jamplate.glucose.spec.parameter.resource.ReferenceSpec;
import org.jamplate.glucose.spec.syntax.symbol.ExclamationSpec;
import org.jamplate.glucose.spec.syntax.symbol.PlusSpec;
import org.jamplate.glucose.spec.syntax.term.WordSpec;
import org.jamplate.glucose.spec.tool.DebugSpec;
import org.jamplate.impl.unit.Action;
import org.jamplate.impl.spec.MultiSpec;
import org.jamplate.impl.unit.UnitImpl;
import org.jamplate.impl.instruction.Idle;
import org.jamplate.impl.document.PseudoDocument;
import org.jamplate.util.Query;
import org.jamplate.memory.Memory;
import org.jamplate.model.Document;
import org.jamplate.model.Tree;
import org.junit.jupiter.api.Test;

import static org.jamplate.impl.compiler.FallbackCompiler.fallback;
import static org.jamplate.impl.compiler.FilterCompiler.filter;
import static org.jamplate.glucose.internal.compiler.FlattenCompiler.flatten;
import static org.jamplate.glucose.internal.compiler.MandatoryCompiler.mandatory;
import static org.jamplate.util.Functions.compiler;
import static org.jamplate.util.Specs.compiler;
import static org.jamplate.util.Specs.listener;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class NotSpecTest {
	@Test
	public void singleton() {
		Document document = new PseudoDocument(
				//name
				"Test",
				//content
				"!"
		);

		Unit unit = new UnitImpl(new MultiSpec(
				//name
				"MainSpec",
				//document
				LogicSpec.INSTANCE,
				//parameters
				new ParameterSpec(
						//syntax
						ExclamationSpec.INSTANCE,
						//operators
						NotSpec.INSTANCE
				),
				//listeners
				listener(event -> {
					if (event.getAction().equals(Action.POST_ANALYZE)) {
						Tree parameter = event.getTree();
						Tree not = parameter.getChild();
						Tree exclamation = not.getChild();

						assertEquals(
								ParameterSpec.KIND,
								parameter.getSketch().getKind(),
								"The root is not a parameter"
						);
						assertEquals(
								NotSpec.KIND,
								not.getSketch().getKind(),
								"Not operator not applied"
						);
						assertEquals(
								ExclamationSpec.KIND,
								exclamation.getSketch().getKind(),
								"Exclamation not detected"
						);
					}
				})
		));

		if (
				!unit.initialize(document) ||
				!unit.parse(document) ||
				!unit.analyze(document)
		) {
			unit.diagnostic();
			fail("Test not completed");
		}
	}

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
					String actual = memory.peek().eval(memory);

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
				String result = memory.pop().eval(memory);

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
