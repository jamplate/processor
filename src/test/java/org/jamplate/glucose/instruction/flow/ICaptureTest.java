package org.jamplate.glucose.instruction.flow;

import org.jamplate.glucose.instruction.memory.console.IPrint;
import org.jamplate.glucose.instruction.memory.resource.IPushConst;
import org.jamplate.impl.instruction.Block;
import org.jamplate.impl.model.EnvironmentImpl;
import org.jamplate.memory.Memory;
import org.jamplate.model.Environment;
import org.junit.jupiter.api.Test;

import static org.jamplate.glucose.internal.util.Values.text;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ICaptureTest {
	@Test
	public void manualAssembly() {
		Environment environment = new EnvironmentImpl();
		Memory memory = new Memory();

		Block instruction = new Block(
				new IPushConst(text("FirstValue")),
				IPrint.INSTANCE,
				new ICapture(new Block(
						new IPushConst(text("SecondValue")),
						IPrint.INSTANCE
				)),
				new IPushConst(text("ThirdValue")),
				IPrint.INSTANCE
		);

		instruction.exec(environment, memory);

		String console = memory.getConsole().read();
		String stack = memory.pop().eval(memory);

		assertEquals(
				"FirstValueThirdValue",
				console,
				"Unexpected value printed to the console"
		);
		assertEquals(
				"SecondValue",
				stack,
				"Unexpected value at the stack"
		);
	}
}
