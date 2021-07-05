package org.jamplate.glucose.spec.command.hashcapture;

import org.jamplate.api.Unit;
import org.jamplate.glucose.spec.document.RootSpec;
import org.jamplate.glucose.spec.document.TextSpec;
import org.jamplate.glucose.spec.element.CommandSpec;
import org.jamplate.glucose.spec.element.FlowSpec;
import org.jamplate.glucose.spec.element.ParameterSpec;
import org.jamplate.glucose.spec.misc.NewlineSpec;
import org.jamplate.glucose.spec.misc.NewlineSuppressedSpec;
import org.jamplate.glucose.spec.parameter.resource.ReferenceSpec;
import org.jamplate.glucose.spec.syntax.term.WordSpec;
import org.jamplate.glucose.spec.tool.DebugSpec;
import org.jamplate.glucose.spec.tool.OptimizeSpec;
import org.jamplate.impl.api.Action;
import org.jamplate.impl.api.MultiSpec;
import org.jamplate.impl.api.UnitImpl;
import org.jamplate.impl.model.PseudoDocument;
import org.jamplate.memory.Memory;
import org.jamplate.model.Document;
import org.junit.jupiter.api.Test;

import static org.jamplate.internal.util.Specs.listener;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class FlowCaptureSpecTest {
	@Test
	public void test() {
		Document document = new PseudoDocument(
				//name
				"Test",
				//content
				"The first line",
				"#capture X",
				"The third line",
				"#endcapture",
				"The fourth line"
		);
		String expectedConsole = "The first lineThe fourth line";
		String expectedX = "The third line";

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
				//flow control
				new FlowSpec(
						FlowCaptureSpec.INSTANCE
				),
				//commands
				new CommandSpec(
						HashCaptureSpec.INSTANCE,
						HashEndcaptureSpec.INSTANCE
				),
				//parameters
				new ParameterSpec(
						//syntax
						WordSpec.INSTANCE,
						//resource
						ReferenceSpec.INSTANCE
				),
				//glue
				TextSpec.INSTANCE
		));

		unit.getSpec().add(listener(event -> {
			if (event.getAction().equals(Action.POST_EXEC)) {
				Memory memory = event.getMemory();
				String actualConsole = memory.getConsole().read();
				String actualX = memory.get("X").evaluate(memory);

				assertEquals(
						expectedConsole,
						actualConsole,
						"Unexpected text printed to the console"
				);
				assertEquals(
						expectedX,
						actualX,
						"Unexpected value printed to X"
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

		if (!unit.execute(document)) {
			unit.diagnostic();
			fail("Uncompleted test invocation");
		}
	}
}
