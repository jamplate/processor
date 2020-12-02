///*
// *	Copyright 2020 Cufy
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
//package org.cufy.preprocessor;
//
//import org.cufy.text.AbstractElement;
//import org.cufy.text.Element;
//import org.cufy.preprocessor.link.Logic;
//import org.cufy.preprocessor.link.Scope;
//import org.cufy.text.Memory;
//
//import java.io.IOException;
//import java.util.Objects;
//
///**
// * An abstract of the interface {@link Scope}.
// * <p>
// * Relations:
// * <ul>
// *     <li>Previous: {@link Scope}</li>
// *     <li>Next: {@link Scope}</li>
// * </ul>
// *
// * @param <V> the type of the value logic of this scope.
// * @author LSafer
// * @version 0.0.1
// * @since 0.0.1 ~2020.09.17
// */
//public abstract class AbstractScope<V extends Logic> extends AbstractElement<Scope, V> implements Scope<V> {
//	/**
//	 * Construct a new scope with its {@link #value} not initialized.
//	 *
//	 * @since 0.0.b ~2020.10.11
//	 */
//	public AbstractScope() {
//	}
//
//	/**
//	 * Construct a new scope with its {@link #value} set to the given {@code value}.
//	 *
//	 * @param value the value of the constructed scope.
//	 * @throws NullPointerException if the given {@code value} is null.
//	 * @since 0.0.b ~2020.10.11
//	 */
//	public AbstractScope(V value) {
//		super(value);
//	}
//
//	@Override
//	public Logic find(String address) {
//		Objects.requireNonNull(address, "address");
//		return this.previous == null ?
//			   null :
//			   this.previous.find(address);
//	}
//
//	@Override
//	public Appendable invoke(Appendable appendable, Memory memory) throws IOException {
//		Objects.requireNonNull(appendable, "appendable");
//		Objects.requireNonNull(memory, "memory");
//
//		if (this.next != null)
//			appendable = this.next.invoke(appendable, memory);
//
//		return appendable;
//	}
//
//	@Override
//	public boolean pushElement(Element element) {
//		Objects.requireNonNull(element, "element");
//		if (element instanceof Logic) {
//			this.setValue((V) element);
//			return true;
//		}
//
//		return super.pushElement(element);
//	}
//
//	@Override
//	public boolean setBranch(Element branch) {
//		Objects.requireNonNull(branch, "branch");
//		return branch instanceof Scope &&
//			   super.setBranch(branch);
//	}
//
//	@Override
//	public boolean setFork(Element fork) {
//		Objects.requireNonNull(fork, "fork");
//		return fork instanceof Scope &&
//			   super.setFork(fork);
//	}
//
//	@Override
//	public boolean setNext(Element next) {
//		Objects.requireNonNull(next, "next");
//		return next instanceof Scope &&
//			   super.setNext(next);
//	}
//
//	@Override
//	public boolean setPrevious(Element previous) {
//		return previous instanceof Scope &&
//			   super.setPrevious(previous);
//	}
//}
