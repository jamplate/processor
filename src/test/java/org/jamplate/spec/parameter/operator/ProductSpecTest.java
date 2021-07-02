package org.jamplate.spec.parameter.operator;

import org.jamplate.api.Spec;
import org.jamplate.api.Unit;
import org.jamplate.diagnostic.Message;
import org.jamplate.function.Compiler;
import org.jamplate.internal.api.UnitImpl;
import org.jamplate.internal.function.compiler.router.FlattenCompiler;
import org.jamplate.internal.function.compiler.concrete.ToIdleCompiler;
import org.jamplate.internal.function.compiler.router.FallbackCompiler;
import org.jamplate.internal.model.PseudoDocument;
import org.jamplate.model.*;
import org.jamplate.spec.document.LogicSpec;
import org.jamplate.spec.element.ParameterSpec;
import org.jamplate.spec.tool.DebugSpec;
import org.jamplate.spec.parameter.resource.NumberSpec;
import org.jamplate.spec.syntax.symbol.AsteriskSpec;
import org.jamplate.spec.syntax.symbol.MinusSpec;
import org.jamplate.spec.syntax.term.DigitsSpec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
				unit.getSpec().add(new Spec() {
					@Override
					public void onDestroyMemory(@NotNull Compilation compilation, @NotNull Memory memory) {
						String actual = memory.peek().evaluate(memory);

						assertEquals(
								expected,
								actual,
								"Unexpected result"
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

	@Test
	public void wrap0() {
		//run variables
		Unit unit = new UnitImpl();
		Document document = new PseudoDocument("45 * 6");

		//specs
		unit.getSpec().add(MultiplierSpec.INSTANCE);
		unit.getSpec().add(NumberSpec.INSTANCE);
		unit.getSpec().add(DebugSpec.INSTANCE);
		unit.getSpec().add(new Spec() {
			@NotNull
			@Override
			public Compiler getCompiler() {
				return new FlattenCompiler(
						FallbackCompiler.INSTANCE,
						ToIdleCompiler.INSTANCE
				);
			}

			@Override
			public void onCreateCompilation(@Nullable Unit unit, @NotNull Compilation compilation) {
				//pseudo parse
				Tree root = compilation.getRootTree();
				Document document = root.document();

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
			}

			@Override
			public void onDestroyMemory(@NotNull Compilation compilation, @NotNull Memory memory) {
				String result = memory.pop().evaluate(memory);

				assertEquals(
						"270",
						result,
						"45 * 6 = 270"
				);
			}

			@Override
			public void onDiagnostic(@NotNull Environment environment, @NotNull Message message) {
				environment.getDiagnostic().flush(true);

				if (message.isFetal())
					if (message.getException() == null)
						throw new RuntimeException(message.getMessagePhrase());
					else
						throw new RuntimeException(message.getException());
			}
		});

		//run
		unit.initialize(document);
		unit.parse(document);
		unit.analyze(document);
		unit.compile(document);
		unit.execute(document);
		unit.diagnostic();
	}
}
