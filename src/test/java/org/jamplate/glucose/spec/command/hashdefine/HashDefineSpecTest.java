package org.jamplate.glucose.spec.command.hashdefine;

import org.jamplate.api.Unit;
import org.jamplate.glucose.spec.document.RootSpec;
import org.jamplate.glucose.spec.document.TextSpec;
import org.jamplate.glucose.spec.element.CommandSpec;
import org.jamplate.glucose.spec.element.ParameterSpec;
import org.jamplate.glucose.spec.parameter.resource.ReferenceSpec;
import org.jamplate.glucose.spec.syntax.term.WordSpec;
import org.jamplate.glucose.spec.tool.DebugSpec;
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

public class HashDefineSpecTest {
	@Test
	public void test0() {
		Document document = new PseudoDocument(
				//name
				"Test",
				//content
				"#define __PATH__ Main"
		);
		String expectedPath = "Main";
		String expectedDefine = "{\"__PATH__\":\"Main\"}";

		Unit unit = new UnitImpl(new MultiSpec(
				//document
				RootSpec.INSTANCE,
				//tool
				DebugSpec.INSTANCE,
				//command
				new CommandSpec(
						HashDefineSpec.INSTANCE
				),
				//parameter
				new ParameterSpec(
						//syntax
						WordSpec.INSTANCE,
						//resource
						ReferenceSpec.INSTANCE
				),
				//glue
				TextSpec.INSTANCE,
				//listener
				listener(event -> {
					if (event.getAction().equals(Action.POST_EXEC)) {
						Memory memory = event.getMemory();
						String actualPath = memory.get("__PATH__").eval(memory);
						String actualDefine = memory.get("__DEFINE__").eval(memory);

						assertEquals(
								expectedPath,
								actualPath,
								"__PATH__ not updated"
						);
						assertEquals(
								expectedDefine,
								actualDefine,
								"__DEFINE__[__PATH__] not updated"
						);
					}
				})
		));

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
