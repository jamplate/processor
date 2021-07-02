package org.jamplate.spec.parameter.resource;

import org.jamplate.api.Spec;
import org.jamplate.api.Unit;
import org.jamplate.internal.api.UnitImpl;
import org.jamplate.internal.model.PseudoDocument;
import org.jamplate.model.Compilation;
import org.jamplate.model.Document;
import org.jamplate.model.Memory;
import org.jamplate.spec.element.ParameterSpec;
import org.jamplate.spec.parameter.operator.AdderSpec;
import org.jamplate.spec.parameter.operator.NotSpec;
import org.jamplate.spec.parameter.operator.PairSpec;
import org.jamplate.spec.syntax.enclosure.BracesSpec;
import org.jamplate.spec.syntax.enclosure.BracketsSpec;
import org.jamplate.spec.syntax.symbol.ColonSpec;
import org.jamplate.spec.syntax.symbol.CommaSpec;
import org.jamplate.spec.syntax.symbol.ExclamationSpec;
import org.jamplate.spec.syntax.symbol.PlusSpec;
import org.jamplate.spec.syntax.term.DigitsSpec;
import org.jamplate.spec.tool.DebugSpec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ObjectSpecTest {
	@Test
	public void test0() {
		Unit unit = new UnitImpl();
		Document document = new PseudoDocument("{1 + [1, 2, 3]}");
		String expected = "{1[1,2,3]:\"\"}";

		unit.getSpec().add(new ParameterSpec(
				//syntax
				ExclamationSpec.INSTANCE,
				BracesSpec.INSTANCE,
				DigitsSpec.INSTANCE,
				PlusSpec.INSTANCE,
				BracketsSpec.INSTANCE,
				CommaSpec.INSTANCE,
				ColonSpec.INSTANCE,
				//parameters
				ObjectSpec.INSTANCE,
				NumberSpec.INSTANCE,
				ArraySpec.INSTANCE,
				//operators
				NotSpec.INSTANCE,
				AdderSpec.INSTANCE,
				PairSpec.INSTANCE
		));
		unit.getSpec().add(DebugSpec.INSTANCE);
		unit.getSpec().add(new Spec() {
			@Override
			public void onCreateCompilation(@Nullable Unit unit, @NotNull Compilation compilation) {
				compilation.getRootTree().getSketch().setKind(ParameterSpec.KIND);
			}
		});
		unit.getSpec().add(new Spec() {
			@Override
			public void onDestroyMemory(@NotNull Compilation compilation, @NotNull Memory memory) {
				String results = memory.peek().evaluate(memory);

				assertEquals(
						expected,
						results,
						"Unexpected results"
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

	@Test
	public void test1() {
		Unit unit = new UnitImpl();
		//		Document document = new PseudoDocument("{1 + [1,2,3]}");
		Document document = new PseudoDocument("{1, 2, 3, 4}");
		String expected = "{1:\"\",2:\"\",3:\"\",4:\"\"}";

		unit.getSpec().add(new ParameterSpec(
				//syntax
				ExclamationSpec.INSTANCE,
				BracesSpec.INSTANCE,
				DigitsSpec.INSTANCE,
				PlusSpec.INSTANCE,
				BracketsSpec.INSTANCE,
				CommaSpec.INSTANCE,
				ColonSpec.INSTANCE,
				//parameters
				ObjectSpec.INSTANCE,
				NumberSpec.INSTANCE,
				ArraySpec.INSTANCE,
				//operators
				NotSpec.INSTANCE,
				AdderSpec.INSTANCE,
				PairSpec.INSTANCE
		));
		unit.getSpec().add(DebugSpec.INSTANCE);
		unit.getSpec().add(new Spec() {
			@Override
			public void onCreateCompilation(@Nullable Unit unit, @NotNull Compilation compilation) {
				compilation.getRootTree().getSketch().setKind(ParameterSpec.KIND);
			}
		});
		unit.getSpec().add(new Spec() {
			@Override
			public void onDestroyMemory(@NotNull Compilation compilation, @NotNull Memory memory) {
				String results = memory.peek().evaluate(memory);

				assertEquals(
						expected,
						results,
						"Unexpected results"
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

	@Test
	public void test2() {
		Unit unit = new UnitImpl();
		Document document = new PseudoDocument("{1:9, 2:8, 3:7, 4:6, 5:5}");

		unit.getSpec().add(new ParameterSpec(
				//syntax
				ExclamationSpec.INSTANCE,
				BracesSpec.INSTANCE,
				DigitsSpec.INSTANCE,
				PlusSpec.INSTANCE,
				BracketsSpec.INSTANCE,
				CommaSpec.INSTANCE,
				ColonSpec.INSTANCE,
				//parameters
				ObjectSpec.INSTANCE,
				NumberSpec.INSTANCE,
				ArraySpec.INSTANCE,
				//operators
				NotSpec.INSTANCE,
				AdderSpec.INSTANCE,
				PairSpec.INSTANCE
		));
		unit.getSpec().add(DebugSpec.INSTANCE);
		unit.getSpec().add(new Spec() {
			@Override
			public void onCreateCompilation(@Nullable Unit unit, @NotNull Compilation compilation) {
				compilation.getRootTree().getSketch().setKind(ParameterSpec.KIND);
			}
		});
		unit.getSpec().add(new Spec() {
			@Override
			public void onDestroyMemory(@NotNull Compilation compilation, @NotNull Memory memory) {
				String results = memory.peek().evaluate(memory);

				assertEquals(
						"{1:9,2:8,3:7,4:6,5:5}",
						results,
						"Unexpected results"
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
