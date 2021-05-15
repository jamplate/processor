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
//package org.jamplate.model.element;
//
//import org.jamplate.runtime.envirnoment.Environment;
//import org.jamplate.model.instruction.SequentialInstruction;
//import org.jamplate.model.instruction.Instruction;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//import java.util.stream.Collectors;
//
///**
// * @author LSafer
// * @version 0.0.0
// * @since 0.0.0 ~2021.01.30
// */
//public abstract class AbstractBlockElement implements Element {
//	protected final List<Element> elements = new ArrayList<>();
//
//	protected AbstractBlockElement() {
//	}
//
//	@Override
//	public Instruction compile(Environment environment) {
//		Objects.requireNonNull(environment, "environment");
//		return new SequentialInstruction(
//				this.elements.stream()
//						.map(environment::compile)
//						.collect(Collectors.toList())
//		);
//	}
//}
