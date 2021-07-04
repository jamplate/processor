package org.jamplate.glucose.spec.parameter.operator;

import org.jamplate.api.Unit;
import org.jamplate.diagnostic.Diagnostic;
import org.jamplate.diagnostic.Message;
import org.jamplate.glucose.instruction.flow.Idle;
import org.jamplate.glucose.spec.document.LogicSpec;
import org.jamplate.glucose.spec.element.ParameterSpec;
import org.jamplate.glucose.spec.parameter.resource.NumberSpec;
import org.jamplate.glucose.spec.syntax.symbol.AsteriskSpec;
import org.jamplate.glucose.spec.syntax.symbol.MinusSpec;
import org.jamplate.glucose.spec.syntax.term.DigitsSpec;
import org.jamplate.glucose.spec.tool.DebugSpec;
import org.jamplate.impl.api.Action;
import org.jamplate.impl.api.UnitImpl;
import org.jamplate.impl.model.PseudoDocument;
import org.jamplate.memory.Memory;
import org.jamplate.model.Document;
import org.jamplate.model.Reference;
import org.jamplate.model.Sketch;
import org.jamplate.model.Tree;
import org.junit.jupiter.api.Test;

import static org.jamplate.impl.compiler.FallbackCompiler.fallback;
import static org.jamplate.internal.compiler.FlattenCompiler.flatten;
import static org.jamplate.internal.util.Specs.compiler;
import static org.jamplate.internal.util.Specs.listener;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ProductSpecTest {
	@Test
	public void test0() {
		for (int i : new int[]{-2, -1, 0, 1, 2})
			for (int j : new int[]{-2, -1, 0, 1, 2}) {
				Document document = new PseudoDocument(i + "*" + j);
				String expected = String.valueOf(i * j);

				Unit unit = new UnitImpl();

				unit.getSpec().add(DebugSpec.INSTANCE);

				unit.getSpec().add(LogicSpec.INSTANCE);
				unit.getSpec().add(new ParameterSpec(
						//syntax
						DigitsSpec.INSTANCE,
						AsteriskSpec.INSTANCE,
						MinusSpec.INSTANCE,
						//value
						NumberSpec.INSTANCE,
						//operator
						MultiplierSpec.INSTANCE,
						SubtractorSpec.INSTANCE
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
				) {
					unit.diagnostic();
					fail("Uncompleted test invocation");
				}
			}
	}

	@Test
	public void wrap0() {
		//run variables
		Unit unit = new UnitImpl();
		Document document = new PseudoDocument("45 * 6");

		//specs
		unit.getSpec().add(DebugSpec.INSTANCE);
		unit.getSpec().add(LogicSpec.INSTANCE);
		unit.getSpec().add(MultiplierSpec.INSTANCE);
		unit.getSpec().add(NumberSpec.INSTANCE);
		unit.getSpec().add(compiler(
				flatten(
						fallback(),
						(compiler, compilation, tree) -> Idle.INSTANCE
				)
		));
		unit.getSpec().add(listener(event -> {
			//noinspection SwitchStatementDensity
			switch (event.getAction()) {
				case Action.POST_INIT: {
					//pseudo parse
					Tree root = event.getTree();

					root.getSketch().setKind(ParameterSpec.KIND);
					root.offer(new Tree(
							document,
							new Reference(0, 2),
							new Sketch(DigitsSpec.KIND)
					));
					root.offer(new Tree(
							document,
							new Reference(3, 1),
							new Sketch(AsteriskSpec.KIND)
					));
					root.offer(new Tree(
							document,
							new Reference(5, 1),
							new Sketch(DigitsSpec.KIND)
					));
					break;
				}
				case Action.POST_EXEC: {
					Memory memory = event.getMemory();
					String result = memory.pop().evaluate(memory);

					assertEquals(
							"270",
							result,
							"45 * 6 = 270"
					);
					break;
				}
				case Action.DIAGNOSTIC: {
					Diagnostic diagnostic = event.getDiagnostic();

					for (Message message : diagnostic)
						if (message.isFetal())
							if (message.getException() == null)
								throw new RuntimeException(message.getMessagePhrase());
							else
								throw new RuntimeException(message.getException());

					diagnostic.flush(true);
					break;
				}
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
