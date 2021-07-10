package org.jamplate.glucose.instruction.operator.struct;

import org.jamplate.glucose.instruction.memory.resource.IPushConst;
import org.jamplate.impl.instruction.Block;
import org.jamplate.impl.environment.EnvironmentImpl;
import org.jamplate.memory.Memory;
import org.junit.jupiter.api.Test;

import static org.jamplate.glucose.internal.util.Values.obj;
import static org.jamplate.glucose.internal.util.Values.pair;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ISpreadTest {
	@Test
	public void test0() {
		Memory memory = new Memory();

		new Block(
				new IPushConst(obj(
						pair("Oniichan", "Bro"),
						pair("Oneechan", "Sister")
				)),
				ISpread.INSTANCE
		).exec(new EnvironmentImpl(), memory);

		assertEquals(
				"Bro",
				memory.get("Oniichan").eval(memory),
				"Not spread properly"
		);
		assertEquals(
				"Sister",
				memory.get("Oneechan").eval(memory),
				"Not spread properly"
		);
	}
}
