package org.jamplate.spec.operator;

import org.jamplate.api.Spec;
import org.jamplate.api.Unit;
import org.jamplate.function.Compiler;
import org.jamplate.internal.api.UnitImpl;
import org.jamplate.internal.function.compiler.router.FlattenCompiler;
import org.jamplate.internal.function.compiler.concrete.ToIdleCompiler;
import org.jamplate.internal.function.compiler.router.FallbackCompiler;
import org.jamplate.internal.function.compiler.filter.FilterWhitespaceCompiler;
import org.jamplate.internal.function.compiler.mode.MandatoryCompiler;
import org.jamplate.internal.model.PseudoDocument;
import org.jamplate.model.Compilation;
import org.jamplate.model.Document;
import org.jamplate.model.Memory;
import org.jamplate.spec.document.LogicSpec;
import org.jamplate.spec.element.ParameterSpec;
import org.jamplate.spec.tool.DebugSpec;
import org.jamplate.spec.parameter.ReferenceSpec;
import org.jamplate.spec.syntax.symbol.ExclamationSpec;
import org.jamplate.spec.syntax.symbol.PlusSpec;
import org.jamplate.spec.syntax.term.WordSpec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

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
		Document document = new PseudoDocument("!!!false + !!!true");

		//specs
		unit.getSpec().add(PlusSpec.INSTANCE);
		unit.getSpec().add(ExclamationSpec.INSTANCE);
		unit.getSpec().add(WordSpec.INSTANCE);
		unit.getSpec().add(AdderSpec.INSTANCE);
		unit.getSpec().add(NotSpec.INSTANCE);
		unit.getSpec().add(ReferenceSpec.INSTANCE);
		unit.getSpec().add(DebugSpec.INSTANCE);
		unit.getSpec().add(new Spec() {
			@NotNull
			@Override
			public Compiler getCompiler() {
				return new FlattenCompiler(
						new MandatoryCompiler(FallbackCompiler.INSTANCE),
						new MandatoryCompiler(new FilterWhitespaceCompiler(ToIdleCompiler.INSTANCE))
				);
			}

			@Override
			public void onCreateCompilation(@Nullable Unit unit, @NotNull Compilation compilation) {
				compilation.getRootTree().getSketch().setKind(ParameterSpec.KIND);
			}

			@Override
			public void onDestroyMemory(@NotNull Compilation compilation, @NotNull Memory memory) {
				String result = memory.pop().evaluate(memory);

				//noinspection SpellCheckingInspection
				assertEquals(
						"truefalse",
						result,
						"!!!false + !!!true = truefalse"
				);
			}
		});

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
