package org.jamplate.glucose.instruction.flow;

import org.jamplate.glucose.instruction.memory.frame.IDumpFrame;
import org.jamplate.glucose.instruction.memory.frame.IPushFrame;
import org.jamplate.glucose.instruction.memory.frame.IGlueFrame;
import org.jamplate.glucose.instruction.memory.heap.IAlloc;
import org.jamplate.glucose.instruction.memory.resource.IPushConst;
import org.jamplate.glucose.instruction.memory.stack.IDup;
import org.jamplate.glucose.instruction.memory.stack.IPop;
import org.jamplate.glucose.instruction.memory.stack.ISwap;
import org.jamplate.glucose.instruction.operator.cast.ICastArray;
import org.jamplate.glucose.instruction.operator.cast.ICastBoolean;
import org.jamplate.glucose.instruction.operator.logic.ICompare;
import org.jamplate.glucose.instruction.operator.struct.IReverse;
import org.jamplate.glucose.instruction.operator.struct.ISplit;
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

public class IRepeatTest {
	@Test
	public void execute0() {
		Document document = new PseudoDocument("");
		Environment environment = new EnvironmentImpl();
		Compilation compilation = new CompilationImpl(environment, new Tree(document));
		Memory memory = new Memory();

		String keyV = "MyKey";
		String resultKeyV = "ResultKeyV";

		Instruction valueI = new IPushConst(array("[X, Y, Z, '', 'Z', 'Y', 'X']"));
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
				new IPushFrame(),
				//run the value
				valueI,
				//glue the answer
				IGlueFrame.INSTANCE,
				//cast the answer into array
				ICastArray.INSTANCE,
				//reverse the array
				new IReverse(),
				//dump the frame
				IDumpFrame.INSTANCE
		);

		//this block duplicates the top value
		//then compare it to null then convert
		//the comparison result to a boolean
		Block conditionI = new Block(
				//duplicate the value
				IDup.INSTANCE,
				//push a null to compare
				IPushConst.NULL,
				//compare to null
				ICompare.INSTANCE,
				//cast the comparison result to boolean
				ICastBoolean.INSTANCE
		);

		Block block = new Block(
				//push an anchoring null
				IPushConst.NULL,
				//lazily evaluate the parameter
				valueWrapI,
				//spread the evaluated value
				new ISplit(),
				//anchor check
				conditionI,
				Idle.INSTANCE,
				//iterate until the anchoring null
				new IRepeat(new Block(
						//push the allocation address
						new IPushConst(text(keyV)),
						//swap the address with the value
						ISwap.INSTANCE,
						//allocate the loop variable
						IAlloc.INSTANCE,
						//execute the body
						bodyI,
						//anchor check for the next round
						conditionI
				)),
				//pop the anchoring null
				IPop.INSTANCE
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
