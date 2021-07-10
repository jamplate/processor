package org.jamplate.glucose

import org.jamplate.unit.Unit
import org.jamplate.glucose.spec.GlucoseSpec
import org.jamplate.glucose.spec.tool.DebugSpec
import org.jamplate.impl.unit.UnitImpl
import org.jamplate.impl.document.PseudoDocument
import org.jamplate.model.Document
import org.junit.jupiter.api.Test

class GlucoseGroovyTest {
	@Test
	void example0() {
		Document document = new PseudoDocument(
				//name
				"main.jamplate",
				//content
				"#message Hello ' ' World"
		)

		Unit unit = new UnitImpl(new GlucoseSpec())

		if (
		!unit.initialize(document) ||
		!unit.parse(document) ||
		!unit.analyze(document) ||
		!unit.compile(document) ||
		!unit.execute(document)
		) {
			unit.diagnostic()
		}
	}

	@Test
	void example1() {
		Document document = new PseudoDocument(
				//name
				"main.jamplate",
				//content
				"#{ Hello ' ' World }#"
		)

		Unit unit = new UnitImpl(new GlucoseSpec(
				DebugSpec.INSTANCE
		))

		if (
		!unit.initialize(document) ||
		!unit.parse(document) ||
		!unit.analyze(document) ||
		!unit.compile(document) ||
		!unit.execute(document)
		) {
			unit.diagnostic()
		}
	}
}
