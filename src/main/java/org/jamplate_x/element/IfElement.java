///*
// *	Copyright 2021 Cufy
// *
// *	Licensed under the Apache License, Version 2.0 (the "License");
// *	you may not use this file except in compliance with the License.
// *	You may obtain a copy of the License at
// *
// *	    http://www.apache.org/licenses/LICENSE-2.0
// *
// *	Unless required by applicable law or agreed to in writing, software
// *	distributed under the License is distributed on an "AS IS" BASIS,
// *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// *	See the License for the specific language governing permissions and
// *	limitations under the License.
// */
//package org.cufyplate.element;
//
//import org.jamplate.model.element.Element;
//import org.jamplate.model.instruction.FallbackInstruction;
//import org.jamplate.model.instruction.Instruction;
//import org.jamplate.model.Reference;
//import org.jamplate.runtime.envirnoment.Environment;
//
//import java.util.Objects;
//
///**
// * @author LSafer
// * @version 0.0.0
// * @since 0.0.0 ~2021.01.31
// */
//public class IfElement implements Element {
//	protected Element branch;
//	protected Element condition;
//	protected Element scope;
//
//	@Override
//	public Instruction compile(Environment environment) {
//		Instruction condition = environment.compile(this.condition);
//		Instruction scope = environment.compile(this.scope);
//		Instruction branch = environment.compile(this.branch);
//		return new FallbackInstruction(
//				condition,
//				env -> {
//					Object ext = env.memory().pop();
//					return ext == null || ext.equals(false) || ext.equals(0) ?
//						   branch :
//						   scope;
//				}
//		);
//	}
//
//	@Override
//	public void put(Element element) {
//		Objects.requireNonNull(element, "element");
//		if (element instanceof ElseElement)
//			this.branch = element;
//	}
//
//	@Override
//	public Reference reference() {
//		return null;
//	}
//}
