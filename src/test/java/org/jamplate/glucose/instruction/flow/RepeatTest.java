package org.jamplate.glucose.instruction.flow;

import org.jamplate.glucose.instruction.memory.frame.DumpFrame;
import org.jamplate.glucose.instruction.memory.frame.PushFrame;
import org.jamplate.glucose.instruction.memory.frame.GlueFrame;
import org.jamplate.glucose.instruction.memory.heap.Alloc;
import org.jamplate.glucose.instruction.memory.resource.PushConst;
import org.jamplate.glucose.instruction.memory.stack.Dup;
import org.jamplate.glucose.instruction.memory.stack.Pop;
import org.jamplate.glucose.instruction.memory.stack.Swap;
import org.jamplate.glucose.instruction.operator.cast.CastArray;
import org.jamplate.glucose.instruction.operator.cast.CastBoolean;
import org.jamplate.glucose.instruction.operator.logic.Compare;
import org.jamplate.glucose.instruction.operator.struct.Invert;
import org.jamplate.glucose.instruction.operator.struct.Split;
import org.jamplate.impl.instruction.Block;
import org.jamplate.impl.instruction.Idle;
import org.jamplate.impl.model.CompilationImpl;
import org.jamplate.impl.model.EnvironmentImpl;
import org.jamplate.impl.model.PseudoDocument;
import org.jamplate.memory.Memory;
import org.jamplate.memory.Value;
import org.jamplate.model.*;
import org.junit.jupiter.api.Test;

import static org.jamplate.glucose.internal.util.Values.array;
import static org.jamplate.glucose.internal.util.Values.text;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RepeatTest {
	@Test
	public void execute0() {
		Document document = new PseudoDocument("");
		Environment environment = new EnvironmentImpl();
		Compilation compilation = new CompilationImpl(environment, new Tree(document));
		Memory memory = new Memory();

		String keyV = "MyKey";
		String resultKeyV = "ResultKeyV";

		Instruction valueI = new PushConst(array("[X, Y, Z, '', 'Z', 'Y', 'X']"));
		Instruction bodyI = (env, mem) -> {
			Value next = mem.get(keyV);
			mem.compute(resultKeyV, prev -> m ->
					prev.eval(m) +
					", " +
					next.eval(m)
			);
		};

		Block valueWrapI = new Block(
				//push a new frame
				new PushFrame(),
				//run the value
				valueI,
				//glue the answer
				GlueFrame.INSTANCE,
				//cast the answer into array
				CastArray.INSTANCE,
				//reverse the array
				new Invert(),
				//dump the frame
				DumpFrame.INSTANCE
		);

		//this block duplicates the top value
		//then compare it to null then convert
		//the comparison result to a boolean
		Block conditionI = new Block(
				//duplicate the value
				Dup.INSTANCE,
				//push a null to compare
				PushConst.NULL,
				//compare to null
				Compare.INSTANCE,
				//cast the comparison result to boolean
				CastBoolean.INSTANCE
		);

		Block block = new Block(
				//push an anchoring null
				PushConst.NULL,
				//lazily evaluate the parameter
				valueWrapI,
				//spread the evaluated value
				new Split(),
				//anchor check
				conditionI,
				Idle.INSTANCE,
				//iterate until the anchoring null
				new Repeat(new Block(
						//push the allocation address
						new PushConst(text(keyV)),
						//swap the address with the value
						Swap.INSTANCE,
						//allocate the loop variable
						Alloc.INSTANCE,
						//execute the body
						bodyI,
						//anchor check for the next round
						conditionI
				)),
				//pop the anchoring null
				Pop.INSTANCE
		);

		block.exec(environment, memory);

		String result = memory.get(resultKeyV).eval(memory);

		assertEquals(
				", X, Y, Z, , Z, Y, X",
				result,
				"Unexpected execution results"
		);
	}
}
