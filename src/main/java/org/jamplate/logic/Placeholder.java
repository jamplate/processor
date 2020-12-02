//package org.jamplate.logic;
//
//import org.cufy.preprocessor.AbstractLogic;
//import org.cufy.preprocessor.link.Logic;
//import org.cufy.preprocessor.invoke.Memory;
//
//import java.util.Objects;
//
//@Deprecated
//public class Placeholder extends AbstractLogic {
//	protected Logic logic;
//
//	public Placeholder() {
//	}
//
//	public Placeholder(Logic logic) {
//		Objects.requireNonNull(logic, "logic");
//	}
//
//	@Override
//	public String toString() {
//		return String.valueOf(this.logic);
//	}
//
//	@Override
//	public synchronized String evaluateString(Memory memory) {
//		Objects.requireNonNull(memory, "memory");
//		return this.logic == null ? null : this.logic.evaluateString(memory);
//	}
//
//	public synchronized Logic logic() {
//		return this.logic;
//	}
//
//	public synchronized void logic(Logic logic) {
//		Objects.requireNonNull(logic, "logic");
//		this.logic = logic;
//	}
//}
