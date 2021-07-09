package org.jamplate.glucose.spec.command.hashif;

import org.jamplate.api.Unit;
import org.jamplate.glucose.spec.GlucoseSpec;
import org.jamplate.glucose.spec.document.RootSpec;
import org.jamplate.glucose.spec.document.TextSpec;
import org.jamplate.glucose.spec.element.CommandSpec;
import org.jamplate.glucose.spec.element.FlowSpec;
import org.jamplate.glucose.spec.element.ParameterSpec;
import org.jamplate.glucose.spec.misc.NewlineEscapedSpec;
import org.jamplate.glucose.spec.misc.NewlineSpec;
import org.jamplate.glucose.spec.misc.NewlineSuppressedSpec;
import org.jamplate.glucose.spec.parameter.resource.ReferenceSpec;
import org.jamplate.glucose.spec.syntax.term.WordSpec;
import org.jamplate.glucose.spec.tool.DebugSpec;
import org.jamplate.impl.api.Action;
import org.jamplate.impl.api.MultiSpec;
import org.jamplate.impl.api.UnitImpl;
import org.jamplate.impl.model.PseudoDocument;
import org.jamplate.model.Document;
import org.jamplate.model.Tree;
import org.junit.jupiter.api.Test;

import static org.jamplate.internal.util.Specs.listener;
import static org.junit.jupiter.api.Assertions.*;

public class FlowIfSpecTest {
	@Test
	public void analytic0() {
		Document document = new PseudoDocument(
				"Test",
				"#if x",
				"#elif x",
				"#else",
				"#elif x",
				"#endif"
		);

		Unit unit = new UnitImpl(new MultiSpec(
				//document
				RootSpec.INSTANCE,
				//tool
				DebugSpec.INSTANCE,
				//misc
				NewlineSpec.INSTANCE,
				NewlineEscapedSpec.INSTANCE,
				NewlineSuppressedSpec.INSTANCE,
				//flow
				new FlowSpec(
						FlowIfSpec.INSTANCE
				),
				//command
				new CommandSpec(
						HashIfSpec.INSTANCE,
						HashIfdefSpec.INSTANCE,
						HashIfndefSpec.INSTANCE,
						HashElifSpec.INSTANCE,
						HashElifdefSpec.INSTANCE,
						HashElifndefSpec.INSTANCE,
						HashElseSpec.INSTANCE,
						HashEndifSpec.INSTANCE
				),
				//parameter
				new ParameterSpec(
						//symbol
						WordSpec.INSTANCE,
						//resource
						ReferenceSpec.INSTANCE
				),
				//glue
				TextSpec.INSTANCE,
				//listener
				listener(event -> {
					if (event.getAction().equals(Action.POST_ANALYZE)) {
						Tree root = event.getTree();

						//first level
						Tree flowT1 = root.getChild();
						Tree ifT1 = flowT1.getChild();
						Tree bodyT1 = ifT1.getNext();
						Tree endifT1 = bodyT1.getNext().getNext();

						assertEquals(
								FlowIfSpec.KIND,
								flowT1.getSketch().getKind(),
								"Bad analytic"
						);
						assertEquals(
								HashIfSpec.KIND,
								ifT1.getSketch().getKind(),
								"Bad analytic"
						);
						assertEquals(
								HashEndifSpec.KIND,
								endifT1.getSketch().getKind(),
								"Bad analytic"
						);

						//second level
						Tree flow2 = bodyT1.getNext();
						Tree elifT2 = flow2.getChild();
						Tree bodyT2 = elifT2.getNext();

						assertEquals(
								FlowIfSpec.KIND,
								flow2.getSketch().getKind(),
								"Bad analytic"
						);
						assertEquals(
								HashElifSpec.KIND,
								elifT2.getSketch().getKind(),
								"Bad analytic"
						);

						//third level
						Tree flowT3 = bodyT2.getNext();
						Tree elseT3 = flowT3.getChild();
						Tree bodyT3 = elseT3.getNext();
						Tree newline = bodyT3.getChild();
						Tree elifInBodyT3 = newline.getNext();

						assertEquals(
								FlowIfSpec.KIND,
								flowT3.getSketch().getKind(),
								"Bad analytic"
						);
						assertEquals(
								HashElseSpec.KIND,
								elseT3.getSketch().getKind(),
								"Bad analytic"
						);
						assertEquals(
								HashElifSpec.KIND,
								elifInBodyT3.getSketch().getKind(),
								"Bad analytic"
						);

						//relations (level 1)
						assertSame(
								ifT1,
								flowT1.getSketch().get(FlowSpec.KEY_START).getTree(),
								"Inconsistent analytic"
						);
						assertSame(
								elifT2,
								flowT1.getSketch().get(FlowSpec.KEY_MIDDLE).getTree(),
								"Inconsistent analytic"
						);
						assertSame(
								bodyT1,
								flowT1.getSketch().get(FlowSpec.KEY_BODY).getTree(),
								"Inconsistent analytic"
						);
						assertSame(
								flow2,
								flowT1.getSketch().get(FlowSpec.KEY_SUB).getTree(),
								"Inconsistent analytic"
						);
						assertSame(
								endifT1,
								flowT1.getSketch().get(FlowSpec.KEY_END).getTree(),
								"Inconsistent analytic"
						);

						//relations (level 2)
						assertSame(
								elifT2,
								flow2.getSketch().get(FlowSpec.KEY_START).getTree(),
								"Inconsistent analytic"
						);
						assertSame(
								elseT3,
								flow2.getSketch().get(FlowSpec.KEY_MIDDLE).getTree(),
								"Inconsistent analytic"
						);
						assertSame(
								bodyT2,
								flow2.getSketch().get(FlowSpec.KEY_BODY).getTree(),
								"Inconsistent analytic"
						);
						assertSame(
								flowT3,
								flow2.getSketch()
									 .get(FlowSpec.KEY_SUB)
									 .getTree(),
								"Inconsistent analytic"
						);

						//relations (level 3)
						assertSame(
								elseT3,
								flowT3.getSketch().get(FlowSpec.KEY_START).getTree(),
								"Inconsistent analytic"
						);
						assertSame(
								bodyT3,
								flowT3.getSketch().get(FlowSpec.KEY_BODY).getTree(),
								"Inconsistent analytic"
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
			fail("Uncompleted test invocation");
		}
	}

	@Test
	public void outOfIfFlow() {
		Document document = new PseudoDocument(
				"Test",
				"#elif true"
		);

		Unit unit = new UnitImpl(new GlucoseSpec());

		if (
				!unit.initialize(document) ||
				!unit.parse(document) ||
				!unit.analyze(document)
		) {
			unit.diagnostic();
			fail("Uncompleted test invocation");
		}

		if (unit.compile(document))
			fail("This should not compile");

		assertEquals(
				"org.jamplate.model.CompileException: #Elif outside if flow",
				unit.getEnvironment().getDiagnostic().iterator().next().getMessagePhrase(),
				"Wrong exception"
		);
	}

	@Test
	public void test0() {
		for (String[] s : new String[][]{
				//first, second, third, console
				{"true", "__PATH__", "__X__", "1 if -1"},
				{"false", "__PATH__", "__X__", "1 elifdef -1"},
				{"false", "__X__", "__X__", "1 elifndef -1"},
				{"false", "__X__", "__PATH__", "1 else -1"}
		}) {
			Document document = new PseudoDocument(
					"Test",
					/* 01 */ "#{ 1 ' ' }#",
					/* 02 */ "#if " + s[0],
					/* 03 */ "#{ 'if' }#",
					/* 04 */ "#elifdef " + s[1],
					/* 05 */ "#{ 'elifdef' }#",
					/* 06 */ "#elifndef " + s[2],
					/* 07 */ "#{ 'elifndef' }#",
					/* 08 */ "#else",
					/* 09 */ "#{ 'else' }#",
					/* 10 */ "#endif",
					/* 11 */ "#{ ' ' (-1) }#"
			);
			String expectedConsole = s[3];

			Unit unit = new UnitImpl(new GlucoseSpec(
					DebugSpec.INSTANCE,
					listener(event -> {
						if (event.getAction().equals(Action.POST_EXEC)) {
							String actualConsole = event.getMemory().getConsole().read();

							assertEquals(
									expectedConsole,
									actualConsole,
									"Unexpected console output"
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
}
